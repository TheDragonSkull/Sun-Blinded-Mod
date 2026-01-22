package net.thedragonskull.sunblinded.events;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.render.layer.SunBlindEyesLayer;
import net.thedragonskull.sunblinded.util.KeyBindings;

@EventBusSubscriber(modid = SunBlinded.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.TOGGLE_GLASSES);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);

            if (renderer != null) {
                renderer.addLayer(new SunBlindEyesLayer(renderer));
            }
        }
    }

}
