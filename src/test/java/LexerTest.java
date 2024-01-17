import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.trad.pcl.Lexer.Lexer;
import org.trad.pcl.Lexer.Tokens.Tag;
import org.trad.pcl.Lexer.Tokens.Token;
import org.trad.pcl.Services.ErrorService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LexerTest {


    private static List<Token> readSolutionTokens(File solutionFile) throws IOException {
        String content = Files.readString(solutionFile.toPath());
        List<Token> tokens = new ArrayList<>();

        String[] tokenStrings = content.split(">\\s*<");
        for (String tokenString : tokenStrings) {
            tokenString = tokenString.replace("<", "").replace(">", "");
            if (tokenString.isEmpty()) {
                continue;
            }
            String[] parts = tokenString.split(",\\s", -1);
            if (parts.length != 3) {
                throw new IOException("Format de token invalide dans le fichier de solution : " + tokenString);
            }
            Tag tag = Tag.valueOf(parts[0].trim());
            int lineNumber = Integer.parseInt(parts[1].trim());
            String value = parts[2];
            tokens.add(new Token(tag, lineNumber, value));
        }

        return tokens;
    }

    private void performLexerTest(File testFile, List<Token> expectedTokens) {
        try {
            ErrorService.resetInstance();

            Lexer lexer = new Lexer(testFile);
            List<Token> actualTokens = lexer.getAllTokens();

            assertEquals(expectedTokens, actualTokens);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    static Stream<TestData> testDataProvider() throws IOException {
        Path testFolder = Paths.get("src", "main", "resources", "tests", "lexer", "tests");
        Path solutionFolder = Paths.get("src", "main", "resources", "tests", "lexer", "solutions");

        return Files.list(testFolder)
                .filter(file -> file.toString().endsWith(".canAda"))
                .map(testFile -> {
                    File solutionFile = solutionFolder.resolve(testFile.getFileName().toString().replace(".canAda", "_solution.txt")).toFile();
                    try {
                        List<Token> expectedTokens = readSolutionTokens(solutionFile);
                        return new TestData(testFile.toFile(), expectedTokens);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Error reading solution file: " + solutionFile, e);
                    }
                });
    }

    @ParameterizedTest
    @MethodSource("testDataProvider")
    public void testLexer(TestData testData) {
        performLexerTest(testData.getTestFile(), testData.getExpectedTokens());
    }

    private static class TestData {
        private final File testFile;
        private final List<Token> expectedTokens;

        public TestData(File testFile, List<Token> expectedTokens) {
            this.testFile = testFile;
            this.expectedTokens = expectedTokens;
        }

        public File getTestFile() {
            return testFile;
        }

        public List<Token> getExpectedTokens() {
            return expectedTokens;
        }

        @Override
        public String toString() {
            return testFile.getName();
        }
    }

}
