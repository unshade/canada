package org.trad.pcl.Lexer.Tokens;
import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;


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
        return colorize("<", Attribute.YELLOW_TEXT()) + colorize(String.valueOf(this.tag), Attribute.RED_TEXT()) + ", " + colorize(String.valueOf(this.line), Attribute.BRIGHT_MAGENTA_TEXT()) + ", " + colorize(this.lexeme, Attribute.BLUE_TEXT()) + colorize(">", Attribute.YELLOW_TEXT()) + " ";
    }

    public String getValue() {
        return this.lexeme;
    }
}
