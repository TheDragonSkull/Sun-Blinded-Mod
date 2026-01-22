package net.thedragonskull.sunblinded.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.thedragonskull.sunblinded.SunBlinded;

import java.util.UUID;

public record S2CBurningEyesSync(UUID playerId, boolean blinded) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CBurningEyesSync> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "burning_eyes_sync_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBurningEyesSync> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC,
                    S2CBurningEyesSync::playerId,
                    ByteBufCodecs.BOOL,
                    S2CBurningEyesSync::blinded,
                    S2CBurningEyesSync::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


}
