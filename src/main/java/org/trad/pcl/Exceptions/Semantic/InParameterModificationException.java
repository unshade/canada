package org.trad.pcl.Exceptions.Semantic;

public class InParameterModificationException extends Exception {
    public InParameterModificationException(String var) {
        super("The mode of the parameter " + var + " is in and cannot be modified");
    }
}