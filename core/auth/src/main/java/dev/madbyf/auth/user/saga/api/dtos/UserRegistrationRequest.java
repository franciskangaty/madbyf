package dev.madbyf.auth.user.saga.api.dtos;

import java.time.LocalDate;
import java.util.Set;

import dev.madbyf.auth.common.util.Contact;

public record UserRegistrationRequest(
    String firstName,
    String middleName,
    String lastName,
    String username,
    Set<Contact> contacts,
    LocalDate dateOfBirth,
    String password,
    String confirmPassword
) {
}