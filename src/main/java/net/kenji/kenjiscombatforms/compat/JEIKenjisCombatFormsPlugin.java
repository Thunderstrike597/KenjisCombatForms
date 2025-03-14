package net.kenji.kenjiscombatforms.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.recipe.ScrollFormingRecipe;
import net.kenji.kenjiscombatforms.recipe.EssenceInfusionRecipe;
import net.kenji.kenjiscombatforms.screen.ScrollFormingScreen;
import net.kenji.kenjiscombatforms.screen.EssenceInfusingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@JeiPlugin
public class JEIKenjisCombatFormsPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EssenceInfusionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ScrollFormingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<EssenceInfusionRecipe> infusionRecipes = recipeManager.getAllRecipesFor(EssenceInfusionRecipe.Type.INSTANCE);
        registration.addRecipes(EssenceInfusionCategory.ESSENCE_INFUSION_TYPE, infusionRecipes);

        List<ScrollFormingRecipe> scrollFormingRecipes = recipeManager.getAllRecipesFor(ScrollFormingRecipe.Type.INSTANCE);
        registration.addRecipes(ScrollFormingCategory.SCROLL_FORMING_TYPE, scrollFormingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EssenceInfusingScreen.class, 60, 30, 20, 30,
                EssenceInfusionCategory.ESSENCE_INFUSION_TYPE);
        registration.addRecipeClickArea(ScrollFormingScreen.class, 60, 30, 20, 30,
                ScrollFormingCategory.SCROLL_FORMING_TYPE);

    }


}
