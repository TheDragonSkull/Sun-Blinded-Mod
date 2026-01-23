package net.thedragonskull.sunblinded.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SunglassesDyeCategory implements IRecipeCategory<SunglassesDyeJeiRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "sunglasses_dye");

    public static final RecipeType<SunglassesDyeJeiRecipe> TYPE =
            new RecipeType<>(UID, SunglassesDyeJeiRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public SunglassesDyeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 54);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.SUNGLASSES.get()));

        this.arrow = guiHelper.getRecipeArrow();
    }

    @Override
    public RecipeType<SunglassesDyeJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Dyeing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(SunglassesDyeJeiRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 60, 18);
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            SunglassesDyeJeiRecipe recipe,
            IFocusGroup focuses
    ) {
        builder.addSlot(RecipeIngredientRole.INPUT, 0, 18)
                .addItemStack(recipe.getInputGlasses());

        builder.addSlot(RecipeIngredientRole.INPUT, 18, 18)
                .addIngredients(recipe.getDyeIngredient());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18)
                .addItemStack(recipe.getOutput());

/*        CraftingGridHelper gridHelper = CraftingGridHelper.INSTANCE;

        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(Collections.singletonList(recipe.getInputGlasses())); // slot 0
        inputs.add(recipe.getDyeIngredient().getItems().length == 0
                ? Collections.emptyList()
                : Arrays.asList(recipe.getDyeIngredient().getItems())); // slot 1

        gridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, 2, 1);

        // Output
        gridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, Collections.singletonList(recipe.getOutput()));*/
    }
}
