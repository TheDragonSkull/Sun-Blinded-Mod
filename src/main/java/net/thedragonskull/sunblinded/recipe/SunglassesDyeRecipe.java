package net.thedragonskull.sunblinded.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.thedragonskull.sunblinded.item.ModItems;

public class SunglassesDyeRecipe extends CustomRecipe {

    public SunglassesDyeRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int sunglassesCount = 0;
        int dyeCount = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack sunglasses = ItemStack.EMPTY;
        ItemStack dye = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglasses = stack;
            } else if (stack.is(Tags.Items.DYES)) {
                dye = stack;
            }
        }

        ItemStack result = sunglasses.copy();
        result.setCount(1);

        String color = getColorFromDye(dye);
        result.getOrCreateTag().putString("color", color);

        if (sunglasses.hasTag() && sunglasses.getTag().contains("color")) return ItemStack.EMPTY;

        return result;
    }

    private String getColorFromDye(ItemStack dye) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(dye.getItem());
        return id.getPath().replace("_dye", "");
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
