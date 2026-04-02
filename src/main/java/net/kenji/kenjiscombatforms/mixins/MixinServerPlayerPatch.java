package net.kenji.kenjiscombatforms.mixins;

import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Objects;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class MixinServerPlayerPatch {

    @Inject(method = "updateHeldItem", at = @At("HEAD"), cancellable = true, remap = false)
    public void onUpdateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci){
        ServerPlayerPatch self = (ServerPlayerPatch) (Object)this;


        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if(formItem == null) return;
        CapabilityItem capItem = self.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        WeaponCategory category = capItem.getWeaponCategory();
        if(category != CapabilityItem.WeaponCategories.FIST && category != CapabilityItem.WeaponCategories.NOT_WEAPON) return;

    }
}
