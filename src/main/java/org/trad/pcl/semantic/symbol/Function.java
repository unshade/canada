package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import java.util.ArrayList;
import java.util.List;

public class Function extends Symbol {

    private String returnType;

    private final List<String> indexedParametersTypes;
    public Function(String identifier, int shift) {
        super(identifier, shift);
        this.indexedParametersTypes = new ArrayList<>();
    }

    public void addParameter(String parameterType) {
        indexedParametersTypes.add(parameterType);
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getIndexedParametersTypes() {
        return indexedParametersTypes;
    }


    @Override
    public String toString() {
        return "Function { " +
                super.toString() +
                ", indexedParametersTypes = '" + indexedParametersTypes.toString() + '\'' +
                " }";
    }
}
