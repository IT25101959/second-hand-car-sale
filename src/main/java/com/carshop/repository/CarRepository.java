package com.carshop.repository;

import com.carshop.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByBrandIgnoreCase(String brand);

    List<Car> findByFeaturedTrue();

    List<Car> findTop6ByOrderByCreatedAtDesc();

    Page<Car> findAll(Pageable pageable);

    @Query("SELECT c FROM Car c WHERE " +
           "(:brand IS NULL OR LOWER(c.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR c.price <= :maxPrice) AND " +
           "(:year IS NULL OR c.year = :year) AND " +
           "(:fuelType IS NULL OR LOWER(c.fuelType) = LOWER(:fuelType))")
    Page<Car> searchCars(@Param("brand") String brand,
                         @Param("minPrice") BigDecimal minPrice,
                         @Param("maxPrice") BigDecimal maxPrice,
                         @Param("year") Integer year,
                         @Param("fuelType") String fuelType,
                         Pageable pageable);

    @Query("SELECT DISTINCT c.brand FROM Car c ORDER BY c.brand")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT c.fuelType FROM Car c ORDER BY c.fuelType")
    List<String> findDistinctFuelTypes();

    long count();
}
