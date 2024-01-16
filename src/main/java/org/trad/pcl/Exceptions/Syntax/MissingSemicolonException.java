package org.trad.pcl.Exceptions.Syntax;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Lexer.Tokens.Token;
import static com.diogonunes.jcolor.Ansi.colorize;


public final class MissingSemicolonException extends Exception {
    public MissingSemicolonException(Token got) {
        super("⚠️ " + colorize("Syntax warning: missing semicolon at line " + got.line() + " (got " + got + ")", Attribute.TEXT_COLOR(255, 165, 0)));
    }
}
