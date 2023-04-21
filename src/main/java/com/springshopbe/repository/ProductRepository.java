package com.springshopbe.repository;

import com.springshopbe.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    /*
        Trong truy vấn này, lower được sử dụng để chuyển tên sản phẩm và tham số name thành chữ thường để tìm kiếm không phân biệt chữ hoa/chữ thường.
        concat được sử dụng để nối chuỗi tìm kiếm và % để tìm kiếm các sản phẩm có chứa chuỗi con đó.
        :name được sử dụng để tham chiếu đến tham số name trong phương thức,
        và @Param("name") được sử dụng để chỉ định tên tham số trong truy vấn.
     */
    @Query("SELECT p FROM ProductEntity p WHERE lower(p.name) LIKE lower(concat('%', :name,'%'))")
    Page<ProductEntity> findByNameContainsIgnoreCase(@Param("name") String name, Pageable pageable);
}
