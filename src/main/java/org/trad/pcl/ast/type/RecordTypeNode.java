package org.trad.pcl.ast.type;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Variable;

import java.util.ArrayList;
import java.util.List;

public final class RecordTypeNode extends TypeNode {

    private List<VariableDeclarationNode> fields;

    public RecordTypeNode() {
        super();
        fields = new ArrayList<>();
    }

    public void addField(VariableDeclarationNode field) {
        fields.add(field);
    }

    public void addFields(List<VariableDeclarationNode> fields) {
        for (VariableDeclarationNode field : fields) {
            addField(field);
        }
    }

    public List<VariableDeclarationNode> getFields() {
        return fields;
    }


    public Symbol toSymbol() throws UndefinedVariableException {
        Record record = new Record(getIdentifier(), 0);
        List<Variable> symbols = new ArrayList<>();
        int shift = 0;
        for (VariableDeclarationNode field : fields) {
            Variable var = field.toSymbol();
            int temp = var.getShift();
            var.setShift(record.getShift() + shift);
            //  record.setShift(var.getShift());
            symbols.add(var);
            shift += temp;
        }
        record.setSize(shift);
        record.setFields(symbols);
        return record;
    }

}
