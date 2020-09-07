package net.bestjoy.cloud.security.encrypt.util;


import net.bestjoy.cloud.core.error.SysException;

import java.security.MessageDigest;

import static net.bestjoy.cloud.core.error.Errors.Sys.ENCRYPT_ERROR;


/***
 * md5加密
 * @author ray
 */
public class MD5Util {
    public static String md5Hex(String password, String salt) {
        return md5Hex(password + salt);
    }

    public static String md5Hex(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(str.getBytes());
            StringBuffer md5StrBuff = new StringBuffer();

            for (int i = 0; i < bs.length; ++i) {
                if (Integer.toHexString(255 & bs[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & bs[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & bs[i]));
                }
            }

            return md5StrBuff.toString();
        } catch (Exception var5) {
            throw new SysException(ENCRYPT_ERROR);
        }
    }
}
