package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsClient;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

@OnlyIn(Dist.CLIENT)
public class SimpleAbilityBarHandler {

    SimpleAbilityBarHandler(){

    }

    private static boolean getHideAbilityBars() {
        return KenjisCombatFormsClient.HIDE_ABILITY_BARS.get();
    }

    public static boolean getHideAbilityBarsFirstPerson(Player player){
       if(player.level().isClientSide) {
           if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
               if (KenjisCombatFormsClient.HIDE_ABILITY_BARS_FIRST_PERSON.get()) {
                   return false;
               } else
                   return true;
           }
       }
           return true;
    }

    public static boolean getCanUseAbilitiesWithoutForms(Player player){
        if(!(player.getMainHandItem().getItem() instanceof BaseFistClass)){
            return KenjisCombatFormsCommon.CAN_USE_ABILITIES_NO_FORM.get();
        }
        return true;
    }


    public void drawAbility1Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownWidth) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                   if (data.isHandCombat || !getHideAbilityBars()) {
                        event.getGuiGraphics().blit(abilityResource, iconX - 10, iconY - 5, iconU, iconV + 7, iconWidth, iconHeight);

                       event.getGuiGraphics().blit(abilityResource, iconX + iconWidth - 10, iconY + iconHeight - 5, iconU + 106, iconV + 20, -cooldownWidth, -iconHeight);
                    }
                }
            }
        }
    }
    public static void drawAbility4Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(abilityResource, iconX + 106 - 30, iconY - 10, iconU + 121, iconV, iconWidth, iconHeight);

                        event.getGuiGraphics().blit(abilityResource, iconX + iconWidth + 106 - 30, iconY + iconHeight - 10, iconU + 118, iconV + iconHeight, -iconWidth, -cooldownHeight);
                    }
                }
        }
    }


    public static void drawAbility5Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
                if (getHideAbilityBarsFirstPerson(player)) {

                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(abilityResource, iconX + 106 - 20, iconY - 10, iconU + 138, iconV, iconWidth, iconHeight);
                        event.getGuiGraphics().blit(abilityResource, iconX + iconWidth + 106 - 20, iconY + iconHeight - 10, iconU + 135, iconV + iconHeight, -iconWidth, -cooldownHeight);
                    }
                }
        }
    }


    public static void drawAbility2Icon(RenderGuiOverlayEvent event, boolean areFinalsActive, int ability2Cooldown, int ability5Cooldown, ResourceLocation ability2BgResource, ResourceLocation ability2Resource, ResourceLocation ability2OverlayResource,  ResourceLocation ability5BgResource, ResourceLocation ability5Resource, ResourceLocation ability5OverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownWidth, int ability5CooldownHeight) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {

                if (getHideAbilityBarsFirstPerson(player)) {

                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(ability2Resource, iconX - 10, iconY - 13, iconU, iconV + 23, iconWidth, iconHeight);

                        event.getGuiGraphics().blit(ability2Resource, iconX + iconWidth - 10, iconY + iconHeight - 13, iconU + iconWidth, iconV + 36, -cooldownWidth, -iconHeight);
                    }
                }
            }
        }
    }
    public static void drawAbility3Icon(RenderGuiOverlayEvent event, int ability3Cooldown, ResourceLocation ability3BgResource, ResourceLocation ability3Resource, ResourceLocation ability3OverlayResource,  int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownWidth) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {

                if (getHideAbilityBarsFirstPerson(player)) {

                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(ability3Resource, iconX - 10, iconY - 25, iconU, iconV + 40, iconWidth, iconHeight + 3);
                        event.getGuiGraphics().blit(ability3Resource, iconX + iconWidth - 10, iconY + iconHeight - 25, iconU + iconWidth, iconV + 60, -cooldownWidth, -iconHeight - 3);
                    }
                }
            }
        }
    }
}
