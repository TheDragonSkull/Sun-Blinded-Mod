package net.thedragonskull.sunblinded.compat;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SunglassesDyeJeiRecipe {
    private final ItemStack inputGlasses;
    private final Ingredient dyeIngredient;
    private final ItemStack output;

    public SunglassesDyeJeiRecipe(ItemStack inputGlasses, Ingredient dyeIngredient, ItemStack output) {
        this.inputGlasses = inputGlasses;
        this.dyeIngredient = dyeIngredient;
        this.output = output;
    }

    public ItemStack getInputGlasses() {
        return inputGlasses;
    }

    public Ingredient getDyeIngredient() {
        return dyeIngredient;
    }

    public ItemStack getOutput() {
        return output;
    }
}
