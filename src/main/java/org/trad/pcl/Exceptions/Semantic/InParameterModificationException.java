package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class InParameterModificationException extends Exception {
    public InParameterModificationException(String var) {
        super("The mode of the parameter " + colorize(var, Attribute.YELLOW_TEXT()) + " is "+ colorize("IN", Attribute.MAGENTA_TEXT()) + " and cannot be modified");
    }
}