package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.skill.Skill;

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
    ItemStack getFormItem(UUID playerId);
    Skill getFormSkill(Player player);

}