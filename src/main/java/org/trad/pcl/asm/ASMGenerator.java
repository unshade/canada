package org.trad.pcl.asm;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.OperatorEnum;
import org.trad.pcl.Helpers.StringFormatHelper;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.FunctionDeclarationNode;
import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.ast.declaration.TypeDeclarationNode;
import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.TypeNode;
import org.trad.pcl.semantic.ASTNodeVisitor;
import org.trad.pcl.semantic.SemanticAnalysisVisitor;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// TODO DECLARE FUNCTIONS AND PROCEDURE AT END OF FILE ELSE IT WILL BE DISPLAYED WRONGLY
public final class ASMGenerator implements ASTNodeVisitor {

    private List<SymbolTable> symbolTables;
    private int symbolTableIndex = 0;
    private static final Stack<SymbolTable> scopeStack = new Stack<>();

    private final StringBuilder output;

    public ASMGenerator(List<SymbolTable> symbolTables) {
        this.symbolTables = symbolTables;
        output = new StringBuilder();
    }

    public String getOutput() {
        return this.output.toString();
    }

    public Symbol findSymbolInScopes(String identifier) throws UndefinedVariableException {

        for (SymbolTable symbolTable : this.symbolTables) {
            Symbol symbol = symbolTable.findSymbol(identifier);
            if (symbol != null) {
                return symbol;
            }
        }

        throw new UndefinedVariableException(identifier);
    }

    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        this.output.append(symbol.getIdentifier()).append("\n").append("""
                \t STMFD   R13!, {R11, LR} ;
                \t MOV     R11, R13 ;
                """);
        node.getParameters().forEach(param -> {
            try {
                param.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        node.getBody().accept(this);
    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        this.output.append(symbol.getIdentifier()).append("\n").append("""
                \t STMFD   R13!, {R11, LR} ;
                \t MOV     R11, R13 ;
                """);
        node.getParameters().forEach(param -> {
            try {
                param.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        node.getBody().accept(this);
    }

    @Override
    public void visit(TypeDeclarationNode node) throws Exception {

    }

    @Override
    public void visit(VariableDeclarationNode node) throws Exception {
        this.output.append("""
                \t SUB R13, R13, #4 ;
                """);

    }

    @Override
    public void visit(AssignmentStatementNode node) throws Exception {
        node.getVariableReference().accept(this);
        node.getExpression().accept(this);
        this.output.append("""
                \t STR     R0, [R11, #%s] ;
                """.formatted(findSymbolInScopes(node.getVariableReference().getIdentifier()).getShift()));
    }

    @Override
    public void visit(BlockNode node) {
        node.getStatements().forEach(statement -> {
            try {
                statement.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        node.getDeclarations().forEach(declaration -> {
            try {
                declaration.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void visit(CallNode node) throws Exception {
        Symbol symbol = this.findSymbolInScopes(node.getIdentifier());
        node.getArguments().forEach(arg -> {
            try {
                arg.accept(this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.output.append("""
                \t BL     %s ;
                """.formatted(symbol.getIdentifier()));
    }

    @Override
    public void visit(IfStatementNode node) throws Exception {
        node.getCondition().accept(this);
        node.getElseBranch().accept(this);
        node.getElseIfBranch().accept(this);
        node.getThenBranch().accept(this);
    }

    @Override
    public void visit(LoopStatementNode node) throws Exception {

    }

    @Override
    public void visit(ReturnStatementNode node) throws Exception {
        this.output.append("""
                \t MOV     R0, #0 ;
                \t MOV     R13, R11 ;
                \t LDMFD   R13!, {R11, PC} ;
                """);
    }

    @Override
    public void visit(WhileStatementNode node) throws Exception {

    }

    @Override
    public void visit(BinaryExpressionNode node) throws Exception {
        node.getLeft().accept(this);
        node.getRight().accept(this);
    }

    @Override
    public void visit(CharacterValExpressionNode node) throws Exception {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(VariableReferenceNode node) throws Exception {

    }

    @Override
    public void visit(NewExpressionNode node) throws UndefinedVariableException {

    }

    @Override
    public void visit(UnaryExpressionNode node) throws Exception {
        node.getOperator().accept(this);
        node.getOperand().accept(this);
    }

    @Override
    public void visit(TypeNode node) throws Exception {
        // TODO FINAL NODE
    }

    @Override
    public void visit(ProgramNode node) {
        try {
            node.getRootProcedure().accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.output.append("\t END\n");
    }

    @Override
    public void visit(ParameterNode node) throws Exception {
        node.getType().accept(this);
    }
}
