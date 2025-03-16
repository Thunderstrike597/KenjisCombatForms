package net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseSwiftClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwiftFistItem extends BaseSwiftClass {
    private static SwiftFistItem INSTANCE;


    public SwiftFistItem(Properties properties) {
        super(properties);
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static SwiftFistItem getInstance(){
        return INSTANCE;
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        Player player = (Player)entity;


        super.inventoryTick(itemStack, level, entity, i, b);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.void_fist1.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return super.getDefaultTooltipHideFlags(stack);
    }


    public void setFormMainHand(Player player, int slot){
        player.getInventory().setItem(slot, this.getDefaultInstance());
    }


    private boolean isValidReplaceItem(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.isEmpty() || mainHandItem.is(Items.AIR) || mainHandItem.getItem() instanceof BaseFistClass &&
                !(mainHandItem.getItem() instanceof SwiftFistItem);
    }

}

