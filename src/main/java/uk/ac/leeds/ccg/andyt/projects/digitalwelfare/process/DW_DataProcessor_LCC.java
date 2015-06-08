/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_DataProcessor_LCC extends DW_Processor {

    private DW_SHBE_Handler tDW_SHBE_Handler;
    private DW_UnderOccupiedReport_Handler tDW_UnderOccupiedReport_Handler;

    public DW_DataProcessor_LCC() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new DW_DataProcessor_LCC().run();
    }

    @Override
    public void run() {
        init_handlers();
//        init_OutputTextFilePrintWriter(
//                DW_Files.getOutputSHBETablesDir(),
//                "DigitalWelfare");
//        // Load Data
//        ArrayList<Object[]> SHBEData;
//        SHBEData = tDW_SHBE_Handler.loadSHBEData();
//
//        ArrayList<DW_UnderOccupiedReport_Set>[] underOccupiedReport_Sets;
//        underOccupiedReport_Sets = tDW_UnderOccupiedReport_Handler.loadUnderOccupiedReportData();

//        // Data generalisation and output
//        processSHBEReportData(
//                SHBEData,
//                underOccupiedReport_Sets);
//        processSHBEReportDataForSarah(
//                SHBEData,
//                underOccupiedReport_Sets);
//        processSHBEReportDataIntoMigrationMatricesForApril(
//                SHBEData,
//                underOccupiedReport_Sets);
//        processforChangeInTenancyForMoversMatrixesForApril(
//                SHBEData,
//                underOccupiedReport_Sets);
//        processforChangeInTenancyMatrixesForApril(
//                SHBEData);
//        getTotalClaimantsByTenancyType();
        String level;
        String[] types;
        types = new String[4];
        types[0] = "NewEntrant";
        types[1] = "Stable";
        types[2] = "Churn";
        types[3] = "Unknown";
        for (int i = 0; i < types.length; i++) {
//            level = "OA";
//            aggregateClaimants(level, types[i]);
            level = "LSOA";
            aggregateClaimants(level, types[i]);
//            level = "MSOA";
//            aggregateClaimants(level, types[i]);
        }
//        level = "OA";
//        aggregateClaimants(level);
//        level = "LSOA";
//        aggregateClaimants(level);
//        level = "MSOA";
//        aggregateClaimants(level);
//        level = "PostcodeUnit";
//        aggregateClaimants(level);
//        level = "PostcodeSector";
//        aggregateClaimants(level);
//        level = "PostcodeDistrict";
//        aggregateClaimants(level);
    }

    /**
     * @param level currently only for Census levels
     * @param type type = NewEntrant type = Stable type = Churn
     */
    public void aggregateClaimants(
            String level,
            String type) {
        File outputDir;
        outputDir = new File(
                DW_Files.getGeneratedSHBEDir(),
                level);
        File dir = new File(
                outputDir,
                type);
        dir.mkdirs();
        TreeMap<String, String> tLookupFromPostcodeToCensusCode;
//        if (level.startsWith("Postcode")) {
//            getA
//        } else {
        tLookupFromPostcodeToCensusCode = getLookupFromPostcodeToCensusCode(
                level,
                2011);
//        }
        // 0, 2, 4, 7, 11, 17, 29, 41 are April data for 2008, 2009, 2010, 2011,  
        // 2012, 2013, 2014, 2015 respectively
        String[] SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        //String SHBEFilename = SHBEFilenames[0];
        Object[] SHBEData0;

        //SHBEData0 = loadSHBEData(SHBEFilenames[35]); //Here to debug
        SHBEData0 = loadSHBEData(SHBEFilenames[0]);

        for (int i = 1; i < SHBEFilenames.length; i++) {
            /*
             * Counts storesa count of those data that are in some way unexpected/erroneous
             */
            TreeMap<String, Integer> counts;
            counts = new TreeMap<String, Integer>();

            Object[] SHBEData1 = loadSHBEData(SHBEFilenames[i]);
            String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
            String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
            TreeMap<String, Integer> claimantsCountByArea;
            claimantsCountByArea = new TreeMap<String, Integer>();
            /*
             * result[0] is a TreeMap<String,DW_SHBE_Record> representing records with
             * DRecords, --------------------------------------------------------------
             * result[1] is a TreeMap<String, DW_SHBE_Record> representing records
             * without DRecords, ------------------------------------------------------
             * result[2] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,
             * result[3] is a HashSet<String> of PartnerNationalInsuranceNumberIDs,
             * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs
             * result[5] is a HashSet<String> of NonDependentsNationalInsuranceNumberIDs
             * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs
             */
            TreeMap<String, DW_SHBE_Record> DRecords0;
            DRecords0 = (TreeMap<String, DW_SHBE_Record>) SHBEData0[0];
            TreeMap<String, DW_SHBE_Record> DRecords1;
            DRecords1 = (TreeMap<String, DW_SHBE_Record>) SHBEData1[0];
            Iterator<String> ite = DRecords1.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                DW_SHBE_Record DRecord1 = DRecords1.get(claimID);
                String postcode1 = DRecord1.getClaimantsPostcode();
                if (postcode1 != null) {
                    String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode1);
                    String censusCode;
                    censusCode = tLookupFromPostcodeToCensusCode.get(
                            formattedPostcode);
                    if (censusCode != null) {
                        DW_SHBE_Record DRecord0 = DRecords0.get(claimID);
                        String postcode0;
                        if (DRecord0 == null) {
                            //This is a new entrant to the data
                            if (type.equalsIgnoreCase("NewEntrant")) {
                                Generic_Collections.addToTreeMapStringInteger(
                                        claimantsCountByArea,
                                        censusCode,
                                        1);
                            }
                        } else {
                            postcode0 = DRecord0.getClaimantsPostcode();
                            if (postcode0 == null) {
                                // Unknown
                                if (type.equalsIgnoreCase("Unknown")) {
                                    Generic_Collections.addToTreeMapStringInteger(
                                            claimantsCountByArea,
                                            censusCode,
                                            1);
                                }
                            } else {
                                if (postcode0.equalsIgnoreCase(postcode1)) {
                                    //no change
                                    if (type.equalsIgnoreCase("Stable")) {
                                        Generic_Collections.addToTreeMapStringInteger(
                                                claimantsCountByArea,
                                                censusCode,
                                                1);
                                    }
                                } else {
                                    // Churn
                                    if (type.equalsIgnoreCase("Churn")) {
                                        Generic_Collections.addToTreeMapStringInteger(
                                                claimantsCountByArea,
                                                censusCode,
                                                1);
                                    }
                                }
                            }
                        }
                    } else {
                        //System.out.println("No Census code for postcode: " + postcode1);
                        String firstPartPostcode;
                        firstPartPostcode = postcode1.trim().split(" ")[0];
                        Generic_Collections.addToTreeMapStringInteger(
                                counts, firstPartPostcode, 1);
                    }
                } else {
                    Generic_Collections.addToTreeMapStringInteger(
                            counts, "null", 1);
                }
            }
            //Write out result
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    year + month + ".csv");
            pw.println(level + ", Count");
            Iterator<String> ite2 = claimantsCountByArea.keySet().iterator();
            while (ite2.hasNext()) {
                String areaCode = ite2.next();
                Integer Count = claimantsCountByArea.get(areaCode);
                pw.println(areaCode + ", " + Count);
            }
            pw.close();
            SHBEData0 = SHBEData1;
//            //Write out result
//            pw1 = init_OutputTextFilePrintWriter("HBTenancyType" + year + month + ".csv");
//            pw1.println("TenancyType, Count");
//            ite2 = ymHBResult.keySet().iterator();
//            while (ite2.hasNext()) {
//                Integer aTenancyType = ite2.next();
//                Integer Count = ymHBResult.get(aTenancyType);
//                pw1.println(aTenancyType + ", " + Count);
//            }
//            pw1.close();
            // Report counts
            System.out.println("code,count");
            ite2 = counts.keySet().iterator();
            while (ite2.hasNext()) {
                String code = ite2.next();
                Integer count = counts.get(code);
                System.out.println("" + code + ", " + count);
            }
        }
//        return result;
    }

    /**
     * currently only for Census levels
     *
     * @param level
     */
    public void aggregateClaimants(String level) {
        File outputDir;
        outputDir = new File(
                DW_Files.getGeneratedSHBEDir(),
                level);
        String allClaimants_String = "AllClaimants";
        File dir = new File(
                outputDir,
                allClaimants_String);
        dir.mkdirs();
        TreeMap<String, String> tLookupFromPostcodeToCensusCode;
        tLookupFromPostcodeToCensusCode = getLookupFromPostcodeToCensusCode(
                level,
                2011);
        // 0, 2, 4, 7, 11, 17, 29, 41 are April data for 2008, 2009, 2010, 2011,  
        // 2012, 2013, 2014, 2015 respectively
        String[] SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        //String SHBEFilename = SHBEFilenames[0];
        for (String SHBEFilename : SHBEFilenames) {
            Object[] SHBEData = loadSHBEData(SHBEFilename);
            String year = DW_SHBE_Handler.getYear(SHBEFilename);
            String month = DW_SHBE_Handler.getMonth(SHBEFilename);
            TreeMap<String, Integer> claimantsCountByArea;
            claimantsCountByArea = new TreeMap<String, Integer>();
            /* 
             * SHBEData[0] is a TreeMap<String, DW_SHBE_Record> representing DRecords,---
             * SHBEData[1] is a TreeMap<String, DW_SHBE_Record> representing SRecords,---
             * SHBEData[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
             * SHBEData[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
             * SHBEData[5] is a HashSet<String> of
             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
             * SHBEData[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
             */
            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[0];
            Iterator<String> ite = DRecords.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                DW_SHBE_Record DRecord = DRecords.get(claimID);
                String postcode = DRecord.getClaimantsPostcode();
                String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
                String censusCode;
                censusCode = tLookupFromPostcodeToCensusCode.get(
                        formattedPostcode);
                if (censusCode != null) {
                    if (claimantsCountByArea.containsKey(censusCode)) {
                        int currentCount = claimantsCountByArea.get(censusCode);
                        int newCount = currentCount + 1;
                        claimantsCountByArea.put(censusCode, newCount);
                    } else {
                        claimantsCountByArea.put(censusCode, 1);
                    }
                } else {
                    System.out.println("No Census code for postcode: " + postcode);
                }
            }
            //Write out result
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    year + month + ".csv");
            pw.println(level + ", Count");
            Iterator<String> ite2 = claimantsCountByArea.keySet().iterator();
            while (ite2.hasNext()) {
                String areaCode = ite2.next();
                Integer Count = claimantsCountByArea.get(areaCode);
                pw.println(areaCode + ", " + Count);
            }
            pw.close();
//            //Write out result
//            pw1 = init_OutputTextFilePrintWriter("HBTenancyType" + year + month + ".csv");
//            pw1.println("TenancyType, Count");
//            ite2 = ymHBResult.keySet().iterator();
//            while (ite2.hasNext()) {
//                Integer aTenancyType = ite2.next();
//                Integer Count = ymHBResult.get(aTenancyType);
//                pw1.println(aTenancyType + ", " + Count);
//            }
//            pw1.close();
        }
//        return result;
    }

    public void getTotalClaimantsByTenancyType() {
        //TreeMap<String,TreeMap<Integer,Integer>> result = new TreeMap<String,TreeMap<Integer,Integer>>();
        String[] SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        for (String SHBEFilename : SHBEFilenames) {
            Object[] SHBEData = loadSHBEData(SHBEFilename);
            String year = DW_SHBE_Handler.getYear(SHBEFilename);
            String month = DW_SHBE_Handler.getMonth(SHBEFilename);
            TreeMap<Integer, Integer> ymAllResult = new TreeMap<Integer, Integer>();
            TreeMap<Integer, Integer> ymHBResult = new TreeMap<Integer, Integer>();
            //result.put(year+month, ymResult);
            /* result[0] is a TreeMap<String, DW_SHBE_Record> representing DRecords,---
             * result[1] is a TreeMap<String, DW_SHBE_Record> representing SRecords,---
             * result[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
             * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
             * result[5] is a HashSet<String> of
             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
             * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
             */
            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[0];
            Iterator<String> ite = DRecords.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                DW_SHBE_Record DRecord = DRecords.get(claimID);
                int aTenancyType = DRecord.getTenancyType();
                Integer aTenancyTypeInteger = aTenancyType;
                if (ymAllResult.containsKey(aTenancyTypeInteger)) {
                    int count = ymAllResult.get(aTenancyTypeInteger);
                    count++;
                    ymAllResult.put(aTenancyTypeInteger, count);
                } else {
                    ymAllResult.put(aTenancyTypeInteger, 1);
                }
                String aHousingBenefitClaimReferenceNumber = DRecord.getHousingBenefitClaimReferenceNumber();
                if (!aHousingBenefitClaimReferenceNumber.isEmpty()) {
                    if (ymHBResult.containsKey(aTenancyTypeInteger)) {
                        int count = ymAllResult.get(aTenancyTypeInteger);
                        count++;
                        ymHBResult.put(aTenancyTypeInteger, count);
                    } else {
                        ymHBResult.put(aTenancyTypeInteger, 1);
                    }
                }
            }
            PrintWriter pw;
            Iterator<Integer> ite2;
            //Write out result
            pw = init_OutputTextFilePrintWriter(
                    DW_Files.getOutputSHBETablesDir(),
                    "AllTenancyType" + year + month + ".csv");
            pw.println("TenancyType, Count");
            ite2 = ymAllResult.keySet().iterator();
            while (ite2.hasNext()) {
                Integer aTenancyType = ite2.next();
                Integer Count = ymAllResult.get(aTenancyType);
                pw.println(aTenancyType + ", " + Count);
            }
            pw.close();
            //Write out result
            pw = init_OutputTextFilePrintWriter(
                    DW_Files.getOutputSHBETablesDir(),
                    "HBTenancyType" + year + month + ".csv");
            pw.println("TenancyType, Count");
            ite2 = ymHBResult.keySet().iterator();
            while (ite2.hasNext()) {
                Integer aTenancyType = ite2.next();
                Integer Count = ymHBResult.get(aTenancyType);
                pw.println(aTenancyType + ", " + Count);
            }
            pw.close();
        }
//        return result;
    }

//    public TreeMap<String,TreeMap<Integer,Integer>> getTotalClaimantsByTenancyType() {
//        TreeMap<String,TreeMap<Integer,Integer>> result = new TreeMap<String,TreeMap<Integer,Integer>>();
//        String[] SHBEFilenames = getSHBEFilenamesAll();
//        for (int i = 0; i < SHBEFilenames.length; i++) {
//            Object[] SHBEData = loadSHBEData(SHBEFilenames[i]);
//            String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
//            String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
//            TreeMap<Integer,Integer> ymResult = new TreeMap<Integer,Integer>();
//            result.put(year+month, ymResult);
//            /* result[0] is a TreeMap<String, DW_SHBE_Record> representing DRecords,---
//             * result[1] is a TreeMap<String, DW_SHBE_Record> representing SRecords,---
//             * result[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
//             * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
//             * result[5] is a HashSet<String> of
//             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
//             * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
//             */
//            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[0];
//            Iterator<String> ite = DRecords.keySet().iterator();
//            while (ite.hasNext()) {
//                String claimID = ite.next();
//                DW_SHBE_Record aSHBE_DataRecord = DRecords.get(claimID);
//                int aTenancyType = aSHBE_DataRecord.getTenancyType();
//                Integer aTenancyTypeInteger = new Integer(aTenancyType);
//                if (ymResult.containsKey(aTenancyTypeInteger)) {
//                    int count = ymResult.get(aTenancyTypeInteger);
//                    count ++;
//                    ymResult.put(aTenancyTypeInteger,count);
//                } else {
//                    ymResult.put(aTenancyTypeInteger,1);
//                }
//            }
//            //Write out result
//            PrintWriter pw = init_OutputTextFilePrintWriter(year+month+"TenancyTypeAll");
//            pw.println("TenancyType, Count");
//            Iterator<Integer> ite2 = ymResult.keySet().iterator();
//            while (ite2.hasNext()) {
//                Integer aTenancyType = ite2.next();
//                Integer Count = ymResult.get(aTenancyType);
//                pw.println(aTenancyType + ", " + Count);
//            }
//        }
//        return result;
//    }
    /**
     *
     * @param underOccupiedReportData
     */
    public void processforChangeInTenancyForMoversMatrixesForApril(
            ArrayList<Object[]> SHBEData,
            ArrayList<DW_UnderOccupiedReport_Set>[] aUnderOccupiedReport_Set) {
//        TreeMap<String, DW_UnderOccupiedReport_Record>[] councilRecords;
//        TreeMap<String, DW_UnderOccupiedReport_Record>[] registeredSocialLandlordRecords;
//        councilRecords = aUnderOccupiedReport_Set.getCouncilRecords();
//        registeredSocialLandlordRecords = aUnderOccupiedReport_Set.getRegisteredSocialLandlordRecords();

        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "CountOfClaimsByDates.txt");

        // 0, 2, 4, 7, 11, 17, 29, 41 are April data for 2008, 2009, 2010, 2011,  
        // 2012, 2013, 2014, 2015 respectively
        String[] allSHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
        int startIndex;
        int endIndex;
        HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims;
        AllNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
        HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndMoves;
        AllNationalInsuranceNumbersAndMoves = new HashMap<String, HashSet<String>>();
        HashMap<String, TreeSet<String>> HBNationalInsuranceNumbersAndDatesOfClaims;
        HBNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
        HashMap<String, HashSet<String>> HBNationalInsuranceNumbersAndMoves;
        HBNationalInsuranceNumbersAndMoves = new HashMap<String, HashSet<String>>();
        String startMonth;
        String endMonth;
        String startYear;
        String endYear;
        int countOfNewButPreviousClaimant;
        Object[] migrationData;

        // 2008 2009
        startIndex = 0;
        endIndex = 2;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        /**
         * migrationData[0] = HashSet<String>
         * migrationData[1] = HashSet<String>
         * migrationData[2] = HashMap<String, String>
         * migrationData[3] = HashSet<String>
         * migrationData[4] = HashSet<String>
         * migrationData[5] = HashMap<String, String>
         */
        HashSet<String> AllNINOOfClaimants2008 = (HashSet<String>) migrationData[0];
        HashSet<String> AllNINOOfClaimants2009 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20082009 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2008 = (HashSet<String>) migrationData[3];
        HashSet<String> HBNINOOfClaimants2009 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20082009 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2008,
                AllNINOOfClaimants2009,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2009 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2009 2010
        startIndex = 2;
        endIndex = 4;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2010 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20092010 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2010 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20092010 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2009,
                AllNINOOfClaimants2010,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2010 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2010 2011
        startIndex = 4;
        endIndex = 7;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2011 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20102011 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2011 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20102011 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2010,
                AllNINOOfClaimants2011,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2011 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2011 2012
        startIndex = 7;
        endIndex = 11;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2012 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20112012 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2012 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20112012 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2011,
                AllNINOOfClaimants2012,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2012 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2012 2013
        startIndex = 11;
        endIndex = 17;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2013 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2013 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2012,
                AllNINOOfClaimants2013,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2013 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2013 2014
        startIndex = 17;
        endIndex = 29;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2014 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20132014 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2014 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20132014 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2013,
                AllNINOOfClaimants2014,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2014 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2014 2015
        startIndex = 29;
        endIndex = 41;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                aUnderOccupiedReport_Set,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2015 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20142015 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2015 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20142015 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2014,
                AllNINOOfClaimants2015,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2015 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

