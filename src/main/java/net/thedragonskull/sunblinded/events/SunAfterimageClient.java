package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL30;

public class SunAfterimageClient {

    public static TextureTarget snapshot;
    public static boolean active = false;
    public static boolean wasLookingAtSun = false;
    public static boolean captureRequested = false;

    public static int fadeTicks = 0;
    public static final int MAX_FADE_TICKS = 100;

    private static float requestedExposure;

    public static void requestCapture(float exposure) {
        if (active || captureRequested) return;
        if (Minecraft.getInstance().level == null) return;
        captureRequested = true;
        requestedExposure = exposure;
    }

    public static void captureIfRequested() {
        if (!captureRequested) return;

        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();

        int w = main.width;
        int h = main.height;

        if (snapshot == null || snapshot.width != w || snapshot.height != h) {
            if (snapshot != null) {
                snapshot.destroyBuffers();
            }
            snapshot = new TextureTarget(w, h, true, Minecraft.ON_OSX);
        }

        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.frameBufferId);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, snapshot.frameBufferId);

        GlStateManager._glBlitFrameBuffer(
                0, 0, w, h,
                0, 0, w, h,
                GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST
        );

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        active = true;

        fadeTicks = Mth.clamp(
                Math.round(requestedExposure * MAX_FADE_TICKS),
                1,
                MAX_FADE_TICKS
        );

        captureRequested = false;
    }

    public static void tickFade() {
        if (!active) return;

        fadeTicks--;

        if (fadeTicks <= 0) {
            reset();
        }
    }

    public static void reset() {
        active = false;
        fadeTicks = 0;
        captureRequested = false;
    }
}

