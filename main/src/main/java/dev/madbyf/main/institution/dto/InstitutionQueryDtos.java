package dev.madbyf.main.institution.dto;

import java.util.UUID;

import dev.madbyf.main.institution.view.InstitutionStatus;

public class InstitutionQueryDtos {

   public record InstitutionSearchRequest(
           String registrationNumber,
           String legalName,
           String displayName,
           String country,
           String city,
           String ownershipType,
           InstitutionStatus status,
           Boolean registrationVerified,
           Integer page,
           Integer size,
           String sortBy,
           String direction
   ){}

   public record InstitutionResponse(
           UUID id,
           String registrationNumber,
           String legalName,
           String displayName,
           InstitutionStatus status,
           String country,
           String city
   
   ){}
}