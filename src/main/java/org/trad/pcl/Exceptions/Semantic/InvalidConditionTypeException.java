package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class InvalidConditionTypeException extends SemanticException {
    public InvalidConditionTypeException(String type, int line) {
        super("The condition type " + colorize(type, Attribute.YELLOW_TEXT()) + " is invalid", line);
    }
}
