package net.thedragonskull.sunblinded.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

import java.util.UUID;

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleSunBlindTrigger(C2SSunBlindTriggerPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT)) {

                UUID id = player.getUUID();

                player.addEffect(new MobEffectInstance(
                        ModEffects.SUN_BLINDED_EFFECT,
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
    }

    public void handleToggleGlasses(C2SToggleGlassesPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();

            ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
            if (glasses == null) return;

            boolean current = SunglassesUtils.areGlassesUp(glasses);
            SunglassesUtils.setGlassesUp(glasses, !current);
            player.level().playSound(null, player.blockPosition(), SoundEvents.CHICKEN_STEP, SoundSource.PLAYERS);
        });
    }
}
