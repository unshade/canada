package org.trad.pcl.ast.statement;


import org.trad.pcl.ast.declaration.DeclarationNode;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends StatementNode {
    private List<StatementNode> statements;
    private List<DeclarationNode> declarations;

    public BlockNode() {
        this.statements = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    public void addStatement(StatementNode statement) {
        statements.add(statement);
        statement.setParent(this);
    }

    public void addDeclaration(DeclarationNode declaration) {
        declarations.add(declaration);
        declaration.setParent(this);
    }

    public void addDeclarations(List<DeclarationNode> declarations) {
        for (DeclarationNode declaration : declarations) {
            addDeclaration(declaration);
        }
    }

    public void addStatements(List<StatementNode> statements) {
        for (StatementNode statement : statements) {
            addStatement(statement);
        }
    }

}
