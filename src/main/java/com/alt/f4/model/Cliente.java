package com.alt.f4.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="cliente")
@PrimaryKeyJoinColumn(name="id_usuario")
public class Cliente extends Usuario{
	
	private String nome;
	
	private String telefone;
	
	private String email;

	public Cliente(String username, String password, TipoPerfil perfil,
			String nome, String telefone, String email) {
		super(username, password, perfil);
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
	}
	
	public Cliente() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
