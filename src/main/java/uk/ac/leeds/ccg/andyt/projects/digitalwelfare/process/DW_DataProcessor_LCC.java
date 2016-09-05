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
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
//import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_intID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.Summary;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.SummaryUO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.TenancyChangesUO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_PersonID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Collection;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_CollectionHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler.sPaymentTypeAll;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler.sPaymentTypeIn;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler.sPaymentTypeOther;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler.sPaymentTypeSuspended;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_S_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType_PostcodeID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.ID_TenancyType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UOReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts.DW_LineGraph;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_DataProcessor_LCC extends DW_AbstractProcessor {

    String sU = "U";
    //String sUOInApril2013 = "UOInApril2013";
    String sUOInApril2013 = "UInApr13";
    //String sTenancyChange = "TenancyChange";
    String sTenancyChange = "TC";
    //String sWithOrWithoutPostcodeChange = "WithOrWithoutPostcodeChange";
    public static final String sWithOrWithoutPostcodeChange = "MPC"; // Maybe Postcode Changed 
    String sAll = "All";
    String sMinus999 = "-999";
    String sAAN_NAA = "AAN_NAA";

    String sOA = "OA";
    String sLSOA = "LSOA";
    
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
    
    String sInDistanceChurn = "InDistanceChurn";
    String sWithinDistanceChurn = "WithinDistanceChurn";
    String sOutDistanceChurn = "OutDistanceChurn";
    
    private DW_SHBE_CollectionHandler collectionHandler;
    private DW_SHBE_Handler tDW_SHBE_Handler;
    private DW_UnderOccupiedReport_Handler tDW_UnderOccupiedReport_Handler;
    //private double distanceThreshold;

    public DW_DataProcessor_LCC(DW_Environment env) {
        super(env);
        tDW_SHBE_Handler = env.getDW_SHBE_Handler();
    }

    @Override
    public void run() {
        init_handlers();
//        init_OutputTextFilePrintWriter(
//                DW_Files.getOutputSHBETablesDir(),
//                "DigitalWelfare");
//        // Load Data
//        ArrayList<Object[]> SHBEData;
//        SHBEData = tDW_SHBE_Handler.getSHBEData();

        Object[] underOccupiedData;
        underOccupiedData = DW_UnderOccupiedReport_Handler.loadUnderOccupiedReportData(env);

//        DW_SHBE_Handler DW_SHBE_Handler;
//        DW_SHBE_Handler = new DW_SHBE_Handler(env);
        HashMap<String, DW_intID> tPostcodeToPostcodeIDLookup;
        tPostcodeToPostcodeIDLookup = tDW_SHBE_Handler.getPostcodeToPostcodeIDLookup();
//        reportUnderOccupancyTotals(underOccupiedData);
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

        boolean handleOutOfMemoryError = false;

        String[] SHBEFilenames;
        SHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();

        ArrayList<String> claimantTypes;
        claimantTypes = tDW_SHBE_Handler.getClaimantTypes();

        ArrayList<String> levels;
        levels = new ArrayList<String>();
        levels.add(sOA);
        levels.add(sLSOA);
//        levels.add("MSOA");
//        levels.add("PostcodeUnit");
//        levels.add("PostcodeSector");
//        levels.add("PostcodeDistrict");

        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add(sAllClaimants); // Count of all claimants
//        types.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add(sOnFlow); // These are people not claiming the previous month and that have not claimed before.
        types.add(sReturnFlow); // These are people not claiming the previous month but that have claimed before.
        types.add(sStable); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add(sAllInChurn); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add(sAllOutChurn); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        ArrayList<String> distanceTypes;
        distanceTypes = new ArrayList<String>();
        distanceTypes.add(sInDistanceChurn); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add(sWithinDistanceChurn); // A count of all claimants that have moved within this area.
        distanceTypes.add(sOutDistanceChurn); // A count of all claimants that have moved out from this area.

        HashMap<Boolean, ArrayList<String>> tenancyTypes;
        tenancyTypes = new HashMap<Boolean, ArrayList<String>>();
        tenancyTypes.put(Boolean.TRUE, DW_SHBE_TenancyType_Handler.getTenancyTypeAll(Boolean.TRUE));
        tenancyTypes.put(Boolean.FALSE, DW_SHBE_TenancyType_Handler.getTenancyTypeAll(Boolean.FALSE));
        Object[] ttgs = DW_SHBE_TenancyType_Handler.getTenancyTypeGroups();
        HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups;
        tenancyTypeGroups = (HashMap<Boolean, TreeMap<String, ArrayList<String>>>) ttgs[0];
        HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped;
        tenancyTypesGrouped = (HashMap<Boolean, ArrayList<String>>) ttgs[1];
        HashMap<Boolean, ArrayList<Integer>> regulatedGroups;
        regulatedGroups = (HashMap<Boolean, ArrayList<Integer>>) ttgs[2];
        HashMap<Boolean, ArrayList<Integer>> unregulatedGroups;
        unregulatedGroups = (HashMap<Boolean, ArrayList<Integer>>) ttgs[3];

        // Includes
        TreeMap<String, ArrayList<Integer>> includes;

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }
        boolean loadData;
        loadData = false;
//        loadData = true;

        Generic_UKPostcode_Handler postcodeHandler;
        postcodeHandler = new Generic_UKPostcode_Handler();

        ArrayList<Boolean> bArray;
        bArray = new ArrayList<Boolean>();
        bArray.add(true);
        bArray.add(false);
        Iterator<Boolean> iteB;

        HashMap<DW_PersonID, DW_intID> tDW_PersonIDtoDW_IDLookup;
        tDW_PersonIDtoDW_IDLookup = tDW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
        ArrayList<String> paymentTypes;
        Iterator<String> paymentTypesIte;

        Object[] table;
        table = null;

        boolean doGrouped;
        boolean doTenancyChangesUO;
        boolean doSummaryTables;
        boolean doSummaryTablesAllOnly;
        boolean doSummaryTablesUOOnly;
        boolean doTenancyTransitions;
        boolean doPostcodeChanges;
        boolean doTenancyChanges;
        boolean doTenancyAndPostcodeChanges;
        boolean doAggregation;
        boolean doHBGeneralAggregateStatistics;

        doGrouped = true;
        doGrouped = false;
        doTenancyChangesUO = true;
        doTenancyChangesUO = false;
        doSummaryTables = true;
        doSummaryTables = false;
        doSummaryTablesAllOnly = true;
        doSummaryTablesAllOnly = false;
        doSummaryTablesUOOnly = true;
        doSummaryTablesUOOnly = false;

        doTenancyTransitions = true;
//        doTenancyTransitions = false;
        doPostcodeChanges = true;
//        doPostcodeChanges = false;
        doTenancyChanges = true;
//        doTenancyChanges = false;
        doTenancyAndPostcodeChanges = true;
//        doTenancyAndPostcodeChanges = false;

        doAggregation = true;
        doAggregation = false;

        doHBGeneralAggregateStatistics = true;
        doHBGeneralAggregateStatistics = false;

        if (doTenancyChangesUO) {
            includes = tDW_SHBE_Handler.getIncludes();
//            includes.remove(tDW_SHBE_Handler.sIncludeAll);
//            includes.remove(tDW_SHBE_Handler.sIncludeYearly);
//            includes.remove(tDW_SHBE_Handler.sInclude6Monthly);
//            includes.remove(tDW_SHBE_Handler.sInclude3Monthly);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthly);
            paymentTypes = tDW_Strings.getPaymentTypes();
//            paymentTypes.remove(sPaymentTypeAll);
//            paymentTypes.remove(sPaymentTypeIn);
//            paymentTypes.remove(sPaymentTypeSuspended);
//            paymentTypes.remove(sPaymentTypeOther);
            boolean doUnderOccupancy;
            doUnderOccupancy = true;
            String name;
            name = sTenancyChange;
            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                TenancyChangesUO tTenancyChangesUO = new TenancyChangesUO(
                        env,
                        collectionHandler,
                        tDW_SHBE_Handler,
                        tPostcodeToPostcodeIDLookup,
                        handleOutOfMemoryError);
                Iterator<String> includesIte;
                includesIte = includes.keySet().iterator();
                while (includesIte.hasNext()) {
                    String includeKey;
                    includeKey = includesIte.next();
                    ArrayList<Integer> include;
                    include = includes.get(includeKey);
                    int index;
                    String startMonth;
                    String startYear;
                    String endMonth;
                    String endYear;
                    index = include.get(0);
                    startMonth = tDW_SHBE_Handler.getMonth(
                            SHBEFilenames[index]);
                    startYear = tDW_SHBE_Handler.getYear(
                            SHBEFilenames[index]);
                    index = include.get(include.size() - 1);
                    endMonth = tDW_SHBE_Handler.getMonth(
                            SHBEFilenames[index]);
                    endYear = tDW_SHBE_Handler.getYear(
                            SHBEFilenames[index]);
                    iteB = bArray.iterator();
                    while (iteB.hasNext()) {
                        boolean includePreUnderOccupancyValues;
                        includePreUnderOccupancyValues = iteB.next();
                        table = tTenancyChangesUO.getTable(
                                underOccupiedData,
                                SHBEFilenames,
                                include,
                                paymentType,
                                includePreUnderOccupancyValues);
                        tTenancyChangesUO.writeTenancyChangeTables(
                                table,
                                paymentType,
                                includeKey,
                                doUnderOccupancy,
                                includePreUnderOccupancyValues,
                                name,
                                startMonth,
                                startYear,
                                endMonth,
                                endYear);
                    }
//                    System.exit(0);
                }
            }
        }

        // Summary Tables
        boolean forceNewSummaries;
        includes = tDW_SHBE_Handler.getIncludes();
//        includes.remove(tDW_SHBE_Handler.sIncludeAll);
//        includes.remove(tDW_SHBE_Handler.sIncludeYearly);
//        includes.remove(tDW_SHBE_Handler.sInclude6Monthly);
//        includes.remove(tDW_SHBE_Handler.sInclude3Monthly);
//        includes.remove(tDW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//        includes.remove(tDW_SHBE_Handler.sIncludeMonthly);
        paymentTypes = tDW_Strings.getPaymentTypes();
//        paymentTypes.remove(sPaymentTypeAll);
//        paymentTypes.remove(sPaymentTypeIn);
//        paymentTypes.remove(sPaymentTypeSuspended);
//        paymentTypes.remove(sPaymentTypeOther);
//        forceNewSummaries = false;
        forceNewSummaries = true;
        // Runtime approximately 2 hours.
        if (doSummaryTables) {
            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                //DW_PersonIDtoDW_IDLookup = tDW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
                int nTT;
                nTT = tDW_SHBE_Handler.getNumberOfTenancyTypes();
                int nEG;
                //nEG = DW_SHBE_Handler.getNumberOfClaimantsEthnicGroups();
                nEG = tDW_SHBE_Handler.getNumberOfClaimantsEthnicGroupsGrouped();
                int nPSI;
                nPSI = tDW_SHBE_Handler.getOneOverMaxValueOfPassportStandardIndicator();
                Summary tSummary = new Summary(
                        env,
                        collectionHandler,
                        nTT,
                        nEG,
                        nPSI,
                        handleOutOfMemoryError);
                SummaryUO tSummaryUO = new SummaryUO(
                        env,
                        collectionHandler,
                        nTT,
                        nEG,
                        nPSI,
                        handleOutOfMemoryError);
                Iterator<String> includesIte;
                includesIte = includes.keySet().iterator();
                while (includesIte.hasNext()) {
                    String includeKey;
                    includeKey = includesIte.next();
                    ArrayList<Integer> include;
                    include = includes.get(includeKey);
                    boolean doUnderOccupancy;
                    TreeMap<String, HashMap<String, String>> summaryTableAll;
                    summaryTableAll = tSummary.getSummaryTable(
                            SHBEFilenames,
                            include,
                            forceNewSummaries,
                            paymentType,
                            nTT,
                            nEG,
                            nPSI,
                            //underOccupiedData,
                            handleOutOfMemoryError);
                    doUnderOccupancy = false;
                    tSummary.writeSummaryTables(
                            summaryTableAll,
                            paymentType,
                            includeKey,
                            doUnderOccupancy,
                            nTT, nEG, nPSI);
                    TreeMap<String, HashMap<String, String>> summaryTableUO;
                    summaryTableUO = tSummaryUO.getSummaryTable(
                            summaryTableAll,
                            SHBEFilenames,
                            include,
                            forceNewSummaries,
                            paymentType,
                            nTT,
                            nEG,
                            nPSI,
                            underOccupiedData,
                            tDW_PersonIDtoDW_IDLookup,
                            tPostcodeToPostcodeIDLookup,
                            handleOutOfMemoryError);
                    doUnderOccupancy = true;
                    tSummaryUO.writeSummaryTables(
                            summaryTableUO,
                            paymentType,
                            includeKey,
                            doUnderOccupancy,
                            nTT, nEG, nPSI);
                }
            }
        }

        // Summary Tables
        includes = tDW_SHBE_Handler.getIncludes();
//        includes.remove(tDW_SHBE_Handler.sIncludeAll);
//        includes.remove(tDW_SHBE_Handler.sIncludeYearly);
//        includes.remove(tDW_SHBE_Handler.sInclude6Monthly);
//        includes.remove(tDW_SHBE_Handler.sInclude3Monthly);
//        includes.remove(tDW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//        includes.remove(tDW_SHBE_Handler.sIncludeMonthly);
        paymentTypes = tDW_Strings.getPaymentTypes();
//        paymentTypes.remove(sPaymentTypeAll);
//        paymentTypes.remove(sPaymentTypeIn);
//        paymentTypes.remove(sPaymentTypeSuspended);
//        paymentTypes.remove(sPaymentTypeOther);
//        forceNewSummaries = false;
        forceNewSummaries = true;
        // Runtime approximately 2 hours.
        if (doSummaryTablesAllOnly) {
            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                //DW_PersonIDtoDW_IDLookup = tDW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
                int nTT;
                nTT = tDW_SHBE_Handler.getNumberOfTenancyTypes();
                int nEG;
                //nEG = DW_SHBE_Handler.getNumberOfClaimantsEthnicGroups();
                nEG = tDW_SHBE_Handler.getNumberOfClaimantsEthnicGroupsGrouped();
                int nPSI;
                nPSI = tDW_SHBE_Handler.getOneOverMaxValueOfPassportStandardIndicator();
                Summary tSummary = new Summary(
                        env,
                        collectionHandler,
                        nTT,
                        nEG,
                        nPSI,
                        handleOutOfMemoryError);
                Iterator<String> includesIte;
                includesIte = includes.keySet().iterator();
                while (includesIte.hasNext()) {
                    String includeKey;
                    includeKey = includesIte.next();
                    ArrayList<Integer> include;
                    include = includes.get(includeKey);
                    boolean doUnderOccupancy;
                    TreeMap<String, HashMap<String, String>> summaryTableAll;
                    summaryTableAll = null;
                    summaryTableAll = tSummary.getSummaryTable(
                            SHBEFilenames,
                            include,
                            forceNewSummaries,
                            paymentType,
                            nTT,
                            nEG,
                            nPSI,
                            //underOccupiedData,
                            handleOutOfMemoryError);
                    doUnderOccupancy = false;
                    tSummary.writeSummaryTables(
                            summaryTableAll,
                            paymentType,
                            includeKey,
                            doUnderOccupancy,
                            nTT, nEG, nPSI);
                }
            }
        }

        if (doSummaryTablesUOOnly) {
            // Summary Tables
            includes = tDW_SHBE_Handler.getIncludes();
//            includes.remove(tDW_SHBE_Handler.sIncludeAll);
//            includes.remove(tDW_SHBE_Handler.sIncludeYearly);
//            includes.remove(tDW_SHBE_Handler.sInclude6Monthly);
//            includes.remove(tDW_SHBE_Handler.sInclude3Monthly);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthly);
            paymentTypes = tDW_Strings.getPaymentTypes();
//            paymentTypes.remove(sPaymentTypeAll);
//            paymentTypes.remove(sPaymentTypeIn);
//            paymentTypes.remove(sPaymentTypeSuspended);
//            paymentTypes.remove(sPaymentTypeOther);
            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                //DW_PersonIDtoDW_IDLookup = tDW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
                int nTT;
                nTT = tDW_SHBE_Handler.getNumberOfTenancyTypes();
                int nEG;
                //nEG = DW_SHBE_Handler.getNumberOfClaimantsEthnicGroups();
                nEG = tDW_SHBE_Handler.getNumberOfClaimantsEthnicGroupsGrouped();
                int nPSI;
                nPSI = tDW_SHBE_Handler.getOneOverMaxValueOfPassportStandardIndicator();
                SummaryUO tSummaryUO = new SummaryUO(
                        env,
                        collectionHandler,
                        nTT,
                        nEG,
                        nPSI,
                        handleOutOfMemoryError);
                Iterator<String> includesIte;
                includesIte = includes.keySet().iterator();
                while (includesIte.hasNext()) {
                    String includeKey;
                    includeKey = includesIte.next();
                    ArrayList<Integer> include;
                    include = includes.get(includeKey);
                    boolean doUnderOccupancy;
                    TreeMap<String, HashMap<String, String>> summaryTableAll;
                    summaryTableAll = null;
                    TreeMap<String, HashMap<String, String>> summaryTableUO;
                    summaryTableUO = tSummaryUO.getSummaryTable(
                            summaryTableAll,
                            SHBEFilenames,
                            include,
                            forceNewSummaries,
                            paymentType,
                            nTT,
                            nEG,
                            nPSI,
                            underOccupiedData,
                            tDW_PersonIDtoDW_IDLookup,
                            tPostcodeToPostcodeIDLookup,
                            handleOutOfMemoryError);
                    doUnderOccupancy = true;
                    tSummaryUO.writeSummaryTables(
                            summaryTableUO,
                            paymentType,
                            includeKey,
                            doUnderOccupancy,
                            nTT, nEG, nPSI);
//                    System.exit(0);
                }
            }
        }

