package net.bestjoy.cloud.security.encrypt.jwt;

import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtHelper {

    @NonNull
    private SecurityProperties securityProperties;





    /***
     * 构建token
     * @param subject   userId 不能为空
     * @param claimsMap   扩展
     * @return
     */
    public String buildJwtToken(
            String subject, Map<String, Object> claimsMap, Integer expireInterval) {

        JwtBuilder builder = Jwts.builder().setSubject(subject);
        //签发者
        builder.setIssuer(securityProperties.getJwtIssuer());

        if (!StringUtils.isEmpty(securityProperties.getJwtAppName())) {
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
        if (expireInterval > 0) {
            builder.setExpiration(Dates.addMinutes(new Date(), expireInterval));
        }
        //todo 加密
//        if (!StringUtils.isEmpty(securityProperties.getJwtSignType())
//                && !StringUtils.isEmpty(securityProperties.getJwtSignKey())) {
//            builder.signWith(SignatureAlgorithm.valueOf(securityProperties.getJwtSignType()), securityProperties.getJwtSignKey());
//        }
        return builder.compact();
    }

    /***
     * 是否已过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    /***
     * 解析jwtToken
     * @param jwtToken
     * @return
     */
    public Claims parseClaims(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            throw new SysException(Errors.Biz.ARGS_NOT_EMPTY_ERROR);
        }

        //todo 解密
        return Jwts.parser().parseClaimsJwt(jwtToken).getBody();
    }

}
