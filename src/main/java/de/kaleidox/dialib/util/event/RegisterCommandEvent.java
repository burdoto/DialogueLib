package de.kaleidox.dialib.util.event;

import de.kaleidox.dialib.cmd.DialogueCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        var commandDispatcher = event.getDispatcher();

        DialogueCommand.register(commandDispatcher);
    }
}
