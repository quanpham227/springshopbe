package com.springshopbe.repository;

import com.springshopbe.entity.ManufacturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Long> {


    @Query("SELECT e FROM ManufacturerEntity e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManufacturerEntity> findByNameContainsIgnoreCase(@Param("name") String name);


    @Query("SELECT m FROM ManufacturerEntity m WHERE m.id <> :id AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManufacturerEntity> findByIdNotAndNameContainsIgnoreCase(Long id, String name);
}
