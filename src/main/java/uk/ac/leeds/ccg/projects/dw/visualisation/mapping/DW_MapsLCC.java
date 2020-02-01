
package uk.ac.leeds.ccg.projects.dw.visualisation.mapping;

import java.io.IOException;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.process.DW_Processor;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_MapsLCC extends DW_Maps {

    protected DW_Processor Processor;
    
    public DW_MapsLCC(DW_Environment env) throws IOException {
        super(env);
    }
}
