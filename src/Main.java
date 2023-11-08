import Exceptions.BadFileExtension;
import Exceptions.Lexical.InvalidToken;
import Lexer.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String REQUIRED_EXTENSION = "canAda";

    public static void main(String[] args) throws BadFileExtension, IOException, InvalidToken {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file> [options]");
            return;
        }

        Path filePath = Paths.get(args[0]);

        if (!Files.isReadable(filePath)) {
            throw new IOException("File does not exist or cannot be read");
        }

        if (!getFileExtension(filePath).equalsIgnoreCase(REQUIRED_EXTENSION)) {
            throw new BadFileExtension("File extension must be ." + REQUIRED_EXTENSION);
        }

        if (args.length > 1 && "-t".equals(args[1])) {
            Lexer lexer = new Lexer(filePath.toFile());
            lexer.displayAllTokens();
        }
    }

    private static String getFileExtension(Path path) {
        String fileName = path.toString();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
