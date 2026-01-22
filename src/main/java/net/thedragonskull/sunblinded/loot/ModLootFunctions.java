package net.thedragonskull.sunblinded.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedragonskull.sunblinded.SunBlinded;

import java.util.function.Supplier;

public class ModLootFunctions {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SunBlinded.MOD_ID);

    public static final Supplier<LootItemFunctionType<SetRandomColorFunction>> SET_RANDOM_COLOR =
            LOOT_FUNCTION_TYPES.register("set_random_color", () -> new LootItemFunctionType(SetRandomColorFunction.CODEC));

    public static void register(IEventBus eventBus) {
        LOOT_FUNCTION_TYPES.register(eventBus);
    }
}
