package org.trad.pcl.Exceptions.Syntax;


import org.trad.pcl.Lexer.Tokens.Token;

public class UnexpectedTokenException extends Exception {
    public UnexpectedTokenException(Token expected, Token got) {
        super("Syntax error: expected " + expected + " but got " + got + " at line " + got.line());
    }
}
