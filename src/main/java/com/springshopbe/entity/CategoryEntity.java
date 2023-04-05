package com.springshopbe.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated
    @Column(name = "status")
    private CategoryStatus status;

    public CategoryEntity() {
    }

    public CategoryEntity(String name, CategoryStatus status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }
}
