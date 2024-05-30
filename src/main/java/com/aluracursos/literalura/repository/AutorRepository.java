package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreAndNacimientoAndDeceso(String name, LocalDate nacimiento, LocalDate deceso);

    @Query("SELECT a FROM Autor a WHERE YEAR(a.nacimiento) <= " +
            ":year AND (a.deceso IS NULL OR YEAR(a.deceso) >= :year)")
    List<Autor> findAutoresVivosEn(@Param("year") int year);

    @Query("SELECT a FROM Autor a WHERE a.nombre ILIKE %:nombreBuscado%")
    List<Autor> buscarPorNombre(@Param("nombreBuscado") String nombreBuscado);
}