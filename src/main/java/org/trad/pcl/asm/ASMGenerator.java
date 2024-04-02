package org.trad.pcl.asm;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
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

import java.util.ArrayList;
import java.util.List;

public class ASMGenerator implements ASTNodeVisitor {

    private List<SymbolTable> symbolTables;

    public ASMGenerator(List<SymbolTable> symbolTables) {
        this.symbolTables = symbolTables;
    }


    @Override
    public void visit(FunctionDeclarationNode node) throws Exception {

    }

    @Override
    public void visit(ProcedureDeclarationNode node) throws Exception {

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

    }

    @Override
    public void visit(CallNode node) throws Exception {

    }

    @Override
    public void visit(IfStatementNode node) throws Exception {

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

    }

    @Override
    public void visit(TypeNode node) throws Exception {

    }

    @Override
    public void visit(ProgramNode node) {

    }

    @Override
    public void visit(ParameterNode node) throws Exception {

    }
}
