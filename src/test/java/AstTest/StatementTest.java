package AstTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trad.pcl.Helpers.OperatorEnum;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.expression.BinaryExpressionNode;
import org.trad.pcl.ast.expression.CallNode;
import org.trad.pcl.ast.expression.VariableReferenceNode;
import org.trad.pcl.ast.statement.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class StatementTest {

    private Lexer lexer;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        lexer = mock(Lexer.class);
        parser = new Parser(lexer);
        ErrorService.resetInstance();
    }

    @Test
    public void testCallFunction() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1, "foo"), new Token(Tag.SEMICOLON, 1, ";"));
        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode callFunctionNode = parser.statement();

        assertNotNull(callFunctionNode);

        assertInstanceOf(CallNode.class, callFunctionNode);

        CallNode callFunction = (CallNode) callFunctionNode;

        assertEquals("foo", callFunction.getIdentifier());

        assertNull(callFunction.getNextExpression());

        assertNull(callFunction.getArguments());

    }

    @Test
    public void testCallFunctionWithArguments() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.OPEN_PAREN, 1, "("),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.COMMA, 1, ","),
                new Token(Tag.IDENT, 1, "baz"),
                new Token(Tag.CLOSE_PAREN, 1, ")"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode callFunctionNode = parser.statement();

        assertNotNull(callFunctionNode);

        assertInstanceOf(CallNode.class, callFunctionNode);

        CallNode callFunction = (CallNode) callFunctionNode;

        assertEquals("foo", callFunction.getIdentifier());

        assertNotNull(callFunction.getArguments());

        assertEquals(2, callFunction.getArguments().size());

        assertInstanceOf(VariableReferenceNode.class, callFunction.getArguments().get(0));
        assertInstanceOf(VariableReferenceNode.class, callFunction.getArguments().get(1));

        VariableReferenceNode arg1 = (VariableReferenceNode) callFunction.getArguments().get(0);
        VariableReferenceNode arg2 = (VariableReferenceNode) callFunction.getArguments().get(1);

        assertEquals("bar", arg1.getIdentifier());
        assertEquals("baz", arg2.getIdentifier());

    }

    @Test
    public void testAssignmentStatement() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode assignmentStatementNode = parser.statement();

        assertNotNull(assignmentStatementNode);

        assertInstanceOf(AssignmentStatementNode.class, assignmentStatementNode);

        AssignmentStatementNode assignmentStatement = (AssignmentStatementNode) assignmentStatementNode;

        assertEquals("foo", assignmentStatement.getVariableReference().getIdentifier());

        assertNull(assignmentStatement.getVariableReference().getNextExpression());

        assertNotNull(assignmentStatement.getExpression());

        assertInstanceOf(VariableReferenceNode.class, assignmentStatement.getExpression());

        VariableReferenceNode expression = (VariableReferenceNode) assignmentStatement.getExpression();

        assertEquals("bar", expression.getIdentifier());

    }

    @Test
    public void testMultipleIdentStatement() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.DOT, 1, "."),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.DOT, 1, "."),
                new Token(Tag.IDENT, 1, "baz"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.ENTIER, 1, "1"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode multipleIdentStatementNode = parser.statement();

        assertNotNull(multipleIdentStatementNode);

        assertInstanceOf(AssignmentStatementNode.class, multipleIdentStatementNode);

        AssignmentStatementNode assignStatement = (AssignmentStatementNode) multipleIdentStatementNode;

        VariableReferenceNode ident = assignStatement.getVariableReference().getNextExpression();

        assertEquals("foo", ident.getIdentifier());

        assertNotNull(ident.getNextExpression());

        assertInstanceOf(VariableReferenceNode.class, ident.getNextExpression());

        VariableReferenceNode ident2 = (VariableReferenceNode) ident.getNextExpression();

        assertEquals("bar", ident2.getIdentifier());

        assertNotNull(ident2.getNextExpression());

        assertInstanceOf(VariableReferenceNode.class, ident2.getNextExpression());

        VariableReferenceNode ident3 = (VariableReferenceNode) ident2.getNextExpression();

        assertEquals("baz", ident3.getIdentifier());

    }

    @Test
    public void testReturnStatement() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.RETURN, 1, "return"),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode returnStatementNode = parser.statement();

        assertNotNull(returnStatementNode);

        assertInstanceOf(ReturnStatementNode.class, returnStatementNode);

        ReturnStatementNode returnStatement = (ReturnStatementNode) returnStatementNode;

        assertNotNull(returnStatement.getExpression());

        assertInstanceOf(VariableReferenceNode.class, returnStatement.getExpression());

        VariableReferenceNode expression = (VariableReferenceNode) returnStatement.getExpression();

        assertEquals("foo", expression.getIdentifier());

    }

    @Test
    public void testReturnStatementWithoutExpression() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.RETURN, 1, "return"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode returnStatementNode = parser.statement();

        assertNotNull(returnStatementNode);

        assertInstanceOf(ReturnStatementNode.class, returnStatementNode);

        ReturnStatementNode returnStatement = (ReturnStatementNode) returnStatementNode;

        assertNull(returnStatement.getExpression());

    }

    @Test
    public void testIfStatement() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IF, 1, "if"),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.EQ, 1, "="),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.THEN, 1, "then"),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.SEMICOLON, 1, ";"),
                new Token(Tag.END, 1, "end"),
                new Token(Tag.IF, 1, "if"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode ifStatementNode = parser.statement();

        assertNotNull(ifStatementNode);

        assertInstanceOf(IfStatementNode.class, ifStatementNode);

        IfStatementNode ifStatement = (IfStatementNode) ifStatementNode;

        assertNotNull(ifStatement.getCondition());

        assertInstanceOf(BinaryExpressionNode.class, ifStatement.getCondition());

        BinaryExpressionNode condition = (BinaryExpressionNode) ifStatement.getCondition();

        assertEquals(OperatorEnum.EQUALS, condition.getOperatorNode().getOperator());

        assertInstanceOf(VariableReferenceNode.class, condition.getLeft());
        assertInstanceOf(AssignmentStatementNode.class, ifStatement.getThenBranch().getStatements().get(0));

        assertNull(ifStatement.getElseBranch());
        assertNull(ifStatement.getElseIfBranch());

    }

    @Test
    public void testIfStatementWithMultipleElseIf() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IF, 1, "if"),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.EQ, 1, "="),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.THEN, 1, "then"),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.SEMICOLON, 1, ";"),
                new Token(Tag.ELSIF, 1, "elsif"),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.EQ, 1, "="),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.THEN, 1, "then"),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.SEMICOLON, 1, ";"),
                new Token(Tag.ELSIF, 1, "elsif"),
                new Token(Tag.IDENT, 1, "foo1"),
                new Token(Tag.EQ, 1, "="),
                new Token(Tag.IDENT, 1, "bar1"),
                new Token(Tag.THEN, 1, "then"),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.SEMICOLON, 1, ";"),
                new Token(Tag.END, 1, "end"),
                new Token(Tag.IF, 1, "if"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode ifStatementNode = parser.statement();

        assertNotNull(ifStatementNode);

        assertInstanceOf(IfStatementNode.class, ifStatementNode);

        IfStatementNode ifStatement = (IfStatementNode) ifStatementNode;
        assertNotNull(ifStatement.getCondition());
        assertNotNull(ifStatement.getThenBranch());
        assertNotNull(ifStatement.getElseIfBranch());
        assertNull(ifStatement.getElseBranch());

        assertNotNull(ifStatement.getElseIfBranch().getCondition());
        assertNotNull(ifStatement.getElseIfBranch().getThenBranch());
        assertNotNull(ifStatement.getElseIfBranch().getElseIfBranch());
        assertNull(ifStatement.getElseIfBranch().getElseBranch());

    }

    @Test
    public void testForStatement() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.FOR, 1, "for"),
                new Token(Tag.IDENT, 1, "foo"),
                new Token(Tag.IN, 1, "in"),
                new Token(Tag.IDENT, 1, "bar"),
                new Token(Tag.DOTDOT, 1, ".."),
                new Token(Tag.IDENT, 1, "baz"),
                new Token(Tag.LOOP, 1, "loop"),
                new Token(Tag.IDENT, 1, "bar1"),
                new Token(Tag.ASSIGN, 1, ":="),
                new Token(Tag.IDENT, 1, "foo1"),
                new Token(Tag.SEMICOLON, 1, ";"),
                new Token(Tag.END, 1, "end"),
                new Token(Tag.LOOP, 1, "loop"),
                new Token(Tag.SEMICOLON, 1, ";"));

        this.parser.setCurrentToken(lexer.nextToken());

        StatementNode forStatementNode = parser.statement();

        assertNotNull(forStatementNode);
        assertInstanceOf(LoopStatementNode.class, forStatementNode);

        LoopStatementNode forStatement = (LoopStatementNode) forStatementNode;

        assertNotNull(forStatement.getIdentifier());
        assertEquals("foo", forStatement.getIdentifier());

        assertNotNull(forStatement.getStartExpression());
        assertInstanceOf(VariableReferenceNode.class, forStatement.getStartExpression());

        assertNotNull(forStatement.getEndExpression());
        assertInstanceOf(VariableReferenceNode.class, forStatement.getEndExpression());

        assertNotNull(forStatement.getBody());
        assertInstanceOf(BlockNode.class, forStatement.getBody());

        assertEquals(1, forStatement.getBody().getStatements().size());
        assertInstanceOf(AssignmentStatementNode.class, forStatement.getBody().getStatements().get(0));
    }


}
