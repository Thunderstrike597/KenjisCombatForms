package net.kenji.kenjiscombatforms.screen.Slots;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;


public class TemplateSlot extends SlotItemHandler {
    public TemplateSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    TagKey<Item> formTemplatesTag = ItemTags.create(new ResourceLocation("kenjiscombatforms", "formtemplates"));

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(formTemplatesTag);
    }
}
