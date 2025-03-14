package net.kenji.kenjiscombatforms.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScrollFormingRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final List<Integer> counts;
    private final ItemStack output;
    private final ResourceLocation id;


    public ScrollFormingRecipe(NonNullList<Ingredient> inputItems, List<Integer> counts, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.counts = counts;
        this.id = id;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        for (int i = 0; i < inputItems.size(); i++) {
            Ingredient ingredient = inputItems.get(i);
            int requiredCount = counts.get(i);
            ItemStack stack = simpleContainer.getItem(getSlotForIndex(i));

            if (!ingredient.test(stack) || stack.getCount() < requiredCount) {
                return false;
            }
        }
        return true;
    }

    private int getSlotForIndex(int index) {
        // Adjust this method based on your actual slot layout
        switch (index) {
            case 0: return 0;
            case 1: return 2;
            case 2: return 3;
            default: throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    public List<Integer> getCounts() {
        return counts;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer, @NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.SCROLL_FORMING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ScrollFormingRecipe>{
        public static Type INSTANCE = new Type();
        public static final String ID = "scroll_forming";
    }

    public static class Serializer implements RecipeSerializer<ScrollFormingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "scroll_forming");


        @Override
        public @NotNull ScrollFormingRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(3, Ingredient.EMPTY);
            List<Integer> counts = new ArrayList<>();

            for (int i = 0; i < inputs.size(); i++) {
                JsonObject ingredientJson = ingredients.get(i).getAsJsonObject();
                inputs.set(i, Ingredient.fromJson(ingredientJson));
                counts.add(GsonHelper.getAsInt(ingredientJson, "count", 1));
            }

            return new ScrollFormingRecipe(inputs, counts, output, pRecipeId);
        }

        @Override
        public @Nullable ScrollFormingRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            List<Integer> counts = new ArrayList<>();

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
                counts.add(pBuffer.readInt());
            }

            ItemStack output = pBuffer.readItem();
            return new ScrollFormingRecipe(inputs, counts, output, pRecipeId);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull ScrollFormingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for (int i = 0; i < pRecipe.getIngredients().size(); i++) {
                pRecipe.getIngredients().get(i).toNetwork(pBuffer);
                pBuffer.writeInt(pRecipe.getCounts().get(i));
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}