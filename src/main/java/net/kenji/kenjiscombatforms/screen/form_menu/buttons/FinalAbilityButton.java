package net.kenji.kenjiscombatforms.screen.form_menu.buttons;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FinalAbilityButton extends Button {
    public ResourceLocation TEXTURE =
            new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/menus/form_choose_menu.png");

    private final int textureX, textureY;

    public FinalAbilityButton(int x, int y, int textureX, int textureY, Component message, OnPress onPress, int width, int height) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        this.textureX = textureX;
        this.textureY = textureY;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Calculate the hover offset based on the button's actual height in the texture// This should match the height of your button in the texture
        int textureY = this.isHovered() ? this.textureY : this.textureY;

        // Render the button
        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.textureX, textureY, this.getWidth(), this.getHeight());

        // Render the text
        int textColor = this.isHovered() ? 16777120 : 14737632; // Yellow when hovered, default otherwise
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage(),
                this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, textColor);
    }
}
