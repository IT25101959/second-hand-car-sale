package com.carshop.controller;

import com.carshop.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CarService carService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredCars", carService.getFeaturedCars());
        model.addAttribute("totalCars", carService.getTotalCarsCount());
        model.addAttribute("brands", carService.getAllBrands());
        return "index";
    }
}
