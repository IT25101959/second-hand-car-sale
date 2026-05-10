package com.carshop.controller;

import com.carshop.model.Car;
import com.carshop.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CarService carService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/admin-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCars", carService.getTotalCarsCount());
        model.addAttribute("recentCars", carService.getRecentCars());
        model.addAttribute("brands", carService.getAllBrands());
        return "admin/admin-dashboard";
    }

    @GetMapping("/cars")
    public String manageCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "admin/admin-manage-cars";
    }

    @GetMapping("/cars/add")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "admin/admin-add-car";
    }

    @PostMapping("/cars/add")
    public String addCar(@Valid @ModelAttribute("car") Car car,
                         BindingResult result,
                         @RequestParam("imageFile") MultipartFile imageFile,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            return "admin/admin-add-car";
        }
        try {
            carService.saveCar(car, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Car listing added successfully!");
            return "redirect:/admin/cars";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload image: " + e.getMessage());
            return "admin/admin-add-car";
        }
    }

    @GetMapping("/cars/edit/{id}")
    public String showEditCarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Car not found.");
            return "redirect:/admin/cars";
        }
        model.addAttribute("car", car.get());
        return "admin/admin-edit-car";
    }

    @PostMapping("/cars/edit/{id}")
    public String updateCar(@PathVariable Long id,
                            @Valid @ModelAttribute("car") Car car,
                            BindingResult result,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            return "admin/admin-edit-car";
        }
        car.setId(id);
        Optional<Car> existing = carService.getCarById(id);
        existing.ifPresent(e -> {
            if ((imageFile == null || imageFile.isEmpty()) && car.getImageUrl() == null) {
                car.setImageUrl(e.getImageUrl());
            }
            car.setCreatedAt(e.getCreatedAt());
        });
        try {
            carService.updateCar(car, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Car updated successfully!");
            return "redirect:/admin/cars";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload image: " + e.getMessage());
            return "admin/admin-edit-car";
        }
    }

    @PostMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carService.deleteCar(id);
        redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully!");
        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/toggle-featured/{id}")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        carService.getCarById(id).ifPresent(car -> {
            car.setFeatured(!Boolean.TRUE.equals(car.getFeatured()));
            try {
                carService.updateCar(car, null);
            } catch (IOException e) {
                // no image update
            }
        });
        redirectAttributes.addFlashAttribute("successMessage", "Featured status updated.");
        return "redirect:/admin/cars";
    }
}
