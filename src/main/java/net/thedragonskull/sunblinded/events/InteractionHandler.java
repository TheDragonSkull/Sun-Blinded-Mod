package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
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

        boolean lookingAtSun = SunglassesUtils.isLookingAtSun(player);

        if (SunglassesUtils.hasSunglasses(player)) {
            SunExposureClient.exposure = 0f;
        } else {
            if (SunExposureClient.blindnessCooldown > 0) {
                SunExposureClient.blindnessCooldown--;
                return;
            }

            if (lookingAtSun) {
                SunExposureClient.exposure += 1f / (5f * 20f);
            } else {
                SunExposureClient.exposure -= 1f / (5f * 20f);
            }
        }

        SunExposureClient.exposure = Mth.clamp(
                SunExposureClient.exposure, 0f, 1f
        );

        if (SunAfterimageClient.wasLookingAtSun && !lookingAtSun
                && SunExposureClient.exposure > 0.15F) {
            SunAfterimageClient.requestCapture(SunExposureClient.exposure);
        }

        SunAfterimageClient.wasLookingAtSun = lookingAtSun;

        if (SunExposureClient.exposure >= 1.0f) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100)); //TODO: se queda perma
            SunExposureClient.exposure = 0f;
            SunExposureClient.blindnessCooldown = 100;
        }

    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!SunAfterimageClient.active) return;
        if (SunAfterimageClient.snapshot == null) return;

        float alpha = (SunAfterimageClient.fadeTicks
                / (float) SunAfterimageClient.MAX_FADE_TICKS) * 0.5F;

        Minecraft mc = Minecraft.getInstance();
        Window win = mc.getWindow();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(
                0,
                SunAfterimageClient.snapshot.getColorTextureId()
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        drawFullscreenQuad(
                win.getGuiScaledWidth(),
                win.getGuiScaledHeight()
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }


    private static void drawFullscreenQuad(int w, int h) {
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // V inverted
        bb.vertex(0,   h, 0).uv(0, 0).endVertex();
        bb.vertex(w,   h, 0).uv(1, 0).endVertex();
        bb.vertex(w,   0, 0).uv(1, 1).endVertex();
        bb.vertex(0,   0, 0).uv(0, 1).endVertex();

        BufferUploader.drawWithShader(bb.end());
    }


    //todo: detect curios or armor head item method
}
