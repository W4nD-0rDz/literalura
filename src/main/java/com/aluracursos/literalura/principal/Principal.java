package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.Conversa;
import com.aluracursos.literalura.service.LibroService;
import com.aluracursos.literalura.service.Muestra;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {
    LibroService servicio = new LibroService();
    Scanner input = new Scanner(System.in);
    Muestra mostrador = new Muestra();
    private LibroRepository repositorio;
    private AutorRepository repositori1;


    String menuDelUsuario = """
            ********************************
            Bienvenido a la Biblioteca Alura

            1 - Buscar Libro por Título
            2 - Listar libros registrados
            3 - Listar autores registrados
            4 - Listar autores por fecha
            5 - Listar autores por nombre
            0 - salir
            *********************************
            """;

    public Principal(LibroService servicio, LibroRepository repository, AutorRepository repository1, Muestra muestra) {
        this.servicio = servicio;
        this.repositorio = repository;
        this.repositori1 = repository1;
        this.mostrador = muestra;
    }

    //Menu del usuario
    public void menu() {
        int option;
        do {
            System.out.println(menuDelUsuario);
            System.out.print("Por favor, seleccione una acción a realizar: ");
            option = Integer.parseInt(input.nextLine());
            switch (option) {
                case 1:
                    servicio.gestionaBusquedaDeLibroABD();
                    break;
                case 2:
                    mostrador.muestraListaDeLibros();
                    break;
                case 3:
                    mostrador.muestraAutores();
                    break;
                case 4:
                    mostrador.muestraAutoresVivosEn();
                    break;
                case 5:
                    mostrador.muestraAutoresPorNombre();
                    break;

                case 0:
                    System.out.println("Hasta pronto. \n¡Gracias por visitar Biblioteca Alura!");
                    break;
                default:
                    System.out.println("Elija una opción válida");
                    break;
            }
        } while (option != 0);
    }
}
