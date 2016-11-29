package com.upkar.springdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.upkar")
public class SpringDemoApplication {

	public static void main(String[] args) {
		System.out.println("upkar: starting");
		SpringApplication.run(SpringDemoApplication.class, args);
		System.out.println("upkar: done");
	}
}
