package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

@Query("SELECT l FROM Libro l WHERE l.idioma = :idiomaBuscado ORDER BY l.descargas DESC")
    List<Libro> obtenerLibrosPorIdioma(@Param("idiomaBuscado")Idioma idioma);

@Query("SELECT l FROM Libro l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> top10Libros();

    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE a.nombre = :nombreAutor")
    List<Libro> obtenerLibrosPorAutor(@Param("nombreAutor") String nombreAutor);
}
