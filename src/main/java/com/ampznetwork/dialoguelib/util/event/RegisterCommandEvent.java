package com.ampznetwork.dialoguelib.util.event;

import com.ampznetwork.dialoguelib.cmd.DialogueCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        var commandDispatcher = event.getDispatcher();

        DialogueCommand.register(commandDispatcher);
    }
}
