package net.thedragonskull.sunblinded.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(SunBlinded.MOD_ID) ;

    public static final DeferredItem<Item> SUNGLASSES = ITEMS.register("sunglasses",
            () -> new Sunglasses(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
