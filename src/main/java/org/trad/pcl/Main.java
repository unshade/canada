package org.trad.pcl;

import org.trad.pcl.Exceptions.BadFileExtension;
import org.trad.pcl.Helpers.FileHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ProgramNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws BadFileExtension, IOException {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file> [options]");
            return;
        }

        // String argPath = args[0];
        String argPath = "/test.canAda";
        File file = new File(Objects.requireNonNull(Main.class.getResource(argPath)).getFile());
        if (!file.exists() || !file.canRead()) {
            throw new IOException("File does not exist or cannot be read");
        }

        if (!FileHelper.getFileExtension(file).equalsIgnoreCase(Constants.REQUIRED_EXTENSION)) {
            throw new BadFileExtension(Constants.REQUIRED_EXTENSION);
        }

        Lexer lexer = Lexer.getInstance(file);
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
