package com.mtvn.auth.server.repository;

import com.mtvn.persistence.entities.auth.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    List<UserGroup> findByUsername(String username);
}
