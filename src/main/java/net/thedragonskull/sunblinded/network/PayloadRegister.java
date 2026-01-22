package net.thedragonskull.sunblinded.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thedragonskull.sunblinded.SunBlinded;

@EventBusSubscriber(modid = SunBlinded.MOD_ID)
public class PayloadRegister {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // Client -> Server
        registrar.playToServer(
                C2SSunBlindTriggerPacket.TYPE,
                C2SSunBlindTriggerPacket.STREAM_CODEC,
                ServerPayloadHandler.getInstance()::handleSunBlindTrigger
        );

        registrar.playToServer(
                C2SToggleGlassesPacket.TYPE,
                C2SToggleGlassesPacket.STREAM_CODEC,
                ServerPayloadHandler.getInstance()::handleToggleGlasses
        );

        // Server -> Client
        registrar.playToClient(
                S2CBurningEyesSync.TYPE,
                S2CBurningEyesSync.STREAM_CODEC,
                ClientPayloadHandler.getInstance()::handleBurningEyesSync
        );
    }
}
