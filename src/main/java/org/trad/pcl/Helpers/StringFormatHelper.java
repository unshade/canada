package org.trad.pcl.Helpers;

import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.semantic.SymbolTable;
import org.trad.pcl.semantic.symbol.Symbol;

import java.util.Map;
import java.util.Set;

import static com.diogonunes.jcolor.Ansi.colorize;

public class StringFormatHelper {
    public static void printTDS(SymbolTable tds, String type, String nodeIdentifier) {
        System.out.println(colorize("Entering new scope", Attribute.GREEN_TEXT()) + " -> creating new SymbolTable for " + colorize(type + " ", Attribute.MAGENTA_TEXT()) + colorize(nodeIdentifier, Attribute.BLUE_TEXT()) + " : ");
        Set<Map.Entry<String, Symbol>> entrySet = tds.getSymbols().entrySet();
        for (Map.Entry<String, Symbol> entry : entrySet) {
            System.out.println("\t" + colorize(entry.getKey(), Attribute.YELLOW_TEXT()) + " -> "+ entry.getValue());
        }
        System.out.println();
    }
}
