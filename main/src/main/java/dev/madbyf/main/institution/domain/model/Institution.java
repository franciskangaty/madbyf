package dev.madbyf.main.institution.domain.model;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record Institution(
   UUID id,
   String name,
   String shortName,
   InstitutionType type,
   String registrationNumber,
   String taxNumber,
   Set<Address> address,
   Set<Contact> contact,
   // String email,
   // String phoneNumber,
   String website,
   String logoUrl,
   String description,
   Instant createdAt,
   String createdBy,
   Instant updatedAt,
   String updatedBy
) {
}