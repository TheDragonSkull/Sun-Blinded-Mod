package net.thedragonskull.sunblinded.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.ModItems;

@JeiPlugin
public class JEISunblindedPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SunBlinded.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new SunglassesDyeCategory(guiHelper)
        );
        registration.addRecipeCategories(
                new SunglassesClearCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
                SunglassesDyeCategory.TYPE,
                JEIRecipeFactory.createSunglassesDyeRecipes()
        );
        registration.addRecipes(
                SunglassesClearCategory.TYPE,
                JEIRecipeFactory.createSunglassesClearRecipes()
        );
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(
                ModItems.SUNGLASSES.get(),
                (stack, context) -> {
                    if (stack.hasTag() && stack.getTag().contains("color")) {
                        return stack.getTag().getString("color");
                    }
                    return "classic";
                }
        );
    }

}
