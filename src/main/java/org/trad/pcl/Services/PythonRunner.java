package org.trad.pcl.Services;

import com.diogonunes.jcolor.AnsiFormat;
import com.diogonunes.jcolor.Attribute;

import java.io.IOException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PythonRunner {
    public static void exec(String json) {
        try {
            String pythonScriptPath = "python/ast_graphe.py";
            String pythonInterpreterPath = "python/venv/bin/python3";
            if (System.getProperty("os.name").startsWith("Windows")) {
                pythonInterpreterPath = "python\\venv\\Scripts\\python.exe";
            }
            String cleanedJson = json.replaceAll("\u001B\\[[;\\d]*m", "");
            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreterPath, pythonScriptPath, cleanedJson);
            processBuilder.start().waitFor();

        } catch (IOException | InterruptedException e) {
            AnsiFormat fWarning = new AnsiFormat(Attribute.WHITE_TEXT(), Attribute.RED_BACK(), Attribute.BOLD());
            System.out.println("\n❌ " + colorize("ERROR WHILE ATTEMPTING TO GRAPH AST", fWarning));
        }
    }
}
