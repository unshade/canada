package org.trad.pcl.semantic;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.StringFormatHelper;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.FunctionDeclarationNode;
import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.ast.declaration.TypeDeclarationNode;
import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.Stack;

import static com.diogonunes.jcolor.Ansi.colorize;

public class SemanticAnalysisVisitor implements ASTNodeVisitor {
    private final ErrorService errorService;
    private final Stack<SymbolTable> scopeStack = new Stack<>();

    public SemanticAnalysisVisitor() {
        this.errorService = ErrorService.getInstance();
        // Global scope
        scopeStack.push(new SymbolTable());

        // Build-in features
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

        node.getBody().accept(this);
        StringFormatHelper.printTDS(scopeStack.peek(), "FUNCTION", node.getIdentifier());

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
            parameter.accept(this);
        }

        // Traverse the body
        node.getBody().accept(this);

        StringFormatHelper.printTDS(scopeStack.peek(), "PROCEDURE", node.getIdentifier());

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
        node.getDeclarations().forEach(declarationNode -> declarationNode.accept(this));
        // Traverse the statements
        node.getStatements().forEach(statementNode -> statementNode.accept(this));
    }

    @Override
    public void visit(FunctionCallNode node) {

        // Check if the function is defined + get the function symbol
        Function calledFunctionDeclaration;
        if ((calledFunctionDeclaration = (Function) findSymbolInScopes(node.getIdentifier())) == null) {
            return;
        }

        // Check if the number of arguments match the number of declared parameters
        if (node.getArguments().size() != calledFunctionDeclaration.getIndexedParametersTypes().size()) {
            errorService.registerSemanticError(new Exception("The number of arguments does not match the number of parameters (expected " + calledFunctionDeclaration.getIndexedParametersTypes().size() + " but got " + node.getArguments().size() + ")" + " for function " + node.getIdentifier()));
        }

        for (ExpressionNode argument : node.getArguments()) {
            argument.accept(this);
            switch (argument.getClass().getSimpleName()) {
                case "FunctionCallNode" -> {
                    Function argumentFunction = (Function) findSymbolInScopes(((FunctionCallNode) argument).getIdentifier());

                    if (!argumentFunction.getReturnType().equals(calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)))) {
                        errorService.registerSemanticError(new Exception("The type of the argument does not match the type of the function " + colorize(argumentFunction.getIdentifier(), Attribute.BLUE_TEXT()) + " return type (expected " + calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)) + " but got " + argumentFunction.getReturnType() + ")" + " for function " + colorize(node.getIdentifier(), Attribute.BLUE_TEXT())));
                    }
                }
                case "LiteralNode" -> {
                    LiteralNode literal = (LiteralNode) argument;
                    String objectValueInstance = literal.getValue().getClass().getSimpleName();
                    switch (objectValueInstance) {
                        case "Integer" -> {
                            if (!calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)).equals("integer")) {
                                errorService.registerSemanticError(new Exception("The type of the argument does not match the type of the parameter (expected " + calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)) + " but got " + objectValueInstance + ")" + " for function " + node.getIdentifier()));
                            }
                        }
                        case "Character" -> {
                            if (!calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)).equals("character")) {
                                errorService.registerSemanticError(new Exception("The type of the argument does not match the type of the parameter (expected " + calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)) + " but got " + objectValueInstance + ")" + " for function " + node.getIdentifier()));
                            }
                        }
                        default -> {
                            // TODO unknown type
                        }
                    }
                }
                case "VariableReferenceNode" -> {
                }
            }
        }

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
        node.getStartExpression().accept(this);
        node.getEndExpression().accept(this);
        node.getBody().accept(this);
    }

    @Override
    public void visit(ReturnStatementNode node) {
        node.getExpressions().forEach(expressionNode -> expressionNode.accept(this));
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
