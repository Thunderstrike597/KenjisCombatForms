package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.PowerControl;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
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
public class NewWitherFinalAbilityCooldownGui {

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
        }else return 74;
    }

   static int getY(){
        if(KenjisCombatFormsCommon.ABILITY_SELECTION_MODE.get()){
            return simpleAbilityHandler.getSelectionModeY();
        }else return 42;
    }



    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation abilityBackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability3_gui1.png");
        ResourceLocation abilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability3_gui2.png");
        ResourceLocation abilityOverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability3_gui3.png");


        if (player != null) {

            WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
            WitherPlayerDataSets.WitherFormPlayerData abilityData = dataSets.getOrCreateWitherFormPlayerData(player);


            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;

            int v = 44;
            int abilityCooldown = ClientWitherData.getCooldown3();


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




            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenFinal == AbilityManager.AbilityOption3.WITHER_FINAL ||
                    ClientFistData.getChosenAbility3() == AbilityManager.AbilityOption3.WITHER_FINAL) {

                simpleAbilityHandler.drawAbility3Icon(event,  abilityResource, abilityBackgroundResource, abilityOverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilitySize, abilityBarHeight, maxCooldown, abilityCooldown);
            }
        }
    }
}
