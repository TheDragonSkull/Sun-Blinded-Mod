package net.thedragonskull.sunblinded.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.thedragonskull.sunblinded.util.SunglassesUtils;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SunglassesCurioRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity living = slotContext.entity();
        if (!(living instanceof Player player)) return;
        if (player.isInvisible()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == player && mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoid)) {
            return;
        }

        matrixStack.pushPose();

        humanoid.getHead().translateAndRotate(matrixStack);

        matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStack.scale(0.62F, 0.62F, 0.62F);
        matrixStack.translate(0, 0.4F, 0);

        ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
        if (player.getItemBySlot(EquipmentSlot.HEAD) == stack) return;

        if (SunglassesUtils.areGlassesUp(glasses)) {
            matrixStack.translate(0, 0.2F, 0F);
            matrixStack.mulPose(Axis.XP.rotationDegrees(40.0F));
        }


        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.HEAD,
                light,
                OverlayTexture.NO_OVERLAY,
                matrixStack,
                renderTypeBuffer,
                slotContext.entity().level(),
                0
        );

        matrixStack.popPose();
    }

}
