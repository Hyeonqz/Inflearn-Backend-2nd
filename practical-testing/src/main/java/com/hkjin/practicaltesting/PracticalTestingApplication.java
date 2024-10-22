package com.hkjin.practicaltesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Entity 변경시 시간 측정
@SpringBootApplication
public class PracticalTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticalTestingApplication.class, args);
	}

}
