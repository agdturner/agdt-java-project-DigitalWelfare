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
import java.util.Set;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_UO_Set implements Serializable {

    /**
     * aUnderOccupiedReport_Record.getClaimReferenceNumber(),
     * aUnderOccupiedReport_Record
     */
    protected TreeMap<String, DW_UO_Record> map;

    /**
     * If reload == true then this reloads data from source. Otherwise it checks
     * to see if a generated file exists to load the data from there. If the
     * generated file exists, the set is loaded from there, otherwise, the set
     * is reloaded from source and saved where the generated file is saved. The
     * result is returned.
     *
     * @param tDW_Files
     * @param tDW_UnderOccupiedReport_Handler
     * @param type Indicates type, e.g. RSL, Council.
     * @param filename
     * @param yM3
     * @param reload
     */
    public DW_UO_Set(
            DW_Files tDW_Files,
            DW_UO_Handler tDW_UnderOccupiedReport_Handler,
            String type,
            String filename,
            String yM3,
            boolean reload) {
        File dirIn;
        dirIn = tDW_Files.getInputUnderOccupiedDir();
        File dirOut;
        dirOut = new File(tDW_Files.getGeneratedUnderOccupiedDir(),
                type);
        dirOut = new File(dirOut,
                yM3);
        if (!dirOut.exists()) {
            dirOut.mkdirs();
        }
        File fOut;
        fOut = new File(
                dirOut,
                "DW_UnderOccupiedReport_Set.thisFile");
        if (fOut.exists()) {
            DW_UO_Set loadDummy;
            loadDummy = (DW_UO_Set) Generic_StaticIO.readObject(fOut);
            map = loadDummy.map;
        } else {
            map = tDW_UnderOccupiedReport_Handler.loadInputData(
                    dirIn,
                    filename);
            Generic_StaticIO.writeObject(this, fOut);
        }
    }

//        councilRecords = new TreeMap[numberOfUnderOccupiedReportFiles];
//        result[0] = councilRecords;
//        RSLRecords = new TreeMap[numberOfUnderOccupiedReportFiles];
    public TreeMap<String, DW_UO_Record> getMap() {
        return map;
    }
    
    /**
     * Returns a list of CTBRefs {@code return map.keySet();}.
     * @return 
     */
    public Set<String> getCTBRefs(){
        return map.keySet();
    }
}
