package dev.madbyf.authorization.user.domain.model;

import dev.madbyf.authorization.security.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "contacts")
public class Contact extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactType type;
    @Column(nullable = false, unique = true)
    private String value;
    private boolean verified;
    @ManyToOne
    private User user;
}
