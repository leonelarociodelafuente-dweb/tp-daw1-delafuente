package com.biblioteca.controlador;

import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.LibroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroRepository repositorio;

    public LibroController(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Libro crear(@Valid @RequestBody Libro libro) {
        if (repositorio.findByIsbn(libro.getIsbn()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El ISBN ya existe"
            );
        }
        return repositorio.save(libro);
    }

    @GetMapping
    public List<Libro> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public Libro buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"
                ));
    }

    @PutMapping("/{id}")
    public Libro modificar(@PathVariable Long id,
                           @Valid @RequestBody Libro datos) {
        Libro libro = buscar(id);
        libro.setTitulo(datos.getTitulo());
        libro.setAutor(datos.getAutor());
        libro.setIsbn(datos.getIsbn());
        return repositorio.save(libro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        repositorio.delete(buscar(id));
    }
}

