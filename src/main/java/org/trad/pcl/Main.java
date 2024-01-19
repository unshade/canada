package org.trad.pcl;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.trad.pcl.Exceptions.BadFileExtension;
import org.trad.pcl.Helpers.FileHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.Services.PythonRunner;
import org.trad.pcl.ast.ProgramNode;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.diogonunes.jcolor.Ansi.colorize;

public final class Main {

    public static void main(String[] args) throws BadFileExtension, IOException {
        if (args.length < 1) {
            System.err.println("Usage: java Main <file> [options]");
            return;
        }

        String argPath = args[0];
        System.out.println("Parsing file: " + argPath);
        //String argPath = "/test.canAda";
        File file = new File(Objects.requireNonNull(Main.class.getResource(argPath)).getFile());
        if (!file.exists() || !file.canRead()) {
            throw new IOException("File does not exist or cannot be read");
        }

        if (!FileHelper.getFileExtension(file).equalsIgnoreCase(Constants.REQUIRED_EXTENSION)) {
            throw new BadFileExtension(Constants.REQUIRED_EXTENSION);
        }

        Lexer lexer = new Lexer(file);
        ErrorService errorService = ErrorService.getInstance();
        if (args.length > 1 && "-t".equals(args[1])) {
            lexer.displayAllTokens();
        } else {
            Parser parser = new Parser(lexer);
            ProgramNode AST = parser.parse();
            if (errorService.hasNoErrors()) {
                AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.GREEN_BACK(), Attribute.BOLD());
                System.out.println("\n✅ " + colorize("PARSING PHASE COMPLETED, GENERATING AST", fWarning));
                System.out.println(AST);
                if (args.length > 1 && "-g".equals(args[1])) {
                    AST.setIsJson(true);
                    String json = AST.toString();
                    PythonRunner.exec(json);
                }
            } else {
                AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.RED_BACK(), Attribute.BOLD());
                System.out.println("\n❌ " + colorize("PARSING PHASE FAILED, STOPPING", fWarning));
            }

        }

        errorService.handleErrorsDisplay();

    }

}
