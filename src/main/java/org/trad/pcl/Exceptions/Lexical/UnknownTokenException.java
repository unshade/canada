package org.trad.pcl.Exceptions.Lexical;


import org.trad.pcl.Lexer.Tokens.Token;

public final class UnknownTokenException extends Exception {

    private Token token;
    public UnknownTokenException(Token token) {
        super("Unknown token: " + token + " at line " + token.line());
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnknownTokenException) {
            UnknownTokenException exception = (UnknownTokenException) obj;
            return this.token.equals(exception.token);
        }
        return false;
    }

}
