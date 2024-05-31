package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    private Imprime impresor = new Imprime();
    private Valida validador = new Valida();

    private Datos datos;
    private DatosLibro datosLibro;
    private List<DatosLibro> listaDeDatosLibro = new ArrayList<>();

    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_BUSQUEDA = "?search=";

    //Acá van los métodos de búsqueda en gutendex y persistencia de libros y sus autores en la base de datos

    //Extracción y conversión de datos
    public Datos buscaYConvierteDatos(String url) {
        var json = consumidor.obtenerDatos(url);
        datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    public List<DatosLibro> extraeListaDatosLibro(Datos datos) {
        try {
            listaDeDatosLibro = datos.resultados();
            if (datos.cantidadDeRegistros() >= 1) {
                return listaDeDatosLibro;
            } else {
                throw new RuntimeException("No se encontraron registros.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("La consulta no ha devuelto resultados * . " + e.getMessage());
            System.out.println("");
        } catch (RuntimeException e) {
            System.out.println("La consulta no ha devuelto resultados.");
            System.out.println("");
        }
        return Collections.emptyList(); // Retorna una lista vacía en lugar de null
    }

    //Opción 1 - Consulta a la API y gestiona la persistencia del libro y autor(es) en la BD
    @Transactional()
    public void gestionaBusquedaDeLibroABaseDeDatos() {
        try {
            buscaLibroEnApiPorTitulo();
            Optional.ofNullable(datosLibro)
                    .map(Libro::new)
                    .ifPresent(libro -> {
                        libro.setAutores(armaListaAutores());
                        if (validador.valida()) {
                            repositorio.save(libro);
                        }
                    });
        } catch (DataIntegrityViolationException e) {
            System.out.println("Error de integridad de datos al guardar el libro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al procesar el libro: " + e.getMessage());
        }
    }

    public void buscaLibroEnApiPorTitulo() {
        int intentos = 0;
        boolean libroEncontrado = false;

        while (!libroEncontrado && intentos < 3) {
            try {
                System.out.println("Ingrese una o más palabras del título buscado");
                String titulo = input.nextLine().replace(" ", "+");
                String url = URL_BASE + URL_BUSQUEDA + titulo;
                List<DatosLibro> listaDatosLibro = extraeListaDatosLibro(buscaYConvierteDatos(url));

                if (listaDatosLibro == null || listaDatosLibro.isEmpty()) {
                    System.out.println("La consulta no ha devuelto resultados. Intente nuevamente.");
                } else if (listaDatosLibro.size() > 1) {
                    Map<Integer, DatosLibro> listaDeDatosLibros = convertirListaAMapa(listaDatosLibro);
                    datosLibro = eligeLibroDeMapa(listaDeDatosLibros);
                    libroEncontrado = datosLibro != null && datosLibro.titulo() != null && !datosLibro.titulo().isEmpty();
                    if (!libroEncontrado) {
                        System.out.println("La selección no es válida. Por favor, intente nuevamente.");
                    }
                } else {
                    impresor.mostrarElemento(listaDatosLibro.get(0));
                    datosLibro = listaDatosLibro.get(0);
                    libroEncontrado = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Entrada no válida. Por favor, ingrese un número.");
            } catch (RuntimeException e) {
                System.out.println("La consulta no ha devuelto los resultados esperados.");
                System.out.println("");
            } catch (Exception e) {
                System.out.println("Error general del sistema: " + e);
                e.printStackTrace();
            }
            intentos++;
            if (intentos == 3 && !libroEncontrado) {
                System.out.println("Demasiados intentos fallidos. Vuelva a intentar más tarde.");
            }
        }
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
        System.out.println("Elija uno de los siguientes " + listaDatosLibros.size() + " libros." +
                "\nEn caso que no se encuentre el libro buscado, elija 0.");
        impresor.mostrarElemento(listaDatosLibros);
        Integer respuesta = Integer.parseInt(input.nextLine());

        if (respuesta > 0 && respuesta <= listaDatosLibros.size()) {
            return listaDatosLibros.get(respuesta);
        } else {
            // Si la respuesta está fuera del rango Integer de la respuesta, devuelve un DatosLibro vacío
            return listaDatosLibros.get(0);
        }
    }

    public List<Autor> armaListaAutores() {
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
        return autores;
    }

    public Optional<Autor> validaAutor(Autor autorAValidar) {
        return repositorio1.findByNombreAndNacimientoAndDeceso(
                autorAValidar.getNombre(), autorAValidar.getNacimiento(), autorAValidar.getDeceso());
    }

    //Opción 2 - arma la lista de todos los libros de la BD
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> todosLosLibros = repositorio.findAll();
        if (todosLosLibros.isEmpty()) {
            System.out.println("La base de datos está vacía");
            return todosLosLibros;
        } else {
            return todosLosLibros;
        }
    }

    //Opción 6 - filtra libros por idioma
    public List<Libro> buscaLibrosPorIdioma() {
        List<Libro> libroPorIdioma = repositorio.obtenerLibrosPorIdioma(selectorDeIdioma());
        if (libroPorIdioma.isEmpty()) {
            System.out.println("No hay resultados para esta búsqueda");
        }
        return libroPorIdioma;
    }

    public Map<Integer, String[]> mapeaIdiomas() {
        Map<Integer, String[]> mapaDeIdiomas = new HashMap<>();
        do {
            System.out.println("Ingrese al menos una letra del idioma");
            String idiomaABuscar = String.valueOf(input.nextLine()).toLowerCase();
            mapaDeIdiomas = IntStream.range(0, Idioma.values().length)
                    .filter(i -> {
                        Idioma idioma = Idioma.values()[i];
                        return idioma.getIdiomaEnEspanol().toLowerCase().contains(idiomaABuscar)
                                || idioma.getIdiomaEnIngles().toLowerCase().contains(idiomaABuscar)
                                || idioma.getSigla().toLowerCase().contains(idiomaABuscar);
                    })
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> i + 1,
                            i -> {
                                Idioma idioma = Idioma.values()[i];
                                return new String[]{idioma.getSigla(), idioma.getIdiomaEnEspanol()};
                            }
                    ));
            if (mapaDeIdiomas.isEmpty()) {
                System.out.println("Disculpe, no se ha encontrado el idioma." + "\nIntente nuevamente.");
            }
        } while (mapaDeIdiomas.isEmpty());
        return mapaDeIdiomas;
    }

    public Idioma selectorDeIdioma() {
        Idioma idioma = null;
        try {
            do {
                Map<Integer, String[]> mapaIdiomas = mapeaIdiomas();
                System.out.println("Lista de idiomas que coinciden con las letras ingresadas");
                impresor.mostrarElemento(mapaIdiomas);
                System.out.println("Elija el código del idioma en la lista. Si no se encuentra presione 0");
                Integer indice = Integer.parseInt(input.nextLine());
                if (indice != 0) {
                    idioma = Optional.ofNullable(mapaIdiomas.get(indice))
                            .map(info -> Idioma.desdeUsuarioEnEspanol(info[1]))
                            .orElse(null);
                }
            } while (idioma == null);
        } catch (NumberFormatException e) {
            System.out.println("Por favor ingrese el código del idioma de su elección." +
                    "\nSi no la encuentra presione 0.");
        } catch (NullPointerException e) {
            System.out.println("Disculpas, no se encuentra ningún idioma conteniendo esas letras." +
                    "\nIntente nuevamente.");
        }
        return idioma;
    }

    //Opción 7 - lista los 10 libros con mayor cantidad de descargas
    public List<Libro> librosMasDescargados() {
        return repositorio.top10Libros();
    }

    //Opción 8 - Filtra libros por autor
    public List<Libro> buscaLibrosPorAutor() {
        List<Libro> libroPorAutor = repositorio.obtenerLibrosPorAutor(selectorDeAutor());
        if (libroPorAutor.isEmpty()) {
            System.out.println("No hay resultados para esta búsqueda");
        }
        return libroPorAutor;
    }

    public Map<Integer, String> mapeaAutores() {
        List<Autor> listaDeAutores = repositorio1.findAll();
        Map<Integer, String> mapaDeAutores = new HashMap<>();
        int intentos = 0;
        do {
            System.out.println("Ingrese al menos una letra del nombre del autor");
            String letrasDelNombre = String.valueOf(input.nextLine()).toLowerCase();
            intentos++;
            mapaDeAutores = IntStream.range(0, listaDeAutores.size())
                    .filter(i -> listaDeAutores.get(i).getNombre().toLowerCase().contains(letrasDelNombre))
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> i + 1,
                            i -> listaDeAutores.get(i).getNombre()));

            if (mapaDeAutores.isEmpty()) {
                System.out.println("Disculpe, no se han encontrado autores.\nIntente nuevamente.");
            }
            if (intentos == 3) {
                System.out.println("Demasiados intentos fallidos. Vuelva a intentar más tarde.");
            }
        } while (mapaDeAutores.isEmpty() && intentos < 3);
        return mapaDeAutores;
    }

    public String selectorDeAutor() {
        String nombreAutor = null;
        do {
            Map<Integer, String> mapaAutores = mapeaAutores();
            System.out.println("Lista de autores que coinciden con las letras ingresadas");
            impresor.mostrarElemento(mapaAutores);
            System.out.println("Elija el código de un autor de la lista. Si no se encuentra presione 0");
            try {
                int indice = Integer.parseInt(input.nextLine());
                nombreAutor = Optional.ofNullable(mapaAutores.get(indice))
                        .orElseGet(() -> {
                            System.out.println("El valor " + indice + " no es válido. Intente nuevamente.");
                            return null;
                        });
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido");
            }
        } while (nombreAutor == null);
        return nombreAutor;
    }
}
