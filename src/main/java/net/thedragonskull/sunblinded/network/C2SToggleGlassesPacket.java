package net.thedragonskull.sunblinded.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

import java.util.function.Supplier;

public class C2SToggleGlassesPacket {

    public C2SToggleGlassesPacket() {
    }

    public C2SToggleGlassesPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(C2SToggleGlassesPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
            if (glasses == null) return;

            boolean current = SunglassesUtils.areGlassesUp(glasses);
            SunglassesUtils.setGlassesUp(glasses, !current);
            player.level().playSound(null, player.blockPosition(), SoundEvents.CHICKEN_STEP, SoundSource.PLAYERS);
        });
        ctx.get().setPacketHandled(true);
    }
}
