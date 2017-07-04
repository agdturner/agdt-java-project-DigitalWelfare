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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
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
    private final TreeMap<String, DW_UO_Set> RSLSets;

    /**
     * The store of all UnderOccupancy Council Sets. Keys are yM3s,
     * values are the respective sets.
     */
    private final TreeMap<String, DW_UO_Set> CouncilSets;

    /**
     * Store of Sets of ClaimIDs. Keys are yM3s, values are the respective sets.
     */
    private TreeMap<String, HashSet<DW_ID>> ClaimIDs;

    public DW_UO_Data(
            DW_Environment env,
            TreeMap<String, DW_UO_Set> tRSLData,
            TreeMap<String, DW_UO_Set> tCouncilData) {
        this.env = env;
        this.RSLSets = tRSLData;
        this.CouncilSets = tCouncilData;
        initClaimIDs();
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
     * @return the ClaimIDs
     */
    public TreeMap<String, HashSet<DW_ID>> getClaimIDs() {
        return ClaimIDs;
    }

    /**
     * Initialises ClaimIDs.
     */
    private void initClaimIDs() {
        ClaimIDs = new TreeMap<String, HashSet<DW_ID>>();
        String yM3;
        Iterator<String> ite;
        ite = CouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            yM3 = ite.next();
            HashSet<DW_ID> ClaimIDsForYM3;
            ClaimIDsForYM3 = new HashSet<DW_ID>();
            ClaimIDsForYM3.addAll(CouncilSets.get(yM3).getMap().keySet());
            ClaimIDsForYM3.addAll(RSLSets.get(yM3).getMap().keySet());
            ClaimIDs.put(yM3, ClaimIDsForYM3);
        }
    }

}
