package net.kenji.kenjiscombatforms.screen.form_menu;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form1SwapPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form2SwapPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form3SwapPacket;
import net.kenji.kenjiscombatforms.screen.form_menu.buttons.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class FormSwapMenu extends Screen {

    public FormSwapMenu(Component title) {
        super(title);
    }
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/menus/form_swap_menu.png");

    private static final int IMAGE_WIDTH = 256;
    private static final int IMAGE_HEIGHT = 256;

    int xOffset = 20;
    int yOffset = 35;
    int widgetWidth;
    int widgetHeight;

    Component basicFormButtonText = Component.translatable("menu.kenjiscombatforms.basic_form_button");
    Component voidFormButtonText = Component.translatable("menu.kenjiscombatforms.void_form_button");
    Component witherFormButtonText = Component.translatable("menu.kenjiscombatforms.wither_form_button");


    private static String formToSwap;


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


            int form1ButtonOffsetY = guiTop + yOffset + 47;
            int form2ButtonOffsetY = guiTop + yOffset + 73;
            int form3ButtonOffsetY = guiTop + yOffset + 99;


            String form1Text = getForm1Name().toLowerCase();
            String form2Text = getForm2Name().toLowerCase();
            String form3Text = getForm3Name().toLowerCase();


            Component translatedForm1Text = Component.translatable("menu.kenjiscombatforms." + form1Text);
            Component translatedForm2Text = Component.translatable("menu.kenjiscombatforms." + form2Text);
            Component translatedForm3Text = Component.translatable("menu.kenjiscombatforms." + form3Text);



            if (ClientFistData.getForm1Option() != FormManager.FormSelectionOption.NONE) {
                form1DrawPosY();
                Form1SwapButton formButton1 = new Form1SwapButton(formButtonOffsetX, form1ButtonOffsetY, formButtonDrawPosX, form1ButtonDrawPosY, translatedForm1Text, button -> chooseForm1Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton1);
            }
            if (ClientFistData.getForm2Option()  != FormManager.FormSelectionOption.NONE) {
                form2DrawPosY();
                Form2SwapButton formButton2 = new Form2SwapButton(formButtonOffsetX, form2ButtonOffsetY, formButtonDrawPosX, form2ButtonDrawPosY, translatedForm2Text, button -> chooseForm2Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton2);
            }
            if (ClientFistData.getForm3Option()  != FormManager.FormSelectionOption.NONE) {
                form3DrawPosY();
                Form3SwapButton formButton3 = new Form3SwapButton(formButtonOffsetX, form3ButtonOffsetY, formButtonDrawPosX, form3ButtonDrawPosY, translatedForm3Text, button -> chooseForm3Option(getMinecraft().player), 76, 20);
                this.addRenderableWidget(formButton3);
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


    public static void setFormToSwap(String form){
        formToSwap = form;
    }
    public String getFormToSwap(){
        if(formToSwap != null) {
            return formToSwap;
        }
        return "none";
    }



    private void form1DrawPosY(){
        if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.VOID){
            form1ButtonDrawPosY = 151;
        } if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.WITHER){
            form1ButtonDrawPosY = 172;
        } if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.SWIFT){
            form1ButtonDrawPosY = 193;
        } if(ClientFistData.getForm1Option() == FormManager.FormSelectionOption.POWER){
            form1ButtonDrawPosY = 214;
        }
    }
    private void form2DrawPosY(){
        if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.VOID){
            form2ButtonDrawPosY = 151;
        } if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.WITHER){
            form2ButtonDrawPosY = 172;
        }if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.SWIFT){
            form2ButtonDrawPosY = 193;
        }if(ClientFistData.getForm2Option() == FormManager.FormSelectionOption.POWER){
            form2ButtonDrawPosY = 214;
        }
    }
    private void form3DrawPosY(){
        if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.VOID){
            form3ButtonDrawPosY = 151;
        } if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.WITHER){
            form3ButtonDrawPosY = 172;
        } if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.SWIFT){
            form3ButtonDrawPosY = 193;
        } if(ClientFistData.getForm3Option() == FormManager.FormSelectionOption.POWER){
            form3ButtonDrawPosY = 214;
        }
    }


    private void chooseForm1Option(Player player){
        NetworkHandler.INSTANCE.sendToServer(new Form1SwapPacket(getFormToSwap()));
        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm2Option(Player player){
        NetworkHandler.INSTANCE.sendToServer(new Form2SwapPacket(getFormToSwap()));
        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
    private void chooseForm3Option(Player player){
        NetworkHandler.INSTANCE.sendToServer(new Form3SwapPacket(getFormToSwap()));
        if(minecraft != null && minecraft.screen != null) {
            minecraft.setScreen(null);
            SoundManager.playVoidFormChooseSound(player);
        }
    }
}
