package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Lexer.Tokens.Token;

import static com.diogonunes.jcolor.Ansi.colorize;

public class UndefinedVariableException extends SemanticException {

    public UndefinedVariableException(String var, int line) {
        super("The identifier " + colorize(var, Attribute.YELLOW_TEXT()) + " has not been declared", line);
    }
}
