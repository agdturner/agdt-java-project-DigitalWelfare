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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;

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
    protected DW_UO_Handler DW_UO_Handler;

    /**
     * UnderOccupied in April 2013
     */
    String sUOInApril2013 = "UInApr13";

    //String sTenancyChange = "TenancyChange";
    String sMinus999 = "-999";
    String sAAN_NAA = "AAN_NAA";

    //String sAllClaimants = "Al";
    String sAllClaimants = "All";
    //String sOnFlow = "OF";
    String sOnFlow = "OnFlow";
    //String sReturnFlow = "RF";
    String sReturnFlow = "ReturnFlow";
    //String sStable = "St";
    String sStable = "Stable";
    //String sAllInChurn = "AI";
    String sAllInChurn = "AllInChurn";
    //String sAllOutChurn = "AO";
    String sAllOutChurn = "AllOutChurn";
    String sUnknown = "Unknown";
    String snull = "null";

    String sInDistanceChurn = "InDistanceChurn";
    String sWithinDistanceChurn = "WithinDistanceChurn";
    String sOutDistanceChurn = "OutDistanceChurn";

    boolean doGrouped = false;
    boolean doTenancyChangesUO = false;
    boolean doSummaryTables = false;
    boolean doSummaryTablesAllOnly = false;
    boolean doSummaryTablesUOOnly = false;
    boolean doTenancyTransitions = false;
    boolean doPostcodeChanges = false;
    boolean doTenancyChanges = false;
    boolean doTTTAndPT = false;
    boolean doAggregation = false;
    boolean doHBGeneralAggregateStatistics = false;

    /**
     * For convenience DW_SHBE_TenancyType_Handler =
     * env.getDW_SHBE_TenancyType_Handler().
     */
    protected DW_SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;

    public DW_ProcessorLCCHBGeneralAggregateStatistics(DW_Environment env) {
        super(env);
        DW_UO_Handler = env.getDW_UO_Handler();
        DW_SHBE_TenancyType_Handler = env.getDW_SHBE_TenancyType_Handler();
        DW_SHBE_Data = env.getDW_SHBE_Data();
        ClaimIDToClaimRefLookup = DW_SHBE_Data.getClaimIDToClaimRefLookup();
        DW_UO_Data = env.getDW_UO_Data();
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
    }

    @Override
    public void run() {
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
        DW_SHBE_Records DW_SHBE_Records;
        HashMap<DW_ID, DW_SHBE_Record> ClaimIDToDW_SHBE_RecordMap;
        DW_ID DW_ID;
        DW_SHBE_D_Record DRecord;
        String ClaimPostcodeF;
        int numberOfChildDependents;
        int householdSize;
        Iterator<String> levelsIte;
        String level;
        TreeMap<String, String> ClaimPostcodeF_To_LevelCodeMap;
        String AreaCode;
        File outDir;
        File outDir1;
        File outDir2;
        File outFile;
        String YM3;
        PrintWriter outPW;
        String PT;

        // Initialisiation
        levels = new ArrayList<String>();
//        levels.add(DW_Strings.sOA);
//        levels.add(DW_Strings.sLSOA);
//        levels.add("MSOA");
//        levels.add("PostcodeUnit");
//        levels.add("PostcodeSector");
//        levels.add("PostcodeDistrict");
        levels.add(DW_Strings.sParliamentaryConstituency);
        levels.add(DW_Strings.sStatisticalWard);
        includes = DW_SHBE_Handler.getIncludes();
//            includes.remove(DW_Strings.sIncludeAll);
//            includes.remove(DW_Strings.sIncludeYearly);
//            includes.remove(DW_Strings.sInclude6Monthly);
//            includes.remove(DW_Strings.sInclude3Monthly);
//            includes.remove(DW_Strings.sIncludeMonthlySinceApril2013);
//            includes.remove(DW_Strings.sIncludeMonthly);
        PTs = DW_Strings.getPaymentTypes();
//            PTs.remove(DW_Strings.sPaymentTypeAll);
//            PTs.remove(DW_Strings.sPaymentTypeIn);
//            PTs.remove(DW_Strings.sPaymentTypeSuspended);
//            PTs.remove(DW_Strings.sPaymentTypeOther);
        PTsIte = PTs.iterator();
        outDir = new File(
                DW_Files.getOutputSHBETablesDir(),
                "HBGeneralAggregateStatistics");
        env.log("Output Directory " + outDir.toString());
        PTsIte = PTs.iterator();
        while (PTsIte.hasNext()) {
            PT = PTsIte.next();
            includeName = DW_Strings.sIncludeAll;
            outDir1 = new File(
                    outDir,
                    PT);
            include = includes.get(includeName);
            includeIte = include.iterator();
            while (includeIte.hasNext()) {
                i = includeIte.next();
                YM3 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                env.logO("Generalising " + YM3, true);
                // Get Lookup
                ClaimPostcodeF_To_LevelCode_Maps = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3);
                // Load first data
                DW_SHBE_Records = DW_SHBE_Data.getDW_SHBE_Records(YM3);
                ClaimIDToDW_SHBE_RecordMap = DW_SHBE_Records.getClaimIDToDW_SHBE_RecordMap(true);
                //DW_SHBE_Records = env.getDW_SHBE_Data().getData().get(YM3);
                //records0 = DW_SHBE_Records.getDataPTI(env._HandleOutOfMemoryError_boolean);
                TreeMap<String, TreeMap<String, int[]>> result;
                result = new TreeMap<String, TreeMap<String, int[]>>();
                TreeMap<String, int[]> result0;
                int[] resultValues;

                // Iterate over records
                Iterator<DW_ID> DW_IDIte = ClaimIDToDW_SHBE_RecordMap.keySet().iterator();
                while (DW_IDIte.hasNext()) {
                    DW_ID = DW_IDIte.next();
                    DW_SHBE_Record DW_SHBE_Record;
                    DW_SHBE_Record = ClaimIDToDW_SHBE_RecordMap.get(DW_ID);
                    DRecord = DW_SHBE_Record.getDRecord();
                    numberOfChildDependents = DRecord.getNumberOfChildDependents();
                    householdSize = DW_SHBE_Handler.getHouseholdSizeint(DRecord);
                    ClaimPostcodeF = DW_SHBE_Record.getClaimPostcodeF();
                    //councilTaxBenefitClaimReferenceNumber0 = DRecord0.getCouncilTaxBenefitClaimReferenceNumber();
                    //claimantType = DW_SHBE_Handler.getClaimantType(DRecord0);
                    if (ClaimPostcodeF != null) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            level = levelsIte.next();
                            if (result.containsKey(level)) {
                                result0 = result.get(level);
                            } else {
                                result0 = new TreeMap<String, int[]>();
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
                                resultValues[1] += numberOfChildDependents;
                                resultValues[2] += householdSize;
                            } else {
                                resultValues = new int[3];
                                resultValues[0] = 1;
                                resultValues[1] = numberOfChildDependents;
                                resultValues[2] = householdSize;
                                result0.put(AreaCode, resultValues);
                            }
                        }
                    }
                }

                // Write out result
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
                    outPW = Generic_StaticIO.getPrintWriter(outFile, false);
                    outPW.println("AreaCode, NumberOfHBClaims, NumberOfChildDependentsInHBClaimingHouseholds, TotalPopulationInHBClaimingHouseholds");
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
                                    + ", " + resultValues[2]);
                        }
                    }
                    outPW.flush();
                    outPW.close();
                }
            }
        }
    }

    /**
     * For the postcode input, this returns the area code for the level input
     * using tLookupFromPostcodeToCensusCode.
     *
     * @param level
     * @param postcode
     * @param LookupFromPostcodeToCensusCode
     * @return The area code for level from tLookupFromPostcodeToCensusCode for
     * postcode. This may return "" or null.
     */
    public String getAreaCode(
            String level,
            String postcode,
            TreeMap<String, String> LookupFromPostcodeToCensusCode) {
        String result = "";
        // Special Case
        if (postcode.trim().isEmpty()) {
            return result;
        }
        if (level.equalsIgnoreCase(DW_Strings.sOA)
                || level.equalsIgnoreCase(DW_Strings.sLSOA)
                || level.equalsIgnoreCase(DW_Strings.sMSOA)
                || level.equalsIgnoreCase(DW_Strings.sStatisticalWard)
                || level.equalsIgnoreCase(DW_Strings.sParliamentaryConstituency)) {
            String formattedPostcode = DW_Postcode_Handler.formatPostcode(postcode);
            result = LookupFromPostcodeToCensusCode.get(
                    formattedPostcode);
            if (result == null) {
                result = "";
            }
        } else if (level.equalsIgnoreCase(DW_Strings.sPostcodeUnit)
                || level.equalsIgnoreCase(DW_Strings.sPostcodeSector)
                || level.equalsIgnoreCase(DW_Strings.sPostcodeDistrict)) {
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeUnit)) {
                result = postcode;
            }
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeSector)) {
                result = DW_Postcode_Handler.getPostcodeSector(postcode);
            }
            if (level.equalsIgnoreCase(DW_Strings.sPostcodeDistrict)) {
                result = DW_Postcode_Handler.getPostcodeDistrict(postcode);
            }
        } else {
            // Unrecognised level
            env.log("Unrecognised level in "
                    + this.getClass().getName()
                    + ".getAreaCode(String,String,TreeMap<String, String>)");
        }
        return result;
    }

    /**
     * For the postcode input, this returns the area code for the level input
     * using tLookupFromPostcodeToCensusCode.
     *
     * @param level
     * @param ClaimPostcodeF1
     * @param tLookupFromPostcodeToCensusCode
     * @return The area code for level from tLookupFromPostcodeToCensusCode for
     * postcode. This may return "" or null.
     */
    public String getAreaCode(
            String level,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode,
            String ClaimPostcodeF1
    ) {
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getDW_Postcode_Handler();
        String result = "";
        // Special Case
        if (ClaimPostcodeF1.trim().isEmpty()) {
            return result;
        }
        if (level.equalsIgnoreCase("OA")
                || level.equalsIgnoreCase("LSOA")
                || level.equalsIgnoreCase("MSOA")) {
            result = tLookupFromPostcodeToCensusCode.get(
                    ClaimPostcodeF1);
            if (result == null) {
                result = "";
            }
        } else if (level.equalsIgnoreCase("PostcodeUnit")
                || level.equalsIgnoreCase("PostcodeSector")
                || level.equalsIgnoreCase("PostcodeDistrict")) {
            if (level.equalsIgnoreCase("PostcodeUnit")) {
                result = ClaimPostcodeF1;
            }
            if (level.equalsIgnoreCase("PostcodeSector")) {
                result = DW_Postcode_Handler.getPostcodeSector(ClaimPostcodeF1);
            }
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                result = DW_Postcode_Handler.getPostcodeDistrict(ClaimPostcodeF1);
            }
        } else {
            // Unrecognised level
            int debug = 1;
        }
        return result;
    }
}
