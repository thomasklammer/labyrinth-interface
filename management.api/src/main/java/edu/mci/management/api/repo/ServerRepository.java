package edu.mci.management.api.repo;

import edu.mci.management.api.dto.GameServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServerRepository extends JpaRepository<GameServer, Integer> {

    @Query("SELECT s FROM GameServer s WHERE  s.currentPlayerCount < s.maxPlayers")
    List<GameServer> findAvailableServers();
}
