package net.thedragonskull.sunblinded.events;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

@Mod.EventBusSubscriber(modid = SunBlinded.MOD_ID)
public class InteractionHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();
        if (!level.isClientSide) return;

        if (SunglassesUtils.hasSunglasses(player)) {
            SunExposureClient.exposure = 0f;
            return;
        }

        if (SunExposureClient.blindnessCooldown > 0) {
            SunExposureClient.blindnessCooldown--;
            return;
        }

        if (SunglassesUtils.isLookingAtSun(player)) {
            SunExposureClient.exposure += 1f / (5f * 20f); // 5 seconds
        } else {
            SunExposureClient.exposure -= 1f / (2f * 20f);
        }

        SunExposureClient.exposure = Mth.clamp(
                SunExposureClient.exposure, 0f, 1f
        );

        if (SunExposureClient.exposure >= 1.0f) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100));
            SunExposureClient.exposure = 0f;
            SunExposureClient.blindnessCooldown = 100;
        }
    }

    //todo: detect curios or armor head item method
}
