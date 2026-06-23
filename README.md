# Trabajo práctico cuatrimestral

* Es el trabajo practico de la materia *Conceptos y paradigmas de lenguajes de programación*.
* La variante asignada fue '*switch*'

## Integrantes del *Grupo R*
* Morena Rios Carnevale
* Luciano Rodriguez

## Descripción del lenguaje
**MiniLang** es un mini lenguaje de programación de paradigma imperativo, secuencial y fuertemente tipado. 

#### Caracteristicas principales:
* **Tipado estricto**: Toda variable debe tener un tipo definido
* **Declaración Obligatoria**: No es posible utilizar ni asignar valor a una variable que no haya sido declarada previamente de forma explícita.
* **Inexistencia de declaracion y asignación al mismo tiempo**: No se puede declarar y asignar un valor a una variable en la misma linea.

#### Tipos de datos soportados:
* `int`: Representa números enteros
* `float`: Representa números decimales de punto flotante
* `string`: Cadenas de texto alfanumérico delimitadas por comillas
* `boolean`:Valores lógicos de verdad (true o false). 

#### Operadores y Sintaxis
* **Aritméticos**: Suma (+), resta (-), multiplicación (*) y división (/). Operan únicamente entre tipos homogéneos (int con int, o float con float).
* **Relacionales**: Operadores de comparación numérica (>, <, >=, <=, ==, !=).
* **Lógicos**: AND (&&), OR (||) y NOT (!). 
* **Entrada/Salida**: Instrucción nativa print que permite la impresión en consola de expresiones.

## Decisiones de diseño tomadas
* Se optó por extender de MiniLangBaseVisitor en lugar de utilizar un Listener. La arquitectura Visitor permite un recorrido dirigido y personalizado del Árbol de Sintaxis Abstracta (AST).


* El sistema se dividió en dos pasadas independientes sobre el AST:  
    * Se creó `SymbolTable.java` para  separar conceptualmente la existencia y tipado de una variable de su valor real en memoria. 
    * Usamos `SemanticAnalyzer.java` para realizar una verificación previa. Valida que las variables existan, que los tipos en las asignaciones coincidan y que los operandos sean válidos. Si encuentra un error, frena el proceso inmediatamente. 
    * En el `Interpreter.java`: Solo se ejecuta si la fase semántica finalizó con éxito. Esto garantiza una filosofía Fail-Fast (error temprano) y asegura que el intérprete nunca falle a mitad de ejecución por un error de tipos. 


## Instrucciones de compilación y ejecución.
* Una vez que se tiene el repositorio clonado, el ciclo de vida del proyecto esta automatizado mediante **Apache Maven** (se requiere JDK 17 o superior instalado).

### Para  probar cualquier programa estos son los pasos:
1. **Generar los archivos de ANTLR y compilar**:
   * Para que el entorno reconozca la gramática y genere el Parser, Lexer y los Visitors, abrir una terminal en la raíz del proyecto (donde está el archivo `pom.xml`) y ejecutar:
```bash
mvn clean compile
```
2. **Elegir en el archivo de prueba**:
   * En `App.java` indicar el programa (archivo .txt) a ejecutar y colocar el nombre dentro de la linea del `CharStream`:
```text
CharStream input = CharStreams.fromFileName("programaPrueba.txt");
```

3. **Ejecutar la aplicacion**:
   * Una vez guardado el cambio en App.java ya se puede ejecutar el programa mediante:
```bash
mvn exec:java -Dexec.mainClass="tp.tp_interprete.App"
```
   * O directamente haciendo click derecho sobre App.java -> Run As -> Java Application.

## Ejemplos de uso
   * En la raiz del repositorio hay una serie de archivos `.txt` con ejemplos de programas breves para testear el comportamiento del programa y cada una de las validaciones.
     
   * Un ejemplo:
#### Caso 1: Código Válido (`programaPruebaCompletoOk.txt`)
* Programa escrito correctamente que utiliza la variante diferencial `switch`, condicionales `if-else` anidados y la instrucción `print` con múltiples argumentos:
```text
program PruebaCompleta {
    var int nota;
    var string grupo;
    var int dia;
    
    grupo = "R";
    nota = 8;
    dia = 4;
    
    print("El tp se entrega el dia Miercoles");
    switch (dia) {
        case 1:
            print("Hoy es lunes todavia quedan dos dias..");
            break;
        case 2:
            print("Hoy es martes, ultimando detalles porque se entrega mañana!");
            break;
        case 3:
            print("Hoy es miercoles!!! Se entrega hoy!");
            print("Su nota es...", nota);
            
            if (nota >= 4) {
                print("Grupo:", grupo, "APROBADO!");
            } else {
                print("Grupo:", grupo, "DESAPROBADOS :(");
            }
            break;
        case 4:
            print("Hoy es jueves, se entrego ayer, su nota es: ", nota);
            break;
    }
}
```
* **Salida esperada en la consola**:
* Carga de archivo...
* Intérprete, ejecución inicio con exito
* El tp se entrega el dia Miercoles 
* Hoy es jueves, se entrego ayer, su nota es:  8 
* Ejecucion ok!
