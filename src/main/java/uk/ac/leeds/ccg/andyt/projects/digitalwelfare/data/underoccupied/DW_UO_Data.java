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
import java.util.Set;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 * Class for holds and referring to all the UnderOccupancy data.
 *
 * @author geoagdt
 */
public class DW_UO_Data extends DW_Object implements Serializable {

    /**
     * For storing all DW_UO_Sets for RSL. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<ONSPD_YM3, DW_UO_Set> RSLUOSets;

    /**
     * For storing all DW_UO_Sets for Council. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets;

    /**
     * For storing sets of ClaimIDsInUO. Keys are YM3, values are the respective
     * ClaimIDsInUO for claims classed as Under Occupying.
     */
    private TreeMap<ONSPD_YM3, Set<SHBE_ID>> ClaimIDsInUO;

    /**
     * For storing ClaimIDs of claims that were classed as Under Occupying
     * Council claims at some time.
     */
    private HashSet<SHBE_ID> ClaimIDsInCouncilUO;

    /**
     * For storing ClaimIDs of Council claims that were expected in March 2013
     * to be UnderOccupying in April 2013.
     */
    private HashSet<SHBE_ID> ClaimIDsInCouncilBaseline;

    /**
     * For storing ClaimIDs of RSL claims that were expected in March 2013 to be
     * UnderOccupying in April 2013.
     */
    private HashSet<SHBE_ID> ClaimIDsInRSLBaseline;

    public DW_UO_Data(DW_Environment env) {
        super(env);
    }

    public DW_UO_Data(DW_Environment env, 
            TreeMap<ONSPD_YM3, DW_UO_Set> RSLUOSets,
            TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets) {
        super(env);
        this.RSLUOSets = RSLUOSets;
        this.CouncilUOSets = CouncilUOSets;
        initClaimIDs();
    }

    /**
     * @return the RSL_Sets
     */
    public TreeMap<ONSPD_YM3, DW_UO_Set> getRSLUOSets() {
        return RSLUOSets;
    }

    /**
     * @return the tCouncil_Data
     */
    public TreeMap<ONSPD_YM3, DW_UO_Set> getCouncilUOSets() {
        return CouncilUOSets;
    }

    /**
     * @return the ClaimIDsInUO
     */
    public TreeMap<ONSPD_YM3, Set<SHBE_ID>> getClaimIDsInUO() {
        return ClaimIDsInUO;
    }

    /**
     * Initialises ClaimIDsInUO.
     */
    private void initClaimIDs() {
        ClaimIDsInUO = new TreeMap<>();
        ClaimIDsInCouncilBaseline = new HashSet<>();
        ClaimIDsInRSLBaseline = new HashSet<>();
        ONSPD_YM3 YM3;
        ONSPD_YM3 baselineYM3;
        baselineYM3 = getBaselineYM3();
        Iterator<ONSPD_YM3> ite;
        ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            if (YM3.equals(baselineYM3)) {
                ClaimIDsInCouncilBaseline.addAll(CouncilUOSets.get(YM3).getClaimIDs());
                ClaimIDsInRSLBaseline.addAll(RSLUOSets.get(YM3).getClaimIDs());
            } else {
                HashSet<SHBE_ID> ClaimIDsForYM3;
                ClaimIDsForYM3 = new HashSet<>();
                ClaimIDsForYM3.addAll(CouncilUOSets.get(YM3).getClaimIDs());
                ClaimIDsForYM3.addAll(RSLUOSets.get(YM3).getClaimIDs());
                ClaimIDsInUO.put(YM3, ClaimIDsForYM3);
            }
        }
    }

    /**
     * @return ClaimIDsInCouncilUO initialising it first if it is null.
     */
    public HashSet<SHBE_ID> getClaimIDsInCouncilUO() {
        if (ClaimIDsInCouncilUO == null) {
            initAllCouncilUOClaimIDs();
        }
        return ClaimIDsInCouncilUO;
    }

    /**
     * Initialises ClaimIDsInCouncilUO.
     */
    private void initAllCouncilUOClaimIDs() {
        ClaimIDsInCouncilUO = new HashSet<>();
        ONSPD_YM3 YM3;
        ONSPD_YM3 baselineYM3 = getBaselineYM3();
        Iterator<ONSPD_YM3> ite;
        ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            if (!YM3.equals(baselineYM3)) {
                ClaimIDsInCouncilUO.addAll(CouncilUOSets.get(YM3).getClaimIDs());
            }
        }
    }

    ONSPD_YM3 BaselineYM3;

    public ONSPD_YM3 getBaselineYM3() {
        //if (BaselineYM3 == null) {
        BaselineYM3 = new ONSPD_YM3(2013, 4);
        //}
        return BaselineYM3;
    }
}
