package org.trad.pcl.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

public class Procedure extends Symbol {

    private final List<String> indexedParametersTypes;

    public Procedure(String identifier, int shift) {
        super(identifier, shift);
        this.indexedParametersTypes = new ArrayList<>();
    }

    public void addParameter(String parameterType) {
        indexedParametersTypes.add(parameterType);
    }

    public List<String> getIndexedParametersTypes() {
        return indexedParametersTypes;
    }
}
