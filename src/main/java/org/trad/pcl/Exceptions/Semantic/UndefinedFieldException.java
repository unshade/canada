package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class UndefinedFieldException extends SemanticException {
    public UndefinedFieldException(String field, String record, int line) {
        super("The field " + colorize(field, Attribute.YELLOW_TEXT()) + " is not defined in the record " + colorize(record, Attribute.YELLOW_TEXT()), line);
    }
}
