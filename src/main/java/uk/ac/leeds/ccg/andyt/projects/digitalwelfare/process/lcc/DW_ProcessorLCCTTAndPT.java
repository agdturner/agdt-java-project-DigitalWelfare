/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_ProcessorLCCTTAndPT extends DW_ProcessorLCC {

    /**
     * For convenience
     */
    protected DW_UO_Handler DW_UO_Handler;
    protected DW_SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;
    protected HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
    protected HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;

    /**
     * For convenience.
     */
    String sU = DW_Strings.sU;

    //String sUOInApril2013 = "UOInApril2013";
    String sUOInApril2013 = "UInApr13";
    //String sTenancyChange = "TenancyChange";
    String sTenancyChange = "TC";
    String sMinus999 = "-999";
    String sAAN_NAA = "AAN_NAA";

    //String sAllClaimants = "Al";
    String sAllClaimants = "All";
    String sNewEntrant = "NewEntrant";
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
    boolean DoPostcodeChanges = false;
    boolean DoAnyTenancyChanges = false;
    boolean DoTenancyChanges = false;
    boolean DoTenancyAndPostcodeChanges = false;

    public DW_ProcessorLCCTTAndPT(DW_Environment env) {
        super(env);
        DW_UO_Handler = env.getDW_UO_Handler();
        DW_SHBE_TenancyType_Handler = env.getDW_SHBE_TenancyType_Handler();
        PostcodeToPostcodeIDLookup = DW_SHBE_Data.getPostcodeToPostcodeIDLookup();
        PostcodeIDToPostcodeLookup = DW_SHBE_Data.getPostcodeIDToPostcodeLookup();
    }

    @Override
    public void run() {
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add(sAllClaimants); // Count of all claimants
        types.add(sNewEntrant); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
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

        HashMap<Boolean, ArrayList<String>> TTs;
        TTs = new HashMap<Boolean, ArrayList<String>>();

        boolean doUO;
        doUO = true;
        TTs.put(doUO, DW_SHBE_TenancyType_Handler.getTenancyTypeAll(doUO));
        doUO = false;
        TTs.put(doUO, DW_SHBE_TenancyType_Handler.getTenancyTypeAll(doUO));
        Object[] ttgs = DW_SHBE_TenancyType_Handler.getTenancyTypeGroups();
        HashMap<Boolean, TreeMap<String, ArrayList<String>>> TTGroups;
        TTGroups = (HashMap<Boolean, TreeMap<String, ArrayList<String>>>) ttgs[0];
        HashMap<Boolean, ArrayList<String>> TTsGrouped;
        TTsGrouped = (HashMap<Boolean, ArrayList<String>>) ttgs[1];
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

        ArrayList<Boolean> bArray;
        bArray = new ArrayList<Boolean>();
        bArray.add(true);
        bArray.add(false);
        Iterator<Boolean> iteB;

//        doGrouped = true;
        DoPostcodeChanges = true;
        DoAnyTenancyChanges = true;
        DoTenancyChanges = true;
        DoTenancyAndPostcodeChanges = true;
        
        // Runtime approximately 1 hour 5 minutes.
        includes = DW_SHBE_Handler.getIncludes();
        includes.remove(DW_Strings.sIncludeAll);
        includes.remove(DW_Strings.sIncludeYearly);
        includes.remove(DW_Strings.sInclude6Monthly);
        includes.remove(DW_Strings.sInclude3Monthly);
//            includes.remove(DW_Strings.sIncludeMonthlySinceApril2013);
        includes.remove(DW_Strings.sIncludeMonthly);

        boolean DoUnderOccupiedData;
        Iterator<Boolean> iteB3;
        iteB3 = bArray.iterator();
        while (iteB3.hasNext()) {
            DoUnderOccupiedData = iteB3.next();
            env.logO("<DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
            if (DoUnderOccupiedData) {
                TreeMap<String, DW_UO_Set> CouncilUOSets;
                CouncilUOSets = DW_UO_Data.getCouncilUOSets();
                TreeMap<String, DW_UO_Set> RSLUOSets;
                RSLUOSets = DW_UO_Data.getRSLUOSets();
                TreeMap<String, DW_UO_Set> AllUOSets = null;
                AllUOSets = combineDW_UO_Sets(
                        CouncilUOSets,
                        RSLUOSets);
                Set<DW_ID> UOApril2013ClaimIDs;
                UOApril2013ClaimIDs = DW_UO_Handler.getUOStartApril2013ClaimIDs(DW_UO_Data);
                if (DoPostcodeChanges) {
                    env.logO("<DoPostcodeChanges>", true);
                    iteB = bArray.iterator();
                    while (iteB.hasNext()) {
                        boolean CheckPreviousTenure;
                        CheckPreviousTenure = iteB.next();
                        env.logO("<CheckPreviousTenure " + CheckPreviousTenure + ">", true);
                        Iterator<Boolean> iteB2;
                        iteB2 = bArray.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.logO("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            Iterator<Boolean> iteB5;
                            iteB5 = bArray.iterator();
                            while (iteB5.hasNext()) {
                                boolean PostcodeChange;
                                PostcodeChange = iteB5.next();
                                env.logO("<PostcodeChange " + PostcodeChange + ">", true);
                                Iterator<Boolean> iteB6;
                                iteB6 = bArray.iterator();
                                while (iteB6.hasNext()) {
                                    boolean CheckPreviousPostcode;
                                    CheckPreviousPostcode = iteB6.next();
                                    env.logO("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    boolean DoUOOnlyOnThoseOriginallyUO;
                                    //doUOOnlyOnThoseOriginallyUO = true;
                                    Iterator<Boolean> iteB0;
                                    iteB0 = bArray.iterator();
                                    while (iteB0.hasNext()) {
                                        DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                        env.logO("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        Set<DW_ID> UOApril2013ClaimIDsDummy;
                                        if (DoUOOnlyOnThoseOriginallyUO) {
                                            UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                        } else {
                                            UOApril2013ClaimIDsDummy = null;
                                        }
                                        doPostcodeChangesU(
                                                SHBEFilenames,
                                                TTs.get(DoUnderOccupiedData),
                                                includes,
                                                loadData,
                                                CheckPreviousTenure,
                                                ReportTenancyTransitionBreaks,
                                                PostcodeChange,
                                                CheckPreviousPostcode,
                                                AllUOSets,
                                                CouncilUOSets,
                                                RSLUOSets,
                                                UOApril2013ClaimIDsDummy);
                                        env.logO("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    }
                                    env.logO("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                }
                                env.logO("</PostcodeChange " + PostcodeChange + ">", true);
                            }
                            env.logO("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.logO("</CheckPreviousTenure " + CheckPreviousTenure + ">", true);
                    }
                    env.logO("</DoPostcodeChanges>", true);
                }
                if (DoAnyTenancyChanges) {
                    env.logO("<DoAnyTenancyChanges>", true);
                    // TenancyTransitions
                    boolean CheckPreviousTenure;
                    iteB = bArray.iterator();
                    while (iteB.hasNext()) {
                        CheckPreviousTenure = iteB.next();
                        env.logO("<CheckPreviousTenure " + CheckPreviousTenure + ">", true);
                        Iterator<Boolean> iteB2;
                        iteB2 = bArray.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.logO("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            if (DoTenancyChanges) {
                                env.logO("<DoTenancyChanges " + DoTenancyChanges + ">", true);
                                boolean DoUOOnlyOnThoseOriginallyUO;
//                                doUOOnlyOnThoseOriginallyUO = true;
                                Iterator<Boolean> iteB0;
                                iteB0 = bArray.iterator();
                                while (iteB0.hasNext()) {
                                    DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                    env.logO("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    Set<DW_ID> UOApril2013ClaimIDsDummy;
                                    if (DoUOOnlyOnThoseOriginallyUO) {
                                        UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                    } else {
                                        UOApril2013ClaimIDsDummy = null;
                                    }
                                    doTTTsU(
                                            SHBEFilenames,
                                            TTs.get(DoUnderOccupiedData),
                                            includes,
                                            CheckPreviousTenure,
                                            ReportTenancyTransitionBreaks,
                                            AllUOSets,
                                            CouncilUOSets,
                                            RSLUOSets,
                                            UOApril2013ClaimIDsDummy);
                                    env.logO("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                }
                                env.logO("</DoTenancyChanges " + DoTenancyChanges + ">", true);
                            }
                            if (DoTenancyAndPostcodeChanges) {
                                env.logO("<DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                                Iterator<Boolean> iteB5;
                                iteB5 = bArray.iterator();
                                while (iteB5.hasNext()) {
                                    boolean PostcodeChange;
                                    PostcodeChange = iteB5.next();
                                    env.logO("<PostcodeChange " + PostcodeChange + ">", true);
                                    Iterator<Boolean> iteB6;
                                    iteB6 = bArray.iterator();
                                    while (iteB6.hasNext()) {
                                        boolean CheckPreviousPostcode;
                                        CheckPreviousPostcode = iteB6.next();
                                        env.logO("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                        boolean DoUOOnlyOnThoseOriginallyUO;
                                        //doUOOnlyOnThoseOriginallyUO = true;
                                        Iterator<Boolean> iteB0;
                                        iteB0 = bArray.iterator();
                                        while (iteB0.hasNext()) {
                                            DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                            env.logO("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                            Set<DW_ID> UOApril2013ClaimIDsDummy;
                                            if (DoUOOnlyOnThoseOriginallyUO) {
                                                UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                            } else {
                                                UOApril2013ClaimIDsDummy = null;
                                            }
                                            doTTAndPostcodeChangesU(
                                                    SHBEFilenames,
                                                    TTs.get(DoUnderOccupiedData),
                                                    includes,
                                                    CheckPreviousTenure,
                                                    ReportTenancyTransitionBreaks,
                                                    PostcodeChange,
                                                    CheckPreviousPostcode,
                                                    AllUOSets,
                                                    CouncilUOSets,
                                                    RSLUOSets,
                                                    UOApril2013ClaimIDsDummy);
                                            env.logO("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        }
                                        env.logO("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    }
                                    env.logO("</PostcodeChange " + PostcodeChange + ">", true);
                                }
                                env.logO("</DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                            }
                            env.logO("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.logO("</CheckPreviousTenure " + CheckPreviousTenure + ">", true);
                    }
                    env.logO("</DoAnyTenancyChanges>", true);
                }
            }
            env.logO("</DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
        }
    }

    /**
     * What?
     *
     * @param underOccupiedData
     * @return
     */
    private TreeMap<String, DW_UO_Set> combineDW_UO_Sets(
            TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil,
            TreeMap<String, DW_UO_Set> DW_UO_SetsRSL) {
        TreeMap<String, DW_UO_Set> result;
        result = new TreeMap<String, DW_UO_Set>();
        String YM3;
        DW_UO_Set DW_UO_SetAll;
        DW_UO_Set DW_UO_SetCouncil;
        DW_UO_Set DW_UO_SetRSL;

        Iterator<String> ite;
        ite = DW_UO_SetsCouncil.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            DW_UO_SetAll = result.get(YM3);
            if (DW_UO_SetAll == null) {
                DW_UO_SetAll = new DW_UO_Set(env);
                result.put(YM3, DW_UO_SetAll);
            }
            DW_UO_SetCouncil = DW_UO_SetsCouncil.get(YM3);
            DW_UO_SetAll.getMap().putAll(DW_UO_SetCouncil.getMap());
        }
        ite = DW_UO_SetsRSL.keySet().iterator();
        while (ite.hasNext()) {
            YM3 = ite.next();
            DW_UO_SetAll = result.get(YM3);
            if (DW_UO_SetAll == null) {
                DW_UO_SetAll = new DW_UO_Set(env);
                result.put(YM3, DW_UO_SetAll);
            }
            DW_UO_SetRSL = DW_UO_SetsRSL.get(YM3);
            DW_UO_SetAll.getMap().putAll(DW_UO_SetRSL.getMap());
        }
        return result;
    }

    /**
     * Calculates and writes out: Tenancy Type Transition Matrixes; and Tenancy
     * Type Transition Frequencies for under occupied.
     *
     * @param SHBEFilenames
     * @param TTs
     * @param includes
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param AllUOSets
     * @param CouncilUOSets
     * @param RSLUOSets
     * @param ClaimIDs
     */
    public void doTTTsU(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            TreeMap<String, DW_UO_Set> AllUOSets,
            TreeMap<String, DW_UO_Set> CouncilUOSets,
            TreeMap<String, DW_UO_Set> RSLUOSets,
            Set<DW_ID> ClaimIDs) {
        HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups;
        ClaimIDToPostcodeIDLookups = new HashMap<Integer, HashMap<DW_ID, DW_ID>>();
        HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup;
        File dirOut;
        dirOut = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                DW_Strings.sAll,
                checkPreviousTenure);
        dirOut = new File(
                dirOut,
                DW_Strings.sWithOrWithoutPostcodeChange);
        dirOut = DW_Files.getUOFile(
                dirOut,
                true,
                true,
                true);
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
            if (ClaimIDs != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        DW_Strings.sAll);
            }
            dirOut2.mkdirs();
            env.logO("dirOut " + dirOut2, true);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            HashMap<Integer, String> indexYM3s;
            indexYM3s = DW_SHBE_Handler.getIndexYM3s();
            int i;
            i = includeIte.next();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String YM30;
            YM30 = DW_SHBE_Handler.getYM3(filename);
            DW_UO_Set AllUOSet0 = null;
            DW_UO_Set CouncilUOSet0 = null;
            DW_UO_Set RSLUOSet0 = null;
            DW_SHBE_Records DW_SHBE_Records0 = null;
            DW_SHBE_Records0 = DW_SHBE_Data.getDW_SHBE_Records(YM30);
            HashMap<DW_ID, DW_SHBE_Record> Records0;
            Records0 = DW_SHBE_Records0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
            // ClaimIDToTTLookups
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups;
            ClaimIDToTTLookups = new HashMap<Integer, HashMap<DW_ID, Integer>>();
            // ClaimIDToTTLookup0
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0;
            ClaimIDToTTLookup0 = loadClaimIDToTTLookup(
                    YM30,
                    i,
                    ClaimIDToTTLookups);
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets = null;
            HashMap<Integer, Set<DW_ID>> CouncilUOClaimIDSets = null;
            HashMap<Integer, Set<DW_ID>> RSLUOClaimIDSets = null;
            boolean doloop = true;
            AllUOSet0 = AllUOSets.get(YM30);
            CouncilUOSet0 = CouncilUOSets.get(YM30);
            RSLUOSet0 = RSLUOSets.get(YM30);
            UOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
            CouncilUOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
            RSLUOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
            if (AllUOSet0 == null) {
                doloop = false;
            } else {
                UOClaimIDSets.put(i, AllUOSet0.getClaimIDs());
                CouncilUOClaimIDSets.put(i, CouncilUOSet0.getClaimIDs());
                RSLUOClaimIDSets.put(i, RSLUOSet0.getClaimIDs());
                ClaimIDToPostcodeIDLookup = loadClaimIDToPostcodeIDLookup(
                        YM30,
                        i,
                        ClaimIDToPostcodeIDLookups);
                ClaimIDToPostcodeIDLookups.put(i, ClaimIDToPostcodeIDLookup);
            }
            if (doloop) {
                HashMap<DW_ID, ArrayList<String>> TTCs;
                TTCs = new HashMap<DW_ID, ArrayList<String>>();
                HashMap<DW_ID, ArrayList<String>> GTTCs;
                GTTCs = new HashMap<DW_ID, ArrayList<String>>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = SHBEFilenames[i];
                    ClaimIDToPostcodeIDLookups.put(i, new HashMap<DW_ID, DW_ID>());
                    // Set Year and Month variables
                    String YM31 = DW_SHBE_Handler.getYM3(filename);
                    env.logO("Year Month " + YM31, true);
                    DW_SHBE_Records DW_SHBE_Records1;
                    DW_SHBE_Records1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
                    ClaimIDToPostcodeIDLookup = loadClaimIDToPostcodeIDLookup(
                            YM31,
                            i,
                            ClaimIDToPostcodeIDLookups);
                    ClaimIDToPostcodeIDLookups.put(i, ClaimIDToPostcodeIDLookup);
                    HashMap<DW_ID, DW_SHBE_Record> Records1;
                    Records1 = DW_SHBE_Records1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
                    // ClaimIDToTTLookup1
                    HashMap<DW_ID, Integer> ClaimIDToTTLookup1;
                    ClaimIDToTTLookup1 = loadClaimIDToTTLookup(
                            YM31,
                            i,
                            ClaimIDToTTLookups);
                    DW_UO_Set DW_UO_SetAll1 = null;
                    DW_UO_Set DW_UO_SetCouncil1 = null;
                    DW_UO_Set DW_UO_SetRSL1 = null;
                    DW_UO_SetAll1 = AllUOSets.get(YM31);
                    DW_UO_SetCouncil1 = CouncilUOSets.get(YM31);
                    DW_UO_SetRSL1 = RSLUOSets.get(YM31);
                    // Get TenancyTypeTranistionMatrix
                    TreeMap<String, TreeMap<String, Integer>> TTTM;
                    TTTM = getTTTMatrixAndRecordTTTU(
                            ClaimIDToPostcodeIDLookups,
                            Records0,
                            Records1,
                            ClaimIDToTTLookup0,
                            ClaimIDToTTLookup1,
                            ClaimIDToTTLookups,
                            UOClaimIDSets,
                            CouncilUOClaimIDSets,
                            RSLUOClaimIDSets,
                            TTCs,
                            YM30,
                            YM31,
                            checkPreviousTenure,
                            i,
                            include,
                            indexYM3s,
                            AllUOSet0,
                            CouncilUOSet0,
                            RSLUOSet0,
                            DW_UO_SetAll1,
                            DW_UO_SetCouncil1,
                            DW_UO_SetRSL1,
                            ClaimIDs);
                    writeTTTMatrix(
                            TTTM,
                            YM30,
                            YM31,
                            dirOut2,
                            TTs);
                    Records0 = Records1;
                    YM30 = YM31;
                    ClaimIDToTTLookup0 = ClaimIDToTTLookup1;
                    AllUOSet0 = DW_UO_SetAll1;
                    CouncilUOSet0 = DW_UO_SetCouncil1;
                    RSLUOSet0 = DW_UO_SetRSL1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<DW_ID> TTCIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = 0;
                TTCIte = TTCs.keySet().iterator();
                while (TTCIte.hasNext()) {
                    DW_ID ClaimID;
                    ClaimID = TTCIte.next();
                    ArrayList<String> transition;
                    transition = TTCs.get(ClaimID);
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
                env.logO("line 597 " + includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = 0;
                    TTCIte = GTTCs.keySet().iterator();
                    while (TTCIte.hasNext()) {
                        DW_ID ClaimID;
                        ClaimID = TTCIte.next();
                        ArrayList<String> transition;
                        transition = TTCs.get(ClaimID);
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
                    env.logO("line 650 " + includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1), true);
                    writeTransitionFrequencies(
                            transitions,
                            dirOut2,
                            DW_Strings.sGrouped,
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
     * @param SHBEFilenames
     * @param TTs
     * @param includes
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param DW_UO_SetsAll
     * @param DW_UO_SetsCouncil
     * @param DW_UO_SetsRSL
     * @param UOInApril2013
     */
    public void doTTAndPostcodeChangesU(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            TreeMap<String, DW_UO_Set> DW_UO_SetsAll,
            TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil,
            TreeMap<String, DW_UO_Set> DW_UO_SetsRSL,
            Set<DW_ID> UOInApril2013) {
        File dirOut;
        dirOut = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                DW_Strings.sAll,
                checkPreviousTenure);
        dirOut = new File(
                dirOut,
                DW_Strings.sTenancyAndPostcodeChanges);

        HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups;
        ClaimIDToPostcodeIDLookups = new HashMap<Integer, HashMap<DW_ID, DW_ID>>();

        dirOut = DW_Files.getUOFile(dirOut, true, true, true);
        if (postcodeChange) {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChanged);
        } else {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChangedNo);
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
            if (UOInApril2013 != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        DW_Strings.sAll);
            }
            dirOut2.mkdirs();
            env.logO("dirOut " + dirOut2, true);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String YM30;
            YM30 = DW_SHBE_Handler.getYM3(filename);
            // ClaimIDToTTLookups
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups;
            ClaimIDToTTLookups = new HashMap<Integer, HashMap<DW_ID, Integer>>();
            // ClaimIDToTTLookup0
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0;
            ClaimIDToTTLookup0 = loadClaimIDToTTLookup(
                    YM30,
                    i,
                    ClaimIDToTTLookups);
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupancies = null;
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupanciesCouncil = null;
//            HashMap<Integer, HashSet<DW_ID>> tUnderOccupanciesRSL = null;
//            HashSet<DW_ID> tUnderOccupiedIDs0 = null;
//            HashSet<DW_ID> tUnderOccupiedIDs1 = null;
//            if (doUnderOccupied) {
//                underOccupiedSet0 = underOccupiedSets.get(YM30);
//                tUnderOccupancies = new HashMap<Integer, HashSet<DW_ID>>();
//                tUnderOccupiedIDs0 = getUnderOccupiedIDs(
//                        underOccupiedSet0,
//                        i,
//                        tUnderOccupancies);
//            }

            DW_UO_Set UOSet0 = null;
            DW_UO_Set CouncilUOSet0 = null;
            DW_UO_Set RSLUOSet0 = null;

            // ClaimIDToPostcodeIDLookup0
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0;
            ClaimIDToPostcodeIDLookup0 = loadClaimIDToPostcodeIDLookup(
                    YM30,
                    i,
                    ClaimIDToPostcodeIDLookups);

            HashMap<Integer, Set<DW_ID>> UOClaimIDs = null;
            HashMap<Integer, Set<DW_ID>> CouncilUOClaimIDs = null;
            HashMap<Integer, Set<DW_ID>> RSLUOClaimIDs = null;

            Set<DW_ID> UOClaimIDs0 = null;
            Set<DW_ID> CouncilUOClaimIDs0 = null;
            Set<DW_ID> RSLUOClaimIDs0 = null;
            Set<DW_ID> UOClaimIDs1 = null;
            Set<DW_ID> CouncilUOClaimIDs1 = null;
            Set<DW_ID> RSLUOClaimIDs1 = null;

            boolean doloop = true;
            UOClaimIDs = new HashMap<Integer, Set<DW_ID>>();
            CouncilUOClaimIDs = new HashMap<Integer, Set<DW_ID>>();
            RSLUOClaimIDs = new HashMap<Integer, Set<DW_ID>>();
            UOSet0 = DW_UO_SetsAll.get(YM30);
            CouncilUOSet0 = DW_UO_SetsCouncil.get(YM30);
            RSLUOSet0 = DW_UO_SetsRSL.get(YM30);
            if (UOSet0 != null) {
                UOClaimIDs0 = UOSet0.getClaimIDs();
            } else {
                doloop = false;
            }
            if (CouncilUOSet0 == null) {
                CouncilUOClaimIDs0 = CouncilUOSet0.getClaimIDs();
            } else {
                doloop = false;
            }
            if (RSLUOSet0 == null) {
                RSLUOClaimIDs0 = RSLUOSet0.getClaimIDs();
            } else {
                doloop = false;
            }
            if (doloop) {
                // Init TTCs and GTTCs
                HashMap<DW_ID, ArrayList<String>> TTCs;
                TTCs = new HashMap<DW_ID, ArrayList<String>>();
                HashMap<DW_ID, ArrayList<String>> GTTCs;
                GTTCs = new HashMap<DW_ID, ArrayList<String>>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = SHBEFilenames[i];
                    // Set Year and Month variables
                    String YM31 = DW_SHBE_Handler.getYM3(filename);
                    // UOSet1
                    DW_UO_Set UOSet1 = null;
                    String key;
                    key = DW_SHBE_Handler.getYM3(filename);
                    UOSet1 = DW_UO_SetsAll.get(key);
                    UOClaimIDs1 = UOSet1.getClaimIDs();
                    // ClaimIDToTTLookup1
                    HashMap<DW_ID, Integer> ClaimIDToTTLookup1;
                    ClaimIDToTTLookup1 = loadClaimIDToTTLookup(
                            YM31,
                            i,
                            ClaimIDToTTLookups);
                    // ClaimIDToPostcodeIDLookup1
                    HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1;
                    ClaimIDToPostcodeIDLookup1 = loadClaimIDToPostcodeIDLookup(
                            YM31,
                            i,
                            ClaimIDToPostcodeIDLookups);
                    // Get TenancyTypeTranistionMatrix
                    TreeMap<String, TreeMap<String, Integer>> TTTM;
                    TTTM = getTTTMatrixAndWritePTDetailsU(
                            dirOut2,
                            ClaimIDToTTLookup0,
                            ClaimIDToTTLookup1,
                            ClaimIDToTTLookups,
                            UOClaimIDs0,
                            UOClaimIDs1,
                            UOClaimIDs,
                            CouncilUOClaimIDs,
                            RSLUOClaimIDs,
                            TTCs,
                            YM30,
                            YM31,
                            checkPreviousTenure,
                            i,
                            include,
                            ClaimIDToPostcodeIDLookup0,
                            ClaimIDToPostcodeIDLookup1,
                            ClaimIDToPostcodeIDLookups,
                            postcodeChange,
                            checkPreviousPostcode,
                            UOSet0,
                            UOSet1,
                            UOInApril2013);
                    writeTTTMatrix(
                            TTTM,
                            YM30,
                            YM31,
                            dirOut2,
                            TTs);
                    YM30 = YM31;
                    ClaimIDToTTLookup0 = ClaimIDToTTLookup1;
                    ClaimIDToPostcodeIDLookup0 = ClaimIDToPostcodeIDLookup1;
                    UOClaimIDs0 = UOClaimIDs1;
                    UOSet0 = UOSet1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<DW_ID> TTCsITe;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = 0;
                TTCsITe = TTCs.keySet().iterator();
                while (TTCsITe.hasNext()) {
                    DW_ID ClaimID;
                    ClaimID = TTCsITe.next();
                    ArrayList<String> transition;
                    transition = TTCs.get(ClaimID);
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
                env.logO("line 927 " + includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = 0;
                    TTCsITe = GTTCs.keySet().iterator();
                    while (TTCsITe.hasNext()) {
                        DW_ID ClaimID;
                        ClaimID = TTCsITe.next();
                        ArrayList<String> transition;
                        transition = GTTCs.get(ClaimID);
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
                    env.logO("line 980 " + includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1), true);
                    writeTransitionFrequencies(
                            transitions,
                            dirOut2,
                            DW_Strings.sGrouped,
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
     * @param TTs
     * @param includes
     * @param loadData
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param DW_UO_SetsCouncil
     * @param DW_UO_SetsRSL
     * @param DW_UO_SetsAll
     * @param ClaimIDs
     */
    public void doPostcodeChangesU(
            String[] SHBEFilenames,
            //String PT,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil,
            TreeMap<String, DW_UO_Set> DW_UO_SetsRSL,
            TreeMap<String, DW_UO_Set> DW_UO_SetsAll,
            Set<DW_ID> ClaimIDs) {
        File dirOut;
        dirOut = new File(
                DW_Files.getOutputSHBETablesDir(),
                DW_Strings.sPostcodeChanges);
        dirOut = DW_Files.getUOFile(dirOut, true, true, true);
        if (postcodeChange) {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChanged);
        } else {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChangedNo);
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
            if (ClaimIDs != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        DW_Strings.sAll);
            }
            dirOut2.mkdirs();
            //env.logO("dirOut " + dirOut2);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String YM30;
            YM30 = DW_SHBE_Handler.getYM3(filename);
            // UOSet0
            DW_UO_Set UOSet0 = null;
            // ClaimIDToTTLookups
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups;
            ClaimIDToTTLookups = new HashMap<Integer, HashMap<DW_ID, Integer>>();
            // ClaimIDToTTLookup0
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0;
            ClaimIDToTTLookup0 = loadClaimIDToTTLookup(
                    YM30,
                    i,
                    ClaimIDToTTLookups);
            // ClaimIDToPostcodeIDLookups
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups;
            ClaimIDToPostcodeIDLookups = new HashMap<Integer, HashMap<DW_ID, DW_ID>>();
            // ClaimIDToPostcodeIDLookup0
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0;
            ClaimIDToPostcodeIDLookup0 = loadClaimIDToPostcodeIDLookup(
                    // loadData,
                    YM30,
                    i,
                    ClaimIDToPostcodeIDLookups);
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets = null;
            Set<DW_ID> UOClaimIDSet0 = null;
            Set<DW_ID> UOClaimIDSet1 = null;
            UOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
            UOSet0 = DW_UO_SetsAll.get(YM30);
            if (UOSet0 == null) {
                env.logO("underOccupiedSet0 == null, YM30 = " + YM30, true);
            } else {
                UOClaimIDSet0 = UOSet0.getClaimIDs();
            }
            // Init TenancyTypeChanges and GroupedTenancyTypeChanges
            HashMap<DW_ID, ArrayList<String>> TTCs;
            TTCs = new HashMap<DW_ID, ArrayList<String>>();
            HashMap<DW_ID, ArrayList<String>> GTTCs;
            GTTCs = new HashMap<DW_ID, ArrayList<String>>();
            // Main loop
            while (includeIte.hasNext()) {
                i = includeIte.next();
                filename = SHBEFilenames[i];
                // Set Year and Month variables
                String YM31 = DW_SHBE_Handler.getYM3(filename);
                // UOSet1
                DW_UO_Set UOSet1 = null;
                // ClaimIDToTTLookup1
                HashMap<DW_ID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = loadClaimIDToTTLookup(
                        YM31,
                        i,
                        ClaimIDToTTLookups);
                // ClaimIDToPostcodeIDLookup1
                HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = loadClaimIDToPostcodeIDLookup(
                        //loadData,
                        YM31,
                        i,
                        ClaimIDToPostcodeIDLookups);
                UOSet1 = DW_UO_SetsAll.get(YM31);
                if (UOSet1 == null) {
                    env.logO("underOccupiedSet1 == null, YM31 = " + YM31, true);
                } else {
                    UOClaimIDSet1 = UOSet1.getClaimIDs();
                }
                // Get PostcodeTransitionCounts
                TreeMap<String, TreeMap<String, Integer>> postcodeTransitionCounts;
                if (UOClaimIDSet0 == null || UOClaimIDSet1 == null) {
                    env.logO("Not calculating or writing out postcodeTransitionCounts", true);
                } else {
                    postcodeTransitionCounts = getPTCountsNoTTTU(
                            dirOut2,
                            ClaimIDToTTLookup0,
                            ClaimIDToTTLookup1,
                            ClaimIDToTTLookups,
                            UOClaimIDSet0,
                            UOClaimIDSet1,
                            UOClaimIDSets,
                            TTCs,
                            YM30,
                            YM31,
                            checkPreviousTenure,
                            i,
                            include,
                            ClaimIDToPostcodeIDLookup0,
                            ClaimIDToPostcodeIDLookup1,
                            ClaimIDToPostcodeIDLookups,
                            postcodeChange,
                            checkPreviousPostcode,
                            UOSet0,
                            UOSet1,
                            ClaimIDs);
                    writeTTTMatrix(
                            postcodeTransitionCounts,
                            YM30,
                            YM31,
                            dirOut2,
                            TTs);
                }
                YM30 = YM31;
                ClaimIDToTTLookup0 = ClaimIDToTTLookup1;
                ClaimIDToPostcodeIDLookup0 = ClaimIDToPostcodeIDLookup1;
                UOClaimIDSet0 = UOClaimIDSet1;
                UOSet0 = UOSet1;
            }
            // Deal with the frequency of under occupancy changes.
            TreeMap<String, Integer> transitions;
            int max;
            Iterator<DW_ID> TTCIte;
            // Ungrouped
            transitions = new TreeMap<String, Integer>();
            max = 0;
            TTCIte = TTCs.keySet().iterator();
            while (TTCIte.hasNext()) {
                DW_ID ClaimID;
                ClaimID = TTCIte.next();
                ArrayList<String> transition;
                transition = TTCs.get(ClaimID);
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
            env.logO("line 1222 " + includeKey + " maximum number of transitions "
                    + max + " out of a possible " + (include.size() - 1), true);
            writeTransitionFrequencies(transitions,
                    dirOut2,
                    DW_Strings.sGroupedNo,
                    "Frequencies.txt",
                    reportTenancyTransitionBreaks);
            // Grouped
            if (doGrouped) {
                transitions = new TreeMap<String, Integer>();
                max = 0;
                TTCIte = GTTCs.keySet().iterator();
                while (TTCIte.hasNext()) {
                    DW_ID ClaimID;
                    ClaimID = TTCIte.next();
                    ArrayList<String> transition;
                    transition = GTTCs.get(ClaimID);
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
                env.logO("line 1275 " + includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(
                        transitions,
                        dirOut2,
                        DW_Strings.sGrouped,
                        "FrequenciesGrouped.txt",
                        reportTenancyTransitionBreaks);
            }
        }
    }

    /**
     * For counting where postcode has changed and reporting by tenancy type.
     *
     * @param SHBEFilenames
     * @param PT
     * @param doGrouped
     * @param TTs
     * @param GTTs
     * @param RGs
     * @param UGs
     * @param includes
     * @param loadData
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param DW_UO_Data
     * @param doUnderOccupied
     * @param ClaimIDs
     */
    public void doPostcodeChanges(
            String[] SHBEFilenames,
            String PT,
            boolean doGrouped,
            ArrayList<String> TTs,
            ArrayList<String> GTTs,
            ArrayList<Integer> RGs,
            ArrayList<Integer> UGs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Data DW_UO_Data,
            boolean doUnderOccupied,
            Set<DW_ID> ClaimIDs) {
        File dirOut;
        dirOut = new File(
                DW_Files.getOutputSHBETablesDir(),
                DW_Strings.sPostcodeChanges);
        dirOut = new File(
                dirOut,
                PT);
        TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil;
        DW_UO_SetsCouncil = DW_UO_Data.getCouncilUOSets();
        TreeMap<String, DW_UO_Set> DW_UO_SetsRSL;
        DW_UO_SetsRSL = DW_UO_Data.getRSLUOSets();
        TreeMap<String, DW_UO_Set> DW_UO_SetsAll;
        DW_UO_SetsAll = combineDW_UO_Sets(
                DW_UO_SetsCouncil,
                DW_UO_SetsRSL);
        dirOut = DW_Files.getUOFile(dirOut, doUnderOccupied, doUnderOccupied, doUnderOccupied);
        if (postcodeChange) {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChanged);
        } else {
            dirOut = new File(
                    dirOut,
                    DW_Strings.sPostcodeChangedNo);
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
            if (ClaimIDs != null) {
                dirOut2 = new File(
                        dirOut2,
                        sUOInApril2013);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        DW_Strings.sAll);
            }
            dirOut2.mkdirs();
            //env.logO("dirOut " + dirOut2);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            int i;
            i = includeIte.next();
            // Load first data
            String filename;
            filename = SHBEFilenames[i];
            String YM30;
            YM30 = DW_SHBE_Handler.getYM3(filename);
            // UOSet0
            DW_UO_Set UOSet0 = null;
            // ClaimIDToTTLookups
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups;
            ClaimIDToTTLookups = new HashMap<Integer, HashMap<DW_ID, Integer>>();
            // ClaimIDToTTLookup0
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0;
            ClaimIDToTTLookup0 = loadClaimIDToTTLookup(
                    YM30,
                    i,
                    ClaimIDToTTLookups);
            // ClaimIDToPostcodeIDLookups
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups;
            ClaimIDToPostcodeIDLookups = new HashMap<Integer, HashMap<DW_ID, DW_ID>>();
            // ClaimIDToPostcodeIDLookup0
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0;
            ClaimIDToPostcodeIDLookup0 = loadClaimIDToPostcodeIDLookup(
                    YM30,
                    i,
                    ClaimIDToPostcodeIDLookups);
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets = null;
            Set<DW_ID> UOClaimIDSet0 = null;
            Set<DW_ID> UOClaimIDSet1 = null;
            if (doUnderOccupied) {
                UOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
                UOSet0 = DW_UO_SetsAll.get(YM30);
                if (UOSet0 == null) {
                    env.logO("UOSet0 == null, YM30 = " + YM30, true);
                } else {
                    UOClaimIDSet0 = UOSet0.getClaimIDs();
                }
            }
            // Init TenancyTypeChanges and GroupedTenancyTypeChanges
            HashMap<DW_ID, ArrayList<String>> TTCs;
            TTCs = new HashMap<DW_ID, ArrayList<String>>();
            HashMap<DW_ID, ArrayList<String>> GTTCs;
            GTTCs = new HashMap<DW_ID, ArrayList<String>>();
            // Main loop
            while (includeIte.hasNext()) {
                i = includeIte.next();
                filename = SHBEFilenames[i];
                // Set Year and Month variables
                String YM31 = DW_SHBE_Handler.getYM3(filename);
                // UOSet1
                DW_UO_Set UOSet1 = null;
                // ClaimIDToTTLookup1
                HashMap<DW_ID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = loadClaimIDToTTLookup(
                        YM31,
                        i,
                        ClaimIDToTTLookups);
                // ClaimIDToPostcodeIDLookup1
                HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = loadClaimIDToPostcodeIDLookup(
                        YM31,
                        i,
                        ClaimIDToPostcodeIDLookups);
                if (doUnderOccupied) {
                    UOSet1 = DW_UO_SetsAll.get(YM31);
                    if (UOSet1 == null) {
                        env.logO("underOccupiedSet1 == null, YM31 = " + YM31, true);
                    } else {
                        UOClaimIDSet1 = UOSet1.getClaimIDs();
                    }
                }
                // Get PostcodeTransitionCounts
                TreeMap<String, TreeMap<String, Integer>> postcodeTransitionCounts;
                if (doUnderOccupied && (UOClaimIDSet0 == null || UOClaimIDSet1 == null)) {
                    env.logO("Not calculating or writing out postcodeTransitionCounts", true);
                } else {
                    postcodeTransitionCounts = getPTCountsNoTTT(
                            dirOut2,
                            ClaimIDToTTLookup0,
                            ClaimIDToTTLookup1,
                            ClaimIDToTTLookups,
                            UOClaimIDSet0,
                            UOClaimIDSet1,
                            UOClaimIDSets,
                            TTCs,
                            YM30,
                            YM31,
                            checkPreviousTenure,
                            i,
                            include,
                            ClaimIDToPostcodeIDLookup0,
                            ClaimIDToPostcodeIDLookup1,
                            ClaimIDToPostcodeIDLookups,
                            postcodeChange,
                            checkPreviousPostcode,
                            UOSet0,
                            UOSet1,
                            doUnderOccupied,
                            ClaimIDs);
                    writeTTTMatrix(
                            postcodeTransitionCounts,
                            YM30,
                            YM31,
                            dirOut2,
                            TTs);
                }
                YM30 = YM31;
                ClaimIDToTTLookup0 = ClaimIDToTTLookup1;
                ClaimIDToPostcodeIDLookup0 = ClaimIDToPostcodeIDLookup1;
                UOClaimIDSet0 = UOClaimIDSet1;
                UOSet0 = UOSet1;
            }
            // The only concern here is the frequency of under occupancy changes.
            if (doUnderOccupied) {
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<DW_ID> TTCIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = 0;
                TTCIte = TTCs.keySet().iterator();
                while (TTCIte.hasNext()) {
                    DW_ID ClaimID;
                    ClaimID = TTCIte.next();
                    ArrayList<String> transition;
                    transition = TTCs.get(ClaimID);
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
                env.logO("line 1537 " + includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.txt",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = 0;
                    TTCIte = GTTCs.keySet().iterator();
                    while (TTCIte.hasNext()) {
                        DW_ID ClaimID;
                        ClaimID = TTCIte.next();
                        ArrayList<String> transition;
                        transition = GTTCs.get(ClaimID);
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
                    env.logO("line 1590 " + includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1), true);
                    writeTransitionFrequencies(
                            transitions,
                            dirOut2,
                            DW_Strings.sGrouped,
                            "FrequenciesGrouped.txt",
                            reportTenancyTransitionBreaks);
                }
            }
        }
    }

    protected HashMap<DW_ID, Integer> loadClaimIDToTTLookup(
            String YM3,
            Integer key,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups) {
        HashMap<DW_ID, Integer> result;
        if (ClaimIDToTTLookups.containsKey(key)) {
            return ClaimIDToTTLookups.get(key);
        }
        result = new HashMap<DW_ID, Integer>();
        DW_SHBE_Records DW_SHBE_Records;
        DW_SHBE_Records = DW_SHBE_Data.getDW_SHBE_Records(YM3);
        HashMap<DW_ID, DW_SHBE_Record> records;
        records = DW_SHBE_Records.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        Iterator<DW_ID> ite;
        ite = records.keySet().iterator();
        DW_ID ClaimID;
        while (ite.hasNext()) {
            ClaimID = ite.next();
            result.put(
                    ClaimID,
                    records.get(ClaimID).getDRecord().getTenancyType());
        }
        ClaimIDToTTLookups.put(key, result);
        return result;
    }

    protected HashMap<DW_ID, DW_ID> loadClaimIDToPostcodeIDLookup(
            String YM3,
            Integer key,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups) {
        HashMap<DW_ID, DW_ID> result;
        if (ClaimIDToPostcodeIDLookups.containsKey(key)) {
            return ClaimIDToPostcodeIDLookups.get(key);
        }
        result = DW_SHBE_Data.getDW_SHBE_Records(YM3).getClaimIDToPostcodeIDLookup(
                env._HandleOutOfMemoryError_boolean);
        ClaimIDToPostcodeIDLookups.put(key, result);
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
                    DW_Strings.sIncludingTenancyTransitionBreaks);
        } else {
            dirOut2 = new File(
                    dirOut2,
                    DW_Strings.sIncludingTenancyTransitionBreaksNo);
        }
        dirOut2.mkdirs();
        File f;
        f = new File(
                dirOut2,
                name);
        env.logO("Write " + f, true);
        result = Generic_StaticIO.getPrintWriter(f, false);
        return result;
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> YM31CountAreas,
            String YM31,
            TreeMap<Integer, Integer> TTCounts,
            String level,
            File dir) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result = writeResults(
                    areaCounts,
                    TTCounts,
                    level,
                    dir,
                    YM31);
            int num = 5;
            // Write out areas with biggest increases from last year
            writeExtremeAreaChanges(
                    result,
                    YM31CountAreas,
                    "LastYear",
                    num,
                    dir,
                    YM31);
            return result;
        } else {
            return null;
        }
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, Integer> TTCounts,
            String level,
            File dir,
            String YM3) {
        if (areaCounts.size() > 0) {
            int num = 5;
            TreeMap<Integer, TreeSet<String>> result;
            // Write out counts by area
            result = writeCountsByArea(
                    areaCounts,
                    level,
                    dir,
                    YM3);
            // Write out areas with highest counts
            writeAreasWithHighestNumbersOfClaimants(
                    result,
                    num,
                    dir,
                    YM3);
            // Write out counts by TT
            writeCountsByTenure(
                    TTCounts,
                    dir,
                    YM3);
            return result;
        } else {
            return null;
        }
    }

    protected void writeExtremeAreaChanges(
            TreeMap<Integer, TreeSet<String>> countAreas,
            TreeMap<Integer, TreeSet<String>> lastTimeCountAreas,
            String YM30,
            int num,
            File dir,
            String YM31) {
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
                    YM30,
                    num,
                    dir,
                    YM31);
            TreeMap<Double, TreeSet<String>> relativeDiffsAreas;
            relativeDiffsAreas = getCountAreas(areaRelativeDiffs);
            writeDiffs(
                    relativeDiffsAreas,
                    "Relative",
                    YM30,
                    num,
                    dir,
                    YM31);
        }
    }

    /**
     *
     * @param a
     * @param b
     * @param n
     * @return
     */
    public double getRelativeDifference(
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

    protected void writeDiffs(
            TreeMap<Double, TreeSet<String>> diffsAreas,
            String name,
            String YM30,
            int num,
            File dir,
            String YM31) {
        if (diffsAreas.size() > 0) {
            PrintWriter pw;
            String type;
            type = "Increases";
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "ExtremeAreaChanges" + name + type + YM30 + "_TO_" + YM31 + ".csv");
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
                    "ExtremeAreaChanges" + name + type + YM30 + "_TO_" + YM31 + ".csv");
            iteD = diffsAreas.keySet().iterator();
            writeDiffs(
                    diffsAreas,
                    num,
                    type,
                    pw,
                    iteD);
        }
    }

    protected void writeDiffs(
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

    protected TreeMap<String, Integer> getAreaCounts(
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

    protected TreeMap<Double, TreeSet<String>> getCountAreas(
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
     * @param YM3
     */
    protected void writeAreasWithHighestNumbersOfClaimants(
            TreeMap<Integer, TreeSet<String>> countAreas,
            int num,
            File dir,
            String YM3) {
        if (countAreas.size() > 0) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "HighestClaimants" + YM3 + ".csv");
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
     * @param YM3
     * @return {@code TreeMap<Integer, TreeSet<String>>} count by list of areas.
     * This is an ordered list from minimum to maximum counts.
     */
    protected TreeMap<Integer, TreeSet<String>> writeCountsByArea(
            TreeMap<String, Integer> areaCounts,
            String level,
            File dir,
            String YM3) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result;
            result = new TreeMap<Integer, TreeSet<String>>();
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    YM3 + ".csv");
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

    private void writeCountsByTenure(
            TreeMap<Integer, Integer> TTCounts,
            File dir,
            String YM3) {
        if (TTCounts.size() > 0) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(
                    dir,
                    "CountsByTenure" + YM3 + ".csv");
            pw.println("Tenure, Count");
            Iterator<Integer> ite;
            ite = TTCounts.keySet().iterator();
            while (ite.hasNext()) {
                Integer TT0 = ite.next();
                Integer count = TTCounts.get(TT0);
                pw.println(TT0 + ", " + count);
            }
            pw.close();
        }
    }

    private void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts,
            String areaCode,
            String claimantType,
            String TT,
            String level,
            String type,
            Integer TTInt) {
        addToAreaCount(claimantTypeTenureLevelTypeAreaCounts, areaCode, claimantType, TT, level, type);
        TreeMap<Integer, Integer> TTCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(TT).get(level).get(type);
        Generic_Collections.addToTreeMapIntegerInteger(
                TTCounts,
                TTInt,
                1);
    }

    private void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts,
            String areaCode,
            String claimantType,
            String TT,
            String level,
            String type,
            Integer TTInt,
            double distance) {
        addToAreaCount(claimantTypeTenureLevelTypeDistanceAreaCounts, areaCode, claimantType, TT, level, type, distance);
        TreeMap<Integer, Integer> tenureCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(TT).get(level).get(type).get(distance);
        Generic_Collections.addToTreeMapIntegerInteger(
                tenureCounts,
                TTInt,
                1);
    }

    private void addToAreaCount(
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

    private void addToAreaCount(
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
//            env.logO("No area counts for distance " + distance);
//            env.logO("claimantType " + claimantType);
//            env.logO("tenure " + tenure);
//            env.logO("level " + level);
//            env.logO("type " + type);
//        }
        Generic_Collections.addToTreeMapStringInteger(
                areaCounts,
                areaCode,
                1);
    }

    /**
     *
     * @param ClaimID
     * @param ClaimIDToTTLookups
     * @param YM30
     * @return true iff ClaimIDToTTLookups.get(YM30) has a ClaimID key.
     */
    public boolean hasTT(
            DW_ID ClaimID,
            HashMap<String, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            String YM30) {
        HashMap<DW_ID, Integer> ClaimIDByTTLookup;
        ClaimIDByTTLookup = ClaimIDToTTLookups.get(YM30);
        return ClaimIDByTTLookup.containsKey(ClaimID);
    }

    /**
     *
     * @param ClaimID
     * @param TTTs
     * @param YM3
     * @param TTT
     */
    public void recordTTTs(
            DW_ID ClaimID,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM3,
            String TTT) {
        ArrayList<String> TTCs;
        TTCs = TTTs.get(ClaimID);
        if (TTCs == null) {
            TTCs = new ArrayList<String>();
            TTTs.put(ClaimID, TTCs);
        }
        TTCs.add(TTT + ":" + YM3);
    }

    private String getTTTName(
            Integer TT0,
            Integer TT1) {
        return getTTTName(
                TT0.toString(),
                TT1.toString());
    }

    /**
     *
     * @param TT0
     * @param TT1
     * @return
     */
    public String getTTTName(
            String TT0,
            String TT1) {
        String result;
        result = TT0 + " - " + TT1;
        return result;
    }

    private String[] getTTTName(
            Integer TT0,
            boolean isU0,
            Integer TT1,
            boolean isU1) {
        return getTTTName(
                TT0.toString(),
                isU0,
                TT1.toString(),
                isU1);
    }

    /**
     *
     * @param TT0
     * @param isU0
     * @param TT1
     * @param isU1
     * @return String[] result: result[0] is the name for the TenancyType
     * Transition. If isU0 or isU1 then sU is appended to TT0 and/or TT1 as
     * appropriate and these are returned in result[1] and result[2]
     * respectively.
     */
    public String[] getTTTName(
            String TT0,
            boolean isU0,
            String TT1,
            boolean isU1) {
        String[] result;
        result = new String[3];
        String s0;
        s0 = TT0;
        if (isU0) {
            s0 += sU;
        }
        String s1;
        s1 = TT1;
        if (isU1) {
            s1 += sU;
        }
        result[0] = s0 + " - " + s1;
        result[1] = s0;
        result[2] = s1;
        return result;
    }

//    /**
//     *
//     * @param ClaimIDToTTLookup0 Before
//     * @param ClaimIDToTTLookup1 Now
//     * @param ClaimIDToTTLookups
//     * @param YM30
//     * @return A count matrix of TT changes.
//     * }
//     */
//    public Object[] getMultipleTTTMatrix(
//            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
//            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
//            HashMap<String, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
//            String YM30) {
//        Object[] result;
//        result = new Object[2];
//        TreeMap<Integer, TreeMap<Integer, Integer>> TTTMatrix;
//        TTTMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
//        result[0] = TTTMatrix;
//        HashMap<DW_ID, String> TTT;
//        TTT = new HashMap<DW_ID, String>();
//        result[1] = TTT;
//        Iterator<DW_ID> ite;
//        // Go through all current claims
//        ite = ClaimIDToTTLookup1.keySet().iterator();
//        while (ite.hasNext()) {
//            DW_ID ClaimID;
//            ClaimID = ite.next();
//            boolean hasTT0;
//            hasTT0 = hasTT(
//                    ClaimID,
//                    ClaimIDToTTLookups,
//                    YM30);
//            Integer TT1 = ClaimIDToTTLookup1.get(ClaimID);
//            Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
//            if (TT0 == null) {
//                TT0 = -999;
//            }
//            if (hasTT0) {
//                if (TTTMatrix.containsKey(TT1)) {
//                    TreeMap<Integer, Integer> TTCount;
//                    TTCount = TTTMatrix.get(TT1);
//                    Generic_Collections.addToTreeMapIntegerInteger(TTCount, TT0, 1);
//                } else {
//                    TreeMap<Integer, Integer> TTCount;
//                    TTCount = new TreeMap<Integer, Integer>();
//                    TTCount.put(TT0, 1);
//                    TTTMatrix.put(TT1, TTCount);
//                }
//            }
//            if (TT0.compareTo(TT1) != 0) {
//                TTT.put(
//                        ClaimID,
//                        "" + TT0 + " - " + TT1);
//            }
//        }
//        ite = ClaimIDToTTLookup0.keySet().iterator();
//        while (ite.hasNext()) {
//            DW_ID ClaimID;
//            ClaimID = ite.next();
//            boolean hasTT0;
//            hasTT0 = hasTT(
//                    ClaimID,
//                    ClaimIDToTTLookups,
//                    YM30);
//            Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
//            if (TT0 == null) {
//                TT0 = -999;
//            }
//            Integer TT1;
//            TT1 = -999;
//            if (hasTT0) {
//                if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
//                    if (TTTMatrix.containsKey(TT1)) {
//                        TreeMap<Integer, Integer> TTCount;
//                        TTCount = TTTMatrix.get(TT1);
//                        Generic_Collections.addToTreeMapIntegerInteger(TTCount, TT0, 1);
//                    } else {
//                        TreeMap<Integer, Integer> TTCount;
//                        TTCount = new TreeMap<Integer, Integer>();
//                        TTCount.put(TT0, 1);
//                        TTTMatrix.put(TT1, TTCount);
//                    }
//                }
//            }
////            if (!TT0.equalsIgnoreCase(TT1)) {
////                tIDTenureChange.put(
////                        tID,
////                        "" + TT0 + " - " + TT1);
////            }
//        }
//        return result;
//    }
    /**
     *
     * @param ClaimIDToPostcodeIDLookups
     * @param indexYM3s
     * @param ClaimID
     * @param ClaimIDToTTLookups
     * @param UOClaimIDSets
     * @param CouncilUOClaimIDSets
     * @param RSLUOClaimIDSets
     * @param i
     * @param include
     * @return Previous TenanctType and Postcode
     */
    public Object[] getPreviousTTAndPU(
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            HashMap<Integer, String> indexYM3s,
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> CouncilUOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> RSLUOClaimIDSets,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[4];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        String YM3;
        YM3 = indexYM3s.get(index);

        DW_ID PostcodeID;
        PostcodeID = null;
        String Postcode;
        Postcode = sAAN_NAA;

        HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup;

//        env.logO("i " + i);
//        env.logO("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer TT = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
            YM3 = indexYM3s.get(previousIndex);
            result[3] = YM3;
            ClaimIDToPostcodeIDLookup = ClaimIDToPostcodeIDLookups.get(previousIndex);
            //env.logO("previousIndex " + previousIndex);
            HashMap<DW_ID, Integer> ClaimIDToTTLookup;
            ClaimIDToTTLookup = ClaimIDToTTLookups.get(previousIndex);
            if (ClaimIDToTTLookup != null) {
                TT = ClaimIDToTTLookup.get(ClaimID);
                Set<DW_ID> UOClaimIDSet;
                UOClaimIDSet = UOClaimIDSets.get(previousIndex);
                if (UOClaimIDSet != null) {
                    if (UOClaimIDSet.contains(ClaimID)) {
                        underOccupancy = sU;
                        if (TT == null) {
                            TT = -999;
                            Set<DW_ID> CouncilUOClaimIDSet;
                            CouncilUOClaimIDSet = CouncilUOClaimIDSets.get(previousIndex);
                            if (CouncilUOClaimIDSet.contains(ClaimID)) {
                                result[0] = Integer.toString(TT) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(TT) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            PostcodeID = ClaimIDToPostcodeIDLookup.get(ClaimID);
                            if (PostcodeID == null) {
                                Postcode = sAAN_NAA;
                            } else {
                                Postcode = PostcodeIDToPostcodeLookup.get(PostcodeID);
                            }
                            result[2] = Postcode;
                            return result;
                        } else if (TT == -999) {
                            Set<DW_ID> RSLUOClaimIDSet;
                            RSLUOClaimIDSet = RSLUOClaimIDSets.get(previousIndex);
                            if (RSLUOClaimIDSet.contains(ClaimID)) {
                                result[0] = Integer.toString(TT) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(TT) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            result[2] = sAAN_NAA;
                            return result;
                        } else {
                            result[0] = Integer.toString(TT) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            PostcodeID = ClaimIDToPostcodeIDLookup.get(ClaimID);
                            if (PostcodeID == null) {
                                Postcode = sAAN_NAA;
                            } else {
                                Postcode = PostcodeIDToPostcodeLookup.get(PostcodeID);
                            }
                            result[2] = Postcode;
                            return result;
                        }
                    }
                }
                if (TT != null) {
                    if (TT != -999) {
                        result[0] = Integer.toString(TT) + underOccupancy;
                        //result[1] = include.indexOf(previousIndex);
                        result[1] = previousIndex;
                        result[2] = sAAN_NAA;
                        return result;
                    }
                }
            }
        }
        if (TT == null) {
            TT = -999;
        }
        result[0] = Integer.toString(TT) + underOccupancy;
        result[1] = null;
        if (PostcodeID == null) {
            Postcode = sAAN_NAA;
        } else {
            Postcode = PostcodeIDToPostcodeLookup.get(PostcodeID);
        }
        result[2] = Postcode;
        result[3] = YM3;
        return result;
    }

    /**
     *
     * @param ClaimID
     * @param ClaimIDToTTLookups
     * @param UOClaimIDSets
     * @param CouncilUOClaimIDSets
     * @param RSLUOClaimIDSets
     * @param i
     * @param include
     * @return
     */
    public Object[] getPreviousTT(
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> CouncilUOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> RSLUOClaimIDSets,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer TT = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();
//            env.logO("previousIndex " + previousIndex);
            HashMap<DW_ID, Integer> ClaimIDToTTLookup;
            ClaimIDToTTLookup = ClaimIDToTTLookups.get(previousIndex);
            Set<DW_ID> UOClaimIDSet;
            UOClaimIDSet = UOClaimIDSets.get(previousIndex);
            if (ClaimIDToTTLookup != null) {
                TT = ClaimIDToTTLookup.get(ClaimID);
                if (UOClaimIDSet != null) {
                    if (UOClaimIDSet.contains(ClaimID)) {
                        underOccupancy = sU;
                        if (TT == null) {
                            TT = -999;
                            Set<DW_ID> CouncilUOClaimIDSet;
                            CouncilUOClaimIDSet = CouncilUOClaimIDSets.get(previousIndex);
                            if (CouncilUOClaimIDSet.contains(ClaimID)) {
                                result[0] = Integer.toString(TT) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(TT) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        } else if (TT == -999) {
                            Set<DW_ID> CouncilUOClaimIDSet;
                            CouncilUOClaimIDSet = CouncilUOClaimIDSets.get(previousIndex);
                            if (CouncilUOClaimIDSet.contains(ClaimID)) {
                                result[0] = Integer.toString(TT) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(TT) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        } else {
                            result[0] = Integer.toString(TT) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;
                            return result;
                        }
                    }
                }
                if (TT != null) {
                    if (TT != -999) {
                        result[0] = Integer.toString(TT) + underOccupancy;
                        //result[1] = include.indexOf(previousIndex);
                        result[1] = previousIndex;
                        return result;
                    }
                }
            }
        }
        if (TT == null) {
            TT = -999;
        }
        result[0] = Integer.toString(TT) + underOccupancy;
        result[1] = null;
        return result;
    }

    /**
     *
     * @param ClaimID
     * @param ClaimIDToTTLookups
     * @param i
     * @param include
     * @param UOClaimIDSets
     * @return
     */
    public Object[] getPreviousTT(
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[2];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer TT = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex;
            previousIndex = li.previous();

            //System.out.println("previousIndex " + previousIndex);
            HashMap<DW_ID, Integer> ClaimIDToTTLookup;
            ClaimIDToTTLookup = ClaimIDToTTLookups.get(previousIndex);
            Set<DW_ID> UOClaimIDSet;
            UOClaimIDSet = UOClaimIDSets.get(previousIndex);
            if (ClaimIDToTTLookup != null) {
                TT = ClaimIDToTTLookup.get(ClaimID);
                if (UOClaimIDSet != null) {
                    if (UOClaimIDSet.contains(ClaimID)) {
                        underOccupancy = sU;
                    }
                }
                if (TT != null) {
                    if (TT != -999) {
                        result[0] = Integer.toString(TT) + underOccupancy;
                        //result[1] = include.indexOf(previousIndex);
                        result[1] = previousIndex;
                        return result;
                    }
                }
            }
        }
        if (TT == null) {
            TT = -999;
        }
        result[0] = Integer.toString(TT) + underOccupancy;
        result[1] = null;
        return result;
    }

    /**
     *
     * @param ClaimIDToPostcodeIDLookups
     * @param Records0
     * @param Records1
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToTTLookups
     * @param UOClaimIDSets
     * @param CouncilUOClaimIDSets
     * @param RSLUOClaimIDSets
     * @param TTTs
     * @param YM30
     * @param YM31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param indexYM3s
     * @param DW_UO_SetAll0
     * @param DW_UO_SetCouncil0
     * @param DW_UO_SetRSL0
     * @param DW_UO_SetAll1
     * @param DW_UO_SetCouncil1
     * @param DW_UO_SetRSL1
     * @param ClaimIDs
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count
     * }
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixAndRecordTTTU(
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            HashMap<DW_ID, DW_SHBE_Record> Records0,
            HashMap<DW_ID, DW_SHBE_Record> Records1,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> UOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> CouncilUOClaimIDSets,
            HashMap<Integer, Set<DW_ID>> RSLUOClaimIDSets,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            HashMap<Integer, String> indexYM3s,
            DW_UO_Set DW_UO_SetAll0,
            DW_UO_Set DW_UO_SetCouncil0,
            DW_UO_Set DW_UO_SetRSL0,
            DW_UO_Set DW_UO_SetAll1,
            DW_UO_Set DW_UO_SetCouncil1,
            DW_UO_Set DW_UO_SetRSL1,
            Set<DW_ID> ClaimIDs
    ) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        if (ClaimIDs == null) {
            ClaimIDs = new HashSet<DW_ID>();
            ClaimIDs.addAll(DW_UO_SetAll0.getClaimIDs());
            ClaimIDs.addAll(DW_UO_SetAll1.getClaimIDs());
        }
        DW_ID ClaimID;
        DW_SHBE_Record Record0;
        DW_SHBE_Record Record1;
        DW_SHBE_D_Record DRecord0;
        DW_SHBE_D_Record DRecord1;
        DW_UO_Record DW_UO_Record0;
        DW_UO_Record DW_UO_Record1;
        Iterator<DW_ID> ite;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            Record0 = Records0.get(ClaimID);
            Record1 = Records1.get(ClaimID);
            String sTT0;
            if (Record0 != null) {
                DRecord0 = Record0.getDRecord();
                sTT0 = Integer.toString(DRecord0.getTenancyType());
            } else {
                DRecord0 = null;
                sTT0 = sMinus999;
            }
            String sTT1;
            if (Record1 != null) {
                DRecord1 = Record1.getDRecord();
                sTT1 = Integer.toString(DRecord1.getTenancyType());
            } else {
                DRecord1 = null;
                sTT1 = sMinus999;
            }
            DW_UO_Record0 = DW_UO_SetAll0.getMap().get(ClaimID);
            DW_UO_Record1 = DW_UO_SetAll1.getMap().get(ClaimID);
            String pc0 = sAAN_NAA;
            String pc1 = sAAN_NAA;
            if (checkPreviousTenure) {
                if (sTT0.equalsIgnoreCase(sMinus999)) {
                    if (DW_UO_Record0 != null) {
                        sTT0 += sU;
                        if (DW_UO_SetCouncil0.getMap().containsKey(ClaimID)) {
                            sTT0 += "1";
                        }
                        if (DW_UO_SetRSL0.getMap().containsKey(ClaimID)) {
                            sTT0 += "4";
                        }
                        if (DRecord0 != null) {
                            pc0 = DRecord0.getClaimantsPostcode();
                        } else {
                            pc0 = sAAN_NAA;
                        }
                    } else {
                        Object[] PreviousTTAndP;
                        PreviousTTAndP = getPreviousTTAndPU(
                                ClaimIDToPostcodeIDLookups,
                                indexYM3s,
                                ClaimID,
                                ClaimIDToTTLookups,
                                UOClaimIDSets,
                                CouncilUOClaimIDSets,
                                RSLUOClaimIDSets,
                                index,
                                include);
                        sTT0 = (String) PreviousTTAndP[0];
                        pc0 = (String) PreviousTTAndP[2];
                        YM30 = (String) PreviousTTAndP[3];
                    }
                } else {
                    //underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                    if (DW_UO_Record0 != null) {
                        sTT0 += sU;
                    }
                    if (DRecord0 != null) {
                        pc0 = DRecord0.getClaimantsPostcode();
//                        String pf = Record0.getClaimPostcodeF();
//                        DW_ID PostcodeID = PostcodeToPostcodeIDLookup.get(pf);
//                        ClaimIDToPostcodeIDLookups.get(index).put(ClaimID, PostcodeID);
                    } else {
                        pc0 = sAAN_NAA;
                    }
                }
                if (sTT1.equalsIgnoreCase(sMinus999)) {
                    if (DW_UO_Record1 != null) {
                        sTT1 += sU;
                        if (DW_UO_SetCouncil1.getMap().containsKey(ClaimID)) {
                            sTT1 += "1";
                        }
                        if (DW_UO_SetRSL1.getMap().containsKey(ClaimID)) {
                            sTT1 += "4";
                        }
                        if (DRecord1 != null) {
                            pc1 = DRecord1.getClaimantsPostcode();
                            String pf = Record0.getClaimPostcodeF();
                            DW_ID PostcodeID = PostcodeToPostcodeIDLookup.get(pf);
                            ClaimIDToPostcodeIDLookups.get(index).put(ClaimID, PostcodeID);
                        } else {
                            pc1 = sAAN_NAA;
                        }
                    } else {
                        Object[] previousTTAndP;
                        previousTTAndP = getPreviousTTAndPU(
                                ClaimIDToPostcodeIDLookups,
                                indexYM3s,
                                ClaimID,
                                ClaimIDToTTLookups,
                                UOClaimIDSets,
                                CouncilUOClaimIDSets,
                                RSLUOClaimIDSets,
                                index,
                                include);
                        sTT1 = (String) previousTTAndP[0];
                        pc1 = (String) previousTTAndP[2];
                        YM31 = (String) previousTTAndP[3];
                    }
                } else {
                    //underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    if (DW_UO_Record1 != null) {
                        sTT1 += sU;
                    }
                    if (DRecord1 != null) {
                        pc1 = DRecord1.getClaimantsPostcode();
                    } else {
                        pc1 = sAAN_NAA;
                    }
                }
            }
            if (checkPreviousTenure) {
                if (sTT1.equalsIgnoreCase(sMinus999)) {
                    Object[] previousTenure;
                    previousTenure = getPreviousTT(
                            ClaimID,
                            ClaimIDToTTLookups,
                            UOClaimIDSets,
                            CouncilUOClaimIDSets,
                            RSLUOClaimIDSets,
                            index,
                            include);
                    sTT1 = (String) previousTenure[0];
                }
            }
            if (!checkPreviousTenure) {
                if (DW_UO_Record0 != null) {
                    sTT0 += sU;
                    if (sTT0.startsWith(sMinus999)) {
                        if (DW_UO_SetCouncil0.getMap().containsKey(ClaimID)) {
                            sTT0 += "1";
                        }
                        if (DW_UO_SetRSL0.getMap().containsKey(ClaimID)) {
                            sTT0 += "4";
                        }
                    }
                }
                if (DW_UO_Record1 != null) {
                    sTT1 += sU;
                    if (sTT1.startsWith(sMinus999)) {
                        if (DW_UO_SetCouncil1.getMap().containsKey(ClaimID)) {
                            sTT1 += "1";
                        }
                        if (DW_UO_SetRSL1.getMap().containsKey(ClaimID)) {
                            sTT1 += "4";
                        }
                    }
                }
            }
            if (!sTT0.equalsIgnoreCase(sTT1)) {
                String[] TTTDetails;
                TTTDetails = getTTTDetails(
                        sTT0,
                        Record0,
                        sTT1,
                        Record1);
                recordTTTs(
                        ClaimID,
                        TTTs,
                        YM31,
                        TTTDetails[0]);
            } else if (pc0 != null && pc1 != null) {
                if (!pc0.equalsIgnoreCase(pc1)) {
                    if (Record0 != null && Record1 != null) {
                        if (Record0.isClaimPostcodeFMappable()
                                && Record1.isClaimPostcodeFMappable()) {
                            String TTT = sTT0 + " - " + sTT1;
                            TTT += DW_Strings.sPostcodeChanged;
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTT);
                        }
                    }
                }
            }
            if (result.containsKey(sTT1)) {
                TreeMap<String, Integer> TTCount;
                TTCount = result.get(sTT1);
                Generic_Collections.addToTreeMapStringInteger(
                        TTCount,
                        sTT0,
                        1);
            } else {
                TreeMap<String, Integer> TTCount;
                TTCount = new TreeMap<String, Integer>();
                TTCount.put(sTT0, 1);
                result.put(sTT1, TTCount);
            }
        }

        // Go through current
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            boolean doMainLoop = true;
            ClaimID = ite.next();
            if (ClaimIDs.contains(ClaimID)) {
                // UnderOccupancy
                DW_UO_Record0 = null;
                DW_UO_Record1 = null;
                if (DW_UO_SetAll0 != null) {
                    DW_UO_Record0 = DW_UO_SetAll0.getMap().get(ClaimID);
                }
                if (DW_UO_SetAll1 != null) {
                    DW_UO_Record1 = DW_UO_SetAll1.getMap().get(ClaimID);
                }
                doMainLoop = DW_UO_Record0 != null || DW_UO_Record1 != null;
                if (doMainLoop) {
                    Integer TT0;
                    TT0 = ClaimIDToTTLookup0.get(ClaimID);
                    String sTT0;
                    if (TT0 == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    UOClaimIDSets,
                                    index,
                                    include);
                            sTT0 = (String) previousTenure[0];
                        } else {
                            sTT0 = sMinus999;
                        }
                    } else if (TT0 == -999) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    UOClaimIDSets,
                                    index,
                                    include);
                            sTT0 = (String) previousTenure[0];
                        } else {
                            sTT0 = sMinus999;
                        }
                    } else {
                        sTT0 = Integer.toString(TT0);
                    }
                    Integer TT1Integer;
                    TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                    String TT1 = Integer.toString(TT1Integer);
                    if (!checkPreviousTenure) {
                        if (DW_UO_Record0 != null) {
                            sTT0 += sU;
                        }
                    }
                    if (DW_UO_Record1 != null) {
                        TT1 += sU;
                    }
                    if (!sTT0.equalsIgnoreCase(TT1)) {
                        String[] TTTDetails;
//                    TTTDetails = getTenancyTypeChangeDetails(
//                            doUnderOccupiedData,
//                            underOccupied0,
//                            underOccupied1,
//                            TT0Integer,
//                            TT1Integer);
                        TTTDetails = getTTTDetails(
                                sTT0,
                                TT1);
//                    TT0 = TTTDetails[1];
//                    TT1 = TTTDetails[2];

                        if (!sTT0.equalsIgnoreCase(TTTDetails[1])) {
                            int debug = 1;
                        }

                        if (!TT1.equalsIgnoreCase(TTTDetails[2])) {
                            int debug = 1;
                        }

                        boolean doRecord = true;
                        if (!sTT0.endsWith(sU)) {
//                            env.logO(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            env.logO(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = underOccupiedInApril2013.contains(CTBRef);
//                            env.logO(test);
                            if (!TTTs.containsKey(ClaimID)) {
                                //int debug = 1;
                                // This only happens in cases where there is a -999U initial case!
                                if (ClaimIDs.contains(ClaimID)) {
                                    sTT0 += sU;
                                    TTTDetails[0] = sTT0 + " - " + TT1;
                                    if (sTT0.equalsIgnoreCase(TT1)) {
                                        doRecord = false;
                                    }
                                } else {
                                    int debug = 1;
                                }
                            } else if (sTT0.equalsIgnoreCase(sMinus999)) {
                                if (ClaimIDs.contains(ClaimID)) {
                                    //env.logO(CTBRef);
                                } else {
                                    int debug = 1;
                                    env.logO(ClaimID.toString(), true);
                                }
                            }
                        }
                        if (doRecord) {
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTTDetails[0]);
                        }
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount,
                                sTT0,
                                1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(sTT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        // Go through previous for those records not in current
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            ClaimID = ite.next();
            if (ClaimIDs.contains(ClaimID)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UO_Record0 = null;
                DW_UO_Record1 = null;
                if (DW_UO_SetAll0 != null) {
                    if (ClaimIDs != null) {
                        if (ClaimIDs.contains(ClaimID)) {
                            DW_UO_Record0 = DW_UO_SetAll0.getMap().get(ClaimID);
                        }
                    } else {
                        DW_UO_Record0 = DW_UO_SetAll0.getMap().get(ClaimID);
                    }
                }
                if (DW_UO_SetAll1 != null) {
                    if (ClaimIDs != null) {
                        if (ClaimIDs.contains(ClaimID)) {
                            DW_UO_Record1 = DW_UO_SetAll1.getMap().get(ClaimID);
                        }
                    } else {
                        DW_UO_Record1 = DW_UO_SetAll1.getMap().get(ClaimID);
                    }
                }
                doMainLoop = DW_UO_Record0 != null || DW_UO_Record1 != null;
                if (doMainLoop) {
                    Integer TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    String TT0 = Integer.toString(TT0Integer);
                    Integer TT1Integer = -999;
                    String TT1;
                    TT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (DW_UO_Record0 != null) {
                        TT0 += sU;
                    }
                    if (DW_UO_Record1 != null) {
                        TT1 += sU;
                    }
//                    if (!TT0.equalsIgnoreCase(TT1)) {
//                        //if (TT0Integer.compareTo(TT1Integer) != 0) {
//                        String[] TTTDetails;
//                        TTTDetails = getTenancyTypeChangeDetails(
//                                doUnderOccupiedData,
//                                underOccupied0,
//                                underOccupied1,
//                                TT0Integer,
//                                TT1Integer);
//                        TT0 = TTTDetails[1];
//                        TT1 = TTTDetails[2];
//
//                        if (!TT0.endsWith(sU)) {
//                            int debug = 1;
//                        }
//
//                        recordTTTs(
//                                //tID0,
//                                CTBRef,
//                                TTTs,
//                                YM31,
//                                TTTDetails[0]);
                    if (!TT0.equalsIgnoreCase(TT1)) {
                        String[] TTTDetails;
                        TTTDetails = getTTTDetails(
                                true,
                                DW_UO_Record0,
                                DW_UO_Record1,
                                TT0Integer,
                                TT1Integer);
                        TT0 = TTTDetails[1];
                        TT1 = TTTDetails[2];
                        if (!TT0.equalsIgnoreCase(TTTDetails[1])) {
                            int debug = 1;
                        }

                        if (!TT1.equalsIgnoreCase(TTTDetails[2])) {
                            int debug = 1;
                        }
                        boolean doRecord = true;
                        if (!TT0.endsWith(sU)) {
//                            env.logO(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            env.logO(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = tCTBRefs.contains(CTBRef);
//                            env.logO(test);
                            if (!TTTs.containsKey(ClaimID)) {
                                //int debug = 1;
                                // This only happens in cases where there is a -999U initial case!
                                if (ClaimIDs.contains(ClaimID)) {
                                    TT0 += sU;
                                    TTTDetails[0] = TT0 + " - " + TT1;
                                    if (TT0.equalsIgnoreCase(TT1)) {
                                        doRecord = false;
                                    }
                                } else {
                                    int debug = 1;
                                }
                            }
                        }
                        if (doRecord) {
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTTDetails[0]);
                        }
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount,
                                TT0,
                                1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
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
     * Get TenancyType Transition Matrix and Write Postcode Transition details.
     *
     * @param dirOut
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToPostcodeIDLookup0
     * @param tUnderOccupancy0
     * @param tUnderOccupancy1
     * @param tUnderOccupancies
     * @param tUnderOccupanciesCouncil
     * @param ClaimIDToPostcodeIDLookup1
     * @param postcodeChange
     * @param TTTs Passed in to be modified.
     * @param tUnderOccupanciesRSL
     * @param YM30
     * @param YM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param ClaimIDToTTLookups
     * @param checkPreviousPostcode
     * @param ClaimIDToPostcodeIDLookups
     * @param underOccupiedSet0
     * @param underOccupiedSet1
     * @param underOccupiedInApril2013
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixAndWritePTDetailsU(
            File dirOut,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            Set<DW_ID> tUnderOccupancy0,
            Set<DW_ID> tUnderOccupancy1,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<Integer, Set<DW_ID>> tUnderOccupanciesCouncil,
            HashMap<Integer, Set<DW_ID>> tUnderOccupanciesRSL,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set underOccupiedSet0,
            DW_UO_Set underOccupiedSet1,
            Set<DW_ID> underOccupiedInApril2013) {
        HashMap<Integer, String> indexYM3s;
        indexYM3s = DW_SHBE_Handler.getIndexYM3s();
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_ID> ite;
        // Go through current Tenancy Type
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (underOccupiedSet0 != null) {
                if (underOccupiedInApril2013 == null) {
                    underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                } else if (underOccupiedInApril2013.contains(ClaimID)) {
                    underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                }
            }
            if (underOccupiedSet1 != null) {
                if (underOccupiedInApril2013 == null) {
                    underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                } else if (underOccupiedInApril2013.contains(ClaimID)) {
                    underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                }
            }
            doMainLoop = underOccupied0 != null || underOccupied1 != null;
            if (doMainLoop) {
                Integer TT0Integer = DW_SHBE_TenancyType_Handler.iMinus999;
                String TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                DW_ID postcode0 = null;
                if (ClaimIDToTTLookup1 != null) {
                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    if (TT0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTTAndP;
                            previousTTAndP = getPreviousTTAndPU(
                                    ClaimIDToPostcodeIDLookups,
                                    indexYM3s,
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    tUnderOccupancies,
                                    tUnderOccupanciesCouncil,
                                    tUnderOccupanciesRSL,
                                    i,
                                    include);
                            TT0 = (String) previousTTAndP[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTTAndP[1];
                            if (indexOfLastKnownTenureOrNot != null) {
//                                  env.logO("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                if (checkPreviousPostcode) {
                                    postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                }
                            }
                        } else {
                            TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                            if (underOccupied0 != null) {
                                TT0 += sU;
                            }
                        }
                    }
                } else {
                    TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (underOccupied0 != null) {
                        TT0 += sU;
                    }
                }
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                DW_ID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equals(postcode1);
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    if (TT0Integer.compareTo(TT1Integer) != 0) {
                        String[] TTTDetails;
                        TTTDetails = getTTTDetails(
                                true,
                                underOccupied0,
                                underOccupied1,
                                TT0Integer,
                                TT1Integer);
//                        TTTDetails = getTTTDetails(
//                                TT0,
//                                TT1);
                        TT0 = TTTDetails[1];
                        TT1 = TTTDetails[2];
                        recordTTTs(
                                ClaimID,
                                TTTs,
                                YM31,
                                TTTDetails[0]);
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(
                                    ClaimID,
                                    YM30,
                                    YM31,
                                    TTTDetails[0],
                                    postcode0,
                                    postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                TTCount, TT0, 1);
//                    } else {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = new TreeMap<String, Integer>();
//                        TTCount.put(TT0, 1);
//                        result.put(TT1, TTCount);
//                    }
//                }
            }
        }
        // Go through previous for those records not in current
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (underOccupiedSet0 != null) {
                underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
            }
            doMainLoop = underOccupied0 != null;
            if (underOccupiedSet1 != null) {
                underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
            }
            if (doMainLoop) {
                Integer TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT0 = Integer.toString(TT0Integer);
                Integer TT1Integer = -999;
                String TT1;
                TT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                DW_ID postcode0;
                postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                //if (!TT0.equalsIgnoreCase(TT1)) { // Always the case
                String[] TTTDetails;
                //                    TTTDetails = getTenancyTypeChangeDetails(
//                            doUnderOccupiedData,
//                            underOccupied0,
//                            underOccupied1,
//                            TT0Integer,
//                            TT1Integer);
                TTTDetails = getTTTDetails(
                        TT0,
                        TT1);
                TT0 = TTTDetails[1];
                TT1 = TTTDetails[2];
                recordTTTs(
                        ClaimID,
                        TTTs,
                        YM31,
                        TTTDetails[0]);
                //}
                if (result.containsKey(TT1)) {
                    TreeMap<String, Integer> TTCount;
                    TTCount = result.get(TT1);
                    Generic_Collections.addToTreeMapStringInteger(
                            TTCount, TT0, 1);
                } else {
                    TreeMap<String, Integer> TTCount;
                    TTCount = new TreeMap<String, Integer>();
                    TTCount.put(TT0, 1);
                    result.put(TT1, TTCount);
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode,
                        DW_Strings.sGroupedNo);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    private String[] getTTTDetails(
            boolean doUnderOccupied,
            DW_UO_Record DW_UO_Record0,
            DW_UO_Record DW_UO_Record1,
            Integer TT0Integer,
            Integer TT1Integer) {
        String[] result;
        result = new String[3];
        if (doUnderOccupied) {
            String[] ttc = getTTTName(
                    TT0Integer,
                    DW_UO_Record0 != null,
                    TT1Integer,
                    DW_UO_Record1 != null);
            result[0] = ttc[0];
            result[1] = ttc[1];
            result[2] = ttc[2];
        } else {
            result[0] = getTTTName(
                    TT0Integer,
                    TT1Integer);
            result[1] = Integer.toString(TT0Integer);
            result[2] = Integer.toString(TT1Integer);
        }
        return result;
    }

    /**
     * Gets the TenancyType Transition Details
     *
     * @param sTT0
     * @param sTT1
     * @return
     */
    private String[] getTTTDetails(
            String sTT0,
            String sTT1) {
        String[] result;
        result = new String[3];
        result[0] = sTT0 + " - " + sTT1;
        result[1] = sTT0;
        result[2] = sTT1;
        return result;
    }

    private String[] getTTTDetails(
            String sTT0,
            DW_SHBE_Record Record0,
            String sTT1,
            DW_SHBE_Record Record1) {
        String[] result;
        result = new String[3];
        DW_ID PostCodeID0 = null;
        boolean isMappablePostcodeFormat0 = false;
        if (Record0 != null) {
            PostCodeID0 = Record0.getPostcodeID();
            isMappablePostcodeFormat0 = Record0.isClaimPostcodeFMappable();
        }
        DW_ID PostCodeID1 = null;
        boolean isMappablePostcodeFormat1 = false;
        if (Record1 != null) {
            PostCodeID1 = Record1.getPostcodeID();
            isMappablePostcodeFormat1 = Record1.isClaimPostcodeFMappable();
        }
        result[0] = sTT0 + " - " + sTT1;
        if (PostCodeID0 != null && PostCodeID1 != null) {
            if (!PostCodeID0.equals(PostCodeID1)) {
                if (isMappablePostcodeFormat0 && isMappablePostcodeFormat1) {
                    result[0] += "P";
                }
            }
        }
        result[1] = sTT0;
        result[2] = sTT1;
        return result;
    }

    public String[] getPTName(
            DW_ID ClaimID,
            String YM30,
            String YM31,
            String TTChange,
            DW_ID PostcodeID0,
            DW_ID PostcodeID1) {
        String[] result;
        result = new String[6];
        result[0] = ClaimID.toString();
        result[1] = YM30;
        result[2] = YM31;
        result[3] = TTChange;
        result[4] = env.getDW_SHBE_Data().getPostcodeIDToPostcodeLookup().get(PostcodeID0);
        result[5] = env.getDW_SHBE_Data().getPostcodeIDToPostcodeLookup().get(PostcodeID1);
        return result;
    }

    /**
     * Gets Postcode Transition Counts where there is no TenancyType Transition.
     *
     * @param dirOut
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToTTLookups
     * @param tUnderOccupancyIDs0
     * @param tUnderOccupancyIDs1
     * @param TTTs
     * @param YM30
     * @param tUnderOccupancies
     * @param YM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param ClaimIDToPostcodeIDLookup0
     * @param ClaimIDToPostcodeIDLookup1
     * @param ClaimIDToPostcodeIDLookups
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedSet0
     * @param underOccupiedSet1
     * @param tCTBRefs
     * @return A count of changes in matrix form (only entries for the same
     * Tenancy Types and for -999 are in the matrix).
     */
    public TreeMap<String, TreeMap<String, Integer>> getPTCountsNoTTTU(
            File dirOut,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            Set<DW_ID> tUnderOccupancyIDs0,
            Set<DW_ID> tUnderOccupancyIDs1,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set underOccupiedSet0,
            DW_UO_Set underOccupiedSet1,
            Set<DW_ID> tCTBRefs
    ) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_ID> ite;
        // Go through current claimants
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (underOccupiedSet0 != null) {
                if (tCTBRefs == null) {
                    underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                } else if (tCTBRefs.contains(ClaimID)) {
                    underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                }
            }
            if (underOccupiedSet1 != null) {
                if (tCTBRefs == null) {
                    underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                } else if (tCTBRefs.contains(ClaimID)) {
                    underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                }
            }
            doMainLoop = underOccupied0 != null || underOccupied1 != null;
            if (doMainLoop) {
                Integer TT0Integer = -999;
                String TT0;
                DW_ID postcode0 = null;
                if (ClaimIDToTTLookup1 != null) {
                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    if (TT0Integer != null) {
                        TT0 = Integer.toString(TT0Integer);
                        boolean isValidPostcodeFormPostcode0 = false;
                        postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                        if (TT0 == null) {
                            if (checkPreviousTenure) {
                                Object[] previousTenure;
                                previousTenure = getPreviousTT(
                                        ClaimID,
                                        ClaimIDToTTLookups,
                                        tUnderOccupancies,
                                        i,
                                        include);
                                TT0 = (String) previousTenure[0];
                                if (checkPreviousPostcode) {
                                    Integer indexOfLastKnownTenureOrNot;
                                    indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                    if (indexOfLastKnownTenureOrNot != null) {
//                                       env.logO("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                        if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                            postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                        }
                                    }
                                }
                            } else {
                                TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                                TT0 += sU;
                            }
                        }
                    } else {
                        TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                        TT0 += sU;
                    }
                } else {
                    TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                    TT0 += sU;
                }
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                DW_ID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    if (postcode0 == null) {
                        doCount = postcode1 != null;
                    } else {
                        doCount = !postcode0.equals(postcode1);
                    }
                } else if (postcode0 == null) {
                    doCount = postcode1 == null;
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    String TTChange;
                    String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                            TT0Integer,
                            underOccupied0 != null,
                            TT1Integer,
                            underOccupied1 != null);
                    TTChange = ttc[0];
                    TT0 = ttc[1];

//                            if (!TT0.endsWith(sU)) {
//                                int debug = 1;
//                            }
                    TT1 = ttc[2];
                    if (TT0.equalsIgnoreCase(TT1)) {
//                            if (!TT0.equalsIgnoreCase(TT1)) {
//                                recordTTTs(
//                                        tID,
//                                        TTTs,
//                                        YM31,
//                                        TTChange);
//                            }
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(
                                    ClaimID,
                                    YM30,
                                    YM31,
                                    TTChange,
                                    postcode0,
                                    postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                        if (result.containsKey(TT1)) {
                            TreeMap<String, Integer> TTCount;
                            TTCount = result.get(TT1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<String, Integer>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                TTCount, TT0, 1);
//                    } else {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = new TreeMap<String, Integer>();
//                        TTCount.put(TT0, 1);
//                        result.put(TT1, TTCount);
//                    }
//                }
            }
        }
        // Go through all those previously and record for all those that are not
        // in the current data. Also record for all those that were under 
        // occupying, but are now not and have changed postcode.
        ite = ClaimIDToTTLookup0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
                DW_UO_Record underOccupied0 = null;
                //DW_UnderOccupiedReport_Record underOccupied1 = null;
                if (underOccupiedSet0 != null) {
                    underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                }
                doMainLoop = underOccupied0 != null;
                if (doMainLoop) {
                    Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
                    String sTT0 = Integer.toString(TT0);
                    Integer TT1 = -999;
                    String sTT1;
                    sTT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String TTT;
                    String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                            TT0,
                            underOccupied0 != null,
                            TT1,
                            false);
                    TTT = ttc[0];
                    sTT0 = ttc[1];
                    sTT1 = ttc[2];
                    if (sTT0.equalsIgnoreCase(sTT1)) {
                        recordTTTs(
                                ClaimID,
                                TTTs,
                                YM31,
                                TTT);
                    }
                    if (result.containsKey(sTT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(sTT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, sTT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(sTT0, 1);
                        result.put(sTT1, TTCount);
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode,
                        DW_Strings.sGroupedNo);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    /**
     * Gets Postcode Transition Counts where there is no TenancyType Transition.
     *
     * @param dirOut
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToTTLookups
     * @param tUnderOccupancyIDs0
     * @param tUnderOccupancyIDs1
     * @param TTTs
     * @param YM30
     * @param tUnderOccupancies
     * @param YM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param ClaimIDToPostcodeIDLookup0
     * @param ClaimIDToPostcodeIDLookup1
     * @param ClaimIDToPostcodeIDLookups
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedSet0
     * @param underOccupiedSet1
     * @param doUnderOccupiedData
     * @param tCTBRefs
     * @return A count of changes in matrix form (only entries for the same
     * Tenancy Types and for -999 are in the matrix).
     */
    public TreeMap<String, TreeMap<String, Integer>> getPTCountsNoTTT(
            File dirOut,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            Set<DW_ID> tUnderOccupancyIDs0,
            Set<DW_ID> tUnderOccupancyIDs1,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set underOccupiedSet0,
            DW_UO_Set underOccupiedSet1,
            boolean doUnderOccupiedData,
            Set<DW_ID> tCTBRefs
    ) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_ID> ite;
        // Go through current claimants
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (tCTBRefs == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    } else if (tCTBRefs.contains(ClaimID)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (tCTBRefs == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                    } else if (tCTBRefs.contains(ClaimID)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer TT0Integer = -999;
                String TT0;
                DW_ID postcode0 = null;
                if (ClaimIDToTTLookup1 != null) {
                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    if (TT0Integer != null) {
                        TT0 = Integer.toString(TT0Integer);
                        boolean isValidPostcodeFormPostcode0 = false;
                        postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                        if (TT0 == null) {
                            if (checkPreviousTenure) {
                                Object[] previousTenure;
                                previousTenure = getPreviousTT(
                                        ClaimID,
                                        ClaimIDToTTLookups,
                                        tUnderOccupancies,
                                        i,
                                        include);
                                TT0 = (String) previousTenure[0];
                                if (checkPreviousPostcode) {
                                    Integer indexOfLastKnownTenureOrNot;
                                    indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                    if (indexOfLastKnownTenureOrNot != null) {
//                                       env.logO("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                        if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                            postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                        }
                                    }
                                }
                            } else {
                                TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                                if (doUnderOccupiedData) {
                                    TT0 += sU;
                                }
                            }
                        }
                    } else {
                        TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                        if (doUnderOccupiedData) {
                            TT0 += sU;
                        }
                    }
                } else {
                    TT0 = DW_SHBE_TenancyType_Handler.sMinus999;
                    if (doUnderOccupiedData) {
                        TT0 += sU;
                    }
                }
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                DW_ID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    if (postcode0 == null) {
                        doCount = postcode1 != null;
                    } else {
                        doCount = !postcode0.equals(postcode1);
                    }
                } else if (postcode0 == null) {
                    doCount = postcode1 == null;
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    String TTChange;
                    if (doUnderOccupiedData) {
                        String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                TT0Integer,
                                underOccupied0 != null,
                                TT1Integer,
                                underOccupied1 != null);
                        TTChange = ttc[0];
                        TT0 = ttc[1];

//                            if (!TT0.endsWith(sU)) {
//                                int debug = 1;
//                            }
                        TT1 = ttc[2];
                        if (TT0.equalsIgnoreCase(TT1)) {
//                            if (!TT0.equalsIgnoreCase(TT1)) {
//                                recordTTTs(
//                                        tID,
//                                        TTTs,
//                                        YM31,
//                                        TTChange);
//                            }
                            if (postcodeChange) {
                                String[] postcodeChangeResult;
                                postcodeChangeResult = getPTName(
                                        ClaimID,
                                        YM30,
                                        YM31,
                                        TTChange,
                                        postcode0,
                                        postcode1);
                                postcodeChanges.add(postcodeChangeResult);
                            }
                            if (result.containsKey(TT1)) {
                                TreeMap<String, Integer> TTCount;
                                TTCount = result.get(TT1);
                                Generic_Collections.addToTreeMapStringInteger(
                                        TTCount, TT0, 1);
                            } else {
                                TreeMap<String, Integer> TTCount;
                                TTCount = new TreeMap<String, Integer>();
                                TTCount.put(TT0, 1);
                                result.put(TT1, TTCount);
                            }
                        }
                    } else if (TT0Integer.compareTo(TT1Integer) == 0
                            || TT0.equalsIgnoreCase(DW_SHBE_TenancyType_Handler.sMinus999)) { // Major diff
                        TTChange = getTTTName(
                                TT0Integer,
                                TT1Integer);
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(
                                    ClaimID,
                                    YM30,
                                    YM31,
                                    TTChange,
                                    postcode0,
                                    postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                        if (result.containsKey(TT1)) {
                            TreeMap<String, Integer> TTCount;
                            TTCount = result.get(TT1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<String, Integer>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                TTCount, TT0, 1);
//                    } else {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = new TreeMap<String, Integer>();
//                        TTCount.put(TT0, 1);
//                        result.put(TT1, TTCount);
//                    }
//                }
            }
        }
        // Go through all those previously and record for all those that are not
        // in the current data. Also record for all those that were under 
        // occupying, but are now not and have changed postcode.
        ite = ClaimIDToTTLookup0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
// This was double counting!
//                    if (!set.contains(tID)) {
//                        // UnderOccupancy
//                        DW_UO_Record underOccupied0 = null;
//                        DW_UO_Record underOccupied1 = null;
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
//                            String TT0 = ClaimIDToTTLookup1.get(
//                                    tID);
//                            String TT1;
//                            TT1 = ClaimIDToTTLookup1.get(tID);
//                            if (TT0.equalsIgnoreCase(TT1)) {
//                                String postcode0;
//                                postcode0 = ClaimIDToPostcodeIDLookup0.get(tID);
//                                boolean isValidPostcode0 = false;
//                                if (postcode0 != null) {
//                                    isValidPostcode0 = DW_Postcode_Handler.isValidPostcode(postcode0);
//                                }
//                                String postcode1;
//                                postcode1 = ClaimIDToPostcodeIDLookup1.get(tID);
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
//                                        String TTChange;
//                                        if (doUnderOccupiedData) {
//                                            String[] ttc = getTTTName(
//                                                    TT0,
//                                                    underOccupied0 != null,
//                                                    TT1,
//                                                    underOccupied1 != null);
//                                            TTChange = ttc[0];
//                                            TT0 = ttc[1];
//                                            TT1 = ttc[2];
//                                            if (!TT0.equalsIgnoreCase(TT1)) {
//                                                recordTTTs(
//                                                        tID,
//                                                        TTTs,
//                                                        year,
//                                                        month,
//                                                        TTChange);
//                                            }
//                                        } else {
//                                            TTChange = getTTTName(
//                                                    TT0,
//                                                    TT1);
//                                        }
//                                        if (postcodeChange) {
//                                            String[] postcodeChangeResult;
//                                postcodeChangeResult = getPTName(
//                                        tID,
//                                        YM30, 
//                                        YM31, 
//                                        TTChange,
//                                        postcode0,
//                                        postcode1);
//                                DoPostcodeChanges.add(postcodeChangeResult);
//                                        }
//                                        if (result.containsKey(TT1)) {
//                                            TreeMap<String, Integer> TTCount;
//                                            TTCount = result.get(TT1);
//                                            Generic_Collections.addToTreeMapStringInteger(
//                                                    TTCount, TT0, 1);
//                                        } else {
//                                            TreeMap<String, Integer> TTCount;
//                                            TTCount = new TreeMap<String, Integer>();
//                                            TTCount.put(TT0, 1);
//                                            result.put(TT1, TTCount);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    } else {
                // UnderOccupancy
                DW_UO_Record underOccupied0 = null;
                //DW_UnderOccupiedReport_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    }
                    doMainLoop = underOccupied0 != null;
                }
                if (doMainLoop) {
                    Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
                    String sTT0 = Integer.toString(TT0);
                    Integer TT1 = -999;
                    String sTT1;
                    sTT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    String TTT;
                    if (doUnderOccupiedData) {
                        String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                TT0,
                                underOccupied0 != null,
                                TT1,
                                false);
                        TTT = ttc[0];
                        sTT0 = ttc[1];
                        sTT1 = ttc[2];
                        if (sTT0.equalsIgnoreCase(sTT1)) {
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTT);
                        }
                    }
                    if (result.containsKey(sTT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(sTT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, sTT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(sTT0, 1);
                        result.put(sTT1, TTCount);
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode,
                        DW_Strings.sGroupedNo);
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
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            Set<DW_ID> tUnderOccupancy0,
            Set<DW_ID> tUnderOccupancy1,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set DW_UO_Set0,
            DW_UO_Set DW_UO_Set1,
            boolean doUnderOccupiedData,
            Set<DW_ID> underOccupiedInApril2013) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_ID> ite;
        // Go through current claimants
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (DW_UO_Set0 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied0 = DW_UO_Set0.getMap().get(ClaimID);
                    } else if (underOccupiedInApril2013.contains(ClaimID)) {
                        underOccupied0 = DW_UO_Set0.getMap().get(ClaimID);
                    }
                }
                if (DW_UO_Set1 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied1 = DW_UO_Set1.getMap().get(ClaimID);
                    } else if (underOccupiedInApril2013.contains(ClaimID)) {
                        underOccupied1 = DW_UO_Set1.getMap().get(ClaimID);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer TT0Integer;
                String TT0;
                DW_ID postcode0 = null;
                if (ClaimIDToTTLookup1 != null) {
                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    if (TT0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    tUnderOccupancies,
                                    i,
                                    include);
                            TT0 = (String) previousTenure[0];
                            if (checkPreviousPostcode) {
                                Integer indexOfLastKnownTenureOrNot;
                                indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                if (indexOfLastKnownTenureOrNot != null) {
//                                       env.logO("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                    if (checkPreviousPostcode) {
                                        postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                    }
                                }
                            }
                        } else {
                            TT0 = sMinus999;
                        }
                    } else {
                        TT0 = Integer.toString(TT0Integer);
                    }
                } else {
                    TT0 = sMinus999;
                }
                TT0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT0);
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT1Integer);
                DW_ID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equals(postcode1);
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    if (TT0.equalsIgnoreCase(TT1)
                            || TT0.equalsIgnoreCase(DW_SHBE_TenancyType_Handler.sMinus999)) { // Major diff
                        String TTChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    underOccupied0 != null,
                                    TT1,
                                    underOccupied1 != null);
                            TTChange = ttc[0];
                            TT0 = ttc[1];
                            TT1 = ttc[2];
                            //if (!TT0.equalsIgnoreCase(TT1)) {
                            if (TT0.equalsIgnoreCase(TT1)) {
                                recordTTTs(
                                        ClaimID,
                                        TTTs,
                                        YM31,
                                        TTChange);
                            }
                        } else {
                            TTChange = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    TT1);
                        }
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(
                                    ClaimID,
                                    YM30,
                                    YM31,
                                    TTChange,
                                    postcode0,
                                    postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                        if (result.containsKey(TT1)) {
                            TreeMap<String, Integer> TTCount;
                            TTCount = result.get(TT1);
                            Generic_Collections.addToTreeMapStringInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<String, Integer>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                TTCount, TT0, 1);
//                    } else {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = new TreeMap<String, Integer>();
//                        TTCount.put(TT0, 1);
//                        result.put(TT1, TTCount);
//                    }
//                }
            }
        }
        // Go through all those previously and record for all those that are not
        // in the current data. Also record for all those that were under 
        // occupying but are now not (and vice vesa) and have changed postcode.
        ite = ClaimIDToTTLookup0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
                boolean doMainLoop = true;
                DW_UO_Record underOccupied0 = null;
                DW_UO_Record underOccupied1 = null;
                if (doUnderOccupiedData) {
                    if (DW_UO_Set0 != null) {
                        underOccupied0 = DW_UO_Set0.getMap().get(ClaimID);
                    }
                    doMainLoop = underOccupied0 != null;
                    if (DW_UO_Set1 != null) {
                        underOccupied1 = DW_UO_Set1.getMap().get(ClaimID);
                    }
                }
                if (doMainLoop) {
                    Integer TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    String TT0;
                    TT0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT0Integer);
                    String TT1;
                    TT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    DW_ID postcode0;
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    String TTChange;
                    if (doUnderOccupiedData) {
                        String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                TT0,
                                underOccupied0 != null,
                                TT1,
                                underOccupied1 != null);
                        TTChange = ttc[0];
                        TT0 = ttc[1];
                        TT1 = ttc[2];
                        if (TT0.equalsIgnoreCase(TT1)) {
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTChange);
                        }
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode,
                        DW_Strings.sGrouped);
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
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToTTLookups
     * @param tUnderOccupancy0
     * @param tUnderOccupancy1
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param tUnderOccupancies
     * @param TTTs
     * @param YM30
     * @param YM31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param ClaimIDToPostcodeIDLookup0
     * @param ClaimIDToPostcodeIDLookup1
     * @param ClaimIDToPostcodeIDLookups
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param underOccupiedSet0
     * @param underOccupiedSet1
     * @param doUnderOccupiedData
     * @param underOccupiedInApril2013
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTenancyTypeTransitionMatrixGroupedAndWritePostcodeChangeDetails(
            File dirOut,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup0,
            HashMap<DW_ID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            Set<DW_ID> tUnderOccupancy0,
            Set<DW_ID> tUnderOccupancy1,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0,
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set underOccupiedSet0,
            DW_UO_Set underOccupiedSet1,
            boolean doUnderOccupiedData,
            Set<DW_ID> underOccupiedInApril2013) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<String[]>();
        }
        Iterator<DW_ID> ite;
        // Go through for current
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (doUnderOccupiedData) {
                if (underOccupiedSet0 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    } else if (underOccupiedInApril2013.contains(ClaimID)) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    }
                }
                if (underOccupiedSet1 != null) {
                    if (underOccupiedInApril2013 == null) {
                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                    } else if (underOccupiedInApril2013.contains(ClaimID)) {
                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
                    }
                }
                doMainLoop = underOccupied0 != null || underOccupied1 != null;
            }
            if (doMainLoop) {
                Integer TT0Integer;
                String TT0;
                DW_ID postcode0 = null;
                if (ClaimIDToTTLookup1 != null) {
                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    if (TT0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    tUnderOccupancies,
                                    index,
                                    include);
                            TT0 = (String) previousTenure[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                            if (indexOfLastKnownTenureOrNot != null) {
                                postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                            }
                        } else {
                            TT0 = sMinus999;
                        }
                    } else {
                        TT0 = Integer.toString(TT0Integer);
                    }
                } else {
                    TT0 = sMinus999;
                }
                TT0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT0);
                Integer TT1Integer;
                TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1;
                TT1 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        TT1Integer);
                DW_ID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equals(postcode1);
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    if (!TT0.equalsIgnoreCase(TT1)) {
                        String TTChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    underOccupied0 != null,
                                    TT1,
                                    underOccupied1 != null);
                            TTChange = ttc[0];
                            TT0 = ttc[1];
                            TT1 = ttc[2];
                        } else {
                            TTChange = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    TT1);
                        }
                        recordTTTs(
                                ClaimID,
                                TTTs,
                                YM31,
                                TTChange);
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(
                                    ClaimID,
                                    YM30,
                                    YM31,
                                    TTChange,
                                    postcode0,
                                    postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        // Go through for previous not in current
        ite = ClaimIDToTTLookup0.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (!ClaimIDToTTLookup0.containsKey(ClaimID)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UO_Record underOccupied0 = null;
                if (doUnderOccupiedData) {
                    if (underOccupiedSet0 != null) {
                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
                    }
                    doMainLoop = underOccupied0 != null;
                }
                if (doMainLoop) {
                    Integer TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    String TT0;
                    TT0 = getTenancyTypeGroup(
                            regulatedGroups,
                            unregulatedGroups,
                            TT0Integer);
                    String TT1;
                    TT1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    DW_ID postcode0;
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    if (!TT0.equalsIgnoreCase(TT1)) { // Always the case
                        String TTChange;
                        if (doUnderOccupiedData) {
                            String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    underOccupied0 != null,
                                    TT1,
                                    false);
                            TTChange = ttc[0];
                            TT0 = ttc[1];
                            TT1 = ttc[2];
                        } else {
                            TTChange = DW_ProcessorLCCTTAndPT.this.getTTTName(
                                    TT0,
                                    TT1);
                        }
                        recordTTTs(
                                ClaimID,
                                TTTs,
                                YM31,
                                TTChange);
                    }
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToTreeMapStringInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<String, Integer>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode,
                        DW_Strings.sGrouped);
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
     * @param postcodeChanges (DW_ID, StartTime, EndTime, TenancyTypeChange,
     * StartPostcode, End Postcode)
     * @param YM30
     * @param YM31
     * @param checkPreviousPostcode
     * @param type
     */
    private void writePostcodeChanges(
            File dirOut,
            ArrayList<String[]> postcodeChanges,
            String YM30,
            String YM31,
            boolean checkPreviousPostcode,
            String type) {
        File dirOut2 = new File(
                dirOut,
                type);
        dirOut2 = new File(
                dirOut2,
                DW_Strings.sPostcodeChanges);
        if (checkPreviousPostcode) {
            dirOut2 = new File(
                    dirOut2,
                    DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirOut2 = new File(
                    dirOut2,
                    DW_Strings.sCheckedPreviousPostcodeNo);
        }
        dirOut2.mkdirs();
        File f;
        f = new File(
                dirOut2,
                DW_Strings.sPostcodeChanges
                + "_Start_" + YM30
                + "_End_" + YM31 + ".csv");

        env.logO("Write " + f, true);
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
            Integer TT) {
        String result;
        if (TT == -999) {
            result = DW_SHBE_TenancyType_Handler.sMinus999;
        } else {
            result = DW_Strings.sGroupedNo;
            if (regulatedGroups.contains(TT)) {
                result = "Regulated";
            }
            if (unregulatedGroups.contains(TT)) {
                result = "Unregulated";
            }
        }
        return result;
    }

    public String getTenancyTypeGroup(
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            String TT) {
        String result;
        Integer tt;
        if (TT.endsWith(sU)) {
            tt = Integer.valueOf(TT.substring(0, TT.length() - 1));
        } else {
            tt = Integer.valueOf(TT);
        }
        result = getTenancyTypeGroup(
                regulatedGroups,
                unregulatedGroups,
                tt);
        if (TT.endsWith(sU)) {
            result += sU;
        }
        return result;
    }

    /**
     *
     * @param TTTMatrix {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count}
     * @param YM30
     * @param YM31
     * @param dirOut
     * @param TTs
     * @param grouped
     */
    public void writeTTTMatrix(
            TreeMap<String, TreeMap<String, Integer>> TTTMatrix,
            String YM30,
            String YM31,
            File dirOut,
            ArrayList<String> TTs) {
        writeTransitionMatrix(
                TTTMatrix,
                YM30,
                YM31,
                dirOut,
                TTs,
                DW_Strings.sTenancyTypeTransition);
    }

    /**
     *
     * @param TMatrix {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count}
     * @param YM30
     * @param YM31
     * @param dirOut
     * @param grouped
     * @param TTs
     * @param name
     */
    public void writeTransitionMatrix(
            TreeMap<String, TreeMap<String, Integer>> TMatrix,
            String YM30,
            String YM31,
            File dirOut,
            ArrayList<String> Ts,
            String name) {
        if (TMatrix == null) {
            return;
        }
        if (TMatrix.isEmpty()) {
            //if (TTMatrix.size() == 0) {
            return;
        }
        dirOut.mkdir();
        File f;
        f = new File(
                dirOut,
                name
                + "_Start_" + YM30
                + "_End_" + YM31 + ".csv");
        PrintWriter pw;
        try {
            pw = new PrintWriter(f);
            String line;
            line = DW_Strings.sTenancyType + YM31 + "\\" + DW_Strings.sTenancyType + YM30;
            Iterator<String> ite;
            ite = Ts.iterator();
            while (ite.hasNext()) {
                line += "," + ite.next();
            }
            line += ",-999";
            pw.println(line);
            ite = Ts.iterator();
            while (ite.hasNext()) {
                String T0;
                T0 = ite.next();
                line = T0;
                TreeMap<String, Integer> TCounts;
                TCounts = TMatrix.get(T0);
                if (TCounts == null) {
                    for (String T : Ts) {
                        line += ",0";
                    }
                    line += ",0";
                } else {
                    String T1;
                    Iterator<String> ite2;
                    ite2 = Ts.iterator();
                    while (ite2.hasNext()) {
                        T1 = ite2.next();
                        Integer count;
                        count = TCounts.get(T1);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    T1 = DW_SHBE_TenancyType_Handler.sMinus999;
                    Integer nullCount = TCounts.get(T1);
                    if (nullCount == null) {
                        line += ",0";
                    } else {
                        line += "," + nullCount.toString();
                    }
                }
                pw.println(line);
            }
            TreeMap<String, Integer> TTCounts;
            TTCounts = TMatrix.get(DW_SHBE_TenancyType_Handler.sMinus999);
            line = DW_SHBE_TenancyType_Handler.sMinus999;
            if (TTCounts == null) {
                for (String T : Ts) {
                    line += ",0";
                }
                line += ",0";
            } else {
                String T1;
                Iterator<String> ite2;
                ite2 = Ts.iterator();
                while (ite2.hasNext()) {
                    T1 = ite2.next();
                    Integer count;
                    count = TTCounts.get(T1);
                    if (count == null) {
                        line += ",0";
                    } else {
                        line += "," + count.toString();
                    }
                }
                T1 = DW_SHBE_TenancyType_Handler.sMinus999;
                Integer nullCount = TTCounts.get(T1);
                if (nullCount == null) {
                    line += ",0";
                } else {
                    line += nullCount.toString();
                }
            }
            pw.println(line);
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_ProcessorLCCTTAndPT.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a migration matrix for all claimants.
     *
     * @param SHBEFilenames
     * @param aPT
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
            String aPT,
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
        String YM30;
        YM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[startIndex]);
        DW_SHBE_Records recs0;
        recs0 = DW_SHBE_Data.getDW_SHBE_Records(YM30);
//        recs0 = env.getDW_SHBE_Data().getData().get(YM30);
        HashMap<DW_ID, DW_SHBE_Record> recordsStart;
        recordsStart = recs0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        // End
        String YM31;
        YM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[endIndex]);
        DW_SHBE_Records recs1;
        recs1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
//        recs1 = env.getDW_SHBE_Data().getData().get(YM31);
        HashMap<DW_ID, DW_SHBE_Record> recordsEnd;
        recordsEnd = recs1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<DW_ID> ite;
        ite = recordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            DW_ID DW_ID = ite.next();
            DW_SHBE_D_Record startDRecord;
            startDRecord = recordsStart.get(DW_ID).getDRecord();
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
                endDRecord = recordsEnd.get(DW_ID).getDRecord();
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
            DW_ID DW_ID = ite.next();
            DW_SHBE_D_Record DRecordEnd;
            DRecordEnd = recordsEnd.get(DW_ID).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart;
                DRecordStart = recordsStart.get(DW_ID).getDRecord();
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
     * @param aPT
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
            String aPT,
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
        String YM30;
        YM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[startIndex]);
        DW_SHBE_Records recs0;
        recs0 = DW_SHBE_Data.getDW_SHBE_Records(YM30);
        //recs0 = env.getDW_SHBE_Data().getData().get(YM30);
        HashMap<DW_ID, DW_SHBE_Record> recordsStart;
        recordsStart = recs0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        String YM31;
        YM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[endIndex]);
        DW_SHBE_Records recs1;
        recs1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
        //recs1 = env.getDW_SHBE_Data().getData().get(YM31);
        HashMap<DW_ID, DW_SHBE_Record> recordsEnd;
        recordsEnd = recs1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<DW_ID> ite;
        ite = recordsStart.keySet().iterator();
        DW_ID DW_ID;
        while (ite.hasNext()) {
            DW_ID = ite.next();
            DW_SHBE_D_Record startDRecord = recordsStart.get(DW_ID).getDRecord();
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<Integer, Integer>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                DW_SHBE_D_Record endDRecord = recordsEnd.get(DW_ID).getDRecord();
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
            DW_ID = ite.next();
            DW_SHBE_D_Record endDRecord = recordsEnd.get(DW_ID).getDRecord();
            if (endDRecord != null) {
                DW_SHBE_D_Record DRecordStart = recordsStart.get(DW_ID).getDRecord();
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
}
