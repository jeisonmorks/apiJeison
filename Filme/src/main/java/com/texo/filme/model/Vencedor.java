package com.texo.filme.model;

import java.util.Objects;

public class Vencedor {

	private String producer;
	private Long interval;
	private Long previousWin;
	private Long followingWin;

	public Vencedor(String producer, Long followingWin) {
		super();
		this.producer = producer;
		this.followingWin = followingWin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(producer);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vencedor other = (Vencedor) obj;
		return Objects.equals(producer, other.producer);
	}

	public String getProducer() {
		return producer;
	}
	
	public void setProducer(String producer) {
		this.producer = producer;
	}

	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public Long getPreviousWin() {
		return previousWin;
	}

	public void setPreviousWin(Long previousWin) {
		this.previousWin = previousWin;
	}

	public Long getFollowingWin() {
		return followingWin;
	}

	public void setFollowingWin(Long followingWin) {
		this.followingWin = followingWin;
	}
	
}
