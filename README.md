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

## Ejemplos de uso
