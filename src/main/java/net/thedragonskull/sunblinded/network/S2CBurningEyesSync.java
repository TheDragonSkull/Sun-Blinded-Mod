package net.thedragonskull.sunblinded.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.thedragonskull.sunblinded.effect.SunBlindedEffect;
import net.thedragonskull.sunblinded.events.BurningEyesClient;

import java.util.UUID;
import java.util.function.Supplier;

public class S2CBurningEyesSync {
    private final UUID playerId;
    private final boolean blinded;

    public S2CBurningEyesSync(UUID playerId, Boolean blinded) {
        this.playerId = playerId;
        this.blinded = blinded;
    }

    public S2CBurningEyesSync(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.blinded = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeBoolean(blinded);
    }

    public static void handle(S2CBurningEyesSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (msg.blinded) {
                BurningEyesClient.addBlinded(msg.playerId);
            } else {
                BurningEyesClient.removeBlinded(msg.playerId);
            }
        });

        ctx.get().setPacketHandled(true);
    }


}
