package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Lexer.Tokens.Token;

import static com.diogonunes.jcolor.Ansi.colorize;

public class UndefinedVariableException extends Exception {

    public UndefinedVariableException(String var) {
        super("The identifier " + colorize(var, Attribute.YELLOW_TEXT()) + " has not been declared");
    }
}
