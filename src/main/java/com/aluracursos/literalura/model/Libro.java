package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long numeroID;

    @Column
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    //@Transient
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @Column
    private Double descargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.numeroID = datosLibro.numeroID();
        this.titulo = datosLibro.titulo();
        this.idioma = Idioma.desdeListaIdioma(datosLibro.idiomas());
        this.descargas = datosLibro.descargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroID() {
        return numeroID;
    }

    public void setNumeroID(Long numeroID) {
        this.numeroID = numeroID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> listaAutores) {
        this.autores = listaAutores;
    }

    public Double getDescargas() {
        return descargas;
    }

    public void setDescargas(Double descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return titulo.toUpperCase() + " (" + idioma.getIdiomaEnEspanol() + ") " +
                "\nAutor(es): " + autores + "\nDescargado: " + descargas + " veces";
    }
}