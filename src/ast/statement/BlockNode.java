package ast.statement;

import ast.declaration.DeclarationNode;

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

    @Override
    public String toString() {
        System.out.println(this.getDepth());
        String res = "BlockNode { \n" +
                "\t statements = " + statements + ", \n" +
                "\t declarations = " + declarations + "\n" +
                "}";
        return this.format(res);
    }
}