//        TreeMap<String, Integer> countOfClaimsByDate = getCountOfClaimsByDates(
//                AllNationalInsuranceNumbersAndDatesOfClaims);
//        // writeout countOfClaimsByDate
//        pw.println("CountOfClaimsByDates");
//        pw.println("Dates,CountOfClaims");
//        Iterator<String> ite = countOfClaimsByDate.keySet().iterator();
//        while (ite.hasNext()) {
//            String dates = ite.next();
//            Integer count = countOfClaimsByDate.get(dates);
//            pw.println(dates + " " + count);
//        }
        pw.close();

    }

    /**
     *
     */
    public void processforChangeInTenancyMatrixesForApril(
            ArrayList<Object[]> SHBEData) {
//
//        PrintWriter pw = init_OutputTextFilePrintWriter("CountOfClaimsByDates.txt");

        // 0, 2, 4, 7, 11, 17, 29, 41 are April data for 2008, 2009, 2010, 2011,  
        // 2012, 2013, 2014, 2015 respectively
        String[] allSHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
        int startIndex;
        int endIndex;
        String startMonth;
        String endMonth;
        String startYear;
        String endYear;

        // 2008 2009
        startIndex = 0;
        endIndex = 2;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2009 2010
        startIndex = 2;
        endIndex = 4;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2010 2011
        startIndex = 4;
        endIndex = 7;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2011 2012
        startIndex = 7;
        endIndex = 11;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2012 2013
        startIndex = 11;
        endIndex = 17;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2013 2014
        startIndex = 17;
        endIndex = 29;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);

        // 2014 2015
        startIndex = 29;
        endIndex = 41;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        processSHBEforChangeInTenancyMatrixesForApril(
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
    }

    public Object[] processSHBEforChangeInTenancyForMoversMatrixesForApril(
            ArrayList<DW_UnderOccupiedReport_Set>[] aUnderOccupiedReport_Set,
            HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndMoves,
            HashMap<String, TreeSet<String>> HBNationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> HBNationalInsuranceNumbersAndMoves,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            String[] allSHBEFilenames,
            int startIndex,
            int endIndex) {
        Object[] result;
        result = new Object[6];
        HashSet<String> AllNINOOfClaimantsStartYear = new HashSet<String>();
        HashSet<String> AllNINOOfClaimantsEndYear = new HashSet<String>();
        HashMap<String, String> AllNINOOfClaimantsThatMoved = new HashMap<String, String>();
        HashSet<String> HBNINOOfClaimantsStartYear = new HashSet<String>();
        HashSet<String> HBNINOOfClaimantsEndYear = new HashSet<String>();
        HashMap<String, String> HBNINOOfClaimantsThatMoved = new HashMap<String, String>();
        result[0] = AllNINOOfClaimantsStartYear;
        result[1] = AllNINOOfClaimantsEndYear;
        result[2] = AllNINOOfClaimantsThatMoved;
        result[3] = HBNINOOfClaimantsStartYear;
        result[4] = HBNINOOfClaimantsEndYear;
        result[5] = HBNINOOfClaimantsThatMoved;

        String type;
        Object[] migrationData;
        // Allmigration
        type = "all";
        migrationData = getChangeInTenancyForMovers(
                allSHBEFilenames,
                AllNINOOfClaimantsStartYear,
                AllNINOOfClaimantsEndYear,
                AllNINOOfClaimantsThatMoved,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                startIndex,
                endIndex);
        writeOutTenancyTypeTransitionMatrix(
                allSHBEFilenames,
                migrationData,
                startIndex,
                endIndex,
                type);
//        // Housing Benefit only migration
//        type = "HB";
//        migrationData = getHBClaimantOnlyMigrationData(
//                allSHBEFilenames,
//                HBNINOOfClaimantsStartYear,
//                HBNINOOfClaimantsEndYear,
//                HBNINOOfClaimantsThatMoved,
//                HBNationalInsuranceNumbersAndDatesOfClaims,
//                HBNationalInsuranceNumbersAndMoves,
//                startYear,
//                startMonth,
//                endYear,
//                endMonth,
//                startIndex,
//                endIndex);
//        writeOutMigrationMatrix(
//                allSHBEFilenames,
//                migrationData,
//                startIndex,
//                endIndex,
//                type);
        return result;
    }

    public void processSHBEforChangeInTenancyMatrixesForApril(
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            String[] allSHBEFilenames,
            int startIndex,
            int endIndex) {
        String type;
        Object[] migrationData;
        // All Transitions
        type = "all";
        migrationData = getChangeInTenancy(
                allSHBEFilenames,
                startYear,
                startMonth,
                endYear,
                endMonth,
                startIndex,
                endIndex);
        writeOutTenancyTypeTransitionMatrix(
                allSHBEFilenames,
                migrationData,
                startIndex,
                endIndex,
                type);
    }

    /**
     * Returns a migration matrix for all claimants.
     *
     * @param tSHBEfilenames
     * @param NINOOfClaimantsStartYear
     * @param NINOOfClaimantsEndYear
     * @param NINOOfClaimantsThatMoved
     * @param NationalInsuranceNumbersAndDatesOfClaims
     * @param NationalInsuranceNumbersAndMoves
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param startIndex
     * @param endMonth
     * @param endIndex
     * @return Object[] result where: ------------------------------------------
     * result[0] is a <code>TreeMap<Integer, TreeMap<String, Integer>></code>
     * Migration Matrix; -------------------------------------------------------
     * result[1] is a <code>TreeSet<String></code> of origins/destinations; ----
     */
    public Object[] getChangeInTenancyForMovers(
            String[] tSHBEfilenames,
            HashSet<String> NINOOfClaimantsStartYear,
            HashSet<String> NINOOfClaimantsEndYear,
            HashMap<String, String> NINOOfClaimantsThatMoved,
            HashMap<String, TreeSet<String>> NationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> NationalInsuranceNumbersAndMoves,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            int startIndex,
            int endIndex) {
        Object[] result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> resultMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        result[0] = resultMatrix;
        TreeSet<Integer> originsAndDestinations = new TreeSet<Integer>();
        originsAndDestinations.add(1);
        originsAndDestinations.add(2);
        originsAndDestinations.add(3);
        originsAndDestinations.add(4);
        originsAndDestinations.add(5);
        originsAndDestinations.add(6);
        originsAndDestinations.add(7);
        originsAndDestinations.add(8);
        originsAndDestinations.add(9);
        originsAndDestinations.add(-999);
        result[1] = originsAndDestinations;
        Object[] SHBEDataStart = loadSHBEData(tSHBEfilenames[startIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record startDRecord = DRecordsStart.get(councilTaxClaimNumber);
            if (startDRecord != null) {
                String postcodeStart = startDRecord.getClaimantsPostcode();
                postcodeStart = DW_Postcode_Handler.formatPostcode(postcodeStart);
                int startTenancyType = startDRecord.getTenancyType();
                //boolean claimantAlreadyHasBeenClaimant = false;
                // Check if claimant has already been a claimant and if so set claimantAlreadyHasBeenClaimant
                String NINO = startDRecord.getClaimantsNationalInsuranceNumber();
                NINOOfClaimantsStartYear.add(NINO);
                if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                    TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                    DatesOfClaims.add(startMonth + startYear);
                } else {
                    TreeSet<String> DatesOfClaims = new TreeSet<String>();
                    DatesOfClaims.add(startMonth + startYear);
                    NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                }
                //if (!startPostcodeDistrict.isEmpty()) {
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    //originsAndDestinations.add(startPostcodeDistrict);
                }

                DW_SHBE_Record endDRecord = DRecordsEnd.get(councilTaxClaimNumber);
                //String destinationPostcodeDistrict;
                Integer endTenancyType;
                String destinationPostcode = null;
                if (endDRecord == null) {
                    endTenancyType = -999;
                } else {
                    destinationPostcode = endDRecord.getClaimantsPostcode();
                    destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                    endTenancyType = endDRecord.getTenancyType();
                    NINOOfClaimantsEndYear.add(NINO);
                    if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                        TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                        DatesOfClaims.add(endMonth + endYear);
                    } else {
                        TreeSet<String> DatesOfClaims = new TreeSet<String>();
                        DatesOfClaims.add(endMonth + endYear);
                        NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                    }
                }
                // Filter out any non moves assumed to be when the postcode has not changed
                if (!postcodeStart.equalsIgnoreCase(destinationPostcode)) {
                    //destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {
                    if (destinationCounts.containsKey(endTenancyType)) {
                        int current = destinationCounts.get(endTenancyType);
                        destinationCounts.put(endTenancyType, current + 1);
                    } else {
                        destinationCounts.put(endTenancyType, 1);
                        originsAndDestinations.add(endTenancyType);
                    }
                    String move = startMonth + startYear + ", OriginPostcode " + postcodeStart + ", DestinationPostcode " + destinationPostcode;
                    NINOOfClaimantsThatMoved.put(NINO, move);
                    if (NationalInsuranceNumbersAndMoves.containsKey(NINO)) {
                        HashSet<String> DatesOfMoves = NationalInsuranceNumbersAndMoves.get(NINO);
                        DatesOfMoves.add(move);
                    } else {
                        HashSet<String> DatesOfMoves = new HashSet<String>();
                        DatesOfMoves.add(move);
                        NationalInsuranceNumbersAndMoves.put(NINO, DatesOfMoves);
                    }
                }
                //}
                //}
            }
        }
        // Add to matrix from unknown origins
        ite = DRecordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
                if (DRecordStart == null) {
                    //String startPostcodeDistrict = "unknown";
                    Integer startTenancyType = -999;
                    //boolean claimantAlreadyHasBeenClaimant = false;
                    // Check if claimant has already been a claimant and if so set claimantAlreadyHasBeenClaimant
                    String NINO = DRecordEnd.getClaimantsNationalInsuranceNumber();
                    NINOOfClaimantsEndYear.add(NINO);
                    if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                        TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                        DatesOfClaims.add(startMonth + startYear);
                        //startPostcodeDistrict += "_butPreviousClaimant";
                        //claimantAlreadyHasBeenClaimant = true;
                    } else {
                        TreeSet<String> DatesOfClaims = new TreeSet<String>();
                        DatesOfClaims.add(startMonth + startYear);
                        NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                    }
                    String destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                    Integer endTenancyType = DRecordEnd.getTenancyType();
                    //String destinationPostcodeDistrict = getPostcodeDistrict(destinationPostcode);
                    //destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {

                    if (resultMatrix.containsKey(endTenancyType)) {
                        destinationCounts = resultMatrix.get(endTenancyType);
                    } else {
                        destinationCounts = new TreeMap<Integer, Integer>();
                        resultMatrix.put(endTenancyType, destinationCounts);
                        //originsAndDestinations.add(startPostcodeDistrict);
                    }
                    if (destinationCounts.containsKey(endTenancyType)) {
                        int current = destinationCounts.get(endTenancyType);
                        destinationCounts.put(endTenancyType, current + 1);
                    } else {
                        destinationCounts.put(endTenancyType, 1);
                        originsAndDestinations.add(endTenancyType);
                    }
                    String move = startMonth + startYear + ", startTenancyType " + startTenancyType + ", endTenancyType " + endTenancyType;
                    NINOOfClaimantsThatMoved.put(NINO, move);
                    if (NationalInsuranceNumbersAndMoves.containsKey(NINO)) {
                        HashSet<String> DatesOfMoves = NationalInsuranceNumbersAndMoves.get(NINO);
                        DatesOfMoves.add(move);
                    } else {
                        HashSet<String> DatesOfMoves = new HashSet<String>();
                        DatesOfMoves.add(move);
                        NationalInsuranceNumbersAndMoves.put(NINO, DatesOfMoves);
                    }
                    //}
                }
            }
        }
        return result;
    }

    /**
     * Returns a migration matrix for all claimants.
     *
     * @param tSHBEfilenames
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param startIndex
     * @param endMonth
     * @param endIndex
     * @return Object[] result where: ------------------------------------------
     * result[0] is a TreeMap<Integer, TreeMap<String, Integer>> Matrix
     * where:--- keys are the starts or origins; and values Maps with keys being
     * the ends or destinations and values being the counts;--------------------
     * result[1] is a <code>TreeSet<String></code> of origins/destinations;-----
     */
    public Object[] getChangeInTenancy(
            String[] tSHBEfilenames,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            int startIndex,
            int endIndex) {
        Object[] result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> resultMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        result[0] = resultMatrix;
        TreeSet<Integer> originsAndDestinations = new TreeSet<Integer>();
        originsAndDestinations.add(1);
        originsAndDestinations.add(2);
        originsAndDestinations.add(3);
        originsAndDestinations.add(4);
        originsAndDestinations.add(5);
        originsAndDestinations.add(6);
        originsAndDestinations.add(7);
        originsAndDestinations.add(8);
        originsAndDestinations.add(9);
        originsAndDestinations.add(-999);
        result[1] = originsAndDestinations;
        Object[] SHBEDataStart = loadSHBEData(tSHBEfilenames[startIndex]);
        TreeMap<String, DW_SHBE_Record> startDRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_Record> endDRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = startDRecords.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record startDRecord = startDRecords.get(councilTaxClaimNumber);
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                DW_SHBE_Record endDRecord = endDRecords.get(councilTaxClaimNumber);
                Integer endTenancyType;
                if (endDRecord == null) {
                    endTenancyType = -999;
                } else {
                    endTenancyType = endDRecord.getTenancyType();
                }
                if (destinationCounts.containsKey(endTenancyType)) {
                    int current = destinationCounts.get(endTenancyType);
                    destinationCounts.put(endTenancyType, current + 1);
                } else {
                    destinationCounts.put(endTenancyType, 1);
                    originsAndDestinations.add(endTenancyType);
                }
            }
        }
        // Add to matrix from unknown origins
        ite = endDRecords.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record endDRecord = endDRecords.get(councilTaxClaimNumber);
            if (endDRecord != null) {
                DW_SHBE_Record DRecordStart = startDRecords.get(councilTaxClaimNumber);
                if (DRecordStart == null) {
                    //String startPostcodeDistrict = "unknown";
                    Integer startTenancyType = -999;
                    Integer endTenancyType = endDRecord.getTenancyType();
                    if (resultMatrix.containsKey(endTenancyType)) {
                        destinationCounts = resultMatrix.get(endTenancyType);
                    } else {
                        destinationCounts = new TreeMap<Integer, Integer>();
                        resultMatrix.put(endTenancyType, destinationCounts);
                        originsAndDestinations.add(endTenancyType);
                    }
                    if (destinationCounts.containsKey(endTenancyType)) {
                        int current = destinationCounts.get(endTenancyType);
                        destinationCounts.put(endTenancyType, current + 1);
                    } else {
                        destinationCounts.put(endTenancyType, 1);
                        originsAndDestinations.add(endTenancyType);
                    }
                }
            }
        }
        return result;
    }

    /**
     * A method used for creating some preliminary results.
     *
     * @param SHBEData_Sets
     * @param underOccupiedReport_Sets
     */
    public void processSHBEReportData(
            ArrayList<Object[]> SHBEData_Sets,
            ArrayList<DW_UnderOccupiedReport_Set>[] underOccupiedReport_Sets) {
        /*
         * 0 Apr 2013 14 Under Occupied Report For University Year Start Council Tenants.csv
         * 1 May 2013 14 Under Occupied Report For University Month 1 Council Tenants.csv
         * 2 Jun 2013 14 Under Occupied Report For University Month 2 Council Tenants.csv
         * 3 Jul 2013 14 Under Occupied Report For University Month 3 Council Tenants.csv
         * 4 Aug 2013 14 Under Occupied Report For University Month 4 Council Tenants.csv
         * 5 Sep 2013 14 Under Occupied Report For University Month 5 Council Tenants.csv
         * 6 Oct 2013 14 Under Occupied Report For University Month 6 Council Tenants.csv
         * 7 Nov 2013 14 Under Occupied Report For University Month 7 Council Tenants.csv
         * 8 Dec 2013 14 Under Occupied Report For University Month 8 Council Tenants.csv
         * 9 Jan 2013 14 Under Occupied Report For University Month 9 Council Tenants.csv
         * 10 Feb 2013 14 Under Occupied Report For University Month 10 Council Tenants.csv
         * 11 Mar 2013 14 Under Occupied Report For University Month 11 Council Tenants.csv
         * 12 Apr 2013 14 Under Occupied Report For University Month 12 Council Tenants.csv
         * 13 May 2014 15 Under Occupied Report For University Month 1 Council Tenants.csv
         * 14 Jun 2014 15 Under Occupied Report For University Month 2 Council Tenants.csv
         * 15 Jul 2014 15 Under Occupied Report For University Month 3 Council Tenants.csv
         */
        /*
         * 0 hb9803_SHBE_206728k April 2008.csv
         * 1 hb9803_SHBE_234696k October 2008.csv
         * 2 hb9803_SHBE_265149k April 2009.csv
         * 3 hb9803_SHBE_295723k October 2009.csv
         * 4 hb9803_SHBE_329509k April 2010.csv
         * 5 hb9803_SHBE_363186k October 2010.csv
         * 6 hb9803_SHBE_391746k March 2011.csv
         * 7 hb9803_SHBE_397524k April 2011.csv
         * 8 hb9803_SHBE_415181k July 2011.csv
         * 9 hb9803_SHBE_433970k October 2011.csv
         * 10 hb9803_SHBE_451836k January 2012.csv
         * 11 hb9803_SHBE_470742k April 2012.csv
         * 12 hb9803_SHBE_490903k July 2012.csv
         * 13 hb9803_SHBE_511038k October 2012.csv
         * 14 hb9803_SHBE_530243k January 2013.csv
         * 15 hb9803_SHBE_536123k February 2013.csv
         * 16 hb9991_SHBE_543169k March 2013.csv
         * 17 hb9991_SHBE_549416k April 2013.csv
         * 18 hb9991_SHBE_555086k May 2013.csv
         * 19 hb9991_SHBE_562036k June 2013.csv
         * 20 hb9991_SHBE_568694k July 2013.csv
         * 21 hb9991_SHBE_576432k August 2013.csv
         * 22 hb9991_SHBE_582832k September 2013.csv
         * 23 hb9991_SHBE_589664k Oct 2013.csv
         * 24 hb9991_SHBE_596500k Nov 2013.csv
         * 25 hb9991_SHBE_603335k Dec 2013.csv
         * 26 hb9991_SHBE_609791k Jan 2014.csv
         * 27 hb9991_SHBE_615103k Feb 2014.csv
         * 28 hb9991_SHBE_621666k Mar 2014.csv
         * 29 hb9991_SHBE_629066k Apr 2014.csv
         */
        /*
         * Correspondence between data
         * UnderoccupancyIndex, SHBEIndex
         * 0, 17
         * 1, 18
         * 2, 19
         * 3, 20
         * 4, 21
         * 5, 22
         * 6, 23
         * 7, 24
         * 8, 25
         * 9, 26
         * 10, 27
         * 11, 28
         * 12, 29
         */
        String[] dates;
        dates = new String[13];
        dates[0] = "2013-04";
        dates[1] = "2013-05";
        dates[2] = "2013-06";
        dates[3] = "2013-07";
        dates[4] = "2013-08";
        dates[5] = "2013-09";
        dates[6] = "2013-10";
        dates[7] = "2013-11";
        dates[8] = "2013-12";
        dates[9] = "2014-01";
        dates[10] = "2014-02";
        dates[11] = "2014-03";
        dates[12] = "2014-04";
        ArrayList<DW_UnderOccupiedReport_Set> councilRecords;
        councilRecords = underOccupiedReport_Sets[0];
        PrintWriter pwAggregate;
        pwAggregate = init_OutputTextFilePrintWriter(
                DW_Files.getOutputUnderOccupiedDir(),
                "DigitalWelfareOutputUnderOccupiedReport" + dates[0] + "To" + dates[dates.length - 1] + ".txt");
        TreeMap<String, BigDecimal> postcodeTotalArrearsTotal = new TreeMap<String, BigDecimal>();
        TreeMap<String, Integer> postcodeClaimsTotal = new TreeMap<String, Integer>();
        int UnderoccupancyIndex;
        int SHBEIndex;
        for (int i = 0; i < dates.length; i++) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    DW_Files.getOutputUnderOccupiedDir(),
                    "DigitalWelfareOutputUnderOccupiedReport" + dates[i] + ".txt");
            UnderoccupancyIndex = i;
            SHBEIndex = i + 17;
            DW_UnderOccupiedReport_Set set;
            set = councilRecords.get(UnderoccupancyIndex);
            Object[] SHBESet;
            SHBESet = SHBEData_Sets.get(SHBEIndex);

