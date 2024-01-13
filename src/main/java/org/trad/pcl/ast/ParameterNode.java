package org.trad.pcl.ast;

import org.trad.pcl.ast.type.TypeNode;

public class ParameterNode extends ASTNode {
    private String name;
    private TypeNode type;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TypeNode type) {
        this.type = type;
    }
}
