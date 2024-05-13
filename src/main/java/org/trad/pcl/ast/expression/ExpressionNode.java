package org.trad.pcl.ast.expression;

import org.trad.pcl.Exceptions.Semantic.UndefinedVariableException;
import org.trad.pcl.Helpers.TypeEnum;
import org.trad.pcl.ast.VisitorElement;
import org.trad.pcl.semantic.StackTDS;
import org.trad.pcl.semantic.SymbolTable;

public interface ExpressionNode extends VisitorElement {

    // Classe de base pour les expressions
    String getType(StackTDS stack) throws UndefinedVariableException;
}
