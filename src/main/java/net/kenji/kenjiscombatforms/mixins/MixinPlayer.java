package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class)
public class MixinPlayer {


    @Inject(method = "getItemBySlot", at = @At("RETURN"), cancellable = true)
    private void maybeReplaceGetItemBySlot(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir) {
        Player player = (Player) (Object)this;
        if(equipmentSlot != EquipmentSlot.MAINHAND) return;

        Form currentForm = FormManager.getCurrentForm(player);
        ItemStack currentFormItem = FormManager.getCurrentFormItem(player);
        if(currentForm == null || currentFormItem.isEmpty()) return;
        if(!FormManager.isHeldCategoryValid(player)) return;
        ItemStack originalReturnValue = cir.getReturnValue();

        cir.setReturnValue(currentFormItem);
    }
    @Inject(method = "setItemSlot", at = @At("HEAD"), cancellable = true)
    public void onUpdateHeldItem(EquipmentSlot par1, ItemStack par2, CallbackInfo ci) {
        Player self = (Player) (Object) this;

        if(par2.getItem() instanceof BaseFistClass)
            ci.cancel();
    }
}
