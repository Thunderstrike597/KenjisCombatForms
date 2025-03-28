package net.kenji.kenjiscombatforms.screen.guiEffects;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class BlinkEffect {
    private static float fadeAmount = 0;
    private static final float FADE_SPEED = 0.6f; // Adjust for faster/slower fade
    private static boolean isFading = false;
    private static boolean isFadingOut = true;


    public static void triggerFade(Player player) {
        if(player != null) {
            isFading = true;
            isFadingOut = true;
            fadeAmount = 0;
        }
    }

    public static void render(GuiGraphics guiGraphics, float partialTick) {
        if (!isFading) return;

        if (isFadingOut) {
            fadeAmount += FADE_SPEED;
            if (fadeAmount >= 1) {
                isFadingOut = false;
            }
        } else {
            fadeAmount -= FADE_SPEED;
            if (fadeAmount <= 0) {
                isFading = false;
                fadeAmount = 0;
            }
        }

        fadeAmount = Mth.clamp(fadeAmount, 0, 1);
        int alpha = (int) (fadeAmount * 255);
        guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), alpha << 24);
    }
}
