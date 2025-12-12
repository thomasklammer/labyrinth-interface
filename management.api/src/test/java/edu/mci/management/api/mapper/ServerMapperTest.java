package edu.mci.management.api.mapper;

import edu.mci.management.api.domain.GameServerState;
import edu.mci.management.api.generated.dto.GameServer;
import edu.mci.management.api.generated.dto.GameServerRegistration;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerMapperTest {

    private ServerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServerMapper();
    }


    @Test
    void toDto_ShouldMapAllFieldsCorrectly() {
        var domain = new edu.mci.management.api.domain.GameServer("TestName", "http://test.uri", 100);
        domain.setId(UUID.randomUUID());
        domain.setCurrentPlayerCount(42);
        domain.setStatus(GameServerState.IN_GAME);
        Instant now = Instant.now();
        domain.setLastSeen(now);

        GameServer dto = mapper.toDto(domain);

        assertThat(dto.getId()).isEqualTo(domain.getId());
        assertThat(dto.getName()).isEqualTo("TestName");
        assertThat(dto.getUri()).isEqualTo("http://test.uri");
        assertThat(dto.getMaxPlayers()).isEqualTo(100);
        assertThat(dto.getCurrentPlayerCount()).isEqualTo(42);
        assertThat(dto.getStatus().toString()).isEqualTo("IN_GAME");
        assertThat(dto.getLastSeen()).isEqualTo(now.atOffset(ZoneOffset.UTC));
    }

    @Test
    void toDto_ShouldReturnNull_WhenInputIsNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    void toDto_ShouldHandleNullLastSeen() {
        var domain = new edu.mci.management.api.domain.GameServer();
        domain.setLastSeen(null);

        GameServer dto = mapper.toDto(domain);

        assertThat(dto).isNotNull();
        assertThat(dto.getLastSeen()).isNull();
    }


    @Test
    void toDtoList_ShouldMapList() {
        var domain1 = new edu.mci.management.api.domain.GameServer("S1", "u1", 10);
        var domain2 = new edu.mci.management.api.domain.GameServer("S2", "u2", 20);
        List<edu.mci.management.api.domain.GameServer> list = List.of(domain1, domain2);

        List<GameServer> dtoObjects = mapper.toDtoList(list);

        assertThat(dtoObjects).hasSize(2);
        assertThat(dtoObjects.get(0).getName()).isEqualTo("S1");
        assertThat(dtoObjects.get(1).getName()).isEqualTo("S2");
    }


    @Test
    void toDomain_ShouldMapRegistrationDto() {

        var regDto = new GameServerRegistration()
                .name("NewServer")
                .uri("http://new.uri")
                .maxPlayers(50);


        var domain = mapper.toDomain(regDto);


        assertThat(domain.getName()).isEqualTo("NewServer");
        assertThat(domain.getUri()).isEqualTo("http://new.uri");
        assertThat(domain.getMaxPlayers()).isEqualTo(50);
        assertThat(domain.getId()).isNull();
    }

    @Test
    void toDomain_ShouldReturnNull_WhenInputIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }


    @Test
    void updateDomainFromDto_ShouldUpdateFields() {
        var domain = new edu.mci.management.api.domain.GameServer();
        domain.setCurrentPlayerCount(0);
        domain.setStatus(GameServerState.LOBBY);

        var updateDto = new GameServerUpdate()
                .currentPlayerCount(5)
                .status(GameServerUpdate.StatusEnum.IN_GAME);


        mapper.updateDomainFromDto(updateDto, domain);


        assertThat(domain.getCurrentPlayerCount()).isEqualTo(5);
        assertThat(domain.getStatus()).isEqualTo(GameServerState.IN_GAME);
    }

    @Test
    void updateDomainFromDto_ShouldDoNothing_WhenInputIsNull() {
        var domain = new edu.mci.management.api.domain.GameServer();
        domain.setCurrentPlayerCount(10);

        mapper.updateDomainFromDto(null, domain);

        assertThat(domain.getCurrentPlayerCount()).isEqualTo(10);
    }


    @Test
    void toDomainStatus_ShouldMapEnumCorrectly() {
        assertThat(mapper.toDomainStatus(GameServerUpdate.StatusEnum.LOBBY))
                .isEqualTo(GameServerState.LOBBY);
    }

    @Test
    void toDomainStatus_ShouldReturnNull_WhenNull() {
        assertThat(mapper.toDomainStatus(null)).isNull();
    }
}