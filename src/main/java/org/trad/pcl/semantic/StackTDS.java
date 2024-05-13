package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.DuplicateSymbolException;
import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.Stack;

public class StackTDS extends Stack<SymbolTable>{
    public StackTDS() {
        super();
    }

    //sortir de la portée
    public void exitScope() {
        if (!this.isEmpty()) {
            this.pop();
        }
    }

    public void addSymbolInScopes(Symbol symbol, int line) throws DuplicateSymbolException {
        for (int i = this.size() - 1; i >= 0; i--) {
            Symbol s = this.get(i).findSymbol(symbol.getIdentifier());
            if (s != null) {
                throw new DuplicateSymbolException(symbol.getIdentifier(), line);
            }
        }
        this.peek().addSymbol(symbol);
    }

    public Symbol findSymbolInScopes(String identifier, int line) throws UndefinedVariableException {

        for (int i = this.size() - 1; i >= 0; i--) {
            Symbol s = this.get(i).findSymbol(identifier);
            if (s != null) {
                return s;
            }
        }

        throw new UndefinedVariableException(identifier, line);
    }

    public Symbol findSymbolInScopes(String identifier) {

        for (int i = this.size() - 1; i >= 0; i--) {
            Symbol s = this.get(i).findSymbol(identifier);
            if (s != null) {
                return s;
            }

        }

        assert false : "Variable " + identifier + " not found in any scope";
        return null;
    }
}
