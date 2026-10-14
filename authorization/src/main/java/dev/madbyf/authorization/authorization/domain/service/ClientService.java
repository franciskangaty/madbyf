package dev.madbyf.authorization.authorization.domain.service;

import dev.madbyf.authorization.authorization.api.dto.ClientRegistrationRequest;
import dev.madbyf.authorization.authorization.api.dto.ClientResponse;
import dev.madbyf.authorization.authorization.api.dto.ClientSearchCriteria;
import dev.madbyf.authorization.authorization.api.dto.ClientUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    void create(ClientRegistrationRequest request);

    void update(String id, ClientUpdateRequest request);

    void delete(String id);

    ClientResponse get(String id);

    Page<ClientResponse> getAll(ClientSearchCriteria criteria, Pageable pageable);
}
