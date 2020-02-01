
package uk.ac.leeds.ccg.projects.dw.visualisation.mapping;

import java.nio.file.Path;
import uk.ac.leeds.ccg.geotools.Geotools_Shapefile;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Shapefile extends Geotools_Shapefile {
    
    public DW_Shapefile(Path f) {
        setFile(f);
    }
    
}
