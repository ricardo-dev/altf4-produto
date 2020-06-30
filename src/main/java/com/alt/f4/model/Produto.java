package com.alt.f4.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="produto")
public class Produto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min=3, max=20)
	private String nome;
	
	@Column(length=200)
	@Size(max=200)
	private String descricao;
	
	/*
	@Column(name="path_imagem")
	private String pathImagem;
	*/
	
	@NotNull
	private BigDecimal preco;
	

	@JsonIgnoreProperties("produto")
	@OneToMany(mappedBy="produto", 
	    	cascade=CascadeType.REMOVE)
	private List<Media> medias;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/*
	public String getPathImagem() {
		return pathImagem;
	}

	public void setPathImagem(String path_imagem) {
		this.pathImagem = path_imagem;
	}
	*/

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
