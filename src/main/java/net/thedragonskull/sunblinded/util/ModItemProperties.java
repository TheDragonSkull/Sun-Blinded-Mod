package net.thedragonskull.sunblinded.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.thedragonskull.sunblinded.item.ModItems;

public class ModItemProperties {

    public static void addCustomItemProperties() {
        sunglassesColor(ModItems.SUNGLASSES.get());
    }

    private static void sunglassesColor(Item item) {
        ItemProperties.register(item, ResourceLocation.parse("color"), (itemStack, level, livingEntity, var) -> {

            if (!itemStack.hasTag()) return -1.0F;

            var tag = itemStack.getTag();
            if (!tag.contains("color")) return -1.0F;

            String color = tag.getString("color");
            return SunglassesUtils.sunglassesModel(color);
        });
    }

}
