package com.fortinoiv.apijun.repository;

import com.fortinoiv.apijun.domain.constans.ERole;
import com.fortinoiv.apijun.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
