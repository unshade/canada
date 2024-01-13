package ast2;

import ast2.declaration.DeclarationNode;

import java.util.ArrayList;
import java.util.List;

class ProgramNode extends ASTNode {
    private List<DeclarationNode> declarations;

    public ProgramNode() {
        this.declarations = new ArrayList<>();
    }

    public void addDeclaration(DeclarationNode declaration) {
        declarations.add(declaration);
        declaration.setParent(this);
    }

}
