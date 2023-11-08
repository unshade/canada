package Lexer;

import Lexer.Tokens.Tag;
import Lexer.Tokens.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Lexer {
    Reader reader;
    private List<Token> tokens;
    private int currentLine;
    private Map<Pattern, Tag> keywords;
    private Map<Pattern, Tag> ruledTerminals;
    private Map<Pattern, Tag> operators;

    private int currentChar;
    private int previousChar;

    public Lexer(File file) throws IOException {

        this.reader = new BufferedReader(new FileReader(file));
        this.tokens = new ArrayList<>();
        this.currentLine = 1;
        this.currentChar = this.reader.read();

        this.keywords = Map.ofEntries(
                Map.entry(Pattern.compile("procedure"), Tag.PROCEDURE),
                Map.entry(Pattern.compile("is"), Tag.IS),
                Map.entry(Pattern.compile("begin"), Tag.BEGIN),
                Map.entry(Pattern.compile("end"), Tag.END),
                Map.entry(Pattern.compile(";"), Tag.SEMICOLON),
                Map.entry(Pattern.compile("type"), Tag.TYPE),
                Map.entry(Pattern.compile("access"), Tag.ACCESS),
                Map.entry(Pattern.compile("record"), Tag.RECORD),
                Map.entry(Pattern.compile(":"), Tag.COLON),
                Map.entry(Pattern.compile("function"), Tag.FUNCTION),
                Map.entry(Pattern.compile("return"), Tag.RETURN),
                Map.entry(Pattern.compile("in"), Tag.IN),
                Map.entry(Pattern.compile("out"), Tag.OUT),
                Map.entry(Pattern.compile("if"), Tag.IF),
                Map.entry(Pattern.compile("then"), Tag.THEN),
                Map.entry(Pattern.compile("elsif"), Tag.ELSIF),
                Map.entry(Pattern.compile("else"), Tag.ELSE),
                Map.entry(Pattern.compile("loop"), Tag.LOOP),
                Map.entry(Pattern.compile("for"), Tag.FOR),
                Map.entry(Pattern.compile("reverse"), Tag.REVERSE),
                Map.entry(Pattern.compile("while"), Tag.WHILE),
                Map.entry(Pattern.compile("rem"), Tag.REM),
                Map.entry(Pattern.compile("and"), Tag.AND),
                Map.entry(Pattern.compile("or"), Tag.OR),
                Map.entry(Pattern.compile("\\."), Tag.DOT),
                Map.entry(Pattern.compile("val"), Tag.VAL),
                Map.entry(Pattern.compile("\\("), Tag.OPEN_PAREN),
                Map.entry(Pattern.compile("\\)"), Tag.CLOSE_PAREN),
                Map.entry(Pattern.compile("true"), Tag.TRUE),
                Map.entry(Pattern.compile("false"), Tag.FALSE),
                Map.entry(Pattern.compile(","), Tag.COMMA)
        );

        this.operators = Map.ofEntries(
                Map.entry(Pattern.compile("\\+"), Tag.PLUS),
                Map.entry(Pattern.compile("-"), Tag.MINUS),
                Map.entry(Pattern.compile("\\*"), Tag.MULTI),
                Map.entry(Pattern.compile("/"), Tag.DIV),
                Map.entry(Pattern.compile("="), Tag.EQ),
                Map.entry(Pattern.compile("/="), Tag.NE),
                Map.entry(Pattern.compile("<"), Tag.LT),
                Map.entry(Pattern.compile("<="), Tag.LE),
                Map.entry(Pattern.compile(">"), Tag.GT),
                Map.entry(Pattern.compile(">="), Tag.GE),
                Map.entry(Pattern.compile(":="), Tag.ASSIGN)
        );

        this.ruledTerminals = Map.of(
                Pattern.compile("[A-Za-z][A-Za-z0-9_]*"), Tag.IDENT,
                Pattern.compile("[0-9]+"), Tag.ENTIER,
                Pattern.compile("'[A-Za-z]'"), Tag.CARACTERE
        );
    }

    public Token nextToken() throws IOException {

        StringBuilder lexeme = new StringBuilder();

        while (this.currentChar != -1) {

            this.previousChar = this.currentChar;

            this.currentChar = this.reader.read();

            if ((Character.isLetterOrDigit((char) currentChar) && Character.isLetterOrDigit((char) previousChar)) || (!Character.isLetterOrDigit((char) currentChar) && !Character.isLetterOrDigit((char) previousChar)) || !Character.isWhitespace((char) currentChar)) {
                lexeme.append((char) currentChar);

            } else {
                for (Map.Entry<Pattern, Tag> entry : this.keywords.entrySet()) {
                    Pattern pattern = entry.getKey();
                    Tag tag = entry.getValue();

                    if (pattern.matcher(lexeme.toString()).matches()) {
                        return new Token(tag, this.currentLine, lexeme.toString());
                    }
                }

                for (Map.Entry<Pattern, Tag> entry : this.ruledTerminals.entrySet()) {
                    Pattern pattern = entry.getKey();
                    Tag tag = entry.getValue();

                    if (pattern.matcher(lexeme.toString()).matches()) {
                        return new Token(tag, this.currentLine, lexeme.toString());
                    }
                }

                for (Map.Entry<Pattern, Tag> entry : this.operators.entrySet()) {
                    Pattern pattern = entry.getKey();
                    Tag tag = entry.getValue();

                    if (pattern.matcher(lexeme.toString()).matches()) {
                        return new Token(tag, this.currentLine, lexeme.toString());
                    }
                }

                if (Character.isWhitespace(currentChar)) {
                    if (currentChar == '\n') {
                        this.currentLine++;
                    }

                    currentChar = this.reader.read();
                    continue;
                }

                lexeme.append((char) currentChar);
            }
        }

        return new Token(Tag.EOF, this.currentLine, lexeme.toString());

    }


    private int read() throws IOException {

        while (Character.isWhitespace(currentChar)) {
            if (currentChar == '\n') {
                this.currentLine++;
            }

            currentChar = this.reader.read();
        }

        return currentChar;
    }

}
