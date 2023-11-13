package Helpers;

import Lexer.Tokens.Tag;

public class TagHelper {
    public static String getTagString(Tag tag) {
        return switch (tag) {
            case IDENT -> "IDENT";
            case ENTIER -> "INT";
            case CARACTERE -> "CHAR";
            case SEMICOLON -> ";";
            case PROCEDURE -> "PROCEDURE";
            case IS -> "IS";
            case BEGIN -> "BEGIN";
            case END -> "END";
            case TYPE -> "TYPE";
            case ACCESS -> "ACCESS";
            case RECORD -> "RECORD";
            case COLON -> ":";
            case FUNCTION -> "FUNCTION";
            case RETURN -> "RETURN";
            case OUT -> "OUT";
            case IN -> "IN";
            case IF -> "IF";
            case THEN -> "THEN";
            case ELSIF -> "ELSIF";
            case ELSE -> "ELSE";
            case LOOP -> "LOOP";
            case FOR -> "FOR";
            case REVERSE -> "REVERSE";
            case WHILE -> "WHILE";
            case REM -> "REM";
            case AND -> "AND";
            case OR -> "OR";
            case DOT -> ".";
            case VAL -> "VAL";
            case OPEN_PAREN -> "(";
            case CLOSE_PAREN -> ")";
            case TRUE -> "TRUE";
            case FALSE -> "FALSE";
            case COMMA -> ",";
            case APOSTROPHE -> "'";
            case ADA_TEXT_IO -> "ADA_TEXT_IO";
            case USEADA_TEXT_IO -> "USEADA_TEXT_IO";
            case WITH -> "WITH";
            case PLUS -> "+";
            case MINUS -> "-";
            case MULTI -> "*";
            case DIV -> "/";
            case NOT -> "NOT";
            case EQ -> "=";
            case NE -> "/=";
            case LT -> "<";
            case LE -> "<=";
            case GT -> ">";
            case GE -> ">=";
            case ASSIGN -> ":=";
            case EOF -> "EOF";
            case UNKNOWN -> "UNKNOWN";
            case NULL -> "NULL";
            case NEW -> "NEW";
            case CHARACTER -> "CHARACTER";
        };
    }

}
