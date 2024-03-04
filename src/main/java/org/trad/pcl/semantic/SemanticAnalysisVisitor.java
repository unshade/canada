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
import org.trad.pcl.semantic.symbol.Variable;

import java.util.Stack;

import static com.diogonunes.jcolor.Ansi.colorize;

public class SemanticAnalysisVisitor implements ASTNodeVisitor {
    private final ErrorService errorService;
    private final Stack<SymbolTable> scopeStack = new Stack<>();
    private String currentFunctionReturnType;

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
        this.currentFunctionReturnType = node.getReturnType().getIdentifier();
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

        Variable variable = (Variable) findSymbolInScopes(node.getIdentifier());
        if (variable == null) {
            return;
        }
        System.out.println(variable.getIdentifier() + " " + variable.getType() );
        node.getExpression().accept(this);

        // check if expression type is the same as variable type
        switch (node.getExpression().getClass().getSimpleName()) {
            case "FunctionCallNode" -> {
                Function function = (Function) findSymbolInScopes(((FunctionCallNode) node.getExpression()).getIdentifier());
                if (function == null) {
                    return;
                }
                if (!function.getReturnType().equals(variable.getType())) {
                    errorService.registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + colorize(variable.getType(), Attribute.MAGENTA_TEXT()) + " but got " + colorize(function.getReturnType(), Attribute.MAGENTA_TEXT()) + ")"));
                }
            }
            case "LiteralNode" -> {
                LiteralNode literal = (LiteralNode) node.getExpression();
                String objectValueInstance = literal.getValue().getClass().getSimpleName();
                if (objectValueInstance.equals("Long")) {
                    objectValueInstance = "Integer";
                }
                System.out.println(objectValueInstance);
                switch (objectValueInstance) {
                    case "Integer" -> {
                        if (!variable.getType().equals("integer")) {
                            errorService.registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + colorize(variable.getType(), Attribute.MAGENTA_TEXT()) + " but got " + colorize(objectValueInstance, Attribute.MAGENTA_TEXT()) + ")"));
                        }
                    }
                    case "Character" -> {
                        if (!variable.getType().equals("character")) {
                            errorService.registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + colorize(variable.getType(), Attribute.MAGENTA_TEXT()) + " but got " + colorize(objectValueInstance, Attribute.MAGENTA_TEXT()) + ")"));
                        }
                    }
                    default -> {
                        // TODO unknown type
                    }
                }
            }
            case "VariableReferenceNode" -> {
                VariableReferenceNode variableReferenceNode = (VariableReferenceNode) node.getExpression();
                Variable variableExpression = (Variable) findSymbolInScopes(variableReferenceNode.getIdentifier());
                if (!variableExpression.getType().equals(variable.getType())) {
                    errorService.registerSemanticError(new Exception("The type of the expression does not match the type of the variable (expected " + colorize(variable.getType(), Attribute.MAGENTA_TEXT()) + " but got " + colorize(variableExpression.getType(), Attribute.MAGENTA_TEXT()) + ")"));
                }
            }
        }

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
                                errorService.registerSemanticError(new Exception("The type of the argument does not match the type of the parameter (expected " + colorize(calledFunctionDeclaration.getIndexedParametersTypes().get(node.getArguments().indexOf(argument)), Attribute.MAGENTA_TEXT()) + " but got " + colorize(objectValueInstance, Attribute.MAGENTA_TEXT()) + ")" + " for function " + colorize(node.getIdentifier(), Attribute.BLUE_TEXT())));
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
        if (node.getExpressions().size() == 1) {
            node.getExpressions().get(0).accept(this);
            VariableReferenceNode variableReferenceNode = (VariableReferenceNode) node.getExpressions().get(0);
            Variable variable = (Variable) findSymbolInScopes(variableReferenceNode.getIdentifier());
            String returnVariableType = variable.getType();
            if (!returnVariableType.equals(currentFunctionReturnType)) {
                errorService.registerSemanticError(new Exception("The return type does not match the function return type (expected " + colorize(currentFunctionReturnType, Attribute.MAGENTA_TEXT()) + " but got " + colorize(returnVariableType, Attribute.MAGENTA_TEXT()) + ")"));
            }
        } else {
            node.getExpressions().forEach(expressionNode -> expressionNode.accept(this));
        }
    }

    @Override
    public void visit(WhileStatementNode node) {
        node.getCondition().accept(this);
        node.getBody().accept(this);
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
