package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class IncorrectNumberOfArgumentsException extends Exception {
    public IncorrectNumberOfArgumentsException(String name, int expected, int got) {
        super("The " + colorize("FUNCTION ", Attribute.MAGENTA_TEXT()) + colorize(name, Attribute.YELLOW_TEXT()) + " expects " + colorize(String.valueOf(expected), Attribute.MAGENTA_TEXT()) + " arguments but got " + colorize(String.valueOf(got), Attribute.MAGENTA_TEXT()));
    }
}