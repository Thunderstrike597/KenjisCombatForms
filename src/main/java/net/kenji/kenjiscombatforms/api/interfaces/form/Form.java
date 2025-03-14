package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface Form {
    String getName();
    AbstractFormData getFormData(UUID player);
    void updatePlayerData(UUID playerUUID, AbstractFormData formData);
    void syncDataToClient(Player player);
}