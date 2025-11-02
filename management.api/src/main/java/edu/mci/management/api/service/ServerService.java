package edu.mci.management.api.service;


import edu.mci.management.api.domain.GameServer;
import edu.mci.management.api.domain.GameServerState;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import edu.mci.management.api.mapper.ServerMapper;
import edu.mci.management.api.repo.ServerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerService
{
    private final ServerMapper serverMapper;
    private final ServerRepository serverRepository;

    public List<GameServer> findAvailableServers()
    {
        return serverRepository.findAvailableServers();
    }

    public GameServer findById(UUID id)
    {
        return serverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GameServer not found"));
    }

    public GameServer create(GameServer server)
    {
        return serverRepository.save(server);
    }

    public GameServer updateHeartbeat(UUID id, GameServerUpdate gameServerUpdate)
    {
        var gameServer = findById(id);
        gameServer.setCurrentPlayerCount(gameServerUpdate.getCurrentPlayerCount());
        gameServer.setStatus(serverMapper.toDomainStatus(gameServerUpdate.getStatus()));
        gameServer.setLastSeen(Instant.now());
        return serverRepository.save(gameServer);
    }


    public void delete(UUID id)
    {
        serverRepository.deleteById(id);
    }
}
