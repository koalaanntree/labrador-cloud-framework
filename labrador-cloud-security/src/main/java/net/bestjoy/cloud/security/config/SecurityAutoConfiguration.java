package net.bestjoy.cloud.security.config;

import net.bestjoy.cloud.security.constant.SecurityConstant;
import net.bestjoy.cloud.security.filter.AppFilterInvocationSecurityMetadataSource;
import net.bestjoy.cloud.security.filter.JwtAuthenticationTokenFilter;
import net.bestjoy.cloud.security.handler.AppAccessDecisionManager;
import net.bestjoy.cloud.security.handler.AppAuthenticationProvider;
import net.bestjoy.cloud.security.handler.JwtAuthenticationEntryPoint;
import net.bestjoy.cloud.security.persistent.config.SecurityMapperConfig;
import net.bestjoy.cloud.security.service.impl.AppUserDetailsServiceImpl;
import net.bestjoy.cloud.security.util.JwtHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static net.bestjoy.cloud.security.constant.SecurityConstant.PRINCIPAL_ANONYMOUS;

/***
 * security统一配置
 * @author ray
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
@ComponentScan(basePackages = {"net.bestjoy.cloud.security"})
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    private JwtHelper jwtHelper;
    @Resource
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private AppUserDetailsServiceImpl appUserDetailsService;
    @Resource
    private AppAccessDecisionManager accessDecisionManager;
    @Autowired
    private SecurityProperties securityProperties;

    @Resource
    private Docket api;


    @Bean
    public void securitySwagger() {
        if (api != null) {
            api.securitySchemes(securitySchemes());
            api.securityContexts(securityContexts());
        }
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }


    private List<ApiKey> securitySchemes() {
        return new ArrayList<ApiKey>() {
            {
                add(new ApiKey("Authorization", SecurityConstant.AUTH_HEADER, "header"));
            }
        };
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /***
         * webIgnore 绕过了security的授权
         */
        web.ignoring().antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/v2/api-doc**")
                .antMatchers("/swagger-resources/**");
        //过滤security url
        for (String url : SecurityConstant.SECURITY_IGNORED_URLS) {
            web.ignoring().antMatchers(url);
        }

        //过滤外部自定义url
        if (!CollectionUtils.isEmpty(securityProperties.getIgnoredUrls())) {
            securityProperties.getIgnoredUrls().forEach(url -> {
                web.ignoring().antMatchers(url);
            });
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider appAuthenticationProvider() {
        return new AppAuthenticationProvider(appUserDetailsService, passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().authenticationFilter(
                new AnonymousAuthenticationFilter("anonymousAuth", PRINCIPAL_ANONYMOUS, new ArrayList<GrantedAuthority>() {{
                    add(new SimpleGrantedAuthority(SecurityConstant.ROLE_ANONYMOUS));
                }}));
        http
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager)
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(new AppFilterInvocationSecurityMetadataSource(o.getSecurityMetadataSource()));
                        return o;
                    }
                })
                //这里表示"/any"和"/ignore"不需要权限校验
                /***
                 * 这里并不是绕过security的授权，而是Anonymous访问;需使用ignoreAuth注解过滤，添加自定义的匿名用户
                 * 如果是静态资源不需要security授权，请用WebSecurity.ignoring()处理
                 */
//                .antMatchers("/ignore/**", "/**/login", "/**/registerAndLogin").permitAll()
                .anyRequest().authenticated().and()
                .authenticationProvider(appAuthenticationProvider())
                // custom token authorize exception handler
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint).and()
                // since we use jwt, session is not necessary
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // since we use jwt, csrf is not necessary
                .csrf().disable();
        http.addFilterBefore(new JwtAuthenticationTokenFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class);
        http.logout()
                .addLogoutHandler(securityContextLogoutHandler());
        // disable cache
        http.headers().cacheControl();
    }
}
