package org.trad.pcl.semantic.symbol;

import org.trad.pcl.Helpers.TypeEnum;

public class Access extends Type {

    private String typeAccess;

      public Access(String identifier, int shift) {
          super(identifier, shift);
      }

        public void setTypeAccess(String typeAccess) {
            this.typeAccess = typeAccess;
        }
}
