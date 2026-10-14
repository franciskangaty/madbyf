package dev.madbyf.main.institution.query;

import dev.madbyf.main.institution.view.InstitutionStatus;

public final class InstitutionQueries {

    public record FindInstitutions(
            InstitutionFilter filter,
            int page,
            int size,
            String sortBy,
            SortDirection direction
    ){}

    public record FindInstitution(
            LookupType type,
            String value
    ){}

    public record InstitutionFilter(
            String registrationNumber,
            String legalName,
            String displayName,
            String country,
            String city,
            String ownershipType,
            InstitutionStatus status,
            Boolean registrationVerified
    ){}

    public enum LookupType{
        ID,
        REGISTRATION_NUMBER,
        LEGAL_NAME,
        EMAIL
    }

    public enum SortDirection{
        ASC,
        DESC
    }

}