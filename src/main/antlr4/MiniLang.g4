grammar MiniLang;

program: 
	PROGRAMA ID LLAVE_ABRE 
		sentencia*
	LLAVE_CIERRA;

sentencia: 
	declaracion | asignacion | condicional | switch | impresion;

declaracion: 
	VAR TIPO ID PUNTO_COMA;
	
asignacion: 
	ID ASIG expresion PUNTO_COMA;

condicional:
	IF PARENTESIS_ABRE expresion PARENTESIS_CIERRA LLAVE_ABRE 
		sentencia+ 
	LLAVE_CIERRA
	(ELSE LLAVE_ABRE sentencia+ LLAVE_CIERRA)?; // ?puede haber else o no

switch:
	SWITCH PARENTESIS_ABRE expresion PARENTESIS_CIERRA LLAVE_ABRE 
		casos_sw+ default_sw? 
	LLAVE_CIERRA;

casos_sw:
	CASE expresion DOS_PUNTOS 
		sentencia+ 
		(BREAK PUNTO_COMA)?;

default_sw: 
	DEFAULT DOS_PUNTOS 
		sentencia+;

expresion: 
	PARENTESIS_ABRE expresion PARENTESIS_CIERRA #Parentesis
	| expresion (MULT | DIV | MOD) expresion #MultiplicacionDivisionMod
	| expresion (SUMA | RESTA) expresion #SumaResta
	| expresion (MAYOR | MENOR | MAYOR_IGUAL | MENOR_IGUAL | IGUAL_QUE | DISTINTO) expresion #Comparacion
	| expresion (Y | O) expresion #Logica
	| NO expresion #Negacion
	| ID #Variable
	| INT #Entero
	| FLOAT #Decimal
	| STRING #Cadena
	| BOOLEAN #Booleano;
	
impresion: PRINT PARENTESIS_ABRE expresion PARENTESIS_CIERRA PUNTO_COMA;

//lexer	
// palabras reservadas
PROGRAM: 'program';
VAR: 'var'; 
PRINT: 'print';
IF: 'if';
ELSE: 'else';
SWITCH: 'switch';
CASE: 'case';
DEFAULT: 'default';
BREAK: 'break';

//operadores aritmeticos
SUMA: '+';
RESTA: '-';
MULT: '*';
DIV: '/';
MOD: '%';

//logicos
Y: '&&';
O: '||';
NO: '!';

//comparacion
MAYOR: '>';
MENOR: '<';
MAYOR_IGUAL: '>=';
MENOR_IGUAL: '<=';
IGUAL_QUE: '==';
DISTINTO: '!=';

//tipos de dato
TIPO: 'int'|'float'|'string'|'boolean';
BOOLEAN: 'true'|'false';
INT: [0-9]+;
FLOAT: [0-9]+'.'[0-9]+;
STRING: '"' (~[\r\n"])* '"';

//asignar
ASIGNAR: '=';

//puntuacion
LLAVE_ABRE: '{';
LLAVE_CIERRA: '}';
CORCHETE_ABRE: '[';
CORCHETE_CIERRA: ']';
PARENTESIS_ABRE: '(';
PARENTESIS_CIERRA: ')';
PUNTO_COMA: ';';
DOS_PUNTOS: ':';

//identificadores
ID: [a-zA-Z][a-zA-Z0-9_]*; //nombres de variables y programa

//comentarios
COMENTARIO: '//' ~[\r\n]* -> skip;
COMENTARIO_MULTILINEA: '**' .*? '**' -> skip;

//espacios en blanco/tabulaciones
WS: [ \t\n\r]+ -> skip;
