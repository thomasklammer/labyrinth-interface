package edu.mci.management.api.controller;

import edu.mci.management.api.generated.dto.GameServer;
import edu.mci.management.api.generated.controller.ServersApiDelegate;
import edu.mci.management.api.generated.dto.GameServerRegistration;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import edu.mci.management.api.mapper.ServerMapper;
import edu.mci.management.api.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ServerController implements ServersApiDelegate
{
    private final ServerService serverService;
    private final ServerMapper serverMapper;
    private static final Logger log = LoggerFactory.getLogger(ServerController.class);

    @Override
    public ResponseEntity<GameServer> createServer(GameServerRegistration request)
    {
        log.info("Creating GameServer received: Name='{}', URI='{}', MaxPlayers={}",
                request.getName(),
                request.getUri(),
                request.getMaxPlayers()
        );
        var gameServer = serverMapper.toDomain(request);
        var createdServer = serverService.create(gameServer);
        log.info("GameServer created: Id='{}', Name='{}', URI='{}', MaxPlayers={}, Status='{}', LastSeen='{}'",
                createdServer.getId(),
                createdServer.getName(),
                createdServer.getUri(),
                createdServer.getMaxPlayers(),
                createdServer.getStatus(),
                createdServer.getLastSeen()
        );
        var responseDto = serverMapper.toDto(createdServer);
        return new ResponseEntity<GameServer>(responseDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GameServer> updateServer(UUID serverId, GameServerUpdate request)
    {
        log.info("Update GameServer received: Id='{}', Status='{}', CurrentPlayerCount='{}'",
                serverId,
                request.getStatus(),
                request.getCurrentPlayerCount()
        );
        var updatedGameServer = serverService
                .updateHeartbeat(
                        serverId,
                        request);
        var responseDto = serverMapper.toDto(updatedGameServer);
        log.info("GameServer updated: Id='{}', Name='{}', URI='{}', MaxPlayers={}, Status='{}', LastSeen='{}'",
                updatedGameServer.getId(),
                updatedGameServer.getName(),
                updatedGameServer.getUri(),
                updatedGameServer.getMaxPlayers(),
                updatedGameServer.getStatus(),
                updatedGameServer.getLastSeen()
        );
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<Void> deleteServer(UUID serverId)
    {
        log.info("Delete GameServer received Id: {}", serverId);
        serverService.delete(serverId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<GameServer>> listServers()
    {
        var gameServers = serverService.findAvailableServers();
        var responseDto = serverMapper.toDtoList(gameServers);
        return ResponseEntity.ok(responseDto);
    }
}