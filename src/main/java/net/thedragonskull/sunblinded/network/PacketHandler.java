package net.thedragonskull.sunblinded.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.thedragonskull.sunblinded.SunBlinded;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SunBlinded.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int id = 0;

    public static void register() {

        // TO SERVER
        INSTANCE.messageBuilder(C2SToggleGlassesPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SToggleGlassesPacket::encode)
                .decoder(C2SToggleGlassesPacket::new)
                .consumerMainThread(C2SToggleGlassesPacket::handle)
                .add();

        INSTANCE.messageBuilder(C2SSunBlindTriggerPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SSunBlindTriggerPacket::encode)
                .decoder(C2SSunBlindTriggerPacket::new)
                .consumerMainThread(C2SSunBlindTriggerPacket::handle)
                .add();

        // TO CLIENT
        INSTANCE.messageBuilder(S2CBurningEyesSync.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CBurningEyesSync::encode)
                .decoder(S2CBurningEyesSync::new)
                .consumerMainThread(S2CBurningEyesSync::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToAllPlayer(Object msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
