package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.ParameterMode;

public class Parameter extends Symbol{

    private ParameterMode mode;

    private String type;

    public Parameter(String identifier, int shift) {
        super(identifier, shift);
    }

    public void setMode(ParameterMode mode) {
        this.mode = mode;
    }

    public ParameterMode getMode() {
        return mode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