//            TreeMap<String, DW_SHBE_Record> DRecords;
//            DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
            DW_SHBE_CollectionHandler handler;
            handler = (DW_SHBE_CollectionHandler) SHBESet[0];

            TreeMap<String, DW_SHBE_Record> SRecordsWithoutDRecord;
            SRecordsWithoutDRecord = (TreeMap<String, DW_SHBE_Record>) SHBESet[1];
            // Iterate over councilRecords and join these with SHBE records
            // Aggregate totalRentArrears by postcode
            int aggregations = 0;
            int totalSRecordCount = 0;
            int countNotMissingDRecords = 0;
            int countMissingDRecords = 0;
            BigDecimal totalRentArrears_BigDecimal = BigDecimal.ZERO;
            TreeMap<String, BigDecimal> postcodeTotalArrears = new TreeMap<String, BigDecimal>();
            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
            TreeMap<String, DW_UnderOccupiedReport_Record> map = set.getSet();
            Iterator<String> ite2;
            ite2 = map.keySet().iterator();
            String councilTaxClaimNumber;
            while (ite2.hasNext()) {
                councilTaxClaimNumber = ite2.next();
                DW_UnderOccupiedReport_Record underOccupiedReport_DataRecord;
                underOccupiedReport_DataRecord = map.get(councilTaxClaimNumber);
                double rentArrears = underOccupiedReport_DataRecord.getTotalRentArrears();
                BigDecimal rentArrears_BigDecimal = new BigDecimal(rentArrears);
                totalRentArrears_BigDecimal = totalRentArrears_BigDecimal.add(rentArrears_BigDecimal);
                DW_SHBE_Record DRecord;

                //DRecord = DRecords.get(councilTaxClaimNumber);
                DRecord = handler.getDRecord(councilTaxClaimNumber);

                if (DRecord == null) {
                    System.out.println("Warning: No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                    countMissingDRecords++;
                    DW_SHBE_Record SRecord = SRecordsWithoutDRecord.get(councilTaxClaimNumber);
                    if (SRecord != null) {
                        int dosomething = 1;
                        System.out.println("There is a SRecord without a DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                    }
                } else {
                    countNotMissingDRecords++;
                    String postcode = DRecord.getClaimantsPostcode();
                    String truncatedPostcode = postcode.substring(0, postcode.length() - 2);
                    int SRecordCount = DRecord.getSRecords().size();
                    totalSRecordCount += SRecordCount;
                    if (rentArrears < 0) {
                        int debug = 0;
                    }
                    // Add to totals for this month
                    if (postcodeTotalArrears.containsKey(truncatedPostcode)) {
                        BigDecimal current = postcodeTotalArrears.get(truncatedPostcode);
                        BigDecimal arrears = current.add(rentArrears_BigDecimal);
                        postcodeTotalArrears.put(truncatedPostcode, arrears);
                        aggregations++;
                    } else {
                        postcodeTotalArrears.put(truncatedPostcode, rentArrears_BigDecimal);
                    }
                    if (postcodeClaims.containsKey(truncatedPostcode)) {
                        int current = postcodeClaims.get(truncatedPostcode);
                        postcodeClaims.put(truncatedPostcode, current + 1);
                    } else {
                        postcodeClaims.put(truncatedPostcode, 1);
                    }
                    // Add to total for all months
                    if (postcodeTotalArrearsTotal.containsKey(truncatedPostcode)) {
                        BigDecimal current = postcodeTotalArrearsTotal.get(truncatedPostcode);
                        BigDecimal arrears = current.add(rentArrears_BigDecimal);
                        postcodeTotalArrearsTotal.put(truncatedPostcode, arrears);
                        aggregations++;
                    } else {
                        postcodeTotalArrearsTotal.put(truncatedPostcode, rentArrears_BigDecimal);
                    }
                    if (postcodeClaimsTotal.containsKey(truncatedPostcode)) {
                        int current = postcodeClaimsTotal.get(truncatedPostcode);
                        postcodeClaimsTotal.put(truncatedPostcode, current + 1);
                    } else {
                        postcodeClaimsTotal.put(truncatedPostcode, 1);
                    }
                    //System.out.println("" + underOccupiedReport_DataRecord + ", " + SRecordCount + ", " + postcode);
                }
            }
            // Report for each month
            pw.println("countNotMissingDRecords " + countNotMissingDRecords);
            pw.println("totalRentArrears " + totalRentArrears_BigDecimal.setScale(2, RoundingMode.HALF_UP));
            pw.println("Count of aggregations " + aggregations);
            pw.println("countMissingDRecords " + countMissingDRecords);
            pw.println("totalSRecordCount " + totalSRecordCount);
            pw.println("postcode, claims, arrears");
            ite2 = postcodeTotalArrears.keySet().iterator();
            while (ite2.hasNext()) {
                String postcode = ite2.next();
                int claims = postcodeClaims.get(postcode);
                BigDecimal arrears = postcodeTotalArrears.get(postcode);

                // Format postcode
                postcode = postcode.trim();
                String[] postcodeSplit = postcode.split(" ");
                if (postcodeSplit.length > 3) {
                    int debug = 1;
                    postcode = "mangled";
                } else {
                    if (postcodeSplit.length == 3) {
                        postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
                    } else {
                        if (postcodeSplit.length == 2) {
                            postcode = postcodeSplit[0] + " " + postcodeSplit[1];
                        }
                    }
                }
                // Write answer
                pw.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP));
            }
            pw.close();
        }
        // Report for all months aggregated
        Iterator<String> ite2 = postcodeTotalArrearsTotal.keySet().iterator();
        while (ite2.hasNext()) {
            String postcode = ite2.next();
            int claims = postcodeClaimsTotal.get(postcode);
            BigDecimal arrears = postcodeTotalArrearsTotal.get(postcode);

            // Format postcode
            postcode = postcode.trim();
            String[] postcodeSplit = postcode.split(" ");
            if (postcodeSplit.length > 3) {
                int debug = 1;
                postcode = "mangled";
            } else {
                if (postcodeSplit.length == 3) {
                    postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
                } else {
                    if (postcodeSplit.length == 2) {
                        postcode = postcodeSplit[0] + " " + postcodeSplit[1];
                    }
                }
            }
            // Write answer
            pwAggregate.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP));
        }
        pwAggregate.close();
    }

    /**
     * Method for initialising aSHBE_DataRecord_Handler
     *
     * @param args
     */
    private void init_handlers() {
        tDW_SHBE_Handler = new DW_SHBE_Handler();
        tDW_UnderOccupiedReport_Handler = new DW_UnderOccupiedReport_Handler();
    }

    /**
     * Method for reporting how many bedroom tax people have moved from one
     * month to the next.
     *
     * @param aUnderOccupiedReport_Set
     */
    public void processSHBEReportDataForSarah(
            ArrayList<Object[]> SHBE_Sets,
            ArrayList<DW_UnderOccupiedReport_Set>[] underOccupiedReport_Sets) {
        /*
         * 0 Apr 2013 14 Under Occupied Report For University Year Start Council Tenants.csv
         * 1 May 2013 14 Under Occupied Report For University Month 1 Council Tenants.csv
         * 2 Jun 2013 14 Under Occupied Report For University Month 2 Council Tenants.csv
         * 3 Jul 2013 14 Under Occupied Report For University Month 3 Council Tenants.csv
         * 4 Aug 2013 14 Under Occupied Report For University Month 4 Council Tenants.csv
         * 5 Sep 2013 14 Under Occupied Report For University Month 5 Council Tenants.csv
         * 6 Oct 2013 14 Under Occupied Report For University Month 6 Council Tenants.csv
         * 7 Nov 2013 14 Under Occupied Report For University Month 7 Council Tenants.csv
         * 8 Dec 2013 14 Under Occupied Report For University Month 8 Council Tenants.csv
         * 9 Jan 2013 14 Under Occupied Report For University Month 9 Council Tenants.csv
         * 10 Feb 2013 14 Under Occupied Report For University Month 10 Council Tenants.csv
         * 11 Mar 2013 14 Under Occupied Report For University Month 11 Council Tenants.csv
         * 12 Apr 2013 14 Under Occupied Report For University Month 12 Council Tenants.csv
         * 13 May 2014 15 Under Occupied Report For University Month 1 Council Tenants.csv
         * 14 Jun 2014 15 Under Occupied Report For University Month 2 Council Tenants.csv
         * 15 Jul 2014 15 Under Occupied Report For University Month 3 Council Tenants.csv
         */
        /*
         * 0 hb9803_SHBE_206728k April 2008.csv
         * 1 hb9803_SHBE_234696k October 2008.csv
         * 2 hb9803_SHBE_265149k April 2009.csv
         * 3 hb9803_SHBE_295723k October 2009.csv
         * 4 hb9803_SHBE_329509k April 2010.csv
         * 5 hb9803_SHBE_363186k October 2010.csv
         * 6 hb9803_SHBE_391746k March 2011.csv
         * 7 hb9803_SHBE_397524k April 2011.csv
         * 8 hb9803_SHBE_415181k July 2011.csv
         * 9 hb9803_SHBE_433970k October 2011.csv
         * 10 hb9803_SHBE_451836k January 2012.csv
         * 11 hb9803_SHBE_470742k April 2012.csv
         * 12 hb9803_SHBE_490903k July 2012.csv
         * 13 hb9803_SHBE_511038k October 2012.csv
         * 14 hb9803_SHBE_530243k January 2013.csv
         * 15 hb9803_SHBE_536123k February 2013.csv
         * 16 hb9991_SHBE_543169k March 2013.csv
         * 17 hb9991_SHBE_549416k April 2013.csv
         * 18 hb9991_SHBE_555086k May 2013.csv
         * 19 hb9991_SHBE_562036k June 2013.csv
         * 20 hb9991_SHBE_568694k July 2013.csv
         * 21 hb9991_SHBE_576432k August 2013.csv
         * 22 hb9991_SHBE_582832k September 2013.csv
         * 23 hb9991_SHBE_589664k Oct 2013.csv
         * 24 hb9991_SHBE_596500k Nov 2013.csv
         * 25 hb9991_SHBE_603335k Dec 2013.csv
         * 26 hb9991_SHBE_609791k Jan 2014.csv
         * 27 hb9991_SHBE_615103k Feb 2014.csv
         * 28 hb9991_SHBE_621666k Mar 2014.csv
         * 29 hb9991_SHBE_629066k Apr 2014.csv
         */
        /*
         * Correspondence between data
         * UnderoccupancyIndex, SHBEIndex
         * 0, 17
         * 1, 18
         * 2, 19
         * 3, 20
         * 4, 21
         * 5, 22
         * 6, 23
         * 7, 24
         * 8, 25
         * 9, 26
         * 10, 27
         * 11, 28
         * 12, 29
         */
        String[] dates;
        dates = new String[13];
        dates[0] = "2013-04";
        dates[1] = "2013-05";
        dates[2] = "2013-06";
        dates[3] = "2013-07";
        dates[4] = "2013-08";
        dates[5] = "2013-09";
        dates[6] = "2013-10";
        dates[7] = "2013-11";
        dates[8] = "2013-12";
        dates[9] = "2014-01";
        dates[10] = "2014-02";
        dates[11] = "2014-03";
        dates[12] = "2014-04";
        ArrayList<DW_UnderOccupiedReport_Set> councilRecords;
        councilRecords = underOccupiedReport_Sets[0];
        ArrayList<DW_UnderOccupiedReport_Set> RSLRecords;
        RSLRecords = underOccupiedReport_Sets[1];
        File dir = new File(
                DW_Files.getOutputDir(),
                "Sarah");
        File outputDir = new File(
                dir,
                "DigitalWelfareOutputReportForSarah");
        String level = "MSOA";
        TreeMap<String, String> lookupFromPostcodeToCensusCode;
        lookupFromPostcodeToCensusCode = getLookupFromPostcodeToCensusCode(
                level,
                2011);
        int UnderoccupancyIndex;
        int SHBEIndex;
        for (int i = 0; i < dates.length; i++) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    DW_Files.getOutputUnderOccupiedDir(),
                    "DigitalWelfareOutputUnderOccupiedReport" + dates[i] + ".txt");
            UnderoccupancyIndex = i;
            SHBEIndex = i + 17;
            DW_UnderOccupiedReport_Set councilSet;
            councilSet = councilRecords.get(UnderoccupancyIndex);
            DW_UnderOccupiedReport_Set RSLSet;
            RSLSet = RSLRecords.get(UnderoccupancyIndex);
            Object[] SHBESet;
            SHBESet = SHBE_Sets.get(SHBEIndex);
            PrintWriter outPW;
            outPW = init_OutputTextFilePrintWriter(
                    outputDir,
                    "DigitalWelfareOutputReportForSarah" + dates[i]);
            if (i < dates.length - 1) {
                // Report aggregates by postcode
                // Unit postcodes such as "LS2 9JT" are aggregated to "LS2 9"
                Object[] SHBESet2;
                SHBESet2 = SHBE_Sets.get(SHBEIndex + 1);
                // Council Records
                outPW.println("Council Records");
                DW_UnderOccupiedReport_Set councilSet2;
                councilSet2 = councilRecords.get(UnderoccupancyIndex + 1);
                reportBedroomTaxChanges(
                        dates[i],
                        dates[i + 1],
                        SHBESet,
                        SHBESet2,
                        councilSet,
                        councilSet2);
                // RSL Records
                outPW.println("RSL Records");
                DW_UnderOccupiedReport_Set RSLSet2;
                RSLSet2 = RSLRecords.get(UnderoccupancyIndex + 1);
                reportBedroomTaxChanges(
                        dates[i],
                        dates[i + 1],
                        SHBESet,
                        SHBESet2,
                        RSLSet,
                        RSLSet2);
                // reportUnderOccupiedIndebtedness
                reportUnderOccupiedIndebtedness(
                        dates[i],
                        dates[i + 1],
                        SHBESet,
                        SHBESet2,
                        councilSet,
                        councilSet2);
            }
            // CouncilRecordsAggregation
            String name;
            Iterator<String> ite;
            HashMap<String, TreeMap<String, Integer>> councilRecordAggregatedData;
            councilRecordAggregatedData = getUnderOccupiedbyMSOA(
                    dates[i],
                    councilSet,
                    SHBESet,
                    lookupFromPostcodeToCensusCode);
            name = "DigitalWelfareOutputReportForSarahCouncilRecordUnderOccupiedGeneralisation";
            ite = councilRecordAggregatedData.keySet().iterator();
            while (ite.hasNext()) {
                String month = ite.next();
                System.out.println("Writeout month " + month);
                TreeMap<String, Integer> aCouncilRecordgeneralisation = councilRecordAggregatedData.get(month);
                createCSV(
                        outputDir,
                        aCouncilRecordgeneralisation,
                        name + month,
                        "Postcode",
                        "Count");
            }
            // RSLRecordsAggregation
            HashMap<String, TreeMap<String, Integer>> RSLRecordAggregatedData;
            RSLRecordAggregatedData = getUnderOccupiedbyMSOA(
                    dates[i],
                    RSLSet,
                    SHBESet,
                    lookupFromPostcodeToCensusCode);
            name = "DigitalWelfareOutputReportForSarahRSLRecordUnderOccupiedGeneralisation";
            ite = RSLRecordAggregatedData.keySet().iterator();
            while (ite.hasNext()) {
                String month = ite.next();
                System.out.println("Writeout month " + month);
                TreeMap<String, Integer> aRSLRecordgeneralisation = RSLRecordAggregatedData.get(month);
                createCSV(
                        outputDir,
                        aRSLRecordgeneralisation,
                        name + month,
                        "Postcode",
                        "Count");
            }
            // Summarise SHBE data by postcode
            // Unit postcodes such as "LS2 9JT" are aggregated to "LS2 9"
            //       HashMap<String, TreeMap<String, Integer>> allMonthsSHBEgeneralisation = getCouncilTaxClaimCountByPostcode(tSHBEfilenames);
            HashMap<String, TreeMap<String, Integer>> allMonthsSHBEgeneralisation;
            allMonthsSHBEgeneralisation = getCouncilTaxClaimCountByMSOA(
                    dates[i],
                    RSLSet,
                    SHBESet,
                    //tSHBEfilenames,
                    lookupFromPostcodeToCensusCode);
            name = "DigitalWelfareOutputReportForSarahSHBEGeneralisation";
            ite = allMonthsSHBEgeneralisation.keySet().iterator();
            while (ite.hasNext()) {
                String month = ite.next();
                System.out.println("Writeout month " + month);
                TreeMap<String, Integer> aMonthsSHBEgeneralisation;
                aMonthsSHBEgeneralisation = allMonthsSHBEgeneralisation.get(month);
                createCSV(
                        outputDir,
                        aMonthsSHBEgeneralisation,
                        name + month,
                        "Postcode",
                        "Count");
            }
            String header;
            // header = "postcode,allClaimCount,CouncilUnderOccupiedCount,RSLUnderOccupiedCount,TotalUnderOccupiedCount,Proportion";
            header = "MSOA,allClaimCount,CouncilUnderOccupiedCount,RSLUnderOccupiedCount,TotalUnderOccupiedCount,PercentageClaimsUnderoccupied";

            ite = allMonthsSHBEgeneralisation.keySet().iterator();
            while (ite.hasNext()) {
                String month = ite.next();
                System.out.println("Writeout month " + month);
                name = "DigitalWelfareOutputReportForSarahGeneralisation" + month;

                TreeMap<String, Integer> aMonthsSHBEgeneralisation = allMonthsSHBEgeneralisation.get(month);
                TreeMap<String, Integer> aCouncilRecordgeneralisation = councilRecordAggregatedData.get(month);
                TreeMap<String, Integer> aRSLRecordgeneralisation = RSLRecordAggregatedData.get(month);

                createCSV(
                        outputDir,
                        aMonthsSHBEgeneralisation,
                        aCouncilRecordgeneralisation,
                        aRSLRecordgeneralisation, name, header);
            }

            String[] allSHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
            int startIndex = 0;
            int endIndex = allSHBEFilenames.length - 1;
            HashSet<String> AllNINOOfClaimantsStartYear = new HashSet<String>();
            HashSet<String> AllNINOOfClaimantsEndYear = new HashSet<String>();
            HashMap<String, String> AllNINOOfClaimantsThatMoved = new HashMap<String, String>();
            HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndDatesOfMoves;
            AllNationalInsuranceNumbersAndDatesOfMoves = new HashMap<String, HashSet<String>>();
            HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims;
            AllNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
            String startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
            String startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
            String endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
            String endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
            Object[] migrationData = getAllClaimantMigrationData(
                    allSHBEFilenames,
                    AllNINOOfClaimantsStartYear,
                    AllNINOOfClaimantsEndYear,
                    AllNINOOfClaimantsThatMoved,
                    AllNationalInsuranceNumbersAndDatesOfClaims,
                    AllNationalInsuranceNumbersAndDatesOfMoves,
                    startYear,
                    startMonth,
                    endYear,
                    endMonth,
                    startIndex,
                    endIndex);

            String type = "all";
            writeOutMigrationMatrix(
                    allSHBEFilenames,
                    migrationData,
                    startIndex,
                    endIndex,
                    type);
        }
    }

    /**
     *
     * @param underOccupiedReport_Sets
     */
    public void processSHBEReportDataIntoMigrationMatricesForApril(
            ArrayList<Object[]> SHBEData,
            ArrayList<DW_UnderOccupiedReport_Set>[] underOccupiedReport_Sets) {
        ArrayList<DW_UnderOccupiedReport_Set> councilRecords;
        ArrayList<DW_UnderOccupiedReport_Set> registeredSocialLandlordRecords;
        councilRecords = underOccupiedReport_Sets[0];
        registeredSocialLandlordRecords = underOccupiedReport_Sets[1];
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "CountOfClaimsByDates.txt");
        // 0, 2, 4, 7, 11, 17, 29, 41 are April data for 2008, 2009, 2010, 2011,  
        // 2012, 2013, 2014, 2015 respectively
        String[] allSHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
        int startIndex;
        int endIndex;
        HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims;
        AllNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
        HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndMoves;
        AllNationalInsuranceNumbersAndMoves = new HashMap<String, HashSet<String>>();
        HashMap<String, TreeSet<String>> HBNationalInsuranceNumbersAndDatesOfClaims;
        HBNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
        HashMap<String, HashSet<String>> HBNationalInsuranceNumbersAndMoves;
        HBNationalInsuranceNumbersAndMoves = new HashMap<String, HashSet<String>>();
        String startMonth;
        String endMonth;
        String startYear;
        String endYear;
        int countOfNewButPreviousClaimant;
        Object[] migrationData;

        // 2008 2009
        startIndex = 0;
        endIndex = 2;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        /**
         * migrationData[0] = HashSet<String>
         * migrationData[1] = HashSet<String>
         * migrationData[2] = HashMap<String, String>
         * migrationData[3] = HashSet<String>
         * migrationData[4] = HashSet<String>
         * migrationData[5] = HashMap<String, String>
         */
        HashSet<String> AllNINOOfClaimants2008 = (HashSet<String>) migrationData[0];
        HashSet<String> AllNINOOfClaimants2009 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20082009 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2008 = (HashSet<String>) migrationData[3];
        HashSet<String> HBNINOOfClaimants2009 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20082009 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2008,
                AllNINOOfClaimants2009,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2009 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2009 2010
        startIndex = 2;
        endIndex = 4;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2010 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20092010 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2010 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20092010 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2009,
                AllNINOOfClaimants2010,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2010 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2010 2011
        startIndex = 4;
        endIndex = 7;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2011 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20102011 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2011 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20102011 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2010,
                AllNINOOfClaimants2011,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2011 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2011 2012
        startIndex = 7;
        endIndex = 11;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2012 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20112012 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2012 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20112012 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2011,
                AllNINOOfClaimants2012,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2012 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2012 2013
        startIndex = 11;
        endIndex = 17;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2013 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2013 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2012,
                AllNINOOfClaimants2013,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2013 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2013 2014
        startIndex = 17;
        endIndex = 29;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2014 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20132014 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2014 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20132014 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2013,
                AllNINOOfClaimants2014,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2014 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        // 2014 2015
        startIndex = 29;
        endIndex = 41;
        startYear = DW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = DW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = DW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReport_Sets,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                allSHBEFilenames,
                startIndex,
                endIndex);
        HashSet<String> AllNINOOfClaimants2015 = (HashSet<String>) migrationData[1];
        HashMap<String, String> AllNINOOfClaimantsThatMoved20142015 = (HashMap<String, String>) migrationData[2];
        HashSet<String> HBNINOOfClaimants2015 = (HashSet<String>) migrationData[4];
        HashMap<String, String> HBNINOOfClaimantsThatMoved20142015 = (HashMap<String, String>) migrationData[5];
        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
                AllNINOOfClaimants2014,
                AllNINOOfClaimants2015,
                AllNationalInsuranceNumbersAndDatesOfClaims);
        pw.println("2015 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);

        TreeMap<String, Integer> countOfClaimsByDate = getCountOfClaimsByDates(
                AllNationalInsuranceNumbersAndDatesOfClaims);
        // writeout countOfClaimsByDate
        pw.println("CountOfClaimsByDates");
        pw.println("Dates,CountOfClaims");
        Iterator<String> ite = countOfClaimsByDate.keySet().iterator();
        while (ite.hasNext()) {
            String dates = ite.next();
            Integer count = countOfClaimsByDate.get(dates);
            pw.println(dates + " " + count);
        }
        pw.close();
    }

    public int getCountOfNewButPreviousClaimant(
            HashSet<String> NINOOfClaimantsInStartYear,
            HashSet<String> NINOOfClaimantsInEndYear,
            HashMap<String, TreeSet<String>> NationalInsuranceNumbersAndDatesOfClaims) {
        int result = 0;
        Iterator<String> ite = NINOOfClaimantsInEndYear.iterator();
        while (ite.hasNext()) {
            String NINO = ite.next();
            if (!NINOOfClaimantsInStartYear.contains(NINO)) {
                if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                    result++;
                }
//                TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
//                Iterator<String> ite2 = DatesOfClaims.iterator();
//                while (ite2.hasNext()) {
//                    String dateOfClaim = ite2.next();
//                    if (dateOfClaim.contains(year)) {
//                        result++;
//                    }
//                }
            }
        }
        return result;
    }

    /**
     * 2008,2009,2010,2011,2012,2013
     *
     * @param NationalInsuranceNumbersAndDatesOfClaims
     * @return
     */
    public TreeMap<String, Integer> getCountOfClaimsByDates(
            HashMap<String, TreeSet<String>> NationalInsuranceNumbersAndDatesOfClaims) {
        TreeMap<String, Integer> result = new TreeMap<String, Integer>();
        result.put("April2008", 0);
        result.put("April2008,April2009", 0);
        result.put("April2008,April2009,April2010", 0);
        result.put("April2008,April2009,April2010,April2011", 0);
        result.put("April2008,April2009,April2010,April2012", 0);
        result.put("April2008,April2009,April2010,April2013", 0);
        result.put("April2008,April2009,April2010,April2011,April2012", 0);
        result.put("April2008,April2009,April2010,April2012,April2013", 0);
        result.put("April2008,April2009,April2010,April2011,April2012,April2013", 0);
        result.put("April2008,April2009,April2011", 0);
        result.put("April2008,April2009,April2012", 0);
        result.put("April2008,April2009,April2012,April2013", 0);
        result.put("April2008,April2009,April2013", 0);
        result.put("April2008,April2010", 0);
        result.put("April2008,April2010,April2011", 0);
        result.put("April2008,April2010,April2011,April2012", 0);
        result.put("April2008,April2010,April2011,April2013", 0);
        result.put("April2008,April2010,April2012", 0);
        result.put("April2008,April2010,April2012,April2013", 0);
        result.put("April2008,April2010,April2013", 0);
        result.put("April2008,April2011", 0);
        result.put("April2008,April2011,April2012", 0);
        result.put("April2008,April2011,April2013", 0);
        result.put("April2008,April2012", 0);
        result.put("April2008,April2012,April2013", 0);
        result.put("April2008,April2013", 0);
        result.put("April2009", 0);
        result.put("April2009,April2010", 0);
        result.put("April2009,April2010,April2011", 0);
        result.put("April2009,April2010,April2011,April2012", 0);
        result.put("April2009,April2010,April2011,April2012,April2013", 0);
        result.put("April2009,April2010,April2012", 0);
        result.put("April2009,April2010,April2012,April2013", 0);
        result.put("April2009,April2010,April2013", 0);
        result.put("April2009,April2011", 0);
        result.put("April2009,April2011,April2012", 0);
        result.put("April2009,April2011,April2012,April2013", 0);
        result.put("April2009,April2012", 0);
        result.put("April2009,April2012,April2013", 0);
        result.put("April2009,April2013", 0);
        result.put("April2010", 0);
        result.put("April2010,April2011", 0);
        result.put("April2010,April2011,April2012", 0);
        result.put("April2010,April2011,April2012,April2013", 0);
        result.put("April2010,April2012", 0);
        result.put("April2010,April2012,April2013", 0);
        result.put("April2010,April2013", 0);
        result.put("April2011", 0);
        result.put("April2011,April2012", 0);
        result.put("April2011,April2012,April2013", 0);
        result.put("April2012", 0);
        result.put("April2012,April2013", 0);
        result.put("April2013", 0);
        Iterator<String> ite = NationalInsuranceNumbersAndDatesOfClaims.keySet().iterator();
        while (ite.hasNext()) {
            String NINO = ite.next();
            TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
            Iterator<String> ite2 = DatesOfClaims.iterator();
            String type = "";
            while (ite2.hasNext()) {
                String dateOfClaim = ite2.next();
                if (type.isEmpty()) {
                    type = dateOfClaim;
                } else {
                    type += "," + dateOfClaim;
                }
            }
            Integer pre = result.get(type);
            if (pre == null) {
                result.put(type, 1);
            } else {
                result.put(type, pre + 1);
            }
        }
        return result;
    }

    /**
     *
     * @param aUnderOccupiedReport_Set
     * @param AllNationalInsuranceNumbersAndDatesOfClaims
     * @param AllNationalInsuranceNumbersAndMoves
     * @param HBNationalInsuranceNumbersAndDatesOfClaims
     * @param HBNationalInsuranceNumbersAndMoves
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @param allSHBEFilenames
     * @param startIndex
     * @param endIndex
     * @return
     */
    public Object[] processSHBEReportDataIntoMigrationMatricesForApril(
            ArrayList<DW_UnderOccupiedReport_Set>[] aUnderOccupiedReport_Set,
            HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndMoves,
            HashMap<String, TreeSet<String>> HBNationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> HBNationalInsuranceNumbersAndMoves,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            String[] allSHBEFilenames,
            int startIndex,
            int endIndex) {
        Object[] result;
        result = new Object[6];
        HashSet<String> AllNINOOfClaimantsStartYear = new HashSet<String>();
        HashSet<String> AllNINOOfClaimantsEndYear = new HashSet<String>();
        HashMap<String, String> AllNINOOfClaimantsThatMoved = new HashMap<String, String>();
        HashSet<String> HBNINOOfClaimantsStartYear = new HashSet<String>();
        HashSet<String> HBNINOOfClaimantsEndYear = new HashSet<String>();
        HashMap<String, String> HBNINOOfClaimantsThatMoved = new HashMap<String, String>();
        result[0] = AllNINOOfClaimantsStartYear;
        result[1] = AllNINOOfClaimantsEndYear;
        result[2] = AllNINOOfClaimantsThatMoved;
        result[3] = HBNINOOfClaimantsStartYear;
        result[4] = HBNINOOfClaimantsEndYear;
        result[5] = HBNINOOfClaimantsThatMoved;

        String type;
        Object[] migrationData;
        // Allmigration
        type = "all";
        migrationData = getAllClaimantMigrationData(
                allSHBEFilenames,
                AllNINOOfClaimantsStartYear,
                AllNINOOfClaimantsEndYear,
                AllNINOOfClaimantsThatMoved,
                AllNationalInsuranceNumbersAndDatesOfClaims,
                AllNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                startIndex,
                endIndex);
        writeOutMigrationMatrix(
                allSHBEFilenames,
                migrationData,
                startIndex,
                endIndex,
                type);
        // Housing Benefit only migration
        type = "HB";
        migrationData = getHBClaimantOnlyMigrationData(
                allSHBEFilenames,
                HBNINOOfClaimantsStartYear,
                HBNINOOfClaimantsEndYear,
                HBNINOOfClaimantsThatMoved,
                HBNationalInsuranceNumbersAndDatesOfClaims,
                HBNationalInsuranceNumbersAndMoves,
                startYear,
                startMonth,
                endYear,
                endMonth,
                startIndex,
                endIndex);
        writeOutMigrationMatrix(
                allSHBEFilenames,
                migrationData,
                startIndex,
                endIndex,
                type);
        return result;
    }

    public void writeOutTenancyTypeTransitionMatrix(
            String[] allSHBEFilenames,
            Object[] migrationData,
            int startIndex,
            int endIndex,
            String type) {
        initOddPostcodes();
        String startName = allSHBEFilenames[startIndex];
        String startMonth = DW_SHBE_Handler.getMonth(startName);
        String startYear = DW_SHBE_Handler.getYear(startName);
        String endName = allSHBEFilenames[endIndex];
        String endMonth = DW_SHBE_Handler.getMonth(endName);
        String endYear = DW_SHBE_Handler.getYear(endName);

        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "TenancyTypeTransition_" + type + "__Start_" + startMonth + "" + startYear + "_End_" + endMonth + "" + endYear + ".csv");

        TreeMap<Integer, TreeMap<Integer, Integer>> migrationMatrix = (TreeMap<Integer, TreeMap<Integer, Integer>>) migrationData[0];
        TreeSet<Integer> originsAndDestinations = (TreeSet<Integer>) migrationData[1];
        Iterator<Integer> ite = originsAndDestinations.iterator();
        String header = "TenancyTypeTransitionMatrix";
        while (ite.hasNext()) {
            Integer t = ite.next();
            header += "," + t;
        }
        pw.println(header);
        ite = originsAndDestinations.iterator();
        while (ite.hasNext()) {
            Integer startTenancyType = ite.next();
            TreeMap<Integer, Integer> destinations = migrationMatrix.get(startTenancyType);
            if (destinations != null) {
                String line = "";
                Iterator<Integer> ite2 = originsAndDestinations.iterator();
                while (ite2.hasNext()) {
                    Integer endTenancyType = ite2.next();
                    Integer count = destinations.get(endTenancyType);
                    if (count == null) {
                        count = 0;
                    }
                    if (line.isEmpty()) {
                        line += startTenancyType;
                    }
                    line += "," + count.toString();
                }
                pw.println(line);
            }
        }
        pw.close();
    }

    /**
     *
     * @param allSHBEFilenames
     * @param migrationData
     * @param startIndex
     * @param endIndex
     * @param type
     */
    public void writeOutMigrationMatrix(
            String[] allSHBEFilenames,
            Object[] migrationData,
            int startIndex,
            int endIndex,
            String type) {
        initOddPostcodes();
        String startName = allSHBEFilenames[startIndex];
        String startMonth = DW_SHBE_Handler.getMonth(startName);
        String startYear = DW_SHBE_Handler.getYear(startName);
        String endName = allSHBEFilenames[endIndex];
        String endMonth = DW_SHBE_Handler.getMonth(endName);
        String endYear = DW_SHBE_Handler.getYear(endName);

        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "migration_" + type + "__Start_" + startMonth + "" + startYear + "_End_" + endMonth + "" + endYear + ".csv");
        TreeMap<String, TreeMap<String, Integer>> migrationMatrix = (TreeMap<String, TreeMap<String, Integer>>) migrationData[0];
        //TreeSet<String> originsAndDestinations = (TreeSet<String>) migrationData[1];

        Iterator<String> ite = getExpectedPostcodes().iterator();
        String header = "PostcodeDistrictMigrationMatrix";
        while (ite.hasNext()) {
            String district = ite.next();
            if (!district.equalsIgnoreCase("unknown_butPreviousClaimant")) {
                header += "," + district;
            }
        }
        pw.println(header);
        ite = getExpectedPostcodes().iterator();
        while (ite.hasNext()) {
            String district = ite.next();
            TreeMap<String, Integer> destinations = migrationMatrix.get(district);
            if (destinations != null) {
                String line = "";
                Iterator<String> ite2 = getExpectedPostcodes().iterator();
                while (ite2.hasNext()) {
                    String destination = ite2.next();
                    if (!destination.equalsIgnoreCase("unknown_butPreviousClaimant")) {
                        Integer count = destinations.get(destination);
                        if (count == null) {
                            count = 0;
                        }
                        if (line.isEmpty()) {
                            line += district;
                            if (line.equalsIgnoreCase("unknown")) {
                                line += "_butNotPreviousClaimant";
                            }
                        }
                        line += "," + count.toString();
                    }
                }
                pw.println(line);
            }
        }
        pw.close();
    }