//        System.exit(0);
//        paymentTypes.remove(sPaymentTypeAll);
////        paymentTypes.remove(sPaymentTypeIn);
//        paymentTypes.remove(sPaymentTypeSuspended);
//        paymentTypes.remove(sPaymentTypeOther);
// Postcode and Tenancy Type transitions 
        // Runtime approximately 1 hour 5 minutes.
        if (doTenancyTransitions) {
            includes = tDW_SHBE_Handler.getIncludes();
//            includes.remove(tDW_SHBE_Handler.sIncludeAll);
//            includes.remove(tDW_SHBE_Handler.sIncludeYearly);
//            includes.remove(tDW_SHBE_Handler.sInclude6Monthly);
//            includes.remove(tDW_SHBE_Handler.sInclude3Monthly);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthlySinceApril2013);
//            includes.remove(tDW_SHBE_Handler.sIncludeMonthly);

            HashSet<String> tCTBRefs0;

            tCTBRefs0 = DW_UnderOccupiedReport_Handler.getUnderOccupiedInApril2013CTBRefs(env); // run UOApril2013
//            tCTBRefs0 = DW_UnderOccupiedReport_Handler.getUnderOccupiedCTBRefs(); // AHH!!!
//            tCTBRefs0.removeAll(DW_UnderOccupiedReport_Handler.getUnderOccupiedInApril2013CTBRefs());

            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                //NINOtoIDLookup = DW_SHBE_Handler.getDW_PersonIDToDW_IDLookup(paymentType);
                // Postcode Changes (same tenancy)
                if (doPostcodeChanges) {
                    iteB = bArray.iterator();
                    while (iteB.hasNext()) {
                        boolean checkPreviousTenure;
                        checkPreviousTenure = iteB.next();
                        System.out.println("CheckPreviousTenure " + checkPreviousTenure);
                        Iterator<Boolean> iteB2;
                        iteB2 = bArray.iterator();
                        while (iteB2.hasNext()) {
                            boolean reportTenancyTransitionBreaks;
                            reportTenancyTransitionBreaks = iteB2.next();
                            System.out.println("ReportTenancyTransitionBreaks " + reportTenancyTransitionBreaks);
                            System.out.println("PostcodeChanges");
                            boolean doUnderOccupiedData;
//                            doUnderOccupiedData = false;
//                            doUnderOccupiedData = true;
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                doUnderOccupiedData = iteB3.next();
                                if (doUnderOccupiedData) {
                                    System.out.println(sU);
                                    Iterator<Boolean> iteB5;
                                    iteB5 = bArray.iterator();
                                    while (iteB5.hasNext()) {
                                        boolean postcodeChange;
                                        postcodeChange = iteB5.next();
                                        System.out.println("postcodeChange " + postcodeChange);
                                        Iterator<Boolean> iteB6;
                                        iteB6 = bArray.iterator();
                                        while (iteB6.hasNext()) {
                                            boolean checkPreviousPostcode;
                                            checkPreviousPostcode = iteB6.next();
                                            System.out.println("checkPreviousPostcode " + checkPreviousPostcode);
                                            boolean doUOOnlyOnThoseOriginallyUO;
                                            //doUOOnlyOnThoseOriginallyUO = true;
                                            Iterator<Boolean> iteB0;
                                            iteB0 = bArray.iterator();
                                            while (iteB0.hasNext()) {
                                                doUOOnlyOnThoseOriginallyUO = iteB0.next();
                                                HashSet<String> underOccupiedInApril2013;
                                                if (doUOOnlyOnThoseOriginallyUO) {
                                                    underOccupiedInApril2013 = tCTBRefs0;
                                                } else {
                                                    underOccupiedInApril2013 = null;
                                                }
                                                postcodeChanges(
                                                        SHBEFilenames,
                                                        paymentType,
                                                        doGrouped,
                                                        tenancyTypes.get(doUnderOccupiedData),
                                                        tenancyTypesGrouped.get(doUnderOccupiedData),
                                                        regulatedGroups.get(doUnderOccupiedData),
                                                        unregulatedGroups.get(doUnderOccupiedData),
                                                        includes,
                                                        loadData,
                                                        checkPreviousTenure,
                                                        reportTenancyTransitionBreaks,
                                                        postcodeHandler,
                                                        postcodeChange,
                                                        checkPreviousPostcode,
                                                        underOccupiedData,
                                                        doUnderOccupiedData,
                                                        underOccupiedInApril2013);
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("All");
                                    Iterator<Boolean> iteB4;
                                    iteB4 = bArray.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean postcodeChange;
                                        postcodeChange = iteB4.next();
                                        System.out.println("postcodeChange " + postcodeChange);
                                        Iterator<Boolean> iteB5;
                                        iteB5 = bArray.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean checkPreviousPostcode;
                                            checkPreviousPostcode = iteB5.next();
                                            System.out.println("checkPreviousPostcode " + checkPreviousPostcode);
                                            postcodeChanges(
                                                    SHBEFilenames,
                                                    paymentType,
                                                    //tenancyTypes.get(doUnderOccupiedData),
                                                    doGrouped,
                                                    tenancyTypes.get(doUnderOccupiedData),
                                                    tenancyTypesGrouped.get(doUnderOccupiedData),
                                                    regulatedGroups.get(doUnderOccupiedData),
                                                    unregulatedGroups.get(doUnderOccupiedData),
                                                    includes,
                                                    loadData,
                                                    checkPreviousTenure,
                                                    reportTenancyTransitionBreaks,
                                                    postcodeHandler,
                                                    postcodeChange,
                                                    checkPreviousPostcode,
                                                    underOccupiedData,
                                                    doUnderOccupiedData,
                                                    null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TenancyTransitions
                boolean checkPreviousTenure;
                checkPreviousTenure = false;
                iteB = bArray.iterator();
                while (iteB.hasNext()) {
                    checkPreviousTenure = iteB.next();
                    System.out.println("CheckPreviousTenure " + checkPreviousTenure);
                    Iterator<Boolean> iteB2;
                    iteB2 = bArray.iterator();
                    while (iteB2.hasNext()) {
                        boolean reportTenancyTransitionBreaks;
                        reportTenancyTransitionBreaks = iteB2.next();
                        System.out.println("ReportTenancyTransitionBreaks " + reportTenancyTransitionBreaks);
                        if (doTenancyChanges) {
                            System.out.println("TenancyChanges");
                            boolean doUnderOccupiedData;
//                            doUnderOccupiedData = true;
//                            doUnderOccupiedData = false;
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                doUnderOccupiedData = iteB3.next();
                                if (doUnderOccupiedData) {
                                    System.out.println(sU);
                                    boolean doUOOnlyOnThoseOriginallyUO;
//                                doUOOnlyOnThoseOriginallyUO = true;
                                    Iterator<Boolean> iteB0;
                                    iteB0 = bArray.iterator();
                                    while (iteB0.hasNext()) {
                                        doUOOnlyOnThoseOriginallyUO = iteB0.next();
                                        HashSet<String> tCTBRefs;
                                        if (doUOOnlyOnThoseOriginallyUO) {
                                            tCTBRefs = tCTBRefs0;
                                        } else {
                                            tCTBRefs = null;
                                        }
                                        tenancyChanges(
                                                SHBEFilenames,
                                                paymentType,
                                                doGrouped,
                                                tenancyTypes.get(doUnderOccupiedData),
                                                tenancyTypesGrouped.get(doUnderOccupiedData),
                                                regulatedGroups.get(doUnderOccupiedData),
                                                unregulatedGroups.get(doUnderOccupiedData),
                                                includes,
                                                loadData,
                                                checkPreviousTenure,
                                                reportTenancyTransitionBreaks,
                                                underOccupiedData,
                                                doUnderOccupiedData,
                                                tCTBRefs);
                                    }
                                } else {
                                    System.out.println("All");
                                    tenancyChanges(
                                            SHBEFilenames,
                                            paymentType,
                                            doGrouped,
                                            tenancyTypes.get(doUnderOccupiedData),
                                            tenancyTypesGrouped.get(doUnderOccupiedData),
                                            regulatedGroups.get(doUnderOccupiedData),
                                            unregulatedGroups.get(doUnderOccupiedData),
                                            includes,
                                            loadData,
                                            checkPreviousTenure,
                                            reportTenancyTransitionBreaks,
                                            underOccupiedData,
                                            doUnderOccupiedData,
                                            null);
                                }
                            }
                        }
                        if (doTenancyAndPostcodeChanges) {
                            System.out.println("TenancyAndPostcodeChanges");
                            boolean doUnderOccupiedData;
//                            doUnderOccupiedData = false;
//                            doUnderOccupiedData = true;
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                doUnderOccupiedData = iteB3.next();
                                if (doUnderOccupiedData) {
                                    System.out.println(sU);
                                    Iterator<Boolean> iteB5;
                                    iteB5 = bArray.iterator();
                                    while (iteB5.hasNext()) {
                                        boolean postcodeChange;
                                        postcodeChange = iteB5.next();
                                        System.out.println("postcodeChange " + postcodeChange);
                                        Iterator<Boolean> iteB6;
                                        iteB6 = bArray.iterator();
                                        while (iteB6.hasNext()) {
                                            boolean checkPreviousPostcode;
                                            checkPreviousPostcode = iteB6.next();
                                            System.out.println("checkPreviousPostcode " + postcodeChange);
                                            boolean doUOOnlyOnThoseOriginallyUO;
                                            //doUOOnlyOnThoseOriginallyUO = true;
                                            Iterator<Boolean> iteB0;
                                            iteB0 = bArray.iterator();
                                            while (iteB0.hasNext()) {
                                                doUOOnlyOnThoseOriginallyUO = iteB0.next();
                                                HashSet<String> underOccupiedInApril2013;
                                                if (doUOOnlyOnThoseOriginallyUO) {
                                                    underOccupiedInApril2013 = tCTBRefs0;
                                                } else {
                                                    underOccupiedInApril2013 = null;
                                                }
                                                tenancyAndPostcodeChanges(
                                                        SHBEFilenames,
                                                        paymentType,
                                                        doGrouped,
                                                        tenancyTypes.get(doUnderOccupiedData),
                                                        tenancyTypesGrouped.get(doUnderOccupiedData),
                                                        regulatedGroups.get(doUnderOccupiedData),
                                                        unregulatedGroups.get(doUnderOccupiedData),
                                                        includes,
                                                        loadData,
                                                        checkPreviousTenure,
                                                        reportTenancyTransitionBreaks,
                                                        postcodeHandler,
                                                        postcodeChange,
                                                        checkPreviousPostcode,
                                                        underOccupiedData,
                                                        doUnderOccupiedData,
                                                        underOccupiedInApril2013);
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("All");
                                    Iterator<Boolean> iteB4;
                                    iteB4 = bArray.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean postcodeChange;
                                        postcodeChange = iteB4.next();
                                        System.out.println("postcodeChange " + postcodeChange);
                                        Iterator<Boolean> iteB5;
                                        iteB5 = bArray.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean checkPreviousPostcode;
                                            checkPreviousPostcode = iteB5.next();
                                            System.out.println("checkPreviousPostcode " + postcodeChange);
                                            tenancyAndPostcodeChanges(
                                                    SHBEFilenames,
                                                    paymentType,
                                                    doGrouped,
                                                    tenancyTypes.get(doUnderOccupiedData),
                                                    tenancyTypesGrouped.get(doUnderOccupiedData),
                                                    regulatedGroups.get(doUnderOccupiedData),
                                                    unregulatedGroups.get(doUnderOccupiedData),
                                                    includes,
                                                    loadData,
                                                    checkPreviousTenure,
                                                    reportTenancyTransitionBreaks,
                                                    postcodeHandler,
                                                    postcodeChange,
                                                    checkPreviousPostcode,
                                                    underOccupiedData,
                                                    doUnderOccupiedData,
                                                    null);
                                        }
                                    }
                                }
                            }
                        }
                    }
////                if (true) {
//                if (false) {
//                    File dirOut;
//                    dirOut = new File(
//                            DW_Files.getOutputSHBETablesDir(),
//                            "MultipleTenancyTypeChanges");
//                    Iterator<Boolean> iteB3;
//                    iteB3 = bArray.iterator();
//                    while (iteB3.hasNext()) {
//                        boolean doUnderOccupiedData;
//                        doUnderOccupiedData = iteB3.next();
//                        if (doUnderOccupiedData) {
//                            System.out.println(sU);
//                            Iterator<Boolean> iteB4;
//                            iteB4 = bArray.iterator();
//                            while (iteB4.hasNext()) {
//                                boolean doCouncil;
//                                doCouncil = iteB4.next();
//                                File dirOut2;
//                                dirOut2 = DW_Files.getUOFile(dirOut, doUnderOccupiedData, doCouncil);
//                                multipleTenancyChanges(
//                                        SHBEFilenames,
//                                        tenancyTypeGroups.get(doUnderOccupiedData),
//                                        tenancyTypesGrouped.get(doUnderOccupiedData),
//                                        regulatedGroups.get(doUnderOccupiedData),
//                                        unregulatedGroups.get(doUnderOccupiedData),
//                                        includes,
//                                        levels,
//                                        checkPreviousTenure,
//                                        dirOut2);
//                            }
//                        }
//                    }
//                }
                }
            }
        }
//        System.exit(0);

        if (doHBGeneralAggregateStatistics) {
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode;
            lookupsFromPostcodeToLevelCode = getLookupsFromPostcodeToLevelCode(levels);
            outputHBGeneralAggregateStatistics(
                    lookupsFromPostcodeToLevelCode,
                    SHBEFilenames,
                    paymentTypes,
                    includes,
                    levels,
                    tDW_PersonIDtoDW_IDLookup);
        }

        // Aggregate
        if (doAggregation) {
            DW_Postcode_Handler tDW_Postcode_Handler;
            tDW_Postcode_Handler = env.getDW_Postcode_Handler();
            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                //DW_SHBE_Handler.DW_PersonIDtoDW_IDLookup = DW_SHBE_Handler.getDW_PersonIDToDW_IDLookup();
                TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode; // Work needed to load the appropriate look up for the appropriate years and months of postcode!
                lookupsFromPostcodeToLevelCode = getLookupsFromPostcodeToLevelCode(levels);
                //Generic_UKPostcode_Handler.isValidPostcodeForm(String postcode)
                iteB = bArray.iterator();
                while (iteB.hasNext()) {
                    boolean doUnderOccupied;
                    doUnderOccupied = iteB.next();
                    if (doUnderOccupied) {
                        if (true) {
//                        if (false) {
                            aggregateClaimants(
                                    doUnderOccupied,
                                    true,
                                    true,
                                    underOccupiedData,
                                    lookupsFromPostcodeToLevelCode,
                                    SHBEFilenames,
                                    paymentType,
                                    claimantTypes,
                                    tenancyTypeGroups.get(doUnderOccupied),
                                    tenancyTypesGrouped.get(doUnderOccupied),
                                    regulatedGroups.get(doUnderOccupied),
                                    unregulatedGroups.get(doUnderOccupied),
                                    includes,
                                    levels,
                                    types,
                                    distanceTypes,
                                    distances,
                                    tDW_PersonIDtoDW_IDLookup);
                        }
                        aggregateClaimants(
                                doUnderOccupied,
                                true,
                                false,
                                underOccupiedData,
                                lookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                paymentType,
                                claimantTypes,
                                tenancyTypeGroups.get(doUnderOccupied),
                                tenancyTypesGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances,
                                tDW_PersonIDtoDW_IDLookup);
                        aggregateClaimants(
                                doUnderOccupied,
                                false,
                                true,
                                underOccupiedData,
                                lookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                paymentType,
                                claimantTypes,
                                tenancyTypeGroups.get(doUnderOccupied),
                                tenancyTypesGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances,
                                tDW_PersonIDtoDW_IDLookup);
                    } else {
                        aggregateClaimants(
                                doUnderOccupied,
                                false,
                                false,
                                underOccupiedData,
                                lookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                paymentType,
                                claimantTypes,
                                tenancyTypeGroups.get(doUnderOccupied),
                                tenancyTypesGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances,
                                tDW_PersonIDtoDW_IDLookup);
                    }
                }
            }
        }
    }

    /**
     * @param levels A set of levels expected values include OA, LSOA, MSOA,
     * PostcodeUnit, PostcodeSector, PostcodeDistrict
     * @return A set of look ups from postcodes to each level input in levels.
     */
    public TreeMap<String, TreeMap<String, String>> getLookupsFromPostcodeToLevelCode(
            ArrayList<String> levels) {
        TreeMap<String, TreeMap<String, String>> result;
        result = new TreeMap<String, TreeMap<String, String>>();
        Iterator<String> ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
            tLookupFromPostcodeToLevelCode = getLookupFromPostcodeToLevelCode(
                    env,
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
        } else if (level.equalsIgnoreCase("PostcodeUnit")
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
        return result;
    }

    /**
     * @param claimantNINO1
     * @param i
     * @param tIDIndexes
     * @return True iff claimantNINO1 is in tIDIndexes in any index from 0 to i
     * - 1.
     */
    private boolean getHasClaimantBeenSeenBefore(
            DW_PersonID ID,
            int i,
            ArrayList<Integer> include,
            ArrayList<HashSet<DW_PersonID>> tIDIndexes) {
        boolean result = false;
        Iterator<Integer> iteInclude;
        iteInclude = include.iterator();
        int j = 0;
        while (iteInclude.hasNext()) {
            int i0;
            i0 = iteInclude.next();
            if (i0 == i) {
                break;
            }
            HashSet<DW_PersonID> tIDIndex;
            tIDIndex = tIDIndexes.get(j);
            if (tIDIndex.contains(ID)) {
                return true;
            }
            j++;
        }
        return result;
    }

    public String getLastMonth_yearMonth(
            String year,
            String month,
            String[] SHBEFilenames,
            int i,
            int startIndex) {
        String result = null;
        if (i > startIndex + 2) {
            String lastMonthYear = tDW_SHBE_Handler.getYear(SHBEFilenames[i - 1]);
            int yearInt = Integer.parseInt(year);
            int lastMonthYearInt = Integer.parseInt(lastMonthYear);
            if (!(yearInt == lastMonthYearInt || yearInt == lastMonthYearInt + 1)) {
                return null;
            }
            String lastMonthMonth = tDW_SHBE_Handler.getMonth(SHBEFilenames[i - 1]);
            result = lastMonthYear + lastMonthMonth;
        }

        return result;
    }

    public String getLastYear_yearMonth(
            String year,
            String month,
            String[] SHBEFilenames,
            int i,
            int startIndex) {
        String result = null;
        if (i > startIndex + 13) {
            String lastYearYear = tDW_SHBE_Handler.getYear(SHBEFilenames[i - 12]);
            String lastYearMonth = tDW_SHBE_Handler.getMonth(SHBEFilenames[i - 12]);
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
     * @param tenancyTypeGroups
     * @param tenancyTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param checkPreviousTenure
     * @param dirOut
     * @param grouped
     * @param doUnderOccupancy
     */
    public void multipleTenancyChanges(
            String[] SHBEFilenames,
            TreeMap<String, ArrayList<String>> tenancyTypeGroups,
            ArrayList<String> tenancyTypesGrouped,
            ArrayList<String> regulatedGroups,
            ArrayList<String> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            boolean checkPreviousTenure,
            File dirOut,
            boolean grouped,
            boolean doUnderOccupancy) {
//        HashMap<String, String> tIDByTenancyType0;
//        HashMap<String, HashMap<String, String>> tIDByTenancyTypes;
//        tIDByTenancyTypes = new HashMap<String, HashMap<String, String>>();
//        HashMap<String, HashMap<String, String>> tIDTenureChanges;
//        tIDTenureChanges = new HashMap<String, HashMap<String, String>>();
//        HashMap<String, HashMap<String, String>> tIDTenureChangesGrouped;
//        tIDTenureChangesGrouped = new HashMap<String, HashMap<String, String>>();
//        Iterator<String> ite;
//        ite = includes.keySet().iterator();
//        while (ite.hasNext()) {
//            String includeKey;
//            includeKey = ite.next();
//            ArrayList<Integer> include;
//            include = includes.get(includeKey);
//            Iterator<Integer> ite2;
//            ite2 = include.iterator();
//            int i;
//            i = ite2.next();
//            File f;
//            f = DW_SHBE_Handler.getClaimantIDToTenancyTypeLookupFile(
//                    SHBEFilenames[i]);
//            String yM30;
//            yM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
//            if (tIDByTenancyTypes.containsKey(yM30)) {
//                tIDByTenancyType0 = tIDByTenancyTypes.get(yM30);
//            } else {
//                tIDByTenancyType0 = (HashMap<String, String>) Generic_StaticIO.readObject(
//                        f);
//                tIDByTenancyTypes.put(yM30, tIDByTenancyType0);
//            }
//            while (ite2.hasNext()) {
//                i = ite2.next();
//                String yM31;
//                yM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
//                HashMap<String, String> tIDByTenancyType1;
//                if (tIDByTenancyTypes.containsKey(yM31)) {
//                    tIDByTenancyType1 = tIDByTenancyTypes.get(yM31);
//                } else {
//                    f = DW_SHBE_Handler.getClaimantIDToTenancyTypeLookupFile(
//                            SHBEFilenames[i]);
//                    tIDByTenancyType1 = (HashMap<String, String>) Generic_StaticIO.readObject(
//                            f);
//                    tIDByTenancyTypes.put(yM31, tIDByTenancyType1);
//                }
//                // Get TenancyTypeTranistionMatrix
//                Object[] tenancyTypeTranistionMatrixETC;
//                tenancyTypeTranistionMatrixETC = getMultipleTenancyTypeTranistionMatrix(
//                        tIDByTenancyType0,
//                        tIDByTenancyType1,
//                        tIDTenureChanges,
//                        yM30);
//                TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrix;
//                tenancyTypeTranistionMatrix = (TreeMap<String, TreeMap<String, Integer>>) tenancyTypeTranistionMatrixETC[0];
//                HashMap<String, String> tIDTenureChange;
//                tIDTenureChange = (HashMap<String, String>) tenancyTypeTranistionMatrixETC[1];
//                tIDTenureChanges.put(yM31, tIDTenureChange);
//                writeTenancyTypeTransitionMatrix(
//                        tenancyTypeTranistionMatrix,
//                        yM30,
//                        yM31,
//                        dirOut,
//                        tenancyTypes,
//                        grouped,
//                        doUnderOccupancy);
//                Object[] tenancyTypeTranistionMatrixGroupedEtc;
//                tenancyTypeTranistionMatrixGroupedEtc = getMultipleTenancyTypeTranistionMatrixGrouped(
//                        tIDByTenancyType0,
//                        tIDByTenancyType1,
//                        regulatedGroups,
//                        unregulatedGroups,
//                        tIDTenureChanges,
//                        yM30);
//                TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
//                tenancyTypeTranistionMatrixGrouped = (TreeMap<String, TreeMap<String, Integer>>) tenancyTypeTranistionMatrixGroupedEtc[0];
//                HashMap<String, String> tIDTenureChangeGrouped;
//                tIDTenureChangeGrouped = (HashMap<String, String>) tenancyTypeTranistionMatrixGroupedEtc[1];
//                tIDTenureChangesGrouped.put(yM31, tIDTenureChangeGrouped);
//                writeTenancyTypeTransitionMatrix(
//                        tenancyTypeTranistionMatrix,
//                        yM30,
//                        yM31,
//                        dirOut,
//                        tenancyTypesGrouped,
//                        grouped,
//                        doUnderOccupancy);
//                yM30 = yM31;
//            }
//        }
    }

    public void reportUnderOccupancyTotals(Object[] underOccupiedData) {
        TreeMap<String, DW_UnderOccupiedReport_Set> tCouncilSets;
        tCouncilSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        Iterator<String> ite;
        Iterator<String> ite2;
        HashSet<String> totalCouncil;
        totalCouncil = new HashSet<String>();
        ite = tCouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            String yM;
            yM = ite.next();
            DW_UnderOccupiedReport_Set s;
            s = tCouncilSets.get(yM);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                String tID;
                tID = ite2.next();
                totalCouncil.add(tID);
            }
        }
        System.out.println("Number of Council tenants effected by underoccupancy " + totalCouncil.size());
        TreeMap<String, DW_UnderOccupiedReport_Set> tRSLSets;
        tRSLSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        HashSet<String> totalRSL;
        totalRSL = new HashSet<String>();
        ite = tRSLSets.keySet().iterator();
        while (ite.hasNext()) {
            String yM;
            yM = ite.next();
            DW_UnderOccupiedReport_Set s;
            s = tRSLSets.get(yM);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                String tID;
                tID = ite2.next();
                totalRSL.add(tID);
            }
        }
        System.out.println("Number of RSL tenants effected by underoccupancy " + totalRSL.size());
        totalCouncil.addAll(totalRSL);
        System.out.println("Number of Council tenants effected by underoccupancy " + totalCouncil.size());
    }

    private TreeMap<String, DW_UnderOccupiedReport_Set> combineUOSets(
            Object[] underOccupiedData) {
        TreeMap<String, DW_UnderOccupiedReport_Set> result;
        result = new TreeMap<String, DW_UnderOccupiedReport_Set>();
        TreeMap<String, DW_UnderOccupiedReport_Set> CouncilPart;
        CouncilPart = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLPart;
        RSLPart = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        String yM3;
        DW_UnderOccupiedReport_Set set;
        DW_UnderOccupiedReport_Set CouncilSet;
        DW_UnderOccupiedReport_Set RSLSet;
        Iterator<String> ite;
        ite = CouncilPart.keySet().iterator();
        while (ite.hasNext()) {
            yM3 = ite.next();
            set = result.get(yM3);
            if (set == null) {
                set = new DW_UnderOccupiedReport_Set(env);
                result.put(yM3, set);
            }
            CouncilSet = CouncilPart.get(yM3);
            set.getMap().putAll(CouncilSet.getMap());
        }
        ite = RSLPart.keySet().iterator();
        while (ite.hasNext()) {
            yM3 = ite.next();
            set = result.get(yM3);
            if (set == null) {
                set = new DW_UnderOccupiedReport_Set(env);
                result.put(yM3, set);
            }
            RSLSet = RSLPart.get(yM3);
            set.getMap().putAll(RSLSet.getMap());
        }
        return result;
    }

    /**
     * Calculates and writes out: Tenancy Type Transition Matrixes; Transition
     * Frequencies.
     *
     * @param SHBEFilenames
     * @param paymentType
     * @param doGrouped
     * @param tenancyTypes
     * @param tenancyTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param loadData
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param underOccupiedData
     * @param doUnderOccupied
     * @param tCTBRefs
     */
    public void tenancyChanges(
            String[] SHBEFilenames,
            String paymentType,
            boolean doGrouped,
            ArrayList<String> tenancyTypes,
            ArrayList<String> tenancyTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            HashSet<String> tCTBRefs) {

        // This is a place where the program is likely to run out of memory as 
        // it potentially tries to load in lots of data. To be more smart about 
        // this, the data should be loaded on an as needed basis and if memory 
        // runs low, then data swapping can be done to prevent OutOfMemoryErrors
        // being thrown.
        //HashMap<String, DW_SHBE_Collection> tSHBECollections;
        //tSHBECollections = null; // Only works for notCheckedPreviousTenure!
        //tSHBECollections = DW_SHBE_Handler.loadSHBEData(15, tCTBRefs);
//        tSHBECollections = DW_SHBE_Handler.loadSHBEData(0, tCTBRefs);
//        System.out.println("Hurray, loaded all the data!");
        HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes;
        tIDByPostcodes = new HashMap<Integer, HashMap<DW_intID, String>>();

        File dirOut;
        dirOut = tDW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                sAll,
                paymentType,
                checkPreviousTenure);
        dirOut = new File(
                dirOut,
                sWithOrWithoutPostcodeChange);
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSetsCouncil;
        underOccupiedSetsCouncil = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSetsRSL;
        underOccupiedSetsRSL = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSets = null;
        if (doUnderOccupied) {
            underOccupiedSets = combineUOSets(underOccupiedData);
        }
        dirOut = tDW_Files.getUOFile(
                dirOut,
                doUnderOccupied,
                doUnderOccupied,
                doUnderOccupied);
        dirOut.mkdirs();
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey;
            includeKey = includesIte.next();
            File dirOut2;
            dirOut2 = new File(
                    dirOut,
                    includeKey);
            if (tCTBRefs != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        sAll);
            }
            dirOut2.mkdirs();
            System.out.println("dirOut " + dirOut2);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            HashMap<Integer, String> indexYM3s;
            indexYM3s = tDW_SHBE_Handler.getIndexYM3s();
            int i;
            i = includeIte.next();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String yM30;
            yM30 = tDW_SHBE_Handler.getYM3(filename);

            HashMap<Integer, HashMap<DW_intID, String>> tIDByCTBRefs = null;
            tIDByCTBRefs = new HashMap<Integer, HashMap<DW_intID, String>>();
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs = null;
            tCTBRefByIDs = new HashMap<Integer, HashMap<String, DW_intID>>();

            DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
            DW_UnderOccupiedReport_Set underOccupiedSetCouncil0 = null;
            DW_UnderOccupiedReport_Set underOccupiedSetRSL0 = null;

            DW_SHBE_Collection tSHBECollection0 = null;
            //tSHBECollection0 = tSHBECollections.get(yM30);
            tSHBECollection0 = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);

            HashMap<DW_intID, String> tIDByCTBRef0 = null;
            HashMap<String, DW_intID> tCTBRefByID0 = null;
            tIDByCTBRef0 = loadIDByCTBRef(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByCTBRefs);
            tCTBRefByID0 = loadCTBRefByID(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tCTBRefByIDs);

            // tIDByTenancyType0
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes;
            HashMap<DW_intID, Integer> tIDByTenancyType0;
            tIDByTenancyTypes = new HashMap<Integer, HashMap<DW_intID, Integer>>();
            tIDByTenancyType0 = loadIDByTenancyType(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByTenancyTypes);

            // tIDByPostcode0
            HashMap<DW_intID, String> tIDByPostcode0;
            tIDByPostcode0 = loadIDByPostcode(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByPostcodes);

            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies = null;
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil = null;
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL = null;
            HashSet<DW_intID> tUnderOccupiedIDs0 = null;
            HashSet<DW_intID> tUnderOccupiedIDsCouncil0 = null;
            HashSet<DW_intID> tUnderOccupiedIDsRSL0 = null;
            HashSet<DW_intID> tUnderOccupiedIDs1 = null;
            HashSet<DW_intID> tUnderOccupiedIDsCouncil1 = null;
            HashSet<DW_intID> tUnderOccupiedIDsRSL1 = null;

            boolean doloop = true;
            if (doUnderOccupied) {
                underOccupiedSet0 = underOccupiedSets.get(yM30);
                underOccupiedSetCouncil0 = underOccupiedSetsCouncil.get(yM30);
                underOccupiedSetRSL0 = underOccupiedSetsRSL.get(yM30);
                tUnderOccupancies = new HashMap<Integer, HashSet<DW_intID>>();
                tUnderOccupanciesCouncil = new HashMap<Integer, HashSet<DW_intID>>();
                tUnderOccupanciesRSL = new HashMap<Integer, HashSet<DW_intID>>();
                if (underOccupiedSet0 != null) {
                    tUnderOccupiedIDs0 = getUnderOccupiedIDs(
                            underOccupiedSet0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupancies);
                } else {
                    doloop = false;
                }
                if (underOccupiedSetCouncil0 != null) {
                    tUnderOccupiedIDsCouncil0 = getUnderOccupiedIDs(
                            underOccupiedSetCouncil0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupanciesCouncil);
                } else {
                    doloop = false;
                }
                if (underOccupiedSetRSL0 != null) {
                    tUnderOccupiedIDsRSL0 = getUnderOccupiedIDs(
                            underOccupiedSetRSL0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupanciesRSL);
                } else {
                    doloop = false;
                }
            }
            if (doloop) {
                // Init tenancyTypeChangesAllIncludes and tenancyTypeChangesGroupedAllIncludes
//            HashMap<DW_ID, ArrayList<String>> tenancyChanges;
//            tenancyChanges = new HashMap<DW_ID, ArrayList<String>>();
//            HashMap<DW_ID, ArrayList<String>> tenancyChangesGrouped;
//            tenancyChangesGrouped = new HashMap<DW_ID, ArrayList<String>>();
                HashMap<String, ArrayList<String>> tenancyChanges;
                tenancyChanges = new HashMap<String, ArrayList<String>>();
                HashMap<String, ArrayList<String>> tenancyChangesGrouped;
                tenancyChangesGrouped = new HashMap<String, ArrayList<String>>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = SHBEFilenames[i];
                    // Set Year and Month variables
                    String yM31 = tDW_SHBE_Handler.getYM3(filename);
                    System.out.println("Year Month " + yM31);

                    DW_SHBE_Collection tSHBECollection1 = null;
                    //tSHBECollection1 = tSHBECollections.get(yM31);
                    tSHBECollection1 = new DW_SHBE_Collection(
                            env,
                            filename,
                            paymentType);

                    // underOccupiedSet1
                    DW_UnderOccupiedReport_Set underOccupiedSet1 = null;
                    DW_UnderOccupiedReport_Set underOccupiedSetCouncil1 = null;
                    DW_UnderOccupiedReport_Set underOccupiedSetRSL1 = null;
                    HashMap<DW_intID, String> tIDByCTBRef1 = null;
                    // tIDByTenancyType1
                    HashMap<DW_intID, Integer> tIDByTenancyType1;
                    tIDByTenancyType1 = loadIDByTenancyType(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByTenancyTypes);
                    // tIDByPostcode1
                    HashMap<DW_intID, String> tIDByPostcode1;
                    tIDByPostcode1 = loadIDByPostcode(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByPostcodes);
                    HashMap<String, DW_intID> tCTBRefByID1 = null;
                    tCTBRefByID1 = loadCTBRefByID(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tCTBRefByIDs);
                    tIDByCTBRef1 = loadIDByCTBRef(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByCTBRefs);
                    if (doUnderOccupied) {
                        underOccupiedSet1 = underOccupiedSets.get(yM31);
                        underOccupiedSetCouncil1 = underOccupiedSetsCouncil.get(yM31);
                        underOccupiedSetRSL1 = underOccupiedSetsRSL.get(yM31);
                        tUnderOccupiedIDs1 = getUnderOccupiedIDs(
                                underOccupiedSet1,
                                tCTBRefByID1,
                                i,
                                tUnderOccupancies);
                        tUnderOccupiedIDsCouncil1 = getUnderOccupiedIDs(
                                underOccupiedSetCouncil1,
                                tCTBRefByID1,
                                i,
                                tUnderOccupanciesCouncil);
                        tUnderOccupiedIDsRSL1 = getUnderOccupiedIDs(
                                underOccupiedSetRSL1,
                                tCTBRefByID1,
                                i,
                                tUnderOccupanciesRSL);
                    }
                    // Get TenancyTypeTranistionMatrix
                    TreeMap<String, TreeMap<String, Integer>> tenancyTypeTransitionMatrix;
                    tenancyTypeTransitionMatrix = getTenancyTypeTransitionMatrixAndRecordTenancyChange(
                            //tSHBECollections,
                            tIDByPostcodes,
                            tSHBECollection0,
                            tSHBECollection1,
                            tIDByTenancyType0,
                            tIDByTenancyType1,
                            tIDByTenancyTypes,
                            tUnderOccupiedIDs0,
                            tUnderOccupiedIDsCouncil0,
                            tUnderOccupiedIDsRSL0,
                            tUnderOccupiedIDs1,
                            tUnderOccupiedIDsCouncil1,
                            tUnderOccupiedIDsRSL1,
                            tUnderOccupancies,
                            tUnderOccupanciesCouncil,
                            tUnderOccupanciesRSL,
                            tenancyChanges,
                            yM30,
                            yM31,
                            checkPreviousTenure,
                            i,
                            include,
                            indexYM3s,
                            underOccupiedSet0,
                            underOccupiedSetCouncil0,
                            underOccupiedSetRSL0,
                            tIDByCTBRef0,
                            tCTBRefByID0,
                            underOccupiedSet1,
                            underOccupiedSetCouncil1,
                            underOccupiedSetRSL1,
                            tIDByCTBRef1,
                            tCTBRefByID1,
                            tCTBRefByIDs,
                            doUnderOccupied,
                            tCTBRefs);
                    writeTenancyTypeTransitionMatrix(
                            tenancyTypeTransitionMatrix,
                            yM30,
                            yM31,
                            dirOut2,
                            tenancyTypes,
                            false,
                            doUnderOccupied);
                    if (doGrouped) {
                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
                        tenancyTypeTranistionMatrixGrouped = getTenancyTypeTranistionMatrixGroupedAndRecordTenancyChange(
                                tIDByTenancyType0,
                                tIDByTenancyType1,
                                tIDByTenancyTypes,
                                tUnderOccupiedIDs0,
                                tUnderOccupiedIDs1,
                                tUnderOccupancies,
                                regulatedGroups,
                                unregulatedGroups,
                                tenancyChangesGrouped,
                                yM31,
                                checkPreviousTenure,
                                i,
                                include,
                                underOccupiedSet0,
                                tIDByCTBRef0,
                                tCTBRefByID0,
                                underOccupiedSet1,
                                tIDByCTBRef1,
                                tCTBRefByID1,
                                tCTBRefByIDs,
                                doUnderOccupied,
                                tCTBRefs);
                        writeTenancyTypeTransitionMatrix(
                                tenancyTypeTranistionMatrixGrouped,
                                yM30,
                                yM31,
                                dirOut2,
                                tenancyTypesGrouped,
                                true,
                                doUnderOccupied);
                    }
                    tSHBECollection0 = tSHBECollection1;
                    tIDByPostcode0 = tIDByPostcode1; // Probably not necessary
                    yM30 = yM31;
                    tIDByTenancyType0 = tIDByTenancyType1;
                    tUnderOccupiedIDs0 = tUnderOccupiedIDs1;
                    tUnderOccupiedIDsCouncil0 = tUnderOccupiedIDsCouncil1;
                    tUnderOccupiedIDsRSL0 = tUnderOccupiedIDsRSL1;
                    underOccupiedSet0 = underOccupiedSet1;
                    underOccupiedSetCouncil0 = underOccupiedSetCouncil1;
                    underOccupiedSetRSL0 = underOccupiedSetRSL1;
                    tIDByCTBRef0 = tIDByCTBRef1;
                    tCTBRefByID0 = tCTBRefByID1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                //Iterator<DW_ID> tenancyChangesIte;
                Iterator<String> tenancyChangesIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = Integer.MIN_VALUE;
                tenancyChangesIte = tenancyChanges.keySet().iterator();
                while (tenancyChangesIte.hasNext()) {
                    //DW_ID ID = tenancyChangesIte.next();
                    String CTBRef = tenancyChangesIte.next();
                    ArrayList<String> transition;
                    //transition = tenancyChanges.get(ID);
                    transition = tenancyChanges.get(CTBRef);
                    max = Math.max(max, transition.size());
                    String out;
                    out = "";
                    Iterator<String> transitionIte;
                    transitionIte = transition.iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        String t;
                        t = transitionIte.next();
                        String[] splitT;
                        splitT = t.split(":");
                        if (reportTenancyTransitionBreaks) {
                            if (!doneFirst) {
                                out += splitT[0];
                                doneFirst = true;
                            } else {
                                out += ", " + splitT[0];
                            }
                        } else if (!doneFirst) {
                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToTreeMapStringInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                System.out.println(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1));
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        tDW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
                    tenancyChangesIte = tenancyChangesGrouped.keySet().iterator();
                    while (tenancyChangesIte.hasNext()) {
                        //DW_ID ID = tenancyChangesIte.next();
                        String CTBRef = tenancyChangesIte.next();
                        ArrayList<String> transition;
                        //transition = tenancyChanges.get(ID);
                        transition = tenancyChanges.get(CTBRef);
                        max = Math.max(max, transition.size());
                        String out;
                        out = "";
                        Iterator<String> transitionIte;
                        transitionIte = transition.iterator();
                        boolean doneFirst = false;
                        while (transitionIte.hasNext()) {
                            String t;
                            t = transitionIte.next();
                            String[] splitT;
                            splitT = t.split(":");
                            if (reportTenancyTransitionBreaks) {
                                if (!doneFirst) {
                                    out += splitT[0];
                                    doneFirst = true;
                                } else {
                                    out += ", " + splitT[0];
                                }
                            } else if (!doneFirst) {
                                if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                    out += splitT[0];
                                    doneFirst = true;
                                }
                            } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out += ", " + splitT[0];
                            }
                        }
                        if (!out.isEmpty()) {
                            if (transitions.containsKey(out)) {
                                Generic_Collections.addToTreeMapStringInteger(
                                        transitions, out, 1);
                            } else {
                                transitions.put(out, 1);
                            }
                        }
                    }
                    System.out.println(includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1));
                    writeTransitionFrequencies(transitions,
                            dirOut2,
                            tDW_Strings.sGrouped,
                            "FrequenciesGrouped.csv",
                            reportTenancyTransitionBreaks);
                }
            }
        }
    }

    /**
     * Calculates and writes out: Tenancy Type Transition Matrixes; Transition
     * Frequencies.
     *
     * @param SHBEFilenames A list of all SHBE filenames
     * @param paymentType
     * @param doGrouped
     * @param tenancyTypes
     * @param tenancyTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes The indexes of the SHBEFilenames to use in the
     * summarisation.
     * @param loadData If true then the data is loaded from source, otherwise
     * index files are used which are much quicker to load. The index files are
     * built from the original source, so if the source changes, then the data
     * must be reloaded.
     * @param checkPreviousTenure If true, then if the Tenure Type previously is
     * not recorded (because the claimant was not in the SHBE) then the
     * algorithm checks back for the last time the claimant had a recorded
     * Tenancy Type in the SHBE.
     * @param reportTenancyTransitionBreaks If true then when a claimant moves
     * off the SHBE in a time period, this is recorded in the Frequency Tables
     * as a Tenancy Type Transition to -999, otherwise it is not.
     * @param postcodeHandler
     * @param postcodeChange If true postcode changes are summarised. If false
     * non postcode changes are summarised.
     * @param checkPreviousPostcode If true, when the postcode does not
     * validate, the algorithm looks back for a earlier postcode. If false, no
     * further checking is done.
     * @param underOccupiedData
     * @param doUnderOccupied
     * @param underOccupiedInApril2013
     */
    public void tenancyAndPostcodeChanges(
            String[] SHBEFilenames,
            String paymentType,
            boolean doGrouped,
            ArrayList<String> tenancyTypes,
            ArrayList<String> tenancyTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            Generic_UKPostcode_Handler postcodeHandler,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            HashSet<String> underOccupiedInApril2013) {
        File dirOut;
        dirOut = tDW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                sAll,
                paymentType,
                checkPreviousTenure);
        dirOut = new File(
                dirOut,
                tDW_Strings.sTenancyAndPostcodeChanges);

        HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes;
        tIDByPostcodes = new HashMap<Integer, HashMap<DW_intID, String>>();

        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSetsCouncil;
        underOccupiedSetsCouncil = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSetsRSL;
        underOccupiedSetsRSL = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSets = null;
        if (doUnderOccupied) {
            underOccupiedSets = combineUOSets(underOccupiedData);
        }

        dirOut = tDW_Files.getUOFile(dirOut, doUnderOccupied, doUnderOccupied, doUnderOccupied);
        if (postcodeChange) {
            dirOut = new File(
                    dirOut,
                    tDW_Strings.sPostcodeChanged);
        } else {
            dirOut = new File(
                    dirOut,
                    tDW_Strings.sPostcodeChangedNo);
        }
        dirOut.mkdirs();
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey;
            includeKey = includesIte.next();
            File dirOut2;
            dirOut2 = new File(
                    dirOut,
                    includeKey);
            if (underOccupiedInApril2013 != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        sAll);
            }
            dirOut2.mkdirs();
            System.out.println("dirOut " + dirOut2);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
//            HashMap<Integer, HashMap<String, DW_ID>> tCTBRefByIDs = null;
//            tCTBRefByIDs = new HashMap<Integer, HashMap<String, DW_ID>>();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String yM30;
            yM30 = tDW_SHBE_Handler.getYM3(filename);
            // underOccupiedSet0
