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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.capabilitiy.PlayerSunBlindness;
import net.thedragonskull.sunblinded.capabilitiy.PlayerSunBlindnessProvider;
import net.thedragonskull.sunblinded.item.ModItems;

@Mod.EventBusSubscriber(modid = SunBlinded.MOD_ID)
public class CommonEvents {

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
                event.addCapability(ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "properties"), new PlayerSunBlindnessProvider());
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

/*    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) { todo:remove
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level();

        ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);

        boolean glassesProtect = glasses != null && !SunglassesUtils.areGlassesUp(glasses);

        boolean lookingAtSun = SunglassesUtils.isLookingAtSun(player) && !player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get()) && !glassesProtect;

        if (level.isClientSide) {
            if (glassesProtect) {
                SunExposureClient.exposure = 0f;
            } else {
                if (SunExposureClient.blindnessCooldown > 0) {
                    SunExposureClient.blindnessCooldown--;
                    return;
                }

                if (lookingAtSun) {
                    SunExposureClient.exposure += 1f / (5f * 20f);
                } else {
                    SunExposureClient.exposure -= 1f / (5f * 20f);
                }
            }

            SunExposureClient.exposure = Mth.clamp(
                    SunExposureClient.exposure, 0f, 1f
            );

            if (SunAfterimageClient.wasLookingAtSun && !lookingAtSun && (SunExposureClient.exposure > 0.15F && SunExposureClient.exposure < 1.0F)) {
                SunAfterimageClient.requestCapture(SunExposureClient.exposure);
            }

            SunAfterimageClient.wasLookingAtSun = lookingAtSun;
        } else {
            if (SunExposureClient.exposure >= 1.0f) {

                SunExposureClient.exposure = 0f;
                SunExposureClient.blindnessCooldown = 100;

                if (!player.hasEffect(ModEffects.SUN_BLINDED_EFFECT.get())) {

                    player.addEffect(new MobEffectInstance(
                            ModEffects.SUN_BLINDED_EFFECT.get(),
                            -1,
                            0,
                            false,
                            false,
                            true
                    ));

                    player.addEffect(new MobEffectInstance(
                            MobEffects.BLINDNESS,
                            20,
                            255,
                            false,
                            false,
                            false
                    ));
                }

            }
        }

    }*/

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
