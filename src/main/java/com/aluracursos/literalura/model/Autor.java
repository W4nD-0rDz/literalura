package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    String nombre;
    @Column
    LocalDate nacimiento;
    @Column
    LocalDate deceso;


    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        //Autor anónimo
        if (datosAutor.nacimiento() == null && datosAutor.deceso() == null && datosAutor.nombre().equalsIgnoreCase("Anonymous")) {
            this.nombre = datosAutor.nombre();
            this.nacimiento = null;
            this.deceso = null;
        } else {
            this.nombre = Optional.ofNullable(datosAutor.nombre())
                    .orElseThrow(() -> new IllegalArgumentException("El nombre no puede ser nulo"));

            this.nacimiento = Optional.ofNullable(datosAutor.nacimiento())
                    .map(year -> LocalDate.of(datosAutor.nacimiento(), 1, 1))
                    .orElseThrow(() -> new IllegalArgumentException("El año de nacimiento no puede ser nulo"));

            this.deceso = Optional.ofNullable(datosAutor.deceso())
                    .map(year -> LocalDate.of(datosAutor.deceso(), 12, 31))
                    .orElse(LocalDate.now());

                // Corregir fechas si están incorrectamente asignadas
            if (datosAutor.nacimiento() > datosAutor.deceso()) {
                this.nacimiento = LocalDate.of(datosAutor.deceso(), 1, 1);
                this.deceso = LocalDate.of(datosAutor.nacimiento(), 12, 31);
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public LocalDate getDeceso() {
        return deceso;
    }

    public int getNacimientoYear() {
        return nacimiento != null ? nacimiento.getYear() : Integer.MAX_VALUE;
    }

    public int getDecesoYear() {
        return deceso != null ? deceso.getYear() : Integer.MIN_VALUE;
    }


    @Override
    public String toString() {
        return nombre + " (" + getNacimientoYear() + "-" + getDecesoYear() + ")";
    }
}
