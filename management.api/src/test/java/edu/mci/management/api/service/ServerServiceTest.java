package edu.mci.management.api.service;

import edu.mci.management.api.domain.GameServer;
import edu.mci.management.api.domain.GameServerState;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import edu.mci.management.api.mapper.ServerMapper;
import edu.mci.management.api.repo.ServerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ServerMapper serverMapper;

    @InjectMocks
    private ServerService serverService;


    @Test
    void findById_ShouldReturnServer_WhenExists() {
        UUID id = UUID.randomUUID();
        GameServer mockServer = new GameServer();
        mockServer.setId(id);

        when(serverRepository.findById(id)).thenReturn(Optional.of(mockServer));

        GameServer result = serverService.findById(id);

        assertThat(result).isEqualTo(mockServer);
        verify(serverRepository).findById(id);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(serverRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serverService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("GameServer not found");
    }


    @Test
    void updateHeartbeat_ShouldUpdateFieldsAndSave() {
        UUID id = UUID.randomUUID();

        GameServer existingServer = new GameServer();
        existingServer.setId(id);
        existingServer.setLastSeen(Instant.now().minusSeconds(100)); // Alt


        GameServerUpdate updateDto = new GameServerUpdate();
        updateDto.setCurrentPlayerCount(42);


        when(serverRepository.findById(id)).thenReturn(Optional.of(existingServer));
        when(serverMapper.toDomainStatus(any())).thenReturn(GameServerState.IN_GAME);
        when(serverRepository.save(any(GameServer.class))).thenAnswer(i -> i.getArguments()[0]);


        GameServer updatedServer = serverService.updateHeartbeat(id, updateDto);


        assertThat(updatedServer.getCurrentPlayerCount()).isEqualTo(42);
        assertThat(updatedServer.getStatus()).isEqualTo(GameServerState.IN_GAME);

        assertThat(updatedServer.getLastSeen()).isAfter(Instant.now().minusSeconds(1));

        verify(serverRepository).save(existingServer);
        verify(serverMapper).toDomainStatus(any());
    }

    @Test
    void updateHeartbeat_ShouldThrowException_WhenServerNotFound() {
        UUID id = UUID.randomUUID();
        when(serverRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serverService.updateHeartbeat(id, new GameServerUpdate()))
                .isInstanceOf(EntityNotFoundException.class);

        verify(serverRepository, never()).save(any());
    }


    @Test
    void findAvailableServers_ShouldDelegateToRepo() {
        List<GameServer> mockList = List.of(new GameServer(), new GameServer());
        when(serverRepository.findAvailableServers()).thenReturn(mockList);

        List<GameServer> result = serverService.findAvailableServers();

        assertThat(result).hasSize(2);
        verify(serverRepository).findAvailableServers();
    }


    @Test
    void delete_ShouldCallRepo() {
        UUID id = UUID.randomUUID();
        serverService.delete(id);
        verify(serverRepository).deleteById(id);
    }
}