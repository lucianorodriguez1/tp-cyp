grammar MiniLang;

start
:
	'Hello world'
;

WS
:
	[\t\r\n]+ -> skip
;
	