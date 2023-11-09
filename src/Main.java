import Exceptions.BadFileExtension;
import Exceptions.Lexical.InvalidToken;
import Helpers.FileHelper;
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

        if (!FileHelper.getFileExtension(filePath).equalsIgnoreCase(REQUIRED_EXTENSION)) {
            throw new BadFileExtension(REQUIRED_EXTENSION);
        }

        if (args.length > 1 && "-t".equals(args[1])) {
            Lexer lexer = new Lexer(filePath.toFile());
            lexer.displayAllTokens();
        }
    }

}
