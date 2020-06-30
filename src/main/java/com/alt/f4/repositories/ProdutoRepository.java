package com.alt.f4.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alt.f4.model.Produto;
import com.alt.f4.repositories.produto.ProdutoRepositoryQuery;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryQuery{
	public Page<Produto> findByNomeContaining(String nome, Pageable page);
}
