package net.thedragonskull.sunblinded.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Random;

public class SetRandomColorFunction extends LootItemConditionalFunction {

    protected SetRandomColorFunction(LootItemCondition[] pPredicates) {
        super(pPredicates);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        int color = Mth.nextInt(context.getRandom(), 0, 15);
        stack.getOrCreateTag().putFloat("color", color);
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
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
        stack.getOrCreateTag().putString("color", color);
        return stack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomColorFunction> {

        @Override
        public void serialize(JsonObject json, SetRandomColorFunction value, JsonSerializationContext context) {
            super.serialize(json, value, context);
        }

        @Override
        public SetRandomColorFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
            return new SetRandomColorFunction(conditions);
        }
    }
}
