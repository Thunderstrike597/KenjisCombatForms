package net.kenji.kenjiscombatforms.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.recipe.ScrollFormingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ScrollFormingCategory implements IRecipeCategory<ScrollFormingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(KenjisCombatForms.MOD_ID, "scroll_forming");
    public static final ResourceLocation TEXTURE = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/menus/scroll_forming_menu.png");

    public static final RecipeType<ScrollFormingRecipe> SCROLL_FORMING_TYPE = new RecipeType<>(UID, ScrollFormingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ScrollFormingCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(TEXTURE, 0, 0 , 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SCROLL_FORMING_STATION.get()));
    }

    @Override
    public RecipeType<ScrollFormingRecipe> getRecipeType() {
        return SCROLL_FORMING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.kenjiscombatforms.scroll_forming_table");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }



    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ScrollFormingRecipe recipe, IFocusGroup focuses) {
        int[] counts = recipe.getIngredientCounts(); // your count array

        addCountedSlot(builder, recipe, 0,  80, 17, counts[0]);
        addCountedSlot(builder, recipe, 1,  54, 26, counts[1]);
        addCountedSlot(builder, recipe, 2, 106, 26, counts[2]);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 55)
                .addItemStack(recipe.getResultItem(null));
    }

    private void addCountedSlot(IRecipeLayoutBuilder builder, ScrollFormingRecipe recipe,
                                int ingredientIndex, int x, int y, int count) {
        ItemStack[] stacks = recipe.getIngredients().get(ingredientIndex).getItems();

        if (stacks.length == 0) {
            // Fallback: add the ingredient directly so JEI doesn't choke
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredients(recipe.getIngredients().get(ingredientIndex));
            return;
        }

        List<ItemStack> counted = Arrays.stream(stacks)
                .map(s -> { ItemStack copy = s.copy(); copy.setCount(count); return copy; })
                .toList();

        builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addItemStacks(counted);
    }


    @Override
    public void draw(ScrollFormingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

    }
}
