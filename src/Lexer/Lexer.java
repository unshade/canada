package Lexer;

import Exceptions.Lexical.InvalidToken;
import Lexer.Tokens.Tag;
import Lexer.Tokens.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Lexer class to read a file and return tokens
 *
 * @author Noé Steiner
 * @author Alexis Marcel
 * @author Lucas Laurent
 */
public class Lexer {

    /**
     * Lexer attributes
     */
    private final PeekingReader reader;
    private final Map<Tag, Pattern> keywords;
    private final Map<Tag, Pattern> ruledTerminals;
    private final Map<Tag, Pattern> operators;
    private int currentChar;

    /**
     * Lexer constructor
     *
     * @param file the file to read
     * @throws IOException if the file cannot be read
     */
    public Lexer(File file) throws IOException {

        this.reader = new PeekingReader(new FileReader(file));
        this.currentChar = this.reader.read();
        this.keywords = Map.ofEntries(
                Map.entry(Tag.PROCEDURE, Pattern.compile("procedure")),
                Map.entry(Tag.IS, Pattern.compile("is")),
                Map.entry(Tag.BEGIN, Pattern.compile("begin")),
                Map.entry(Tag.END, Pattern.compile("end")),
                Map.entry(Tag.SEMICOLON, Pattern.compile(";")),
                Map.entry(Tag.TYPE, Pattern.compile("type")),
                Map.entry(Tag.ACCESS, Pattern.compile("access")),
                Map.entry(Tag.RECORD, Pattern.compile("record")),
                Map.entry(Tag.COLON, Pattern.compile(":")),
                Map.entry(Tag.FUNCTION, Pattern.compile("function")),
                Map.entry(Tag.RETURN, Pattern.compile("return")),
                Map.entry(Tag.IN, Pattern.compile("in")),
                Map.entry(Tag.OUT, Pattern.compile("out")),
                Map.entry(Tag.IF, Pattern.compile("if")),
                Map.entry(Tag.THEN, Pattern.compile("then")),
                Map.entry(Tag.ELSIF, Pattern.compile("elsif")),
                Map.entry(Tag.ELSE, Pattern.compile("else")),
                Map.entry(Tag.LOOP, Pattern.compile("loop")),
                Map.entry(Tag.FOR, Pattern.compile("for")),
                Map.entry(Tag.REVERSE, Pattern.compile("reverse")),
                Map.entry(Tag.WHILE, Pattern.compile("while")),
                Map.entry(Tag.REM, Pattern.compile("rem")),
                Map.entry(Tag.AND, Pattern.compile("and")),
                Map.entry(Tag.OR, Pattern.compile("or")),
                Map.entry(Tag.DOT, Pattern.compile("\\.")),
                Map.entry(Tag.VAL, Pattern.compile("val")),
                Map.entry(Tag.OPEN_PAREN, Pattern.compile("\\(")),
                Map.entry(Tag.CLOSE_PAREN, Pattern.compile("\\)")),
                Map.entry(Tag.TRUE, Pattern.compile("true")),
                Map.entry(Tag.FALSE, Pattern.compile("false")),
                Map.entry(Tag.COMMA, Pattern.compile(","))
        );

        this.operators = Map.ofEntries(
                Map.entry(Tag.PLUS, Pattern.compile("\\+")),
                Map.entry(Tag.MINUS, Pattern.compile("-")),
                Map.entry(Tag.MULTI, Pattern.compile("\\*")),
                Map.entry(Tag.DIV, Pattern.compile("/")),
                Map.entry(Tag.EQ, Pattern.compile("=")),
                Map.entry(Tag.NE, Pattern.compile("/=")),
                Map.entry(Tag.LT, Pattern.compile("<")),
                Map.entry(Tag.LE, Pattern.compile("<=")),
                Map.entry(Tag.GT, Pattern.compile(">")),
                Map.entry(Tag.GE, Pattern.compile(">=")),
                Map.entry(Tag.ASSIGN, Pattern.compile(":="))
        );

        this.ruledTerminals = Map.of(
                Tag.IDENT, Pattern.compile("[A-Za-z][A-Za-z0-9_]*"),
                Tag.ENTIER, Pattern.compile("[0-9]+"),
                Tag.CARACTERE, Pattern.compile("'[A-Za-z]'")
        );

    }

