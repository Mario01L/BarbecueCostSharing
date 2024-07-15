package Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class FileWriteHelper {

    public static void writeToFile(String fileName, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }
}
