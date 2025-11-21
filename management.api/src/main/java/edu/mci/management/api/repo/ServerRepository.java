package edu.mci.management.api.repo;

import edu.mci.management.api.domain.GameServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ServerRepository extends JpaRepository<GameServer, UUID> {

    @Query("SELECT s FROM GameServer s WHERE  s.currentPlayerCount < s.maxPlayers")
    List<GameServer> findAvailableServers();

    List<GameServer> findAllByLastSeenBefore(Instant lastSeen);

}
