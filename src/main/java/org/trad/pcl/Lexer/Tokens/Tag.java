package org.trad.pcl.Lexer.Tokens;

/**
 * Token tags
 */
public enum Tag {
    // Ruled Terminals
    IDENT,
    ENTIER,
    CARACTERE,

    // Keywords
    SEMICOLON,
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
    APOSTROPHE,
    ADA_TEXT_IO,
    USEADA_TEXT_IO,
    WITH,
    NULL,
    NEW,
    CHARACTER,

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
    DOTDOT,

    // Special
    EOF,
    UNKNOWN
}
