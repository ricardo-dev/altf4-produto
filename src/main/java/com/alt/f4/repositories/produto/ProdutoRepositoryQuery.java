package com.alt.f4.repositories.produto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alt.f4.model.Produto;
import com.alt.f4.repositories.filter.ProdutoFilter;

public interface ProdutoRepositoryQuery {

	public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable); 
	
	public Produto produtos(String nome);
	
	public List<Produto> produtosLista(String nome);
}
