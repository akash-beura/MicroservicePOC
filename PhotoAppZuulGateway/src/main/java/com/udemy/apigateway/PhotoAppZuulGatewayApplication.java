package com.udemy.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class PhotoAppZuulGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppZuulGatewayApplication.class, args);
	}

}