//     public void writeOutMigrationMatrix(
//            String[] allSHBEFilenames,
//            Object[] migrationData,
//            int startIndex,
//            int endIndex,
//            String type) {
//        initOddPostcodes();
//        String startName = allSHBEFilenames[startIndex];
//        String startMonth = DW_SHBE_Handler.getMonth(startName);
//        String startYear = DW_SHBE_Handler.getYear(startName);
//        String endName = allSHBEFilenames[endIndex];
//        String endMonth = DW_SHBE_Handler.getMonth(endName);
//        String endYear = DW_SHBE_Handler.getYear(endName);
//
//        PrintWriter printWriter = init_OutputTextFilePrintWriter("migration_" + type + "__Start_" + startMonth + "" + startYear + "_End_" + endMonth + "" + endYear + ".csv");
//        TreeMap<String, TreeMap<String, Integer>> migrationMatrix = (TreeMap<String, TreeMap<String, Integer>>) migrationData[0];
//        TreeSet<String> originsAndDestinations = (TreeSet<String>) migrationData[1];
//        Iterator<String> ite = originsAndDestinations.iterator();
//        String header = "PostcodeDistrictMigrationMatrix";
//        while (ite.hasNext()) {
//            String district = ite.next();
//            header += "," + district;
//        }
//        printWriter.println(header);
//        ite = originsAndDestinations.iterator();
//        while (ite.hasNext()) {
//            String district = ite.next();
//            TreeMap<String, Integer> destinations = migrationMatrix.get(district);
//            if (destinations != null) {
//                String line = "";
//                Iterator<String> ite2 = originsAndDestinations.iterator();
//                while (ite2.hasNext()) {
//                    String destination = ite2.next();
//                    Integer count = destinations.get(destination);
//                    if (count == null) {
//                        count = 0;
//                    }
//                    if (line.isEmpty()) {
//                        line += district;
//                    }
//                    line += "," + count.toString();
//                }
//                printWriter.println(line);
//            }
//        }
//        printWriter.close();
//    }       
    /**
     * Returns a migration matrix for all claimants.
     *
     * @param tSHBEfilenames
     * @param NINOOfClaimantsStartYear
     * @param NINOOfClaimantsEndYear
     * @param NINOOfClaimantsThatMoved
     * @param NationalInsuranceNumbersAndDatesOfClaims
     * @param NationalInsuranceNumbersAndMoves
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param startIndex
     * @param endMonth
     * @param endIndex
     * @return Object[] result where: ------------------------------------------
     * result[0] is a TreeMap<String, TreeMap<String, Integer>> Migration
     * Matrix;------------------------------------------------------------------
     * result[1] is a TreeSet<String> of origins/destinations;------------------
     */
    public Object[] getAllClaimantMigrationData(
            String[] tSHBEfilenames,
            HashSet<String> NINOOfClaimantsStartYear,
            HashSet<String> NINOOfClaimantsEndYear,
            HashMap<String, String> NINOOfClaimantsThatMoved,
            HashMap<String, TreeSet<String>> NationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> NationalInsuranceNumbersAndMoves,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            int startIndex,
            int endIndex) {
        Object[] result = new Object[2];
        TreeMap<String, TreeMap<String, Integer>> resultMatrix;
        resultMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = resultMatrix;
        TreeSet<String> originsAndDestinations;
        originsAndDestinations = new TreeSet<String>();
        result[1] = originsAndDestinations;
        Object[] SHBEDataStart = loadSHBEData(tSHBEfilenames[startIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        int stayPutCount = 0;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
            if (DRecordStart != null) {
                String postcodeStart = DRecordStart.getClaimantsPostcode();
                postcodeStart = DW_Postcode_Handler.formatPostcode(postcodeStart);
                String startPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(postcodeStart);
                startPostcodeDistrict = formatPostcodeDistrict(startPostcodeDistrict);
                //boolean claimantAlreadyHasBeenClaimant = false;
                // Check if claimant has already been a claimant and if so set claimantAlreadyHasBeenClaimant
                String NINO = DRecordStart.getClaimantsNationalInsuranceNumber();
                NINOOfClaimantsStartYear.add(NINO);
                if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                    TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                    DatesOfClaims.add(startMonth + startYear);
                } else {
                    TreeSet<String> DatesOfClaims = new TreeSet<String>();
                    DatesOfClaims.add(startMonth + startYear);
                    NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                }
                //if (!startPostcodeDistrict.isEmpty()) {
                if (resultMatrix.containsKey(startPostcodeDistrict)) {
                    destinationCounts = resultMatrix.get(startPostcodeDistrict);
                } else {
                    destinationCounts = new TreeMap<String, Integer>();
                    resultMatrix.put(startPostcodeDistrict, destinationCounts);
                    //originsAndDestinations.add(startPostcodeDistrict);
                }

                DW_SHBE_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
                String destinationPostcodeDistrict;
                String destinationPostcode = null;
                if (DRecordEnd == null) {
                    destinationPostcodeDistrict = "unknown";
                } else {
                    destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                    destinationPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(destinationPostcode);
                    NINOOfClaimantsEndYear.add(NINO);
                    if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                        TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                        DatesOfClaims.add(endMonth + endYear);
                    } else {
                        TreeSet<String> DatesOfClaims = new TreeSet<String>();
                        DatesOfClaims.add(endMonth + endYear);
                        NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                    }
                }
                // Filter out any non moves assumed to be when the postcode has not changed
                if (!postcodeStart.equalsIgnoreCase(destinationPostcode)) {
                    destinationPostcodeDistrict = formatPostcodeDistrict(
                            destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {
                    if (destinationCounts.containsKey(destinationPostcodeDistrict)) {
                        int current = destinationCounts.get(destinationPostcodeDistrict);
                        destinationCounts.put(destinationPostcodeDistrict, current + 1);
                    } else {
                        destinationCounts.put(destinationPostcodeDistrict, 1);
                        originsAndDestinations.add(destinationPostcodeDistrict);
                    }
                    String move = startMonth + startYear + ", OriginPostcode " + postcodeStart + ", DestinationPostcode " + destinationPostcode;

                    // Maybe want to split here between known moves and unknown moves
                    // i.e. when the destination postcode is not null and when it is null.
                    NINOOfClaimantsThatMoved.put(NINO, move);
                    if (NationalInsuranceNumbersAndMoves.containsKey(NINO)) {
                        HashSet<String> DatesOfMoves = NationalInsuranceNumbersAndMoves.get(NINO);
                        DatesOfMoves.add(move);
                    } else {
                        HashSet<String> DatesOfMoves = new HashSet<String>();
                        DatesOfMoves.add(move);
                        NationalInsuranceNumbersAndMoves.put(NINO, DatesOfMoves);
                    }
                } else {
                    stayPutCount++;
                }
                //}
                //}
            }
            //System.out.println("stayPutCount " + stayPutCount);
        }
        System.out.println("stayPutCount " + stayPutCount);
        // Add to matrix from unknown origins
        ite = DRecordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
                if (DRecordStart == null) {
                    String startPostcodeDistrict = "unknown";
                    //boolean claimantAlreadyHasBeenClaimant = false;
                    // Check if claimant has already been a claimant and if so set claimantAlreadyHasBeenClaimant
                    String NINO = DRecordEnd.getClaimantsNationalInsuranceNumber();
                    NINOOfClaimantsEndYear.add(NINO);
                    if (NationalInsuranceNumbersAndDatesOfClaims.containsKey(NINO)) {
                        TreeSet<String> DatesOfClaims = NationalInsuranceNumbersAndDatesOfClaims.get(NINO);
                        DatesOfClaims.add(startMonth + startYear);
                        startPostcodeDistrict += "_butPreviousClaimant";
                        //claimantAlreadyHasBeenClaimant = true;
                    } else {
                        TreeSet<String> DatesOfClaims = new TreeSet<String>();
                        DatesOfClaims.add(startMonth + startYear);
                        NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                    }
                    String destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                    String destinationPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(destinationPostcode);
                    destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {

                    if (resultMatrix.containsKey(startPostcodeDistrict)) {
                        destinationCounts = resultMatrix.get(startPostcodeDistrict);
                    } else {
                        destinationCounts = new TreeMap<String, Integer>();
                        resultMatrix.put(startPostcodeDistrict, destinationCounts);
                        //originsAndDestinations.add(startPostcodeDistrict);
                    }
                    if (destinationCounts.containsKey(destinationPostcodeDistrict)) {
                        int current = destinationCounts.get(destinationPostcodeDistrict);
                        destinationCounts.put(destinationPostcodeDistrict, current + 1);
                    } else {
                        destinationCounts.put(destinationPostcodeDistrict, 1);
                        originsAndDestinations.add(destinationPostcodeDistrict);
                    }
                    String move = startMonth + startYear + ", OriginPostcode " + startPostcodeDistrict + ", DestinationPostcode " + destinationPostcode;
                    NINOOfClaimantsThatMoved.put(NINO, move);
                    if (NationalInsuranceNumbersAndMoves.containsKey(NINO)) {
                        HashSet<String> DatesOfMoves = NationalInsuranceNumbersAndMoves.get(NINO);
                        DatesOfMoves.add(move);
                    } else {
                        HashSet<String> DatesOfMoves = new HashSet<String>();
                        DatesOfMoves.add(move);
                        NationalInsuranceNumbersAndMoves.put(NINO, DatesOfMoves);
                    }
                    //}
//                } else {
//                    System.out.println("Person a claimant 1 year ago, so we know about them");
                }
            } else {
                int debug = 1;
            }
        }
        return result;
    }

    /**
     * Returns a migration matrix for all claimants.
     *
     * @param tSHBEfilenames
     * @param AllNINOOfClaimantsStartYear
     * @param AllNINOOfClaimantsEndYear
     * @param AllNINOOfClaimantsThatMoved
     * @param NationalInsuranceNumbersAndDatesOfClaims
     * @param NationalInsuranceNumbersAndMoves
     * @param startYear
     * @param startMonth
     * @param endMonth
     * @param endYear
     * @param startIndex
     * @param endIndex
     * @return Object[] result where: ------------------------------------------
     * result[0] is a TreeMap<String, TreeMap<String, Integer>> Migration
     * Matrix;------------------------------------------------------------------
     * result[1] is a TreeSet<String> of origins/destinations;------------------
     */
    public Object[] getHBClaimantOnlyMigrationData(
            String[] tSHBEfilenames,
            HashSet<String> AllNINOOfClaimantsStartYear,
            HashSet<String> AllNINOOfClaimantsEndYear,
            HashMap<String, String> AllNINOOfClaimantsThatMoved,
            HashMap<String, TreeSet<String>> NationalInsuranceNumbersAndDatesOfClaims,
            HashMap<String, HashSet<String>> NationalInsuranceNumbersAndMoves,
            String startYear,
            String startMonth,
            String endYear,
            String endMonth,
            int startIndex,
            int endIndex) {
        Object[] result = new Object[2];
        TreeMap<String, TreeMap<String, Integer>> resultMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = resultMatrix;
        TreeSet<String> originsAndDestinations = new TreeSet<String>();
        result[1] = originsAndDestinations;
        Object[] SHBEDataStart = loadSHBEData(tSHBEfilenames[startIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
            if (DRecordStart != null) {
                // Filter for only Housing Benefit Claimants
                if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                    String postcodeStart = DRecordStart.getClaimantsPostcode();
                    postcodeStart = DW_Postcode_Handler.formatPostcode(postcodeStart);
                    String startPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(postcodeStart);
                    startPostcodeDistrict = formatPostcodeDistrict(startPostcodeDistrict);
                    //if (!startPostcodeDistrict.isEmpty()) {
                    if (resultMatrix.containsKey(startPostcodeDistrict)) {
                        destinationCounts = resultMatrix.get(startPostcodeDistrict);
                    } else {
                        destinationCounts = new TreeMap<String, Integer>();
                        resultMatrix.put(startPostcodeDistrict, destinationCounts);
                        //originsAndDestinations.add(startPostcodeDistrict);
                    }
                    DW_SHBE_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
                    // Filter for only Housing Benefit Claimants
                    if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                        String destinationPostcodeDistrict;
                        String destinationPostcode = null;
                        if (DRecordEnd == null) {
                            destinationPostcodeDistrict = "unknown";
                        } else {
                            destinationPostcode = DRecordEnd.getClaimantsPostcode();
                            destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                            destinationPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(destinationPostcode);
                        }
                        // Filter out any non moves assumed to be when the postcode has not changed
                        if (!postcodeStart.equalsIgnoreCase(destinationPostcode)) {
                            destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                            //if (!destinationPostcodeDistrict.isEmpty()) {

                            if (destinationCounts.containsKey(destinationPostcodeDistrict)) {
                                int current = destinationCounts.get(destinationPostcodeDistrict);
                                destinationCounts.put(destinationPostcodeDistrict, current + 1);
                            } else {
                                destinationCounts.put(destinationPostcodeDistrict, 1);
                                originsAndDestinations.add(destinationPostcodeDistrict);
                            }
                        }
                    }
                    //}
                    //}
                }
            }
        }
        // Add to matrix from unknown origins
        ite = DRecordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
                if (DRecordStart == null) {
                    String startPostcodeDistrict = "unknown";
                    String destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = DW_Postcode_Handler.formatPostcode(destinationPostcode);
                    String destinationPostcodeDistrict = DW_Postcode_Handler.getPostcodeDistrict(destinationPostcode);
                    destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {

                    if (resultMatrix.containsKey(startPostcodeDistrict)) {
                        destinationCounts = resultMatrix.get(startPostcodeDistrict);
                    } else {
                        destinationCounts = new TreeMap<String, Integer>();
                        resultMatrix.put(startPostcodeDistrict, destinationCounts);
                        //originsAndDestinations.add(startPostcodeDistrict);
                    }
                    if (destinationCounts.containsKey(destinationPostcodeDistrict)) {
                        int current = destinationCounts.get(destinationPostcodeDistrict);
                        destinationCounts.put(destinationPostcodeDistrict, current + 1);
                    } else {
                        destinationCounts.put(destinationPostcodeDistrict, 1);
                        originsAndDestinations.add(destinationPostcodeDistrict);
                    }
                    //}
                }
            }
        }
        return result;
    }

    /**
     * A method for looking at changes between consecutive months in the Under-
     * Occupancy Report data.
     *
     * @param underOccupancyRecords
     * @param tSHBEfilenames
     */
    public void reportBedroomTaxChanges(
            String date1,
            String date2,
            Object[] SHBESet,
            Object[] SHBESet2,
            DW_UnderOccupiedReport_Set underOccupiedReportSet,
            DW_UnderOccupiedReport_Set underOccupiedReportSet2) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "BedroomTaxChanges" + date1 + "-" + date2 + ".txt");

