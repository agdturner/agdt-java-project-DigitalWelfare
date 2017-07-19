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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Claim;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public final class RentArrearsUO extends DW_Object {

    /**
     * For convenience
     */
    // For convenience.
    public transient DW_SHBE_Data DW_SHBE_Data;
    public transient DW_SHBE_Handler DW_SHBE_Handler;
    public transient DW_Strings DW_Strings;
    public transient DW_Files DW_Files;
    public transient DW_UO_Data DW_UO_Data;

    public HashMap<DW_ID, DW_Claim> ClaimData;

    public RentArrearsUO(DW_Environment env) {
        super(env);
        this.DW_SHBE_Data = env.getDW_SHBE_Data();
        this.DW_SHBE_Handler = env.getDW_SHBE_Handler();
        this.DW_Strings = env.getDW_Strings();
        this.DW_Files = env.getDW_Files();
        this.DW_UO_Data = env.getDW_UO_Data();
        initClaimData();
    }

    /**
     *
     * @param SHBEFilenames
     * @param include
     * @param forceNewSummaries
     * @param HB_CTB
     * @param PTs
     * @param nTT
     * @param nEG
     * @param nPSI
     * @param DW_UO_Data
     * @param handleOutOfMemoryError
     * @return
     */
    void initClaimData() {
        String methodName = "initClaimData()";
        env.logO("<" + methodName + ">", true);
        // Declare and fill ClaimData with empty DW_Claims
        ClaimData = new HashMap<DW_ID, DW_Claim>();
        HashSet<DW_ID> AllCouncilUOClaimIDs;
        AllCouncilUOClaimIDs = DW_UO_Data.getClaimIDsInCouncilUO();
        env.logO("AllCouncilUOClaimIDs.size() " + AllCouncilUOClaimIDs.size(), true);
        DW_ID ClaimID;
        Iterator<DW_ID> ite;
        ite = AllCouncilUOClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimData.put(ClaimID, new DW_Claim(ClaimID));
        }
        // Loop through and add data to ClaimData
        // Declare variables
        int i;
        String[] SHBEFilenames;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        String filename;
        String YM3;
        DW_SHBE_Records DW_SHBE_Records;
        HashMap<DW_ID, DW_SHBE_Record> Records;
        TreeMap<String, DW_UO_Set> CouncilUOSets;
        DW_UO_Set CouncilUOSet;
        DW_SHBE_Record DW_SHBE_Record;
        DW_SHBE_D_Record DRecord;
        DW_Claim DW_Claim;
        int DHP;
        DW_UO_Record DW_UO_Record;
        int BedroomRequirement;
        int BedroomsInProperty;
        Double RentArrears;
        int WERA;
        double bedDiff;
        double BT;
        String PostcodeF;

        CouncilUOSets = DW_UO_Data.getCouncilUOSets();

        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        //include = DW_SHBE_Handler.getIncludeMonthlyUO();
        include = DW_SHBE_Handler.getIncludeAll();

        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename = SHBEFilenames[i];
            YM3 = DW_SHBE_Handler.getYM3(filename);
            env.logO("YM3 " + YM3, true);
            CouncilUOSet = CouncilUOSets.get(YM3);
            if (CouncilUOSet == null) {
                DW_SHBE_Records = DW_SHBE_Data.getDW_SHBE_Records(YM3);
                Records = DW_SHBE_Records.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    ClaimID = ite.next();
                    DW_Claim = ClaimData.get(ClaimID);
                    if (Records.containsKey(ClaimID)) {
                        DW_SHBE_Record = Records.get(ClaimID);
                        DRecord = DW_SHBE_Record.getDRecord();
                        DHP = DRecord.getWeeklyAdditionalDiscretionaryPayment();
                        DW_Claim.DHP.put(i, DHP);
                        if (DRecord.getStatusOfHBClaimAtExtractDate() == 1) {
                            DW_Claim.Suspended.put(i, false);
                        } else {
                            DW_Claim.Suspended.put(i, true);
                        }
                        DW_Claim.InUO.put(i, false);
                        DW_Claim.InSHBE.put(i, true);
                    } else {
                        DW_Claim.InSHBE.put(i, false);
                    }
                }
            } else {
                HashMap<DW_ID, DW_UO_Record> CouncilUOMap;
                CouncilUOMap = CouncilUOSet.getMap();

                DW_SHBE_Records = DW_SHBE_Data.getDW_SHBE_Records(YM3);
                Records = DW_SHBE_Records.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);

                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    ClaimID = ite.next();
                    DW_Claim = ClaimData.get(ClaimID);
                    if (Records.containsKey(ClaimID)) {
                        DW_SHBE_Record = Records.get(ClaimID);
                        DRecord = DW_SHBE_Record.getDRecord();
                        DHP = DRecord.getWeeklyAdditionalDiscretionaryPayment();
                        DW_Claim.DHP.put(i, DHP);
                        if (DRecord.getStatusOfHBClaimAtExtractDate() == 1) {
                            DW_Claim.Suspended.put(i, false);
                        } else {
                            DW_Claim.Suspended.put(i, true);
                        }
                        if (CouncilUOMap.keySet().contains(ClaimID)) {
                            DW_Claim.InUO.put(i, true);
                            DW_UO_Record = CouncilUOMap.get(ClaimID);
                            RentArrears = DW_UO_Record.getTotalRentArrears();
                            DW_Claim.RentArrears.put(i, RentArrears);
                            BedroomRequirement = DW_UO_Record.getBedroomRequirement();
                            BedroomsInProperty = DW_UO_Record.getBedroomsInProperty();
                            bedDiff = BedroomsInProperty - BedroomRequirement;
                            WERA = DRecord.getWeeklyEligibleRentAmount();
                            if (bedDiff == 1) {
                                BT = (14 * WERA) / 100.0d;
                                DW_Claim.Type14.put(i, true);
                            } else {
                                BT = (25 * WERA) / 100.0d;
                                DW_Claim.Type14.put(i, false);
                            }
                            DW_Claim.BT.put(i, BT);
                            if ((DW_UO_Record.getFemaleChildrenUnder10()
                                    + DW_UO_Record.getMaleChildrenUnder10()) > 0) {
                                DW_Claim.ChildUnder11.put(i, true);
                            }
                        } else {
                            DW_Claim.InUO.put(i, false);
                        }
                        DW_Claim.InSHBE.put(i, true);
                        PostcodeF = DW_SHBE_Record.getClaimPostcodeF();
                        DW_Claim.PostcodeFs.put(i, PostcodeF);
                    } else {
                        if (CouncilUOSet.getClaimIDs().contains(ClaimID)) {
                            DW_Claim.InUO.put(i, true);
                            env.logO(env.DEBUG_Level_FINE,
                                    "ClaimID " + ClaimID + 
                                            " Odd case where UO data exists, but claim in SHBE does not.");
                        } else {
                            DW_Claim.InUO.put(i, false);
                        }
                        DW_Claim.InSHBE.put(i, false);
                        DW_Claim.ChildUnder11.put(i, true);
                    }
                }
            }
        }
    }

}
