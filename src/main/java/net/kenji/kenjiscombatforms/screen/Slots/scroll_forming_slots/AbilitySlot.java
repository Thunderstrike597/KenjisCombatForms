package net.kenji.kenjiscombatforms.screen.Slots.scroll_forming_slots;

import net.kenji.kenjiscombatforms.block.entity.ScrollFormingBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;


public class AbilitySlot extends SlotItemHandler {
    private final ScrollFormingBlockEntity blockEntity;
    public AbilitySlot(ScrollFormingBlockEntity blockEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
       blockEntity.removeCraftItem();
        super.onTake(player, itemStack);
    }

    TagKey<Item> essenceItemsTag = ItemTags.create(new ResourceLocation("kenjiscombatforms", "ability_items"));

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(essenceItemsTag);
    }
}
