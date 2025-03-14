package net.kenji.kenjiscombatforms.item.custom.base_items;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BaseFistClass extends Item {
    public BaseFistClass(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return false;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        player.setForcedPose(Pose.STANDING);
        player.setPose(Pose.STANDING);
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
