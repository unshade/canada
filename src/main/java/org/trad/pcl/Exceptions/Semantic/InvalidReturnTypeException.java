package org.trad.pcl.Exceptions.Semantic;

public class InvalidReturnTypeException extends Exception {
    public InvalidReturnTypeException(String expected, String got) {
        super("The return type of the function does not match the type of the expression (expected " + expected + " but got " + got + ")");
    }
}
