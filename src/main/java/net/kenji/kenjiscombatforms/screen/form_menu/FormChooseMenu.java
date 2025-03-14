package net.kenji.kenjiscombatforms.screen.form_menu;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability1ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability2ChoosePacket;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability3ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.BasicFormChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form1ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form2ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form3ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_level.SyncServerFormLevelPacket;
import net.kenji.kenjiscombatforms.screen.form_menu.buttons.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class FormChooseMenu extends Screen {

    public FormChooseMenu(Component title) {
        super(title);
    }
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/menus/form_choose_menu.png");

    private static final int IMAGE_WIDTH = 256;
    private static final int IMAGE_HEIGHT = 256;

    int xOffset = 20;
    int yOffset = 35;
    int widgetWidth;
    int widgetHeight;

    Component basicFormButtonText = Component.translatable("menu.kenjiscombatforms.basic_form_button");
    Component voidFormButtonText = Component.translatable("menu.kenjiscombatforms.void_form_button");
    Component witherFormButtonText = Component.translatable("menu.kenjiscombatforms.wither_form_button");
    Component levelUpFormButtonText = Component.translatable("menu.kenjiscombatforms.level_up_button");

    int ability1ButtonDrawPosY;
    int ability2ButtonDrawPosY;
    int finalAbilityButtonDrawPosY;

    int form1ButtonDrawPosY;
    int form2ButtonDrawPosY;
    int form3ButtonDrawPosY;

    private void renderVoidLockGui(GuiGraphics guiGraphics){
        int guiLeft = (this.width - IMAGE_WIDTH) / 2;
        int guiTop = (this.height - IMAGE_HEIGHT) / 2;

        int voidFormLockOffsetX = guiLeft + xOffset + 24;
        int voidFormLockOffsetY = guiTop + yOffset + 48;
        int voidFormLockDrawPosX = 1;
        int voidFormLockDrawPosY = 101 ;

        guiGraphics.blit(TEXTURE, voidFormLockOffsetX, voidFormLockOffsetY, voidFormLockDrawPosX, voidFormLockDrawPosY, 68, 17);
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        super.renderBackground(guiGraphics);
       int imageWidth = 256;
       int imageHeight = 256;
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        widgetWidth = x;
        widgetHeight = y;
        guiGraphics.blit(TEXTURE, x + xOffset, y + yOffset, 70, 0, imageWidth - 70, imageHeight + yOffset - 146);
        int totalXpBarHeight = 122;
       Player player = Minecraft.getInstance().player;

       if(player != null) {


           int currentFormXp = ClientFistData.getCurrentFormXp();
           int currentFormXpMAX = ClientFistData.getCurrentFormXpMAX();

           float xpBarProgress = (float) currentFormXp / currentFormXpMAX;
           xpBarProgress = Math.max(0, Math.min(1, xpBarProgress));

           int xpBarHeight = Math.round(xpBarProgress * totalXpBarHeight);
           if (currentFormXp > 0 && xpBarHeight == 0) {
               xpBarHeight = 1;
           }

           if (xpBarHeight > 0) {
               guiGraphics.blit(TEXTURE, x + xOffset + 6, y + yOffset + totalXpBarHeight - xpBarHeight + 10, 62, 9, 5, xpBarHeight);
           }
       }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }



    @Override
    protected void init() {
        super.init();
        if (minecraft != null) {
            int guiLeft = (this.width - IMAGE_WIDTH) / 2;
            int guiTop = (this.height - IMAGE_HEIGHT) / 2;

            int formButtonOffsetX = guiLeft + xOffset + 19;
            int formButtonDrawPosX = 90;

            int basicFormButtonOffsetY = guiTop + yOffset + 22;
            int basicFormButtonDrawPosY = 148;

            int form1ButtonOffsetY = guiTop + yOffset + 47;
            int form2ButtonOffsetY = guiTop + yOffset + 73;
            int form3ButtonOffsetY = guiTop + yOffset + 99;


            String form1Text = getForm1Name().toLowerCase();
            String form2Text = getForm2Name().toLowerCase();
            String form3Text = getForm3Name().toLowerCase();

            String ability1Text = getLearnedAbility1Name().toLowerCase();
            String ability2Text = getLearnedAbility2Name().toLowerCase();
            String ability3Text = getLearnedAbility3Name().toLowerCase();

            Component translatedForm1Text = Component.translatable("menu.kenjiscombatforms." + form1Text);
            Component translatedForm2Text = Component.translatable("menu.kenjiscombatforms." + form2Text);
            Component translatedForm3Text = Component.translatable("menu.kenjiscombatforms." + form3Text);

            Component translatedAbility1Text = Component.translatable("menu.kenjiscombatforms." + ability1Text);
            Component translatedAbility2Text = Component.translatable("menu.kenjiscombatforms." + ability2Text);
            Component translatedAbility3Text = Component.translatable("menu.kenjiscombatforms." + ability3Text);

            BasicFormButton basicFormButton = new BasicFormButton(formButtonOffsetX, basicFormButtonOffsetY, formButtonDrawPosX, basicFormButtonDrawPosY, basicFormButtonText, button -> chooseBasicForm(getMinecraft().player), 76, 20);
            this.addRenderableWidget(basicFormButton);

            if (ClientFistData.getForm1Option() != FormManager.FormSelectionOption.NONE) {
                form1DrawPosY();
                Form1Button formButton1 = new Form1Button(formButtonOffsetX, form1ButtonOffsetY, formButtonDrawPosX, form1ButtonDrawPosY, translatedForm1Text, button -> chooseForm1Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton1);
            }
            if (ClientFistData.getForm2Option()  != FormManager.FormSelectionOption.NONE) {
                form2DrawPosY();
                Form2Button formButton2 = new Form2Button(formButtonOffsetX, form2ButtonOffsetY, formButtonDrawPosX, form2ButtonDrawPosY, translatedForm2Text, button -> chooseForm2Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton2);
            }
            if (ClientFistData.getForm3Option() != FormManager.FormSelectionOption.NONE) {
                form3DrawPosY();
                Form3Button formButton3 = new Form3Button(formButtonOffsetX, form3ButtonOffsetY, formButtonDrawPosX, form3ButtonDrawPosY, translatedForm3Text, button -> chooseForm3Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton3);
            }

            int ability1ButtonOffsetX = guiLeft + xOffset + 117;
            int ability1ButtonOffsetY = guiTop + yOffset + 14;
            int AbilityButtonDrawPosX = 186;


            if (ClientFistData.getCurrentAbility1() != AbilityManager.AbilityOption1.NONE) {
                abilityDrawPosY();
                Ability1Button abilityButton1 = new Ability1Button(ability1ButtonOffsetX, ability1ButtonOffsetY, AbilityButtonDrawPosX, ability1ButtonDrawPosY, translatedAbility1Text, button -> chooseAbility1(getMinecraft().player), 68, 18);
                this.addRenderableWidget(abilityButton1);
            }


            int ability2ButtonOffsetX = guiLeft + xOffset + 117;
            int ability2ButtonOffsetY = guiTop + yOffset + 39;
            int voidAbility2ButtonDrawPosX = 186;

            if (ClientFistData.getCurrentAbility2() != AbilityManager.AbilityOption2.NONE) {
                abilityDrawPosY();
                Ability2Button abilityButton2 = new Ability2Button(ability2ButtonOffsetX, ability2ButtonOffsetY, voidAbility2ButtonDrawPosX, ability2ButtonDrawPosY, translatedAbility2Text, button -> chooseAbility2(getMinecraft().player), 68, 18);
                this.addRenderableWidget(abilityButton2);
            }

            int finalAbilityButtonOffsetX = guiLeft + xOffset + 117;
            int finalAbilityButtonOffsetY = guiTop + yOffset + 64;
            int finalAbilityButtonDrawPosX = 186;


                if (ClientFistData.getCurrentAbility3() != AbilityManager.AbilityOption3.NONE) {
                    abilityDrawPosY();
                    FinalAbilityButton abilityButton3 = new FinalAbilityButton(finalAbilityButtonOffsetX, finalAbilityButtonOffsetY, finalAbilityButtonDrawPosX, finalAbilityButtonDrawPosY, translatedAbility3Text, button -> chooseAbility3(getMinecraft().player), 68, 18);
                    this.addRenderableWidget(abilityButton3);
                }



            int levelUpButtonOffsetX = guiLeft + xOffset + 28;
            int levelUpButtonOffsetY = guiTop + yOffset + 8;
            int levelUpButtonDrawPosX = 6;
            int levelUpButtonDrawPosY = 8;
        if(Minecraft.getInstance().player != null){
            if (isAtMaxXp(Minecraft.getInstance().player)) {
                if (ClientFistData.getCurrentFormLevel() != FormLevelManager.FormLevel.LEVEL3) {
                    LevelUpFormButton levelUpFormButton = new LevelUpFormButton(levelUpButtonOffsetX, levelUpButtonOffsetY, levelUpButtonDrawPosX, levelUpButtonDrawPosY, levelUpFormButtonText, button -> levelUpForm(getMinecraft().player), 54, 13);
                    this.addRenderableWidget(levelUpFormButton);
                }
            }
        }
}
    }

    private String getForm1Name(){
        if(ClientFistData.getForm1Option() != FormManager.FormSelectionOption.NONE){
            return ClientFistData.getForm1Option().name();
        }
        return "none";
    }
    private String getForm2Name(){
        if(ClientFistData.getForm2Option() != FormManager.FormSelectionOption.NONE){
            return ClientFistData.getForm2Option().name();
        }
        return "none";
    }
    private String getForm3Name(){
        if(ClientFistData.getForm3Option() != FormManager.FormSelectionOption.NONE){
            return ClientFistData.getForm3Option().name();
        }
        return "none";
    }

    private String getLearnedAbility1Name() {
        if(ClientFistData.getCurrentAbility1() != null) {
            return ClientFistData.getCurrentAbility1().name();
        }
        return "none";
    }
    private String getLearnedAbility2Name() {
        if(ClientFistData.getCurrentAbility2() != null) {
            return ClientFistData.getCurrentAbility2().name();
        }
        return "none";
    }
    private String getLearnedAbility3Name() {
        if(ClientFistData.getCurrentAbility3() != null) {
            return ClientFistData.getCurrentAbility3().name();
        }
        return "none";
    }


    private void form1DrawPosY(){
        if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.VOID){
            form1ButtonDrawPosY = 169;
        } if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.WITHER){
            form1ButtonDrawPosY = 190;
        } if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.SWIFT){
            form1ButtonDrawPosY = 211;
        }if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.POWER){
            form1ButtonDrawPosY = 232;
        }
    }
    private void form2DrawPosY(){
        if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.VOID){
            form2ButtonDrawPosY = 169;
        } if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.WITHER){
            form2ButtonDrawPosY = 190;
        }if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.SWIFT){
            form2ButtonDrawPosY = 211;
        }if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.POWER){
            form2ButtonDrawPosY = 232;
        }
    }
    private void form3DrawPosY(){
        if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.VOID){
            form3ButtonDrawPosY = 169;
        } if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.WITHER){
            form3ButtonDrawPosY = 190;
        }if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.SWIFT){
            form3ButtonDrawPosY = 211;
        }if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.POWER){
            form3ButtonDrawPosY = 232;
        }
    }

    private void abilityDrawPosY(){
        if(ClientFistData.getCurrentAbility1() == AbilityManager.AbilityOption1.VOID_ABILITY1) {
           ability1ButtonDrawPosY = 155;
        }if(ClientFistData.getCurrentAbility1() == AbilityManager.AbilityOption1.WITHER_ABILITY1) {
            ability1ButtonDrawPosY = 180;
        }if(ClientFistData.getCurrentAbility1() == AbilityManager.AbilityOption1.SWIFT_ABILITY1) {
            ability1ButtonDrawPosY = 205;
        }if(ClientFistData.getCurrentAbility1() == AbilityManager.AbilityOption1.POWER_ABILITY1) {
            ability1ButtonDrawPosY = 230;
        }

        if(ClientFistData.getCurrentAbility2() == AbilityManager.AbilityOption2.VOID_ABILITY2) {
            ability2ButtonDrawPosY = 155;
        }if(ClientFistData.getCurrentAbility2() == AbilityManager.AbilityOption2.WITHER_ABILITY2) {
            ability2ButtonDrawPosY = 180;
        }if(ClientFistData.getCurrentAbility2() == AbilityManager.AbilityOption2.SWIFT_ABILITY2) {
            ability2ButtonDrawPosY = 205;
        }if(ClientFistData.getCurrentAbility2() == AbilityManager.AbilityOption2.POWER_ABILITY2) {
            ability2ButtonDrawPosY = 230;
        }

        if(ClientFistData.getCurrentAbility3() == AbilityManager.AbilityOption3.VOID_FINAL) {
            finalAbilityButtonDrawPosY = 155;
        }if(ClientFistData.getCurrentAbility3() == AbilityManager.AbilityOption3.WITHER_FINAL) {
            finalAbilityButtonDrawPosY = 180;
        }
    }

    private void levelUpForm(Player player){
        if(minecraft != null && minecraft.screen != null && Minecraft.getInstance().player != null) {
            minecraft.setScreen(null);


            if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                int updatedXp = 0;
                int updatedXpMAX = ClientFistData.getCurrentFormXpMAX() * KenjisCombatFormsCommon.LEVEL2_FORM_MAX_XP_ADDITION.get();
                FormLevelManager.FormLevel newFormLevel = FormLevelManager.FormLevel.LEVEL2;


                NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(ClientFistData.getSelectedForm().name(), updatedXp, updatedXpMAX, newFormLevel));
            }
            else if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                FormLevelManager.FormLevel newFormLevel = FormLevelManager.FormLevel.LEVEL3;

                NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(ClientFistData.getSelectedForm().name(), ClientFistData.getCurrentFormXp(), ClientFistData.getCurrentFormXpMAX(), newFormLevel));

            }
        }
    }

    private void chooseForm1Option(Player player){

        NetworkHandler.INSTANCE.sendToServer(new Form1ChoosePacket(getForm1Name()));
        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm2Option(Player player){
        NetworkHandler.INSTANCE.sendToServer(new Form2ChoosePacket(getForm2Name()));

        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm3Option(Player player){
        NetworkHandler.INSTANCE.sendToServer(new Form3ChoosePacket(getForm3Name()));

        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }

    private void chooseBasicForm(Player player){
        NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playBasicFormChooseSound(player);
        }
    }

    private void chooseAbility1(Player player){
        if(minecraft != null && minecraft.screen != null) {
            SoundManager.playVoidFormChooseSound(player);
            NetworkHandler.INSTANCE.sendToServer(new Ability1ChoosePacket(ClientFistData.getCurrentAbility1().name()));
        }
    }
    private void chooseAbility2(Player player){
        if(minecraft != null && minecraft.screen != null) {
            NetworkHandler.INSTANCE.sendToServer(new Ability2ChoosePacket(ClientFistData.getCurrentAbility2().name()));
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseAbility3(Player player){
        if(minecraft != null && minecraft.screen != null) {
            NetworkHandler.INSTANCE.sendToServer(new Ability3ChoosePacket(ClientFistData.getCurrentAbility3().name()));
            SoundManager.playVoidFormChooseSound(player);
        }
    }

    public boolean isAtMaxXp(Player player) {
        if(Minecraft.getInstance().player != null) {
            AbstractFormData specificFormData = ClientFistData.getSpecificFormData(Minecraft.getInstance().player);

            int currentXp = ClientFistData.getCurrentFormXp();
            int maxXp = ClientFistData.getCurrentFormXpMAX();
            return currentXp >= maxXp;
        }
        return false;
    }
}
