package org.trad.pcl.semantic;

import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.BlockNode;
import org.trad.pcl.ast.declaration.FunctionDeclarationNode;
import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.ast.declaration.TypeDeclarationNode;
import org.trad.pcl.ast.declaration.VariableDeclarationNode;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.TypeNode;

public interface ASTNodeVisitor {

    // DeclarationNode
    void visit(FunctionDeclarationNode node);

    void visit(ProcedureDeclarationNode node);

    void visit(TypeDeclarationNode node);

    void visit(VariableDeclarationNode node);

    // StatementNode

    void visit(AssignmentStatementNode node);

    void visit(BlockNode node);

    void visit(FunctionCallNode node);

    void visit(IfStatementNode node);

    void visit(LoopStatementNode node);

    void visit(ReturnStatementNode node);

    void visit(WhileStatementNode node);

    // ExpressionNode

    void visit(BinaryExpressionNode node);

    void visit(CharacterValExpressionNode node);

    void visit(LiteralNode node);

    void visit(VariableReferenceNode node);

    void visit(NewExpressionNode node);

    void visit(UnaryExpressionNode node);




    // Tupe

    void visit(TypeNode node);
    // Other

    void visit(ProgramNode node);

    void visit(ParameterNode node);








}
