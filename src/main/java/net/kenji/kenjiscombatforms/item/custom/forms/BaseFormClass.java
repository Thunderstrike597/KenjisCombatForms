package net.kenji.kenjiscombatforms.item.custom.forms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import yesman.epicfight.world.item.WeaponItem;

public class BaseFormClass extends WeaponItem {
    public BaseFormClass(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }
}
