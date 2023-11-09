package Helpers;

import java.nio.file.Path;

/**
 * FileHelper class
 *
 * @author Noé Steiner
 * @author Alexis Marcel
 * @author Lucas Laurent
 */
public class FileHelper {

    /**
     * Get the file extension of a path
     *
     * @param path the path
     * @return the file extension
     */
    public static String getFileExtension(Path path) {
        String fileName = path.toString();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
