package AstTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.expression.LiteralNode;
import org.trad.pcl.ast.type.AccessTypeNode;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.TypeNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class DeclarationTest {

    private Lexer lexer;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        lexer = mock(Lexer.class);
        parser = new Parser(lexer);
        ErrorService.resetInstance();
    }

    @Test
    public void testProcedureDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.PROCEDURE, 1,"procedure"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.BEGIN, 1,"begin"),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.END, 1,"end"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(ProcedureDeclarationNode.class, declarations.get(0));

        ProcedureDeclarationNode procedureDeclarationNode = (ProcedureDeclarationNode) declarations.get(0);

        assertEquals("test", procedureDeclarationNode.getIdentifier());

        assertEquals(0, procedureDeclarationNode.getParameters().size());

        assertNotNull(procedureDeclarationNode.getBody());
    }

    @Test
    public void testProcedureDeclarationWithParameters() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.PROCEDURE, 1,"procedure"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.OPEN_PAREN, 1,"("),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"),
                new Token(Tag.IDENT, 1,"b"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.CLOSE_PAREN, 1,")"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.BEGIN, 1,"begin"),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.END, 1,"end"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(ProcedureDeclarationNode.class, declarations.get(0));

        ProcedureDeclarationNode procedureDeclarationNode = (ProcedureDeclarationNode) declarations.get(0);

        assertEquals("test", procedureDeclarationNode.getIdentifier());

        assertNotNull(procedureDeclarationNode.getBody());

        assertEquals(2, procedureDeclarationNode.getParameters().size());
    }

    @Test
    public void testFunctionDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.FUNCTION, 1,"function"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.RETURN, 1,"return"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.BEGIN, 1,"begin"),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.END, 1,"end"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(FunctionDeclarationNode.class, declarations.get(0));

        FunctionDeclarationNode functionDeclarationNode = (FunctionDeclarationNode) declarations.get(0);

        assertEquals("test", functionDeclarationNode.getIdentifier());

        assertNotNull(functionDeclarationNode.getBody());

        assertNotNull(functionDeclarationNode.getReturnType());

        assertEquals(0, functionDeclarationNode.getParameters().size());
    }

    @Test
    public void testFunctionDeclarationWithParameters() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.FUNCTION, 1,"function"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.OPEN_PAREN, 1,"("),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"),
                new Token(Tag.IDENT, 1,"b"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.CLOSE_PAREN, 1,")"),
                new Token(Tag.RETURN, 1,"return"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.BEGIN, 1,"begin"),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.END, 1,"end"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(FunctionDeclarationNode.class, declarations.get(0));

        FunctionDeclarationNode functionDeclarationNode = (FunctionDeclarationNode) declarations.get(0);

        assertEquals("test", functionDeclarationNode.getIdentifier());

        assertNotNull(functionDeclarationNode.getBody());

        assertNotNull(functionDeclarationNode.getReturnType());

        assertEquals(2, functionDeclarationNode.getParameters().size());
    }

    @Test
    public void testVariableDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1,"var"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(0));

        VariableDeclarationNode declarationNode = (VariableDeclarationNode) declarations.get(0);

        assertEquals("var", declarationNode.getIdentifier());

        assertNotNull(declarationNode.getType());

        assertInstanceOf(TypeNode.class, declarationNode.getType());

        assertNull(declarationNode.getAssignment());
    }

    @Test
    public void testVariableDeclarationWithAssignment() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1,"var"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(0));

        VariableDeclarationNode declarationNode = (VariableDeclarationNode) declarations.get(0);

        assertEquals("var", declarationNode.getIdentifier());

        assertNotNull(declarationNode.getType());

        assertInstanceOf(TypeNode.class, declarationNode.getType());

        assertNotNull(declarationNode.getAssignment());

        assertInstanceOf(LiteralNode.class, declarationNode.getAssignment().getExpression());

        assertEquals(Long.parseLong("1"), ((LiteralNode) declarationNode.getAssignment().getExpression()).getValue());
    }

    @Test
    public void testMultipleVariableDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1,"var"),
                new Token(Tag.COMMA, 1,","),
                new Token(Tag.IDENT, 1,"var2"),
                new Token(Tag.COMMA, 1,","),
                new Token(Tag.IDENT, 1,"var3"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        System.out.println(declarations);
        assertEquals(3, declarations.size());
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(0));
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(1));
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(2));

        VariableDeclarationNode v1 = (VariableDeclarationNode) declarations.get(0);
        VariableDeclarationNode v2 = (VariableDeclarationNode) declarations.get(1);
        VariableDeclarationNode v3 = (VariableDeclarationNode) declarations.get(2);

        assertEquals("var", v1.getIdentifier());
        assertEquals("var2", v2.getIdentifier());
        assertEquals("var3", v3.getIdentifier());

        assertNotNull(v1.getType());
        assertNotNull(v2.getType());
        assertNotNull(v3.getType());

        assertInstanceOf(TypeNode.class, v1.getType());
        assertInstanceOf(TypeNode.class, v2.getType());
        assertInstanceOf(TypeNode.class, v3.getType());

        assertEquals("integer", ((TypeNode) v1.getType()).getIdentifier());
        assertEquals(v1.getType(), v2.getType());
        assertEquals(v1.getType(), v3.getType());

        assertNull(v1.getAssignment());
        assertNull(v2.getAssignment());
        assertNull(v3.getAssignment());
    }

    @Test
    public void testMultipleVariableDeclarationWithAssignment() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.IDENT, 1,"var"),
                new Token(Tag.COMMA, 1,","),
                new Token(Tag.IDENT, 1,"var2"),
                new Token(Tag.COMMA, 1,","),
                new Token(Tag.IDENT, 1,"var3"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.ASSIGN, 1,":="),
                new Token(Tag.ENTIER, 1,"1"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);

        assertEquals(3, declarations.size());

        assertInstanceOf(VariableDeclarationNode.class, declarations.get(0));
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(1));
        assertInstanceOf(VariableDeclarationNode.class, declarations.get(2));

        VariableDeclarationNode v1 = (VariableDeclarationNode) declarations.get(0);
        VariableDeclarationNode v2 = (VariableDeclarationNode) declarations.get(1);
        VariableDeclarationNode v3 = (VariableDeclarationNode) declarations.get(2);

        assertEquals("var", v1.getIdentifier());
        assertEquals("var2", v2.getIdentifier());
        assertEquals("var3", v3.getIdentifier());

        assertNotNull(v1.getType());
        assertNotNull(v2.getType());
        assertNotNull(v3.getType());

        assertNotNull(v1.getAssignment());
        assertNotNull(v2.getAssignment());
        assertNotNull(v3.getAssignment());

        assertEquals(Long.parseLong("1"), ((LiteralNode) v1.getAssignment().getExpression()).getValue());
        assertEquals("var", v1.getAssignment().getIdentifier().getIdentifier());

        assertEquals(Long.parseLong("1"), ((LiteralNode) v2.getAssignment().getExpression()).getValue());
        assertEquals("var2", v2.getAssignment().getIdentifier().getIdentifier());

        assertEquals(Long.parseLong("1"), ((LiteralNode) v3.getAssignment().getExpression()).getValue());
        assertEquals("var3", v3.getAssignment().getIdentifier().getIdentifier());

    }

    @Test
    public void testTypeDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.TYPE, 1,"type"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(TypeDeclarationNode.class, declarations.get(0));

        TypeDeclarationNode typeDeclarationNode = (TypeDeclarationNode) declarations.get(0);

        assertEquals("test", typeDeclarationNode.getIdentifier());

        assertNotNull(typeDeclarationNode.getType());
        assertInstanceOf(TypeNode.class, typeDeclarationNode.getType());

        assertEquals("test", typeDeclarationNode.getType().getIdentifier());
    }

    @Test
    public void testTypeAccessDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.TYPE, 1,"type"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.ACCESS, 1,"access"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(TypeDeclarationNode.class, declarations.get(0));

        TypeDeclarationNode typeDeclarationNode = (TypeDeclarationNode) declarations.get(0);

        assertEquals("test", typeDeclarationNode.getIdentifier());

        assertNotNull(typeDeclarationNode.getType());
        assertInstanceOf(AccessTypeNode.class, typeDeclarationNode.getType());

        assertEquals("test", typeDeclarationNode.getType().getIdentifier());
        assertEquals("integer", ((AccessTypeNode) typeDeclarationNode.getType()).getBaseType().getIdentifier());
    }

    @Test
    public void testTypeRecordDeclaration() {
        when(lexer.nextToken()).thenReturn(new Token(Tag.TYPE, 1,"type"),
                new Token(Tag.IDENT, 1,"test"),
                new Token(Tag.IS, 1,"is"),
                new Token(Tag.RECORD, 1,"record"),
                new Token(Tag.IDENT, 1,"a"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.SEMICOLON, 1,";"),
                new Token(Tag.IDENT, 1,"b"),
                new Token(Tag.COLON, 1,":"),
                new Token(Tag.IDENT, 1,"integer"),
                new Token(Tag.END, 1,"end"),
                new Token(Tag.SEMICOLON, 1,";"));

        this.parser.setCurrentToken(lexer.nextToken());

        List<DeclarationNode> declarations = parser.declaration();

        assertNotNull(declarations);
        assertEquals(1, declarations.size());
        assertInstanceOf(TypeDeclarationNode.class, declarations.get(0));

        TypeDeclarationNode typeDeclarationNode = (TypeDeclarationNode) declarations.get(0);

        assertEquals("test", typeDeclarationNode.getIdentifier());

        assertNotNull(typeDeclarationNode.getType());
        assertInstanceOf(RecordTypeNode.class, typeDeclarationNode.getType());

        assertEquals("test", typeDeclarationNode.getType().getIdentifier());
        assertEquals(2, ((RecordTypeNode) typeDeclarationNode.getType()).getFields().size());
    }



}
