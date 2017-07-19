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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
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
public class DW_ProcessorLCCAggregate extends DW_ProcessorLCC {

    /**
     * For convenience DW_UO_Handler = env.getDW_UO_Handler().
     */
    protected DW_UO_Handler DW_UO_Handler;

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

    public DW_ProcessorLCCAggregate(DW_Environment env) {
        super(env);
        DW_UO_Handler = env.getDW_UO_Handler();
        DW_SHBE_TenancyType_Handler = env.getDW_SHBE_TenancyType_Handler();
        DW_SHBE_Data = env.getDW_SHBE_Data();
        ClaimIDToClaimRefLookup = DW_SHBE_Data.getClaimIDToClaimRefLookup();
        DW_UO_Data = env.getDW_UO_Data();
    }

    @Override
    public void run() {
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
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
        doTenancyTransitions = true;
        doPostcodeChanges = true;
        doTenancyChanges = true;
        doTTTAndPT = true;
//        doAggregation = true;

//        doHBGeneralAggregateStatistics = true;
        
        
    }

    /**
     * For aggregating data spatially.
     *
     * @param YM3
     * @param PTs
     * @param levels
     * @param bArray
     * @param DW_UO_Data
     * @param SHBEFilenames
     * @param claimantTypes
     * @param types
     * @param distances
     * @param distanceTypes
     * @param TTs
     * @param TTGroups
     * @param TTsGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     */
    protected void aggregate(
            String YM3,
            ArrayList<String> PTs,
            ArrayList<String> levels,
            ArrayList<Boolean> bArray,
            DW_UO_Data DW_UO_Data,
            String[] SHBEFilenames,
            ArrayList<String> claimantTypes,
            ArrayList<String> types,
            ArrayList<Double> distances,
            ArrayList<String> distanceTypes,
            HashMap<Boolean, ArrayList<String>> TTs,
            HashMap<Boolean, TreeMap<String, ArrayList<String>>> TTGroups,
            HashMap<Boolean, ArrayList<String>> TTsGrouped,
            HashMap<Boolean, ArrayList<Integer>> regulatedGroups,
            HashMap<Boolean, ArrayList<Integer>> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes) {
        env.logO("<doAggregation>", true);
        // Get Lookup
            TreeMap<String, TreeMap<String, String>> LookupsFromPostcodeToLevelCode;
                LookupsFromPostcodeToLevelCode = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3);
            Iterator<Boolean> iteB;
        Iterator<String> tPTIte;
        tPTIte = PTs.iterator();
        while (tPTIte.hasNext()) {
            String aPT;
            aPT = tPTIte.next();
            env.logO("<" + aPT + ">", true);
                //Generic_UKPostcode_Handler.isValidPostcodeForm(String postcode)
            iteB = bArray.iterator();
            while (iteB.hasNext()) {
                boolean doUnderOccupied;
                doUnderOccupied = iteB.next();
                boolean doCouncil;
                boolean doRSL;
                if (doUnderOccupied) {
                    if (true) {
//                    if (false) {
                        doCouncil = true;
                        doRSL = true;
                        env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                LookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                aPT,
                                claimantTypes,
                                TTGroups.get(doUnderOccupied),
                                TTsGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances);
                        env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = true;
                        doRSL = false;
                        env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                LookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                aPT,
                                claimantTypes,
                                TTGroups.get(doUnderOccupied),
                                TTsGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances);
                        env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = false;
                        doRSL = true;
                        env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                LookupsFromPostcodeToLevelCode,
                                SHBEFilenames,
                                aPT,
                                claimantTypes,
                                TTGroups.get(doUnderOccupied),
                                TTsGrouped.get(doUnderOccupied),
                                regulatedGroups.get(doUnderOccupied),
                                unregulatedGroups.get(doUnderOccupied),
                                includes,
                                levels,
                                types,
                                distanceTypes,
                                distances);
                        env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                    }
                } else {
                    doCouncil = false;
                    doRSL = false;
                    env.logO("<aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
                    aggregateClaims(
                            doUnderOccupied,
                            doCouncil,
                            doRSL,
                            DW_UO_Data,
                            LookupsFromPostcodeToLevelCode,
                            SHBEFilenames,
                            aPT,
                            claimantTypes,
                            TTGroups.get(doUnderOccupied),
                            TTsGrouped.get(doUnderOccupied),
                            regulatedGroups.get(doUnderOccupied),
                            unregulatedGroups.get(doUnderOccupied),
                            includes,
                            levels,
                            types,
                            distanceTypes,
                            distances);
                    env.logO("</aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
                }
            }
            env.logO("</" + aPT + ">", true);
        }
        env.logO("</doAggregation>", true);
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
    public String getAreaCode(
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
            String formattedPostcode = DW_Postcode_Handler.formatPostcode(postcode);
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

    /**
     * @param claimantNINO1
     * @param i
     * @param tIDIndexes
     * @return True iff claimantNINO1 is in tIDIndexes in any index from 0 to i
     * - 1.
     */
    private boolean getHasClaimantBeenSeenBefore(
            DW_ID ID,
            int i,
            ArrayList<Integer> include,
            ArrayList<ArrayList<DW_ID>> tIDIndexes) {
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
            ArrayList<DW_ID> tIDIndex;
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

    public String getLastYear_yearMonth(
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
     * @param UnderOccupiedData
     */
    public void reportUnderOccupancyTotals(Object[] UnderOccupiedData) {
        TreeMap<String, DW_UO_Set> CouncilSets;
        CouncilSets = (TreeMap<String, DW_UO_Set>) UnderOccupiedData[0];
        Iterator<String> ite;
        Iterator<DW_ID> ite2;
        HashSet<DW_ID> totalCouncil;
        totalCouncil = new HashSet<DW_ID>();
        ite = CouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            String YM3;
            YM3 = ite.next();
            DW_UO_Set s;
            s = CouncilSets.get(YM3);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                DW_ID CTBRefID;
                CTBRefID = ite2.next();
                totalCouncil.add(CTBRefID);
            }
        }
        env.logO("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
        TreeMap<String, DW_UO_Set> tRSLSets;
        tRSLSets = (TreeMap<String, DW_UO_Set>) UnderOccupiedData[1];
        HashSet<DW_ID> totalRSL;
        totalRSL = new HashSet<DW_ID>();
        ite = tRSLSets.keySet().iterator();
        while (ite.hasNext()) {
            String yM;
            yM = ite.next();
            DW_UO_Set s;
            s = tRSLSets.get(yM);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                DW_ID CTBRefID;
                CTBRefID = ite2.next();
                totalRSL.add(CTBRefID);
            }
        }
        env.logO("Number of RSL tenants effected by underoccupancy " + totalRSL.size(), true);
        totalCouncil.addAll(totalRSL);
        env.logO("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
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
     * @param DW_UO_SetsAll
     * @param DW_UO_SetsCouncil
     * @param DW_UO_SetsRSL
     * @param ClaimIDs
     */
    public void doTTTsU(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            TreeMap<String, DW_UO_Set> DW_UO_SetsAll,
            TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil,
            TreeMap<String, DW_UO_Set> DW_UO_SetsRSL,
            Set<DW_ID> ClaimIDs) {
        HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups;
        ClaimIDToPostcodeIDLookups = new HashMap<Integer, HashMap<DW_ID, DW_ID>>();
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
            DW_UO_Set DW_UO_SetAll0 = null;
            DW_UO_Set DW_UO_SetCouncil0 = null;
            DW_UO_Set DW_UO_SetRSL0 = null;
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
            HashMap<Integer, Set<DW_ID>> UOClaimIDSetsCouncil = null;
            HashMap<Integer, Set<DW_ID>> UOClaimIDSetsRSL = null;
            boolean doloop = true;
            DW_UO_SetAll0 = DW_UO_SetsAll.get(YM30);
            DW_UO_SetCouncil0 = DW_UO_SetsCouncil.get(YM30);
            DW_UO_SetRSL0 = DW_UO_SetsRSL.get(YM30);
            UOClaimIDSets = new HashMap<Integer, Set<DW_ID>>();
            UOClaimIDSetsCouncil = new HashMap<Integer, Set<DW_ID>>();
            UOClaimIDSetsRSL = new HashMap<Integer, Set<DW_ID>>();
            if (DW_UO_SetAll0 == null) {
                doloop = false;
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
                    // Set Year and Month variables
                    String YM31 = DW_SHBE_Handler.getYM3(filename);
                    env.logO("Year Month " + YM31, true);
                    DW_SHBE_Records DW_SHBE_Records1;
                    DW_SHBE_Records1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
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
                    DW_UO_SetAll1 = DW_UO_SetsAll.get(YM31);
                    DW_UO_SetCouncil1 = DW_UO_SetsCouncil.get(YM31);
                    DW_UO_SetRSL1 = DW_UO_SetsRSL.get(YM31);
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
                            UOClaimIDSetsCouncil,
                            UOClaimIDSetsRSL,
                            TTCs,
                            YM30,
                            YM31,
                            checkPreviousTenure,
                            i,
                            include,
                            indexYM3s,
                            DW_UO_SetAll0,
                            DW_UO_SetCouncil0,
                            DW_UO_SetRSL0,
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
                    DW_UO_SetAll0 = DW_UO_SetAll1;
                    DW_UO_SetCouncil0 = DW_UO_SetCouncil1;
                    DW_UO_SetRSL0 = DW_UO_SetRSL1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<DW_ID> TTCIte;
                // Ungrouped
                transitions = new TreeMap<String, Integer>();
                max = Integer.MIN_VALUE;
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
                env.logO(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
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
                    env.logO(includeKey + " maximum number of transitions "
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
     * @param SHBEFilenames A list of all SHBE filenames
     * @param TTs Tenancy Types
     * @param includes The indexes of the SHBEFilenames to use in the
     * summarisation.
     * @param checkPreviousTenure If true, then if the Tenure Type previously is
     * not recorded (because the claimant was not in the SHBE) then the
     * algorithm checks back for the last time the claimant had a recorded
     * Tenancy Type in the SHBE.
     * @param reportTenancyTransitionBreaks If true then when a claimant moves
     * off the SHBE in a time period, this is recorded in the Frequency Tables
     * as a Tenancy Type Transition to -999, otherwise it is not.
     * @param postcodeChange If true postcode changes are summarised. If false
     * non postcode changes are summarised.
     * @param DW_UO_SetsAll
     * @param DW_UO_SetsCouncil
     * @param DW_UO_SetsRSL
     * @param checkPreviousPostcode If true, when the postcode does not
     * validate, the algorithm looks back for a earlier postcode. If false, no
     * further checking is done.
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
            DW_UO_Set UOSetCouncil0 = null;
            DW_UO_Set UOSetRSL0 = null;

            // ClaimIDToPostcodeIDLookup0
            HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup0;
            ClaimIDToPostcodeIDLookup0 = loadClaimIDToPostcodeIDLookup(
                    YM30,
                     i,
                    ClaimIDToPostcodeIDLookups);

            HashMap<Integer, Set<DW_ID>> UOClaimIDs = null;
            HashMap<Integer, Set<DW_ID>> UOCouncil = null;
            HashMap<Integer, Set<DW_ID>> UORSL = null;

            Set<DW_ID> UOClaimIDs0 = null;
            Set<DW_ID> UOClaimIDsCouncil0 = null;
            Set<DW_ID> UOClaimIDsRSL0 = null;
            Set<DW_ID> UOClaimIDs1 = null;
            Set<DW_ID> UOClaimIDsCouncil1 = null;
            Set<DW_ID> UOClaimIDsRSL1 = null;

            boolean doloop = true;
            UOClaimIDs = new HashMap<Integer, Set<DW_ID>>();
            UOCouncil = new HashMap<Integer, Set<DW_ID>>();
            UORSL = new HashMap<Integer, Set<DW_ID>>();
            UOSet0 = DW_UO_SetsAll.get(YM30);
            UOSetCouncil0 = DW_UO_SetsCouncil.get(YM30);
            UOSetRSL0 = DW_UO_SetsRSL.get(YM30);
            if (UOSet0 != null) {
                UOClaimIDs0 = UOSet0.getClaimIDs();
            } else {
                doloop = false;
            }
            if (UOSetCouncil0 == null) {
                UOClaimIDsCouncil0 = UOSetCouncil0.getClaimIDs();
            } else {
                doloop = false;
            }
            if (UOSetRSL0 == null) {
                UOClaimIDsRSL0 = UOSetRSL0.getClaimIDs();
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
                            UOCouncil,
                            UORSL,
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
                max = Integer.MIN_VALUE;
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
                env.logO(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
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
                    env.logO(includeKey + " maximum number of transitions "
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
            max = Integer.MIN_VALUE;
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
            env.logO(includeKey + " maximum number of transitions "
                    + max + " out of a possible " + (include.size() - 1), true);
            writeTransitionFrequencies(transitions,
                    dirOut2,
                    DW_Strings.sGroupedNo,
                    "Frequencies.txt",
                    reportTenancyTransitionBreaks);
            // Grouped
            if (doGrouped) {
                transitions = new TreeMap<String, Integer>();
                max = Integer.MIN_VALUE;
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
                env.logO(includeKey + " maximum number of transitions "
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
                    env.logO("underOccupiedSet0 == null, YM30 = " + YM30, true);
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
                max = Integer.MIN_VALUE;
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
                env.logO(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions,
                        dirOut2,
                        DW_Strings.sGroupedNo,
                        "Frequencies.txt",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (doGrouped) {
                    transitions = new TreeMap<String, Integer>();
                    max = Integer.MIN_VALUE;
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
                    env.logO(includeKey + " maximum number of transitions "
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
        result = env.getDW_SHBE_Data().getDW_SHBE_Records(YM3).getClaimIDToPostcodeIDLookup(
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
        env.logO("Write " + f, true);
        result = Generic_StaticIO.getPrintWriter(f, false);
        return result;
    }

    /**
     * TODO: We want to be able to distinguish those claimants that have been:
     * continually claiming; claiming for at least 3 months; claiming for at
     * least 3 months, then stopped, then started claiming again.
     *
     * @param doUnderOccupied
     * @param doCouncil
     * @param doRSL
     * @param DW_UO_Data
     * @param lookupsFromPostcodeToLevelCode
     * @param SHBEFilenames
     * @param aPT
     * @param claimantTypes
     * @param TTGroups
     * @param TTsGrouped
     * @param regulatedGroups
     * @param unregulatedGroups
     * @param includes
     * @param levels
     * @param types type = NewEntrant type = Stable type = Churn
     * @param distanceTypes
     * @param distances
     */
    public void aggregateClaims(
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL,
            DW_UO_Data DW_UO_Data,
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames,
            String aPT,
            ArrayList<String> claimantTypes,
            TreeMap<String, ArrayList<String>> TTGroups,
            ArrayList<String> TTsGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<String> levels,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances) {
        TreeMap<String, File> outputDirs;
        outputDirs = DW_Files.getOutputSHBELevelDirsTreeMap(
                levels,
                doUnderOccupied,
                doCouncil,
                doRSL);
        // Init underOccupiedSets
        TreeMap<String, DW_UO_Set> councilUnderOccupiedSets = null;
        TreeMap<String, DW_UO_Set> RSLUnderOccupiedSets = null;
        if (doUnderOccupied) {
            if (doCouncil) {
                councilUnderOccupiedSets = DW_UO_Data.getCouncilUOSets();
            }
            if (doRSL) {
                RSLUnderOccupiedSets = DW_UO_Data.getRSLUOSets();
            }
        }

        // Declare iterators
        Iterator<String> claimantTypesIte;
        Iterator<String> TTIte;
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
            String YM30;
            YM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            DW_SHBE_Records recs0;
            recs0 = env.getDW_SHBE_Data().getDW_SHBE_Records(YM30);
            String YM30v;
            YM30v = recs0.getNearestYM3ForONSPDLookup();
            HashMap<DW_ID, DW_SHBE_Record> records0;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
            claimantTypeTenureLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>>();
            claimantTypeTenureLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>>();
            claimantTypeTenureLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>>();
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType;
                claimantType = claimantTypesIte.next();
                // Initialise Dirs
                TreeMap<String, TreeMap<String, TreeMap<String, File>>> TTLevelTypeDirs;
                TTLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, File>>>();
                // Initialise AreaCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts;
                TTLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>();
                // Initialise TenureCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts;
                TTLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>();
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT = TTIte.next();
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
                                    TT);
                            outDir1.mkdirs();
                            typeDirs.put(type, outDir1);
                            // Initialise AreaCounts
                            TreeMap<String, Integer> areaCount;
                            areaCount = new TreeMap<String, Integer>();
                            typeAreaCounts.put(type, areaCount);
                            // Initialise TenureCounts
                            TreeMap<Integer, Integer> TTCounts;
                            TTCounts = new TreeMap<Integer, Integer>();
                            typeTenureCounts.put(type, TTCounts);
                        }
                        levelTypeDirs.put(level, typeDirs);
                        levelTypeAreaCounts.put(level, typeAreaCounts);
                        levelTypeTenureCounts.put(level, typeTenureCounts);
                    }
                    TTLevelTypeDirs.put(TT, levelTypeDirs);
                    TTLevelTypeAreaCounts.put(TT, levelTypeAreaCounts);
                    TTLevelTypeTenureCounts.put(TT, levelTypeTenureCounts);
                }
                claimantTypeTenureLevelTypeDirs.put(claimantType, TTLevelTypeDirs);
                claimantTypeTenureLevelTypeAreaCounts.put(claimantType, TTLevelTypeAreaCounts);
                claimantTypeTenureLevelTypeTenureCounts.put(claimantType, TTLevelTypeTenureCounts);
            }
            records0 = recs0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);

            // Init underOccupiedSets
            DW_UO_Set councilUOSet0 = null;
            DW_UO_Set RSLUOSet0 = null;
            if (doUnderOccupied) {
                if (doCouncil) {
                    councilUOSet0 = councilUnderOccupiedSets.get(YM30);
                }
                if (doRSL) {
                    RSLUOSet0 = RSLUnderOccupiedSets.get(YM30);
                }
            }
            // Iterator over records
            Iterator<DW_ID> recordsIte;
            recordsIte = records0.keySet().iterator();
            while (recordsIte.hasNext()) {
                DW_ID ClaimID;
                ClaimID = recordsIte.next();
                DW_SHBE_Record DW_SHBE_Record0;
                DW_SHBE_Record0 = records0.get(ClaimID);
                DW_SHBE_D_Record DRecord0 = DW_SHBE_Record0.getDRecord();
                String postcode0 = DRecord0.getClaimantsPostcode();
                Integer TT1Integer = DRecord0.getTenancyType();
                String TT1 = TT1Integer.toString();
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT;
                    TT = TTIte.next();
                    ArrayList<String> TTs;
                    TTs = TTGroups.get(TT);
                    if (TTs.contains(TT1)) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level = levelsIte.next();
                            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                            tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                            String claimantType;
                            claimantType = DW_SHBE_Handler.getClaimantType(DRecord0);
                            Integer TTInt = DRecord0.getTenancyType();
                            if (postcode0 != null) {
                                String areaCode;
                                areaCode = getAreaCode(
                                        level,
                                        postcode0,
                                        tLookupFromPostcodeToLevelCode);
                                String type;
                                type = sAllClaimants;
                                if (types.contains(type)) {
                                    boolean doMainLoop = true;
                                    // Check for UnderOccupied
                                    if (doUnderOccupied) {
                                        // UnderOccupancy
                                        boolean doCouncilMainLoop = true;
                                        if (doCouncil) {
                                            DW_UO_Record councilUnderOccupied0 = null;
                                            if (councilUOSet0 != null) {
                                                councilUnderOccupied0 = councilUOSet0.getMap().get(
                                                        ClaimID);
                                            }
                                            doCouncilMainLoop = councilUnderOccupied0 != null;
                                        }
                                        boolean doRSLMainLoop = true;
                                        if (doRSL) {
                                            DW_UO_Record RSLUnderOccupied0 = null;
                                            if (RSLUOSet0 != null) {
                                                RSLUnderOccupied0 = RSLUOSet0.getMap().get(
                                                        ClaimID);
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
                                                TT,
                                                level,
                                                type,
                                                TTInt);
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
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                TTLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>();
                claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT;
                    TT = TTIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                    levelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>();
                    TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
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
                            TreeMap<Integer, Integer> TTCounts;
                            File dir;
                            areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(TT).get(level).get(type);
                            TTCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(TT).get(level).get(type);
                            dir = claimantTypeTenureLevelTypeDirs.get(claimantType).get(TT).get(level).get(type);
                            TreeMap<Integer, TreeSet<String>> countAreas;
                            countAreas = writeResults(
                                    areaCounts,
                                    TTCounts,
                                    level,
                                    dir,
                                    YM30);
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
            ArrayList<ArrayList<DW_ID>> tIDIndexes;
            tIDIndexes = new ArrayList<ArrayList<DW_ID>>();
            if (true) {
//                ArrayList<DW_ID> tID_HashSet;
//                tID_HashSet = recs0.getClaimIDToClaimantPersonIDLookup(env._HandleOutOfMemoryError_boolean);
//                tIDIndexes.add(tID_HashSet);
            }

            while (includeIte.hasNext()) {
                i = includeIte.next();
                // Set Year and Month variables
                String YM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                // Load next data
                DW_SHBE_Records recs1;
                recs1 = env.getDW_SHBE_Data().getDW_SHBE_Records(YM31);
                HashMap<DW_ID, String> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = null;//recs1.getClaimDW_IDToPostcodeLookup();
                HashMap<DW_ID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = null;//recs1.getClaimantIDToTenancyTypeLookup();
                String YM31v;
                YM31v = recs1.getNearestYM3ForONSPDLookup();
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
                DW_UO_Set councilUOSet1 = null;
                DW_UO_Set RSLUOSet1 = null;
                if (doUnderOccupied) {
                    if (doCouncil) {
                        councilUOSet1 = councilUnderOccupiedSets.get(YM31);
                    }
                    if (doRSL) {
                        RSLUOSet1 = RSLUnderOccupiedSets.get(YM31);
                    }
                }

                if (true) {
//                    ArrayList<DW_ID> tID_HashSet;
//                    tID_HashSet = recs1.getClaimantClaimIDs(env._HandleOutOfMemoryError_boolean);
//                    tIDIndexes.add(tID_HashSet);
                }
                //records0 = (TreeMap<String, DW_SHBE_Record>) SHBEData0[0];
                HashMap<DW_ID, DW_SHBE_Record> records1;
                records1 = recs1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
                /* Initialise A:
                 * output directories;
                 * claimantTypeTenureLevelTypeDirs;
                 * TTLevelTypeDistanceDirs;
                 * TTAreaCount;
                 * TTDistanceAreaCount.
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
                    TreeMap<String, TreeMap<String, TreeMap<String, File>>> TTLevelTypeDirs;
                    TTLevelTypeDirs = new TreeMap<String, TreeMap<String, TreeMap<String, File>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>> TTLevelTypeDistanceDirs;
                    TTLevelTypeDistanceDirs = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts;
                    TTLevelTypeAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>> TTLevelTypeDistanceAreaCounts;
                    TTLevelTypeDistanceAreaCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts;
                    TTLevelTypeTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>> TTLevelTypeDistanceTenureCounts;
                    TTLevelTypeDistanceTenureCounts = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>();
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT = TTIte.next();
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
                                        TT);
                                outDir1.mkdirs();
                                typeDirs.put(type, outDir1);
                                // Initialise AreaCounts
                                TreeMap<String, Integer> areaCount;
                                areaCount = new TreeMap<String, Integer>();
                                typeAreaCounts.put(type, areaCount);
                                // Initialise TenureCounts
                                TreeMap<Integer, Integer> TTCounts;
                                TTCounts = new TreeMap<Integer, Integer>();
                                typeTenureCounts.put(type, TTCounts);
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
                                            TT);
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
                                    TreeMap<Integer, Integer> TTCounts;
                                    TTCounts = new TreeMap<Integer, Integer>();
                                    distanceTenureCounts.put(distance, TTCounts);
                                }
                                typeDistanceDirs.put(distanceType, distanceDirs);
                                typeDistanceAreaCounts.put(distanceType, distanceAreaCounts);
                                typeDistanceTenureCounts.put(distanceType, distanceTenureCounts);
                            }
                            levelTypeDistanceDirs.put(level, typeDistanceDirs);
                            levelTypeDistanceAreaCounts.put(level, typeDistanceAreaCounts);
                            levelTypeDistanceTenureCounts.put(level, typeDistanceTenureCounts);
                        }
                        TTLevelTypeDirs.put(TT, levelTypeDirs);
                        TTLevelTypeDistanceDirs.put(TT, levelTypeDistanceDirs);
                        TTLevelTypeAreaCounts.put(TT, levelTypeAreaCounts);
                        TTLevelTypeDistanceAreaCounts.put(TT, levelTypeDistanceAreaCounts);
                        TTLevelTypeTenureCounts.put(TT, levelTypeTenureCounts);
                        TTLevelTypeDistanceTenureCounts.put(TT, levelTypeDistanceTenureCounts);
                    }
                    claimantTypeTenureLevelTypeDirs.put(claimantType, TTLevelTypeDirs);
                    claimantTypeTenureLevelTypeDistanceDirs.put(claimantType, TTLevelTypeDistanceDirs);
                    claimantTypeTenureLevelTypeAreaCounts.put(claimantType, TTLevelTypeAreaCounts);
                    claimantTypeTenureLevelTypeDistanceAreaCounts.put(claimantType, TTLevelTypeDistanceAreaCounts);
                    claimantTypeTenureLevelTypeTenureCounts.put(claimantType, TTLevelTypeTenureCounts);
                    claimantTypeTenureLevelTypeDistanceTenureCounts.put(claimantType, TTLevelTypeDistanceTenureCounts);
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
                Iterator<DW_ID> DW_IDIte = records1.keySet().iterator();
                while (DW_IDIte.hasNext()) {
                    DW_ID ClaimID;
                    ClaimID = DW_IDIte.next();
                    DW_SHBE_Record Record1;
                    Record1 = records1.get(ClaimID);
                    DW_SHBE_D_Record DRecord1 = Record1.getDRecord();
                    DW_ID PostcodeID1 = Record1.getPostcodeID();
                    String ClaimPostcodeF;
                    ClaimPostcodeF = Record1.getClaimPostcodeF();
                    Integer TT1Integer = DRecord1.getTenancyType();
                    String TT1 = TT1Integer.toString();
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT;
                        TT = TTIte.next();
                        ArrayList<String> TTs;
                        TTs = TTGroups.get(TT);
                        if (TTs.contains(TT1)) {
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                String level = levelsIte.next();
                                TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                                tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                                TreeMap<String, Integer> unexpectedCounts;
                                unexpectedCounts = levelUnexpectedCounts.get(level);
                                String CTBRef1;
                                CTBRef1 = DRecord1.getCouncilTaxBenefitClaimReferenceNumber();
                                DW_ID ClaimID1;
                                ClaimID1 = DW_SHBE_Data.getClaimRefToClaimIDLookup().get(CTBRef1);
                                DW_ID claimantDW_ID1;
                                claimantDW_ID1 = null;//DW_PersonIDtoDW_IDLookup.get(claimantDW_PersonID1);
                                String claimantType;
                                claimantType = DW_SHBE_Handler.getClaimantType(DRecord1);
                                boolean doAdd = true;
                                // Check for UnderOccupied
                                if (doUnderOccupied) {
                                    // UnderOccupancy
                                    boolean councilDoAdd = false;
                                    if (doCouncil) {
                                        DW_UO_Record councilUO0 = null;
                                        DW_UO_Record councilUO1 = null;
                                        if (councilUOSet0 != null) {
                                            councilUO0 = councilUOSet0.getMap().get(
                                                    ClaimID1);
                                        }
                                        if (councilUOSet1 != null) {
                                            councilUO1 = councilUOSet1.getMap().get(
                                                    ClaimID1);
                                        }
                                        councilDoAdd = councilUO0 != null || councilUO1 != null;
                                    }
                                    boolean RSLDoAdd = false;
                                    if (doCouncil) {
                                        DW_UO_Record RSLUO0 = null;
                                        DW_UO_Record RSLUO1 = null;
                                        if (RSLUOSet0 != null) {
                                            RSLUO0 = RSLUOSet0.getMap().get(
                                                    ClaimID);
                                        }
                                        if (RSLUOSet1 != null) {
                                            RSLUO1 = RSLUOSet1.getMap().get(
                                                    ClaimID);
                                        }
                                        RSLDoAdd = RSLUO0 != null || RSLUO1 != null;
                                    }
                                    if (councilDoAdd || RSLDoAdd) {
                                        doAdd = false;
                                    }
                                }
                                if (doAdd) {
                                    if (PostcodeID1 != null) {
                                        String areaCode;
                                        areaCode = getAreaCode(
                                                level,
                                                tLookupFromPostcodeToLevelCode,
                                                ClaimPostcodeF);
                                        if (areaCode != null) {
                                            String type;
                                            type = sAllClaimants;
                                            Integer TTInt;
                                            TTInt = DRecord1.getTenancyType();
                                            if (types.contains(type)) {
                                                addToResult(
                                                        claimantTypeTenureLevelTypeAreaCounts,
                                                        claimantTypeTenureLevelTypeTenureCounts,
                                                        areaCode,
                                                        claimantType,
                                                        TT,
                                                        level,
                                                        type,
                                                        TTInt);
                                            }
                                            if (areaCode != null) {
                                                DW_SHBE_Record record0 = records0.get(ClaimID);
                                                DW_ID PostcodeID0;
                                                if (record0 == null) {
//                                        //This is a new entrant to the data
//                                        type = "NewEntrant";
                                                    // If this claimantNINO has never been seen before it is an OnFlow
                                                    boolean hasBeenSeenBefore;
                                                    hasBeenSeenBefore = getHasClaimantBeenSeenBefore(
                                                            claimantDW_ID1,
                                                            i,
                                                            include,
                                                            tIDIndexes);
                                                    if (!hasBeenSeenBefore) {
                                                        type = sOnFlow;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
                                                    } else {
                                                        // If this claimantNINO has been seen before it is a ReturnFlow
                                                        type = sReturnFlow;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
// Here we could also try to work out for those Return flows, have any moved from previous claim postcode or changed TT.
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
                                                    PostcodeID0 = record0.getPostcodeID();
                                                    if (PostcodeID0 == null) {
                                                        // Unknown
                                                        type = sUnknown;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
                                                    } else /*
                                                     * There is an issue here as it seems that sometimes a postcode is misrecorded 
                                                     * initially and is then corrected. Some thought is needed about how to identify
                                                     * and deal with this and discern if this has any significant effect on the 
                                                     * results.
                                                     */ if (PostcodeID0.equals(PostcodeID1)) {
                                                        // Stable
                                                        type = sStable;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
                                                    } else {
                                                        // AllInChurn
                                                        type = sAllInChurn;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
                                                        // AllOutChurn
                                                        type = sAllOutChurn;
                                                        if (types.contains(type)) {
                                                            addToResult(
                                                                    claimantTypeTenureLevelTypeAreaCounts,
                                                                    claimantTypeTenureLevelTypeTenureCounts,
                                                                    areaCode,
                                                                    claimantType,
                                                                    TT,
                                                                    level,
                                                                    type,
                                                                    TTInt);
                                                        }
                                                        double distance;
                                                        distance = DW_Postcode_Handler.getDistanceBetweenPostcodes(
                                                                YM30v,
                                                                YM31v,
                                                                PostcodeID0,
                                                                PostcodeID1);
                                                        Iterator<Double> ite3;
                                                        ite3 = distances.iterator();
                                                        while (ite3.hasNext()) {
                                                            double distanceThreshold = ite3.next();
                                                            if (distance > distanceThreshold) {
                                                                // InDistanceChurn
                                                                type = sInDistanceChurn;
                                                                if (distanceTypes.contains(type)) {
                                                                    addToResult(
                                                                            claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                            claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                            areaCode,
                                                                            claimantType,
                                                                            TT,
                                                                            level,
                                                                            type,
                                                                            TTInt,
                                                                            distanceThreshold);
                                                                }
                                                                // OutDistanceChurn
                                                                type = sOutDistanceChurn;
                                                                if (distanceTypes.contains(type)) {
                                                                    addToResult(
                                                                            claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                            claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                            areaCode,
                                                                            claimantType,
                                                                            TT,
                                                                            level,
                                                                            type,
                                                                            TTInt,
                                                                            distanceThreshold);
                                                                }
                                                            } else {
                                                                // WithinDistanceChurn
                                                                type = sWithinDistanceChurn;
                                                                if (distanceTypes.contains(type)) {
                                                                    addToResult(
                                                                            claimantTypeTenureLevelTypeDistanceAreaCounts,
                                                                            claimantTypeTenureLevelTypeDistanceTenureCounts,
                                                                            areaCode,
                                                                            claimantType,
                                                                            TT,
                                                                            level,
                                                                            type,
                                                                            TTInt,
                                                                            distanceThreshold);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                //env.logO("No Census code for postcode: " + postcode1);
                                                Generic_Collections.addToTreeMapStringInteger(
                                                        unexpectedCounts, ClaimPostcodeF, 1);
                                            }
                                        }
                                    }
                                } else {
                                    Generic_Collections.addToTreeMapStringInteger(
                                            unexpectedCounts, snull, 1);
                                }
                            }
                        }
                    }
                }
                // Write out results
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
                claimantTypeTenureLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>();
                yearMonthClaimantTypeTenureLevelTypeCountAreas.put(YM31, claimantTypeTenureLevelTypeCountAreas);
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
                claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>();
                yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(YM31, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType = claimantTypesIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                    TTLevelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>();
                    claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>> TTLevelDistanceTypeDistanceCountAreas;
                    TTLevelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>();
                    claimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(claimantType, TTLevelDistanceTypeDistanceCountAreas);
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT;
                        TT = TTIte.next();
                        TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                        levelTypeCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>();
                        TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
                        TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>> levelDistanceTypeDistanceCountAreas;
                        levelDistanceTypeDistanceCountAreas = new TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>();
                        TTLevelDistanceTypeDistanceCountAreas.put(TT, levelDistanceTypeDistanceCountAreas);
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
                                TreeMap<Integer, TreeSet<String>> YM31CountAreas;
                                TreeMap<Integer, Integer> TTCounts;
                                File dir;
                                areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(TT).get(level).get(type);
                                YM31CountAreas = yearMonthClaimantTypeTenureLevelTypeCountAreas.get(YM31).get(claimantType).get(TT).get(level).get(type);
                                TTCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(TT).get(level).get(type);
                                dir = claimantTypeTenureLevelTypeDirs.get(claimantType).get(TT).get(level).get(type);
                                TreeMap<Integer, TreeSet<String>> countAreas;
                                countAreas = writeResults(
                                        areaCounts,
                                        YM31CountAreas,
                                        YM31,
                                        TTCounts,
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
                                    TreeMap<Integer, TreeSet<String>> YM31CountAreas;
                                    TreeMap<Integer, Integer> TTCounts;
                                    File dir;
                                    areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(TT).get(level).get(distanceType).get(distance);
                                    YM31CountAreas = yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.get(YM31).get(claimantType).get(TT).get(level).get(distanceType).get(distance);
                                    TTCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(TT).get(level).get(distanceType).get(distance);
                                    dir = claimantTypeTenureLevelTypeDistanceDirs.get(claimantType).get(TT).get(level).get(distanceType).get(distance);
                                    TreeMap<Integer, TreeSet<String>> countAreas;
                                    countAreas = writeResults(
                                            areaCounts,
                                            YM31CountAreas,
                                            YM31,
                                            TTCounts,
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
                            if (!unexpectedCounts.isEmpty()) {
                                if (env.DEBUG_Level < env.DEBUG_Level_NORMAL) {
                                    env.logO("Unexpected Counts for:"
                                            + " Claimant Type " + claimantType
                                            + " Tenure " + TT
                                            + " Level " + level, true);
                                    env.logO("code,count", true);
                                    Iterator<String> unexpectedCountsIte;
                                    unexpectedCountsIte = unexpectedCounts.keySet().iterator();
                                    while (unexpectedCountsIte.hasNext()) {
                                        String code = unexpectedCountsIte.next();
                                        Integer count = unexpectedCounts.get(code);
                                        env.logO("" + code + ", " + count, true);
                                    }
                                }
                            }
                        }
                    }
                }
                recs0 = recs1;
                if (doCouncil) {
                    councilUOSet0 = councilUOSet1;
                }
                if (doRSL) {
                    RSLUOSet0 = RSLUOSet1;
                }
                records0 = records1;
                //ClaimIDToPostcodeIDLookup0 = ClaimIDToPostcodeIDLookup1;
                YM30 = YM31;
                YM30v = YM31v;
            }
        }
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
     * @param tUnderOccupancies
     * @param UOCouncilClaimIDSets
     * @param UORSLClaimIDSets
     * @param i
     * @param include
     * @return Previous TenanctType and Postcode
     */
    public Object[] getPreviousTTAndPU(
            HashMap<Integer, HashMap<DW_ID, DW_ID>> ClaimIDToPostcodeIDLookups,
            HashMap<Integer, String> indexYM3s,
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<Integer, Set<DW_ID>> UOCouncilClaimIDSets,
            HashMap<Integer, Set<DW_ID>> UORSLClaimIDSets,
            int i,
            ArrayList<Integer> include) {
        Object[] result;
        result = new Object[4];
        ListIterator<Integer> li;
        int index;
        index = include.indexOf(i);
        String YM3;
        YM3 = indexYM3s.get(index);

        DW_ID postcode;
        //postcode = sAAN_NAA;
        postcode = null;

        HashMap<DW_ID, DW_ID> ClaimIDToPostcodeLookup;

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

            ClaimIDToPostcodeLookup = ClaimIDToPostcodeIDLookups.get(previousIndex);

//            env.logO("previousIndex " + previousIndex);
            HashMap<DW_ID, Integer> ClaimIDToTTLookup;
            ClaimIDToTTLookup = ClaimIDToTTLookups.get(previousIndex);
            if (ClaimIDToTTLookup != null) {
                TT = ClaimIDToTTLookup.get(ClaimID);
                Set<DW_ID> tUnderOccupancy;
                tUnderOccupancy = tUnderOccupancies.get(previousIndex);
                if (tUnderOccupancy != null) {
                    if (tUnderOccupancy.contains(ClaimID)) {
                        underOccupancy = sU;
                        if (TT == null) {
                            TT = -999;
                            Set<DW_ID> UOCouncilClaimIDSet;
                            UOCouncilClaimIDSet = UOCouncilClaimIDSets.get(previousIndex);
                            if (UOCouncilClaimIDSet.contains(ClaimID)) {
                                result[0] = Integer.toString(TT) + underOccupancy + "1";
                            } else {
                                result[0] = Integer.toString(TT) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            result[1] = previousIndex;

                            postcode = ClaimIDToPostcodeLookup.get(ClaimID);
//                                    if (postcode == null) {
//                                        postcode = sAAN_NAA;
//                                    }
                            result[2] = postcode;
                            return result;
                        } else if (TT == -999) {
                            Set<DW_ID> UORSLClaimIDSet;
                            UORSLClaimIDSet = UOCouncilClaimIDSets.get(previousIndex);
                            if (UORSLClaimIDSet.contains(ClaimID)) {
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
                            postcode = ClaimIDToPostcodeLookup.get(ClaimID);
//                                    if (postcode == null) {
//                                        postcode = sAAN_NAA;
//                                    }
                            result[2] = postcode;
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
        result[2] = env.getDW_SHBE_Data().getPostcodeIDToPostcodeLookup().get(postcode);
        result[3] = YM3;
        return result;
    }

    /**
     *
     * @param ClaimID
     * @param ClaimIDToTTLookups
     * @param tUnderOccupancies
     * @param tUnderOccupanciesCouncil
     * @param tUnderOccupanciesRSL
     * @param i
     * @param include
     * @return
     */
    public Object[] getPreviousTT(
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
            HashMap<Integer, Set<DW_ID>> tUnderOccupanciesCouncil,
            HashMap<Integer, Set<DW_ID>> tUnderOccupanciesRSL,
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
            HashMap<DW_ID, Integer> tIDByTenancyType;
            tIDByTenancyType = ClaimIDToTTLookups.get(previousIndex);
            Set<DW_ID> tUnderOccupancy;
            tUnderOccupancy = tUnderOccupancies.get(previousIndex);
            if (tIDByTenancyType != null) {
                TT = tIDByTenancyType.get(ClaimID);
                if (tUnderOccupancy.contains(ClaimID)) {
                    underOccupancy = sU;
                    if (TT == null) {
                        TT = -999;
                        Set<DW_ID> tUnderOccupancyCouncil;
                        tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                        if (tUnderOccupancyCouncil.contains(ClaimID)) {
                            result[0] = Integer.toString(TT) + underOccupancy + "1";
                        } else {
                            result[0] = Integer.toString(TT) + underOccupancy + "4";
                        }
                        //result[1] = include.indexOf(previousIndex);
                        result[1] = previousIndex;
                        return result;
                    } else if (TT == -999) {
                        Set<DW_ID> tUnderOccupancyCouncil;
                        tUnderOccupancyCouncil = tUnderOccupanciesCouncil.get(previousIndex);
                        if (tUnderOccupancyCouncil.contains(ClaimID)) {
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
     * @param tUnderOccupancies
     * @return
     */
    public Object[] getPreviousTT(
            DW_ID ClaimID,
            HashMap<Integer, HashMap<DW_ID, Integer>> ClaimIDToTTLookups,
            HashMap<Integer, Set<DW_ID>> tUnderOccupancies,
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
            HashMap<DW_ID, Integer> ClaimIDToTTLookup;
            ClaimIDToTTLookup = ClaimIDToTTLookups.get(previousIndex);
            Set<DW_ID> tUnderOccupancy;
            tUnderOccupancy = tUnderOccupancies.get(previousIndex);
            if (ClaimIDToTTLookup != null) {
                TT = ClaimIDToTTLookup.get(ClaimID);
                if (tUnderOccupancy.contains(ClaimID)) {
                    underOccupancy = sU;
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
     * @param UOClaimIDSet0
     * @param UOClaimIDSets
     * @param UOClaimIDSetsCouncil
     * @param UOClaimIDSetsRSL
     * @param TTTs
     * @param YM30
     * @param YM31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param indexYM3s
     * @param underOccupiedSetCouncil0
     * @param underOccupiedSetRSL0
     * @param UOClaimIDSet1
     * @param underOccupiedSetCouncil1
     * @param underOccupiedSetRSL1
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
            HashMap<Integer, Set<DW_ID>> UOClaimIDSetsCouncil,
            HashMap<Integer, Set<DW_ID>> UOClaimIDSetsRSL,
            HashMap<DW_ID, ArrayList<String>> TTTs,
            String YM30,
            String YM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            HashMap<Integer, String> indexYM3s,
            DW_UO_Set UOClaimIDSet0,
            DW_UO_Set underOccupiedSetCouncil0,
            DW_UO_Set underOccupiedSetRSL0,
            DW_UO_Set UOClaimIDSet1,
            DW_UO_Set underOccupiedSetCouncil1,
            DW_UO_Set underOccupiedSetRSL1,
            Set<DW_ID> ClaimIDs
    ) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<String, TreeMap<String, Integer>>();

        //originals = underOccupiedInApril2013; // This is only for not starting UO run horrible hack!
        //checkPreviousTenure = true; // YAHH!!!
        Iterator<DW_ID> ite;
        ite = ClaimIDs.iterator();
        while (ite.hasNext()) {
            DW_ID ClaimID;
            ClaimID = ite.next();
            DW_SHBE_Record Record0;
            Record0 = Records0.get(ClaimID);
            DW_SHBE_Record Record1;
            Record1 = Records1.get(ClaimID);
            String sTT0;
            if (Records0.containsKey(ClaimID)) {
                sTT0 = Integer.toString(Record0.getDRecord().getTenancyType());
            } else {
                sTT0 = sMinus999;
            }
            String sTT1;
            if (Records1.containsKey(ClaimID)) {
                sTT1 = Integer.toString(Record1.getDRecord().getTenancyType());
            } else {
                sTT1 = sMinus999;
            }
            DW_UO_Record DW_UO_Record0 = null;
            DW_UO_Record DW_UO_Record1 = null;
            DW_UO_Record0 = UOClaimIDSet0.getMap().get(ClaimID);
            DW_UO_Record1 = UOClaimIDSet1.getMap().get(ClaimID);
            String pc0 = sAAN_NAA;
            String pc1 = sAAN_NAA;
            if (checkPreviousTenure) {
                if (sTT0.equalsIgnoreCase(sMinus999)) {
                    if (DW_UO_Record0 != null) {
                        sTT0 += sU;
                        if (underOccupiedSetCouncil0.getMap().containsKey(ClaimID)) {
                            sTT0 += "1";
                        }
                        if (underOccupiedSetRSL0.getMap().containsKey(ClaimID)) {
                            sTT0 += "4";
                        }
                        pc0 = sAAN_NAA;
                    } else {
                        Object[] PreviousTTAndP;
                        PreviousTTAndP = getPreviousTTAndPU(
                                ClaimIDToPostcodeIDLookups,
                                indexYM3s,
                                ClaimID,
                                ClaimIDToTTLookups,
                                UOClaimIDSets,
                                UOClaimIDSetsCouncil,
                                UOClaimIDSetsRSL,
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
                    try {
                        pc0 = Records0.get(ClaimID).getDRecord().getClaimantsPostcode();
                    } catch (NullPointerException e) {
                    }
                }
                if (sTT1.equalsIgnoreCase(sMinus999)) {
                    if (DW_UO_Record1 != null) {
                        sTT1 += sU;
                        if (underOccupiedSetCouncil1.getMap().containsKey(ClaimID)) {
                            sTT1 += "1";
                        }
                        if (underOccupiedSetRSL1.getMap().containsKey(ClaimID)) {
                            sTT1 += "4";
                        }
                        try {
                            pc1 = Records1.get(ClaimID).getDRecord().getClaimantsPostcode();
                        } catch (NullPointerException e) {
                        }
                    } else {
                        Object[] previousTTAndP;
                        previousTTAndP = getPreviousTTAndPU(
                                ClaimIDToPostcodeIDLookups,
                                indexYM3s,
                                ClaimID,
                                ClaimIDToTTLookups,
                                UOClaimIDSets,
                                UOClaimIDSetsCouncil,
                                UOClaimIDSetsRSL,
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
                    try {
                        pc1 = Records1.get(ClaimID).getDRecord().getClaimantsPostcode();
                    } catch (NullPointerException e) {
                    }
                }
            }
            if (checkPreviousTenure) {
                if (sTT1.equalsIgnoreCase(sMinus999)) {
                    Object[] previousTenure;
                    previousTenure = DW_ProcessorLCCAggregate.this.getPreviousTT(
                            ClaimID,
                            ClaimIDToTTLookups,
                            UOClaimIDSets,
                            UOClaimIDSetsCouncil,
                            UOClaimIDSetsRSL,
                            index,
                            include);
                    sTT1 = (String) previousTenure[0];
                }
            }
            if (!checkPreviousTenure) {
                if (DW_UO_Record0 != null) {
                    sTT0 += sU;
                    if (sTT0.startsWith(sMinus999)) {
                        if (underOccupiedSetCouncil0.getMap().containsKey(ClaimID)) {
                            sTT0 += "1";
                        }
                        if (underOccupiedSetRSL0.getMap().containsKey(ClaimID)) {
                            sTT0 += "4";
                        }
                    }
                }
                if (DW_UO_Record1 != null) {
                    sTT1 += sU;
                    if (sTT1.startsWith(sMinus999)) {
                        if (underOccupiedSetCouncil1.getMap().containsKey(ClaimID)) {
                            sTT1 += "1";
                        }
                        if (underOccupiedSetRSL1.getMap().containsKey(ClaimID)) {
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
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (ClaimIDs.contains(ClaimID)) {
                // UnderOccupancy
                DW_UO_Record DW_UO_Record0 = null;
                DW_UO_Record DW_UO_Record1 = null;
                if (UOClaimIDSet0 != null) {
                    DW_UO_Record0 = UOClaimIDSet0.getMap().get(ClaimID);
                }
                if (UOClaimIDSet1 != null) {
                    DW_UO_Record1 = UOClaimIDSet1.getMap().get(ClaimID);
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
            DW_ID ClaimID;
            ClaimID = ite.next();
            if (ClaimIDs.contains(ClaimID)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UO_Record DW_UO_Record0 = null;
                DW_UO_Record DW_UO_Record1 = null;
                if (UOClaimIDSet0 != null) {
                    if (ClaimIDs != null) {
                        if (ClaimIDs.contains(ClaimID)) {
                            DW_UO_Record0 = UOClaimIDSet0.getMap().get(ClaimID);
                        }
                    } else {
                        DW_UO_Record0 = UOClaimIDSet0.getMap().get(ClaimID);
                    }
                }
                if (UOClaimIDSet1 != null) {
                    if (ClaimIDs != null) {
                        if (ClaimIDs.contains(ClaimID)) {
                            DW_UO_Record1 = UOClaimIDSet1.getMap().get(ClaimID);
                        }
                    } else {
                        DW_UO_Record1 = UOClaimIDSet1.getMap().get(ClaimID);
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
                    String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
                    String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
                        String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
//                                    isValidPostcode0 = DW_Postcode_Handler.isMappablePostcode(postcode0);
//                                }
//                                String postcode1;
//                                postcode1 = ClaimIDToPostcodeIDLookup1.get(tID);
//                                boolean isValidPostcode1 = false;
//                                if (postcode1 != null) {
//                                    isValidPostcode1 = DW_Postcode_Handler.isMappablePostcode(postcode1);
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
//                                doPostcodeChanges.add(postcodeChangeResult);
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
                        String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
                            String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
                            TTChange = DW_ProcessorLCCAggregate.this.getTTTName(
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
                        String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
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
                            String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
                                    TT0,
                                    underOccupied0 != null,
                                    TT1,
                                    underOccupied1 != null);
                            TTChange = ttc[0];
                            TT0 = ttc[1];
                            TT1 = ttc[2];
                        } else {
                            TTChange = DW_ProcessorLCCAggregate.this.getTTTName(
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
                            String[] ttc = DW_ProcessorLCCAggregate.this.getTTTName(
                                    TT0,
                                    underOccupied0 != null,
                                    TT1,
                                    false);
                            TTChange = ttc[0];
                            TT0 = ttc[1];
                            TT1 = ttc[2];
                        } else {
                            TTChange = DW_ProcessorLCCAggregate.this.getTTTName(
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
     * @param Ts
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
            Logger.getLogger(DW_ProcessorLCCAggregate.class
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

    /**
     * A method used for creating some preliminary results.
     *
     * @param SHBEData_Sets
     * @param underOccupiedReport_Sets
     */
    public void processSHBEReportData(
            ArrayList<Object[]> SHBEData_Sets,
            ArrayList<DW_UO_Set>[] underOccupiedReport_Sets) {
//        /*
//         * 0 Apr 2013 14 Under Occupied Report For University Year Start Council Tenants.csv
//         * 1 May 2013 14 Under Occupied Report For University Month 1 Council Tenants.csv
//         * 2 Jun 2013 14 Under Occupied Report For University Month 2 Council Tenants.csv
//         * 3 Jul 2013 14 Under Occupied Report For University Month 3 Council Tenants.csv
//         * 4 Aug 2013 14 Under Occupied Report For University Month 4 Council Tenants.csv
//         * 5 Sep 2013 14 Under Occupied Report For University Month 5 Council Tenants.csv
//         * 6 Oct 2013 14 Under Occupied Report For University Month 6 Council Tenants.csv
//         * 7 Nov 2013 14 Under Occupied Report For University Month 7 Council Tenants.csv
//         * 8 Dec 2013 14 Under Occupied Report For University Month 8 Council Tenants.csv
//         * 9 Jan 2013 14 Under Occupied Report For University Month 9 Council Tenants.csv
//         * 10 Feb 2013 14 Under Occupied Report For University Month 10 Council Tenants.csv
//         * 11 Mar 2013 14 Under Occupied Report For University Month 11 Council Tenants.csv
//         * 12 Apr 2013 14 Under Occupied Report For University Month 12 Council Tenants.csv
//         * 13 May 2014 15 Under Occupied Report For University Month 1 Council Tenants.csv
//         * 14 Jun 2014 15 Under Occupied Report For University Month 2 Council Tenants.csv
//         * 15 Jul 2014 15 Under Occupied Report For University Month 3 Council Tenants.csv
//         */
// /*
//         * 0 hb9803_SHBE_206728k April 2008.csv
//         * 1 hb9803_SHBE_234696k October 2008.csv
//         * 2 hb9803_SHBE_265149k April 2009.csv
//         * 3 hb9803_SHBE_295723k October 2009.csv
//         * 4 hb9803_SHBE_329509k April 2010.csv
//         * 5 hb9803_SHBE_363186k October 2010.csv
//         * 6 hb9803_SHBE_391746k March 2011.csv
//         * 7 hb9803_SHBE_397524k April 2011.csv
//         * 8 hb9803_SHBE_415181k July 2011.csv
//         * 9 hb9803_SHBE_433970k October 2011.csv
//         * 10 hb9803_SHBE_451836k January 2012.csv
//         * 11 hb9803_SHBE_470742k April 2012.csv
//         * 12 hb9803_SHBE_490903k July 2012.csv
//         * 13 hb9803_SHBE_511038k October 2012.csv
//         * 14 hb9803_SHBE_530243k January 2013.csv
//         * 15 hb9803_SHBE_536123k February 2013.csv
//         * 16 hb9991_SHBE_543169k March 2013.csv
//         * 17 hb9991_SHBE_549416k April 2013.csv
//         * 18 hb9991_SHBE_555086k May 2013.csv
//         * 19 hb9991_SHBE_562036k June 2013.csv
//         * 20 hb9991_SHBE_568694k July 2013.csv
//         * 21 hb9991_SHBE_576432k August 2013.csv
//         * 22 hb9991_SHBE_582832k September 2013.csv
//         * 23 hb9991_SHBE_589664k Oct 2013.csv
//         * 24 hb9991_SHBE_596500k Nov 2013.csv
//         * 25 hb9991_SHBE_603335k Dec 2013.csv
//         * 26 hb9991_SHBE_609791k Jan 2014.csv
//         * 27 hb9991_SHBE_615103k Feb 2014.csv
//         * 28 hb9991_SHBE_621666k Mar 2014.csv
//         * 29 hb9991_SHBE_629066k Apr 2014.csv
//         */
// /*
//         * Correspondence between data
//         * UnderoccupancyIndex, SHBEIndex
//         * 0, 17
//         * 1, 18
//         * 2, 19
//         * 3, 20
//         * 4, 21
//         * 5, 22
//         * 6, 23
//         * 7, 24
//         * 8, 25
//         * 9, 26
//         * 10, 27
//         * 11, 28
//         * 12, 29
//         */
//        String[] dates;
//        dates = new String[13];
//        dates[0] = "2013-04";
//        dates[1] = "2013-05";
//        dates[2] = "2013-06";
//        dates[3] = "2013-07";
//        dates[4] = "2013-08";
//        dates[5] = "2013-09";
//        dates[6] = "2013-10";
//        dates[7] = "2013-11";
//        dates[8] = "2013-12";
//        dates[9] = "2014-01";
//        dates[10] = "2014-02";
//        dates[11] = "2014-03";
//        dates[12] = "2014-04";
//        ArrayList<DW_UO_Set> councilRecords;
//        councilRecords = underOccupiedReport_Sets[0];
//        PrintWriter pwAggregate;
//        pwAggregate = init_OutputTextFilePrintWriter(
//                DW_Files.getOutputUnderOccupiedDir(),
//                "DigitalWelfareOutputUnderOccupiedReport" + dates[0] + "To" + dates[dates.length - 1] + ".txt");
//        TreeMap<String, BigDecimal> postcodeTotalArrearsTotal = new TreeMap<String, BigDecimal>();
//        TreeMap<String, Integer> postcodeClaimsTotal = new TreeMap<String, Integer>();
//        int UnderoccupancyIndex;
//        int SHBEIndex;
//        for (int i = 0; i < dates.length; i++) {
//            PrintWriter pw;
//            pw = init_OutputTextFilePrintWriter(
//                    DW_Files.getOutputUnderOccupiedDir(),
//                    "DigitalWelfareOutputUnderOccupiedReport" + dates[i] + ".txt");
//            UnderoccupancyIndex = i;
//            SHBEIndex = i + 17;
//            DW_UO_Set set;
//            set = councilRecords.get(UnderoccupancyIndex);
//            Object[] SHBESet;
//            SHBESet = SHBEData_Sets.get(SHBEIndex);
//
////            TreeMap<String, DW_SHBE_Record> DRecords;
////            DRecords = (TreeMap<String, DW_SHBE_Record>) SHBESet[0];
//            DW_SHBE_DataPT DW_SHBE_DataPT;
//            DW_SHBE_DataPT = (DW_SHBE_DataPT) SHBESet[0];
//
//            TreeMap<String, DW_SHBE_S_Record> SRecordsWithoutDRecord;
//            SRecordsWithoutDRecord = (TreeMap<String, DW_SHBE_S_Record>) SHBESet[1];
//            // Iterate over councilRecords and join these with SHBE records
//            // Aggregate totalRentArrears by postcode
//            int aggregations = 0;
//            int totalSRecordCount = 0;
//            int countNotMissingDRecords = 0;
//            int countMissingDRecords = 0;
//            BigDecimal totalRentArrears_BigDecimal = BigDecimal.ZERO;
//            TreeMap<String, BigDecimal> postcodeTotalArrears = new TreeMap<String, BigDecimal>();
//            TreeMap<String, Integer> postcodeClaims = new TreeMap<String, Integer>();
//            TreeMap<String, DW_UO_Record> map = set.getMap();
//            Iterator<String> ite2;
//            ite2 = map.keySet().iterator();
//            DW_ID DW_ID;
//            while (ite2.hasNext()) {
//                DW_ID = ite2.next();
//                DW_UO_Record underOccupiedReport_DataRecord;
//                underOccupiedReport_DataRecord = map.get(DW_ID);
//                double rentArrears = underOccupiedReport_DataRecord.getTotalRentArrears();
//                BigDecimal rentArrears_BigDecimal = new BigDecimal(rentArrears);
//                totalRentArrears_BigDecimal = totalRentArrears_BigDecimal.add(rentArrears_BigDecimal);
//                DW_SHBE_Record record;
//                record = handler.getData(DW_ID);
//                DW_SHBE_D_Record DRecord;
//                DRecord = record.getDRecord();
//                if (DRecord == null) {
//                    env.logO("Warning: No DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
//                    countMissingDRecords++;
//                    DW_SHBE_S_Record SRecord = SRecordsWithoutDRecord.get(DW_ID);
//                    if (SRecord != null) {
//                        int dosomething = 1;
//                        env.logO("There is a SRecord without a DRecord for underOccupiedReport_DataRecord " + underOccupiedReport_DataRecord);
//                    }
//                } else {
//                    countNotMissingDRecords++;
//                    String postcode = DRecord.getClaimantsPostcode();
//                    String truncatedPostcode = postcode.substring(0, postcode.length() - 2);
//                    int SRecordCount = record.getSRecordsWithoutDRecords().size();
//                    totalSRecordCount += SRecordCount;
//                    if (rentArrears < 0) {
//                        int debug = 0;
//                    }
//                    // Add to totals for this month
//                    if (postcodeTotalArrears.containsKey(truncatedPostcode)) {
//                        BigDecimal current = postcodeTotalArrears.get(truncatedPostcode);
//                        BigDecimal arrears = current.add(rentArrears_BigDecimal);
//                        postcodeTotalArrears.put(truncatedPostcode, arrears);
//                        aggregations++;
//                    } else {
//                        postcodeTotalArrears.put(truncatedPostcode, rentArrears_BigDecimal);
//                    }
//                    if (postcodeClaims.containsKey(truncatedPostcode)) {
//                        int current = postcodeClaims.get(truncatedPostcode);
//                        postcodeClaims.put(truncatedPostcode, current + 1);
//                    } else {
//                        postcodeClaims.put(truncatedPostcode, 1);
//                    }
//                    // Add to total for all months
//                    if (postcodeTotalArrearsTotal.containsKey(truncatedPostcode)) {
//                        BigDecimal current = postcodeTotalArrearsTotal.get(truncatedPostcode);
//                        BigDecimal arrears = current.add(rentArrears_BigDecimal);
//                        postcodeTotalArrearsTotal.put(truncatedPostcode, arrears);
//                        aggregations++;
//                    } else {
//                        postcodeTotalArrearsTotal.put(truncatedPostcode, rentArrears_BigDecimal);
//                    }
//                    if (postcodeClaimsTotal.containsKey(truncatedPostcode)) {
//                        int current = postcodeClaimsTotal.get(truncatedPostcode);
//                        postcodeClaimsTotal.put(truncatedPostcode, current + 1);
//                    } else {
//                        postcodeClaimsTotal.put(truncatedPostcode, 1);
//                    }
//                    //env.logO("" + underOccupiedReport_DataRecord + ", " + SRecordCount + ", " + postcode);
//                }
//            }
//            // Report for each month
//            pw.println("countNotMissingDRecords " + countNotMissingDRecords);
//            pw.println("totalRentArrears " + totalRentArrears_BigDecimal.setScale(2, RoundingMode.HALF_UP));
//            pw.println("Count of aggregations " + aggregations);
//            pw.println("countMissingDRecords " + countMissingDRecords);
//            pw.println("totalSRecordCount " + totalSRecordCount);
//            pw.println("postcode, claims, arrears");
//            ite2 = postcodeTotalArrears.keySet().iterator();
//            while (ite2.hasNext()) {
//                String postcode = ite2.next();
//                int claims = postcodeClaims.get(postcode);
//                BigDecimal arrears = postcodeTotalArrears.get(postcode);
//
//                // Format postcode
//                postcode = postcode.trim();
//                String[] postcodeSplit = postcode.split(" ");
//                if (postcodeSplit.length > 3) {
//                    int debug = 1;
//                    postcode = "mangled";
//                } else if (postcodeSplit.length == 3) {
//                    postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
//                } else if (postcodeSplit.length == 2) {
//                    postcode = postcodeSplit[0] + " " + postcodeSplit[1];
//                }
//                // Write answer
//                pw.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP));
//            }
//            pw.close();
//        }
//        // Report for all months aggregated
//        Iterator<String> ite2 = postcodeTotalArrearsTotal.keySet().iterator();
//        while (ite2.hasNext()) {
//            String postcode = ite2.next();
//            int claims = postcodeClaimsTotal.get(postcode);
//            BigDecimal arrears = postcodeTotalArrearsTotal.get(postcode);
//
//            // Format postcode
//            postcode = postcode.trim();
//            String[] postcodeSplit = postcode.split(" ");
//            if (postcodeSplit.length > 3) {
//                int debug = 1;
//                postcode = "mangled";
//            } else if (postcodeSplit.length == 3) {
//                postcode = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
//            } else if (postcodeSplit.length == 2) {
//                postcode = postcodeSplit[0] + " " + postcodeSplit[1];
//            }
//            // Write answer
//            pwAggregate.println(postcode + ", " + claims + ", " + arrears.setScale(2, RoundingMode.HALF_UP));
//        }
//        pwAggregate.close();
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
            ArrayList<DW_UO_Set>[] underOccupiedReport_Sets) {
        ArrayList<DW_UO_Set> councilRecords;
        ArrayList<DW_UO_Set> registeredSocialLandlordRecords;
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
            ArrayList<DW_UO_Set>[] aUnderOccupiedReport_Set,
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
     * @return Object[] result where: result[0] is a
     * {@code TreeMap<String, TreeMap<String, Integer>>} Migration Matrix;
     * result[1] is a {@code TreeSet<String>} of origins/destinations.
     */
    public Object[] getAllClaimantMigrationData(
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
        TreeMap<String, TreeMap<String, Integer>> resultMatrix;
        resultMatrix = new TreeMap<String, TreeMap<String, Integer>>();
        result[0] = resultMatrix;
        TreeSet<String> originsAndDestinations;
        originsAndDestinations = new TreeSet<String>();
        result[1] = originsAndDestinations;
        String YM30;
        YM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[startIndex]);
        DW_SHBE_Records recs0;
        recs0 = DW_SHBE_Data.getDW_SHBE_Records(YM30);
//        recs0 = env.getDW_SHBE_Data().getData().get(YM30);
        HashMap<DW_ID, DW_SHBE_Record> DRecordsStart = recs0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        String YM31;
        YM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[endIndex]);
        DW_SHBE_Records recs1;
        recs1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
        //recs1 = env.getDW_SHBE_Data().getData().get(YM31);
        HashMap<DW_ID, DW_SHBE_Record> DRecordsEnd = recs1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<DW_ID> ite;
        ite = DRecordsStart.keySet().iterator();
        DW_ID DW_ID;
        int stayPutCount = 0;
        while (ite.hasNext()) {
            DW_ID = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(DW_ID).getDRecord();
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

                DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(DW_ID).getDRecord();
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
            //env.logO("stayPutCount " + stayPutCount);
        }
        env.logO("stayPutCount " + stayPutCount, true);
        // Add to matrix from unknown origins
        ite = DRecordsEnd.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID = ite.next();
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(DW_ID).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(DW_ID).getDRecord();
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
//                    env.logO("Person a claimant 1 year ago, so we know about them");
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
     * @param aPT
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
            String aPT,
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
        String YM30;
        YM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[startIndex]);
        DW_SHBE_Records recs0;
        recs0 = DW_SHBE_Data.getDW_SHBE_Records(YM30);
//        recs0 = env.getDW_SHBE_Data().getData().get(YM30);
        HashMap<DW_ID, DW_SHBE_Record> DRecordsStart = recs0.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        String YM31;
        YM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[startIndex]);
        DW_SHBE_Records recs1;
        recs1 = DW_SHBE_Data.getDW_SHBE_Records(YM31);
        //recs1 = env.getDW_SHBE_Data().getData().get(YM31);
        HashMap<DW_ID, DW_SHBE_Record> DRecordsEnd = recs1.getClaimIDToDW_SHBE_RecordMap(env._HandleOutOfMemoryError_boolean);
        //TreeMap<String, DW_SHBE_Record> SRecordsEnd = (TreeMap<String, DW_SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<String, Integer> destinationCounts;
        Iterator<DW_ID> ite;
        ite = DRecordsStart.keySet().iterator();
        DW_ID DW_ID;
        while (ite.hasNext()) {
            DW_ID = ite.next();
            DW_SHBE_D_Record DRecordStart = DRecordsStart.get(DW_ID).getDRecord();
            if (DRecordStart != null) {
                // Filter for only Housing Benefit Claimants
                //if (!DRecordStart.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                if (DW_SHBE_Handler.isHBClaimInPayment(DRecordStart)) {
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
                    DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(DW_ID).getDRecord();
                    // Filter for only Housing Benefit Claimants
//                    if (!DRecordEnd.getHousingBenefitClaimReferenceNumber().isEmpty()) {
                    if (DW_SHBE_Handler.isHBClaimInPayment(DRecordEnd)) {
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
            DW_ID = ite.next();
            DW_SHBE_D_Record DRecordEnd = DRecordsEnd.get(DW_ID).getDRecord();
            if (DRecordEnd != null) {
                DW_SHBE_D_Record DRecordStart = DRecordsStart.get(DW_ID).getDRecord();
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


}
