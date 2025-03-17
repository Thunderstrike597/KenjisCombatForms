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
public class VoidAbility1CooldownGui {

    private static final SimpleAbilityBarHandler simpleAbilityBarHandler = new SimpleAbilityBarHandler();


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientVoidData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        //if (pPlayer != null) {
        int ability1Cooldown = ClientVoidData.getCooldown();
        int ability4Cooldown = ClientVoidData.getLevitationCooldown();
        boolean isEnderActive = ClientVoidData.getIsEnderActive();
        boolean isWitherActive = ClientWitherData.getIsWitherActive();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability1_gui.png");
        ResourceLocation abilityBgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability1_bg-gui.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability1_gui_overlay.png");
        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        ResourceLocation voidAbility2LockResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability2_lock_gui.png");
        ResourceLocation voidAbility3LockResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/void_abilities/void_ability3_lock_gui.png");


        if (player != null) {

            EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
            EnderPlayerDataSets.TeleportPlayerData tpData = dataSets.getOrCreateTeleportPlayerData(player);
            EnderPlayerDataSets.EnderLevitationPlayerData lData = dataSets.getOrCreateEnderLevitationPlayerData(player);


            int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int voidImageWidth = 100;
            int voidImageHeight = 50;

            int abilityX = voidScreenWidth - voidImageWidth;
            int abilityY = voidScreenHeight - voidImageHeight;



            int voidAbility4MaxCooldown = lData.getMAX_COOLDOWN();


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 106;
            int simpleAbilityHeight = 5;
            int simpleAbilityWidth2 = 5;
            int simpleAbilityHeight2 = 62;

            int abilitySimpleMaxCooldown = KenjisCombatFormsCommon.ABILITY1_COOLDOWN.get();
            int abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - ability1Cooldown;
            int abilitySimpleBarWidth = (int) ((float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown * simpleAbilityWidth);

            int voidAbilityElapsedCooldown2 = voidAbility4MaxCooldown - ability4Cooldown;
            int simpleBarHeight2 = (int) ((float) voidAbilityElapsedCooldown2 / voidAbility4MaxCooldown * simpleAbilityHeight2);


            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1 == AbilityManager.AbilityOption1.VOID_ABILITY1 ||
                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.VOID_ABILITY1) {

                if (!areFinalsActive) {

                   // simpleAbilityBarHandler.drawAbility1Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth);
                }
            }
            if (isEnderActive) {
               // SimpleAbilityBarHandler.drawAbility4Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth2, simpleAbilityHeight2, simpleBarHeight2);
            }
        }
    }
}
