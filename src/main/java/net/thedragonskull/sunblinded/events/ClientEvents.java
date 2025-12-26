package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
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
    //TODO: Set/Map/Enum key - value -> dye/glass color - ARGB

    private static int getOverlayColor(ItemStack sunglasses) {
        String glassColor = SunglassesUtils.getColor(sunglasses);

        if (glassColor == null) {
            return 0x50101025;
        }

        return switch (glassColor) {
            case "orange" -> 0x50402F00;
            case "blue"   -> 0x50003CFF;
            case "red"    -> 0x50FF0000;
            default       -> 0x50101025;
        };
    }


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        SunAfterimageClient.captureIfRequested();
        SunAfterimageClient.tickFade();
    }


}
