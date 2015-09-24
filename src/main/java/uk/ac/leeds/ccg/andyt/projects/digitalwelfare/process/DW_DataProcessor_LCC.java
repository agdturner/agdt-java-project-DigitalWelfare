/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_S_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

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
    //private double distanceThreshold;

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
        String[] SHBEFilenames;
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        ArrayList<String> claimantTypes;
        claimantTypes = new ArrayList<String>();
        claimantTypes.add("HB");
        claimantTypes.add("CTB");
        ArrayList<String> levels;
        levels = new ArrayList<String>();
        levels.add("OA");
        levels.add("LSOA");
        levels.add("MSOA");
        levels.add("PostcodeUnit");
        levels.add("PostcodeSector");
        levels.add("PostcodeDistrict");

        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add("All"); // Count of all claimants
////                allTypes.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add("OnFlow"); // These are people not claiming the previous month and that have not claimed before.
        types.add("ReturnFlow"); // These are people not claiming the previous month but that have claimed before.
        types.add("Stable"); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add("AllInChurn"); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add("AllOutChurn"); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        ArrayList<String> distanceTypes;
        distanceTypes = new ArrayList<String>();
        distanceTypes.add("InDistanceChurn"); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add("WithinDistanceChurn"); // A count of all claimants that have moved within this area.
        distanceTypes.add("OutDistanceChurn"); // A count of all claimants that have moved out from this area.

        // Tenure Type Groups
        TreeMap<String, ArrayList<Integer>> tenureTypeGroups;
        tenureTypeGroups = new TreeMap<String, ArrayList<Integer>>();
        ArrayList<Integer> all;
        all = DW_SHBE_Handler.getTenureTypeAll();
        tenureTypeGroups.put("all", all);
        ArrayList<Integer> regulated;
        regulated = DW_SHBE_Handler.getTenureTypeRegulated();
        tenureTypeGroups.put("regulated", regulated);
        ArrayList<Integer> unregulated;
        unregulated = new ArrayList<Integer>();
        unregulated.add(3);
        tenureTypeGroups.put("unregulated", unregulated);
        ArrayList<String> tenureTypesGrouped;
        tenureTypesGrouped = new ArrayList<String>();
        tenureTypesGrouped.add("Regulated");
        tenureTypesGrouped.add("Unregulated");
        tenureTypesGrouped.add("Ungrouped");
        tenureTypesGrouped.add("-999");
        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_LineGraph.getIncludes();

        // A useful indication of places from where people are displaced?
        //types.add("Unknown"); // Not worth doing?
        int startIndex;
        //startIndex = SHBEFilenames.length - 2; //this is for a normal carry on run
        //startIndex = 33; //Deals with from the problem with October 2014
        startIndex = 0; //From the beginning.

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }
        ArrayList<Integer> regulatedGroups;
        regulatedGroups = DW_SHBE_Handler.getTenureTypeRegulated();
        ArrayList<Integer> unregulatedGroups;
        unregulatedGroups = DW_SHBE_Handler.getTenureTypeUnregulated();
        boolean loadData;
        loadData = false;

        Generic_UKPostcode_Handler postcodeHandler;
        postcodeHandler = new Generic_UKPostcode_Handler();
        ArrayList<Boolean> bArray;
        bArray = new ArrayList<Boolean>();
        bArray.add(true);
        bArray.add(false);
        Iterator<Boolean> iteB;
        iteB = bArray.iterator();
        while (iteB.hasNext()) {
            boolean checkPreviousTenure;
            checkPreviousTenure = iteB.next();
            System.out.println("checkPreviousTenure " + checkPreviousTenure);
            Iterator<Boolean> iteB2;
            iteB2 = bArray.iterator();
            while (iteB2.hasNext()) {
                boolean reportTenancyTransitionBreaks;
                reportTenancyTransitionBreaks = iteB2.next();
                reportTenancyTransitionBreaks = false;
                System.out.println("reportTenancyTransitionBreaks " + reportTenancyTransitionBreaks);
                tenancyChanges(
                        SHBEFilenames,
                        startIndex,
                        tenureTypeGroups,
                        tenureTypesGrouped,
                        regulatedGroups,
                        unregulatedGroups,
                        includes,
                        levels,
                        loadData,
                        checkPreviousTenure,
                        reportTenancyTransitionBreaks);
                Iterator<Boolean> iteB3;
                iteB3 = bArray.iterator();
                while (iteB3.hasNext()) {
                    boolean postcodeChange;
                    postcodeChange = iteB3.next();
                    Iterator<Boolean> iteB4;
                    iteB4 = bArray.iterator();
                    while (iteB4.hasNext()) {
                        boolean checkPreviousPostcode;
                        checkPreviousPostcode = iteB4.next();
                        tenancyTypeAndPostcodeChanges(
                                postcodeHandler,
                                SHBEFilenames,
                                startIndex,
                                tenureTypeGroups,
                                tenureTypesGrouped,
                                regulatedGroups,
                                unregulatedGroups,
                                includes,
                                levels,
                                loadData,
                                postcodeChange,
                                checkPreviousPostcode,
                                checkPreviousTenure,
                                reportTenancyTransitionBreaks);
                    }
                }
            }
//            multipleTenancyChanges(
//                    SHBEFilenames,
//                    startIndex,
//                    tenureTypeGroups,
//                    tenureTypesGrouped,
//                    regulatedGroups,
//                    unregulatedGroups,
//                    includes,
//                    levels,
//                    checkPreviousTenure);
        }
