package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerboundPlayerActionPacket {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    public void onHandle(ServerboundPlayerActionPacket par1, CallbackInfo ci) {
        if (this.player == null) return;
        ItemStack mainHandItem = this.player.getMainHandItem();
        ItemStack offHandItem = this.player.getOffhandItem();

        if(FormManager.isInstanceOfForm(mainHandItem) || FormManager.isInstanceOfForm(offHandItem)) {
            if (par1.getAction() == ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
                ci.cancel();
            }
        }
    }

}
