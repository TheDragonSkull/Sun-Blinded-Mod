package net.thedragonskull.sunblinded.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.thedragonskull.sunblinded.SunBlinded;

public class ModLootFunctions {
    public static final DeferredRegister<LootItemFunctionType> FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SunBlinded.MOD_ID);

    public static final RegistryObject<LootItemFunctionType> SET_RANDOM_COLOR =
            FUNCTIONS.register("set_random_color",
                    () -> new LootItemFunctionType(new SetRandomColorFunction.Serializer()));

    public static void register(IEventBus eventBus) {
        FUNCTIONS.register(eventBus);
    }
}
