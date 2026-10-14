package dev.madbyf.authorization.config;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity extends AuditingEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
