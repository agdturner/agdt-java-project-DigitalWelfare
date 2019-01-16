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
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public final class DW_RentArrearsUO extends DW_Object {

    /**
     * For convenience
     */
    // For convenience.
    public transient SHBE_Data SHBE_Data;
    public transient SHBE_Handler SHBE_Handler;
    public transient DW_Strings Strings;
    public transient DW_Files Files;
    public transient DW_UO_Data UO_Data;

    public HashMap<SHBE_ID, DW_Claim> ClaimData;

    public DW_RentArrearsUO(DW_Environment env) {
        super(env);
        this.SHBE_Data = env.getSHBE_Data();
        this.SHBE_Handler = env.getSHBE_Handler();
        this.Strings = env.getStrings();
        this.Files = env.getFiles();
        this.UO_Data = env.getUO_Data();
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
        Env.logO("<" + methodName + ">", true);
        // Declare and fill ClaimData with empty DW_Claims
        ClaimData = new HashMap<>();
        HashSet<SHBE_ID> AllCouncilUOClaimIDs;
        AllCouncilUOClaimIDs = UO_Data.getClaimIDsInCouncilUO();
        Env.logO("AllCouncilUOClaimIDs.size() " + AllCouncilUOClaimIDs.size(), true);
        SHBE_ID ClaimID;
        Iterator<SHBE_ID> ite;
        ite = AllCouncilUOClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            ClaimData.put(ClaimID, new DW_Claim(Env, ClaimID));
        }
        // Loop through and add data to ClaimData
        // Declare variables
        int i;
        String[] SHBEFilenames;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        String filename;
        ONSPD_YM3 YM3;
        SHBE_Records SHBE_Records;
        HashMap<SHBE_ID, SHBE_Record> Records;
        TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets;
        DW_UO_Set CouncilUOSet;
        SHBE_Record SHBE_Record;
        SHBE_D_Record DRecord;
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

        CouncilUOSets = UO_Data.getCouncilUOSets();

        SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
        //include = SHBE_Handler.getIncludeMonthlyUO();
        include = SHBE_Handler.getIncludeAll();

        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename = SHBEFilenames[i];
            YM3 = SHBE_Handler.getYM3(filename);
            Env.logO("YM3 " + YM3, true);
            CouncilUOSet = CouncilUOSets.get(YM3);
            if (CouncilUOSet == null) {
                SHBE_Records = SHBE_Data.getRecords(YM3, Env.HOOME);
                Records = SHBE_Records.getRecords(Env.HOOME);
                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    ClaimID = ite.next();
                    DW_Claim = ClaimData.get(ClaimID);
                    if (Records.containsKey(ClaimID)) {
                        SHBE_Record = Records.get(ClaimID);
                        DRecord = SHBE_Record.getDRecord();
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
                HashMap<SHBE_ID, DW_UO_Record> CouncilUOMap;
                CouncilUOMap = CouncilUOSet.getMap();

                SHBE_Records = SHBE_Data.getRecords(YM3, Env.HOOME);
                Records = SHBE_Records.getRecords(Env.HOOME);

                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    ClaimID = ite.next();
                    DW_Claim = ClaimData.get(ClaimID);
                    if (Records.containsKey(ClaimID)) {
                        SHBE_Record = Records.get(ClaimID);
                        DRecord = SHBE_Record.getDRecord();
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
                        PostcodeF = SHBE_Record.getClaimPostcodeF();
                        DW_Claim.PostcodeFs.put(i, PostcodeF);
                    } else {
                        if (CouncilUOSet.getClaimIDs().contains(ClaimID)) {
                            DW_Claim.InUO.put(i, true);
                            Env.logO(Env.DEBUG_Level_FINE,
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
