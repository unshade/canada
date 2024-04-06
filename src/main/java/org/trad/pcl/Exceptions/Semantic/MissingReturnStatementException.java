package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class MissingReturnStatementException extends SemanticException {
    public MissingReturnStatementException(String identifier, int line) {
        super("The function " + colorize(identifier, Attribute.YELLOW_TEXT()) + " does not have a return statement", line);
    }
}
