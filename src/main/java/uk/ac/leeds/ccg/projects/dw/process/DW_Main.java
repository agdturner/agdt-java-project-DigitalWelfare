
package uk.ac.leeds.ccg.projects.dw.process;

import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.process.lcc.DW_ProcessorLCC;

/**
 * This is the main processing class for the project.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Main extends DW_Processor {

    public DW_Main(DW_Environment env) {
        super(env);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.err.println("Expected an argument which is the location "
                        + "of the directory containing the input data. "
                        + "Aborting.");
                System.exit(0);
            } else {
                Path dataDir = Paths.get(args[1]);
                Generic_Environment ge = new Generic_Environment(
                        new Generic_Defaults(dataDir));
                Data_Environment de = new Data_Environment(ge);
                DW_Environment env = new DW_Environment(de);
                DW_Main p;
                p = new DW_Main(env);
//                p.env.shbeEnv = new SHBE_Environment(p.files.getDataDir(), 
//                SHBE_Environment.DEBUG_Level_NORMAL);
//                p.env.ONSPD_Env = new ONSPD_Environment(p.files.getDataDir());
                p.run();
                env.ge.closeLog(0);
            }
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
            StackTraceElement[] stes = e.getStackTrace();
            for (StackTraceElement ste : stes) {
                System.err.println(ste.toString());
            }
        }
    }

    /**
     * This is the main run method for the Digital welfare project.
     *
     * @throws Exception
     */
    public void run() throws Exception, Error {
        /**
         * Run Advice Leeds processing
         */

        /**
         * Run LCC SHBE data processing
         */
        DW_ProcessorLCC p = new DW_ProcessorLCC(env);
        p.run();
    }

}
