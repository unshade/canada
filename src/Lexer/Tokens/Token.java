package Lexer.Tokens;

/**
 * Token
 *
 * @param tag    Tokens tag (see Tag.java)
 * @param line   Line number of the token
 * @param lexeme Lexeme of the token
 * @author Noé Steiner
 * @author Alexis Marcel
 * @author Lucas Laurent
 */
public record Token(Tag tag, int line, String lexeme) {
    @Override
    public String toString() {
        return "<" + this.tag + ", " + this.line + ", " + this.lexeme + ">" + " ";
    }
}
