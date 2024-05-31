package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.LibroService;
import com.aluracursos.literalura.service.Controla;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class Principal {
    LibroService servicio = new LibroService();
    Scanner input = new Scanner(System.in);
    Controla mostrador = new Controla();
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
            6 - Listar libros por idioma
            7 - Listar libros más descargados
            8 - Listar libros por autor
                        
            0 - salir
            *********************************
            
            """;

    public Principal(LibroService servicio, LibroRepository repository, AutorRepository repository1, Controla controla) {
        this.servicio = servicio;
        this.repositorio = repository;
        this.repositori1 = repository1;
        this.mostrador = controla;
    }

    //Menu del usuario
    public void menu() {
        int option = -1;
        do {
            try {
                System.out.println(menuDelUsuario);
                System.out.print("Por favor, seleccione una acción a realizar: ");
                option = Integer.parseInt(input.nextLine());
                switch (option) {
                    case 1:
                        servicio.gestionaBusquedaDeLibroABaseDeDatos();
                        break;
                    case 2:
                        mostrador.muestraTodosLosLibros();
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
                    case 6:
                        mostrador.muestraLibrosPorIdioma();
                        break;
                    case 7:
                        mostrador.muestra10LibrosMasDescargados();
                        break;
                    case 8:
                        mostrador.muestraLibrosPorAutor();
                        break;
                    case 0:
                        System.out.println("Hasta pronto. \n¡Gracias por visitar Biblioteca Alura!");
                        break;
                    default:
                        System.out.println("Elija una opción válida");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error. El valor ingresado es incorrecto. \nPor favor intente nuevamente.");
            }
        } while (option != 0);
    }
}