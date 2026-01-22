package net.thedragonskull.sunblinded.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindness;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindnessProvider;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.events.SunBlindClient;
import net.thedragonskull.sunblinded.util.SunglassesUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class SunBlindShaderMixin {

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void sunblinded$onRenderLevel(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (mc.player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) {
            if (SunBlindClient.shaderLoaded) {
                mc.gameRenderer.shutdownEffect();
                SunBlindClient.shaderLoaded = false;
            }
            return;
        }

        ItemStack glasses = SunglassesUtils.getEquippedSunglasses(mc.player);
        if (glasses != null && !SunglassesUtils.areGlassesUp(glasses)) {
            if (SunBlindClient.shaderLoaded) {
                mc.gameRenderer.shutdownEffect();
                SunBlindClient.shaderLoaded = false;
            }
            return;
        }

        if (!SunBlindClient.shaderLoaded || mc.gameRenderer.currentEffect() == null) {
            mc.gameRenderer.loadEffect(SunglassesUtils.getSunBlindExposure());
            SunBlindClient.shaderLoaded = true;
        }

        float exposure = mc.player
                .getCapability(PlayerSunBlindnessProvider.SUN_BLINDNESS)
                .map(PlayerSunBlindness::getExposure)
                .orElse(0f);

        float eased = (float) Mth.smoothstep(exposure);
        float saturation = 1.0F + exposure * 20.5F;
        float brightness = 1.0F + eased * 0.8F;
        float burn = eased * 0.5F;

        PostChain chain = mc.gameRenderer.currentEffect();
        if (chain != null) {
            for (PostPass pass : ((PostChainAccessor) chain).sunblinded$getPasses()) {
                EffectInstance effect = pass.getEffect();
                effect.safeGetUniform("Saturation").set(saturation);
                effect.safeGetUniform("ColorScale").set(brightness, brightness, brightness);
                effect.safeGetUniform("Offset").set(burn, burn, burn);
                effect.safeGetUniform("Gray").set(0.4F, 0.5F, 0.1F);
            }
        }
    }

}


