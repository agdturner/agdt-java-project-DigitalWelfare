/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.core.ONSPD_ID;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;

/**
 * Class for aggregating data.
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_ProcessorLCCAggregate extends DW_ProcessorLCC {

    /**
     * For convenience.
     */
    protected DW_UO_Handler UO_Handler;
    protected SHBE_TenancyType_Handler SHBE_TenancyType_Handler;

//    /**
//     * For convenience.
//     */
//    String sU = Strings.sU;
//
//    //String sUOInApril2013 = "UOInApril2013";
//    String sUOInApril2013 = "UInApr13";
//    //String sTenancyChange = "TenancyChange";
//    String sTenancyChange = "TC";
//    String sMinus999 = "-999";
//    String sAAN_NAA = "AAN_NAA";
//
//    //String sAllClaimants = "Al";
//    String sAllClaimants = "All";
//    //String sOnFlow = "OF";
//    String sOnFlow = "OnFlow";
//    //String sReturnFlow = "RF";
//    String sReturnFlow = "ReturnFlow";
//    //String sStable = "St";
//    String sStable = "Stable";
//    //String sAllInChurn = "AI";
//    String sAllInChurn = "AllInChurn";
//    //String sAllOutChurn = "AO";
//    String sAllOutChurn = "AllOutChurn";
//    String sUnknown = "Unknown";
//    String snull = "null";
//
//    String sInDistanceChurn = "InDistanceChurn";
//    String sWithinDistanceChurn = "WithinDistanceChurn";
//    String sOutDistanceChurn = "OutDistanceChurn";
    boolean doAggregation = false;
    boolean doHBGeneralAggregateStatistics = false;

    public DW_ProcessorLCCAggregate(DW_Environment env) {
        super(env);
        UO_Handler = env.getUO_Handler();
        SHBE_TenancyType_Handler = env.getSHBE_TenancyType_Handler();
        SHBE_Handler = env.getSHBE_Handler();
        ClaimIDToClaimRefLookup = SHBE_Handler.getClaimIDToClaimRefLookup();
        UO_Data = env.getUO_Data();
    }

    @Override
    public void run() throws Exception, Error {
        SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
        // Declaration
        ArrayList<String> PTs;
        ArrayList<String> levels;
        TreeMap<String, ArrayList<Integer>> includes;
        ArrayList<String> claimantTypes;
        ArrayList<String> types;
        ArrayList<Double> distances;
        ArrayList<String> distanceTypes;
        HashMap<Boolean, ArrayList<String>> TTs;
        HashMap<Boolean, TreeMap<String, ArrayList<String>>> TTGroups;
        HashMap<Boolean, ArrayList<String>> TTsGrouped;
        HashMap<Boolean, ArrayList<Integer>> regulatedGroups;
        HashMap<Boolean, ArrayList<Integer>> unregulatedGroups;

        PTs = Env.SHBE_Env.Strings.getPaymentTypes();
//            PTs.remove(Strings.sPaymentTypeAll);
//            PTs.remove(Strings.sPaymentTypeIn);
//            PTs.remove(Strings.sPaymentTypeSuspended);
//            PTs.remove(Strings.sPaymentTypeOther);

        levels = new ArrayList<>();
//        levels.add(Strings.sOA);
        levels.add(Strings.sLSOA);
        levels.add(Strings.sMSOA);
//        levels.add("PostcodeUnit");
//        levels.add("PostcodeSector");
//        levels.add("PostcodeDistrict");

        int CensusYear = 2011;

        // Initialisiation
        // intialise levels
        levels.add(Strings.sParliamentaryConstituency);
        levels.add(Strings.sStatisticalWard);

        // Initialise includes
        includes = SHBE_Handler.getIncludes();
//            includes.remove(Strings.sIncludeAll);
//            includes.remove(Strings.sIncludeYearly);
//            includes.remove(Strings.sInclude6Monthly);
//            includes.remove(Strings.sInclude3Monthly);
//            includes.remove(Strings.sIncludeMonthlySinceApril2013);
//            includes.remove(Strings.sIncludeMonthly);

        // Initialise claimantTypes
        claimantTypes = Strings.getHB_CTB();

        // Initialise types
        types = new ArrayList<>();
        types.add(Strings.sAllClaimants); // Count of all claimants
//        types.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add(Strings.sOnFlow); // These are people not claiming the previous month and that have not claimed before.
        types.add(Strings.sReturnFlow); // These are people not claiming the previous month but that have claimed before.
        types.add(Strings.sStable); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add(Strings.sAllInChurn); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add(Strings.sAllOutChurn); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        // Initialise distanceTypes
        distanceTypes = new ArrayList<>();
        distanceTypes.add(Strings.sInDistanceChurn); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add(Strings.sWithinDistanceChurn); // A count of all claimants that have moved within this area.
        distanceTypes.add(Strings.sOutDistanceChurn); // A count of all claimants that have moved out from this area.

        // Initialise/Specifiy distances
        distances = new ArrayList<>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        // Initialise TTs
        TTs = new HashMap<>();
        boolean doUO;
        doUO = true;
        TTs.put(doUO, SHBE_TenancyType_Handler.getTenancyTypeAll(doUO));
        doUO = false;
        TTs.put(doUO, SHBE_TenancyType_Handler.getTenancyTypeAll(doUO));

        // Initialise TTGroups, TTsGrouped, regulatedGroups and unregulatedGroups
        Object[] TTGs;
        TTGs = SHBE_TenancyType_Handler.getTenancyTypeGroups();
        TTGroups = (HashMap<Boolean, TreeMap<String, ArrayList<String>>>) TTGs[0];
        TTsGrouped = (HashMap<Boolean, ArrayList<String>>) TTGs[1];
        regulatedGroups = (HashMap<Boolean, ArrayList<Integer>>) TTGs[2];
        unregulatedGroups = (HashMap<Boolean, ArrayList<Integer>>) TTGs[3];
        doAggregation = true;
        doHBGeneralAggregateStatistics = true;

        if (doAggregation) {
            ONSPD_YM3 YM3;
//            for (int i = 0; i < SHBEFilenames.length; i++) {
//                YM3 = SHBE_Handler.getYM3(SHBEFilenames[i]);
            for (String SHBEFilename : SHBEFilenames) {
                YM3 = SHBE_Handler.getYM3(SHBEFilename);
                aggregate(YM3,
                        PTs,
                        levels,
                        this.getArrayListBoolean(),
                        UO_Data,
                        SHBEFilenames,
                        claimantTypes,
                        types,
                        distances,
                        distanceTypes,
                        TTs,
                        TTGroups,
                        TTsGrouped,
                        regulatedGroups,
                        unregulatedGroups,
                        includes);
            }

        }

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
            ONSPD_YM3 YM3,
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
        Env.logO("<doAggregation>", true);
        int CensusYear = 2011;
        // Get Lookup
        TreeMap<String, TreeMap<String, String>> LookupsFromPostcodeToLevelCode;
        LookupsFromPostcodeToLevelCode = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3, CensusYear);
        Iterator<Boolean> iteB;
        Iterator<String> tPTIte;
        tPTIte = PTs.iterator();
        while (tPTIte.hasNext()) {
            String aPT;
            aPT = tPTIte.next();
            Env.logO("<" + aPT + ">", true);
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
                        Env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
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
                        Env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = true;
                        doRSL = false;
                        Env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
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
                        Env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = false;
                        doRSL = true;
                        Env.logO("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
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
                        Env.logO("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                    }
                } else {
                    doCouncil = false;
                    doRSL = false;
                    Env.logO("<aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
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
                    Env.logO("</aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
                }
            }
            Env.logO("</" + aPT + ">", true);
        }
        Env.logO("</doAggregation>", true);
    }

    /**
     * @param claimantNINO1
     * @param i
     * @param tIDIndexes
     * @return True iff claimantNINO1 is in tIDIndexes in any index from 0 to i
     * - 1.
     */
    private boolean getHasClaimantBeenSeenBefore(SHBE_ID ID, int i,
            ArrayList<Integer> include,
            ArrayList<ArrayList<SHBE_ID>> tIDIndexes) {
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
            ArrayList<SHBE_ID> tIDIndex;
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
            String lastMonthYear = SHBE_Handler.getYear(SHBEFilenames[i - 1]);
            int yearInt = Integer.parseInt(year);
            int lastMonthYearInt = Integer.parseInt(lastMonthYear);
            if (!(yearInt == lastMonthYearInt || yearInt == lastMonthYearInt + 1)) {
                return null;
            }
            String lastMonthMonth = SHBE_Handler.getMonth(SHBEFilenames[i - 1]);
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
            String lastYearYear = SHBE_Handler.getYear(SHBEFilenames[i - 12]);
            String lastYearMonth = SHBE_Handler.getMonth(SHBEFilenames[i - 12]);
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
        Iterator<SHBE_ID> ite2;
        HashSet<SHBE_ID> totalCouncil;
        totalCouncil = new HashSet<>();
        ite = CouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            String YM3;
            YM3 = ite.next();
            DW_UO_Set s;
            s = CouncilSets.get(YM3);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                SHBE_ID CTBRefID;
                CTBRefID = ite2.next();
                totalCouncil.add(CTBRefID);
            }
        }
        Env.logO("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
        TreeMap<String, DW_UO_Set> tRSLSets;
        tRSLSets = (TreeMap<String, DW_UO_Set>) UnderOccupiedData[1];
        HashSet<SHBE_ID> totalRSL;
        totalRSL = new HashSet<>();
        ite = tRSLSets.keySet().iterator();
        while (ite.hasNext()) {
            String yM;
            yM = ite.next();
            DW_UO_Set s;
            s = tRSLSets.get(yM);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                SHBE_ID CTBRefID;
                CTBRefID = ite2.next();
                totalRSL.add(CTBRefID);
            }
        }
        Env.logO("Number of RSL tenants effected by underoccupancy " + totalRSL.size(), true);
        totalCouncil.addAll(totalRSL);
        Env.logO("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
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
        result = new TreeMap<>();
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
                DW_UO_SetAll = new DW_UO_Set(Env);
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
                DW_UO_SetAll = new DW_UO_Set(Env);
                result.put(YM3, DW_UO_SetAll);
            }
            DW_UO_SetRSL = DW_UO_SetsRSL.get(YM3);
            DW_UO_SetAll.getMap().putAll(DW_UO_SetRSL.getMap());
        }
        return result;
    }

    protected HashMap<SHBE_ID, Integer> loadClaimIDToTTLookup(
            ONSPD_YM3 YM3, Integer key,
            HashMap<Integer, HashMap<SHBE_ID, Integer>> ClaimIDToTTLookups) {
        HashMap<SHBE_ID, Integer> result;
        if (ClaimIDToTTLookups.containsKey(key)) {
            return ClaimIDToTTLookups.get(key);
        }
        result = new HashMap<>();
        SHBE_Records SHBE_Records;
        SHBE_Records = SHBE_Handler.getRecords(YM3, Env.HOOME);
        HashMap<SHBE_ID, SHBE_Record> records;
        records = SHBE_Records.getRecords(Env.HOOME);
        Iterator<SHBE_ID> ite;
        ite = records.keySet().iterator();
        SHBE_ID ClaimID;
        while (ite.hasNext()) {
            ClaimID = ite.next();
            result.put(ClaimID,
                    records.get(ClaimID).getDRecord().getTenancyType());
        }
        ClaimIDToTTLookups.put(key, result);
        return result;
    }

    protected HashMap<SHBE_ID, ONSPD_ID> loadClaimIDToPostcodeIDLookup(
            ONSPD_YM3 YM3, Integer key,
            HashMap<Integer, HashMap<SHBE_ID, ONSPD_ID>> ClaimIDToPostcodeIDLookups) {
        HashMap<SHBE_ID, ONSPD_ID> r;
        if (ClaimIDToPostcodeIDLookups.containsKey(key)) {
            return ClaimIDToPostcodeIDLookups.get(key);
        }
        r = Env.getSHBE_Handler().getRecords(YM3, Env.HOOME).getClaimIDToPostcodeIDLookup(Env.HOOME);
        ClaimIDToPostcodeIDLookups.put(key, r);
        return r;
    }

    private void writeTransitionFrequencies(
            TreeMap<String, Integer> transitions,
            File dirOut,
            String dirname,
            String name,
            boolean reportTenancyTransitionBreaks) {
        if (transitions.size() > 0) {
            File dirOut2;
            dirOut2 = new File(dirOut, dirname);
            dirOut2.mkdir();
            PrintWriter pw;
            pw = getFrequencyPrintWriter(dirOut2, name,
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
                    Env.SHBE_Env.Strings.sIncludingTenancyTransitionBreaks);
        } else {
            dirOut2 = new File(
                    dirOut2,
                    Env.SHBE_Env.Strings.sIncludingTenancyTransitionBreaksNo);
        }
        dirOut2.mkdirs();
        File f;
        f = new File(
                dirOut2,
                name);
        Env.logO("Write " + f, true);
        result = Generic_IO.getPrintWriter(f, false);
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
    public void aggregateClaims(boolean doUnderOccupied, boolean doCouncil, 
            boolean doRSL,            DW_UO_Data DW_UO_Data,
            TreeMap<String, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames,            String aPT,
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
        outputDirs = Files.getOutputSHBELevelDirsTreeMap(
                levels,
                doUnderOccupied,
                doCouncil,
                doRSL);
        // Init underOccupiedSets
        TreeMap<ONSPD_YM3, DW_UO_Set> councilUnderOccupiedSets = null;
        TreeMap<ONSPD_YM3, DW_UO_Set> RSLUnderOccupiedSets = null;
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
            ONSPD_YM3 YM30;
            YM30 = SHBE_Handler.getYM3(SHBEFilenames[i]);
            SHBE_Records recs0;
            recs0 = Env.getSHBE_Handler().getRecords(YM30, Env.HOOME);
            ONSPD_YM3 YM30v;
            YM30v = recs0.getNearestYM3ForONSPDLookup();
            HashMap<SHBE_ID, SHBE_Record> records0;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
            claimantTypeTenureLevelTypeDirs = new TreeMap<>();
            claimantTypeTenureLevelTypeAreaCounts = new TreeMap<>();
            claimantTypeTenureLevelTypeTenureCounts = new TreeMap<>();
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType;
                claimantType = claimantTypesIte.next();
                // Initialise Dirs
                TreeMap<String, TreeMap<String, TreeMap<String, File>>> TTLevelTypeDirs;
                TTLevelTypeDirs = new TreeMap<>();
                // Initialise AreaCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts;
                TTLevelTypeAreaCounts = new TreeMap<>();
                // Initialise TenureCounts
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts;
                TTLevelTypeTenureCounts = new TreeMap<>();
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT = TTIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<String, File>> levelTypeDirs;
                    levelTypeDirs = new TreeMap<>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts;
                    levelTypeAreaCounts = new TreeMap<>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts;
                    levelTypeTenureCounts = new TreeMap<>();
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
                        typeDirs = new TreeMap<>();
                        // Initialise AreaCounts
                        TreeMap<String, TreeMap<String, Integer>> typeAreaCounts;
                        typeAreaCounts = new TreeMap<>();
                        // Initialise TenureCounts
                        TreeMap<String, TreeMap<Integer, Integer>> typeTenureCounts;
                        typeTenureCounts = new TreeMap<>();
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
                            areaCount = new TreeMap<>();
                            typeAreaCounts.put(type, areaCount);
                            // Initialise TenureCounts
                            TreeMap<Integer, Integer> TTCounts;
                            TTCounts = new TreeMap<>();
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
            records0 = recs0.getRecords(Env.HOOME);

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
            Iterator<SHBE_ID> recordsIte;
            recordsIte = records0.keySet().iterator();
            while (recordsIte.hasNext()) {
                SHBE_ID ClaimID;
                ClaimID = recordsIte.next();
                SHBE_Record SHBE_Record0;
                SHBE_Record0 = records0.get(ClaimID);
                SHBE_D_Record DRecord0 = SHBE_Record0.getDRecord();
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
                            claimantType = SHBE_Handler.getClaimantType(DRecord0);
                            Integer TTInt = DRecord0.getTenancyType();
                            if (postcode0 != null) {
                                String areaCode;
                                areaCode = getAreaCode(
                                        level,
                                        postcode0,
                                        tLookupFromPostcodeToLevelCode);
                                String type;
                                type = Strings.sAllClaimants;
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
            claimantTypeTenureLevelTypeCountAreas = new TreeMap<>();
            //yearMonthClaimantTypeTenureLevelTypeCountAreas.put(yearMonth, claimantTypeTenureLevelTypeCountAreas);
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<>();
            //yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(yearMonth, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
            // claimantTypeLoop
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType = claimantTypesIte.next();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                TTLevelTypeCountAreas = new TreeMap<>();
                claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT;
                    TT = TTIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                    levelTypeCountAreas = new TreeMap<>();
                    TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        String level = levelsIte.next();
                        TreeMap<String, TreeMap<Integer, TreeSet<String>>> typeCountAreas;
                        typeCountAreas = new TreeMap<>();
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
            TreeMap<ONSPD_YM3, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>> yearMonthClaimantTypeTenureLevelTypeCountAreas;
            yearMonthClaimantTypeTenureLevelTypeCountAreas = new TreeMap<ONSPD_YM3, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>>();

            TreeMap<ONSPD_YM3, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>> yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<ONSPD_YM3, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>>();

            // Initialise tIDIndexes
            ArrayList<ArrayList<SHBE_ID>> tIDIndexes;
            tIDIndexes = new ArrayList<>();
            if (true) {
//                ArrayList<SHBE_ID> tID_HashSet;
//                tID_HashSet = recs0.getClaimIDToClaimantPersonIDLookup(Env.HOOME);
//                tIDIndexes.add(tID_HashSet);
            }

            while (includeIte.hasNext()) {
                i = includeIte.next();
                // Set Year and Month variables
                ONSPD_YM3 YM31 = SHBE_Handler.getYM3(SHBEFilenames[i]);
                // Load next data
                SHBE_Records recs1;
                recs1 = Env.getSHBE_Handler().getRecords(YM31, Env.HOOME);
                HashMap<SHBE_ID, String> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = null;//recs1.getClaimSHBE_IDToPostcodeLookup();
                HashMap<SHBE_ID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = null;//recs1.getClaimantIDToTenancyTypeLookup();
                ONSPD_YM3 YM31v;
                YM31v = recs1.getNearestYM3ForONSPDLookup();
//            String yearMonth = year + month;
//            String lastMonth_yearMonth;
//            String year = SHBE_Handler.getYear(SHBEFilenames[i]);
//            String month = SHBE_Handler.getMonth(SHBEFilenames[i]);
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
//                    ArrayList<SHBE_ID> tID_HashSet;
//                    tID_HashSet = recs1.getClaimantClaimIDs(Env.HOOME);
//                    tIDIndexes.add(tID_HashSet);
                }
                //records0 = (TreeMap<String, SHBE_Record>) SHBEData0[0];
                HashMap<SHBE_ID, SHBE_Record> records1;
                records1 = recs1.getRecords(Env.HOOME);
                /* Initialise A:
                 * output directories;
                 * claimantTypeTenureLevelTypeDirs;
                 * TTLevelTypeDistanceDirs;
                 * TTAreaCount;
                 * TTDistanceAreaCount.
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, File>>>> claimantTypeTenureLevelTypeDirs;
                claimantTypeTenureLevelTypeDirs = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>>> claimantTypeTenureLevelTypeDistanceDirs;
                claimantTypeTenureLevelTypeDistanceDirs = new TreeMap<>();
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
                claimantTypeTenureLevelTypeAreaCounts = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts;
                claimantTypeTenureLevelTypeDistanceAreaCounts = new TreeMap<>();
                /* Initialise B:
                 * claimantTypeLevelTypeTenureCounts;
                 * claimantTypeLevelTypeDistanceTenureCounts;
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
                claimantTypeTenureLevelTypeTenureCounts = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts;
                claimantTypeTenureLevelTypeDistanceTenureCounts = new TreeMap<>();
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypesIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<String, TreeMap<String, File>>> TTLevelTypeDirs;
                    TTLevelTypeDirs = new TreeMap<>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, File>>>> TTLevelTypeDistanceDirs;
                    TTLevelTypeDistanceDirs = new TreeMap<>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts;
                    TTLevelTypeAreaCounts = new TreeMap<>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>> TTLevelTypeDistanceAreaCounts;
                    TTLevelTypeDistanceAreaCounts = new TreeMap<>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts;
                    TTLevelTypeTenureCounts = new TreeMap<>();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>> TTLevelTypeDistanceTenureCounts;
                    TTLevelTypeDistanceTenureCounts = new TreeMap<>();
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT = TTIte.next();
                        // Initialise Dirs
                        TreeMap<String, TreeMap<String, File>> levelTypeDirs;
                        levelTypeDirs = new TreeMap<>();
                        TreeMap<String, TreeMap<String, TreeMap<Double, File>>> levelTypeDistanceDirs;
                        levelTypeDistanceDirs = new TreeMap<>();
                        // Initialise AreaCounts
                        TreeMap<String, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts;
                        levelTypeAreaCounts = new TreeMap<>();
                        TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>> levelTypeDistanceAreaCounts;
                        levelTypeDistanceAreaCounts = new TreeMap<>();
                        // Initialise TenureCounts
                        TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts;
                        levelTypeTenureCounts = new TreeMap<>();
                        TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>> levelTypeDistanceTenureCounts;
                        levelTypeDistanceTenureCounts = new TreeMap<>();
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
                            typeDirs = new TreeMap<>();
                            TreeMap<String, TreeMap<Double, File>> typeDistanceDirs;
                            typeDistanceDirs = new TreeMap<>();
                            // Initialise AreaCounts
                            TreeMap<String, TreeMap<String, Integer>> typeAreaCounts;
                            typeAreaCounts = new TreeMap<>();
                            TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>> typeDistanceAreaCounts;
                            typeDistanceAreaCounts = new TreeMap<>();
                            // Initialise TenureCounts
                            TreeMap<String, TreeMap<Integer, Integer>> typeTenureCounts;
                            typeTenureCounts = new TreeMap<>();
                            TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>> typeDistanceTenureCounts;
                            typeDistanceTenureCounts = new TreeMap<>();
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
                                areaCount = new TreeMap<>();
                                typeAreaCounts.put(type, areaCount);
                                // Initialise TenureCounts
                                TreeMap<Integer, Integer> TTCounts;
                                TTCounts = new TreeMap<>();
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
                                distanceDirs = new TreeMap<>();
                                // Initialise AreaCounts
                                TreeMap<Double, TreeMap<String, Integer>> distanceAreaCounts;
                                distanceAreaCounts = new TreeMap<>();
                                // Initialise TenureCounts
                                TreeMap<Double, TreeMap<Integer, Integer>> distanceTenureCounts;
                                distanceTenureCounts = new TreeMap<>();
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
                                    areaCounts = new TreeMap<>();
                                    distanceAreaCounts.put(distance, areaCounts);
                                    // Initialise TenureCounts
                                    TreeMap<Integer, Integer> TTCounts;
                                    TTCounts = new TreeMap<>();
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
                levelUnexpectedCounts = new TreeMap<>();
                levelsIte = levels.iterator();
                while (levelsIte.hasNext()) {
                    String level;
                    level = levelsIte.next();
                    TreeMap<String, Integer> unexpectedCounts;
                    unexpectedCounts = new TreeMap<>();
                    levelUnexpectedCounts.put(level, unexpectedCounts);
                }
                // Iterator over records
                Iterator<SHBE_ID> SHBE_IDIte = records1.keySet().iterator();
                while (SHBE_IDIte.hasNext()) {
                    SHBE_ID ClaimID;
                    ClaimID = SHBE_IDIte.next();
                    SHBE_Record Record1;
                    Record1 = records1.get(ClaimID);
                    SHBE_D_Record DRecord1 = Record1.getDRecord();
                    ONSPD_ID PostcodeID1 = Record1.getPostcodeID();
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
                                SHBE_ID claimID1;
                                claimID1 = SHBE_Handler.getClaimRefToClaimIDLookup().get(CTBRef1);
                                SHBE_ID claimantSHBE_ID1;
                                claimantSHBE_ID1 = null;//DW_PersonIDtoSHBE_IDLookup.get(claimantDW_PersonID1);
                                String claimantType;
                                claimantType = SHBE_Handler.getClaimantType(DRecord1);
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
                                                    claimID1);
                                        }
                                        if (councilUOSet1 != null) {
                                            councilUO1 = councilUOSet1.getMap().get(
                                                    claimID1);
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
                                            type = Strings.sAllClaimants;
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
                                                SHBE_Record record0 = records0.get(ClaimID);
                                                ONSPD_ID PostcodeID0;
                                                if (record0 == null) {
//                                        //This is a new entrant to the data
//                                        type = "NewEntrant";
                                                    // If this claimantNINO has never been seen before it is an OnFlow
                                                    boolean hasBeenSeenBefore;
                                                    hasBeenSeenBefore = getHasClaimantBeenSeenBefore(
                                                            claimantSHBE_ID1,
                                                            i,
                                                            include,
                                                            tIDIndexes);
                                                    if (!hasBeenSeenBefore) {
                                                        type = Strings.sOnFlow;
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
                                                        type = Strings.sReturnFlow;
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
                                                    SHBE_D_Record DRecord0 = record0.getDRecord();
                                                    PostcodeID0 = record0.getPostcodeID();
                                                    if (PostcodeID0 == null) {
                                                        // Unknown
                                                        type = Strings.sUnknown;
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
                                                        type = Strings.sStable;
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
                                                        type = Strings.sAllInChurn;
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
                                                        type = Strings.sAllOutChurn;
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
                                                        distance = Postcode_Handler.getDistanceBetweenPostcodes(
                                                                Env.getSHBE_Handler().getPostcodeIDToPointLookup(YM30v).get(PostcodeID0),
                                                                Env.getSHBE_Handler().getPostcodeIDToPointLookup(YM31v).get(PostcodeID1),
                                                                Env.getSHBE_Handler().getPostcodeIDToPointLookup(YM31v).get(PostcodeID0),
                                                                Env.getSHBE_Handler().getPostcodeIDToPointLookup(YM30v).get(PostcodeID1),
                                                                YM30v, YM31v, PostcodeID0, PostcodeID1);
                                                        Iterator<Double> ite3;
                                                        ite3 = distances.iterator();
                                                        while (ite3.hasNext()) {
                                                            double distanceThreshold = ite3.next();
                                                            if (distance > distanceThreshold) {
                                                                // InDistanceChurn
                                                                type = Strings.sInDistanceChurn;
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
                                                                type = Strings.sOutDistanceChurn;
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
                                                                type = Strings.sWithinDistanceChurn;
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
                                                Generic_Collections.addToTreeMapValueInteger(
                                                        unexpectedCounts, ClaimPostcodeF, 1);
                                            }
                                        }
                                    }
                                } else {
                                    Generic_Collections.addToTreeMapValueInteger(
                                            unexpectedCounts, Strings.snull, 1);
                                }
                            }
                        }
                    }
                }
                // Write out results
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
                claimantTypeTenureLevelTypeCountAreas = new TreeMap<>();
                yearMonthClaimantTypeTenureLevelTypeCountAreas.put(YM31, claimantTypeTenureLevelTypeCountAreas);
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
                claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<>();
                yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(YM31, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType = claimantTypesIte.next();
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                    TTLevelTypeCountAreas = new TreeMap<>();
                    claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>> TTLevelDistanceTypeDistanceCountAreas;
                    TTLevelDistanceTypeDistanceCountAreas = new TreeMap<>();
                    claimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(claimantType, TTLevelDistanceTypeDistanceCountAreas);
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT;
                        TT = TTIte.next();
                        TreeMap<String, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                        levelTypeCountAreas = new TreeMap<>();
                        TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
                        TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>> levelDistanceTypeDistanceCountAreas;
                        levelDistanceTypeDistanceCountAreas = new TreeMap<>();
                        TTLevelDistanceTypeDistanceCountAreas.put(TT, levelDistanceTypeDistanceCountAreas);
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level = levelsIte.next();
                            TreeMap<String, TreeMap<Integer, TreeSet<String>>> typeCountAreas;
                            typeCountAreas = new TreeMap<>();
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
                            distanceTypeDistanceCountAreas = new TreeMap<>();
                            levelDistanceTypeDistanceCountAreas.put(level, distanceTypeDistanceCountAreas);
                            distanceTypesIte = distanceTypes.iterator();
                            while (distanceTypesIte.hasNext()) {
                                String distanceType;
                                distanceType = distanceTypesIte.next();
                                TreeMap<Double, TreeMap<Integer, TreeSet<String>>> distanceCountAreas;
                                distanceCountAreas = new TreeMap<>();
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
                                if (Env.DEBUG_Level < Generic_Environment.DEBUG_Level_NORMAL) {
                                    Env.logO("Unexpected Counts for:"
                                            + " Claimant Type " + claimantType
                                            + " Tenure " + TT
                                            + " Level " + level, true);
                                    Env.logO("code,count", true);
                                    Iterator<String> unexpectedCountsIte;
                                    unexpectedCountsIte = unexpectedCounts.keySet().iterator();
                                    while (unexpectedCountsIte.hasNext()) {
                                        String code = unexpectedCountsIte.next();
                                        Integer count = unexpectedCounts.get(code);
                                        Env.logO("" + code + ", " + count, true);
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
                //YM30 = YM31;
                YM30 = new ONSPD_YM3(YM31);
                //YM30v = YM31v;
                YM30v = new ONSPD_YM3(YM31v);
            }
        }
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> YM31CountAreas,
            ONSPD_YM3 YM31,
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
            ONSPD_YM3 YM3) {
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
            ONSPD_YM3 YM31) {
        if (lastTimeCountAreas != null) {
            TreeMap<String, Integer> areaCounts;
            areaCounts = getAreaCounts(countAreas);
            TreeMap<String, Integer> lastTimeAreaCounts;
            lastTimeAreaCounts = getAreaCounts(lastTimeCountAreas);
            int n = 2;
            TreeMap<String, Double> areaAbsoluteDiffs;
            areaAbsoluteDiffs = new TreeMap<>();
            TreeMap<String, Double> areaRelativeDiffs;
            areaRelativeDiffs = new TreeMap<>();
            TreeSet<String> allAreas;
            allAreas = new TreeSet<>();
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
            ONSPD_YM3 YM31) {
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
        result = new TreeMap<>();
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
        result = new TreeMap<>();
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
                areas = new TreeSet<>();
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
            ONSPD_YM3 YM3) {
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
            ONSPD_YM3 YM3) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result;
            result = new TreeMap<>();
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
                    set = new TreeSet<>();
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
            ONSPD_YM3 YM3) {
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
            String areaCode, String claimantType, String tenure, String level, String type) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenure).get(level).get(type);
        Generic_Collections.addToTreeMapValueInteger(areaCounts, areaCode, 1);
    }

    private void addToAreaCount(
            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            String areaCode, String claimantType, String tenure, String level, String type, Double distance) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(tenure).get(level).get(type).get(distance);
//        //Debug
//        if (areaCounts == null) {
//            // No area counts for distance
//            Env.logO("No area counts for distance " + distance);
//            Env.logO("claimantType " + claimantType);
//            Env.logO("tenure " + tenure);
//            Env.logO("level " + level);
//            Env.logO("type " + type);
//        }
        Generic_Collections.addToTreeMapValueInteger(areaCounts, areaCode, 1);
    }
}
