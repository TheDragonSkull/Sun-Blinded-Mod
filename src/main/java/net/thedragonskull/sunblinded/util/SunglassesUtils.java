package net.thedragonskull.sunblinded.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.thedragonskull.sunblinded.item.custom.Sunglasses;

public class SunglassesUtils {

    public static boolean hasSunglasses(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof Sunglasses;
    }

    /**
     * Returns true if player is looking at the sun (current tick).
     */
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

}
