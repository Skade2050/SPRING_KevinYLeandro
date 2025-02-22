package com.KevinYLeandro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GestorAulasApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GestorAulasApplication.class, args);
        System.out.println("¡Aplicación iniciada correctamente!");
        System.out.println("Servidor escuchando en el puerto 8080");
    }
} 