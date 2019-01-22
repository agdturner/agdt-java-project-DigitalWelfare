/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_ProcessorLCCHBGeneralAggregateStatistics extends DW_ProcessorLCC {

    /**
     * For convenience.
     */
    protected DW_UO_Handler UO_Handler;
    
    boolean doHBGeneralAggregateStatistics = false;

    /**
     * For convenience SHBE_TenancyType_Handler =
 Env.getSHBE_TenancyType_Handler().
     */
    protected SHBE_TenancyType_Handler SHBE_TenancyType_Handler;

    public DW_ProcessorLCCHBGeneralAggregateStatistics(DW_Environment env) {
        super(env);
        UO_Handler = env.getUO_Handler();
        SHBE_TenancyType_Handler = env.getSHBE_TenancyType_Handler();
        SHBE_Handler = env.getSHBE_Handler();
        ClaimIDToClaimRefLookup = SHBE_Handler.getClaimIDToClaimRefLookup();
        UO_Data = env.getUO_Data();
        SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
    }

    @Override
    public void run() throws Exception, Error {
        // Declaration
        ArrayList<String> levels;
        TreeMap<String, ArrayList<Integer>> includes;
        ArrayList<String> PTs;
        Iterator<String> PTsIte;
        TreeMap<String, TreeMap<String, String>> ClaimPostcodeF_To_LevelCode_Maps;
        String includeName;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        int i;
        SHBE_Records SHBE_Records;
        HashMap<SHBE_ID, SHBE_Record> ClaimIDToSHBE_RecordMap;
        SHBE_ID SHBE_ID;
        SHBE_D_Record DRecord;
        String ClaimPostcodeF;
        int NumberOfChildDependents;
        int HouseholdSize;
        int BedroomRequirement = 0;
        Iterator<String> levelsIte;
        String level;
        TreeMap<String, String> ClaimPostcodeF_To_LevelCodeMap;
        String AreaCode;
        File outDir;
        File outDir1;
        File outDir2;
        File outFile;
        ONSPD_YM3 YM3;
        PrintWriter outPW;
        String PT;

        int CensusYear = 2011;
        // Initialisiation
        levels = new ArrayList<>();
//        levels.add(strings.sOA);
        levels.add(strings.sLSOA);
        levels.add(strings.sMSOA);
//        levels.add("PostcodeUnit");
//        levels.add("PostcodeSector");
//        levels.add("PostcodeDistrict");
        levels.add(strings.sParliamentaryConstituency);
        levels.add(strings.sStatisticalWard);
        includes = SHBE_Handler.getIncludes();
//            includes.remove(Env.SHBE_Env.strings.sIncludeAll);
//            includes.remove(Env.SHBE_Env.strings.sIncludeYearly);
//            includes.remove(Env.SHBE_Env.strings.sInclude6Monthly);
//            includes.remove(Env.SHBE_Env.strings.sInclude3Monthly);
//            includes.remove(Env.SHBE_Env.strings.sIncludeMonthlySinceApril2013);
//            includes.remove(Env.SHBE_Env.strings.sIncludeMonthly);
        PTs = Env.SHBE_Env.strings.getPaymentTypes();
//            PTs.remove(strings.sPaymentTypeAll);
//            PTs.remove(strings.sPaymentTypeIn);
//            PTs.remove(strings.sPaymentTypeSuspended);
//            PTs.remove(strings.sPaymentTypeOther);
        outDir = new File(
                files.getOutputSHBETablesDir(),
                strings.sHBGeneralAggregateStatistics);
        // Load UOdata
        TreeMap<ONSPD_YM3, DW_UO_Set> CouncilUOSets;
        DW_UO_Set CouncilUOSet;
        HashMap<SHBE_ID, DW_UO_Record> CouncilUOMap = null;
        TreeMap<ONSPD_YM3, DW_UO_Set> RSLUOSets;
        DW_UO_Set RSLUOSet;
        HashMap<SHBE_ID, DW_UO_Record> RSLUOMap = null;
        CouncilUOSets = UO_Data.getCouncilUOSets();
        RSLUOSets = UO_Data.getRSLUOSets();

        Env.ge.log("Output Directory " + outDir.toString());
        PTsIte = PTs.iterator();
        while (PTsIte.hasNext()) {
            PT = PTsIte.next();
            includeName = Env.SHBE_Env.strings.sIncludeAll;
            outDir1 = new File(
                    outDir,
                    PT);
            include = includes.get(includeName);
            includeIte = include.iterator();
            while (includeIte.hasNext()) {
                i = includeIte.next();
                YM3 = SHBE_Handler.getYM3(SHBEFilenames[i]);
                Env.ge.log("Generalising " + YM3, true);
                // Get Lookup
                ClaimPostcodeF_To_LevelCode_Maps = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3, CensusYear);
                // Load SHBE
                SHBE_Records = SHBE_Handler.getRecords(YM3, Env.HOOME);
                // Load UOdata
                CouncilUOSet = CouncilUOSets.get(YM3);
                RSLUOSet = RSLUOSets.get(YM3);
                if (CouncilUOSet != null) {
                    CouncilUOMap = CouncilUOSet.getMap();
                    RSLUOMap = RSLUOSet.getMap();
                }
                ClaimIDToSHBE_RecordMap = SHBE_Records.getRecords(true);
                //SHBE_Records = Env.getSHBE_Handler().getData().get(YM3);
                //records0 = SHBE_Records.getDataPTI(Env._HandleOutOfMemoryError_boolean);
                TreeMap<String, TreeMap<String, int[]>> result;
                result = new TreeMap<>();
                TreeMap<String, int[]> result0;
                int[] resultValues;

                // Iterate over records
                Iterator<SHBE_ID> SHBE_IDIte = ClaimIDToSHBE_RecordMap.keySet().iterator();
                while (SHBE_IDIte.hasNext()) {
                    SHBE_ID = SHBE_IDIte.next();
                    SHBE_Record SHBE_Record;
                    SHBE_Record = ClaimIDToSHBE_RecordMap.get(SHBE_ID);
                    DRecord = SHBE_Record.getDRecord();
                    if (SHBE_Handler.isHBClaim(DRecord)) {
                        NumberOfChildDependents = DRecord.getNumberOfChildDependents();
                        HouseholdSize = SHBE_Handler.getHouseholdSizeint(DRecord);
                        ClaimPostcodeF = SHBE_Record.getClaimPostcodeF();
                        boolean DoUO;
                        DW_UO_Record DW_UO_Record;
                        if (CouncilUOSet != null) {
                            if (CouncilUOMap.containsKey(SHBE_ID)) {
                                DW_UO_Record = CouncilUOMap.get(SHBE_ID);
                                BedroomRequirement = DW_UO_Record.getBedroomRequirement();
                                DoUO = true;
                            } else if (RSLUOMap.containsKey(SHBE_ID)) {
                                DW_UO_Record = RSLUOMap.get(SHBE_ID);
                                BedroomRequirement = DW_UO_Record.getBedroomRequirement();
                                DoUO = true;
                            } else {
                                DoUO = false;
                            }
                        } else {
                            DoUO = false;
                        }
                        //councilTaxBenefitClaimReferenceNumber0 = DRecord0.getCouncilTaxBenefitClaimReferenceNumber();
                        //claimantType = SHBE_Handler.getClaimantType(DRecord0);
                        if (ClaimPostcodeF != null) {
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                level = levelsIte.next();
                                if (result.containsKey(level)) {
                                    result0 = result.get(level);
                                } else {
                                    result0 = new TreeMap<>();
                                    result.put(level, result0);
                                }
                                ClaimPostcodeF_To_LevelCodeMap = ClaimPostcodeF_To_LevelCode_Maps.get(level);
                                AreaCode = getAreaCode(
                                        level,
                                        ClaimPostcodeF,
                                        ClaimPostcodeF_To_LevelCodeMap);
                                if (result0.containsKey(AreaCode)) {
                                    resultValues = result0.get(AreaCode);
                                    resultValues[0] += 1;
                                    resultValues[1] += NumberOfChildDependents;
                                    resultValues[2] += HouseholdSize;
                                    if (DoUO) {
                                        switch (BedroomRequirement) {
                                            case 1:
                                                resultValues[3] += 1;
                                                break;
                                            case 2:
                                                resultValues[4] += 1;
                                                break;
                                            case 3:
                                                resultValues[5] += 1;
                                                break;
                                            case 4:
                                                resultValues[6] += 1;
                                                break;
                                            default:
                                                resultValues[7] += 1;
                                                break;
                                        }
                                    }

                                } else {
                                    resultValues = new int[8];
                                    resultValues[0] = 1;
                                    resultValues[1] = NumberOfChildDependents;
                                    resultValues[2] = HouseholdSize;
                                    switch (BedroomRequirement) {
                                        case 1:
                                            resultValues[3] += 1;
                                            break;
                                        case 2:
                                            resultValues[4] += 1;
                                            break;
                                        case 3:
                                            resultValues[5] += 1;
                                            break;
                                        case 4:
                                            resultValues[6] += 1;
                                            break;
                                        default:
                                            resultValues[7] += 1;
                                            break;
                                    }
                                    result0.put(AreaCode, resultValues);
                                }
                            }
                        }
                    }
                }

                // Write out results
                Iterator<String> ite;
                ite = result.keySet().iterator();
                while (ite.hasNext()) {
                    level = ite.next();
                    result0 = result.get(level);
                    outDir2 = new File(
                            outDir1,
                            level);
                    outDir2.mkdirs();
                    outFile = new File(
                            outDir2,
                            YM3 + ".csv");
                    outPW = Generic_IO.getPrintWriter(outFile, false);
                    outPW.println("AreaCode, NumberOfHBClaims, "
                            + "NumberOfChildDependentsInHBClaimingHouseholds, "
                            + "TotalPopulationInHBClaimingHouseholds, "
                            + "NumberOfUOHouseholdsRequiring1Room, "
                            + "NumberOfUOHouseholdsRequiring2Rooms, "
                            + "NumberOfUOHouseholdsRequiring3Rooms, "
                            + "NumberOfUOHouseholdsRequiring4Rooms, "
                            + "NumberOfUOHouseholdsRequiring5OrMoreRooms");
                    Iterator<String> ite2;
                    ite2 = result0.keySet().iterator();
                    while (ite2.hasNext()) {
                        AreaCode = ite2.next();
                        if (!AreaCode.isEmpty()) {
                            resultValues = result0.get(AreaCode);
                            outPW.println(
                                    AreaCode
                                    + ", " + resultValues[0]
                                    + ", " + resultValues[1]
                                    + ", " + resultValues[2]
                                    + ", " + resultValues[3]
                                    + ", " + resultValues[4]
                                    + ", " + resultValues[5]
                                    + ", " + resultValues[6]
                                    + ", " + resultValues[7]
                            );
                        }
                    }
                    outPW.flush();
                    outPW.close();
                }
            }
        }
    }
    
}
