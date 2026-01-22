package net.thedragonskull.sunblinded.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thedragonskull.sunblinded.SunBlinded;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, SunBlinded.MOD_ID);

    public static final Supplier<MobEffect> SUN_BLINDED_EFFECT = MOB_EFFECTS.register("thedragon_sun_blinded",
            () -> new SunBlindedEffect(MobEffectCategory.HARMFUL, 0x101025));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
