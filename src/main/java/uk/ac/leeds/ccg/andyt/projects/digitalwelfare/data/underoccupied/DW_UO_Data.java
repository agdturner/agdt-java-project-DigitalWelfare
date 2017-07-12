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
     * For storing all DW_UO_Sets for RSL. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<String, DW_UO_Set> RSLUOSets;

    /**
     * For storing all DW_UO_Sets for Council. Keys are YM3, values are the
     * respective sets.
     */
    private TreeMap<String, DW_UO_Set> CouncilUOSets;

    /**
     * For storing sets of ClaimIDsInUO. Keys are YM3, values are the
     * respective ClaimIDsInUO for claims classed as Under Occupying.
     */
    private TreeMap<String, Set<DW_ID>> ClaimIDsInUO;

    /**
     * For storing ClaimIDs of claims that were classed as Under Occupying
     * Council claims at some time.
     */
    private HashSet<DW_ID> ClaimIDsInCouncilUO;

    /**
     * For storing ClaimIDs of Council claims that were expected in March
     * 2013 to be UnderOccupying in April 2013.
     */
    private HashSet<DW_ID> ClaimIDsInCouncilBaseline;

    /**
     * For storing ClaimIDs of RSL claims that were expected in March 2013 to
     * be UnderOccupying in April 2013.
     */
    private HashSet<DW_ID> ClaimIDsInRSLBaseline;

    public DW_UO_Data() {
    }

    public DW_UO_Data(
            DW_Environment env) {
        super(env);
    }

    public DW_UO_Data(
            DW_Environment env,
            TreeMap<String, DW_UO_Set> RSLUOSets,
            TreeMap<String, DW_UO_Set> CouncilUOSets) {
        super(env);
        this.RSLUOSets = RSLUOSets;
        this.CouncilUOSets = CouncilUOSets;
        initClaimIDs();
    }

    /**
     * @return the RSL_Sets
     */
    public TreeMap<String, DW_UO_Set> getRSLUOSets() {
        return RSLUOSets;
    }

    /**
     * @return the tCouncil_Data
     */
    public TreeMap<String, DW_UO_Set> getCouncilUOSets() {
        return CouncilUOSets;
    }

    /**
     * @return the ClaimIDsInUO
     */
    public TreeMap<String, Set<DW_ID>> getClaimIDsInUO() {
        return ClaimIDsInUO;
    }

    /**
     * Initialises ClaimIDsInUO.
     */
    private void initClaimIDs() {
        ClaimIDsInUO = new TreeMap<String, Set<DW_ID>>();
        ClaimIDsInCouncilBaseline = new HashSet<DW_ID>();
        ClaimIDsInRSLBaseline = new HashSet<DW_ID>();
        String YM3;
        Iterator<String> ite;
        ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            if (YM3.equalsIgnoreCase("2013_Mar")) {
                ClaimIDsInCouncilBaseline.addAll(CouncilUOSets.get(YM3).getClaimIDs());
                ClaimIDsInRSLBaseline.addAll(RSLUOSets.get(YM3).getClaimIDs());
            } else {
                HashSet<DW_ID> ClaimIDsForYM3;
                ClaimIDsForYM3 = new HashSet<DW_ID>();
                ClaimIDsForYM3.addAll(CouncilUOSets.get(YM3).getClaimIDs());
                ClaimIDsForYM3.addAll(RSLUOSets.get(YM3).getClaimIDs());
                ClaimIDsInUO.put(YM3, ClaimIDsForYM3);
            }
        }
    }

    /**
     * @return ClaimIDsInCouncilUO initialising it first if it is null.
     */
    public HashSet<DW_ID> getClaimIDsInCouncilUO() {
        if (ClaimIDsInCouncilUO == null) {
            initAllCouncilUOClaimIDs();
        }
        return ClaimIDsInCouncilUO;
    }

    /**
     * Initialises ClaimIDsInCouncilUO.
     */
    private void initAllCouncilUOClaimIDs() {
        ClaimIDsInCouncilUO = new HashSet<DW_ID>();
        String YM3;
        Iterator<String> ite;
        ite = CouncilUOSets.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            if (!YM3.equalsIgnoreCase("2013_Mar")) {
                ClaimIDsInCouncilUO.addAll(CouncilUOSets.get(YM3).getClaimIDs());
            }
        }
    }

}
