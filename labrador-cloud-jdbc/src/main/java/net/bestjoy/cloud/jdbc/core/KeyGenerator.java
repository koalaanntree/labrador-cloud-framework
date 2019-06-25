package net.bestjoy.cloud.jdbc.core;

import net.bestjoy.cloud.core.util.UniqueId;

public final class KeyGenerator {
    public static Long defualtKeyGenerator() {
        return new UniqueId().newUniqueId();
    }
}
