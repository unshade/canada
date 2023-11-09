package Exceptions;

public class BadFileExtension extends Exception {
    public BadFileExtension(String extension) {
        super("File extension must be ." + extension);
    }
}
