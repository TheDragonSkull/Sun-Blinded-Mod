package net.thedragonskull.sunblinded.attachments;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nullable;

/* todo: remove
public class PlayerSunBlindnessProvider implements ICapabilityProvider<CompoundTag>, INBTSerializable<CompoundTag> {

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

    @Override
    public @Nullable Object getCapability(Object o, Object o2) {
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {

    }
}
*/
