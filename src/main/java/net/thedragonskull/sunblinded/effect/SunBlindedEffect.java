package net.thedragonskull.sunblinded.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SunBlindedEffect extends MobEffect {

    public SunBlindedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static class BurningEyesClient {
        private static final Set<UUID> blindedPlayers = new HashSet<>();

        public static void addBlinded(UUID id) {
            blindedPlayers.add(id);
        }

        public static void removeBlinded(UUID id) {
            blindedPlayers.remove(id);
        }

        public static boolean isBlinded(Player player) {
            return blindedPlayers.contains(player.getUUID());
        }
    }
}
