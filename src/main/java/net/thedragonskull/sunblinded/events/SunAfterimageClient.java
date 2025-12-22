package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30;

public class SunAfterimageClient {

    public static TextureTarget snapshot;
    public static boolean active = false;
    public static boolean wasLookingAtSun = false;
    private static boolean captureRequested = false;

    public static void requestCapture() {
        if (!active) {
            captureRequested = true;
        }
    }

    public static void captureIfRequested() {
        if (!captureRequested) return;

        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();

        int w = main.width;
        int h = main.height;

        reset();

        TextureTarget copy = new TextureTarget(w, h, true, Minecraft.ON_OSX);

        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, main.frameBufferId);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, copy.frameBufferId);

        GlStateManager._glBlitFrameBuffer(
                0, 0, w, h,
                0, 0, w, h,
                GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST
        );

        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        snapshot = copy;
        active = true;
        captureRequested = false;
    }

    public static void reset() {
        if (snapshot != null) {
            snapshot.destroyBuffers();
            snapshot = null;
        }
        active = false;
        captureRequested = false;
    }
}

