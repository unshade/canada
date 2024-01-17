package org.trad.pcl.ast.type;

import org.trad.pcl.ast.ASTNode;

public class TypeNode extends ASTNode {
    private String identifier;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}