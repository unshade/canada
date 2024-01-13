package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.type.TypeNode;

public class TypeDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public TypeDeclarationNode(String name) {
        super(name);
    }

}