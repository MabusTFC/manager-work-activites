package com.example.managerworkactivites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagerWorkActivitesApplication {

    public static void main(String[] args) {

        SpringApplication.run(ManagerWorkActivitesApplication.class, args);
        System.out.println("Swagger: http://localhost:8080/swagger-ui.html");
    }

}
