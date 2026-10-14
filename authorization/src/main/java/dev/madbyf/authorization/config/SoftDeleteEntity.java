package dev.madbyf.authorization.config;

import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Data
@NoArgsConstructor
@Builder
@MappedSuperclass
@SoftDelete(columnName = "deleted", strategy = SoftDeleteType.TIMESTAMP)
public class SoftDeleteEntity {
    // TODO: implement column name
}
