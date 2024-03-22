package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.InvalidReturnTypeException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.StringFormatHelper;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.AccessTypeNode;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.symbol.Function;
import org.trad.pcl.semantic.symbol.Record;
import org.trad.pcl.semantic.symbol.Symbol;
import org.trad.pcl.semantic.symbol.Type;
import org.trad.pcl.semantic.symbol.Variable;

import java.util.List;
import java.util.Stack;

public class SemanticAnalysisVisitor implements ASTNodeVisitor {
    private static final Stack<SymbolTable> scopeStack = new Stack<>();
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

    public static Symbol findSymbolInScopes(String identifier) {

        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Symbol s = scopeStack.get(i).findSymbol(identifier);
            if (s != null) {
                return s;
            }
        }

        ErrorService.getInstance().registerSemanticError(new UndefinedVariableException(identifier));
        return null;
    }

    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {
        // Add the function to the current scope with current shift
        scopeStack.peek().addSymbol(node.toSymbol(), 0);
        // Create a new scope
        scopeStack.push(new SymbolTable(node.getIdentifier()));

        node.checkHasReturn();

        node.checkEndIdentifier();

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
    public void visit(ProcedureDeclarationNode node) throws Exception {
        // Add the procedure to the current scope
        scopeStack.peek().addSymbol(node.toSymbol(), 0);
        // Create a new scope
        scopeStack.push(new SymbolTable(node.getIdentifier()));

        node.checkEndIdentifier();

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
    public void visit(TypeDeclarationNode node) throws Exception {
    try {
        node.getType().accept(this);
        scopeStack.peek().addSymbol(node.toSymbol(), 4);
    } catch (Exception e) {
        errorService.registerSemanticError(e);
    }
    }

    @Override
    public void visit(VariableDeclarationNode node) throws Exception {

        node.getType().accept(this);
        scopeStack.peek().addSymbol(node.toSymbol(), 4);
        if (node.getAssignment() != null) {
            node.getAssignment().accept(this);
        }

    }

    @Override
    public void visit(AssignmentStatementNode node) {
        try{
        node.getVariableReference().accept(this);
        node.getExpression().accept(this);
        node.checkIfAssignable();
        } catch (Exception e) {
            errorService.registerSemanticError(e);
        }
    }

    @Override
    public void visit(BlockNode node) {

        // Traverse the declarations
        for (DeclarationNode declarationNode : node.getDeclarations()) {
            try{
                declarationNode.accept(this);
            } catch (Exception ignored) {

            }
        }
        // Traverse the statements
        for (StatementNode statementNode : node.getStatements()) {
            try{
            statementNode.accept(this);
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public void visit(FunctionCallNode node) throws Exception {
        // Check if the function is defined
        node.getVariableReference().accept(this);
        for (ExpressionNode expressionNode : node.getArguments()) {
            expressionNode.accept(this);
        }
        node.checkParametersSize();
        node.checkParametersTypes();
    }

    @Override
    public void visit(IfStatementNode node) {
        try {
            node.getCondition().accept(this);

            node.getThenBranch().accept(this);

            if (node.getElseIfBranch() != null) {
                node.getElseIfBranch().accept(this);
            }

            if (node.getElseBranch() != null) {
                node.getElseBranch().accept(this);
            }

            node.checkConditionType();
        } catch (Exception e) {
            errorService.registerSemanticError(e);
        }

    }

    @Override
    public void visit(LoopStatementNode node) {
        try {
            node.getStartExpression().accept(this);
            node.getEndExpression().accept(this);
            node.getBody().accept(this);
        } catch (Exception e) {
            errorService.registerSemanticError(e);
        }

    }

    @Override
    public void visit(ReturnStatementNode node) throws Exception {
        node.getExpression().accept(this);
        Symbol s = findSymbolInScopes(scopeStack.peek().getScopeIdentifier());
        if (!(s instanceof Function)) {
            errorService.registerSemanticError(new Exception("Return statement can only be used in a function"));
            return;
        }
        if (!node.getExpression().getType().equals(((Function) s).getType())) {
            errorService.registerSemanticError(new InvalidReturnTypeException(((Function) s).getType(), node.getExpression().getType()));
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
            errorService.registerSemanticError(new Exception("The expression is not a integer"));
        }
    }

    @Override
    public void visit(LiteralNode node) {
    }

    @Override
    public void visit(VariableReferenceNode node) throws Exception {
        // Check if the variable is defined
        Symbol var = findSymbolInScopes(node.getIdentifier());
        if (!(var instanceof Variable variable)) {
            throw new Exception("The identifier " + node.getIdentifier() + " is not a valid variable");
        }

        while(node.getNextExpression() != null) {
            Symbol type = findSymbolInScopes((variable).getType());
            if (!(type instanceof Record)) {
                throw new Exception("The type " + (variable).getType() + " is not a valid record");
            }

            Variable field = ((Record) type).getField(node.getNextExpression().getIdentifier());
            if (field == null) {
                throw new Exception("The field " + node.getNextExpression().getIdentifier() + " for the record " + ((Variable) variable).getType() + " is not defined");
            }

            node = node.getNextExpression();
            variable = field;

        }
    }

    @Override
    public void visit(NewExpressionNode node) {
        // Check if the type is defined
        Symbol s = findSymbolInScopes(node.getIdentifier());
    }

    @Override
    public void visit(UnaryExpressionNode node) throws Exception {
        node.getOperand().accept(this);
        if (!node.getOperator().getType().equals(node.getOperand().getType())) {
            errorService.registerSemanticError(new Exception("Type mismatch"));
        }
    }

    @Override
    public void visit(TypeNode node) {
        // Check if the type is defined
        Symbol s = findSymbolInScopes(node.getIdentifier());

    }

    @Override
    public void visit(RecordTypeNode node) throws Exception {
        System.out.println("RecordTypeNode " + node.getIdentifier());

        if(node.getFields().isEmpty()) {
            throw new Exception("Record " + node.getIdentifier() + " has no fields");
        }

        List<String> identifiers = node.getFields().stream().map(VariableDeclarationNode::getIdentifier).toList();

        for (VariableDeclarationNode field : node.getFields()) {
            if (identifiers.indexOf(field.getIdentifier()) != identifiers.lastIndexOf(field.getIdentifier())) {
                throw new Exception("Field " + field.getIdentifier() + " is defined multiple times");
            }
            field.getType().accept(this);

        }
    }

    @Override
    public void visit(AccessTypeNode node) {

    }

    @Override
    public void visit(ProgramNode node) {
        // Global scope
        scopeStack.push(new SymbolTable());

        // Build-in features
        scopeStack.peek().addSymbol(Symbol.builtinFunction("put"), 0);

        addPrimitiveTypes();
        StringFormatHelper.printTDS(scopeStack.peek(), "PROCEDURE", "root");

        try {
            node.getRootProcedure().accept(this);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void visit(ParameterNode node) throws Exception {

        scopeStack.peek().addSymbol(node.toSymbol(), 4);
        node.getType().accept(this);
    }

    //sortir de la portée
    public void exitScope() {
        if (!scopeStack.isEmpty()) {
            scopeStack.pop();
        }
    }
}
