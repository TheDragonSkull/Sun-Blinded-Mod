package net.thedragonskull.sunblinded.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
        if (!(entity instanceof Player player)) return true;

        if (slot == EquipmentSlot.HEAD) {
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
    public SoundEvent getEquipSound() {
        return SoundEvents.CHICKEN_STEP;
    }

    // Curios
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return true;

        return !player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SUNGLASSES.get());
    }

    @Override
    public @NotNull ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.CHICKEN_STEP, 1.0F, 1.0F);
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        tooltips.clear();
        return tooltips;
    }
}
