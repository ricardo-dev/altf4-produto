package com.alt.f4.resources;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alt.f4.event.RecursoCriadoEvent;
import com.alt.f4.model.Produto;
import com.alt.f4.repositories.ProdutoRepository;
import com.alt.f4.repositories.filter.ProdutoFilter;
import com.alt.f4.service.ProdutoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/produto")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
    private ApplicationEventPublisher publisher;
	
	@ApiOperation(value="Upload das imagens para os produtos")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma imagem em string em base64"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/foto", method=RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN') and #oauth2.hasScope('write')")
	public ResponseEntity<?> uploadImagem(@RequestParam MultipartFile imagem) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("data:image/png;base64,");
		sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imagem.getBytes(), false)));
		
		 return ResponseEntity.ok(sb);
	}
	
	@ApiOperation(value="Salva um novo produto")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna um produto salvo"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(method=RequestMethod.POST)
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
	public ResponseEntity<?> salvarProduto(
			@RequestBody @Valid Produto produto, 
			HttpServletResponse response){
		
		Produto produtoSalvo = produtoService.salvar(produto);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, produtoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}
	
	@ApiOperation(value="Recupera todos os produtos")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma lista de produtos"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<?> pegarTodos(){
		List<Produto> produtos = produtoRepository.findAll();
		return ResponseEntity.ok(produtos);
	}
	
	@ApiOperation(value="Recupera todos os produtos com filtro e paginacao")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma pagina de produtos"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/filtro", method=RequestMethod.GET)
	public ResponseEntity<?> pegarTodosComFiltro(
			ProdutoFilter filtro,
			Pageable pageable){
		//RequestParam(required=false, defaultValue="") String nome,
		Page<Produto> produtos = produtoRepository.filtrar(filtro, pageable);
		return ResponseEntity.ok(produtos);
	}
	
	@ApiOperation(value="Recupera todos os produtos com filtro e paginacao")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma pagina de produtos"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/nome", method=RequestMethod.GET)
	public ResponseEntity<?> pegarTodosComNome(@RequestParam(required=false, defaultValue="") String nome){
		//RequestParam(required=false, defaultValue="") String nome,
		Produto produtos = produtoRepository.produtos(nome);
		return ResponseEntity.ok(produtos);
	}
	
	@ApiOperation(value="Recupera todos os produtos com filtro e paginacao")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna uma pagina de produtos"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/nomes", method=RequestMethod.GET)
	public ResponseEntity<?> pegarTodosComNomeLista(@RequestParam(required=false, defaultValue="") String nome){
		//RequestParam(required=false, defaultValue="") String nome,
		List<Produto> produtos = produtoRepository.produtosLista(nome);
		return ResponseEntity.ok(produtos);
	}
	
	@ApiOperation(value="Recupera os detalhes de um produto")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna um produto salvo"),
			@ApiResponse(code=404, message="Não foi encontrado nenhum produto com esse ID"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> pegarPorId(@PathVariable("id") Long id){
		Optional<Produto> produtoSalvo = produtoRepository.findById(id);
		return produtoSalvo.isPresent() ? ResponseEntity.of(produtoSalvo) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Atualiza os dados do produto")
	@ApiResponses(value= {
			@ApiResponse(code=200, message="Retorna um produto atualizado"),
			@ApiResponse(code=404, message="Não foi encontrado nenhum produto com esse ID"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
	public ResponseEntity<?> atualizarProdutp(@PathVariable("id") Long id, @RequestBody @Valid Produto produto){
		Produto produtoSalvo = produtoService.atualizarProduto(id, produto);
		return ResponseEntity.ok(produtoSalvo);
	}
	
	@RequestMapping(value="/{id}/promocao", method=RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarProdutoPromocao(@PathVariable("id") Long id, @RequestBody boolean promocao){
		produtoService.atualizarProdutoPromocao(id, promocao);
	}
	
	@ApiOperation(value="Remove um produto")
	@ApiResponses(value= {
			@ApiResponse(code=204, message="Retorno sem conteúdo"),
			@ApiResponse(code=404, message="Não foi encontrado nenhum produto com esse ID"),
			@ApiResponse(code=500, message="Erro inesperado")
	})
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN') and #oauth2.hasScope('write')")
	public void removerProduto(@PathVariable("id") Long id){
		produtoRepository.deleteById(id);
	}
}
