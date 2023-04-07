package me.gepronix.decaliumcustomitems.utils;

import me.gepronix.decaliumcustomitems.DecaliumCustomItems;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class DataType {
    private static final KeyFactory DecaliumKeyFactory = new KeyFactory(DecaliumCustomItems.get());

    public static NamespacedKey key(@NotNull String name) {
        return DecaliumKeyFactory.key(name);
    }
}
