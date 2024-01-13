package ast.declaration;

import ast.type.TypeNode;

public class VariableDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public VariableDeclarationNode(String name, TypeNode type) {
        super(name);
        this.type = type;
    }
}
