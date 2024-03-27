package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.TypeEnum;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Type extends Symbol {

    public Type(String identifier, int shift) {
        super(identifier, shift);
    }

    @Override
    public String toString() {
        return "Type { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", shift = " + colorize(Integer.toString(shift), Attribute.RED_TEXT()) +
                " }";
    }

}
