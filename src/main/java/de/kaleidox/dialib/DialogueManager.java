package de.kaleidox.dialib;

import com.google.gson.Gson;
import de.kaleidox.dialib.dto.DialogueBlob;
import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class DialogueManager {
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
