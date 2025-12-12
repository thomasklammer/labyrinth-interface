package edu.mci.management.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mci.management.api.domain.GameServer;
import edu.mci.management.api.generated.dto.GameServerRegistration;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import edu.mci.management.api.mapper.ServerMapper;
import edu.mci.management.api.service.ServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServerController.class)
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServerService serverService;

    @MockitoBean
    private ServerMapper serverMapper;


    @Test
    void createServer_ShouldReturn201_WhenValidInput() throws Exception {
        var registrationDto = new GameServerRegistration()
                .name("TestServer")
                .maxPlayers(10)
                .uri("ws://testserver.example.com");

        var domainServer = new GameServer();
        domainServer.setId(UUID.randomUUID());

        var responseDto = new edu.mci.management.api.generated.dto.GameServer();
        responseDto.setId(domainServer.getId());
        responseDto.setName("TestServer");

        when(serverMapper.toDomain(any(GameServerRegistration.class))).thenReturn(domainServer);
        when(serverService.create(any(GameServer.class))).thenReturn(domainServer);
        when(serverMapper.toDto(any(GameServer.class))).thenReturn(responseDto);

        mockMvc.perform(post("/servers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestServer"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void listServers_ShouldReturn200_AndList() throws Exception {
        var domainList = List.of(new GameServer(), new GameServer());
        var dtoList = List.of(
                new edu.mci.management.api.generated.dto.GameServer(),
                new edu.mci.management.api.generated.dto.GameServer()
        );

        when(serverService.findAvailableServers()).thenReturn(domainList);
        when(serverMapper.toDtoList(domainList)).thenReturn(dtoList);

        mockMvc.perform(get("/servers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateServer_ShouldReturn200_WhenUpdated() throws Exception {
        UUID id = UUID.randomUUID();
        var updateDto = new GameServerUpdate()
                .currentPlayerCount(5)
                .status(GameServerUpdate.StatusEnum.LOBBY);
        var domainServer = new GameServer();
        var responseDto = new edu.mci.management.api.generated.dto.GameServer();

        when(serverService.updateHeartbeat(eq(id), any(GameServerUpdate.class))).thenReturn(domainServer);
        when(serverMapper.toDto(domainServer)).thenReturn(responseDto);

        mockMvc.perform(put("/servers/{id}", id) // or patch, depending on your API definition
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        verify(serverService).updateHeartbeat(eq(id), any(GameServerUpdate.class));
    }

    @Test
    void deleteServer_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/servers/{id}", id))
                .andExpect(status().isNoContent());

        verify(serverService).delete(id);
    }
}