package net.thedragonskull.sunblinded.capabilitiy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlayerSunBlindnessProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<PlayerSunBlindness> SUN_BLINDNESS =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final PlayerSunBlindness instance = new PlayerSunBlindness();
    private final LazyOptional<PlayerSunBlindness> optional = LazyOptional.of(() -> instance);

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == SUN_BLINDNESS ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Exposure", instance.getExposure());
        tag.putInt("Cooldown", instance.getCooldown());
        tag.putBoolean("BlindPacketSent", instance.isBlindPacketSent());
        tag.putBoolean("WasLookingAtSun", instance.wasSunReachingEyes());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        instance.setExposure(tag.getFloat("Exposure"));
        instance.setCooldown(tag.getInt("Cooldown"));
        instance.setBlindPacketSent(tag.getBoolean("BlindPacketSent"));
        instance.setWasSunReachingEyes(tag.getBoolean("WasLookingAtSun"));
    }
}
