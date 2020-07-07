package com.alt.f4.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="funcionario")
@PrimaryKeyJoinColumn(name="id_usuario")
public class Funcionario extends Usuario{
	
	private String nome;
	
	private String departamento;
	
	public Funcionario(String username, String password, TipoPerfil perfil,
			String nome, String departamento) {
		super(username, password, perfil);
		this.nome = nome;
		this.departamento = departamento;
	}
	
	public Funcionario() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
}
