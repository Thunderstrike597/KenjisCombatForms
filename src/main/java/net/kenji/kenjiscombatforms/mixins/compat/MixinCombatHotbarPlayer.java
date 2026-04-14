package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.CombatModeHandler;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
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

@Mixin(value = Player.class)
public class MixinCombatHotbarPlayer {


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
                        cir.setReturnValue(FormManager.getCurrentFormItem(player));
                        return;
                    }
                    cir.setReturnValue(stack);
                }
            });
        }
    }
    @Inject(method = "setItemSlot", at = @At("HEAD"), cancellable = true)
    public void onUpdateHeldItem(EquipmentSlot par1, ItemStack par2, CallbackInfo ci) {
        Player self = (Player) (Object) this;

        if(par2.getItem() instanceof BaseFistClass)
            ci.cancel();
    }
}
