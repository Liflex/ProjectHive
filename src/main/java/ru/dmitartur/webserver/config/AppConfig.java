package ru.dmitartur.webserver.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.dmitartur.socketserver.SocketServerInit;


@SpringBootApplication
@ComponentScan(basePackages="ru.dmitartur")
@EntityScan( basePackages = {"ru.dmitartur.webserver"} )
@EnableWebSecurity
@EnableTransactionManagement
public class AppConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppConfig.class);
    }
    public static void main(String[] args) {
        SocketServerInit socketServerInit = new SocketServerInit();
        SpringApplication.run(AppConfig.class, args);
        socketServerInit.start();
    }
}
