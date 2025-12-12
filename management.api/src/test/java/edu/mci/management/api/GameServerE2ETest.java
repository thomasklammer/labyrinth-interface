package edu.mci.management.api;

import edu.mci.management.api.generated.dto.GameServer;
import edu.mci.management.api.generated.dto.GameServerRegistration;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import edu.mci.management.api.generated.dto.GameServerUpdate.StatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GameServerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void completeServerLifecycle_ShouldWork() {
        String baseUrl = "http://localhost:" + port + "/servers";

        GameServerRegistration registration = new GameServerRegistration()
                .name("E2E-Test-Server")
                .uri("udp://127.0.0.1:9987")
                .maxPlayers(64);

        ResponseEntity<GameServer> createResponse = restTemplate.postForEntity(
                baseUrl,
                registration,
                GameServer.class
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID serverId = createResponse.getBody().getId();

        ResponseEntity<GameServer[]> listResponse = restTemplate.getForEntity(
                baseUrl,
                GameServer[].class
        );

        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(listResponse.getBody())
                .extracting(GameServer::getId)
                .contains(serverId);


        GameServerUpdate update = new GameServerUpdate()
                .currentPlayerCount(10)
                .status(StatusEnum.IN_GAME);

        HttpEntity<GameServerUpdate> requestEntity = new HttpEntity<>(update);

        ResponseEntity<GameServer> updateResponse = restTemplate.exchange(
                baseUrl + "/" + serverId,
                HttpMethod.PUT,
                requestEntity,
                GameServer.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getCurrentPlayerCount()).isEqualTo(10);
        assertThat(updateResponse.getBody().getStatus().toString()).isEqualTo("IN_GAME");

        restTemplate.delete(baseUrl + "/" + serverId);

        ResponseEntity<GameServer[]> listResponseAfterDelete = restTemplate.getForEntity(
                baseUrl,
                GameServer[].class
        );

        assertThat(listResponseAfterDelete.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(listResponseAfterDelete.getBody())
                .extracting(GameServer::getId)
                .doesNotContain(serverId);
    }
}