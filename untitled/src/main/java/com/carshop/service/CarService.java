package com.carshop.service;

import com.carshop.model.Car;
import com.carshop.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Value("${app.upload.dir:uploads/cars}")
    private String uploadDir;

    public List<Car> getAllCars() {
        return carRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Page<Car> getAllCarsPaginated(int page, int size, String sortBy) {
        Sort sort = getSortOption(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return carRepository.findAll(pageable);
    }

    public Page<Car> searchCars(String brand, BigDecimal minPrice, BigDecimal maxPrice,
                                 Integer year, String fuelType, int page, int size, String sortBy) {
        Sort sort = getSortOption(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return carRepository.searchCars(brand, minPrice, maxPrice, year, fuelType, pageable);
    }

    private Sort getSortOption(String sortBy) {
        if (sortBy == null) return Sort.by(Sort.Direction.DESC, "createdAt");
        return switch (sortBy) {
            case "price_asc"  -> Sort.by(Sort.Direction.ASC,  "price");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "year_desc"  -> Sort.by(Sort.Direction.DESC, "year");
            case "year_asc"   -> Sort.by(Sort.Direction.ASC,  "year");
            default           -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> getFeaturedCars() {
        List<Car> featured = carRepository.findByFeaturedTrue();
        if (featured.isEmpty()) {
            return carRepository.findTop6ByOrderByCreatedAtDesc();
        }
        return featured;
    }

    public List<Car> getRecentCars() {
        return carRepository.findTop6ByOrderByCreatedAtDesc();
    }

    public Car saveCar(Car car, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            car.setImageUrl(imageUrl);
        }
        return carRepository.save(car);
    }

    public Car updateCar(Car car, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            if (car.getImageUrl() != null && !car.getImageUrl().startsWith("http")) {
                deleteImage(car.getImageUrl());
            }
            String imageUrl = saveImage(imageFile);
            car.setImageUrl(imageUrl);
        }
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.findById(id).ifPresent(car -> {
            if (car.getImageUrl() != null && !car.getImageUrl().startsWith("http")) {
                deleteImage(car.getImageUrl());
            }
            carRepository.deleteById(id);
        });
    }

    public long getTotalCarsCount() {
        return carRepository.count();
    }

    public List<String> getAllBrands() {
        return carRepository.findDistinctBrands();
    }

    public List<String> getAllFuelTypes() {
        return carRepository.findDistinctFuelTypes();
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String filename = UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        return "/uploads/cars/" + filename;
    }

    private void deleteImage(String imageUrl) {
        try {
            String filename = imageUrl.replace("/uploads/cars/", "");
            Path filePath = Paths.get(uploadDir, filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // log and continue
        }
    }
}
