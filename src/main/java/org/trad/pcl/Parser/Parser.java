package org.trad.pcl.Parser;

import org.trad.pcl.Exceptions.Syntax.MissingSemicolonException;
import org.trad.pcl.Exceptions.Syntax.UnexpectedTokenException;
import org.trad.pcl.Helpers.TagHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.annotation.PrintMethodName;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.DeclarationNode;
import org.trad.pcl.ast.declaration.FunctionDeclarationNode;
import org.trad.pcl.ast.declaration.ProcedureDeclarationNode;
import org.trad.pcl.ast.declaration.TypeDeclarationNode;
import org.trad.pcl.ast.statement.*;
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
        String rootProcedureName = currentToken.getValue();
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.IS);
        ProcedureDeclarationNode rootProcedure = new ProcedureDeclarationNode(rootProcedureName);
        BlockNode rootProcedureBody = new BlockNode();
        rootProcedureBody.setParent(rootProcedure);
        rootProcedureBody.addDeclarations(decls());
        analyseTerminal(Tag.BEGIN);
        rootProcedureBody.addStatements(instrs());
        rootProcedure.setBody(rootProcedureBody);
        abstractSyntaxTreeRoot.setRootProcedure(rootProcedure);
        analyseTerminal(Tag.END);
        hasident();
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.EOF);

        return abstractSyntaxTreeRoot;
    }

    @PrintMethodName

    private DeclarationNode decl() {
        DeclarationNode declaration;
        switch (this.currentToken.tag()) {
            case PROCEDURE -> {
                declaration = new ProcedureDeclarationNode(currentToken.getValue());
                analyseTerminal(Tag.PROCEDURE);
                analyseTerminal(Tag.IDENT);
                ((ProcedureDeclarationNode) declaration).addParameters(hasparams());
                analyseTerminal(Tag.IS);
                decls();
                analyseTerminal(Tag.BEGIN);
                instrs();
                analyseTerminal(Tag.END);
                hasident();
                analyseTerminal(Tag.SEMICOLON);
            }
            case IDENT -> {
                declaration = new TypeDeclarationNode(currentToken.getValue());
                identsep();
                analyseTerminal(Tag.COLON);
                type_n();
                typexpr();
                analyseTerminal(Tag.SEMICOLON);
            }
            case TYPE -> {
                declaration = new TypeDeclarationNode(currentToken.getValue());
                analyseTerminal(Tag.TYPE);
                analyseTerminal(Tag.IDENT);
                hasischoose();
                analyseTerminal(Tag.SEMICOLON);
            }
            case FUNCTION -> {
                declaration = new FunctionDeclarationNode(currentToken.getValue());
                analyseTerminal(Tag.FUNCTION);
                analyseTerminal(Tag.IDENT);
                ((FunctionDeclarationNode) declaration).addParameters(hasparams());
                analyseTerminal(Tag.RETURN);
                type_n();
                analyseTerminal(Tag.IS);
                decls();
                analyseTerminal(Tag.BEGIN);
                instrs();
                analyseTerminal(Tag.END);
                hasident();
                analyseTerminal(Tag.SEMICOLON);
            }
            default -> {
                declaration = null;
            }
        }
        return declaration;
    }

    @PrintMethodName
    private void hasischoose() {
        switch (this.currentToken.tag()) {
            case IS -> {
                analyseTerminal(Tag.IS);
                accorrec();
            }
            case SEMICOLON -> {
            }
        }
    }

    @PrintMethodName
    private void accorrec() {
        switch (this.currentToken.tag()) {
            case ACCESS -> {
                analyseTerminal(Tag.ACCESS);
                analyseTerminal(Tag.IDENT);
            }
            case RECORD -> {
                analyseTerminal(Tag.RECORD);
                champs();
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.RECORD);
            }
        }
    }
    @PrintMethodName
    private List<DeclarationNode> decls() {
        List<DeclarationNode> declarations = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case PROCEDURE, IDENT, TYPE, FUNCTION -> {
                declarations.add(decl());
                declarations.addAll(decls());
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
    private void identsep() {
        if (this.currentToken.tag() == Tag.IDENT) {
            analyseTerminal(Tag.IDENT);
            identsep2();
        }
    }
    @PrintMethodName
    private void identsep2() {
        switch (this.currentToken.tag()) {
            case COLON -> {
            }
            case COMMA -> {
                analyseTerminal(Tag.COMMA);
                identsep();
            }
        }
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
    @PrintMethodName
    private List<ParameterNode> params() {
        List<ParameterNode> parameters = new ArrayList<>();
        if (this.currentToken.tag() == Tag.OPEN_PAREN) {
            analyseTerminal(Tag.OPEN_PAREN);
            parameters.addAll(paramsep());
            analyseTerminal(Tag.CLOSE_PAREN);
        }
        return parameters;
    }
    @PrintMethodName
    private List<ParameterNode> hasparams() {
        List<ParameterNode> parameters = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IS, RETURN -> {
            }
            case OPEN_PAREN -> {
                parameters.addAll(params());
            }
        }
        return parameters;
    }
    @PrintMethodName
    private List<ParameterNode> paramsep() {
        List<ParameterNode> parameters = new ArrayList<>();
        if (this.currentToken.tag() == Tag.IDENT) {
            parameters.add(param());
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
    @PrintMethodName
    private void typexpr() {
        switch (this.currentToken.tag()) {
            case ASSIGN -> {
                analyseTerminal(Tag.ASSIGN);
                expr();
            }
            case SEMICOLON -> {
            }
        }
    }
    @PrintMethodName
    private ParameterNode param() {
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
    private void expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                or_expr();
            }

        }
    }
    @PrintMethodName
    private void or_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                and_expr();
                or_expr2();
            }
        }
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
    private void hasexpr() {
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                expr();
            }
            case ASSIGN, DOT -> {
                exprsep();
            }
        }
    }
    @PrintMethodName
    private StatementNode instr() {
        StatementNode statement;
        switch (this.currentToken.tag()) {
            case IDENT -> {
                // TODO ??
                statement = new BlockNode();
                analyseTerminal(Tag.IDENT);
                instr2();
            }
            case BEGIN -> {
                statement = new BlockNode();
                analyseTerminal(Tag.BEGIN);
                instrs();
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.SEMICOLON);
            }
            case RETURN -> {
                statement = new ReturnStatementNode();
                analyseTerminal(Tag.RETURN);
                hasexpr();
                analyseTerminal(Tag.SEMICOLON);
            }
            case IF -> {
                statement = new IfStatementNode();
                analyseTerminal(Tag.IF);
                expr();
                analyseTerminal(Tag.THEN);
                instrs();
                elifn();
                elsen();
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.IF);
                analyseTerminal(Tag.SEMICOLON);
            }
            case FOR -> {
                statement = new LoopStatementNode();
                analyseTerminal(Tag.FOR);
                analyseTerminal(Tag.IDENT);
                analyseTerminal(Tag.IN);
                hasreverse();
                expr();
                analyseTerminal(Tag.DOTDOT);
                expr();
                analyseTerminal(Tag.LOOP);
                instrs();
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.LOOP);
                analyseTerminal(Tag.SEMICOLON);
            }
            case WHILE -> {
                statement = new LoopStatementNode();
                analyseTerminal(Tag.WHILE);
                expr();
                analyseTerminal(Tag.LOOP);
                instrs();
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
    @PrintMethodName
    private void instr2() {
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
                analyseTerminal(Tag.SEMICOLON);
            }
            case OPEN_PAREN -> {
                analyseTerminal(Tag.OPEN_PAREN);
                exprsep();
                analyseTerminal(Tag.CLOSE_PAREN);
                instr3();
                hasassign();
                analyseTerminal(Tag.SEMICOLON);
            }
            case ASSIGN, DOT -> {
                instr3();
                analyseTerminal(Tag.ASSIGN);
                expr();
                analyseTerminal(Tag.SEMICOLON);
            }
        }
    }
    @PrintMethodName
    private void instr3() {
        switch (this.currentToken.tag()) {
            case ASSIGN -> {
            }
            case DOT -> {
                analyseTerminal(Tag.DOT);
                analyseTerminal(Tag.IDENT);
                instr3();
            }
        }
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
                instr();
                elifn();
            }
        }
    }
    @PrintMethodName
    private void elsen() {
        switch (this.currentToken.tag()) {
            case END -> {
            }
            case ELSE -> {
                analyseTerminal(Tag.ELSE);
                instrs();
            }
        }
    }
    @PrintMethodName
    private void hasreverse() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
            }
            case REVERSE -> {
                analyseTerminal(Tag.REVERSE);
            }
        }
    }
    @PrintMethodName
    private List<StatementNode> instrs() {
        List<StatementNode> statements = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IDENT, BEGIN, RETURN, IF, FOR, WHILE -> {
                statements.add(instr());
                statements.addAll(instrs2());
            }
        }

        //TODO
        return statements;
    }
    @PrintMethodName
    private List<StatementNode> instrs2() {
        List<StatementNode> statements = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IDENT, BEGIN, RETURN, IF, FOR, WHILE -> {
                statements.add(instr());
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
    private void analyseTerminal(Tag tag) {
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
            return;
        }
        this.currentToken = lexer.nextToken();
    }

}
