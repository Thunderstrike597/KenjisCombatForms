package net.kenji.kenjiscombatforms.screen.Slots.scroll_forming_slots;

import net.kenji.kenjiscombatforms.block.entity.ScrollFormingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;


public class ScrollOutputSlot extends SlotItemHandler {
    private final ScrollFormingBlockEntity blockEntity;
    public ScrollOutputSlot(ScrollFormingBlockEntity blockEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        blockEntity.craftItem();
        super.onTake(player, itemStack);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
