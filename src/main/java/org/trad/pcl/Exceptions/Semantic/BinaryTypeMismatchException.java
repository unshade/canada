package org.trad.pcl.Exceptions.Semantic;

public class BinaryTypeMismatchException extends Exception {
    public BinaryTypeMismatchException(String left, String right, String operator) {
        super("The types " + left + " and " + right + " do not match the operator " + operator);
    }
}
