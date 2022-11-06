package com.texo.filme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.texo.filme.model.Filme;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {

}
