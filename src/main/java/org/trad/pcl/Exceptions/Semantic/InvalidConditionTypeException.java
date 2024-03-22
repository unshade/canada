package org.trad.pcl.Exceptions.Semantic;

public class InvalidConditionTypeException extends Exception {
    public InvalidConditionTypeException(String type) {
        super("The condition type " + type + " is invalid");
    }
}
