package net.thedragonskull.sunblinded.attachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thedragonskull.sunblinded.SunBlinded;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SunBlinded.MOD_ID);

    public static final Supplier<AttachmentType<?>> PLAYER_SUN_BLINDNESS = ATTACHMENTS.register(
            "player_sun_blindness",
            () -> AttachmentType.builder(PlayerSunBlindness::new)
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENTS.register(eventBus);
    }

}
