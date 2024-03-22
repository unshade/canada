package org.trad.pcl.Exceptions.Semantic;

public class InvalidEndIdentifierException extends Exception {
    public InvalidEndIdentifierException(String identifier, String endIdentifier) {
        super("The identifier " + identifier + " does not match the end identifier " + endIdentifier);
    }
}
