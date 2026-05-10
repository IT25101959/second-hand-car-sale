# AutoDeals - Second-Hand Car Sale Management System

A full-stack Spring Boot web application for browsing and managing second-hand car listings.


Developers

- **IT25101949**
- **IT25101956**
- **IT25101959**
- **IT25101961**
- **IT25101966**
- **IT25101968**


## Technology Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2.3 |
| Frontend | HTML, CSS, JavaScript |
| UI Framework | Bootstrap 5.3 |
| Template Engine | Thymeleaf |
| Database | MySQL 8+ |
| Build Tool | Maven |
| Security | Spring Security |
| Server | Apache Tomcat (embedded) |

## Prerequisites

- **JDK 17** or higher
- **MySQL 8.0** or higher
- **Maven 3.8+**
- **IntelliJ IDEA** (recommended)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/secondhand_car_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

## Running the Application

### IntelliJ IDEA


The application starts at: **http://localhost:9090**

## Default Admin Credentials

| Field | Value |
|-------|-------|
| URL |http://localhost:9090/admin/login   |
| Username | `admin` |
| Password | `admin123` |

> The admin account and sample car data are created automatically on first startup.

## Application Pages

### Public
| Page | URL |
|------|-----|
| Home | http://localhost:9090 |
| Browse Cars | http://localhost:9090/cars |
| Car Details | http://localhost:9090/cars/{id} |

### Admin Panel
| Page | URL |
|------|-----|
| Login | http://localhost:9090/admin/login |
| Dashboard | http://localhost:9090/admin/dashboard |
| Manage Cars | http://localhost:9090/admin/cars |
| Add Car | http://localhost:9090/admin/cars/add |
| Edit Car | http://localhost:9090/admin/cars/edit/{id} |

## Project Structure

```
secondhand-car-sale/
├── src/main/java/com/carshop/
│   ├── SecondhandCarSaleApplication.java
│   ├── config/
│   │   ├── DataInitializer.java       # Seeds admin + sample cars
│   │   ├── SecurityConfig.java        # Spring Security config
│   │   └── WebMvcConfig.java          # Static resource handler
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── CarController.java
│   │   └── AdminController.java
│   ├── model/
│   │   ├── Vehicle.java               # Abstract base class (OOP)
│   │   ├── Car.java                   # Extends Vehicle
│   │   └── AdminUser.java
│   ├── repository/
│   │   ├── CarRepository.java
│   │   └── AdminRepository.java
│   └── service/
│       ├── CarService.java
│       └── AdminService.java          # Implements UserDetailsService
│
├── src/main/resources/
│   ├── templates/
│   │   ├── index.html
│   │   ├── cars.html
│   │   ├── car-details.html
│   │   ├── fragments/
│   │   │   └── navbar.html
│   │   └── admin/
│   │       ├── admin-login.html
│   │       ├── admin-dashboard.html
│   │       ├── admin-manage-cars.html
│   │       ├── admin-add-car.html
│   │       └── admin-edit-car.html
│   ├── static/
│   │   ├── css/
│   │   │   ├── style.css
│   │   │   └── admin.css
│   │   ├── js/
│   │   │   ├── main.js
│   │   │   └── admin.js
│   │   └── images/
│   │       └── car-placeholder.jpg
│   └── application.properties
│
├── uploads/cars/                      # Uploaded images (auto-created)
├── pom.xml
└── README.md
```

## Features

### Public Features
- Home page with featured cars and hero search
- Browse all cars with pagination (9 per page)
- Filter by brand, price range, year, fuel type
- Sort by price / year / newest
- Car detail page with full specs and seller contact
- Responsive design (mobile + desktop)

### Admin Features
- Secure login with Spring Security session management
- Dashboard with statistics
- Full CRUD: Create, Read, Update, Delete car listings
- Toggle featured status per car
- Image upload (stored in `/uploads/cars/`) or URL input
- Live table search

### OOP Concepts Implemented
| Concept | Implementation |
|---------|---------------|
| **Encapsulation** | Private fields + getters/setters in all model classes |
| **Inheritance** | `Car extends Vehicle` (abstract base class) |
| **Polymorphism** | `getVehicleDetails()` overridden in `Car` |
| **Abstraction** | `Vehicle` is `@MappedSuperclass` abstract class |

## File Upload

Uploaded images are saved to `uploads/cars/` relative to the working directory.
They are served at `http://localhost:8080/uploads/cars/<filename>`.
Max file size: **10 MB** (configurable in `application.properties`).
