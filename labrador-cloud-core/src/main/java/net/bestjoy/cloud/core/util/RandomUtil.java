package net.bestjoy.cloud.core.util;

import java.util.Random;

/***
 * 随机工具
 * @author ray
 */
public class RandomUtil {

    private static final Random random = new Random();

    private static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public RandomUtil() {
    }

    public static Random getRandom() {
        return random;
    }

    public static String randomChar(int len) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; ++i) {
            sb.append(chars[random.nextInt(chars.length)]);
        }

        return sb.toString();
    }

    public static String randomChar() {
        return randomChar(8);
    }
}
