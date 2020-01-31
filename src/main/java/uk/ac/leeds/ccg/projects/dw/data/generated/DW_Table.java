
package uk.ac.leeds.ccg.projects.dw.data.generated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;

/**
 * A class for methods dealing with tables of data.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Table extends DW_Object {

    public DW_Table(DW_Environment e) {
        super(e);
    }

    /**
     * Reads a CSV file and returns each line as a String in the result.
     *
     * @param f file
     * @return the contents of the file as an {@code ArrayList<String>} with the
     * elements being consecutive lines in the file.
     */
    public ArrayList<String> readCSV(Path f) {
        ArrayList<String> r = new ArrayList<>();
        try (BufferedReader br = Generic_IO.getBufferedReader(f)) {
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_IO.setStreamTokenizerSyntax1(st);
            String line = "";
            int tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        r.add(line);
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
        } catch (IOException ex) {
            Logger.getLogger(DW_Table.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

}
