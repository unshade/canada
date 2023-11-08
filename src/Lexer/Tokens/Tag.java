package Lexer.Tokens;

public enum Tag {
    // Ruled Terminals
    IDENT,
    ENTIER,
    CARACTERE,

    // Keywords
    SEMICOLON,
    DOUBLEPOINT,
    PROCEDURE,
    IS,
    BEGIN,
    END,
    TYPE,
    ACCESS,
    RECORD,
    COLON,
    FUNCTION,
    RETURN,
    OUT,
    IN,
    IF,
    THEN,
    ELSIF,
    ELSE,
    LOOP,
    FOR,
    REVERSE,
    WHILE,
    REM,
    AND,
    OR,
    DOT,
    VAL,
    OPEN_PAREN,
    CLOSE_PAREN,
    TRUE,
    FALSE,
    COMMA,

    // Operators
    PLUS,
    MINUS,
    MULTI,
    DIV,
    NOT,
    EQ,
    NE,
    LT,
    LE,
    GT,
    GE,
    ASSIGN,

    // Special
    EOF,
    UNKNOWN
}
