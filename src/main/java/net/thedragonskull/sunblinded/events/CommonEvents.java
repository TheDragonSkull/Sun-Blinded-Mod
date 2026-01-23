package net.thedragonskull.sunblinded.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.attachments.PlayerSunBlindness;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;
import net.thedragonskull.sunblinded.effect.ModEffects;
import net.thedragonskull.sunblinded.item.ModItems;
import net.thedragonskull.sunblinded.network.S2CBurningEyesSync;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

import java.util.UUID;

@EventBusSubscriber(modid = SunBlinded.MOD_ID)
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
            if (stack.is(ModItems.SUNGLASSES.get()) && stack.has(ModDataComponentTypes.COLOR)) {
                hadColorBefore = true;
                break;
            }
        }

        boolean hasColorNow = result.has(ModDataComponentTypes.COLOR);

        // Clean
        if (hadColorBefore && !hasColorNow) {
            player.level().playSound(null,
                    player.blockPosition(),
                    SoundEvents.BUCKET_EMPTY,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.0f);
        }
        // Dye
        else if (hasColorNow) {
            player.level().playSound(null,
                    player.blockPosition(),
                    SoundEvents.DYE_USE,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.0f);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT)) return;

        UUID id = player.getUUID();

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new S2CBurningEyesSync(id, false));
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
        if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT)) return;

        UUID id = player.getUUID();

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new S2CBurningEyesSync(id, true));
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
        if (event.getEffect() != ModEffects.SUN_BLINDED_EFFECT) return;

        UUID id = player.getUUID();

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new S2CBurningEyesSync(id, false));
    }

    @SubscribeEvent
    public static void onMobSpawn(FinalizeSpawnEvent event) {
        Mob mob = event.getEntity();

        ItemStack glasses = new ItemStack(ModItems.SUNGLASSES.get());

        EquipmentSlot slot = mob.getEquipmentSlotForItem(glasses);
        if (slot != EquipmentSlot.HEAD) return;

        float chance = 0.005f;
        if (mob.getRandom().nextFloat() > chance) return;

        String[] colors = {
                "white", "orange", "magenta", "light_blue", "yellow", "lime",
                "pink", "gray", "light_gray", "cyan", "purple", "blue",
                "brown", "green", "red", "black"
        };
        String color = colors[mob.getRandom().nextInt(colors.length)];
        glasses.set(ModDataComponentTypes.COLOR, color);

        mob.setItemSlot(EquipmentSlot.HEAD, glasses);

        mob.setDropChance(EquipmentSlot.HEAD, 1);
    }


    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Pre event) {
        if (!SunAfterimageClient.active) return;
        if (SunAfterimageClient.snapshot == null) return;

        float alpha = (SunAfterimageClient.fadeTicks
                / (float) SunAfterimageClient.MAX_FADE_TICKS) * 0.5F;

        GuiGraphics gg = event.getGuiGraphics();
        Minecraft mc = Minecraft.getInstance();
        Window win = mc.getWindow();

        int fbW = win.getWidth();
        int fbH = win.getHeight();

        int guiW = win.getGuiScaledWidth();
        int guiH = win.getGuiScaledHeight();

        float scaleX = (float) guiW / fbW;
        float scaleY = (float) guiH / fbH;

        gg.pose().pushPose();

        // 🔥 Convertimos framebuffer → GUI space
        gg.pose().scale(scaleX, scaleY, 1f);

        gg.setColor(1f, 1f, 1f, alpha); // 👈 ESTO SÍ funciona

        // Screenshot exacto + flip vertical
        gg.blit(
                SunAfterimageClient.AFTERIMAGE_RL,
                0,
                0,
                0,
                0,
                fbW,
                fbH,
                fbW,
                fbH
        );

        gg.setColor(1f, 1f, 1f, 1f);
        gg.pose().popPose();
    }

}
