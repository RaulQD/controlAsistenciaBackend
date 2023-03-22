package com.proyecto.controlasistenciabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class ControlAsistenciaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlAsistenciaBackendApplication.class, args);
    }

    // ETAPA DE CORRECCION "ERROR COMMONSMULTIPARTRESOLVER"
//    @Bean(name="multipartResolver")
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(10000000);
//        return multipartResolver;
//    }
    //

}
