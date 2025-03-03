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
        this.warninglogFile = warninglogFile;
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

    public void writeWarnings() {
        if ( !warninglogBuffer.isEmpty() ) {
            try {
                FileUtils.writeStringToFile( warninglogFile, warninglogBuffer.toString(), "UTF-8", true );
            } catch ( IOException e ) {
                System.err.println( "Error writing to warning log file: " + e.getMessage() );
            }
        }
        zeroBuffer();
    }


}