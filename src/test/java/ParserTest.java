import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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

public class ParserTest {

    private static Stream<Arguments> provideGoodFiles() throws URISyntaxException, IOException {
        Stream<Path> goodFiles = Files.walk(Path.of(Objects.requireNonNull(Main.class.getResource("/tests/Parser/good")).toURI()), 1)
                .filter(Files::isRegularFile);
        return goodFiles.map(path -> Arguments.of(path.getFileName().toString(),path, true));
    }

    private static Stream<Arguments> provideBadFiles() throws URISyntaxException, IOException {
        Stream<Path> badFiles = Files.walk(Path.of(Objects.requireNonNull(Main.class.getResource("/tests/Parser/bad")).toURI()), 1)
                .filter(Files::isRegularFile);
        return badFiles.map(path -> Arguments.of(path.getFileName().toString(),path, false));
    }

    @ParameterizedTest(name = "Test Good File: {0}")
    @MethodSource("provideGoodFiles")
    public void testGoodFiles(String fileName, Path filePath, boolean expected) {
        testFile(filePath, expected);
    }

    @ParameterizedTest(name = "Test Bad File: {0}")
    @MethodSource("provideBadFiles")
    public void testBadFiles(String fileName, Path filePath, boolean expected) {
        testFile(filePath, expected);
    }

    private void testFile(Path filePath, boolean expected) {
        try {
            System.out.println("Testing file: " + filePath);
            File file = new File(filePath.toString());
            if (!file.exists() || !file.canRead()) {
                throw new IOException("File does not exist or cannot be read");
            }

            if (!FileHelper.getFileExtension(file).equalsIgnoreCase(Constants.REQUIRED_EXTENSION)) {
                throw new BadFileExtension(Constants.REQUIRED_EXTENSION);
            }
            assertEquals(run(file), expected);
        } catch (BadFileExtension | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean run(File file) throws BadFileExtension, IOException {
        Lexer lexer = new Lexer(file);
        ErrorService.resetInstance();
        ErrorService errorService = ErrorService.getInstance();
        Parser parser = new Parser(lexer);
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

