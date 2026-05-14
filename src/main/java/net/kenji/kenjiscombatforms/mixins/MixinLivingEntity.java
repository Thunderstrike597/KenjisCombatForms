package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = LivingEntity.class)
public class MixinLivingEntity {


    @Inject(method = "getItemInHand", at = @At("RETURN"), cancellable = true)
    private void maybeReplaceGetItemInHand(CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
            if (patch == null || !patch.isEpicFightMode()) return;

            if (cir.getReturnValue().getItem() instanceof BaseFistClass) {
                ItemStack stack = player.getInventory().getSelected();

                cir.setReturnValue(stack);
            }
        }
    }

    @Inject(method = "getMainHandItem", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            if(cir.getReturnValue() == null)return;

            if (cir.getReturnValue().getItem() instanceof BaseFistClass) {
                    ItemStack stack = player.getInventory().getSelected();

                    if (FormManager.isHeldCategoryValid(player, stack)) {
                        boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(player.getUUID(), true);
                        if (isToggled)
                            cir.setReturnValue(FormManager.getCurrentFormItem(player));
                        return;
                    }
                    cir.setReturnValue(stack);
            }
        }
    }
}
