package org.trad.pcl.ast;

import org.trad.pcl.semantic.ASTNodeVisitor;

public interface VisitorElement {
    void accept(ASTNodeVisitor visitor) throws Exception;
}
