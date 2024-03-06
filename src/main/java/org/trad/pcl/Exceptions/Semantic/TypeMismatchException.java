package org.trad.pcl.Exceptions.Semantic;

public class TypeMismatchException extends Exception {
    public TypeMismatchException(String expected, String got) {
        super("The type of the expression does not match the type of the variable (expected " + expected + " but got " + got + ")");
    }
}
