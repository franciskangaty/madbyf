package dev.madbyf.authorization.user.domain.repository;

import dev.madbyf.authorization.user.domain.model.User;
import dev.madbyf.authorization.user.domain.model.ContactType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>,
                JpaSpecificationExecutor<User> {

    boolean existsByUsername(String username);

    @Query("select count(u) > 0 from User u join u.roles r where r = :role")
    boolean existsByRoles(@Param("role") String role);

    @Query("""
        select u
        from User u
        where u.username = :value
            or exists (
                select c.id
                from Contact c
                where c.user = u
                    and c.value = :value
            )
    """)
    Optional<User> findByUsernameOrContactValue(@Param("value") String value);
}
// public interface UserRepository extends JpaRepository<User, UUID> {

//     boolean existsByUsername(String username);

//     @Query("select count(u) > 0 from User u join u.roles r where r = :role")
//     boolean existsByRoles(@Param("role") String role);

//     @Query("""
//         select u
//         from User u
//         where u.username = :value
//             or exists (
//                 select c.id
//                 from Contact c
//                 where c.user = u
//                     and c.value = :value
//             )
//     """)
//     Optional<User> findByUsernameOrContactValue(@Param("value") String value);

//     @Query(
//             value = """
//                 select distinct u
//                 from User u
//                 left join u.roles userRole
//                 where (:username is null or lower(u.username) like lower(concat('%', :username, '%')))
//                     and (:firstName is null or lower(u.firstName) like lower(concat('%', :firstName, '%')))
//                     and (:lastName is null or lower(u.lastName) like lower(concat('%', :lastName, '%')))
//                     and (:role is null or userRole = :role)
//                     and (:enabled is null or u.enabled = :enabled)
//                     and (:verified is null or u.verified = :verified)
//                     and (
//                         (:contactType is null and :contactValue is null)
//                         or exists (
//                             select c.id
//                             from Contact c
//                             where c.user = u
//                                 and (:contactType is null or c.type = :contactType)
//                                 and (:contactValue is null or lower(c.value) like lower(concat('%', :contactValue, '%')))
//                         )
//                     )
//             """,
//             countQuery = """
//                 select count(distinct u)
//                 from User u
//                 left join u.roles userRole
//                 where (:username is null or lower(u.username) like lower(concat('%', :username, '%')))
//                     and (:firstName is null or lower(u.firstName) like lower(concat('%', :firstName, '%')))
//                     and (:lastName is null or lower(u.lastName) like lower(concat('%', :lastName, '%')))
//                     and (:role is null or userRole = :role)
//                     and (:enabled is null or u.enabled = :enabled)
//                     and (:verified is null or u.verified = :verified)
//                     and (
//                         (:contactType is null and :contactValue is null)
//                         or exists (
//                             select c.id
//                             from Contact c
//                             where c.user = u
//                                 and (:contactType is null or c.type = :contactType)
//                                 and (:contactValue is null or lower(c.value) like lower(concat('%', :contactValue, '%')))
//                         )
//                     )
//             """
//     )
//     Page<User> search(
//             @Param("username") String username,
//             @Param("firstName") String firstName,
//             @Param("lastName") String lastName,
//             @Param("role") String role,
//             @Param("enabled") Boolean enabled,
//             @Param("verified") Boolean verified,
//             @Param("contactType") ContactType contactType,
//             @Param("contactValue") String contactValue,
//             Pageable pageable
//     );
// }
