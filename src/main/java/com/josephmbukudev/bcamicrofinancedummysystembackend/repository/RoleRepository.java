package com.josephmbukudev.bcamicrofinancedummysystembackend.repository;

import com.josephmbukudev.bcamicrofinancedummysystembackend.models.ERole;
import com.josephmbukudev.bcamicrofinancedummysystembackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
