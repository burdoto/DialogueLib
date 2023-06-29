package de.kaleidox.dialib.dto;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
public class DialogueContent {
    public @NotNull String text;
    public @Builder.Default long printDuration = 500;
    public @Builder.Default long waitDuration = 250;
}
