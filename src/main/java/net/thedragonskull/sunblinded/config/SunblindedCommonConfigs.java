package net.thedragonskull.sunblinded.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SunblindedCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue SUN_LOOKING_RADIUS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_SUNGLASSES_OVERLAY;

    static {
        BUILDER.push("Configs for Vape Mod");

        SUN_LOOKING_RADIUS = BUILDER.comment(
                "How precisely the player must look at the sun.",
                "0.99 = almost exact, 0.8 = wide cone",
                "Default: 0.8")
                .defineInRange("look_threshold", 0.8D, 0.1D, 0.99D);

        ENABLE_SUNGLASSES_OVERLAY = BUILDER.comment(
                "Enable the sunglasses color overlay.",
                "If false, sunglasses still work but no screen tint is rendered.")
                .define("overlay_enabled", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
