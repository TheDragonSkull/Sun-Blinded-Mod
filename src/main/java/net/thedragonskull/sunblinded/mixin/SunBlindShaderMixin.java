package net.thedragonskull.sunblinded.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.events.SunBlindClient;
import net.thedragonskull.sunblinded.events.SunExposureClient;
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

        if (!SunBlindClient.shaderLoaded || mc.gameRenderer.currentEffect() == null) {
            mc.gameRenderer.loadEffect(SUNBLIND);
            SunBlindClient.shaderLoaded = true;
        }

        float exposure = SunExposureClient.exposure;

        float eased = (float) Mth.smoothstep(exposure);
        float saturation = 1.0F + exposure * 20.5F;
        float brightness = 1.0F + eased * 0.4F;
        float burn = eased * 0.25F;

        PostChain chain = mc.gameRenderer.currentEffect();
        if (chain != null) {
            for (PostPass pass : ((PostChainAccessor) chain).sunblinded$getPasses()) {
                EffectInstance effect = pass.getEffect();
                effect.safeGetUniform("Saturation").set(saturation);
                effect.safeGetUniform("ColorScale").set(brightness, brightness, brightness);
                effect.safeGetUniform("Offset").set(burn, burn, burn);
            }
        }
    }

}


