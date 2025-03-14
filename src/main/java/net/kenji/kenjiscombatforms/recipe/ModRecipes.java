package net.kenji.kenjiscombatforms.recipe;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIAlIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EssenceInfusionRecipe>> CRISTAL_CHARGING_SERIALIZER =
            SERIAlIZERS.register("essence_infusion", () -> EssenceInfusionRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<ScrollFormingRecipe>> SCROLL_FORMING_SERIALIZER =
            SERIAlIZERS.register("scroll_forming", () -> ScrollFormingRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIAlIZERS.register(eventBus);
    }
}
