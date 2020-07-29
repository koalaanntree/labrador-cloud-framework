package net.bestjoy.cloud.security.signature.impl;

import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.security.signature.SignTypeEnum;

/***
 * rsa 签名方式
 * @author ray
 */
@Slf4j
public class RSASignature extends RSA2Signature {

    public RSASignature() {
        this.signType = SignTypeEnum.RSA;
    }

    public RSASignature(String appPublicKey, String privateKey) {
        super(appPublicKey, privateKey);
    }

    public RSASignature(String appPublicKey, String privateKey, String charset) {
        super(appPublicKey, privateKey, charset);
    }
}
