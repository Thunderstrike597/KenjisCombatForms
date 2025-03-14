package net.kenji.kenjiscombatforms.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EssenceInfusingScreen extends AbstractContainerScreen<EssenceInfusingMenu> {
    SoundManager soundManager;
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/essence_infusing_menu.png");

    public EssenceInfusingScreen(EssenceInfusingMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
        this.soundManager = new SoundManager();
    }



    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    renderChargeBar(guiGraphics, x, y);
    }



    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
            if (menu.isCrafting()) {
                guiGraphics.blit(TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledProgress());
                soundManager.essenceInfusionSoundEvent(menu.invertoryPlayer);
            } else if(!menu.isCrafting()){
                soundManager.essenceInfusionSoundEventCancel(menu.invertoryPlayer);
            }

    }
    private void renderChargeBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCharging()) {
            guiGraphics.blit(TEXTURE, x + 118, y + 40, 196, 29, menu.getScaledProgress2(), -2);
        }
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
