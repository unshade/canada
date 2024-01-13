package ast.declaration;

import ast.type.TypeNode;

public class TypeDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public TypeDeclarationNode(String name) {
        super(name);
    }

}