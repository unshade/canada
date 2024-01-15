package org.trad.pcl.Parser;

import org.trad.pcl.Exceptions.Syntax.MissingSemicolonException;
import org.trad.pcl.Exceptions.Syntax.UnexpectedTokenException;
import org.trad.pcl.Helpers.TagHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.annotation.PrintMethodName;
import org.trad.pcl.ast.AccessReferenceNode;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.DeclarationNode;
import org.trad.pcl.ast.declaration.FunctionDeclarationNode;
import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.ast.declaration.TypeDeclarationNode;
import org.trad.pcl.ast.expression.ExpressionNode;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.AccessTypeNode;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.SimpleTypeNode;
import org.trad.pcl.ast.type.TypeNode;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Parser instance;
    private final ErrorService errorService;
    Lexer lexer;
    private Token currentToken;

    private Parser() {
        this.lexer = Lexer.getInstance();
        this.errorService = ErrorService.getInstance();
        this.currentToken = lexer.nextToken();
    }

    public static Parser getInstance() {
        if (!(instance == null)) {
            return instance;
        }
        instance = new Parser();
        return instance;
    }

    public ProgramNode parse() {
        return fichier();
    }

    @PrintMethodName
    private ProgramNode fichier() {
        ProgramNode abstractSyntaxTreeRoot = new ProgramNode();

        analyseTerminal(Tag.WITH);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.DOT);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.DOT);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.PROCEDURE);
        ProcedureDeclarationNode rootProcedure = new ProcedureDeclarationNode();
        rootProcedure.setName(analyseTerminal(Tag.IDENT).getValue());
        analyseTerminal(Tag.IS);
        BlockNode rootProcedureBody = new BlockNode();
        rootProcedureBody.addDeclarations(multipleDeclarations());
        analyseTerminal(Tag.BEGIN);
        rootProcedureBody.addStatements(multipleStatements());
        rootProcedure.setBody(rootProcedureBody);
        abstractSyntaxTreeRoot.setRootProcedure(rootProcedure);
        analyseTerminal(Tag.END);
        hasident();
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.EOF);

        return abstractSyntaxTreeRoot;
    }

    /**
     * Grammar rule : decl
     */
    @PrintMethodName
    private List<DeclarationNode> declaration() {
        List<DeclarationNode> declarations = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case PROCEDURE -> {
                ProcedureDeclarationNode declaration = new ProcedureDeclarationNode();
                declarations.add(declaration);
                analyseTerminal(Tag.PROCEDURE);
                declaration.setName(analyseTerminal(Tag.IDENT).getValue());
                declaration.addParameters(hasParameters());
                analyseTerminal(Tag.IS);
                BlockNode procedureBody = new BlockNode();
                procedureBody.addDeclarations(multipleDeclarations());
                analyseTerminal(Tag.BEGIN);
                procedureBody.addStatements(multipleStatements());
                declaration.setBody(procedureBody);
                analyseTerminal(Tag.END);
                hasident();
                analyseTerminal(Tag.SEMICOLON);
            }
            case IDENT -> {
                //declaration.setName(this.currentToken.getValue());
                List<TypeDeclarationNode> typeNodes = identsep();
                analyseTerminal(Tag.COLON);
                TypeNode typeNode = type_n();
                for (TypeDeclarationNode typeDeclarationNode : typeNodes) {
                    typeDeclarationNode.setType(typeNode);
                    declarations.add(typeDeclarationNode);
                }
                // TODO : typexpr();
                declarationExpression();
                analyseTerminal(Tag.SEMICOLON);
            }
            case TYPE -> {
                TypeDeclarationNode declaration = new TypeDeclarationNode();
                declarations.add(declaration);
                analyseTerminal(Tag.TYPE);
                declaration.setName(analyseTerminal(Tag.IDENT).getValue());
                declaration.setType(isAccessOrRecord());
                analyseTerminal(Tag.SEMICOLON);
            }
            case FUNCTION -> {
                FunctionDeclarationNode declaration = new FunctionDeclarationNode();
                declarations.add(declaration);
                analyseTerminal(Tag.FUNCTION);
                declaration.setName(analyseTerminal(Tag.IDENT).getValue());
                declaration.addParameters(hasParameters());
                analyseTerminal(Tag.RETURN);
                declaration.setReturnType(type_n());
                analyseTerminal(Tag.IS);
                BlockNode functionBody = new BlockNode();
                functionBody.addDeclarations(multipleDeclarations());
                analyseTerminal(Tag.BEGIN);
                functionBody.addStatements(multipleStatements());
                declaration.setBody(functionBody);
                analyseTerminal(Tag.END);
                hasident();
                analyseTerminal(Tag.SEMICOLON);
            }
            default -> {
                declarations = null;
            }
        }
        return declarations;
    }

    /**
     * Grammar rule : hasischoose
     */
    @PrintMethodName
    private TypeNode isAccessOrRecord() {
        TypeNode type = null;
        switch (this.currentToken.tag()) {
            case IS -> {
                analyseTerminal(Tag.IS);
                type = AccessOrRecord();
            }
            case SEMICOLON -> {
                type = new SimpleTypeNode();
            }
        }
        return type;
    }


    /**
     * Grammar rule : accorrec
     */
    @PrintMethodName
    private TypeNode AccessOrRecord() {
        TypeNode type = null;
        switch (this.currentToken.tag()) {
            case ACCESS -> {
                type = new AccessTypeNode();
                analyseTerminal(Tag.ACCESS);
                SimpleTypeNode simpleTypeNode = new SimpleTypeNode();
                simpleTypeNode.setTypeName(analyseTerminal(Tag.IDENT).getValue());
                ((AccessTypeNode) type).setBaseType(simpleTypeNode);
            }
            case RECORD -> {
                type = new RecordTypeNode();
                analyseTerminal(Tag.RECORD);
                // TODO : ((RecordTypeNode) type).addFields(champs());
                champs();
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.RECORD);
            }
        }
        return type;
    }

    /**
     * Grammar rule : decls
     */
    @PrintMethodName
    private List<DeclarationNode> multipleDeclarations() {
        List<DeclarationNode> declarations = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case PROCEDURE, IDENT, TYPE, FUNCTION -> {
                declarations.addAll(declaration());
                declarations.addAll(multipleDeclarations());
            }
            case BEGIN -> {
            }
        }
        return declarations;
    }
    @PrintMethodName
    private void hasident() {
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case IDENT -> analyseTerminal(Tag.IDENT);
        }
    }

    @PrintMethodName
    private List<TypeDeclarationNode> identsep() {
        List<TypeDeclarationNode> declarations = new ArrayList<>();
        if (this.currentToken.tag() == Tag.IDENT) {
            TypeDeclarationNode declaration = new TypeDeclarationNode();
            declarations.add(declaration);
            declaration.setName(analyseTerminal(Tag.IDENT).getValue());
            declarations.addAll(identsep2());
        }
        return declarations;
    }
    @PrintMethodName
    private List<TypeDeclarationNode> identsep2() {
        List<TypeDeclarationNode> declarations = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case COLON -> {
            }
            case COMMA -> {
                analyseTerminal(Tag.COMMA);
                declarations.addAll(identsep());
            }
        }
        return declarations;
    }
    @PrintMethodName
    private void champ() {
        if (this.currentToken.tag() == Tag.IDENT) {
            identsep();
            analyseTerminal(Tag.COLON);
            type_n();
            analyseTerminal(Tag.SEMICOLON);
        }
    }
    @PrintMethodName
    private void champs() {
        if (this.currentToken.tag() == Tag.IDENT) {
            champ();
            champs2();
        }
    }
    @PrintMethodName
    private void champs2() {
        switch (this.currentToken.tag()) {
            case IDENT -> champs();
            case END -> {
            }
        }
    }
    @PrintMethodName
    private TypeNode type_n() {
        TypeNode type = null;
        switch (this.currentToken.tag()) {
            case ACCESS -> {
                analyseTerminal(Tag.ACCESS);
                analyseTerminal(Tag.IDENT);
            }
            case IDENT -> {
                type = new SimpleTypeNode();
                ((SimpleTypeNode) type).setTypeName(currentToken.getValue());
                analyseTerminal(Tag.IDENT);
            }
        }
        return type;
    }

    /**
     * Grammar rule : params
     */
    @PrintMethodName
    private List<ParameterNode> multipleParameters() {
        List<ParameterNode> parameters = new ArrayList<>();
        if (this.currentToken.tag() == Tag.OPEN_PAREN) {
            analyseTerminal(Tag.OPEN_PAREN);
            parameters.addAll(paramsep());
            analyseTerminal(Tag.CLOSE_PAREN);
        }
        return parameters;
    }

    /**
     * Grammar rule : hasparams
     */
    @PrintMethodName
    private List<ParameterNode> hasParameters() {
        List<ParameterNode> parameters = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IS, RETURN -> {
            }
            case OPEN_PAREN -> {
                parameters.addAll(multipleParameters());
            }
        }
        return parameters;
    }

    @PrintMethodName
    private List<ParameterNode> paramsep() {
        List<ParameterNode> parameters = new ArrayList<>();
        if (this.currentToken.tag() == Tag.IDENT) {
            parameters.add(parameter());
            parameters.addAll(paramsep2());
        }
        return parameters;
    }
    @PrintMethodName
    private List<ParameterNode> paramsep2() {
        List<ParameterNode> parameters = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
                analyseTerminal(Tag.SEMICOLON);
                parameters.addAll(paramsep());
            }
            case CLOSE_PAREN -> {
            }
        }
        return parameters;
    }

    /**
     * Grammar rule : typexpr
     */
    @PrintMethodName
    private ExpressionNode declarationExpression() {
        ExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case ASSIGN -> {
                analyseTerminal(Tag.ASSIGN);
                expression = expr();
            }
            case SEMICOLON -> {
            }
        }
        return expression;
    }

    /**
     * Grammar rule : param
     */
    @PrintMethodName
    private ParameterNode parameter() {
        ParameterNode parameter = null;
        if (this.currentToken.tag() == Tag.IDENT) {
            parameter = new ParameterNode();
            parameter.setName(currentToken.getValue());
            identsep();
            analyseTerminal(Tag.COLON);
            mode();
            parameter.setType(type_n());
        }
        return parameter;
    }
    @PrintMethodName
    private void mode() {
        switch (this.currentToken.tag()) {
            case IDENT, ACCESS -> {
            }
            case IN -> {
                analyseTerminal(Tag.IN);
                modeout();
            }

        }
    }
    @PrintMethodName
    private void modeout() {
        switch (this.currentToken.tag()) {
            case IDENT, ACCESS -> {
            }
            case OUT -> {
                analyseTerminal(Tag.OUT);
            }
        }
    }
    @PrintMethodName
    private ExpressionNode expr() {
        // TODO : expr
        ExpressionNode expression = new ExpressionNode();
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                expression = or_expr();
            }

        }
        return expression;
    }
    @PrintMethodName
    private ExpressionNode or_expr() {
        // TODO : or_expr
        ExpressionNode expression = new ExpressionNode();
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                and_expr();
                or_expr2();
            }
        }
        return expression;
    }
    @PrintMethodName
    private void or_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, THEN, DOTDOT, LOOP -> {
            }
            case OR -> {
                analyseTerminal(Tag.OR);
                or_expr3();
            }
        }
    }
    @PrintMethodName
    private void or_expr3() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, ELSE, DOT, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                and_expr();
                or_expr2();
            }
        }
    }
    @PrintMethodName
    private void and_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                not_expr();
                and_expr2();
            }

        }
    }
    @PrintMethodName
    private void and_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, THEN, DOTDOT, LOOP -> {
            }
            case AND -> {
                analyseTerminal(Tag.AND);
                and_expr3();
            }
        }
    }
    @PrintMethodName
    private void and_expr3() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                not_expr();
                and_expr2();
            }
            case THEN -> {
                analyseTerminal(Tag.THEN);
                not_expr();
                and_expr2();
            }
        }
    }
    @PrintMethodName
    private void not_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                equality_expr();
                not_expr2();
            }
        }
    }
    @PrintMethodName
    private void not_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, DOTDOT, LOOP -> {
            }
            case NOT -> {
                analyseTerminal(Tag.NOT);
                equality_expr();
                not_expr2();
            }
        }
    }
    @PrintMethodName
    private void equality_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                relational_expr();
                equality_expr2();
            }
        }
    }
    @PrintMethodName
    private void equality_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, DOTDOT, LOOP -> {
            }
            case EQ -> {
                analyseTerminal(Tag.EQ);
                relational_expr();
                equality_expr2();
            }
            case NE -> {
                analyseTerminal(Tag.NE);
                relational_expr();
                equality_expr2();
            }
        }
    }
    @PrintMethodName
    private void relational_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                additive_expr();
                relational_expr2();
            }
        }
    }
    @PrintMethodName
    private void relational_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, DOTDOT, LOOP -> {
            }
            case LT -> {
                analyseTerminal(Tag.LT);
                additive_expr();
                relational_expr2();
            }
            case LE -> {
                analyseTerminal(Tag.LE);
                additive_expr();
                relational_expr2();
            }
            case GT -> {
                analyseTerminal(Tag.GT);
                additive_expr();
                relational_expr2();
            }
            case GE -> {
                analyseTerminal(Tag.GE);
                additive_expr();
                relational_expr2();
            }
        }
    }
    @PrintMethodName
    private void additive_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                multiplicative_expr();
                additive_expr2();
            }
        }
    }
    @PrintMethodName
    private void additive_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, DOTDOT, LOOP -> {
            }
            case PLUS -> {
                analyseTerminal(Tag.PLUS);
                multiplicative_expr();
                additive_expr2();
            }
            case MINUS -> {
                analyseTerminal(Tag.MINUS);
                multiplicative_expr();
                additive_expr2();
            }
        }
    }
    @PrintMethodName
    private void multiplicative_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                unary_expr();
                multiplicative_expr2();
            }
        }
    }
    @PrintMethodName
    private void multiplicative_expr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, DOTDOT, LOOP -> {
            }
            case MULTI -> {
                analyseTerminal(Tag.MULTI);
                unary_expr();
                multiplicative_expr2();
            }
            case DIV -> {
                analyseTerminal(Tag.DIV);
                unary_expr();
                multiplicative_expr2();
            }
            case REM -> {
                analyseTerminal(Tag.REM);
                unary_expr();
                multiplicative_expr2();
            }
        }
    }
    @PrintMethodName
    private void unary_expr() {
        switch (this.currentToken.tag()) {
            case MINUS -> {
                analyseTerminal(Tag.MINUS);
                primary();
            }
            case IDENT, OPEN_PAREN, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                primary();
            }
        }
    }
    @PrintMethodName
    private void primary() {
        switch (this.currentToken.tag()) {
            case IDENT -> {
                analyseTerminal(Tag.IDENT);
                primary2();
            }
            case OPEN_PAREN -> {
                analyseTerminal(Tag.OPEN_PAREN);
                expr();
                analyseTerminal(Tag.CLOSE_PAREN);
            }
            case ENTIER -> {
                analyseTerminal(Tag.ENTIER);
            }
            case CARACTERE -> {
                analyseTerminal(Tag.CARACTERE);
            }
            case TRUE -> {
                analyseTerminal(Tag.TRUE);
            }
            case FALSE -> {
                analyseTerminal(Tag.FALSE);
            }
            case NULL -> {
                analyseTerminal(Tag.NULL);
            }
            case NEW -> {
                analyseTerminal(Tag.NEW);
                analyseTerminal(Tag.IDENT);
            }
            case CHARACTER -> {
                analyseTerminal(Tag.CHARACTER);
                analyseTerminal(Tag.APOSTROPHE);
                analyseTerminal(Tag.VAL);
                analyseTerminal(Tag.OPEN_PAREN);
                expr();
                analyseTerminal(Tag.CLOSE_PAREN);
            }

        }
    }
    @PrintMethodName
    private void primary2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, MULTI, DIV, REM, DOTDOT, LOOP, DOT -> {
                acces();
            }
            case OPEN_PAREN -> {
                analyseTerminal(Tag.OPEN_PAREN);
                exprsep();
                analyseTerminal(Tag.CLOSE_PAREN);
                acces();
            }
        }
    }
    @PrintMethodName
    private void exprsep() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                expr();
                exprsep2();
            }
        }
    }
    @PrintMethodName
    private void exprsep2() {
        switch (this.currentToken.tag()) {
            case COMMA -> {
                analyseTerminal(Tag.COMMA);
                exprsep();
            }
            case CLOSE_PAREN -> {
            }
        }
    }
    @PrintMethodName
    private ExpressionNode hasexpr() {
        ExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                expression = expr();
            }
            case ASSIGN, DOT -> {
                exprsep();
            }
        }
        return expression;
    }

    /**
     * Grammar rule : instr
     */
    @PrintMethodName
    private StatementNode statement() {
        StatementNode statement;
        switch (this.currentToken.tag()) {
            case IDENT -> {
                // appel de fonction et Assign
                String ident = analyseTerminal(Tag.IDENT).getValue();
                statement = identifiableStatement();
                ((IdentifiableStatementNode) statement).setIdentifier(ident);
            }
            case BEGIN -> {
                statement = new BlockNode();
                analyseTerminal(Tag.BEGIN);
                ((BlockNode) statement).addStatements(multipleStatements());
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.SEMICOLON);
            }
            case RETURN -> {
                statement = new ReturnStatementNode();
                analyseTerminal(Tag.RETURN);
                // TODO
                hasexpr();
                analyseTerminal(Tag.SEMICOLON);
            }
            case IF -> {
                statement = new IfStatementNode();
                analyseTerminal(Tag.IF);
                expr();
                analyseTerminal(Tag.THEN);
                BlockNode thenBranch = new BlockNode();
                thenBranch.addStatements(multipleStatements());
                ((IfStatementNode) statement).setThenBranch(thenBranch);
                // TODO
                elifn();
                BlockNode elseBranch = new BlockNode();
                elseBranch.addStatements(elsen());
                ((IfStatementNode) statement).setElseBranch(elseBranch);
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.IF);
                analyseTerminal(Tag.SEMICOLON);
            }
            // For var in reverse expr .. expr loop  end loop;
            case FOR -> {
                statement = new LoopStatementNode();
                analyseTerminal(Tag.FOR);
                analyseTerminal(Tag.IDENT);
                analyseTerminal(Tag.IN);
                ((LoopStatementNode) statement).setReverse(hasreverse());
                ((LoopStatementNode) statement).setStartExpression(expr());
                analyseTerminal(Tag.DOTDOT);
                ((LoopStatementNode) statement).setEndExpression(expr());
                analyseTerminal(Tag.LOOP);
                BlockNode loopBody = new BlockNode();
                loopBody.addStatements(multipleStatements());
                ((LoopStatementNode) statement).setBody(loopBody);
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.LOOP);
                analyseTerminal(Tag.SEMICOLON);
            }
            case WHILE -> {
                statement = new WhileStatementNode();
                analyseTerminal(Tag.WHILE);
                ((WhileStatementNode) statement).setCondition(expr());
                analyseTerminal(Tag.LOOP);
                BlockNode loopBody = new BlockNode();
                loopBody.addStatements(multipleStatements());
                ((WhileStatementNode) statement).setBody(loopBody);
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.LOOP);
                analyseTerminal(Tag.SEMICOLON);
            }
            default -> {
                statement = null;
            }
        }
        return statement;
    }

    /**
     * Grammar rule : instr2
     */
    @PrintMethodName
    private IdentifiableStatementNode identifiableStatement() {
        IdentifiableStatementNode statement = null;
        switch (this.currentToken.tag()) {

            // var;
            case SEMICOLON -> {
                statement = new FunctionCallStatementNode();
                analyseTerminal(Tag.SEMICOLON);
            }

            case OPEN_PAREN -> {
                // TODO
                statement = new FunctionCallStatementNode();
                analyseTerminal(Tag.OPEN_PAREN);
                exprsep();
                analyseTerminal(Tag.CLOSE_PAREN);
                instr3();
                hasassign();
                analyseTerminal(Tag.SEMICOLON);
            }
            case ASSIGN, DOT -> {
                // TODO : Assign recursive variable.variable.variable
                statement = new AssignmentNode();
                ((AssignmentNode) statement).setVariableReference(instr3());
                analyseTerminal(Tag.ASSIGN);
                ((AssignmentNode)statement).setExpression(expr());
                analyseTerminal(Tag.SEMICOLON);
            }
        }
        return statement;
    }
    @PrintMethodName
    private AccessReferenceNode instr3() {
        AccessReferenceNode accessReferenceNode = null;
        switch (this.currentToken.tag()) {
            case ASSIGN -> {
            }
            case DOT -> {
                analyseTerminal(Tag.DOT);
                accessReferenceNode = new AccessReferenceNode();
                accessReferenceNode.setVariableName(analyseTerminal(Tag.IDENT).getValue());
                accessReferenceNode.setNextVariable(instr3());
            }
        }
        return accessReferenceNode ;
    }
    @PrintMethodName
    private void hasassign() {
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case ASSIGN -> {
                analyseTerminal(Tag.ASSIGN);
                expr();
            }
        }
    }
    @PrintMethodName
    private void elifn() {
        switch (this.currentToken.tag()) {
            case END, ELSE -> {
            }
            case ELSIF -> {
                analyseTerminal(Tag.ELSIF);
                expr();
                analyseTerminal(Tag.THEN);
                statement();
                elifn();
            }
        }
    }
    @PrintMethodName
    private List<StatementNode> elsen() {
        List<StatementNode> statements = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case END -> {
            }
            case ELSE -> {
                analyseTerminal(Tag.ELSE);
                statements.addAll(multipleStatements());
            }
        }
        return statements;
    }
    @PrintMethodName
    private boolean hasreverse() {
        boolean hasReverse = false;
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
            }
            case REVERSE -> {
                analyseTerminal(Tag.REVERSE);
                hasReverse = true;
            }
        }
        return hasReverse;
    }

    /**
     * Grammar rule : instrs
     */
    @PrintMethodName
    private List<StatementNode> multipleStatements() {
        List<StatementNode> statements = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IDENT, BEGIN, RETURN, IF, FOR, WHILE -> {
                statements.add(statement());
                statements.addAll(instrs2());
            }
        }
        return statements;
    }
    @PrintMethodName
    private List<StatementNode> instrs2() {
        List<StatementNode> statements = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IDENT, BEGIN, RETURN, IF, FOR, WHILE -> {
                statements.add(statement());
                statements.addAll(instrs2());
            }
            case END, ELSE, ELSIF -> {
            }
        }
        return statements;
    }
    @PrintMethodName
    private void acces() {
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, END, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, MULTI, DIV, REM, DOTDOT, LOOP -> {
            }
            case DOT -> {
                analyseTerminal(Tag.DOT);
                analyseTerminal(Tag.IDENT);
                acces();
            }
        }

    }
    @PrintMethodName
    private Token analyseTerminal(Tag tag) {
        System.out.println("\t\t↪️ " + this.currentToken);
        if (!(this.currentToken.tag() == tag)) {
            Token expectedToken = new Token(tag, this.currentToken.line(), TagHelper.getTagString(tag));
            if (expectedToken.tag() == Tag.SEMICOLON) {
                this.errorService.registerSyntaxWarning(new MissingSemicolonException(this.currentToken));
            } else {
            this.errorService.registerSyntaxError(new UnexpectedTokenException(expectedToken, this.currentToken));}
        }
        // Contient le prochain token ou <EOF, currentLine,""> si fin de fichier
        if (this.currentToken.tag() == Tag.EOF) {
            return this.currentToken;
        }
        Token temp = this.currentToken;
        this.currentToken = lexer.nextToken();
        return temp;
    }

}
