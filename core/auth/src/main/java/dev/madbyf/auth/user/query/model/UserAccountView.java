package dev.madbyf.auth.user.query.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity 
@Table(name = "user_accounts")
public class UserAccountView {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "name_verified", nullable = false)
    private boolean nameVerified;

    @Column(name = "age_verified", nullable = false)
    private boolean ageVerified;

    @Column(name = "password_set", nullable = false)
    private boolean passwordSet;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "suspended", nullable = false)
    private boolean suspended;

    @Column(name = "credential_expired", nullable = false)
    private boolean credentialExpired;

    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    public UserAccountView(
            UUID userId,
            String firstName,
            String middleName,
            String lastName,
            LocalDate dateOfBirth) {

        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;

        this.nameVerified = false;
        this.ageVerified = false;
        this.passwordSet = false;

        this.active = true;
        this.suspended = false;
        this.credentialExpired = false;
        this.accountExpired = false;
        this.locked = false;
    }

    public void updateNames(
            String firstName,
            String middleName,
            String lastName) {

        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public void updateDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public void markNameVerified() {
        this.nameVerified = true;
    }

    public void markAgeVerified() {
        this.ageVerified = true;
    }

    public void markPasswordSet() {
        this.passwordSet = true;
    }

    public void markPasswordRemoved() {
        this.passwordSet = false;
    }

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public void expireCredentials() {
        this.credentialExpired = true;
    }

    public void renewCredentials() {
        this.credentialExpired = false;
    }

    public void expireAccount() {
        this.accountExpired = true;
    }

    public void reactivateAccount() {
        this.accountExpired = false;
    }
}
