package ast2.declaration;

import ast2.type.TypeNode;

public class TypeDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public TypeDeclarationNode(String name) {
        super(name);
    }

}