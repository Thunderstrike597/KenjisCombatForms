package net.kenji.kenjiscombatforms.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;

@Mixin(value = PatchedItemInHandLayer.class, remap = false)
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

        return player.getInventory().getSelected();

    }

}
