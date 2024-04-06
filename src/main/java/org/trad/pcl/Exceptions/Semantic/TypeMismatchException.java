package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class TypeMismatchException extends SemanticException {
    public TypeMismatchException(String expected, String got, int line) {
        super("Type mismatch: expected " + colorize(expected, Attribute.YELLOW_TEXT()) + " but got " + colorize(got, Attribute.MAGENTA_TEXT()), line);
    }
}
