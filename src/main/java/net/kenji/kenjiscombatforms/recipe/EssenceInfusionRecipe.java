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

public class EssenceInfusionRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;


    public EssenceInfusionRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
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
        return inputItems.get(0).test(simpleContainer.getItem(0)) && inputItems.get(1).test(simpleContainer.getItem(2)) && inputItems.get(2).test(simpleContainer.getItem(3));
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
        return ModRecipes.CRISTAL_CHARGING_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EssenceInfusionRecipe>{
        public static Type INSTANCE = new Type();
        public static final String ID = "essence_infusion";
    }

    public static class Serializer implements RecipeSerializer<EssenceInfusionRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation resource = new ResourceLocation(KenjisCombatForms.MOD_ID, "essence_infusion");


        @Override
        public @NotNull EssenceInfusionRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            NonNullList<Ingredient> input = NonNullList.withSize(3, Ingredient.EMPTY);

            for (int i = 0; i < input.size(); i++) {
                input.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            ;

            return new EssenceInfusionRecipe(input, output, pRecipeId);
        }

        @Override
        public @Nullable EssenceInfusionRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            return new EssenceInfusionRecipe(inputs, output, pRecipeId);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull EssenceInfusionRecipe essenceInfusionRecipe) {
            toNetwork2(friendlyByteBuf, essenceInfusionRecipe);
        }

        public void toNetwork2(@NotNull FriendlyByteBuf pBuffer, @NotNull EssenceInfusionRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
