package org.trad.pcl.Exceptions;

public final class BadFileExtension extends Exception {
    public BadFileExtension(String extension) {
        super("File extension must be ." + extension);
    }
}
