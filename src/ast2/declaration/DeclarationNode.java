package ast2.declaration;

import ast2.ASTNode;

public class DeclarationNode extends ASTNode {
    // Classe de base pour les déclarations
    protected String name;

    public DeclarationNode(String name) {
        this.name = name;
    }
}