//            DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
//            HashMap<Integer, HashMap<DW_ID, String>> tIDByCTBRefs = null;
//            tIDByCTBRefs = new HashMap<Integer, HashMap<DW_ID, String>>();
//            HashMap<String, DW_ID> tCTBRefByID0 = null;
//            HashMap<String, DW_ID> tCTBRefByID1 = null;
//            tCTBRefByID0 = loadCTBRefByID(
//                    loadData,
//                    filename,
//                    paymentType,
//                    i,
//                    tCTBRefByIDs);
//            HashMap<DW_ID, String> tIDByCTBRef0 = null;
//            tIDByCTBRef0 = loadIDByCTBRef(
//                    loadData,
//                    filename,
//                    paymentType,
//                    i,
//                    tIDByCTBRefs);
//            // tIDByTenancyType0
//            HashMap<Integer, HashMap<DW_ID, Integer>> tIDByTenancyTypes;
//            tIDByTenancyTypes = new HashMap<Integer, HashMap<DW_ID, Integer>>();
//            HashMap<DW_ID, Integer> tIDByTenancyType0;
//            tIDByTenancyType0 = loadIDByTenancyType(
//                    loadData,
//                    filename,
//                    paymentType,
//                    i,
//                    tIDByTenancyTypes);
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupancies = null;
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupanciesCouncil = null;
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupanciesRSL = null;
//            HashSet<DW_ID> tUnderOccupiedIDs0 = null;
//            HashSet<DW_ID> tUnderOccupiedIDs1 = null;
//            if (doUnderOccupied) {
//                tIDByCTBRefs = new HashMap<Integer, HashMap<DW_ID, String>>();
//                underOccupiedSet0 = underOccupiedSets.get(yM30);
//                tUnderOccupancies = new HashMap<Integer, HashSet<DW_ID>>();
//                tUnderOccupiedIDs0 = getUnderOccupiedIDs(
//                        underOccupiedSet0,
//                        tCTBRefByID0,
//                        i,
//                        tUnderOccupancies);
//            }

            HashMap<Integer, HashMap<DW_intID, String>> tIDByCTBRefs = null;
            tIDByCTBRefs = new HashMap<Integer, HashMap<DW_intID, String>>();
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs = null;
            tCTBRefByIDs = new HashMap<Integer, HashMap<String, DW_intID>>();

            DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
            DW_UnderOccupiedReport_Set underOccupiedSetCouncil0 = null;
            DW_UnderOccupiedReport_Set underOccupiedSetRSL0 = null;

            DW_SHBE_Collection tSHBECollection0 = null;
            //tSHBECollection0 = tSHBECollections.get(yM30);
            tSHBECollection0 = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);

            HashMap<DW_intID, String> tIDByCTBRef0 = null;
            HashMap<String, DW_intID> tCTBRefByID0 = null;
            tIDByCTBRef0 = loadIDByCTBRef(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByCTBRefs);
            tCTBRefByID0 = loadCTBRefByID(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tCTBRefByIDs);

            // tIDByTenancyType0
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes;
            HashMap<DW_intID, Integer> tIDByTenancyType0;
            tIDByTenancyTypes = new HashMap<Integer, HashMap<DW_intID, Integer>>();
            tIDByTenancyType0 = loadIDByTenancyType(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByTenancyTypes);

            // tIDByPostcode0
            HashMap<DW_intID, String> tIDByPostcode0;
            tIDByPostcode0 = loadIDByPostcode(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByPostcodes);

            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies = null;
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil = null;
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL = null;
            HashSet<DW_intID> tUnderOccupiedIDs0 = null;
            HashSet<DW_intID> tUnderOccupiedIDsCouncil0 = null;
            HashSet<DW_intID> tUnderOccupiedIDsRSL0 = null;
            HashSet<DW_intID> tUnderOccupiedIDs1 = null;
            HashSet<DW_intID> tUnderOccupiedIDsCouncil1 = null;
            HashSet<DW_intID> tUnderOccupiedIDsRSL1 = null;

            boolean doloop = true;
            if (doUnderOccupied) {
                underOccupiedSet0 = underOccupiedSets.get(yM30);
                underOccupiedSetCouncil0 = underOccupiedSetsCouncil.get(yM30);
                underOccupiedSetRSL0 = underOccupiedSetsRSL.get(yM30);
                tUnderOccupancies = new HashMap<Integer, HashSet<DW_intID>>();
                tUnderOccupanciesCouncil = new HashMap<Integer, HashSet<DW_intID>>();
                tUnderOccupanciesRSL = new HashMap<Integer, HashSet<DW_intID>>();
                if (underOccupiedSet0 != null) {
                    tUnderOccupiedIDs0 = getUnderOccupiedIDs(
                            underOccupiedSet0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupancies);
                } else {
                    doloop = false;
                }
                if (underOccupiedSetCouncil0 == null) {
                    tUnderOccupiedIDsCouncil0 = getUnderOccupiedIDs(
                            underOccupiedSetCouncil0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupanciesCouncil);
                } else {
                    doloop = false;
                }
                if (underOccupiedSetRSL0 == null) {
                    tUnderOccupiedIDsRSL0 = getUnderOccupiedIDs(
                            underOccupiedSetRSL0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupanciesRSL);
                } else {
                    doloop = false;
                }

            }

            if (doloop) {
                // Init tenancyChanges and tenancyChangesGrouped
//            HashMap<DW_ID, ArrayList<String>> tenancyChanges;
//            tenancyChanges = new HashMap<DW_ID, ArrayList<String>>();
//            HashMap<DW_ID, ArrayList<String>> tenancyChangesGrouped;
//            tenancyChangesGrouped = new HashMap<DW_ID, ArrayList<String>>();
                HashMap<String, ArrayList<String>> tenancyChanges;
                tenancyChanges = new HashMap<String, ArrayList<String>>();
                HashMap<String, ArrayList<String>> tenancyChangesGrouped;
                tenancyChangesGrouped = new HashMap<String, ArrayList<String>>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = SHBEFilenames[i];
                    // Set Year and Month variables
                    String yM31 = tDW_SHBE_Handler.getYM3(filename);
                    // underOccupiedSet1
                    DW_UnderOccupiedReport_Set underOccupiedSet1 = null;
                    HashMap<DW_intID, String> tIDByCTBRef1 = null;
                    tIDByCTBRef1 = loadIDByCTBRef(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByCTBRefs);
                    HashMap<String, DW_intID> tCTBRefByID1;
                    tCTBRefByID1 = loadCTBRefByID(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tCTBRefByIDs);
                    if (doUnderOccupied) {
                        String key;
                        key = tDW_SHBE_Handler.getYM3(filename);
                        underOccupiedSet1 = underOccupiedSets.get(key);
                        tUnderOccupiedIDs1 = getUnderOccupiedIDs(
                                underOccupiedSet1,
                                tCTBRefByID1,
                                i,
                                tUnderOccupancies);
                    }
                    // tIDByTenancyType1
                    HashMap<DW_intID, Integer> tIDByTenancyType1;
                    tIDByTenancyType1 = loadIDByTenancyType(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByTenancyTypes);
                    // tIDByPostcode1
                    HashMap<DW_intID, String> tIDByPostcode1;
                    tIDByPostcode1 = loadIDByPostcode(
                            loadData,
                            filename,
                            paymentType,
                            i,
                            tIDByPostcodes);
                    // Get TenancyTypeTranistionMatrix
                    TreeMap<String, TreeMap<String, Integer>> tenancyTypeTransitionMatrix;
                    tenancyTypeTransitionMatrix = getTenancyTypeTransitionMatrixAndWritePostcodeChangeDetails(
                            dirOut2,
                            tIDByTenancyType0,
                            tIDByTenancyType1,
                            tIDByTenancyTypes,
                            tUnderOccupiedIDs0,
                            tUnderOccupiedIDs1,
                            tUnderOccupancies,
                            tUnderOccupanciesCouncil,
                            tUnderOccupanciesRSL,
                            tenancyChanges,
                            yM30,
                            yM31,
                            checkPreviousTenure,
                            i,
                            include,
                            postcodeHandler,
                            tIDByPostcode0,
                            tIDByPostcode1,
                            tIDByPostcodes,
                            postcodeChange,
                            checkPreviousPostcode,
                            underOccupiedSet0,
                            tIDByCTBRef0,
                            underOccupiedSet1,
                            tIDByCTBRef1,
                            tCTBRefByIDs,
                            doUnderOccupied,
                            underOccupiedInApril2013);
                    writeTenancyTypeTransitionMatrix(
                            tenancyTypeTransitionMatrix,
                            yM30,
                            yM31,
                            dirOut2,
                            tenancyTypes,
                            false,
                            doUnderOccupied);
                    if (doGrouped) {
                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTransitionMatrixGrouped;
                        tenancyTypeTransitionMatrixGrouped = getTenancyTypeTransitionMatrixGroupedAndWritePostcodeChangeDetails(
                                dirOut2,
                                tIDByTenancyType0,
                                tIDByTenancyType1,
                                tIDByTenancyTypes,
                                tUnderOccupiedIDs0,
                                tUnderOccupiedIDs1,
                                tUnderOccupancies,
                                regulatedGroups,
                                unregulatedGroups,
                                tenancyChangesGrouped,
                                yM30,
                                yM31,
                                checkPreviousTenure,
                                i,
                                include,
                                postcodeHandler,
                                tIDByPostcode0,
                                tIDByPostcode1,
                                tIDByPostcodes,
                                postcodeChange,
                                checkPreviousPostcode,
                                underOccupiedSet0,
                                tIDByCTBRef0,
                                underOccupiedSet1,
                                tIDByCTBRef1,
                                tCTBRefByIDs,
                                doUnderOccupied,
                                underOccupiedInApril2013);
                        writeTenancyTypeTransitionMatrix(
                                tenancyTypeTransitionMatrixGrouped,
                                yM30,
                                yM31,
                                dirOut2,
                                tenancyTypesGrouped,
                                true,
                                doUnderOccupied);
                    }
                    yM30 = yM31;
                    tIDByTenancyType0 = tIDByTenancyType1;
                    tIDByPostcode0 = tIDByPostcode1;
                    tIDByCTBRef0 = tIDByCTBRef1;
                    tCTBRefByID0 = tCTBRefByID1;
                    tUnderOccupiedIDs0 = tUnderOccupiedIDs1;
                    underOccupiedSet0 = underOccupiedSet1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                //Iterator<DW_ID> tenancyChangesIte;
                Iterator<String> tenancyChangesIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = Integer.MIN_VALUE;
                tenancyChangesIte = tenancyChanges.keySet().iterator();
                while (tenancyChangesIte.hasNext()) {
                    //DW_ID ID = tenancyChangesIte.next();
                    String CTBRef = tenancyChangesIte.next();
                    ArrayList<String> transition;
                    //transition = tenancyChanges.get(ID);
                    transition = tenancyChanges.get(CTBRef);
                    max = Math.max(max, transition.size());
                    String out;
                    out = "";
                    Iterator<String> transitionIte;
                    transitionIte = transition.iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        String t;
                        t = transitionIte.next();
                        String[] splitT;
                        splitT = t.split(":");
                        if (reportTenancyTransitionBreaks) {
                            if (!doneFirst) {
                                out += splitT[0];
                                doneFirst = true;
                            } else {
                                out += ", " + splitT[0];
                            }
                        } else if (!doneFirst) {
                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToTreeMapStringInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                System.out.println(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1));
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        tDW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
                    tenancyChangesIte = tenancyChangesGrouped.keySet().iterator();
                    while (tenancyChangesIte.hasNext()) {
                        //DW_ID ID = tenancyChangesIte.next();
                        String CTBRef = tenancyChangesIte.next();
                        ArrayList<String> transition;
                        //transition = tenancyChangesGrouped.get(ID);
                        transition = tenancyChangesGrouped.get(CTBRef);
                        max = Math.max(max, transition.size());
                        String out;
                        out = "";
                        Iterator<String> transitionsIte;
                        transitionsIte = transition.iterator();
                        boolean doneFirst = false;
                        while (transitionsIte.hasNext()) {
                            String t;
                            t = transitionsIte.next();
                            String[] splitT;
                            splitT = t.split(":");
                            if (reportTenancyTransitionBreaks) {
                                if (!doneFirst) {
                                    out += splitT[0];
                                    doneFirst = true;
                                } else {
                                    out += ", " + splitT[0];
                                }
                            } else if (!doneFirst) {
                                if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                    out += splitT[0];
                                    doneFirst = true;
                                }
                            } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out += ", " + splitT[0];
                            }
                        }
                        if (!out.isEmpty()) {
                            if (transitions.containsKey(out)) {
                                Generic_Collections.addToTreeMapStringInteger(
                                        transitions, out, 1);
                            } else {
                                transitions.put(out, 1);
                            }
                        }
                    }
                    System.out.println(includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1));
                    writeTransitionFrequencies(transitions,
                            dirOut2,
                            tDW_Strings.sGrouped,
                            "FrequenciesGrouped.csv",
                            reportTenancyTransitionBreaks);
                }
            }
        }
    }

    /**
     * For counting where postcode has changed and reporting by tenancy type.
     *
     * @param SHBEFilenames
     * @param paymentType
     * @param doGrouped
     * @param tenancyTypes
     * @param tenancyTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param loadData
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param postcodeHandler
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedData
     * @param doUnderOccupied
     * @param tCTBRefs
     */
    public void postcodeChanges(
            String[] SHBEFilenames,
            String paymentType,
            boolean doGrouped,
            ArrayList<String> tenancyTypes,
            ArrayList<String> tenancyTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            Generic_UKPostcode_Handler postcodeHandler,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            HashSet<String> tCTBRefs) {
        File dirOut;
//        dirOut = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
//                "All",
//                paymentType,
//                checkPreviousTenure);
        dirOut = new File(
                tDW_Files.getOutputSHBETablesDir(),
                tDW_Strings.sPostcodeChanges);
        dirOut = new File(
                dirOut,
                paymentType);
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSets = null;
        if (doUnderOccupied) {
            underOccupiedSets = combineUOSets(underOccupiedData);
        }
        dirOut = tDW_Files.getUOFile(dirOut, doUnderOccupied, doUnderOccupied, doUnderOccupied);
        if (postcodeChange) {
            dirOut = new File(
                    dirOut,
                    tDW_Strings.sPostcodeChanged);
        } else {
            dirOut = new File(
                    dirOut,
                    tDW_Strings.sPostcodeChangedNo);
        }
        dirOut.mkdirs();
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey;
            includeKey = includesIte.next();
            File dirOut2;
            dirOut2 = new File(
                    dirOut,
                    includeKey);
            if (tCTBRefs != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        sAll);
            }
            dirOut2.mkdirs();
            //System.out.println("dirOut " + dirOut2);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs = null;
            tCTBRefByIDs = new HashMap<Integer, HashMap<String, DW_intID>>();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String yM30;
            yM30 = tDW_SHBE_Handler.getYM3(filename);
            // underOccupiedSet0
            DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
            HashMap<Integer, HashMap<DW_intID, String>> tIDByCTBRefs = null;
            HashMap<String, DW_intID> tCTBRefByID0 = null;
            HashMap<String, DW_intID> tCTBRefByID1 = null;
            tCTBRefByID0 = loadCTBRefByID(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tCTBRefByIDs);
            HashMap<DW_intID, String> tIDByCTBRef0 = null;
            // tIDByTenancyType0
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes;
            tIDByTenancyTypes = new HashMap<Integer, HashMap<DW_intID, Integer>>();
            HashMap<DW_intID, Integer> tIDByTenancyType0;
            tIDByTenancyType0 = loadIDByTenancyType(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByTenancyTypes);
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes;
            tIDByPostcodes = new HashMap<Integer, HashMap<DW_intID, String>>();
            HashMap<DW_intID, String> tIDByPostcode0;
            tIDByPostcode0 = loadIDByPostcode(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByPostcodes);
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies = null;
            HashSet<DW_intID> tUnderOccupiedIDs0 = null;
            HashSet<DW_intID> tUnderOccupiedIDs1 = null;
            tIDByCTBRefs = new HashMap<Integer, HashMap<DW_intID, String>>();
            tIDByCTBRef0 = loadIDByCTBRef(
                    loadData,
                    filename,
                    paymentType,
                    i,
                    tIDByCTBRefs);
            if (doUnderOccupied) {
                tUnderOccupancies = new HashMap<Integer, HashSet<DW_intID>>();
                underOccupiedSet0 = underOccupiedSets.get(yM30);
                if (underOccupiedSet0 == null) {
                    System.out.println("underOccupiedSet0 == null, yM30 = " + yM30);
                } else {
                    tUnderOccupiedIDs0 = getUnderOccupiedIDs(
                            underOccupiedSet0,
                            tCTBRefByID0,
                            i,
                            tUnderOccupancies);
                }
            }

            // Init tenancyChanges and tenancyChangesGrouped
            //HashMap<DW_ID, ArrayList<String>> tenancyChanges;
            //tenancyChanges = new HashMap<DW_ID, ArrayList<String>>();
            //HashMap<DW_ID, ArrayList<String>> tenancyChangesGrouped;
            //tenancyChangesGrouped = new HashMap<DW_ID, ArrayList<String>>();
            HashMap<String, ArrayList<String>> tenancyChanges;
            tenancyChanges = new HashMap<String, ArrayList<String>>();
            HashMap<String, ArrayList<String>> tenancyChangesGrouped;
            tenancyChangesGrouped = new HashMap<String, ArrayList<String>>();
            // Main loop
            while (includeIte.hasNext()) {
                i = includeIte.next();
                filename = SHBEFilenames[i];
                // Set Year and Month variables
                String yM31 = tDW_SHBE_Handler.getYM3(filename);
                // underOccupiedSet1
                DW_UnderOccupiedReport_Set underOccupiedSet1 = null;
                HashMap<DW_intID, String> tIDByCTBRef1 = null;
                // tIDByTenancyType1
                HashMap<DW_intID, Integer> tIDByTenancyType1;
                tIDByTenancyType1 = loadIDByTenancyType(
                        loadData,
                        filename,
                        paymentType,
                        i,
                        tIDByTenancyTypes);
                tCTBRefByID1 = loadCTBRefByID(
                        loadData,
                        filename,
                        paymentType,
                        i,
                        tCTBRefByIDs);
                // tIDByPostcode1
                HashMap<DW_intID, String> tIDByPostcode1;
                tIDByPostcode1 = loadIDByPostcode(
                        loadData,
                        filename,
                        paymentType,
                        i,
                        tIDByPostcodes);
                tIDByCTBRef1 = loadIDByCTBRef(
                        loadData,
                        filename,
                        paymentType,
                        i,
                        tIDByCTBRefs);
                if (doUnderOccupied) {
                    underOccupiedSet1 = underOccupiedSets.get(yM31);
                    if (underOccupiedSet1 == null) {
                        System.out.println("underOccupiedSet1 == null, yM31 = " + yM31);
                    } else {
                        tUnderOccupiedIDs1 = getUnderOccupiedIDs(
                                underOccupiedSet1,
                                tCTBRefByID1,
                                i,
                                tUnderOccupancies);
                    }
                }
                // Get PostcodeTransitionCounts
                TreeMap<String, TreeMap<String, Integer>> postcodeTransitionCounts;
                if (doUnderOccupied && (tUnderOccupiedIDs0 == null || tUnderOccupiedIDs1 == null)) {
                    System.out.println("Not calculating or writing out postcodeTransitionCounts");
                } else {
                    postcodeTransitionCounts = getPostcodeTransitionCountsNoTenancyTypeChange(
                            dirOut2,
                            tIDByTenancyType0,
                            tIDByTenancyType1,
                            tIDByTenancyTypes,
                            tUnderOccupiedIDs0,
                            tUnderOccupiedIDs1,
                            tUnderOccupancies,
                            tenancyChanges,
                            yM30,
                            yM31,
                            checkPreviousTenure,
                            i,
                            include,
                            postcodeHandler,
                            tIDByPostcode0,
                            tIDByPostcode1,
                            tIDByPostcodes,
                            postcodeChange,
                            checkPreviousPostcode,
                            underOccupiedSet0,
                            tIDByCTBRef0,
                            underOccupiedSet1,
                            tIDByCTBRef1,
                            tCTBRefByIDs,
                            doUnderOccupied,
                            tCTBRefs);
                    writeTenancyTypeTransitionMatrix(
                            postcodeTransitionCounts,
                            yM30,
                            yM31,
                            dirOut2,
                            tenancyTypes,
                            false,
                            doUnderOccupied);
                    if (doGrouped) {
                        postcodeTransitionCounts = getPostcodeTransitionCountsNoTenancyTypeChangeGrouped(
                                dirOut2,
                                regulatedGroups,
                                unregulatedGroups,
                                tIDByTenancyType0,
                                tIDByTenancyType1,
                                tIDByTenancyTypes,
                                tUnderOccupiedIDs0,
                                tUnderOccupiedIDs1,
                                tUnderOccupancies,
                                tenancyChangesGrouped,
                                yM30,
                                yM31,
                                checkPreviousTenure,
                                i,
                                include,
                                postcodeHandler,
                                tIDByPostcode0,
                                tIDByPostcode1,
                                tIDByPostcodes,
                                postcodeChange,
                                checkPreviousPostcode,
                                underOccupiedSet0,
                                tIDByCTBRef0,
                                underOccupiedSet1,
                                tIDByCTBRef1,
                                tCTBRefByIDs,
                                doUnderOccupied,
                                tCTBRefs);
                        writeTenancyTypeTransitionMatrix(
                                postcodeTransitionCounts,
                                yM30,
                                yM31,
                                dirOut2,
                                tenancyTypesGrouped,
                                true,
                                doUnderOccupied);
                    }
                }
                yM30 = yM31;
                tIDByTenancyType0 = tIDByTenancyType1;
                tIDByPostcode0 = tIDByPostcode1;
                tIDByCTBRef0 = tIDByCTBRef1;
                tCTBRefByID0 = tCTBRefByID1;
                tUnderOccupiedIDs0 = tUnderOccupiedIDs1;
                underOccupiedSet0 = underOccupiedSet1;
            }
            // The only concern here is the frequency of under occupancy changes.
            if (doUnderOccupied) {
                TreeMap<String, Integer> transitions;
                int max;
                //Iterator<DW_ID> tenancyChangesIte;
                Iterator<String> tenancyChangesIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = Integer.MIN_VALUE;
                tenancyChangesIte = tenancyChanges.keySet().iterator();
                while (tenancyChangesIte.hasNext()) {
                    //DW_ID ID = tenancyChangesIte.next();
                    String CTBRef = tenancyChangesIte.next();
                    ArrayList<String> transition;
                    //transition = tenancyChanges.get(ID);
                    transition = tenancyChanges.get(CTBRef);
                    max = Math.max(max, transition.size());
                    String out;
                    out = "";
                    Iterator<String> transitionIte;
                    transitionIte = transition.iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        String t;
                        t = transitionIte.next();
                        String[] splitT;
                        splitT = t.split(":");
                        if (reportTenancyTransitionBreaks) {
                            if (!doneFirst) {
                                out = splitT[0];
                                doneFirst = true;
                            } else {
                                out += ", " + splitT[0];
                            }
                        } else if (!doneFirst) {
                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out = splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToTreeMapStringInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                System.out.println(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1));
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        tDW_Strings.sGroupedNo,
                        "Frequencies.txt",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
                    tenancyChangesIte = tenancyChangesGrouped.keySet().iterator();
                    while (tenancyChangesIte.hasNext()) {
                        //DW_ID ID = tenancyChangesIte.next();
                        String CTBRef = tenancyChangesIte.next();
                        ArrayList<String> transition;
                        //transition = tenancyChangesGrouped.get(ID);
                        transition = tenancyChangesGrouped.get(CTBRef);
                        max = Math.max(max, transition.size());
                        String out;
                        out = "";
                        Iterator<String> transitionsIte;
                        transitionsIte = transition.iterator();
                        boolean doneFirst = false;
                        while (transitionsIte.hasNext()) {
                            String t;
                            t = transitionsIte.next();
                            String[] splitT;
                            splitT = t.split(":");
                            if (reportTenancyTransitionBreaks) {
                                if (!doneFirst) {
                                    out += splitT[0];
                                    doneFirst = true;
                                } else {
                                    out += ", " + splitT[0];
                                }
                            } else if (!doneFirst) {
                                if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                    out += splitT[0];
                                    doneFirst = true;
                                }
                            } else if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
                                out += ", " + splitT[0];
                            }
                        }
                        if (!out.isEmpty()) {
                            if (transitions.containsKey(out)) {
                                Generic_Collections.addToTreeMapStringInteger(
                                        transitions, out, 1);
                            } else {
                                transitions.put(out, 1);
                            }
                        }
                    }
                    System.out.println(includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1));
                    writeTransitionFrequencies(
                            transitions,
                            dirOut2,
                            tDW_Strings.sGrouped,
                            "FrequenciesGrouped.txt",
                            reportTenancyTransitionBreaks);
                }
            }
        }
    }

//    protected HashMap<String, DW_ID> loadCTBRefByID(
//            boolean loadData,
//            String filename,
//            String paymentType,
//            Integer key,
//            HashMap<Integer, HashMap<String, DW_ID>> tCTBRefByIDs) {
//        HashMap<String, DW_ID> result;
//        if (tCTBRefByIDs.containsKey(key)) {
//            return tCTBRefByIDs.get(key);
//        }
//        result = loadCTBRefByID(loadData, filename, paymentType, key);
//        tCTBRefByIDs.put(key, result);
//        //System.out.println("...done.");
//        return result;
//    }
//
//    protected HashMap<String, DW_ID> loadCTBRefByID(
//            boolean loadData,
//            String filename,
//            String paymentType,
//            Integer key) {
//        HashMap<String, DW_ID> result;
//        if (loadData) {
//            //System.out.print("Loading " + filename + " ...");
//            DW_SHBE_Collection aSHBEData;
//            aSHBEData = getSHBEData(filename, paymentType);
//            result = (HashMap<String, DW_ID>) aSHBEData.getCTBRefToClaimantIDLookup();
//        } else {
//            File f;
//            f = DW_SHBE_Handler.getCTBRefToClaimantIDLookupFile(paymentType, filename);
//            //System.out.print("Loading " + f + " ...");
//            result = (HashMap<String, DW_ID>) Generic_StaticIO.readObject(
//                    f);
//        }
//        //System.out.println("...done.");
//        return result;
//    }
    /**
     *
     * @param loadData
     * @param filename
     * @param paymentType
     * @param key
     * @param tIDByCTBRefs
     * @return {@code if (tIDByCTBRefs.containsKey(key)) {
     * return tIDByCTBRefs.get(key);
     * }} otherwise (if loadData == true then the SHBEData is loaded and the
     * resulting ID to CTB ref lookup is put in tIDByCTBRefs and returned; or
     * the ID to CTB ref lookup is loaded from the file it is specifically
     * stored in, then put in tIDByCTBRefs and returned).
     */
    protected HashMap<DW_intID, String> loadIDByCTBRef(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByCTBRefs) {
        HashMap<DW_intID, String> result;
        if (tIDByCTBRefs.containsKey(key)) {
            return tIDByCTBRefs.get(key);
        }
        result = loadIDByCTBRef(loadData, filename, paymentType, key);
        tIDByCTBRefs.put(key, result);
        return result;
    }

    protected HashMap<DW_intID, String> loadIDByCTBRef(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key) {
        HashMap<DW_intID, String> result;
        if (loadData) {
            //System.out.print("Loading " + filename + " ...");
            DW_SHBE_Collection aSHBEData;
            aSHBEData = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);
            result = (HashMap<DW_intID, String>) aSHBEData.getClaimantIDToCTBRefLookup();
        } else {
            File f;
            f = tDW_SHBE_Handler.getClaimantIDToCTBRefLookupFile(paymentType, filename);
            //System.out.print("Loading " + f + " ...");
            result = (HashMap<DW_intID, String>) Generic_StaticIO.readObject(f);
        }
        //System.out.println("...done.");
        return result;
    }

    protected HashMap<String, DW_intID> loadCTBRefByID(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs) {
        HashMap<String, DW_intID> result;
        result = null;
        if (tCTBRefByIDs.containsKey(key)) {
            result = tCTBRefByIDs.get(key);
        }
        if (result == null) {
            result = loadCTBRefByID(loadData, filename, paymentType, key);
            tCTBRefByIDs.put(key, result);
        }
        return result;
    }

    protected HashMap<String, DW_intID> loadCTBRefByID(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key) {
        HashMap<String, DW_intID> result;
        if (loadData) {
            //System.out.print("Loading " + filename + " ...");
            DW_SHBE_Collection aSHBEData;
            aSHBEData = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);
            result = (HashMap<String, DW_intID>) aSHBEData.getCTBRefToClaimantIDLookup();
        } else {
            File f;
            //f = DW_SHBE_Handler.getClaimantIDToCTBRefLookupFile(paymentType, filename);
            f = tDW_SHBE_Handler.getCTBRefToClaimantIDLookupFile(paymentType, filename);
            //System.out.print("Loading " + f + " ...");
            result = (HashMap<String, DW_intID>) Generic_StaticIO.readObject(f);
        }
        //System.out.println("...done.");
        return result;
    }

    protected HashSet<DW_intID> getUnderOccupiedIDs(
            DW_UnderOccupiedReport_Set underOccupiedSet,
            HashMap<String, DW_intID> tCTBRefByID,
            Integer key,
            HashMap<Integer, HashSet<DW_intID>> tIDByUnderOccupancies) {
        HashSet<DW_intID> result;
        if (tIDByUnderOccupancies.containsKey(key)) {
            return tIDByUnderOccupancies.get(key);
        }
        result = getUnderOccupiedIDs(underOccupiedSet, tCTBRefByID);
        tIDByUnderOccupancies.put(key, result);
        return result;
    }

    protected HashSet<DW_intID> getUnderOccupiedIDs(
            DW_UnderOccupiedReport_Set underOccupiedSet,
            HashMap<String, DW_intID> tCTBRefByID) {
        if (underOccupiedSet == null) {
            return null;
        }
        HashSet<DW_intID> result;
        result = new HashSet<DW_intID>();
        TreeMap<String, DW_UOReport_Record> map;
        map = underOccupiedSet.getMap();
        Iterator<String> ite;
        ite = map.keySet().iterator();
        DW_intID tID;
        while (ite.hasNext()) {
            tID = tCTBRefByID.get(ite.next());
            result.add(tID);
        }
        return result;
    }

    protected HashMap<DW_intID, Integer> loadIDByTenancyType(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes) {
        HashMap<DW_intID, Integer> result;
        if (tIDByTenancyTypes.containsKey(key)) {
            return tIDByTenancyTypes.get(key);
        }
        result = loadIDByTenancyType(loadData, filename, paymentType, key);
        tIDByTenancyTypes.put(key, result);
        return result;
    }

    /**
     *
     * @param loadData
     * @param filename
     * @param paymentType
     * @param key
     * @return
     */
    protected HashMap<DW_intID, Integer> loadIDByTenancyType(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key) {
        HashMap<DW_intID, Integer> result;
        if (loadData) {
            //System.out.print("Loading " + filename + " ...");
            DW_SHBE_Collection aSHBEData;
            aSHBEData = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);
            result = (HashMap<DW_intID, Integer>) aSHBEData.getClaimantIDToTenancyTypeLookup();
        } else {
            File f;
            f = tDW_SHBE_Handler.getClaimantIDToTenancyTypeLookupFile(
                    paymentType, filename);
            //System.out.print("Loading " + f + " ...");
            result = (HashMap<DW_intID, Integer>) Generic_StaticIO.readObject(f);
        }
        //System.out.println("...done.");
        return result;
    }

    protected HashMap<DW_intID, String> loadIDByPostcode(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes) {
        HashMap<DW_intID, String> result;
        if (tIDByPostcodes.containsKey(key)) {
            return tIDByPostcodes.get(key);
        }
        result = loadIDByPostcode(loadData, filename, paymentType, key);
        tIDByPostcodes.put(key, result);
        return result;
    }

    protected HashMap<DW_intID, String> loadIDByPostcode(
            boolean loadData,
            String filename,
            String paymentType,
            Integer key) {
        HashMap<DW_intID, String> result;
        if (loadData) {
            //System.out.print("Loading " + filename + " ...");
            DW_SHBE_Collection aSHBEData;
            aSHBEData = new DW_SHBE_Collection(
                    env,
                    filename,
                    paymentType);
            result = (HashMap<DW_intID, String>) aSHBEData.getClaimantIDToPostcodeLookup();
        } else {
            File f;
            f = tDW_SHBE_Handler.getClaimantIDToPostcodeLookupFile(
                    paymentType, filename);
            //System.out.print("Loading " + f + " ...");
            result = (HashMap<DW_intID, String>) Generic_StaticIO.readObject(f);
        }
        //System.out.println("...done.");
        return result;
    }

    private void writeTransitionFrequencies(
            TreeMap<String, Integer> transitions,
            File dirOut,
            String dirname,
            String name,
            boolean reportTenancyTransitionBreaks) {
        if (transitions.size() > 0) {
            File dirOut2;
            dirOut2 = new File(
                    dirOut,
                    dirname);
            dirOut2.mkdir();
            PrintWriter pw;
            pw = getFrequencyPrintWriter(
                    dirOut2,
                    name,
                    reportTenancyTransitionBreaks);
            pw.println("Count, Type");
            Iterator<String> ite2;
            ite2 = transitions.keySet().iterator();
            while (ite2.hasNext()) {
                String type;
                type = ite2.next();
                Integer count;
                count = transitions.get(type);
                pw.println(count + ", " + type);
            }
            pw.flush();
            pw.close();
        }
    }

    private PrintWriter getFrequencyPrintWriter(
            File dirOut,
            String name,
            boolean reportTenancyTransitionBreaks) {
        PrintWriter result;
        File dirOut2;
        dirOut2 = new File(
                dirOut,
                "Frequencies");
        if (reportTenancyTransitionBreaks) {
            dirOut2 = new File(
                    dirOut2,
                    "IncludingTenancyTransitionBreaks");
        } else {
            dirOut2 = new File(
                    dirOut2,
                    "NotIncludingTenancyTransitionBreaks");
        }
        dirOut2.mkdirs();
        File f;
        f = new File(
                dirOut2,
                name);
        System.out.println("Write " + f);
        result = Generic_StaticIO.getPrintWriter(f, false);
        return result;
    }

