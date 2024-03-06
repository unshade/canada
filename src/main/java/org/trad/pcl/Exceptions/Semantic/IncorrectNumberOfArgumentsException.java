package org.trad.pcl.Exceptions.Semantic;

public class IncorrectNumberOfArgumentsException extends Exception {
    public IncorrectNumberOfArgumentsException(String name, int expected, int got) {
        super("The function " + name + " expects " + expected + " arguments but got " + got);
    }
}