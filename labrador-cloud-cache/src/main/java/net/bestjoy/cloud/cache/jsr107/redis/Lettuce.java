package net.bestjoy.cloud.cache.jsr107.redis;

import io.lettuce.core.SetArgs;
import io.lettuce.core.ZAddArgs;

/**
 * Lettuce一些常量
 */
public interface Lettuce {
    SetArgs SET_NX = SetArgs.Builder.nx();
    SetArgs SET_XX = SetArgs.Builder.xx();
    ZAddArgs Z_ADD_NX = ZAddArgs.Builder.nx();
    ZAddArgs Z_ADD_XX = ZAddArgs.Builder.xx();

    static boolean ok(String reply) {
        return "OK".equals(reply);
    }
}
