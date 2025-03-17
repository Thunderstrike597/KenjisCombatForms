package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.PowerPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
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
public class PowerAbility1CooldownGui {

    private static final SimpleAbilityBarHandler simpleAbilityBarHandler = new SimpleAbilityBarHandler();


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientPowerData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        //if (pPlayer != null) {
        int abilityCooldown = ClientPowerData.getCooldown();
        boolean isWitherActive = ClientWitherData.getIsWitherActive();
        boolean isEnderActive = ClientVoidData.getIsEnderActive();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/power_abilities/power_ability1_gui.png");
        ResourceLocation abilityBgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/power_abilities/power_ability1_bg-gui.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/power_abilities/power_ability1_gui_overlay.png");

        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        if (player != null) {

            PowerPlayerDataSets dataSets = PowerPlayerDataSets.getInstance();
            PowerPlayerDataSets.StrengthPlayerData stData = dataSets.getOrCreateStrengthPlayerData(player);

            int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int voidImageWidth = 100;
            int voidImageHeight = 50;

            int abilityX = voidScreenWidth - voidImageWidth;
            int abilityY = voidScreenHeight - voidImageHeight;


            int voidAbility1FullHeight = 38;
            int voidAbility1MaxCooldown = stData.getMAX_COOLDOWN();
            float voidAbility1ElapsedCooldown = voidAbility1MaxCooldown - abilityCooldown;
            float voidAbility1CooldownProgress = (float) voidAbility1ElapsedCooldown / voidAbility1MaxCooldown;
            int abilityBarHeight = (int) (voidAbility1FullHeight * voidAbility1CooldownProgress);

            int abilityU = 0;
            int abilityV = 38;
            int abilityWidth = 34;
            int abilityHeight = 38;


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 5;


            int abilitySimpleMaxCooldown = stData.getMAX_COOLDOWN();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);


            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (!areFinalsActive) {
                if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1 == AbilityManager.AbilityOption1.POWER_ABILITY1 ||
                        ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.POWER_ABILITY1) {

               //     simpleAbilityBarHandler.drawAbility1Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth);
                }
            }
        }
    }
}
