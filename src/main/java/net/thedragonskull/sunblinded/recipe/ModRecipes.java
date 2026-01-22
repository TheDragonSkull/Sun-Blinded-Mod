package net.thedragonskull.sunblinded.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedragonskull.sunblinded.SunBlinded;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SunBlinded.MOD_ID);

    public static final Supplier<RecipeSerializer<SunglassesDyeRecipe>> SUNGLASSES_DYE_SERIALIZER =
            SERIALIZERS.register("sunglasses_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(SunglassesDyeRecipe::new));

    public static final Supplier<RecipeSerializer<SunglassesClearRecipe>> SUNGLASSES_CLEAR_SERIALIZER =
            SERIALIZERS.register("sunglasses_clear", ()
                    -> new SimpleCraftingRecipeSerializer<>(SunglassesClearRecipe::new));

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
