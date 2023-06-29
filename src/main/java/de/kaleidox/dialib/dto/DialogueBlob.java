package de.kaleidox.dialib.dto;

import lombok.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
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
    public transient @Nullable DialogueBlob parent;

    public void start(Player player) {
        var session = new Session(this);
        session.exec(player);
    }

    private String choiceToString(Map.Entry<String, DialogueBlob> choice) {
        return "[%s] %s".formatted(
                choice.getKey(),
                Objects.requireNonNullElseGet(choice.getValue().desc, choice::getKey));
    }

    private DialogueBlob getChoice(final String key) {
        return choices.entrySet().stream()
                .filter(e -> key.equals(e.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .flatMap(c -> c.ref == null ? Optional.of(c) : fromRepo(c.ref))
                .orElseThrow(()->new NoSuchElementException("No choice with key " + key + " was found", new AssertionError()));
    }

    private Optional<DialogueBlob> fromRepo(@Nullable String key) {
        return key == null ? Optional.empty() : repo.stream()
                .filter(c -> Objects.equals(c.key, key))
                .findAny()
                .or(() -> parent != null ? parent.fromRepo(key) : Optional.empty());
    }

    @Data
    public static class Session {
        public final DialogueBlob root;
        private DialogueBlob current;

        public void advance(String arg) {
            current = current.getChoice(arg);
        }

        public void exec(Player player) {
            for (var txt : current.content)
                player.sendMessage(Component.nullToEmpty(txt.toString()), player.getUUID());
            for (var e : current.choices.entrySet())
                player.sendMessage(Component.nullToEmpty(choiceToString(e)), player.getUUID());
        }
    }
}
