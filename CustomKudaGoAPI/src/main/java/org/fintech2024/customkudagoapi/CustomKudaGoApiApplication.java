package org.fintech2024.customkudagoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"org.fintech2024.customkudagoapi",
		"org.fintech2024.logexecutiontimestarter"
})
public class CustomKudaGoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomKudaGoApiApplication.class, args);
	}

}
