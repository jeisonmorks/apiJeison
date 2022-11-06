package com.texo.filme.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Resultado {

	private Collection<Vencedor> min;
	private Collection<Vencedor> max;
	
	public Resultado(Collection<Vencedor> min, Collection<Vencedor> max) {
		super();
		this.min = min;
		this.max = max;
	}

	public void addMenorVencedor(Vencedor vencedor) {
		if (min == null) {
			min = new ArrayList<>();
		}
		min.add(vencedor);
	}
	
	public void addMaiorVencedor(Vencedor vencedor) {
		if (max == null) {
			max = new ArrayList<>();
		}
		max.add(vencedor);
	}
	
	public Collection<Vencedor> getMin() {
		return Collections.unmodifiableCollection(min);
	}
	
	public Collection<Vencedor> getMax() {
		return Collections.unmodifiableCollection(max);
	}
	
	
}
