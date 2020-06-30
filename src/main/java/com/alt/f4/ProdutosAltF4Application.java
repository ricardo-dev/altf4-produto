package com.alt.f4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.alt.f4.config.property.ProdutoApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(ProdutoApiProperty.class)
public class ProdutosAltF4Application {

	public static void main(String[] args) {
		SpringApplication.run(ProdutosAltF4Application.class, args);
	}

}
