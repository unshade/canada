package org.trad.pcl.Exceptions.Semantic;

public class ArgumentTypeMismatchException extends Exception {
    public ArgumentTypeMismatchException(String expected, String got) {
        super("The type of the argument does not match the type of the parameter (expected " + expected + " but got " + got + ")");
    }
}