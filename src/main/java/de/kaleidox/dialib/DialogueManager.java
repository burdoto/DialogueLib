package de.kaleidox.dialib;

import com.google.gson.Gson;
import de.kaleidox.dialib.dto.DialogueBlob;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class DialogueManager {
    private final Map<String, DialogueBlob> cache = new ConcurrentHashMap<>();

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
}
