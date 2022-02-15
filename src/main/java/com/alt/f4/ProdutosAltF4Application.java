package com.alt.f4;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import com.alt.f4.config.property.ProdutoApiProperty;
import com.alt.f4.model.Cliente;
import com.alt.f4.model.Funcionario;
import com.alt.f4.model.TipoPerfil;
import com.alt.f4.model.Usuario;
import com.alt.f4.repositories.ClienteRepository;
import com.alt.f4.repositories.FuncionarioRepository;
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
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@EventListener
	private void init(ApplicationReadyEvent event) {
		if(!usuarioRepository.findByUsername("admin").isPresent()) {
			/*Usuario usuario = new Usuario();
			usuario.setUsername("admin");
			usuario.setPassword(GeradorSenha.gerarBCrypt("12345"));
			usuario.setPerfil(TipoPerfil.ROLE_ADMIN);
			usuarioRepository.save(usuario);*/
			
			System.out.println("############# inserindo nos bancos de dados...");
			
			Funcionario f1 = new Funcionario("admin", GeradorSenha.gerarBCrypt("12345"),
					TipoPerfil.ROLE_ADMIN, "Ricardo", "Admin");
			Funcionario f2 = new Funcionario("user01", GeradorSenha.gerarBCrypt("12345"),
					TipoPerfil.ROLE_FUNC, "Kety", "Admin");
			Cliente c1 = new Cliente("aquiles_08", GeradorSenha.gerarBCrypt("12345"), 
					TipoPerfil.ROLE_USER,"Aquiles", "(62) 99384-0336", "email@email.com");
			
			funcionarioRepository.save(f1);
			funcionarioRepository.save(f2);
			clienteRepository.save(c1);
			
			Optional<Usuario> usuario = usuarioRepository.findByUsername(f1.getUsername());
			if(usuario.isPresent()) {
				System.out.println(">>>>>>>>>>>>>> Achou: "+usuario.get().getUsername());
			}
		}
	}
}
