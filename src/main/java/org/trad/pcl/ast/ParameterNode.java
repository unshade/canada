package org.trad.pcl.ast;

import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.type.TypeNode;

import java.util.List;

public final class ParameterNode extends ASTNode {
    private List<VariableDeclarationNode> variables;
    private String mode;


    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setVariables(List<VariableDeclarationNode> variables) {
        this.variables = variables;
    }

    public void setType(TypeNode type) {
        for (VariableDeclarationNode variable : variables) {
            variable.setType(type);
        }
    }


}
