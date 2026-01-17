package net.thedragonskull.sunblinded;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.item.ModCreativeModeTab;
import net.thedragonskull.sunblinded.item.ModItems;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;
import net.thedragonskull.sunblinded.loot.ModLootFunctions;
import net.thedragonskull.sunblinded.loot.ModLootModifiers;
import net.thedragonskull.sunblinded.network.PacketHandler;
import net.thedragonskull.sunblinded.recipe.ModRecipes;
import net.thedragonskull.sunblinded.render.SunglassesCurioRenderer;
import net.thedragonskull.sunblinded.util.ModItemProperties;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(SunBlinded.MOD_ID)
public class SunBlinded {
    public static final String MOD_ID = "sunblinded";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SunBlinded() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
        ModEffects.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModLootFunctions.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModItemProperties.addCustomItemProperties();

            event.enqueueWork(PacketHandler::register);

            event.enqueueWork(() -> ModItems.ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(item -> item instanceof Sunglasses)
                    .forEach(item -> CuriosRendererRegistry.register(item, SunglassesCurioRenderer::new)));
        }
    }
}
