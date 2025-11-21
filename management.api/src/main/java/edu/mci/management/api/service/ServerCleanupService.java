package edu.mci.management.api.service;

import edu.mci.management.api.domain.GameServer;
import edu.mci.management.api.repo.ServerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class ServerCleanupService {
    private final ServerRepository serverRepository;

    public ServerCleanupService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void cleanup() {
        Instant cutoff = Instant.now().minusSeconds(15);
        List<GameServer> staleServers = serverRepository.findAllByLastSeenBefore(cutoff);

        if (staleServers.isEmpty()) {
            return;
        }

        for (GameServer server : staleServers) {
            log.info("Remove inactive server: {} ({}) - last heartbeat: {}", server.getName(), server.getId(), server.getLastSeen());
        }

        serverRepository.deleteAllInBatch(staleServers);
    }
}
