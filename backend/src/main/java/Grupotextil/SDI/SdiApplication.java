package Grupotextil.SDI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "Grupotextil.SDI")
public class SdiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdiApplication.class, args);
	}

}
