package net.kenji.kenjiscombatforms.screen.other;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsClient;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities_new.SimpleAbilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class FormLevelGui {


    private static boolean getHideBasicLevel() {
        return EpicFightCombatFormsClient.HIDE_BASIC_LEVEL_GUI.get();
    }
    private static boolean getHideAllLevels() {
        return EpicFightCombatFormsClient.HIDE_ALL_LEVEL_GUI.get();
    }

    private static boolean getHideAbilityBars() {
        return EpicFightCombatFormsClient.HIDE_ABILITY_BARS.get();
    }

    private static boolean getHideAbilityBarsFirstPerson(Player player){
        if(Minecraft.getInstance().options.getCameraType().isFirstPerson()){
            return !EpicFightCombatFormsClient.HIDE_ABILITY_BARS_FIRST_PERSON.get();
        }
        return true;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {
        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        if (player != null) {
            boolean hideGUIWhenNotActive = SimpleAbilityHandler.hideAbilityGUIWhenNotActive(player);
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);

            String BASIC = BasicForm.getInstance().getName();
            String VOID = VoidForm.getInstance().getName();
            String WITHER = WitherForm.getInstance().getName();
            String SWIFT = SwiftForm.getInstance().getName();
            String POWER = PowerForm.getInstance().getName();


            int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int voidImageWidth = 100;
            int voidImageHeight = 50;

            int formLevelX = voidScreenWidth - voidImageWidth;
            int formLevelY = voidScreenHeight - voidImageHeight;


            int formLevelU = 0;
            int formLevelV = 0;
            int formLevelWidth = 50;
            int formLevelHeight = 60;


            if (!getHideAllLevels()) {
                if (hideGUIWhenNotActive) {
                    if (getHideAbilityBarsFirstPerson(player)) {
                        if (data.isHandCombat || !getHideAbilityBars()) {
                            String selectedForm = ClientFistData.getSelectedForm();
                            Form currentForm = FormManager.getInstance().getForm(selectedForm);
                            ResourceLocation level1Resource = currentForm.getLevelResources().get(0);
                            ResourceLocation level2Resource = currentForm.getLevelResources().get(1);
                            ResourceLocation level3Resource = currentForm.getLevelResources().get(2);

                            drawCurrentLevel(player, event, level1Resource, level2Resource, level3Resource, selectedForm, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                        }
                    }
                }
            }
        }
    }
    public static void drawCurrentLevel(Player player, RenderGuiOverlayEvent event, ResourceLocation level1Resource, ResourceLocation level2Resource, ResourceLocation level3Resource, String currentForm, int formLevelX, int formLevelY, int formLevelU, int formLevelV, int levelWidth, int levelHeight){
        Form form = FormManager.getInstance().getForm(ClientFistData.getSelectedForm());

        int guiX = formLevelX + 18;

        AbstractFormData specificFormData = form.getFormData(player.getUUID());
        if(ClientFistData.getSelectedForm() == currentForm) {
            if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                event.getGuiGraphics().blit(level1Resource, guiX, formLevelY, formLevelU, formLevelV, levelWidth, levelHeight);
            }
            else if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                event.getGuiGraphics().blit(level2Resource, guiX, formLevelY, formLevelU, formLevelV, levelWidth, levelHeight);
            }
            else if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL3) {
                event.getGuiGraphics().blit(level3Resource, guiX, formLevelY, formLevelU, formLevelV, levelWidth, levelHeight);
            }
        }
    }
}
