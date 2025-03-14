package net.kenji.kenjiscombatforms.item.custom.form_essence;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerEssenceTier2 extends BaseComponent {
    public PowerEssenceTier2(Properties cProperties) {
        super(cProperties.stacksTo(4));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.tier2_power_essence.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}