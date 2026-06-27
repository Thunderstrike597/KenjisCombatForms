package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFormWeaponCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = PatchedItemInHandLayer.class)
public class MixinPatchedItemInHandLayer {
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
        CapabilityItem itemCap = EpicFightCapabilities.getItemStackCapability(instance.getMainHandItem());
        if((itemCap != null && itemCap.getWeaponCategory() instanceof CombatFormWeaponCategory)) return player.getInventory().getSelected();
        return FormManager.getTrueStackOr(player, player.getInventory().getSelected());
    }

}
