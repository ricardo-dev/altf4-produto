package com.alt.f4.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("altf4")
public class ProdutoApiProperty {
	
	private String originPermitida = "http://localhost:4200";

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}
}
