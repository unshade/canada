package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.statement.AssignmentStatementNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;

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

    public Symbol toSymbol() {
        int shift;
        if (type.getIdentifier().equals("integer") || type.getIdentifier().equals("character")) {
            shift = 4;
        } else {
            // TODO case of a structure, hardcode to 8 for now
            shift = 8;
        }
        return new Symbol(this.identifier, shift);
    }

    public void initTDS(SymbolTable tdsBefore) {
        // Nothing to do
    }

    public void displayTDS() {
        // Nothing to do
    }


    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
