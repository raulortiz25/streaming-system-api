package com.Streaming.StreamingSystem.Repository;

import com.Streaming.StreamingSystem.Model.Enums.RoleEnum;
import com.Streaming.StreamingSystem.Model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional <RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
