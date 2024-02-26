package org.trad.pcl.Exceptions.Semantic;

public class TypeMismatchException extends Exception {
    public TypeMismatchException(String var) {
        super("Semantic error: type mismatch " + var);
    }
}
