package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class NonRecordTypeException extends Exception {
    public NonRecordTypeException(String typeIdent) {
        super("Accessing a field of a non-record type " + colorize(typeIdent, Attribute.YELLOW_TEXT()) + " is not allowed");
    }
}
