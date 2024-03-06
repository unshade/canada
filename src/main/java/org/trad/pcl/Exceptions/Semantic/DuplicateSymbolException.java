package org.trad.pcl.Exceptions.Semantic;

public class DuplicateSymbolException extends Exception {
    public DuplicateSymbolException(String var) {
        super("The symbol " + var + " is already defined");
    }
}
