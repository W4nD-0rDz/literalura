package com.aluracursos.literalura.service;

import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Busca {
    ConsumeApi consumidor = new ConsumeApi();
    ConvierteDatos conversor = new ConvierteDatos();

    @Autowired
    private LibroRepository repositorio;

    //Acá van los métodos de búsqueda en gutendex y persistencia de libros y sus autores en la base de datos
    //Se puede intentar un switch de modos de búsqueda:
    // (por título, por autor, titulo y autor (search), por copyright, por tema, por idioma)




}
