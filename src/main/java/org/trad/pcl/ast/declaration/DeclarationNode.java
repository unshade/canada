package org.trad.pcl.ast.declaration;

import org.trad.pcl.ast.ASTNode;

public class DeclarationNode extends ASTNode {
    // Classe de base pour les déclarations
    protected String identifier;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}