package com.carshop.config;

import com.carshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CarRepository carRepository;

    @Override
    public void run(String... args) {
        if (!adminService.adminExists("admin")) {
            adminService.createAdmin("admin", "admin123");
            System.out.println("Default admin created: admin / admin123");
        }

        if (carRepository.count() == 0) {
            seedSampleCars();
            System.out.println("Sample car data seeded.");
        }
    }

    private void seedSampleCars() {
        Car[] cars = {
            createCar("Toyota", "Camry", 2019, new BigDecimal("18500"), 45000,
                "Petrol", "Well-maintained Toyota Camry with full service history. Single owner.",
                "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?w=800",
                "+1-555-0101", true),
            createCar("Honda", "Civic", 2020, new BigDecimal("16200"), 32000,
                "Petrol", "Honda Civic in excellent condition. Accident-free with low mileage.",
                "https://images.unsplash.com/photo-1606152421802-db97b9c7a11b?w=800",
                "+1-555-0102", true),
            createCar("BMW", "3 Series", 2018, new BigDecimal("28000"), 55000,
                "Diesel", "BMW 3 Series with premium sound system, leather seats and panoramic sunroof.",
                "https://images.unsplash.com/photo-1555215695-3004980ad54e?w=800",
                "+1-555-0103", true),
            createCar("Mercedes-Benz", "C-Class", 2021, new BigDecimal("35000"), 18000,
                "Petrol", "Nearly new Mercedes C-Class. Full dealer service history.",
                "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=800",
                "+1-555-0104", true),
            createCar("Ford", "Mustang", 2017, new BigDecimal("22000"), 68000,
                "Petrol", "Classic Ford Mustang GT. V8 engine, excellent performance.",
                "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=800",
                "+1-555-0105", false),
            createCar("Hyundai", "Tucson", 2020, new BigDecimal("20500"), 27000,
                "Diesel", "Family-friendly Hyundai Tucson SUV. 4WD, spacious interior.",
                "https://images.unsplash.com/photo-1559416523-140ddc3d238c?w=800",
                "+1-555-0106", true),
            createCar("Volkswagen", "Golf", 2019, new BigDecimal("14500"), 41000,
                "Petrol", "VW Golf 1.4 TSI. Fuel efficient, great city car.",
                "https://images.unsplash.com/photo-1617469767886-e9a1f5ef77fc?w=800",
                "+1-555-0107", false),
            createCar("Audi", "A4", 2018, new BigDecimal("24000"), 62000,
                "Diesel", "Audi A4 Quattro. All-wheel drive, premium interior.",
                "https://images.unsplash.com/photo-1606664515524-ed2f786a0bd6?w=800",
                "+1-555-0108", false),
        };

        for (Car car : cars) {
            carRepository.save(car);
        }
    }

    private Car createCar(String brand, String model, int year, BigDecimal price,
                           int mileage, String fuelType, String description,
                           String imageUrl, String sellerContact, boolean featured) {
        Car car = new Car(brand, model, year, price, mileage, fuelType,
                description, imageUrl, sellerContact);
        car.setFeatured(featured);
        return car;
    }
}
