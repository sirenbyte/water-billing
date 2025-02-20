package org.example.waterbilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WaterBillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaterBillingApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedOriginPatterns(
                                "*",
                                "http://localhost:3000",
                                "http://89.218.1.74:30001",
                                "http://10.0.10.27:40001",
                                "http://10.0.10.27:30004"
                        )
                        .allowedMethods("*");
            }
        };
    }

}
