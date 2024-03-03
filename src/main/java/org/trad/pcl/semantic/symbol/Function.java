package org.trad.pcl.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

public class Function extends Symbol {

    private List<String> indexedParametersTypes;
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
        return "Function{" +
                "parameters=" + indexedParametersTypes +
                '}';
    }
}
