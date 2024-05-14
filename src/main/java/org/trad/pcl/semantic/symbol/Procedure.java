package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.asm.ASMGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Procedure extends Symbol {

    private final List<String> indexedParametersTypes;

    public Procedure(String identifier, int shift) {
        super(identifier, shift);
        this.indexedParametersTypes = new ArrayList<>();
    }

    public void addParameter(String parameterType) {
        indexedParametersTypes.add(parameterType);
    }

    public int getParametersSize() {
        int size = 0;
        for (String parameterType : indexedParametersTypes) {
            Type type = (Type) ASMGenerator.scopeStack.findSymbolInScopes(parameterType);
            size += type.getSize();
        }
        return size;
    }

    public List<String> getIndexedParametersTypes() {
        return indexedParametersTypes;
    }

    @Override
    public String toString() {
        return "Procedure { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", indexedParametersTypes = '" + indexedParametersTypes.toString() + '\'' +
                ", shift = " + colorize(Integer.toString(shift), Attribute.RED_TEXT()) +
                " }";
    }
}
