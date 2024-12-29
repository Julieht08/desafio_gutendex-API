package com.julieht.desafio.principal;
import com.julieht.desafio.modelo.DatosGenerales;
import com.julieht.desafio.modelo.DatosLibros;
import com.julieht.desafio.servicio.ConsumoApi;
import com.julieht.desafio.servicio.ConvierteDatos;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoApi consumoAPI = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraMenu() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        System.out.println("********************************************************");
        var datos = conversor.obtenerDatos(json, DatosGenerales.class);
        System.out.println(datos);
        System.out.println("********************************************************");

        //Trabajando con estadísticas
        DoubleSummaryStatistics estadisticas = datos.resultados().stream()
                .filter(d -> d.numeroDescargas() > 0)
                        .collect(Collectors.summarizingDouble(DatosLibros::numeroDescargas));
        System.out.println("--La cantidad media de descargas de libros es: " + estadisticas.getAverage());
        System.out.println("--La cantidad máxima de descargas de libros es: " + estadisticas.getMax());
        System.out.println("--La cantidad mínima de descargas de libros es: " + estadisticas.getMin());
        System.out.println("--La cantidad de los resgistro evaluados es: " + estadisticas.getCount());
        System.out.println("********************************************************");

        //Top 10 de los libros más descargados
        System.out.println("--Top 10 de los libros con más descargas...");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
        System.out.println("********************************************************");

        //Realizar busqueda de libros por el nombre
        System.out.println("--Ingrese el nombre en inglés del libro que desea buscar...");
        var nombreLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, DatosGenerales.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("--El libro ha sido encontrado con exito, los siguientes son los datos del mismo...");
            System.out.println(libroBuscado.get());
        } else {
            System.out.println("--El libro no se pudo encontrar...");
        }
    }
}
