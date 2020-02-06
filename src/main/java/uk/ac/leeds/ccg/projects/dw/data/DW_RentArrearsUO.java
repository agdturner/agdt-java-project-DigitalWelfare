package uk.ac.leeds.ccg.projects.dw.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Data;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public final class DW_RentArrearsUO extends DW_Object {

    /**
     * For convenience
     */
    // For convenience.
    public transient SHBE_Data SHBE_Handler;
    public transient DW_UO_Data UO_Data;

    public HashMap<SHBE_ClaimID, DW_Claim> ClaimData;

    public DW_RentArrearsUO(DW_Environment env) throws IOException, Exception {
        super(env);
        this.SHBE_Handler = env.getShbeData();
        this.UO_Data = env.getUoData();
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
    void initClaimData() throws IOException, ClassNotFoundException {
        String methodName = "initClaimData()";
        env.ge.log("<" + methodName + ">", true);
        // Declare and fill ClaimData with empty DW_Claims
        ClaimData = new HashMap<>();
        HashSet<SHBE_ClaimID> AllCouncilUOClaimIDs = UO_Data.getClaimIDsInCouncilUO();
        env.ge.log("AllCouncilUOClaimIDs.size() " + AllCouncilUOClaimIDs.size(),
                true);
        SHBE_ClaimID claimID;
        Iterator<SHBE_ClaimID> ite = AllCouncilUOClaimIDs.iterator();
        while (ite.hasNext()) {
            claimID = ite.next();
            ClaimData.put(claimID, new DW_Claim(env, claimID));
        }
        // Loop through and add data to ClaimData
        // Declare variables
        int i;
        String[] SHBEFilenames;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        String filename;
        UKP_YM3 YM3;
        SHBE_Records SHBE_Records;
        Map<SHBE_ClaimID, SHBE_Record> records;
        TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets;
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

        SHBEFilenames = SHBE_Handler.getFilenames();
        //include = SHBE_Handler.getIncludeMonthlyUO();
        include = SHBE_Handler.getIncludeAll();

        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            i = includeIte.next();
            filename = SHBEFilenames[i];
            YM3 = SHBE_Handler.getYM3(filename);
            env.ge.log("YM3 " + YM3, true);
            CouncilUOSet = CouncilUOSets.get(YM3);
            if (CouncilUOSet == null) {
                SHBE_Records = SHBE_Handler.getRecords(YM3, env.HOOME);
                records = SHBE_Records.getRecords(env.HOOME);
                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    claimID = ite.next();
                    DW_Claim = ClaimData.get(claimID);
                    if (records.containsKey(claimID)) {
                        SHBE_Record = records.get(claimID);
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
                HashMap<SHBE_ClaimID, DW_UO_Record> CouncilUOMap;
                CouncilUOMap = CouncilUOSet.getMap();

                SHBE_Records = SHBE_Handler.getRecords(YM3, env.HOOME);
                records = SHBE_Records.getRecords(env.HOOME);

                ite = AllCouncilUOClaimIDs.iterator();
                while (ite.hasNext()) {
                    claimID = ite.next();
                    DW_Claim = ClaimData.get(claimID);
                    if (records.containsKey(claimID)) {
                        SHBE_Record = records.get(claimID);
                        DRecord = SHBE_Record.getDRecord();
                        DHP = DRecord.getWeeklyAdditionalDiscretionaryPayment();
                        DW_Claim.DHP.put(i, DHP);
                        if (DRecord.getStatusOfHBClaimAtExtractDate() == 1) {
                            DW_Claim.Suspended.put(i, false);
                        } else {
                            DW_Claim.Suspended.put(i, true);
                        }
                        if (CouncilUOMap.keySet().contains(claimID)) {
                            DW_Claim.InUO.put(i, true);
                            DW_UO_Record = CouncilUOMap.get(claimID);
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
                        if (CouncilUOSet.getClaimIDs().contains(claimID)) {
                            DW_Claim.InUO.put(i, true);
                            env.ge.log("ClaimID " + claimID + " Odd case where "
                                    + "UO data exists, but claim in SHBE does "
                                    + "not.", true);
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
