package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.*;
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
    public static final StackTDS scopeStack = new StackTDS();

    private List<SymbolTable> symbolTables = new ArrayList<>();
    private final ErrorService errorService;

    public SemanticAnalysisVisitor() {
        this.errorService = ErrorService.getInstance();
    }

    public void addPrimitiveTypes() {
        Type integer = new Type(TypeEnum.INT.toString(), 0);
        integer.setTypeEnum(TypeEnum.INT);
        integer.setSize(4);
        Type character = new Type(TypeEnum.CHAR.toString(), 0);
        character.setTypeEnum(TypeEnum.CHAR);
        character.setSize(4);
        Type bool = new Type(TypeEnum.BOOL.toString(), 0);
        bool.setTypeEnum(TypeEnum.BOOL);
        bool.setSize(4);

        scopeStack.peek().addSymbol(integer);
        scopeStack.peek().addSymbol(character);
        scopeStack.peek().addSymbol(bool);
    }

    public void addPredefinedFunctions() {
        Procedure put = new Procedure("put",  0);
        Procedure putInt = new Procedure("putInt",  0);
        putInt.addParameter(TypeEnum.INT.toString());
        put.addParameter(TypeEnum.CHAR.toString());

        scopeStack.peek().addSymbol(put);
        scopeStack.peek().addSymbol(putInt);
    }

    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {

        node.checkHasReturn();

        node.checkEndIdentifier();

        // Add the function to the current scope
        scopeStack.addSymbolInScopes(node.toSymbol(), node.getConcernedLine());

        // Create a new scope
        enterScope(new SymbolTable(node.getIdentifier()));


        // Traverse the parameters
        for (ParameterNode parameter : node.getParameters()) {
            parameter.accept(this);
        }

        node.getReturnType().accept(this);

        node.getBody().accept(this);
        //StringFormatHelper.printTDS(scopeStack.peek(), "FUNCTION", node.getIdentifier());

        // Exit the scope
        scopeStack.exitScope();
    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {

        node.checkEndIdentifier();

        // Add the procedure to the current scope
        scopeStack.addSymbolInScopes(node.toSymbol(), node.getConcernedLine());
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
        scopeStack.exitScope();

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
                        throw new DuplicateRecordFieldException(field.getIdentifier(), recordTypeNode.getIdentifier(), field.getConcernedLine());
                    }
                    field.getType().accept(this);
                }
                break;
            case "TypeNode":
                break;
        }
        scopeStack.addSymbolInScopes(node.toSymbol(), node.getConcernedLine());
    }

    @Override
    public void visit(VariableDeclarationNode node) throws Exception {

        node.getType().accept(this);
        scopeStack.addSymbolInScopes(node.toSymbol(), node.getConcernedLine());
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
        Symbol s = scopeStack.findSymbolInScopes(node.getIdentifier(), node.getConcernedLine());
        if (!(s instanceof Function) && node.getIsExpression()) {
            throw new Exception("Line " + node.getConcernedLine() + ": " + "The identifier " + node.getIdentifier() + " is not a valid function");
        }

        if(s instanceof Function function) {
            node.checkVariableReferenceAccess(function.getReturnType());
        } else if (!(s instanceof Procedure)) {
            throw new Exception("Line " + node.getConcernedLine() + ": " + "The identifier " + node.getIdentifier() + " is not a valid procedure or function");
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

            if (!node.getElseIfBranches().isEmpty()) {
                for (ElseIfStatementNode elseIfStatementNode : node.getElseIfBranches()) {
                    elseIfStatementNode.accept(this);
                }

            }

            if (node.getElseBranch() != null) {
                node.getElseBranch().accept(this);
            }

            node.checkConditionType();
    }

    public void visit(ElseIfStatementNode node) throws Exception {
        node.getCondition().accept(this);
        node.getThenBranch().accept(this);
        node.checkConditionType();
    }

    @Override
    public void visit(LoopStatementNode node) throws Exception {
            Symbol s = scopeStack.findSymbolInScopes(node.getIdentifier(), node.getConcernedLine());
            if (!(s instanceof Variable var)) {
                throw new Exception("Line " + node.getConcernedLine() + ": " + "The identifier " + node.getIdentifier() + " is not a valid variable");
            }

            if (!var.getType().equals(TypeEnum.INT.toString())) {
                throw new TypeMismatchException(TypeEnum.INT.toString(), var.getType(), node.getConcernedLine());
            }

            node.getStartExpression().accept(this);
            node.getEndExpression().accept(this);
            if (!node.getStartExpression().getType(scopeStack).equals(TypeEnum.INT.toString()) || !node.getEndExpression().getType(scopeStack).equals(TypeEnum.INT.toString())) {
                if (!node.getStartExpression().getType(scopeStack).equals(TypeEnum.INT.toString())) {
                    throw new TypeMismatchException(TypeEnum.INT.toString(), node.getStartExpression().getType(scopeStack), node.getConcernedLine());
                }
                if (!node.getEndExpression().getType(scopeStack).equals(TypeEnum.INT.toString())) {
                    throw new TypeMismatchException(TypeEnum.INT.toString(), node.getEndExpression().getType(scopeStack), node.getConcernedLine());
                }
            }
            node.getBody().accept(this);
    }

    @Override
    public void visit(ReturnStatementNode node) throws Exception {
        Symbol s = scopeStack.findSymbolInScopes(scopeStack.peek().getScopeIdentifier(), node.getConcernedLine());
        switch (s.getClass().getSimpleName()) {
            case "Function" -> {
                if (node.getExpression() == null) {
                    throw new InvalidReturnTypeException(((Function) s).getReturnType(), TypeEnum.VOID.toString(), node.getConcernedLine());
                }

                node.getExpression().accept(this);

                if (!node.getExpression().getType(scopeStack).equals(((Function) s).getReturnType())) {
                    throw new InvalidReturnTypeException(((Function) s).getReturnType(), node.getExpression().getType(scopeStack), node.getConcernedLine());
                }
            }
            case "Procedure" -> {
                if (node.getExpression() != null) {
                    throw new InvalidReturnTypeException(TypeEnum.VOID.toString(), node.getExpression().getType(scopeStack), node.getConcernedLine());
                }
            }
        }

    }

    @Override
    public void visit(WhileStatementNode node) throws Exception {
        node.getCondition().accept(this);
        if (!node.getCondition().getType(scopeStack).equals(TypeEnum.BOOL.toString())) {
            throw new TypeMismatchException(TypeEnum.BOOL.toString(), node.getCondition().getType(scopeStack), node.getConcernedLine());
        }
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
        System.out.println(node.getExpression().getType(scopeStack));
        if (!node.getExpression().getType(scopeStack).equals(TypeEnum.INT.toString())) {
            throw new TypeMismatchException(TypeEnum.INT.toString(), node.getExpression().getType(scopeStack), node.getConcernedLine());
        }
    }

    @Override
    public void visit(LiteralNode node) {
        // Do nothing
    }

    @Override
    public void visit(VariableReferenceNode node) throws Exception {
        // Check if the variable is defined
        Symbol var = scopeStack.findSymbolInScopes(node.getIdentifier(), node.getConcernedLine());
        String type;
        if (var instanceof Variable variable ) {
            type = variable.getType();
        } else if (var instanceof Function function) {
            type = function.getReturnType();

        } else {
            throw new InvalidVariableReferenceException(node.getIdentifier(), var.getClass().getSimpleName(), node.getConcernedLine());
        }

        node.checkVariableReferenceAccess(type);
    }

    @Override
    public void visit(NewExpressionNode node) throws UndefinedVariableException {
        // Check if the type is defined
        Symbol s = scopeStack.findSymbolInScopes(node.getIdentifier(), node.getConcernedLine());
    }

    @Override
    public void visit(UnaryExpressionNode node) throws Exception {
        node.getOperand().accept(this);
        if (!node.getOperator().getType().equals(node.getOperand().getType(scopeStack))) {
            throw new TypeMismatchException(node.getOperator().getType(), node.getOperand().getType(scopeStack), node.getConcernedLine());
        }
    }

    @Override
    public void visit(TypeNode node) throws Exception {
        Symbol s = scopeStack.findSymbolInScopes(node.getIdentifier(), node.getConcernedLine());
        if (!(s instanceof Type)) {
            throw new Exception("Line " + node.getConcernedLine() + ": " + "The identifier " + node.getIdentifier() + " is not a valid type");
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
        scopeStack.exitScope();
    }

    @Override
    public void visit(ParameterNode node) throws Exception {

        node.getType().accept(this);
        scopeStack.peek().addSymbol(node.toSymbol());
    }


    public void enterScope(SymbolTable symbolTable) {
        scopeStack.push(symbolTable);
        symbolTables.add(symbolTable);
    }

    public List<SymbolTable> getSymbolTables() {
        return symbolTables;
    }


}
