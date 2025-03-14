package net.kenji.kenjiscombatforms.screen.other;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsClient;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class FormLevelGui {


    private static boolean getHideBasicLevel() {
        return KenjisCombatFormsClient.HIDE_BASIC_LEVEL_GUI.get();
    }
    private static boolean getHideAllLevels() {
        return KenjisCombatFormsClient.HIDE_ALL_LEVEL_GUI.get();
    }

    private static boolean getHideAbilityBars() {
        return KenjisCombatFormsClient.HIDE_ABILITY_BARS.get();
    }

    private static boolean getHideAbilityBarsFirstPerson(Player player){
        if(Minecraft.getInstance().options.getCameraType().isFirstPerson()){
            return !KenjisCombatFormsClient.HIDE_ABILITY_BARS_FIRST_PERSON.get();
        }
        return true;
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {



        float voidAbility1Cooldown = ClientVoidData.getCooldown();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;

        ResourceLocation basicLevel1Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_1.png");
        ResourceLocation basicLevel2Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_2.png");
        ResourceLocation basicLevel3Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/level_3.png");
        ResourceLocation voidLevel1Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl1.png");
        ResourceLocation voidLevel2Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl2.png");
        ResourceLocation voidLevel3Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/void_lvl3.png");
        ResourceLocation witherLevel1Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/wither_lvl1.png");
        ResourceLocation witherLevel2Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/wither_lvl2.png");
        ResourceLocation witherLevel3Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/wither_lvl3.png");
        ResourceLocation swiftLevel1Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/swift_lvl1.png");
        ResourceLocation swiftLevel2Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/swift_lvl2.png");
        ResourceLocation swiftLevel3Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/swift_lvl3.png");
        ResourceLocation powerLevel1Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/power_lvl1.png");
        ResourceLocation powerLevel2Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/power_lvl2.png");
        ResourceLocation powerLevel3Resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/levels/power_lvl3.png");



        if (player != null) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);

            FormManager.FormSelectionOption BASIC = FormManager.FormSelectionOption.BASIC;
            FormManager.FormSelectionOption VOID = FormManager.FormSelectionOption.VOID;
            FormManager.FormSelectionOption WITHER = FormManager.FormSelectionOption.WITHER;
            FormManager.FormSelectionOption SWIFT = FormManager.FormSelectionOption.SWIFT;
            FormManager.FormSelectionOption POWER = FormManager.FormSelectionOption.POWER;


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
                if (getHideAbilityBarsFirstPerson(player)) {
                    if (data.isHandCombat || !getHideAbilityBars()) {
                        if (ClientFistData.getSelectedForm() == FormManager.FormSelectionOption.BASIC) {
                            if (!getHideBasicLevel()) {
                                drawCurrentLevel(player, event, basicLevel1Resource, basicLevel2Resource, basicLevel3Resource, BASIC, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                            }
                        } else if (ClientFistData.getSelectedForm() == FormManager.FormSelectionOption.VOID) {
                            drawCurrentLevel(player, event, voidLevel1Resource, voidLevel2Resource, voidLevel3Resource, VOID, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                        } else if (ClientFistData.getSelectedForm() == FormManager.FormSelectionOption.WITHER) {
                            drawCurrentLevel(player, event, witherLevel1Resource, witherLevel2Resource, witherLevel3Resource, WITHER, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                        } else if (ClientFistData.getSelectedForm() == FormManager.FormSelectionOption.SWIFT) {
                            drawCurrentLevel(player, event, swiftLevel1Resource, swiftLevel2Resource, swiftLevel3Resource, SWIFT, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                        } else if (ClientFistData.getSelectedForm() == FormManager.FormSelectionOption.POWER) {
                            drawCurrentLevel(player, event, powerLevel1Resource, powerLevel2Resource, powerLevel3Resource, POWER, formLevelX, formLevelY, formLevelU, formLevelV, formLevelWidth, formLevelHeight);
                        }
                    }
                }
            }
        }
    }
    public static void drawCurrentLevel(Player player, RenderGuiOverlayEvent event, ResourceLocation level1Resource, ResourceLocation level2Resource, ResourceLocation level3Resource,FormManager.FormSelectionOption currentForm, int formLevelX, int formLevelY, int formLevelU, int formLevelV, int levelWidth, int levelHeight){
        Form form = FormManager.getInstance().getForm(ClientFistData.getSelectedForm().name());

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
