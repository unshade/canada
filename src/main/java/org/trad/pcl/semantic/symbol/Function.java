package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import java.util.ArrayList;
import java.util.List;

public class Function extends Procedure {

    private String returnType;


    public Function(String identifier, int shift) {
        super(identifier, shift);
    }


    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }


    @Override
    public String toString() {
        return "Function { " +
                super.toString() +
                ", indexedParametersTypes = '" + getIndexedParametersTypes().toString() + '\'' +
                " }";
    }
}
