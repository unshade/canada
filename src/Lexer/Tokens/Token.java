package Lexer.Tokens;

public record Token(Tag tag, int line, String lexeme) {
    @Override
    public String toString() {
        return "<" + this.tag + ", " + this.line + ", " + this.lexeme + ">" + " ";
    }
}
