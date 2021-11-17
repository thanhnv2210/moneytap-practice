package com.mtvn.auth.server.repository;

import com.mtvn.persistence.entities.auth.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
