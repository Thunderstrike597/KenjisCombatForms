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
public class Ability4CooldownGui {

    private static final SimpleAbilityHandler simpleAbilityHandler = new SimpleAbilityHandler();


    static int getX() {
        if (EpicFightCombatFormsCommon.ABILITY_SELECTION_MODE.get()) {
            return simpleAbilityHandler.getSelectionModeX();
        } else return 30;
    }

    static int getY() {
        if (EpicFightCombatFormsCommon.ABILITY_SELECTION_MODE.get()) {
            return simpleAbilityHandler.getSelectionModeY();
        } else return 50;
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
            AbstractAbilityData ability3Data = abilityManager.getCurrentAbilityData(player).get(2);
            AbstractAbilityData ability4Data = abilityManager.getCurrentFinalAbilityData(player).get(0);



            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int imageWidth = 128;
            int imageHeight = 128;
            int abilityX = screenWidth - imageWidth;
            int abilityY = screenHeight - imageHeight;

            int v = 32;

            int ability4Cooldown = ability4Data.getClientAbilityCooldown();
            boolean isFinalActive = ability3Data.isAbilityActive();

            int ability4FullHeight = simpleAbilityHandler.getIconSize(v);  // Maximum cooldown bar height
            int ability4MaxCooldown = ability4Data.getMAX_COOLDOWN();  // Total cooldown time
            float ability4ElapsedCooldown = ability4MaxCooldown - ability4Cooldown;  // Remaining cooldown
            float ability4CooldownProgress = ability4ElapsedCooldown / (float) ability4MaxCooldown;  // Normalize to 0-1
            int ability4BarHeight = (int) (ability4FullHeight * ability4CooldownProgress);


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilitySize = simpleAbilityHandler.getIconSize(v);
            int iconX = abilityX + getX();
            int iconY = abilityY + getY();


            if (isFinalActive) {
                simpleAbilityHandler.drawAbility4Icon(event, ability4Resource, ability4BackgroundResource, ability4OverlayResource, iconX, iconY, simpleAbilityU, simpleAbilityV, simpleAbilitySize, ability4BarHeight, ability4MaxCooldown, ability4Cooldown);
            }
        }
    }
}

