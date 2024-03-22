package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class UndefinedFieldException extends Exception {
    public UndefinedFieldException(String field, String record) {
        super("The field " + colorize(field, Attribute.YELLOW_TEXT()) + " is not defined in the record " + colorize(record, Attribute.YELLOW_TEXT()));
    }
}
