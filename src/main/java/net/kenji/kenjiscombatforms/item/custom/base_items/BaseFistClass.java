package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import yesman.epicfight.world.item.WeaponItem;

public class BaseFistClass extends WeaponItem {
    public BaseFistClass() {
        super(Tiers.STONE, 1,  1, new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return false;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
       // player.setForcedPose(Pose.STANDING);
        //player.setPose(Pose.STANDING);
        player.calculateEntityAnimation(false);

        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

}
