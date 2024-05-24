package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class LibroService {

    @Autowired
    private LibroRepository repositorio;
    @Autowired
    private AutorRepository repositorio1;

    private ConsumeApi consumidor = new ConsumeApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner input = new Scanner(System.in);
    private Muestra mostrador = new Muestra();
    private Conversa conversador = new Conversa();

    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_BASE_PAGE = "?page=";
    private final String URL_COPYRIGHT = "?copyright=true,false";
    private final String URL_BUSQUEDA = "?search=";
    private final String URL_TOPIC = "?topic=";

    private List<DatosLibro> listaDeDatosLibro = new ArrayList<>();

    //Acá van los métodos de búsqueda en gutendex y persistencia de libros y sus autores en la base de datos
    //Se puede intentar un switch de modos de búsqueda:
    // (por título, por autor, titulo y autor (search), por copyright, por tema, por idioma)
    public Datos buscaYConvierteDatos(String url) {
        var json = consumidor.obtenerDatos(url);
        Datos d = conversor.obtenerDatos(json, Datos.class);
        return d;
    }

    public List<DatosLibro> extraeListaDatosLibro(Datos datos) {
        List<DatosLibro> listaDatosLibro = datos.resultados();
        return listaDatosLibro;
    }

    public static Map<Integer, DatosLibro> convertirListaAMapa(List<DatosLibro> datosLibros) {
        return IntStream.range(0, datosLibros.size())
                .boxed()
                .collect(Collectors.toMap(
                        index -> index + 1,
                        index -> datosLibros.get(index)
                ));
    }

    public DatosLibro eligeLibroDeMapa(Map<Integer, DatosLibro> listaDatosLibros) {
        System.out.println("Elija uno de los siguientes" + listaDatosLibros.size() + "libros");
        mostrador.muestraMapaDatosLibro(listaDatosLibros);
        Integer respuesta = input.nextInt();
        DatosLibro dl = listaDatosLibros.get(respuesta);
        return dl;
    }

    public DatosLibro buscaLibroEnApiPorTitulo() {
        System.out.println("Ingrese una o más palabras del título buscado");
        String titulo = input.nextLine().replace(" ", "+");
        String url = URL_BASE + URL_BUSQUEDA + titulo;
        List<DatosLibro> listaDatosLibro = extraeListaDatosLibro(buscaYConvierteDatos(url));
        if (listaDatosLibro == null || listaDatosLibro.isEmpty()) {
            throw new IllegalArgumentException("La lista de libros no puede estar vacía");
            //manejo de excepciones: IllegalArgumentException
        } else if (listaDatosLibro.size() > 1) {
            Map<Integer, DatosLibro> listaDeDatosLibros = convertirListaAMapa(listaDatosLibro);
            return eligeLibroDeMapa(listaDeDatosLibros);
        } else {
            mostrador.muestraLibro(listaDatosLibro.get(0));
            return listaDatosLibro.get(0);
        }
    }

    public void guardaLibro(Libro l) {
        repositorio.save(l);
    }

    public void gestionaBusquedaDeLibroABD() {
        boolean validador = false;
        do {
            DatosLibro dl = buscaLibroEnApiPorTitulo();
            if (conversador.valida() == true) {
                validador = true;
                Libro libro = new Libro(dl);
                libro.setAutores(asignaAutores(dl));
                guardaLibro(libro);
            }
        } while (validador == false);
    }

    public List<Autor> asignaAutores(DatosLibro datosLibro){
        List<DatosAutor> listaDeDatosAutor = datosLibro.autores();
        List<Autor> autores = new ArrayList<>();
        for (DatosAutor datosAutor : listaDeDatosAutor){
            Autor autor = new Autor(datosAutor);
            Optional<Autor> autorExistente = repositorio1.findByNombreAndNacimientoAndDeceso(autor.getNombre(), autor.getNacimiento(), autor.getDeceso());
            autores.add(autor);
            if(autorExistente.isEmpty()){
                guardaAutor(autor);
            }
        }
        return autores;
    }

    public void guardaAutor(Autor autor){
        repositorio1.save(autor);
    }













}
