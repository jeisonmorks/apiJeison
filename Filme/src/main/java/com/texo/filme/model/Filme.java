package com.texo.filme.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Filme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long ano;
	
	@Column(nullable = false)
	private String titulo;
	
	@Column(nullable = false)
	private String estudios;
	
	@Column(nullable = false)
	private String produtores;
	
	@Column(nullable = true)
	private String vencedor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAno() {
		return ano;
	}

	public void setAno(Long ano) {
		this.ano = ano;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEstudios() {
		return estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public String getProdutores() {
		return produtores;
	}

	public void setProdutores(String produtores) {
		this.produtores = produtores;
	}

	public String getVencedor() {
		return vencedor;
	}

	public void setVencedor(String vencedor) {
		this.vencedor = vencedor;
	}
	
	public boolean isVencedor() {
		return this.vencedor != null 
				&& "yes".equals(this.vencedor);
	}
	
}
