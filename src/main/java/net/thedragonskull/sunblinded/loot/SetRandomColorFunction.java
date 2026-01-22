package net.thedragonskull.sunblinded.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.thedragonskull.sunblinded.component.ModDataComponentTypes;

import java.util.List;

public class SetRandomColorFunction extends LootItemConditionalFunction {

    protected SetRandomColorFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    public static final MapCodec<SetRandomColorFunction> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    LootItemConditionalFunction.commonFields(inst)
                    .apply(inst, SetRandomColorFunction::new)
            );

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        int color = Mth.nextInt(context.getRandom(), 0, 15);
        stack.set(ModDataComponentTypes.COLOR.get(), String.valueOf(color));
        return stack;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ModLootFunctions.SET_RANDOM_COLOR.get();
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(SetRandomColorFunction::new);
    }

    public static ItemStack apply(ItemStack stack, RandomSource random) {
        String[] colors = {
                "white", "orange", "magenta", "light_blue", "yellow", "lime",
                "pink", "gray", "light_gray", "cyan", "purple", "blue",
                "brown", "green", "red", "black"
        };

        String color = colors[random.nextInt(colors.length)];
        stack.set(ModDataComponentTypes.COLOR.get(), color);
        return stack;
    }
}
