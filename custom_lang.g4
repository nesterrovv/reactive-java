grammar custom_lang;

options {
    output=AST;
    backtrack=true;
    memoize=true;
    k=3; 
}

// Лексические правила (tokens)
BOOL : 'true' | 'false' ;
IDENTIFIER : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;
STRING : '"' ( ~('\\'|'"') | '\\' . )* '"' ;
CHAR : '\'' (~'\'') '\'' ;
HEX : '0' ('x'|'X') ('0'..'9'|'A'..'F'|'a'..'f')+ ;
BITS : '0' ('b'|'B') ('0'|'1')+ ;
DEC : ('0'..'9')+ ;

WS : (' '|'\t')+ { skip(); } ;

// Операторы
LTS: '<' ;
LTSE: '<=' ;
ADD: '+' ;
SUB: '-' ;
MUL: '*' ;
DIV: '/' ;
MOD: '%' ;
AND: '&&' ;
OR: '||' ;
EQ: '==' ;
NEQ: '!=' ;
GT: '>' ;
GTE: '>=' ;
ASSIGN: '=' ;

// Синтаксические правила
source: sourceItem* ;

basicType
    : 'bool'
    | 'byte'
    | 'int'
    | 'uint'
    | 'long'
    | 'ulong'
    | 'char'
    | 'string'
    | IDENTIFIER
    ;

typeRef
    : basicType ('array' '[' DEC ']')?
    ;

funcSignature
    : IDENTIFIER '(' argList ')' ('of' typeRef)?
    ;

argList
    : (arg (',' arg)*)?
    ;

arg
    : IDENTIFIER ('of' typeRef)?
    ;

sourceItem
    : funcDef
    ;

funcDef
    : 'def' funcSignature blockStatement 'end'
    ;

statement
    : ifStatement
    | loopStatement
    | repeatStatement
    | breakStatement
    | expressionStatement
    | blockStatement
    | returnStatement
    ;

returnStatement
    : 'return' (expr | IDENTIFIER) ';'
    ;

ifStatement
    : 'if' expr 'then' statement ('else' statement)?
    ;

loopStatement
    : ('while'|'until') expr statement* 'end'
    ;

repeatStatement
    : 'repeat' statement ('while'|'until') expr ';'
    ;

breakStatement
    : 'break' ';'
    ;

expressionStatement
    : expr ';'
    ;

blockStatement
    : ('begin' | '{') (statement | sourceItem)* ('end' | '}')
    ;

expr
    : (IDENTIFIER | arrayAccess) ASSIGN additiveExpr
    | additiveExpr
    ;

additiveExpr
    : primaryExpr ( binOp primaryExpr )*
    ;

primaryExpr
    : literal
    | IDENTIFIER
    | functionCall
    | arrayAccess
    | '(' expr ')'
    | unOp primaryExpr
    ;

functionCall
    : IDENTIFIER '(' exprList? ')'
    ;

arrayAccess
    : IDENTIFIER '[' rangeList ']'
    ;

rangeList
    : range (',' range)*
    ;

range
    : expr ('..' expr)?
    ;

exprList
    : expr (',' expr)*
    ;

literal
    : BOOL
    | STRING
    | CHAR
    | HEX
    | BITS
    | DEC
    ;

binOp
    : ADD
    | SUB
    | MUL
    | DIV
    | MOD
    | AND
    | OR
    | EQ
    | NEQ
    | GT
    | LTS
    | GTE
    | LTSE
    ;

unOp
    : ADD
    | SUB
    | '!' ;
