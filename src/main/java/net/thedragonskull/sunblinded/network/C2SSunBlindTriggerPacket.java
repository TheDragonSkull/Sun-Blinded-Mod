package net.thedragonskull.sunblinded.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.thedragonskull.sunblinded.SunBlinded;

public record C2SSunBlindTriggerPacket() implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<C2SSunBlindTriggerPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "sun_blind_trigger_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSunBlindTriggerPacket> STREAM_CODEC =
            StreamCodec.unit(new C2SSunBlindTriggerPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
