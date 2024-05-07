package org.trad.pcl.ast.statement;


import org.trad.pcl.Exceptions.Semantic.MissingReturnStatementException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ASTNode;
import org.trad.pcl.ast.declaration.DeclarationNode;
import org.trad.pcl.ast.statement.StatementNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends ASTNode implements StatementNode {
    private List<StatementNode> statements;
    private List<DeclarationNode> declarations;

    //private SymbolTable tds;

    public BlockNode() {
        this.statements = new ArrayList<>();
        this.declarations = new ArrayList<>();
    }

    /*public void initTDS(SymbolTable tds) {
        this.tds = new SymbolTable(tds);
        for (DeclarationNode declaration : declarations) {
            declaration.initTDS(this.tds);
            this.tds.addSymbol(declaration.toSymbol());
        }
    }

    public void displayTDS() {
        System.out.println(this.tds);
        for (DeclarationNode declaration : declarations) {
            declaration.displayTDS();
        }
    }*/

   /* public SymbolTable getTDS() {
        return tds;
    }*/

    public void addStatement(StatementNode statement) {
        statements.add(statement);
    }

    public void addDeclaration(DeclarationNode declaration) {
        declarations.add(declaration);
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

    public boolean hasReturn() {
        for (StatementNode statement : getStatements()) {
            if (statement instanceof ReturnStatementNode) {
                return true;
            }
            if (statement instanceof IfStatementNode) {
                if (((IfStatementNode) statement).hasReturn()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<DeclarationNode> getDeclarations() {
        return declarations;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public void accept(ASTNodeVisitor visitor) {
        visitor.visit(this);
    }
}
