package edu.mci.management.api.mapper;

import edu.mci.management.api.generated.dto.GameServer;
import edu.mci.management.api.generated.dto.GameServerRegistration;
import edu.mci.management.api.generated.dto.GameServerUpdate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServerMapper
{
    /**
     * Converts a {@link edu.mci.management.api.domain.GameServer} domain entity into a
     * {@link GameServer} DTO (Data Transfer Object).
     *
     * @param domain The GameServer domain entity to convert.
     * @return The corresponding GameServer DTO, or {@code null} if the input was {@code null}.
     */
    public GameServer toDto(edu.mci.management.api.domain.GameServer domain)
    {
        if (domain == null)
        {
            return null;
        }

        GameServer dto = new GameServer();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setUri(domain.getUri());
        dto.setMaxPlayers(domain.getMaxPlayers());
        dto.setStatus(toDtoStatus(domain.getStatus()));
        dto.setCurrentPlayerCount(domain.getCurrentPlayerCount());
        if (domain.getLastSeen() != null)
        {
            dto.setLastSeen(domain.getLastSeen().atOffset(ZoneOffset.UTC));
        }
        return dto;
    }

    /**
     * Converts a list of {@link edu.mci.management.api.domain.GameServer} domain entities
     * into a list of {@link GameServer} DTOs.
     *
     * @param domainList The list of domain entities to convert.
     * @return A list of corresponding GameServer DTOs.
     */
    public List<GameServer> toDtoList(List<edu.mci.management.api.domain.GameServer> domainList)
    {
        return domainList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new domain entity from a {@link GameServerRegistration} DTO.
     * <p>
     * Used in the 'createServer' flow.
     *
     * @param dto The registration DTO containing the initial server data.
     * @return A new {@link edu.mci.management.api.domain.GameServer} entity, ready to be saved.
     */
    public edu.mci.management.api.domain.GameServer toDomain(GameServerRegistration dto)
    {
        if (dto == null)
        {
            return null;
        }
        edu.mci.management.api.domain.GameServer domain = new edu.mci.management.api.domain.GameServer(
                dto.getName(),
                dto.getUri(),
                dto.getMaxPlayers());
        return domain;
    }

    /**
     * Updates an existing domain entity with data from a {@link GameServerUpdate} DTO.
     * <p>
     * Used in the 'updateServer' (heartbeat) flow. This method modifies the passed-in entity.
     *
     * @param dto The update DTO containing the new status and player count.
     * @param domainToUpdate The existing domain entity (e.g., fetched from the DB) to be updated.
     */
    public void updateDomainFromDto(GameServerUpdate dto, edu.mci.management.api.domain.GameServer domainToUpdate)
    {
        if (dto == null || domainToUpdate == null)
        {
            return;
        }
        domainToUpdate.setCurrentPlayerCount(dto.getCurrentPlayerCount());
        domainToUpdate.setStatus(toDomainStatus(dto.getStatus()));
    }

    /**
     * Converts the domain status enum (GameServerState) to the DTO status enum (GameServer.StatusEnum).
     *
     * @param domainStatus The status from the domain entity.
     * @return The corresponding {@link GameServer.StatusEnum} for the DTO.
     */
    private GameServer.StatusEnum toDtoStatus(edu.mci.management.api.domain.GameServerState domainStatus)
    {
        if (domainStatus == null)
        {
            return null;
        }
        return GameServer.StatusEnum.valueOf(domainStatus.name());
    }

    /**
     * Converts the DTO status enum (GameServerUpdate.StatusEnum) to the domain status enum (GameServerState).
     *
     * @param dtoStatus The status from the {@link GameServerUpdate} DTO.
     * @return The corresponding {@link edu.mci.management.api.domain.GameServerState} for the domain.
     */
    public edu.mci.management.api.domain.GameServerState toDomainStatus(GameServerUpdate.StatusEnum dtoStatus)
    {
        if (dtoStatus == null)
        {
            return null;
        }
        return edu.mci.management.api.domain.GameServerState.valueOf(dtoStatus.name());
    }
}