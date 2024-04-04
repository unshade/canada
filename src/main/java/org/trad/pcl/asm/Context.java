package org.trad.pcl.asm;

public class Context {

    private String callerName;

    private static Context instance;

    private Context() {
        callerName = "unknown, that's bad bruh";
    }

    public static Context background() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerName() {
        return callerName;
    }


}
