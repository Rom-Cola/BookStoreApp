package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Role;
import com.loievroman.bookstoreapp.model.Role.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName role);
}
