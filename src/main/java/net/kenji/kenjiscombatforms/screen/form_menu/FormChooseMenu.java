package net.kenji.kenjiscombatforms.screen.form_menu;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
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

import java.util.List;
import java.util.Objects;


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

    int ability1ButtonDrawPosX;
    int ability2ButtonDrawPosX;
    int ability3ButtonDrawPosX;

    int form1ButtonDrawPosY;
    int form2ButtonDrawPosY;
    int form3ButtonDrawPosY;



    String ability1Text = getLearnedAbility1Name().toLowerCase();
    String ability2Text = getLearnedAbility2Name().toLowerCase();
    String ability3Text = getLearnedAbility3Name().toLowerCase();


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
            if (minecraft.player != null) {
                int guiLeft = (this.width - IMAGE_WIDTH) / 2;
                int guiTop = (this.height - IMAGE_HEIGHT) / 2;

                int formButtonOffsetX = guiLeft + xOffset + 19;
                int formButtonDrawPosX = 90;

                int basicFormButtonOffsetY = guiTop + yOffset + 22;
                int basicFormButtonDrawPosY = BasicForm.getInstance().getGUIDrawPosY();

                int form1ButtonOffsetY = guiTop + yOffset + 47;
                int form2ButtonOffsetY = guiTop + yOffset + 73;
                int form3ButtonOffsetY = guiTop + yOffset + 99;


                String form1Text = getForm1Name().toLowerCase();
                String form2Text = getForm2Name().toLowerCase();
                String form3Text = getForm3Name().toLowerCase();

                Ability ability1 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(0);
                Ability ability2 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(1);
                Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(2);

                Component translatedForm1Text = Component.translatable("menu.kenjiscombatforms." + form1Text);
                Component translatedForm2Text = Component.translatable("menu.kenjiscombatforms." + form2Text);
                Component translatedForm3Text = Component.translatable("menu.kenjiscombatforms." + form3Text);

                Component translatedAbility1Text = Component.translatable("menu.kenjiscombatforms." + ability1Text);
                Component translatedAbility2Text = Component.translatable("menu.kenjiscombatforms." + ability2Text);
                Component translatedAbility3Text = Component.translatable("menu.kenjiscombatforms." + ability3Text);

                BasicFormButton basicFormButton = new BasicFormButton(formButtonOffsetX, basicFormButtonOffsetY, formButtonDrawPosX, basicFormButtonDrawPosY, basicFormButtonText, button -> chooseBasicForm(getMinecraft().player), 76, 20);
                this.addRenderableWidget(basicFormButton);


                List<Form> formValue = FormManager.getInstance().getCurrentForms(minecraft.player);                List<AbstractFormData> formData = FormManager.getInstance().getCurrentFormData(minecraft.player);

                if (!Objects.equals(formValue.get(1).getName(), "NONE")) {
                    form1DrawPosY();
                    Form1Button formButton1 = new Form1Button(formButtonOffsetX, form1ButtonOffsetY, formButtonDrawPosX, form1ButtonDrawPosY, translatedForm1Text, button -> chooseForm1Option(getMinecraft().player), 76, 20);
                    this.addRenderableWidget(formButton1);
                }
                if (!Objects.equals(formValue.get(2).getName(), "NONE")) {
                    form2DrawPosY();
                    Form2Button formButton2 = new Form2Button(formButtonOffsetX, form2ButtonOffsetY, formButtonDrawPosX, form2ButtonDrawPosY, translatedForm2Text, button -> chooseForm2Option(getMinecraft().player), 76, 20);
                    this.addRenderableWidget(formButton2);
                }
                if (!Objects.equals(formValue.get(3).getName(), "NONE")) {
                    form3DrawPosY();
                    Form3Button formButton3 = new Form3Button(formButtonOffsetX, form3ButtonOffsetY, formButtonDrawPosX, form3ButtonDrawPosY, translatedForm3Text, button -> chooseForm3Option(getMinecraft().player), 76, 20);
                    this.addRenderableWidget(formButton3);
                }

                int ability1ButtonOffsetX = guiLeft + xOffset + 117;
                int ability1ButtonOffsetY = guiTop + yOffset + 14;
                int AbilityButtonDrawPosX = 186;

                if (!Objects.equals(ability1.getName(), "NONE") && !ability1.getName().isEmpty()) {
                    abilityDrawPosY();
                    abilityDrawPosX();
                    Ability1Button abilityButton1 = new Ability1Button(ability1ButtonOffsetX, ability1ButtonOffsetY, ability1ButtonDrawPosX, ability1ButtonDrawPosY, translatedAbility1Text, button -> chooseAbility1(getMinecraft().player), 68, 18);
                    this.addRenderableWidget(abilityButton1);
                }


                int ability2ButtonOffsetX = guiLeft + xOffset + 117;
                int ability2ButtonOffsetY = guiTop + yOffset + 39;


                if (!Objects.equals(ability2.getName(), "NONE") && !ability2.getName().isEmpty()) {
                    abilityDrawPosY();
                    abilityDrawPosX();
                    Ability2Button abilityButton2 = new Ability2Button(ability2ButtonOffsetX, ability2ButtonOffsetY, ability2ButtonDrawPosX, ability2ButtonDrawPosY, translatedAbility2Text, button -> chooseAbility2(getMinecraft().player), 68, 18);
                    this.addRenderableWidget(abilityButton2);
                }

                int finalAbilityButtonOffsetX = guiLeft + xOffset + 117;
                int finalAbilityButtonOffsetY = guiTop + yOffset + 64;
                int finalAbilityButtonDrawPosX = 186;


                if (!Objects.equals(ability3.getName(), "NONE") && !ability3.getName().isEmpty()) {
                    abilityDrawPosY();
                    abilityDrawPosX();
                    FinalAbilityButton abilityButton3 = new FinalAbilityButton(finalAbilityButtonOffsetX, finalAbilityButtonOffsetY, ability3ButtonDrawPosX, finalAbilityButtonDrawPosY, translatedAbility3Text, button -> chooseAbility3(getMinecraft().player), 68, 18);
                    this.addRenderableWidget(abilityButton3);
                }


                int levelUpButtonOffsetX = guiLeft + xOffset + 28;
                int levelUpButtonOffsetY = guiTop + yOffset + 8;
                int levelUpButtonDrawPosX = 6;
                int levelUpButtonDrawPosY = 8;
                if (Minecraft.getInstance().player != null) {
                    if (isAtMaxXp(Minecraft.getInstance().player)) {
                        if (ClientFistData.getCurrentFormLevel() != FormLevelManager.FormLevel.LEVEL3) {
                            LevelUpFormButton levelUpFormButton = new LevelUpFormButton(levelUpButtonOffsetX, levelUpButtonOffsetY, levelUpButtonDrawPosX, levelUpButtonDrawPosY, levelUpFormButtonText, button -> levelUpForm(getMinecraft().player), 54, 13);
                            this.addRenderableWidget(levelUpFormButton);
                        }
                    }
                }
            }
        }
    }

    private String getForm1Name(){
        if(ClientFistData.getForm1Option() != null){
            System.out.println(" Client Form 1 Option: " + ClientFistData.getForm1Option());
            return ClientFistData.getForm1Option();
        }
        return "NONE";
    }
    private String getForm2Name(){
        if(ClientFistData.getForm2Option() != null){
            return ClientFistData.getForm2Option();
        }
        return "NONE";
    }
    private String getForm3Name(){
        if(ClientFistData.getForm3Option() != null){
            return ClientFistData.getForm3Option();
        }
        return "NONE";
    }

    private String getLearnedAbility1Name() {
        if(minecraft != null) {
            AbilityManager abilityManager = AbilityManager.getInstance();
            if(abilityManager.getCurrentAbilities(getMinecraft().player).get(0) != null){
                return ClientFistData.getCurrentStoredAbility1(minecraft.player);
            }
        }
        return "NONE";
    }
    private String getLearnedAbility2Name() {
        if(minecraft != null) {
            AbilityManager abilityManager = AbilityManager.getInstance();
            if(abilityManager.getCurrentAbilities(getMinecraft().player).get(1) != null){
                return ClientFistData.getCurrentStoredAbility2(minecraft.player);
            }
        }
        return "NONE";
    }
    private String getLearnedAbility3Name() {
        if(minecraft != null) {
            AbilityManager abilityManager = AbilityManager.getInstance();
            if(abilityManager.getCurrentAbilities(getMinecraft().player).get(2) != null){
                return ClientFistData.getCurrentStoredAbility3(minecraft.player);
            }
        }
        return "NONE";
    }


    private void form1DrawPosY(){
        if(getForm1Name() != null) {
            form1ButtonDrawPosY = FormManager.getInstance().getForm(getForm1Name()).getGUIDrawPosY();
        }
    }
    private void form2DrawPosY(){
        if(getForm2Name() != null) {
            form2ButtonDrawPosY = FormManager.getInstance().getForm(getForm2Name()).getGUIDrawPosY();
        }
    }
    private void form3DrawPosY(){
        if(getForm3Name() != null) {
            form3ButtonDrawPosY = FormManager.getInstance().getForm(getForm3Name()).getGUIDrawPosY();
        }
    }

    private void abilityDrawPosY(){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(getMinecraft().player);
        Player player = getMinecraft().player;
        if(player != null) {
            Ability ability1 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(0);
            Ability ability2 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(1);
            Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(2);

            if (ability1 != null) {
                ability1ButtonDrawPosY = ability1.getGUIDrawPosY();
            }
            if (ability2 != null) {
                ability2ButtonDrawPosY = ability2.getGUIDrawPosY();
            }
            if (ability3 != null) {
                finalAbilityButtonDrawPosY = ability3.getGUIDrawPosY();
            }
        }
    }
    private void abilityDrawPosX(){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(getMinecraft().player);
        Player player = getMinecraft().player;
        if(player != null) {
            Ability ability1 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(0);
            Ability ability2 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(1);
            Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(getMinecraft().player).get(2);

            if (ability1 != null) {
                ability1ButtonDrawPosX = ability1.getGUIDrawPosX();
            }
            if (ability2 != null) {
                ability2ButtonDrawPosX = ability2.getGUIDrawPosX();
            }
            if (ability3 != null) {
                ability3ButtonDrawPosX = ability3.getGUIDrawPosX();
            }
        }
    }



    private void levelUpForm(Player player){
        if(minecraft != null && minecraft.screen != null && Minecraft.getInstance().player != null) {
            minecraft.setScreen(null);


            if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL1) {
                int updatedXp = 0;
                int updatedXpMAX = ClientFistData.getCurrentFormXpMAX() * EpicFightCombatFormsCommon.LEVEL2_FORM_MAX_XP_ADDITION.get();
                FormLevelManager.FormLevel newFormLevel = FormLevelManager.FormLevel.LEVEL2;

                NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(ClientFistData.getSelectedForm(), updatedXp, updatedXpMAX, newFormLevel));
            }
            else if (ClientFistData.getCurrentFormLevel() == FormLevelManager.FormLevel.LEVEL2) {
                FormLevelManager.FormLevel newFormLevel = FormLevelManager.FormLevel.LEVEL3;

                NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(ClientFistData.getSelectedForm(), ClientFistData.getCurrentFormXp(), ClientFistData.getCurrentFormXpMAX(), newFormLevel));

            }
        }
    }

    private void chooseForm1Option(Player player){

        if(minecraft != null && minecraft.screen != null) {
            ClientFistData.setSelectedForm(getForm1Name());
            ClientFistData.setSpecificFormData(player);
            NetworkHandler.INSTANCE.sendToServer(new Form1ChoosePacket(getForm1Name()));

            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm2Option(Player player){


        if(minecraft != null && minecraft.screen != null) {
            ClientFistData.setSelectedForm(getForm2Name());
            ClientFistData.setSpecificFormData(player);
            NetworkHandler.INSTANCE.sendToServer(new Form2ChoosePacket(getForm2Name()));
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm3Option(Player player){
        if(minecraft != null && minecraft.screen != null) {
            ClientFistData.setSelectedForm(getForm3Name());
            ClientFistData.setSpecificFormData(player);
            NetworkHandler.INSTANCE.sendToServer(new Form3ChoosePacket(getForm3Name()));

            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }

    private void chooseBasicForm(Player player){
        if(minecraft != null && minecraft.screen != null) {
            ClientFistData.setSelectedForm(BasicForm.getInstance().getName());
            ClientFistData.setSpecificFormData(player);
            NetworkHandler.INSTANCE.sendToServer(new BasicFormChoosePacket());
            AbilityChangeHandler.getInstance().setFormsAndAbilities(player, FormManager.getInstance().getForm(BasicForm.getInstance().getName()).getFormData(player.getUUID()));
            minecraft.setScreen(null);
            SoundManager.playBasicFormChooseSound(player);
        }
    }

    private void chooseAbility1(Player player){
        if(minecraft != null && minecraft.screen != null) {
            SoundManager.playVoidFormChooseSound(player);
            NetworkHandler.INSTANCE.sendToServer(new Ability1ChoosePacket(ClientFistData.getCurrentStoredAbility1(player)));
        }
    }
    private void chooseAbility2(Player player){
        if(minecraft != null && minecraft.screen != null) {
            NetworkHandler.INSTANCE.sendToServer(new Ability2ChoosePacket(ClientFistData.getCurrentStoredAbility2(player)));
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseAbility3(Player player){
        if(minecraft != null && minecraft.screen != null) {
            NetworkHandler.INSTANCE.sendToServer(new Ability3ChoosePacket(ClientFistData.getCurrentStoredAbility3(player)));
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
