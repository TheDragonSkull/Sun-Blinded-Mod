package net.thedragonskull.sunblinded.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SunBlinded.MOD_ID) ;

    public static final RegistryObject<Item> BASE_SUNGLASSES = ITEMS.register("base_sunglasses",
            () -> new Sunglasses(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