    /**
     * Get the next token from the file
     *
     * @return the next token
     * @throws IOException  if the file cannot be read
     * @throws InvalidToken if the lexer finds an invalid token
     */
    public Token nextToken() throws IOException, InvalidToken {
        StringBuilder lexeme = new StringBuilder();

        while (this.currentChar != -1) {

            if (this.isComment()) {
                this.skipComment();
            } else if (Character.isWhitespace((char) currentChar)) {
                this.skipWhitespace();
            } else {
                lexeme.append((char) currentChar);

                if (this.isEndOfToken()) {
                    Token token =  this.matchToken(lexeme.toString());
                    currentChar = this.reader.read();
                    return token;
                }
                currentChar = this.reader.read();
            }

        }

        this.reader.close();
        return new Token(Tag.EOF, this.reader.getCurrentLine(), lexeme.toString());
    }

    /**
     * Check if the current character is the beginning of a comment
     *
     * @return true if the current character is the beginning of a comment, false otherwise
     * @throws IOException if the file cannot be read
     */
    private boolean isComment() throws IOException {
        return this.currentChar == '-' && this.reader.peek(1) == '-' && this.reader.peek(2) != '-';
    }

    /**
     * Skip the current comment
     *
     * @throws IOException if the file cannot be read
     */
    private void skipComment() throws IOException {
        while (this.currentChar != '\n' && this.currentChar != -1) {
            this.currentChar = this.reader.read();
        }
        if (this.currentChar != -1) {
            this.currentChar = this.reader.read();
        }
    }

    /**
     * Check if the current character is the end of a token
     *
     * @return true if the current character is the end of a token, false otherwise
     * @throws IOException if the file cannot be read
     */
    private boolean isEndOfToken() throws IOException {
        char current = (char) currentChar;
        char next = (char) this.reader.peek(1);

        boolean isCurrentLetterOrDigit = Character.isLetterOrDigit(current);
        boolean isNextLetterOrDigit = Character.isLetterOrDigit(next);
        boolean isNextWhitespace = Character.isWhitespace(next);

        return (isCurrentLetterOrDigit && !isNextLetterOrDigit) || (!isCurrentLetterOrDigit && (isNextLetterOrDigit || isNextWhitespace));
    }

    /**
     * Skip all whitespaces
     *
     * @throws IOException if the file cannot be read
     */
    private void skipWhitespace() throws IOException {
        while (Character.isWhitespace((char) this.currentChar)) {
            this.currentChar = this.reader.read();
        }
    }

    /**
     * Match the lexeme with a pattern and return the corresponding token
     *
     * @param lexeme the lexeme to match
     * @return the corresponding token
     * @throws InvalidToken if the lexeme does not match any pattern
     */
    private Token matchToken(String lexeme) throws InvalidToken {
        List<Map<Tag, Pattern>> patterns = List.of(this.keywords, this.ruledTerminals, this.operators);

        for (Map<Tag, Pattern> pattern : patterns) {
            for (Map.Entry<Tag, Pattern> entry : pattern.entrySet()) {
                Pattern p = entry.getValue();
                Tag tag = entry.getKey();

                if (p.matcher(lexeme).matches()) {
                    return new Token(tag, this.reader.getCurrentLine(), lexeme);
                }
            }
        }
        throw new InvalidToken(new Token(Tag.UNKNOWN, this.reader.getCurrentLine(), lexeme));
    }

    /**
     * Display all tokens from the file in the standard output
     *
     * @throws IOException  if the file cannot be read
     * @throws InvalidToken if the lexer finds an invalid token
     */
    public void displayAllTokens() throws IOException, InvalidToken {
        Token token;
        List<Token> tokens = new ArrayList<>();
        while ((token = this.nextToken()).tag() != Tag.EOF) {
            tokens.add(token);
        }
        tokens.add(token);
        tokens.forEach(System.out::print);
    }

}
