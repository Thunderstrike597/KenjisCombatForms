package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class VoidAbility2CooldownGui {


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        int abilityCooldown = ClientVoidData.getCooldown2();
        int ability5Cooldown = ClientVoidData.getGrabCooldown();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability2_gui.png");
        ResourceLocation abilityBgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability2_bg-gui.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability2_gui_overlay.png");

        ResourceLocation ability5Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/void_grab_gui.png");
        ResourceLocation ability5BgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/void_grab_bg-gui.png");
        ResourceLocation ability5OverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/second_abilities/void_grab_gui_overlay.png");

        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        boolean isEnderActive = ClientVoidData.getIsEnderActive();
        boolean isWitherActive = ClientWitherData.getIsWitherActive();

        int imageWidth = 100;
        int imageHeight = 50;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int abilityX = screenWidth - imageWidth;
        int abilityY = screenHeight - imageHeight;


        if (player != null) {

            EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
            EnderPlayerDataSets.TeleportPlayerData tpData = dataSets.getOrCreateTeleportPlayerData(player);
            EnderPlayerDataSets.VoidRiftPlayerData vData = dataSets.getOrCreateVoidRiftPlayerData(player);
            EnderPlayerDataSets.VoidGrabPlayerData gData = dataSets.getOrCreateVoidGrabPlayerData(player);


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 5;
            int simpleAbilityWidth2 = 5;
            int simpleAbilityHeight2 = 62;

            int abilitySimpleMaxCooldown = KenjisCombatFormsCommon.ABILITY2_COOLDOWN.get();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);


            int voidAbilityFullHeight2 = 62;
            int voidAbility5MaxCooldown = KenjisCombatFormsCommon.ABILITY5_COOLDOWN.get();
            float voidAbilityElapsedCooldown2 = voidAbility5MaxCooldown - ability5Cooldown;
            float voidAbilityCooldownProgress2 = (float) voidAbilityElapsedCooldown2 / voidAbility5MaxCooldown;
            int simpleBarHeight2 = (int) (voidAbilityFullHeight2 * voidAbilityCooldownProgress2);


            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility2 == AbilityManager.AbilityOption2.VOID_ABILITY2 ||
                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.VOID_ABILITY2) {

                if (!areFinalsActive) {
                    SimpleAbilityBarHandler.drawAbility2Icon(event, false, abilityCooldown, abilityCooldown, emptyResource, simpleAbilityResource, emptyResource, emptyResource, emptyResource, emptyResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth, simpleAbilityHeight);
                }
            }
            if (isEnderActive) {
                SimpleAbilityBarHandler.drawAbility5Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth2, simpleAbilityHeight2, simpleBarHeight2);
            }
        }
    }
}
