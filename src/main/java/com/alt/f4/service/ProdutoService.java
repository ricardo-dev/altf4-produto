package com.alt.f4.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.alt.f4.model.Media;
import com.alt.f4.model.Produto;
import com.alt.f4.repositories.MediaRepository;
import com.alt.f4.repositories.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	public Produto salvar(Produto produto) {
		Produto produtoSalvo = produtoRepository.save(produto);
		produto.getMedias().forEach(m -> {
			m.setProduto(produtoSalvo);
			mediaRepository.save(m);
			});
		return produtoSalvo;
	}

	public Produto atualizarProduto(Long id,Produto produto) {
		Produto produtoSalvo = this.findProduto(id);
		
		if(!produtoSalvo.getMedias().isEmpty()) {
			mediaRepository.deleteAll(produtoSalvo.getMedias());
		}
		
		List<Media> medias = new ArrayList<>();
		produto.getMedias().forEach(pm -> {
			Media m = new Media(pm.getPathImagem(), produtoSalvo, pm.isPrincipal());
			medias.add(mediaRepository.save(m));
		});
		produtoSalvo.setMedias(medias);
		
		BeanUtils.copyProperties(produto, produtoSalvo, "id","medias");
		return produtoRepository.save(produtoSalvo);
	}
	
	public void atualizarProdutoPromocao(Long id, boolean promocao) {
		Produto produtoSalvo = this.findProduto(id);
		produtoSalvo.setPromocao(promocao);
		produtoRepository.save(produtoSalvo);
	}

	private Produto findProduto(Long id) {
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return produto;
	}
}
