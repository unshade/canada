package Exceptions.Lexical;

import Lexer.Tokens.Token;

public class UnknownTokenException extends Exception {
    public UnknownTokenException(Token token) {
        super("Unknown token: " + token + " at line " + token.line());
    }
}
