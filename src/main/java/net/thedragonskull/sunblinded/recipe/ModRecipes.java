package net.thedragonskull.sunblinded.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.thedragonskull.sunblinded.SunBlinded;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SunBlinded.MOD_ID);

    public static final RegistryObject<RecipeSerializer<?>> SUNGLASSES_DYE =
            RECIPES.register(
                    "sunglasses_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(SunglassesDyeRecipe::new)
            );

    public static final RegistryObject<RecipeSerializer<?>> SUNGLASSES_CLEAR =
            RECIPES.register(
                    "sunglasses_clear",
                    () -> new SimpleCraftingRecipeSerializer<>(SunglassesClearRecipe::new)
            );

    public static void register(IEventBus eventBus) {
        RECIPES.register(eventBus);
    }
}
