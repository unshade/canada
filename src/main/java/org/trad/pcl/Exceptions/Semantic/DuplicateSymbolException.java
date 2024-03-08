package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class DuplicateSymbolException extends Exception {
    public DuplicateSymbolException(String var) {
        super("The symbol " + colorize(var, Attribute.YELLOW_TEXT()) + " is already defined");
    }
}
