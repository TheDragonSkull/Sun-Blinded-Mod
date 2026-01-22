package net.thedragonskull.sunblinded.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.item.ModItems;

public class SunglassesClearRecipe extends CustomRecipe {

    public SunglassesClearRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int sunglassesCount = 0;
        int bucketCount = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglassesCount++;
            }
            else if (stack.is(Items.WATER_BUCKET)) {
                bucketCount++;
            }
            else {
                return false;
            }
        }

        return sunglassesCount == 1 && bucketCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack sunglasses = ItemStack.EMPTY;
        ItemStack bucket = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglasses = stack;
            } else if (stack.is(Items.WATER_BUCKET)) {
                bucket = stack;
            }
        }

        if (sunglasses.isEmpty() || bucket.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!sunglasses.has(ModDataComponentTypes.COLOR)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = sunglasses.copy();
        result.setCount(1);

        result.remove(ModDataComponentTypes.COLOR);

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.WATER_BUCKET)) {
                remaining.set(i, new ItemStack(Items.BUCKET));
            }
        }

        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SUNGLASSES_CLEAR_SERIALIZER.get();
    }
}

