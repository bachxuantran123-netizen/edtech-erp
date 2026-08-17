package com.example.edtecherp.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.example.edtecherp.common.entity",
        "com.example.edtecherp.assessment.entity"
})
public class AssessmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentServiceApplication.class, args);
    }
}
