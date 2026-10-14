package dev.madbyf.auth.user.command.api;

import java.time.LocalDate;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.madbyf.auth.user.command.api.commands.UserAccountAggregateCommand;
import dev.madbyf.auth.user.command.api.dtos.UserNamesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController 
@RequestMapping(path = "/api/{version}/users")
@RequiredArgsConstructor 
public class UserCommandController {

    private final CommandGateway commandGateway;

    @PutMapping(path = "/{userId}/names",version = "v1")
    public ResponseEntity<Void> updateUserNames(
        @PathVariable String userId, 
        @RequestBody UserNamesDTO userNamesDTO
    ) {
        // Create and send the command to update user names
        var command = new UserAccountAggregateCommand.UpdateNames(
            UUID.fromString(userId),
            userNamesDTO.firstName(),
            userNamesDTO.middleName(),
            userNamesDTO.lastName()
        );

        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/date-of-birth", version = "v1")
    public ResponseEntity<Void> updateDateOfBirth(
        @PathVariable String userId,
        @RequestBody LocalDate dateOfBirth
    ) {
        // Create and send the command to update date of birth
        var command = new UserAccountAggregateCommand.UpdateDateOfBirth(
            UUID.fromString(userId),
            dateOfBirth
        );

        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/password", version = "v1")
    public ResponseEntity<Void> updatePassword(
        @PathVariable String userId,
        @RequestBody UserAccountAggregateCommand.UpdatePassword updatePasswordRequest
    ) {
        // Create and send the command to update password
        var command = new UserAccountAggregateCommand.UpdatePassword(
            UUID.fromString(userId),
            updatePasswordRequest.oldPasswordHash(),
            updatePasswordRequest.newPasswordHash()
        );

        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/profile-photo", version = "v1")
    public ResponseEntity<Void> setProfilePhotoUrl(
        @PathVariable String userId,
        @RequestBody String profilePhotoUrl
    ) {
        var command = new UserAccountAggregateCommand.SetProfilePhotoUrl(
            UUID.fromString(userId),
            profilePhotoUrl
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{userId}/verify/name", version = "v1")
    public ResponseEntity<Void> verifyName(@RequestBody String verificationMethod, @PathVariable String userId) {
        //TODO: modify command to include verification attachment (e.g., document/ID-check backed)

        var command = new UserAccountAggregateCommand.VerifyName(
            UUID.fromString(userId),
            verificationMethod
        );
        commandGateway.send(command);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping(path = "/{userId}/verify/age", version = "v1")
    public ResponseEntity<Void> verifyAge(
        @PathVariable String userId, 
        @RequestBody String verificationMethod
    ) {
        var command = new UserAccountAggregateCommand.VerifyAge(
            UUID.fromString(userId),
            verificationMethod
        );
        commandGateway.send(command);

        return ResponseEntity.ok().build();
    }
    
    @PostMapping(path = "/{userId}/password", version = "v1")
    public ResponseEntity<Void> setPassword(
        @PathVariable String userId,
        @RequestBody String password
    ) {
        var command = new UserAccountAggregateCommand.SetPassword(
            UUID.fromString(userId),
            password
        );
        commandGateway.send(command); 
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{userId}/password", version = "v1")
    public ResponseEntity<Void> removePassword(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.RemovePassword(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/enable", version = "v1")
    public ResponseEntity<Void> enableUser(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.Enable(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/disable", version = "v1")
    public ResponseEntity<Void> disableUser(
        @PathVariable String userId,
        @RequestBody String reason
    ) {
        var command = new UserAccountAggregateCommand.Disable(
            UUID.fromString(userId),
            reason
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping(path = "/{userId}/lock", version = "v1")
    public ResponseEntity<Void> lockUser(
        @PathVariable String userId,
        @RequestBody String reason
    ) {
        var command = new UserAccountAggregateCommand.Lock(
            UUID.fromString(userId),
            reason
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/unlock", version = "v1")
    public ResponseEntity<Void> unlockUser(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.Unlock(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/expire-credentials", version = "v1")
    public ResponseEntity<Void> expireCredentials(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.ExpireCredentials(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/renew-credentials", version = "v1")
    public ResponseEntity<Void> renewCredentials(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.RenewCredentials(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/expire-account", version = "v1")
    public ResponseEntity<Void> expireAccount(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.ExpireAccount(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userId}/reactivate", version = "v1")
    public ResponseEntity<Void> reactivateUser(@PathVariable String userId) {
        var command = new UserAccountAggregateCommand.ReactivateAccount(
            UUID.fromString(userId)
        );
        commandGateway.send(command);
        return ResponseEntity.ok().build();
    }

}