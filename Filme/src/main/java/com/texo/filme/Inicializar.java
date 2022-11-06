package com.texo.filme;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.texo.filme.model.Filme;
import com.texo.filme.repository.FilmeRepository;

@Service
public class Inicializar {

	@Autowired
	private FilmeRepository filmeRepository;
	
	public Inicializar() {
		super();
	}

	@Autowired
	public void inicializaBanco() {
		try {  
			URL url = this.getClass().getClassLoader().getResource("movielist.csv");
			if (url == null) {
				return ;
			}
			try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
				CSVReader csvReader = new CSVReaderBuilder(reader)
						.withSkipLines(1)//para o caso do CSV ter cabeçalho.
						.build();
				
				String [] leitura;
				while ((leitura = csvReader.readNext()) != null) {
					Filme newFilme = new Filme();
					String linha = null;
					for (int i = 0; i < leitura.length; i++) {
						if (linha == null) {
							linha = leitura[i];
						} else {
							linha = linha + "," + leitura[i];
						}
					}
					String[] valores = linha.split(";");
					int length = valores.length;
					if (length >= 1) {
						try {
							newFilme.setAno(Long.valueOf(valores[0]));
						} catch (NumberFormatException e) {
							newFilme.setAno(0L);
						}
					}
					if (length >= 2) {
						newFilme.setTitulo(String.valueOf(valores[1]));
					}
					if (length >= 3) {
						newFilme.setEstudios(String.valueOf(valores[2]));
					}
					if (length >= 4) {
						newFilme.setProdutores(String.valueOf(valores[3]));
					}
					if (length >= 5) {
						newFilme.setVencedor(String.valueOf(valores[4]));
					}
					
					filmeRepository.save(newFilme);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
