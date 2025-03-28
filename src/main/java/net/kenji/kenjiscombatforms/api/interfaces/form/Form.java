package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

public interface Form {
    String getName();

    AbstractFormData getFormData(UUID player);
    List<ResourceLocation> getLevelResources();

    int getGUIDrawPosY();
    int getGUIDrawPosX();
    int getGUISwapPos();
    void updatePlayerData(UUID playerUUID, AbstractFormData formData);
    void syncDataToClient(Player player);

    void setCurrentForm(Player player, int slot);
}