package org.trad.pcl.Exceptions.Lexical;


import org.trad.pcl.Lexer.Tokens.Token;

public final class UnknownTokenException extends Exception {
    public UnknownTokenException(Token token) {
        super("Unknown token: " + token.printWithoutColor() + " at line " + token.line());
    }
}
