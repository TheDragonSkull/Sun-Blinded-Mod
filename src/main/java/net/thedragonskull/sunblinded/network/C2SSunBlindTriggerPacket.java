package net.thedragonskull.sunblinded.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;
import net.thedragonskull.sunblinded.effect.ModEffects;

import java.util.UUID;
import java.util.function.Supplier;

public class C2SSunBlindTriggerPacket {

    public C2SSunBlindTriggerPacket() {}

    public C2SSunBlindTriggerPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(C2SSunBlindTriggerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) {

                UUID id = player.getUUID();

                player.addEffect(new MobEffectInstance(
                        ModEffects.SUN_BLINDED_EFFECT.get(),
                        -1,
                        0,
                        false,
                        false,
                        true
                ));

                player.addEffect(new MobEffectInstance(
                        MobEffects.BLINDNESS,
                        20,
                        255,
                        false,
                        false,
                        false
                ));

                PacketHandler.sendToAllPlayer(new S2CBurningEyesSync(id, true));
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
