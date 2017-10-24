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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;

/**
 *
 * @author geoagdt
 */
public class DW_UO_Set extends DW_Object implements Serializable {

    /**
     * For convenience
     */
    transient DW_Strings DW_Strings;
    transient DW_Files DW_Files;
    transient DW_UO_Handler DW_UO_Handler;

    /**
     * DW_UO_Records indexed by ClaimID
     */
    protected HashMap<DW_ID, DW_UO_Record> map;

    public DW_UO_Set(DW_Environment env) {
        super(env);
        init();
        map = new HashMap<DW_ID, DW_UO_Record>();
    }

    protected final void init() {
        DW_Strings = env.getStrings();
        DW_Files = env.getFiles();
        DW_UO_Handler = env.getUO_Handler();
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
    public DW_UO_Set(
            DW_Environment env,
            String type,
            String filename,
            DW_YM3 YM3,
            boolean reload) {
        super(env);
        String methodName;
        methodName = "DW_UO_Set(...)";
        env.logO("<" + methodName + ">", true);
        env.logO("filename " + filename, true);
        init();
        File dirIn;
        dirIn = DW_Files.getInputUnderOccupiedDir();
        File dirOut;
        dirOut = new File(DW_Files.getGeneratedUnderOccupiedDir(),
                type);
        dirOut = new File(dirOut,
                YM3.toString());
        if (!dirOut.exists()) {
            dirOut.mkdirs();
        }
        File fOut;
        fOut = new File(
                dirOut,
                DW_Strings.sDW_UO_Set + DW_Strings.sBinaryFileExtension);
        if (fOut.exists() || !reload) {
            DW_UO_Set loadDummy;
            loadDummy = (DW_UO_Set) Generic_StaticIO.readObject(fOut);
            map = loadDummy.map;
        } else {
            map = DW_UO_Handler.loadInputData(
                    dirIn,
                    filename);
            Generic_StaticIO.writeObject(this, fOut);
        }
        env.logO("</" + methodName + ">", true);
    }

    /**
     *
     * @return map
     */
    public HashMap<DW_ID, DW_UO_Record> getMap() {
        return map;
    }

    /**
     * Returns a Set of ClaimIDs {@code return map.keySet();}.
     *
     * @return
     */
    public Set<DW_ID> getClaimIDs() {
        return map.keySet();
    }
}
