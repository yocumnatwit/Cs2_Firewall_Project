package components.warnman;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class WarningManager {
    private static StringBuffer warninglogBuffer = new StringBuffer();
    private static File warninglogFile = new File("warninglog.txt");

    /**
     * Null constructor for the WarningManager class.
     * This constructor initializes the warninglogBuffer and warninglogFile called warninglog.txt.
     **/   
    public WarningManager() {}

    /**
     * Constructor for the WarningManager class.
     * This constructor initializes the warninglogBuffer and warninglogFile with the given file.
     * @param warninglogFile The file to be used for the warning log.
     **/
    public WarningManager( File warninglogFile ) {
        WarningManager.warninglogFile = warninglogFile;
    }

    /**
     * This method checks if the warninglogFile exists.
     * If it does not exist, it creates a new file.
     * It is used to ensure that the warning log file is available for writing.
     */
    private static void checkFileExistence() {
        if ( !warninglogFile.exists() ) {
            try {
                boolean output = warninglogFile.createNewFile();
                if ( !output ) {
                    System.err.println( "Error creating warning log file." );
                }
            } catch ( IOException e ) {
                System.err.println( "Error creating warning log file: " + e.getMessage() );
            }
        }
    }

    /**
     * This method zeros the warninglogBuffer.
     * It is used to clear the buffer after the warnings have been logged.
     **/
    private static void zeroBuffer() {
       warninglogBuffer = new StringBuffer();
    }

    /**
     * This method writes a warning to the warninglogBuffer.
     * @param warning The warning to be written to the buffer.
     **/
    public static void writeWarning( String warning ) {
        warninglogBuffer.append( String.format( "%s%n", warning ) );
    }

    /**
     * This method writes the warnings from the warninglogBuffer to the warninglogFile.
     * It checks if the buffer is not empty before writing to the file.
     */
    public void writeWarnings() {

        checkFileExistence();
        

        if ( !warninglogBuffer.isEmpty() ) {
            try {
                FileUtils.writeStringToFile( warninglogFile, warninglogBuffer.toString(), "UTF-8", true );
            } catch ( IOException e ) {
                System.err.println( "Error writing to warning log file: " + e.getMessage() );
            }

            zeroBuffer();
        }

    }

    /**
     * This method writes an array of warnings to the warninglogBuffer.
     * It iterates over the array and writes each warning to the buffer.
     * After writing all warnings, it calls the writeWarnings() method to write the buffer to the file.
     * @param warnings The array of warnings to be written to the buffer.
     */
    public void writeWarnings( String[] warnings ) {

        for (String warning : warnings) {
            writeWarning(warning);
        }

        writeWarnings();
    }

    /**
     * Prints the content of the warning log file to the console.
     * This method reads the content of the warninglogFile and outputs it to the standard output.
     * If an error occurs during the file reading process, an error message is displayed on the standard error output.
     */
    public static void printLogFile() {
        String content = readLogs();
        if ( content != null ) {
            System.out.println( content );
        } else {
            System.err.println( "Error reading warning log file." );
        }
    }

    /**
     *
     */
    private static String readLogs() {
        try {
            return FileUtils.readFileToString(warninglogFile, "UTF-8");
        } catch (IOException e) {
            System.err.println("Error reading warning log file: " + e.getMessage());
        }
        return null;
    }

    public static void startWarningManager() {

    }

    public static void stopWarningManager() {

    }
}