//        TreeMap<String, DW_SHBE_Record> DRecordsMonth1;
//        DRecordsMonth1 = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
        DW_SHBE_CollectionHandler DRecordsMonth1;
        DRecordsMonth1 = (DW_SHBE_CollectionHandler) SHBESet[0];

        // Load next SHBE data
//        TreeMap<String, DW_SHBE_Record> DRecordsMonth2;
//        DRecordsMonth2 = (TreeMap<String, DW_SHBE_Record>) SHBESet2[0];
        DW_SHBE_CollectionHandler DRecordsMonth2;
        DRecordsMonth2 = (DW_SHBE_CollectionHandler) SHBESet2[0];

        // Get the set of those underOccupancy ids from month 1 not in month 2
        int countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds = 0;
        int countOfRemainingClaimantsThatAreNoLongerUnderOccupiers = 0;
        int countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds = 0;
        Set<String> potentiallyMoved = underOccupiedReportSet.getSet().keySet();
        potentiallyMoved.removeAll(underOccupiedReportSet2.getSet().keySet());
        String councilTaxClaimNumber;
        Iterator<String> ite;
        ite = potentiallyMoved.iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordMonth2;
//            DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
            DRecordMonth2 = DRecordsMonth2.getDRecord(councilTaxClaimNumber);
            if (DRecordMonth2 == null) {
                countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds++;
            } else {
                DW_SHBE_Record DRecordMonth1;
//                DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
                DRecordMonth1 = DRecordsMonth1.getDRecord(councilTaxClaimNumber);
                String postcode1 = DRecordMonth1.getClaimantsPostcode();
                String postcode2 = DRecordMonth2.getClaimantsPostcode();
                if (!postcode1.equalsIgnoreCase(postcode2)) {
                    countOfRemainingClaimantsThatAreNoLongerUnderOccupiers++;
                } else {
                    countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds++;
                }
            }
        }
        // Iterate over councilRecords for month 2 and count how many of these were in month 1
        int countOfRemainingClaimantsThatAreUnderOccupiers = 0;
        int countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved = 0;
        int countOfNewClaimantsThatAreUnderOccupiers = 0;
        int countOfNoLongerUnderOccupancyClaimants = 0;
        Set<String> s = underOccupiedReportSet.getSet().keySet();
        ite = underOccupiedReportSet2.getSet().keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecordMonth1;
