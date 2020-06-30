package com.alt.f4.repositories.produto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.alt.f4.model.Produto;
import com.alt.f4.repositories.filter.ProdutoFilter;

public class ProdutoRepositoryImpl implements ProdutoRepositoryQuery{
	
	@PersistenceContext
	public EntityManager manager;

	@Override
	public Page<Produto> filtrar(ProdutoFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		Predicate[] predicates = criarRestricoes(filtro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Produto> query = manager.createQuery(criteria);
		adicionarRestricaoDePaginacao(query, pageable);
		
		return new PageImpl<>( query.getResultList(), pageable, total(filtro));
	}

	private Predicate[] criarRestricoes(ProdutoFilter filtro, CriteriaBuilder builder, Root<Produto> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!StringUtils.isEmpty(filtro.getNome())) {
			predicates.add(
				builder.like(builder.lower(root.get("nome")) , "%"+filtro.getNome()+"%")
			);
		}
		if(!StringUtils.isEmpty(filtro.getPrecoMin())) {
			predicates.add(
				builder.greaterThanOrEqualTo(root.get("preco"), filtro.getPrecoMin())
			);
		}
		if(!StringUtils.isEmpty(filtro.getPrecoMax())) {
			predicates.add(
				builder.lessThanOrEqualTo(root.get("preco"), filtro.getPrecoMax())
			);
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void adicionarRestricaoDePaginacao(TypedQuery<Produto> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(ProdutoFilter filtro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		Predicate[] predicates = criarRestricoes(filtro, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}
}
