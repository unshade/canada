package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.ParameterMode;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;

import static com.diogonunes.jcolor.Ansi.colorize;

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

    @Override
    public String toString() {
        return "Parameter { " +
                "identifier = '" + colorize(identifier, Attribute.YELLOW_TEXT()) + '\'' +
                ", type = '" + type + '\'' +
                ", mode = " + mode +
                ", shift = " + colorize(Integer.toString(shift), Attribute.RED_TEXT()) +
                " }";
    }

}
