package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.SwiftPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.swift_form.ClientSwiftData;
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
public class SwiftAbility2CooldownGui {


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        int abilityCooldown = ClientSwiftData.getCooldown2();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/swift_abilities/swift_ability2_gui.png");
        ResourceLocation abilityBgResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/swift_abilities/swift_ability2_bg-gui.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/swift_abilities/swift_ability2_gui_overlay.png");

        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");

        boolean isEnderActive = ClientVoidData.getIsEnderActive();
        boolean isWitherActive = ClientWitherData.getIsWitherActive();

        int imageWidth = 100;
        int imageHeight = 50;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();


        if (player != null) {

            SwiftPlayerDataSets dataSets = SwiftPlayerDataSets.getInstance();
            SwiftPlayerDataSets.SwiftInflictPlayerData sData = dataSets.getOrCreateSwiftInflictPlayerData(player);


            int voidAbility1FullHeight = 38;
            int voidAbility1MaxCooldown = sData.getMAX_COOLDOWN();
            float voidAbility1ElapsedCooldown = voidAbility1MaxCooldown - abilityCooldown;
            float voidAbility1CooldownProgress = (float) voidAbility1ElapsedCooldown / voidAbility1MaxCooldown;
            int abilityBarHeight = (int) (voidAbility1FullHeight * voidAbility1CooldownProgress);

            int abilityU = 0;
            int abilityV = 38;
            int abilityWidth = 37;
            int abilityHeight = 38;


            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;
            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 5;
            int simpleAbilityWidth2 = 5;
            int simpleAbilityHeight2 = 62;


            int abilitySimpleMaxCooldown = sData.getMAX_COOLDOWN();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);


            int voidAbilityFullHeight2 = 62;
            float voidAbilityElapsedCooldown2 = voidAbility1MaxCooldown - abilityCooldown;
            float voidAbilityCooldownProgress2 = (float) voidAbilityElapsedCooldown2 / voidAbility1MaxCooldown;
            int simpleBarHeight2 = (int) (voidAbilityFullHeight2 * voidAbilityCooldownProgress2);


            boolean areFinalsActive = isWitherActive || isEnderActive;



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility2 == AbilityManager.AbilityOption2.SWIFT_ABILITY2 ||
                    ClientFistData.getChosenAbility2() == AbilityManager.AbilityOption2.SWIFT_ABILITY2) {

                if (!areFinalsActive) {
                    SimpleAbilityBarHandler.drawAbility2Icon(event, true, abilityCooldown, abilityCooldown, emptyResource, simpleAbilityResource, emptyResource, emptyResource, emptyResource, emptyResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth, simpleAbilityHeight);
                }
            }
        }
    }
}
