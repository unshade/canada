package org.trad.pcl.ast.expression;

import org.trad.pcl.ast.VisitorElement;
import org.trad.pcl.semantic.SymbolTable;

public interface ExpressionNode extends VisitorElement {

    // Classe de base pour les expressions
    String getType();
}
