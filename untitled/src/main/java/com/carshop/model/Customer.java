package com.carshop.model;

public class Customer {
    package com.carshop.model;


import jakarta.persistence.*;

    @Entity
    public class Customer {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String fullName;
        private String email;
        private String password;
        private String phone;

        public Customer() {
        }

        public Customer(String fullName, String email, String password, String phone) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.phone = phone;
        }

        public Long getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }





}
