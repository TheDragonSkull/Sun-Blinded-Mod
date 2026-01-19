package net.thedragonskull.sunblinded.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Internal;
import mezz.jei.library.gui.helpers.CraftingGridHelper;
import mezz.jei.library.gui.recipes.ShapelessIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.ModItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SunglassesClearCategory implements IRecipeCategory<SunglassesClearJeiRecipe> {
    public static final ResourceLocation UID =
            new ResourceLocation(SunBlinded.MOD_ID, "sunglasses_clear");

    public static final RecipeType<SunglassesClearJeiRecipe> TYPE =
            new RecipeType<>(UID, SunglassesClearJeiRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final ShapelessIcon shapelessIcon;

    public SunglassesClearCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(120, 54);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.SUNGLASSES.get()));

        this.arrow = guiHelper.getRecipeArrow();

        this.shapelessIcon = new ShapelessIcon(
                Internal.getTextures().getShapelessIcon(),
                110, 0
        );
    }

    @Override
    public RecipeType<SunglassesClearJeiRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Clearing");
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
    public void draw(SunglassesClearJeiRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {

        arrow.draw(guiGraphics, 60, 18);
        shapelessIcon.draw(guiGraphics);

        if (shapelessIcon.isMouseOver((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    Component.translatable("jei.tooltip.shapeless.recipe"),
                    (int) mouseX,
                    (int) mouseY
            );
        }
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            SunglassesClearJeiRecipe recipe,
            IFocusGroup focuses
    ) {
        CraftingGridHelper gridHelper = CraftingGridHelper.INSTANCE;

        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(Collections.singletonList(recipe.getInputGlasses())); // slot 0: colored sunglasses
        inputs.add(Collections.singletonList(recipe.getWaterBucket()));   // slot 1: water bucket

        // Grid 2x1 (dos ingredientes, horizontal)
        gridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, 2, 1);

        // Output
        gridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, Collections.singletonList(recipe.getOutput()));
    }
}
