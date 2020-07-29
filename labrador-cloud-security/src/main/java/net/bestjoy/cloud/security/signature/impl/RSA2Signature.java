package net.bestjoy.cloud.security.signature.impl;

import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.error.bean.BusinessException;
import net.bestjoy.cloud.error.util.BusinessAssert;
import net.bestjoy.cloud.security.signature.Signature;
import net.bestjoy.cloud.security.signature.SignTypeEnum;

import java.util.Map;

import static net.bestjoy.cloud.error.bean.Errors.Sys.SIGN_FAILED_ERROR;
import static net.bestjoy.cloud.error.bean.Errors.Sys.SIGN_VERIFY_FAILED_ERROR;

/***
 * RSA2签名方式
 * @author ray
 */
@Slf4j
public class RSA2Signature implements Signature {

    /**
     * 签名方式
     */
    protected SignTypeEnum signType;
    /***
     * 编码
     */
    protected String charset = "UTF-8";

    /***
     * 应用公钥（第三方调用方提供）
     */
    protected String appPublicKey;

    /***
     * 加签私钥（服务方提供）
     */
    protected String privateKey;

    public RSA2Signature() {
        this.signType = SignTypeEnum.RSA2;
    }

    public RSA2Signature(String appPublicKey, String privateKey) {
        this();
        this.appPublicKey = appPublicKey;
        this.privateKey = privateKey;
    }

    public RSA2Signature(String appPublicKey, String privateKey, String charset) {
        this(appPublicKey, privateKey);
        this.charset = charset;
    }


    @Override
    public SignTypeEnum getSignType() {
        return this.signType;
    }

    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public String sign(Map<String, String> signatureParams) {
        BusinessAssert.notBlank(this.appPublicKey, "应用公钥未初始化");
        BusinessAssert.notBlank(this.privateKey, "私钥未初始化");

        String signContent = AlipaySignature.getSignCheckContentV2(signatureParams);

        if (log.isDebugEnabled()) {
            log.info("rsa2 sign content:{}", signContent);
        }

        try {
            return AlipaySignature.rsaSign(signContent, privateKey, charset, this.signType.name());
        } catch (Exception e) {
            log.warn("rsa2 sign error:{}", e.getMessage());
            throw new BusinessException(SIGN_FAILED_ERROR);
        }
    }

    @Override
    public boolean verify(Map<String, String> signatureParams) {
        BusinessAssert.notBlank(this.appPublicKey, "应用公钥未初始化");
        BusinessAssert.notBlank(this.privateKey, "私钥未初始化");

        try {
            return AlipaySignature.rsaCheckV2(signatureParams, appPublicKey, charset, this.signType.name());
        } catch (Exception e) {
            log.warn("rsa2 sign error:{}", e.getMessage());
            throw new BusinessException(SIGN_VERIFY_FAILED_ERROR);
        }
    }


