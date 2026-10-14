package dev.madbyf.authorization.user.domain.repository;

import dev.madbyf.authorization.user.domain.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    boolean existsByValue(String value);

    boolean existsByValueAndIdNot(String value, UUID id);

    List<Contact> findByUser_Id(UUID userId);

    List<Contact> findByUser_IdIn(Collection<UUID> userIds);

    Optional<Contact> findByIdAndUser_Id(UUID id, UUID userId);
}
