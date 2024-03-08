package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class ArgumentTypeMismatchException extends Exception {
    public ArgumentTypeMismatchException(String expected, String got) {
        super("The type of the argument does not match the type of the parameter (expected " + colorize(expected, Attribute.MAGENTA_TEXT()) + " but got " + colorize(got, Attribute.MAGENTA_TEXT()) + ")");
    }
}