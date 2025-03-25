package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.api.PowerControl;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsClient;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;

@OnlyIn(Dist.CLIENT)
public class SimpleAbilityHandler {

    SimpleAbilityHandler(){

    }

    public int getSelectionModeX(){
        return 49;
    }
    public int getSelectionModeY(){
        return 79;
    }

    int getIconSize(int value){
        if(isSelectionMode()){
            return 42;
        }
        return value;
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

    boolean isSelectionMode(){
        return KenjisCombatFormsCommon.ABILITY_SELECTION_MODE.get();
    }

    int abilityIndex(Player player){
        PowerControl.controlRelatedEvents.PlayerData playerData = PowerControl.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            return playerData.currentAbilityIndex;
    }




    public void drawAbility1Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconSize, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            if(isSelectionMode()){
                if(abilityIndex(player) != 1)
                    return;
            }


            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (iconSize - cooldownHeight),
                                iconU, iconV + (iconSize - cooldownHeight),
                                iconSize, cooldownHeight, // ✅ Uses positive height
                                iconSize, iconSize);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );
                    }
                }
            }
        }
    }
    public void drawAbility4Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconSize, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            if(isSelectionMode()){
                if(abilityIndex(player) != 1)
                    return;
            }
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (iconSize - cooldownHeight),
                                iconU, iconV + (iconSize - cooldownHeight),
                                iconSize, cooldownHeight,
                                iconSize, iconSize);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );
                    }
                }
            }
        }
    }


    public void drawAbility5Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconSize, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            if(isSelectionMode()){
                if(abilityIndex(player) != 2)
                    return;
            }
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (iconSize - cooldownHeight),
                                iconU, iconV + (iconSize - cooldownHeight),
                                iconSize, cooldownHeight,
                                iconSize, iconSize);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );
                    }
                }
            }
        }
    }


    public void drawAbility2Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconSize, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            if(isSelectionMode()){
                if(abilityIndex(player) != 2)
                    return;
            }

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (iconSize - cooldownHeight),
                                iconU, iconV + (iconSize - cooldownHeight),
                                iconSize, cooldownHeight,
                                iconSize, iconSize);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );
                    }
                }
            }
        }
    }
    public void drawAbility3Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconSize, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {
            if(isSelectionMode()){
                if(abilityIndex(player) != 3)
                    return;
            }

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {


                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (iconSize - cooldownHeight),
                                iconU, iconV + (iconSize - cooldownHeight),
                                iconSize, cooldownHeight,
                                iconSize, iconSize);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                iconSize, iconSize,
                                iconSize, iconSize
                        );
                    }
                }
            }
        }
    }
}
