package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class SemanticException extends Exception {

    public SemanticException(String message, int line) {
        super("Line " + colorize(Integer.toString(line), Attribute.GREEN_TEXT()) + ": " + message);
    }
}
