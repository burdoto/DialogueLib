package de.kaleidox.dialib.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DialogueBlob {
    public @Nullable String key;
    public @Nullable String desc;
    public @Nullable String title;
    public List<DialogueBlob> repo;
    public @Singular("content") List<String> content;
    public @Singular Map<String, DialogueBlob> choices;
}
