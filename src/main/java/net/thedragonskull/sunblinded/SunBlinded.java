package net.thedragonskull.sunblinded;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.thedragonskull.sunblinded.attachments.ModAttachments;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.config.SunblindedCommonConfigs;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.item.ModCreativeModeTab;
import net.thedragonskull.sunblinded.item.ModItems;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;
import net.thedragonskull.sunblinded.loot.ModLootFunctions;
import net.thedragonskull.sunblinded.loot.ModLootModifiers;
import net.thedragonskull.sunblinded.recipe.ModRecipes;
import net.thedragonskull.sunblinded.render.SunglassesCurioRenderer;
import net.thedragonskull.sunblinded.util.ModItemProperties;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod(SunBlinded.MOD_ID)
public class SunBlinded {
    public static final String MOD_ID = "sunblinded";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SunBlinded(IEventBus modEventBus, ModContainer container) {

        ModAttachments.register(modEventBus);
        ModDataComponentTypes.register(modEventBus);

        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
        ModEffects.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModLootFunctions.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        container.registerConfig(ModConfig.Type.COMMON, SunblindedCommonConfigs.CONFIG_SPEC, "sunblinded-common.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModItemProperties.addCustomItemProperties();

            event.enqueueWork(() -> ModItems.ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(item -> item instanceof Sunglasses)
                    .forEach(item -> CuriosRendererRegistry.register(item, SunglassesCurioRenderer::new)));
        }
    }
}
