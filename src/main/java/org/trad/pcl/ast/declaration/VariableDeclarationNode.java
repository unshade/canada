package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.type.TypeNode;

public class VariableDeclarationNode extends DeclarationNode {
    private TypeNode type;

    public void setType(TypeNode type) {
        this.type = type;
        type.setParent(this);
    }
}
