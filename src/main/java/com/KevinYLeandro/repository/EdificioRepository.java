package com.KevinYLeandro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KevinYLeandro.model.Edificio;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {

} 