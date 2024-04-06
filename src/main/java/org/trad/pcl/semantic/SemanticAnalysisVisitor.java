package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.*;
import org.trad.pcl.Helpers.StringFormatHelper;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.symbol.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAnalysisVisitor implements ASTNodeVisitor {
    private static final Stack<SymbolTable> scopeStack = new Stack<>();

    private List<SymbolTable> symbolTables = new ArrayList<>();
    private final ErrorService errorService;

    public SemanticAnalysisVisitor() {
        this.errorService = ErrorService.getInstance();
    }

    public void addPrimitiveTypes() {
        Type integer = new Type(TypeEnum.INT.toString(), 4);
        Type character = new Type(TypeEnum.CHAR.toString(), 1);

        scopeStack.peek().addSymbol(integer, 0);
        scopeStack.peek().addSymbol(character, 0);
    }

    public void addPredefinedFunctions() {
        Procedure put = new Procedure("put",  4);
        put.addParameter(TypeEnum.CHAR.toString());

        scopeStack.peek().addSymbol(put, 0);
    }

    public static Symbol findSymbolInScopes(String identifier) throws UndefinedVariableException {

        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Symbol s = scopeStack.get(i).findSymbol(identifier);
            if (s != null) {
                return s;
            }
        }

        throw new UndefinedVariableException(identifier);
    }

    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {

        node.checkHasReturn();

        node.checkEndIdentifier();

        // Add the function to the current scope
        addSymbolInScopes(node.toSymbol());

        // Create a new scope
        enterScope(new SymbolTable(node.getIdentifier()));


        // Traverse the parameters
        for (ParameterNode parameter : node.getParameters()) {
            parameter.accept(this);
        }

        node.getBody().accept(this);
        //StringFormatHelper.printTDS(scopeStack.peek(), "FUNCTION", node.getIdentifier());

        // Exit the scope
        exitScope();
    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {

        node.checkEndIdentifier();

        // Add the procedure to the current scope
        addSymbolInScopes(node.toSymbol());
        // Create a new scope
        enterScope(new SymbolTable(node.getIdentifier()));

        // Traverse the parameters
        for (ParameterNode parameter : node.getParameters()) {
            parameter.accept(this);
        }

        // Traverse the body
        node.getBody().accept(this);

        //StringFormatHelper.printTDS(scopeStack.peek(), "PROCEDURE", node.getIdentifier());

        // Exit the scope
        exitScope();

    }

    @Override
    public void visit(TypeDeclarationNode node) throws Exception {
        switch (node.getType().getClass().getSimpleName()) {
            case "AccessTypeNode":

                break;
            case "RecordTypeNode":
                RecordTypeNode recordTypeNode = (RecordTypeNode) node.getType();

                List<String> identifiers = recordTypeNode.getFields().stream().map(VariableDeclarationNode::getIdentifier).toList();

                for (VariableDeclarationNode field : recordTypeNode.getFields()) {
                    if (identifiers.indexOf(field.getIdentifier()) != identifiers.lastIndexOf(field.getIdentifier())) {
                        throw new DuplicateRecordFieldException(field.getIdentifier(), recordTypeNode.getIdentifier());
                    }
                    field.getType().accept(this);
                }
                break;
            case "TypeNode":
                break;
        }
        addSymbolInScopes(node.toSymbol());
    }

    @Override
    public void visit(VariableDeclarationNode node) throws Exception {

        node.getType().accept(this);
        addSymbolInScopes(node.toSymbol());
        if (node.getAssignment() != null) {
            node.getAssignment().accept(this);
        }

    }

    @Override
    public void visit(AssignmentStatementNode node) throws Exception {
        node.getVariableReference().accept(this);
        node.getExpression().accept(this);
        node.checkIfAssignable();
    }

    @Override
    public void visit(BlockNode node) {

        // Traverse the declarations
        for (DeclarationNode declarationNode : node.getDeclarations()) {
            try{
                declarationNode.accept(this);
            } catch (Exception e) {
                errorService.registerSemanticError(e);
            }
        }
        // Traverse the statements
        for (StatementNode statementNode : node.getStatements()) {
            try{
            statementNode.accept(this);
            } catch (Exception e) {
                errorService.registerSemanticError(e);
            }
        }
    }


    @Override
    public void visit(CallNode node) throws Exception {
        Symbol s = findSymbolInScopes(node.getIdentifier());
        if (!(s instanceof Function) && node.getIsExpression()) {
            throw new Exception("The identifier " + node.getIdentifier() + " is not a valid function");
        }

        if(s instanceof Function function) {
            node.checkVariableReferenceAccess(function.getReturnType());
        } else if (!(s instanceof Procedure)) {
            throw new Exception("The identifier " + node.getIdentifier() + " is not a valid procedure or function");
        }

        if (node.getArguments() != null) { // Check if the function or procedure has arguments
            for (ExpressionNode expressionNode : node.getArguments()) {
                expressionNode.accept(this);
            }
        }

        node.checkParametersSize();
        node.checkParametersTypes();
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
            node.getCondition().accept(this);

            node.getThenBranch().accept(this);

            if (node.getElseIfBranch() != null) {
                node.getElseIfBranch().accept(this);
            }

            if (node.getElseBranch() != null) {
                node.getElseBranch().accept(this);
            }

            node.checkConditionType();
    }

    @Override
    public void visit(LoopStatementNode node) throws Exception {
            node.getStartExpression().accept(this);
            node.getEndExpression().accept(this);
            node.getBody().accept(this);
    }

    @Override
    public void visit(ReturnStatementNode node) throws Exception {
        Symbol s = findSymbolInScopes(scopeStack.peek().getScopeIdentifier());
        switch (s.getClass().getSimpleName()) {
            case "Function" -> {
                if (node.getExpression() == null) {
                    throw new InvalidReturnTypeException(((Function) s).getReturnType(), TypeEnum.VOID.toString());
                }

                node.getExpression().accept(this);

                if (!node.getExpression().getType().equals(((Function) s).getReturnType())) {
                    throw new InvalidReturnTypeException(((Function) s).getReturnType(), node.getExpression().getType());
                }
            }
            case "Procedure" -> {
                if (node.getExpression() != null) {
                    throw new InvalidReturnTypeException(TypeEnum.VOID.toString(), node.getExpression().getType());
                }
            }
        }

    }

    @Override
    public void visit(WhileStatementNode node) throws Exception {
        node.getCondition().accept(this);
        node.getBody().accept(this);
    }

    @Override
    public void visit(BinaryExpressionNode node) throws Exception {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        node.checkType();

    }

    @Override
    public void visit(CharacterValExpressionNode node) throws Exception {
        // Check if the expression is valid
        node.getExpression().accept(this);
        // Check if the expression is an integer
        if (!node.getExpression().getType().equals(TypeEnum.CHAR.toString())) {
            throw new TypeMismatchException(TypeEnum.CHAR.toString(), node.getExpression().getType());
        }
    }

    @Override
    public void visit(LiteralNode node) {
        // Do nothing
    }

    @Override
    public void visit(VariableReferenceNode node) throws Exception {
        // Check if the variable is defined
        Symbol var = findSymbolInScopes(node.getIdentifier());
        if (!(var instanceof Variable variable)) {
            throw new InvalidVariableReferenceException(node.getIdentifier(), var.getClass().getSimpleName());
        }

        node.checkVariableReferenceAccess(variable.getType());
    }

    @Override
    public void visit(NewExpressionNode node) throws UndefinedVariableException {
        // Check if the type is defined
        Symbol s = findSymbolInScopes(node.getIdentifier());
    }

    @Override
    public void visit(UnaryExpressionNode node) throws Exception {
        node.getOperand().accept(this);
        if (!node.getOperator().getType().equals(node.getOperand().getType())) {
            throw new TypeMismatchException(node.getOperator().getType(), node.getOperand().getType());
        }
    }

    @Override
    public void visit(TypeNode node) throws Exception {
        Symbol s = findSymbolInScopes(node.getIdentifier());
        if (!(s instanceof Type)) {
            throw new Exception("The identifier " + node.getIdentifier() + " is not a valid type");
        }
    }

    @Override
    public void visit(ProgramNode node) {
        // Global scope
        enterScope(new SymbolTable("root"));

        // Build-in features
        addPrimitiveTypes();
        addPredefinedFunctions();

        //StringFormatHelper.printTDS(scopeStack.peek(), "PROCEDURE", "root");

        try {
            node.getRootProcedure().accept(this);
        } catch (Exception ignored) {
        }
        exitScope();
    }

    @Override
    public void visit(ParameterNode node) throws Exception {

        scopeStack.peek().addSymbol(node.toSymbol(), 4);
        node.getType().accept(this);
    }


    public void enterScope(SymbolTable symbolTable) {
        scopeStack.push(symbolTable);
        symbolTables.add(symbolTable);
    }

    //sortir de la portée
    public void exitScope() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }

    public List<SymbolTable> getSymbolTables() {
        return symbolTables;
    }

    public void addSymbolInScopes(Symbol symbol) throws DuplicateSymbolException {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Symbol s = scopeStack.get(i).findSymbol(symbol.getIdentifier());
            if (s != null) {
                throw new DuplicateSymbolException(symbol.getIdentifier());
            }
        }
        scopeStack.peek().addSymbol(symbol, 4);
    }

}
