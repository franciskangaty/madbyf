package dev.madbyf.auth.user.query.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import dev.madbyf.auth.user.query.model.UserAccountView;

public interface UserAccountViewRepository
        extends JpaRepository<UserAccountView, UUID>,
        JpaSpecificationExecutor<UserAccountView> {
}