package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = LivingEntityPatch.class, remap = false)
public class MixinLivingEntityPatchCombatHotbar {


    @Inject(method = "getAdvancedHoldingItemStack", at = @At("RETURN"), cancellable = true)
    private void maybeReplaceGetItemBySlot(InteractionHand hand, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntityPatch<?> self = (LivingEntityPatch<?>) (Object) this;
        if(self instanceof PlayerPatch<?> playerPatch) {
            if(cir.getReturnValue() == null)return;
            if (hand == InteractionHand.MAIN_HAND) {
                playerPatch.getOriginal().getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
                    int selectedSlot = HotbarSlotHandler.getSelectedSlot(playerPatch.getOriginal());
                    ItemStack stack = handler.getStackInSlot(selectedSlot);
                    CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(stack);
                    if (FormManager.isHeldCategoryValid(playerPatch.getOriginal(), stack) && capItem.getWeaponCategory() instanceof CombatFormWeaponCategory) {
                        boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(playerPatch.getOriginal().getUUID(), true);
                        if (isToggled) {
                            cir.setReturnValue(stack);
                        }
                    }
                });
            }
        }
    }

}
