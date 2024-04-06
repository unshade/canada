package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class DuplicateSymbolException extends SemanticException {
    public DuplicateSymbolException(String var, int line) {
        super("The symbol " + colorize(var, Attribute.YELLOW_TEXT()) + " is already defined", line);
    }
}
