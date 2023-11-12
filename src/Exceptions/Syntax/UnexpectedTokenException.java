package Exceptions.Syntax;

import Lexer.Tokens.Token;

public class UnexpectedTokenException extends Exception {
    public UnexpectedTokenException(Token expected, Token got) {
        super("Syntax error: expected " + expected + " but got " + got + " at line " + got.line());
    }
}
