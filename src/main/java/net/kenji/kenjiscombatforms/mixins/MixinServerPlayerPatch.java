package net.kenji.kenjiscombatforms.mixins;

import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.forgeevent.InnateSkillChangeEvent;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class MixinServerPlayerPatch {
    @Unique
    private static Map<UUID, ItemStack> lastHeldItem = new HashMap<>();


   /* @Inject(method = "updateHeldItem", at = @At("HEAD"), cancellable = true, remap = false)
    public void onUpdateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci) {
        ServerPlayerPatch self = (ServerPlayerPatch) (Object) this;

        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;

        ServerPlayer player = (ServerPlayer) self.getOriginal();

        boolean fromIsFist = fromCap.getWeaponCategory() == CapabilityItem.WeaponCategories.FIST
                || fromCap.getWeaponCategory() == CapabilityItem.WeaponCategories.NOT_WEAPON;
        boolean toIsFist = toCap.getWeaponCategory() == CapabilityItem.WeaponCategories.FIST
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
        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null) return;
        patch.getHoldingItemCapability(InteractionHand.MAIN_HAND).changeWeaponInnateSkill(patch, formItem);

    }*/

    @Inject(method = "updateHeldItem", at = @At("TAIL"), cancellable = true, remap = false)
    public void onUpdateHeldItemTail(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci) {
        ServerPlayerPatch self = (ServerPlayerPatch) (Object) this;

        if(hand != InteractionHand.MAIN_HAND) return;
        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal().getUUID()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;
        CapabilityItem formCap = EpicFightCapabilities.getItemStackCapability(formItem);
        ServerPlayer player = (ServerPlayer) self.getOriginal();

        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null) return;

        if(!FormManager.isHeldCategoryValid(player, player.getInventory().getSelected())) return;
        ItemStack lastItem = lastHeldItem.getOrDefault(player.getUUID(), player.getMainHandItem());
        if(lastItem.getItem() != patch.getOriginal().getMainHandItem().getItem() || FormManager.getLastForm(player) != FormManager.getCurrentForm(player)) {

            formCap.changeWeaponInnateSkill(patch, formItem);
            MinecraftForge.EVENT_BUS.post(new InnateSkillChangeEvent(self, from, fromCap, formItem, formCap, hand));

            self.modifyLivingMotionByCurrentItem();
        }
        FormManager.updateLastForm(player);
        lastHeldItem.put(player.getUUID(), player.getMainHandItem());
    }
}
