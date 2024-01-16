package org.trad.pcl.Exceptions.Syntax;


import org.trad.pcl.Lexer.Tokens.Token;

import java.util.List;

public class UnexpectedTokenListException extends Exception {
    public UnexpectedTokenListException(List<Token> expected, Token got) {
        super("Syntax error: expected " + expected + " but got " + got + " at line " + got.line());
    }
}
