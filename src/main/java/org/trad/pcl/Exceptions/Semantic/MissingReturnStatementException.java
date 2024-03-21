package org.trad.pcl.Exceptions.Semantic;

public class MissingReturnStatementException extends Exception {
    public MissingReturnStatementException(String identifier) {
        super("The function " + identifier + " does not have a return statement");
    }
}
