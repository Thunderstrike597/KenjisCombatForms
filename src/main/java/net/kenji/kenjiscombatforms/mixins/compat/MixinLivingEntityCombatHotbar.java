package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public class MixinLivingEntityCombatHotbar {


    @Inject(method = "getMainHandItem", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if(self instanceof Player player) {
            if(cir.getReturnValue() == null)return;

            if (cir.getReturnValue().getItem() instanceof BaseFistClass) {
                player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
                    int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
                    ItemStack stack = handler.getStackInSlot(selectedSlot);

                    if(FormManager.isHeldCategoryValid(player, stack)) {
                        boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(player.getUUID(), true);
                        if(isToggled)
                            cir.setReturnValue(FormManager.getCurrentFormItem(player));
                        return;
                    }
                    cir.setReturnValue(stack);
                });
            }
        }
    }
}
