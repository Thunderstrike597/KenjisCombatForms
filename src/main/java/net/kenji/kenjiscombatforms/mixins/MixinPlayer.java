package net.kenji.kenjiscombatforms.mixins;


import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;

import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = Player.class, priority = 500)
public class MixinPlayer {

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if(livingEntity instanceof Player player) {

            int selectedSlot = player.getInventory().selected;
            ItemStack stack = player.getInventory().getSelected();


            ItemStack lastStack = FormManager.getLastTrueStackOr(player, ItemStack.EMPTY);
            int lastSelected = FormManager.getLastSelectedOr(player, selectedSlot);


            boolean slotChanged = selectedSlot != lastSelected;

            // Always track the slot, regardless of category
            FormManager.setLastSelected(player, selectedSlot);

            if (slotChanged) {
                // Snapshot what WAS in trueStackMap as the "last"
                FormManager.setLastStackMap(player, lastStack);
                // Now record the real item in the new slot
                FormManager.setTrueStackMap(player, stack);
            }

            if (!slotChanged) {
                FormManager.setTrueStackMap(player, stack);
            }
        }
    }

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if(equipmentSlot != EquipmentSlot.MAINHAND)return;

        if (livingEntity instanceof Player player) {
            PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
            if (patch == null || !patch.isEpicFightMode()) return;


            ItemStack stack = player.getInventory().getSelected();
            if (equipmentSlot == EquipmentSlot.MAINHAND) {
                if (FormManager.isHeldCategoryValid(player, stack)) {
                    boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(player.getUUID(), true);
                    if (isToggled) {
                        cir.setReturnValue(FormManager.getCurrentFormItem(player));
                    }
                    return;
                }

                cir.setReturnValue(stack);
            }
        }
    }
    @Inject(method = "setItemSlot", at = @At("HEAD"), cancellable = true)
    public void onUpdateHeldItem(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if(slot != EquipmentSlot.MAINHAND)return;
        ItemStack mainHandItem = self.getMainHandItem();
        if(stack == self.getOffhandItem() && (mainHandItem.getItem() instanceof BaseFistClass || mainHandItem.isEmpty())){
            ci.cancel();
        }
        CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(stack);

        if(stack.getItem() instanceof BaseFistClass || (itemCap.getWeaponCategory() instanceof CombatFormWeaponCategory))
            ci.cancel();
    }
}
