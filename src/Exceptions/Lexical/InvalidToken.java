package Exceptions.Lexical;

import Lexer.Tokens.Token;

public class InvalidToken extends Exception {
    public InvalidToken(Token token) {
        super("Invalid token: "  + token + " at line " + token.line());
    }
}
