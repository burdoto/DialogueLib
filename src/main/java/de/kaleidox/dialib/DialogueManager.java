package de.kaleidox.dialib;

import com.google.gson.Gson;
import de.kaleidox.dialib.dto.DialogueBlob;
import de.kaleidox.dialib.dto.DialogueBlob.Session;
import lombok.experimental.UtilityClass;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class DialogueManager {
    private final Map<String, DialogueBlob> cache = new ConcurrentHashMap<>();
    private final Map<UUID, Session> ongoing = new ConcurrentHashMap<>();

    public CompletableFuture<DialogueBlob> findByName(final String name) {
        if (cache.containsKey(name))
            return CompletableFuture.completedFuture(cache.get(name));
        var future = CompletableFuture.supplyAsync(() -> getByName(name));
        future.thenAccept(it -> cache.put(name, it));
        return future;
    }

    public DialogueBlob getByName(String name) {
        try (var data = new FileInputStream(DialogueLib.INSTANCE.config("dialogues/"+name+".json"))) {
            return parse(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Dialogue not found: " + name, e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read dialogue " + name, e);
        }
    }

    public DialogueBlob parse(InputStream data) {
        return new Gson().fromJson(new InputStreamReader(data), DialogueBlob.class);
    }

    public void start(DialogueBlob dialogue, Player player) {
        var session = new Session(dialogue, player, dialogue);
        ongoing.put(player.getUUID(), session);
        session.exec(player);
    }

    public void next(Player player, String arg) {
        var session = ongoing.get(player.getUUID());
        session.advance(arg);
        session.exec(player);
    }
}
