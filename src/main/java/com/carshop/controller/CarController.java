package com.carshop.controller;

import com.carshop.model.Car;
import com.carshop.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public String listCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String fuelType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String sortBy,
            Model model) {

        boolean hasSearch = (brand != null && !brand.isBlank())
                || minPrice != null || maxPrice != null
                || year != null
                || (fuelType != null && !fuelType.isBlank());

        Page<Car> carsPage;
        if (hasSearch) {
            String brandParam = (brand != null && brand.isBlank()) ? null : brand;
            String fuelParam  = (fuelType != null && fuelType.isBlank()) ? null : fuelType;
            carsPage = carService.searchCars(brandParam, minPrice, maxPrice, year, fuelParam, page, size, sortBy);
        } else {
            carsPage = carService.getAllCarsPaginated(page, size, sortBy);
        }

        model.addAttribute("cars", carsPage.getContent());
        model.addAttribute("currentPage", carsPage.getNumber());
        model.addAttribute("totalPages", carsPage.getTotalPages());
        model.addAttribute("totalElements", carsPage.getTotalElements());
        model.addAttribute("brands", carService.getAllBrands());
        model.addAttribute("fuelTypes", carService.getAllFuelTypes());
        model.addAttribute("searchBrand", brand);
        model.addAttribute("searchMinPrice", minPrice);
        model.addAttribute("searchMaxPrice", maxPrice);
        model.addAttribute("searchYear", year);
        model.addAttribute("searchFuelType", fuelType);
        model.addAttribute("sortBy", sortBy);
        return "cars";
    }

    @GetMapping("/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isEmpty()) {
            return "redirect:/cars";
        }
        model.addAttribute("car", car.get());
        model.addAttribute("recentCars", carService.getRecentCars());
        return "car-details";
    }
}
