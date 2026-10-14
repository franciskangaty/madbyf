package dev.madbyf.main.institution.domain.model;

import java.util.UUID;

public record InstitutionBranding(
    UUID id,
    Institution institution,
    String nickname,
    String logoUrl,
    String primaryColor,
    String secondaryColor,
    SocialMediaLinks socialMediaLinks
) {
    
}
