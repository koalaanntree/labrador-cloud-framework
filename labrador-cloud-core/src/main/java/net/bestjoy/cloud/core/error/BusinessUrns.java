package net.bestjoy.cloud.core.error;

import java.util.regex.Pattern;

/**
 * 业务URN，不能是UUID及纯数字
 *
 * @author : joy lee
 */
public final class BusinessUrns {
    private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{4}-?[0-9a-fA-F]{12}");
    private static final Pattern NUMBER = Pattern.compile("[0-9]+");

    public static boolean isUUID(String string) {
        return UUID.matcher(string).matches();
    }

    public static boolean isNumber(String string) {
        return NUMBER.matcher(string).matches();
    }

    public static boolean isUrn(String urn) {
        //目前只判断urn为非uuid和纯数字
        return !isNumber(urn) && !isUUID(urn);
    }
}
