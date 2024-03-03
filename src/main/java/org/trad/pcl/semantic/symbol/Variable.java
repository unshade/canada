package org.trad.pcl.semantic.symbol;

public class Variable extends Symbol {

    private String type;

    public Variable(String identifier, int shift) {
        super(identifier, shift);
        this.type = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
