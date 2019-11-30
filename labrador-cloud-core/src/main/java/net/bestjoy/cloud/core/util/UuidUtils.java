package net.bestjoy.cloud.core.util;

import java.util.UUID;

/***
 * @author ray
 */
public class UuidUtils {

    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
