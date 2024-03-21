package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.AssignmentStatementNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Variable;

public final class VariableDeclarationNode extends ASTNode implements DeclarationNode {
    private TypeNode type;

    private String identifier;

    private AssignmentStatementNode assignment;

    public void setType(TypeNode type) {
        this.type = type;
    }

    public void setAssignment(AssignmentStatementNode assignment) {
        this.assignment = assignment;
    }

    public TypeNode getType() {
        return type;
    }

    public AssignmentStatementNode getAssignment() {
        return assignment;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Variable toSymbol() {
        int shift;
        if (type.getIdentifier().equals("integer") || type.getIdentifier().equals("character")) {
            shift = 4;
        } else {
            // TODO case of a structure, hardcode to 8 for now
            shift = 8;
        }
        Variable variable = new Variable(this.identifier, shift);
        variable.setType(type.getIdentifier());
        return variable;
    }

    public void initTDS(SymbolTable tdsBefore) {
        // Nothing to do
    }

    public void displayTDS() {
        // Nothing to do
    }


    @Override
    public void accept(ASTNodeVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
