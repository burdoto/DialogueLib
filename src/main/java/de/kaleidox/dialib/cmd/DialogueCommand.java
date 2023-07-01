package de.kaleidox.dialib.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.kaleidox.dialib.DialogueManager;
import de.kaleidox.dialib.dto.DialogueBlob;
import lombok.SneakyThrows;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.io.InputStream;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.MessageArgument.message;

public class DialogueCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("dialogue")
                .requires((src) -> src.hasPermission(1))
                .then(literal("tutorial")
                        .executes(DialogueCommand::runTutorial))
                .then(literal("start")
                        .then(argument("name", message())
                                .executes(DialogueCommand::runStart)))
                .then(literal("pick")
                        .then(argument("arg", message())
                                .executes(DialogueCommand::runPick)))
        );
    }

    private static int runTutorial(CommandContext<CommandSourceStack> ctx) {
        //ctx.getSource().getServer().getPlayerList().broadcastMessage(Component.nullToEmpty("hello world"), ChatType.CHAT, entity.getUUID());

        var player = getPlayer(ctx);
        DialogueBlob blob;
        try (InputStream data = ClassLoader.getSystemResourceAsStream("assets/dialogue/tutorial.json")) {
            blob = DialogueManager.parse(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read tutorial.json", e);
        }
        DialogueManager.start(blob, player);
        return 1;
    }

    private static int runStart(CommandContext<CommandSourceStack> ctx) {
        var player = getPlayer(ctx);
        var name = ctx.getArgument("name", String.class);
        var dialogue = DialogueManager.findByName(name).join();
        DialogueManager.start(dialogue, player);
        return 1;
    }

    private static int runPick(CommandContext<CommandSourceStack> ctx) {
        var player = getPlayer(ctx);
        var arg = ctx.getArgument("arg", String.class);
        DialogueManager.next(player, arg);
        return 1;
    }

    @SneakyThrows
    private static Player getPlayer(CommandContext<CommandSourceStack> ctx) {
        return ctx.getSource().getPlayerOrException();
    }
}
