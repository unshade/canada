package Parser;

import Lexer.Lexer;
import Lexer.Tokens.Tag;
import Lexer.Tokens.Token;

public class Parser {

    Lexer lexer = Lexer.getInstance();

    public void fichier () {

        analyseTerminal(Tag.WITH);
        analyseTerminal(Tag.ADA_TEXT_IO);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.USEADA_TEXT_IO);
        analyseTerminal(Tag.SEMICOLON);
        analyseTerminal(Tag.PROCEDURE);
        analyseTerminal(Tag.IDENT);
        analyseTerminal(Tag.IS);

    }

    private void decls () {

        switch (lexer.nextToken().tag()) {
            case Tag.PROCEDURE, Tag.IDENT, Tag.TYPE, Tag.FUNCTION -> {
                decl();
                decls();
            }
            case Tag.BEGIN -> {
            }
        }
    }

    private void decl () {

        switch (lexer.nextToken().tag()) {
            case Tag.PROCEDURE -> {
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
            case Tag.IDENT -> {
                identsep();
                analyseTerminal(Tag.COLON);
                type_n();
                typexpr();
                analyseTerminal(Tag.SEMICOLON);
            }
            case Tag.TYPE -> {
                analyseTerminal(Tag.IDENT);
                hasischoose();
                analyseTerminal(Tag.SEMICOLON);
            }
            case Tag.FUNCTION -> {
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

    private void hasischoose () {

        switch (lexer.nextToken().tag()) {
            case Tag.IS -> {
                accorrec();
            }
            case Tag.SEMICOLON -> {
            }
        }
    }

    private void hasparams () {

        switch (lexer.nextToken().tag()) {
            case Tag.IS -> {
            }
            case Tag.RETURN -> {
            }
            case Tag.OPEN_PAREN -> {
                params();
            }
        }
    }

    private void params () {

        switch (lexer.nextToken().tag()) {
            case Tag.OPEN_PAREN -> {
                paramsep();
                analyseTerminal(Tag.CLOSE_PAREN);
            }
        }
    }

    private void paramsep () {

        switch (lexer.nextToken().tag()) {
            case Tag.IDENT -> {
                param();
                paramsep2();
            }
        }
    }

    private void param () {

        switch (lexer.nextToken().tag()) {
            case Tag.IDENT -> {
                identsep();
            }
        }
    }

    private void identsep () {

        switch (lexer.nextToken().tag()) {
            case Tag.IDENT -> {
                identsep2();
            }
        }
    }

    private void identsep2 () {

        switch (lexer.nextToken().tag()) {
            case Tag.COLON -> {
            }
        }
    }

    private void paramsep2 () {

        switch (lexer.nextToken().tag()) {
            case Tag.SEMICOLON -> {
                paramsep();
            }
            case Tag.CLOSE_PAREN -> {
            }
        }
    }



    private void analyseTerminal( Tag tag ) {
        if ( lexer.nextToken().tag() == tag ) {
            return;
        } else {
            throw new Exception("Error");
        }

    }

}
