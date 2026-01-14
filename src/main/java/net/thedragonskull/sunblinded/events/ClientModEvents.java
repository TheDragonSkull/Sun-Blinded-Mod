package net.thedragonskull.sunblinded.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.util.KeyBindings;

@Mod.EventBusSubscriber(modid = SunBlinded.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.TOGGLE_GLASSES);
    }

}
