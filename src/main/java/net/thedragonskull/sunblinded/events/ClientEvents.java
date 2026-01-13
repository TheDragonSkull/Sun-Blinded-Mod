package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

@Mod.EventBusSubscriber(modid = SunBlinded.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if (!mc.options.getCameraType().isFirstPerson()) return;

        ItemStack sunglasses = SunglassesUtils.getEquippedSunglasses(player);
        if (sunglasses == null) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = event.getWindow().getGuiScaledWidth();
        int height = event.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        int color = getOverlayColor(sunglasses);

        guiGraphics.fill(0, 0, width, height, color);

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private static int getOverlayColor(ItemStack sunglasses) {
        String glassColor = SunglassesUtils.getColor(sunglasses);

        if (glassColor == null) {
            return 0x50101025;
        }

/*        return switch (glassColor) {
            case "white"       -> 0x20FFFFFF;
            case "orange"      -> 0x309e6c00;
            case "magenta"     -> 0x30a61ea6;
            case "light_blue"  -> 0x304b98bf;
            case "yellow"      -> 0x30a1a140;
            case "lime"        -> 0x256d9e3c;
            case "pink"        -> 0x30b3698e;
            case "gray"        -> 0x50545454;
            case "light_gray"  -> 0x40858585;
            case "cyan"        -> 0x3020a8a8;
            case "purple"      -> 0x305000bf;
            case "blue"        -> 0x401a2d78;
            case "brown"       -> 0x50402F00;
            case "green"       -> 0x35326332;
            case "red"         -> 0x20FF0000;
            case "black"       -> 0x50000000;
            default            -> 0x50101025;
        };*/

        return SunglassesUtils.getOverlayColor(glassColor);
    }

    private static final ResourceLocation SUNBLIND_INVERT =
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "shaders/post/sun_blind_invert.json");

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;


        if (player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) {

            if (mc.options.getCameraType().isFirstPerson()) {
                if (!SunBlindClient.blindShaderLoaded
                        || mc.gameRenderer.currentEffect() == null) {

                    mc.gameRenderer.loadEffect(SUNBLIND_INVERT);
                    SunBlindClient.blindShaderLoaded = true;
                }
            }

        } else {
            SunAfterimageClient.captureIfRequested();
            SunAfterimageClient.tickFade();

            if (SunBlindClient.blindShaderLoaded) {
                mc.gameRenderer.shutdownEffect();
            }
            SunBlindClient.blindShaderLoaded = false;
        }
    }


}
