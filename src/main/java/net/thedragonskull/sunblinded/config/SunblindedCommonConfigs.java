package net.thedragonskull.sunblinded.config;


import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class SunblindedCommonConfigs {
    public static final SunblindedCommonConfigs CONFIGS;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.DoubleValue SUN_LOOKING_RADIUS;
    public final ModConfigSpec.BooleanValue ENABLE_SUNGLASSES_OVERLAY;

    private SunblindedCommonConfigs(ModConfigSpec.Builder builder) {
        SUN_LOOKING_RADIUS = builder.comment(
                "How precisely the player must look at the sun.",
                "0.99 = almost exact, 0.8 = wide cone",
                "Default: 0.8")
                .defineInRange("look_threshold", 0.8D, 0.1D, 0.99D);

        ENABLE_SUNGLASSES_OVERLAY = builder.comment(
                "Enable the sunglasses color overlay.",
                "If false, sunglasses still work but no screen tint is rendered.")
                .define("overlay_enabled", true);
    }

    static {
        Pair<SunblindedCommonConfigs, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(SunblindedCommonConfigs::new);

        CONFIGS = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}
