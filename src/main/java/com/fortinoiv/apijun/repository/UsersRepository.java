package com.fortinoiv.apijun.repository;

import com.fortinoiv.apijun.domain.entities.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    Users findByIdAndStatus(@Param("id") Integer id, @Param("status") String status);
}
