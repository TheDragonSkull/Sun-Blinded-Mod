package net.thedragonskull.sunblinded.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.events.SunExposureClient;
import net.thedragonskull.sunblinded.shader.SunBlindShader;
import net.thedragonskull.sunblinded.util.SunglassesUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class SunBlindShaderMixin {

    private static final ResourceLocation SUNBLIND =
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "shaders/post/sun_blind.json");

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void sunblinded$onRenderLevel(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        float exposure = SunExposureClient.exposure;

        float eased = (float) Mth.smoothstep(exposure);
        float saturation = 1.0F + exposure * 100.5F;
        float brightness = 1.0F + eased * 0.4F;
        //todo: mas brillo
        //todo: smoother

        if (exposure > 0.001F) { //todo la mano/item dejan de renderizarse

            if (mc.gameRenderer.currentEffect() == null
                    || !SUNBLIND.equals(mc.gameRenderer.currentEffect().getName())) {
                mc.gameRenderer.loadEffect(SUNBLIND);
            }

            PostChain chain = mc.gameRenderer.currentEffect();
            if (chain != null) {
                for (PostPass pass : ((PostChainAccessor) chain).sunblinded$getPasses()) {
                    EffectInstance effect = pass.getEffect();
                    effect.safeGetUniform("Saturation").set(saturation);
                    effect.safeGetUniform("ColorScale").set(brightness, brightness, brightness);
                }
            }
        } else {
            if (mc.gameRenderer.currentEffect() != null) {
                mc.gameRenderer.shutdownEffect();
            }
        }
    }

}