//                DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
            DRecordMonth1 = DRecordsMonth1.getDRecord(councilTaxClaimNumber);
            DW_SHBE_Record DRecordMonth2;
//            DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
            DRecordMonth2 = DRecordsMonth2.getDRecord(councilTaxClaimNumber);
            if (s.contains(councilTaxClaimNumber)) {
                countOfRemainingClaimantsThatAreUnderOccupiers++;
                String postcode1 = "";
                String postcode2 = "";
                if (DRecordMonth1 == null) {
                    System.out.println("" + DRecordMonth1);
                } else {
                    postcode1 = DRecordMonth1.getClaimantsPostcode();
                }
                if (DRecordMonth2 == null) {
//                    System.out.println("" + DRecordMonth2);
                } else {
                    postcode2 = DRecordMonth2.getClaimantsPostcode();
                }
                if (!postcode1.equalsIgnoreCase(postcode2)) {
                    countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved++;
                }
            } else {
                countOfNewClaimantsThatAreUnderOccupiers++;
            }
        }
        s.removeAll(underOccupiedReportSet.getSet().keySet());
        countOfNoLongerUnderOccupancyClaimants = s.size();
        // Report
        pw.println("Bedroom Tax " + date2);
        pw.println("countOfNewClaimantsThatAreUnderOccupiers " + countOfNewClaimantsThatAreUnderOccupiers);
        pw.println("countOfRemainingClaimantsThatAreUnderOccupiers " + countOfRemainingClaimantsThatAreUnderOccupiers);
        pw.println("countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved " + countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved);
        pw.println("countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds " + countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds);
        pw.println("countOfNoLongerUnderOccupancyClaimants " + countOfNoLongerUnderOccupancyClaimants);
        pw.println("countOfRemainingClaimantsThatAreNoLongerUnderOccupiers " + countOfRemainingClaimantsThatAreNoLongerUnderOccupiers);
        pw.println("countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds " + countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds);
        pw.close();
    }

    /**
     * Loads SHBE Data from filename.
     *
     * @param filename
     * @return Object[7] result where:------------------------------------------
     * result[0] is a TreeMap<String,DW_SHBE_Record> representing records with
     * DRecords, --------------------------------------------------------------
     * result[1] is a TreeMap<String, DW_SHBE_Record> representing records
     * without DRecords, ------------------------------------------------------
     * result[2] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,
     * result[3] is a HashSet<String> of PartnerNationalInsuranceNumberIDs,
     * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs
     * result[5] is a HashSet<String> of NonDependentsNationalInsuranceNumberIDs
     * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs
     */
    public Object[] loadSHBEData(String filename) {
        Object[] result = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
//        TreeMap<String, DW_SHBE_Record> tDRecords;
//        tDRecords = (TreeMap<String, DW_SHBE_Record>) result[0];
//        System.out.println("" + tDRecords.size() + " records with DRecords loaded from " + filename);
//        TreeMap<String, DW_SHBE_Record> tSRecords;
//        tSRecords = (TreeMap<String, DW_SHBE_Record>) result[1];
//        System.out.println("" + tSRecords.size() + " records without DRecords loaded from " + filename);
//        HashSet<String> tClaimantNationalInsuranceNumberIDs;
//        tClaimantNationalInsuranceNumberIDs = (HashSet<String>) result[2];
//        System.out.println("Unique Claimant National Insurance Numbers count " + tClaimantNationalInsuranceNumberIDs.size());
//        HashSet<String> tPartnerNationalInsuranceNumberIDs;
//        tPartnerNationalInsuranceNumberIDs = (HashSet<String>) result[3];
//        System.out.println("Unique Partner National Insurance Numbers count " + tPartnerNationalInsuranceNumberIDs.size());
//        HashSet<String> tDependentNationalInsuranceNumberIDs;
//        tDependentNationalInsuranceNumberIDs = (HashSet<String>) result[4];
//        System.out.println("Unique Dependent National Insurance Numbers count " + tDependentNationalInsuranceNumberIDs.size());
//        HashSet<String> tNonDependentNationalInsuranceNumberIDs;
//        tNonDependentNationalInsuranceNumberIDs = (HashSet<String>) result[5];
//        System.out.println("Unique NonDependent National Insurance Numbers count " + tNonDependentNationalInsuranceNumberIDs.size());
//        HashSet<String> tAllHouseholdNationalInsuranceNumberIDs;
//        tAllHouseholdNationalInsuranceNumberIDs = (HashSet<String>) result[6];
//        System.out.println("Unique All Households National Insurance Numbers count " + tAllHouseholdNationalInsuranceNumberIDs.size());
        return result;
    }

    /**
     * Writes to files aggregated details about rent arrears totals and by
     * aggregated postcode.
     *
     * @param underOccupiedReportData
     * @param tSHBEfilenames
     */
    public void reportUnderOccupiedIndebtedness(
            String date1,
            String date2,
            Object[] SHBESet,
            Object[] SHBESet2,
            DW_UnderOccupiedReport_Set underOccupiedReportSet,
            DW_UnderOccupiedReport_Set underOccupiedReportSet2) {
//            DW_UnderOccupiedReport_Set aUnderOccupiedReport_Set,
//            String[] tSHBEfilenames) {
        TreeMap<String, DW_UnderOccupiedReport_Record> recs;
        recs = underOccupiedReportSet.getSet();
//        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
//        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
//            int underOccupancyMonth = month;
//            //int underOccupancyMonth = month + 2;
//            String monthString = DW_SHBE_Handler.getMonth(tSHBEfilenames[month + 1]);
        DW_SHBE_CollectionHandler DRecords;
        DRecords = (DW_SHBE_CollectionHandler) SHBESet[0];
//        TreeMap<String, DW_SHBE_Record> DRecords;
//        DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
        TreeMap<String, DW_SHBE_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet2[1];
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputUnderOccupiedDir(),
                "DigitalWelfareOutputReportForSarahUnderOccupied" + date1 + "-" + date2 + ".txt");
        // Iterate over councilRecords and join these with SHBE records
        // Aggregate totalRentArrears by postcode
        int countOfUnderOccupiedClaimantsInArrears = 0;
        int aggregations = 0;
        int totalSRecordCount = 0;
        int countMissingDRecords = 0;
        int countOfSRecordsWithNoDRecord = 0;
        int totalClaimsWithDRecords = 0;
        BigDecimal totalRentArrears_BigDecimal = BigDecimal.ZERO;
        TreeMap<String, BigDecimal> postcodeTotalArrears = new TreeMap<String, BigDecimal>();
        TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
        Iterator<String> ite = recs.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_UnderOccupiedReport_Record underOccupiedReport_Record;
            underOccupiedReport_Record = recs.get(councilTaxClaimNumber);
            double rentArrears = underOccupiedReport_Record.getTotalRentArrears();
            BigDecimal rentArrears_BigDecimal = BigDecimal.ZERO;
            if (rentArrears > 0) {
                countOfUnderOccupiedClaimantsInArrears++;
                rentArrears_BigDecimal = new BigDecimal(rentArrears);
                totalRentArrears_BigDecimal = totalRentArrears_BigDecimal.add(rentArrears_BigDecimal);
            }
            DW_SHBE_Record DRecord;
//            DRecord = DRecords.get(councilTaxClaimNumber);
            DRecord = DRecords.getDRecord(councilTaxClaimNumber);
            if (DRecord == null) {
                System.out.println("No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_Record);
                countMissingDRecords++;
                DW_SHBE_Record SRecord = SRecords.get(councilTaxClaimNumber);
                if (SRecord != null) {
                    countOfSRecordsWithNoDRecord++;
                }
            } else {
//                    DRecord.getClaimantsIncomeFromAttendanceAllowance()
//                                        DRecord.getTotalNumberOfRooms()
//                            

                totalClaimsWithDRecords++;
                String postcode = DRecord.getClaimantsPostcode();
                String truncatedPostcode = postcode.substring(0, postcode.length() - 2);
                int SRecordCount = DRecord.getSRecords().size();
                totalSRecordCount += SRecordCount;
                if (postcodeTotalArrears.containsKey(truncatedPostcode)) {
                    BigDecimal current = postcodeTotalArrears.get(truncatedPostcode);
                    BigDecimal arrears = current.add(rentArrears_BigDecimal);
                    postcodeTotalArrears.put(truncatedPostcode, arrears);
                    aggregations++;
                } else {
                    postcodeTotalArrears.put(truncatedPostcode, rentArrears_BigDecimal);
                }
                if (postcodeClaims.containsKey(truncatedPostcode)) {
                    int current = postcodeClaims.get(truncatedPostcode);
                    postcodeClaims.put(truncatedPostcode, current + 1);
                } else {
                    postcodeClaims.put(truncatedPostcode, 1);
                }
                //System.out.println("" + underOccupiedReport_DataRecord + ", " + SRecordCount + ", " + postcode);
            }
        }
        // Report
        pw.println("totalRentArrears " + totalRentArrears_BigDecimal.setScale(2, RoundingMode.HALF_UP));
        pw.println("countOfUnderOccupiedClaimantsInArrears " + countOfUnderOccupiedClaimantsInArrears);
        BigDecimal averageArrearsOfThoseInArrears = Generic_BigDecimal.divideRoundIfNecessary(
                totalRentArrears_BigDecimal,
                BigInteger.valueOf(countOfUnderOccupiedClaimantsInArrears),
                2,
                RoundingMode.HALF_UP);
        pw.println("averageArrearsOfThoseInArrears " + averageArrearsOfThoseInArrears);
        pw.println("totalClaimsWithDRecords " + totalClaimsWithDRecords);
        pw.println("aggregations " + aggregations);
        pw.println("countMissingDRecords " + countMissingDRecords);
        pw.println("countOfSRecordsWithNoDRecord " + countOfSRecordsWithNoDRecord);
        pw.println("totalSRecordCount " + totalSRecordCount);
        pw.println("postcode, claims, arrears, average");
        ite = postcodeTotalArrears.keySet().iterator();
        while (ite.hasNext()) {
            String postcode = ite.next();
            int claims = postcodeClaims.get(postcode);
            BigDecimal arrears = postcodeTotalArrears.get(postcode);

            // Format postcode
            postcode = DW_Postcode_Handler.formatPostcode(postcode);
            // Write answer
            BigDecimal average = Generic_BigDecimal.divideRoundIfNecessary(
                    arrears,
                    BigInteger.valueOf(claims),
                    2,
                    RoundingMode.UP);
            pw.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP) + ", " + average.setScale(2, RoundingMode.HALF_UP));
        }
        pw.close();
        //}
    }

    /**
     * Returns data about under occupancy by postcode.
     *
     * @param records
     * @param tSHBEfilenames
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as Postcode sectors and
     * values are counts of the number of claims subject to the bedroom tax.
     */
    public HashMap<String, TreeMap<String, Integer>> getUnderOccupiedbyPostcode(
            TreeMap<String, DW_UnderOccupiedReport_Record>[] records,
            String[] tSHBEfilenames) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
            int underOccupancyMonth = month;
            //int underOccupancyMonth = month + 2;
            String monthString = DW_SHBE_Handler.getMonth(tSHBEfilenames[month]);
            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataMonth1[0];
            TreeMap<String, DW_SHBE_Record> SRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataMonth1[1];
            // Iterate over records and join these with SHBE records to get postcodes
            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
            result.put(monthString, postcodeClaims);
            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
