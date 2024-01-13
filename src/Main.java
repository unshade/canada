import Exceptions.BadFileExtension;
import Helpers.FileHelper;
import Lexer.Lexer;
import Parser.Parser;
import Services.ErrorService;
import ast.ASTNode;
import ast.ProgramNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws BadFileExtension, IOException {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file> [options]");
            return;
        }

        Path filePath = Paths.get(args[0]);

        if (!Files.isReadable(filePath)) {
            throw new IOException("File does not exist or cannot be read");
        }

        if (!FileHelper.getFileExtension(filePath).equalsIgnoreCase(Constants.REQUIRED_EXTENSION)) {
            throw new BadFileExtension(Constants.REQUIRED_EXTENSION);
        }

        Lexer lexer = Lexer.getInstance(filePath.toFile());
        ErrorService errorService = ErrorService.getInstance();
        if (args.length > 1 && "-t".equals(args[1])) {
            lexer.displayAllTokens();
        } else {
            Parser parser = Parser.getInstance();
            ProgramNode AST = parser.parse();
            System.out.println(AST);
        }

        if (errorService.hasErrors()) {
            errorService.handleErrorsDisplay();
        }
    }

}
