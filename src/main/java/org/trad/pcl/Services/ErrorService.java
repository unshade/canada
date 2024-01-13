package org.trad.pcl.Services;

import java.util.ArrayList;
import java.util.List;

public class ErrorService {

    private static ErrorService instance;

    private final List<Exception> lexicalErrors;
    private final List<Exception> syntaxErrors;
    private final List<Exception> semanticErrors;

    private ErrorService() {
        this.lexicalErrors = new ArrayList<>();
        this.syntaxErrors = new ArrayList<>();
        this.semanticErrors = new ArrayList<>();
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

    public void registerSemanticError(Exception e) {
        this.semanticErrors.add(e);
    }

    public boolean hasErrors() {
        return !this.lexicalErrors.isEmpty() || !this.syntaxErrors.isEmpty() || !this.semanticErrors.isEmpty();
    }

    public void handleErrorsDisplay() {
        if (!this.lexicalErrors.isEmpty()) {
            System.err.println("Lexical errors:");
            for (Exception e : this.lexicalErrors) {
                System.err.println("\t" + e.getMessage());
            }
            System.out.println();
        }
        if (!this.syntaxErrors.isEmpty()) {
            System.err.println("Syntax errors:");
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
    }

    public List<Exception> getLexicalErrors() {
        return lexicalErrors;
    }
}