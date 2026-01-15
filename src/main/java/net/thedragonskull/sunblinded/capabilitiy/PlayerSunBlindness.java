package net.thedragonskull.sunblinded.capabilitiy;

import net.minecraft.util.Mth;

public class PlayerSunBlindness {
    private float exposure = 0f;
    private int cooldown = 0;
    private boolean blindPacketSent = false;
    private boolean wasLookingAtSun = false;

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = Mth.clamp(exposure, 0f, 1f);
    }

    public void addExposure(float amount) {
        setExposure(this.exposure + amount);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public boolean isBlindPacketSent() {
        return blindPacketSent;
    }

    public void setBlindPacketSent(boolean blindPacketSent) {
        this.blindPacketSent = blindPacketSent;
    }

    public boolean wasLookingAtSun() {
        return wasLookingAtSun;
    }

    public void setWasLookingAtSun(boolean wasLookingAtSun) {
        this.wasLookingAtSun = wasLookingAtSun;
    }

    public void reset() {
        exposure = 0f;
        cooldown = 0;
        blindPacketSent = false;
        wasLookingAtSun = false;
    }

    public void copyFrom(PlayerSunBlindness source) {
        this.exposure = source.exposure;
        this.cooldown = source.cooldown;
        this.blindPacketSent = source.blindPacketSent;
        this.wasLookingAtSun = source.wasLookingAtSun;
    }
}
