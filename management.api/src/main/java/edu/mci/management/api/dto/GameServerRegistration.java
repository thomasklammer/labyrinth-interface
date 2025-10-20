package edu.mci.management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameServerRegistration {
    @NotBlank
    @Schema(example = "MCI Pro Labyrinth", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(example = "ws://play.example.com:9000 oder ws:192.168.0.1:9000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uri;

    @Min(1)
    @Schema(minimum = "1", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private int maxPlayers;
}
