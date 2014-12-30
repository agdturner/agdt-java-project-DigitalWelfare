/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.SHBE_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.SHBE_DataRecord_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.UnderOccupiedReport_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.UnderOccupiedReport_DataRecord_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeGeocoder;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_DataProcessor_SHBE extends DW_Processor {

    private SHBE_DataRecord_Handler aSHBE_DataHandler;
    private UnderOccupiedReport_DataRecord_Handler aUnderOccupiedReport_DataHandler;

    public DW_DataProcessor_SHBE() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new DW_DataProcessor_SHBE().run(args);
    }

    /**
     * @param args args[0] is for specifying the _DW_directory path to the
     * project data. If nothing is passed in the default:
     * "/scratch02/DW_Processor/SHBEData/" is used.
     */
    @Override
    public void run(String[] args) {
        init_SHBE_DataHandler(args);

//        init_OutputTextFilePrintWriter(
//                DW_Files.getOutputSHBETablesDir(),
//                "DigitalWelfare");
//        Object[] underOccupiedReportData = loadUnderOccupiedReportData(args);
//        processSHBEReportData(underOccupiedReportData);
//        processSHBEReportDataForSarah(underOccupiedReportData, args);
//        processSHBEReportDataIntoMigrationMatricesForApril(underOccupiedReportData);
        //processforChangeInTenancyForMoversMatrixesForApril(underOccupiedReportData);
        //processforChangeInTenancyMatrixesForApril();
        //getTotalClaimantsByTenancyType();
        String level;
        //level = "LSOA";
        //aggregateClients(level);
//        level = "OA";
//        aggregateClients(level);
        level = "MSOA";
        aggregateClients(level);
    }

    /**
     *
     */
    public void aggregateClients(String level) {
        
        File outputDir;
        outputDir = new File(
                DW_Files.getGeneratedSHBEDir(),
                level);
        String allClaimants_String = "AllClaimants";
        
        File dir = new File(
        outputDir,
        allClaimants_String);
        dir.mkdirs();
                            
        String[] allSHBEFilenames = getSHBEFilenamesAll();
        TreeMap<String, String> tLookupFromPostcodeToCensusCode;
        tLookupFromPostcodeToCensusCode = getLookupFromPostcodeToCensusCode(
                level,
                2011);

        // 0,2,4,7,11,17 are April data for 2008, 2009, 2010, 2011, 2012 and 2013 respectively
        int startIndex;
        int endIndex;
        String startMonth;
        String endMonth;
        String startYear;
        String endYear;

        String[] SHBEFilenames = getSHBEFilenamesAll();
        //String SHBEFilename = SHBEFilenames[0];
        for (String SHBEFilename : SHBEFilenames) {
            Object[] SHBEData = loadSHBEData(SHBEFilename);
            String year = getYear(SHBEFilename);
            String month = getMonth(SHBEFilename);

            TreeMap<String, Integer> claimantsCountByLSOA;
            claimantsCountByLSOA = new TreeMap<String, Integer>();

            /* 
             * SHBEData[0] is a TreeMap<String, SHBE_DataRecord> representing DRecords,---
             * SHBEData[1] is a TreeMap<String, SHBE_DataRecord> representing SRecords,---
             * SHBEData[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
             * SHBEData[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
             * SHBEData[5] is a HashSet<String> of
             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
             * SHBEData[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
             */
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
            Iterator<String> ite = DRecords.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                SHBE_DataRecord DRecord = DRecords.get(claimID);
                String postcode = DRecord.getClaimantsPostcode();
                String formattedPostcode = formatPostcodeForONSPDLookup(postcode);
                String censusCode;
                censusCode = tLookupFromPostcodeToCensusCode.get(
                        formattedPostcode);
                if (censusCode != null) {
                    if (claimantsCountByLSOA.containsKey(censusCode)) {
                        int currentCount = claimantsCountByLSOA.get(censusCode);
                        int newCount = currentCount + 1;
                        claimantsCountByLSOA.put(censusCode, newCount);
                    } else {
                        claimantsCountByLSOA.put(censusCode, 1);
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
            Iterator<String> ite2 = claimantsCountByLSOA.keySet().iterator();
            while (ite2.hasNext()) {
                String LSOA = ite2.next();
                Integer Count = claimantsCountByLSOA.get(LSOA);
                pw.println(LSOA + ", " + Count);
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
        String[] SHBEFilenames = getSHBEFilenamesAll();
        for (String SHBEFilename : SHBEFilenames) {
            Object[] SHBEData = loadSHBEData(SHBEFilename);
            String year = getYear(SHBEFilename);
            String month = getMonth(SHBEFilename);
            TreeMap<Integer, Integer> ymAllResult = new TreeMap<Integer, Integer>();
            TreeMap<Integer, Integer> ymHBResult = new TreeMap<Integer, Integer>();
            //result.put(year+month, ymResult);
            /* result[0] is a TreeMap<String, SHBE_DataRecord> representing DRecords,---
             * result[1] is a TreeMap<String, SHBE_DataRecord> representing SRecords,---
             * result[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
             * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
             * result[5] is a HashSet<String> of
             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
             * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
             */
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
            Iterator<String> ite = DRecords.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                SHBE_DataRecord DRecord = DRecords.get(claimID);
                int aTenancyType = DRecord.getTenancyType();
                Integer aTenancyTypeInteger = new Integer(aTenancyType);
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
//            String year = getYear(SHBEFilenames[i]);
//            String month = getMonth(SHBEFilenames[i]);
//            TreeMap<Integer,Integer> ymResult = new TreeMap<Integer,Integer>();
//            result.put(year+month, ymResult);
//            /* result[0] is a TreeMap<String, SHBE_DataRecord> representing DRecords,---
//             * result[1] is a TreeMap<String, SHBE_DataRecord> representing SRecords,---
//             * result[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
//             * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
//             * result[5] is a HashSet<String> of
//             * NonDependentsNationalInsuranceNumberIDs,---------------------------------
//             * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
//             */
//            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
//            Iterator<String> ite = DRecords.keySet().iterator();
//            while (ite.hasNext()) {
//                String claimID = ite.next();
//                SHBE_DataRecord aSHBE_DataRecord = DRecords.get(claimID);
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
            Object[] underOccupiedReportData) {

        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "CountOfClaimsByDates.txt");

        String[] allSHBEFilenames = getSHBEFilenamesAll();
        // 0,2,4,7,11,17 are April data for 2008, 2009, 2010, 2011, 2012 and 2013 respectively
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEforChangeInTenancyForMoversMatrixesForApril(
                underOccupiedReportData,
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
//        HashSet<String> AllNINOOfClaimants2013 = (HashSet<String>) migrationData[1];
//        HashMap<String, String> AllNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[2];
//        HashSet<String> HBNINOOfClaimants2013 = (HashSet<String>) migrationData[4];
//        HashMap<String, String> HBNINOOfClaimantsThatMoved20122013 = (HashMap<String, String>) migrationData[5];
//        countOfNewButPreviousClaimant = getCountOfNewButPreviousClaimant(
//                AllNINOOfClaimants2012,
//                AllNINOOfClaimants2013,
//                AllNationalInsuranceNumbersAndDatesOfClaims);
//        pw.println("2013 countOfNewButPreviousClaimant " + countOfNewButPreviousClaimant);
//
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
    public void processforChangeInTenancyMatrixesForApril() {
//
//        PrintWriter pw = init_OutputTextFilePrintWriter("CountOfClaimsByDates.txt");

        String[] allSHBEFilenames = getSHBEFilenamesAll();
        // 0,2,4,7,11,17 are April data for 2008, 2009, 2010, 2011, 2012 and 2013 respectively
        int startIndex;
        int endIndex;
        String startMonth;
        String endMonth;
        String startYear;
        String endYear;

        // 2008 2009
        startIndex = 0;
        endIndex = 2;
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
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
            Object[] underOccupiedReportData,
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
        TreeMap<String, SHBE_DataRecord> DRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, SHBE_DataRecord> DRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            SHBE_DataRecord startDRecord = DRecordsStart.get(councilTaxClaimNumber);
            if (startDRecord != null) {
                String postcodeStart = startDRecord.getClaimantsPostcode();
                postcodeStart = formatPostcode(postcodeStart);
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

                SHBE_DataRecord endDRecord = DRecordsEnd.get(councilTaxClaimNumber);
                //String destinationPostcodeDistrict;
                Integer endTenancyType;
                String destinationPostcode = null;
                if (endDRecord == null) {
                    endTenancyType = -999;
                } else {
                    destinationPostcode = endDRecord.getClaimantsPostcode();
                    destinationPostcode = formatPostcode(destinationPostcode);
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
            SHBE_DataRecord DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                SHBE_DataRecord DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
                    destinationPostcode = formatPostcode(destinationPostcode);
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
        TreeMap<String, SHBE_DataRecord> startDRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, SHBE_DataRecord> endDRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = startDRecords.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            SHBE_DataRecord startDRecord = startDRecords.get(councilTaxClaimNumber);
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                SHBE_DataRecord endDRecord = endDRecords.get(councilTaxClaimNumber);
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
            SHBE_DataRecord endDRecord = endDRecords.get(councilTaxClaimNumber);
            if (endDRecord != null) {
                SHBE_DataRecord DRecordStart = startDRecords.get(councilTaxClaimNumber);
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
     * Method that loads the Under-Occupied report data for Leeds.
     *
     * @param args args[0] is the _DW_directory
     * @return Object[] result: where result[0] is a TreeMap<String,
     * UnderOccupiedReport_DataRecord>[]
     */
    public Object[] loadUnderOccupiedReportData(String[] args) {
        Object[] result = new Object[2];
        int numberOfUnderOccupiedReportFiles = 11;
        TreeMap<String, UnderOccupiedReport_DataRecord>[] councilRecords = new TreeMap[numberOfUnderOccupiedReportFiles];
        result[0] = councilRecords;
        TreeMap<String, UnderOccupiedReport_DataRecord>[] RSLRecords = new TreeMap[numberOfUnderOccupiedReportFiles];
        result[1] = RSLRecords;
        aUnderOccupiedReport_DataHandler = new UnderOccupiedReport_DataRecord_Handler();
        File dir;
        dir = DW_Files.getInputUnderOccupiedDir();
        String filename;
        filename = "2013 14 Under Occupied Report For University Year Start Council Tenants.csv";
        councilRecords[0] = aUnderOccupiedReport_DataHandler.loadInputData(
                dir,
                filename);
        System.out.println("" + councilRecords[0].size() + " records loaded from " + filename);
        filename = "2013 14 Under Occupied Report For University Year Start RSLs.csv";
        RSLRecords[0] = aUnderOccupiedReport_DataHandler.loadInputData(
                dir,
                filename);
        System.out.println("" + RSLRecords[0].size() + " records loaded from " + filename);
        for (int i = 1; i < numberOfUnderOccupiedReportFiles; i++) {
            filename = "2013 14 Under Occupied Report For University Month " + i + " Council Tenants.csv";
            councilRecords[i] = aUnderOccupiedReport_DataHandler.loadInputData(
                    dir,
                    filename);
            System.out.println("" + councilRecords[i].size() + " records loaded from " + filename);
            filename = "2013 14 Under Occupied Report For University Month " + i + " RSLs.csv";
            RSLRecords[i] = aUnderOccupiedReport_DataHandler.loadInputData(
                    dir,
                    filename);
            System.out.println("" + RSLRecords[i].size() + " records loaded from " + filename);
        }
        return result;
    }

    /**
     * A method used for creating some preliminary results.
     *
     * @param underOccupiedReportData
     */
    public void processSHBEReportData(Object[] underOccupiedReportData) {
        TreeMap<String, UnderOccupiedReport_DataRecord>[] councilRecords;
        councilRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[0];
        //TreeMap<String, UnderOccupiedReport_DataRecord>[] RSLRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[1];

        int underOccupancyMonth = 2;
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputUnderOccupiedDir(),
                "DigitalWelfareOutputUnderOccupiedReport");
        String[] filenames = getSHBEFilenamesSome();
        String filename = filenames[2];
        Object[] SHBEData;

        SHBEData = loadSHBEData(filename);
        TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
        TreeMap<String, SHBE_DataRecord> SRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[1];
        //loadSHBEData();

        // Iterate over councilRecords and join these with SHBE records
        // Aggregate totalRentArrears by postcode
        int aggregations = 0;
        int totalSRecordCount = 0;
        int countMissingDRecords = 0;
        BigDecimal totalRentArrears_BigDecimal = BigDecimal.ZERO;
        TreeMap<String, BigDecimal> postcodeTotalArrears = new TreeMap<String, BigDecimal>();
        TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
        Iterator<String> ite = councilRecords[underOccupancyMonth].keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            UnderOccupiedReport_DataRecord underOccupiedReport_DataRecord = councilRecords[underOccupancyMonth].get(councilTaxClaimNumber);
            double rentArrears = underOccupiedReport_DataRecord.getTotalRentArrears();
            BigDecimal rentArrears_BigDecimal = new BigDecimal(rentArrears);
            totalRentArrears_BigDecimal = totalRentArrears_BigDecimal.add(rentArrears_BigDecimal);
            SHBE_DataRecord DRecord = DRecords.get(councilTaxClaimNumber);
            if (DRecord == null) {
                System.out.println("No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                countMissingDRecords++;
                SHBE_DataRecord SRecord = SRecords.get(councilTaxClaimNumber);
                if (SRecord != null) {
                    int dosomething = 1;
                }
            } else {
                String postcode = DRecord.getClaimantsPostcode();
                String truncatedPostcode = postcode.substring(0, postcode.length() - 2);
                int SRecordCount = DRecord.getSRecords().size();
                totalSRecordCount += SRecordCount;

                if (rentArrears > 0) {
                    int debug = 0;
                }

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
        pw.println("aggregations " + aggregations);
        pw.println("countMissingDRecords " + countMissingDRecords);
        pw.println("totalSRecordCount " + totalSRecordCount);
        pw.println("postcode, claims, arrears");
        ite = postcodeTotalArrears.keySet().iterator();
        while (ite.hasNext()) {
            String postcode = ite.next();
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

    /**
     * Method for initialising aSHBE_DataRecord_Handler
     *
     * @param args
     */
    private void init_SHBE_DataHandler(String[] args) {
        aSHBE_DataHandler = new SHBE_DataRecord_Handler();
    }

    /**
     * Method for reporting how many bedroom tax people have moved from one
     * month to the next.
     *
     * @param underOccupiedReportData
     */
    public void processSHBEReportDataForSarah(
            Object[] underOccupiedReportData) {
        File dir = new File(
                DW_Files.getOutputDir(),
                "Sarah");
        File outputDir = new File(
                dir,
                "DigitalWelfareOutputReportForSarah");
        TreeMap<String, UnderOccupiedReport_DataRecord>[] councilRecords;
        councilRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[0];
        TreeMap<String, UnderOccupiedReport_DataRecord>[] RSLRecords;
        RSLRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[1];
        PrintWriter outPW;
        outPW = init_OutputTextFilePrintWriter(
                outputDir,
                "DigitalWelfareOutputReportForSarah");
        String[] tSHBEfilenames = getSHBEFilenamesSome();

        String level = "MSOA";
        TreeMap<String, String> lookupFromPostcodeToCensusCode;
        lookupFromPostcodeToCensusCode = getLookupFromPostcodeToCensusCode(
                level,
                2011);
        // Council Records
        outPW.println("Council Records");
        reportBedroomTaxChanges(
                councilRecords,
                tSHBEfilenames);
        // RSL Records
        outPW.println("RSL Records");
        reportBedroomTaxChanges(
                RSLRecords,
                tSHBEfilenames);

        // Report aggregates by postcode
        // Unit postcodes such as "LS2 9JT" are aggregated to "LS2 9"
        // reportUnderOccupiedIndebtedness
        reportUnderOccupiedIndebtedness(
                underOccupiedReportData,
                tSHBEfilenames);
        // CouncilRecordsAggregation
        String name;
        Iterator<String> ite;
//        HashMap<String, TreeMap<String, Integer>> councilRecordAggregatedData = getUnderOccupiedbyPostcode(
//              councilRecords, tSHBEfilenames);
        HashMap<String, TreeMap<String, Integer>> councilRecordAggregatedData;
        councilRecordAggregatedData = getUnderOccupiedbyMSOA(
                councilRecords, tSHBEfilenames, lookupFromPostcodeToCensusCode);
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
//        HashMap<String, TreeMap<String, Integer>> RSLRecordAggregatedData = getUnderOccupiedbyPostcode(
//                RSLRecords, tSHBEfilenames);
        HashMap<String, TreeMap<String, Integer>> RSLRecordAggregatedData = getUnderOccupiedbyMSOA(
                RSLRecords, tSHBEfilenames, lookupFromPostcodeToCensusCode);
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
                tSHBEfilenames,
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

        String[] allSHBEFilenames = getSHBEFilenamesAll();
        int startIndex = 0;
        int endIndex = allSHBEFilenames.length - 1;
        HashSet<String> AllNINOOfClaimantsStartYear = new HashSet<String>();
        HashSet<String> AllNINOOfClaimantsEndYear = new HashSet<String>();
        HashMap<String, String> AllNINOOfClaimantsThatMoved = new HashMap<String, String>();
        HashMap<String, HashSet<String>> AllNationalInsuranceNumbersAndDatesOfMoves;
        AllNationalInsuranceNumbersAndDatesOfMoves = new HashMap<String, HashSet<String>>();
        HashMap<String, TreeSet<String>> AllNationalInsuranceNumbersAndDatesOfClaims;
        AllNationalInsuranceNumbersAndDatesOfClaims = new HashMap<String, TreeSet<String>>();
        String startYear = getYear(allSHBEFilenames[startIndex]);
        String startMonth = getMonth(allSHBEFilenames[startIndex]);
        String endYear = getYear(allSHBEFilenames[endIndex]);
        String endMonth = getMonth(allSHBEFilenames[endIndex]);
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

    /**
     *
     * @param underOccupiedReportData
     */
    public void processSHBEReportDataIntoMigrationMatricesForApril(
            Object[] underOccupiedReportData) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "CountOfClaimsByDates.txt");
        String[] allSHBEFilenames = getSHBEFilenamesAll();
        // 0,2,4,7,11,17 are April data for 2008, 2009, 2010, 2011, 2012 and 2013 respectively
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReportData,
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
        startYear = getYear(allSHBEFilenames[startIndex]);
        endYear = getYear(allSHBEFilenames[endIndex]);
        startMonth = getMonth(allSHBEFilenames[startIndex]);
        endMonth = getMonth(allSHBEFilenames[endIndex]);
        migrationData = processSHBEReportDataIntoMigrationMatricesForApril(
                underOccupiedReportData,
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

    public Object[] processSHBEReportDataIntoMigrationMatricesForApril(
            Object[] underOccupiedReportData,
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
        String startMonth = getMonth(startName);
        String startYear = getYear(startName);
        String endName = allSHBEFilenames[endIndex];
        String endMonth = getMonth(endName);
        String endYear = getYear(endName);

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

    public void writeOutMigrationMatrix(
            String[] allSHBEFilenames,
            Object[] migrationData,
            int startIndex,
            int endIndex,
            String type) {
        initOddPostcodes();
        String startName = allSHBEFilenames[startIndex];
        String startMonth = getMonth(startName);
        String startYear = getYear(startName);
        String endName = allSHBEFilenames[endIndex];
        String endMonth = getMonth(endName);
        String endYear = getYear(endName);

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
//        String startMonth = getMonth(startName);
//        String startYear = getYear(startName);
//        String endName = allSHBEFilenames[endIndex];
//        String endMonth = getMonth(endName);
//        String endYear = getYear(endName);
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
        TreeMap<String, TreeMap<String, Integer>> resultMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = resultMatrix;
        TreeSet<String> originsAndDestinations = new TreeSet<String>();
        result[1] = originsAndDestinations;
        Object[] SHBEDataStart = loadSHBEData(tSHBEfilenames[startIndex]);
        TreeMap<String, SHBE_DataRecord> DRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, SHBE_DataRecord> DRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            SHBE_DataRecord DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
            if (DRecordStart != null) {
                String postcodeStart = DRecordStart.getClaimantsPostcode();
                postcodeStart = formatPostcode(postcodeStart);
                String startPostcodeDistrict = getPostcodeDistrict(postcodeStart);
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

                SHBE_DataRecord DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
                String destinationPostcodeDistrict;
                String destinationPostcode = null;
                if (DRecordEnd == null) {
                    destinationPostcodeDistrict = "unknown";
                } else {
                    destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = formatPostcode(destinationPostcode);
                    destinationPostcodeDistrict = getPostcodeDistrict(destinationPostcode);
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
                    destinationPostcodeDistrict = formatPostcodeDistrict(destinationPostcodeDistrict);
                    //if (!destinationPostcodeDistrict.isEmpty()) {
                    if (destinationCounts.containsKey(destinationPostcodeDistrict)) {
                        int current = destinationCounts.get(destinationPostcodeDistrict);
                        destinationCounts.put(destinationPostcodeDistrict, current + 1);
                    } else {
                        destinationCounts.put(destinationPostcodeDistrict, 1);
                        originsAndDestinations.add(destinationPostcodeDistrict);
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
            SHBE_DataRecord DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                SHBE_DataRecord DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
                    destinationPostcode = formatPostcode(destinationPostcode);
                    String destinationPostcodeDistrict = getPostcodeDistrict(destinationPostcode);
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
                }
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
        TreeMap<String, SHBE_DataRecord> DRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsStart = (TreeMap<String, SHBE_DataRecord>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, SHBE_DataRecord> DRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[0];
        //TreeMap<String, SHBE_DataRecord> SRecordsEnd = (TreeMap<String, SHBE_DataRecord>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            SHBE_DataRecord DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
            if (DRecordStart != null) {
                // Filter for only Housing Benefit Claimants
                if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                    String postcodeStart = DRecordStart.getClaimantsPostcode();
                    postcodeStart = formatPostcode(postcodeStart);
                    String startPostcodeDistrict = getPostcodeDistrict(postcodeStart);
                    startPostcodeDistrict = formatPostcodeDistrict(startPostcodeDistrict);
                    //if (!startPostcodeDistrict.isEmpty()) {
                    if (resultMatrix.containsKey(startPostcodeDistrict)) {
                        destinationCounts = resultMatrix.get(startPostcodeDistrict);
                    } else {
                        destinationCounts = new TreeMap<String, Integer>();
                        resultMatrix.put(startPostcodeDistrict, destinationCounts);
                        //originsAndDestinations.add(startPostcodeDistrict);
                    }
                    SHBE_DataRecord DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
                    // Filter for only Housing Benefit Claimants
                    if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                        String destinationPostcodeDistrict;
                        String destinationPostcode = null;
                        if (DRecordEnd == null) {
                            destinationPostcodeDistrict = "unknown";
                        } else {
                            destinationPostcode = DRecordEnd.getClaimantsPostcode();
                            destinationPostcode = formatPostcode(destinationPostcode);
                            destinationPostcodeDistrict = getPostcodeDistrict(destinationPostcode);
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
            SHBE_DataRecord DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                SHBE_DataRecord DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
                if (DRecordStart == null) {
                    String startPostcodeDistrict = "unknown";
                    String destinationPostcode = DRecordEnd.getClaimantsPostcode();
                    destinationPostcode = formatPostcode(destinationPostcode);
                    String destinationPostcodeDistrict = getPostcodeDistrict(destinationPostcode);
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
            TreeMap<String, UnderOccupiedReport_DataRecord>[] underOccupancyRecords,
            String[] tSHBEfilenames) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                DW_Files.getOutputSHBETablesDir(),
                "BedroomTaxChanges.txt");
        
        //TreeMap<String,TreeMap<String,>>
        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
            int underOccupancyMonth1 = month;
            int underOccupancyMonth2 = underOccupancyMonth1 + 1;
//            int underOccupancyMonth1 = month + 2;
//            int underOccupancyMonth2 = underOccupancyMonth1 + 1;
            TreeMap<String, SHBE_DataRecord> DRecordsMonth1 = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
            // Load next SHBE data
            Object[] SHBEDataMonth2 = loadSHBEData(tSHBEfilenames[month + 1]);
            TreeMap<String, SHBE_DataRecord> DRecordsMonth2 = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
            // Get the set of those underOccupancy ids from month 1 not in month 2
            int countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds = 0;
            int countOfRemainingClaimantsThatAreNoLongerUnderOccupiers = 0;
            int countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds = 0;
            Set<String> potentiallyMoved = underOccupancyRecords[underOccupancyMonth1].keySet();
            potentiallyMoved.removeAll(underOccupancyRecords[underOccupancyMonth2].keySet());
            String councilTaxClaimNumber;
            Iterator<String> ite;
            ite = potentiallyMoved.iterator();
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                SHBE_DataRecord DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
                if (DRecordMonth2 == null) {
                    countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds++;
                } else {
                    SHBE_DataRecord DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
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
            Set<String> s = underOccupancyRecords[underOccupancyMonth1].keySet();
            ite = underOccupancyRecords[underOccupancyMonth2].keySet().iterator();
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                SHBE_DataRecord DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
                SHBE_DataRecord DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
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
            s.removeAll(underOccupancyRecords[underOccupancyMonth2].keySet());
            countOfNoLongerUnderOccupancyClaimants = s.size();
            // Report
            pw.println("Bedroom Tax " + getMonth(tSHBEfilenames[month + 1]));
            pw.println("countOfNewClaimantsThatAreUnderOccupiers " + countOfNewClaimantsThatAreUnderOccupiers);
            pw.println("countOfRemainingClaimantsThatAreUnderOccupiers " + countOfRemainingClaimantsThatAreUnderOccupiers);
            pw.println("countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved " + countOfRemainingClaimantsThatAreStillUnderOccupiersThatHaveMoved);
            pw.println("countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds " + countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds);
            pw.println("countOfNoLongerUnderOccupancyClaimants " + countOfNoLongerUnderOccupancyClaimants);
            pw.println("countOfRemainingClaimantsThatAreNoLongerUnderOccupiers " + countOfRemainingClaimantsThatAreNoLongerUnderOccupiers);
            pw.println("countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds " + countOfRemainingClaimantsThatAreNoLongerUnderOccupiersThatHaveMovedWithinLeeds);
            SHBEDataMonth1 = SHBEDataMonth2;
            pw.close();
        }
    }

    /**
     * Method for getting SHBE data filenames in an array
     *
     * @return String[] SHBE data filenames
     */
    public String[] getSHBEFilenamesSome() {
        String[] result = new String[6];
        /* January and February are not counted in this as yet as the 
         * underoccupancy data starts with a baseline in March 2013
         */
//        result[0] = "hb9803_SHBE_530243k January 2013.csv";
//        result[1] = "hb9803_SHBE_536123k February 2013.csv";
//        result[2] = "hb9991_SHBE_543169k March 2013.csv";
        result[0] = "hb9991_SHBE_549416k April 2013.csv";
        result[1] = "hb9991_SHBE_555086k May 2013.csv";
        result[2] = "hb9991_SHBE_562036k June 2013.csv";
        result[3] = "hb9991_SHBE_568694k July 2013.csv";
        result[4] = "hb9991_SHBE_576432k August 2013.csv";
        result[5] = "hb9991_SHBE_582832k September 2013.csv";
        return result;
    }

    /**
     * Method for getting SHBE data filenames in an array
     *
     * @return String[] result of SHBE data filenames where---------------------
     * result[0] = "hb9803_SHBE_206728k April 2008.csv";------------------------
     * result[1] = "hb9803_SHBE_234696k October 2008.csv";----------------------
     * result[2] = "hb9803_SHBE_265149k April 2009.csv";------------------------
     * result[3] = "hb9803_SHBE_295723k October 2009.csv";----------------------
     * result[4] = "hb9803_SHBE_329509k April 2010.csv";------------------------
     * result[5] = "hb9803_SHBE_363186k October 2010.csv";----------------------
     * result[6] = "hb9803_SHBE_391746k March 2011.csv";------------------------
     * result[7] = "hb9803_SHBE_397524k April 2011.csv";------------------------
     * result[8] = "hb9803_SHBE_415181k July 2011.csv";-------------------------
     * result[9] = "hb9803_SHBE_433970k October 2011.csv";----------------------
     * result[11] = "hb9803_SHBE_470742k April 2012.csv";-----------------------
     * result[12] = "hb9803_SHBE_490903k July 2012.csv";------------------------
     * result[13] = "hb9803_SHBE_511038k October 2012.csv";---------------------
     * result[14] = "hb9803_SHBE_530243k January 2013.csv";---------------------
     * result[15] = "hb9803_SHBE_536123k February 2013.csv";--------------------
     * result[16] = "hb9991_SHBE_543169k March 2013.csv";-----------------------
     * result[17] = "hb9991_SHBE_549416k April 2013.csv";-----------------------
     * result[18] = "hb9991_SHBE_555086k May 2013.csv";-------------------------
     * result[19] = "hb9991_SHBE_562036k June 2013.csv";------------------------
     * result[20] = "hb9991_SHBE_568694k July 2013.csv";------------------------
     * result[21] = "hb9991_SHBE_576432k August 2013.csv";----------------------
     * result[22] = "hb9991_SHBE_582832k September 2013.csv";-------------------
     */
    public String[] getSHBEFilenamesAll() {
        String[] result = new String[23];
        result[0] = "hb9803_SHBE_206728k April 2008.csv";
        result[1] = "hb9803_SHBE_234696k October 2008.csv";
        result[2] = "hb9803_SHBE_265149k April 2009.csv";
        result[3] = "hb9803_SHBE_295723k October 2009.csv";
        result[4] = "hb9803_SHBE_329509k April 2010.csv";
        result[5] = "hb9803_SHBE_363186k October 2010.csv";
        result[6] = "hb9803_SHBE_391746k March 2011.csv";
        result[7] = "hb9803_SHBE_397524k April 2011.csv";
        result[8] = "hb9803_SHBE_415181k July 2011.csv";
        result[9] = "hb9803_SHBE_433970k October 2011.csv";
        result[10] = "hb9803_SHBE_451836k January 2012.csv";
        result[11] = "hb9803_SHBE_470742k April 2012.csv";
        result[12] = "hb9803_SHBE_490903k July 2012.csv";
        result[13] = "hb9803_SHBE_511038k October 2012.csv";
        result[14] = "hb9803_SHBE_530243k January 2013.csv";
        result[15] = "hb9803_SHBE_536123k February 2013.csv";
        result[16] = "hb9991_SHBE_543169k March 2013.csv";
        result[17] = "hb9991_SHBE_549416k April 2013.csv";
        result[18] = "hb9991_SHBE_555086k May 2013.csv";
        result[19] = "hb9991_SHBE_562036k June 2013.csv";
        result[20] = "hb9991_SHBE_568694k July 2013.csv";
        result[21] = "hb9991_SHBE_576432k August 2013.csv";
        result[22] = "hb9991_SHBE_582832k September 2013.csv";
        return result;
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "May"
     *
     * @param SHBEFilename
     * @return
     */
    public String getMonth(String SHBEFilename) {
        return SHBEFilename.split(" ")[1];
    }

    /**
     * For example for SHBEFilename "hb9991_SHBE_555086k May 2013.csv", this
     * returns "2013"
     *
     * @param SHBEFilename
     * @return
     */
    public String getYear(String SHBEFilename) {
        return SHBEFilename.split(" ")[2].substring(0, 4);
    }

    /**
     * Loads SHBE Data from filename.
     *
     * @param filename
     * @return Object[7] result where:------------------------------------------
     * result[0] is a TreeMap<String, SHBE_DataRecord> representing DRecords,---
     * result[1] is a TreeMap<String, SHBE_DataRecord> representing SRecords,---
     * result[3] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs,----
     * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs,--
     * result[5] is a HashSet<String> of
     * NonDependentsNationalInsuranceNumberIDs,---------------------------------
     * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs.
     */
    public Object[] loadSHBEData(String filename) {
        Object[] result = aSHBE_DataHandler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        TreeMap<String, SHBE_DataRecord> tSHBE_Records = (TreeMap<String, SHBE_DataRecord>) result[0];
        System.out.println("" + tSHBE_Records.size() + " DRecords loaded from " + filename);
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
            Object[] underOccupiedReportData,
            String[] tSHBEfilenames) {
        TreeMap<String, UnderOccupiedReport_DataRecord>[] councilRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[0];
        //TreeMap<String, UnderOccupiedReport_DataRecord>[] RSLRecords = (TreeMap<String, UnderOccupiedReport_DataRecord>[]) underOccupiedReportData[1];
        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
            int underOccupancyMonth = month;
            //int underOccupancyMonth = month + 2;
            String monthString = getMonth(tSHBEfilenames[month + 1]);
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
            TreeMap<String, SHBE_DataRecord> SRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[1];
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    DW_Files.getOutputUnderOccupiedDir(),
                    "DigitalWelfareOutputReportForSarahUnderOccupied" + monthString + ".txt");
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
            Iterator<String> ite = councilRecords[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                UnderOccupiedReport_DataRecord underOccupiedReport_DataRecord = councilRecords[underOccupancyMonth].get(councilTaxClaimNumber);
                double rentArrears = underOccupiedReport_DataRecord.getTotalRentArrears();
                BigDecimal rentArrears_BigDecimal = BigDecimal.ZERO;
                if (rentArrears > 0) {
                    countOfUnderOccupiedClaimantsInArrears++;
                    rentArrears_BigDecimal = new BigDecimal(rentArrears);
                    totalRentArrears_BigDecimal = totalRentArrears_BigDecimal.add(rentArrears_BigDecimal);
                }
                SHBE_DataRecord DRecord = DRecords.get(councilTaxClaimNumber);
                if (DRecord == null) {
                    System.out.println("No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                    countMissingDRecords++;
                    SHBE_DataRecord SRecord = SRecords.get(councilTaxClaimNumber);
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
                postcode = formatPostcode(postcode);
                // Write answer
                BigDecimal average = Generic_BigDecimal.divideRoundIfNecessary(
                        arrears,
                        BigInteger.valueOf(claims),
                        2,
                        RoundingMode.UP);
                pw.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP) + ", " + average.setScale(2, RoundingMode.HALF_UP));
            }
            pw.close();
        }
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
            TreeMap<String, UnderOccupiedReport_DataRecord>[] records,
            String[] tSHBEfilenames) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
            int underOccupancyMonth = month;
            //int underOccupancyMonth = month + 2;
            String monthString = getMonth(tSHBEfilenames[month]);
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
            TreeMap<String, SHBE_DataRecord> SRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[1];
            // Iterate over records and join these with SHBE records to get postcodes
            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
            result.put(monthString, postcodeClaims);
            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
//            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                SHBE_DataRecord DRecord = DRecords.get(councilTaxClaimNumber);
                if (DRecord != null) {
                    String postcode = DRecord.getClaimantsPostcode();

                    // Format postcode
                    postcode = formatPostcode(postcode);
                    String postcodeSector = null;
                    try {
                        postcodeSector = getPostcodeSector(postcode);
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
     * @param records
     * @param lookupFromPostcodeToCensusCodes Used to look up MSOA code from
     * unit postcode.
     * @param tSHBEfilenames
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as MSOA codes, and
     * values are counts of the number of claims subject to the bedroom tax.
     */
    public HashMap<String, TreeMap<String, Integer>> getUnderOccupiedbyMSOA(
            TreeMap<String, UnderOccupiedReport_DataRecord>[] records,
            String[] tSHBEfilenames,
            TreeMap<String, String> lookupFromPostcodeToCensusCode) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {

            int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;

            int underOccupancyMonth = month;
            //int underOccupancyMonth = month + 2;
            String monthString = getMonth(tSHBEfilenames[month]);
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
            TreeMap<String, SHBE_DataRecord> SRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[1];
            // Iterate over records and join these with SHBE records to get postcodes
            TreeMap<String, Integer> aggregatedClaims = new TreeMap<String, Integer>();
            result.put(monthString, aggregatedClaims);
            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
//            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                SHBE_DataRecord DRecord = DRecords.get(councilTaxClaimNumber);
                if (DRecord != null) {
                    String postcode = DRecord.getClaimantsPostcode();

                    // Format postcode
                    postcode = formatPostcode(postcode);
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
            System.out.println("underOccupancyMonth " + underOccupancyMonth);
            System.out.println("countOfRecordsNotAggregatedDueToUnrecognisedPostcode " + countOfRecordsNotAggregatedDueToUnrecognisedPostcode);
        }

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
        //new PostcodeGeocoder(inFile, outFile).generatePostCodePointLookup();
        //new PostcodeGeocoder(inFile, outFile).run1();
        TreeMap<String, String[]> result = new PostcodeGeocoder(inFile, outFile).generatePostcodeStringsLookup();
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
            String monthString = getMonth(tSHBEfilenames[month]);
            result.put(monthString, monthsResult);
            Object[] SHBEData = loadSHBEData(tSHBEfilenames[month]);
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
            //TreeMap<String, SHBE_DataRecord> SRecordsWithoutDRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[1];
            Iterator<String> ite = DRecords.keySet().iterator();
            String postcode;
            while (ite.hasNext()) {
                String councilTaxClaimNumber = ite.next();
                SHBE_DataRecord rec = DRecords.get(councilTaxClaimNumber);
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
     * @param tSHBEfilenames
     * @param lookupFromPostcodeToCensusCodes Used to look up MSOA code from
     * unit postcode.
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as MSOA codes and
     * values are counts of the number of claims.
     */
    protected HashMap<String, TreeMap<String, Integer>> getCouncilTaxClaimCountByMSOA(
            String[] tSHBEfilenames,
            TreeMap<String, String> lookupFromPostcodeToCensusCode) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {

            int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;

            TreeMap<String, Integer> monthsResult = new TreeMap<String, Integer>();
            String monthString = getMonth(tSHBEfilenames[month]);
            result.put(monthString, monthsResult);
            Object[] SHBEData = loadSHBEData(tSHBEfilenames[month]);
            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[0];
            //TreeMap<String, SHBE_DataRecord> SRecordsWithoutDRecords = (TreeMap<String, SHBE_DataRecord>) SHBEData[1];
            Iterator<String> ite = DRecords.keySet().iterator();
            String postcode;
            while (ite.hasNext()) {
                String councilTaxClaimNumber = ite.next();
                SHBE_DataRecord rec = DRecords.get(councilTaxClaimNumber);
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
            System.out.println("SHBE month " + monthString);
            System.out.println("countOfRecordsNotAggregatedDueToUnrecognisedPostcode " + countOfRecordsNotAggregatedDueToUnrecognisedPostcode);

        }
        return result;
    }

//    
//    protected TreeMap<String,String> getCouncilTaxClaimCountByPostcode(String[] tSHBEfilenames) {
//        TreeMap<String,String> result = new TreeMap<String,String>();
//        Object[] SHBEDataMonth1 = loadSHBEData(tSHBEfilenames[0]);
//        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
//            String monthString = tSHBEfilenames[month + 1].split(" ")[1];
//            SHBEDataMonth2 = loadSHBEData(tSHBEfilenames[month + 1]);
//            TreeMap<String, SHBE_DataRecord> DRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[0];
//            TreeMap<String, SHBE_DataRecord> SRecords = (TreeMap<String, SHBE_DataRecord>) SHBEDataMonth1[1];
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
        Object[] tSHBE_January2013 = aSHBE_DataHandler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        HashSet<SHBE_DataRecord> tSHBE_January2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_January2013[0];
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
        Object[] tSHBE_February2013 = aSHBE_DataHandler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        HashSet<SHBE_DataRecord> tSHBE_February2013_Records = (HashSet<SHBE_DataRecord>) tSHBE_February2013[0];
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

        HashSet<SHBE_DataRecord> tSHBE_time1DRecords = (HashSet<SHBE_DataRecord>) tSHBE_time1[0];
        HashSet<SHBE_DataRecord> tSHBE_time1SRecords = (HashSet<SHBE_DataRecord>) tSHBE_time1[1];
        HashSet<String> tSHBE_time1ClaimantNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[2];
        HashSet<String> tSHBE_time1PartnerNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[3];
        HashSet<String> tSHBE_time1DependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[4];
        HashSet<String> tSHBE_time1NonDependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[5];
        HashSet<String> tSHBE_time1AllHouseholdNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[6];

        HashSet<SHBE_DataRecord> tSHBE_time2DRecords = (HashSet<SHBE_DataRecord>) tSHBE_time2[0];
        HashSet<SHBE_DataRecord> tSHBE_time2SRecords = (HashSet<SHBE_DataRecord>) tSHBE_time2[1];
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
