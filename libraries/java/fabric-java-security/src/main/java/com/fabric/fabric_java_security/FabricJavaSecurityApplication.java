package com.fabric.fabric_java_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fabric.fabric_java_security")
public class FabricJavaSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FabricJavaSecurityApplication.class, args);
	}
}