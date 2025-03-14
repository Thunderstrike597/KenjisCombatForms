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
import net.kenji.kenjiscombatforms.recipe.EssenceInfusionRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EssenceInfusionCategory implements IRecipeCategory<EssenceInfusionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(KenjisCombatForms.MOD_ID, "essence_infusion");
    public static final ResourceLocation TEXTURE = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/essence_infusing_menu.png");
    public static final ResourceLocation ARROW = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/essence_infusing_menu.png");

    public static final RecipeType<EssenceInfusionRecipe> ESSENCE_INFUSION_TYPE = new RecipeType<>(UID, EssenceInfusionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public EssenceInfusionCategory(IGuiHelper helper) {

        IDrawableStatic iStatic = new IDrawableStatic() {
            @Override
            public void draw(GuiGraphics guiGraphics, int i, int i1, int i2, int i3, int i4, int i5) {
                guiGraphics.blit(ARROW,85 , 30, 176, 0, 8, tickTimer.getValue());
            }


            TickTimer tickTimer = new TickTimer(40, 40, false);

            @Override
            public int getWidth() {
                return 7;
            }

            @Override
            public int getHeight() {
                return 26;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int i, int i1) {

            }
        };

        this.background = helper.createDrawable(TEXTURE, 0, 0 , 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SCROLL_FORMING_STATION.get()));
        //this.arrow = helper.createDrawable(ARROW, 176,0, 7, 26);
        this.arrow = helper.createAnimatedDrawable(iStatic, 1, IDrawableAnimated.StartDirection.TOP, false);

    }

    @Override
    public RecipeType<EssenceInfusionRecipe> getRecipeType() {
        return ESSENCE_INFUSION_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.kenjiscombatforms.essence_infusion_station");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    public IDrawable getArrow() {
        return this.arrow;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, EssenceInfusionRecipe essenceInfusionRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(essenceInfusionRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 33, 19).addIngredients(essenceInfusionRecipe.getIngredients().get(1));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 33, 39).addIngredients(essenceInfusionRecipe.getIngredients().get(2));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(essenceInfusionRecipe.getResultItem(null));
    }

    @Override
    public void draw(EssenceInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 2, 2);
    }
}