//        // Get Lookups from postcodes to level codes
//        TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode;
//        lookupsFromPostcodeToLevelCode = getLookupsFromPostcodeToLevelCode(levels);
//        //Generic_UKPostcode_Handler.isValidPostcodeForm(String postcode)
//        aggregateClaimants(
//                lookupsFromPostcodeToLevelCode,
//                SHBEFilenames,
//                startIndex,
//                claimantTypes,
//                tenureTypeGroups,
//                tenureTypesGrouped,
//                regulatedGroups,
//                unregulatedGroups,
//                includes,
//                levels,
//                types,
//                distanceTypes,
//                distances);
    }

    /**
     * @param levels A set of levels expected values include OA, LSOA, MSOA,
     * PostcodeUnit, PostcodeSector, PostcodeDistrict
     * @return A set of look ups from postcodes to each level input in levels.
     */
    public static TreeMap<String, TreeMap<String, String>> getLookupsFromPostcodeToLevelCode(
            ArrayList<String> levels) {
        TreeMap<String, TreeMap<String, String>> result;
        result = new TreeMap<String, TreeMap<String, String>>();
        Iterator<String> ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
            tLookupFromPostcodeToLevelCode = getLookupFromPostcodeToLevelCode(
                    level,
                    2011);
            result.put(level, tLookupFromPostcodeToLevelCode);
        }
        return result;
    }

    /**
     * For the postcode input, this returns the area code for the level input
     * using tLookupFromPostcodeToCensusCode.
     *
     * @param level
     * @param postcode
     * @param tLookupFromPostcodeToCensusCode
     * @return The area code for level from tLookupFromPostcodeToCensusCode for
     * postcode. This may return "" or null.
     */
    public static String getAreaCode(
            String level,
            String postcode,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode) {
        String result = "";
        // Special Case
        if (postcode.trim().isEmpty()) {
            return result;
        }
        if (level.equalsIgnoreCase("OA")
                || level.equalsIgnoreCase("LSOA")
                || level.equalsIgnoreCase("MSOA")) {
            String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
            result = tLookupFromPostcodeToCensusCode.get(
                    formattedPostcode);
            if (result == null) {
                result = "";
            }
        } else {
            if (level.equalsIgnoreCase("PostcodeUnit")
                    || level.equalsIgnoreCase("PostcodeSector")
                    || level.equalsIgnoreCase("PostcodeDistrict")) {
                if (level.equalsIgnoreCase("PostcodeUnit")) {
                    result = postcode;
                }
                if (level.equalsIgnoreCase("PostcodeSector")) {
                    result = DW_Postcode_Handler.getPostcodeSector(postcode);
                }
                if (level.equalsIgnoreCase("PostcodeDistrict")) {
                    result = DW_Postcode_Handler.getPostcodeDistrict(postcode);
                }
            } else {
                // Unrecognised level
                int debug = 1;
            }
        }
        return result;
    }

    /**
     * @param claimantNINO1
     * @param i
     * @param claimantNationalInsuranceNumberIndexes
     * @return True iff claimantNINO1 is in
     * claimantNationalInsuranceNumberIndexes in any index from 0 to i - 1.
     */
    private boolean getHasClaimantBeenSeenBefore(
            String claimantNINO1,
            int i,
            ArrayList<HashSet<String>> claimantNationalInsuranceNumberIndexes) {
        boolean result = false;
        for (int index = i - 1; index > 0; index--) {
            //for (int index = 0; index < i; index++) {
            HashSet<String> claimantNationalInsuranceNumberIndex;
            claimantNationalInsuranceNumberIndex = claimantNationalInsuranceNumberIndexes.get(index);
            if (claimantNationalInsuranceNumberIndex.contains(claimantNINO1)) {
                return true;
            }
        }
        return result;
    }

    public static String getLastMonth_yearMonth(
            String year,
            String month,
            String[] SHBEFilenames,
            int i,
            int startIndex) {
        String result = null;
        if (i > startIndex + 2) {
            String lastMonthYear = DW_SHBE_Handler.getYear(SHBEFilenames[i - 1]);
            int yearInt = Integer.parseInt(year);
            int lastMonthYearInt = Integer.parseInt(lastMonthYear);
            if (!(yearInt == lastMonthYearInt || yearInt == lastMonthYearInt + 1)) {
                return null;
            }
            String lastMonthMonth = DW_SHBE_Handler.getMonth(SHBEFilenames[i - 1]);
            result = lastMonthYear + lastMonthMonth;
        }

        return result;
    }

    public static String getLastYear_yearMonth(
            String year,
            String month,
            String[] SHBEFilenames,
            int i,
            int startIndex) {
        String result = null;
        if (i > startIndex + 13) {
            String lastYearYear = DW_SHBE_Handler.getYear(SHBEFilenames[i - 12]);
            String lastYearMonth = DW_SHBE_Handler.getMonth(SHBEFilenames[i - 12]);
            int yearInt = Integer.parseInt(year);
            int lastYearYearInt = Integer.parseInt(lastYearYear);
            if (!(yearInt == lastYearYearInt + 1)) {
                return null;
            }
            if (!(lastYearMonth.contains(month) || month.contains(lastYearMonth))) {
                return null;
            }
            result = lastYearYear + lastYearMonth;
        }
        return result;
    }

    /**
     *
     * @param SHBEFilenames
     * @param startIndex
     * @param tenureTypeGroups
     * @param tenureTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param checkPreviousTenure
     */
    public void multipleTenancyChanges(
            String[] SHBEFilenames,
            int startIndex,
            TreeMap<String, ArrayList<Integer>> tenureTypeGroups,
            ArrayList<String> tenureTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            boolean checkPreviousTenure) {
        String filename;
        filename = SHBEFilenames[startIndex];
        File f;
        f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                filename);
        HashMap<String, Integer> nationalInsuranceNumberByTenure0;
        nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                f);
        HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures;
        nationalInsuranceNumberByTenures = new HashMap<Integer, HashMap<String, Integer>>();
        nationalInsuranceNumberByTenures.put(startIndex, nationalInsuranceNumberByTenure0);
        HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberTenureChanges;
        nationalInsuranceNumberTenureChanges = new HashMap<Integer, HashMap<String, String>>();
        HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberTenureChangesGrouped;
        nationalInsuranceNumberTenureChangesGrouped = new HashMap<Integer, HashMap<String, String>>();

        HashMap<String, Boolean> initFirsts;
        initFirsts = new HashMap<String, Boolean>();
        HashMap<String, Integer> previousIndexs;
        previousIndexs = new HashMap<String, Integer>();

        Iterator<String> ite;
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            ArrayList<Integer> include = includes.get(includeKey);
            if (include.contains(startIndex)) {
                initFirsts.put(includeKey, true);
                previousIndexs.put(includeKey, startIndex);
            } else {
                initFirsts.put(includeKey, false);
            }
        }

        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
            // Load next data
            filename = SHBEFilenames[i];
            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                    filename);
            HashMap<String, Integer> nationalInsuranceNumberByTenure;
            nationalInsuranceNumberByTenure = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                    f);
            nationalInsuranceNumberByTenures.put(i, nationalInsuranceNumberByTenure);
            ite = includes.keySet().iterator();
            while (ite.hasNext()) {
                String includeKey;
                includeKey = ite.next();
                ArrayList<Integer> include;
                include = includes.get(includeKey);
                HashMap<String, ArrayList<String>> tenureChanges;
                Boolean initFirst;
                initFirst = initFirsts.get(includeKey);
                if (!initFirst) {
                    if (include.contains(i)) {
                        // Initialise
                        nationalInsuranceNumberByTenures.put(i, nationalInsuranceNumberByTenure);
                        previousIndexs.put(includeKey, i);
                        initFirsts.put(includeKey, true);
                    }
                } else {
                    if (include.contains(i)) {
                        int previousIndex;
                        previousIndex = previousIndexs.get(includeKey);
                        nationalInsuranceNumberByTenure0 = nationalInsuranceNumberByTenures.get(previousIndex);
                        String year0;
                        year0 = DW_SHBE_Handler.getYear(SHBEFilenames[previousIndex]);
                        String month0;
                        month0 = DW_SHBE_Handler.getMonth(SHBEFilenames[previousIndex]);
                        // Set Year and Month variables
                        String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
                        String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
                        // Get TenancyTypeTranistionMatrix
                        Object[] tenancyTypeTranistionMatrixETC;
                        tenancyTypeTranistionMatrixETC = getMultipleTenancyTypeTranistionMatrix(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                nationalInsuranceNumberTenureChanges,
                                include,
                                i);
                        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
                        tenancyTypeTranistionMatrix = (TreeMap<Integer, TreeMap<Integer, Integer>>) tenancyTypeTranistionMatrixETC[0];
                        HashMap<String, String> nationalInsuranceNumberTenureChange;
                        nationalInsuranceNumberTenureChange = (HashMap<String, String>) tenancyTypeTranistionMatrixETC[1];
                        nationalInsuranceNumberTenureChanges.put(i, nationalInsuranceNumberTenureChange);
                        writeTenancyTypeTransitionMatrix(
                                tenancyTypeTranistionMatrix, year0, month0,
                                year, month, "All", "/Multiple", includeKey,
                                checkPreviousTenure);
                        Object[] tenancyTypeTranistionMatrixGroupedEtc;
                        tenancyTypeTranistionMatrixGroupedEtc = getMultipleTenancyTypeTranistionMatrixGrouped(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                regulatedGroups,
                                unregulatedGroups,
                                nationalInsuranceNumberTenureChanges,
                                include,
                                i);
                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
                        tenancyTypeTranistionMatrixGrouped = (TreeMap<String, TreeMap<String, Integer>>) tenancyTypeTranistionMatrixGroupedEtc[0];
                        HashMap<String, String> nationalInsuranceNumberTenureChangeGrouped;
                        nationalInsuranceNumberTenureChangeGrouped = (HashMap<String, String>) tenancyTypeTranistionMatrixGroupedEtc[1];
                        nationalInsuranceNumberTenureChangesGrouped.put(i, nationalInsuranceNumberTenureChangeGrouped);
                        writeTenancyTypeTransitionMatrixGrouped(
                                tenancyTypeTranistionMatrixGrouped,
                                tenureTypesGrouped, year0, month0, year,
                                month, "All", "/Multiple", includeKey,
                                checkPreviousTenure);
                        previousIndexs.put(includeKey, i);
                    }
                }
            }
        }
    }

    /**
     *
     * @param SHBEFilenames
     * @param startIndex
     * @param tenureTypeGroups
     * @param tenureTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param loadData
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     */
    public void tenancyChanges(
            String[] SHBEFilenames,
            int startIndex,
            TreeMap<String, ArrayList<Integer>> tenureTypeGroups,
            ArrayList<String> tenureTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks) {
        // Load first data
        String filename;
        filename = SHBEFilenames[startIndex];
        HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures;
        nationalInsuranceNumberByTenures = new HashMap<Integer, HashMap<String, Integer>>();
        HashMap<String, Integer> nationalInsuranceNumberByTenure0;
        if (loadData) {
            System.out.print("Loading " + filename + " ...");
            Object[] aSHBEData;
            aSHBEData = loadSHBEData(filename);
            nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) aSHBEData[9];
        } else {
            File f;
            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                    filename);
            System.out.print("Loading " + f + " ...");
            nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                    f);
        }
        System.out.println("...done.");
        nationalInsuranceNumberByTenures.put(startIndex, nationalInsuranceNumberByTenure0);
        // Initialise initFirsts and previousIndexs
        HashMap<String, Boolean> initFirsts;
        initFirsts = new HashMap<String, Boolean>();
        HashMap<String, Integer> previousIndexs;
        previousIndexs = new HashMap<String, Integer>();
        // Init tenureChangesAllIncludes and tenureChangesGroupedAllIncludes
        HashMap<String, HashMap<String, ArrayList<String>>> tenureChangesAllIncludes;
        tenureChangesAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();
        HashMap<String, HashMap<String, ArrayList<String>>> tenureChangesGroupedAllIncludes;
        tenureChangesGroupedAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();
        Iterator<String> ite;
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            tenureChangesAllIncludes.put(
                    includeKey,
                    new HashMap<String, ArrayList<String>>());
            tenureChangesGroupedAllIncludes.put(
                    includeKey,
                    new HashMap<String, ArrayList<String>>());
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            if (include.contains(startIndex)) {
                initFirsts.put(includeKey, true);
                previousIndexs.put(includeKey, startIndex);
            } else {
                initFirsts.put(includeKey, false);
            }
        }
        //Main loop
        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
            HashMap<String, Integer> nationalInsuranceNumberByTenure;
            filename = SHBEFilenames[i];
            // Load next data            
            if (loadData) {
                System.out.print("Loading " + filename + " ...");
                Object[] aSHBEData;
                aSHBEData = loadSHBEData(filename);
                nationalInsuranceNumberByTenure = (HashMap<String, Integer>) aSHBEData[9];
            } else {
                File f;
                f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                        filename);
                System.out.print("Loading " + f + " ...");
                nationalInsuranceNumberByTenure = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                        f);
            }
            System.out.println("...done.");
            nationalInsuranceNumberByTenures.put(
                    i, nationalInsuranceNumberByTenure);
            ite = includes.keySet().iterator();
            while (ite.hasNext()) {
                String includeKey;
                includeKey = ite.next();
                ArrayList<Integer> include;
                include = includes.get(includeKey);
                Boolean initFirst;
                initFirst = initFirsts.get(includeKey);
                HashMap<String, ArrayList<String>> tenureChanges;
                tenureChanges = tenureChangesAllIncludes.get(includeKey);
                HashMap<String, ArrayList<String>> tenureChangesGrouped;
                tenureChangesGrouped = tenureChangesGroupedAllIncludes.get(includeKey);
                if (!initFirst) {
                    if (include.contains(i)) {
                        // Initialise
                        nationalInsuranceNumberByTenures.put(
                                i, nationalInsuranceNumberByTenure);
                        previousIndexs.put(includeKey, i);
                        initFirsts.put(includeKey, true);
                    }
                } else {
                    if (include.contains(i)) {
                        int previousIndex;
                        previousIndex = previousIndexs.get(includeKey);
                        nationalInsuranceNumberByTenure0 = nationalInsuranceNumberByTenures.get(previousIndex);
                        String year0;
                        year0 = DW_SHBE_Handler.getYear(SHBEFilenames[previousIndex]);
                        String month0;
                        month0 = DW_SHBE_Handler.getMonth(SHBEFilenames[previousIndex]);
                        // Set Year and Month variables
                        String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
                        String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
                        // Get TenancyTypeTranistionMatrix
                        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
                        tenancyTypeTranistionMatrix = getTenancyTypeTranistionMatrixAndRecordTenancyChange(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                tenureChanges, year, month,
                                checkPreviousTenure,
                                nationalInsuranceNumberByTenures,
                                i,
                                include);
                        writeTenancyTypeTransitionMatrix(
                                tenancyTypeTranistionMatrix, year0, month0,
                                year, month, "All", "TenureOnly", includeKey,
                                checkPreviousTenure);
                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
                        tenancyTypeTranistionMatrixGrouped = getTenancyTypeTranistionMatrixGroupedAndRecordTenancyChange(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                regulatedGroups,
                                unregulatedGroups,
                                tenureChangesGrouped,
                                year,
                                month,
                                checkPreviousTenure,
                                nationalInsuranceNumberByTenures,
                                i,
                                include);
                        writeTenancyTypeTransitionMatrixGrouped(
                                tenancyTypeTranistionMatrixGrouped,
                                tenureTypesGrouped, year0, month0, year,
                                month, "All", "TenureOnly", includeKey, checkPreviousTenure);
                        previousIndexs.put(includeKey, i);
                    }
                }
            }
            nationalInsuranceNumberByTenure0 = nationalInsuranceNumberByTenure;
        }
        File dir;
        dir = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                "All",
                checkPreviousTenure);
        TreeMap<String, TreeMap<String, Integer>> allTrans;
        // Calculate and write out Tenure Type transition frequencies
        System.out.println("Calculate and write out Tenure Type transition frequencies");
        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            TreeMap<String, Integer> trans;
            trans = new TreeMap<String, Integer>();
            allTrans.put(includeKey, trans);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            int max = Integer.MIN_VALUE;
            HashMap<String, ArrayList<String>> tenureChanges;
            tenureChanges = tenureChangesAllIncludes.get(includeKey);
            Iterator<String> ite2;
            ite2 = tenureChanges.keySet().iterator();
            while (ite2.hasNext()) {
                String nino = ite2.next();
                ArrayList<String> transitions;
                transitions = tenureChanges.get(nino);
                max = Math.max(max, transitions.size());
                String out;
                out = "";
                Iterator<String> ite3;
                ite3 = transitions.iterator();
                boolean doneFirst = false;
                while (ite3.hasNext()) {
                    String t;
                    t = ite3.next();
                    String[] splitT;
                    splitT = t.split(":");
                    if (reportTenancyTransitionBreaks) {
                        if (!doneFirst) {
                            out += splitT[0];
                            doneFirst = true;
                        } else {
                            out += ", " + splitT[0];
                        }
                    } else {
                        if (!doneFirst) {
                            if (!splitT[0].contains("-999")) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else {
                            if (!splitT[0].contains("-999")) {
                                out += ", " + splitT[0];
                            }
                        }
                    }
                }
                if (!out.isEmpty()) {
                    if (trans.containsKey(out)) {
                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
                    } else {
                        trans.put(out, 1);
                    }
                }
//                if (transitions.size() == max) {
//                    out = "Transitions";
//                    ite3 = transitions.iterator();
//                    while (ite3.hasNext()) {
//                        out += ", " + ite3.next();
//                    }
//                    System.out.println(out);
//                }
            }
            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
        }
        writeTransitionFrequencies(
                allTrans,
                dir,
                "Frequencies.txt",
                reportTenancyTransitionBreaks);
        // Calculate and write out Grouped Tenure Type transition frequencies
        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            TreeMap<String, Integer> trans;
            trans = new TreeMap<String, Integer>();
            allTrans.put(includeKey, trans);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            int max = Integer.MIN_VALUE;
            HashMap<String, ArrayList<String>> tenureChanges;
            tenureChanges = tenureChangesGroupedAllIncludes.get(includeKey);
            Iterator<String> ite2;
            ite2 = tenureChanges.keySet().iterator();
            while (ite2.hasNext()) {
                String nino = ite2.next();
                ArrayList<String> transitions;
                transitions = tenureChanges.get(nino);
                max = Math.max(max, transitions.size());
                String out;
                out = "";
                Iterator<String> ite3;
                ite3 = transitions.iterator();
                boolean doneFirst = false;
                while (ite3.hasNext()) {
                    String t;
                    t = ite3.next();
                    String[] splitT;
                    splitT = t.split(":");
                    if (reportTenancyTransitionBreaks) {
                        if (!doneFirst) {
                            out += splitT[0];
                            doneFirst = true;
                        } else {
                            out += ", " + splitT[0];
                        }
                    } else {
                        if (!doneFirst) {
                            if (!splitT[0].contains("-999")) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else {
                            if (!splitT[0].contains("-999")) {
                                out += ", " + splitT[0];
                            }
                        }
                    }
                }
                if (!out.isEmpty()) {
                    if (trans.containsKey(out)) {
                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
                    } else {
                        trans.put(out, 1);
                    }
                }
//                if (transitions.size() == max) {
//                    out = "Transitions Grouped";
//                    ite3 = transitions.iterator();
//                    while (ite3.hasNext()) {
//                        out += ", " + ite3.next();
//                    }
//                    System.out.println(out);
//                }
            }
            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
        }
        writeTransitionFrequencies(
                allTrans,
                dir,
                "FrequenciesGrouped.txt",
                reportTenancyTransitionBreaks);
    }

    private void writeTransitionFrequencies(
            TreeMap<String, TreeMap<String, Integer>> allTrans,
            File dir,
            String name,
            boolean reportTenancyTransitionBreaks) {
        Iterator<String> ite;
        ite = allTrans.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            PrintWriter pw;
            pw = getFrequencyPrintWriter(
                    dir,
                    name,
                    includeKey,
                    reportTenancyTransitionBreaks);
            pw.println("Count, Type");
            TreeMap<String, Integer> trans;
            trans = allTrans.get(includeKey);
            Iterator<String> ite2;
            ite2 = trans.keySet().iterator();
            while (ite2.hasNext()) {
                String type;
                type = ite2.next();
                Integer count;
                count = trans.get(type);
                pw.println(count + ", " + type);
            }
            pw.flush();
            pw.close();
        }
    }

    private PrintWriter getFrequencyPrintWriter(
            File dir,
            String name,
            String includeKey,
            boolean reportTenancyTransitionBreaks) {
        PrintWriter result;
        File dir2;
        dir2 = new File(
                dir,
                includeKey);
        dir2 = new File(
                dir2,
                "Frequencies");
        if (reportTenancyTransitionBreaks) {
            dir2 = new File(
                    dir2,
                    "IncludingTenancyTransitionBreaks");
        } else {
            dir2 = new File(
                    dir2,
                    "NotIncludingTenancyTransitionBreaks");
        }
        dir2.mkdirs();
        File f;
        f = new File(
                dir2,
                name);
        result = Generic_StaticIO.getPrintWriter(f, false);
        return result;
    }

    /**
     *
     * @param postcodeHandler
     * @param SHBEFilenames
     * @param startIndex
     * @param tenureTypeGroups
     * @param tenureTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param loadData
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     */
    public void tenancyTypeAndPostcodeChanges(
            Generic_UKPostcode_Handler postcodeHandler,
            String[] SHBEFilenames,
            int startIndex,
            TreeMap<String, ArrayList<Integer>> tenureTypeGroups,
            ArrayList<String> tenureTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            boolean loadData,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks) {
        // Load first data
        String filename;
        filename = SHBEFilenames[startIndex];
        HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByPostcodes;
        nationalInsuranceNumberByPostcodes = new HashMap<Integer, HashMap<String, String>>();
        HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures;
        nationalInsuranceNumberByTenures = new HashMap<Integer, HashMap<String, Integer>>();
        HashMap<String, String> nationalInsuranceNumberByPostcode0;
        HashMap<String, Integer> nationalInsuranceNumberByTenure0;
        if (loadData) {
            Object[] aSHBEData;
            aSHBEData = loadSHBEData(filename);
            nationalInsuranceNumberByPostcode0 = (HashMap<String, String>) aSHBEData[8];
            nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) aSHBEData[9];
        } else {
            File f;
            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToPostcodeLookupFile(
                    filename);
            nationalInsuranceNumberByPostcode0 = (HashMap<String, String>) Generic_StaticIO.readObject(
                    f);
            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                    filename);
            nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                    f);
        }
        nationalInsuranceNumberByPostcodes.put(startIndex, nationalInsuranceNumberByPostcode0);
        nationalInsuranceNumberByTenures.put(startIndex, nationalInsuranceNumberByTenure0);

        HashMap<String, Boolean> initFirsts;
        initFirsts = new HashMap<String, Boolean>();
        HashMap<String, Integer> previousIndexs;
        previousIndexs = new HashMap<String, Integer>();

        HashMap<String, HashMap<String, ArrayList<String>>> tenureChangesAllIncludes;
        tenureChangesAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();
        HashMap<String, HashMap<String, ArrayList<String>>> tenureChangesGroupedAllIncludes;
        tenureChangesGroupedAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();

        Iterator<String> ite;
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            tenureChangesAllIncludes.put(
                    includeKey,
                    new HashMap<String, ArrayList<String>>());
            tenureChangesGroupedAllIncludes.put(
                    includeKey,
                    new HashMap<String, ArrayList<String>>());
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            if (include.contains(startIndex)) {
                initFirsts.put(includeKey, true);
                previousIndexs.put(includeKey, startIndex);
            } else {
                initFirsts.put(includeKey, false);
            }
        }
        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
            HashMap<String, String> nationalInsuranceNumberByPostcode;
            HashMap<String, Integer> nationalInsuranceNumberByTenure;
            filename = SHBEFilenames[i];
            // Load next data            
            if (loadData) {
                Object[] aSHBEData;
                aSHBEData = loadSHBEData(filename);
                nationalInsuranceNumberByPostcode = (HashMap<String, String>) aSHBEData[8];
                nationalInsuranceNumberByTenure = (HashMap<String, Integer>) aSHBEData[9];
            } else {
                File f;
                f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToPostcodeLookupFile(
                        filename);
                nationalInsuranceNumberByPostcode = (HashMap<String, String>) Generic_StaticIO.readObject(
                        f);
                f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
                        filename);
                nationalInsuranceNumberByTenure = (HashMap<String, Integer>) Generic_StaticIO.readObject(
                        f);
            }
            nationalInsuranceNumberByPostcodes.put(i, nationalInsuranceNumberByPostcode);
            nationalInsuranceNumberByTenures.put(i, nationalInsuranceNumberByTenure);
            ite = includes.keySet().iterator();
            while (ite.hasNext()) {
                String includeKey;
                includeKey = ite.next();
                ArrayList<Integer> include;
                include = includes.get(includeKey);
                Boolean initFirst;
                initFirst = initFirsts.get(includeKey);
                HashMap<String, ArrayList<String>> tenureChanges;
                tenureChanges = tenureChangesAllIncludes.get(includeKey);
                HashMap<String, ArrayList<String>> tenureChangesGrouped;
                tenureChangesGrouped = tenureChangesGroupedAllIncludes.get(includeKey);
                if (!initFirst) {
                    if (include.contains(i)) {
                        // Initialise
                        nationalInsuranceNumberByPostcodes.put(i, nationalInsuranceNumberByPostcode);
                        nationalInsuranceNumberByTenures.put(i, nationalInsuranceNumberByTenure);
                        previousIndexs.put(includeKey, i);
                        initFirsts.put(includeKey, true);
                    }
                } else {
                    if (include.contains(i)) {
                        int previousIndex;
                        previousIndex = previousIndexs.get(includeKey);
                        nationalInsuranceNumberByPostcode0 = nationalInsuranceNumberByPostcodes.get(previousIndex);
                        nationalInsuranceNumberByTenure0 = nationalInsuranceNumberByTenures.get(previousIndex);
                        String year0;
                        year0 = DW_SHBE_Handler.getYear(SHBEFilenames[previousIndex]);
                        String month0;
                        month0 = DW_SHBE_Handler.getMonth(SHBEFilenames[previousIndex]);
                        // Set Year and Month variables
                        String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
                        String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
                        // Get TenancyTypeTranistionMatrix
                        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
                        tenancyTypeTranistionMatrix = getTenancyTypeTranistionMatrixAndMaybeWritePostcodeChanges(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                nationalInsuranceNumberByPostcode0,
                                nationalInsuranceNumberByPostcode,
                                postcodeHandler,
                                postcodeChange,
                                tenureChanges,
                                year,
                                month,
                                checkPreviousTenure,
                                nationalInsuranceNumberByTenures,
                                checkPreviousPostcode,
                                nationalInsuranceNumberByPostcodes,
                                i,
                                include);
                        String type2;
                        if (postcodeChange) {
                            type2 = "/PostcodeChanged/";
                        } else {
                            type2 = "/PostcodeUnchanged/";
                        }
                        writeTenancyTypeTransitionMatrix(
                                tenancyTypeTranistionMatrix, year0, month0,
                                year, month, "All", type2, includeKey,
                                checkPreviousTenure);
                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
                        tenancyTypeTranistionMatrixGrouped = getTenancyTypeTranistionMatrixGroupedAndMaybeWritePostcodeChanges(
                                nationalInsuranceNumberByTenure0,
                                nationalInsuranceNumberByTenure,
                                nationalInsuranceNumberByPostcode0,
                                nationalInsuranceNumberByPostcode,
                                postcodeHandler,
                                regulatedGroups,
                                unregulatedGroups,
                                tenureChangesGrouped,
                                year,
                                month,
                                checkPreviousTenure,
                                nationalInsuranceNumberByTenures,
                                postcodeChange,
                                checkPreviousPostcode,
                                nationalInsuranceNumberByPostcodes,
                                i,
                                include);
                        writeTenancyTypeTransitionMatrixGrouped(
                                tenancyTypeTranistionMatrixGrouped,
                                tenureTypesGrouped, year0, month0, year, month,
                                "All", type2, includeKey, checkPreviousTenure);
                        previousIndexs.put(includeKey, i);
                    }
                }
            }
            nationalInsuranceNumberByPostcode0 = nationalInsuranceNumberByPostcode;
            nationalInsuranceNumberByTenure0 = nationalInsuranceNumberByTenure;
        }
        File dir;
        dir = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                "All",
                checkPreviousTenure);
        if (postcodeChange) {
            dir = new File(
                    dir,
                    "/PostcodeChanged/");
        } else {
            dir = new File(
                    dir,
                    "/PostcodeUnchanged/");
        }
        TreeMap<String, TreeMap<String, Integer>> allTrans;
        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
        // Calculate and write out Tenure Type transition frequencies
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            TreeMap<String, Integer> trans;
            trans = new TreeMap<String, Integer>();
            allTrans.put(includeKey, trans);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            int max = Integer.MIN_VALUE;
            HashMap<String, ArrayList<String>> tenureChanges;
            tenureChanges = tenureChangesAllIncludes.get(includeKey);
            Iterator<String> ite2;
            ite2 = tenureChanges.keySet().iterator();
            while (ite2.hasNext()) {
                String nino = ite2.next();
                ArrayList<String> transitions;
                transitions = tenureChanges.get(nino);
                max = Math.max(max, transitions.size());
                String out;
                out = "";
                Iterator<String> ite3;
                ite3 = transitions.iterator();
                boolean doneFirst = false;
                while (ite3.hasNext()) {
                    String t;
                    t = ite3.next();
                    String[] splitT;
                    splitT = t.split(":");
                    if (reportTenancyTransitionBreaks) {
                        if (!doneFirst) {
                            out += splitT[0];
                            doneFirst = true;
                        } else {
                            out += ", " + splitT[0];
                        }
                    } else {
                        if (!doneFirst) {
                            if (!splitT[0].contains("-999")) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else {
                            if (!splitT[0].contains("-999")) {
                                out += ", " + splitT[0];
                            }
                        }
                    }
                }
                if (!out.isEmpty()) {
                    if (trans.containsKey(out)) {
                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
                    } else {
                        trans.put(out, 1);
                    }
                }
//                if (transitions.size() == max) {
//                    out = "Transitions";
//                    ite3 = transitions.iterator();
//                    while (ite3.hasNext()) {
//                        out += ", " + ite3.next();
//                    }
//                    System.out.println(out);
//                }
            }
            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
        }
        writeTransitionFrequencies(
                allTrans,
                dir,
                "Frequencies.txt",
                reportTenancyTransitionBreaks);
        // Calculate and write out Grouped Tenure Type transition frequencies
        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
            TreeMap<String, Integer> trans;
            trans = new TreeMap<String, Integer>();
            allTrans.put(includeKey, trans);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            int max = Integer.MIN_VALUE;
            HashMap<String, ArrayList<String>> tenureChanges;
            tenureChanges = tenureChangesGroupedAllIncludes.get(includeKey);
            Iterator<String> ite2;
            ite2 = tenureChanges.keySet().iterator();
            while (ite2.hasNext()) {
                String nino = ite2.next();
                ArrayList<String> transitions;
                transitions = tenureChanges.get(nino);
                max = Math.max(max, transitions.size());
                String out;
                out = "";
                Iterator<String> ite3;
                ite3 = transitions.iterator();
                boolean doneFirst = false;
                while (ite3.hasNext()) {
                    String t;
                    t = ite3.next();
                    String[] splitT;
                    splitT = t.split(":");
                    if (reportTenancyTransitionBreaks) {
                        if (!doneFirst) {
                            out += splitT[0];
                            doneFirst = true;
                        } else {
                            out += ", " + splitT[0];
                        }
                    } else {
                        if (!doneFirst) {
                            if (!splitT[0].contains("-999")) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else {
                            if (!splitT[0].contains("-999")) {
                                out += ", " + splitT[0];
                            }
                        }
                    }
                }
                if (!out.isEmpty()) {
                    if (trans.containsKey(out)) {
                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
                    } else {
                        trans.put(out, 1);
                    }
                }
//                if (transitions.size() == max) {
//                    out = "Transitions Grouped";
//                    ite3 = transitions.iterator();
//                    while (ite3.hasNext()) {
//                        out += ", " + ite3.next();
//                    }
//                    System.out.println(out);
//                }
            }
            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
        }
        writeTransitionFrequencies(
                allTrans,
                dir,
                "FrequenciesGrouped.txt",
                reportTenancyTransitionBreaks);
    }

    /**
     * TODO: We want to be able to distinguish those claimants that have been:
     * continually claiming; claiming for at least 3 months; claiming for at
     * least 3 months, then stopped, then started claiming again.
     *
     * @param lookupsFromPostcodeToLevelCode
     * @param SHBEFilenames
     * @param startIndex
     * @param claimantTypes
     * @param tenureTypeGroups
     * @param tenureTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param types type = NewEntrant type = Stable type = Churn
     * @param distanceTypes
     * @param distances
     */
    public void aggregateClaimants(
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames,
            int startIndex,
            ArrayList<String> claimantTypes,
            TreeMap<String, ArrayList<Integer>> tenureTypeGroups,
            ArrayList<String> tenureTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances) {
        TreeMap<String, File> outputDirs;
        outputDirs = DW_Files.getGeneratedSHBELevelDirsTreeMap(levels);

        // Declare iterators
        Iterator<String> claimantTypesIte;
        Iterator<String> tenureIte;
        Iterator<String> levelsIte;
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;

        // Load first data
        Object[] SHBEData0;
        SHBEData0 = loadSHBEData(SHBEFilenames[startIndex]);
        HashMap<String, String> nationalInsuranceNumberByPostcode0;
        nationalInsuranceNumberByPostcode0 = (HashMap<String, String>) SHBEData0[8];
        HashMap<String, Integer> nationalInsuranceNumberByTenure0;
        nationalInsuranceNumberByTenure0 = (HashMap<String, Integer>) SHBEData0[9];

        String year0;
        year0 = DW_SHBE_Handler.getYear(SHBEFilenames[startIndex]);
        String month0;
        month0 = DW_SHBE_Handler.getMonth(SHBEFilenames[startIndex]);

        // Get Top 10 areas
        TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>> yearMonthClaimantTypeTenureLevelTypeCountAreas;
        yearMonthClaimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>>();

        TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>> yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas;
        yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>>();

        // Initialise claimantNationalInsuranceNumberIndexes
        ArrayList<HashSet<String>> claimantNationalInsuranceNumberIndexes;
        claimantNationalInsuranceNumberIndexes = new ArrayList<HashSet<String>>();
        if (true) {
            HashSet<String> claimantNationalInsuranceNumberID_HashSet;
            claimantNationalInsuranceNumberID_HashSet = (HashSet<String>) SHBEData0[2];
            claimantNationalInsuranceNumberIndexes.add(claimantNationalInsuranceNumberID_HashSet);
        }
        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {

            // Load next data
            Object[] SHBEData1;
            SHBEData1 = loadSHBEData(SHBEFilenames[i]);
            HashMap<String, String> nationalInsuranceNumberByPostcode1;
            nationalInsuranceNumberByPostcode1 = (HashMap<String, String>) SHBEData1[8];
            HashMap<String, Integer> nationalInsuranceNumberByTenure1;
            nationalInsuranceNumberByTenure1 = (HashMap<String, Integer>) SHBEData1[9];

//            // Set Year and Month variables
            String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
            String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
            String yearMonth = year + month;
            String lastMonth_yearMonth;
            lastMonth_yearMonth = getLastMonth_yearMonth(
                    year,
                    month,
                    SHBEFilenames,
                    i,
                    startIndex);
            String lastYear_yearMonth;
            lastYear_yearMonth = getLastYear_yearMonth(
                    year,
                    month,
                    SHBEFilenames,
                    i,
                    startIndex);
            if (true) {
                HashSet<String> claimantNationalInsuranceNumberID_HashSet;
                claimantNationalInsuranceNumberID_HashSet = (HashSet<String>) SHBEData1[2];
                claimantNationalInsuranceNumberIndexes.add(claimantNationalInsuranceNumberID_HashSet);
            }
            TreeMap<String, DW_SHBE_Record> records0;
            records0 = (TreeMap<String, DW_SHBE_Record>) SHBEData0[0];
            TreeMap<String, DW_SHBE_Record> records1;
            records1 = (TreeMap<String, DW_SHBE_Record>) SHBEData1[0];
            /* Initialise A:
             * output directories;
             * claimantTypeTenureLevelTypeDirs;
             * tenureLevelTypeDistanceDirs;
             * tenureTypeAreaCount;
             * tenureTypeDistanceAreaCount.
             */
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
            claimantTypeTenureLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>>();
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>> claimantTypeTenureLevelTypeDistanceDirs;
            claimantTypeTenureLevelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>>();
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
            claimantTypeTenureLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>>();
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts;
            claimantTypeTenureLevelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>>();
            /* Initialise B:
             * claimantTypeLevelTypeTenureCounts;
             * claimantTypeLevelTypeDistanceTenureCounts;
             */
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
            claimantTypeTenureLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>>();
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts;
            claimantTypeTenureLevelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>>();
            // claimantTypeLoop
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType;
                claimantType = claimantTypesIte.next();
                // Initialise Dirs
                TreeMap<String, TreeMap<String, TreeMap<String, File>>> tenureLevelTypeDirs;
                tenureLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, File>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>> tenureLevelTypeDistanceDirs;
                tenureLevelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>();
                // Initialise AreaCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> tenureLevelTypeAreaCounts;
                tenureLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>> tenureLevelTypeDistanceAreaCounts;
                tenureLevelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>();
                // Initialise TenureCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> tenureLevelTypeTenureCounts;
                tenureLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>> tenureLevelTypeDistanceTenureCounts;
                tenureLevelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>();
                tenureIte = tenureTypeGroups.keySet().iterator();
                while (tenureIte.hasNext()) {
                    String tenure = tenureIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<String, File>> levelTypeDirs;
                    levelTypeDirs = new TreeMap<String, TreeMap<String, File>>();
                    TreeMap<String, TreeMap<String, TreeMap<Double, File>>> levelTypeDistanceDirs;
                    levelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<Double, File>>>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts;
                    levelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>();
                    TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>> levelTypeDistanceAreaCounts;
                    levelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts;
                    levelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>();
                    TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>> levelTypeDistanceTenureCounts;
                    levelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>();
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        String level = levelsIte.next();
                        // Initialise Dirs
                        File outputDir = outputDirs.get(level);
                        TreeMap<String, File> typeDirs;
                        typeDirs = new TreeMap<String, File>();
                        TreeMap<String, TreeMap<Double, File>> typeDistanceDirs;
                        typeDistanceDirs = new TreeMap<String, TreeMap<Double, File>>();
                        // Initialise AreaCounts
                        TreeMap<String, TreeMap<String, Integer>> typeAreaCounts;
                        typeAreaCounts = new TreeMap<String, TreeMap<String, Integer>>();
                        TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>> typeDistanceAreaCounts;
                        typeDistanceAreaCounts = new TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>();
                        // Initialise TenureCounts
                        TreeMap<String, TreeMap<Integer, Integer>> typeTenureCounts;
                        typeTenureCounts = new TreeMap<String, TreeMap<Integer, Integer>>();
                        TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>> typeDistanceTenureCounts;
                        typeDistanceTenureCounts = new TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>();
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            // Initialise Dirs
                            File dir = new File(
                                    outputDir,
                                    type);
                            dir = new File(
                                    dir,
                                    claimantType);
                            dir = new File(
                                    dir,
                                    tenure);
                            dir.mkdirs();
                            typeDirs.put(type, dir);
                            // Initialise AreaCounts
                            TreeMap<String, Integer> areaCount;
                            areaCount = new TreeMap<String, Integer>();
                            typeAreaCounts.put(type, areaCount);
                            // Initialise TenureCounts
                            TreeMap<Integer, Integer> tenureCounts;
                            tenureCounts = new TreeMap<Integer, Integer>();
                            typeTenureCounts.put(type, tenureCounts);
                        }
                        levelTypeDirs.put(level, typeDirs);
                        levelTypeAreaCounts.put(level, typeAreaCounts);
                        levelTypeTenureCounts.put(level, typeTenureCounts);
                        distanceTypesIte = distanceTypes.iterator();
                        while (distanceTypesIte.hasNext()) {
                            String distanceType;
                            distanceType = distanceTypesIte.next();
                            // Initialise Dirs
                            TreeMap<Double, File> distanceDirs;
                            distanceDirs = new TreeMap<Double, File>();
                            // Initialise AreaCounts
                            TreeMap<Double, TreeMap<String, Integer>> distanceAreaCounts;
                            distanceAreaCounts = new TreeMap<Double, TreeMap<String, Integer>>();
                            // Initialise TenureCounts
                            TreeMap<Double, TreeMap<Integer, Integer>> distanceTenureCounts;
                            distanceTenureCounts = new TreeMap<Double, TreeMap<Integer, Integer>>();
                            distancesIte = distances.iterator();
                            while (distancesIte.hasNext()) {
                                double distance = distancesIte.next();
                                // Initialise Dirs
                                File dir = new File(
                                        outputDir,
                                        distanceType);
                                dir = new File(
                                        dir,
                                        claimantType);
                                dir = new File(
                                        dir,
                                        tenure);
                                dir = new File(
                                        dir,
                                        "" + distance);
                                dir.mkdirs();
                                distanceDirs.put(distance, dir);
                                // Initialise AreaCounts
                                TreeMap<String, Integer> areaCounts;
                                areaCounts = new TreeMap<String, Integer>();
                                distanceAreaCounts.put(distance, areaCounts);
                                // Initialise TenureCounts
                                TreeMap<Integer, Integer> tenureCounts;
                                tenureCounts = new TreeMap<Integer, Integer>();
                                distanceTenureCounts.put(distance, tenureCounts);
                            }
                            typeDistanceDirs.put(distanceType, distanceDirs);
                            typeDistanceAreaCounts.put(distanceType, distanceAreaCounts);
                            typeDistanceTenureCounts.put(distanceType, distanceTenureCounts);
                        }
                        levelTypeDistanceDirs.put(level, typeDistanceDirs);
                        levelTypeDistanceAreaCounts.put(level, typeDistanceAreaCounts);
                        levelTypeDistanceTenureCounts.put(level, typeDistanceTenureCounts);
                    }
                    tenureLevelTypeDirs.put(tenure, levelTypeDirs);
                    tenureLevelTypeDistanceDirs.put(tenure, levelTypeDistanceDirs);
                    tenureLevelTypeAreaCounts.put(tenure, levelTypeAreaCounts);
                    tenureLevelTypeDistanceAreaCounts.put(tenure, levelTypeDistanceAreaCounts);
                    tenureLevelTypeTenureCounts.put(tenure, levelTypeTenureCounts);
                    tenureLevelTypeDistanceTenureCounts.put(tenure, levelTypeDistanceTenureCounts);
                }
                claimantTypeTenureLevelTypeDirs.put(claimantType, tenureLevelTypeDirs);
                claimantTypeTenureLevelTypeDistanceDirs.put(claimantType, tenureLevelTypeDistanceDirs);
                claimantTypeTenureLevelTypeAreaCounts.put(claimantType, tenureLevelTypeAreaCounts);
                claimantTypeTenureLevelTypeDistanceAreaCounts.put(claimantType, tenureLevelTypeDistanceAreaCounts);
                claimantTypeTenureLevelTypeTenureCounts.put(claimantType, tenureLevelTypeTenureCounts);
                claimantTypeTenureLevelTypeDistanceTenureCounts.put(claimantType, tenureLevelTypeDistanceTenureCounts);
            }
            // Initialise levelUnexpectedCounts
            TreeMap<String, TreeMap<String, Integer>> levelUnexpectedCounts;
            levelUnexpectedCounts = new TreeMap<String, TreeMap<String, Integer>>();
            levelsIte = levels.iterator();
            while (levelsIte.hasNext()) {
                String level;
                level = levelsIte.next();
                TreeMap<String, Integer> unexpectedCounts;
                unexpectedCounts = new TreeMap<String, Integer>();
                levelUnexpectedCounts.put(level, unexpectedCounts);
            }
            // Iterator over records
            Iterator<String> recordsIte;
            recordsIte = records1.keySet().iterator();
            while (recordsIte.hasNext()) {
                String claimID = recordsIte.next();
                DW_SHBE_D_Record DRecord1 = records1.get(claimID).getDRecord();
                String postcode1 = DRecord1.getClaimantsPostcode();
                Integer tenancyType1 = DRecord1.getTenancyType();
                tenureIte = tenureTypeGroups.keySet().iterator();
                while (tenureIte.hasNext()) {
                    String tenure = tenureIte.next();
                    ArrayList<Integer> tenureTypes;
                    tenureTypes = tenureTypeGroups.get(tenure);
                    if (tenureTypes.contains(tenancyType1)) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level = levelsIte.next();
                            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                            tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                            TreeMap<String, Integer> unexpectedCounts;
                            unexpectedCounts = levelUnexpectedCounts.get(level);
                            String housingBenefitClaimReferenceNumber1;
                            housingBenefitClaimReferenceNumber1 = DRecord1.getHousingBenefitClaimReferenceNumber();
                            String claimantNINO1 = DRecord1.getClaimantsNationalInsuranceNumber();
                            String claimantType;
                            if (housingBenefitClaimReferenceNumber1 == null) {
                                claimantType = "CTB";
                            } else {
                                if (housingBenefitClaimReferenceNumber1.isEmpty()) {
                                    claimantType = "CTB";
                                } else {
                                    claimantType = "HB";
                                }
                            }
                            Integer tenancyType = DRecord1.getTenancyType();
                            if (postcode1 != null) {
                                String areaCode;
                                areaCode = getAreaCode(
                                        level,
                                        postcode1,
                                        tLookupFromPostcodeToLevelCode);
                                String type;
                                type = "All";
                                if (types.contains(type)) {
                                    addToResult(
                                            claimantTypeTenureLevelTypeAreaCounts,
                                            claimantTypeTenureLevelTypeTenureCounts,
                                            areaCode,
                                            claimantType,
                                            tenure,
                                            level,
                                            type,
                                            tenancyType);
                                }
                                if (areaCode != null) {
                                    DW_SHBE_Record record0 = records0.get(claimID);
                                    String postcode0;
                                    if (record0 == null) {
//                                        //This is a new entrant to the data
//                                        type = "NewEntrant";
                                        // If this claimantNINO has never been seen before it is an OnFlow
                                        boolean hasBeenSeenBefore;
                                        hasBeenSeenBefore = getHasClaimantBeenSeenBefore(
                                                claimantNINO1,
                                                i,
                                                claimantNationalInsuranceNumberIndexes);
                                        if (!hasBeenSeenBefore) {
                                            type = "OnFlow";
                                            if (types.contains(type)) {
                                                addToResult(
                                                        claimantTypeTenureLevelTypeAreaCounts,
                                                        claimantTypeTenureLevelTypeTenureCounts,
                                                        areaCode,
                                                        claimantType,
                                                        tenure,
                                                        level,
                                                        type,
                                                        tenancyType);
                                            }
                                        } else {
                                            // If this claimantNINO has been seen before it is a ReturnFlow
                                            type = "ReturnFlow";
                                            if (types.contains(type)) {
                                                addToResult(
                                                        claimantTypeTenureLevelTypeAreaCounts,
                                                        claimantTypeTenureLevelTypeTenureCounts,
                                                        areaCode,
                                                        claimantType,
                                                        tenure,
                                                        level,
                                                        type,
                                                        tenancyType);
                                            }
// Here we could also try to work out for those Return flows, have any moved from previous claim postcode or changed tenure.
//                                addToType(type, types, claimantCountsByArea, areaCode);
//                                type = "ReturnFlowMoved";
//                                addToType(type, types, claimantCountsByArea, areaCode);
//                                type = "ReturnFlowNotmoved";
//                                addToType(type, types, claimantCountsByArea, areaCode);
//                                type = "ReturnFlowMovedAndChangedTenure";
//                                addToType(type, types, claimantCountsByArea, areaCode);
                                        }
                                    } else {
                                        DW_SHBE_D_Record DRecord0 = record0.getDRecord();
                                        postcode0 = DRecord0.getClaimantsPostcode();
                                        if (postcode0 == null) {
                                            // Unknown
                                            type = "Unknown";
                                            if (types.contains(type)) {
                                                addToResult(
                                                        claimantTypeTenureLevelTypeAreaCounts,
                                                        claimantTypeTenureLevelTypeTenureCounts,
                                                        areaCode,
                                                        claimantType,
                                                        tenure,
                                                        level,
                                                        type,
                                                        tenancyType);
                                            }
                                        } else {
//areaCode0 used to be used to determine WithinChurn, but now a distance is calculated
//                                        String areaCode0;
//                                        areaCode0 = getAreaCode(
//                                                level,
//                                                postcode0,
//                                                tLookupFromPostcodeToLevelCode);
                                        /*
                                             * There is an issue here as it seems that sometimes a postcode is misrecorded 
                                             * initially and is then corrected. Some thought is needed about how to identify
                                             * and deal with this and discern if this has any significant effect on the 
                                             * results.
                                             */
                                            if (postcode0.equalsIgnoreCase(postcode1)) {
                                                // Stable
                                                type = "Stable";
                                                if (types.contains(type)) {
                                                    addToResult(
                                                            claimantTypeTenureLevelTypeAreaCounts,
                                                            claimantTypeTenureLevelTypeTenureCounts,
                                                            areaCode,
                                                            claimantType,
                                                            tenure,
                                                            level,
                                                            type,
                                                            tenancyType);
                                                }
                                            } else {
                                                // AllInChurn
                                                type = "AllInChurn";
                                                if (types.contains(type)) {
                                                    addToResult(
                                                            claimantTypeTenureLevelTypeAreaCounts,
                                                            claimantTypeTenureLevelTypeTenureCounts,
                                                            areaCode,
                                                            claimantType,
                                                            tenure,
                                                            level,
                                                            type,
                                                            tenancyType);
                                                }
                                                // AllOutChurn
                                                type = "AllOutChurn";
                                                if (types.contains(type)) {
                                                    addToResult(
                                                            claimantTypeTenureLevelTypeAreaCounts,
                                                            claimantTypeTenureLevelTypeTenureCounts,
                                                            areaCode,
                                                            claimantType,
                                                            tenure,
                                                            level,
                                                            type,
                                                            tenancyType);
                                                }
                                                double distance;
                                                distance = DW_Postcode_Handler.getDistanceBetweenPostcodes(
                                                        postcode0,
                                                        postcode1);
                                                Iterator<Double> ite3;
                                                ite3 = distances.iterator();
                                                while (ite3.hasNext()) {
                                                    double distanceThreshold = ite3.next();
                                                    if (distance > distanceThreshold) {
                                                        // InDistanceChurn
                                                        type = "InDistanceChurn";
                                                        if (distanceTypes.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                    claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    tenure,
                                                                    level,
                                                                    type,
                                                                    tenancyType,
                                                                    distanceThreshold);
                                                        }
                                                        // OutDistanceChurn
                                                        type = "OutDistanceChurn";
                                                        if (distanceTypes.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                    claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    tenure,
                                                                    level,
                                                                    type,
                                                                    tenancyType,
                                                                    distanceThreshold);
                                                        }
                                                    } else {
                                                        // WithinDistanceChurn
                                                        type = "WithinDistanceChurn";
                                                        if (distanceTypes.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                    claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    tenure,
                                                                    level,
                                                                    type,
                                                                    tenancyType,
                                                                    distanceThreshold);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //System.out.println("No Census code for postcode: " + postcode1);
                                    String firstPartPostcode;
                                    firstPartPostcode = postcode1.trim().split(" ")[0];
                                    Generic_Collections.addToTreeMapStringInteger(
                                            unexpectedCounts, firstPartPostcode, 1);
                                }
                            } else {
                                Generic_Collections.addToTreeMapStringInteger(
                                        unexpectedCounts, "null", 1);
                            }
                        }
                    }
                }
            }
            // Write out results
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
            claimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>();
            yearMonthClaimantTypeTenureLevelTypeCountAreas.put(yearMonth, claimantTypeTenureLevelTypeCountAreas);
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>();
            yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(yearMonth, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
            // claimantTypeLoop
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType = claimantTypesIte.next();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> tenureLevelTypeCountAreas;
                tenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>();
                claimantTypeTenureLevelTypeCountAreas.put(claimantType, tenureLevelTypeCountAreas);
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>> tenureLevelDistanceTypeDistanceCountAreas;
                tenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>();
                claimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(claimantType, tenureLevelDistanceTypeDistanceCountAreas);
                tenureIte = tenureTypeGroups.keySet().iterator();
                while (tenureIte.hasNext()) {
                    String tenure = tenureIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                    levelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>();
                    tenureLevelTypeCountAreas.put(tenure, levelTypeCountAreas);
                    TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>> levelDistanceTypeDistanceCountAreas;
                    levelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>();
                    tenureLevelDistanceTypeDistanceCountAreas.put(tenure, levelDistanceTypeDistanceCountAreas);
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        String level = levelsIte.next();
                        TreeMap<String, TreeMap<Integer, TreeSet<String>>> typeCountAreas;
                        typeCountAreas = new TreeMap<String, TreeMap<Integer, TreeSet<String>>>();
                        levelTypeCountAreas.put(level, typeCountAreas);
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            TreeMap<String, Integer> areaCounts;
                            TreeMap<Integer, TreeSet<String>> lastYearCountAreas;
                            TreeMap<Integer, TreeSet<String>> lastMonthCountAreas;
                            TreeMap<Integer, Integer> tenureCounts;
                            File dir;
                            areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenure).get(level).get(type);
                            if (lastYear_yearMonth != null) {
                                lastYearCountAreas = yearMonthClaimantTypeTenureLevelTypeCountAreas.get(lastYear_yearMonth).get(claimantType).get(tenure).get(level).get(type);
                            } else {
                                lastYearCountAreas = null;
                            }
                            if (lastMonth_yearMonth != null) {
                                lastMonthCountAreas = yearMonthClaimantTypeTenureLevelTypeCountAreas.get(lastMonth_yearMonth).get(claimantType).get(tenure).get(level).get(type);
                            } else {
                                lastMonthCountAreas = null;
                            }
                            tenureCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(tenure).get(level).get(type);
                            dir = claimantTypeTenureLevelTypeDirs.get(claimantType).get(tenure).get(level).get(type);
                            TreeMap<Integer, TreeSet<String>> countAreas;
                            countAreas = writeResults(
                                    areaCounts,
                                    lastYearCountAreas,
                                    lastYear_yearMonth,
                                    lastMonthCountAreas,
                                    lastMonth_yearMonth,
                                    tenureCounts,
                                    level,
                                    dir,
                                    year,
                                    month);
                            typeCountAreas.put(type, countAreas);
                        }
                        TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>> distanceTypeDistanceCountAreas;
                        distanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>();
                        levelDistanceTypeDistanceCountAreas.put(level, distanceTypeDistanceCountAreas);
                        distanceTypesIte = distanceTypes.iterator();
                        while (distanceTypesIte.hasNext()) {
                            String distanceType;
                            distanceType = distanceTypesIte.next();
                            TreeMap<Double, TreeMap<Integer, TreeSet<String>>> distanceCountAreas;
                            distanceCountAreas = new TreeMap<Double, TreeMap<Integer, TreeSet<String>>>();
                            distanceTypeDistanceCountAreas.put(distanceType, distanceCountAreas);
                            distancesIte = distances.iterator();
                            while (distancesIte.hasNext()) {
                                double distance = distancesIte.next();
                                TreeMap<String, Integer> areaCounts;
                                TreeMap<Integer, TreeSet<String>> lastYearCountAreas;
                                TreeMap<Integer, TreeSet<String>> lastMonthCountAreas;
                                TreeMap<Integer, Integer> tenureCounts;
                                File dir;
                                areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(tenure).get(level).get(distanceType).get(distance);
                                if (lastYear_yearMonth != null) {
                                    lastYearCountAreas = yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.get(lastYear_yearMonth).get(claimantType).get(tenure).get(level).get(distanceType).get(distance);
                                } else {
                                    lastYearCountAreas = null;
                                }
                                if (lastMonth_yearMonth != null) {
                                    lastMonthCountAreas = yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.get(lastMonth_yearMonth).get(claimantType).get(tenure).get(level).get(distanceType).get(distance);
                                } else {
                                    lastMonthCountAreas = null;
                                }
                                tenureCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(tenure).get(level).get(distanceType).get(distance);
                                dir = claimantTypeTenureLevelTypeDistanceDirs.get(claimantType).get(tenure).get(level).get(distanceType).get(distance);
                                TreeMap<Integer, TreeSet<String>> countAreas;
                                countAreas = writeResults(
                                        areaCounts,
                                        lastYearCountAreas,
                                        lastYear_yearMonth,
                                        lastMonthCountAreas,
                                        lastMonth_yearMonth,
                                        tenureCounts,
                                        level,
                                        dir,
                                        year,
                                        month);
                                distanceCountAreas.put(distance, countAreas);
                            }
                        }
                        //Report unexpectedCounts
                        // Currently this is only written to stdout and is not captured in a
                        // file per se.
                        TreeMap<String, Integer> unexpectedCounts;
                        unexpectedCounts = levelUnexpectedCounts.get(level);
                        System.out.println("Unexpected Counts for:"
                                + " Claimant Type " + claimantType
                                + " Tenure " + tenure
                                + " Level " + level);
                        System.out.println("code,count");
                        Iterator<String> unexpectedCountsIte;
                        unexpectedCountsIte = unexpectedCounts.keySet().iterator();
                        while (unexpectedCountsIte.hasNext()) {
                            String code = unexpectedCountsIte.next();
                            Integer count = unexpectedCounts.get(code);
                            System.out.println("" + code + ", " + count);
                        }
                    }
                }
            }
            SHBEData0 = SHBEData1;
        }
    }

    protected static TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> lastYearCountAreas,
            String lastYear_yearMonth,
            TreeMap<Integer, TreeSet<String>> lastMonthCountAreas,
            String lastMonth_yearMonth,
            TreeMap<Integer, Integer> tenureCounts,
            String level,
            File dir,
            String year,
            String month) {

        int num = 5;

        TreeMap<Integer, TreeSet<String>> result;
        // Write out counts by area
        result = writeCountsByArea(
                areaCounts,
                level,
                dir,
                year,
                month);
        // Write out areas with highest counts
        writeAreasWithHighestNumbersOfClaimants(
                result,
                num,
                dir,
                year,
                month);
        // Write out areas with biggest increases from last year
        writeExtremeAreaChanges(
                result,
                lastYearCountAreas,
                "LastYear",
                num,
                dir,
                year,
                month);
        // Write out areas with biggest increases from last month
        writeExtremeAreaChanges(
                result,
                lastMonthCountAreas,
                "LastMonth",
                num,
                dir,
                year,
                month);
        // Write out counts by tenure
        writeCountsByTenure(
                tenureCounts,
                dir,
                year,
                month);
        return result;
    }

    protected static void writeExtremeAreaChanges(
            TreeMap<Integer, TreeSet<String>> countAreas,
            TreeMap<Integer, TreeSet<String>> lastTimeCountAreas,
            String lastTime,
            int num,
            File dir,
            String year,
            String month) {
        if (lastTimeCountAreas != null) {
            TreeMap<String, Integer> areaCounts;
            areaCounts = getAreaCounts(countAreas);
            TreeMap<String, Integer> lastTimeAreaCounts;
            lastTimeAreaCounts = getAreaCounts(lastTimeCountAreas);
            int n = 2;
            TreeMap<String, Double> areaAbsoluteDiffs;
            areaAbsoluteDiffs = new TreeMap<String, Double>();
            TreeMap<String, Double> areaRelativeDiffs;
            areaRelativeDiffs = new TreeMap<String, Double>();
            TreeSet<String> allAreas;
            allAreas = new TreeSet<String>();
            allAreas.addAll(areaCounts.keySet());
            allAreas.addAll(lastTimeAreaCounts.keySet());
            Iterator<String> ite;
            ite = allAreas.iterator();
            while (ite.hasNext()) {
                String area;
                area = ite.next();
                int count;
                if (areaCounts.get(area) == null) {
                    count = 0;
                } else {
                    count = areaCounts.get(area);
                }
                int lastTimeCount;
                if (lastTimeAreaCounts.get(area) == null) {
                    lastTimeCount = 0;
                } else {
                    lastTimeCount = lastTimeAreaCounts.get(area);
                }
                double absoluteDifference;
                absoluteDifference = count - lastTimeCount;
                areaAbsoluteDiffs.put(area, absoluteDifference);
                double relativeDifference;
                relativeDifference = getRelativeDifference(count, lastTimeCount, n);
                areaRelativeDiffs.put(area, relativeDifference);
            }
            TreeMap<Double, TreeSet<String>> absoluteDiffsAreas;
            absoluteDiffsAreas = getCountAreas(areaAbsoluteDiffs);
            writeDiffs(
                    absoluteDiffsAreas,
                    "Absolute",
                    lastTime,
                    num,
                    dir,
                    year,
                    month);
            TreeMap<Double, TreeSet<String>> relativeDiffsAreas;
            relativeDiffsAreas = getCountAreas(areaRelativeDiffs);
            writeDiffs(
                    relativeDiffsAreas,
                    "Relative",
                    lastTime,
                    num,
                    dir,
                    year,
                    month);
        }
    }

    /**
     *
     * @param a
     * @param b
     * @param n
     * @return
     */
    public static double getRelativeDifference(
            double a,
            double b,
            int n) {
        double result;
        if (a == b) {
            return 0.0d;
        }
        double delta = a - b;
        double num;
//        num = delta;
//        num = (delta * delta * delta) * (b + 1);
//        num = delta * delta * delta * delta * delta;
//        num = delta * delta * (b + 1) * (a + 1);
        num = delta * (b + 1) * (a + 1);
        double denom;
//        denom = (a + b) / 2;
//        denom = Math.max(a, b);
        denom = (a + b);
//        denom *= denom;
        result = num / denom;
//        result *= 100;
//        if (b > a) {
//            result *= -1.0d;
//        }

        return result;
    }

    protected static void writeDiffs(
            TreeMap<Double, TreeSet<String>> diffsAreas,
            String name,
            String lastTime,
            int num,
            File dir,
            String year,
            String month) {
        PrintWriter pw;
        String type;
        type = "Increases";
        pw = init_OutputTextFilePrintWriter(
                dir,
                "ExtremeAreaChanges" + name + type + lastTime + year + month + ".csv");
        Iterator<Double> iteD;
        iteD = diffsAreas.descendingKeySet().iterator();
        writeDiffs(
                diffsAreas,
                num,
                type,
                pw,
                iteD);
        type = "Decreases";
        pw = init_OutputTextFilePrintWriter(
                dir,
                "ExtremeAreaChanges" + name + type + lastTime + year + month + ".csv");
        iteD = diffsAreas.keySet().iterator();
        writeDiffs(
                diffsAreas,
                num,
                type,
                pw,
                iteD);
    }

    protected static void writeDiffs(
            TreeMap<Double, TreeSet<String>> diffsAreas,
            int num,
            String type,
            PrintWriter pw,
            Iterator<Double> iteD) {
        MathContext mc;
        mc = new MathContext(5, RoundingMode.HALF_UP);
        pw.println("Area, " + type);
        int counter;
        counter = 0;
        while (iteD.hasNext()) {
            Double count = iteD.next();
            if (count != Double.NaN) {
                if (counter < num) {
                    TreeSet<String> areas;
                    areas = diffsAreas.get(count);
                    Iterator<String> ite2;
                    ite2 = areas.iterator();
                    while (ite2.hasNext()) {
                        String area;
                        area = ite2.next();
                        BigDecimal roundedCount;
                        roundedCount = new BigDecimal(count);
                        roundedCount = roundedCount.round(mc);
                        pw.println(area + ", " + roundedCount.toPlainString());
                        //pw.println(count + ", " + area);
                        counter++;
                    }
                } else {
                    break;
                }
            }
        }
        pw.close();
    }

    protected static TreeMap<String, Integer> getAreaCounts(
            TreeMap<Integer, TreeSet<String>> countAreas) {
        TreeMap<String, Integer> result;
        result = new TreeMap<String, Integer>();
        Iterator<Integer> ite;
        ite = countAreas.keySet().iterator();
        while (ite.hasNext()) {
            Integer count = ite.next();
            if (count == null) {
                count = 0;
            }
            if (count == Double.NaN) {
                count = 0;
            }
            TreeSet<String> areas;
            areas = countAreas.get(count);
            Iterator<String> ite2;
            ite2 = areas.iterator();
            while (ite2.hasNext()) {
                String area;
                area = ite2.next();
                result.put(area, count);
            }
        }
        return result;
    }

    protected static TreeMap<Double, TreeSet<String>> getCountAreas(
            TreeMap<String, Double> areaCounts) {
        TreeMap<Double, TreeSet<String>> result;
        result = new TreeMap<Double, TreeSet<String>>();
        Iterator<String> ite;
        ite = areaCounts.keySet().iterator();
        while (ite.hasNext()) {
            String area = ite.next();
            Double count = areaCounts.get(area);
            if (count == null) {
                count = 0.0d;
            }
            if (count == Double.NaN) {
                count = 0.0d;
            }
            TreeSet<String> areas;
            areas = result.get(count);
            if (areas == null) {
                areas = new TreeSet<String>();
                areas.add(area);
                result.put(count, areas);
            } else {
                areas.add(area);
            }
        }
        return result;
    }

    /**
     * Writes the top num countAreas to file.
     *
     * @param countAreas
     * @param num
     * @param dir
     * @param year
     * @param month
     */
    protected static void writeAreasWithHighestNumbersOfClaimants(
            TreeMap<Integer, TreeSet<String>> countAreas,
            int num,
            File dir,
            String year,
            String month) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                "HighestClaimants" + year + month + ".csv");
        pw.println("Area, Count");
        int counter;
        counter = 0;
        Iterator<Integer> ite;
        ite = countAreas.descendingKeySet().iterator();
        while (ite.hasNext()) {
            Integer count = ite.next();
            if (counter < num) {
                TreeSet<String> areas;
                areas = countAreas.get(count);
                Iterator<String> ite2;
                ite2 = areas.iterator();
                while (ite2.hasNext()) {
                    String area;
                    area = ite2.next();
                    pw.println(area + ", " + count);
                    counter++;
                }
            } else {
                break;
            }
        }
        pw.close();
    }

    /**
     *
     * @param areaCounts
     * @param level
     * @param dir
     * @param year
     * @param month
     * @return {@code TreeMap<Integer, TreeSet<String>>} count by list of areas.
     * This is an ordered list from minimum to maximum counts.
     */
    protected static TreeMap<Integer, TreeSet<String>> writeCountsByArea(
            TreeMap<String, Integer> areaCounts,
            String level,
            File dir,
            String year,
            String month) {
        TreeMap<Integer, TreeSet<String>> result;
        result = new TreeMap<Integer, TreeSet<String>>();
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                year + month + ".csv");
        pw.println(level + ", Count");
        Iterator<String> ite;
        ite = areaCounts.keySet().iterator();
        while (ite.hasNext()) {
            String areaCode = ite.next().trim();
            if (level.equalsIgnoreCase("PostcodeUnit")) {
                if (areaCode.length() != 7) {
                    areaCode = areaCode.replace(" ", "");
                }
            }
            Integer count = areaCounts.get(areaCode);
            if (count == null) {
                count = 0;
            }
            TreeSet<String> set;
            set = result.get(count);
            if (set == null) {
                set = new TreeSet<String>();
                set.add(areaCode);
                result.put(count, set);
            } else {
                set.add(areaCode);
            }
            pw.println(areaCode + ", " + count);
        }
        pw.close();
        return result;
    }

    private static void writeCountsByTenure(
            TreeMap<Integer, Integer> tenureCounts,
            File dir,
            String year,
            String month) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                "CountsByTenure" + year + month + ".csv");
        pw.println("Tenure, Count");
        Iterator<Integer> ite;
        ite = tenureCounts.keySet().iterator();
        while (ite.hasNext()) {
            Integer tenure0 = ite.next();
            Integer count = tenureCounts.get(tenure0);
            pw.println(tenure0 + ", " + count);
        }
        pw.close();
    }

    private static void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts,
            String areaCode,
            String claimantType,
            String tenure,
            String level,
            String type,
            Integer tenancyType) {
        addToAreaCount(claimantTypeTenureLevelTypeAreaCounts, areaCode, claimantType, tenure, level, type);
        TreeMap<Integer, Integer> tenureCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(tenure).get(level).get(type);
        Generic_Collections.addToTreeMapIntegerInteger(
                tenureCounts,
                tenancyType,
                1);
    }

    private static void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts,
            String areaCode,
            String claimantType,
            String tenure,
            String level,
            String type,
            Integer tenancyType,
            double distance) {
        addToAreaCount(claimantTypeTenureLevelTypeDistanceAreaCounts, areaCode, claimantType, tenure, level, type, distance);
        TreeMap<Integer, Integer> tenureCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(tenure).get(level).get(type).get(distance);
        Generic_Collections.addToTreeMapIntegerInteger(
                tenureCounts,
                tenancyType,
                1);
    }

    private static void addToAreaCount(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            String areaCode,
            String claimantType,
            String tenure,
            String level,
            String type) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenure).get(level).get(type);
        Generic_Collections.addToTreeMapStringInteger(
                areaCounts,
                areaCode,
                1);
    }

    private static void addToAreaCount(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            String areaCode,
            String claimantType,
            String tenure,
            String level,
            String type,
            Double distance) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(tenure).get(level).get(type).get(distance);
