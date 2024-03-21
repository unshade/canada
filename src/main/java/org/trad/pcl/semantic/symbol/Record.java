package org.trad.pcl.semantic.symbol;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Helpers.TypeEnum;

import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Record extends Symbol {

    List<Variable> fields;


    public Record(String identifier, int shift) {
        super(identifier, shift);
    }

    public List<Variable> getFields() {
        return fields;
    }

    public void setFields(List<Variable> fields) {
        this.fields = fields;
    }

    public Variable getField(String identifier) {
        for (Variable field : fields) {
            if (field.getIdentifier().equals(identifier)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Record { " +
                super.toString() +
                ", fields = '" + colorize(fields.toString(), Attribute.YELLOW_TEXT()) + '\'' +
                " }";
    }



}

