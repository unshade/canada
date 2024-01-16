import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.trad.pcl.Constants;
import org.trad.pcl.Exceptions.BadFileExtension;
import org.trad.pcl.Helpers.FileHelper;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Main;
import org.trad.pcl.Parser.Parser;
import org.trad.pcl.Services.ErrorService;
import org.trad.pcl.ast.ProgramNode;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

import static com.diogonunes.jcolor.Ansi.colorize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParseTest {

    private static Stream<Path> provideCanAdaFiles() throws URISyntaxException, IOException {
        return Files.walk(Path.of(Objects.requireNonNull(Main.class.getResource("/tests/typing/good")).toURI()), 1)
                .filter(Files::isRegularFile);
    }

    @ParameterizedTest
    @MethodSource("provideCanAdaFiles")
    public void testCanAdaFile(Path filePath) {
        try {
            System.out.println("Testing file: " + filePath);
            File file = filePath.toFile();
            if (!file.exists() || !file.canRead()) {
                throw new IOException("File does not exist or cannot be read");
            }
            assertTrue(run("/tests/typing/good/" + file.getName()));
        } catch (BadFileExtension | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean run(String argPath) throws BadFileExtension, IOException {
        System.out.println("Testing file: " + argPath);
        File file = new File(Objects.requireNonNull(Main.class.getResource(argPath)).getFile());
        if (!file.exists() || !file.canRead()) {
            throw new IOException("File does not exist or cannot be read");
        }

        if (!FileHelper.getFileExtension(file).equalsIgnoreCase(Constants.REQUIRED_EXTENSION)) {
            throw new BadFileExtension(Constants.REQUIRED_EXTENSION);
        }

        Lexer lexer = Lexer.newInstance(file);
        ErrorService.resetInstance();
        ErrorService errorService = ErrorService.getInstance();
        Parser parser = Parser.newInstance();
        ProgramNode AST = parser.parse();
        if (errorService.hasNoErrors()) {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.GREEN_BACK(), Attribute.BOLD());
            System.out.println("\n✅ " + colorize("PARSING PHASE COMPLETED, GENERATING AST", fWarning));
            return true;
        } else {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.RED_BACK(), Attribute.BOLD());
            System.out.println("\n❌ " + colorize("PARSING PHASE FAILED, STOPPING", fWarning));
            errorService.handleErrorsDisplay();
            return false;
        }
    }
}

