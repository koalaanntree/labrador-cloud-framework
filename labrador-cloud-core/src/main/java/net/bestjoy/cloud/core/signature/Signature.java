package net.bestjoy.cloud.core.signature;


import java.util.Map;

/***
 * 签名工具
 * @author ray
 */
public interface Signature {
    /***
     * 获取签名方式
     * @return
     */
    SignTypeEnum getSignType();

    /***
     * 获取编码
     * @return
     */
    String getCharset();

    /***
     * 加签
     * @param signatureParams 签名参数列表
     * @return 签名串
     */
    String sign(Map<String, String> signatureParams);


    /***
     * 验签
     * @param signatureParams 签名参数列表
     * @return 验签结果
     */
    boolean verify(Map<String, String> signatureParams);
}
