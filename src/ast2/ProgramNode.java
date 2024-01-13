package ast2;

import ast2.declaration.DeclarationNode;
import ast2.statement.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends ASTNode {
    private List<DeclarationNode> declarations;
    private List<StatementNode> statements;

    public ProgramNode() {
        this.declarations = new ArrayList<>();
        this.statements = new ArrayList<>();
    }

    public void addDeclaration(DeclarationNode declaration) {
        declarations.add(declaration);
        declaration.setParent(this);
    }

    public void addStatement(StatementNode statement) {
        statements.add(statement);
        statement.setParent(this);
    }

}
