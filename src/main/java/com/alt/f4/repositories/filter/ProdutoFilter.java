package com.alt.f4.repositories.filter;

import java.math.BigDecimal;

public class ProdutoFilter {
	
	private String nome;
	
	private BigDecimal precoMin;
	
	private BigDecimal precoMax;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPrecoMin() {
		return precoMin;
	}

	public void setPrecoMin(BigDecimal precoMin) {
		this.precoMin = precoMin;
	}

	public BigDecimal getPrecoMax() {
		return precoMax;
	}

	public void setPrecoMax(BigDecimal precoMax) {
		this.precoMax = precoMax;
	}
}
