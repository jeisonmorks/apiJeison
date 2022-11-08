package com.texo.filme.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.texo.filme.CompareUtils;
import com.texo.filme.IntervaloUtils;
import com.texo.filme.model.Filme;
import com.texo.filme.model.Resultado;
import com.texo.filme.model.Vencedor;
import com.texo.filme.repository.FilmeRepository;

@RestController
@RequestMapping("/filme")
public class FilmeController {
	
	@Autowired
	private FilmeRepository filmeRepository;

	@GetMapping(path = "/{id:[0-9][0-9]*}")
	public ResponseEntity<Filme> get(@PathVariable("id") Long id) {
		Optional<Filme> findFilme = getFilme(id);
		return findFilme.isPresent() ? ResponseEntity.ok(findFilme.get()) : ResponseEntity.notFound().build();
	}
	
	
	@GetMapping(path = "/todos")
	public Collection<Filme> buscaTodos() {
		List<Filme> resultados = filmeRepository.findAll();
		return resultados;
	}
	
	@GetMapping(path = "/vencedor")
	public Resultado buscaFilmes() {
		List<Filme> resultados = filmeRepository.findAll();
		resultados = resultados.stream().filter((resultado) -> resultado.isVencedor()).collect(Collectors.toList());
		
		//Ordenar por ano + id para pegar os casos em que o prêmio é inserido fora de ordem
		resultados.sort((o1, o2) -> getComparePremios(o1, o2));
		
		Long menorIntervalo = null;
		Long maiorIntervalo = null;
		Map<String, Long> mapVencedores = new HashMap<>();
		Collection<Vencedor> menoresVencedores = new ArrayList<>();
		Collection<Vencedor> maioresVencedores = new ArrayList<>();
		for (Iterator<Filme> iterator = resultados.iterator(); iterator.hasNext();) {
			Filme filme = iterator.next();
			
			Long ano = filme.getAno();
			String produtores = filme.getProdutores();
			String[] listaProdutores = produtores.split(",\\s|\\sand\\s");
			for (String produtor : listaProdutores) {
				if (mapVencedores.containsKey(produtor)) {
					Long anoAnterior = mapVencedores.get(produtor);
					Long intervalo = IntervaloUtils.calculaIntervalo(ano, anoAnterior);

					Supplier<Vencedor> retornaVencedor = () -> new Vencedor(produtor, intervalo, anoAnterior, ano);
					
					menorIntervalo = verificaMenorIntervalo(menorIntervalo, menoresVencedores, retornaVencedor, intervalo);
					maiorIntervalo = verificaMaiorIntervalo(maiorIntervalo, maioresVencedores, retornaVencedor, intervalo);
				}
				
				mapVencedores.put(produtor, ano);
			}
			
		}
		
		return new Resultado(menoresVencedores, maioresVencedores);
	}

	private int getComparePremios(Filme o1, Filme o2) {
		int compare = CompareUtils.compare(o1.getAno(), o2.getAno());
		if (compare == 0) {
			compare = CompareUtils.compare(o1.getId(), o2.getId());
		}
		return compare;
	}
	
	/**
	 * Verifica o menor intervalo
	 * @param menorIntervalo
	 * @param menoresVencedores
	 * @param vencedor
	 * @param intervalo
	 * @return
	 */
	private Long verificaMenorIntervalo(Long menorIntervalo, Collection<Vencedor> menoresVencedores, Supplier<Vencedor> retornaVencedor,
			Long intervalo) {
		if (menorIntervalo == null) {
			menorIntervalo = intervalo;
		}
		int compare = CompareUtils.compare(intervalo, menorIntervalo);
		if (compare <= 0) {
			if (compare < 0) {
				menoresVencedores.clear();
				menorIntervalo = intervalo;
			}
			menoresVencedores.add(retornaVencedor.get());
		}
		return menorIntervalo;
	}

	/**
	 * Verifica o maior intervalo
	 * @param maiorIntervalo
	 * @param maioresVencedores
	 * @param vencedor
	 * @param intervalo
	 * @return
	 */
	private Long verificaMaiorIntervalo(Long maiorIntervalo, Collection<Vencedor> maioresVencedores, Supplier<Vencedor> retornaVencedor,
			Long intervalo) {
		if (maiorIntervalo == null) {
			maiorIntervalo = intervalo;
		}
		
		int compare = CompareUtils.compare(intervalo, maiorIntervalo);
		if (compare >= 0) {
			if (compare > 0) {
				maioresVencedores.clear();
				maiorIntervalo = intervalo;
			}
			maioresVencedores.add(retornaVencedor.get());
		}
		return maiorIntervalo;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Filme adicionarFilme(@RequestBody Filme filme) {
		return filmeRepository.save(filme);
	}
	
	@PutMapping
	public Filme atualizarFilme(@RequestBody Filme filme) {
		Optional<Filme> findFilme = getFilme(filme.getId());
		if (findFilme.isPresent()) {
			Filme atualizar = findFilme.get();
			atualizar.setAno(filme.getAno());
			atualizar.setTitulo(filme.getTitulo());
			atualizar.setEstudios(filme.getEstudios());
			atualizar.setProdutores(filme.getProdutores());
			atualizar.setVencedor(filme.getVencedor());
			return filmeRepository.save(atualizar);
		} else {
			return filmeRepository.save(filme);
		}
	}
	
	@PatchMapping
	public Filme atualizarParcial(@RequestBody Filme filme) {
		Optional<Filme> findFilme = getFilme(filme.getId());
		if (findFilme.isPresent()) {
			Filme atualizar = findFilme.get();
			atualizaSeExistir(filme.getAno(), atualizar::setAno);
			atualizaSeExistir(filme.getTitulo(), atualizar::setTitulo);
			atualizaSeExistir(filme.getEstudios(), atualizar::setEstudios);
			atualizaSeExistir(filme.getProdutores(), atualizar::setProdutores);
			atualizaSeExistir(filme.getVencedor(), atualizar::setVencedor);
			return filmeRepository.save(atualizar);
		}
		return filme;
	}
	
	protected <T> void atualizaSeExistir(T novo, Consumer<T> consume) {
		if (novo != null) {
			consume.accept(novo);
		}
	}
	
	@DeleteMapping
	public void deletaFilme(@RequestParam(name = "id") Long id) {
		filmeRepository.deleteById(id);
	}
	
	/**
	 * Retorna um filme
	 * @param id - id do filme
	 * @return
	 */
	private Optional<Filme> getFilme(Long id) {
		return id == null ? Optional.empty() : filmeRepository.findById(id);
	}
	
}
