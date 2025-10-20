package edu.mci.management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameServerUpdate {
    @Schema(example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private int currentPlayerCount;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private GameServerState status;
}
