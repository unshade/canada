package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.type.TypeNode;

public final class TypeDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public void setType(TypeNode type) {
        this.type = type;
    }


}