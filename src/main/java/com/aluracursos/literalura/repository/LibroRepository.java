package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

@Query("SELECT l FROM Libro l WHERE l.idioma = :idiomaBuscado ORDER BY l.descargas DESC")
    List<Libro> obtenerLibrosPorIdioma(@Param("idiomaBuscado")Idioma idioma);
}
