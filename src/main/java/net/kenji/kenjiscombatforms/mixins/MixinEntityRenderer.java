package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.EnderFormAbility;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class) // Target EntityRenderer since it defines getTextureLocation()
public abstract class MixinEntityRenderer<T extends Entity> {
    private static final ResourceLocation ENDER_TEXTURE = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/player_textures/ender_player_texture.png");
    private static final ResourceLocation WITHER_TEXTURE = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/player_textures/wither_player_texture.png");


    @Inject(method = "getTextureLocation*", at = @At("HEAD"), cancellable = true)
    private void injectGetTextureLocation(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> cir) {
        // Apply only to players
        boolean getIsEnderActive = EnderFormAbility.getInstance().getEnderFormActive(player);
        boolean isClientEnderActive = ClientVoidData.getIsEnderActive();
        boolean getIsWitherActive = WitherFormAbility.getInstance().getWitherFormActive(player);
        boolean isClientWitherActive = ClientWitherData.getIsWitherActive();
        if (getIsEnderActive || isClientEnderActive) {

            cir.setReturnValue(ENDER_TEXTURE);
        }
        if (getIsWitherActive || isClientWitherActive) {
            cir.setReturnValue(WITHER_TEXTURE);
        }
    }
}