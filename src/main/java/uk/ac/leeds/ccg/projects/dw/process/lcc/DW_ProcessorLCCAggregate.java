package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.data.id.UKP_RecordID;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Strings;
import uk.ac.leeds.ccg.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Handler;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Data;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;

/**
 * Class for aggregating data.
 *
 * @author Andy Turner
 * @version 1.0.0
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
//    String sU = DW_Strings.sU;
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

    public DW_ProcessorLCCAggregate(DW_Environment env) throws IOException, Exception {
        super(env);
        UO_Handler = env.getUO_Handler();
        SHBE_TenancyType_Handler = env.getSHBE_TenancyType_Handler();
        shbeHandler = env.getSHBE_Handler();
        cid2c = shbeHandler.getCid2c();
        UO_Data = env.getUO_Data();
    }

    @Override
    public void run() throws Exception, Error {
        shbeFilenames = shbeHandler.getFilenames();
        // Declaration
        ArrayList<String> PTs;
        ArrayList<Integer> levels;
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

        PTs = SHBE_Strings.getPaymentTypes();
//            PTs.remove(DW_Strings.sPaymentTypeAll);
//            PTs.remove(DW_Strings.sPaymentTypeIn);
//            PTs.remove(DW_Strings.sPaymentTypeSuspended);
//            PTs.remove(DW_Strings.sPaymentTypeOther);

        levels = new ArrayList<>();
//        levels.add(UKP_Data.TYPE_OA);
        levels.add(UKP_Data.TYPE_LSOA);
        levels.add(UKP_Data.TYPE_MSOA);
//        levels.add(UKP_Data.TYPE_UNIT);
//        levels.add(UKP_Data.TYPE_SECTOR);
//        levels.add(UKP_Data.TYPE_DISTRICT);

        int CensusYear = 2011;

        // Initialisiation
        // intialise levels
        levels.add(UKP_Data.TYPE_ParliamentaryConstituency);
        levels.add(UKP_Data.TYPE_StatisticalWard);

        // Initialise includes
        includes = shbeHandler.getIncludes();
//            includes.remove(DW_Strings.sIncludeAll);
//            includes.remove(DW_Strings.sIncludeYearly);
//            includes.remove(DW_Strings.sInclude6Monthly);
//            includes.remove(DW_Strings.sInclude3Monthly);
//            includes.remove(DW_Strings.sIncludeMonthlySinceApril2013);
//            includes.remove(DW_Strings.sIncludeMonthly);

        // Initialise claimantTypes
        claimantTypes = DW_Strings.getHB_CTB();

        // Initialise types
        types = new ArrayList<>();
        types.add(DW_Strings.sAllClaimants); // Count of all claimants
//        types.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add(DW_Strings.sOnFlow); // These are people not claiming the previous month and that have not claimed before.
        types.add(DW_Strings.sReturnFlow); // These are people not claiming the previous month but that have claimed before.
        types.add(DW_Strings.sStable); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add(DW_Strings.sAllInChurn); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add(DW_Strings.sAllOutChurn); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        // Initialise distanceTypes
        distanceTypes = new ArrayList<>();
        distanceTypes.add(DW_Strings.sInDistanceChurn); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add(DW_Strings.sWithinDistanceChurn); // A count of all claimants that have moved within this area.
        distanceTypes.add(DW_Strings.sOutDistanceChurn); // A count of all claimants that have moved out from this area.

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
            UKP_YM3 YM3;
//            for (int i = 0; i < shbeFilenames.length; i++) {
//                YM3 = shbeHandler.getYM3(shbeFilenames[i]);
            for (String SHBEFilename : shbeFilenames) {
                YM3 = shbeHandler.getYM3(SHBEFilename);
                aggregate(YM3, PTs, levels, getArrayListBoolean(), UO_Data,
                        shbeFilenames, claimantTypes, types, distances,
                        distanceTypes, TTs, TTGroups, TTsGrouped,
                        regulatedGroups, unregulatedGroups, includes);
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
     * @throws java.lang.ClassNotFoundException
     */
    protected void aggregate(UKP_YM3 YM3, ArrayList<String> PTs,
            ArrayList<Integer> levels, ArrayList<Boolean> bArray,
            DW_UO_Data DW_UO_Data, String[] SHBEFilenames,
            ArrayList<String> claimantTypes, ArrayList<String> types,
            ArrayList<Double> distances, ArrayList<String> distanceTypes,
            HashMap<Boolean, ArrayList<String>> TTs,
            HashMap<Boolean, TreeMap<String, ArrayList<String>>> TTGroups,
            HashMap<Boolean, ArrayList<String>> TTsGrouped,
            HashMap<Boolean, ArrayList<Integer>> regulatedGroups,
            HashMap<Boolean, ArrayList<Integer>> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes) throws IOException,
            ClassNotFoundException, Exception {
        env.ge.log("<doAggregation>", true);
        int CensusYear = 2011;
        // Get Lookup
        TreeMap<Integer, TreeMap<String, String>> postcodeToLevelCode
                = getClaimPostcodeF_To_LevelCode_Maps(levels, YM3, CensusYear);
        Iterator<Boolean> iteB;
        Iterator<String> tPTIte = PTs.iterator();
        while (tPTIte.hasNext()) {
            String aPT = tPTIte.next();
            env.ge.log("<" + aPT + ">", true);
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
                        env.ge.log("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                postcodeToLevelCode,
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
                        env.ge.log("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = true;
                        doRSL = false;
                        env.ge.log("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                postcodeToLevelCode,
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
                        env.ge.log("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        doCouncil = false;
                        doRSL = true;
                        env.ge.log("<aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                        aggregateClaims(
                                doUnderOccupied,
                                doCouncil,
                                doRSL,
                                DW_UO_Data,
                                postcodeToLevelCode,
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
                        env.ge.log("</aggregateClaimants(doCouncil " + doCouncil + ", doRSL " + doRSL + ")>", true);
                    }
                } else {
                    doCouncil = false;
                    doRSL = false;
                    env.ge.log("<aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
                    aggregateClaims(
                            doUnderOccupied,
                            doCouncil,
                            doRSL,
                            DW_UO_Data,
                            postcodeToLevelCode,
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
                    env.ge.log("</aggregateClaimants(doUnderOccupied " + doUnderOccupied + ")>", true);
                }
            }
            env.ge.log("</" + aPT + ">", true);
        }
        env.ge.log("</doAggregation>", true);
    }

    /**
     * @param claimantNINO1
     * @param i
     * @param tIDIndexes
     * @return True iff claimantNINO1 is in tIDIndexes in any index from 0 to i
     * - 1.
     */
    private boolean getHasClaimantBeenSeenBefore(SHBE_ClaimID ID, int i,
            ArrayList<Integer> include,
            ArrayList<ArrayList<SHBE_ClaimID>> tIDIndexes) {
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
            ArrayList<SHBE_ClaimID> tIDIndex;
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
            String lastMonthYear = shbeHandler.getYear(SHBEFilenames[i - 1]);
            int yearInt = Integer.parseInt(year);
            int lastMonthYearInt = Integer.parseInt(lastMonthYear);
            if (!(yearInt == lastMonthYearInt || yearInt == lastMonthYearInt + 1)) {
                return null;
            }
            String lastMonthMonth = shbeHandler.getMonth(SHBEFilenames[i - 1]);
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
            String lastYearYear = shbeHandler.getYear(SHBEFilenames[i - 12]);
            String lastYearMonth = shbeHandler.getMonth(SHBEFilenames[i - 12]);
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
        Iterator<SHBE_ClaimID> ite2;
        HashSet<SHBE_ClaimID> totalCouncil;
        totalCouncil = new HashSet<>();
        ite = CouncilSets.keySet().iterator();
        while (ite.hasNext()) {
            String YM3;
            YM3 = ite.next();
            DW_UO_Set s;
            s = CouncilSets.get(YM3);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                SHBE_ClaimID CTBRefID;
                CTBRefID = ite2.next();
                totalCouncil.add(CTBRefID);
            }
        }
        env.ge.log("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
        TreeMap<String, DW_UO_Set> tRSLSets;
        tRSLSets = (TreeMap<String, DW_UO_Set>) UnderOccupiedData[1];
        HashSet<SHBE_ClaimID> totalRSL;
        totalRSL = new HashSet<>();
        ite = tRSLSets.keySet().iterator();
        while (ite.hasNext()) {
            String yM;
            yM = ite.next();
            DW_UO_Set s;
            s = tRSLSets.get(yM);
            ite2 = s.getMap().keySet().iterator();
            while (ite2.hasNext()) {
                SHBE_ClaimID CTBRefID;
                CTBRefID = ite2.next();
                totalRSL.add(CTBRefID);
            }
        }
        env.ge.log("Number of RSL tenants effected by underoccupancy " + totalRSL.size(), true);
        totalCouncil.addAll(totalRSL);
        env.ge.log("Number of Council tenants effected by underoccupancy " + totalCouncil.size(), true);
    }

    /**
     * What?
     *
     * @param mC A map of Council sets.
     * @param mR A map of RSL sets.
     * @return
     */
    private TreeMap<String, DW_UO_Set> combineDW_UO_Sets(
            TreeMap<String, DW_UO_Set> mC, TreeMap<String, DW_UO_Set> mR) {
        TreeMap<String, DW_UO_Set> r = new TreeMap<>();
        String ym3;
        // All
        DW_UO_Set sa;
        // Council
        DW_UO_Set sc;
        // RSL
        DW_UO_Set sr;

        Iterator<String> ite;
        ite = mC.keySet().iterator();
        while (ite.hasNext()) {
            ym3 = ite.next();
            sa = r.get(ym3);
            if (sa == null) {
                sa = new DW_UO_Set(env);
                r.put(ym3, sa);
            }
            sc = mC.get(ym3);
            sa.getMap().putAll(sc.getMap());
        }
        ite = mR.keySet().iterator();
        while (ite.hasNext()) {
            ym3 = ite.next();
            sa = r.get(ym3);
            if (sa == null) {
                sa = new DW_UO_Set(env);
                r.put(ym3, sa);
            }
            sr = mR.get(ym3);
            sa.getMap().putAll(sr.getMap());
        }
        return r;
    }

    /**
     *
     * @param ym3
     * @param key
     * @param m A map of ClaimID To Tenancy Type Lookups.
     * @return
     */
    protected HashMap<SHBE_ClaimID, Integer> loadClaimIDToTTLookup(UKP_YM3 ym3,
            Integer key, HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> m)
            throws IOException, ClassNotFoundException {
        HashMap<SHBE_ClaimID, Integer> r;
        if (m.containsKey(key)) {
            r = m.get(key);
        } else {
            r = new HashMap<>();
            SHBE_Records sr = shbeHandler.getRecords(ym3, env.HOOME);
            Map<SHBE_ClaimID, SHBE_Record> recs = sr.getRecords(env.HOOME);
            Iterator<SHBE_ClaimID> ite = recs.keySet().iterator();
            while (ite.hasNext()) {
                SHBE_ClaimID claimID = ite.next();
                r.put(claimID, recs.get(claimID).getDRecord().getTenancyType());
            }
            m.put(key, r);
        }
        return r;
    }

    /**
     * Loads a ClaimID To PostcodeID Lookup.
     *
     * @param ym3
     * @param key
     * @param m ClaimID To PostcodeID Lookups
     * @return a specific ClaimID To PostcodeID Lookup.
     * @throws java.io.IOException
     */
    protected Map<SHBE_ClaimID, UKP_RecordID> loadClaimIDToPostcodeIDLookup(
            UKP_YM3 ym3, Integer key,
            HashMap<Integer, Map<SHBE_ClaimID, UKP_RecordID>> m)
            throws IOException, Exception {
        Map<SHBE_ClaimID, UKP_RecordID> r;
        if (m.containsKey(key)) {
            r = m.get(key);
        } else {
            SHBE_Records recs = env.getSHBE_Handler().getRecords(ym3, env.HOOME);
            r = recs.getCid2postcodeID(env.HOOME);
            m.put(key, r);
        }
        return r;
    }

    private void writeTransitionFrequencies(
            TreeMap<String, Integer> transitions, Path dirOut, String dirname,
            String name, boolean reportTenancyTransitionBreaks)
            throws IOException {
        if (transitions.size() > 0) {
            Path d = Paths.get(dirOut.toString(), dirname);
            Files.createDirectories(d);
            try (PrintWriter pw = getFrequencyPrintWriter(d, name,
                    reportTenancyTransitionBreaks)) {
                pw.println("Count, Type");
                Iterator<String> ite2 = transitions.keySet().iterator();
                while (ite2.hasNext()) {
                    String type = ite2.next();
                    Integer count = transitions.get(type);
                    pw.println(count + ", " + type);
                }
                pw.flush();
            }
        }
    }

    private PrintWriter getFrequencyPrintWriter(Path dirOut, String name,
            boolean reportTenancyTransitionBreaks) throws IOException {
        Path d = Paths.get(dirOut.toString(), "Frequencies");
        if (reportTenancyTransitionBreaks) {
            d = Paths.get(d.toString(), SHBE_Strings.s_IncludingTenancyTransitionBreaks);
        } else {
            d = Paths.get(d.toString(), SHBE_Strings.s_IncludingTenancyTransitionBreaksNo);
        }
        Files.createDirectories(d);
        Path f = Paths.get(d.toString(), name);
        env.ge.log("Write " + f, true);
        return Generic_IO.getPrintWriter(f, false);
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
     * @throws java.io.IOException
     */
    public void aggregateClaims(boolean doUnderOccupied, boolean doCouncil,
            boolean doRSL, DW_UO_Data DW_UO_Data,
            TreeMap<Integer, TreeMap<String, String>> lookupsFromPostcodeToLevelCode,
            String[] SHBEFilenames, String aPT,
            ArrayList<String> claimantTypes,
            TreeMap<String, ArrayList<String>> TTGroups,
            ArrayList<String> TTsGrouped,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            TreeMap<String, ArrayList<Integer>> includes,
            ArrayList<Integer> levels,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances) throws IOException, Exception {
        TreeMap<Integer, Path> outputDirs = files.getOutputSHBELevelDirsTreeMap(
                levels, doUnderOccupied, doCouncil, doRSL);
        // Init underOccupiedSets
        TreeMap<UKP_YM3, DW_UO_Set> councilUnderOccupiedSets = null;
        TreeMap<UKP_YM3, DW_UO_Set> RSLUnderOccupiedSets = null;
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
        Iterator<Integer> levelsIte;
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
            UKP_YM3 YM30;
            YM30 = shbeHandler.getYM3(SHBEFilenames[i]);
            SHBE_Records recs0 = env.getSHBE_Handler().getRecords(YM30, env.HOOME);
            UKP_YM3 ym30v = recs0.getNearestYM3ForONSPDLookup();
            Map<SHBE_ClaimID, SHBE_Record> records0;
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, Path>>>> claimantTypeTenureLevelTypeDirs;
            claimantTypeTenureLevelTypeDirs = new TreeMap<>();
            claimantTypeTenureLevelTypeAreaCounts = new TreeMap<>();
            claimantTypeTenureLevelTypeTenureCounts = new TreeMap<>();
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType = claimantTypesIte.next();
                // Initialise Dirs
                TreeMap<String, TreeMap<Integer, TreeMap<String, Path>>> TTLevelTypeDirs;
                TTLevelTypeDirs = new TreeMap<>();
                // Initialise AreaCounts
                TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts;
                TTLevelTypeAreaCounts = new TreeMap<>();
                // Initialise TenureCounts
                TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts;
                TTLevelTypeTenureCounts = new TreeMap<>();
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT = TTIte.next();
                    // Initialise Dirs
                    TreeMap<Integer, TreeMap<String, Path>> levelTypeDirs;
                    levelTypeDirs = new TreeMap<>();
                    // Initialise AreaCounts
                    TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts;
                    levelTypeAreaCounts = new TreeMap<>();
                    // Initialise TenureCounts
                    TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts;
                    levelTypeTenureCounts = new TreeMap<>();
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        Integer level = levelsIte.next();
                        // Initialise Dirs
                        Path outDir0 = outputDirs.get(level);
                        outDir0 = Paths.get(outDir0.toString(), includeName);
                        TreeMap<String, Path> typeDirs = new TreeMap<>();
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
                            Path outDir1 = Paths.get(outDir0.toString(),
                                    type, claimantType, TT);
                            Files.createDirectories(outDir1);
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
            records0 = recs0.getRecords(env.HOOME);

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
            Iterator<SHBE_ClaimID> recordsIte;
            recordsIte = records0.keySet().iterator();
            while (recordsIte.hasNext()) {
                SHBE_ClaimID ClaimID;
                ClaimID = recordsIte.next();
                SHBE_Record SHBE_Record0;
                SHBE_Record0 = records0.get(ClaimID);
                SHBE_D_Record DRecord0 = SHBE_Record0.getDRecord();
                String postcode0 = DRecord0.getClaimantsPostcode();
                Integer TT1Integer = DRecord0.getTenancyType();
                String TT1 = TT1Integer.toString();
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT = TTIte.next();
                    ArrayList<String> TTs = TTGroups.get(TT);
                    if (TTs.contains(TT1)) {
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            Integer level = levelsIte.next();
                            TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                            tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                            String claimantType;
                            claimantType = shbeHandler.getClaimantType(DRecord0);
                            Integer TTInt = DRecord0.getTenancyType();
                            if (postcode0 != null) {
                                String areaCode;
                                areaCode = getAreaCode(
                                        level,
                                        postcode0,
                                        tLookupFromPostcodeToLevelCode);
                                String type;
                                type = DW_Strings.sAllClaimants;
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
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>> claimantTypeTenureLevelTypeCountAreas;
            claimantTypeTenureLevelTypeCountAreas = new TreeMap<>();
            //yearMonthClaimantTypeTenureLevelTypeCountAreas.put(yearMonth, claimantTypeTenureLevelTypeCountAreas);
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>> claimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            claimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<>();
            //yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(yearMonth, claimantTypeTenureLevelDistanceTypeDistanceCountAreas);
            // claimantTypeLoop
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType = claimantTypesIte.next();
                TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                TTLevelTypeCountAreas = new TreeMap<>();
                claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                TTIte = TTGroups.keySet().iterator();
                while (TTIte.hasNext()) {
                    String TT;
                    TT = TTIte.next();
                    TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                    levelTypeCountAreas = new TreeMap<>();
                    TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        Integer level = levelsIte.next();
                        TreeMap<String, TreeMap<Integer, TreeSet<String>>> typeCountAreas;
                        typeCountAreas = new TreeMap<>();
                        levelTypeCountAreas.put(level, typeCountAreas);
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            TreeMap<String, Integer> areaCounts;
                            TreeMap<Integer, Integer> TTCounts;
                            Path dir;
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
            TreeMap<UKP_YM3, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>>>> yearMonthClaimantTypeTenureLevelTypeCountAreas;
            yearMonthClaimantTypeTenureLevelTypeCountAreas = new TreeMap<>();

            TreeMap<UKP_YM3, TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>>>> yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas;
            yearMonthClaimantTypeTenureLevelDistanceTypeDistanceCountAreas = new TreeMap<>();

            // Initialise tIDIndexes
            ArrayList<ArrayList<SHBE_ClaimID>> tIDIndexes;
            tIDIndexes = new ArrayList<>();
            if (true) {
//                ArrayList<SHBE_ClaimID> tID_HashSet;
//                tID_HashSet = recs0.getClaimIDToClaimantPersonIDLookup(env.HOOME);
//                tIDIndexes.add(tID_HashSet);
            }

            while (includeIte.hasNext()) {
                i = includeIte.next();
                // Set Year and Month variables
                UKP_YM3 YM31 = shbeHandler.getYM3(SHBEFilenames[i]);
                // Load next data
                SHBE_Records recs1;
                recs1 = env.getSHBE_Handler().getRecords(YM31, env.HOOME);
                HashMap<SHBE_ClaimID, String> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = null;//recs1.getClaimSHBE_ClaimIDToPostcodeLookup();
                HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = null;//recs1.getClaimantIDToTenancyTypeLookup();
                UKP_YM3 ym31v;
                ym31v = recs1.getNearestYM3ForONSPDLookup();
//            String yearMonth = year + month;
//            String lastMonth_yearMonth;
//            String year = shbeHandler.getYear(shbeFilenames[i]);
//            String month = shbeHandler.getMonth(shbeFilenames[i]);
//            String yearMonth = year + month;
//            String lastMonth_yearMonth;
//            lastMonth_yearMonth = getLastMonth_yearMonth(
//                    year,
//                    month,
//                    shbeFilenames,
//                    i,
//                    startIndex);
//            String lastYear_yearMonth;
//            lastYear_yearMonth = getLastYear_yearMonth(
//                    year,
//                    month,
//                    shbeFilenames,
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
//                    ArrayList<SHBE_ClaimID> tID_HashSet;
//                    tID_HashSet = recs1.getClaimantClaimIDs(env.HOOME);
//                    tIDIndexes.add(tID_HashSet);
                }
                //records0 = (TreeMap<String, SHBE_Record>) SHBEData0[0];
                Map<SHBE_ClaimID, SHBE_Record> records1 = recs1.getRecords(env.HOOME);
                /* Initialise A:
                 * output directories;
                 * claimantTypeTenureLevelTypeDirs;
                 * TTLevelTypeDistanceDirs;
                 * TTAreaCount;
                 * TTDistanceAreaCount.
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Path>>>> claimantTypeTenureLevelTypeDirs;
                claimantTypeTenureLevelTypeDirs = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, Path>>>>> claimantTypeTenureLevelTypeDistanceDirs;
                claimantTypeTenureLevelTypeDistanceDirs = new TreeMap<>();
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts;
                claimantTypeTenureLevelTypeAreaCounts = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts;
                claimantTypeTenureLevelTypeDistanceAreaCounts = new TreeMap<>();
                /* Initialise B:
                 * claimantTypeLevelTypeTenureCounts;
                 * claimantTypeLevelTypeDistanceTenureCounts;
                 */
//            TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts;
                claimantTypeTenureLevelTypeTenureCounts = new TreeMap<>();
                TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts
                        = new TreeMap<>();
                // claimantTypeLoop
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypesIte.next();
                    // Initialise Dirs
                    TreeMap<String, TreeMap<Integer, TreeMap<String, Path>>> TTLevelTypeDirs
                            = new TreeMap<>();
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, Path>>>> TTLevelTypeDistanceDirs
                            = new TreeMap<>();
                    // Initialise AreaCounts
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>>> TTLevelTypeAreaCounts
                            = new TreeMap<>();
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>> TTLevelTypeDistanceAreaCounts
                            = new TreeMap<>();
                    // Initialise TenureCounts
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>> TTLevelTypeTenureCounts
                            = new TreeMap<>();
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>> TTLevelTypeDistanceTenureCounts
                            = new TreeMap<>();
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT = TTIte.next();
                        // Initialise Dirs
                        TreeMap<Integer, TreeMap<String, Path>> levelTypeDirs
                                = new TreeMap<>();
                        TreeMap<Integer, TreeMap<String, TreeMap<Double, Path>>> levelTypeDistanceDirs
                                = new TreeMap<>();
                        // Initialise AreaCounts
                        TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>> levelTypeAreaCounts
                                = new TreeMap<>();
                        TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>> levelTypeDistanceAreaCounts
                                = new TreeMap<>();
                        // Initialise TenureCounts
                        TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>> levelTypeTenureCounts
                                = new TreeMap<>();
                        TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>> levelTypeDistanceTenureCounts
                                = new TreeMap<>();
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            Integer level = levelsIte.next();
                            // Initialise Dirs
                            Path outDir0 = outputDirs.get(level);
                            outDir0 = Paths.get(outDir0.toString(), includeName);
                            TreeMap<String, Path> typeDirs = new TreeMap<>();
                            TreeMap<String, TreeMap<Double, Path>> typeDistanceDirs
                                    = new TreeMap<>();
                            // Initialise AreaCounts
                            TreeMap<String, TreeMap<String, Integer>> typeAreaCounts
                                    = new TreeMap<>();
                            TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>> typeDistanceAreaCounts
                                    = new TreeMap<>();
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
                                Path outDir1 = Paths.get(outDir0.toString(), type);
                                outDir1 = Paths.get(outDir1.toString(), claimantType);
                                outDir1 = Paths.get(outDir1.toString(), TT);
                                Files.createDirectories(outDir1);
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
                                TreeMap<Double, Path> distanceDirs;
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
                                    Path outDir1 = Paths.get(outDir0.toString(),
                                            distanceType, claimantType, TT,
                                            "" + distance);
                                    Files.createDirectories(outDir1);
                                    distanceDirs.put(distance, outDir1);
                                    // Initialise AreaCounts
                                    TreeMap<String, Integer> areaCounts
                                            = new TreeMap<>();
                                    distanceAreaCounts.put(distance, areaCounts);
                                    // Initialise TenureCounts
                                    TreeMap<Integer, Integer> TTCounts
                                            = new TreeMap<>();
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
                TreeMap<Integer, TreeMap<String, Integer>> levelUnexpectedCounts;
                levelUnexpectedCounts = new TreeMap<>();
                levelsIte = levels.iterator();
                while (levelsIte.hasNext()) {
                    Integer level = levelsIte.next();
                    TreeMap<String, Integer> unexpectedCounts = new TreeMap<>();
                    levelUnexpectedCounts.put(level, unexpectedCounts);
                }
                // Iterator over records
                Iterator<SHBE_ClaimID> SHBE_ClaimIDIte = records1.keySet().iterator();
                while (SHBE_ClaimIDIte.hasNext()) {
                    SHBE_ClaimID ClaimID;
                    ClaimID = SHBE_ClaimIDIte.next();
                    SHBE_Record Record1;
                    Record1 = records1.get(ClaimID);
                    SHBE_D_Record DRecord1 = Record1.getDRecord();
                    UKP_RecordID PostcodeID1 = Record1.getPostcodeID();
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
                                Integer level = levelsIte.next();
                                TreeMap<String, String> tLookupFromPostcodeToLevelCode;
                                tLookupFromPostcodeToLevelCode = lookupsFromPostcodeToLevelCode.get(level);
                                TreeMap<String, Integer> unexpectedCounts;
                                unexpectedCounts = levelUnexpectedCounts.get(level);
                                String CTBRef1;
                                CTBRef1 = DRecord1.getCouncilTaxBenefitClaimReferenceNumber();
                                SHBE_ClaimID claimID1 = shbeHandler.getC2cid().get(CTBRef1);
                                SHBE_ClaimID claimantSHBE_ClaimID1 = null;
                                String claimantType = shbeHandler.getClaimantType(DRecord1);
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
                                        String areaCode = getAreaCode(level,
                                                tLookupFromPostcodeToLevelCode,
                                                ClaimPostcodeF);
                                        if (areaCode != null) {
                                            String type;
                                            type = DW_Strings.sAllClaimants;
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
                                                UKP_RecordID PostcodeID0;
                                                if (record0 == null) {
//                                        //This is a new entrant to the data
//                                        type = "NewEntrant";
                                                    // If this claimantNINO has never been seen before it is an OnFlow
                                                    boolean hasBeenSeenBefore;
                                                    hasBeenSeenBefore = getHasClaimantBeenSeenBefore(
                                                            claimantSHBE_ClaimID1,
                                                            i,
                                                            include,
                                                            tIDIndexes);
                                                    if (!hasBeenSeenBefore) {
                                                        type = DW_Strings.sOnFlow;
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
                                                        type = DW_Strings.sReturnFlow;
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
                                                        type = DW_Strings.sUnknown;
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
                                                        type = DW_Strings.sStable;
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
                                                        type = DW_Strings.sAllInChurn;
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
                                                        type = DW_Strings.sAllOutChurn;
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
                                                                env.getSHBE_Handler().getPid2point(ym30v).get(PostcodeID0),
                                                                env.getSHBE_Handler().getPid2point(ym31v).get(PostcodeID1),
                                                                env.getSHBE_Handler().getPid2point(ym31v).get(PostcodeID0),
                                                                env.getSHBE_Handler().getPid2point(ym30v).get(PostcodeID1),
                                                                ym30v, ym31v, PostcodeID0, PostcodeID1);
                                                        Iterator<Double> ite3;
                                                        ite3 = distances.iterator();
                                                        while (ite3.hasNext()) {
                                                            double distanceThreshold = ite3.next();
                                                            if (distance > distanceThreshold) {
                                                                // InDistanceChurn
                                                                type = DW_Strings.sInDistanceChurn;
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
                                                                type = DW_Strings.sOutDistanceChurn;
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
                                                                type = DW_Strings.sWithinDistanceChurn;
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
                                                //env.ge.log("No Census code for postcode: " + postcode1);
                                                Generic_Collections.addToMapInteger(
                                                        unexpectedCounts, ClaimPostcodeF, 1);
                                            }
                                        }
                                    }
                                } else {
                                    Generic_Collections.addToMapInteger(
                                            unexpectedCounts, DW_Strings.snull, 1);
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
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>>> TTLevelTypeCountAreas;
                    TTLevelTypeCountAreas = new TreeMap<>();
                    claimantTypeTenureLevelTypeCountAreas.put(claimantType, TTLevelTypeCountAreas);
                    TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>>> TTLevelDistanceTypeDistanceCountAreas;
                    TTLevelDistanceTypeDistanceCountAreas = new TreeMap<>();
                    claimantTypeTenureLevelDistanceTypeDistanceCountAreas.put(claimantType, TTLevelDistanceTypeDistanceCountAreas);
                    TTIte = TTGroups.keySet().iterator();
                    while (TTIte.hasNext()) {
                        String TT;
                        TT = TTIte.next();
                        TreeMap<Integer, TreeMap<String, TreeMap<Integer, TreeSet<String>>>> levelTypeCountAreas;
                        levelTypeCountAreas = new TreeMap<>();
                        TTLevelTypeCountAreas.put(TT, levelTypeCountAreas);
                        TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, TreeSet<String>>>>> levelDistanceTypeDistanceCountAreas;
                        levelDistanceTypeDistanceCountAreas = new TreeMap<>();
                        TTLevelDistanceTypeDistanceCountAreas.put(TT, levelDistanceTypeDistanceCountAreas);
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            Integer level = levelsIte.next();
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
                                Path dir;
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
                                    Path dir;
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
                                env.ge.log("Unexpected Counts for:"
                                        + " Claimant Type " + claimantType
                                        + " Tenure " + TT
                                        + " Level " + level, true);
                                env.ge.log("code,count", true);
                                Iterator<String> unexpectedCountsIte;
                                unexpectedCountsIte = unexpectedCounts.keySet().iterator();
                                while (unexpectedCountsIte.hasNext()) {
                                    String code = unexpectedCountsIte.next();
                                    Integer count = unexpectedCounts.get(code);
                                    env.ge.log("" + code + ", " + count, true);
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
                YM30 = new UKP_YM3(YM31);
                //YM30v = YM31v;
                ym30v = new UKP_YM3(ym31v);
            }
        }
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> YM31CountAreas, UKP_YM3 YM31,
            TreeMap<Integer, Integer> TTCounts, Integer level, Path dir)
            throws IOException {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> r = writeResults(
                    areaCounts, TTCounts, level, dir, YM31);
            int num = 5;
            // Write out areas with biggest increases from last year
            writeExtremeAreaChanges(r, YM31CountAreas, "LastYear", num,
                    dir, YM31);
            return r;
        } else {
            return null;
        }
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, Integer> TTCounts,
            Integer level, Path dir, UKP_YM3 YM3) throws IOException {
        if (areaCounts.size() > 0) {
            int num = 5;
            TreeMap<Integer, TreeSet<String>> r;
            // Write out counts by area
            r = writeCountsByArea(areaCounts, level, dir, YM3);
            // Write out areas with highest counts
            writeAreasWithHighestNumbersOfClaimants(r, num, dir, YM3);
            // Write out counts by TT
            writeCountsByTenure(TTCounts, dir, YM3);
            return r;
        } else {
            return null;
        }
    }

    protected void writeExtremeAreaChanges(
            TreeMap<Integer, TreeSet<String>> countAreas,
            TreeMap<Integer, TreeSet<String>> lastTimeCountAreas,
            String YM30,
            int num,
            Path dir,
            UKP_YM3 YM31) throws IOException {
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
            writeDiffs(absoluteDiffsAreas, "Absolute", YM30, num, dir, YM31);
            TreeMap<Double, TreeSet<String>> relativeDiffsAreas;
            relativeDiffsAreas = getCountAreas(areaRelativeDiffs);
            writeDiffs(relativeDiffsAreas, "Relative", YM30, num, dir, YM31);
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

    protected void writeDiffs(TreeMap<Double, TreeSet<String>> diffsAreas,
            String name, String YM30, int num, Path dir, UKP_YM3 YM31)
            throws IOException {
        if (diffsAreas.size() > 0) {
            String type = "Increases";
            Path p = Paths.get(dir.toString(),
                    "ExtremeAreaChanges" + name + type + YM30 + "_TO_" + YM31 + ".csv");
            PrintWriter pw = Generic_IO.getPrintWriter(p, false);
            Iterator<Double> iteD = diffsAreas.descendingKeySet().iterator();
            writeDiffs(diffsAreas, num, type, pw, iteD);
            type = "Decreases";
            p = Paths.get(dir.toString(),
                    "ExtremeAreaChanges" + name + type + YM30 + "_TO_" + YM31 + ".csv");
            pw = Generic_IO.getPrintWriter(p, false);
            iteD = diffsAreas.keySet().iterator();
            writeDiffs(diffsAreas, num, type, pw, iteD);
        }
    }

    protected void writeDiffs(
            TreeMap<Double, TreeSet<String>> diffsAreas,
            int num,
            String type,
            PrintWriter pw,
            Iterator<Double> iteD) {
        if (diffsAreas.size() > 0) {
            try (pw) {
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
            }
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
     * @param ym3
     */
    protected void writeAreasWithHighestNumbersOfClaimants(
            TreeMap<Integer, TreeSet<String>> countAreas, int num, Path dir,
            UKP_YM3 ym3) throws IOException {
        if (countAreas.size() > 0) {
            Path p = Paths.get(dir.toString(), "HighestClaimants" + ym3 + ".csv");
            PrintWriter pw = Generic_IO.getPrintWriter(p, false);
            pw.println("Area, Count");
            int counter = 0;
            Iterator<Integer> ite = countAreas.descendingKeySet().iterator();
            while (ite.hasNext()) {
                Integer count = ite.next();
                if (counter < num) {
                    TreeSet<String> areas = countAreas.get(count);
                    Iterator<String> ite2 = areas.iterator();
                    while (ite2.hasNext()) {
                        String area = ite2.next();
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
     * @param ym3
     * @return {@code TreeMap<Integer, TreeSet<String>>} count by list of areas.
     * This is an ordered list from minimum to maximum counts.
     */
    protected TreeMap<Integer, TreeSet<String>> writeCountsByArea(
            TreeMap<String, Integer> areaCounts, Integer level, Path dir,
            UKP_YM3 ym3) throws IOException {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> r = new TreeMap<>();
            Path p = Paths.get(dir.toString(), ym3 + ".csv");
            try (PrintWriter pw = Generic_IO.getPrintWriter(p, true)) {
                pw.println(level + ", Count");
                Iterator<String> ite = areaCounts.keySet().iterator();
                while (ite.hasNext()) {
                    String areaCode = ite.next().trim();
                    if (level == UKP_Data.TYPE_UNIT) {
                        if (areaCode.length() != 7) {
                            areaCode = areaCode.replace(" ", "");
                        }
                    }
                    Integer count = areaCounts.get(areaCode);
                    if (count == null) {
                        count = 0;
                    }
                    TreeSet<String> set = r.get(count);
                    if (set == null) {
                        set = new TreeSet<>();
                        set.add(areaCode);
                        r.put(count, set);
                    } else {
                        set.add(areaCode);
                    }
                    pw.println(areaCode + ", " + count);
                }
            }
            return r;
        } else {
            return null;
        }
    }

    private void writeCountsByTenure(TreeMap<Integer, Integer> TTCounts,
            Path dir, UKP_YM3 YM3) throws IOException {
        if (TTCounts.size() > 0) {
            Path p = Paths.get(dir.toString(), "CountsByTenure" + YM3 + ".csv");
            try (PrintWriter pw = Generic_IO.getPrintWriter(p, true)) {
                pw.println("Tenure, Count");
                Iterator<Integer> ite = TTCounts.keySet().iterator();
                while (ite.hasNext()) {
                    Integer TT0 = ite.next();
                    Integer count = TTCounts.get(TT0);
                    pw.println(TT0 + ", " + count);
                }
            }
        }
    }

    private void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Integer, Integer>>>>> claimantTypeTenureLevelTypeTenureCounts,
            String areaCode, String claimantType, String TT, Integer level,
            String type, Integer TTInt) {
        addToAreaCount(claimantTypeTenureLevelTypeAreaCounts, areaCode, claimantType, TT, level, type);
        TreeMap<Integer, Integer> TTCounts = claimantTypeTenureLevelTypeTenureCounts.get(claimantType).get(TT).get(level).get(type);
        Generic_Collections.addToMapInteger(TTCounts, TTInt, 1);
    }

    private void addToResult(
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<Integer, Integer>>>>>> claimantTypeTenureLevelTypeDistanceTenureCounts,
            String areaCode, String claimantType, String TT, Integer level,
            String type, Integer TTInt, double distance) {
        addToAreaCount(claimantTypeTenureLevelTypeDistanceAreaCounts, areaCode, claimantType, TT, level, type, distance);
        TreeMap<Integer, Integer> tenureCounts = claimantTypeTenureLevelTypeDistanceTenureCounts.get(claimantType).get(TT).get(level).get(type).get(distance);
        Generic_Collections.addToMapInteger(tenureCounts, TTInt, 1);
    }

    private void addToAreaCount(
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<String, Integer>>>>> claimantTypeTenureLevelTypeAreaCounts,
            String areaCode, String claimantType, String tenure, Integer level, String type) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeAreaCounts.get(claimantType).get(tenure).get(level).get(type);
        Generic_Collections.addToMapInteger(areaCounts, areaCode, 1);
    }

    private void addToAreaCount(
            TreeMap<String, TreeMap<String, TreeMap<Integer, TreeMap<String, TreeMap<Double, TreeMap<String, Integer>>>>>> claimantTypeTenureLevelTypeDistanceAreaCounts,
            String areaCode, String claimantType, String tenure, Integer level, String type, Double distance) {
        TreeMap<String, Integer> areaCounts = claimantTypeTenureLevelTypeDistanceAreaCounts.get(claimantType).get(tenure).get(level).get(type).get(distance);
//        //Debug
//        if (areaCounts == null) {
//            // No area counts for distance
//            env.ge.log("No area counts for distance " + distance);
//            env.ge.log("claimantType " + claimantType);
//            env.ge.log("tenure " + tenure);
//            env.ge.log("level " + level);
//            env.ge.log("type " + type);
//        }
        Generic_Collections.addToMapInteger(areaCounts, areaCode, 1);
    }
}
