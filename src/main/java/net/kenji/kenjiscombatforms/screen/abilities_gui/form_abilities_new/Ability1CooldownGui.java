package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class Ability1CooldownGui {

    private static final SimpleAbilityHandler simpleAbilityHandler = new SimpleAbilityHandler();



    static int getX(){
        if(EpicFightCombatFormsCommon.ABILITY_SELECTION_MODE.get()){
            return simpleAbilityHandler.getSelectionModeX();
        }else return 30;
    }

    static int getY(){
        if(EpicFightCombatFormsCommon.ABILITY_SELECTION_MODE.get()){
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

        ResourceLocation ability4BackgroundResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui1.png");
        ResourceLocation ability4Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui2.png");
        ResourceLocation ability4OverlayResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/ability4_gui3.png");


        if (player != null) {

            AbilityManager abilityManager = AbilityManager.getInstance();
            AbstractAbilityData ability1Data = abilityManager.getCurrentAbilityData(player).get(0);

            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;

            int v = 32;

            int abilityCooldown = ability1Data.getClientAbilityCooldown();

            int abilityFullHeight = simpleAbilityHandler.getIconSize(v);  // Maximum cooldown bar height
            int maxCooldown = ability1Data.getMAX_COOLDOWN();
            float abilityElapsedCooldown = maxCooldown - abilityCooldown;  // Remaining cooldown
            float abilityCooldownProgress = abilityElapsedCooldown / (float) maxCooldown;  // Normalize to 0-1
            int abilityBarHeight = (int) (abilityFullHeight * abilityCooldownProgress);


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilitySize = simpleAbilityHandler.getIconSize(v);
            int iconX = abilityX + getX();
            int iconY = abilityY + getY();

            boolean areFinalsActive = ClientEventHandler.getInstance().getAreFinalsActive();


            if (!Objects.equals(AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1, "NONE") ||
                    !Objects.equals(ClientFistData.getChosenAbility1(), "NONE")) {

                if (!areFinalsActive) {
                    simpleAbilityHandler.drawAbility1Icon(event, abilityResource, abilityBackgroundResource, abilityOverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilitySize, abilityBarHeight, maxCooldown, abilityCooldown);
                }
            }
        }
    }
}
