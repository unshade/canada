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
import org.trad.pcl.semantic.SymbolTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ASMGenerator implements ASTNodeVisitor {

    private List<SymbolTable> symbolTables;
    private final StringBuilder output;

    public ASMGenerator(List<SymbolTable> symbolTables) {
        this.symbolTables = symbolTables;
        output = new StringBuilder();
    }


    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {

    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {
        this.output.append("""
                               
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

    }

    @Override
    public void visit(AssignmentStatementNode node) throws Exception {

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
        this.output.append("""
                main
                \t MOV R11, R13 ;
                \t STMFD   R13!, {R0, R1} ;
                """);
        try {
            node.getRootProcedure().accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(ParameterNode node) throws Exception {
        node.getType().accept(this);
    }
}
