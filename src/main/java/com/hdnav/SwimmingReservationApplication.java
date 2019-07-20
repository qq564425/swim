package com.hdnav;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration 
@Configuration
@ComponentScan("com.hdnav")
@MapperScan("com.hdnav.dao")
@SpringBootApplication
public class SwimmingReservationApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SwimmingReservationApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SwimmingReservationApplication.class);
	}

}