//    /**
//     *OLD
//     * @param postcodeHandler
//     * @param SHBEFilenames
//     * @param startIndex
//     * @param tenancyTypeGroups
//     * @param tenancyTypesGrouped
//     * @param regulatedGroups
//     * @param unregulatedGroups
//     * @param includes
//     * @param levels
//     * @param loadData
//     * @param postcodeChange
//     * @param checkPreviousPostcode
//     * @param checkPreviousTenure
//     * @param reportTenancyTransitionBreaks
//     */
//    public void tenancyTypeAndPostcodeChanges(
//            Generic_UKPostcode_Handler postcodeHandler,
//            String[] SHBEFilenames,
//            int startIndex,
//            TreeMap<String, ArrayList<Integer>> tenancyTypeGroups,
//            ArrayList<String> tenancyTypesGrouped,
//            ArrayList<Integer> regulatedGroups,
//            ArrayList<Integer> unregulatedGroups,
//            TreeMap<String, ArrayList<Integer>> includes,
//            ArrayList<String> levels,
//            boolean loadData,
//            Boolean postcodeChange,
//            Boolean checkPreviousPostcode,
//            boolean checkPreviousTenure,
//            boolean reportTenancyTransitionBreaks) {
//        // Load first data
//        String filename;
//        filename = SHBEFilenames[startIndex];
//        HashMap<Integer, HashMap<String, String>> tIDByPostcodes;
//        tIDByPostcodes = new HashMap<Integer, HashMap<String, String>>();
//        HashMap<Integer, HashMap<String, Integer>> tIDByTenancyTypes;
//        tIDByTenancyTypes = new HashMap<Integer, HashMap<String, Integer>>();
//        HashMap<String, String> tIDByPostcode0;
//        HashMap<String, Integer> tIDByTenancyType0;
//        if (loadData) {
//            Object[] aSHBEData;
//            aSHBEData = getSHBEData(filename);
//            tIDByPostcode0 = (HashMap<String, String>) aSHBEData[8];
//            tIDByTenancyType0 = (HashMap<String, Integer>) aSHBEData[9];
//        } else {
//            File f;
//            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToPostcodeLookupFile(
//                    filename);
//            tIDByPostcode0 = (HashMap<String, String>) Generic_StaticIO.readObject(
//                    f);
//            f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
//                    filename);
//            tIDByTenancyType0 = (HashMap<String, Integer>) Generic_StaticIO.readObject(
//                    f);
//        }
//        tIDByPostcodes.put(startIndex, tIDByPostcode0);
//        tIDByTenancyTypes.put(startIndex, tIDByTenancyType0);
//
//        HashMap<String, Boolean> initFirsts;
//        initFirsts = new HashMap<String, Boolean>();
//        HashMap<String, Integer> previousIndexs;
//        previousIndexs = new HashMap<String, Integer>();
//
//        HashMap<String, HashMap<String, ArrayList<String>>> tenancyTypeChangesAllIncludes;
//        tenancyTypeChangesAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();
//        HashMap<String, HashMap<String, ArrayList<String>>> tenancyTypeChangesGroupedAllIncludes;
//        tenancyTypeChangesGroupedAllIncludes = new HashMap<String, HashMap<String, ArrayList<String>>>();
//
//        Iterator<String> ite;
//        ite = includes.keySet().iterator();
//        while (ite.hasNext()) {
//            String includeKey;
//            includeKey = ite.next();
//            tenancyTypeChangesAllIncludes.put(
//                    includeKey,
//                    new HashMap<String, ArrayList<String>>());
//            tenancyTypeChangesGroupedAllIncludes.put(
//                    includeKey,
//                    new HashMap<String, ArrayList<String>>());
//            ArrayList<Integer> include;
//            include = includes.get(includeKey);
//            if (include.contains(startIndex)) {
//                initFirsts.put(includeKey, true);
//                previousIndexs.put(includeKey, startIndex);
//            } else {
//                initFirsts.put(includeKey, false);
//            }
//        }
//        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
//            HashMap<String, String> tIDByPostcode;
//            HashMap<String, Integer> tIDByTenancyType;
//            filename = SHBEFilenames[i];
//            // Load next data            
//            if (loadData) {
//                Object[] aSHBEData;
//                aSHBEData = getSHBEData(filename);
//                tIDByPostcode = (HashMap<String, String>) aSHBEData[8];
//                tIDByTenancyType = (HashMap<String, Integer>) aSHBEData[9];
//            } else {
//                File f;
//                f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToPostcodeLookupFile(
//                        filename);
//                tIDByPostcode = (HashMap<String, String>) Generic_StaticIO.readObject(
//                        f);
//                f = DW_SHBE_Handler.getClaimantNationalInsuranceNumberIDToTenureLookupFile(
//                        filename);
//                tIDByTenancyType = (HashMap<String, Integer>) Generic_StaticIO.readObject(
//                        f);
//            }
//            tIDByPostcodes.put(i, tIDByPostcode);
//            tIDByTenancyTypes.put(i, tIDByTenancyType);
//            ite = includes.keySet().iterator();
//            while (ite.hasNext()) {
//                String includeKey;
//                includeKey = ite.next();
//                ArrayList<Integer> include;
//                include = includes.get(includeKey);
//                Boolean initFirst;
//                initFirst = initFirsts.get(includeKey);
//                HashMap<String, ArrayList<String>> tenancyTypeChanges;
//                tenancyTypeChanges = tenancyTypeChangesAllIncludes.get(includeKey);
//                HashMap<String, ArrayList<String>> tenancyTypeChangesGrouped;
//                tenancyTypeChangesGrouped = tenancyTypeChangesGroupedAllIncludes.get(includeKey);
//                if (!initFirst) {
//                    if (include.contains(i)) {
//                        // Initialise
//                        tIDByPostcodes.put(i, tIDByPostcode);
//                        tIDByTenancyTypes.put(i, tIDByTenancyType);
//                        previousIndexs.put(includeKey, i);
//                        initFirsts.put(includeKey, true);
//                    }
//                } else {
//                    if (include.contains(i)) {
//                        int previousIndex;
//                        previousIndex = previousIndexs.get(includeKey);
//                        tIDByPostcode0 = tIDByPostcodes.get(previousIndex);
//                        tIDByTenancyType0 = tIDByTenancyTypes.get(previousIndex);
//                        String year0;
//                        year0 = DW_SHBE_Handler.getYear(SHBEFilenames[previousIndex]);
//                        String month0;
//                        month0 = DW_SHBE_Handler.getMonth(SHBEFilenames[previousIndex]);
//                        // Set Year and Month variables
//                        String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
//                        String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
//                        // Get TenancyTypeTranistionMatrix
//                        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
//                        tenancyTypeTranistionMatrix = getTenancyTypeTransitionMatrixAndWritePostcodeChangeDetails(
//                                tIDByTenancyType0,
//                                tIDByTenancyType,
//                                tIDByPostcode0,
//                                tIDByPostcode,
//                                postcodeHandler,
//                                postcodeChange,
//                                tenancyTypeChanges,
//                                year,
//                                month,
//                                checkPreviousTenure,
//                                tIDByTenancyTypes,
//                                checkPreviousPostcode,
//                                tIDByPostcodes,
//                                i,
//                                include);
//                        String type2;
//                        if (postcodeChange) {
//                            type2 = "/PostcodeChanged/";
//                        } else {
//                            type2 = "/PostcodeUnchanged/";
//                        }
//                        writeTenancyTypeTransitionMatrix(
//                                tenancyTypeTranistionMatrix, year0, month0,
//                                year, month, "All", type2, includeKey,
//                                checkPreviousTenure);
//                        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrixGrouped;
//                        tenancyTypeTranistionMatrixGrouped = getTenancyTypeTransitionMatrixGroupedAndWritePostcodeChangeDetails(
//                                tIDByTenancyType0,
//                                tIDByTenancyType,
//                                tIDByPostcode0,
//                                tIDByPostcode,
//                                postcodeHandler,
//                                regulatedGroups,
//                                unregulatedGroups,
//                                tenancyTypeChangesGrouped,
//                                year,
//                                month,
//                                checkPreviousTenure,
//                                tIDByTenancyTypes,
//                                postcodeChange,
//                                checkPreviousPostcode,
//                                tIDByPostcodes,
//                                i,
//                                include);
//                        writeTenancyTypeTransitionMatrixGrouped(
//                                tenancyTypeTranistionMatrixGrouped,
//                                tenancyTypesGrouped, year0, month0, year, month,
//                                "All", type2, includeKey, checkPreviousTenure);
//                        previousIndexs.put(includeKey, i);
//                    }
//                }
//            }
//            tIDByPostcode0 = tIDByPostcode;
//            tIDByTenancyType0 = tIDByTenancyType;
//        }
//        File dir;
//        dir = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
//                "All",
//                checkPreviousTenure);
//        if (postcodeChange) {
//            dir = new File(
//                    dir,
//                    "/PostcodeChanged/");
//        } else {
//            dir = new File(
//                    dir,
//                    "/PostcodeUnchanged/");
//        }
//        TreeMap<String, TreeMap<String, Integer>> allTrans;
//        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
//        // Calculate and write out Tenure Type transition frequencies
//        ite = includes.keySet().iterator();
//        while (ite.hasNext()) {
//            String includeKey;
//            includeKey = ite.next();
//            TreeMap<String, Integer> trans;
//            trans = new TreeMap<String, Integer>();
//            allTrans.put(includeKey, trans);
//            ArrayList<Integer> include;
//            include = includes.get(includeKey);
//            int max = Integer.MIN_VALUE;
//            HashMap<String, ArrayList<String>> tenancyTypeChanges;
//            tenancyTypeChanges = tenancyTypeChangesAllIncludes.get(includeKey);
//            Iterator<String> ite2;
//            ite2 = tenancyTypeChanges.keySet().iterator();
//            while (ite2.hasNext()) {
//                String nino = ite2.next();
//                ArrayList<String> transitions;
//                transitions = tenancyTypeChanges.get(nino);
//                max = Math.max(max, transitions.size());
//                String out;
//                out = "";
//                Iterator<String> ite3;
//                ite3 = transitions.iterator();
//                boolean doneFirst = false;
//                while (ite3.hasNext()) {
//                    String t;
//                    t = ite3.next();
//                    String[] splitT;
//                    splitT = t.split(":");
//                    if (reportTenancyTransitionBreaks) {
//                        if (!doneFirst) {
//                            out += splitT[0];
//                            doneFirst = true;
//                        } else {
//                            out += ", " + splitT[0];
//                        }
//                    } else {
//                        if (!doneFirst) {
//                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
//                                out += splitT[0];
//                                doneFirst = true;
//                            }
//                        } else {
//                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
//                                out += ", " + splitT[0];
//                            }
//                        }
//                    }
//                }
//                if (!out.isEmpty()) {
//                    if (trans.containsKey(out)) {
//                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
//                    } else {
//                        trans.put(out, 1);
//                    }
//                }
////                if (transitions.size() == max) {
////                    out = "Transitions";
////                    ite3 = transitions.iterator();
////                    while (ite3.hasNext()) {
////                        out += ", " + ite3.next();
////                    }
////                    System.out.println(out);
////                }
//            }
//            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
//        }
////        writeTransitionFrequencies(
////                allTrans,
////                dir,
////                "Frequencies.txt",
////                reportTenancyTransitionBreaks);
//        // Calculate and write out Grouped Tenure Type transition frequencies
//        allTrans = new TreeMap<String, TreeMap<String, Integer>>();
//        ite = includes.keySet().iterator();
//        while (ite.hasNext()) {
//            String includeKey;
//            includeKey = ite.next();
//            TreeMap<String, Integer> trans;
//            trans = new TreeMap<String, Integer>();
//            allTrans.put(includeKey, trans);
//            ArrayList<Integer> include;
//            include = includes.get(includeKey);
//            int max = Integer.MIN_VALUE;
//            HashMap<String, ArrayList<String>> tenancyTypeChanges;
//            tenancyTypeChanges = tenancyTypeChangesGroupedAllIncludes.get(includeKey);
//            Iterator<String> ite2;
//            ite2 = tenancyTypeChanges.keySet().iterator();
//            while (ite2.hasNext()) {
//                String nino = ite2.next();
//                ArrayList<String> transitions;
//                transitions = tenancyTypeChanges.get(nino);
//                max = Math.max(max, transitions.size());
//                String out;
//                out = "";
//                Iterator<String> ite3;
//                ite3 = transitions.iterator();
//                boolean doneFirst = false;
//                while (ite3.hasNext()) {
//                    String t;
//                    t = ite3.next();
//                    String[] splitT;
//                    splitT = t.split(":");
//                    if (reportTenancyTransitionBreaks) {
//                        if (!doneFirst) {
//                            out += splitT[0];
//                            doneFirst = true;
//                        } else {
//                            out += ", " + splitT[0];
//                        }
//                    } else {
//                        if (!doneFirst) {
//                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
//                                out += splitT[0];
//                                doneFirst = true;
//                            }
//                        } else {
//                            if (!splitT[0].contains(DW_SHBE_TenancyType_Handler.sMinus999)) {
//                                out += ", " + splitT[0];
//                            }
//                        }
//                    }
//                }
//                if (!out.isEmpty()) {
//                    if (trans.containsKey(out)) {
//                        Generic_Collections.addToTreeMapStringInteger(trans, out, 1);
//                    } else {
//                        trans.put(out, 1);
//                    }
//                }
////                if (transitions.size() == max) {
////                    out = "Transitions Grouped";
////                    ite3 = transitions.iterator();
////                    while (ite3.hasNext()) {
////                        out += ", " + ite3.next();
////                    }
////                    System.out.println(out);
////                }
//            }
//            System.out.println(includeKey + " maximum number of transitions " + max + " out of a possible " + (include.size() - 1));
//        }
////        writeTransitionFrequencies(
////                allTrans,
////                dir,
////                "FrequenciesGrouped.txt",
////                reportTenancyTransitionBreaks);
//    }
    /**
     * TODO: We want to be able to distinguish those claimants that have been:
     * continually claiming; claiming for at least 3 months; claiming for at
     * least 3 months, then stopped, then started claiming again.
     *
     * @param tDW_Postcode_Handler
     * @param doUnderOccupied
     * @param doCouncil
     * @param doRSL
     * @param underOccupiedData
     * @param lookupsFromPostcodeToLevelCode
     * @param SHBEFilenames
     * @param paymentType
     * @param claimantTypes
     * @param tenancyTypeGroups
     * @param tenancyTypesGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param types type = NewEntrant type = Stable type = Churn
     * @param distanceTypes
     * @param distances
     * @param DW_PersonIDtoDW_IDLookup
     */
    public void aggregateClaimants(
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL,
            Object[] underOccupiedData,
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames,
            String paymentType,
            ArrayList<String> claimantTypes,
            TreeMap<String, ArrayList<String>> tenancyTypeGroups,
            ArrayList<String> tenancyTypesGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances,
            HashMap<DW_PersonID, DW_intID> DW_PersonIDtoDW_IDLookup) {
        TreeMap<String, File> outputDirs;
        outputDirs = tDW_Files.getGeneratedSHBELevelDirsTreeMap(
                levels,
                doUnderOccupied,
                doCouncil,
                doRSL);
        // Init underOccupiedSets
        TreeMap<String, DW_UnderOccupiedReport_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UnderOccupiedReport_Set> RSLUnderOccupiedSets = null;
        if (doUnderOccupied) {
            if (doCouncil) {
                councilUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
            }
            if (doRSL) {
                RSLUnderOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
            }
        }

        // Declare iterators
        Iterator<String> claimantTypesIte;
        Iterator<String> tenancyTypeIte;
        Iterator<String> levelsIte;
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;
        Iterator<String> includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeName;
            includeName = includesIte.next();
            ArrayList<Integer> include;
            include = includes.get(includeName);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
            // Load first data
            DW_SHBE_Collection SHBEData0;
            SHBEData0 = new DW_SHBE_Collection(
                    env,
                    SHBEFilenames[i],
                    paymentType);
            String yM30;
            yM30 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            String yM30v;
            yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
            TreeMap<String, DW_SHBE_Record> records0;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
            claimantTypeTenureLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>>();
            // Write aggregated counts for first data
            claimantTypeTenureLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>>();
            claimantTypeTenureLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>>();
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType;
                claimantType = claimantTypesIte.next();
                // Initialise Dirs
                TreeMap<String, TreeMap<String, TreeMap<String, File>>> tenancyTypeLevelTypeDirs;
                tenancyTypeLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, File>>>();
                // Initialise AreaCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> tenancyTypeLevelTypeAreaCounts;
                tenancyTypeLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>();
                // Initialise TenureCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> tenancyTypeLevelTypeTenureCounts;
                tenancyTypeLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>();
                tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                while (tenancyTypeIte.hasNext()) {
                    String tenancyType = tenancyTypeIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<String, File>> levelTypeDirs;
                    levelTypeDirs = new TreeMap<String, TreeMap<String, File>>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts;
                    levelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts;
                    levelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>();
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        String level = levelsIte.next();
                        // Initialise Dirs
                        File outDir0;
                        outDir0 = outputDirs.get(level);
                        outDir0 = new File(
                                outDir0,
                                includeName);
                        TreeMap<String, File> typeDirs;
                        typeDirs = new TreeMap<String, File>();
                        // Initialise AreaCounts
                        TreeMap<String, TreeMap<String, Integer>> typeAreaCounts;
                        typeAreaCounts = new TreeMap<String, TreeMap<String, Integer>>();
                        // Initialise TenureCounts
                        TreeMap<String, TreeMap<Integer, Integer>> typeTenureCounts;
                        typeTenureCounts = new TreeMap<String, TreeMap<Integer, Integer>>();
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            // Initialise Dirs
                            File outDir1 = new File(
                                    outDir0,
                                    type);
                            outDir1 = new File(
                                    outDir1,
                                    claimantType);
                            outDir1 = new File(
                                    outDir1,
                                    tenancyType);
                            outDir1.mkdirs();
                            typeDirs.put(type, outDir1);
                            // Initialise AreaCounts
                            TreeMap<String, Integer> areaCount;
                            areaCount = new TreeMap<String, Integer>();
                            typeAreaCounts.put(type, areaCount);
                            // Initialise TenureCounts
                            TreeMap<Integer, Integer> tenancyTypeCounts;
                            tenancyTypeCounts = new TreeMap<Integer, Integer>();
                            typeTenureCounts.put(type, tenancyTypeCounts);
                        }
                        levelTypeDirs.put(level, typeDirs);
                        levelTypeAreaCounts.put(level, typeAreaCounts);
                        levelTypeTenureCounts.put(level, typeTenureCounts);
                    }
                    tenancyTypeLevelTypeDirs.put(tenancyType, levelTypeDirs);
                    tenancyTypeLevelTypeAreaCounts.put(tenancyType, levelTypeAreaCounts);
                    tenancyTypeLevelTypeTenureCounts.put(tenancyType, levelTypeTenureCounts);
                }
                claimantTypeTenureLevelTypeDirs.put(claimantType, tenancyTypeLevelTypeDirs);
                claimantTypeTenureLevelTypeAreaCounts.put(claimantType, tenancyTypeLevelTypeAreaCounts);
                claimantTypeTenureLevelTypeTenureCounts.put(claimantType, tenancyTypeLevelTypeTenureCounts);
            }
            records0 = SHBEData0.getRecords();
            // Init underOccupiedSets
            DW_UnderOccupiedReport_Set councilUOSet0 = null;
            DW_UnderOccupiedReport_Set RSLUOSet0 = null;
            if (doUnderOccupied) {
                if (doCouncil) {
                    councilUOSet0 = councilUnderOccupiedSets.get(yM30);
                }
                if (doRSL) {
                    RSLUOSet0 = RSLUnderOccupiedSets.get(yM30);
                }
            }
            // Iterator over records
            Iterator<String> recordsIte;
            recordsIte = records0.keySet().iterator();
            while (recordsIte.hasNext()) {
                String claimID = recordsIte.next();
                DW_SHBE_D_Record DRecord0 = records0.get(claimID).getDRecord();
                String postcode0 = DRecord0.getClaimantsPostcode();
                Integer tenancyType1Integer = DRecord0.getTenancyType();
                String tenancyType1 = tenancyType1Integer.toString();
                tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                while (tenancyTypeIte.hasNext()) {
                    String tenancyType;
                    tenancyType = tenancyTypeIte.next();
                    ArrayList<String> tenancyTypes;
                    tenancyTypes = tenancyTypeGroups.get(tenancyType);
                    if (tenancyTypes.contains(tenancyType1)) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level = levelsIte.next();
                            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                            tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
//                            String housingBenefitClaimReferenceNumber0;
//                            housingBenefitClaimReferenceNumber0 = DRecord0.getHousingBenefitClaimReferenceNumber();
                            String councilTaxBenefitClaimReferenceNumber0;
                            councilTaxBenefitClaimReferenceNumber0 = DRecord0.getCouncilTaxBenefitClaimReferenceNumber();
                            //String claimantNINO1 = DRecord0.getClaimantsNationalInsuranceNumber();
                            String claimantType;
//                            claimantType = tDW_SHBE_Handler.getClaimantType(
//                                    housingBenefitClaimReferenceNumber0);
                            claimantType = tDW_SHBE_Handler.getClaimantType(DRecord0);
                            Integer tenancyTypeInt = DRecord0.getTenancyType();
                            if (postcode0 != null) {
                                String areaCode;
                                areaCode = getAreaCode(
                                        level,
                                        postcode0,
                                        tLookupFromPostcodeToLevelCode);
                                String type;
                                type = "All";
                                if (types.contains(type)) {
                                    boolean doMainLoop = true;
                                    // Check for UnderOccupied
                                    if (doUnderOccupied) {
                                        // UnderOccupancy
                                        boolean doCouncilMainLoop = true;
                                        if (doCouncil) {
                                            DW_UOReport_Record councilUnderOccupied0 = null;
                                            if (councilUOSet0 != null) {
                                                councilUnderOccupied0 = councilUOSet0.getMap().get(
                                                        councilTaxBenefitClaimReferenceNumber0);
                                            }
                                            doCouncilMainLoop = councilUnderOccupied0 != null;
                                        }
                                        boolean doRSLMainLoop = true;
                                        if (doRSL) {
                                            DW_UOReport_Record RSLUnderOccupied0 = null;
                                            if (RSLUOSet0 != null) {
                                                RSLUnderOccupied0 = RSLUOSet0.getMap().get(
                                                        councilTaxBenefitClaimReferenceNumber0);
                                            }
                                            doRSLMainLoop = RSLUnderOccupied0 != null;
                                        }
                                        if (!(doCouncilMainLoop || doRSLMainLoop)) {
                                            doMainLoop = false;
                                        }
                                    }
                                    if (doMainLoop) {
                                        addToResult(
                                                claimantTypeTenureLevelTypeAreaCounts,
                                                claimantTypeTenureLevelTypeTenureCounts,
                                                areaCode,
                                                claimantType,
                                                tenancyType,
                                                level,
                                                type,
                                                tenancyTypeInt);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Write out results
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
            claimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>();
            //yearMonthClaimantTypeTenureLevelTypeCountAreas.put(yearMonth, claimantTypeTenureLevelTypeCountAreas);
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>();
            //yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(yearMonth, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
            // claimantTypeLoop
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType = claimantTypesIte.next();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> tenancyTypeLevelTypeCountAreas;
                tenancyTypeLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>();
                claimantTypeTenureLevelTypeCountAreas.put(claimantType, tenancyTypeLevelTypeCountAreas);
                tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                while (tenancyTypeIte.hasNext()) {
                    String tenancyType;
                    tenancyType = tenancyTypeIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                    levelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>();
                    tenancyTypeLevelTypeCountAreas.put(tenancyType, levelTypeCountAreas);
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
                            TreeMap<Integer, Integer> tenancyTypeCounts;
                            File dir;
                            areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenancyType).get(level).get(type);
                            tenancyTypeCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(tenancyType).get(level).get(type);
                            dir = claimantTypeTenureLevelTypeDirs.get(claimantType).get(tenancyType).get(level).get(type);
                            TreeMap<Integer, TreeSet<String>> countAreas;
                            countAreas = writeResults(
                                    areaCounts,
                                    tenancyTypeCounts,
                                    level,
                                    dir,
                                    yM30);
                            typeCountAreas.put(type, countAreas);
                        }
                    }
                }
            }

            // Get Top 10 areas
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>> yearMonthClaimantTypeTenureLevelTypeCountAreas;
            yearMonthClaimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>>();

            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>> yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>>();

            // Initialise tIDIndexes
            ArrayList<HashSet<DW_PersonID>> tIDIndexes;
            tIDIndexes = new ArrayList<HashSet<DW_PersonID>>();
            if (true) {
                HashSet<DW_PersonID> tID_HashSet;
                tID_HashSet = SHBEData0.getClaimantIDs();
                tIDIndexes.add(tID_HashSet);
            }

            while (includeIte.hasNext()) {
                i = includeIte.next();
                // Load next data
                DW_SHBE_Collection SHBEData1;
                SHBEData1 = new DW_SHBE_Collection(
                        env,
                        SHBEFilenames[i],
                        paymentType);
                HashMap<DW_intID, String> tIDByPostcode1;
                tIDByPostcode1 = SHBEData1.getClaimantIDToPostcodeLookup();
                HashMap<DW_intID, Integer> tIDByTenancyType1;
                tIDByTenancyType1 = SHBEData1.getClaimantIDToTenancyTypeLookup();
                // Set Year and Month variables
                String yM31 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                String yM31v;
                yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
//            String yearMonth = year + month;
//            String lastMonth_yearMonth;
//            String year = DW_SHBE_Handler.getYear(SHBEFilenames[i]);
//            String month = DW_SHBE_Handler.getMonth(SHBEFilenames[i]);
//            String yearMonth = year + month;
//            String lastMonth_yearMonth;
//            lastMonth_yearMonth = getLastMonth_yearMonth(
//                    year,
//                    month,
//                    SHBEFilenames,
//                    i,
//                    startIndex);
//            String lastYear_yearMonth;
//            lastYear_yearMonth = getLastYear_yearMonth(
//                    year,
//                    month,
//                    SHBEFilenames,
//                    i,
//                    startIndex);
                // Get UnderOccupancy Data
                DW_UnderOccupiedReport_Set councilUOSet1 = null;
                DW_UnderOccupiedReport_Set RSLUOSet1 = null;
                if (doUnderOccupied) {
                    if (doCouncil) {
                        councilUOSet1 = councilUnderOccupiedSets.get(yM31);
                    }
                    if (doRSL) {
                        RSLUOSet1 = RSLUnderOccupiedSets.get(yM31);
                    }
                }

                if (true) {
                    HashSet<DW_PersonID> tID_HashSet;
                    tID_HashSet = SHBEData1.getClaimantIDs();
                    tIDIndexes.add(tID_HashSet);
                }
                //records0 = (TreeMap<String, DW_SHBE_Record>) SHBEData0[0];
                TreeMap<String, DW_SHBE_Record> records1;
                records1 = SHBEData1.getRecords();
                /* Initialise A:
                 * output directories;
                 * claimantTypeTenureLevelTypeDirs;
                 * tenancyTypeLevelTypeDistanceDirs;
                 * tenancyTypeAreaCount;
                 * tenancyTypeDistanceAreaCount.
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
                claimantTypeTenureLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>> claimantTypeTenureLevelTypeDistanceDirs;
                claimantTypeTenureLevelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>>();
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
                claimantTypeTenureLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts;
                claimantTypeTenureLevelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>>();
                /* Initialise B:
                 * claimantTypeLevelTypeTenureCounts;
                 * claimantTypeLevelTypeDistanceTenureCounts;
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
                claimantTypeTenureLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts;
                claimantTypeTenureLevelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>>();
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypesIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<String, TreeMap<String, File>>> tenancyTypeLevelTypeDirs;
                    tenancyTypeLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, File>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>> tenancyTypeLevelTypeDistanceDirs;
                    tenancyTypeLevelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> tenancyTypeLevelTypeAreaCounts;
                    tenancyTypeLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>> tenancyTypeLevelTypeDistanceAreaCounts;
                    tenancyTypeLevelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> tenancyTypeLevelTypeTenureCounts;
                    tenancyTypeLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>> tenancyTypeLevelTypeDistanceTenureCounts;
                    tenancyTypeLevelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>();
                    tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                    while (tenancyTypeIte.hasNext()) {
                        String tenancyType = tenancyTypeIte.next();
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
                            File outDir0;
                            outDir0 = outputDirs.get(level);
                            outDir0 = new File(
                                    outDir0,
                                    includeName);
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
                                File outDir1 = new File(
                                        outDir0,
                                        type);
                                outDir1 = new File(
                                        outDir1,
                                        claimantType);
                                outDir1 = new File(
                                        outDir1,
                                        tenancyType);
                                outDir1.mkdirs();
                                typeDirs.put(type, outDir1);
                                // Initialise AreaCounts
                                TreeMap<String, Integer> areaCount;
                                areaCount = new TreeMap<String, Integer>();
                                typeAreaCounts.put(type, areaCount);
                                // Initialise TenureCounts
                                TreeMap<Integer, Integer> tenancyTypeCounts;
                                tenancyTypeCounts = new TreeMap<Integer, Integer>();
                                typeTenureCounts.put(type, tenancyTypeCounts);
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
                                    File outDir1 = new File(
                                            outDir0,
                                            distanceType);
                                    outDir1 = new File(
                                            outDir1,
                                            claimantType);
                                    outDir1 = new File(
                                            outDir1,
                                            tenancyType);
                                    outDir1 = new File(
                                            outDir1,
                                            "" + distance);
                                    outDir1.mkdirs();
                                    distanceDirs.put(distance, outDir1);
                                    // Initialise AreaCounts
                                    TreeMap<String, Integer> areaCounts;
                                    areaCounts = new TreeMap<String, Integer>();
                                    distanceAreaCounts.put(distance, areaCounts);
                                    // Initialise TenureCounts
                                    TreeMap<Integer, Integer> tenancyTypeCounts;
                                    tenancyTypeCounts = new TreeMap<Integer, Integer>();
                                    distanceTenureCounts.put(distance, tenancyTypeCounts);
                                }
                                typeDistanceDirs.put(distanceType, distanceDirs);
                                typeDistanceAreaCounts.put(distanceType, distanceAreaCounts);
                                typeDistanceTenureCounts.put(distanceType, distanceTenureCounts);
                            }
                            levelTypeDistanceDirs.put(level, typeDistanceDirs);
                            levelTypeDistanceAreaCounts.put(level, typeDistanceAreaCounts);
                            levelTypeDistanceTenureCounts.put(level, typeDistanceTenureCounts);
                        }
                        tenancyTypeLevelTypeDirs.put(tenancyType, levelTypeDirs);
                        tenancyTypeLevelTypeDistanceDirs.put(tenancyType, levelTypeDistanceDirs);
                        tenancyTypeLevelTypeAreaCounts.put(tenancyType, levelTypeAreaCounts);
                        tenancyTypeLevelTypeDistanceAreaCounts.put(tenancyType, levelTypeDistanceAreaCounts);
                        tenancyTypeLevelTypeTenureCounts.put(tenancyType, levelTypeTenureCounts);
                        tenancyTypeLevelTypeDistanceTenureCounts.put(tenancyType, levelTypeDistanceTenureCounts);
                    }
                    claimantTypeTenureLevelTypeDirs.put(claimantType, tenancyTypeLevelTypeDirs);
                    claimantTypeTenureLevelTypeDistanceDirs.put(claimantType, tenancyTypeLevelTypeDistanceDirs);
                    claimantTypeTenureLevelTypeAreaCounts.put(claimantType, tenancyTypeLevelTypeAreaCounts);
                    claimantTypeTenureLevelTypeDistanceAreaCounts.put(claimantType, tenancyTypeLevelTypeDistanceAreaCounts);
                    claimantTypeTenureLevelTypeTenureCounts.put(claimantType, tenancyTypeLevelTypeTenureCounts);
                    claimantTypeTenureLevelTypeDistanceTenureCounts.put(claimantType, tenancyTypeLevelTypeDistanceTenureCounts);
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
                recordsIte = records1.keySet().iterator();
                while (recordsIte.hasNext()) {
                    String claimID = recordsIte.next();
                    DW_SHBE_D_Record DRecord1 = records1.get(claimID).getDRecord();
                    String postcode1 = DRecord1.getClaimantsPostcode();
                    Integer tenancyType1Integer = DRecord1.getTenancyType();
                    String tenancyType1 = tenancyType1Integer.toString();
                    tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                    while (tenancyTypeIte.hasNext()) {
                        String tenancyType;
                        tenancyType = tenancyTypeIte.next();
                        ArrayList<String> tenancyTypes;
                        tenancyTypes = tenancyTypeGroups.get(tenancyType);
                        if (tenancyTypes.contains(tenancyType1)) {
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                String level = levelsIte.next();
                                TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                                tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                                TreeMap<String, Integer> unexpectedCounts;
                                unexpectedCounts = levelUnexpectedCounts.get(level);
                                String CTBRef1;
                                CTBRef1 = DRecord1.getCouncilTaxBenefitClaimReferenceNumber();
                                DW_PersonID claimantID1 = tDW_SHBE_Handler.getClaimantDW_PersonID(DRecord1);
                                String claimantType;
                                claimantType = tDW_SHBE_Handler.getClaimantType(DRecord1);
                                boolean doAdd = true;
                                // Check for UnderOccupied
                                if (doUnderOccupied) {
                                    // UnderOccupancy
                                    boolean councilDoAdd = false;
                                    if (doCouncil) {
                                        DW_UOReport_Record councilUO0 = null;
                                        DW_UOReport_Record councilUO1 = null;
                                        if (councilUOSet0 != null) {
                                            councilUO0 = councilUOSet0.getMap().get(
                                                    CTBRef1);
                                        }
                                        if (councilUOSet1 != null) {
                                            councilUO1 = councilUOSet1.getMap().get(
                                                    CTBRef1);
                                        }
                                        councilDoAdd = councilUO0 != null || councilUO1 != null;
                                    }
                                    boolean RSLDoAdd = false;
                                    if (doCouncil) {
                                        DW_UOReport_Record RSLUO0 = null;
                                        DW_UOReport_Record RSLUO1 = null;
                                        if (RSLUOSet0 != null) {
                                            RSLUO0 = RSLUOSet0.getMap().get(
                                                    CTBRef1);
                                        }
                                        if (RSLUOSet1 != null) {
                                            RSLUO1 = RSLUOSet1.getMap().get(
                                                    CTBRef1);
                                        }
                                        RSLDoAdd = RSLUO0 != null || RSLUO1 != null;
                                    }
                                    if (councilDoAdd || RSLDoAdd) {
                                        doAdd = false;
                                    }
                                }
                                if (doAdd) {
                                    Integer tenancyTypeInt = DRecord1.getTenancyType();
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
                                                    tenancyType,
                                                    level,
                                                    type,
                                                    tenancyTypeInt);
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
                                                        claimantID1,
                                                        i,
                                                        include,
                                                        tIDIndexes);
                                                if (!hasBeenSeenBefore) {
                                                    type = "OnFlow";
                                                    if (types.contains(type)) {
                                                        addToResult(
                                                                claimantTypeTenureLevelTypeAreaCounts,
                                                                claimantTypeTenureLevelTypeTenureCounts,
                                                                areaCode,
                                                                claimantType,
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
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
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
                                                    }
// Here we could also try to work out for those Return flows, have any moved from previous claim postcode or changed tenancyType.
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
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
                                                    }
                                                } else /*
                                                     * There is an issue here as it seems that sometimes a postcode is misrecorded 
                                                     * initially and is then corrected. Some thought is needed about how to identify
                                                     * and deal with this and discern if this has any significant effect on the 
                                                     * results.
                                                 */ if (postcode0.equalsIgnoreCase(postcode1)) {
                                                    // Stable
                                                    type = "Stable";
                                                    if (types.contains(type)) {
                                                        addToResult(
                                                                claimantTypeTenureLevelTypeAreaCounts,
                                                                claimantTypeTenureLevelTypeTenureCounts,
                                                                areaCode,
                                                                claimantType,
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
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
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
                                                    }
                                                    // AllOutChurn
                                                    type = "AllOutChurn";
                                                    if (types.contains(type)) {
                                                        addToResult(
                                                                claimantTypeTenureLevelTypeAreaCounts,
                                                                claimantTypeTenureLevelTypeTenureCounts,
                                                                areaCode,
                                                                claimantType,
                                                                tenancyType,
                                                                level,
                                                                type,
                                                                tenancyTypeInt);
                                                    }
                                                    double distance;
                                                    distance = tDW_Postcode_Handler.getDistanceBetweenPostcodes(
                                                            yM30v,
                                                            yM31v,
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
                                                                        tenancyType,
                                                                        level,
                                                                        type,
                                                                        tenancyTypeInt,
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
                                                                        tenancyType,
                                                                        level,
                                                                        type,
                                                                        tenancyTypeInt,
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
                                                                        tenancyType,
                                                                        level,
                                                                        type,
                                                                        tenancyTypeInt,
                                                                        distanceThreshold);
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
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
                claimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>();
                yearMonthClaimantTypeTenureLevelTypeCountAreas.put(yM31, claimantTypeTenureLevelTypeCountAreas);
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
                claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>();
                yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(yM31, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType = claimantTypesIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> tenancyTypeLevelTypeCountAreas;
                    tenancyTypeLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>();
                    claimantTypeTenureLevelTypeCountAreas.put(claimantType, tenancyTypeLevelTypeCountAreas);
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>> tenancyTypeLevelDistanceTypeDistanceCountAreas;
                    tenancyTypeLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>();
                    claimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(claimantType, tenancyTypeLevelDistanceTypeDistanceCountAreas);
                    tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
                    while (tenancyTypeIte.hasNext()) {
                        String tenancyType;
                        tenancyType = tenancyTypeIte.next();
                        TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                        levelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>();
                        tenancyTypeLevelTypeCountAreas.put(tenancyType, levelTypeCountAreas);
                        TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>> levelDistanceTypeDistanceCountAreas;
                        levelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>();
                        tenancyTypeLevelDistanceTypeDistanceCountAreas.put(tenancyType, levelDistanceTypeDistanceCountAreas);
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
                                TreeMap<Integer, TreeSet<String>> yM31CountAreas;
                                TreeMap<Integer, Integer> tenancyTypeCounts;
                                File dir;
                                areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenancyType).get(level).get(type);
                                yM31CountAreas = yearMonthClaimantTypeTenureLevelTypeCountAreas.get(yM31).get(claimantType).get(tenancyType).get(level).get(type);
                                tenancyTypeCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(tenancyType).get(level).get(type);
                                dir = claimantTypeTenureLevelTypeDirs.get(claimantType).get(tenancyType).get(level).get(type);
                                TreeMap<Integer, TreeSet<String>> countAreas;
                                countAreas = writeResults(
                                        areaCounts,
                                        yM31CountAreas,
                                        yM31,
                                        tenancyTypeCounts,
                                        level,
                                        dir);
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
                                    TreeMap<Integer, TreeSet<String>> yM31CountAreas;
                                    TreeMap<Integer, Integer> tenancyTypeCounts;
                                    File dir;
                                    areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(tenancyType).get(level).get(distanceType).get(distance);
                                    yM31CountAreas = yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.get(yM31).get(claimantType).get(tenancyType).get(level).get(distanceType).get(distance);
                                    tenancyTypeCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(tenancyType).get(level).get(distanceType).get(distance);
                                    dir = claimantTypeTenureLevelTypeDistanceDirs.get(claimantType).get(tenancyType).get(level).get(distanceType).get(distance);
                                    TreeMap<Integer, TreeSet<String>> countAreas;
                                    countAreas = writeResults(
                                            areaCounts,
                                            yM31CountAreas,
                                            yM31,
                                            tenancyTypeCounts,
                                            level,
                                            dir);
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
                                    + " Tenure " + tenancyType
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
                if (doCouncil) {
                    councilUOSet0 = councilUOSet1;
                }
                if (doRSL) {
                    RSLUOSet0 = RSLUOSet1;
                }
                records0 = records1;
                //tIDByPostcode0 = tIDByPostcode1;
                yM30 = yM31;
                yM30v = yM31v;
            }
        }
    }

    protected static TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> yM31CountAreas,
            String yM31,
            TreeMap<Integer, Integer> tenancyTypeCounts,
            String level,
            File dir) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result = writeResults(
                    areaCounts,
                    tenancyTypeCounts,
                    level,
                    dir,
                    yM31);
            int num = 5;
            // Write out areas with biggest increases from last year
            writeExtremeAreaChanges(
                    result,
                    yM31CountAreas,
                    "LastYear",
                    num,
                    dir,
                    yM31);
            return result;
        } else {
            return null;
        }
    }

    protected static TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, Integer> tenancyTypeCounts,
            String level,
            File dir,
            String yM3) {
        if (areaCounts.size() > 0) {
            int num = 5;
            TreeMap<Integer, TreeSet<String>> result;
            // Write out counts by area
            result = writeCountsByArea(
                    areaCounts,
                    level,
                    dir,
                    yM3);
            // Write out areas with highest counts
            writeAreasWithHighestNumbersOfClaimants(
                    result,
                    num,
                    dir,
                    yM3);
            // Write out counts by tenancyType
            writeCountsByTenure(
                    tenancyTypeCounts,
                    dir,
                    yM3);
            return result;
        } else {
            return null;
        }
    }

    protected static void writeExtremeAreaChanges(
            TreeMap<Integer, TreeSet<String>> countAreas,
            TreeMap<Integer, TreeSet<String>> lastTimeCountAreas,
            String yM30,
            int num,
            File dir,
            String yM31) {
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
                    yM30,
                    num,
                    dir,
                    yM31);
            TreeMap<Double, TreeSet<String>> relativeDiffsAreas;
            relativeDiffsAreas = getCountAreas(areaRelativeDiffs);
            writeDiffs(
                    relativeDiffsAreas,
                    "Relative",
                    yM30,
                    num,
                    dir,
                    yM31);
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
            String yM30,
            int num,
            File dir,
            String yM31) {
        if (diffsAreas.size() > 0) {
            PrintWriter pw;
            String type;
            type = "Increases";
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "ExtremeAreaChanges" + name + type + yM30 + "_TO_" + yM31 + ".csv");
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
                    "ExtremeAreaChanges" + name + type + yM30 + "_TO_" + yM31 + ".csv");
            iteD = diffsAreas.keySet().iterator();
            writeDiffs(
                    diffsAreas,
                    num,
                    type,
                    pw,
                    iteD);
        }
    }

    protected static void writeDiffs(
            TreeMap<Double, TreeSet<String>> diffsAreas,
            int num,
            String type,
            PrintWriter pw,
            Iterator<Double> iteD) {
        if (diffsAreas.size() > 0) {
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
     * @param yM3
     */
    protected static void writeAreasWithHighestNumbersOfClaimants(
            TreeMap<Integer, TreeSet<String>> countAreas,
            int num,
            File dir,
            String yM3) {
        if (countAreas.size() > 0) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "HighestClaimants" + yM3 + ".csv");
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
    }

    /**
     *
     * @param areaCounts
     * @param level
     * @param dir
     * @param yM3
     * @return {@code TreeMap<Integer, TreeSet<String>>} count by list of areas.
     * This is an ordered list from minimum to maximum counts.
     */
    protected static TreeMap<Integer, TreeSet<String>> writeCountsByArea(
            TreeMap<String, Integer> areaCounts,
            String level,
            File dir,
            String yM3) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result;
            result = new TreeMap<Integer, TreeSet<String>>();
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    yM3 + ".csv");
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
        } else {
            return null;
        }
    }

    private static void writeCountsByTenure(
            TreeMap<Integer, Integer> tenancyTypeCounts,
            File dir,
            String yM3) {
        if (tenancyTypeCounts.size() > 0) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "CountsByTenure" + yM3 + ".csv");
            pw.println("Tenure, Count");
            Iterator<Integer> ite;
            ite = tenancyTypeCounts.keySet().iterator();
            while (ite.hasNext()) {
                Integer tenancyType0 = ite.next();
                Integer count = tenancyTypeCounts.get(tenancyType0);
                pw.println(tenancyType0 + ", " + count);
            }
            pw.close();
        }
    }

    private static void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts,
            String areaCode,
            String claimantType,
            String tenancyType,
            String level,
            String type,
            Integer tenancyTypeInt) {
        addToAreaCount(claimantTypeTenureLevelTypeAreaCounts, areaCode, claimantType, tenancyType, level, type);
        TreeMap<Integer, Integer> tenancyTypeCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(tenancyType).get(level).get(type);
        Generic_Collections.addToTreeMapIntegerInteger(
                tenancyTypeCounts,
                tenancyTypeInt,
                1);
    }

    private static void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts,
            String areaCode,
            String claimantType,
            String tenancyType,
            String level,
            String type,
            Integer tenancyTypeInt,
            double distance) {
        addToAreaCount(claimantTypeTenureLevelTypeDistanceAreaCounts, areaCode, claimantType, tenancyType, level, type, distance);
        TreeMap<Integer, Integer> tenureCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(tenancyType).get(level).get(type).get(distance);
        Generic_Collections.addToTreeMapIntegerInteger(
                tenureCounts,
                tenancyTypeInt,
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
            DW_intID tID,
            HashMap<String, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            String yM30) {
        HashMap<DW_intID, Integer> tIDByTenancyType;
        tIDByTenancyType = tIDByTenancyTypes.get(yM30);
        return tIDByTenancyType.containsKey(tID);
    }

    public static void recordTenancyTypeChanges(
            String CTBRef,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM3,
            String tenancyTypeChange) {
        ArrayList<String> tenancyTypeChangeList;
        tenancyTypeChangeList = tenancyTypeChanges.get(CTBRef);
        if (tenancyTypeChangeList == null) {
            tenancyTypeChangeList = new ArrayList<String>();
            tenancyTypeChanges.put(CTBRef, tenancyTypeChangeList);
//            if (tenancyTypeChange.startsWith("-999 ")) {
//                int debug = 1;
//            }
        }
        tenancyTypeChangeList.add(tenancyTypeChange + ":" + yM3);

//        if (tenancyTypeChangeList.size() == 2) {
//            String firstChange = tenancyTypeChangeList.get(0);
//            String SecondChange = tenancyTypeChange + ":" + yM3;
//            String[] s1 = firstChange.split(":");
//            String[] s1s = s1[0].split(" - ");
//            String[] s2s = tenancyTypeChange.split(" - ");
//            if (!s1s[1].equalsIgnoreCase(s2s[0])) {
//                int debug = 1;
//                System.out.println("s1s[1].equalsIgnoreCase(s2s[0]) " + s1s[1] + " != " + s2s[0] + " CTBRef " + CTBRef + " recordTenancyTypeChanges(String,HashMap, String,String)");
//            }
//
//        }
    }

//    public static void recordTenancyTypeChanges(
//            DW_ID tID,
//            HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
//            String yM3,
//            String tenancyTypeChange) {
//        ArrayList<String> tenancyTypeChangeList;
//        tenancyTypeChangeList = tenancyTypeChanges.get(tID);
//        if (tenancyTypeChangeList == null) {
//            tenancyTypeChangeList = new ArrayList<String>();
//            tenancyTypeChanges.put(tID, tenancyTypeChangeList);
//        }
//        tenancyTypeChangeList.add(tenancyTypeChange + ":" + yM3);
//    }
    public String getTenancyTypeTransitionName(
            Integer tenancyType0,
            Integer tenancyType1) {
        return getTenancyTypeTransitionName(
                tenancyType0.toString(),
                tenancyType1.toString());
    }

    public String[] getTenancyTypeTransitionName(
            Integer tenancyType0,
            boolean originUnderoccupied,
            Integer tenancyType1,
            boolean destinationUnderoccupied) {
        return getTenancyTypeTransitionName(
                tenancyType0.toString(),
                originUnderoccupied,
                tenancyType1.toString(),
                destinationUnderoccupied);
    }

    public String[] getTenancyTypeTransitionName(
            String tenancyType0,
            boolean originUnderoccupied,
            String tenancyType1,
            boolean destinationUnderoccupied) {
        String[] result;
        result = new String[3];
        String s0;
        s0 = tenancyType0;
        if (originUnderoccupied) {
            s0 += sU;
        }
        String s1;
        s1 = tenancyType1;
        if (destinationUnderoccupied) {
            s1 += sU;
        }
        result[0] = s0 + " - " + s1;
        result[1] = s0;
        result[2] = s1;
        return result;
    }

    public String getTenancyTypeTransitionName(
            String tenancyType0,
            String tenancyType1) {
        String result;
        result = tenancyType0 + " - " + tenancyType1;
        return result;
    }

    /**
     *
     * @param tIDByTenancyType0 Before
     * @param tIDByTenancyType1 Now
     * @param tIDByTenancyTypes
     * @param yM30
     * @return A count matrix of tenancyType changes {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count
     * }
     */
    public Object[] getMultipleTenancyTypeTranistionMatrix(
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<String, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            //ArrayList<Integer> include,
            String yM30) {
        Object[] result;
        result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> tenancyTypeTranistionMatrix;
        tenancyTypeTranistionMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        result[0] = tenancyTypeTranistionMatrix;
        HashMap<DW_intID, String> tIDTenureChange;
        tIDTenureChange = new HashMap<DW_intID, String>();
        result[1] = tIDTenureChange;
        Iterator<DW_intID> ite;
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    tID,
                    tIDByTenancyTypes,
                    yM30);
            Integer tenancyType1 = tIDByTenancyType1.get(tID);
            Integer tenancyType0 = tIDByTenancyType0.get(tID);
            if (tenancyType0 == null) {
                tenancyType0 = -999;
            }
            if (hasPreviousTenureChange) {
                if (tenancyTypeTranistionMatrix.containsKey(tenancyType1)) {
                    TreeMap<Integer, Integer> tenancyTypeCount;
                    tenancyTypeCount = tenancyTypeTranistionMatrix.get(tenancyType1);
                    //Generic_Collections.addToTreeMapStringInteger(tenancyTypeCount, tenancyType0, 1);
                    Generic_Collections.addToTreeMapIntegerInteger(tenancyTypeCount, tenancyType0, 1);
                } else {
                    TreeMap<Integer, Integer> tenancyTypeCount;
                    tenancyTypeCount = new TreeMap<Integer, Integer>();
                    tenancyTypeCount.put(tenancyType0, 1);
                    tenancyTypeTranistionMatrix.put(tenancyType1, tenancyTypeCount);
                }
            }
            if (tenancyType0.compareTo(tenancyType1) != 0) {
                tIDTenureChange.put(
                        tID,
                        "" + tenancyType0 + " - " + tenancyType1);
            }
        }
        Set<DW_intID> set;
        set = tIDByTenancyType1.keySet();
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    tID,
                    tIDByTenancyTypes,
                    yM30);
            Integer tenancyType0 = tIDByTenancyType0.get(tID);
            if (tenancyType0 == null) {
                tenancyType0 = -999;
            }
            Integer tenancyType1;
            tenancyType1 = -999;
            if (hasPreviousTenureChange) {
                if (!set.contains(tID)) {
                    if (tenancyTypeTranistionMatrix.containsKey(tenancyType1)) {
                        TreeMap<Integer, Integer> tenancyTypeCount;
                        tenancyTypeCount = tenancyTypeTranistionMatrix.get(tenancyType1);
                        //Generic_Collections.addToTreeMapStringInteger(tenancyTypeCount, tenancyType0, 1);
                        Generic_Collections.addToTreeMapIntegerInteger(tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<Integer, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<Integer, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        tenancyTypeTranistionMatrix.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
//            if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                tIDTenureChange.put(
//                        tID,
//                        "" + tenancyType0 + " - " + tenancyType1);
//            }
        }
        return result;
    }

    /**
     *
     * @param tIDByPostcodes
     * @param indexYM3s
     * @param tID0
     * @param CTBRef
     * @param tCTBRefByIDs
     * @param tIDByTenancyTypes
     * @param doUnderOccupancy
     * @param tUnderOccupancies
     * @param tUnderOccupanciesCouncil
     * @param tUnderOccupanciesRSL
     * @param i
     * @param include
     * @return
     */
    public Object[] getPreviousTenureAndPostcode(
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            //HashMap<String, DW_SHBE_Collection> tSHBECollections,
            HashMap<Integer, String> indexYM3s,
            DW_intID tID0,
            String CTBRef,
            //HashMap<Integer, HashMap<DW_ID, String>> tIDByCTBRefs,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            boolean doUnderOccupancy,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[4];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        String yM3;
        yM3 = indexYM3s.get(index);

        String postcode = sAAN_NAA;
        DW_SHBE_Collection tSHBECollection;

        HashMap<DW_intID, String> tIDByPostcode;

//        HashMap<DW_ID, String> tIDByCTBRef;
//        tIDByCTBRef = tIDByCTBRefs.get(index);
//        String CTBRef;
//        CTBRef = tIDByCTBRef.get(tID0);
        DW_intID tID;

//        System.out.println("i " + i);
//        System.out.println("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tenancyType = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
            yM3 = indexYM3s.get(previousIndex);
            result[3] = yM3;

            HashMap<String, DW_intID> tCTBRefByID;
            tCTBRefByID = tCTBRefByIDs.get(previousIndex);
            tID = tCTBRefByID.get(CTBRef);

            if (tID != null) {

                tIDByPostcode = tIDByPostcodes.get(previousIndex);

                //tSHBECollection = tSHBECollections.get(yM3);
//            System.out.println("previousIndex " + previousIndex);
                HashMap<DW_intID, Integer> tIDByTenancyType;
                tIDByTenancyType = tIDByTenancyTypes.get(previousIndex);
                if (tIDByTenancyType != null) {
                    tenancyType = tIDByTenancyType.get(tID);
                    if (doUnderOccupancy) {
                        HashSet<DW_intID> tUnderOccupancy;
                        tUnderOccupancy = tUnderOccupancies.get(previousIndex);
                        if (tUnderOccupancy != null) {
                            if (tUnderOccupancy.contains(tID)) {
                                underOccupancy = sU;
                                if (tenancyType == null) {
                                    tenancyType = -999;
                                    HashSet<DW_intID> tUnderOccupancyCouncil;
                                    tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                                    if (tUnderOccupancyCouncil.contains(tID)) {
                                        result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                                    } else {
                                        result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                                    }
                                    //result[1] = include.indexOf(previousIndex);
                                    result[1] = previousIndex;

                                    postcode = tIDByPostcode.get(tID);
                                    if (postcode == null) {
                                        postcode = sAAN_NAA;
                                    }
                                    result[2] = postcode;
                                    return result;
                                } else if (tenancyType == -999) {
                                    HashSet<DW_intID> tUnderOccupancyCouncil;
                                    tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                                    if (tUnderOccupancyCouncil.contains(tID)) {
                                        result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                                    } else {
                                        result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                                    }
                                    //result[1] = include.indexOf(previousIndex);
                                    result[1] = previousIndex;
                                    result[2] = sAAN_NAA;
                                    return result;
                                } else {
                                    result[0] = Integer.toString(tenancyType) + underOccupancy;
                                    //result[1] = include.indexOf(previousIndex);
                                    result[1] = previousIndex;
                                    postcode = tIDByPostcode.get(tID);
                                    if (postcode == null) {
                                        postcode = sAAN_NAA;
                                    }
                                    result[2] = postcode;
                                    return result;
                                }
                            }
                        }
                    }
                    if (tenancyType != null) {
                        if (tenancyType != -999) {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            result[2] = sAAN_NAA;
                            return result;
                        }
                    } else {
                        int a = 1; //just checking sometimes we get here.
                    }
                }
            }

        }
        if (tenancyType == null) {
            tenancyType = -999;
        }
        result[0] = Integer.toString(tenancyType) + underOccupancy;
        result[1] = null;
        result[2] = postcode;
        result[3] = yM3;
        return result;
    }

    /**
     *
     * @param tID
     * @param tIDByTenancyTypes
     * @param i
     * @param include
     * @param tUnderOccupanciesRSL
     * @param tUnderOccupanciesCouncil
     * @return
     */
    @Deprecated
    public Object[] getPreviousTenureAndPostcode(
            boolean flag,
            HashMap<String, DW_SHBE_Collection> tSHBECollections,
            HashMap<Integer, String> indexYM3s,
            DW_intID tID0,
            String CTBRef,
            //HashMap<Integer, HashMap<DW_ID, String>> tIDByCTBRefs,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[4];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        String yM3;
        yM3 = indexYM3s.get(index);

        String postcode = sAAN_NAA;
        DW_SHBE_Collection tSHBECollection;

//        HashMap<DW_ID, String> tIDByCTBRef;
//        tIDByCTBRef = tIDByCTBRefs.get(index);
//        String CTBRef;
//        CTBRef = tIDByCTBRef.get(tID0);
        DW_intID tID;

//        System.out.println("i " + i);
//        System.out.println("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tenancyType = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
            yM3 = indexYM3s.get(previousIndex);
            result[3] = yM3;
            tSHBECollection = tSHBECollections.get(yM3);
//            System.out.println("previousIndex " + previousIndex);

            HashMap<String, DW_intID> tCTBRefByID;
            tCTBRefByID = tCTBRefByIDs.get(previousIndex);
            tID = tCTBRefByID.get(CTBRef);

            if (tID != null) {

                HashMap<DW_intID, Integer> tIDByTenancyType;
                tIDByTenancyType = tIDByTenancyTypes.get(previousIndex);
                HashSet<DW_intID> tUnderOccupancy;
                tUnderOccupancy = tUnderOccupancies.get(previousIndex);
                if (tIDByTenancyType != null) {
                    tenancyType = tIDByTenancyType.get(tID);
                    if (tUnderOccupancy.contains(tID)) {
                        underOccupancy = sU;
                        if (tenancyType == null) {
                            tenancyType = -999;
                            HashSet<DW_intID> tUnderOccupancyCouncil;
                            tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                            if (tUnderOccupancyCouncil.contains(tID)) {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            DW_SHBE_Record rec;
                            rec = tSHBECollection.getRecords().get(CTBRef);
                            if (rec == null) {
                                result[2] = sAAN_NAA;
                            } else {
                                result[2] = rec.getDRecord().getClaimantsPostcode();
                            }
                            return result;
                        } else if (tenancyType == -999) {
                            HashSet<DW_intID> tUnderOccupancyCouncil;
                            tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                            if (tUnderOccupancyCouncil.contains(tID)) {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            result[2] = sAAN_NAA;
                            return result;
                        } else {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            DW_SHBE_Record rec;
                            rec = tSHBECollection.getRecords().get(CTBRef);
                            if (rec == null) {
                                result[2] = sAAN_NAA;
                            } else {
                                result[2] = rec.getDRecord().getClaimantsPostcode();
                            }
                            return result;
                        }
                    }
                    if (tenancyType != null) {
                        if (tenancyType != -999) {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            result[2] = sAAN_NAA;
                            return result;
                        }
                    } else {
                        int a = 1; //just checking sometimes we get here.
                    }
                }
            }

        }
        if (tenancyType == null) {
            tenancyType = -999;
        }
        result[0] = Integer.toString(tenancyType) + underOccupancy;
        result[1] = null;
        result[2] = postcode;
        result[3] = yM3;
        return result;
    }

    /**
     * 
     * @param tID0
     * @param CTBRef
     * @param tCTBRefByIDs
     * @param tIDByTenancyTypes
     * @param tUnderOccupancies
     * @param tUnderOccupanciesCouncil
     * @param tUnderOccupanciesRSL
     * @param i
     * @param include
     * @return 
     */
    public Object[] getPreviousTenure(
            DW_intID tID0,
            String CTBRef,
            //HashMap<Integer, HashMap<DW_ID, String>> tIDByCTBRefs,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);

//        HashMap<DW_ID, String> tIDByCTBRef;
//        tIDByCTBRef = tIDByCTBRefs.get(index);
//        String CTBRef;
//        CTBRef = tIDByCTBRef.get(tID0);
        DW_intID tID;

//        System.out.println("i " + i);
//        System.out.println("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tenancyType = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
//            System.out.println("previousIndex " + previousIndex);

            HashMap<String, DW_intID> tCTBRefByID;
            tCTBRefByID = tCTBRefByIDs.get(previousIndex);
            tID = tCTBRefByID.get(CTBRef);

            if (tID != null) {

                HashMap<DW_intID, Integer> tIDByTenancyType;
                tIDByTenancyType = tIDByTenancyTypes.get(previousIndex);
                HashSet<DW_intID> tUnderOccupancy;
                tUnderOccupancy = tUnderOccupancies.get(previousIndex);
                if (tIDByTenancyType != null) {
                    tenancyType = tIDByTenancyType.get(tID);
                    if (tUnderOccupancy.contains(tID)) {
                        underOccupancy = sU;
                        if (tenancyType == null) {
                            tenancyType = -999;
                            HashSet<DW_intID> tUnderOccupancyCouncil;
                            tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                            if (tUnderOccupancyCouncil.contains(tID)) {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        } else if (tenancyType == -999) {
                            HashSet<DW_intID> tUnderOccupancyCouncil;
                            tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                            if (tUnderOccupancyCouncil.contains(tID)) {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(tenancyType) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        } else {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        }
                    }
                    if (tenancyType != null) {
                        if (tenancyType != -999) {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        }
                    } else {
                        int a = 1; //just checking sometimes we get here.
                    }
                }
            }

        }
        if (tenancyType == null) {
            tenancyType = -999;
        }
        result[0] = Integer.toString(tenancyType) + underOccupancy;
        result[1] = null;
        return result;
    }

    /**
     *
     * @param tID0
     * @param CTBRef
     * @param tIDByTenancyTypes
     * @param tCTBRefByIDs
     * @param i
     * @param include
     * @param tUnderOccupancies
     * @return
     */
    public Object[] getPreviousTenure(
            DW_intID tID0,
            String CTBRef,
            //HashMap<Integer, HashMap<DW_ID, String>> tIDByCTBRefs,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);

//        HashMap<DW_ID, String> tIDByCTBRef;
//        tIDByCTBRef = tIDByCTBRefs.get(index);
//        String CTBRef;
//        CTBRef = tIDByCTBRef.get(tID0);
        DW_intID tID;

//        System.out.println("i " + i);
//        System.out.println("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tenancyType = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
//            System.out.println("previousIndex " + previousIndex);

            HashMap<String, DW_intID> tCTBRefByID;
            tCTBRefByID = tCTBRefByIDs.get(previousIndex);
            tID = tCTBRefByID.get(CTBRef);

            if (tID != null) {

                HashMap<DW_intID, Integer> tIDByTenancyType;
                tIDByTenancyType = tIDByTenancyTypes.get(previousIndex);
                HashSet<DW_intID> tUnderOccupancy;
                tUnderOccupancy = tUnderOccupancies.get(previousIndex);
                if (tIDByTenancyType != null) {
                    tenancyType = tIDByTenancyType.get(tID);
                    if (tUnderOccupancy.contains(tID)) {
                        underOccupancy = sU;
                    }
                    if (tenancyType != null) {
                        if (tenancyType != -999) {
                            result[0] = Integer.toString(tenancyType) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        }
                    }
                }
            }

        }
        if (tenancyType == null) {
            tenancyType = -999;
        }
        result[0] = Integer.toString(tenancyType) + underOccupancy;
        result[1] = null;
        return result;
    }

    /**
     * 
     * @param tIDByPostcodes
     * @param tSHBECollection0
     * @param tSHBECollection1
     * @param tIDByTenancyType0
     * @param tIDByTenancyType1
     * @param tIDByTenancyTypes
     * @param tUnderOccupiedIDs0
     * @param tUnderOccupiedIDsRSL0
     * @param tUnderOccupiedIDsCouncil0
     * @param tUnderOccupiedIDs1
     * @param tUnderOccupiedIDsCouncil1
     * @param tUnderOccupiedIDsRSL1
     * @param tUnderOccupancies
     * @param tUnderOccupanciesCouncil
     * @param tUnderOccupanciesRSL
     * @param tenancyTypeChanges
     * @param yM30
     * @param yM31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param indexYM3s
     * @param underOccupiedSet0
     * @param underOccupiedSetCouncil0
     * @param underOccupiedSetRSL0
     * @param tIDByCTBRef0
     * @param tCTBRefByID0
     * @param underOccupiedSet1
     * @param underOccupiedSetCouncil1
     * @param underOccupiedSetRSL1
     * @param tIDByCTBRef1
     * @param tCTBRefByID1
     * @param tCTBRefByIDs
     * @param doUnderOccupiedData
     * @param tCTBRefs
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count
     * }
     */
    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTransitionMatrixAndRecordTenancyChange(
            //HashMap<String, DW_SHBE_Collection> tSHBECollections,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            DW_SHBE_Collection tSHBECollection0,
            DW_SHBE_Collection tSHBECollection1,
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupiedIDs0,
            HashSet<DW_intID> tUnderOccupiedIDsRSL0,
            HashSet<DW_intID> tUnderOccupiedIDsCouncil0,
            HashSet<DW_intID> tUnderOccupiedIDs1,
            HashSet<DW_intID> tUnderOccupiedIDsCouncil1,
            HashSet<DW_intID> tUnderOccupiedIDsRSL1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            HashMap<Integer, String> indexYM3s,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            DW_UnderOccupiedReport_Set underOccupiedSetCouncil0,
            DW_UnderOccupiedReport_Set underOccupiedSetRSL0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            HashMap<String, DW_intID> tCTBRefByID0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            DW_UnderOccupiedReport_Set underOccupiedSetCouncil1,
            DW_UnderOccupiedReport_Set underOccupiedSetRSL1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<String, DW_intID> tCTBRefByID1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> tCTBRefs
    ) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();

        //originals = underOccupiedInApril2013; // This is only for not starting UO run horrible hack!
        //checkPreviousTenure = true; // YAHH!!!
        String nearestYM300;
        nearestYM300 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
        String nearestYM30;

        String nearestYM310;
        nearestYM310 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
        String nearestYM31;

        Iterator<String> iteS;
        if (tCTBRefs == null) {
            iteS = tCTBRefByID1.keySet().iterator();
        } else {
            iteS = tCTBRefs.iterator();
        }
        while (iteS.hasNext()) {
            nearestYM30 = nearestYM300;
            nearestYM31 = nearestYM310;
            String CTBRef = iteS.next();
            DW_intID tID0;
            tID0 = tCTBRefByID0.get(CTBRef);
            DW_intID tID1;
            tID1 = tCTBRefByID1.get(CTBRef);

            String tt0;
            if (tID0 == null) {
                tt0 = sMinus999;
            } else {
                tt0 = Integer.toString(tIDByTenancyType0.get(tID0));
            }

            String tt1;
            if (tID1 == null) {
                tt1 = sMinus999;
            } else {
                tt1 = Integer.toString(tIDByTenancyType1.get(tID1));
            }

            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
            }

            String pc0 = sAAN_NAA;
            String pc1 = sAAN_NAA;

            if (checkPreviousTenure) {
                if (tt0.equalsIgnoreCase(sMinus999)) {
                    if (underOccupied0 != null) {
                        tt0 += sU;
                        if (underOccupiedSetCouncil0.getMap().containsKey(CTBRef)) {
                            tt0 += "1";
                        }
                        if (underOccupiedSetRSL0.getMap().containsKey(CTBRef)) {
                            tt0 += "4";
                        }
                        pc0 = sAAN_NAA;
                    } else {
                        Object[] previousTenure;
                        previousTenure = getPreviousTenureAndPostcode(
                                //tSHBECollections,
                                tIDByPostcodes,
                                indexYM3s,
                                tID0, CTBRef, tCTBRefByIDs, tIDByTenancyTypes,
                                doUnderOccupiedData,
                                tUnderOccupancies,
                                tUnderOccupanciesCouncil,
                                tUnderOccupanciesRSL,
                                index,
                                include);
//                        previousTenure = getPreviousTenure(
//                                tID0, CTBRef, tCTBRefByIDs, tIDByTenancyTypes,
//                                tUnderOccupancies, tUnderOccupanciesCouncil,
//                                tUnderOccupanciesRSL, index, include);
                        tt0 = (String) previousTenure[0];
                        pc0 = (String) previousTenure[2];
                        yM30 = (String) previousTenure[3];
                        nearestYM30 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                    }
                } else {
                    //underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    if (underOccupied0 != null) {
                        tt0 += sU;
                    }
                    try {
                        pc0 = tSHBECollection0.getRecords().get(CTBRef).getDRecord().getClaimantsPostcode();
                    } catch (NullPointerException e) {
                    }
                }
                if (tt1.equalsIgnoreCase(sMinus999)) {
                    if (underOccupied1 != null) {
                        tt1 += sU;
                        if (underOccupiedSetCouncil1.getMap().containsKey(CTBRef)) {
                            tt1 += "1";
                        }
                        if (underOccupiedSetRSL1.getMap().containsKey(CTBRef)) {
                            tt1 += "4";
                        }
                        try {
                            pc1 = tSHBECollection1.getRecords().get(CTBRef).getDRecord().getClaimantsPostcode();
                        } catch (NullPointerException e) {
                        }
                    } else {
                        Object[] previousTenure;
                        previousTenure = getPreviousTenureAndPostcode(
                                //tSHBECollections,
                                tIDByPostcodes,
                                indexYM3s,
                                tID1, CTBRef, tCTBRefByIDs, tIDByTenancyTypes,
                                doUnderOccupiedData,
                                tUnderOccupancies, tUnderOccupanciesCouncil,
                                tUnderOccupanciesRSL, index, include);
                        tt1 = (String) previousTenure[0];
                        pc1 = (String) previousTenure[2];
                        yM31 = (String) previousTenure[3];
                        nearestYM31 = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                    }
                } else {
                    //underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    if (underOccupied1 != null) {
                        tt1 += sU;
                    }
                    try {
                        pc1 = tSHBECollection1.getRecords().get(CTBRef).getDRecord().getClaimantsPostcode();
                    } catch (NullPointerException e) {
                    }
                }
            }

