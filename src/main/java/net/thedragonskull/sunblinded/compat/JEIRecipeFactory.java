package net.thedragonskull.sunblinded.compat;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeFactory {

    public static List<SunglassesDyeJeiRecipe> createSunglassesDyeRecipes() {
        List<SunglassesDyeJeiRecipe> recipes = new ArrayList<>();

        ItemStack baseGlasses = new ItemStack(ModItems.SUNGLASSES.get());

        for (DyeColor color : DyeColor.values()) {
            Item dyeItem = DyeItem.byColor(color);
            Ingredient dye = Ingredient.of(dyeItem);

            ItemStack output = new ItemStack(ModItems.SUNGLASSES.get());
            output.set(ModDataComponentTypes.COLOR, color.getName());

            recipes.add(new SunglassesDyeJeiRecipe(
                    baseGlasses,
                    dye,
                    output
            ));
        }

        return recipes;
    }

    public static List<SunglassesClearJeiRecipe> createSunglassesClearRecipes() {
        List<SunglassesClearJeiRecipe> recipes = new ArrayList<>();

        ItemStack baseGlasses = new ItemStack(ModItems.SUNGLASSES.get());
        ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);

        for (DyeColor color : DyeColor.values()) {
            ItemStack input = baseGlasses.copy();
            input.set(ModDataComponentTypes.COLOR, color.getName());

            ItemStack output = baseGlasses.copy();

            recipes.add(new SunglassesClearJeiRecipe(input, waterBucket, output));
        }

        return recipes;
    }
}

