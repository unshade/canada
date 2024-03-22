package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class InvalidVariableReferenceException extends Exception {
    public InvalidVariableReferenceException(String identifier, String got) {
        super("The identifier " + colorize(identifier, Attribute.YELLOW_TEXT()) + " is not a valid reference to a Variable. Got " + colorize(got, Attribute.MAGENTA_TEXT()));
    }
}
