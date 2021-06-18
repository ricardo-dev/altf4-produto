package com.alt.f4.repositories.produto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Restrictions;
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
		//criteria.orderBy(builder.desc(root.get("id"));
		TypedQuery<Produto> query = manager.createQuery(criteria);
		adicionarRestricaoDePaginacao(query, pageable);
		
		return new PageImpl<>( query.getResultList(), pageable, total(filtro));
	}
	
	public Produto produtos(String nome){
		String sql = "SELECT * FROM produto p WHERE p.nome = ?";
		Query query = manager.createNativeQuery(sql, Produto.class);
		query.setParameter(1, nome);
		Produto produto = (Produto) query.getSingleResult();
		return produto;
	}
	
	public List<Produto> produtosLista(String nome){
		String sql = "SELECT * FROM produto p WHERE p.nome LIKE ?";
		Query query = manager.createNativeQuery(sql, Produto.class);
		query.setParameter(1, "%"+nome+"%");
		List<Produto> produtos = (List<Produto>) query.getResultList();
		return produtos;
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
			predicates.add(
					(Predicate) Restrictions.eq("tabela.propriedade", filtro.getNome()));
		}
		if(!StringUtils.isEmpty(filtro.getPrecoMax())) {
			predicates.add(
				builder.lessThanOrEqualTo(root.get("preco"), filtro.getPrecoMax())
			);
			/*predicates.add(
				builder.or(
						builder.lessThanOrEqualTo(root.get("preco"), filtro.getPrecoMax()),
						builder.lessThanOrEqualTo(root.get("preco"), filtro.getPrecoMax()))
					);*/
		}
		/*
		 * builder.equal(root.join("condomimiun").get("id"), filter.getCondominium()) // join - relacionamento
		 * */
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
