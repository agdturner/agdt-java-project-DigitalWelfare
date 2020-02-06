
package uk.ac.leeds.ccg.projects.dw.data.uo;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_UO_Set extends DW_Object implements Serializable {

    /**
     * DW_UO_Records indexed by ClaimID
     */
    protected HashMap<SHBE_ClaimID, DW_UO_Record> Map;

    public DW_UO_Set(DW_Environment env) {
        super(env);
        Map = new HashMap<>();
    }

    /**
     * If reload == true then this reloads data from source. Otherwise it checks
     * to see if a generated file exists to load the data from there. If the
     * generated file exists, the set is loaded from there, otherwise, the set
     * is reloaded from source and saved where the generated file is saved. The
     * result is returned.
     *
     * @param env
     * @param type Indicates type, e.g. RSL, Council.
     * @param filename
     * @param YM3
     * @param reload If true this forces a reload of the data.
     */
    public DW_UO_Set(DW_Environment env, String type, String filename,
            UKP_YM3 YM3, boolean reload) throws IOException, ClassNotFoundException, Exception {
        super(env);
        String methodName;
        methodName = "DW_UO_Set(...)";
        this.env.ge.log("<" + methodName + ">", true);
        this.env.ge.log("filename " + filename, true);
        Path dirIn = files.getInputUnderOccupiedDir();
        Path dirOut;
        dirOut = Paths.get(files.getGeneratedUnderOccupiedDir().toString(), type);
        dirOut = Paths.get(dirOut.toString(), YM3.toString());
        if (!Files.exists(dirOut)) {
            Files.createDirectories(dirOut);
        }
        Path fOut;
        fOut = Paths.get(dirOut.toString(), DW_Strings.sDW_UO_Set + DW_Strings.sBinaryFileExtension);
        if (Files.exists(fOut) || !reload) {
            DW_UO_Set loadDummy;
            loadDummy = (DW_UO_Set) Generic_IO.readObject(fOut);
            Map = loadDummy.Map;
        } else {
            Map = this.env.getUoHandler().loadInputData(dirIn, filename);
            Generic_IO.writeObject(this, fOut);
        }
        this.env.ge.log("</" + methodName + ">", true);
    }

    /**
     *
     * @return Map
     */
    public HashMap<SHBE_ClaimID, DW_UO_Record> getMap() {
        return Map;
    }

    /**
     * @return {@link #Map} keySet.
     */
    public Set<SHBE_ClaimID> getClaimIDs() {
        return Map.keySet();
    }
}
