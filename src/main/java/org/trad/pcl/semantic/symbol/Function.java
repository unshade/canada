package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import java.util.ArrayList;
import java.util.List;

public class Function extends Variable {

    private final List<String> indexedParametersTypes;
    public Function(String identifier, int shift) {
        super(identifier, shift);
        this.indexedParametersTypes = new ArrayList<>();
    }

    public void addParameter(String parameterType) {
        indexedParametersTypes.add(parameterType);
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
