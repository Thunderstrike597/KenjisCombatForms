package net.kenji.kenjiscombatforms.screen.other;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class TranslucentGui {

    private static boolean translucentGui(){
        return EpicFightCombatFormsClient.TRANSLUCENT_GUI.get();
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;

        if (player != null) {
            if (translucentGui()) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                double bgAlpha = EpicFightCombatFormsClient.GUI_TRANSLUCENT_AMOUNT.get();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, (float) bgAlpha);
            }
        }
    }
}