//            if (checkPreviousTenure) {
//                if (tt1.equalsIgnoreCase(sMinus999)) {
//                    Object[] previousTenure;
//                    previousTenure = getPreviousTenure(
//                            tID1, CTBRef, tCTBRefByIDs, tIDByTenancyTypes,
//                            tUnderOccupancies, tUnderOccupanciesCouncil,
//                            tUnderOccupanciesRSL, index, include);
//                    tt1 = (String) previousTenure[0];
//
//                    if (!tt1.equalsIgnoreCase(sMinus999)) {
//                        int a = 1;
//                    }
//
//                }
//            }
            if (!checkPreviousTenure) {
                if (underOccupied0 != null) {
                    tt0 += sU;
                    if (tt0.startsWith(sMinus999)) {
                        if (underOccupiedSetCouncil0.getMap().containsKey(CTBRef)) {
                            tt0 += "1";
                        }
                        if (underOccupiedSetRSL0.getMap().containsKey(CTBRef)) {
                            tt0 += "4";
                        }
                    }
                }
                if (underOccupied1 != null) {
                    tt1 += sU;
                    if (tt1.startsWith(sMinus999)) {
                        if (underOccupiedSetCouncil1.getMap().containsKey(CTBRef)) {
                            tt1 += "1";
                        }
                        if (underOccupiedSetRSL1.getMap().containsKey(CTBRef)) {
                            tt1 += "4";
                        }
                    }
                }
            }
            if (!tt0.equalsIgnoreCase(tt1)) {
                String[] tenancyTypeChangeDetails;
                tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
                        tt0,
                        tt1,
                        nearestYM30,
                        nearestYM31,
                        pc0,
                        pc1);
                recordTenancyTypeChanges(
                        //tID1,
                        CTBRef,
                        tenancyTypeChanges,
                        yM31,
                        tenancyTypeChangeDetails[0]);
            } else if (pc0 != null && pc1 != null) {
                if (!pc0.equalsIgnoreCase(pc1)) {
//            if (DW_Postcode_Handler.isValidPostcode(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30), pc0)
//                    && DW_Postcode_Handler.isValidPostcode(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31), pc1)) {
                    if (tDW_Postcode_Handler.isValidPostcode(nearestYM30, pc0)
                            && tDW_Postcode_Handler.isValidPostcode(nearestYM31, pc1)) {
                        String ttc = tt0 + " - " + tt1;

                        ttc += "P";
//                result[0] += "VP";
//            } else {
//                result[0] += "P";
                        recordTenancyTypeChanges(
                                //tID1,
                                CTBRef,
                                tenancyTypeChanges,
                                yM31,
                                ttc);
                    }
                }
            }
            if (result.containsKey(tt1)) {
                TreeMap<String, Integer> tenancyTypeCount;
                tenancyTypeCount = result.get(tt1);
                Generic_Collections.addToTreeMapStringInteger(
                        tenancyTypeCount,
                        tt0,
                        1);
            } else {
                TreeMap<String, Integer> tenancyTypeCount;
                tenancyTypeCount = new TreeMap<String, Integer>();
                tenancyTypeCount.put(tt0, 1);
                result.put(tt1, tenancyTypeCount);
            }

        }

        // Go through current
        Iterator<DW_intID> ite;
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            boolean doMainLoop = true;
            DW_intID tID1;
            tID1 = ite.next();
            String CTBRef = tIDByCTBRef1.get(tID1);

            if (tCTBRefs.contains(CTBRef)) {

                if (CTBRef == null) {
                    int debug = 1;
                    System.out.println("CTBRef == null");
                }
//            //100247495
//            //100006974
//            //100638689
//            //100639639
//            //100580103
//            //100649242
//            if (CTBRef.equalsIgnoreCase("102305581")) {
//                int debug = 1;
//            }
////            if (CTBRef.equalsIgnoreCase("100080067")) {
////                int debug = 1;
////            }
                // UnderOccupancy
                DW_UOReport_Record underOccupied0 = null;
                DW_UOReport_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        if (tCTBRefs != null) {
                            if (tCTBRefs.contains(CTBRef)) {
                                underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                            }
                        } else {
                            underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                        }
                    }
                    if (underOccupiedSet1 != null) {
                        if (tCTBRefs != null) {
                            if (tCTBRefs.contains(CTBRef)) {
                                underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                            }
                        } else {
                            underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                        }
                    }
                    doMainLoop = underOccupied0 != null || underOccupied1 != null;
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer;
                    DW_intID tID0;
                    tID0 = tCTBRefByID0.get(CTBRef);
                    tenancyType0Integer = tIDByTenancyType0.get(tID0);
                    String tenancyType0;
                    if (tenancyType0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTenure(
                                    tID0,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    tUnderOccupancies,
                                    index,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                        } else {
                            tenancyType0 = sMinus999;
                        }
                    } else if (tenancyType0Integer == -999) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTenure(
                                    tID0,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    tUnderOccupancies,
                                    index,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                        } else {
                            tenancyType0 = sMinus999;
                        }
                    } else {
                        tenancyType0 = Integer.toString(tenancyType0Integer);
                    }
                    Integer tenancyType1Integer;
                    tenancyType1Integer = tIDByTenancyType1.get(tID1);
                    String tenancyType1 = Integer.toString(tenancyType1Integer);
                    if (!checkPreviousTenure) {
                        if (underOccupied0 != null) {
                            tenancyType0 += sU;
                        }
                    }
                    if (underOccupied1 != null) {
                        tenancyType1 += sU;
                    }
                    if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                        String[] tenancyTypeChangeDetails;
