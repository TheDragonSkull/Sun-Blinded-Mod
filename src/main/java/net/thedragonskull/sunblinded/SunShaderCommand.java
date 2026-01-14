package net.thedragonskull.sunblinded;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SunShaderCommand {

    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent event) { //todo: remove

        event.getDispatcher().register(
                Commands.literal("sunshader")

                        // /sunshader off
                        .then(Commands.literal("off")
                                .executes(ctx -> {
                                    shutdown();
                                    message("Shader apagado");
                                    return 1;
                                })
                        )

                        // /sunshader list
                        .then(Commands.literal("list")
                                .executes(ctx -> {
                                    listShaders();
                                    return 1;
                                })
                        )

                        // /sunshader <shader>
                        .then(Commands.argument("shader", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    SunShaderRegistry.SHADERS.keySet()
                                            .forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String name = StringArgumentType.getString(ctx, "shader");
                                    loadShader(name);
                                    return 1;
                                })
                        )
        );
    }

    private static void loadShader(String input) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        shutdown();

        ResourceLocation shader;

        // 1️⃣ Está en tu registry
        if (SunShaderRegistry.SHADERS.containsKey(input)) {
            shader = SunShaderRegistry.SHADERS.get(input);
        }
        // 2️⃣ Es un ResourceLocation válido
        else if (input.contains(":")) {
            shader = ResourceLocation.tryParse(input);
        }
        // 3️⃣ Nombre vanilla directo (creeper, invert, etc)
        else {
            shader = ResourceLocation.fromNamespaceAndPath(
                    "minecraft",
                    "shaders/post/" + input + ".json"
            );
        }

        try {
            mc.gameRenderer.loadEffect(shader);
            message("Shader cargado: " + shader);
        } catch (Exception e) {
            message("No se pudo cargar shader: " + shader);
        }
    }


    private static void shutdown() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameRenderer.currentEffect() != null) {
            mc.gameRenderer.shutdownEffect();
        }
    }

    private static void message(String text) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.displayClientMessage(
                    Component.literal(text),
                    true
            );
        }
    }

    private static void listShaders() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        mc.player.displayClientMessage(
                Component.literal("Shaders disponibles:"),
                false
        );

        for (String name : SunShaderRegistry.SHADERS.keySet()) {
            mc.player.displayClientMessage(
                    Component.literal(" - " + name),
                    false
            );
        }
    }


}

