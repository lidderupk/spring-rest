package com.upkar.springdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
//	@GetMapping("/")
//	public String home() {
//		return "hello world";
//	}
	
	@GetMapping("/")
	public String getName(@RequestParam(name="name", defaultValue="world") String name){
		return "hello " + name;
	}
}
