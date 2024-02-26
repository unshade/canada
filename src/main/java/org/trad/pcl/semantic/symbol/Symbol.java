package org.trad.pcl.semantic.symbol;

public class Symbol {
    private String identifier;
    private int shift;

    public Symbol(String identifier, int shift) {
        this.identifier = identifier;
        this.shift = shift;
    }

    public static Symbol builtinFunction(String identifier) {
        return new Symbol(identifier, 0);
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


}
