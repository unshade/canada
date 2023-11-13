package Parser;

import Exceptions.Syntax.UnexpectedTokenException;
import Helpers.TagHelper;
import Lexer.Lexer;
import Lexer.Tokens.Tag;
import Lexer.Tokens.Token;
import Services.ErrorService;

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

    public void parse() {
        fichier();
    }

    private void fichier() {

        analyseTerminal(Tag.WITH);
        analyseTerminal(Tag.ADA_TEXT_IO);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.USEADA_TEXT_IO);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.PROCEDURE);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.IS);
        decls();
        analyseTerminal(Tag.BEGIN);
        instrs();
        analyseTerminal(Tag.END);
        hasident();
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.EOF);

    }

    private void decl() {

        switch (this.currentToken.tag()) {
            case PROCEDURE -> {
                analyseTerminal(Tag.PROCEDURE);
                analyseTerminal(Tag.IDENT);
                hasparams();
                analyseTerminal(Tag.IS);
                decls();
                analyseTerminal(Tag.BEGIN);
                instrs();
                analyseTerminal(Tag.END);
                hasident();
                analyseTerminal(Tag.SEMICOLON);
            }
            case IDENT -> {
                identsep();
                analyseTerminal(Tag.COLON);
                type_n();
                typexpr();
                analyseTerminal(Tag.SEMICOLON);
            }
            case TYPE -> {
                analyseTerminal(Tag.TYPE);
                analyseTerminal(Tag.IDENT);
                hasischoose();
                analyseTerminal(Tag.SEMICOLON);
            }
            case FUNCTION -> {
                analyseTerminal(Tag.FUNCTION);
                analyseTerminal(Tag.IDENT);
                hasparams();
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
        }

    }

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

    private void decls() {

        switch (this.currentToken.tag()) {
            case PROCEDURE, IDENT, TYPE, FUNCTION -> {
                decl();
                decls();
            }
            case BEGIN -> {
            }
        }
    }

    private void hasident() {

        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
            }
            case IDENT -> analyseTerminal(Tag.IDENT);
        }
    }

    private void identsep() {

        if (this.currentToken.tag() == Tag.IDENT) {
            analyseTerminal(Tag.IDENT);
            identsep2();
        }
    }

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

    private void champ() {

        if (this.currentToken.tag() == Tag.IDENT) {
            identsep();
            analyseTerminal(Tag.COLON);
            type_n();
            analyseTerminal(Tag.SEMICOLON);
        }
    }

    private void champs() {

        if (this.currentToken.tag() == Tag.IDENT) {
            champ();
            champs2();
        }
    }

    private void champs2() {

        switch (this.currentToken.tag()) {
            case IDENT -> champs();
            case END -> {
            }
        }
    }

    private void type_n() {

        switch (this.currentToken.tag()) {
            case ACCESS -> {
                analyseTerminal(Tag.ACCESS);
                analyseTerminal(Tag.IDENT);
            }
            case IDENT -> analyseTerminal(Tag.IDENT);
        }
    }

    private void params() {

        if (this.currentToken.tag() == Tag.OPEN_PAREN) {
            analyseTerminal(Tag.OPEN_PAREN);
            paramsep();
            analyseTerminal(Tag.CLOSE_PAREN);
        }
    }

    private void hasparams() {

        switch (this.currentToken.tag()) {
            case IS, RETURN -> {
            }
            case OPEN_PAREN -> params();
        }
    }

    private void paramsep() {

        if (this.currentToken.tag() == Tag.IDENT) {
            param();
            paramsep2();
        }
    }

    private void paramsep2() {

        switch (this.currentToken.tag()) {
            case SEMICOLON -> {
                analyseTerminal(Tag.SEMICOLON);
                paramsep();
            }
            case CLOSE_PAREN -> {
            }
        }
    }

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

    private void param() {

        if (this.currentToken.tag() == Tag.IDENT) {
            identsep();
            analyseTerminal(Tag.COLON);
            mode();
            type_n();
        }
    }

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

    private void modeout() {
        switch (this.currentToken.tag()) {
            case IDENT, ACCESS -> {
            }
            case OUT -> {
                analyseTerminal(Tag.OUT);
            }
        }
    }

    // TODO check TYPES
    private void expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW -> {
                or_expr();
            }

        }
    }

    private void or_expr() {
        switch (this.currentToken.tag()) {
            case IDENT, OPEN_PAREN, DOT, ENTIER, CARACTERE, TRUE, FALSE, NULL, NEW -> {
                and_expr();
                or_expr2();
            }
        }
    }

    private void or_expr2() {
    }

    private void or_expr3() {
    }

    private void and_expr() {
    }

    private void and_expr2() {
    }

    private void and_expr3() {
    }

    private void not_expr() {
    }

    private void not_expr2() {
    }

    private void equality_expr() {
    }

    private void equality_expr2() {
    }

    private void relational_expr() {
    }

    private void relational_expr2() {
    }

    private void additive_expr() {
    }

    private void additive_expr2() {
    }

    private void multiplicative_expr() {
    }

    private void multiplicative_expr2() {
    }

    private void unary_expr() {
    }

    private void primary() {
    }

    private void primary2() {
    }

    private void exprsep() {
    }

    private void exprsep2() {
    }

    private void hasexpr() {
    }

    private void instr() {
    }

    private void instr2() {
    }

    private void instr3() {
    }

    private void elifn() {
    }

    private void elsen() {
    }

    private void hasreverse() {
    }

    private void instrs() {
    }

    private void instrs2() {
    }

    private void acces() {
    }

    private void analyseTerminal(Tag tag) {
        if (!(this.currentToken.tag() == tag)) {
            Token expectedToken = new Token(tag, this.currentToken.line(), TagHelper.getTagString(tag));
            this.errorService.registerSyntaxError(new UnexpectedTokenException(expectedToken, this.currentToken));
        }
        // Contient le prochain token ou <EOF, currentLine,""> si fin de fichier
        this.currentToken = lexer.nextToken();
    }

}
