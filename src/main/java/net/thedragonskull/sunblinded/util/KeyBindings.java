package net.thedragonskull.sunblinded.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final KeyBindings INSTANCE = new KeyBindings();

    private KeyBindings() {
    }

    public static final String KEY_CATEGORY_SUNGLASSES = "key.category.sunblinded";
    public static final String KEY_TOGGLE_GLASSES = "key.category.sunblinded.toggle_glasses";

    public final KeyMapping TOGGLE_GLASSES = new KeyMapping(
            KEY_TOGGLE_GLASSES,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY_SUNGLASSES);
}
