package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.CombatModeHandler;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = PatchedItemInHandLayer.class)
public class MixinCombatHotbarPatchedItemInHandLayer {
    @Redirect(
            method = "renderLayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getMainHandItem()Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            )
    )
    public ItemStack getAltMainHandItem(LivingEntity instance){
        if(!(instance instanceof Player player)) return instance.getMainHandItem();
        AtomicReference<ItemStack> finalStack = new AtomicReference<>(player.getInventory().getSelected());

        if (CombatModeHandler.isInBattleMode(player)) {
            player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
                int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
                ItemStack stack = handler.getStackInSlot(selectedSlot);
                CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(instance.getMainHandItem());

                finalStack.set(stack);
            });
        }
        return finalStack.get();
    }

}
