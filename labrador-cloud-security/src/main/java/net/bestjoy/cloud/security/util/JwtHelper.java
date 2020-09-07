package net.bestjoy.cloud.security.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.core.AppUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Set;

/***
 * jwt工具
 * @author ray
 */
@Slf4j
@Component
public class JwtHelper {

    private static final String AUTHORITIES_KEY = "authorities";

    private static final String AUTHORITY_DETAIL = "details";

    /**
     * secret key
     */
    private String secretKey;

    /**
     * Token validity time(ms)
     */
    private long tokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        this.secretKey = "SecretKey012345678901234567890123456789012345678901234567890123456789";
        this.tokenValidityInMilliseconds = 1000 * 60 * 30L;//30分钟过期
    }

    /**
     * Create token
     *
     * @param authentication auth info
     * @return token
     */
    public String createToken(Authentication authentication) {
        /**
         * Current time
         */
        long now = (new Date()).getTime();
        /**
         * Validity date
         */
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        Set<String> authoritySet = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        /**
         * create token
         */
        return Jwts.builder()
                .setSubject((String) authentication.getPrincipal())
                .claim(AUTHORITIES_KEY, StringUtils.arrayToDelimitedString(authoritySet.toArray(), ","))
                .claim(AUTHORITY_DETAIL, JSONObject.toJSONString(authentication.getDetails()))
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Get auth Info
     *
     * @param token token
     * @return auth info
     */
    public Authentication getAuthentication(String token) {
        /**
         *  parse the payload of token
         */
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        authentication.setDetails(JSONObject.parseObject((String) claims.get(AUTHORITY_DETAIL), AppUserDetails.class));
        return authentication;
    }

    /**
     * validate token
     *
     * @param token token
     * @return whether valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

}
