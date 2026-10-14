package dev.madbyf.authorization.authorization.domain.repository;

import dev.madbyf.authorization.authorization.domain.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends
        JpaRepository<Client, String>,
        JpaSpecificationExecutor<Client> {

    Optional<Client> findByClientId(String clientId);

    boolean existsByClientId(String clientId);

    boolean existsByClientIdAndIdNot(String clientId, String id);
}
// public interface ClientRepository extends JpaRepository<Client, String> {
//     Optional<Client> findByClientId(String clientId);

//     boolean existsByClientId(String clientId);

//     boolean existsByClientIdAndIdNot(String clientId, String id);

//     @Query("""
//         select c
//         from Client c
//         where (:clientId is null or lower(c.clientId) like lower(concat('%', :clientId, '%')))
//             and (:clientName is null or lower(c.clientName) like lower(concat('%', :clientName, '%')))
//             and (:clientAuthenticationMethod is null or lower(c.clientAuthenticationMethods) like lower(concat('%', :clientAuthenticationMethod, '%')))
//             and (:authorizationGrantType is null or lower(c.authorizationGrantTypes) like lower(concat('%', :authorizationGrantType, '%')))
//             and (:scope is null or lower(c.scopes) like lower(concat('%', :scope, '%')))
//     """)
//     Page<Client> search(
//             @Param("clientId") String clientId,
//             @Param("clientName") String clientName,
//             @Param("clientAuthenticationMethod") String clientAuthenticationMethod,
//             @Param("authorizationGrantType") String authorizationGrantType,
//             @Param("scope") String scope,
//             Pageable pageable
//     );
// }
