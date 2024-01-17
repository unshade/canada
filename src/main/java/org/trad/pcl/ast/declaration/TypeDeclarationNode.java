package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.type.TypeNode;

public final class TypeDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public void setType(TypeNode type) {
        type.setIdentifier(getIdentifier());
        this.type = type;
    }

    public TypeNode getType() {
        return type;
    }


}