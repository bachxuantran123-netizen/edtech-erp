package com.example.edtecherp.academic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.edtecherp.common.entity",
        "com.example.edtecherp.academic.entity"
})
public class AcademicServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicServiceApplication.class, args);
    }
}
