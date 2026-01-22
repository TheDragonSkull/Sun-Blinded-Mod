package net.thedragonskull.sunblinded.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.thedragonskull.sunblinded.SunBlinded;

public record C2SToggleGlassesPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SToggleGlassesPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "toggle_glasses_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SToggleGlassesPacket> STREAM_CODEC =
            StreamCodec.unit(new C2SToggleGlassesPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
