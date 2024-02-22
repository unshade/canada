package org.trad.pcl.semantic;

import org.trad.pcl.semantic.symbol.Symbol;

import java.util.HashMap;

public class TDS {
    private final HashMap<String, Symbol> symbols;

    private TDS before;

    private TDS() {
        this.symbols = new HashMap<>();
    }
}
