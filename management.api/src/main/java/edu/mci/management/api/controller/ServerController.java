package edu.mci.management.api.controller;

import edu.mci.management.api.dto.GameServerRegistration;
import edu.mci.management.api.dto.GameServerUpdate;
import edu.mci.management.api.dto.GameServer;
import edu.mci.management.api.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
@Tag(name = "Servers", description = "Endpoints for registering, updating, discovering, and removing game servers.")
public class ServerController {
    private final ServerService serverService;

    @GetMapping("/")
    @Operation(summary = "Retrieve active game servers", description = "Fetch a list of all currently registered and active game servers. A server is considered active if it has sent a heartbeat within the configured time frame.")
    public List<GameServer> getAvailableServers() {
        return new ArrayList<>(serverService.findAvailableServers());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new game server", description = "The initial configuration and contact information for the new game server.")
    public GameServer create(@Valid @RequestBody GameServerRegistration request) {
        GameServer server = new GameServer(request.getName(), request.getUri(), request.getMaxPlayers());
        return serverService.create(server);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update server status (heartbeat)")
    public GameServer update(@PathVariable int id, @Valid @RequestBody GameServerUpdate request) {
        return serverService.updateHeartbeat(id, request.getStatus(), request.getCurrentPlayerCount());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unregister a game server")
    public void delete(@PathVariable int id) {
        serverService.delete(id);
    }
}
