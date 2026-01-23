package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.attachments.ModAttachments;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindness;
import net.thedragonskull.sunblinded.config.SunblindedCommonConfigs;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.network.C2SSunBlindTriggerPacket;
import net.thedragonskull.sunblinded.network.C2SToggleGlassesPacket;
import net.thedragonskull.sunblinded.util.KeyBindings;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

@EventBusSubscriber(modid = SunBlinded.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Post event) {
        if (!SunblindedCommonConfigs.CONFIGS.ENABLE_SUNGLASSES_OVERLAY.get()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        //if (!mc.options.getCameraType().isFirstPerson()) return;

        ItemStack sunglasses = SunglassesUtils.getEquippedSunglasses(player);
        if (sunglasses == null) return;

        if (SunglassesUtils.areGlassesUp(sunglasses)) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Window window = Minecraft.getInstance().getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        int color = getOverlayColor(sunglasses);
        guiGraphics.fill(0, 0, width, height, 1000, color);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!SunblindedCommonConfigs.CONFIGS.ENABLE_SUNGLASSES_OVERLAY.get()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        ItemStack sunglasses = SunglassesUtils.getEquippedSunglasses(player);
        if (sunglasses == null) return;

        if (SunglassesUtils.areGlassesUp(sunglasses)) return;

        GuiGraphics gg = event.getGuiGraphics();
        int w = event.getScreen().width;
        int h = event.getScreen().height;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        gg.fill(0, 0, w, h, 1000, getOverlayColor(sunglasses));

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static int getOverlayColor(ItemStack sunglasses) {
        String glassColor = SunglassesUtils.getColor(sunglasses);

        if (glassColor == null) {
            return 0x50101025;
        }

        return SunglassesUtils.getOverlayColor(glassColor);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        boolean sunBlinded = player.hasEffect(ModEffects.SUN_BLINDED_EFFECT);

        if (sunBlinded) {

            if (!SunBlindClient.blindShaderLoaded || mc.gameRenderer.currentEffect() == null) {
                mc.gameRenderer.loadEffect(SunglassesUtils.getSunblindInvert());
                SunBlindClient.blindShaderLoaded = true;
            }

            SunAfterimageClient.active = false;
            SunAfterimageClient.fadeTicks = 0;
            SunAfterimageClient.captureRequested = false;
            return;
        }

        SunAfterimageClient.tickFade();
        //SunAfterimageClient.captureIfRequested();

        if (SunBlindClient.blindShaderLoaded) {
            mc.gameRenderer.shutdownEffect();
            SunBlindClient.blindShaderLoaded = false;
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (!player.level().isClientSide) return;

        PlayerSunBlindness data = player.getData(ModAttachments.PLAYER_SUN_BLINDNESS);

        if (player.hasEffect(ModEffects.SUN_BLINDED_EFFECT)) {
            data.reset();
            data.setCooldown(100);
            return;
        }

        if (data.getCooldown() > 0) {
            data.tickCooldown();
            return;
        }

        ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
        boolean glassesProtect = glasses != null && !SunglassesUtils.areGlassesUp(glasses);

        boolean sunReachesEyes = SunglassesUtils.isLookingAtSun(player) && !glassesProtect;

        float delta = 1f / (5f * 20f);
        if (sunReachesEyes) data.addExposure(delta);
        else data.addExposure(-delta);

        if (data.wasSunReachingEyes() && !sunReachesEyes && data.getExposure() > 0.15f) {
            SunAfterimageClient.requestCapture(data.getExposure());
        }

        if (data.getExposure() >= 1.0f && !data.isBlindPacketSent()) {
            data.setBlindPacketSent(true);
            PacketDistributor.sendToServer(new C2SSunBlindTriggerPacket());
        }

        if (data.getExposure() <= 0f) {
            data.setBlindPacketSent(false);
        }

        data.setWasSunReachingEyes(sunReachesEyes);
    }

    @SubscribeEvent
    public static void onInputKeyEvent(InputEvent.Key event) {
        if (KeyBindings.INSTANCE.TOGGLE_GLASSES.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
            if (glasses == null) return;

            PlayerSunBlindness data = player.getData(ModAttachments.PLAYER_SUN_BLINDNESS);

            boolean goingDown = SunglassesUtils.areGlassesUp(glasses);

            if (goingDown && SunglassesUtils.isLookingAtSun(player) && (data.getExposure() > 0.15F && data.getExposure() < 1.0F)) {
                SunAfterimageClient.requestCapture(data.getExposure());
            }

            if (SunglassesUtils.hasSunglassesInCurios(player)) {
                PacketDistributor.sendToServer(new C2SToggleGlassesPacket());
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (!RenderSystem.isOnRenderThread()) return;

        SunAfterimageClient.captureIfRequested();
    }


}
