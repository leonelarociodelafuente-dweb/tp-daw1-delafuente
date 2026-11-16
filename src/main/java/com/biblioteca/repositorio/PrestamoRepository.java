package com.biblioteca.repositorio;

import com.biblioteca.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByEstado(String estado);

    List<Prestamo> findBySocioId(Long socioId);

    List<Prestamo> findByLibroIdAndEstado(Long libroId, String estado);

    List<Prestamo> findBySocioIdAndEstado(Long socioId, String estado);
}