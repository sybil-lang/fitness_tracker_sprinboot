//package com.fitness_monolith.fitness.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customAPI(){
//        return new OpenAPI()
//                // 📘 API Information
//                .info(new Info()
//                        .title("Fitness Tracking API")
//                        .description("APIs for fitness tracking, activities and recommendations")
//                        .version("1.0.0")
//                        .contact(new Contact()
//                                .name("Fitness Team")
//                                .email("support@fitness.com")
//                        )
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("https://www.apache.org/licenses/LICENSE-2.0")
//                        )
//                )
//                ;
//    }
//}
