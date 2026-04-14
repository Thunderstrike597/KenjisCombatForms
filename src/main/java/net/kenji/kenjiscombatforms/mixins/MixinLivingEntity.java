package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public class MixinLivingEntity {

    @Inject(method = "setItemInHand", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(InteractionHand pHand, ItemStack pStack, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if(!(self instanceof Player player)) return;
        if(ControlHandler.useKeyMap.getOrDefault(player.getUUID(), false))
            return;
        if(pStack.getItem() instanceof BaseFistClass)
            ci.cancel();
    }

    @Inject(method = "getUseItem", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if(self instanceof Player player) {
            cir.setReturnValue(player.getInventory().getSelected());
        }
    }
    @Inject(method = "getItemInHand", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(InteractionHand pHand, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if(self instanceof Player player) {
            if (self.getMainHandItem().getItem() instanceof BaseFistClass) {
                cir.setReturnValue(player.getInventory().getSelected());
            }
        }
    }
}
