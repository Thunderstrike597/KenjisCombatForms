package net.kenji.kenjiscombatforms.api.managers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeybindManager {
    private static final KeybindManager INSTANCE = new KeybindManager();
    private final Map<UUID, InputConstants.Key> originalDropKeys = new HashMap<>();

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    public void storeOriginalKey(Player player, InputConstants.Key key) {
        originalDropKeys.put(player.getUUID(), key);
    }

    public InputConstants.Key getOriginalKey(Player player) {
        return originalDropKeys.get(player.getUUID());
    }
}
