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
package uk.ac.leeds.ccg.projects.dw.data.uo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;

/**
 * Class for holds and referring to all the UnderOccupancy data.
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_UO_Data extends DW_Object {

    /**
     * For storing all DW_UO_Sets for RSL. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets;

    /**
     * For storing all DW_UO_Sets for Council. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets;

    /**
     * For storing sets of ClaimIDsInUO. Keys are YM3, values are the respective
     * ClaimIDsInUO for claims classed as Under Occupying.
     */
    private TreeMap<UKP_YM3, Set<SHBE_ClaimID>> ClaimIDsInUO;

    /**
     * For storing ClaimIDs of claims that were classed as Under Occupying
     * Council claims at some time.
     */
    private HashSet<SHBE_ClaimID> ClaimIDsInCouncilUO;

    /**
     * For storing ClaimIDs of Council claims that were expected in March 2013
     * to be UnderOccupying in April 2013.
     */
    private HashSet<SHBE_ClaimID> ClaimIDsInCouncilBaseline;

    /**
     * For storing ClaimIDs of RSL claims that were expected in March 2013 to be
     * UnderOccupying in April 2013.
     */
    private HashSet<SHBE_ClaimID> ClaimIDsInRSLBaseline;

    public DW_UO_Data(DW_Environment env) {
        super(env);
    }

    public DW_UO_Data(DW_Environment env, 
            TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets,
            TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets) {
        super(env);
        this.RSLUOSets = RSLUOSets;
        this.CouncilUOSets = CouncilUOSets;
        initClaimIDs();
    }

    /**
     * @return the RSL_Sets
     */
    public TreeMap<UKP_YM3, DW_UO_Set> getRSLUOSets() {
        return RSLUOSets;
    }

    /**
     * @return the tCouncil_Data
     */
    public TreeMap<UKP_YM3, DW_UO_Set> getCouncilUOSets() {
        return CouncilUOSets;
    }

    /**
     * @return the ClaimIDsInUO
     */
    public TreeMap<UKP_YM3, Set<SHBE_ClaimID>> getClaimIDsInUO() {
        return ClaimIDsInUO;
    }

    /**
     * Initialises ClaimIDsInUO.
     */
    private void initClaimIDs() {
        ClaimIDsInUO = new TreeMap<>();
        ClaimIDsInCouncilBaseline = new HashSet<>();
        ClaimIDsInRSLBaseline = new HashSet<>();
        UKP_YM3 baselineYM3 = getBaselineYM3();
        Iterator<UKP_YM3> ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            UKP_YM3 ym3 = ite.next();
            if (ym3.equals(baselineYM3)) {
                ClaimIDsInCouncilBaseline.addAll(CouncilUOSets.get(ym3).getClaimIDs());
                ClaimIDsInRSLBaseline.addAll(RSLUOSets.get(ym3).getClaimIDs());
            } else {
                HashSet<SHBE_ClaimID> ClaimIDsForYM3;
                ClaimIDsForYM3 = new HashSet<>();
                ClaimIDsForYM3.addAll(CouncilUOSets.get(ym3).getClaimIDs());
                ClaimIDsForYM3.addAll(RSLUOSets.get(ym3).getClaimIDs());
                ClaimIDsInUO.put(ym3, ClaimIDsForYM3);
            }
        }
    }

    /**
     * @return ClaimIDsInCouncilUO initialising it first if it is null.
     */
    public HashSet<SHBE_ClaimID> getClaimIDsInCouncilUO() {
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
        UKP_YM3 YM3;
        UKP_YM3 baselineYM3 = getBaselineYM3();
        Iterator<UKP_YM3> ite;
        ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            if (!YM3.equals(baselineYM3)) {
                ClaimIDsInCouncilUO.addAll(CouncilUOSets.get(YM3).getClaimIDs());
            }
        }
    }

    UKP_YM3 BaselineYM3;

    public UKP_YM3 getBaselineYM3() {
        //if (BaselineYM3 == null) {
        BaselineYM3 = new UKP_YM3(2013, 4);
        //}
        return BaselineYM3;
    }
}
