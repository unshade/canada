package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class DuplicateRecordFieldException extends SemanticException {
    public DuplicateRecordFieldException(String field, String record, int line) {
        super("The field " + colorize(field, Attribute.YELLOW_TEXT()) + " is already defined in the record " + colorize(record, Attribute.YELLOW_TEXT()), line);
    }
}
