package org.trad.pcl.Exceptions.Semantic;

import org.trad.pcl.Lexer.Tokens.Token;

public class UndefinedVariableException extends Exception {

    public UndefinedVariableException(String var) {
        super("Semantic error: undefined variable " + var);
    }
}
