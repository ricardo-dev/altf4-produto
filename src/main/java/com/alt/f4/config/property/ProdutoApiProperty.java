package com.alt.f4.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("altf4")
public class ProdutoApiProperty {
	
	private String originPermitida = "http://localhost:4200";
	
	private final Seguranca seguranca = new Seguranca();
	
	private final Token token = new Token();

	public String getOriginPermitida() {
		return originPermitida;
	}
	
	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public Seguranca getSeguranca() {
		return this.seguranca;
	}
	
	public Token getToken() {
		return this.token;
	}
	
	public static class Seguranca {
		private boolean enableHttps; //default false
		
		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}		
	}
	
	public static class Token {
		
		private String segredo;
		private int tokenExpirado;
		private int refreshTokenExpirado;
		
		public String getSegredo() {
			return segredo;
		}

		public void setSegredo(String segredo) {
			this.segredo = segredo;
		}

		public int getTokenExpirado() {
			return tokenExpirado;
		}

		public void setTokenExpirado(int tokenExpirado) {
			this.tokenExpirado = tokenExpirado;
		}

		public int getRefreshTokenExpirado() {
			return refreshTokenExpirado;
		}

		public void setRefreshTokenExpirado(int refreshTokenExpirado) {
			this.refreshTokenExpirado = refreshTokenExpirado;
		}
	}
}
