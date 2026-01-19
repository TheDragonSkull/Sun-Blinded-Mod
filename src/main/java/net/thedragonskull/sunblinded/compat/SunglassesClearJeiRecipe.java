package net.thedragonskull.sunblinded.compat;

import net.minecraft.world.item.ItemStack;

public class SunglassesClearJeiRecipe {
    private final ItemStack inputGlasses;
    private final ItemStack waterBucket;
    private final ItemStack output;

    public SunglassesClearJeiRecipe(ItemStack inputGlasses, ItemStack waterBucket, ItemStack output) {
        this.inputGlasses = inputGlasses;
        this.waterBucket = waterBucket;
        this.output = output;
    }

    public ItemStack getInputGlasses() {
        return inputGlasses;
    }

    public ItemStack getWaterBucket() {
        return waterBucket;
    }

    public ItemStack getOutput() {
        return output;
    }
}

