package net.thedragonskull.sunblinded.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class SunBlindShader {
    public static ShaderInstance SHADER;

    public static void init(ResourceProvider provider) throws IOException {
        SHADER = new ShaderInstance(
                provider,
                ResourceLocation.fromNamespaceAndPath("sunblinded","sun_blind"),
                DefaultVertexFormat.POSITION_TEX
        );
    }

    public static void update(float intensity) {
        if (SHADER != null) {
            // intensity = 0 -> normal, 1 -> max
            SHADER.safeGetUniform("Saturation").set(1.0f + intensity * 2f);
        }
    }
}

