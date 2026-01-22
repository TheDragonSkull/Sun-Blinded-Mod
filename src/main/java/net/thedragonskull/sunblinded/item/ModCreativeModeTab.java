package net.thedragonskull.sunblinded.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;

import java.util.function.Supplier;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SunBlinded.MOD_ID);

    public static final Supplier<CreativeModeTab> SUNGLASSES_TAB = CREATIVE_MODE_TABS.register("sunglasses_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SUNGLASSES.get()))
                    .title(Component.translatable("creativetab.sunglasses_tab"))
                    .displayItems((pParameters, output) -> {

                        output.accept(new ItemStack(ModItems.SUNGLASSES.get()));

                        for (DyeColor dye : DyeColor.values()) {
                            ItemStack stack = new ItemStack(ModItems.SUNGLASSES.get());
                            stack.set(ModDataComponentTypes.COLOR.get(), dye.getName());
                            output.accept(stack);
                        }

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
