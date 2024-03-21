package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import java.util.ArrayList;
import java.util.List;

public class Function extends Symbol {

    private final List<String> indexedParametersTypes;
    private String returnType;
    public Function(String identifier, int shift) {
        super(identifier, shift);
        this.indexedParametersTypes = new ArrayList<>();
        this.returnType = null;
    }

    public void addParameter(String parameterType) {
        indexedParametersTypes.add(parameterType);
    }

    public List<String> getIndexedParametersTypes() {
        return indexedParametersTypes;
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
                ", indexedParametersTypes = '" + indexedParametersTypes.toString() + '\'' +
                ", returnType = '" + returnType + '\'' +
                " }";
    }
}
