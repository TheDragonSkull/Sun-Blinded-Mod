package net.thedragonskull.sunblinded.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.item.ModItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.Map;

public class SunglassesUtils {
    public static final String TAG_GLASSES_UP = "GlassesUp";

    @Nullable
    public static ItemStack getEquippedSunglasses(Player player) {
        // 1. Head slot
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.is(ModItems.SUNGLASSES.get())) {
            return head;
        }

        // 2. Curios slot
        return CuriosApi.getCuriosInventory(player)
                .resolve()
                .flatMap(inv -> inv.findFirstCurio(ModItems.SUNGLASSES.get()).map(SlotResult::stack))
                .orElse(null);
    }

    public static boolean hasSunglassesInCurios(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(inv -> inv.findFirstCurio(ModItems.SUNGLASSES.get()).isPresent())
                .orElse(false);
    }

    public static boolean areGlassesUp(ItemStack stack) {
        if (stack == null) return false;
        return stack.getOrCreateTag().getBoolean(TAG_GLASSES_UP);
    }

    public static void setGlassesUp(ItemStack stack, boolean up) {
        stack.getOrCreateTag().putBoolean(TAG_GLASSES_UP, up);
    }

    public static boolean isLookingAtSun(Player player) {
        Level level = player.level();
        if (!level.isClientSide) return false;

        Vec3 look = player.getLookAngle().normalize();

        float sunAngle = level.getSunAngle(1.0F);
        Vec3 sunDir = new Vec3(
                -Math.sin(sunAngle),
                Math.cos(sunAngle),
                0
        ).normalize();

        double dot = look.dot(sunDir);

        Vec3 eyePos = player.getEyePosition();
        Vec3 endPos = eyePos.add(sunDir.scale(1000));

        BlockHitResult result = level.clip(new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        boolean sunVisible = result.getType() == HitResult.Type.MISS;

        return level.isDay() && dot > 0.8 && sunVisible;
    }

    @Nullable
    public static String getColor(ItemStack stack) {
        if (!stack.hasTag()) return null;
        var tag = stack.getTag();
        return tag.contains("color") ? tag.getString("color") : null;
    }

    public static float sunglassesModel(String color) {
        return switch (color) {
            case "white" -> 0.0F;
            case "orange" -> 1.0F;
            case "magenta" -> 2.0F;
            case "light_blue" -> 3.0F;
            case "yellow" -> 4.0F;
            case "lime" -> 5.0F;
            case "pink" -> 6.0F;
            case "gray" -> 7.0F;
            case "light_gray" -> 8.0F;
            case "cyan" -> 9.0F;
            case "purple" -> 10.0F;
            case "blue" -> 11.0F;
            case "brown" -> 12.0F;
            case "green" -> 13.0F;
            case "red" -> 14.0F;
            case "black" -> 15.0F;
            default -> -1.0F;
        };
    }

    private static final Map<String, Integer> TEXT_COLORS = Map.ofEntries(
            Map.entry("white",       0xFFFFFF),
            Map.entry("orange",      0xFFB730),
            Map.entry("magenta",     0xFF77FF),
            Map.entry("light_blue",  0x66CCFF),
            Map.entry("yellow",      0xFFFF55),
            Map.entry("lime",        0x77FF55),
            Map.entry("pink",        0xFF99CC),
            Map.entry("gray",        0x666666),
            Map.entry("light_gray",  0xa1a1a1),
            Map.entry("cyan",        0x3020a8a8),
            Map.entry("purple",      0xAA55FF),
            Map.entry("blue",        0x5555FF),
            Map.entry("brown",       0xAA7744),
            Map.entry("green",       0x55AA55),
            Map.entry("red",         0xFF5555),
            Map.entry("black",       0x444444)
    );

    public static int getTextColor(String color) {
        return TEXT_COLORS.getOrDefault(color, 0xFFFFFF);
    }

    private static final Map<String, Integer> OVERLAY_COLORS = Map.ofEntries(
            Map.entry("white",       0x20FFFFFF),
            Map.entry("orange",      0x309e6c00),
            Map.entry("magenta",     0x30a61ea6),
            Map.entry("light_blue",  0x304b98bf),
            Map.entry("yellow",      0x30a1a140),
            Map.entry("lime",        0x256d9e3c),
            Map.entry("pink",        0x30b3698e),
            Map.entry("gray",        0x50545454),
            Map.entry("light_gray",  0x40858585),
            Map.entry("cyan",        0x3020a8a8),
            Map.entry("purple",      0x305000bf),
            Map.entry("blue",        0x401a2d78),
            Map.entry("brown",       0x50402F00),
            Map.entry("green",       0x35326332),
            Map.entry("red",         0x20FF0000),
            Map.entry("black",       0x50000000)
    );

    public static int getOverlayColor(String color) {
        return OVERLAY_COLORS.getOrDefault(color, 0x50101025);
    }

    private static ResourceLocation SUNBLIND_EXPOSURE;

    public static ResourceLocation getSunBlindExposure() {
        if (SUNBLIND_EXPOSURE == null) {
            SUNBLIND_EXPOSURE = new ResourceLocation(SunBlinded.MOD_ID, "shaders/post/sun_blind.json");
        }
        return SUNBLIND_EXPOSURE;
    }

    private static ResourceLocation SUNBLIND_INVERT;

    public static ResourceLocation getSunblindInvert() {
        if (SUNBLIND_INVERT == null) {
            SUNBLIND_INVERT = new ResourceLocation(SunBlinded.MOD_ID, "shaders/post/sun_blind_invert.json");
        }
        return SUNBLIND_INVERT;
    }
}
