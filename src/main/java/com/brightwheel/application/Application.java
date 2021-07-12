package com.brightwheel.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.brightwheel.controller", "com.brightwheel.application", "com.brightwheel.services"})
public class Application {

    /**
     * Main entry point for sprintboot start up
     * @param args comand line args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}