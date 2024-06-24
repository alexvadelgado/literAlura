package com.alura.literalura;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private AutorRepository autorRepository;


    public LiteraluraApplication(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		System.out.println("hola mundo desde springboot");

		System.out.println("¡Bienvenido a Literalura!");
		System.out.println("-------------------------------------");

		// Crear objetos para obtener datos y convertirlos
		ConsumoAPI consumoApi = new ConsumoAPI();
		ConvierteDatos conversor = new ConvierteDatos();

		// Obtener datos JSON de la API
		String json = consumoApi.obtenerDatos("https://gutendex.com/books/");
		Datos datos = conversor.obtenerDatos(json, Datos.class);

		Scanner scanner = new Scanner(System.in);

		boolean continuar = true;

		while (continuar) {
			System.out.println("-------------------------------------");
			System.out.println("Elija una opción:");
			System.out.println("-------------------------------------");
			System.out.println("1- Mostrar libros disponibles en la API.");
			System.out.println("2- Buscar libro en API y guardar libro en Base de Datos.");
			System.out.println("3- Mostrar Libros Guardados en la Base de Datos");
			System.out.println("4- Autores en la Base de Datos");
			System.out.println("5- Mostrar autores vivos en un año específico.");
			System.out.println("6- Mostrar libros por idioma.");
			System.out.println("0- Salir.");
			System.out.print("Opción: ");

			int opcion = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			switch (opcion) {
				case 1:
					mostrarLibros(datos);
					break;
				case 2:
					buscarLibro( datos);
					break;
				case 3:
					mostrarLibrosGuardados();
					break;
				case 4:
					listarAutores();
					break;
				case 5:
					mostrarAutoresVivosEnAno(datos);
					break;
				case 6:
					System.out.println("-------------------------------------");
					System.out.println("Escoge el idioma:");
					System.out.println("1- Español (es)");
					System.out.println("2- Inglés (en)");
					System.out.println("3- Francés (fr)");
					System.out.println("4- Portugués (pt)");
					System.out.println("-------------------------------------");
					System.out.print("Opción: ");
					int idiomaOpcion = scanner.nextInt();
					scanner.nextLine(); // Limpiar el buffer
					String idioma = "";
					switch (idiomaOpcion) {
						case 1:
							idioma = "es";
							break;
						case 2:
							idioma = "en";
							break;
						case 3:
							idioma = "fr";
							break;
						case 4:
							idioma = "pt";
							break;
						default:
							System.out.println("Opción no válida.");
							break;
					}
					listarLibrosPorIdioma(idioma);
					break;
				case 0:
					System.out.println("-------------------------------------");
					System.out.println("¡Gracias por usar Literalura!");
					System.out.println("-------------------------------------");
					continuar = false;
					break;

				default:
					System.out.println("Opción no válida. Por favor, elija una opción válida.");
			}
		}
	}

	private void mostrarLibros(Datos datos) {
		System.out.println("-------------------------------------");
		System.out.println("------ LISTA DE LIBROS DISPONIBLES ------");
		System.out.println("-------------------------------------");

		// Iterar sobre la lista de ResultadoLibro y imprimir los títulos
		for (Libro libro: datos.getLibros()) {
			System.out.println(libro.getTitle());
		}
	}


	public void buscarLibro(Datos datos) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("-------------------------------------");
		System.out.println("Ingrese el nombre de un libro (o parte del nombre): ");
		String nombreLibro = scanner.nextLine().toLowerCase(); // Convertir a minúsculas

		List<Libro> librosEncontrados = new ArrayList<>();

		// Buscar el libro por título
		for (Libro libro : datos.getLibros()) {
			if (libro.getTitle().toLowerCase().contains(nombreLibro)) {
				librosEncontrados.add(libro);
			}
		}

		if (librosEncontrados.isEmpty()) {
			System.out.println("-------------------------------------");
			System.out.println("No se encontraron libros con ese nombre.");
			System.out.println("-------------------------------------");
		} else {
			System.out.println("-------------------------------------");
			System.out.println("Libros encontrados:");
			System.out.println("-------------------------------------");
			for (int i = 0; i < librosEncontrados.size(); i++) {
				Libro libro = librosEncontrados.get(i);
				System.out.println((i + 1) + ". Título: " + libro.getTitle());
				System.out.println("   Autores:");
				for (Autor autor : libro.getAuthors()) {
					System.out.println("   - " + autor.getName());
				}
				System.out.println("   Idiomas: " + libro.getLanguages());
				System.out.println("   Número de descargas: " + libro.getDownloadCount());
				System.out.println();
			}

			System.out.println("-------------------------------------");
			System.out.println("Ingrese el número del libro que desea guardar: ");
			int opcion = scanner.nextInt();
			scanner.nextLine(); // Limpiar el buffer

			if (opcion < 1 || opcion > librosEncontrados.size()) {
				System.out.println("-------------------------------------");
				System.out.println("Opción no válida.");
				System.out.println("-------------------------------------");
			} else {
				Libro libroSeleccionado = librosEncontrados.get(opcion - 1);

				// Verificar si el libro ya está en la base de datos
				boolean libroYaExiste = false;
				for (Libro libroExistente : libroRepository.findAll()) {
					if (libroExistente.getTitle().equalsIgnoreCase(libroSeleccionado.getTitle())) {
						libroYaExiste = true;
						break;
					}
				}

				if (libroYaExiste) {
					System.out.println("-------------------------------------");
					System.out.println("El libro ya existe en la base de datos y no se guardará nuevamente.");
					System.out.println("-------------------------------------");
				} else {
					Libro libroGuardado = Libro.builder()
							.title(libroSeleccionado.getTitle())
							.downloadCount(libroSeleccionado.getDownloadCount())
							.languages(libroSeleccionado.getLanguages())
							.authors(libroSeleccionado.getAuthors())
							.build();

					// Guardar los datos del libro en la base de datos
					libroRepository.save(libroGuardado);

					// Imprimir mensaje de confirmación
					System.out.println("-------------------------------------");
					System.out.println("Libro guardado correctamente:");
					System.out.println("-------------------------------------");
					System.out.println("Título: " + libroSeleccionado.getTitle());
					System.out.println("Autores:");
					for (Autor autor : libroSeleccionado.getAuthors()) {
						System.out.println("- " + autor.getName());
					}
					System.out.println("Idiomas: " + libroSeleccionado.getLanguages());
					System.out.println("Número de descargas: " + libroSeleccionado.getDownloadCount());
					System.out.println("-------------------------------------");
				}
			}
		}
	}

	private void mostrarLibrosGuardados() {
		System.out.println("-------------------------------------");
		System.out.println("------ LIBROS GUARDADOS EN LA BASE DE DATOS ------");
		System.out.println("-------------------------------------");

		List<Libro> librosGuardados = libroRepository.findAll();

		if (librosGuardados.isEmpty()) {
			System.out.println("No hay libros guardados en la base de datos.");
		} else {
			for (Libro libro : librosGuardados) {
				System.out.println("Título: " + libro.getTitle());
				System.out.println("Autores:");
				for (Autor autor : libro.getAuthors()) {
					System.out.println("- " + autor.getName());
				}
    			System.out.println("Idiomas: " + libro.getLanguages());
				System.out.println("Número de descargas: " + libro.getDownloadCount());
				System.out.println("-------------------------------------");
			}
		}
	}


	private void listarAutores() {
		System.out.println("-------------------------------------");
		System.out.println("------ LISTA DE AUTORES DISPONIBLES ------");
		System.out.println("-------------------------------------");

		// Obtener todos los autores de la base de datos
		Iterable<Autor> autoresDB = autorRepository.findAll();

		// Iterar sobre los autores de la base de datos y mostrar la información completa de cada uno
		for (Autor autor : autoresDB) {
			System.out.println("Autor: " + autor.getName());
			System.out.println("Fecha de Nacimiento: " + autor.getBirthYear());
			System.out.println("Fecha de Fallecimiento: " + autor.getDeathYear());

			// Obtener los libros asociados con este autor
			List<Libro> libros = libroRepository.findByAuthorsContaining(autor);

			// Mostrar los libros asociados
			System.out.print("Libros: [ ");
			for (int i = 0; i < libros.size(); i++) {
				Libro libro = libros.get(i);
				System.out.print(libro.getTitle());
				if (i < libros.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println(" ]\n");
		}
	}

	private void mostrarAutoresVivosEnAno(Datos datos) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Ingrese el año para mostrar autores vivos: ");
		int ano = scanner.nextInt();
		scanner.nextLine(); // Limpiar el buffer

		System.out.println("-------------------------------------");
		System.out.println("Autores vivos en el año " + ano + ":");
		System.out.println("-------------------------------------");

		List<Autor> autoresVivos = autorRepository.findByVivosEnAnio(ano);
		for (Autor autor : autoresVivos) {
			System.out.println("Autor: " + autor.getName());
			System.out.println("Fecha de nacimiento: " + autor.getBirthYear());
			System.out.println("Fecha de fallecimiento: " + autor.getDeathYear());
			System.out.println("Libros:");
			// Aquí puedes ajustar el código para obtener los libros del autor de acuerdo a tu modelo de datos
			List<Libro> librosAutor = libroRepository.findByAuthorsContaining(autor);
			for (Libro libro : librosAutor) {
				System.out.println("- " + libro.getTitle());
			}
			System.out.println();
		}
	}


	private void listarLibrosPorIdioma(String idioma) {
		System.out.println("-------------------------------------");
		System.out.println("------ LISTA DE LIBROS POR IDIOMA ------");
		System.out.println("-------------------------------------");

		// Utilizar el repositorio de libros para obtener los libros por idioma
		List<Libro> librosPorIdioma = libroRepository.findByLanguagesContains(idioma);

		// Imprimir los libros encontrados en el idioma especificado
		if (librosPorIdioma.isEmpty()) {
			System.out.println("No se encontraron libros en el idioma " + idioma);
		} else {
			System.out.println("Libros en el idioma " + idioma + ":");
			for (Libro libro : librosPorIdioma) {
				System.out.println("Título: " + libro.getTitle());
				System.out.println("Autores:");
				for (Autor autor : libro.getAuthors()) {
					System.out.println("- " + autor.getName());
				}
				System.out.println("Idiomas: " + libro.getLanguages());
				System.out.println("Número de descargas: " + libro.getDownloadCount());
				System.out.println();
			}
		}
	}

}
