package dev.madbyf.authorization.authorization.api;

import dev.madbyf.authorization.authorization.api.dto.AuthorizationGrantTypeValue;
import dev.madbyf.authorization.authorization.api.dto.ClientAuthenticationMethodType;
import dev.madbyf.authorization.authorization.api.dto.ClientRegistrationRequest;
import dev.madbyf.authorization.authorization.api.dto.ClientResponse;
import dev.madbyf.authorization.authorization.api.dto.ClientSearchCriteria;
import dev.madbyf.authorization.authorization.api.dto.ClientUpdateRequest;
import dev.madbyf.authorization.authorization.domain.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ClientRegistrationRequest request) {
        clientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable String id,
            @Valid @RequestBody ClientUpdateRequest request
    ) {
        clientService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        clientService.delete(id);
    }

    @GetMapping("/{id}")
    public ClientResponse get(@PathVariable String id) {
        return clientService.get(id);
    }

    @GetMapping
    public Page<ClientResponse> getAll(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) ClientAuthenticationMethodType clientAuthenticationMethod,
            @RequestParam(required = false) AuthorizationGrantTypeValue authorizationGrantType,
            @RequestParam(required = false) String scope,
            @ParameterObject
            @PageableDefault(size = 20, sort = "clientId") Pageable pageable
    ) {
        var criteria = new ClientSearchCriteria(
                clientId,
                clientName,
                clientAuthenticationMethod,
                authorizationGrantType,
                scope
        );
        return clientService.getAll(criteria, pageable);
    }
}
