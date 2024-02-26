package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.BlockNode;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.Stack;

public class SemanticAnalysisVisitor implements ASTNodeVisitor {
    private Stack<SymbolTable> scopeStack = new Stack<>();

    private final ErrorService errorService;

    public SemanticAnalysisVisitor() {
        this.errorService = ErrorService.getInstance();
        // Global scope
        scopeStack.push(new SymbolTable());
        scopeStack.peek().addSymbol(Symbol.builtinFunction("put"));
        scopeStack.peek().addSymbol(Symbol.builtinVariable("integer"));
    }

    @Override
    public void visit(FunctionDeclarationNode node) {
        // Add the function to the current scope
        scopeStack.peek().addSymbol(node.toSymbol());
        // Create a new scope
        scopeStack.push(new SymbolTable());

        // Traverse the parameters
        for (ParameterNode parameter : node.getParameters()) {
            parameter.accept(this);
        }

        // Traverse the body
        node.getBody().accept(this);

        System.out.println("TDS pour la fonction : " + node.getIdentifier());
        System.out.println(scopeStack.peek());
        // Exit the scope
        exitScope();
    }

    @Override
    public void visit(ProcedureDeclarationNode node) {
        // Add the procedure to the current scope
        scopeStack.peek().addSymbol(node.toSymbol());
        // Create a new scope
        scopeStack.push(new SymbolTable());

        // Traverse the parameters
        for (ParameterNode parameter : node.getParameters()) {
            scopeStack.peek().addSymbol(parameter.toSymbol());
        }

        // Traverse the body
        node.getBody().accept(this);

        System.out.println("TDS pour la procédure : " + node.getIdentifier());
        System.out.println(scopeStack.peek());
        // Exit the scope
        exitScope();

    }

    @Override
    public void visit(TypeDeclarationNode node) {

    }


    @Override
    public void visit(VariableDeclarationNode node) {
        // Ajoutez la variable à la TDS courante
        try {
            node.getType().accept(this);
            scopeStack.peek().addSymbol(node.toSymbol());
            if (node.getAssignment() != null) {
                node.getAssignment().accept(this);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void visit(AssignmentStatementNode node) {

        Symbol variable = findSymbolInScopes(node.getIdentifier());
        node.getExpression().accept(this);

    }

    @Override
    public void visit(BlockNode node) {

        // Traverse the declarations
        for (DeclarationNode variable : node.getDeclarations()) {
            variable.accept(this);
        }
        // Traverse the statements
        for (StatementNode statement : node.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(FunctionCallNode node) {

        Symbol function = findSymbolInScopes(node.getIdentifier());

    }

    @Override
    public void visit(IfStatementNode node) {

        // Traverse the condition
        node.getCondition().accept(this);

        // Create a new scope for the then block
        scopeStack.push(new SymbolTable());
        node.getThenBranch().accept(this);
        exitScope();

        if (node.getElseIfBranch() != null) {
            node.getElseIfBranch().accept(this);
        }

        // Create a new scope for the else block
        if (node.getElseBranch() != null) {
            scopeStack.push(new SymbolTable());
            node.getElseBranch().accept(this);
            exitScope();
        }

    }

    @Override
    public void visit(LoopStatementNode node) {

    }

    @Override
    public void visit(ReturnStatementNode node) {

    }

    @Override
    public void visit(WhileStatementNode node) {

    }

    @Override
    public void visit(BinaryExpressionNode node) {

        node.getLeft().accept(this);
        node.getRight().accept(this);

    }

    @Override
    public void visit(CharacterValExpressionNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(VariableReferenceNode node) {
        // Check if the variable is defined
        Symbol variable = findSymbolInScopes(node.getIdentifier());
    }

    @Override
    public void visit(NewExpressionNode node) {

    }

    @Override
    public void visit(UnaryExpressionNode node) {

    }

    @Override
    public void visit(TypeNode node) {
        // Check if the type is defined
        Symbol s = findSymbolInScopes(node.getIdentifier());
    }

    @Override
    public void visit(ProgramNode node) {
        node.getRootProcedure().accept(this);
    }

    @Override
    public void visit(ParameterNode node) {
        // Ajoutez le paramètre à la TDS courante
        try {
            scopeStack.peek().addSymbol(node.toSymbol());
            //node.getVariable().accept(this);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Symbol findSymbolInScopes(String identifier) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Symbol s = scopeStack.get(i).findSymbol(identifier);
            if (s != null) {
                return s;
            }
        }
        errorService.registerSemanticError(new UndefinedVariableException(identifier));
        return null;
    }

    //sortir de la portée
    public void exitScope() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }
}