//            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                DW_SHBE_Record DRecord = DRecords.get(councilTaxClaimNumber);
                if (DRecord != null) {
                    String postcode = DRecord.getClaimantsPostcode();
                    // Format postcode
                    postcode = DW_Postcode_Handler.formatPostcode(postcode);
                    String postcodeSector = null;
                    try {
                        postcodeSector = DW_Postcode_Handler.getPostcodeSector(postcode);
                        if (postcodeClaims.containsKey(postcodeSector)) {
                            int current = postcodeClaims.get(postcodeSector);
                            postcodeClaims.put(postcodeSector, current + 1);
                        } else {
                            postcodeClaims.put(postcodeSector, 1);
                        }
                    } catch (Exception e) {
                        System.out.println("Cannot getPostcodeSector for postcode " + postcode);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns data about under occupancy by MSOA.
     *
     * @param date
     * @param underOccupiedReport_Set
     * @param SHBESet
     * @param lookupFromPostcodeToCensusCode
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as MSOA codes, and
     * values are counts of the number of claims subject to the bedroom tax.
     */
    public HashMap<String, TreeMap<String, Integer>> getUnderOccupiedbyMSOA(
            String date,
            DW_UnderOccupiedReport_Set underOccupiedReport_Set,
            Object[] SHBESet,
            TreeMap<String, String> lookupFromPostcodeToCensusCode) {
        HashMap<String, TreeMap<String, Integer>> result;
        result = new HashMap<String, TreeMap<String, Integer>>();

        DW_SHBE_CollectionHandler DRecords;
        DRecords = (DW_SHBE_CollectionHandler) SHBESet[0];
//        TreeMap<String, DW_SHBE_Record> DRecords;
//        DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];

        TreeMap<String, DW_SHBE_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> aggregatedClaims = new TreeMap<String, Integer>();
        result.put(date, aggregatedClaims);
        Iterator<String> ite = underOccupiedReport_Set.getSet().keySet().iterator();
        int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_Record DRecord;
//            DRecord = DRecords.get(councilTaxClaimNumber);
            DRecord = DRecords.getDRecord(councilTaxClaimNumber);
            if (DRecord != null) {
                String postcode = DRecord.getClaimantsPostcode();
                // Format postcode
                postcode = DW_Postcode_Handler.formatPostcode(postcode);
                postcode = postcode.replaceAll("'", "");
                postcode = postcode.replaceAll("/.", "");
                if (postcode.length() == 8) {
                    postcode = postcode.replaceAll(" ", "");
                }
                if (postcode.length() == 6) {
                    postcode = postcode.substring(0, 2) + " " + postcode.substring(3, 6);
                }
                String censusCode;
                censusCode = lookupFromPostcodeToCensusCode.get(postcode);
                if (censusCode == null) {
                    System.out.println("No MSOA for underoccupancy postcode " + postcode);
                    countOfRecordsNotAggregatedDueToUnrecognisedPostcode++;
                } else {
                    if (aggregatedClaims.containsKey(censusCode)) {
                        int current = aggregatedClaims.get(censusCode);
                        aggregatedClaims.put(censusCode, current + 1);
                    } else {
                        aggregatedClaims.put(censusCode, 1);
                    }
                }
            }
        }
        System.out.println("underOccupancyDate " + date);
        System.out.println("countOfRecordsNotAggregatedDueToUnrecognisedPostcode " + countOfRecordsNotAggregatedDueToUnrecognisedPostcode);
        return result;
    }

    /**
     * @return TreeMap<String, String[]> result where:--------------------------
     * Keys are postcodes and values are:---------------------------------------
     * values[0] = rec.getOa01();-----------------------------------------------
     * values[1] = rec.getMsoa01();---------------------------------------------
     * values[2] = rec.getOa11();-----------------------------------------------
     * values[3] = rec.getMsoa11();---------------------------------------------
     */
    private TreeMap<String, String[]> initLookupFromPostcodeToCensusCodes() {
        File inputDirectory = new File("/scratch01/Work/Projects/NewEnclosures/ONSPD/Data/");
        String inputFilename = inputFilename = "ONSPD_AUG_2013_UK_O.csv";
        File inFile = new File(inputDirectory, inputFilename);
        File outputDirectory = new File("/scratch02/DigitalWelfare/ONSPD/processed");
        String outputFilename = "PostcodeLookUp_TreeMap_String_Strings.thisFile";
        File outFile = new File(outputDirectory, outputFilename);
        //new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitPointLookup();
        //new DW_Postcode_Handler(inFile, outFile).run1();
        TreeMap<String, String[]> result = new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitCensusCodesLookup();
        return result;
    }

    /**
     * Returns data about the number of claims per postcode.
     *
     * @param tSHBEfilenames
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as Postcode sectors and
     * values are counts of the number of claims.
     */
    protected HashMap<String, TreeMap<String, Integer>> getCouncilTaxClaimCountByPostcode(
            String[] tSHBEfilenames) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
            TreeMap<String, Integer> monthsResult = new TreeMap<String, Integer>();
            String monthString = DW_SHBE_Handler.getMonth(tSHBEfilenames[month]);
            result.put(monthString, monthsResult);
            Object[] SHBEData = loadSHBEData(tSHBEfilenames[month]);
            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[0];
            //TreeMap<String, DW_SHBE_Record> SRecordsWithoutDRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[1];
            Iterator<String> ite = DRecords.keySet().iterator();
            String postcode;
            while (ite.hasNext()) {
                String councilTaxClaimNumber = ite.next();
                DW_SHBE_Record rec = DRecords.get(councilTaxClaimNumber);
                postcode = rec.getClaimantsPostcode();
                String[] partsOfPostcode = postcode.trim().split(" ");
                if (partsOfPostcode.length == 2) {
                    String truncatedPostcode = partsOfPostcode[0] + " " + partsOfPostcode[1].substring(0, 1);
                    //System.out.println(postcode + " " + truncatedPostcode );
                    //SHBE_DataHandler.
                    if (monthsResult.containsKey(truncatedPostcode)) {
                        Integer count = monthsResult.get(truncatedPostcode);
                        Integer newcount = count + 1;
                        monthsResult.put(truncatedPostcode, newcount);
                    } else {
                        monthsResult.put(truncatedPostcode, 1);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns data about the number of claims per MSOA.
     *
     * @param date
     * @param underOccupiedReport_Set
     * @param SHBESet
     * @param lookupFromPostcodeToCensusCode
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as MSOA codes and
     * values are counts of the number of claims.
     */
    protected HashMap<String, TreeMap<String, Integer>> getCouncilTaxClaimCountByMSOA(
            String date,
            DW_UnderOccupiedReport_Set underOccupiedReport_Set,
            Object[] SHBESet,
            TreeMap<String, String> lookupFromPostcodeToCensusCode) {
        HashMap<String, TreeMap<String, Integer>> result;
        result = new HashMap<String, TreeMap<String, Integer>>();

//            TreeMap<String, DW_SHBE_Record> DRecords;
//            DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
        DW_SHBE_CollectionHandler DRecords;
        DRecords = (DW_SHBE_CollectionHandler) SHBESet[0];

        TreeMap<String, DW_SHBE_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[1];
        int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;
        TreeMap<String, Integer> monthsResult = new TreeMap<String, Integer>();
        result.put(date, monthsResult);

        Iterator<String> ite;
        //ite = DRecords.keySet().iterator();
        ite = DRecords.lookup.keySet().iterator();
        String postcode;
        while (ite.hasNext()) {
            String councilTaxClaimNumber = ite.next();
            DW_SHBE_Record rec;
//            rec = DRecords.get(councilTaxClaimNumber);
            rec = DRecords.getDRecord(councilTaxClaimNumber);
            postcode = rec.getClaimantsPostcode();
            String[] partsOfPostcode = postcode.trim().split(" ");
            if (partsOfPostcode.length == 2) {

                // Format postcode
                postcode = postcode.trim();
                if (postcode.length() == 8) {
                    postcode = postcode.replaceAll(" ", "");
                }
                postcode = postcode.replaceAll("'", "");
                postcode = postcode.replaceAll("/.", "");
                if (postcode.length() == 8) {
                    postcode = postcode.replaceAll(" ", "");
                }
                if (postcode.length() == 6) {
                    postcode = postcode.substring(0, 2) + " " + postcode.substring(3, 6);
                }

                String censusCode;
                censusCode = lookupFromPostcodeToCensusCode.get(postcode);
                if (censusCode == null) {
                    System.out.println("No census Code for SHBE postcode " + postcode);
                    countOfRecordsNotAggregatedDueToUnrecognisedPostcode++;

                } else {
                    if (monthsResult.containsKey(censusCode)) {
                        Integer count = monthsResult.get(censusCode);
                        Integer newcount = count + 1;
                        monthsResult.put(censusCode, newcount);
                    } else {
                        monthsResult.put(censusCode, 1);
                    }
                }
            }
        }
        System.out.println("SHBE date " + date);
        System.out.println("countOfRecordsNotAggregatedDueToUnrecognisedPostcode " + countOfRecordsNotAggregatedDueToUnrecognisedPostcode);
        return result;
    }

//    
//    protected TreeMap<String,String> getCouncilTaxClaimCountByPostcode(String[] tSHBEfilenames) {
//        TreeMap<String,String> result = new TreeMap<String,String>();
//        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
//        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
//            String monthString = tSHBEfilenames[month + 1].split(" ")[1];
//            SHBEDataMonth2 = loadSHBEData(tSHBEfilenames[month + 1]);
//            TreeMap<String, DW_SHBE_Record> DRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataMonth1[0];
//            TreeMap<String, DW_SHBE_Record> SRecords = (TreeMap<String, DW_SHBE_Record>) SHBEDataMonth1[1];
//            PrintWriter reportPW = init_OutputTextFilePrintWriter("DigitalWelfareOutputReportForSarahUnderOccupied" + monthString + ".txt");
//            
//            
//            
//            
//            
//            
//            
//            SHBEDataMonth1 = SHBEDataMonth2;
//        
//        
//        
//        }
//        return result;
//    }
    /**
     * @deprecated
     */
    public void loadSHBEData() {
//        // Load Month_10_2010_11_381112_D_records.csv        
//        filename = "Month_10_2010_11_381112_D_records.csv";
//        Object[] month10 = aSHBE_DataHandler.loadInputData(args, _DW_directory, filename);
//        HashSet<SHBE_DataRecord> month10Records = (HashSet<SHBE_DataRecord>) month10[0];
//        HashSet<String> month10IDs = (HashSet<String>) month10[1];
//        System.out.println("" + month10Records.size() + " records loaded from " + filename);
//        System.out.println("month10IDs.size() " + month10IDs.size());
//
//        // Aggregate Month_10_2010_11_381112_D_records
//        Aggregate_SHBE_DataRecord aggregateMonth10Records = aSHBE_DataHandler.aggregate(month10Records);
//        System.out.println("aggregateMonth10Records " + aggregateMonth10Records.toString());
//
//        // Load Month_1_2010_11_329509_D_records.csv
//        filename = "Month_1_2010_11_329509_D_records.csv";
//        Object[] month1 = aSHBE_DataHandler.loadInputData(args, _DW_directory, filename);
//        HashSet<SHBE_DataRecord> month1Records = (HashSet<SHBE_DataRecord>) month1[0];
//        HashSet<String> month1IDs = (HashSet<String>) month1[1];
//        System.out.println("" + month1Records.size() + " records loaded from " + filename);
//        System.out.println("month1IDs.size() " + month1IDs.size());
//
//        // Aggregate Month_1_2010_11_329509_D_records.csv
//        Aggregate_SHBE_DataRecord aggregateMonth1Records = aSHBE_DataHandler.aggregate(month1Records);
//        System.out.println("aggregateMonth10Records " + aggregateMonth1Records.toString());
//
//        // Report number of new IDs
//        month10IDs.removeAll(month1IDs);
//        System.out.println("new IDs " + month10IDs.size());
        String filename;
        /*
         //2008
         hb9803_SHBE_206728k April 2008.csv
         hb9803_SHBE_234696k October 2008.csv
         //2009
         hb9803_SHBE_265149k April 2009.csv
         hb9803_SHBE_295723k October 2009.csv
         //2010
         hb9803_SHBE_329509k April 2010.csv
         hb9803_SHBE_363186k October 2010.csv
         //2011
         hb9803_SHBE_391746k March 2011.csv // This probably should be January not March.
         hb9803_SHBE_397524k April 2011.csv
         hb9803_SHBE_415181k July 2011.csv
         hb9803_SHBE_433970k October 2011.csv
         //2012
         hb9803_SHBE_451836k January 2012.csv
         hb9803_SHBE_470742k April 2012.csv
         hb9803_SHBE_490903k July 2012.csv
         hb9803_SHBE_511038k October 2012.csv
         //2013
         hb9803_SHBE_530243k January 2013.csv
         hb9803_SHBE_536123k February 2013.csv
         hb9991_SHBE_543169k March 2013.csv
         hb9991_SHBE_549416k April 2013.csv
         hb9991_SHBE_555086k May 2013.csv
         hb9991_SHBE_562036k June 2013.csv
         hb9991_SHBE_568694k July 2013.csv
         hb9991_SHBE_576432k August 2013.csv
         hb9991_SHBE_582832k September 2013.csv
         */

//        filename = "hb9803_SHBE_206728k April 2008.csv";
//        Object[] tSHBE_April2008 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2008_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2008[0];
//        HashSet<String> tSHBE_April2008_IDs = (HashSet<String>) tSHBE_April2008[1];
//        System.out.println("" + tSHBE_April2008_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_April2008_IDs.size() " + tSHBE_April2008_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_April2008 = (TreeSet<Long>) tSHBE_April2008[2];
//        System.out.println("recordIDsNotLoaded_April2008.size() " + recordIDsNotLoaded_April2008.size());
////102785 records loaded from hb9803_SHBE_206728k April 2008.csv
////tSHBE_April2008_IDs.size() 74457
////recordIDsNotLoaded_April2008.size() 15267
//
//        filename = "hb9803_SHBE_234696k October 2008.csv";
//        Object[] tSHBE_October2008 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2008_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2008[0];
//        HashSet<String> tSHBE_October2008_IDs = (HashSet<String>) tSHBE_October2008[1];
//        System.out.println("" + tSHBE_October2008_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_October2008_IDs.size() " + tSHBE_October2008_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_October2008 = (TreeSet<Long>) tSHBE_October2008[2];
//        System.out.println("recordIDsNotLoaded_October2008.size() " + recordIDsNotLoaded_October2008.size());
////109675 records loaded from hb9803_SHBE_234696k October 2008.csv
////tSHBE_October2008_IDs.size() 73998
////recordIDsNotLoaded_October2008.size() 7728
//        
//        filename = "hb9803_SHBE_265149k April 2009.csv";
//        Object[] tSHBE_April2009 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2009_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2009[0];
//        HashSet<String> tSHBE_April2009_IDs = (HashSet<String>) tSHBE_April2009[1];
//        System.out.println("" + tSHBE_April2009_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_April2009_IDs.size() " + tSHBE_April2009_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_April2009 = (TreeSet<Long>) tSHBE_April2009[2];
//        System.out.println("recordIDsNotLoaded_April2009.size() " + recordIDsNotLoaded_April2009.size());
////108326 records loaded from hb9803_SHBE_265149k April 2009.csv
////tSHBE_April2009_IDs.size() 77639
////recordIDsNotLoaded_April2009.size() 14943
//
//        filename = "hb9803_SHBE_295723k October 2009.csv";
//        Object[] tSHBE_October2009 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2009_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2009[0];
//        HashSet<String> tSHBE_October2009_IDs = (HashSet<String>) tSHBE_October2009[1];
//        System.out.println("" + tSHBE_October2009_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_October2009_IDs.size() " + tSHBE_October2009_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_October2009 = (TreeSet<Long>) tSHBE_October2009[2];
//        System.out.println("recordIDsNotLoaded_October2009.size() " + recordIDsNotLoaded_October2009.size());
////114206 records loaded from hb9803_SHBE_295723k October 2009.csv
////tSHBE_October2009_IDs.size() 80969
////recordIDsNotLoaded_October2009.size() 15242
//
//        filename = "hb9803_SHBE_329509k April 2010.csv";
//        Object[] tSHBE_April2010 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2010_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2010[0];
//        HashSet<String> tSHBE_April2010_IDs = (HashSet<String>) tSHBE_April2010[1];
//        System.out.println("" + tSHBE_April2010_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_April2010_IDs.size() " + tSHBE_April2010_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_April2010 = (TreeSet<Long>) tSHBE_April2010[2];
//        System.out.println("recordIDsNotLoaded_April2010.size() " + recordIDsNotLoaded_April2010.size());
////118289 records loaded from hb9803_SHBE_329509k April 2010.csv
////tSHBE_April2010_IDs.size() 82842
////recordIDsNotLoaded_April2010.size() 14982
//
//        filename = "hb9803_SHBE_363186k October 2010.csv";
//        Object[] tSHBE_October2010 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2010_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2010[0];
//        HashSet<String> tSHBE_October2010_IDs = (HashSet<String>) tSHBE_October2010[1];
//        System.out.println("" + tSHBE_October2010_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_October2010_IDs.size() " + tSHBE_October2010_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_October2010 = (TreeSet<Long>) tSHBE_October2010[2];
//        System.out.println("recordIDsNotLoaded_October2010.size() " + recordIDsNotLoaded_October2010.size());
////122732 records loaded from hb9803_SHBE_363186k October 2010.csv
////tSHBE_October2010_IDs.size() 84261
////recordIDsNotLoaded_October2010.size() 14346
//
//        filename = "hb9803_SHBE_391746k March 2011.csv";
//        Object[] tSHBE_January2011 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_January2011_Records = (HashSet<SHBE_DataRecord>) tSHBE_January2011[0];
//        HashSet<String> tSHBE_January2011_IDs = (HashSet<String>) tSHBE_January2011[1];
//        System.out.println("" + tSHBE_January2011_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_January2011_IDs.size() " + tSHBE_January2011_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_January2011 = (TreeSet<Long>) tSHBE_January2011[2];
//        System.out.println("recordIDsNotLoaded_January2011.size() " + recordIDsNotLoaded_January2011.size());
////123895 records loaded from hb9803_SHBE_391746k January 2011.csv
////tSHBE_January2011_IDs.size() 84347
////recordIDsNotLoaded_January2011.size() 13830
//
//        filename = "hb9803_SHBE_397524k April 2011.csv";
//        Object[] tSHBE_April2011 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2011_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2011[0];
//        HashSet<String> tSHBE_April2011_IDs = (HashSet<String>) tSHBE_April2011[1];
//        System.out.println("" + tSHBE_April2011_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_April2011_IDs.size() " + tSHBE_April2011_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_April2011 = (TreeSet<Long>) tSHBE_April2011[2];
//        System.out.println("recordIDsNotLoaded_April2011.size() " + recordIDsNotLoaded_April2011.size());
////123349 records loaded from hb9803_SHBE_397524k April 2011.csv
////tSHBE_April2011_IDs.size() 83750
////recordIDsNotLoaded_April2011.size() 13634
//
//        filename = "hb9803_SHBE_415181k July 2011.csv";
//        Object[] tSHBE_July2011 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_July2011_Records = (HashSet<SHBE_DataRecord>) tSHBE_July2011[0];
//        HashSet<String> tSHBE_July2011_IDs = (HashSet<String>) tSHBE_July2011[1];
//        System.out.println("" + tSHBE_July2011_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_July2011_IDs.size() " + tSHBE_July2011_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_July2011 = (TreeSet<Long>) tSHBE_July2011[2];
//        System.out.println("recordIDsNotLoaded_July2011.size() " + recordIDsNotLoaded_July2011.size());
////127333 records loaded from hb9803_SHBE_415181k July 2011.csv
////tSHBE_July2011_IDs.size() 85573
////recordIDsNotLoaded_July2011.size() 13459
//
//        filename = "hb9803_SHBE_433970k October 2011.csv";
//        Object[] tSHBE_October2011 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2011_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2011[0];
//        HashSet<String> tSHBE_October2011_IDs = (HashSet<String>) tSHBE_October2011[1];
//        System.out.println("" + tSHBE_October2011_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_October2011_IDs.size() " + tSHBE_October2011_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_October2011 = (TreeSet<Long>) tSHBE_October2011[2];
//        System.out.println("recordIDsNotLoaded_October2011.size() " + recordIDsNotLoaded_October2011.size());
////129128 records loaded from hb9803_SHBE_433970k October 2011.csv
////tSHBE_October2011_IDs.size() 86287
////recordIDsNotLoaded_October2011.size() 13351
//             
//        filename = "hb9803_SHBE_451836k January 2012.csv";
//        Object[] tSHBE_January2012 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_January2012_Records = (HashSet<SHBE_DataRecord>) tSHBE_January2012[0];
//        HashSet<String> tSHBE_January2012_IDs = (HashSet<String>) tSHBE_January2012[1];
//        System.out.println("" + tSHBE_January2012_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_January2012_IDs.size() " + tSHBE_January2012_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_January2012 = (TreeSet<Long>) tSHBE_January2012[2];
//        System.out.println("recordIDsNotLoaded_January2012.size() " + recordIDsNotLoaded_January2012.size());
////130737 records loaded from hb9803_SHBE_451836k January 2012.csv
////tSHBE_January2012_IDs.size() 86924
////recordIDsNotLoaded_January2012.size() 13120
//
//        filename = "hb9803_SHBE_470742k April 2012.csv";
//        Object[] tSHBE_April2012 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2012_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2012[0];
//        HashSet<String> tSHBE_April2012_IDs = (HashSet<String>) tSHBE_April2012[1];
//        System.out.println("" + tSHBE_April2012_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_April2012_IDs.size() " + tSHBE_April2012_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_April2012 = (TreeSet<Long>) tSHBE_April2012[2];
//        System.out.println("recordIDsNotLoaded_April2012.size() " + recordIDsNotLoaded_April2012.size());
////132850 records loaded from hb9803_SHBE_470742k April 2012.csv
////tSHBE_April2012_IDs.size() 87650
////recordIDsNotLoaded_April2012.size() 12808
//
//        filename = "hb9803_SHBE_490903k July 2012.csv";
//        Object[] tSHBE_July2012 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_July2012_Records = (HashSet<SHBE_DataRecord>) tSHBE_July2012[0];
//        HashSet<String> tSHBE_July2012_IDs = (HashSet<String>) tSHBE_July2012[1];
//        System.out.println("" + tSHBE_July2012_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_July2012_IDs.size() " + tSHBE_July2012_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_July2012 = (TreeSet<Long>) tSHBE_July2012[2];
//        System.out.println("recordIDsNotLoaded_July2012.size() " + recordIDsNotLoaded_July2012.size());
////134684 records loaded from hb9803_SHBE_490903k July 2012.csv
////tSHBE_July2012_IDs.size() 87991
////recordIDsNotLoaded_July2012.size() 12613
//
//        filename = "hb9803_SHBE_511038k October 2012.csv";
//        Object[] tSHBE_October2012 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2012_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2012[0];
//        HashSet<String> tSHBE_October2012_IDs = (HashSet<String>) tSHBE_October2012[1];
//        System.out.println("" + tSHBE_October2012_Records.size() + " records loaded from " + filename);
//        System.out.println("tSHBE_October2012_IDs.size() " + tSHBE_October2012_IDs.size());
//        TreeSet<Long> recordIDsNotLoaded_October2012 = (TreeSet<Long>) tSHBE_October2012[2];
//        System.out.println("recordIDsNotLoaded_October2012.size() " + recordIDsNotLoaded_October2012.size());
////135895 records loaded from hb9803_SHBE_511038k October 2012.csv
////tSHBE_October2012_IDs.size() 87864
////recordIDsNotLoaded_October2012.size() 12192
//        
        filename = "hb9803_SHBE_530243k January 2013.csv";
        Object[] tSHBE_January2013 = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        HashSet<DW_SHBE_Record> tSHBE_January2013_Records = (HashSet<DW_SHBE_Record>) tSHBE_January2013[0];
        System.out.println("" + tSHBE_January2013_Records.size() + " records loaded from " + filename);
//totalCouncilTaxBenefitClaims 87541
//totalCouncilTaxAndHousingBenefitClaims 71221
//totalHousingBenefitClaims 71221
//countDRecords 87541
//countSRecords 61673
//recordIDsNotLoaded.size() 0
//Count of Unique ClaimantNationalInsuranceNumberIDs 87513
//Count of Unique PartnerNationalInsuranceNumberIDs 18379
//Count of Unique DependentsNationalInsuranceNumberIDs 2783
//Count of Unique NonDependentsNationalInsuranceNumberIDs 1453
//Count of Unique AllHouseholdNationalInsuranceNumberIDs 108691
//87541 records loaded from hb9803_SHBE_530243k January 2013.csv

        filename = "hb9803_SHBE_536123k February 2013.csv";
        Object[] tSHBE_February2013 = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        HashSet<DW_SHBE_Record> tSHBE_February2013_Records = (HashSet<DW_SHBE_Record>) tSHBE_February2013[0];
        System.out.println("" + tSHBE_February2013_Records.size() + " records loaded from " + filename);
//totalCouncilTaxBenefitClaims 87764
//totalCouncilTaxAndHousingBenefitClaims 71497
//totalHousingBenefitClaims 71497
//countDRecords 87764
//countSRecords 61884
//recordIDsNotLoaded.size() 0
//Count of Unique ClaimantNationalInsuranceNumberIDs 87736
//Count of Unique PartnerNationalInsuranceNumberIDs 18428
//Count of Unique DependentsNationalInsuranceNumberIDs 2810
//Count of Unique NonDependentsNationalInsuranceNumberIDs 1466
//Count of Unique AllHouseholdNationalInsuranceNumberIDs 108988
//87764 records loaded from hb9803_SHBE_536123k February 2013.csv
        PrintWriter pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "DifferencesJanuary2013_to_February2013.txt");
        reportDifferences(tSHBE_January2013, tSHBE_February2013, pw);
        pw.close();
//totalRepeatClaimantsByClaimantNationalInsuranceNumberID 86108
//totalNewClaimantsByClaimantNationalInsuranceNumberID 1628

//        filename = "hb9991_SHBE_543169k March 2013.csv";
//        Object[] tSHBE_March2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_March2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_March2013[0];
//        System.out.println("" + tSHBE_March2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9991_SHBE_549416k April 2013.csv";
//        Object[] tSHBE_April2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_April2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_April2013[0];
//        System.out.println("" + tSHBE_April2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9991_SHBE_555086k May 2013.csv";
//        Object[] tSHBE_May2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_May2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_May2013[0];
//        System.out.println("" + tSHBE_May2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9991_SHBE_562036k June 2013.csv";
//        Object[] tSHBE_June2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_June2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_June2013[0];
//        System.out.println("" + tSHBE_June2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9991_SHBE_568694k July 2013.csv";
//        Object[] tSHBE_July2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_July2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_July2013[0];
//        System.out.println("" + tSHBE_July2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9991_SHBE_576432k August 2013.csv";
//        Object[] tSHBE_August2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_August2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_August2013[0];
//        System.out.println("" + tSHBE_August2013_Records.size() + " records loaded from " + filename);
//        
//        
//        filename = "hb9991_SHBE_582832k September 2013.csv";
//        Object[] tSHBE_September2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_September2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_September2013[0];
//        System.out.println("" + tSHBE_September2013_Records.size() + " records loaded from " + filename);
//        
//
//        filename = "hb9803_SHBE_511038k October 2012.csv";
//        Object[] tSHBE_October2013 = aSHBE_DataHandler.loadInputData(_DW_directory, filename);
//        HashSet<SHBE_DataRecord> tSHBE_October2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_October2013[0];
//        System.out.println("" + tSHBE_October2013_Records.size() + " records loaded from " + filename);
//        
    }

    public void reportDifferences(
            Object[] tSHBE_time1,
            Object[] tSHBE_time2,
            PrintWriter reportingPW) {

        HashSet<DW_SHBE_Record> tSHBE_time1DRecords = (HashSet<DW_SHBE_Record>) tSHBE_time1[0];
        HashSet<DW_SHBE_Record> tSHBE_time1SRecords = (HashSet<DW_SHBE_Record>) tSHBE_time1[1];
        HashSet<String> tSHBE_time1ClaimantNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[2];
        HashSet<String> tSHBE_time1PartnerNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[3];
        HashSet<String> tSHBE_time1DependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[4];
        HashSet<String> tSHBE_time1NonDependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[5];
        HashSet<String> tSHBE_time1AllHouseholdNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[6];

        HashSet<DW_SHBE_Record> tSHBE_time2DRecords = (HashSet<DW_SHBE_Record>) tSHBE_time2[0];
        HashSet<DW_SHBE_Record> tSHBE_time2SRecords = (HashSet<DW_SHBE_Record>) tSHBE_time2[1];
        HashSet<String> tSHBE_time2ClaimantNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time2[2];
        HashSet<String> tSHBE_time2PartnerNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time2[3];
        HashSet<String> tSHBE_time2DependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time2[4];
        HashSet<String> tSHBE_time2NonDependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time2[5];
        HashSet<String> tSHBE_time2AllHouseholdNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time2[6];

        Iterator<String> ite;

        // Report totalRepeatClaimantsByClaimantNationalInsuranceNumberID and totalNewClaimantsByClaimantNationalInsuranceNumberID
        int totalRepeatClaimantsByClaimantNationalInsuranceNumberID = 0;
        int totalNewClaimantsByClaimantNationalInsuranceNumberID = 0;
        ite = tSHBE_time2ClaimantNationalInsuranceNumberIDs.iterator();
        while (ite.hasNext()) {
            String time2ClaimantNationalInsuranceNumberID = ite.next();
            if (tSHBE_time1ClaimantNationalInsuranceNumberIDs.contains(time2ClaimantNationalInsuranceNumberID)) {
                totalRepeatClaimantsByClaimantNationalInsuranceNumberID++;
            } else {
                totalNewClaimantsByClaimantNationalInsuranceNumberID++;
            }
        }
        System.out.println("totalRepeatClaimantsByClaimantNationalInsuranceNumberID " + totalRepeatClaimantsByClaimantNationalInsuranceNumberID);
        System.out.println("totalNewClaimantsByClaimantNationalInsuranceNumberID " + totalNewClaimantsByClaimantNationalInsuranceNumberID);

        // Report totalRepeatClaimantsByClaimantNationalInsuranceNumberID and totalNewClaimantsByClaimantNationalInsuranceNumberID
        int totalClaimantsBecomingPartners = 0;
        int totalClaimantsNotBecomingPartners = 0;
        ite = tSHBE_time1ClaimantNationalInsuranceNumberIDs.iterator();
        while (ite.hasNext()) {
            String time1ClaimantNationalInsuranceNumberID = ite.next();
            if (tSHBE_time2PartnerNationalInsuranceNumberIDs.contains(time1ClaimantNationalInsuranceNumberID)) {
                totalClaimantsBecomingPartners++;
            } else {
                totalClaimantsNotBecomingPartners++;
            }
        }
        reportingPW.println("totalClaimantsBecomingPartners " + totalClaimantsBecomingPartners);
        reportingPW.println("totalClaimantsNotBecomingPartners " + totalClaimantsNotBecomingPartners);

        // Report totalPartnersBecomingClaimants and totalPartnersNotBecomingClaimants
        int totalPartnersBecomingClaimants = 0;
        int totalPartnersNotBecomingClaimants = 0;
        ite = tSHBE_time1PartnerNationalInsuranceNumberIDs.iterator();
        while (ite.hasNext()) {
            String time1PartnerNationalInsuranceNumberID = ite.next();
            if (tSHBE_time2ClaimantNationalInsuranceNumberIDs.contains(time1PartnerNationalInsuranceNumberID)) {
                totalPartnersBecomingClaimants++;
            } else {
                totalPartnersNotBecomingClaimants++;
            }
        }
        reportingPW.println("totalPartnersBecomingClaimants " + totalPartnersBecomingClaimants);
        reportingPW.println("totalPartnersNotBecomingClaimants " + totalPartnersNotBecomingClaimants);

        // Report total Dependents becoming Claimants 
        int totalDependentsBecomingClaimants = 0;
        int totalDependentsNotBecomingClaimants = 0;
        ite = tSHBE_time1DependentsNationalInsuranceNumberIDs.iterator();
        while (ite.hasNext()) {
            String time1DependentsNationalInsuranceNumberIDs = ite.next();
            if (tSHBE_time2ClaimantNationalInsuranceNumberIDs.contains(time1DependentsNationalInsuranceNumberIDs)) {
                totalDependentsBecomingClaimants++;
            } else {
                totalDependentsNotBecomingClaimants++;
            }
        }
        reportingPW.println("totalDependentsBecomingClaimants " + totalDependentsBecomingClaimants);
        reportingPW.println("totalDependentsNotBecomingClaimants " + totalDependentsNotBecomingClaimants);

        // Report total Non Dependents becoming Claimants
        int totalNonDependentsBecomingClaimants = 0;
        int totalNonDependentsNotBecomingClaimants = 0;
        ite = tSHBE_time1NonDependentsNationalInsuranceNumberIDs.iterator();
        while (ite.hasNext()) {
            String time1NonDependentsNationalInsuranceNumberIDs = ite.next();
            if (tSHBE_time2ClaimantNationalInsuranceNumberIDs.contains(time1NonDependentsNationalInsuranceNumberIDs)) {
                totalNonDependentsBecomingClaimants++;
            } else {
                totalNonDependentsNotBecomingClaimants++;
            }
        }
        reportingPW.println("totalNonDependentsBecomingClaimants " + totalNonDependentsBecomingClaimants);
        reportingPW.println("totalNonDependentsNotBecomingClaimants " + totalNonDependentsNotBecomingClaimants);

    }

}
