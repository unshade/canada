package org.trad.pcl.Services;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

import java.util.ArrayList;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public final class ErrorService {

    private static ErrorService instance;

    private final List<Exception> lexicalErrors;
    private final List<Exception> syntaxErrors;
    private final List<Exception> semanticErrors;
    private final List<Exception> syntaxWarnings;

    private ErrorService() {
        this.lexicalErrors = new ArrayList<>();
        this.syntaxErrors = new ArrayList<>();
        this.semanticErrors = new ArrayList<>();
        this.syntaxWarnings = new ArrayList<>();
    }

    public static ErrorService getInstance() {
        if (!(instance == null)) {
            return instance;
        }
        instance = new ErrorService();
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void registerLexicalError(Exception e) {
        this.lexicalErrors.add(e);
    }

    public void registerSyntaxError(Exception e) {
        this.syntaxErrors.add(e);
    }

    public void registerSyntaxWarning(Exception e) {
        this.syntaxWarnings.add(e);
    }

    public void registerSemanticError(Exception e) {
        this.semanticErrors.add(e);
    }

    public boolean hasNoErrors() {
        return this.lexicalErrors.isEmpty() && this.syntaxErrors.isEmpty() && this.semanticErrors.isEmpty();
    }

    public void handleErrorsDisplay() {
        if (!this.lexicalErrors.isEmpty()) {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.RED_BACK(), Attribute.BOLD());
            System.err.println("\n❌ " + colorize("LISTING LEXICAL ERRORS :", fWarning));
            for (Exception e : this.lexicalErrors) {
                System.err.println("\t" + e.getMessage());
            }
            System.out.println();
        }
        if (!this.syntaxErrors.isEmpty()) {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.RED_BACK(), Attribute.BOLD());
            System.err.println("\n❌ " + colorize("LISTING SYNTAX ERRORS :", fWarning));
            for (Exception e : this.syntaxErrors) {
                System.err.println("\t" + e.getMessage());
            }
            System.out.println();
        }
        if (!this.semanticErrors.isEmpty()) {
            System.err.println("Semantic errors:");
            for (Exception e : this.semanticErrors) {
                System.err.println("\t" + e.getMessage());
            }
        }
        if (!this.syntaxWarnings.isEmpty()) {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.YELLOW_BACK(), Attribute.BOLD());
            System.err.println("\n⚠️ " + colorize("LISTING SYNTAX WARNINGS :", fWarning));
            for (Exception e : this.syntaxWarnings) {
                System.err.println("\t" + e.getMessage());
            }
            System.out.println();
        }
    }

    public List<Exception> getLexicalErrors() {
        return lexicalErrors;
    }
}
