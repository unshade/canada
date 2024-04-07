package org.trad.pcl.ast.type;

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


    public Symbol toSymbol() {
        Record record = new Record(getIdentifier(), 0);
        List<Variable> symbols = new ArrayList<>();
        for (VariableDeclarationNode field : fields) {
            Variable var = field.toSymbol();
            var.setShift(record.getShift() + var.getShift());
            record.setShift(var.getShift());
            symbols.add(var);
        }
        record.setFields(symbols);
        return record;
    }

}
