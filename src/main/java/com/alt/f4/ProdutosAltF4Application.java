package com.alt.f4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import com.alt.f4.config.property.ProdutoApiProperty;
import com.alt.f4.model.TipoPerfil;
import com.alt.f4.model.Usuario;
import com.alt.f4.repositories.UsuarioRepository;
import com.alt.f4.util.GeradorSenha;

@SpringBootApplication
@EnableConfigurationProperties(ProdutoApiProperty.class)
public class ProdutosAltF4Application {

	public static void main(String[] args) {
		SpringApplication.run(ProdutosAltF4Application.class, args);
	}
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@EventListener
	private void init(ApplicationReadyEvent event) {
		if(!usuarioRepository.findByUsername("admin").isPresent()) {
			Usuario usuario = new Usuario();
			usuario.setUsername("admin");
			usuario.setPassword(GeradorSenha.gerarBCrypt("12345"));
			usuario.setPerfil(TipoPerfil.ROLE_ADMIN);
			usuarioRepository.save(usuario);
		}
	}
}
