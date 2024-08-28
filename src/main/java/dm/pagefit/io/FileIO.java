package dm.pagefit.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class FileIO
{
    /**
     * Return an ArrayList of String Arrays, split using the given delimiter
     * @param filename file in current working directory or full pathname
     * @param delimiter REGEX string delimiter. Catches PatternSyntaxException.
     * @return List of String Arrays
     */
    public static List<String[]> getFileLinesSplit(final String filename, String delimiter)
    {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {
            String input;
            while ((input = br.readLine()) != null) {
                try {
                    input = input.trim();
                    String[] s = input.split(delimiter);
                    list.add(s);
                } catch (PatternSyntaxException pse) {
                    System.err.println("Bad regex syntax. Delimiter: \"" + delimiter + "\"");
                    return new ArrayList<>();
                }
            }
        } catch (IOException ioe) {
            System.err.println("Error reading file: " + filename);
            ioe.printStackTrace();
            return new ArrayList<>();
        }
        return list;
    }

    /**
     * Writes a list of string arrays to a file, joining each array into a line
     * using the provided delimiter.
     *
     * @param filename  file in the current working directory or full pathname
     * @param delimiter delimiter to use when joining string arrays into lines
     * @param data      list of string arrays to be written to the file
     * @return boolean  true if writing was successful, false otherwise
     */
    public static boolean writeFileLines(final String filename, String delimiter, List<String[]> data) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename)))
        {
            for (String[] lineArray : data) {
                String line = String.join(delimiter, lineArray);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ioe) {
            System.err.println("Error writing to file: " + filename);
            ioe.printStackTrace();
            return false; // Indicate that the writing process failed
        }
        return true; // Indicate that the writing process succeeded
    }
}