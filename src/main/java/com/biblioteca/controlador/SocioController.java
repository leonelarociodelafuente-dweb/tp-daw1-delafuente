package com.biblioteca.controlador;

import com.biblioteca.modelo.Socio;
import com.biblioteca.repositorio.SocioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioRepository repositorio;

    public SocioController(SocioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Socio crear(@Valid @RequestBody Socio socio) {
        if (repositorio.findByEmail(socio.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El email ya está registrado"
            );
        }
        return repositorio.save(socio);
    }

    @GetMapping
    public List<Socio> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public Socio buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"
                ));
    }

    @PutMapping("/{id}")
    public Socio modificar(@PathVariable Long id,
                           @Valid @RequestBody Socio datos) {
        Socio socio = buscar(id);
        socio.setNombre(datos.getNombre());
        socio.setEmail(datos.getEmail());
        return repositorio.save(socio);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repositorio.delete(buscar(id));
    }
}
