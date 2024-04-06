package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class InvalidEndIdentifierException extends SemanticException {
    public InvalidEndIdentifierException(String identifier, String endIdentifier, int line) {
        super("The function or procedure " + colorize(identifier, Attribute.YELLOW_TEXT()) + " does not match the end identifier " + colorize(endIdentifier, Attribute.YELLOW_TEXT()), line);
    }
}
