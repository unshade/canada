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
import org.trad.pcl.ast.type.AccessTypeNode;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.TypeNode;

public interface ASTNodeVisitor {

    // DeclarationNode
    void visit(FunctionDeclarationNode node) throws Exception;

    void visit(ProcedureDeclarationNode node) throws Exception;

    void visit(TypeDeclarationNode node) throws Exception;

    void visit(RecordTypeNode node) throws Exception;

    void visit(AccessTypeNode node);

    void visit(VariableDeclarationNode node) throws Exception;

    // StatementNode

    void visit(AssignmentStatementNode node) throws Exception;

    void visit(BlockNode node);

    void visit(FunctionCallNode node);

    void visit(IfStatementNode node);

    void visit(LoopStatementNode node);

    void visit(ReturnStatementNode node) throws Exception;

    void visit(WhileStatementNode node) throws Exception;

    // ExpressionNode

    void visit(BinaryExpressionNode node);

    void visit(CharacterValExpressionNode node) throws Exception;

    void visit(LiteralNode node);

    void visit(VariableReferenceNode node) throws Exception;

    void visit(NewExpressionNode node);

    void visit(UnaryExpressionNode node) throws Exception;




    // Tupe

    void visit(TypeNode node);
    // Other

    void visit(ProgramNode node);

    void visit(ParameterNode node) throws Exception;








}
