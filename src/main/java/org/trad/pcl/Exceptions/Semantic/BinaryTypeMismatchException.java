package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class BinaryTypeMismatchException extends SemanticException {
    public BinaryTypeMismatchException(String left, String right, String operator, int line) {
        super("The types " + colorize(left, Attribute.YELLOW_TEXT()) + " and " + colorize(right, Attribute.YELLOW_TEXT()) + " do not match the operator " + colorize(operator, Attribute.MAGENTA_TEXT()), line);
    }
}
