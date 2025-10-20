package edu.mci.management.api.service;

import edu.mci.management.api.dto.GameServer;
import edu.mci.management.api.dto.GameServerState;
import edu.mci.management.api.repo.ServerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public List<GameServer> findAvailableServers() {
        return serverRepository.findAvailableServers();
    }

    public GameServer findById(int id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    public GameServer create(GameServer server) {
        return serverRepository.save(server);
    }

    public GameServer updateHeartbeat(int id, GameServerState status, int connectedPlayers) {
        GameServer existing = findById(id);

        existing.setCurrentPlayerCount(connectedPlayers);
        existing.setStatus(status);
        existing.setLastSeen(LocalDateTime.now());

        return serverRepository.save(existing);
    }


    public void delete(int id) {
        serverRepository.deleteById(id);
    }
}
