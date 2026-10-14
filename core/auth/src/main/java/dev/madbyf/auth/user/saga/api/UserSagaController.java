package dev.madbyf.auth.user.saga.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.madbyf.auth.user.saga.api.commands.UserRegistrationCommand;
import dev.madbyf.auth.user.saga.api.dtos.UserRegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping(path = "/api/{}/user")
@RequiredArgsConstructor 
public class UserSagaController {

    private final CommandGateway commandGateway;

    @PostMapping(path = "/register", version = "1.0")
    public ResponseEntity<Void> registerUser(
        @RequestBody UserRegistrationRequest request,
        HttpServletRequest httpRequest
    ) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords do not match"
            );
        }

        var registrationId = UUID.randomUUID();

        commandGateway.send(new UserRegistrationCommand.StartRegistration(
            registrationId,
            request.firstName(),
            request.middleName(),
            request.lastName(),
            request.username(),
            request.contacts(),
            request.dateOfBirth(),
            request.password(),
            httpRequest.getRemoteAddr(),
            httpRequest.getHeader("User-Agent")
        ));

        return ResponseEntity.accepted().build();
    }
	
}