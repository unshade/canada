package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.TypeEnum;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Type extends Symbol {

    private int size;

    public Type(String identifier, int shift) {
        super(identifier, shift);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Type { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", size = " + colorize(Integer.toString(size), Attribute.RED_TEXT()) +
                ", shift = " + colorize(Integer.toString(shift), Attribute.RED_TEXT()) +
                " }";
    }

}
