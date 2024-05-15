package org.trad.pcl.Exceptions.Semantic;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.TypeEnum;

import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class BinaryTypeMismatchException extends SemanticException {
    public BinaryTypeMismatchException(String left, String right, List<TypeEnum> operator, int line) {
        super("The types " + colorize(left, Attribute.YELLOW_TEXT()) + " and " + colorize(right, Attribute.YELLOW_TEXT()) + " do not match the operator " + colorize(operator.toString(), Attribute.MAGENTA_TEXT()), line);
    }
}
