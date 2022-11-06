package com.texo.filme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.texo.filme.model.Filme;
import com.texo.filme.model.Resultado;
import com.texo.filme.model.Vencedor;
import com.texo.filme.repository.FilmeRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class FilmeApplicationTests {

	private static final String YES = "yes";

	private static final String QUENTIN_TARANTINO = "Quentin Tarantino";

	private static final String STEVEN_SPIELBERG = "Steven Spielberg";

	private static final String RANDAL_KLEISER = "Randal Kleiser";

	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@Autowired
	private FilmeRepository filmeRepository;
	
	@BeforeAll
	public void carregaDados() {
		criaFilme(1980L, "A lagoa azul", "Columbia Pictures", RANDAL_KLEISER, YES);
		criaFilme(2005L, "S.O.S. do Amor", "Columbia Pictures", RANDAL_KLEISER, YES);
		criaFilme(1971L, "Encurralado", "Universal Studios", STEVEN_SPIELBERG, YES);
		criaFilme(2022L, "The Fabelmans", "Universal Pictures", STEVEN_SPIELBERG, YES);
		criaFilme(1987L, "My Best Friend's Birthday", "Super Happy Fun", QUENTIN_TARANTINO, YES);
		criaFilme(1991L, "Past Midnight", "New Line Cinema", QUENTIN_TARANTINO, YES);
	}
	
	public Filme criaFilme(Long ano, String titulo, String estudios, String produtores, String vencedor) {
		Filme cadastrar = new Filme();
		cadastrar.setAno(ano);
		cadastrar.setTitulo(titulo);
		cadastrar.setEstudios(estudios);
		cadastrar.setProdutores(produtores);
		cadastrar.setVencedor(vencedor);
		return filmeRepository.save(cadastrar);
	}
	
	@Test
	void contextLoads() {
		HttpEntity<?> httpEntity = new HttpEntity<>(HttpEntity.EMPTY);
		
		ResponseEntity<Resultado> response = this.testRestTemplate
	            .exchange("/filme", HttpMethod.GET, httpEntity, Resultado.class);
		
		Resultado body = response.getBody();
		Collection<Vencedor> min = body.getMin();
		assertFalse(min.isEmpty());
		for (Vencedor vencedor : min) {
			assertEquals(vencedor.getProducer(), "Joel Silver");
		}

		Collection<Vencedor> max = body.getMax();
		assertFalse(max.isEmpty());
		for (Vencedor vencedor : max) {
			assertEquals(vencedor.getProducer(), "Matthew Vaughn");
		}
		
	}

}
