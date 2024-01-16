package org.trad.pcl.Exceptions.Syntax;


import org.trad.pcl.Lexer.Tokens.Token;

import java.util.Arrays;
import java.util.List;

public class UnexpectedTokenListException extends Exception {
    public UnexpectedTokenListException( Token got, Token... expected) {
        super("Syntax error: expected " + Arrays.toString(expected) + " but got " + got + " at line " + got.line());

    }
}
