package net.kenji.kenjiscombatforms.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.util.TickTimer;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.recipe.ScrollFormingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ScrollFormingRecipe abilityInfusionRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80, 17).addIngredients(abilityInfusionRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 54, 26).addIngredients(abilityInfusionRecipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 106, 26).addIngredients(abilityInfusionRecipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80, 55).addItemStack(abilityInfusionRecipe.getResultItem(null));
    }

    @Override
    public void draw(ScrollFormingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

    }
}
