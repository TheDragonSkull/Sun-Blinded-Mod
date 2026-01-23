package net.thedragonskull.sunblinded.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.thedragonskull.sunblinded.item.ModItems;
import net.thedragonskull.sunblinded.util.SunglassesUtils;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class Sunglasses extends Item implements Equipable, ICurioItem {

    public Sunglasses(Properties pProperties) {
        super(pProperties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player)) return;

        SunglassesUtils.setGlassesUp(stack, false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return this.swapWithEquipmentSlot(this, pLevel, pPlayer, pHand);
    }

    @Override
    public Component getName(ItemStack stack) {
        String color = SunglassesUtils.getColor(stack);
        if (color == null) {
            return super.getName(stack);
        }

        int rgb = SunglassesUtils.getTextColor(color);

        Component colorName = Component.translatable("color.sunblinded." + color)
                .withStyle(style -> style.withColor(TextColor.fromRgb(rgb)));

        return Component.translatable(
                "item.sunblinded.sunglasses.colored",
                colorName
        );
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        if (!(entity instanceof Player player)) return true;

        if (armorType == EquipmentSlot.HEAD) {
            return !SunglassesUtils.hasSunglassesInCurios(player);
        }

        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        Player player = (Player) context.entity();

        return !player.getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.SUNGLASSES.get());
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return Holder.direct(SoundEvents.CHICKEN_STEP);
    }

    // Curios
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return true;

        return !player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SUNGLASSES.get());
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;

        ItemStack glasses = SunglassesUtils.getEquippedSunglasses(player);
        if (glasses == null) return;

        SunglassesUtils.setGlassesUp(glasses, false);
        slotContext.entity().level().playSound(
                null,
                slotContext.entity().blockPosition(),
                SoundEvents.CHICKEN_STEP,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, TooltipContext context, ItemStack stack) {
        return List.of();
    }
}
