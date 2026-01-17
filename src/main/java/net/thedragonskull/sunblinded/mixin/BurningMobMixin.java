package net.thedragonskull.sunblinded.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.thedragonskull.sunblinded.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class BurningMobMixin {

    @Inject(method = "isSunBurnTick", at = @At("RETURN"), cancellable = true)
    private void preventSunBurnIfSunglasses(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!cir.getReturnValue()) return;

        boolean hasSunglasses = false;

        ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (head.is(ModItems.SUNGLASSES.get())) hasSunglasses = true;

        if (hasSunglasses) {
            cir.setReturnValue(false);
        }
    }

}
