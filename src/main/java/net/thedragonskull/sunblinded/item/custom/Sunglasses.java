package net.thedragonskull.sunblinded.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.thedragonskull.sunblinded.util.SunglassesUtils;

public class Sunglasses extends Item implements Equipable {

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

        ChatFormatting formatting = switch (color) {
            case "orange" -> ChatFormatting.GOLD;
            default -> ChatFormatting.WHITE;
        };

        String colorKey = "color.sunblinded." + color;
        return Component.translatable(
                "item.sunblinded.sunglasses.colored",
                Component.translatable(colorKey).withStyle(formatting)
        );
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.CHICKEN_STEP;
    }
}
