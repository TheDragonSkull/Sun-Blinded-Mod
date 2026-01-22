package net.thedragonskull.sunblinded.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thedragonskull.sunblinded.events.BurningEyesClient;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleBurningEyesSync(S2CBurningEyesSync msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            if (level == null) return;

            Player player = level.getPlayerByUUID(msg.playerId());
            if (player == null) return;

            if (msg.blinded()) {
                BurningEyesClient.addBlinded(msg.playerId());
            } else {
                BurningEyesClient.removeBlinded(msg.playerId());
            }
        });
    }

}
