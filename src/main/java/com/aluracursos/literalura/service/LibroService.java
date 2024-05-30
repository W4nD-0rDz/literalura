package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    private Datos datos;
    private DatosLibro datosLibro;

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
        datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    public List<DatosLibro> extraeListaDatosLibro(Datos datos) {
        try {
            List<DatosLibro> listaDatosLibro = datos.resultados();
            if (datos.cantidadDeRegistros() >= 1) {
                return listaDatosLibro;
            } else {
                throw new RuntimeException("No se encontraron registros.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("La consulta no ha devuelto resultados. Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("La consulta no ha devuelto los resultados esperados. Error: " + e.getMessage());
        }
        return Collections.emptyList(); // Retorna una lista vacía en lugar de null
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
        System.out.println("Elija uno de los siguientes " + listaDatosLibros.size() + " libros");
        mostrador.muestraMapaDatosLibro(listaDatosLibros);
        Integer respuesta = Integer.parseInt(input.nextLine());
        datosLibro = listaDatosLibros.get(respuesta);
        return datosLibro;
    }

    public void buscaLibroEnApiPorTitulo() {
        try {
            System.out.println("Ingrese una o más palabras del título buscado");
            String titulo = String.valueOf(input.nextLine()).replace(" ", "+");
            String url = URL_BASE + URL_BUSQUEDA + titulo;
            List<DatosLibro> listaDatosLibro = extraeListaDatosLibro(buscaYConvierteDatos(url));
            if (listaDatosLibro == null || listaDatosLibro.isEmpty()) {
                throw new IllegalArgumentException("La lista de libros no puede estar vacía");
            } else if (listaDatosLibro.size() > 1) {
                Map<Integer, DatosLibro> listaDeDatosLibros = convertirListaAMapa(listaDatosLibro);
                datosLibro = eligeLibroDeMapa(listaDeDatosLibros);
            } else {
                mostrador.muestraLibro(listaDatosLibro.get(0));
                datosLibro = listaDatosLibro.get(0);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("La consulta no ha devuelto resultados. Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("La consulta no ha devuelto los resultados esperados. Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error general del sistema: " + e.getMessage());
        }
    }
@Transactional
    public void gestionaBusquedaDeLibroABaseDeDatos() {
        try {
            buscaLibroEnApiPorTitulo();
            if (datosLibro != null) {
                Libro libro = new Libro(datosLibro);
                List<Autor> autores = new ArrayList<>();
                for (DatosAutor datosAutor : datosLibro.autores()) {
                    Autor autor = new Autor(datosAutor);
                    Optional<Autor> autorExistente = validaAutor(autor);
                    if (autorExistente.isPresent()) {
                        autores.add(autorExistente.get());
                        System.out.println("Autor existente en Base de Datos");
                    } else {
                        repositorio1.save(autor);
                        autores.add(autor);
                    }
                }
                libro.setAutores(autores);
                repositorio.save(libro);
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error de integridad de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    public Optional<Autor> validaAutor(Autor autorAValidar) {
        return repositorio1.findByNombreAndNacimientoAndDeceso(autorAValidar.getNombre(), autorAValidar.getNacimiento(), autorAValidar.getDeceso());
    }


    public List<Libro> obtenerTodosLosLibros() {
        return repositorio.findAll();
    }

    public Map<Integer, String[]> mapeaIdiomas() {
        Map<Integer, String[]> mapaDeIdiomas = new HashMap<>();
        do {
            System.out.println("Ingrese al menos una letra del idioma");
            String idiomaABuscar = String.valueOf(input.nextLine());
            int indice = 1;
            for (Idioma idioma : Idioma.values()) {
                if (idioma.getIdiomaEnEspanol().toLowerCase().contains(idiomaABuscar.toLowerCase())
                        || idioma.getIdiomaEnIngles().toLowerCase().contains(idiomaABuscar.toLowerCase())

                        || idioma.getSigla().toLowerCase().contains(idiomaABuscar.toLowerCase())) {
                    String[] infoIdioma = {
                            idioma.getSigla(), idioma.getIdiomaEnEspanol(), idioma.getIdiomaEnIngles()};
                    mapaDeIdiomas.put(indice, infoIdioma);
                    indice++;
                }
            }
            if (mapaDeIdiomas.isEmpty()) {
                System.out.println("Disculpe, no se ha encontrado el idioma." +
                        "\nIntente nuevamente.");
            }
        } while (mapaDeIdiomas.isEmpty());
        return mapaDeIdiomas;
    }

    public Idioma selectorDeIdioma() {
        Integer indice;
        Map<Integer, String[]> mapaIdiomas;
        Idioma idioma = null;
        try {
            do {
                mapaIdiomas = mapeaIdiomas();
                System.out.println("Lista de idiomas que coinciden con las letras ingresadas");
                mostrador.muestraGenerico(mapaIdiomas);
                System.out.println("Elija el código de la divisa en la lista. Si no se encuentra presione 0");
                indice = Integer.parseInt(input.nextLine());
                if (indice != 0) {
                    String[] infoIdioma = mapaIdiomas.get(indice);
                    idioma = Idioma.desdeUsuarioEnEspanol(infoIdioma[1]);
                }
            } while (mapaIdiomas.isEmpty() || idioma == null);
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese el código de la divisa de su elección." +
                    "\nSi no la encuentra presione 0.");
        } catch (NullPointerException e) {
            System.out.println("Disculpas, no se encuentra ninguna divisa conteniendo esas letras." +
                    "\nIntente nuevamente.");
        }
        return idioma;
    }

    public List<Libro> buscaLibrosPorIdioma() {
        Idioma idioma = selectorDeIdioma();
        List<Libro> listaDeLibrosPorIdioma = repositorio.obtenerLibrosPorIdioma(idioma);
        return listaDeLibrosPorIdioma;
    }

    public List<Libro> librosMasDescargados() {
        List<Libro> top10Libros = repositorio.top10Libros();
        return top10Libros;
    }

    public Map<Integer, String> mapeaAutores() {
        List<Autor> listaDeAutores = repositorio1.findAll();
        Map<Integer, String> mapaDeAutores = new HashMap<>();
        do {
            System.out.println("Ingrese al menos una letra del nombre del autor");
            String letrasDelNombre = String.valueOf(input.nextLine()).toLowerCase();
            int indice = 1;

            for (Autor autor : listaDeAutores) {
                if (autor.getNombre().toLowerCase().contains(letrasDelNombre)) {
                    String nombres = autor.getNombre();
                    mapaDeAutores.put(indice, nombres);
                    indice++;
                }
            }
            if (mapaDeAutores.isEmpty()) {
                System.out.println("Disculpe, no se han encontrado autores." +
                        "\nIntente nuevamente.");
            }
        } while (mapaDeAutores.isEmpty());
        return mapaDeAutores;
    }

    public String selectorDeAutor() {
        String nombreAutor = null;
        Map<Integer, String> mapaAutores;
        int indice;
        do {
            mapaAutores = mapeaAutores();
            System.out.println("Lista de autores que coinciden con las letras ingresadas");
            mostrador.muestraGenerico(mapaAutores);
            System.out.println("Elija el código de un autor de la lista. Si no se encuentra presione 0");

            try {
                indice = Integer.parseInt(input.nextLine());
                if (indice != 0) {
                    nombreAutor = mapaAutores.get(indice);
                } else {
                    System.out.println("Índice no válido. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        } while (nombreAutor == null);
        return nombreAutor;
    }

    public List<Libro> buscaLibrosPorAutor() {
        String nombreAutor = selectorDeAutor();
        List<Libro> listaLibrosPorAutor = repositorio.obtenerLibrosPorAutor(nombreAutor);
        return listaLibrosPorAutor;
    }
}
