package org.trad.pcl.semantic;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Exceptions.Semantic.DuplicateSymbolException;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;

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

    public void addSymbol(Symbol symbol) {
        symbol.setShift(currentShift+=symbol.getShift());
        symbols.put(symbol.getIdentifier(), symbol);
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
        StringBuilder sb = new StringBuilder();
        //System.out.println(colorize("Entering new scope", Attribute.GREEN_TEXT()) + " -> creating new SymbolTable for " + colorize(type + " ", Attribute.MAGENTA_TEXT()) + colorize(nodeIdentifier, Attribute.BLUE_TEXT()) + " : ");
        sb.append(colorize("Entering new scope", Attribute.GREEN_TEXT())).append(" -> creating new SymbolTable for ").append(colorize(scopeIdentifier + " ", Attribute.MAGENTA_TEXT())).append(" : \n");
        List<Map.Entry<String, Symbol>> entryList = new ArrayList<>(this.getSymbols().entrySet());
        entryList.sort(Comparator.comparingInt(e -> e.getValue().getShift()));
        for (Map.Entry<String, Symbol> entry : entryList) {
            sb.append("\t").append(colorize(entry.getKey(), Attribute.YELLOW_TEXT())).append(" -> ").append(entry.getValue()).append("\n");
        }
        sb.append("\n");
        //System.out.println();
        return sb.toString();
    }


}
