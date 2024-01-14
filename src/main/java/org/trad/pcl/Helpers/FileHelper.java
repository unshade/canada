package org.trad.pcl.Helpers;

import java.io.File;

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
     * @param file the file
     * @return the file extension
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
