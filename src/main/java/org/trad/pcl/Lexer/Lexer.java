package org.trad.pcl.Lexer;


import org.trad.pcl.Exceptions.Lexical.UnknownTokenException;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Services.ErrorService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
     * Singleton instance
     */
    private static Lexer instance;
    /**
     * Lexer attributes
     */
    private final PeekingReader reader;
    private final Map<Tag, Pattern> keywords;
    private final Map<Tag, Pattern> ruledTerminals;
    private final Map<Tag, Pattern> operators;
    private final ErrorService errorService;
    StringBuilder lexeme;
    private int currentChar;

    /**
     * Lexer constructor
     *
     * @param file the file to read
     */
    private Lexer(File file) {

        try {
            this.reader = new PeekingReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.lexeme = new StringBuilder();
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
                Map.entry(Tag.COMMA, Pattern.compile(",")),
                Map.entry(Tag.APOSTROPHE, Pattern.compile("'")),
                Map.entry(Tag.ADA_TEXT_IO, Pattern.compile("Ada.Text_IO")),
                Map.entry(Tag.USEADA_TEXT_IO, Pattern.compile("useAda.Text_IO")),
                Map.entry(Tag.WITH, Pattern.compile("with")),
                Map.entry(Tag.NULL, Pattern.compile("null")),
                Map.entry(Tag.NEW, Pattern.compile("new")),
                Map.entry(Tag.CHARACTER, Pattern.compile("character"))
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
                Map.entry(Tag.ASSIGN, Pattern.compile(":=")),
                Map.entry(Tag.DOTDOT, Pattern.compile("\\.\\."))
        );

        this.ruledTerminals = Map.of(
                Tag.IDENT, Pattern.compile("[A-Za-z][A-Za-z0-9_]*"),
                Tag.ENTIER, Pattern.compile("[0-9]+"),
                Tag.CARACTERE, Pattern.compile("'[\\x00-\\x26\\x28-\\x7F]'")
        );

        this.errorService = ErrorService.getInstance();
    }

    public static Lexer getInstance() {
        if (!(instance == null)) {
            return instance;
        }
        return null;
    }

    public static Lexer getInstance(File file) {

        if (instance == null) {
            instance = new Lexer(file);
        }

        return instance;
    }

    public static Lexer newInstance(File file) {
        return new Lexer(file);
    }

    /**
     * Get the next token from the file
     *
     * @return the next token
     */
    public Token nextToken() {

        while ((this.currentChar = this.reader.read()) != -1) {

            if (this.isComment()) {
                this.skipComment();
            } else if (Character.isWhitespace((char) currentChar)) {
                this.skipWhitespace();
            } else if (isCharacterLiteral()) {
                return this.readCharacterLiteral();
            } else {
                lexeme.append((char) currentChar);
                if (this.isEndOfToken()) {
                    Token token = this.matchToken(lexeme.toString());
                    lexeme.setLength(0); // clear the StringBuilder
                    if (token.tag() == Tag.UNKNOWN) {
                        this.errorService.registerLexicalError(new UnknownTokenException(token));
                    }
                    return token;
                }
            }
        }

        this.reader.close();
        return new Token(Tag.EOF, this.reader.getCurrentLine(), lexeme.toString());
    }

    /**
     * Check if the current character is the beginning of a comment
     *
     * @return true if the current character is the beginning of a comment, false otherwise
     */
    private boolean isComment() {
        return this.currentChar == '-' && this.reader.peek(1) == '-';
    }

    /**
     * Skip the current comment
     */
    private void skipComment() {
        while (this.currentChar != '\n' && this.currentChar != -1) {
            this.currentChar = this.reader.read();
        }
    }

    private boolean isCharacterLiteral() {
        return this.currentChar == '\'' && this.reader.peek(1) != '\n' && this.reader.peek(2) == '\'';
    }

    private Token readCharacterLiteral() {
        // On a déjà lu l'apostrophe de début, on passe au caractère suivant
        currentChar = this.reader.read();
        char charValue = (char) currentChar;

        // Lire le caractère suivant, qui devrait être une apostrophe fermante
        currentChar = this.reader.read();

        return new Token(Tag.CARACTERE, this.reader.getCurrentLine(), String.valueOf(charValue));
    }

    /**
     * Check if the current character is the end of a token
     *
     * @return true if the current character is the end of a token, false otherwise
     */
    private boolean isEndOfToken() {
        char current = (char) currentChar;
        int nextInt = this.reader.peek(1);
        char next = (char) nextInt;

        boolean isCurrentLetterOrDigit = Character.isLetterOrDigit(current) || current == '_';
        boolean isNextLetterOrDigit = Character.isLetterOrDigit(next) || next == '_';
        boolean isNextWhitespace = Character.isWhitespace(next);

        // If the current character is a whitespace or the end of the file, the current character is the end of the token
        if (nextInt == -1 || isNextWhitespace) {
            return true;
        }
        // If the current character is an identifier or an integer, the next character must not be a letter or a digit
        if (isCurrentLetterOrDigit) {
            return !isNextLetterOrDigit;
        }

        Token token = this.matchToken(lexeme.toString());

        // If the current character is a token and the next character is not a token, the current character is the end of the token
        if (token.tag() != Tag.UNKNOWN) {
            Token nextToken = this.matchToken(lexeme.toString() + next);
            return nextToken.tag() == Tag.UNKNOWN;
        }

        return false;
    }

    /**
     * Skip all whitespaces
     */
    private void skipWhitespace() {
        while (Character.isWhitespace((char) this.reader.peek(1))) {
            this.currentChar = this.reader.read();
        }
    }

    /**
     * Match the lexeme with a pattern and return the corresponding token
     *
     * @param lexeme the lexeme to match
     * @return the corresponding token
     */
    private Token matchToken(String lexeme) {
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
        return new Token(Tag.UNKNOWN, this.reader.getCurrentLine(), lexeme);
    }

    /**
     * Display all tokens from the file in the standard output
     */
    public void displayAllTokens() {
        List<Token> tokens = this.getAllTokens();
        tokens.forEach(System.out::print);
    }

    public List<Token> getAllTokens() {
        Token token;
        List<Token> tokens = new ArrayList<>();
        while ((token = this.nextToken()).tag() != Tag.EOF) {
            tokens.add(token);
        }
        tokens.add(token);
        return tokens;
    }

}
