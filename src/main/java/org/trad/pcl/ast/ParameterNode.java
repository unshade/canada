package org.trad.pcl.ast;

import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.symbol.Parameter;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.List;

public final class ParameterNode extends ASTNode {
    private VariableDeclarationNode variable;
    private String mode;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setVariable(VariableDeclarationNode variable) {
        this.variable = variable;
    }

    public void setType(TypeNode type) {
            variable.setType(type);
    }

    public VariableDeclarationNode getVariable() {
        return variable;
    }

    public Symbol toSymbol() {
        return new Parameter(variable.getIdentifier(), 0);
    }

    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }


}
