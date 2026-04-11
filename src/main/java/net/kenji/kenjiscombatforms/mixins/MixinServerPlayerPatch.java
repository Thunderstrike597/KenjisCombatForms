package net.kenji.kenjiscombatforms.mixins;

import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Collection;
import java.util.Objects;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class MixinServerPlayerPatch {


    @Inject(method = "updateHeldItem", at = @At("HEAD"), cancellable = true, remap = false)
    public void onUpdateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci) {
        ServerPlayerPatch self = (ServerPlayerPatch)(Object) this;

        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;

        ServerPlayer player = (ServerPlayer) self.getOriginal();

        boolean fromIsFist = fromCap.getWeaponCategory() == CapabilityItem.WeaponCategories.FIST
                || fromCap.getWeaponCategory() == CapabilityItem.WeaponCategories.NOT_WEAPON;
        boolean toIsFist   = toCap.getWeaponCategory() == CapabilityItem.WeaponCategories.FIST
                || toCap.getWeaponCategory() == CapabilityItem.WeaponCategories.NOT_WEAPON;

        Multimap<Attribute, AttributeModifier> modifiers =
                formItem.getItem().getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
        // Was fist, now switching to weapon → remove form modifiers
        if (fromIsFist && !toIsFist) {
            modifiers.forEach((attribute, modifier) -> {
                AttributeInstance instance = player.getAttribute(attribute);
                if (instance != null && instance.hasModifier(modifier)) {
                    instance.removeModifier(modifier.getId());
                }
            });
        }

        // Was weapon, now switching to fist → add form modifiers
        if (!fromIsFist && toIsFist) {
            modifiers.forEach((attribute, modifier) -> {
                AttributeInstance instance = player.getAttribute(attribute);
                if (instance == null) return;
                // Remove first regardless — ensure no stale/conflicting modifier
                instance.removeModifier(modifier.getId());
                instance.addTransientModifier(modifier);
            });
        }

        // Both fist (e.g. first-tick setup call) → ensure modifiers are applied
        if (fromIsFist && toIsFist) {
            modifiers.forEach((attribute, modifier) -> {
                AttributeInstance instance = player.getAttribute(attribute);
                if (instance != null && !instance.hasModifier(modifier)) {
                    instance.addTransientModifier(modifier);
                }
            });
        }
    }
}
