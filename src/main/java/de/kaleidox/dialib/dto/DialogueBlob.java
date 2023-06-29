package de.kaleidox.dialib.dto;

import lombok.Builder;
import lombok.Data;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Data
@Builder
public class DialogueBlob {
    public @Nullable String ref;
    public @Nullable String key;
    public @Nullable String desc;
    public @Nullable String title;
    public @Builder.Default int waitDuration = 500;
    public @Builder.Default int waitEach = 250;
    public List<DialogueBlob> repo;
    public List<Object> content;
    public Map<String, DialogueBlob> choices;

    public void start(Player player) {
        start(player, null);
    }

    public void start(Player player, @Nullable DialogueBlob parent) {
        for (var txt : content)
            player.sendMessage(Component.nullToEmpty(txt.toString()), player.getUUID());
        for (var e : choices.entrySet())
            player.sendMessage(Component.nullToEmpty(choiceToString(e)), player.getUUID());
    }

    private String choiceToString(Map.Entry<String, DialogueBlob> choice) {
        return "[%s] %s".formatted(
                choice.getKey(),
                Objects.requireNonNullElseGet(choice.getValue().desc, choice::getKey));
    }

    private DialogueBlob getChoice(String key, @Nullable DialogueBlob parent) {
        return choices.values().stream()
                .filter(c -> Objects.equals(c.key, key))
                .findAny()
                .flatMap(c -> c.ref == null ? Optional.of(c) : fromRepo(c.ref, parent))
                .orElseThrow(()->new NoSuchElementException("No choice with key " + key + " was found", new AssertionError()));
    }

    private Optional<DialogueBlob> fromRepo(@Nullable String key, @Nullable DialogueBlob parent) {
        return key == null ? Optional.empty() : repo.stream()
                .filter(c -> Objects.equals(c.key, key))
                .findAny()
                .or(() -> parent != null ? parent.fromRepo(key, null) : Optional.empty());
    }
}
