/*
 * Copyright (C) 2016 geoagdt.
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 * Class for holds and referring to all the UnderOccupancy data.
 *
 * @author geoagdt
 */
public class DW_UO_Data extends DW_Object implements Serializable {

    /**
     * The store of all UnderOccupancy RSL Sets. Keys are yM3s, values
     * are the respective sets.
     */
    private TreeMap<String, DW_UO_Set> RSLSets;

    /**
     * The store of all UnderOccupancy Council Sets. Keys are yM3s,
     * values are the respective sets.
     */
    private TreeMap<String, DW_UO_Set> CouncilSets;

    /**
     * Store of Sets of CTBRefs. Keys are yM3s, values are the respective sets.
     */
    private TreeMap<String, HashSet<String>> CTBRefs;

    public DW_UO_Data(
            DW_Environment env,
            TreeMap<String, DW_UO_Set> tRSLData,
            TreeMap<String, DW_UO_Set> tCouncilData) {
        this.env = env;
        this.RSLSets = tRSLData;
        this.CouncilSets = tCouncilData;
        initCTBRefs();
    }

    /**
     * @return the RSL_Sets
     */
    public TreeMap<String, DW_UO_Set> getRSLSets() {
        return RSLSets;
    }

    /**
     * @return the tCouncil_Data
     */
    public TreeMap<String, DW_UO_Set> getCouncilSets() {
        return CouncilSets;
    }

    /**
     * @return the CTBRefs
     */
    public TreeMap<String, HashSet<String>> getCTBRefs() {
        return CTBRefs;
    }

    /**
     * Initialises CTBRefs.
     */
    private void initCTBRefs() {
        CTBRefs = new TreeMap<String, HashSet<String>>();
        String yM3;
        Iterator<String> ite;
        ite = CouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            yM3 = ite.next();
            HashSet<String> tCTBRefsForYM3;
            tCTBRefsForYM3 = new HashSet<String>();
            tCTBRefsForYM3.addAll(CouncilSets.get(yM3).getMap().keySet());
            tCTBRefsForYM3.addAll(RSLSets.get(yM3).getMap().keySet());
            CTBRefs.put(yM3, tCTBRefsForYM3);
        }
    }

}
