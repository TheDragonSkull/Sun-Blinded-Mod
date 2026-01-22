package net.thedragonskull.sunblinded.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SunBlinded.MOD_ID) ;

    public static final RegistryObject<Item> SUNGLASSES = ITEMS.register("sunglasses",
            () -> new Sunglasses(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
