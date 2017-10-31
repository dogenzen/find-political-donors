import analysis.PoliticalContributionAnalysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Main orchestration class
 *
 *  - Provides input and output files
 *  - Initiates contribution analysis
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class Main
{
    private static final String INPUT_DIR = "input";
    private static final String INPUT_FILE = "itcont.txt";
    private static final String INPUT_PATH = INPUT_DIR + "//" + INPUT_FILE;

    private static final String OUTPUT_DIR = "output";
    private static final String MEDIANS_BY_ZIP = "medianvals_by_zip.txt";
    private static final String MEDIANS_BY_DATE = "medianvals_by_date.txt";

    public static void main(String[] args) {
        if (args.length != 3) {
          System.out.println("USAGE: Main <input-file> <output-zip-file> <output-date-file>");
          System.exit(0);
        }
        final String INPUT_PATH = args[0];
        final String OUTPUT_ZIP_FILE_PATH = args[1];
        final String OUTPUT_DATE_FILE_PATH = args[2];

        try (FileWriter fwZip = new FileWriter(OUTPUT_ZIP_FILE_PATH);
             BufferedWriter bwZip = new BufferedWriter(fwZip); PrintWriter outZip = new PrintWriter(bwZip);
             FileWriter fwDate = new FileWriter(OUTPUT_DATE_FILE_PATH);
             BufferedWriter bwDate = new BufferedWriter(fwDate); PrintWriter outDate = new PrintWriter(bwDate)) {

            PoliticalContributionAnalysis politicalContributionAnalysis =
                    new PoliticalContributionAnalysis(outZip, outDate);

            try (Stream<String> stream = Files.lines(Paths.get(INPUT_PATH))) {
                stream.forEach(politicalContributionAnalysis::processEachContribution);
            } catch (IOException ioe) {
                System.out.println("Failed to open input file " + INPUT_PATH);
            }
            politicalContributionAnalysis.processAllContributions();

        } catch (IOException ioe) {
            System.out.println("Failed to locate output files.. ");
        }
    }
}
