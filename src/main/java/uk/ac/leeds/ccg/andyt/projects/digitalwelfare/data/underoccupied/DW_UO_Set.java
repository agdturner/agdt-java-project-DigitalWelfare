/*
 * Copyright (C) 2015 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;

/**
 *
 * @author geoagdt
 */
public class DW_UO_Set extends DW_Object implements Serializable {

    /**
     * DW_UO_Records indexed by ClaimID
     */
    protected HashMap<SHBE_ID, DW_UO_Record> Map;

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
            ONSPD_YM3 YM3, boolean reload) {
        super(env);
        String methodName;
        methodName = "DW_UO_Set(...)";
        this.env.ge.log("<" + methodName + ">", true);
        this.env.ge.log("filename " + filename, true);
        File dirIn = files.getInputUnderOccupiedDir();
        File dirOut;
        dirOut = new File(files.getGeneratedUnderOccupiedDir(), type);
        dirOut = new File(dirOut, YM3.toString());
        if (!dirOut.exists()) {
            dirOut.mkdirs();
        }
        File fOut;
        fOut = new File(dirOut, DW_Strings.sDW_UO_Set + DW_Strings.sBinaryFileExtension);
        if (fOut.exists() || !reload) {
            DW_UO_Set loadDummy;
            loadDummy = (DW_UO_Set) Generic_IO.readObject(fOut);
            Map = loadDummy.Map;
        } else {
            Map = this.env.getUO_Handler().loadInputData(dirIn, filename);
            Generic_IO.writeObject(this, fOut);
        }
        this.env.ge.log("</" + methodName + ">", true);
    }

    /**
     *
     * @return Map
     */
    public HashMap<SHBE_ID, DW_UO_Record> getMap() {
        return Map;
    }

    /**
     * Returns a Set of ClaimIDs {@code return Map.keySet();}.
     *
     * @return
     */
    public Set<SHBE_ID> getClaimIDs() {
        return Map.keySet();
    }
}
