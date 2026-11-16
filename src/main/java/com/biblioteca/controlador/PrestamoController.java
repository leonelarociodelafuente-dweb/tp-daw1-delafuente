
package com.biblioteca.controlador;

import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Socio;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.repositorio.LibroRepository;
import com.biblioteca.repositorio.SocioRepository;
import com.biblioteca.repositorio.PrestamoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    public PrestamoController(
            PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    public record CrearPrestamoRequest(
            @NotNull Long libroId,
            @NotNull Long socioId,
            @NotNull @Future LocalDate fechaDevolucion) {
    }

    public record EstadoRequest(String estado) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prestamo crear(@Valid @RequestBody CrearPrestamoRequest datos) {

        Libro libro = libroRepository.findById(datos.libroId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"));

        Socio socio = socioRepository.findById(datos.socioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"));

        if (!prestamoRepository.findByLibroIdAndEstado(
                libro.getId(), "ACTIVO").isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El libro ya está prestado");
        }

        long prestamosActivos = prestamoRepository
                .findBySocioIdAndEstado(socio.getId(), "ACTIVO").size();

        if (prestamosActivos >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El socio ya tiene tres préstamos activos");
        }

        Prestamo prestamo = new Prestamo(
                libro, socio, datos.fechaDevolucion());

        return prestamoRepository.save(prestamo);
    }

    @GetMapping
    public List<Prestamo> listar(
            @RequestParam(required = false) String estado) {

        if (estado == null) {
            return prestamoRepository.findAll();
        }

        return prestamoRepository.findByEstado(estado);
    }

    @GetMapping("/{id}")
    public Prestamo buscar(@PathVariable Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Préstamo no encontrado"));
    }

    @PatchMapping("/{id}/estado")
    public Prestamo cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoRequest datos) {

        Prestamo prestamo = buscar(id);
        String actual = prestamo.getEstado();
        String nuevo = datos.estado();

        boolean permitido =
                ("ACTIVO".equals(actual) &&
                        ("DEVUELTO".equals(nuevo) || "VENCIDO".equals(nuevo)))
                        || ("VENCIDO".equals(actual) && "DEVUELTO".equals(nuevo));

        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se permite ese cambio de estado");
        }

        prestamo.setEstado(nuevo);
        return prestamoRepository.save(prestamo);
    }
}

