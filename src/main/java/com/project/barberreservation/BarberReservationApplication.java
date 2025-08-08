package com.project.barberreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.project")
@ComponentScan("com.project")
public class BarberReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarberReservationApplication.class, args);
    }

}
