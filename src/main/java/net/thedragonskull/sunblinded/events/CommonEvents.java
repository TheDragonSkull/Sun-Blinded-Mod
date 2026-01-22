package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindness;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindnessProvider;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.item.ModItems;
import net.thedragonskull.sunblinded.network.S2CBurningEyesSync;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = SunBlinded.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getSlot() == EquipmentSlot.HEAD) {
            ItemStack to = event.getTo();
            if (to.is(ModItems.SUNGLASSES.get())) {
                SunglassesUtils.setGlassesUp(to, false);
            }
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {

        ItemStack result = event.getCrafting();
        Player player = event.getEntity();

        if (!result.is(ModItems.SUNGLASSES.get())) return;

        CraftingContainer inv = (CraftingContainer) event.getInventory();

        boolean hadColorBefore = false;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.is(ModItems.SUNGLASSES.get())
                    && stack.hasTag()
                    && stack.getTag().contains("color")) {
                hadColorBefore = true;
                break;
            }
        }

        CompoundTag tag = result.getTag();

        // Clean
        if (hadColorBefore && (tag == null || !tag.contains("color"))) {
            player.level().playSound(null,
                    player.blockPosition(),
                    SoundEvents.BUCKET_EMPTY,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.0f);
        }
        // Dye
        else if (tag != null && tag.contains("color")) {
            player.level().playSound(null,
                    player.blockPosition(),
                    SoundEvents.DYE_USE,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.0f);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSunBlindnessProvider.SUN_BLINDNESS).isPresent()) {
                event.addCapability(new ResourceLocation(SunBlinded.MOD_ID, "properties"), new PlayerSunBlindnessProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerSunBlindnessProvider.SUN_BLINDNESS).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerSunBlindnessProvider.SUN_BLINDNESS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSunBlindness.class);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) return;

        UUID id = player.getUUID();

        PacketHandler.sendToAllPlayer(new S2CBurningEyesSync(id, false));
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide) {
            BurningEyesClient.removeBlinded(event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) return;

        UUID id = player.getUUID();

        PacketHandler.sendToAllPlayer(new S2CBurningEyesSync(id, true));
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        if (SunAfterimageClient.snapshot != null) {
            SunAfterimageClient.snapshot.destroyBuffers();
            SunAfterimageClient.snapshot = null;
        }
    }

    @SubscribeEvent
    public static void onRemoveEffect(MobEffectEvent.Remove event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (event.getEffect() != ModEffects.SUN_BLINDED_EFFECT.get()) return;

        UUID id = player.getUUID();

        PacketHandler.sendToAllPlayer(new S2CBurningEyesSync(id, false));
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();

        ItemStack glasses = new ItemStack(ModItems.SUNGLASSES.get());

        EquipmentSlot slot = Mob.getEquipmentSlotForItem(glasses);
        if (slot != EquipmentSlot.HEAD) return;

        float chance = 0.005f;
        if (mob.getRandom().nextFloat() > chance) return;

        String[] colors = {
                "white", "orange", "magenta", "light_blue", "yellow", "lime",
                "pink", "gray", "light_gray", "cyan", "purple", "blue",
                "brown", "green", "red", "black"
        };
        String color = colors[mob.getRandom().nextInt(colors.length)];
        glasses.getOrCreateTag().putString("color", color);

        mob.setItemSlot(EquipmentSlot.HEAD, glasses);

        mob.setDropChance(EquipmentSlot.HEAD, 1);
    }


    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (!SunAfterimageClient.active) return;
        if (SunAfterimageClient.snapshot == null) return;

        float alpha = (SunAfterimageClient.fadeTicks
                / (float) SunAfterimageClient.MAX_FADE_TICKS) * 0.5F;

        Minecraft mc = Minecraft.getInstance();
        Window win = mc.getWindow();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(
                0,
                SunAfterimageClient.snapshot.getColorTextureId()
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);

        drawFullscreenQuad(
                win.getGuiScaledWidth(),
                win.getGuiScaledHeight()
        );

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }


    private static void drawFullscreenQuad(int w, int h) {
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // V inverted
        bb.vertex(0,   h, 0).uv(0, 0).endVertex();
        bb.vertex(w,   h, 0).uv(1, 0).endVertex();
        bb.vertex(w,   0, 0).uv(1, 1).endVertex();
        bb.vertex(0,   0, 0).uv(0, 1).endVertex();

        BufferUploader.drawWithShader(bb.end());
    }
}
