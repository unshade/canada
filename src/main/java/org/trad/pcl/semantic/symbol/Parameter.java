package org.trad.pcl.semantic.symbol;

public class Parameter extends Symbol{

    private String mode;

    private String type;

    public Parameter(String identifier, int shift) {
        super(identifier, shift);
    }

    public void setMode(String mode) {

        if (mode == null || mode.isEmpty()) {
            this.mode = "in";
        } else {
            this.mode = mode;
        }
    }

    public String getMode() {
        return mode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
