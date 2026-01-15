package net.thedragonskull.sunblinded.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.thedragonskull.sunblinded.SunBlinded;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SunBlinded.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SunglassesDyeRecipe>> SUNGLASSES_DYE_SERIALIZER =
            SERIALIZERS.register("sunglasses_dye", () -> SunglassesDyeRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<SunglassesClearRecipe>> SUNGLASSES_CLEAR_SERIALIZER =
            SERIALIZERS.register("sunglasses_clear", () -> SunglassesClearRecipe.Serializer.INSTANCE);

/*    public static final RegistryObject<RecipeSerializer<?>> SUNGLASSES_DYE =
            SERIALIZERS.register(
                    "sunglasses_dye",
                    () -> new SimpleCraftingRecipeSerializer<>(SunglassesDyeRecipe::new)
            );

    public static final RegistryObject<RecipeSerializer<?>> SUNGLASSES_CLEAR =
            SERIALIZERS.register(
                    "sunglasses_clear",
                    () -> new SimpleCraftingRecipeSerializer<>(SunglassesClearRecipe::new)
            );*/

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
