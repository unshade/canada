package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.ParameterMode;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

public class Parameter extends Variable{

    private ParameterMode mode;


    public Parameter(String identifier, int shift) {
        super(identifier, shift);
    }

    public void setMode(ParameterMode mode) {
        this.mode = mode;
    }

    public ParameterMode getMode() {
        return mode;
    }

}
