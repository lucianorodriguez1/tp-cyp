grammar MiniLang;

//parser
program: 
	PROGRAM ID OPEN_BRACE 
		sentence*
	CLOSE_BRACE;

sentence: 
	declaration | assignment | condition | switch | print;

declaration: 
	VAR TYPE ID SEMICOLON;
	
assignment: 
	ID ASSIGN expression SEMICOLON;

condition:
	IF OPEN_PARENT expression CLOSE_PARENT OPEN_BRACE
		sentence+ 
	CLOSE_BRACE
	(ELSE OPEN_BRACE sentence+ CLOSE_BRACE)?; // ?puede haber else o no

switch:
	SWITCH OPEN_PARENT expression CLOSE_PARENT OPEN_BRACE 
		cases_sw+ default_sw? 
	CLOSE_BRACE;

cases_sw:
	CASE expression COLON 
		sentence+ 
		(BREAK SEMICOLON)?;

default_sw: 
	DEFAULT SEMICOLON 
		sentence+;

expression: 
	OPEN_PARENT expression CLOSE_PARENT #Parenthesis
	| expression (MULT | DIV ) expression #MultiplicationDivision
	| expression (PLUS | MINUS) expression #PlusMinus
	| expression (GT | LT | GTE | LTE | EQUAL | NOTEQUAL) expression #Comparison
	| expression (AND | OR) expression #Logic
	| NOT expression #Negation
	| ID #Variable
	| INT #Integer
	| FLOAT #Decimal
	| STRING #String
	| BOOLEAN #Boolean;
	
print: PRINT OPEN_PARENT expression CLOSE_PARENT SEMICOLON;

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
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';

//logicos
AND: '&&';
OR: '||';
NOT: '!';

//comparacion
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';
EQUAL: '==';
NOTEQUAL: '!=';

//tipos de dato
TYPE: 'int'|'float'|'string'|'boolean';
BOOLEAN: 'true'|'false';
INT: [0-9]+;
FLOAT: [0-9]+'.'[0-9]+;
STRING: '"' (~[\r\n"])* '"';

//asignar
ASSIGN: '=';

//puntuacion
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
OPEN_PARENT: '(';
CLOSE_PARENT: ')';
SEMICOLON: ';';
COLON: ':';

//identificadores
ID: [a-zA-Z][a-zA-Z0-9_]*; //nombres de variables y programa

//comentarios
COMMENT: '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT: '**' .*? '**' -> skip;

//espacios en blanco/tabulaciones
WS: [ \t\n\r]+ -> skip;
