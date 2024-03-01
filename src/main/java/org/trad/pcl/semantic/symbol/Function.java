package org.trad.pcl.semantic.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function extends Symbol {

    private List<String> parameters;
    public Function(String identifier, int shift) {
        super(identifier, shift);
        this.parameters = new ArrayList<>();
    }


    public void addParameter(String parameterType) {
        parameters.add(parameterType);
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Function{" +
                "parameters=" + parameters +
                '}';
    }
}
