package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

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


    public void drawAbility1Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                32, 32,
                                32, 32
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (32 - cooldownHeight),  // ✅ Moves drawing position up
                                iconU, iconV + (32 - cooldownHeight),  // ✅ Moves texture source up
                                iconWidth, cooldownHeight, // ✅ Uses positive height
                                32, 32);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                32, 32,
                                32, 32
                        );
                    }
                }
            }
        }
    }
    public void drawAbility4Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                32, 32,
                                32, 32
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (32 - cooldownHeight),  // ✅ Moves drawing position up
                                iconU, iconV + (32 - cooldownHeight),  // ✅ Moves texture source up
                                iconWidth, cooldownHeight, // ✅ Uses positive height
                                32, 32);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                32, 32,
                                32, 32
                        );
                    }
                }
            }
        }
    }


    public void drawAbility5Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                38, 38,
                                38, 38
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (38 - cooldownHeight),  // ✅ Moves drawing position up
                                iconU, iconV + (38 - cooldownHeight),  // ✅ Moves texture source up
                                iconWidth, cooldownHeight, // ✅ Uses positive height
                                38, 38);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                38, 38,
                                38, 38
                        );
                    }
                }
            }
        }
    }


    public void drawAbility2Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                38, 38,
                                38, 38
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (38 - cooldownHeight),  // ✅ Moves drawing position up
                                iconU, iconV + (38 - cooldownHeight),  // ✅ Moves texture source up
                                iconWidth, cooldownHeight, // ✅ Uses positive height
                                38, 38);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                38, 38,
                                38, 38
                        );
                    }
                }
            }
        }
    }
    public void drawAbility3Icon(RenderGuiOverlayEvent event, ResourceLocation abilityResource, ResourceLocation abilityBackgroundResource, ResourceLocation abilityOverlayResource, int iconX, int iconY, int iconU, int iconV, int iconWidth, int iconHeight, int cooldownHeight, int MAX_COOLDOWN, int abilityCooldown) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;
        if (player != null && player.level().isClientSide) {

            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (getCanUseAbilitiesWithoutForms(player)) {
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {

                        // ✅ Draw the FULL Background Texture (without clipping)
                        event.getGuiGraphics().blit(abilityBackgroundResource,
                                iconX, iconY,
                                iconU, iconV,
                                44, 44,
                                44, 44
                        );

                        event.getGuiGraphics().blit(abilityResource,
                                iconX, iconY + (44 - cooldownHeight),  // ✅ Moves drawing position up
                                iconU, iconV + (44 - cooldownHeight),  // ✅ Moves texture source up
                                iconWidth, cooldownHeight, // ✅ Uses positive height
                                44, 44);
                    }
                    if(abilityCooldown == 0){

                        event.getGuiGraphics().blit(abilityOverlayResource,
                                iconX, iconY,
                                iconU, iconV,
                                44, 44,
                                44, 44
                        );
                    }
                }
            }
        }
    }
}
