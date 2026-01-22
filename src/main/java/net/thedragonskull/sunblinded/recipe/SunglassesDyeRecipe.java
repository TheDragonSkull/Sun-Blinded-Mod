package net.thedragonskull.sunblinded.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.item.ModItems;

public class SunglassesDyeRecipe extends CustomRecipe {

    public SunglassesDyeRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int sunglassesCount = 0;
        int dyeCount = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglassesCount++;
            }
            else if (stack.is(Tags.Items.DYES)) {
                dyeCount++;
            }
            else {
                return false;
            }
        }

        return sunglassesCount == 1 && dyeCount == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack sunglasses = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglasses = stack;
            } else if (stack.is(Tags.Items.DYES)) {
                dye = stack;
            }
        }

        ItemStack result = sunglasses.copy();
        result.setCount(1);

        String color = getColorFromDye(dye);
        result.set(ModDataComponentTypes.COLOR.get(), color);

        if (sunglasses.has(ModDataComponentTypes.COLOR)) {
            return ItemStack.EMPTY;
        }

        return result;
    }

    private String getColorFromDye(ItemStack dye) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(dye.getItem());
        return id.getPath().replace("_dye", "");
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SUNGLASSES_DYE_SERIALIZER.get();
    }
}
