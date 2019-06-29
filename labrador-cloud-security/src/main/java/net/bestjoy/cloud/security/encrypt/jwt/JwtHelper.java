package net.bestjoy.cloud.security.encrypt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import net.bestjoy.cloud.core.util.Dates;
import net.bestjoy.cloud.error.bean.Errors;
import net.bestjoy.cloud.error.bean.SysException;
import net.bestjoy.cloud.security.config.SecurityProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/***
 * jwt工具
 * @author ray
 */
public class JwtHelper {

    private SecurityProperties securityProperties;

    public JwtHelper() {
    }

    public JwtHelper(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * 加密串配置
     *
     * @return
     */
    private SecretKey generateKey() {
        //todo
        return null;
    }

    /***
     * 构建token
     * @param subject   userId 不能为空
     * @param claimsMap   扩展
     * @return
     */
    public String buildJwtToken(String subject, Map<String, Object> claimsMap) {

        JwtBuilder builder = Jwts.builder().setSubject(subject);
        //签发者
        builder.setIssuer(securityProperties.getJwtIssuer());

        if (StringUtils.isEmpty(securityProperties.getJwtAppName())) {
            //接受者
            builder.setAudience(securityProperties.getJwtAppName());
        }

        if (!CollectionUtils.isEmpty(claimsMap)) {
            builder.addClaims(claimsMap);
        }

        Date now = new Date();
        //签发时间
        builder.setIssuedAt(now);
        //过期时间
        builder.setExpiration(Dates.addMinutes(new Date(), securityProperties.getTokenExpireInterval()));
        //todo  加密
        return builder.compact();
    }

    /***
     * 解析jwtToken
     * @param jwtToken
     * @return
     */
    public Claims resovleClaims(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            throw new SysException(Errors.Biz.ARGS_NOT_EMPTY_ERROR);
        }

        //todo 解密
        return Jwts.parser().parseClaimsJwt(jwtToken).getBody();
    }

}
