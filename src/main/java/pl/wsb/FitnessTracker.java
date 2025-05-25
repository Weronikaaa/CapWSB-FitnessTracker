package pl.wsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.wsb.fitnesstracker"})
public class FitnessTracker {
    public static void main(String[] args) {
        SpringApplication.run(FitnessTracker.class, args);
    }
}