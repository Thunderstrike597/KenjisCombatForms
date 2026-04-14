package net.kenji.kenjiscombatforms.mixins;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCapability;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = CapabilityItem.class, remap = false)
public class MixinEpicFightFist {


    @Inject(method = "getLivingMotionModifier", at = @At("HEAD"), cancellable = true, remap = false)
    private void getCustomLivingMotion(LivingEntityPatch<?> patch, InteractionHand hand, CallbackInfoReturnable<Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> cir) {
        if(patch instanceof PlayerPatch<?> playerPatch) {
            String formName = FormManager.getInstance().getOrCreatePlayerFormData(playerPatch.getOriginal()).selectedForm;
            Form currentForm = FormManager.getInstance().getForm(formName);
            ItemStack formItem = currentForm.getFormItem(playerPatch.getOriginal().getUUID());
            if(formItem == null) return;
            CapabilityItem capItem = playerPatch.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            WeaponCategory category = capItem.getWeaponCategory();
            if(category != CapabilityItem.WeaponCategories.FIST && category != CapabilityItem.WeaponCategories.NOT_WEAPON) return;

            CapabilityItem formCapItem = EpicFightCapabilities.getItemStackCapability(formItem);

            if(formCapItem instanceof WeaponCapability weaponCapability){
                cir.setReturnValue(kenjiscombatforms$getLivingMotions(playerPatch, weaponCapability));
            }
        }
    }

    @Unique
    private Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> kenjiscombatforms$getLivingMotions(PlayerPatch<?> playerPatch, WeaponCapability weaponCapability) {
        WeaponCapabilityAccessor accessor = (WeaponCapabilityAccessor) weaponCapability;

        Map<Style, Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> allMotions = accessor.accessLivingMotions();

        Style style = weaponCapability.getStyle(playerPatch);

        // DEBUG (you should log this)
        //System.out.println("Style: " + style);

        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> styleMotions = allMotions.get(style);
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> commonMotions = allMotions.get(CapabilityItem.Styles.COMMON);

        // Always create a NEW map (avoid mutating internal data)
        Map<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> result = Maps.newHashMap();

        if (commonMotions != null) result.putAll(commonMotions);
        if (styleMotions != null) result.putAll(styleMotions);

        return result;
    }
}