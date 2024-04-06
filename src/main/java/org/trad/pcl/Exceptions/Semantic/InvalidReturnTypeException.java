package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class InvalidReturnTypeException extends SemanticException {
    public InvalidReturnTypeException(String expected, String got, int line) {
        super("The return type of the "+colorize("FUNCTION", Attribute.MAGENTA_TEXT())+" does not match the type of the expression (expected " + colorize(expected, Attribute.MAGENTA_TEXT()) + " but got " + colorize(got, Attribute.MAGENTA_TEXT()) + ")" , line);
    }
}