//        //Debug
//        if (areaCounts == null) {
//            // No area counts for distance
//            System.out.println("No area counts for distance " + distance);
//            System.out.println("claimantType " + claimantType);
//            System.out.println("tenure " + tenure);
//            System.out.println("level " + level);
//            System.out.println("type " + type);
//        }
        Generic_Collections.addToTreeMapStringInteger(
                areaCounts,
                areaCode,
                1);
    }

    public static boolean getHasPreviousTenureChange(
            String nationalInsuranceNumber,
            HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByTenures,
            ArrayList<Integer> includes,
            int index) {
        Iterator<Integer> ite;
        ite = includes.iterator();
        int i;
        // Skip the first
        i = ite.next();
        while (ite.hasNext()) {
            i = ite.next();
            if (i < index) {
                if (includes.contains(i)) {
                    HashMap<String, String> nationalInsuranceNumberByTenure;
                    nationalInsuranceNumberByTenure = nationalInsuranceNumberByTenures.get(i);
                    if (nationalInsuranceNumberByTenure.containsKey(nationalInsuranceNumber)) {
//                        System.err.println("index i = " + i + " is included "
//                                + "and is less than " + index + " and there "
//                                + "is a previous tenancy change for national "
//                                + "insurance number " + nationalInsuranceNumber 
//                                + ".");
                        return true;
//                    } else {
//                        System.err.println("index i = " + i + " is included "
//                                + "and is less than " + index + " , but there "
//                                + "is no previous tenancy change for national "
//                                + "insurance number " + nationalInsuranceNumber 
//                                + ".");
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public static void recordTenureChanges(
            String nationalInsuranceNumber,
            HashMap<String, ArrayList<String>> tenureChanges,
            String year,
            String month,
            String tenureChange) {
        ArrayList<String> previousTenureChanges;
        previousTenureChanges = tenureChanges.get(nationalInsuranceNumber);
        ArrayList<String> tenureChangeList;
        if (previousTenureChanges == null) {
            tenureChangeList = new ArrayList<String>();
            tenureChanges.put(nationalInsuranceNumber, tenureChangeList);
        } else {
            tenureChangeList = tenureChanges.get(nationalInsuranceNumber);
        }
        tenureChangeList.add(tenureChange + ":" + year + month);
    }

    public static String getTenureChange(
            Integer tenure0,
            Integer tenure1) {
        return getTenureChange(
                tenure0.toString(),
                tenure1.toString());
    }

    public static String getTenureChange(
            String tenure0,
            String tenure1) {
        String result;
        result = tenure0 + " - " + tenure1;
        return result;
    }

    /**
     *
     * @param nationalInsuranceNumberByTenure0 Before
     * @param nationalInsuranceNumberByTenure1 Now
     * @param nationalInsuranceNumberByTenures
     * @param include
     * @param index
     * @return A count matrix of tenure changes {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count
     * }
     */
    public Object[] getMultipleTenancyTypeTranistionMatrix(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByTenures,
            ArrayList<Integer> include,
            int index) {
        Object[] result;
        result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
        tenancyTypeTranistionMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        result[0] = tenancyTypeTranistionMatrix;
        HashMap<String, String> nationalInsuranceNumberTenureChange;
        nationalInsuranceNumberTenureChange = new HashMap<String, String>();
        result[1] = nationalInsuranceNumberTenureChange;
        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    nationalInsuranceNumber,
                    nationalInsuranceNumberByTenures,
                    include,
                    index);
            Integer tenure1 = nationalInsuranceNumberByTenure1.get(nationalInsuranceNumber);
            Integer tenure0 = nationalInsuranceNumberByTenure0.get(nationalInsuranceNumber);
            if (tenure0 == null) {
                tenure0 = -999;
            }
            if (hasPreviousTenureChange) {
                if (tenancyTypeTranistionMatrix.containsKey(tenure1)) {
                    TreeMap<Integer, Integer> tenureCount;
                    tenureCount = tenancyTypeTranistionMatrix.get(tenure1);
                    Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
                } else {
                    TreeMap<Integer, Integer> tenureCount;
                    tenureCount = new TreeMap<Integer, Integer>();
                    tenureCount.put(tenure0, 1);
                    tenancyTypeTranistionMatrix.put(tenure1, tenureCount);
                }
            }
            if (tenure0.compareTo(tenure1) != 0) {
                nationalInsuranceNumberTenureChange.put(
                        nationalInsuranceNumber,
                        "" + tenure0 + " - " + tenure1);
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    nationalInsuranceNumber,
                    nationalInsuranceNumberByTenures,
                    include,
                    index);
            Integer tenure0 = nationalInsuranceNumberByTenure0.get(nationalInsuranceNumber);
            if (tenure0 == null) {
                tenure0 = -999;
            }
            Integer tenure1;
            tenure1 = -999;
            if (hasPreviousTenureChange) {
                if (!set.contains(nationalInsuranceNumber)) {
                    if (tenancyTypeTranistionMatrix.containsKey(tenure1)) {
                        TreeMap<Integer, Integer> tenureCount;
                        tenureCount = tenancyTypeTranistionMatrix.get(tenure1);
                        Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
                    } else {
                        TreeMap<Integer, Integer> tenureCount;
                        tenureCount = new TreeMap<Integer, Integer>();
                        tenureCount.put(tenure0, 1);
                        tenancyTypeTranistionMatrix.put(tenure1, tenureCount);
                    }
                }
            }
//            if (tenure0.compareTo(tenure1) != 0) {
//                nationalInsuranceNumberTenureChange.put(
//                        nationalInsuranceNumber,
//                        "" + tenure0 + " - " + tenure1);
//            }
        }
        return result;
    }

    public int[] getPreviousTenure(
            String nationalInsuranceNumber,
            HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures,
            int index,
            ArrayList<Integer> include) {
        int[] result;
        result = new int[2];
        for (int i = index - 1; i > -1; i--) {
            if (include.contains(i)) {
                HashMap<String, Integer> nationalInsuranceNumberByTenure;
                nationalInsuranceNumberByTenure = nationalInsuranceNumberByTenures.get(i);
                Integer tenure;
                tenure = nationalInsuranceNumberByTenure.get(nationalInsuranceNumber);
                if (tenure != null) {
                    if (tenure != -999) {
                        result[0] = tenure;
                        result[1] = i;
                        return result;
                    }
                }
            }
        }
        result[0] = -999;
        result[1] = 0;
        return result;
    }

    /**
     *
     * @param nationalInsuranceNumberByTenure0
     * @param nationalInsuranceNumberByTenure1
     * @param tenureChanges
     * @param year
     * @param month
     * @param checkPreviousTenure
     * @param nationalInsuranceNumberByTenures
     * @param index
     * @param include
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count
     * }
     */
    public TreeMap<Integer, TreeMap<Integer, Integer>> getTenancyTypeTranistionMatrixAndRecordTenancyChange(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            HashMap<String, ArrayList<String>> tenureChanges,
            String year,
            String month,
            boolean checkPreviousTenure,
            HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures,
            int index,
            ArrayList<Integer> include) {
        TreeMap<Integer, TreeMap<Integer, Integer>> result;
        result = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            Integer tenure0 = nationalInsuranceNumberByTenure0.get(nationalInsuranceNumber);
            if (tenure0 == null) {
                if (checkPreviousTenure) {
                    int[] previousTenure;
                    previousTenure = getPreviousTenure(
                            nationalInsuranceNumber,
                            nationalInsuranceNumberByTenures,
                            index,
                            include);
                    tenure0 = previousTenure[0];
                } else {
                    tenure0 = -999;
                }
            } else {
                if (tenure0 == -999) {
                    if (checkPreviousTenure) {
                        int[] previousTenure;
                        previousTenure = getPreviousTenure(
                                nationalInsuranceNumber,
                                nationalInsuranceNumberByTenures,
                                index,
                                include);
                        tenure0 = previousTenure[0];
                    }
                }
            }
            Integer tenure1 = nationalInsuranceNumberByTenure1.get(nationalInsuranceNumber);
            if (tenure0.compareTo(tenure1) != 0) {
                String tenureChange;
                tenureChange = getTenureChange(tenure0, tenure1);
                recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
            }
            if (result.containsKey(tenure1)) {
                TreeMap<Integer, Integer> tenureCount;
                tenureCount = result.get(tenure1);
                Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
            } else {
                TreeMap<Integer, Integer> tenureCount;
                tenureCount = new TreeMap<Integer, Integer>();
                tenureCount.put(tenure0, 1);
                result.put(tenure1, tenureCount);
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            if (!set.contains(nationalInsuranceNumber)) {
                Integer tenure0 = nationalInsuranceNumberByTenure0.get(
                        nationalInsuranceNumber);
                Integer tenure1;
                tenure1 = -999;
                if (tenure0.compareTo(tenure1) != 0) {
                    String tenureChange;
                    tenureChange = getTenureChange(tenure0, tenure1);
                    recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
                }
                if (result.containsKey(tenure1)) {
                    TreeMap<Integer, Integer> tenureCount;
                    tenureCount = result.get(tenure1);
                    Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
                } else {
                    TreeMap<Integer, Integer> tenureCount;
                    tenureCount = new TreeMap<Integer, Integer>();
                    tenureCount.put(tenure0, 1);
                    result.put(tenure1, tenureCount);
                }
            }
        }
        return result;
    }

    /**
     *
     * @param nationalInsuranceNumberByTenure0
     * @param nationalInsuranceNumberByTenure1
     * @param nationalInsuranceNumberByPostcode0
     * @param nationalInsuranceNumberByPostcode1
     * @param postCodeHandler
     * @param postcodeChange
     * @param tenureChanges
     * @param year
     * @param month
     * @param checkPreviousTenure
     * @param nationalInsuranceNumberByTenures
     * @param checkPreviousPostcode
     * @param nationalInsuranceNumberByPostcodes
     * @param index
     * @param include
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<Integer, TreeMap<Integer, Integer>> getTenancyTypeTranistionMatrixAndMaybeWritePostcodeChanges(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            HashMap<String, String> nationalInsuranceNumberByPostcode0,
            HashMap<String, String> nationalInsuranceNumberByPostcode1,
            Generic_UKPostcode_Handler postCodeHandler,
            boolean postcodeChange,
            HashMap<String, ArrayList<String>> tenureChanges,
            String year,
            String month,
            boolean checkPreviousTenure,
            HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures,
            boolean checkPreviousPostcode,
            HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByPostcodes,
            int index,
            ArrayList<Integer> include) {
        TreeMap<Integer, TreeMap<Integer, Integer>> result;
        result = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            Integer tenure0 = nationalInsuranceNumberByTenure0.get(nationalInsuranceNumber);
            String postcode0;
            postcode0 = nationalInsuranceNumberByPostcode0.get(nationalInsuranceNumber);
            boolean isValidPostcodeFormPostcode0;
            isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
            if (tenure0 == null) {
                if (checkPreviousTenure) {
                    int[] previousTenure;
                    previousTenure = getPreviousTenure(
                            nationalInsuranceNumber,
                            nationalInsuranceNumberByTenures,
                            index,
                            include);
                    tenure0 = previousTenure[0];
                    int indexOfLastKnownTenureOrNot;
                    indexOfLastKnownTenureOrNot = previousTenure[1];
                    if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                        postcode0 = nationalInsuranceNumberByPostcodes.get(indexOfLastKnownTenureOrNot).get(nationalInsuranceNumber);
                        isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
                    }
                } else {
                    tenure0 = -999;
                }
            }
            Integer tenure1 = nationalInsuranceNumberByTenure1.get(nationalInsuranceNumber);
            String postcode1;
            postcode1 = nationalInsuranceNumberByPostcode1.get(nationalInsuranceNumber);
            boolean isValidPostcodeFormPostcode1;
            isValidPostcodeFormPostcode1 = postCodeHandler.isValidPostcodeForm(postcode1);
            if (isValidPostcodeFormPostcode0 && isValidPostcodeFormPostcode1) {
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equalsIgnoreCase(postcode1);
                } else {
                    doCount = postcode0.equalsIgnoreCase(postcode1);
                }
                if (doCount) {
                    if (tenure0.compareTo(tenure1) != 0) {
                        String tenureChange;
                        tenureChange = getTenureChange(tenure0, tenure1);
                        recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);

                        if (postcodeChange) {
                            String[] resultItem;
                            resultItem = new String[6];
                            resultItem[0] = nationalInsuranceNumber;
                            resultItem[1] = year;
                            resultItem[2] = month;
                            resultItem[3] = tenureChange;
                            resultItem[4] = postcode0;
                            resultItem[5] = postcode1;
                            postcodeChanges.add(resultItem);
                        }

                    }
                    if (result.containsKey(tenure1)) {
                        TreeMap<Integer, Integer> tenureCount;
                        tenureCount = result.get(tenure1);
                        Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
                    } else {
                        TreeMap<Integer, Integer> tenureCount;
                        tenureCount = new TreeMap<Integer, Integer>();
                        tenureCount.put(tenure0, 1);
                        result.put(tenure1, tenureCount);
                    }
                }
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            if (!set.contains(nationalInsuranceNumber)) {
                Integer tenure0 = nationalInsuranceNumberByTenure0.get(
                        nationalInsuranceNumber);
                Integer tenure1;
                tenure1 = -999;
                if (tenure0.compareTo(tenure1) != 0) {
                    String tenureChange;
                    tenureChange = getTenureChange(tenure0, tenure1);
                    recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
                }
                String postcode0;
                postcode0 = nationalInsuranceNumberByPostcode0.get(nationalInsuranceNumber);
                boolean isValidPostcodeFormPostcode0;
                isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
                String postcode1;
                postcode1 = nationalInsuranceNumberByPostcode1.get(nationalInsuranceNumber);
                boolean isValidPostcodeFormPostcode1;
                isValidPostcodeFormPostcode1 = postCodeHandler.isValidPostcodeForm(postcode1);
                if (isValidPostcodeFormPostcode0 && isValidPostcodeFormPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {

                        if (tenure0.compareTo(tenure1) != 0) {
                            String tenureChange;
                            tenureChange = getTenureChange(tenure0, tenure1);
                            recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);

                            if (postcodeChange) {
                                String[] resultItem;
                                resultItem = new String[6];
                                resultItem[0] = nationalInsuranceNumber;
                                resultItem[1] = year;
                                resultItem[2] = month;
                                resultItem[3] = tenureChange;
                                resultItem[4] = postcode0;
                                resultItem[5] = postcode1;
                                postcodeChanges.add(resultItem);
                            }

                        }
                        if (result.containsKey(tenure1)) {
                            TreeMap<Integer, Integer> tenureCount;
                            tenureCount = result.get(tenure1);
                            Generic_Collections.addToTreeMapIntegerInteger(tenureCount, tenure0, 1);
                        } else {
                            TreeMap<Integer, Integer> tenureCount;
                            tenureCount = new TreeMap<Integer, Integer>();
                            tenureCount.put(tenure0, 1);
                            result.put(tenure1, tenureCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            writePostcodeChanges(
                    postcodeChanges,
                    checkPreviousTenure,
                    checkPreviousPostcode,
                    "Ungrouped");
        }
        return result;
    }

    /**
     *
     * @param nationalInsuranceNumberByTenure0
     * @param nationalInsuranceNumberByTenure1
     * @param nationalInsuranceNumberByPostcode0
     * @param nationalInsuranceNumberByPostcode1
     * @param postCodeHandler
     * @param postcodeChange
     * @param tenureChanges
     * @param year
     * @param month
     * @param checkPreviousTenure
     * @param nationalInsuranceNumberByTenures
     * @param index
     * @param include
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTranistionMatrixGroupedAndMaybeWritePostcodeChanges(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            HashMap<String, String> nationalInsuranceNumberByPostcode0,
            HashMap<String, String> nationalInsuranceNumberByPostcode1,
            Generic_UKPostcode_Handler postCodeHandler,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<String, ArrayList<String>> tenureChanges,
            String year,
            String month,
            boolean checkPreviousTenure,
            HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByPostcodes,
            int index,
            ArrayList<Integer> include) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();

        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }

        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            Integer tenure0 = nationalInsuranceNumberByTenure0.get(nationalInsuranceNumber);
            String postcode0;
            postcode0 = nationalInsuranceNumberByPostcode0.get(nationalInsuranceNumber);
            boolean isValidPostcodeFormPostcode0;
            isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
            if (tenure0 == null) {
                if (checkPreviousTenure) {
                    int[] previousTenure;
                    previousTenure = getPreviousTenure(
                            nationalInsuranceNumber,
                            nationalInsuranceNumberByTenures,
                            index,
                            include);
                    tenure0 = previousTenure[0];
                    int indexOfLastKnownTenureOrNot;
                    indexOfLastKnownTenureOrNot = previousTenure[1];
                    if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                        postcode0 = nationalInsuranceNumberByPostcodes.get(indexOfLastKnownTenureOrNot).get(nationalInsuranceNumber);
                        isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
                    }
                } else {
                    tenure0 = -999;
                }
            }
            String tenureType0;
            tenureType0 = getTenancyTypeGroup(
                    regulatedGroups,
                    unregulatedGroups,
                    tenure0);
            Integer tenure1;
            tenure1 = nationalInsuranceNumberByTenure1.get(nationalInsuranceNumber);
            String tenureType1;
            tenureType1 = getTenancyTypeGroup(
                    regulatedGroups,
                    unregulatedGroups,
                    tenure1);
            String postcode1;
            postcode1 = nationalInsuranceNumberByPostcode1.get(nationalInsuranceNumber);
            boolean isValidPostcodeFormPostcode1;
            isValidPostcodeFormPostcode1 = postCodeHandler.isValidPostcodeForm(postcode1);
            if (isValidPostcodeFormPostcode0 && isValidPostcodeFormPostcode1) {
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equalsIgnoreCase(postcode1);
                } else {
                    doCount = postcode0.equalsIgnoreCase(postcode1);
                }
                if (doCount) {
                    if (!tenureType0.equalsIgnoreCase(tenureType1)) {
                        String tenureChange;
                        tenureChange = getTenureChange(tenureType0, tenureType1);
                        recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);

                        if (postcodeChange) {
                            String[] resultItem;
                            resultItem = new String[6];
                            resultItem[0] = nationalInsuranceNumber;
                            resultItem[1] = year;
                            resultItem[2] = month;
                            resultItem[3] = tenureChange;
                            resultItem[4] = postcode0;
                            resultItem[5] = postcode1;
                            postcodeChanges.add(resultItem);
                        }

                    }
                    if (result.containsKey(tenureType1)) {
                        TreeMap<String, Integer> tenureCount;
                        tenureCount = result.get(tenureType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenureCount, tenureType0, 1);
                    } else {
                        TreeMap<String, Integer> tenureCount;
                        tenureCount = new TreeMap<String, Integer>();
                        tenureCount.put(tenureType0, 1);
                        result.put(tenureType1, tenureCount);
                    }
                }
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            if (!set.contains(nationalInsuranceNumber)) {
                Integer tenure0 = nationalInsuranceNumberByTenure0.get(
                        nationalInsuranceNumber);
                String tenureType0;
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
                String tenureType1;
                tenureType1 = "-999";
                if (!tenureType0.equalsIgnoreCase(tenureType1)) {
                    String tenureChange;
                    tenureChange = getTenureChange(tenureType0, tenureType1);
                    recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
                }
                String postcode0;
                postcode0 = nationalInsuranceNumberByPostcode0.get(nationalInsuranceNumber);
                boolean isValidPostcodeFormPostcode0;
                isValidPostcodeFormPostcode0 = postCodeHandler.isValidPostcodeForm(postcode0);
                String postcode1;
                postcode1 = nationalInsuranceNumberByPostcode1.get(nationalInsuranceNumber);
                boolean isValidPostcodeFormPostcode1;
                isValidPostcodeFormPostcode1 = postCodeHandler.isValidPostcodeForm(postcode1);
                if (isValidPostcodeFormPostcode0 && isValidPostcodeFormPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {
                        if (!tenureType0.equalsIgnoreCase(tenureType1)) {
                            String tenureChange;
                            tenureChange = getTenureChange(tenureType0, tenureType1);
                            recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);

                            if (postcodeChange) {
                                String[] resultItem;
                                resultItem = new String[6];
                                resultItem[0] = nationalInsuranceNumber;
                                resultItem[1] = year;
                                resultItem[2] = month;
                                resultItem[3] = tenureChange;
                                resultItem[4] = postcode0;
                                resultItem[5] = postcode1;
                                postcodeChanges.add(resultItem);
                            }

                        }
                        if (result.containsKey(tenureType1)) {
                            TreeMap<String, Integer> tenureCount;
                            tenureCount = result.get(tenureType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenureCount, tenureType0, 1);
                        } else {
                            TreeMap<String, Integer> tenureCount;
                            tenureCount = new TreeMap<String, Integer>();
                            tenureCount.put(tenureType0, 1);
                            result.put(tenureType1, tenureCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            writePostcodeChanges(
                    postcodeChanges,
                    checkPreviousTenure,
                    checkPreviousPostcode,
                    "Grouped");
        }
        return result;
    }

    private void writePostcodeChanges(
            ArrayList<String[]> postcodeChanges,
            boolean checkPreviousTenure,
            boolean checkPreviousPostcode,
            String type) {
        File dir;
        dir = DW_Files.getOutputSHBETablesDir();
        dir = new File(
                dir,
                "Tenancy");
        dir = new File(
                dir,
                "All");
        dir = new File(
                dir,
                "TenancyAndPostcodeChanges");
        dir = new File(
                dir,
                type);
        if (checkPreviousTenure) {
            dir = new File(
                    dir,
                    "CheckedPreviousTenure");
        } else {
            dir = new File(
                    dir,
                    "NotCheckedPreviousTenure");
        }
        if (checkPreviousPostcode) {
            dir = new File(
                    dir,
                    "CheckedPreviousPostcode");
        } else {
            dir = new File(
                    dir,
                    "NotCheckedPreviousPostcode");
        }
        dir.mkdirs();
        File f;
        f = new File(
                dir,
                "PostcodeChanges.csv");
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(f, true);
        Iterator<String[]> ite;
        ite = postcodeChanges.iterator();
        while (ite.hasNext()) {
            String[] postcodeChange;
            postcodeChange = ite.next();
            String line;
            line = postcodeChange[0];
            for (int i = 1; i < postcodeChange.length; i++) {
                line += ", " + postcodeChange[i];
            }
            pw.println(line);
        }
        pw.flush();
        pw.close();
    }

    public String getTenancyTypeGroup(
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            Integer tenure) {
        String result;
        result = "Ungrouped";
        if (regulatedGroups.contains(tenure)) {
            result = "Regulated";
        }
        if (unregulatedGroups.contains(tenure)) {
            result = "Unregulated";
        }
        return result;
    }

    public Object[] getMultipleTenancyTypeTranistionMatrixGrouped(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<Integer, HashMap<String, String>> nationalInsuranceNumberByTenures,
            ArrayList<Integer> include,
            int index) {
        Object[] result;
        result = new Object[2];
        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrix;
        tenancyTypeTranistionMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = tenancyTypeTranistionMatrix;
        HashMap<String, String> nationalInsuranceNumberTenureChange;
        nationalInsuranceNumberTenureChange = new HashMap<String, String>();
        result[1] = nationalInsuranceNumberTenureChange;
        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    nationalInsuranceNumber,
                    nationalInsuranceNumberByTenures,
                    include,
                    index);
            Integer tenure1;
            tenure1 = nationalInsuranceNumberByTenure1.get(
                    nationalInsuranceNumber);
            String tenureType1;
            tenureType1 = getTenancyTypeGroup(
                    regulatedGroups,
                    unregulatedGroups,
                    tenure1);
            Integer tenure0;
            tenure0 = nationalInsuranceNumberByTenure0.get(
                    nationalInsuranceNumber);
            String tenureType0;
            if (tenure0 == null) {
                tenureType0 = "-999";
            } else {
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
            }
            if (hasPreviousTenureChange) {
                if (tenancyTypeTranistionMatrix.containsKey(tenureType1)) {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = tenancyTypeTranistionMatrix.get(tenureType1);
                    Generic_Collections.addToTreeMapStringInteger(
                            tenureCount, tenureType0, 1);
                } else {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = new TreeMap<String, Integer>();
                    tenureCount.put(tenureType0, 1);
                    tenancyTypeTranistionMatrix.put(tenureType1, tenureCount);
                }
            }
            //if (tenure0.compareTo(tenure1) != 0) { // Using this those that change tenure, but within the same Group are still recorded as Tenure changers! 
            if (tenureType0.compareTo(tenureType1) != 0) {
                nationalInsuranceNumberTenureChange.put(
                        nationalInsuranceNumber,
                        "" + tenureType0 + " - " + tenureType1);
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    nationalInsuranceNumber,
                    nationalInsuranceNumberByTenures,
                    include,
                    index);
            Integer tenure0;
            tenure0 = nationalInsuranceNumberByTenure0.get(
                    nationalInsuranceNumber);
            String tenureType0;
            if (tenure0 == null) {
                tenureType0 = "-999";
            } else {
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
            }
            String tenureType1;
            tenureType1 = "-999";
            if (hasPreviousTenureChange) {
                if (!set.contains(nationalInsuranceNumber)) {
                    if (tenancyTypeTranistionMatrix.containsKey(tenureType1)) {
                        TreeMap<String, Integer> tenureCount;
                        tenureCount = tenancyTypeTranistionMatrix.get(tenureType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenureCount, tenureType0, 1);
                    } else {
                        TreeMap<String, Integer> tenureCount;
                        tenureCount = new TreeMap<String, Integer>();
                        tenureCount.put(tenureType0, 1);
                        tenancyTypeTranistionMatrix.put(tenureType1, tenureCount);
                    }
                }
            }
            //if (tenure0.compareTo(tenure1) != 0) { // Using this those that change tenure, but within the same Group are still recorded as Tenure changers! 
//            if (tenureType0.compareTo(tenureType1) != 0) {
//                nationalInsuranceNumberTenureChange.put(
//                        nationalInsuranceNumber,
//                        "" + tenureType0 + " - " + tenureType1);
//            }
        }
        return result;
    }

    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTranistionMatrixGroupedAndRecordTenancyChange(
            HashMap<String, Integer> nationalInsuranceNumberByTenure0,
            HashMap<String, Integer> nationalInsuranceNumberByTenure1,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<String, ArrayList<String>> tenureChanges,
            String year,
            String month,
            boolean checkPreviousTenure,
            HashMap<Integer, HashMap<String, Integer>> nationalInsuranceNumberByTenures,
            int index,
            ArrayList<Integer> include) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        Iterator<String> ite;
        ite = nationalInsuranceNumberByTenure1.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            Integer tenure1;
            tenure1 = nationalInsuranceNumberByTenure1.get(
                    nationalInsuranceNumber);
            String tenureType1;
            tenureType1 = getTenancyTypeGroup(
                    regulatedGroups,
                    unregulatedGroups,
                    tenure1);
            Integer tenure0;
            tenure0 = nationalInsuranceNumberByTenure0.get(
                    nationalInsuranceNumber);
            String tenureType0;
            if (tenure0 == null) {
                if (checkPreviousTenure) {
                    int[] previousTenure;
                    previousTenure = getPreviousTenure(
                            nationalInsuranceNumber,
                            nationalInsuranceNumberByTenures,
                            index,
                            include);
                    tenure0 = previousTenure[0];
                    tenureType0 = getTenancyTypeGroup(
                            regulatedGroups,
                            unregulatedGroups,
                            tenure0);
                } else {
                    tenureType0 = "-999";
                }
            } else {
                if (tenure0 == -999) {
                    if (checkPreviousTenure) {
                        int[] previousTenure;
                        previousTenure = getPreviousTenure(
                                nationalInsuranceNumber,
                                nationalInsuranceNumberByTenures,
                                index,
                                include);
                        tenure0 = previousTenure[0];
                    }
                }
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
                if (result.containsKey(tenureType1)) {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = result.get(tenureType1);
                    Generic_Collections.addToTreeMapStringInteger(tenureCount, tenureType0, 1);
                } else {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = new TreeMap<String, Integer>();
                    tenureCount.put(tenureType0, 1);
                    result.put(tenureType1, tenureCount);
                }
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
            }
            if (!tenureType0.equalsIgnoreCase(tenureType1)) {
                String tenureChange;
                tenureChange = getTenureChange(tenureType0, tenureType1);
                recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
            }
            if (result.containsKey(tenureType1)) {
                TreeMap<String, Integer> tenureCount;
                tenureCount = result.get(tenureType1);
                Generic_Collections.addToTreeMapStringInteger(
                        tenureCount, tenureType0, 1);
            } else {
                TreeMap<String, Integer> tenureCount;
                tenureCount = new TreeMap<String, Integer>();
                tenureCount.put(tenureType0, 1);
                result.put(tenureType1, tenureCount);
            }
        }
        Set<String> set;
        set = nationalInsuranceNumberByTenure1.keySet();
        ite = nationalInsuranceNumberByTenure0.keySet().iterator();
        while (ite.hasNext()) {
            String nationalInsuranceNumber;
            nationalInsuranceNumber = ite.next();
            if (!set.contains(nationalInsuranceNumber)) {
                Integer tenure0;
                tenure0 = nationalInsuranceNumberByTenure0.get(
                        nationalInsuranceNumber);
                String tenureType0;
                tenureType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenure0);
                String tenureType1;
                tenureType1 = "-999";
                if (!tenureType0.equalsIgnoreCase(tenureType1)) {
                    String tenureChange;
                    tenureChange = getTenureChange(tenureType0, tenureType1);
                    recordTenureChanges(nationalInsuranceNumber, tenureChanges, year, month, tenureChange);
                }
                if (result.containsKey(tenureType1)) {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = result.get(tenureType1);
                    Generic_Collections.addToTreeMapStringInteger(
                            tenureCount, tenureType0, 1);
                } else {
                    TreeMap<String, Integer> tenureCount;
                    tenureCount = new TreeMap<String, Integer>();
                    tenureCount.put(tenureType0, 1);
                    result.put(tenureType1, tenureCount);
                }
            }
        }
        return result;
    }

    public void writeTenancyTypeTransitionMatrix(
            TreeMap<Integer, TreeMap<Integer, Integer>> tenureMatrix,
            String year0,
            String month0,
            String year1,
            String month1,
            String type,
            String type2,
            String type3,
            boolean checkPreviousTenure) {
        File dir;
        dir = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                type,
                checkPreviousTenure);
        dir = new File(
                dir,
                type2);
        dir = new File(
                dir,
                type3);
        dir.mkdirs();
        File fout;
        fout = new File(
                dir,
                "TenancyTypeTransition_" + type
                + "_Start_" + year0 + month0
                + "_End_" + year1 + month1 + ".csv");
        ArrayList<Integer> tenureTypes;
        tenureTypes = DW_SHBE_Handler.getTenureTypeAll();
        PrintWriter pw;
        try {
            pw = new PrintWriter(fout);
            String line;
            line = "TenureNow|TenureBefore";
            Iterator<Integer> ite;
            ite = tenureTypes.iterator();
            while (ite.hasNext()) {
                line += "," + ite.next().toString();
            }
            line += ",-999";
            pw.println(line);
            ite = tenureTypes.iterator();
            while (ite.hasNext()) {
                Integer tenure1;
                tenure1 = ite.next();
                line = tenure1.toString();
                TreeMap<Integer, Integer> tenureCounts;
                tenureCounts = tenureMatrix.get(tenure1);
                if (tenureCounts == null) {
                    for (int i = 0; i < tenureTypes.size(); i++) {
                        line += ",0";
                    }
                    line += ",0";
                } else {
                    Iterator<Integer> ite2;
                    ite2 = tenureTypes.iterator();
                    while (ite2.hasNext()) {
                        Integer tenure0;
                        tenure0 = ite2.next();
                        Integer count;
                        count = tenureCounts.get(tenure0);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    Integer tenure0 = -999;
                    Integer nullCount = tenureCounts.get(tenure0);
                    if (nullCount == null) {
                        line += ",0";
                    } else {
                        line += "," + nullCount.toString();
                    }
                }
                pw.println(line);
            }

            TreeMap<Integer, Integer> tenureCounts;
            tenureCounts = tenureMatrix.get(-999);
            line = "-999";
            if (tenureCounts == null) {
                for (Integer tenureType : tenureTypes) {
                    line += ",0";
                }
                line += ",0";
            } else {
                Iterator<Integer> ite2;
                ite2 = tenureTypes.iterator();
                while (ite2.hasNext()) {
                    Integer tenure0;
                    tenure0 = ite2.next();
                    Integer count;
                    count = tenureCounts.get(tenure0);
                    if (count == null) {
                        line += ",0";
                    } else {
                        line += "," + count.toString();
                    }
                }
                Integer tenure0 = -999;
                Integer nullCount = tenureCounts.get(tenure0);
                if (nullCount == null) {
                    line += ",0";
                } else {
                    line += nullCount.toString();
                }
            }
            pw.println(line);
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_DataProcessor_LCC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeTenancyTypeTransitionMatrixGrouped(
            TreeMap<String, TreeMap<String, Integer>> tenureMatrix,
            ArrayList<String> tenureTypesGrouped,
            String year0,
            String month0,
            String year1,
            String month1,
            String type,
            String type2,
            String type3,
            boolean checkPreviousTenure) {
        File dir;
        dir = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                type,
                checkPreviousTenure);
        dir = new File(
                dir,
                type2);
        dir = new File(
                dir,
                type3);
        dir.mkdirs();
        File fout;
        fout = new File(
                dir,
                "TenancyTypeTransitionGrouped_" + type
                + "_Start_" + year0 + month0
                + "_End_" + year1 + month1 + ".csv");

//        tenureTypes = DW_SHBE_Handler.getTenureTypeAll();
        PrintWriter pw;
        try {
            pw = new PrintWriter(fout);
            String line;
            line = "TenureNow|TenureBefore";
            Iterator<String> ite;
            ite = tenureTypesGrouped.iterator();
            while (ite.hasNext()) {
                line += "," + ite.next();
            }
            pw.println(line);
            ite = tenureTypesGrouped.iterator();
            while (ite.hasNext()) {
                String tenure1;
                tenure1 = ite.next();
                line = tenure1;
                TreeMap<String, Integer> tenureCounts;
                tenureCounts = tenureMatrix.get(tenure1);
                if (tenureCounts == null) {
                    for (String tenureTypesGrouped1 : tenureTypesGrouped) {
                        line += ",0";
                    }
                } else {
                    Iterator<String> ite2;
                    ite2 = tenureTypesGrouped.iterator();
                    while (ite2.hasNext()) {
                        String tenure0;
                        tenure0 = ite2.next();
                        Integer count;
                        count = tenureCounts.get(tenure0);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
//                    String tenure0 = "-999";
//                    Integer nullCount = tenureCounts.get(tenure0);
//                    if (nullCount == null) {
//                        line += ",0";
//                    } else {
//                        line += "," + nullCount.toString();
//                    }
                }
                pw.println(line);
            }
//            TreeMap<String, Integer> tenureCounts;
//            tenureCounts = tenureMatrix.get("-999");
//            line = "-999";
//            if (tenureCounts == null) {
//                for (String tenureTypeGroup : tenureTypesGrouped) {
//                    line += ",0";
//                }
//            } else {
//                Iterator<String> ite2;
//                ite2 = tenureTypesGrouped.iterator();
//                while (ite2.hasNext()) {
//                    String tenure0;
//                    tenure0 = ite2.next();
//                    Integer count;
//                    count = tenureCounts.get(tenure0);
//                    if (count == null) {
//                        line += ",0";
//                    } else {
//                        line += "," + count.toString();
//                    }
//                }
//                String tenure0 = "-999";
//                Integer nullCount = tenureCounts.get(tenure0);
//                if (nullCount == null) {
//                    line += ",0";
//                } else {
//                    line += nullCount.toString();
//                }
//            }
//            pw.println(line);
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_DataProcessor_LCC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
            TreeMap<String, DW_SHBE_D_Record> DRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEData[0];
            Iterator<String> ite = DRecords.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                DW_SHBE_D_Record DRecord = DRecords.get(claimID);
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
     * result[1] is a <code>TreeSet\<String\></code> of origins/destinations;
     * ----
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
        TreeMap<String, DW_SHBE_D_Record> DRecordsStart = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_D_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record startDRecord = DRecordsStart.get(councilTaxClaimNumber);
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

                DW_SHBE_D_Record endDRecord = DRecordsEnd.get(councilTaxClaimNumber);
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
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
     * result[0] is a {@code TreeMap<Integer, TreeMap<String, Integer>>} Matrix
     * where: keys are the starts or origins; and values Maps with keys being
     * the ends or destinations and values being the counts;--------------------
     * result[1] is a {@code TreeSet<String>} of origins/destinations;
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
        TreeMap<String, DW_SHBE_D_Record> startDRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_D_Record> endDRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = startDRecords.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record startDRecord = startDRecords.get(councilTaxClaimNumber);
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                DW_SHBE_D_Record endDRecord = endDRecords.get(councilTaxClaimNumber);
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
            DW_SHBE_D_Record endDRecord = endDRecords.get(councilTaxClaimNumber);
            if (endDRecord != null) {
                DW_SHBE_D_Record DRecordStart = startDRecords.get(councilTaxClaimNumber);
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

            TreeMap<String, DW_SHBE_S_Record> SRecordsWithoutDRecord;
            SRecordsWithoutDRecord = (TreeMap<String, DW_SHBE_S_Record>) SHBESet[1];
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
                DW_SHBE_Record record;
                record = handler.getRecord(councilTaxClaimNumber);
                DW_SHBE_D_Record DRecord;
                DRecord = record.getDRecord();
                if (DRecord == null) {
                    System.out.println("Warning: No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                    countMissingDRecords++;
                    DW_SHBE_S_Record SRecord = SRecordsWithoutDRecord.get(councilTaxClaimNumber);
                    if (SRecord != null) {
                        int dosomething = 1;
                        System.out.println("There is a SRecord without a DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
                    }
                } else {
                    countNotMissingDRecords++;
                    String postcode = DRecord.getClaimantsPostcode();
                    String truncatedPostcode = postcode.substring(0, postcode.length() - 2);
                    int SRecordCount = record.getSRecords().size();
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
     * @param SHBE_Sets
     * @param underOccupiedReport_Sets
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
        lookupFromPostcodeToCensusCode = getLookupFromPostcodeToLevelCode(
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

            String[] allSHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
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
            writeMigrationMatrix(
                    allSHBEFilenames,
                    migrationData,
                    startIndex,
                    endIndex,
                    type);
        }
    }

    /**
     *
     * @param SHBEData
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
        String[] allSHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
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
        type = "All";
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
        writeMigrationMatrix(
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
        writeMigrationMatrix(
                allSHBEFilenames,
                migrationData,
                startIndex,
                endIndex,
                type);
        return result;
    }

    /**
     *
     * @param allSHBEFilenames
     * @param migrationData
     * @param startIndex
     * @param endIndex
     * @param type
     */
    public void writeMigrationMatrix(
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
        File dir;
        dir = new File(
                DW_Files.getOutputSHBETablesDir(),
                "Migration");
        dir = new File(
                dir,
                type);
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                "Migration_" + type + "_Start_" + startMonth + "" + startYear + "_End_" + endMonth + "" + endYear + ".csv");
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

//     public void writeMigrationMatrix(
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
//        PrintWriter printWriter = init_OutputTextFilePrintWriter("Migration_" + type + "_Start_" + startMonth + "" + startYear + "_End_" + endMonth + "" + endYear + ".csv");
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
     * @return Object[] result where: result[0] is a
     * {@code TreeMap<String, TreeMap<String, Integer>>} Migration Matrix;
     * result[1] is a {@code TreeSet<String>} of origins/destinations.
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
        TreeMap<String, DW_SHBE_D_Record> DRecordsStart = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_D_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        int stayPutCount = 0;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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

                DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
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
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
     * @return Object[] result where: result[0] is a
     * {@code TreeMap<String, TreeMap<String, Integer>>} Migration Matrix;
     * result[1] is a {@code TreeSet<String>} of origins/destinations.
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
        TreeMap<String, DW_SHBE_D_Record> DRecordsStart = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataStart[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsStart = (TreeMap<String, DW_SHBE_Record>) SHBEDataStart[1];
        Object[] SHBEDataEnd = loadSHBEData(tSHBEfilenames[endIndex]);
        TreeMap<String, DW_SHBE_D_Record> DRecordsEnd = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataEnd[0];
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
                    DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
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
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber);
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber);
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
     * @param date1
     * @param date2
     * @param SHBESet
     * @param SHBESet2
     * @param underOccupiedReportSet
     * @param underOccupiedReportSet2
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
            DW_SHBE_D_Record DRecordMonth2;
//            DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
            DRecordMonth2 = DRecordsMonth2.getRecord(councilTaxClaimNumber).getDRecord();
            if (DRecordMonth2 == null) {
                countOfUnderOccupancyClaimantsThatAreNoLongerClaimantsInLeeds++;
            } else {
                DW_SHBE_D_Record DRecordMonth1;
//                DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
                DRecordMonth1 = DRecordsMonth1.getRecord(councilTaxClaimNumber).getDRecord();
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
            DW_SHBE_D_Record DRecordMonth1;
//                DRecordMonth1 = DRecordsMonth1.get(councilTaxClaimNumber);
            DRecordMonth1 = DRecordsMonth1.getRecord(councilTaxClaimNumber).getDRecord();
            DW_SHBE_D_Record DRecordMonth2;
//            DRecordMonth2 = DRecordsMonth2.get(councilTaxClaimNumber);
            DRecordMonth2 = DRecordsMonth2.getRecord(councilTaxClaimNumber).getDRecord();
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
     * @return Object[9] result where: null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null null null null null null null null null null     {@code
     * result[0] = TreeMap<String,DW_SHBE_Record> representing records with
     * DRecords;
     * result[1] is a TreeMap<String, DW_SHBE_Record> representing records
     * without DRecords;
     * result[2] is a HashSet<String> of ClaimantNationalInsuranceNumberIDs;
     * result[3] is a HashSet<String> of PartnerNationalInsuranceNumberIDs;
     * result[4] is a HashSet<String> of DependentsNationalInsuranceNumberIDs;
     * result[5] is a HashSet<String> of NonDependentsNationalInsuranceNumberIDs;
     * result[6] is a HashSet<String> of AllHouseholdNationalInsuranceNumberIDs;
     * result[7] is a HashMap<String, String> of NationalInsuranceNumberIDsToPostcode;
     * result[8] is a HashMap<String, Integer> of NationalInsuranceNumberIDsToTenure.
     * }
     */
    public Object[] loadSHBEData(String filename) {
        System.out.println("Loading SHBE from " + filename);
        Object[] result = tDW_SHBE_Handler.loadInputData(
                DW_Files.getInputSHBEDir(),
                filename);
        return result;
    }

    /**
     * Writes to files aggregated details about rent arrears totals and by
     * aggregated postcode.
     *
     * @param date1
     * @param date2
     * @param SHBESet
     * @param SHBESet2
     * @param underOccupiedReportSet
     * @param underOccupiedReportSet2
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
        DW_SHBE_CollectionHandler records;
        records = (DW_SHBE_CollectionHandler) SHBESet[0];
//        TreeMap<String, DW_SHBE_Record> DRecords;
//        DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
        TreeMap<String, DW_SHBE_S_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_S_Record>) SHBESet2[1];
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
            DW_SHBE_Record record;
            record = records.getRecord(councilTaxClaimNumber);
            DW_SHBE_D_Record DRecord;
//            DRecord = DRecords.get(councilTaxClaimNumber);
            DRecord = record.getDRecord();
            if (DRecord == null) {
                System.out.println("No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_Record);
                countMissingDRecords++;
                DW_SHBE_S_Record SRecord = SRecords.get(councilTaxClaimNumber);
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
                int SRecordCount = record.getSRecords().size();
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
            TreeMap<String, DW_SHBE_D_Record> DRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataMonth1[0];
            TreeMap<String, DW_SHBE_D_Record> SRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEDataMonth1[1];
            // Iterate over records and join these with SHBE records to get postcodes
            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
            result.put(monthString, postcodeClaims);
            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
//            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                DW_SHBE_D_Record DRecord = DRecords.get(councilTaxClaimNumber);
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

        TreeMap<String, DW_SHBE_S_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_S_Record>) SHBESet[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> aggregatedClaims = new TreeMap<String, Integer>();
        result.put(date, aggregatedClaims);
        Iterator<String> ite = underOccupiedReport_Set.getSet().keySet().iterator();
        int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecord;
//            DRecord = DRecords.get(councilTaxClaimNumber);
            DRecord = DRecords.getRecord(councilTaxClaimNumber).getDRecord();
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
            TreeMap<String, DW_SHBE_D_Record> DRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBEData[0];
            //TreeMap<String, DW_SHBE_Record> SRecordsWithoutDRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[1];
            Iterator<String> ite = DRecords.keySet().iterator();
            String postcode;
            while (ite.hasNext()) {
                String councilTaxClaimNumber = ite.next();
                DW_SHBE_D_Record rec = DRecords.get(councilTaxClaimNumber);
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

        TreeMap<String, DW_SHBE_D_Record> SRecords;
        SRecords = (TreeMap<String, DW_SHBE_D_Record>) SHBESet[1];
        int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;
        TreeMap<String, Integer> monthsResult = new TreeMap<String, Integer>();
        result.put(date, monthsResult);

        Iterator<String> ite;
        //ite = DRecords.keySet().iterator();
        ite = DRecords.lookup.keySet().iterator();
        String postcode;
        while (ite.hasNext()) {
            String councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record rec;
//            rec = DRecords.get(councilTaxClaimNumber);
            rec = DRecords.getRecord(councilTaxClaimNumber).getDRecord();
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
        HashSet<DW_SHBE_D_Record> tSHBE_January2013_Records = (HashSet<DW_SHBE_D_Record>) tSHBE_January2013[0];
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
        HashSet<DW_SHBE_D_Record> tSHBE_February2013_Records = (HashSet<DW_SHBE_D_Record>) tSHBE_February2013[0];
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

        HashSet<DW_SHBE_D_Record> tSHBE_time1DRecords = (HashSet<DW_SHBE_D_Record>) tSHBE_time1[0];
        HashSet<DW_SHBE_D_Record> tSHBE_time1SRecords = (HashSet<DW_SHBE_D_Record>) tSHBE_time1[1];
        HashSet<String> tSHBE_time1ClaimantNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[2];
        HashSet<String> tSHBE_time1PartnerNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[3];
        HashSet<String> tSHBE_time1DependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[4];
        HashSet<String> tSHBE_time1NonDependentsNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[5];
        HashSet<String> tSHBE_time1AllHouseholdNationalInsuranceNumberIDs = (HashSet<String>) tSHBE_time1[6];

        HashSet<DW_SHBE_D_Record> tSHBE_time2DRecords = (HashSet<DW_SHBE_D_Record>) tSHBE_time2[0];
        HashSet<DW_SHBE_D_Record> tSHBE_time2SRecords = (HashSet<DW_SHBE_D_Record>) tSHBE_time2[1];
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
