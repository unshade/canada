package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class TypeMismatchException extends Exception {
    public TypeMismatchException(String expected, String got) {
        super("The type of the expression does not match the type of the variable (expected " + colorize(expected, Attribute.MAGENTA_TEXT()) + " but got " + colorize(got, Attribute.MAGENTA_TEXT()) + ")");
    }
}
