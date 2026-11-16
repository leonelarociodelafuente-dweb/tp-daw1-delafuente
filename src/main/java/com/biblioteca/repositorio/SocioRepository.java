package com.biblioteca.repositorio;

import com.biblioteca.modelo.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByEmail(String email);

}