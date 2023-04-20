package com.springshopbe.repository;

import com.springshopbe.entity.ManufacturerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Long> {


    @Query("SELECT e FROM ManufacturerEntity e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManufacturerEntity> findByNameContainsIgnoreCase(@Param("name") String name);


    @Query("SELECT m FROM ManufacturerEntity m WHERE m.id <> :id AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ManufacturerEntity> findByIdNotAndNameContainsIgnoreCase(Long id, String name);
    @Query("select m from ManufacturerEntity m order by m.createDate desc ")
    List<ManufacturerEntity> getAllManufacturers();

    @Query("select m from ManufacturerEntity m order by m.createDate desc ")
    Page<ManufacturerEntity> getAllManufacturersPaginged(Pageable pageable);

    @Query("SELECT m FROM ManufacturerEntity m WHERE LOWER(m.name) LIKE %:name%")
   Page<ManufacturerEntity> getAllManufacturersPaginged(String name,Pageable pageable);

    @Query("select m from ManufacturerEntity m where m.id = ?1")
    Optional<ManufacturerEntity> getManufacturerEntitiesById(Long id);

    @Modifying
    @Query("DELETE FROM ManufacturerEntity m WHERE m.id = :id")
    void deleteManufacturerEntityById(@Param("id") Long id);
}
