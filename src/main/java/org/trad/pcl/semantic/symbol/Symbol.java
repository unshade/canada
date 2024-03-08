package org.trad.pcl.semantic.symbol;
import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Symbol {
    private final String identifier;
    private int shift;

    public Symbol(String identifier, int shift) {
        this.identifier = identifier;
        this.shift = shift;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public static Symbol builtinFunction(String identifier) {
        Function f = new Function(identifier, 0);
        f.addParameter("integer");
        return f;
    }

    public static Symbol builtinVariable(String identifier) {
        return new Symbol(identifier, 0);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String[] toStringArray() {
        return new String[] {identifier, Integer.toString(shift)};
    }

    @Override
    public String toString() {
        return "Symbol { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", shift = " + shift +
                " }";
    }
}
