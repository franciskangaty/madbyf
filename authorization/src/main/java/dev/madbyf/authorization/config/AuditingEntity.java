package dev.madbyf.authorization.config;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingEntity {
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;
    @LastModifiedBy
    private String updatedBy;
    @LastModifiedDate
    private Instant updatedAt;
}
