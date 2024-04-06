package org.trad.pcl.asm;

public class Context {

    private String callerName;

    private static Context instance;

    private boolean isLeftOperand;

    public int counter = 0;

    private int nonCallableDeclarationWriteLine;
    private Context() {
        callerName = "unknown, that's bad bruh";
        nonCallableDeclarationWriteLine = 0;
        isLeftOperand = false;
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

    public int getNonCallableDeclarationWriteLine() {
        return nonCallableDeclarationWriteLine;
    }

    public void setNonCallableDeclarationWriteLine(int nonCallableDeclarationWriteLine) {
        this.nonCallableDeclarationWriteLine = nonCallableDeclarationWriteLine;
    }

    public boolean isLeftOperand() {
        return isLeftOperand;
    }

    public void setLeftOperand(boolean leftOperand) {
        isLeftOperand = leftOperand;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
