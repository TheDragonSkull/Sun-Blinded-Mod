package net.thedragonskull.sunblinded;

import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class SunShaderRegistry {

    public static final Map<String, ResourceLocation> SHADERS = new LinkedHashMap<>();

    static {
        // === VANILLA ===
        add("invert", "minecraft:shaders/post/invert.json");
        add("blur", "minecraft:shaders/post/blur.json");
        add("desaturate", "minecraft:shaders/post/desaturate.json");
        add("sobel", "minecraft:shaders/post/sobel.json");
        add("bits", "minecraft:shaders/post/bits.json");
        add("wobble", "minecraft:shaders/post/wobble.json");
        add("ntsc", "minecraft:shaders/post/ntsc.json");
        add("phosphor", "minecraft:shaders/post/phosphor.json");
        add("color_convolve", "minecraft:shaders/post/color_convolve.json");

        // === TU MOD ===
        add("sunblinded", "sunblinded:shaders/post/sun_blind_invert.json");
        add("exposure", "sunblinded:shaders/post/sun_blind_exposure.json");
    }

    private static void add(String name, String rl) {
        SHADERS.put(name, ResourceLocation.tryParse(rl));
    }
}


