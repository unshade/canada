package org.trad.pcl.Parser;

import org.trad.pcl.Exceptions.Syntax.MissingSemicolonException;
import org.trad.pcl.Exceptions.Syntax.UnexpectedTokenException;
import org.trad.pcl.Exceptions.Syntax.UnexpectedTokenListException;
import org.trad.pcl.Helpers.TagHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.annotation.PrintMethodName;
import org.trad.pcl.ast.OperatorNode;
import org.trad.pcl.ast.ParameterNode;
import org.trad.pcl.ast.ProgramNode;
import org.trad.pcl.ast.declaration.*;
import org.trad.pcl.ast.expression.*;
import org.trad.pcl.ast.statement.*;
import org.trad.pcl.ast.type.AccessTypeNode;
import org.trad.pcl.ast.type.RecordTypeNode;
import org.trad.pcl.ast.type.SimpleTypeNode;
import org.trad.pcl.ast.type.TypeNode;

import java.util.ArrayList;
import java.util.List;

public final class Parser {

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

    public static Parser newInstance() {
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
        analyseTerminal(Tag.USE);
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
                List<VariableDeclarationNode> typeNodes = multipleIdent();
                analyseTerminal(Tag.COLON);
                TypeNode typeNode = type_n();
                for (VariableDeclarationNode typeDeclarationNode : typeNodes) {
                    typeDeclarationNode.setType(typeNode);
                    declarations.add(typeDeclarationNode);
                }
                // TODO
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
            default -> declarations = null;
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

    /**
     * Grammar rule : identsep
     */
    @PrintMethodName
    private List<VariableDeclarationNode> multipleIdent() {
        List<VariableDeclarationNode> declarations = new ArrayList<>();
        if (this.currentToken.tag() == Tag.IDENT) {
            VariableDeclarationNode declaration = new VariableDeclarationNode();
            declaration.setName(analyseTerminal(Tag.IDENT).getValue());
            declarations.add(declaration);
            declarations.addAll(identSeparator());
        }
        return declarations;
    }

    /**
     * Grammar rule : identsep2
     */
    @PrintMethodName
    private List<VariableDeclarationNode> identSeparator() {
        List<VariableDeclarationNode> declarations = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case COLON -> {
            }
            case COMMA -> {
                analyseTerminal(Tag.COMMA);
                declarations.addAll(multipleIdent());
            }
        }
        return declarations;
    }
    @PrintMethodName
    private void champ() {
        if (this.currentToken.tag() == Tag.IDENT) {
            multipleIdent();
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
                type = new AccessTypeNode();
                SimpleTypeNode simpleTypeNode = new SimpleTypeNode();
                simpleTypeNode.setTypeName(analyseTerminal(Tag.IDENT).getValue());
                ((AccessTypeNode) type).setBaseType(simpleTypeNode);
            }
            case IDENT -> {
                type = new SimpleTypeNode();
                ((SimpleTypeNode) type).setTypeName(analyseTerminal(Tag.IDENT).getValue());
            }
            default -> {
                List<Token> expectedTokens = new ArrayList<>();
                expectedTokens.add(new Token(Tag.ACCESS, this.currentToken.line(), TagHelper.getTagString(Tag.ACCESS)));
                expectedTokens.add(new Token(Tag.IDENT, this.currentToken.line(), TagHelper.getTagString(Tag.IDENT)));

                this.errorService.registerSyntaxError(new UnexpectedTokenListException(expectedTokens, this.currentToken));
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
            case OPEN_PAREN -> parameters.addAll(multipleParameters());
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
                expression = expression();
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
            multipleIdent();
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

    /**
     * Grammar rule : expr
     */
    @PrintMethodName
    private ExpressionNode expression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                return LeftOrExpression();
            }

        }
        return null;
    }

    /**
     * Grammar rule : or_expr
     */
    @PrintMethodName
    private ExpressionNode LeftOrExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftAndExpression();
                BinaryExpressionNode secondExpression = OrExpression();
                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : or_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftOrExpression
     */
    @PrintMethodName
    private BinaryExpressionNode OrExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, THEN, DOTDOT, LOOP -> {
            }
            case OR -> {
                expression = new BinaryExpressionNode();

                OperatorNode operator = new OperatorNode();
                operator.setOperator(analyseTerminal(Tag.OR).getValue());
                expression.setOperator(operator);

                ExpressionNode secondExpression = RightOrExpression();
                expression.setRight(secondExpression);

            }
        }
        return expression;
    }

    /**
     * Grammar rule : or_expr3
     */
    @PrintMethodName
    private ExpressionNode RightOrExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, ELSE, DOT, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftAndExpression();
                BinaryExpressionNode secondExpression = OrExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : and_expr
     */
    @PrintMethodName
    private ExpressionNode LeftAndExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftNotExpression();
                BinaryExpressionNode secondExpression = AndExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : and_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftAndExpression
     */
    @PrintMethodName
    private BinaryExpressionNode AndExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, THEN, DOTDOT, LOOP -> {
            }
            case AND -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.AND).getValue());

                ExpressionNode secondExpression = RightAndExpression();
                expression.setRight(secondExpression);

            }
        }
        return expression;
    }

    /**
     * Grammar rule : and_expr3
     */
    @PrintMethodName
    private ExpressionNode RightAndExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftNotExpression();
                BinaryExpressionNode secondExpression = AndExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
            case THEN -> {
                // TODO : then
                analyseTerminal(Tag.THEN);
                LeftNotExpression();
                AndExpression();
            }
        }
        return null;
    }

    /**
     * Grammar rule : not_expr
     */
    @PrintMethodName
    private ExpressionNode LeftNotExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftEqualityExpression();
                BinaryExpressionNode secondExpression = NotExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : not_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftNotExpression
     */
    @PrintMethodName
    private BinaryExpressionNode NotExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, DOTDOT, LOOP -> {
            }
            case NOT -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.NOT).getValue());
                expression.setRight(LeftEqualityExpression(), NotExpression());
            }
        }
        return expression;
    }

    /**
     * Grammar rule : equality_expr
     * @return if there is an equality expression, return BinaryExpressionNode with
     */
    @PrintMethodName
    private ExpressionNode LeftEqualityExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftRelationalExpression();
                BinaryExpressionNode secondExpression = EqualityExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : equality_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftEqualityExpression
     */
    @PrintMethodName
    private BinaryExpressionNode EqualityExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, DOTDOT, LOOP -> {
            }
            case EQ -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.EQ).getValue());
                expression.setRight(LeftRelationalExpression(), EqualityExpression());
            }
            case NE -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.NE).getValue());
                expression.setRight(LeftRelationalExpression(), EqualityExpression());
            }
        }
        return expression;
    }
    /**
     * Grammar rule : relational_expr
     */
    @PrintMethodName
    private ExpressionNode LeftRelationalExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = LeftAdditiveExpression();
                BinaryExpressionNode secondExpression = RelationalExpression();

                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : relational_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftRelationalExpression
     */
    @PrintMethodName
    private BinaryExpressionNode RelationalExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, DOTDOT, LOOP -> {
            }
            case LT -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.LT).getValue());
                expression.setRight(LeftAdditiveExpression(), RelationalExpression());
            }
            case LE -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.LE).getValue());
                expression.setRight(LeftAdditiveExpression(), RelationalExpression());
            }
            case GT -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.GT).getValue());
                expression.setRight(LeftAdditiveExpression(), RelationalExpression());
            }
            case GE -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.GE).getValue());
                expression.setRight(LeftAdditiveExpression(), RelationalExpression());
            }
        }
        return expression;
    }

    /**
     * Grammar rule : additive_expr
     */
    @PrintMethodName
    private ExpressionNode LeftAdditiveExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression =  LeftMultiplicativeExpression();
                BinaryExpressionNode secondExpression = AdditiveExpression();
                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : additive_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftAdditiveExpression
     */
    @PrintMethodName
    private BinaryExpressionNode AdditiveExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, DOTDOT, LOOP -> {
            }
            case PLUS -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.PLUS).getValue());
                expression.setRight(LeftMultiplicativeExpression(), AdditiveExpression());
            }
            case MINUS -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.MINUS).getValue());
                expression.setRight(LeftMultiplicativeExpression(), AdditiveExpression());
            }
        }
        return expression;
    }

    /**
     * Grammar rule : multiplicative_expr
     */
    @PrintMethodName
    private ExpressionNode LeftMultiplicativeExpression() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                ExpressionNode firstExpression = unaryExpression();
                BinaryExpressionNode secondExpression = MultiplicativeExpression();
                if (secondExpression != null) {
                    secondExpression.setLeft(firstExpression);
                    return secondExpression;
                } else {
                    return firstExpression;
                }
            }
        }
        return null;
    }

    /**
     * Grammar rule : multiplicative_expr2
     * @return BinaryExpressionNode with operator and right expression
     * the left expression is set in LeftMultiplicativeExpression
     */
    @PrintMethodName
    private BinaryExpressionNode MultiplicativeExpression() {
        BinaryExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, DOTDOT, LOOP -> {
            }
            case MULTI -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.MULTI).getValue());
                expression.setRight(unaryExpression(), MultiplicativeExpression());
            }
            case DIV -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.DIV).getValue());
                expression.setRight(unaryExpression(), MultiplicativeExpression());

            }
            case REM -> {
                expression = new BinaryExpressionNode();
                expression.setOperator(analyseTerminal(Tag.REM).getValue());
                expression.setRight(unaryExpression(), MultiplicativeExpression());
            }
        }
        return expression;
    }

    /**
     * Grammar rule : unary_expr
     */
    @PrintMethodName
    private ExpressionNode unaryExpression() {
        ExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case MINUS -> {
                expression = new UnaryExpressionNode();
                OperatorNode operator = new OperatorNode();
                operator.setOperator(analyseTerminal(Tag.MINUS).getValue());
                ((UnaryExpressionNode) expression).setOperator(operator);
                ((UnaryExpressionNode) expression).setOperand(primary());
            }
            case IDENT, OPEN_PAREN, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> expression = primary();
        }
        return expression;
    }
    @PrintMethodName
    private ExpressionNode primary() {
        ExpressionNode expression = null;
        switch (this.currentToken.tag()) {
            case IDENT -> {
                String ident = analyseTerminal(Tag.IDENT).getValue();
                expression = identPrimary();
                ((VariableReferenceNode) expression).setVariableName(ident);
            }
            case OPEN_PAREN -> {
                analyseTerminal(Tag.OPEN_PAREN);
                expression = expression();
                analyseTerminal(Tag.CLOSE_PAREN);
            }
            case ENTIER -> {
                expression = new LiteralNode();
                ((LiteralNode) expression).setValue(Integer.parseInt(analyseTerminal(Tag.ENTIER).getValue()));
            }
            case CARACTERE -> {
                expression = new LiteralNode();
                ((LiteralNode) expression).setValue(analyseTerminal(Tag.CARACTERE).getValue().charAt(0));
            }
            case TRUE -> {
                expression = new LiteralNode();
                analyseTerminal(Tag.TRUE);
                ((LiteralNode) expression).setValue(true);

            }
            case FALSE -> {
                expression = new LiteralNode();
                analyseTerminal(Tag.FALSE);
                ((LiteralNode) expression).setValue(false);
            }
            case NULL -> {
                expression = new LiteralNode();
                analyseTerminal(Tag.NULL);
                ((LiteralNode) expression).setValue(null);
            }
            case NEW -> {
                // TODO : new
                analyseTerminal(Tag.NEW);
                analyseTerminal(Tag.IDENT);
            }
            case CHARACTER -> {
                // TODO : character ' val(ENTIER)
                analyseTerminal(Tag.CHARACTER);
                analyseTerminal(Tag.APOSTROPHE);
                analyseTerminal(Tag.VAL);
                analyseTerminal(Tag.OPEN_PAREN);
                expression();
                analyseTerminal(Tag.CLOSE_PAREN);
            }

        }
        return expression;
    }

    /**
     * Grammar rule : primary2
     */
    @PrintMethodName
    private VariableReferenceNode identPrimary() {
        VariableReferenceNode expression = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, AND, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, MULTI, DIV, REM, DOTDOT, LOOP, DOT -> {
                expression = new VariableReferenceNode();
                expression.setNextExpression(acces());
            }
            case OPEN_PAREN -> {
                analyseTerminal(Tag.OPEN_PAREN);
                expression = new FunctionCallNode();
                ((FunctionCallNode) expression).setArguments(multipleExpressions());
                analyseTerminal(Tag.CLOSE_PAREN);
                expression.setNextExpression(acces());
            }
        }
        return expression;
    }

    /**
     * Grammar rule : exprsep
     */
    @PrintMethodName
    private List<ExpressionNode> multipleExpressions() {
        List<ExpressionNode> expressions = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> {
                expressions.add(expression());
                expressions.addAll(expressionSeparator());
            }
        }
        return expressions;
    }

    /**
     * Grammar rule : exprsep2
     */
    @PrintMethodName
    private List<ExpressionNode> expressionSeparator() {
        List<ExpressionNode> expressions = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case COMMA -> {
                analyseTerminal(Tag.COMMA);
                expressions.addAll(multipleExpressions());
            }
            case CLOSE_PAREN -> {
            }
        }
        return expressions;
    }

    /**
     * Grammar rule : hasexpr
     */
    @PrintMethodName
    private List<ExpressionNode> hasExpression() {
        List<ExpressionNode> expressions = new ArrayList<>();
        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case IDENT, OPEN_PAREN, MINUS, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW, CHARACTER -> expressions.add(expression());
            case ASSIGN, DOT -> expressions.addAll(multipleExpressions());
        }
        return expressions;
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
                ((ReturnStatementNode) statement).addExpressions(hasExpression());
                analyseTerminal(Tag.SEMICOLON);
            }
            case IF -> {
                statement = new IfStatementNode();
                analyseTerminal(Tag.IF);
                ((IfStatementNode) statement).setCondition(expression());
                analyseTerminal(Tag.THEN);
                BlockNode thenBranch = new BlockNode();
                thenBranch.addStatements(multipleStatements());
                ((IfStatementNode) statement).setThenBranch(thenBranch);
                ((IfStatementNode) statement).setElseIfBranch(elifn());
                ((IfStatementNode) statement).setElseBranch(elsen());
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
                ((LoopStatementNode) statement).setStartExpression(expression());
                analyseTerminal(Tag.DOTDOT);
                ((LoopStatementNode) statement).setEndExpression(expression());
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
                ((WhileStatementNode) statement).setCondition(expression());
                analyseTerminal(Tag.LOOP);
                BlockNode loopBody = new BlockNode();
                loopBody.addStatements(multipleStatements());
                ((WhileStatementNode) statement).setBody(loopBody);
                analyseTerminal(Tag.END);
                analyseTerminal(Tag.LOOP);
                analyseTerminal(Tag.SEMICOLON);
            }
            default -> statement = null;
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
                statement = new FunctionCallStatementNode();
                analyseTerminal(Tag.OPEN_PAREN);
                ((FunctionCallStatementNode) statement).setArguments(multipleExpressions());
                analyseTerminal(Tag.CLOSE_PAREN);
                // TODO
                instr3();
                hasassign();
                analyseTerminal(Tag.SEMICOLON);
            }
            case ASSIGN, DOT -> {
                statement = new AssignmentNode();
                statement.setNextIdentifier(instr3());
                analyseTerminal(Tag.ASSIGN);
                ((AssignmentNode) statement).setExpression(expression());
                analyseTerminal(Tag.SEMICOLON);
            }
        }
        return statement;
    }
    @PrintMethodName
    private VariableReferenceNode instr3() {
        VariableReferenceNode accessReferenceNode = null;
        switch (this.currentToken.tag()) {
            case ASSIGN -> {
            }
            case DOT -> {
                analyseTerminal(Tag.DOT);
                accessReferenceNode = new VariableReferenceNode();
                accessReferenceNode.setVariableName(analyseTerminal(Tag.IDENT).getValue());
                accessReferenceNode.setNextExpression(instr3());
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
                expression();
            }
        }
    }
    @PrintMethodName
    private IfStatementNode elifn() {
        IfStatementNode statement = null;
        switch (this.currentToken.tag()) {
            case END, ELSE -> {
            }
            case ELSIF -> {
                analyseTerminal(Tag.ELSIF);
                statement = new IfStatementNode();
                statement.setCondition(expression());
                analyseTerminal(Tag.THEN);
                BlockNode thenBranch = new BlockNode();
                thenBranch.addStatements(multipleStatements());
                statement.setThenBranch(thenBranch);
                statement.setElseIfBranch(elifn());
            }
        }
        return statement;
    }
    @PrintMethodName
    private BlockNode elsen() {
        BlockNode block = null;
        switch (this.currentToken.tag()) {
            case END -> {
            }
            case ELSE -> {
                analyseTerminal(Tag.ELSE);
                block = new BlockNode();
                block.addStatements(multipleStatements());
            }
        }
        return block;
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
    private VariableReferenceNode acces() {
        VariableReferenceNode variableReferenceNode = null;
        switch (this.currentToken.tag()) {
            case SEMICOLON, COMMA, CLOSE_PAREN, OR, END, THEN, NOT, EQ, NE, LT, LE, GT, GE, PLUS, MINUS, MULTI, DIV, REM, DOTDOT, LOOP -> {
            }
            case DOT -> {
                analyseTerminal(Tag.DOT);
                variableReferenceNode = new VariableReferenceNode();
                variableReferenceNode.setVariableName(analyseTerminal(Tag.IDENT).getValue());
                variableReferenceNode.setNextExpression(acces());
            }
        }
        return variableReferenceNode;
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