//                    tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
//                            doUnderOccupiedData,
//                            underOccupied0,
//                            underOccupied1,
//                            tenancyType0Integer,
//                            tenancyType1Integer);
                        tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
                                tenancyType0,
                                tenancyType1);
//                    tenancyType0 = tenancyTypeChangeDetails[1];
//                    tenancyType1 = tenancyTypeChangeDetails[2];

                        if (!tenancyType0.equalsIgnoreCase(tenancyTypeChangeDetails[1])) {
                            int debug = 1;
                        }

                        if (!tenancyType1.equalsIgnoreCase(tenancyTypeChangeDetails[2])) {
                            int debug = 1;
                        }

                        boolean doRecord = true;
                        if (!tenancyType0.endsWith(sU)) {
//                            System.out.println(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            System.out.println(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = underOccupiedInApril2013.contains(CTBRef);
//                            System.out.println(test);
                            if (!tenancyTypeChanges.containsKey(CTBRef)) {
                                //int debug = 1;
                                // This only happens in cases where there is a -999U initial case!
                                if (tCTBRefs.contains(CTBRef)) {
                                    tenancyType0 += sU;
                                    tenancyTypeChangeDetails[0] = tenancyType0 + " - " + tenancyType1;
                                    if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                        doRecord = false;
                                    }
                                } else {
                                    int debug = 1;
                                }
                            } else if (tenancyType0.equalsIgnoreCase(sMinus999)) {
                                if (tCTBRefs.contains(CTBRef)) {
                                    //System.out.println(CTBRef);
                                } else {
                                    int debug = 1;
                                    System.out.println(CTBRef);
                                }
                            }
                        }
                        if (doRecord) {
                            recordTenancyTypeChanges(
                                    //tID1,
                                    CTBRef,
                                    tenancyTypeChanges,
                                    yM31,
                                    tenancyTypeChangeDetails[0]);
                        }
                    }
                    if (result.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = result.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount,
                                tenancyType0,
                                1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        result.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        // Go through previous for those records not in current
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID0;
            tID0 = ite.next();
            String CTBRef = tIDByCTBRef0.get(tID0);

            if (tCTBRefs.contains(CTBRef)) {

                if (CTBRef.equalsIgnoreCase("102305581")) {
                    int debug = 1;
                }

                if (!tCTBRefByID1.containsKey(CTBRef)) {
//                DW_ID tID1;
//                tID1 = tCTBRefByID1.get(CTBRef);
//                if (!tIDByTenancyType1.containsKey(tID1)) {
                    boolean doMainLoop = true;
                    // UnderOccupancy
                    DW_UOReport_Record underOccupied0 = null;
                    DW_UOReport_Record underOccupied1 = null;
                    if (doUnderOccupiedData) {
                        if (underOccupiedSet0 != null) {
                            if (tCTBRefs != null) {
                                if (tCTBRefs.contains(CTBRef)) {
                                    underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                                }
                            } else {
                                underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                            }
                        }
                        if (underOccupiedSet1 != null) {
                            if (tCTBRefs != null) {
                                if (tCTBRefs.contains(CTBRef)) {
                                    underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                                }
                            } else {
                                underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                            }
                        }
                        //doMainLoop = underOccupied0 != null || underOccupied1 != null;
                    }
                    if (doMainLoop) {
                        Integer tenancyType0Integer = tIDByTenancyType0.get(tID0);
                        String tenancyType0 = Integer.toString(tenancyType0Integer);
                        Integer tenancyType1Integer = -999;
                        String tenancyType1;
                        tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                        if (underOccupied0 != null) {
                            tenancyType0 += sU;
                        }
                        if (underOccupied1 != null) {
                            tenancyType1 += sU;
                        }
//                    if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                        //if (tenancyType0Integer.compareTo(tenancyType1Integer) != 0) {
//                        String[] tenancyTypeChangeDetails;
//                        tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
//                                doUnderOccupiedData,
//                                underOccupied0,
//                                underOccupied1,
//                                tenancyType0Integer,
//                                tenancyType1Integer);
//                        tenancyType0 = tenancyTypeChangeDetails[1];
//                        tenancyType1 = tenancyTypeChangeDetails[2];
//
//                        if (!tenancyType0.endsWith(sU)) {
//                            int debug = 1;
//                        }
//
//                        recordTenancyTypeChanges(
//                                //tID0,
//                                CTBRef,
//                                tenancyTypeChanges,
//                                yM31,
//                                tenancyTypeChangeDetails[0]);
                        if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                            String[] tenancyTypeChangeDetails;
//                      tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
//                              doUnderOccupiedData,
//                              underOccupied0,
//                              underOccupied1,
//                              tenancyType0Integer,
//                              tenancyType1Integer);
                            tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
                                    tenancyType0,
                                    tenancyType1);

//                        tenancyType0 = tenancyTypeChangeDetails[1];
//                        tenancyType1 = tenancyTypeChangeDetails[2];
                            if (!tenancyType0.equalsIgnoreCase(tenancyTypeChangeDetails[1])) {
                                int debug = 1;
                            }

                            if (!tenancyType1.equalsIgnoreCase(tenancyTypeChangeDetails[2])) {
                                int debug = 1;
                            }
                            boolean doRecord = true;
                            if (!tenancyType0.endsWith(sU)) {
//                            System.out.println(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            System.out.println(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = tCTBRefs.contains(CTBRef);
//                            System.out.println(test);
                                if (!tenancyTypeChanges.containsKey(CTBRef)) {
                                    //int debug = 1;
                                    // This only happens in cases where there is a -999U initial case!
                                    if (tCTBRefs.contains(CTBRef)) {
                                        tenancyType0 += sU;
                                        tenancyTypeChangeDetails[0] = tenancyType0 + " - " + tenancyType1;
                                        if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                            doRecord = false;
                                        }
                                    } else {
                                        int debug = 1;
                                    }
                                } else if (tenancyType0.equalsIgnoreCase(sMinus999)) {
                                    if (tCTBRefs.contains(CTBRef)) {
                                        //System.out.println(CTBRef);
                                    } else {
                                        int debug = 1;
                                        System.out.println(CTBRef);
                                    }
                                }
                            }
                            if (doRecord) {
                                recordTenancyTypeChanges(
                                        //tID1,
                                        CTBRef,
                                        tenancyTypeChanges,
                                        yM31,
                                        tenancyTypeChangeDetails[0]);
                            }
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount,
                                    tenancyType0,
                                    1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     *
     * @param dirOut
     * @param tIDByTenancyType0
     * @param tIDByTenancyType1
     * @param tIDByPostcode0
     * @param tIDByPostcode1
     * @param postCodeHandler
     * @param postcodeChange
     * @param tenancyTypeChanges Passed in to be modified.
     * @param tUnderOccupanciesRSL
     * @param yM30
     * @param yM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param tIDByTenancyTypes
     * @param checkPreviousPostcode
     * @param tIDByPostcodes
     * @param underOccupiedSet0
     * @param tIDByCTBRef0
     * @param underOccupiedSet1
     * @param tIDByCTBRef1
     * @param doUnderOccupiedData
     * @param underOccupiedInApril2013
     * @param tCTBRefByIDs
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTransitionMatrixAndWritePostcodeChangeDetails(
            File dirOut,
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupancy0,
            HashSet<DW_intID> tUnderOccupancy1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesCouncil,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupanciesRSL,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            Generic_UKPostcode_Handler postCodeHandler,
            HashMap<DW_intID, String> tIDByPostcode0,
            HashMap<DW_intID, String> tIDByPostcode1,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> underOccupiedInApril2013) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();

        HashMap<Integer, String> indexYM3s;
        indexYM3s = tDW_SHBE_Handler.getIndexYM3s();

        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        String yM30v;
        yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM30);
        String yM31v;
        yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM31);
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_intID> ite;
        // Go through current Tenancy Type
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef;
            CTBRef = tIDByCTBRef1.get(tID);
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer tenancyType0Integer = DW_SHBE_TenancyType_Handler.iMinus999;
                String tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                String postcode0 = null;
                boolean isValidPostcodeFormPostcode0 = false;
                boolean isValidPostcode0 = false;
                if (tIDByTenancyType0 != null) {
                    tenancyType0Integer = tIDByTenancyType0.get(tID);
                    postcode0 = tIDByPostcode0.get(tID);
                    isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                    if (tenancyType0Integer == null) {
                        if (checkPreviousTenure) {
//                            Object[] previousTenure;
//                            previousTenure = getPreviousTenure(
//                                    tID,
//                                    CTBRef,
//                                    tCTBRefByIDs,
//                                    tIDByTenancyTypes,
//                                    tUnderOccupancies,
//                                    i,
//                                    include);
                            Object[] previousTenure;
                            previousTenure = getPreviousTenureAndPostcode(
                                    tIDByPostcodes,
                                    indexYM3s,
                                    tID,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    doUnderOccupiedData,
                                    tUnderOccupancies,
                                    tUnderOccupanciesCouncil,
                                    tUnderOccupanciesRSL,
                                    i,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                            if (indexOfLastKnownTenureOrNot != null) {
//                                  System.out.println("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                    postcode0 = tIDByPostcodes.get(indexOfLastKnownTenureOrNot).get(tID);
                                    isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                                }
                            }
                        } else {
                            tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                            if (underOccupied0 != null) {
                                tenancyType0 += sU;
                            }
                        }
                    }
                } else {
                    tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (underOccupied0 != null) {
                        tenancyType0 += sU;
                    }
                }
                Integer tenancyType1Integer = tIDByTenancyType1.get(tID);
                String tenancyType1 = Integer.toString(tenancyType1Integer);
                String postcode1;
                postcode1 = tIDByPostcode1.get(tID);
                boolean isValidPostcode1;
                isValidPostcode1 = tDW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
                if (isValidPostcode0 && isValidPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {
                        if (tenancyType0Integer.compareTo(tenancyType1Integer) != 0) {
                            String[] tenancyTypeChangeDetails;
                            //                    tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
//                            doUnderOccupiedData,
//                            underOccupied0,
//                            underOccupied1,
//                            tenancyType0Integer,
//                            tenancyType1Integer);
                            tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
                                    tenancyType0,
                                    tenancyType1);
                            tenancyType0 = tenancyTypeChangeDetails[1];
                            tenancyType1 = tenancyTypeChangeDetails[2];
                            recordTenancyTypeChanges(
                                    //tID,
                                    CTBRef,
                                    tenancyTypeChanges,
                                    yM31,
                                    tenancyTypeChangeDetails[0]);
                            if (postcodeChange) {
                                String[] postcodeChangeResult;
                                postcodeChangeResult = getPostcodeChangeResult(
                                        tID,
                                        yM30,
                                        yM31,
                                        tenancyTypeChangeDetails[0],
                                        postcode0,
                                        postcode1);
                                postcodeChanges.add(postcodeChangeResult);
                            }
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                } else if (isValidPostcode1) {
                    if (result.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = result.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        result.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        // Go through previous for those records not in current
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef = tIDByCTBRef0.get(tID);
            if (!tIDByCTBRef1.containsValue(CTBRef)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UOReport_Record underOccupied0 = null;
                DW_UOReport_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                    doMainLoop = underOccupied0 != null;
                    if (underOccupiedSet1 != null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer = tIDByTenancyType0.get(tID);
                    String tenancyType0 = Integer.toString(tenancyType0Integer);
                    Integer tenancyType1Integer = -999;
                    String tenancyType1;
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String postcode0;
                    postcode0 = tIDByPostcode0.get(tID);
                    boolean isValidPostcode0;
                    isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    if (isValidPostcode0) {
                        //if (!tenancyType0.equalsIgnoreCase(tenancyType1)) { // Always the case
                        String[] tenancyTypeChangeDetails;
                        //                    tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
//                            doUnderOccupiedData,
//                            underOccupied0,
//                            underOccupied1,
//                            tenancyType0Integer,
//                            tenancyType1Integer);
                        tenancyTypeChangeDetails = getTenancyTypeChangeDetails(
                                tenancyType0,
                                tenancyType1);
                        tenancyType0 = tenancyTypeChangeDetails[1];
                        tenancyType1 = tenancyTypeChangeDetails[2];
                        recordTenancyTypeChanges(
                                //tID,
                                CTBRef,
                                tenancyTypeChanges,
                                yM31,
                                tenancyTypeChangeDetails[0]);
                        //}
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        yM30,
                        yM31,
                        checkPreviousPostcode,
                        tDW_Strings.sGroupedNo);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    private String[] getTenancyTypeChangeDetails(
            boolean doUnderOccupied,
            DW_UOReport_Record underOccupied0,
            DW_UOReport_Record underOccupied1,
            Integer tenancyType0Integer,
            Integer tenancyType1Integer) {
        String[] result;
        result = new String[3];
        if (doUnderOccupied) {
            String[] ttc = getTenancyTypeTransitionName(
                    tenancyType0Integer,
                    underOccupied0 != null,
                    tenancyType1Integer,
                    underOccupied1 != null);
            result[0] = ttc[0];
            result[1] = ttc[1];
            result[2] = ttc[2];
        } else {
            result[0] = getTenancyTypeTransitionName(
                    tenancyType0Integer,
                    tenancyType1Integer);
            result[1] = Integer.toString(tenancyType0Integer);
            result[2] = Integer.toString(tenancyType1Integer);
        }
        return result;
    }

    private String[] getTenancyTypeChangeDetails(
            String tenancyType0,
            String tenancyType1) {
        String[] result;
        result = new String[3];
        result[0] = tenancyType0 + " - " + tenancyType1;
        result[1] = tenancyType0;
        result[2] = tenancyType1;
        return result;
    }

    private String[] getTenancyTypeChangeDetails(
            String tenancyType0,
            String tenancyType1,
            String nearestYM30,
            String nearestYM31,
            String pc0,
            String pc1) {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        String[] result;
        result = new String[3];
        result[0] = tenancyType0 + " - " + tenancyType1;
        if (pc0 != null && pc1 != null) {
            if (!pc0.equalsIgnoreCase(pc1)) {
//            if (DW_Postcode_Handler.isValidPostcode(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM30), pc0)
//                    && DW_Postcode_Handler.isValidPostcode(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM31), pc1)) {
                if (tDW_Postcode_Handler.isValidPostcode(nearestYM30, pc0)
                        && tDW_Postcode_Handler.isValidPostcode(nearestYM31, pc1)) {
                    result[0] += "P";
//                result[0] += "VP";
//            } else {
//                result[0] += "P";
                }
            }
        }
        result[1] = tenancyType0;
        result[2] = tenancyType1;
        return result;
    }

    public String[] getPostcodeChangeResult(
            DW_intID ID,
            String yM30,
            String yM31,
            String tenancyTypeChange,
            String postcode0,
            String postcode1) {
        String[] result;
        result = new String[6];
        result[0] = ID.toString();
        result[1] = yM30;
        result[2] = yM31;
        result[3] = tenancyTypeChange;
        result[4] = postcode0;
        result[5] = postcode1;
        return result;
    }

    /**
     * Writes out postcode changes for those moves in the same Tenancy Type.
     *
     * @param dirOut
     * @param tIDByTenancyType0
     * @param tIDByTenancyType1
     * @param tIDByTenancyTypes
     * @param tenancyTypeChanges
     * @param yM30
     * @param tUnderOccupancies
     * @param yM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param postCodeHandler
     * @param tIDByPostcode0
     * @param tIDByPostcode1
     * @param tIDByPostcodes
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedSet0
     * @param tIDByCTBRef0
     * @param underOccupiedSet1
     * @param tIDByCTBRef1
     * @param doUnderOccupiedData
     * @param tCTBRefs
     * @param tCTBRefByIDs
     * @return A count of changes in matrix form (only entries for the same
     * Tenancy Types and for -999 are in the matrix).
     */
    public TreeMap<String, TreeMap<String, Integer>> getPostcodeTransitionCountsNoTenancyTypeChange(
            File dirOut,
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupancyIDs0,
            HashSet<DW_intID> tUnderOccupancyIDs1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            Generic_UKPostcode_Handler postCodeHandler,
            HashMap<DW_intID, String> tIDByPostcode0,
            HashMap<DW_intID, String> tIDByPostcode1,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> tCTBRefs
    ) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        String yM30v;
        yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM30);
        String yM31v;
        yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM31);
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_intID> ite;
        // Go through current claimants
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef;

            if (tIDByCTBRef1 == null) {
                int debug = 1;
            }

            CTBRef = tIDByCTBRef1.get(tID);
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (tCTBRefs == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    } else if (tCTBRefs.contains(CTBRef)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (tCTBRefs == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    } else if (tCTBRefs.contains(CTBRef)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer tenancyType0Integer = -999;
                String tenancyType0;
                String postcode0 = null;
                boolean isValidPostcode0 = false;
                if (tIDByTenancyType0 != null) {
                    tenancyType0Integer = tIDByTenancyType0.get(tID);
                    if (tenancyType0Integer != null) {
                        tenancyType0 = Integer.toString(tenancyType0Integer);
                        boolean isValidPostcodeFormPostcode0 = false;
                        postcode0 = tIDByPostcode0.get(tID);
                        if (postcode0 != null) {
                            isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                            isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                        }
                        if (tenancyType0 == null) {
                            if (checkPreviousTenure) {
                                Object[] previousTenure;
                                previousTenure = getPreviousTenure(
                                        tID,
                                        CTBRef,
                                        tCTBRefByIDs,
                                        tIDByTenancyTypes,
                                        tUnderOccupancies,
                                        i,
                                        include);
                                tenancyType0 = (String) previousTenure[0];
                                if (checkPreviousPostcode) {
                                    Integer indexOfLastKnownTenureOrNot;
                                    indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                    if (indexOfLastKnownTenureOrNot != null) {
//                                       System.out.println("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                        if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                            postcode0 = tIDByPostcodes.get(indexOfLastKnownTenureOrNot).get(tID);
                                            if (postcode0 != null) {
                                                isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                                                isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                                            }
                                        }
                                    }
                                }
                            } else {
                                tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                                if (doUnderOccupiedData) {
                                    tenancyType0 += sU;
                                }
                            }
                        }
                    } else {
                        tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                        if (doUnderOccupiedData) {
                            tenancyType0 += sU;
                        }
                    }
                } else {
                    tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (doUnderOccupiedData) {
                        tenancyType0 += sU;
                    }
                }
                Integer tenancyType1Integer = tIDByTenancyType1.get(tID);
                String tenancyType1 = Integer.toString(tenancyType1Integer);
                String postcode1;
                postcode1 = tIDByPostcode1.get(tID);
                boolean isValidPostcode1 = false;
                if (postcode1 != null) {
                    isValidPostcode1 = tDW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
                }
                if (isValidPostcode0 && isValidPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {
                        String tenancyTypeChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = getTenancyTypeTransitionName(
                                    tenancyType0Integer,
                                    underOccupied0 != null,
                                    tenancyType1Integer,
                                    underOccupied1 != null);
                            tenancyTypeChange = ttc[0];
                            tenancyType0 = ttc[1];

//                            if (!tenancyType0.endsWith(sU)) {
//                                int debug = 1;
//                            }

                            tenancyType1 = ttc[2];
                            if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                            if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                                recordTenancyTypeChanges(
//                                        tID,
//                                        tenancyTypeChanges,
//                                        yM31,
//                                        tenancyTypeChange);
//                            }
                                if (postcodeChange) {
                                    String[] postcodeChangeResult;
                                    postcodeChangeResult = getPostcodeChangeResult(
                                            tID,
                                            yM30,
                                            yM31,
                                            tenancyTypeChange,
                                            postcode0,
                                            postcode1);
                                    postcodeChanges.add(postcodeChangeResult);
                                }
                                if (result.containsKey(tenancyType1)) {
                                    TreeMap<String, Integer> tenancyTypeCount;
                                    tenancyTypeCount = result.get(tenancyType1);
                                    Generic_Collections.addToTreeMapStringInteger(
                                            tenancyTypeCount, tenancyType0, 1);
                                } else {
                                    TreeMap<String, Integer> tenancyTypeCount;
                                    tenancyTypeCount = new TreeMap<String, Integer>();
                                    tenancyTypeCount.put(tenancyType0, 1);
                                    result.put(tenancyType1, tenancyTypeCount);
                                }
                            }
                        } else if (tenancyType0Integer.compareTo(tenancyType1Integer) == 0
                                || tenancyType0.equalsIgnoreCase(DW_SHBE_TenancyType_Handler.sMinus999)) { // Major diff
                            tenancyTypeChange = getTenancyTypeTransitionName(
                                    tenancyType0Integer,
                                    tenancyType1Integer);
                            if (postcodeChange) {
                                String[] postcodeChangeResult;
                                postcodeChangeResult = getPostcodeChangeResult(
                                        tID,
                                        yM30,
                                        yM31,
                                        tenancyTypeChange,
                                        postcode0,
                                        postcode1);
                                postcodeChanges.add(postcodeChangeResult);
                            }
                            if (result.containsKey(tenancyType1)) {
                                TreeMap<String, Integer> tenancyTypeCount;
                                tenancyTypeCount = result.get(tenancyType1);
                                Generic_Collections.addToTreeMapStringInteger(
                                        tenancyTypeCount, tenancyType0, 1);
                            } else {
                                TreeMap<String, Integer> tenancyTypeCount;
                                tenancyTypeCount = new TreeMap<String, Integer>();
                                tenancyTypeCount.put(tenancyType0, 1);
                                result.put(tenancyType1, tenancyTypeCount);
                            }
                        }
                    }
                } else if (isValidPostcode1) {
                    if (result.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = result.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        result.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        // Go through all those previously and record for all those that are not
        // in the current data. Also record for all those that were under 
        // occupying, but are now not and have changed postcode.
        Set<DW_intID> set;
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();

            if (tIDByCTBRef0 == null) {
                int debug = 1;
            }

            String CTBRef = tIDByCTBRef0.get(tID);
            boolean doMainLoop = true;
            set = tIDByTenancyType1.keySet();
            if (!set.contains(tID)) {
// This was double counting!
//                    if (!set.contains(tID)) {
//                        // UnderOccupancy
//                        DW_UOReport_Record underOccupied0 = null;
//                        DW_UOReport_Record underOccupied1 = null;
//                        if (doUnderOccupiedData) {
//                            if (underOccupiedSet0 != null) {
//                                if (tIDByCTBRef0 != null) {
//                                    if (CTBRef != null) {
//                                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
//                                    }
//                                }
//                            }
//                            doMainLoop = underOccupied0 != null;
//                        }
//                        if (doMainLoop) {
//                            String tenancyType0 = tIDByTenancyType0.get(
//                                    tID);
//                            String tenancyType1;
//                            tenancyType1 = tIDByTenancyType1.get(tID);
//                            if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                                String postcode0;
//                                postcode0 = tIDByPostcode0.get(tID);
//                                boolean isValidPostcode0 = false;
//                                if (postcode0 != null) {
//                                    isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(postcode0);
//                                }
//                                String postcode1;
//                                postcode1 = tIDByPostcode1.get(tID);
//                                boolean isValidPostcode1 = false;
//                                if (postcode1 != null) {
//                                    isValidPostcode1 = DW_Postcode_Handler.isValidPostcode(postcode1);
//                                }
//                                if (isValidPostcode0 && isValidPostcode1) {
//                                    boolean doCount = false;
//                                    if (postcodeChange) {
//                                        doCount = !postcode0.equalsIgnoreCase(postcode1);
//                                    } else {
//                                        doCount = postcode0.equalsIgnoreCase(postcode1);
//                                    }
//                                    if (doCount) {
//                                        String tenancyTypeChange;
//                                        if (doUnderOccupiedData) {
//                                            String[] ttc = getTenancyTypeTransitionName(
//                                                    tenancyType0,
//                                                    underOccupied0 != null,
//                                                    tenancyType1,
//                                                    underOccupied1 != null);
//                                            tenancyTypeChange = ttc[0];
//                                            tenancyType0 = ttc[1];
//                                            tenancyType1 = ttc[2];
//                                            if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                                                recordTenancyTypeChanges(
//                                                        tID,
//                                                        tenancyTypeChanges,
//                                                        year,
//                                                        month,
//                                                        tenancyTypeChange);
//                                            }
//                                        } else {
//                                            tenancyTypeChange = getTenancyTypeTransitionName(
//                                                    tenancyType0,
//                                                    tenancyType1);
//                                        }
//                                        if (postcodeChange) {
//                                            String[] postcodeChangeResult;
//                                postcodeChangeResult = getPostcodeChangeResult(
//                                        tID,
//                                        yM30, 
//                                        yM31, 
//                                        tenancyTypeChange,
//                                        postcode0,
//                                        postcode1);
//                                postcodeChanges.add(postcodeChangeResult);
//                                        }
//                                        if (result.containsKey(tenancyType1)) {
//                                            TreeMap<String, Integer> tenancyTypeCount;
//                                            tenancyTypeCount = result.get(tenancyType1);
//                                            Generic_Collections.addToTreeMapStringInteger(
//                                                    tenancyTypeCount, tenancyType0, 1);
//                                        } else {
//                                            TreeMap<String, Integer> tenancyTypeCount;
//                                            tenancyTypeCount = new TreeMap<String, Integer>();
//                                            tenancyTypeCount.put(tenancyType0, 1);
//                                            result.put(tenancyType1, tenancyTypeCount);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
                // UnderOccupancy
                DW_UOReport_Record underOccupied0 = null;
                //DW_UnderOccupiedReport_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                    doMainLoop = underOccupied0 != null;
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer = tIDByTenancyType0.get(tID);
                    String tenancyType0 = Integer.toString(tenancyType0Integer);
                    Integer tenancyType1Integer = -999;
                    String tenancyType1;
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String postcode0;
                    postcode0 = tIDByPostcode0.get(tID);
                    boolean isValidPostcode0 = false;
                    if (postcode0 != null) {
                        isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    }
                    if (isValidPostcode0) {
                        String tenancyTypeChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = getTenancyTypeTransitionName(
                                    tenancyType0Integer,
                                    underOccupied0 != null,
                                    tenancyType1Integer,
                                    false);
                            tenancyTypeChange = ttc[0];
                            tenancyType0 = ttc[1];
                            tenancyType1 = ttc[2];
                            if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
//                                if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                recordTenancyTypeChanges(
                                        //tID,
                                        CTBRef,
                                        tenancyTypeChanges,
                                        yM31,
                                        tenancyTypeChange);
//                                } else {
//                                    tenancyTypeChange = getTenancyTypeTransitionName(
//                                            tenancyType0,
//                                            tenancyType1);
                            }
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        yM30,
                        yM31,
                        checkPreviousPostcode,
                        tDW_Strings.sGroupedNo);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public TreeMap<String, TreeMap<String, Integer>> getPostcodeTransitionCountsNoTenancyTypeChangeGrouped(
            File dirOut,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupancy0,
            HashSet<DW_intID> tUnderOccupancy1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            Generic_UKPostcode_Handler postCodeHandler,
            HashMap<DW_intID, String> tIDByPostcode0,
            HashMap<DW_intID, String> tIDByPostcode1,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> underOccupiedInApril2013) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        String yM30v;
        yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM30);
        String yM31v;
        yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM31);
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_intID> ite;
        // Go through current claimants
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef = tIDByCTBRef1.get(tID);
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer tenancyType0Integer;
                String tenancyType0;
                String postcode0 = null;
                boolean isValidPostcode0 = false;
                if (tIDByTenancyType0 != null) {
                    tenancyType0Integer = tIDByTenancyType0.get(tID);
                    boolean isValidPostcodeFormPostcode0 = false;
                    postcode0 = tIDByPostcode0.get(tID);
                    if (postcode0 != null) {
                        isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                        isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                    }
                    if (tenancyType0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTenure(
                                    tID,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    tUnderOccupancies,
                                    i,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                            if (checkPreviousPostcode) {
                                Integer indexOfLastKnownTenureOrNot;
                                indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                if (indexOfLastKnownTenureOrNot != null) {
//                                       System.out.println("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                    if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                        postcode0 = tIDByPostcodes.get(indexOfLastKnownTenureOrNot).get(tID);
                                        if (postcode0 != null) {
                                            isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                                            isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                                        }
                                    }
                                }
                            }
                        } else {
                            tenancyType0 = sMinus999;
                        }
                    } else {
                        tenancyType0 = Integer.toString(tenancyType0Integer);
                    }
                } else {
                    tenancyType0 = sMinus999;
                }
                tenancyType0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, tenancyType0);
                Integer tenancyType1Integer = tIDByTenancyType1.get(tID);
                String tenancyType1 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, tenancyType1Integer);
                String postcode1;
                postcode1 = tIDByPostcode1.get(tID);
                boolean isValidPostcode1 = false;
                if (postcode1 != null) {
                    isValidPostcode1 = tDW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
                }
                if (isValidPostcode0 && isValidPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {
                        if (tenancyType0.equalsIgnoreCase(tenancyType1)
                                || tenancyType0.equalsIgnoreCase(DW_SHBE_TenancyType_Handler.sMinus999)) { // Major diff
                            String tenancyTypeChange;
                            if (doUnderOccupiedData) {
                                String[] ttc = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        underOccupied0 != null,
                                        tenancyType1,
                                        underOccupied1 != null);
                                tenancyTypeChange = ttc[0];
                                tenancyType0 = ttc[1];
                                tenancyType1 = ttc[2];
                                //if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                    recordTenancyTypeChanges(
                                            //tID,
                                            CTBRef,
                                            tenancyTypeChanges,
                                            yM31,
                                            tenancyTypeChange);
                                }
                            } else {
                                tenancyTypeChange = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        tenancyType1);
                            }
                            if (postcodeChange) {
                                String[] postcodeChangeResult;
                                postcodeChangeResult = getPostcodeChangeResult(
                                        tID,
                                        yM30,
                                        yM31,
                                        tenancyTypeChange,
                                        postcode0,
                                        postcode1);
                                postcodeChanges.add(postcodeChangeResult);
                            }
                            if (result.containsKey(tenancyType1)) {
                                TreeMap<String, Integer> tenancyTypeCount;
                                tenancyTypeCount = result.get(tenancyType1);
                                Generic_Collections.addToTreeMapStringInteger(
                                        tenancyTypeCount, tenancyType0, 1);
                            } else {
                                TreeMap<String, Integer> tenancyTypeCount;
                                tenancyTypeCount = new TreeMap<String, Integer>();
                                tenancyTypeCount.put(tenancyType0, 1);
                                result.put(tenancyType1, tenancyTypeCount);
                            }
                        }
                    }
                } else if (isValidPostcode1) {
                    if (result.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = result.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        result.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        // Go through all those previously and record for all those that are not
        // in the current data. Also record for all those that were under 
        // occupying but are now not (and vice vesa) and have changed postcode.
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef = tIDByCTBRef0.get(tID);
            if (!tIDByCTBRef1.containsValue(CTBRef)) {
                boolean doMainLoop = true;
                DW_UOReport_Record underOccupied0 = null;
                DW_UOReport_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                    doMainLoop = underOccupied0 != null;
                    if (underOccupiedSet1 != null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer = tIDByTenancyType0.get(tID);
                    String tenancyType0;
                    tenancyType0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, tenancyType0Integer);
                    String tenancyType1;
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String postcode0;
                    postcode0 = tIDByPostcode0.get(tID);
                    boolean isValidPostcode0 = false;
                    if (postcode0 != null) {
                        isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    }
                    if (isValidPostcode0) {
                        String tenancyTypeChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = getTenancyTypeTransitionName(
                                    tenancyType0,
                                    underOccupied0 != null,
                                    tenancyType1,
                                    underOccupied1 != null);
                            tenancyTypeChange = ttc[0];
                            tenancyType0 = ttc[1];
                            tenancyType1 = ttc[2];
                            //if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                            if (tenancyType0.equalsIgnoreCase(tenancyType1)) {
                                recordTenancyTypeChanges(
                                        //tID,
                                        CTBRef,
                                        tenancyTypeChanges,
                                        yM31,
                                        tenancyTypeChange);
                            }
//                                } else {
//                                    tenancyTypeChange = getTenancyTypeTransitionName(
//                                            tenancyType0,
//                                            tenancyType1);
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        yM30,
                        yM31,
                        checkPreviousPostcode,
                        tDW_Strings.sGrouped);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     *
     * @param dirOut
     * @param tIDByTenancyType0
     * @param tIDByTenancyType1
     * @param tIDByTenancyTypes
     * @param tUnderOccupancy0
     * @param tUnderOccupancy1
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param tUnderOccupancies
     * @param tenancyTypeChanges
     * @param yM30
     * @param yM31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param postCodeHandler
     * @param tIDByPostcode0
     * @param tIDByPostcode1
     * @param tIDByPostcodes
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedSet0
     * @param tIDByCTBRef0
     * @param underOccupiedSet1
     * @param tIDByCTBRef1
     * @param doUnderOccupiedData
     * @param tCTBRefByIDs
     * @param underOccupiedInApril2013
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTransitionMatrixGroupedAndWritePostcodeChangeDetails(
            File dirOut,
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupancy0,
            HashSet<DW_intID> tUnderOccupancy1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            Generic_UKPostcode_Handler postCodeHandler,
            HashMap<DW_intID, String> tIDByPostcode0,
            HashMap<DW_intID, String> tIDByPostcode1,
            HashMap<Integer, HashMap<DW_intID, String>> tIDByPostcodes,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> underOccupiedInApril2013) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        String yM30v;
        yM30v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM30);
        String yM31v;
        yM31v = tDW_Postcode_Handler.getNearestYM3ForONSPDLookup(
                yM31);
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_intID> ite;
        // Go through for current
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef = tIDByCTBRef1.get(tID);
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    } else if (underOccupiedInApril2013.contains(CTBRef)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer tenancyType0Integer;
                String tenancyType0;
                String postcode0 = null;
                boolean isValidPostcode0 = false;
                if (tIDByTenancyType0 != null) {
                    tenancyType0Integer = tIDByTenancyType0.get(tID);
                    postcode0 = tIDByPostcode0.get(tID);
                    isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    boolean isValidPostcodeFormPostcode0 = false;
                    isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                    if (tenancyType0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTenure(
                                    tID,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    tUnderOccupancies,
                                    index,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                            if (indexOfLastKnownTenureOrNot != null) {
                                if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                    postcode0 = tIDByPostcodes.get(indexOfLastKnownTenureOrNot).get(tID);
                                    isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                                    isValidPostcodeFormPostcode0 = Generic_UKPostcode_Handler.isValidPostcodeForm(postcode0);
                                }
                            }
                        } else {
                            tenancyType0 = sMinus999;
                        }
                    } else {
                        tenancyType0 = Integer.toString(tenancyType0Integer);
                    }
                } else {
                    tenancyType0 = sMinus999;
                }
                tenancyType0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, tenancyType0);
                Integer tenancyType1Integer;
                tenancyType1Integer = tIDByTenancyType1.get(tID);
                String tenancyType1;
                tenancyType1 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenancyType1Integer);
                String postcode1;
                postcode1 = tIDByPostcode1.get(tID);
                boolean isValidPostcode1;
                isValidPostcode1 = tDW_Postcode_Handler.isValidPostcode(yM31v, postcode1);
                if (isValidPostcode0 && isValidPostcode1) {
                    boolean doCount;
                    if (postcodeChange) {
                        doCount = !postcode0.equalsIgnoreCase(postcode1);
                    } else {
                        doCount = postcode0.equalsIgnoreCase(postcode1);
                    }
                    if (doCount) {
                        if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                            String tenancyTypeChange;
                            if (doUnderOccupiedData) {
                                String[] ttc = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        underOccupied0 != null,
                                        tenancyType1,
                                        underOccupied1 != null);
                                tenancyTypeChange = ttc[0];
                                tenancyType0 = ttc[1];
                                tenancyType1 = ttc[2];
                            } else {
                                tenancyTypeChange = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        tenancyType1);
                            }
                            recordTenancyTypeChanges(
                                    //tID,
                                    CTBRef,
                                    tenancyTypeChanges,
                                    yM31,
                                    tenancyTypeChange);
                            if (postcodeChange) {
                                String[] postcodeChangeResult;
                                postcodeChangeResult = getPostcodeChangeResult(
                                        tID,
                                        yM30,
                                        yM31,
                                        tenancyTypeChange,
                                        postcode0,
                                        postcode1);
                                postcodeChanges.add(postcodeChangeResult);
                            }
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        // Go through for previous not in current
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            String CTBRef = tIDByCTBRef0.get(tID);
            if (!tIDByCTBRef1.containsValue(CTBRef)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UOReport_Record underOccupied0 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        if (CTBRef != null) {
                            underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                        }
                    }
                    doMainLoop = underOccupied0 != null;
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer = tIDByTenancyType0.get(tID);
                    String tenancyType0;
                    tenancyType0 = getTenancyTypeGroup(
                            regulatedGroups,
                            unregulatedGroups,
                            tenancyType0Integer);
                    String tenancyType1;
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String postcode0;
                    postcode0 = tIDByPostcode0.get(tID);
                    boolean isValidPostcode0;
                    isValidPostcode0 = tDW_Postcode_Handler.isValidPostcode(yM30v, postcode0);
                    if (isValidPostcode0) {
                        if (!tenancyType0.equalsIgnoreCase(tenancyType1)) { // Always the case
                            String tenancyTypeChange;
                            if (doUnderOccupiedData) {
                                String[] ttc = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        underOccupied0 != null,
                                        tenancyType1,
                                        false);
                                tenancyTypeChange = ttc[0];
                                tenancyType0 = ttc[1];
                                tenancyType1 = ttc[2];
                            } else {
                                tenancyTypeChange = getTenancyTypeTransitionName(
                                        tenancyType0,
                                        tenancyType1);
                            }
                            recordTenancyTypeChanges(
                                    //tID,
                                    CTBRef,
                                    tenancyTypeChanges,
                                    yM31,
                                    tenancyTypeChange);
                        }
                        if (result.containsKey(tenancyType1)) {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = result.get(tenancyType1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    tenancyTypeCount, tenancyType0, 1);
                        } else {
                            TreeMap<String, Integer> tenancyTypeCount;
                            tenancyTypeCount = new TreeMap<String, Integer>();
                            tenancyTypeCount.put(tenancyType0, 1);
                            result.put(tenancyType1, tenancyTypeCount);
                        }
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        yM30,
                        yM31,
                        checkPreviousPostcode,
                        tDW_Strings.sGrouped);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * 
     * @param dirOut
     * @param postcodeChanges (DW_ID, StartTime, EndTime, TenancyTypeChange, StartPostcode, End Postcode)
     * @param yM30
     * @param yM31
     * @param checkPreviousPostcode
     * @param type 
     */
    private void writePostcodeChanges(
            File dirOut,
            ArrayList<String[]> postcodeChanges,
            String yM30,
            String yM31,
            boolean checkPreviousPostcode,
            String type) {
        File dirOut2 = new File(
                dirOut,
                type);
        dirOut2 = new File(
                dirOut2,
                tDW_Strings.sPostcodeChanges);
        if (checkPreviousPostcode) {
            dirOut2 = new File(
                    dirOut2,
                    tDW_Strings.sCheckedPreviousPostcode);
        } else {
            dirOut2 = new File(
                    dirOut2,
                    tDW_Strings.sCheckedPreviousPostcodeNo);
        }
        dirOut2.mkdirs();
        File f;
        f = new File(
                dirOut2,
                tDW_Strings.sPostcodeChanges
                + "_Start_" + yM30
                + "_End_" + yM31 + ".csv");

        System.out.println("Write " + f);
        PrintWriter pw;
        pw = Generic_StaticIO.getPrintWriter(f, false);
        pw.println("DW_ID, StartTime, EndTime, TenancyTypeChange, StartPostcode, End Postcode");
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
            Integer tenancyType) {
        String result;
        if (tenancyType == -999) {
            result = DW_SHBE_TenancyType_Handler.sMinus999;
        } else {
            result = tDW_Strings.sGroupedNo;
            if (regulatedGroups.contains(tenancyType)) {
                result = "Regulated";
            }
            if (unregulatedGroups.contains(tenancyType)) {
                result = "Unregulated";
            }
        }
        return result;
    }

    public String getTenancyTypeGroup(
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            String tenancyType) {
        String result;
        Integer tt;
        if (tenancyType.endsWith(sU)) {
            tt = Integer.valueOf(tenancyType.substring(0, tenancyType.length() - 1));
        } else {
            tt = Integer.valueOf(tenancyType);
        }
        result = getTenancyTypeGroup(
                regulatedGroups,
                unregulatedGroups,
                tt);
        if (tenancyType.endsWith(sU)) {
            result += sU;
        }
        return result;
    }

    public Object[] getMultipleTenancyTypeTranistionMatrixGrouped(
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<String, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            //ArrayList<Integer> include,
            String yM30) {
        Object[] result;
        result = new Object[2];
        TreeMap<String, TreeMap<String, Integer>> tenancyTypeTranistionMatrix;
        tenancyTypeTranistionMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = tenancyTypeTranistionMatrix;
        HashMap<DW_intID, String> tIDTenureChange;
        tIDTenureChange = new HashMap<DW_intID, String>();
        result[1] = tIDTenureChange;
        Iterator<DW_intID> ite;
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    tID,
                    tIDByTenancyTypes,
                    yM30);
            Integer tenancyType1Integer;
            tenancyType1Integer = tIDByTenancyType1.get(
                    tID);
            String tenancyType1;
            tenancyType1 = getTenancyTypeGroup(
                    regulatedGroups,
                    unregulatedGroups,
                    tenancyType1Integer);
            Integer tenancyType0Integer;
            tenancyType0Integer = tIDByTenancyType0.get(
                    tID);
            String tenancyType0;
            if (tenancyType0Integer == null) {
                tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
            } else {
                tenancyType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenancyType0Integer);
            }
            if (hasPreviousTenureChange) {
                if (tenancyTypeTranistionMatrix.containsKey(tenancyType1)) {
                    TreeMap<String, Integer> tenancyTypeCount;
                    tenancyTypeCount = tenancyTypeTranistionMatrix.get(tenancyType1);
                    Generic_Collections.addToTreeMapStringInteger(
                            tenancyTypeCount, tenancyType0, 1);
                } else {
                    TreeMap<String, Integer> tenancyTypeCount;
                    tenancyTypeCount = new TreeMap<String, Integer>();
                    tenancyTypeCount.put(tenancyType0, 1);
                    tenancyTypeTranistionMatrix.put(tenancyType1, tenancyTypeCount);
                }
            }
            if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                tIDTenureChange.put(
                        tID,
                        "" + tenancyType0 + " - " + tenancyType1);
            }
        }
        Set<DW_intID> set;
        set = tIDByTenancyType1.keySet();
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID;
            tID = ite.next();
            boolean hasPreviousTenureChange;
            hasPreviousTenureChange = getHasPreviousTenureChange(
                    tID,
                    tIDByTenancyTypes,
                    yM30);
            Integer tenancyType0Integer;
            tenancyType0Integer = tIDByTenancyType0.get(
                    tID);
            String tenancyType0;
            if (tenancyType0Integer == null) {
                tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
            } else {
                tenancyType0 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenancyType0Integer);
            }
            String tenancyType1;
            tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
            if (hasPreviousTenureChange) {
                if (!set.contains(tID)) {
                    if (tenancyTypeTranistionMatrix.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = tenancyTypeTranistionMatrix.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        tenancyTypeTranistionMatrix.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        return result;
    }

    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTranistionMatrixGroupedAndRecordTenancyChange(
            HashMap<DW_intID, Integer> tIDByTenancyType0,
            HashMap<DW_intID, Integer> tIDByTenancyType1,
            HashMap<Integer, HashMap<DW_intID, Integer>> tIDByTenancyTypes,
            HashSet<DW_intID> tUnderOccupancy0,
            HashSet<DW_intID> tUnderOccupancy1,
            HashMap<Integer, HashSet<DW_intID>> tUnderOccupancies,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            //HashMap<DW_ID, ArrayList<String>> tenancyTypeChanges,
            HashMap<String, ArrayList<String>> tenancyTypeChanges,
            String yM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            DW_UnderOccupiedReport_Set underOccupiedSet0,
            HashMap<DW_intID, String> tIDByCTBRef0,
            HashMap<String, DW_intID> tCTBRefByID0,
            DW_UnderOccupiedReport_Set underOccupiedSet1,
            HashMap<DW_intID, String> tIDByCTBRef1,
            HashMap<String, DW_intID> tCTBRefByID1,
            HashMap<Integer, HashMap<String, DW_intID>> tCTBRefByIDs,
            boolean doUnderOccupiedData,
            HashSet<String> underOccupiedInApril2013) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        // Go through current
        Iterator<DW_intID> ite;
        ite = tIDByTenancyType1.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID1;
            tID1 = ite.next();
            String CTBRef = tIDByCTBRef1.get(tID1);
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UOReport_Record underOccupied0 = null;
            DW_UOReport_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (underOccupiedInApril2013 != null) {
                        if (underOccupiedInApril2013.contains(CTBRef)) {
                            underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                        }
                    } else {
                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (underOccupiedInApril2013 != null) {
                        if (underOccupiedInApril2013.contains(CTBRef)) {
                            underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                        }
                    } else {
                        underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer tenancyTypeInt1;
                tenancyTypeInt1 = tIDByTenancyType1.get(
                        tID1);
                String tenancyType1;
                tenancyType1 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        tenancyTypeInt1);
                Integer tenancyType0Integer;
                String tenancyType0;
                if (tIDByTenancyType0 != null) {
                    DW_intID tID0;
                    tID0 = tCTBRefByID0.get(CTBRef);
                    tenancyType0Integer = tIDByTenancyType0.get(
                            tID0);
                    if (tenancyType0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTenure(
                                    tID0,
                                    CTBRef,
                                    tCTBRefByIDs,
                                    tIDByTenancyTypes,
                                    tUnderOccupancies,
                                    index,
                                    include);
                            tenancyType0 = (String) previousTenure[0];
                            tenancyType0 = getTenancyTypeGroup(
                                    regulatedGroups,
                                    unregulatedGroups,
                                    tenancyType0);
                        } else {
                            tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                        }
                    } else {
                        if (tenancyType0Integer == -999) {
                            if (checkPreviousTenure) {
                                Object[] previousTenure;
                                previousTenure = getPreviousTenure(
                                        tID0,
                                        CTBRef,
                                        tCTBRefByIDs,
                                        tIDByTenancyTypes,
                                        tUnderOccupancies,
                                        index,
                                        include);
                                tenancyType0 = (String) previousTenure[0];
                            } else {
                                tenancyType0 = sMinus999;
                            }
                        } else {
                            tenancyType0 = Integer.toString(tenancyType0Integer);
                        }
                        tenancyType0 = getTenancyTypeGroup(
                                regulatedGroups,
                                unregulatedGroups,
                                tenancyType0);
                    }
                } else {
                    tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
                }
                if (!tenancyType0.equalsIgnoreCase(tenancyType1)) {
                    String tenancyTypeChange;
                    if (doUnderOccupiedData) {
                        String[] ttc = getTenancyTypeTransitionName(
                                tenancyType0,
                                underOccupied0 != null,
                                tenancyType1,
                                underOccupied1 != null);
                        tenancyTypeChange = ttc[0];
                        tenancyType0 = ttc[1];
                        tenancyType1 = ttc[2];
                    } else {
                        tenancyTypeChange = getTenancyTypeTransitionName(
                                tenancyType0,
                                tenancyType1);
                    }
                    recordTenancyTypeChanges(
                            //tID1,
                            CTBRef,
                            tenancyTypeChanges,
                            yM31,
                            tenancyTypeChange);
                }
                if (result.containsKey(tenancyType1)) {
                    TreeMap<String, Integer> tenancyTypeCount;
                    tenancyTypeCount = result.get(tenancyType1);
                    Generic_Collections.addToTreeMapStringInteger(
                            tenancyTypeCount, tenancyType0, 1);
                } else {
                    TreeMap<String, Integer> tenancyTypeCount;
                    tenancyTypeCount = new TreeMap<String, Integer>();
                    tenancyTypeCount.put(tenancyType0, 1);
                    result.put(tenancyType1, tenancyTypeCount);
                }
            }
        }
        // Go through previous for those not in current
        ite = tIDByTenancyType0.keySet().iterator();
        while (ite.hasNext()) {
            DW_intID tID0;
            tID0 = ite.next();
            String CTBRef = tIDByCTBRef0.get(tID0);
//            DW_ID tID1;
//            tID1 = tCTBRefByID1.get(CTBRef);
//            if (!tIDByTenancyType1.containsKey(tID1)) {
            if (!tCTBRefByID1.containsKey(CTBRef)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UOReport_Record underOccupied0 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        if (underOccupiedInApril2013 != null) {
                            if (underOccupiedInApril2013.contains(CTBRef)) {
                                underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                            }
                        } else {
                            underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                        }
                    }
                    doMainLoop = underOccupied0 != null;
                }
                if (doMainLoop) {
                    Integer tenancyType0Integer;
                    tenancyType0Integer = tIDByTenancyType0.get(
                            tID0);
                    String tenancyType0;
                    tenancyType0 = getTenancyTypeGroup(
                            regulatedGroups,
                            unregulatedGroups,
                            tenancyType0Integer);
                    String tenancyType1;
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (!tenancyType0.equalsIgnoreCase(tenancyType1)) { // Always the case!
                        String tenancyTypeChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = getTenancyTypeTransitionName(
                                    tenancyType0,
                                    underOccupied0 != null,
                                    tenancyType1,
                                    false);
                            tenancyTypeChange = ttc[0];
                            tenancyType0 = ttc[1];
                            tenancyType1 = ttc[2];
                        } else {
                            tenancyTypeChange = getTenancyTypeTransitionName(
                                    tenancyType0,
                                    tenancyType1);
                        }
                        recordTenancyTypeChanges(
                                //tID0,
                                CTBRef,
                                tenancyTypeChanges,
                                yM31,
                                tenancyTypeChange);
                    }
                    if (result.containsKey(tenancyType1)) {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = result.get(tenancyType1);
                        Generic_Collections.addToTreeMapStringInteger(
                                tenancyTypeCount, tenancyType0, 1);
                    } else {
                        TreeMap<String, Integer> tenancyTypeCount;
                        tenancyTypeCount = new TreeMap<String, Integer>();
                        tenancyTypeCount.put(tenancyType0, 1);
                        result.put(tenancyType1, tenancyTypeCount);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     *
     * @param tenancyTypeMatrix {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count}
     * @param yM30
     * @param yM31
     * @param dirOut
     * @param tenancyTypes
     * @param grouped
     * @param doUnderOccupiedData
     */
    public void writeTenancyTypeTransitionMatrix(
            TreeMap<String, TreeMap<String, Integer>> tenancyTypeMatrix,
            String yM30,
            String yM31,
            File dirOut,
            ArrayList<String> tenancyTypes,
            boolean grouped,
            boolean doUnderOccupiedData) {
        writeTransitionMatrix(
                tenancyTypeMatrix,
                yM30,
                yM31,
                dirOut,
                grouped,
                tenancyTypes,
                //doUnderOccupiedData,
                "TenancyTypeTransition");
    }

    /**
     *
     * @param tenancyTypeMatrix {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count}
     * @param yM30
     * @param yM31
     * @param dirOut
     * @param grouped
     * @param tenancyTypes
     * @param name
     */
    public void writeTransitionMatrix(
            TreeMap<String, TreeMap<String, Integer>> tenancyTypeMatrix,
            String yM30,
            String yM31,
            File dirOut,
            boolean grouped,
            ArrayList<String> tenancyTypes,
            //            boolean doUnderOccupiedData,
            String name) {
        if (tenancyTypeMatrix == null) {
            return;
        }
        if (tenancyTypeMatrix.isEmpty()) {
            //if (tenancyTypeMatrix.size() == 0) {
            return;
        }
        File dirOut2;
        if (grouped) {
            dirOut2 = new File(
                    dirOut,
                    tDW_Strings.sGrouped);
        } else {
            dirOut2 = new File(
                    dirOut,
                    tDW_Strings.sGroupedNo);
//        tenancyTypes = DW_SHBE_Handler.getTenancyTypeAll(doUnderOccupiedData);
        }
        dirOut2.mkdir();
        File f;
        f = new File(
                dirOut2,
                name
                + "_Start_" + yM30
                + "_End_" + yM31 + ".csv");
        PrintWriter pw;
        try {
            pw = new PrintWriter(f);
            String line;
            line = "TenancyTypeNow\\TenancyTypeBefore";
            Iterator<String> ite;
            ite = tenancyTypes.iterator();
            while (ite.hasNext()) {
                line += "," + ite.next();
            }
            line += ",-999";
            pw.println(line);
            ite = tenancyTypes.iterator();
            while (ite.hasNext()) {
                String tenancyType0;
                tenancyType0 = ite.next();
                line = tenancyType0;
                TreeMap<String, Integer> tenancyTypeCounts;
                tenancyTypeCounts = tenancyTypeMatrix.get(tenancyType0);
                if (tenancyTypeCounts == null) {
                    for (String tenancyType : tenancyTypes) {
                        line += ",0";
                    }
                    line += ",0";
                } else {
                    String tenancyType1;
                    Iterator<String> ite2;
                    ite2 = tenancyTypes.iterator();
                    while (ite2.hasNext()) {
                        tenancyType1 = ite2.next();
                        Integer count;
                        count = tenancyTypeCounts.get(tenancyType1);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    Integer nullCount = tenancyTypeCounts.get(tenancyType1);
                    if (nullCount == null) {
                        line += ",0";
                    } else {
                        line += "," + nullCount.toString();
                    }
                }
                pw.println(line);
            }
            TreeMap<String, Integer> tenancyTypeCounts;
            tenancyTypeCounts = tenancyTypeMatrix.get(DW_SHBE_TenancyType_Handler.sMinus999);
            line = DW_SHBE_TenancyType_Handler.sMinus999;
            if (tenancyTypeCounts == null) {
                for (String tenancyType : tenancyTypes) {
                    line += ",0";
                }
                line += ",0";
            } else {
                String tenancyType1;
                Iterator<String> ite2;
                ite2 = tenancyTypes.iterator();
                while (ite2.hasNext()) {
                    tenancyType1 = ite2.next();
                    Integer count;
                    count = tenancyTypeCounts.get(tenancyType1);
                    if (count == null) {
                        line += ",0";
                    } else {
                        line += "," + count.toString();
                    }
                }
                tenancyType1 = DW_SHBE_TenancyType_Handler.sMinus999;
                Integer nullCount = tenancyTypeCounts.get(tenancyType1);
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

//    /**
//     *
//     * @param tenancyTypeMatrix {@code
//     * TreeMap<String, TreeMap<String, Integer>>
//     * Tenure0, Tenure1, Count
//     * }
//     * @
//     * param tenancyTypesGrouped
//     * @param year0
//     * @param month0
//     * @param year1
//     * @param month1
//     * @param dirOut
//     */
//    public void writeTenancyTypeTransitionMatrixGrouped(
//            TreeMap<String, TreeMap<String, Integer>> tenancyTypeMatrix,
//            ArrayList<String> tenancyTypesGrouped,
//            String year0,
//            String month0,
//            String year1,
//            String month1,
//            File dirOut) {
//        if (tenancyTypeMatrix == null) {
//            return;
//        }
//        if (tenancyTypeMatrix.size() == 0) {
//            return;
//        }
//        File dirOut2 = new File(
//                dirOut,
//                DW_Files.sGrouped);
//        dirOut2.mkdir();
//        File f;
//        f = new File(
//                dirOut2,
//                "TenancyTypeTransition"
//                + "_Start_" + year0 + month0
//                + "_End_" + year1 + month1 + ".csv");
////        tenancyTypes = DW_SHBE_Handler.getTenancyTypeAll();
//        PrintWriter pw;
//        try {
//            pw = new PrintWriter(f);
//            String line;
//            line = "TenancyTypeNow\\TenancyTypeBefore";
//            Iterator<String> tenancyTypesGroupedIte;
//            tenancyTypesGroupedIte = tenancyTypesGrouped.iterator();
//            while (tenancyTypesGroupedIte.hasNext()) {
//                line += "," + tenancyTypesGroupedIte.next();
//            }
//            pw.println(line);
//            tenancyTypesGroupedIte = tenancyTypesGrouped.iterator();
//            while (tenancyTypesGroupedIte.hasNext()) {
//                String tenancyType0;
//                tenancyType0 = tenancyTypesGroupedIte.next();
//                line = tenancyType0;
//                TreeMap<String, Integer> tenancyTypeCounts;
//                tenancyTypeCounts = tenancyTypeMatrix.get(tenancyType0);
//                if (tenancyTypeCounts == null) {
//                    for (String tenancyTypesGrouped1 : tenancyTypesGrouped) {
//                        line += ",0";
//                    }
//                } else {
//                    Iterator<String> ite2;
//                    ite2 = tenancyTypesGrouped.iterator();
//                    while (ite2.hasNext()) {
//                        String tenancyType1;
//                        tenancyType1 = ite2.next();
//                        Integer count;
//                        count = tenancyTypeCounts.get(tenancyType1);
//                        if (count == null) {
//                            line += ",0";
//                        } else {
//                            line += "," + count.toString();
//                        }
//                    }
////                    String tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
////                    Integer nullCount = tenancyTypeCounts.get(tenancyType0);
////                    if (nullCount == null) {
////                        line += ",0";
////                    } else {
////                        line += "," + nullCount.toString();
////                    }
//                }
//                pw.println(line);
//            }
////            TreeMap<String, Integer> tenancyTypeCounts;
////            tenancyTypeCounts = tenancyTypeMatrix.get(DW_SHBE_TenancyType_Handler.sMinus999);
////            line = DW_SHBE_TenancyType_Handler.sMinus999;
////            if (tenancyTypeCounts == null) {
////                for (String tenancyTypeGroup : tenancyTypesGrouped) {
////                    line += ",0";
////                }
////            } else {
////                Iterator<String> ite2;
////                ite2 = tenancyTypesGrouped.iterator();
////                while (ite2.hasNext()) {
////                    String tenancyType0;
////                    tenancyType0 = ite2.next();
////                    Integer count;
////                    count = tenancyTypeCounts.get(tenancyType0);
////                    if (count == null) {
////                        line += ",0";
////                    } else {
////                        line += "," + count.toString();
////                    }
////                }
////                String tenancyType0 = DW_SHBE_TenancyType_Handler.sMinus999;
////                Integer nullCount = tenancyTypeCounts.get(tenancyType0);
////                if (nullCount == null) {
////                    line += ",0";
////                } else {
////                    line += nullCount.toString();
////                }
////            }
////            pw.println(line);
//            pw.close();
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(DW_DataProcessor_LCC.class
//                    .getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void getTotalClaimantsByTenancyType(String paymentType) {
        //TreeMap<String,TreeMap<Integer,Integer>> result = new TreeMap<String,TreeMap<Integer,Integer>>();
        String[] SHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
        for (String SHBEFilename : SHBEFilenames) {
            DW_SHBE_Collection SHBEData;
            SHBEData = new DW_SHBE_Collection(
                    env,
                    SHBEFilename,
                    paymentType);
            String year = tDW_SHBE_Handler.getYear(SHBEFilename);
            String month = tDW_SHBE_Handler.getMonth(SHBEFilename);
            TreeMap<Integer, Integer> ymAllResult = new TreeMap<Integer, Integer>();
            TreeMap<Integer, Integer> ymHBResult = new TreeMap<Integer, Integer>();
            TreeMap<String, DW_SHBE_Record> records;
            records = SHBEData.getRecords();
            Iterator<String> ite = records.keySet().iterator();
            while (ite.hasNext()) {
                String claimID = ite.next();
                DW_SHBE_D_Record DRecord = records.get(claimID).getDRecord();
                int aTenancyType = DRecord.getTenancyType();
                Integer aTenancyTypeInteger = aTenancyType;
                if (ymAllResult.containsKey(aTenancyTypeInteger)) {
                    int count = ymAllResult.get(aTenancyTypeInteger);
                    count++;
                    ymAllResult.put(aTenancyTypeInteger, count);
                } else {
                    ymAllResult.put(aTenancyTypeInteger, 1);
                }
//                String aHousingBenefitClaimReferenceNumber = DRecord.getHousingBenefitClaimReferenceNumber();
//                if (!aHousingBenefitClaimReferenceNumber.isEmpty()) {
                if (tDW_SHBE_Handler.isHBClaimInPayment(DRecord)) {
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
                    tDW_Files.getOutputSHBETablesDir(),
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
                    tDW_Files.getOutputSHBETablesDir(),
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
     * @param SHBEFilenames
     * @param paymentType
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
            String[] SHBEFilenames,
            String paymentType,
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
        TreeMap<Integer, TreeMap<Integer, Integer>> resultMatrix;
        resultMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
        result[0] = resultMatrix;
        TreeSet<Integer> originsAndDestinations;
        originsAndDestinations = new TreeSet<Integer>();
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
        // Start
        DW_SHBE_Collection SHBEDataStart;
        SHBEDataStart = new DW_SHBE_Collection(
                env,
                SHBEFilenames[startIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> recordsStart;
        recordsStart = SHBEDataStart.getRecords();
        // End
        DW_SHBE_Collection SHBEDataEnd;
        SHBEDataEnd = new DW_SHBE_Collection(
                env,
                SHBEFilenames[endIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> recordsEnd;
        recordsEnd = SHBEDataEnd.getRecords();
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = recordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record startDRecord;
            startDRecord = recordsStart.get(councilTaxClaimNumber).getDRecord();
            if (startDRecord != null) {
                String postcodeStart = startDRecord.getClaimantsPostcode();
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

                DW_SHBE_D_Record endDRecord;
                endDRecord = recordsEnd.get(councilTaxClaimNumber).getDRecord();
                //String destinationPostcodeDistrict;
                Integer endTenancyType;
                String destinationPostcode = null;
                if (endDRecord == null) {
                    endTenancyType = -999;
                } else {
                    destinationPostcode = endDRecord.getClaimantsPostcode();
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
        ite = recordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecordEnd;
            DRecordEnd = recordsEnd.get(councilTaxClaimNumber).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart;
                DRecordStart = recordsStart.get(councilTaxClaimNumber).getDRecord();
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
     * @param SHBEFilenames
     * @param paymentType
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
            String[] SHBEFilenames,
            String paymentType,
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
        DW_SHBE_Collection SHBEDataStart;
        SHBEDataStart = new DW_SHBE_Collection(
                env,
                SHBEFilenames[startIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> recordsStart;
        recordsStart = SHBEDataStart.getRecords();
        DW_SHBE_Collection SHBEDataEnd;
        SHBEDataEnd = new DW_SHBE_Collection(
                env,
                SHBEFilenames[endIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> recordsEnd;
        recordsEnd = SHBEDataEnd.getRecords();
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<String> ite;
        ite = recordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record startDRecord = recordsStart.get(councilTaxClaimNumber).getDRecord();
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                DW_SHBE_D_Record endDRecord = recordsEnd.get(councilTaxClaimNumber).getDRecord();
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
        ite = recordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record endDRecord = recordsEnd.get(councilTaxClaimNumber).getDRecord();
            if (endDRecord != null) {
                DW_SHBE_D_Record DRecordStart = recordsStart.get(councilTaxClaimNumber).getDRecord();
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
                tDW_Files.getOutputUnderOccupiedDir(),
                "DigitalWelfareOutputUnderOccupiedReport" + dates[0] + "To" + dates[dates.length - 1] + ".txt");
        TreeMap<String, BigDecimal> postcodeTotalArrearsTotal = new TreeMap<String, BigDecimal>();
        TreeMap<String, Integer> postcodeClaimsTotal = new TreeMap<String, Integer>();
        int UnderoccupancyIndex;
        int SHBEIndex;
        for (int i = 0; i < dates.length; i++) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    tDW_Files.getOutputUnderOccupiedDir(),
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
            TreeMap<String, DW_UOReport_Record> map = set.getMap();
            Iterator<String> ite2;
            ite2 = map.keySet().iterator();
            String councilTaxClaimNumber;
            while (ite2.hasNext()) {
                councilTaxClaimNumber = ite2.next();
                DW_UOReport_Record underOccupiedReport_DataRecord;
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
                } else if (postcodeSplit.length == 3) {
                    postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
                } else if (postcodeSplit.length == 2) {
                    postcode = postcodeSplit[0] + " " + postcodeSplit[1];
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
            } else if (postcodeSplit.length == 3) {
                postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
            } else if (postcodeSplit.length == 2) {
                postcode = postcodeSplit[0] + " " + postcodeSplit[1];
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
        tDW_SHBE_Handler = new DW_SHBE_Handler(env);
        collectionHandler = new DW_SHBE_CollectionHandler(env);
        tDW_UnderOccupiedReport_Handler = new DW_UnderOccupiedReport_Handler(
                env);
    }

    /**
     * Method for reporting how many bedroom tax people have moved from one
     * month to the next.
     *
     * @param paymentType
     * @param SHBE_Sets
     * @param underOccupiedReport_Sets
     */
    public void processSHBEReportDataForSarah(
            String paymentType,
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
                tDW_Files.getOutputDir(),
                "Sarah");
        File outputDir = new File(
                dir,
                "DigitalWelfareOutputReportForSarah");
        String level = "MSOA";
        TreeMap<String, String> lookupFromPostcodeToCensusCode;
        lookupFromPostcodeToCensusCode = getLookupFromPostcodeToLevelCode(
                env,
                level,
                2011);
        int UnderoccupancyIndex;
        int SHBEIndex;
        for (int i = 0; i < dates.length; i++) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    tDW_Files.getOutputUnderOccupiedDir(),
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
            String startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
            String startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
            String endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
            String endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
            Object[] migrationData = getAllClaimantMigrationData(
                    allSHBEFilenames,
                    paymentType,
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
     * @param paymentType
     * @param underOccupiedReport_Sets
     */
    public void processSHBEReportDataIntoMigrationMatricesForApril(
            ArrayList<Object[]> SHBEData,
            String paymentType,
            ArrayList<DW_UnderOccupiedReport_Set>[] underOccupiedReport_Sets) {
        ArrayList<DW_UnderOccupiedReport_Set> councilRecords;
        ArrayList<DW_UnderOccupiedReport_Set> registeredSocialLandlordRecords;
        councilRecords = underOccupiedReport_Sets[0];
        registeredSocialLandlordRecords = underOccupiedReport_Sets[1];
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                tDW_Files.getOutputSHBETablesDir(),
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
        startYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[startIndex]);
        endYear = tDW_SHBE_Handler.getYear(allSHBEFilenames[endIndex]);
        startMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[startIndex]);
        endMonth = tDW_SHBE_Handler.getMonth(allSHBEFilenames[endIndex]);
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
                paymentType,
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
     * @param paymentType
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
            String paymentType,
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
                paymentType,
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
                paymentType,
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
        String startMonth = tDW_SHBE_Handler.getMonth(startName);
        String startYear = tDW_SHBE_Handler.getYear(startName);
        String endName = allSHBEFilenames[endIndex];
        String endMonth = tDW_SHBE_Handler.getMonth(endName);
        String endYear = tDW_SHBE_Handler.getYear(endName);
        File dir;
        dir = new File(
                tDW_Files.getOutputSHBETablesDir(),
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
     * @param SHBEFilenames
     * @param paymentType
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
            String[] SHBEFilenames,
            String paymentType,
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
        DW_SHBE_Collection SHBEDataStart = new DW_SHBE_Collection(
                env,
                SHBEFilenames[startIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> DRecordsStart = SHBEDataStart.getRecords();
        DW_SHBE_Collection SHBEDataEnd = new DW_SHBE_Collection(
                env,
                SHBEFilenames[endIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> DRecordsEnd = SHBEDataEnd.getRecords();
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        int stayPutCount = 0;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber).getDRecord();
            if (DRecordStart != null) {
                String postcodeStart = DRecordStart.getClaimantsPostcode();
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

                DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber).getDRecord();
                String destinationPostcodeDistrict;
                String destinationPostcode = null;
                if (DRecordEnd == null) {
                    destinationPostcodeDistrict = "unknown";
                } else {
                    destinationPostcode = DRecordEnd.getClaimantsPostcode();
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
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber).getDRecord();
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
     * @param SHBEFilenames
     * @param paymentType
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
            String[] SHBEFilenames,
            String paymentType,
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
        DW_SHBE_Collection SHBEDataStart = new DW_SHBE_Collection(
                env,
                SHBEFilenames[startIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> DRecordsStart = SHBEDataStart.getRecords();
        DW_SHBE_Collection SHBEDataEnd = new DW_SHBE_Collection(
                env,
                SHBEFilenames[endIndex],
                paymentType);
        TreeMap<String, DW_SHBE_Record> DRecordsEnd = SHBEDataEnd.getRecords();
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<String> ite;
        ite = DRecordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber).getDRecord();
            if (DRecordStart != null) {
                // Filter for only Housing Benefit Claimants
                //if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                if (tDW_SHBE_Handler.isHBClaimInPayment(DRecordStart)) {
                    String postcodeStart = DRecordStart.getClaimantsPostcode();
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
                    DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber).getDRecord();
                    // Filter for only Housing Benefit Claimants
//                    if (!DRecordEnd.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                    if (tDW_SHBE_Handler.isHBClaimInPayment(DRecordEnd)) {
                        String destinationPostcodeDistrict;
                        String destinationPostcode = null;
                        if (DRecordEnd == null) {
                            destinationPostcodeDistrict = "unknown";
                        } else {
                            destinationPostcode = DRecordEnd.getClaimantsPostcode();
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
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(councilTaxClaimNumber).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(councilTaxClaimNumber).getDRecord();
                if (DRecordStart == null) {
                    String startPostcodeDistrict = "unknown";
                    String destinationPostcode = DRecordEnd.getClaimantsPostcode();
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
                tDW_Files.getOutputSHBETablesDir(),
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
        Set<String> potentiallyMoved = underOccupiedReportSet.getMap().keySet();
        potentiallyMoved.removeAll(underOccupiedReportSet2.getMap().keySet());
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
        Set<String> s = underOccupiedReportSet.getMap().keySet();
        ite = underOccupiedReportSet2.getMap().keySet().iterator();
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
        s.removeAll(underOccupiedReportSet.getMap().keySet());
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
        TreeMap<String, DW_UOReport_Record> recs;
        recs = underOccupiedReportSet.getMap();
//        Object[] SHBEDataMonth1 = getSHBEData(tSHBEfilenames[0]);
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
                tDW_Files.getOutputUnderOccupiedDir(),
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
            DW_UOReport_Record underOccupiedReport_Record;
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
     * @param SHBEFilenames
     * @param paymentType
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as Postcode sectors and
     * values are counts of the number of claims subject to the bedroom tax.
     */
    public HashMap<String, TreeMap<String, Integer>> getUnderOccupiedbyPostcode(
            TreeMap<String, DW_UOReport_Record>[] records,
            String[] SHBEFilenames,
            String paymentType) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        DW_SHBE_Collection SHBEDataMonth1 = new DW_SHBE_Collection(
                env,
                SHBEFilenames[0],
                paymentType);
        for (int month = 0; month < SHBEFilenames.length - 1; month++) {
            int underOccupancyMonth = month;
            String monthString = tDW_SHBE_Handler.getMonth(SHBEFilenames[month]);
            TreeMap<String, DW_SHBE_Record> DRecords = SHBEDataMonth1.getRecords();
            // Iterate over records and join these with SHBE records to get postcodes
            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
            result.put(monthString, postcodeClaims);
            Iterator<String> ite = records[underOccupancyMonth].keySet().iterator();
            String councilTaxClaimNumber;
            while (ite.hasNext()) {
                councilTaxClaimNumber = ite.next();
                DW_SHBE_D_Record DRecord = DRecords.get(councilTaxClaimNumber).getDRecord();
                if (DRecord != null) {
                    String postcode = DRecord.getClaimantsPostcode();
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
        Iterator<String> ite = underOccupiedReport_Set.getMap().keySet().iterator();
        int countOfRecordsNotAggregatedDueToUnrecognisedPostcode = 0;
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            councilTaxClaimNumber = ite.next();
            DW_SHBE_D_Record DRecord;
//            DRecord = DRecords.get(councilTaxClaimNumber);
            DRecord = DRecords.getRecord(councilTaxClaimNumber).getDRecord();
            if (DRecord != null) {
                String postcode = DRecord.getClaimantsPostcode();
                postcode = DW_Postcode_Handler.formatPostcodeForMapping(postcode);
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
                } else if (aggregatedClaims.containsKey(censusCode)) {
                    int current = aggregatedClaims.get(censusCode);
                    aggregatedClaims.put(censusCode, current + 1);
                } else {
                    aggregatedClaims.put(censusCode, 1);
                }
            }
        }
        System.out.println("underOccupancyDate " + date);
        System.out.println("countOfRecordsNotAggregatedDueToUnrecognisedPostcode " + countOfRecordsNotAggregatedDueToUnrecognisedPostcode);
        return result;
    }

//    /**
//     * @return TreeMap<String, String[]> result where:--------------------------
//     * Keys are postcodes and values are:---------------------------------------
//     * values[0] = rec.getOa01();-----------------------------------------------
//     * values[1] = rec.getMsoa01();---------------------------------------------
//     * values[2] = rec.getOa11();-----------------------------------------------
//     * values[3] = rec.getMsoa11();---------------------------------------------
//     */
//    private TreeMap<String, String[]> initLookupFromPostcodeToCensusCodes() {
//        File inputDirectory = new File("/scratch01/Work/Projects/NewEnclosures/ONSPD/Data/");
//        String inputFilename = inputFilename = "ONSPD_AUG_2013_UK_O.csv";
//        File inFile = new File(inputDirectory, inputFilename);
//        File outputDirectory = new File("/scratch02/DigitalWelfare/ONSPD/processed");
//        String outputFilename = "PostcodeLookUp_TreeMap_String_Strings.thisFile";
//        File outFile = new File(outputDirectory, outputFilename);
//        //new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitPointLookup();
//        //new DW_Postcode_Handler(inFile, outFile).run1();
//        TreeMap<String, String[]> result = new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitCensusCodesLookup();
//        return result;
//    }
    /**
     * Returns data about the number of claims per postcode.
     *
     * @param SHBEFilenames
     * @param paymentType
     * @return HashMap<String, TreeMap<String, Integer>> The keys are Months,
     * the values are TreeMap<String, Integer> with Keys as Postcode sectors and
     * values are counts of the number of claims.
     */
    protected HashMap<String, TreeMap<String, Integer>> getCouncilTaxClaimCountByPostcode(
            String[] SHBEFilenames,
            String paymentType) {
        HashMap<String, TreeMap<String, Integer>> result = new HashMap<String, TreeMap<String, Integer>>();
        for (int month = 0; month < SHBEFilenames.length - 1; month++) {
            TreeMap<String, Integer> monthsResult = new TreeMap<String, Integer>();
            String monthString = tDW_SHBE_Handler.getMonth(SHBEFilenames[month]);
            result.put(monthString, monthsResult);
            DW_SHBE_Collection SHBEData = new DW_SHBE_Collection(
                    env,
                    SHBEFilenames[month],
                    paymentType);
            TreeMap<String, DW_SHBE_Record> recs = SHBEData.getRecords();
            //TreeMap<String, DW_SHBE_Record> SRecordsWithoutDRecords = (TreeMap<String, DW_SHBE_Record>) SHBEData[1];
            Iterator<String> ite = recs.keySet().iterator();
            String postcode;
            while (ite.hasNext()) {
                String councilTaxClaimNumber = ite.next();
                DW_SHBE_D_Record rec = recs.get(councilTaxClaimNumber).getDRecord();
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

                } else if (monthsResult.containsKey(censusCode)) {
                    Integer count = monthsResult.get(censusCode);
                    Integer newcount = count + 1;
                    monthsResult.put(censusCode, newcount);
                } else {
                    monthsResult.put(censusCode, 1);
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
//        Object[] SHBEDataMonth1 = getSHBEData(tSHBEfilenames[0]);
//        for (int month = 0; month < tSHBEfilenames.length - 1; month++) {
//            String monthString = tSHBEfilenames[month + 1].split(" ")[1];
//            SHBEDataMonth2 = getSHBEData(tSHBEfilenames[month + 1]);
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

    /**
     * This method produces General Aggregate Statistics at LSOA and OA level
     * for each Payment Type for all the
     *
     * @param lookupsFromPostcodeToLevelCode
     * @param SHBEFilenames
     * @param paymentTypes
     * @param includes
     * @param levels
     * @param tDW_PersonIDtoDW_IDLookup
     */
    public void outputHBGeneralAggregateStatistics(
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames,
            ArrayList<String> paymentTypes,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            HashMap<DW_PersonID, DW_intID> tDW_PersonIDtoDW_IDLookup) {

//        Iterator<String> includesIte;
        String includeName;
        ArrayList<Integer> include;
        Iterator<Integer> includeIte;
        int i;
        DW_SHBE_Collection aSHBECollection;
        TreeMap<String, DW_SHBE_Record> records0;
        String claimID;
        DW_SHBE_D_Record DRecord0;
        String postcode0;
        int numberOfChildDependents;
        int householdSize;
        Iterator<String> levelsIte;
        Iterator<String> recordsIte;
        //String councilTaxBenefitClaimReferenceNumber0;
        String level;
        TreeMap<String, String> tLookupFromPostcodeToLevelCode;
        //String claimantType;
        String areaCode;

        File outDir;
        outDir = new File(
                tDW_Files.getOutputSHBETablesDir(),
                "HBGeneralAggregateStatistics");
        File outDir1;
        File outDir2;
        File outFile;
        String yM3;
        PrintWriter outPW;
        String paymentType;

        Iterator<String> paymentTypesIte;
        paymentTypesIte = paymentTypes.iterator();
        while (paymentTypesIte.hasNext()) {
            paymentType = paymentTypesIte.next();
            includeName = tDW_Strings.sIncludeAll;
            outDir1 = new File(
                    outDir,
                    paymentType);
            include = includes.get(includeName);
            includeIte = include.iterator();
            while (includeIte.hasNext()) {
                i = includeIte.next();
                yM3 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                System.out.println("Generalising " + yM3);
                // Load first data
                aSHBECollection = new DW_SHBE_Collection(
                        env,
                        SHBEFilenames[i],
                        paymentType);
                records0 = aSHBECollection.getRecords();
                TreeMap<String, TreeMap<String, int[]>> result;
                result = new TreeMap<String, TreeMap<String, int[]>>();
                TreeMap<String, int[]> result0;
                int[] resultValues;

                // Iterate over records
                recordsIte = records0.keySet().iterator();
                while (recordsIte.hasNext()) {
                    claimID = recordsIte.next();
                    DRecord0 = records0.get(claimID).getDRecord();
                    numberOfChildDependents = DRecord0.getNumberOfChildDependents();
                    householdSize = tDW_SHBE_Handler.getHouseholdSizeint(DRecord0);
                    postcode0 = DRecord0.getClaimantsPostcode();
                    //councilTaxBenefitClaimReferenceNumber0 = DRecord0.getCouncilTaxBenefitClaimReferenceNumber();
                    //claimantType = DW_SHBE_Handler.getClaimantType(DRecord0);
                    if (postcode0 != null) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            level = levelsIte.next();
                            if (result.containsKey(level)) {
                                result0 = result.get(level);
                            } else {
                                result0 = new TreeMap<String, int[]>();
                                result.put(level, result0);
                            }
                            tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                            areaCode = getAreaCode(
                                    level,
                                    postcode0,
                                    tLookupFromPostcodeToLevelCode);
                            if (result0.containsKey(areaCode)) {
                                resultValues = result0.get(areaCode);
                                resultValues[0] += 1;
                                resultValues[1] += numberOfChildDependents;
                                resultValues[2] += householdSize;
                            } else {
                                resultValues = new int[3];
                                resultValues[0] = 1;
                                resultValues[1] = numberOfChildDependents;
                                resultValues[2] = householdSize;
                                result0.put(areaCode, resultValues);
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
                            yM3 + ".csv");
                    outPW = Generic_StaticIO.getPrintWriter(outFile, false);
                    outPW.println("AreaCode, NumberOfHBClaims, NumberOfChildDependentsInHBClaimingHouseholds, TotalPopulationInHBClaimingHouseholds");
                    Iterator<String> ite2;
                    ite2 = result0.keySet().iterator();
                    while (ite2.hasNext()) {
                        areaCode = ite2.next();
                        if (!areaCode.isEmpty()) {
                            resultValues = result0.get(areaCode);
                            outPW.println(
                                    areaCode
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
}
