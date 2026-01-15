package com.example.uptime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UptimeApplication {
	public static void main(String[] args) {
		SpringApplication.run(UptimeApplication.class, args);
	}

}

// Jaki swoj udzial w paczkowaniu danych ma warstwa repository
// 1. Sposob przekazywania parametrow do metody RESTowej:
    // a) Path Variable
    // b) RequestParam
    // c) RequestBody
    // d) Header
// 2. ResponseBody
// 3. JSON JWT certyfikat
// 4. OAuth - podstawy
// 5. Autoryzacja i Autentykacja (czym sie roznia + podstawy)