    public static void main(String[] args) throws Exception {
        String appPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0i89hzpwn0oWYsMD6C59SN4XwqN0+TErS378h/u6uhnpcUP4b6TWZjjVfXgd8n9uLkcMQgU/pcVXucEMHzET7Z2valHsycdrYAxRDU6PWAr0tEL9rRYxGtvWr2F0iizvV1IxU9N6VzJqYtpFPn629jeikwy2PuUd7EG3WJtCUPsN0P0awzCPaUnOJBPS/yb4RcIb6z8j3aSQ6p2INi4bBcpU3kcMk2J7rbJCNJwAPFm9oWYKOC6wBLQ8//lJj7DclYEYLlPvyGYbF3AY9hLyDt+wF3F4L2QynCo9nVsWgkdwsoTpPeAPMr18/FSGe4POPnopPNrPlTyzMd9GNfiCjQIDAQAB";

        String appPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDSLz2HOnCfShZiwwPoLn1I3hfCo3T5MStLfvyH+7q6GelxQ/hvpNZmONV9eB3yf24uRwxCBT+lxVe5wQwfMRPtna9qUezJx2tgDFENTo9YCvS0Qv2tFjEa29avYXSKLO9XUjFT03pXMmpi2kU+frb2N6KTDLY+5R3sQbdYm0JQ+w3Q/RrDMI9pSc4kE9L/JvhFwhvrPyPdpJDqnYg2LhsFylTeRwyTYnutskI0nAA8Wb2hZgo4LrAEtDz/+UmPsNyVgRguU+/IZhsXcBj2EvIO37AXcXgvZDKcKj2dWxaCR3CyhOk94A8yvXz8VIZ7g84+eik82s+VPLMx30Y1+IKNAgMBAAECggEAftaNPdbxcd7mxXDEIdA0QM3t7sJva5XVd7jJMlKh636JlpWbDdyemY+Anjfncpid6AVuC0VZlHMTroAmDYizavh2jCWmTFownQSxwOMAs/Pgmgu3REWZGolB9BJOnrfhM4PhSqHbOVWuR7tKgB090jXewPix3iCrvxAlUa5YBb9zKAvDyMfDsmTa7JJeyinBA5x/E/a3JvBPq6Q+8cBYSQpzicByFx/mebdWbBkqM35kNpqdGvA4/F6HkLFBwMswJoEnwTgXHc5y+Ey56NHClbhYF5KZ2m/cUBktyXS1l4PNhOqXPjUIqiVGwxPrCbzHD9wxD4HCOM7Xp1XR1ltwYQKBgQDrIDIStqlI4yv6IPyUlWrLI74oWsjOb9jCXR1ahhaPkiVj4zUH3jKPTnyJE99kSyjxSaJ4x4kNSoTBORNV1qX2h9VCHxQW0JCJFgnuFMKMKOwPKnfDZzISKwJM/0HgW++RfewIMkHp4+8GFMCqQLlX8vwCALgv4sYBxx7ROTN/mQKBgQDk2DHUeaPTIQY9EoHt3cJ/z1bVYLgG+oQJdQoytf5TZKk6qvlK6PibmRlU7gRi2J8vE0SYkdaeATDWN02BAh94W5WlJbSHAFsDJ6MQYE9E5Vf7ARbGCer4iU20O8VJ+0aj/g/Eb6SZofV1VFG87bSPDnAezxyp+PkyxZ3P/WpDFQKBgF0gGlUJHdFtkIx4JjIfX/XnSTiohGInMH4C7U5eT2873i1K30Unf3T9DBwqz87kaenvVTdrKGAoRJ5XIc5cvsz/DbsrngRPRP/Wx8YRWOC0kJKNKnQtWkqR2E5wx0G6iA9jBWpVpX+itJf+/jQwgHhSF/gfGtR0iCJKIG2Tr075AoGAGexjuK4O4Nvc2Q4/qbJ1VgUm0QUj06aNToTzosr7BHditEKp+tJpVzcAp55O4OOpJKwKGT5sEXoRDjM1lGepKPk2+n5K9QAVT0YuMqy2Thes38I/0i9Nbtoe80vSDY9pcSpWnuoGu2Cdba0flUdYLNx11wefN6H8mMPKHMWYRAECgYA4FtlxgGsdYDWRoaNm4dKT2IEJHY1ItR3uAP8Uzll3NW20PNUcFbKoXc97mERuJxHvdZwkUzaHzBLFhLhAYx9S/bFkS5nuLyeF/uJG0e8ysaYRxZ/vFmQIjtfLhYqeBa/jr64/hoUZj4WY6USoR0IZ/0KiAG0CdZCXwLpsSz7cug==";
        String signContent = "appId=A020200414142710001&bizContent={\"deviceId\":\"D20200714224310001\",\"sendTime\":\"2020-06-11 23:12:13\"}&service=boundary.openapi.fence.device.geo.data&signType=RSA2&version=1.0.0";

        String sign = AlipaySignature.rsa256Sign(signContent, appPrivateKey, "UTF-8");
        System.out.println(sign);
    }
}
