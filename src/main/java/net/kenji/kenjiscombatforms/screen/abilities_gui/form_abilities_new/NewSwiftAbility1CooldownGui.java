package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.SwiftPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.swift_form.ClientSwiftData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class NewSwiftAbility1CooldownGui {

    private static final SimpleAbilityHandler simpleAbilityHandler = new SimpleAbilityHandler();


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientVoidData.getCooldown();
    }

    static int getX(){
        if(KenjisCombatFormsCommon.ABILITY_SELECTION_MODE.get()){
            return simpleAbilityHandler.getSelectionModeX();
        }else return 30;
    }

    static int getY(){
        if(KenjisCombatFormsCommon.ABILITY_SELECTION_MODE.get()){
            return simpleAbilityHandler.getSelectionModeY();
        }else return 50;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityBackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui1.png");
        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui2.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability1_gui3.png");


        if (player != null) {


            SwiftPlayerDataSets dataSets = SwiftPlayerDataSets.getInstance();
            SwiftPlayerDataSets.SpeedPlayerData abilityData = dataSets.getOrCreateSpeedPlayerData(player);


            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;

            int v = 32;

            int abilityCooldown = ClientSwiftData.getCooldown();


            int ability1FullHeight = simpleAbilityHandler.getIconSize(v);  // Maximum cooldown bar height
            int ability1MaxCooldown = abilityData.getMAX_COOLDOWN();  // Total cooldown time
            float ability1ElapsedCooldown = ability1MaxCooldown - abilityCooldown;  // Remaining cooldown
            float ability1CooldownProgress = ability1ElapsedCooldown / (float) ability1MaxCooldown;  // Normalize to 0-1
            int abilityBarHeight = (int) (ability1FullHeight * ability1CooldownProgress);


            int maxCooldown = abilityData.getMAX_COOLDOWN();



            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilitySize = simpleAbilityHandler.getIconSize(v);
            int iconX = abilityX + getX();
            int iconY = abilityY + getY();

            boolean areFinalsActive = ClientEventHandler.getInstance().getAreFinalsActive();




            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1 == AbilityManager.AbilityOption1.SWIFT_ABILITY1 ||
                    ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.SWIFT_ABILITY1) {

                if (!areFinalsActive) {
                    simpleAbilityHandler.drawAbility1Icon(event,  abilityResource, abilityBackgroundResource, abilityOverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilitySize, abilityBarHeight, maxCooldown, abilityCooldown);
                }
            }
        }
    }
}
