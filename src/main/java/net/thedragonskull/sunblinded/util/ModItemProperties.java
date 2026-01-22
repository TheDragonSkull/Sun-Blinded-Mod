package net.thedragonskull.sunblinded.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.item.ModItems;

public class ModItemProperties {

    public static void addCustomItemProperties() {
        sunglassesColor(ModItems.SUNGLASSES.get());
    }

    private static void sunglassesColor(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("color"), (itemStack, level, livingEntity, var) -> {

            String color = itemStack.get(ModDataComponentTypes.COLOR.get());
            if (color == null) return -1.0F;

            return SunglassesUtils.sunglassesModel(color);
        });
    }

}
