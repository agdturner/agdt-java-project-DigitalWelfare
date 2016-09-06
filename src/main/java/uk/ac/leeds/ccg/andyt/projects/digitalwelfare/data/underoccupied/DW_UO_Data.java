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

/**
 * Class for holds and referring to all the UnderOccupancy data.
 *
 * @author geoagdt
 */
public class DW_UO_Data implements Serializable {

    /**
     * The store of all UnderOccupancy RSL Sets. Keys are yM3s, values
     * are the respective sets.
     */
    private TreeMap<String, DW_UO_Set> RSL_Sets;

    /**
     * The store of all UnderOccupancy Council Sets. Keys are yM3s,
     * values are the respective sets.
     */
    private TreeMap<String, DW_UO_Set> Council_Sets;

    /**
     * Store of Sets of CTBRefs. Keys are yM3s, values are the respective sets.
     */
    private TreeMap<String, HashSet<String>> CTBRefs;

    public DW_UO_Data(
            TreeMap<String, DW_UO_Set> tRSL_Data,
            TreeMap<String, DW_UO_Set> tCouncil_Data) {
        this.RSL_Sets = tRSL_Data;
        this.Council_Sets = tCouncil_Data;
        initCTBRefs();
    }

    /**
     * @return the RSL_Sets
     */
    public TreeMap<String, DW_UO_Set> getRSL_Sets() {
        return RSL_Sets;
    }

    /**
     * @return the tCouncil_Data
     */
    public TreeMap<String, DW_UO_Set> getCouncil_Data() {
        return Council_Sets;
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
        ite = Council_Sets.keySet().iterator();
        while (ite.hasNext()) {
            yM3 = ite.next();
            HashSet<String> tCTBRefsForYM3;
            tCTBRefsForYM3 = new HashSet<String>();
            tCTBRefsForYM3.addAll(Council_Sets.get(yM3).getMap().keySet());
            tCTBRefsForYM3.addAll(RSL_Sets.get(yM3).getMap().keySet());
            CTBRefs.put(yM3, tCTBRefsForYM3);
        }
    }

}
