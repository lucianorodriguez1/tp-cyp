package tp.tp_interprete;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import java.util.Scanner;

public class App {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean continuar = true;

		while (continuar) {
			System.out.println("\n========================================");
			System.out.println("   MENÚ DE PRUEBAS - INTÉRPRETE");
			System.out.println("========================================");
			System.out.println("1. Programa de prueba completo (OK)");
			System.out.println("2. Programa de prueba IF (OK)");
			System.out.println("3. Error: División por cero");
			System.out.println("4. Error: Tipos incorrectos");
			System.out.println("5. Prueba: Estructura Switch (OK)");
			System.out.println("6. Error: Variables no declaradas");
			System.out.println("0. Salir");
			System.out.print("Seleccione una opción: ");

			String opcion = scanner.nextLine();
			String archivoElegido = "";

			switch (opcion) {
			case "1":
				archivoElegido = "programaPruebaCompletoOk.txt";
				break;
			case "2":
				archivoElegido = "programaPruebaIfOk.txt";
				break;
			case "3":
				archivoElegido = "pruebaFallidaDivisionPorCero.txt";
				break;
			case "4":
				archivoElegido = "pruebaFallidaTipos.txt";
				break;
			case "5":
				archivoElegido = "pruebaSwitchOk.txt";
				break;
			case "6":
				archivoElegido = "pruebaVariablesNoDeclaradas.txt";
				break;
			case "0":
				continuar = false;
				System.out.println("Saliendo del programa...");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
				esperarTecla(scanner);
				break;
			}

			// si se eligió un archivo válido se ejecuta el intérprete
			if (!archivoElegido.isEmpty()) {
				ejecutarInterprete(archivoElegido, scanner);
			}
		}

		scanner.close();
	}

	private static void ejecutarInterprete(String nombreArchivo, Scanner scanner) {
		System.out.println("\n--- Ejecutando: " + nombreArchivo + " ---");
		try {
			// Tabla compartida
			SymbolTable tablaDeSimbolos = new SymbolTable();

			// Cargar archivo
			CharStream input = CharStreams.fromFileName(nombreArchivo);

			// Análisis léxico - tokens
			MiniLangLexer lexer = new MiniLangLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			// Análisis sintáctico
			MiniLangParser parser = new MiniLangParser(tokens);
			ParseTree arbol = parser.program();

			// Análisis semántico
			SemanticAnalyzer analizSemantico = new SemanticAnalyzer(tablaDeSimbolos);
			analizSemantico.visit(arbol);

			// Intérprete
			Interpreter interprete = new Interpreter(tablaDeSimbolos);
			interprete.visit(arbol);

			System.out.println(">> Ejecución ok!");

		} catch (RuntimeException e) {
			System.err.println(">> Error en el compilador: " + e.getMessage());
		} catch (IOException e) {
			System.err.println(">> No se pudo leer el archivo: " + nombreArchivo);
		}
		// esperar a que todo el output se termine de escribir en la consola antes de pausar
		System.out.flush();
		System.err.flush();

		// método encargado de pausar la pantalla
		esperarTecla(scanner);
	}

	// Método auxiliar para evitar duplicar código de pausa
	private static void esperarTecla(Scanner scanner) {
		System.out.println("\n----------------------------------------");
		System.out.print("Presione ENTER para volver al menú...");
		scanner.nextLine();
	}

}