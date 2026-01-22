package net.thedragonskull.sunblinded.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.thedragonskull.sunblinded.SunBlinded;
import net.thedragonskull.sunblinded.events.BurningEyesClient;

public class SunBlindEyesLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation[] FRAMES = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_0.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_1.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_2.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_3.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_4.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_5.png"),
            ResourceLocation.fromNamespaceAndPath(SunBlinded.MOD_ID, "textures/entity/eyes/eyes_6.png")
    };

    public SunBlindEyesLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer player, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!BurningEyesClient.isBlinded(player)) return;

        int frame = ((int) ((pAgeInTicks + pPartialTick) / 2f)) % FRAMES.length;
        RenderType eyes = RenderType.eyes(FRAMES[frame]);
        VertexConsumer vc = pBuffer.getBuffer(eyes);

        pPoseStack.pushPose();

        this.getParentModel().head.render(
                pPoseStack,
                vc,
                pPackedLight,
                OverlayTexture.NO_OVERLAY
        );

        pPoseStack.popPose();
    }
}
