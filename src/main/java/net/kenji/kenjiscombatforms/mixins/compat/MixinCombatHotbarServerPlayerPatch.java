package net.kenji.kenjiscombatforms.mixins.compat;

import com.google.common.collect.Multimap;
import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseCombatWeapon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.forgeevent.InnateSkillChangeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class MixinCombatHotbarServerPlayerPatch {
    @Unique
    private static Map<UUID, ItemStack> lastHeldItem = new HashMap<>();

    private static final UUID WEAPON_DAMAGE_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");


    @Inject(method = "updateHeldItem", at = @At("HEAD"), cancellable = true, remap = false)
    public void onUpdateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci) {
        ServerPlayerPatch self = (ServerPlayerPatch) (Object) this;

        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal().getUUID()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;
        ServerPlayer player = (ServerPlayer) self.getOriginal();

        ItemStack heldStack = FormManager.trueStackMap.getOrDefault(player.getUUID(), ItemStack.EMPTY);

        boolean fromIsCombatWeapon = fromCap != null && fromCap.getWeaponCategory() instanceof CombatFormWeaponCategory;
        boolean toIsCombatWeapon = toCap != null && toCap.getWeaponCategory() instanceof CombatFormWeaponCategory;


        Multimap<Attribute, AttributeModifier> modifiers =
                formItem.getItem().getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
        // Was fist, now switching to weapon → remove form modifiers
        if (fromIsCombatWeapon && !toIsCombatWeapon) {
            AttributeInstance instance = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (instance != null) {
                instance.removeModifier(WEAPON_DAMAGE_UUID);
            }
        }

        // Was weapon, now switching to fist → add form modifiers
        if (!fromIsCombatWeapon && toIsCombatWeapon) {

            if (heldStack.getItem() instanceof BaseCombatWeapon weapon) {

                AttributeModifier damageModifier = new AttributeModifier(
                        WEAPON_DAMAGE_UUID,
                        "weapon_damage_modifier",
                        weapon.getDamage(),
                        AttributeModifier.Operation.ADDITION
                );                AttributeInstance instance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if(instance != null && !instance.hasModifier(damageModifier)) {
                    instance.addTransientModifier(damageModifier);
                }
                /*modifiers.forEach((attribute, modifier) -> {
                    AttributeInstance instance = player.getAttribute(attribute);
                    if (instance == null) return;
                    // Remove first regardless — ensure no stale/conflicting modifier
                    instance.removeModifier(modifier.getId());
                    instance.addTransientModifier(modifier);
                });*/

            }
        }

        // Both fist (e.g. first-tick setup call) → ensure modifiers are applied
        if (fromIsCombatWeapon && toIsCombatWeapon) {
            if (heldStack.getItem() instanceof BaseCombatWeapon weapon) {
                AttributeModifier damageModifier = new AttributeModifier(
                        WEAPON_DAMAGE_UUID,
                        "weapon_damage_modifier",
                        weapon.getDamage(),
                        AttributeModifier.Operation.ADDITION
                );                AttributeInstance instance = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if (instance != null && !instance.hasModifier(damageModifier))
                    instance.addTransientModifier(damageModifier);

                /*modifiers.forEach((attribute, modifier) -> {
                    AttributeInstance instance = player.getAttribute(attribute);
                    if (instance != null && !instance.hasModifier(modifier)) {
                        instance.addTransientModifier(modifier);
                    }
                });*/
            }
        }
        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null) return;
        patch.getHoldingItemCapability(InteractionHand.MAIN_HAND).changeWeaponInnateSkill(patch, formItem);

    }


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
        player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
            int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
            ItemStack stack = handler.getStackInSlot(selectedSlot);
            if (!FormManager.isHeldCategoryValid(player, stack)) return;
            ItemStack lastItem = lastHeldItem.getOrDefault(player.getUUID(), player.getMainHandItem());
            if(lastItem.getItem() != patch.getOriginal().getMainHandItem().getItem() || FormManager.getLastForm(player) != FormManager.getCurrentForm(player)) {
                formCap.changeWeaponInnateSkill(patch, formItem);
                MinecraftForge.EVENT_BUS.post(new InnateSkillChangeEvent(self, from, fromCap, formItem, formCap, hand));

                self.modifyLivingMotionByCurrentItem();
            }
            FormManager.updateLastForm(player);
            lastHeldItem.put(player.getUUID(), player.getMainHandItem());
        });
    }

}
