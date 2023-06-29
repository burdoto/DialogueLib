package de.kaleidox.dialib.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.kaleidox.dialib.DialogueManager;
import de.kaleidox.dialib.dto.DialogueBlob;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.io.InputStream;

public class DialogueCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dialogue")
                .requires((src) -> src.hasPermission(1))
                .then(Commands.literal("tutorial")
                        .executes(DialogueCommand::runTutorial))
                .then(Commands.literal("start")
                        .then(Commands.argument("name", MessageArgument.message())
                                .executes(DialogueCommand::runStart)))
        );
    }

    private static int runTutorial(CommandContext<CommandSourceStack> ctx) {
        var player = getPlayer(ctx);
        DialogueBlob blob;
        try (InputStream data = ClassLoader.getSystemResourceAsStream("assets/dialogue/tutorial.json")) {
            blob = DialogueManager.parse(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read tutorial.json", e);
        }
        blob.start(player);
        return 1;
    }

    private static int runStart(CommandContext<CommandSourceStack> ctx) {
        //ctx.getSource().getServer().getPlayerList().broadcastMessage(Component.nullToEmpty("hello world"), ChatType.CHAT, entity.getUUID());

        var player = getPlayer(ctx);
        var name = ctx.getArgument("name", String.class);
        var dialogue = DialogueManager.getByName(name);
        dialogue.start(player);
        return 1;
    }

    private static Player getPlayer(CommandContext<CommandSourceStack> ctx) {
    }
}
