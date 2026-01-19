package net.thedragonskull.sunblinded.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class SunglassesClearRecipe extends CustomRecipe {

    public SunglassesClearRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) {
        int sunglassesCount = 0;
        int bucketCount = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
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
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack sunglasses = ItemStack.EMPTY;
        ItemStack bucket = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.SUNGLASSES.get())) {
                sunglasses = stack;
            } else if (stack.is(Items.WATER_BUCKET)) {
                bucket = stack;
            }
        }

        if (sunglasses.isEmpty() || bucket.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!sunglasses.hasTag() || !sunglasses.getTag().contains("color")) {
            return ItemStack.EMPTY;
        }

        ItemStack result = sunglasses.copy();
        result.setCount(1);

        result.removeTagKey("color");

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
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
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<SunglassesClearRecipe> {
        public static final SunglassesClearRecipe.Serializer INSTANCE = new SunglassesClearRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(SunBlinded.MOD_ID, "sunglasses_clear");

        @Override
        public SunglassesClearRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new SunglassesClearRecipe(pRecipeId, CraftingBookCategory.MISC);
        }

        @Override
        public @Nullable SunglassesClearRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new SunglassesClearRecipe(pRecipeId, CraftingBookCategory.MISC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SunglassesClearRecipe pRecipe) {

        }
    }
}

