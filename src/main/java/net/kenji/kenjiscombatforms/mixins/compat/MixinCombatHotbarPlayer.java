package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.CombatModeHandler;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.epic_fight_combat_hotbar.mixins.PlayerMixin;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = Player.class, priority = 1)
public class MixinCombatHotbarPlayer {



    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if(livingEntity instanceof Player player) {

            player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
                int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
                ItemStack stack = handler.getStackInSlot(selectedSlot);


                ItemStack lastStack = FormManager.trueStackMap.getOrDefault(player.getUUID(), ItemStack.EMPTY);
                int lastSelected = FormManager.lastSelectedMap.getOrDefault(player.getUUID(), selectedSlot);


                boolean slotChanged = selectedSlot != lastSelected;

                // Always track the slot, regardless of category
                FormManager.lastSelectedMap.put(player.getUUID(), selectedSlot);

                if (slotChanged) {
                    // Snapshot what WAS in trueStackMap as the "last"
                    FormManager.trueLastStackMap.put(player.getUUID(), lastStack);
                    // Now record the real item in the new slot
                    FormManager.trueStackMap.put(player.getUUID(), stack);
                }

                if(!slotChanged) {
                    FormManager.trueStackMap.put(player.getUUID(), stack);
                }
            });
        }
    }

    @Inject(method = "getItemBySlot", at = @At("HEAD"), cancellable = true)
    private void maybeReplaceGetItemBySlot(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if(livingEntity instanceof Player player) {
            if (!CombatModeHandler.isInBattleMode(player)) {
                return;
            }


            player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
                int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
                ItemStack stack = handler.getStackInSlot(selectedSlot);
                if(equipmentSlot == EquipmentSlot.MAINHAND) {
                    if(FormManager.isHeldCategoryValid(player, stack)) {
                        boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(player.getUUID(), true);
                        if(isToggled) {
                            cir.setReturnValue(FormManager.getCurrentFormItem(player));
                        }
                        return;
                    }

                    // Non-valid category: make sure trueStackMap reflects reality
                    // but only if the slot didn't already handle it above

                    cir.setReturnValue(stack);
                }
            });
        }
    }
    @Inject(method = "setItemSlot", at = @At("HEAD"), cancellable = true)
    public void onUpdateHeldItem(EquipmentSlot par1, ItemStack par2, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(self.getMainHandItem());

        if(par2.getItem() instanceof BaseFistClass || (itemCap.getWeaponCategory() instanceof CombatFormWeaponCategory)) {
            Log.info("Logging Set item By Slot!");
            ci.cancel();
        }
    }
}
