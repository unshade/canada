package org.trad.pcl.semantic;

import org.trad.pcl.Exceptions.Semantic.DuplicateSymbolException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {

    private final HashMap<String, Symbol> symbols;
    private final ErrorService errorService;

    private final String scopeIdentifier;

    private int currentShift;

    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.errorService = ErrorService.getInstance();
        this.currentShift = 0;
        this.scopeIdentifier = null;
    }

    public SymbolTable(String scopeIdentifier) {
        this.symbols = new HashMap<>();
        this.errorService = ErrorService.getInstance();
        this.currentShift = 0;
        this.scopeIdentifier = scopeIdentifier;
    }

    public void addSymbol(Symbol symbol, int shift) {
        if (symbols.containsKey(symbol.getIdentifier())) {
            errorService.registerSemanticError(new DuplicateSymbolException(symbol.getIdentifier()));
        } else {
            currentShift += shift;
            symbol.setShift(currentShift);
            symbols.put(symbol.getIdentifier(), symbol);
        }
    }

    public Symbol findSymbol(String identifier) {
       return symbols.get(identifier);
    }

    public HashMap<String, Symbol> getSymbols() {
        return symbols;
    }

    public String getScopeIdentifier() {
        return scopeIdentifier;
    }

    @Override
    public String toString() {
        List<String[]> liste = new ArrayList<>();

        this.symbols.forEach((k, v) -> liste.add(v.toStringArray()));

        // Détermination du nombre maximum de colonnes
        int maxColumns = 0;
        for (String[] row : liste) {
            maxColumns = Math.max(maxColumns, row.length);
        }

        // Calcul de la largeur maximale de chaque colonne
        int[] maxWidth = new int[maxColumns];
        for (String[] row : liste) {
            for (int i = 0; i < row.length; i++) {
                maxWidth[i] = Math.max(maxWidth[i], row[i].length());
            }
        }

        StringBuilder sb = new StringBuilder();

        // Affichage du tableau
        for (String[] row : liste) {
            for (int i = 0; i < row.length; i++) {
                // Affichage avec padding adapté
                //System.out.printf("%-" + (maxWidth[i] + 2) + "s", row[i]);
                sb.append(String.format("%-" + (maxWidth[i] + 2) + "s", row[i]));
            }
            //System.out.println();
            sb.append("\n");
        }
        return sb.toString();
    }


}
