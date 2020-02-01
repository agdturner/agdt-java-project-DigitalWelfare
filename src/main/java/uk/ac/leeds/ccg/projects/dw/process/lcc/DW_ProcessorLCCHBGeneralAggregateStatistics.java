/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Strings;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Handler;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_ProcessorLCCHBGeneralAggregateStatistics extends DW_ProcessorLCC {

    /**
     * For convenience.
     */
    protected DW_UO_Handler UO_Handler;

    boolean doHBGeneralAggregateStatistics = false;

    /**
     * For convenience SHBE_TenancyType_Handler =
     * env.getSHBE_TenancyType_Handler().
     */
    protected SHBE_TenancyType_Handler SHBE_TenancyType_Handler;

    public DW_ProcessorLCCHBGeneralAggregateStatistics(DW_Environment env)
            throws IOException, Exception {
        super(env);
        UO_Handler = env.getUO_Handler();
        SHBE_TenancyType_Handler = env.getSHBE_TenancyType_Handler();
        shbeHandler = env.getSHBE_Handler();
        cid2c = shbeHandler.getCid2c();
        UO_Data = env.getUO_Data();
        shbeFilenames = shbeHandler.getFilenames();
    }

    @Override
    public void run() throws Exception, Error {
        // Declaration
        ArrayList<Integer> levels;
        TreeMap<String, ArrayList<Integer>> includes;
        ArrayList<String> PTs;
        Iterator<String> PTsIte;
        TreeMap<Integer, TreeMap<String, String>> ClaimPostcodeF_To_LevelCode_Maps;
        String includeName;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        int i;
        SHBE_Records SHBE_Records;
        Map<SHBE_ClaimID, SHBE_Record> ClaimIDToSHBE_RecordMap;
        SHBE_ClaimID SHBE_ClaimID;
        SHBE_D_Record DRecord;
        String ClaimPostcodeF;
        int NumberOfChildDependents;
        int HouseholdSize;
        int BedroomRequirement = 0;
        Iterator<Integer> levelsIte;
        Integer level;
        TreeMap<String, String> ClaimPostcodeF_To_LevelCodeMap;
        String AreaCode;
        Path outDir;
        Path outDir1;
        Path outDir2;
        Path outFile;
        UKP_YM3 YM3;
        PrintWriter outPW;
        String PT;

        int CensusYear = 2011;
        // Initialisiation
        levels = new ArrayList<>();
//        levels.add(DW_Strings.sOA);
        levels.add(UKP_Data.TYPE_LSOA);
        levels.add(UKP_Data.TYPE_MSOA);
//        levels.add(UKP_Data.TYPE_UNIT);
//        levels.add(UKP_Data.TYPE_SECTOR);
//        levels.add(UKP_Data.TYPE_DISTRICT);
        levels.add(UKP_Data.TYPE_ParliamentaryConstituency);
        levels.add(UKP_Data.TYPE_StatisticalWard);
        includes = shbeHandler.getIncludes();
//            includes.remove(SHBE_Strings.s_.sIncludeAll);
//            includes.remove(SHBE_Strings.s_.sIncludeYearly);
//            includes.remove(SHBE_Strings.s_.sInclude6Monthly);
//            includes.remove(SHBE_Strings.s_.sInclude3Monthly);
//            includes.remove(SHBE_Strings.s_.sIncludeMonthlySinceApril2013);
//            includes.remove(SHBE_Strings.s_.sIncludeMonthly);
        PTs = SHBE_Strings.getPaymentTypes();
//            PTs.remove(DW_Strings.sPaymentTypeAll);
//            PTs.remove(DW_Strings.sPaymentTypeIn);
//            PTs.remove(DW_Strings.sPaymentTypeSuspended);
//            PTs.remove(DW_Strings.sPaymentTypeOther);
        outDir = Paths.get(files.getOutputSHBETablesDir().toString(), 
                DW_Strings.sHBGeneralAggregateStatistics);
        // Load UOdata
        TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets;
        DW_UO_Set CouncilUOSet;
        HashMap<SHBE_ClaimID, DW_UO_Record> CouncilUOMap = null;
        TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets;
        DW_UO_Set RSLUOSet;
        HashMap<SHBE_ClaimID, DW_UO_Record> RSLUOMap = null;
        CouncilUOSets = UO_Data.getCouncilUOSets();
        RSLUOSets = UO_Data.getRSLUOSets();

        env.ge.log("Output Directory " + outDir.toString());
        PTsIte = PTs.iterator();
        while (PTsIte.hasNext()) {
            PT = PTsIte.next();
            includeName = SHBE_Strings.s_IncludeAll;
            outDir1 = Paths.get(outDir.toString(), PT);
            include = includes.get(includeName);
            includeIte = include.iterator();
            while (includeIte.hasNext()) {
                i = includeIte.next();
                YM3 = shbeHandler.getYM3(shbeFilenames[i]);
                env.ge.log("Generalising " + YM3, true);
                // Get Lookup
                ClaimPostcodeF_To_LevelCode_Maps = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3, CensusYear);
                // Load SHBE
                SHBE_Records = shbeHandler.getRecords(YM3, env.HOOME);
                // Load UOdata
                CouncilUOSet = CouncilUOSets.get(YM3);
                RSLUOSet = RSLUOSets.get(YM3);
                if (CouncilUOSet != null) {
                    CouncilUOMap = CouncilUOSet.getMap();
                    RSLUOMap = RSLUOSet.getMap();
                }
                ClaimIDToSHBE_RecordMap = SHBE_Records.getRecords(true);
                //SHBE_Records = env.getSHBE_Handler().getData().get(YM3);
                //records0 = SHBE_Records.getDataPTI(env._HandleOutOfMemoryError_boolean);
                TreeMap<Integer, TreeMap<String, int[]>> result;
                result = new TreeMap<>();
                TreeMap<String, int[]> result0;
                int[] resultValues;

                // Iterate over records
                Iterator<SHBE_ClaimID> SHBE_ClaimIDIte = ClaimIDToSHBE_RecordMap.keySet().iterator();
                while (SHBE_ClaimIDIte.hasNext()) {
                    SHBE_ClaimID = SHBE_ClaimIDIte.next();
                    SHBE_Record SHBE_Record;
                    SHBE_Record = ClaimIDToSHBE_RecordMap.get(SHBE_ClaimID);
                    DRecord = SHBE_Record.getDRecord();
                    if (shbeHandler.isHBClaim(DRecord)) {
                        NumberOfChildDependents = DRecord.getNumberOfChildDependents();
                        HouseholdSize = shbeHandler.getHouseholdSizeint(DRecord);
                        ClaimPostcodeF = SHBE_Record.getClaimPostcodeF();
                        boolean DoUO;
                        DW_UO_Record DW_UO_Record;
                        if (CouncilUOSet != null) {
                            if (CouncilUOMap.containsKey(SHBE_ClaimID)) {
                                DW_UO_Record = CouncilUOMap.get(SHBE_ClaimID);
                                BedroomRequirement = DW_UO_Record.getBedroomRequirement();
                                DoUO = true;
                            } else if (RSLUOMap.containsKey(SHBE_ClaimID)) {
                                DW_UO_Record = RSLUOMap.get(SHBE_ClaimID);
                                BedroomRequirement = DW_UO_Record.getBedroomRequirement();
                                DoUO = true;
                            } else {
                                DoUO = false;
                            }
                        } else {
                            DoUO = false;
                        }
                        //councilTaxBenefitClaimReferenceNumber0 = DRecord0.getCouncilTaxBenefitClaimReferenceNumber();
                        //claimantType = shbeHandler.getClaimantType(DRecord0);
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
                Iterator<Integer> ite = result.keySet().iterator();
                while (ite.hasNext()) {
                    level = ite.next();
                    result0 = result.get(level);
                    outDir2 = Paths.get(outDir1.toString(), level.toString());
                    Files.createDirectories(outDir2);
                    outFile = Paths.get(outDir2.toString(), YM3 + ".csv");
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
