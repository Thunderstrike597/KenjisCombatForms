package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.handlers.ControlHandler;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.client.particle.AnimationTrailParticle;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = AnimationTrailParticle.Provider.class)
public class MixinAnimationHotbarTrailParticle {

    @Redirect(
            method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            )
    )
    public ItemStack onCreateParticle(LivingEntity instance, InteractionHand pHand){
        if(!(instance instanceof Player player)) return instance.getMainHandItem();

        AtomicReference<ItemStack> finalStack = new AtomicReference<>(instance.getMainHandItem());
        player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
            int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
            ItemStack stack = handler.getStackInSlot(selectedSlot);
            CapabilityItem capItem = EpicFightCapabilities.getItemStackCapability(stack);
            if (FormManager.isHeldCategoryValid(player, stack) && capItem.getWeaponCategory() instanceof CombatFormWeaponCategory) {
                boolean isToggled = ControlHandler.toggleHandCombatMap.getOrDefault(player.getUUID(), true);
                if (isToggled) {
                    finalStack.set(stack);
                }
            }
        });
        return finalStack.get();
    }
}
