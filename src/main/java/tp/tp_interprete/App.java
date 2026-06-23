package tp.tp_interprete;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class App 
{
    public static void main( String[] args ){
    	
    	try {
    		//tabla compartida
    		SymbolTable tablaDeSimbolos = new SymbolTable(); 
    		
    		//cargar archivo
    		System.out.println("Carga de archivo...");
    		//CharStream input = CharStreams.fromFileName("pruebaFallidaTipos.txt");
    		CharStream input = CharStreams.fromFileName("pruebaVariablesNoDeclaradas.txt");
    		//analisis lexico - tokens
    		MiniLangLexer lexer = new MiniLangLexer(input);
    		CommonTokenStream tokens = new CommonTokenStream(lexer);
    		
    		//Analisis sintactico - arbol 
    		MiniLangParser parser = new MiniLangParser (tokens);
    		ParseTree arbol = parser.program();
    	
    		//semantico
    		SemanticAnalyzer analizSemantico = new SemanticAnalyzer (tablaDeSimbolos);
    		analizSemantico.visit(arbol);
    		
    		//interprete
    		Interpreter interprete = new Interpreter(tablaDeSimbolos);
    		interprete.visit(arbol);
    		
    		System.out.println ("Ejecucion ok!");
    		
    	} catch (RuntimeException e) {
    		System.err.println("Error en el compilador");
    		System.err.println (e.getMessage());
    	} catch (IOException e) {
    		System.err.println ("No se pudo leer el archivo");
    	}
    
   
    }
}
