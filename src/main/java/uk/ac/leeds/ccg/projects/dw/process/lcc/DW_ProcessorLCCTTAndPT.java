package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.FileNotFoundException;
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
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_ProcessorLCCTTAndPT extends DW_ProcessorLCC {

    /**
     * For convenience
     */
    protected DW_UO_Handler uoHandler;
    protected SHBE_TenancyType_Handler shbeTTHandler;
    protected Map<String, UKP_RecordID> p2pid;
    protected Map<UKP_RecordID, String> pid2p;
    String sU = DW_Strings.sU;

    public DW_ProcessorLCCTTAndPT(DW_Environment env) throws IOException, Exception {
        super(env);
        uoHandler = env.getUO_Handler();
        shbeTTHandler = env.getSHBE_TenancyType_Handler();
        p2pid = shbeHandler.getP2pid();
        pid2p = shbeHandler.getPid2p();
    }

    public void run(
            boolean DoGrouped,
            boolean DoPostcodeChanges,
            boolean DoAnyTenancyChanges,
            boolean DoTenancyChanges,
            boolean DoTenancyAndPostcodeChanges
    ) throws IOException, ClassNotFoundException, Exception {
        shbeFilenames = shbeHandler.getFilenames();
        ArrayList<String> types;
        types = new ArrayList<>();
        types.add(DW_Strings.sAllClaimants); // Count of all claimants
        types.add(DW_Strings.sNewEntrant); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add(DW_Strings.sOnFlow); // These are people not claiming the previous month and that have not claimed before.
        types.add(DW_Strings.sReturnFlow); // These are people not claiming the previous month but that have claimed before.
        types.add(DW_Strings.sStable); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add(DW_Strings.sAllInChurn); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add(DW_Strings.sAllOutChurn); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        ArrayList<String> distanceTypes = new ArrayList<>();
        distanceTypes.add(DW_Strings.sInDistanceChurn); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add(DW_Strings.sWithinDistanceChurn); // A count of all claimants that have moved within this area.
        distanceTypes.add(DW_Strings.sOutDistanceChurn); // A count of all claimants that have moved out from this area.

        HashMap<Boolean, ArrayList<String>> tts = new HashMap<>();

        boolean doUO = true;
        tts.put(doUO, shbeTTHandler.getTenancyTypeAll(doUO));
        doUO = false;
        tts.put(doUO, shbeTTHandler.getTenancyTypeAll(doUO));

        Object[] ttgs;
        HashMap<Boolean, ArrayList<String>> GTTs = null;
        HashMap<String, String> TTGLookup = null;
        if (DoGrouped) {
            ttgs = shbeTTHandler.getTenancyTypeGroups();
            TTGLookup = shbeTTHandler.getTenancyTypeGroupLookup();
            GTTs = (HashMap<Boolean, ArrayList<String>>) ttgs[1];
        }

        // Includes
        TreeMap<String, ArrayList<Integer>> includes;

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }
        boolean loadData;
        loadData = false;
//        loadData = true;

        ArrayList<Boolean> b;
        b = getArrayListBoolean();

        // Runtime approximately 1 hour 5 minutes.
        includes = shbeHandler.getIncludes();
//        includes.remove(DW_Strings.sIncludeAll);
//        includes.remove(DW_Strings.sIncludeYearly);
//        includes.remove(DW_Strings.sInclude6Monthly);
//        includes.remove(DW_Strings.sInclude3Monthly);
//        includes.remove(DW_Strings.sIncludeMonthly);
//        includes.remove(DW_Strings.sIncludeMonthlySinceApril2013);
//        includes.remove(DW_Strings.sInclude2MonthlySinceApril2013Offset0);
//        includes.remove(DW_Strings.sInclude2MonthlySinceApril2013Offset1);
//        includes.remove(DW_Strings.sIncludeStartEndSinceApril2013);
//        includes.remove(DW_Strings.sIncludeApril2013May2013);

        boolean DoUnderOccupiedData;
        Iterator<Boolean> iteB;
        Iterator<Boolean> iteB0;
        //Iterator<Boolean> iteB1;
        Iterator<Boolean> iteB2;
        Iterator<Boolean> iteB3;
        //Iterator<Boolean> iteB4;
        Iterator<Boolean> iteB5;
        Iterator<Boolean> iteB6;
        //Iterator<Boolean> iteB7;

        Set<SHBE_ClaimID> UOApril2013ClaimIDs;
        UOApril2013ClaimIDs = uoHandler.getUOStartApril2013ClaimIDs(UO_Data);

        iteB3 = b.iterator();
        while (iteB3.hasNext()) {
            DoUnderOccupiedData = iteB3.next();

//            // <Debug to skip to a particular output creation>
//            DoUnderOccupiedData = false;
//            // </Debug to skip to a particular output creation>
            env.ge.log("<DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
            if (DoUnderOccupiedData) {
                TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets = UO_Data.getCouncilUOSets();
                TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets = UO_Data.getRSLUOSets();
                TreeMap<UKP_YM3, DW_UO_Set> AllUOSets = combineDW_UO_Sets(CouncilUOSets, RSLUOSets);
                if (DoPostcodeChanges) {
                    env.ge.log("<DoPostcodeChanges>", true);
                    iteB = b.iterator();
                    while (iteB.hasNext()) {
                        boolean CheckPreviousTenancyType;
                        CheckPreviousTenancyType = iteB.next();
                        env.ge.log("<CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                        iteB2 = b.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.ge.log("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            iteB5 = b.iterator();
                            while (iteB5.hasNext()) {
                                boolean PostcodeChange;
                                PostcodeChange = iteB5.next();
                                env.ge.log("<PostcodeChange " + PostcodeChange + ">", true);
                                iteB6 = b.iterator();
                                while (iteB6.hasNext()) {
                                    boolean CheckPreviousPostcode;
                                    CheckPreviousPostcode = iteB6.next();
                                    env.ge.log("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    boolean DoUOOnlyOnThoseOriginallyUO;
                                    //doUOOnlyOnThoseOriginallyUO = true;
                                    iteB0 = b.iterator();
                                    while (iteB0.hasNext()) {
                                        DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                        env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                        if (DoUOOnlyOnThoseOriginallyUO) {
                                            UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                        } else {
                                            UOApril2013ClaimIDsDummy = null;
                                        }
                                        doPostcodeChanges(shbeFilenames,
                                                tts.get(DoUnderOccupiedData),
                                                includes,
                                                loadData,
                                                CheckPreviousTenancyType,
                                                ReportTenancyTransitionBreaks,
                                                PostcodeChange,
                                                CheckPreviousPostcode,
                                                DoUnderOccupiedData,
                                                AllUOSets,
                                                UOApril2013ClaimIDsDummy,
                                                DoGrouped);
                                        env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    }
                                    env.ge.log("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                }
                                env.ge.log("</PostcodeChange " + PostcodeChange + ">", true);
                            }
                            env.ge.log("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.ge.log("</CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                    }
                    env.ge.log("</DoPostcodeChanges>", true);
                }
                if (DoAnyTenancyChanges) {
                    env.ge.log("<DoAnyTenancyChanges>", true);
                    // TenancyTransitions
                    boolean CheckPreviousTenancyType;
                    iteB = b.iterator();
                    while (iteB.hasNext()) {
                        CheckPreviousTenancyType = iteB.next();
                        env.ge.log("<CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                        iteB2 = b.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.ge.log("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            if (DoTenancyChanges) {
                                env.ge.log("<DoTenancyChanges " + DoTenancyChanges + ">", true);
                                boolean DoUOOnlyOnThoseOriginallyUO;
//                                doUOOnlyOnThoseOriginallyUO = true;
                                iteB0 = b.iterator();
                                while (iteB0.hasNext()) {
                                    DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                    env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                    if (DoUOOnlyOnThoseOriginallyUO) {
                                        UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                    } else {
                                        UOApril2013ClaimIDsDummy = null;
                                    }
                                    doTTTs(shbeFilenames,
                                            tts.get(DoUnderOccupiedData),
                                            includes,
                                            CheckPreviousTenancyType,
                                            ReportTenancyTransitionBreaks,
                                            DoUnderOccupiedData,
                                            AllUOSets,
                                            CouncilUOSets,
                                            RSLUOSets,
                                            UOApril2013ClaimIDsDummy,
                                            TTGLookup,
                                            DoGrouped,
                                            GTTs);
                                    env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                }
                                env.ge.log("</DoTenancyChanges " + DoTenancyChanges + ">", true);
                            }
                            if (DoTenancyAndPostcodeChanges) {
                                env.ge.log("<DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                                iteB5 = b.iterator();
                                while (iteB5.hasNext()) {
                                    boolean PostcodeChange;
                                    PostcodeChange = iteB5.next();
                                    env.ge.log("<PostcodeChange " + PostcodeChange + ">", true);
                                    iteB6 = b.iterator();
                                    while (iteB6.hasNext()) {
                                        boolean CheckPreviousPostcode;
                                        CheckPreviousPostcode = iteB6.next();
                                        env.ge.log("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                        boolean DoUOOnlyOnThoseOriginallyUO;
                                        //doUOOnlyOnThoseOriginallyUO = true;
                                        iteB0 = b.iterator();
                                        while (iteB0.hasNext()) {
                                            DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                            env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                            Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                            if (DoUOOnlyOnThoseOriginallyUO) {
                                                UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                            } else {
                                                UOApril2013ClaimIDsDummy = null;
                                            }
                                            doTTAndPostcodeChangesU(shbeFilenames,
                                                    tts.get(DoUnderOccupiedData),
                                                    includes,
                                                    CheckPreviousTenancyType,
                                                    ReportTenancyTransitionBreaks,
                                                    PostcodeChange,
                                                    CheckPreviousPostcode,
                                                    AllUOSets,
                                                    CouncilUOSets,
                                                    RSLUOSets,
                                                    UOApril2013ClaimIDsDummy,
                                                    DoGrouped);
                                            env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        }
                                        env.ge.log("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    }
                                    env.ge.log("</PostcodeChange " + PostcodeChange + ">", true);
                                }
                                env.ge.log("</DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                            }
                            env.ge.log("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.ge.log("</CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                    }
                    env.ge.log("</DoAnyTenancyChanges>", true);
                }
                env.ge.log("</DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
            } else {
                env.ge.log("<DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
                if (DoPostcodeChanges) {
                    env.ge.log("<DoPostcodeChanges>", true);
                    iteB = b.iterator();
                    while (iteB.hasNext()) {
                        boolean CheckPreviousTenancyType;
                        CheckPreviousTenancyType = iteB.next();
                        env.ge.log("<CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                        iteB2 = b.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.ge.log("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            iteB5 = b.iterator();
                            while (iteB5.hasNext()) {
                                boolean PostcodeChange;
                                PostcodeChange = iteB5.next();
                                env.ge.log("<PostcodeChange " + PostcodeChange + ">", true);
                                iteB6 = b.iterator();
                                while (iteB6.hasNext()) {
                                    boolean CheckPreviousPostcode;
                                    CheckPreviousPostcode = iteB6.next();
                                    env.ge.log("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    boolean DoUOOnlyOnThoseOriginallyUO;
                                    //doUOOnlyOnThoseOriginallyUO = true;
                                    iteB0 = b.iterator();
                                    while (iteB0.hasNext()) {
                                        DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                        env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                        if (DoUOOnlyOnThoseOriginallyUO) {
                                            UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                        } else {
                                            UOApril2013ClaimIDsDummy = null;
                                        }
                                        doPostcodeChanges(shbeFilenames,
                                                tts.get(DoUnderOccupiedData),
                                                includes,
                                                loadData,
                                                CheckPreviousTenancyType,
                                                ReportTenancyTransitionBreaks,
                                                PostcodeChange,
                                                CheckPreviousPostcode,
                                                DoUnderOccupiedData,
                                                null,
                                                UOApril2013ClaimIDsDummy,
                                                DoGrouped);
                                        env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    }
                                    env.ge.log("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                }
                                env.ge.log("</PostcodeChange " + PostcodeChange + ">", true);
                            }
                            env.ge.log("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.ge.log("</CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                    }
                    env.ge.log("</DoPostcodeChanges>", true);
                }
                if (DoAnyTenancyChanges) {
                    env.ge.log("<DoAnyTenancyChanges>", true);
                    // TenancyTransitions
                    boolean CheckPreviousTenancyType;
                    iteB = b.iterator();
                    while (iteB.hasNext()) {
                        CheckPreviousTenancyType = iteB.next();
                        env.ge.log("<CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                        iteB2 = b.iterator();
                        while (iteB2.hasNext()) {
                            boolean ReportTenancyTransitionBreaks;
                            ReportTenancyTransitionBreaks = iteB2.next();
                            env.ge.log("<ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                            if (DoTenancyChanges) {
                                env.ge.log("<DoTenancyChanges " + DoTenancyChanges + ">", true);
                                boolean DoUOOnlyOnThoseOriginallyUO;
                                //doUOOnlyOnThoseOriginallyUO = true;
                                iteB0 = b.iterator();
                                while (iteB0.hasNext()) {
                                    DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                    env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                    Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                    if (DoUOOnlyOnThoseOriginallyUO) {
                                        UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                    } else {
                                        UOApril2013ClaimIDsDummy = null;
                                    }
                                    doTTTs(shbeFilenames,
                                            tts.get(DoUnderOccupiedData),
                                            includes,
                                            CheckPreviousTenancyType,
                                            ReportTenancyTransitionBreaks,
                                            DoUnderOccupiedData,
                                            null,
                                            null,
                                            null,
                                            UOApril2013ClaimIDsDummy,
                                            TTGLookup,
                                            DoGrouped,
                                            GTTs);
                                    env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                }
                                env.ge.log("</DoTenancyChanges " + DoTenancyChanges + ">", true);
                            }
                            if (DoTenancyAndPostcodeChanges) {
                                env.ge.log("<DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                                iteB5 = b.iterator();
                                while (iteB5.hasNext()) {
                                    boolean PostcodeChange;
                                    PostcodeChange = iteB5.next();
                                    env.ge.log("<PostcodeChange " + PostcodeChange + ">", true);
                                    iteB6 = b.iterator();
                                    while (iteB6.hasNext()) {
                                        boolean CheckPreviousPostcode;
                                        CheckPreviousPostcode = iteB6.next();
                                        env.ge.log("<CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                        boolean DoUOOnlyOnThoseOriginallyUO;
                                        //doUOOnlyOnThoseOriginallyUO = true;
                                        iteB0 = b.iterator();
                                        while (iteB0.hasNext()) {
                                            DoUOOnlyOnThoseOriginallyUO = iteB0.next();
                                            env.ge.log("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                            Set<SHBE_ClaimID> UOApril2013ClaimIDsDummy;
                                            if (DoUOOnlyOnThoseOriginallyUO) {
                                                UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
                                            } else {
                                                UOApril2013ClaimIDsDummy = null;
                                            }
                                            doTTAndPostcodeChanges(shbeFilenames,
                                                    tts.get(DoUnderOccupiedData),
                                                    includes,
                                                    CheckPreviousTenancyType,
                                                    ReportTenancyTransitionBreaks,
                                                    PostcodeChange,
                                                    CheckPreviousPostcode,
                                                    UOApril2013ClaimIDsDummy,
                                                    DoGrouped);
                                            env.ge.log("</DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
                                        }
                                        env.ge.log("</CheckPreviousPostcode " + CheckPreviousPostcode + ">", true);
                                    }
                                    env.ge.log("</PostcodeChange " + PostcodeChange + ">", true);
                                }
                                env.ge.log("</DoTenancyAndPostcodeChanges " + DoTenancyAndPostcodeChanges + ">", true);
                            }
                            env.ge.log("</ReportTenancyTransitionBreaks " + ReportTenancyTransitionBreaks + ">", true);
                        }
                        env.ge.log("</CheckPreviousTenancyType " + CheckPreviousTenancyType + ">", true);
                    }
                    env.ge.log("</DoAnyTenancyChanges>", true);
                }
                env.ge.log("</DoUnderOccupiedData " + DoUnderOccupiedData + ">", true);
            }
        }
    }

    /**
     * What?
     *
     * @param underOccupiedData
     * @return
     */
    private TreeMap<UKP_YM3, DW_UO_Set> combineDW_UO_Sets(
            TreeMap<UKP_YM3, DW_UO_Set> DW_UO_SetsCouncil,
            TreeMap<UKP_YM3, DW_UO_Set> DW_UO_SetsRSL) {
        TreeMap<UKP_YM3, DW_UO_Set> result;
        result = new TreeMap<>();
        UKP_YM3 YM3;
        DW_UO_Set DW_UO_SetAll;
        DW_UO_Set DW_UO_SetCouncil;
        DW_UO_Set DW_UO_SetRSL;

        Iterator<UKP_YM3> ite;
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
     * @param DoUnderOccupiedData
     * @param AllUOSets
     * @param CouncilUOSets
     * @param RSLUOSets
     * @param ClaimIDs
     * @param TTGLookup
     * @param DoGrouped This is optional. If DoGrouped is true then grouped
     * tenancy type results are written out too.
     * @param GTTs
     */
    public void doTTTs(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean DoUnderOccupiedData,
            TreeMap<UKP_YM3, DW_UO_Set> AllUOSets,
            TreeMap<UKP_YM3, DW_UO_Set> CouncilUOSets,
            TreeMap<UKP_YM3, DW_UO_Set> RSLUOSets,
            Set<SHBE_ClaimID> ClaimIDs,
            HashMap<String, String> TTGLookup,
            boolean DoGrouped,
            HashMap<Boolean, ArrayList<String>> GTTs) throws IOException, ClassNotFoundException {
        String methodName;
        methodName = "doTTTs(String[],ArrayList<String>,"
                + "TreeMap<String, ArrayList<Integer>>,boolean,boolean,boolean,"
                + "TreeMap<String, DW_UO_Set>,TreeMap<String, DW_UO_Set>,"
                + "TreeMap<String, DW_UO_Set>, Set<SHBE_ClaimID>,boolean)";

        Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs;
        cid2postcodeIDs = new HashMap<>();
        Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID;
        Path dirOut = files.getOutputSHBETablesTenancyTypeTransitionDir(
                //DW_Strings.sPaymentTypeAll,
                checkPreviousTenure);
        dirOut = Paths.get(dirOut.toString(),
                DW_Strings.sWithOrWithoutPostcodeChange);
        Files.createDirectories(dirOut);
        Iterator<String> includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey = includesIte.next();
            Path dirOut2 = Paths.get(dirOut.toString(), includeKey);
            if (ClaimIDs != null) {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sUOInApril2013);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sAll);
            }
            if (DoUnderOccupiedData) {
                dirOut2 = files.getUOFile(dirOut2, true, true, true);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sAll);
            }
            Files.createDirectories(dirOut2);
            // dirOut3 will be for the grouped or ungrouped results.
            Path dirOut3;
            env.ge.log("dirOut " + dirOut2, true);
            ArrayList<Integer> include = includes.get(includeKey);
            Iterator<Integer> includeIte = include.iterator();
            Map<Integer, UKP_YM3> indexYM3s = shbeHandler.getIndexYM3s();
            int i = includeIte.next();
            // Load first data
            String filename = SHBEFilenames[i];
            UKP_YM3 ym30 = shbeHandler.getYM3(filename);
            DW_UO_Set AllUOSet0 = null;
            DW_UO_Set CouncilUOSet0 = null;
            DW_UO_Set RSLUOSet0 = null;
            SHBE_Records shbeRecords0 = shbeHandler.getRecords(ym30, env.HOOME);
            Map<SHBE_ClaimID, SHBE_Record> recs0 = shbeRecords0.getRecords(env.HOOME);
            // ClaimIDToTTLookups
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts = new HashMap<>();
            // ClaimIDToTTLookup0
            Map<SHBE_ClaimID, Integer> cid2tt0 = loadCid2tt(ym30, i, cid2tts);
            HashMap<Integer, Set<SHBE_ClaimID>> UOClaimIDSets = null;
            HashMap<Integer, Set<SHBE_ClaimID>> CouncilUOClaimIDSets = null;
            HashMap<Integer, Set<SHBE_ClaimID>> RSLUOClaimIDSets = null;
            boolean doloop = true;
            if (DoUnderOccupiedData) {
                AllUOSet0 = AllUOSets.get(ym30);
                CouncilUOSet0 = CouncilUOSets.get(ym30);
                RSLUOSet0 = RSLUOSets.get(ym30);
                UOClaimIDSets = new HashMap<>();
                CouncilUOClaimIDSets = new HashMap<>();
                RSLUOClaimIDSets = new HashMap<>();
            }
            if (AllUOSet0 == null) {
                if (DoUnderOccupiedData) {
                    doloop = false;
                } else {
                    cid2postcodeID = loadCid2postcodeID(
                            ym30, i, cid2postcodeIDs);
                    cid2postcodeIDs.put(i, cid2postcodeID);
                }
            } else {
                UOClaimIDSets.put(i, AllUOSet0.getClaimIDs());
                CouncilUOClaimIDSets.put(i, CouncilUOSet0.getClaimIDs());
                RSLUOClaimIDSets.put(i, RSLUOSet0.getClaimIDs());
                cid2postcodeID = loadCid2postcodeID(
                        ym30,
                        i,
                        cid2postcodeIDs);
                cid2postcodeIDs.put(i, cid2postcodeID);
            }
            if (doloop) {
                HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTCs;
                TTCs = new HashMap<>();
                HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> GTTCs;
                GTTCs = new HashMap<>();
//                HashMap<SHBE_ClaimID, ArrayList<String>> TTCs;
//                TTCs = new HashMap<SHBE_ClaimID, ArrayList<String>>();
//                HashMap<SHBE_ClaimID, ArrayList<String>> GTTCs;
//                GTTCs = new HashMap<SHBE_ClaimID, ArrayList<String>>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = SHBEFilenames[i];
                    cid2postcodeIDs.put(i, new HashMap<>());
                    // Set Year and Month variables
                    UKP_YM3 ym31 = shbeHandler.getYM3(filename);
                    env.ge.log("Year Month " + ym31, true);
                    SHBE_Records shbeRecords1 = shbeHandler.getRecords(ym31, env.HOOME);
                    cid2postcodeID = loadCid2postcodeID(ym31, i, cid2postcodeIDs);
                    cid2postcodeIDs.put(i, cid2postcodeID);
                    Map<SHBE_ClaimID, SHBE_Record> records1 = shbeRecords1.getRecords(env.HOOME);
                    // ClaimIDToTTLookup1
                    Map<SHBE_ClaimID, Integer> cid2tt1 = loadCid2tt(ym31, i, cid2tts);
                    // Get TenancyTypeTranistionMatrix
                    TreeMap<String, TreeMap<String, Integer>> tttm;
                    DW_UO_Set DW_UO_SetAll1 = null;
                    DW_UO_Set DW_UO_SetCouncil1 = null;
                    DW_UO_Set DW_UO_SetRSL1 = null;
                    if (DoUnderOccupiedData) {
                        DW_UO_SetAll1 = AllUOSets.get(ym31);
                        DW_UO_SetCouncil1 = CouncilUOSets.get(ym31);
                        DW_UO_SetRSL1 = RSLUOSets.get(ym31);
                    }
                    tttm = getTTTMatrixAndRecordTTT(
                            cid2postcodeIDs,
                            recs0,
                            records1,
                            cid2tt0,
                            cid2tt1,
                            cid2tts,
                            UOClaimIDSets,
                            CouncilUOClaimIDSets,
                            RSLUOClaimIDSets,
                            TTCs,
                            ym30,
                            ym31,
                            checkPreviousTenure,
                            i,
                            include,
                            indexYM3s,
                            DoUnderOccupiedData,
                            AllUOSet0,
                            CouncilUOSet0,
                            RSLUOSet0,
                            DW_UO_SetAll1,
                            DW_UO_SetCouncil1,
                            DW_UO_SetRSL1,
                            ClaimIDs);
                    dirOut3 = Paths.get(dirOut2.toString(), DW_Strings.sGroupedNo);
                    Files.createDirectories(dirOut3);
                    writeTTTMatrix(tttm, ym30, ym31, dirOut3, TTs, false);
                    if (DoGrouped) {
                        // Get TenancyTypeTransitionMatrix
                        TreeMap<String, TreeMap<String, Integer>> GTTTM;
                        GTTTM = aggregate(TTGLookup, DoUnderOccupiedData, tttm);
                        dirOut3 = Paths.get(dirOut2.toString(), DW_Strings.sGrouped);
                        Files.createDirectories(dirOut3);
                        ArrayList<String> GTT;
                        GTT = GTTs.get(DoUnderOccupiedData);
                        writeTTTMatrix(GTTTM, ym30, ym31, dirOut3, GTT, true);
                    }
                    recs0 = records1;
                    //YM30 = YM31;
                    ym30 = new UKP_YM3(ym31);
                    cid2tt0 = cid2tt1;
                    AllUOSet0 = DW_UO_SetAll1;
                    CouncilUOSet0 = DW_UO_SetCouncil1;
                    RSLUOSet0 = DW_UO_SetRSL1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<SHBE_ClaimID> TTCIte;
                // Ungrouped
                dirOut3 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sGroupedNo);
                Files.createDirectories(dirOut3);
                transitions = new TreeMap<>();
                max = 0;
                TTCIte = TTCs.keySet().iterator();
                while (TTCIte.hasNext()) {
                    SHBE_ClaimID ClaimID;
                    ClaimID = TTCIte.next();

                    TreeMap<UKP_YM3, String> transition;
//                    ArrayList<String> transition;

                    transition = TTCs.get(ClaimID);
                    max = Math.max(max, transition.size());

//                    //Debugging code
//                    if (max > (include.size() - 1)) {
//                        int debug = 1;
//                    }
                    String out;
                    out = "";
                    Iterator<UKP_YM3> transitionIte;
                    transitionIte = transition.keySet().iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        UKP_YM3 k;
                        k = transitionIte.next();
                        String t;
                        t = transition.get(k);
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
                            if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToMapInteger(
                                    transitions, out, 1);

                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                if (max > (include.size() - 1)) {
                    env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 737 ", true);
                }
                env.ge.log(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(transitions, dirOut3,
                        "Frequencies.csv", reportTenancyTransitionBreaks);
                if (DoGrouped) {
                    dirOut3 = Paths.get(dirOut2.toString(), DW_Strings.sGrouped);
                    Files.createDirectories(dirOut3);
                    transitions = new TreeMap<>();
                    max = 0;
                    TTCIte = GTTCs.keySet().iterator();
                    while (TTCIte.hasNext()) {
                        SHBE_ClaimID claimID = TTCIte.next();

                        TreeMap<UKP_YM3, String> transition;
//                        ArrayList<String> transition;
                        transition = GTTCs.get(claimID);

                        max = Math.max(max, transition.size());
                        String out;
                        out = "";
                        Iterator<UKP_YM3> transitionIte;
                        transitionIte = transition.keySet().iterator();
                        boolean doneFirst = false;
                        while (transitionIte.hasNext()) {
                            UKP_YM3 k;
                            k = transitionIte.next();
                            String t;
                            t = transition.get(k);
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
                                if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                    out += splitT[0];
                                    doneFirst = true;
                                }
                            } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += ", " + splitT[0];
                            }
                        }
                        if (!out.isEmpty()) {
                            if (transitions.containsKey(out)) {
                                Generic_Collections.addToMapInteger(
                                        transitions, out, 1);
                            } else {
                                transitions.put(out, 1);
                            }
                        }
                    }
                    if (max > (include.size() - 1)) {
                        env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 796 ", true);
                    }
                    env.ge.log(includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1), true);
                    writeTransitionFrequencies(
                            transitions,
                            dirOut3,
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
     * @param shbeFilenames
     * @param tts
     * @param includes
     * @param checkPreviousTenure
     * @param reportTenancyTransitionBreaks
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param uoSetsAll
     * @param uoSetsCouncil
     * @param uoSetsRSL
     * @param uoInApril2013
     * @param DoGrouped This is optional if true then grouped tenancy type
     * results are written out too.
     */
    public void doTTAndPostcodeChangesU(String[] shbeFilenames,
            ArrayList<String> tts,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            TreeMap<UKP_YM3, DW_UO_Set> uoSetsAll,
            TreeMap<UKP_YM3, DW_UO_Set> uoSetsCouncil,
            TreeMap<UKP_YM3, DW_UO_Set> uoSetsRSL,
            Set<SHBE_ClaimID> uoInApril2013,
            boolean DoGrouped) throws IOException, Exception, Exception {
        String methodName;
        methodName = "doTTAndPostcodeChangesU(String[],ArrayList<String>,"
                + "TreeMap<String, ArrayList<Integer>>,boolean,boolean,boolean,"
                + "boolean,TreeMap<String, DW_UO_Set>,"
                + "TreeMap<String, DW_UO_Set>,TreeMap<String, DW_UO_Set>,"
                + "Set<SHBE_ClaimID>,boolean)";
        Path dirOut = files.getOutputSHBETablesTenancyTypeTransitionDir(
                //DW_Strings.sPaymentTypeAll,
                checkPreviousTenure);
        dirOut = Paths.get(dirOut.toString(),
                DW_Strings.sTenancyAndPostcodeChanges);

        Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs
                = new HashMap<>();

        Iterator<String> includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey = includesIte.next();
            Path dirOut2 = Paths.get(dirOut.toString(), includeKey);
            if (uoInApril2013 != null) {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sUOInApril2013);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sAll);
            }
            dirOut2 = files.getUOFile(dirOut2, true, true, true);
            if (postcodeChange) {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sPostcodeChanged);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sPostcodeChangedNo);
            }
            Files.createDirectories(dirOut2);
            // dirOut3 is for grouped or non-grouped results;
            Path dirOut3;
            env.ge.log("dirOut " + dirOut2, true);
            ArrayList<Integer> include = includes.get(includeKey);
            Iterator<Integer> includeIte = include.iterator();
            int i = includeIte.next();
            // Load first data
            String filename = shbeFilenames[i];
            UKP_YM3 ym30 = shbeHandler.getYM3(filename);
            // ClaimIDToTTLookups
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts = new HashMap<>();
            // ClaimIDToTTLookup0
            Map<SHBE_ClaimID, Integer> cid2tt0 = loadCid2tt(ym30, i, cid2tts);
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupancies = null;
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupanciesCouncil = null;
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupanciesRSL = null;
//            HashSet<SHBE_ClaimID> tUnderOccupiedIDs0 = null;
//            HashSet<SHBE_ClaimID> tUnderOccupiedIDs1 = null;
//            if (doUnderOccupied) {
//                underOccupiedSet0 = underOccupiedSets.get(YM30);
//                tUnderOccupancies = new HashMap<Integer, HashSet<SHBE_ClaimID>>();
//                tUnderOccupiedIDs0 = getUnderOccupiedIDs(
//                        underOccupiedSet0,
//                        i,
//                        tUnderOccupancies);
//            }

            DW_UO_Set uoSet0 = null;
            DW_UO_Set CouncilUOSet0 = null;
            DW_UO_Set RSLUOSet0 = null;

            // ClaimIDToPostcodeIDLookup0
            Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID0;
            cid2postcodeID0 = loadCid2postcodeID(
                    ym30, i, cid2postcodeIDs);

            HashMap<Integer, Set<SHBE_ClaimID>> uoClaimIDs = null;
            HashMap<Integer, Set<SHBE_ClaimID>> councilUOClaimIDs = null;
            HashMap<Integer, Set<SHBE_ClaimID>> rslUOClaimIDs = null;

            Set<SHBE_ClaimID> uoClaimIDs0 = null;
//            Set<SHBE_ClaimID> CouncilUOClaimIDs0 = null;
//            Set<SHBE_ClaimID> RSLUOClaimIDs0 = null;
            Set<SHBE_ClaimID> uoClaimIDs1 = null;
//            Set<SHBE_ClaimID> CouncilUOClaimIDs1 = null;
//            Set<SHBE_ClaimID> RSLUOClaimIDs1 = null;

            boolean doloop = true;
            uoClaimIDs = new HashMap<>();
//            CouncilUOClaimIDs = new HashMap<Integer, Set<SHBE_ClaimID>>();
//            RSLUOClaimIDs = new HashMap<Integer, Set<SHBE_ClaimID>>();
            uoSet0 = uoSetsAll.get(ym30);
//            CouncilUOSet0 = DW_UO_SetsCouncil.get(YM30);
//            RSLUOSet0 = DW_UO_SetsRSL.get(YM30);
            if (uoSet0 != null) {
                uoClaimIDs0 = uoSet0.getClaimIDs();
            } else {
                doloop = false;
            }
//            if (CouncilUOSet0 != null) {
//                CouncilUOClaimIDs0 = CouncilUOSet0.getClaimIDs();
//            } else {
//                doloop = false;
//            }
//            if (RSLUOSet0 != null) {
//                RSLUOClaimIDs0 = RSLUOSet0.getClaimIDs();
//            } else {
//                doloop = false;
//            }
            if (doloop) {
                // Init TTCs and GTTCs
                HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> ttcs
                        = new HashMap<>();
                HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> gttcs
                        = new HashMap<>();
                // Main loop
                while (includeIte.hasNext()) {
                    i = includeIte.next();
                    filename = shbeFilenames[i];
                    // Set Year and Month variables
                    UKP_YM3 ym31 = shbeHandler.getYM3(filename);
                    // UOSet1
                    DW_UO_Set uoSet1 = uoSetsAll.get(ym31);
                    uoClaimIDs1 = uoSet1.getClaimIDs();
                    // ClaimIDToTTLookup1
                    Map<SHBE_ClaimID, Integer> cid2tt1 = loadCid2tt(ym31, i, cid2tts);
                    // ClaimIDToPostcodeIDLookup1
                    Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID1
                            = loadCid2postcodeID(ym31, i, cid2postcodeIDs);
                    // Get TenancyTypeTranistionMatrix
                    Map<String, Map<String, Integer>> tttm
                            = getTTTMatrixAndWritePTDetailsU(dirOut2, cid2tt0,
                                    cid2tt1, cid2tts, uoClaimIDs0, uoClaimIDs1,
                                    uoClaimIDs, councilUOClaimIDs, rslUOClaimIDs,
                                    ttcs, ym30, ym31, checkPreviousTenure, i,
                                    include, cid2postcodeID0, cid2postcodeID1,
                                    cid2postcodeIDs, postcodeChange,
                                    checkPreviousPostcode, uoSet0, uoSet1,
                                    uoInApril2013);
                    writeTTTMatrix(tttm, ym30, ym31, dirOut2, tts, false);                    //YM30 = YM31;
                    ym30 = new UKP_YM3(ym31);
                    cid2tt0 = cid2tt1;
                    cid2postcodeID0 = cid2postcodeID1;
                    uoClaimIDs0 = uoClaimIDs1;
                    uoSet0 = uoSet1;
                }
                TreeMap<String, Integer> transitions;
                int max;
                Iterator<SHBE_ClaimID> TTCsITe;
                // Ungrouped
                dirOut3 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sGroupedNo);
                Files.createDirectories(dirOut3);
                transitions = new TreeMap<>();
                max = 0;
                TTCsITe = ttcs.keySet().iterator();
                while (TTCsITe.hasNext()) {
                    SHBE_ClaimID ClaimID;
                    ClaimID = TTCsITe.next();
                    TreeMap<UKP_YM3, String> transition;
                    transition = ttcs.get(ClaimID);
                    max = Math.max(max, transition.size());
                    String out;
                    out = "";
                    Iterator<UKP_YM3> transitionIte;
                    transitionIte = transition.keySet().iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        UKP_YM3 k;
                        k = transitionIte.next();
                        String t;
                        t = transition.get(k);
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
                            if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToMapInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                if (max > (include.size() - 1)) {
                    env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1090 ", true);
                }
                env.ge.log(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(
                        transitions,
                        dirOut3,
                        "Frequencies.csv",
                        reportTenancyTransitionBreaks);
                // Grouped
                if (DoGrouped) {
                    dirOut3 = Paths.get(
                            dirOut2.toString(),
                            DW_Strings.sGrouped);
                    Files.createDirectories(dirOut3);
                    transitions = new TreeMap<>();
                    max = 0;
                    TTCsITe = gttcs.keySet().iterator();
                    while (TTCsITe.hasNext()) {
                        SHBE_ClaimID ClaimID;
                        ClaimID = TTCsITe.next();
                        TreeMap<UKP_YM3, String> transition;
                        transition = gttcs.get(ClaimID);
                        max = Math.max(max, transition.size());
                        String out;
                        out = "";
                        Iterator<UKP_YM3> transitionsIte;
                        transitionsIte = transition.keySet().iterator();
                        boolean doneFirst = false;
                        while (transitionsIte.hasNext()) {
                            UKP_YM3 k;
                            k = transitionsIte.next();
                            String t;
                            t = transition.get(k);
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
                                if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                    out += splitT[0];
                                    doneFirst = true;
                                }
                            } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += ", " + splitT[0];
                            }
                        }
                        if (!out.isEmpty()) {
                            if (transitions.containsKey(out)) {
                                Generic_Collections.addToMapInteger(
                                        transitions, out, 1);
                            } else {
                                transitions.put(out, 1);
                            }
                        }
                    }
                    if (max > (include.size() - 1)) {
                        env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1150 ", true);
                    }
                    env.ge.log(includeKey + " maximum number of transitions "
                            + max + " out of a possible " + (include.size() - 1), true);
                    writeTransitionFrequencies(transitions, dirOut3,
                            "FrequenciesGrouped.csv", reportTenancyTransitionBreaks);
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
     * @param UOInApril2013
     * @param DoGrouped This is optional. If DoGRouped == true then grouped
     * tenancy type results are written out too.
     */
    public void doTTAndPostcodeChanges(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            Set<SHBE_ClaimID> UOInApril2013,
            boolean DoGrouped) throws IOException, Exception {
        String methodName;
        methodName = "doTTAndPostcodeChanges(String[],ArrayList<String>,"
                + "TreeMap<String, ArrayList<Integer>>,boolean,"
                + "boolean,boolean,boolean,boolean)";
        Path dirOut = files.getOutputSHBETablesTenancyTypeTransitionDir(
                //DW_Strings.sPaymentTypeAll,
                checkPreviousTenure);
        dirOut = Paths.get(dirOut.toString(),
                DW_Strings.sTenancyAndPostcodeChanges);

        Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs
                = new HashMap<>();

        Iterator<String> includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey = includesIte.next();
            Path dirOut2 = Paths.get(dirOut.toString(), includeKey);
            if (UOInApril2013 != null) {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sUOInApril2013);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sAll);
            }
            dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sAll);
            if (postcodeChange) {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sPostcodeChanged);
            } else {
                dirOut2 = Paths.get(dirOut2.toString(), DW_Strings.sPostcodeChangedNo);
            }
            Files.createDirectories(dirOut2);

            // dirOut3 is for grouped or ungrouped results
            Path dirOut3;
            env.ge.log("dirOut " + dirOut2, true);
            ArrayList<Integer> include = includes.get(includeKey);
            Iterator<Integer> includeIte = include.iterator();
            int i;
            i = includeIte.next();
            // Load first data
            String filename = SHBEFilenames[i];
            UKP_YM3 ym30 = shbeHandler.getYM3(filename);
            // ClaimIDToTTLookups
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts = new HashMap<>();
            // ClaimIDToTTLookup0
            Map<SHBE_ClaimID, Integer> cid2tt0 = loadCid2tt(ym30, i, cid2tts);
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupancies = null;
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupanciesCouncil = null;
//            HashMap<Integer, HashSet<SHBE_ClaimID>> tUnderOccupanciesRSL = null;
//            HashSet<SHBE_ClaimID> tUnderOccupiedIDs0 = null;
//            HashSet<SHBE_ClaimID> tUnderOccupiedIDs1 = null;
//            if (doUnderOccupied) {
//                underOccupiedSet0 = underOccupiedSets.get(YM30);
//                tUnderOccupancies = new HashMap<Integer, HashSet<SHBE_ClaimID>>();
//                tUnderOccupiedIDs0 = getUnderOccupiedIDs(
//                        underOccupiedSet0,
//                        i,
//                        tUnderOccupancies);
//            }

            // ClaimIDToPostcodeIDLookup0
            Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID0
                    = loadCid2postcodeID(ym30, i, cid2postcodeIDs);

            boolean doloop = true;
            // Init TTCs and GTTCs
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTCs;
            TTCs = new HashMap<>();
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> GTTCs;
            GTTCs = new HashMap<>();
            // Main loop
            while (includeIte.hasNext()) {
                i = includeIte.next();
                filename = SHBEFilenames[i];
                // Set Year and Month variables
                UKP_YM3 ym31 = shbeHandler.getYM3(filename);
                // ClaimIDToTTLookup1
                Map<SHBE_ClaimID, Integer> cid2tt1 = loadCid2tt(ym31, i, cid2tts);
                // ClaimIDToPostcodeIDLookup1
                Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID1
                        = loadCid2postcodeID(ym31, i, cid2postcodeIDs);
                // Get TenancyTypeTranistionMatrix
                TreeMap<String, TreeMap<String, Integer>> tttm
                        = getTTTMatrixAndWritePTDetails(dirOut2, cid2tt0,
                                cid2tt1, cid2tts, TTCs, ym30, ym31,
                                checkPreviousTenure, i, include,
                                cid2postcodeID0, cid2postcodeID1, cid2postcodeIDs,
                                postcodeChange, checkPreviousPostcode,
                                UOInApril2013);
                writeTTTMatrix(tttm, ym30, ym31, dirOut2, TTs, false);
                //YM30 = new UKP_YM3(YM31);
                ym30 = new UKP_YM3(ym31);
                cid2tt0 = cid2tt1;
                cid2postcodeID0 = cid2postcodeID1;
            }
            TreeMap<String, Integer> transitions;
            int max;
            Iterator<SHBE_ClaimID> TTCsITe;
            // Ungrouped
            dirOut3 = Paths.get(dirOut2.toString(), DW_Strings.sGroupedNo);
            Files.createDirectories(dirOut3);
            transitions = new TreeMap<>();
            max = 0;
            TTCsITe = TTCs.keySet().iterator();
            SHBE_ClaimID ClaimID;
            TreeMap<UKP_YM3, String> transition;
            String out;
            Iterator<UKP_YM3> transitionIte;
            String t;
            String[] splitT;
            while (TTCsITe.hasNext()) {
                ClaimID = TTCsITe.next();
                transition = TTCs.get(ClaimID);
                max = Math.max(max, transition.size());
                out = "";
                transitionIte = transition.keySet().iterator();
                boolean doneFirst = false;
                while (transitionIte.hasNext()) {
                    UKP_YM3 k;
                    k = transitionIte.next();
                    t = transition.get(k);
                    splitT = t.split(":");
                    if (reportTenancyTransitionBreaks) {
                        if (!doneFirst) {
                            out += splitT[0];
                            doneFirst = true;
                        } else {
                            out += ", " + splitT[0];
                        }
                    } else if (!doneFirst) {
                        if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out += splitT[0];
                            doneFirst = true;
                        }
                    } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                        out += ", " + splitT[0];
                    }
                }
                if (!out.isEmpty()) {
                    if (transitions.containsKey(out)) {
                        Generic_Collections.addToMapInteger(
                                transitions, out, 1);
                    } else {
                        transitions.put(out, 1);
                    }
                }
            }
            if (max > (include.size() - 1)) {
                env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1375 ", true);
            }
            env.ge.log(includeKey
                    + " maximum number of transitions " + max
                    + " out of a possible " + (include.size() - 1), true);
            writeTransitionFrequencies(
                    transitions,
                    dirOut3,
                    "Frequencies.csv",
                    reportTenancyTransitionBreaks);
            // Grouped
            if (DoGrouped) {
                dirOut3 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sGrouped);
                Files.createDirectories(dirOut3);
                transitions = new TreeMap<>();
                max = 0;
                TTCsITe = GTTCs.keySet().iterator();
                while (TTCsITe.hasNext()) {
                    ClaimID = TTCsITe.next();
                    transition = GTTCs.get(ClaimID);
                    max = Math.max(max, transition.size());
                    out = "";
                    transitionIte = transition.keySet().iterator();
                    boolean doneFirst = false;
                    while (transitionIte.hasNext()) {
                        UKP_YM3 k;
                        k = transitionIte.next();
                        t = transition.get(k);
                        splitT = t.split(":");
                        if (reportTenancyTransitionBreaks) {
                            if (!doneFirst) {
                                out += splitT[0];
                                doneFirst = true;
                            } else {
                                out += ", " + splitT[0];
                            }
                        } else if (!doneFirst) {
                            if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToMapInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                if (max > (include.size() - 1)) {
                    env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1430 ", true);
                }
                env.ge.log(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(
                        transitions,
                        dirOut3,
                        "FrequenciesGrouped.csv",
                        reportTenancyTransitionBreaks);
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
     * @param DoUnderOccupiedData
     * @param DW_UO_SetsAll
     * @param ClaimIDs
     * @param DoGrouped This is optional. If true then frequencies for grouped
     * tenancies are also written out.
     */
    public void doPostcodeChanges(
            String[] SHBEFilenames,
            ArrayList<String> TTs,
            TreeMap<String, ArrayList<Integer>> includes,
            boolean loadData,
            boolean checkPreviousTenure,
            boolean reportTenancyTransitionBreaks,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            //TreeMap<String, DW_UO_Set> DW_UO_SetsCouncil,
            //TreeMap<String, DW_UO_Set> DW_UO_SetsRSL,
            boolean DoUnderOccupiedData,
            TreeMap<UKP_YM3, DW_UO_Set> DW_UO_SetsAll,
            Set<SHBE_ClaimID> ClaimIDs,
            boolean DoGrouped) throws IOException, Exception {
        String methodName;
        methodName = "doPostcodeChanges(String[],ArrayList<String>,"
                + "TreeMap<String, ArrayList<Integer>>,boolean,"
                + "boolean,boolean,boolean,boolean,boolean,"
                + "TreeMap<String, DW_UO_Set>,Set<SHBE_ClaimID>,boolean)";
        Path dirOut;
        dirOut = Paths.get(
                files.getOutputSHBETablesDir().toString(),
                DW_Strings.sPostcodeChanges);
        if (DoUnderOccupiedData) {
            dirOut = files.getUOFile(dirOut, true, true, true);
        } else {
            dirOut = Paths.get(
                    dirOut.toString(),
                    DW_Strings.s_A);
        }
        if (postcodeChange) {
            dirOut = Paths.get(
                    dirOut.toString(),
                    DW_Strings.sPostcodeChanged);
        } else {
            dirOut = Paths.get(
                    dirOut.toString(),
                    DW_Strings.sPostcodeChangedNo);
        }
        Files.createDirectories(dirOut);
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey;
            includeKey = includesIte.next();
            Path dirOut2;
            dirOut2 = Paths.get(
                    dirOut.toString(),
                    includeKey);
            if (ClaimIDs != null) {
                dirOut2 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sUOInApril2013);
            } else {
                dirOut2 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sAll);
            }
            if (DoUnderOccupiedData) {
                dirOut2 = files.getUOFile(
                        dirOut2,
                        true,
                        true,
                        true);
            } else {
                dirOut2 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sAll);
            }
            Files.createDirectories(dirOut2);
            // dirOut3 is for the grouped or nongrouped outputs.
            Path dirOut3;
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
            UKP_YM3 YM30;
            YM30 = shbeHandler.getYM3(filename);
            // UOSet0
            DW_UO_Set UOSet0 = null;
            // ClaimIDToTTLookups
            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups;
            ClaimIDToTTLookups = new HashMap<>();
            // ClaimIDToTTLookup0
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0;
            ClaimIDToTTLookup0 = loadCid2tt(
                    YM30,
                    i,
                    ClaimIDToTTLookups);
            // ClaimIDToPostcodeIDLookups
            HashMap<Integer, HashMap<SHBE_ClaimID, UKP_RecordID>> ClaimIDToPostcodeIDLookups;
            ClaimIDToPostcodeIDLookups = new HashMap<>();
            // ClaimIDToPostcodeIDLookup0
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup0;
            ClaimIDToPostcodeIDLookup0 = loadCid2postcodeID(
                    YM30, i, ClaimIDToPostcodeIDLookups);
            HashMap<Integer, Set<SHBE_ClaimID>> UOClaimIDSets = null;
            Set<SHBE_ClaimID> UOClaimIDSet0 = null;
            Set<SHBE_ClaimID> UOClaimIDSet1 = null;
            if (DoUnderOccupiedData) {
                UOClaimIDSets = new HashMap<>();
                UOSet0 = DW_UO_SetsAll.get(YM30);
                if (UOSet0 == null) {
                    env.ge.log("UnderOccupiedSet0 == null, YM30 = " + YM30, true);
                } else {
                    UOClaimIDSet0 = UOSet0.getClaimIDs();
                }
            }
            // Init TenancyTypeChanges and GroupedTenancyTypeChanges
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTCs;
            TTCs = new HashMap<>();
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> GTTCs;
            GTTCs = new HashMap<>();
            // Main loop
            while (includeIte.hasNext()) {
                i = includeIte.next();
                filename = SHBEFilenames[i];
                // Set Year and Month variables
                UKP_YM3 YM31 = shbeHandler.getYM3(filename);
                // UOSet1
                DW_UO_Set UOSet1 = null;
                // ClaimIDToTTLookup1
                HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1;
                ClaimIDToTTLookup1 = loadCid2tt(
                        YM31,
                        i,
                        ClaimIDToTTLookups);
                // ClaimIDToPostcodeIDLookup1
                HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup1;
                ClaimIDToPostcodeIDLookup1 = loadCid2postcodeID(
                        //loadData,
                        YM31, i, ClaimIDToPostcodeIDLookups);
                if (DoUnderOccupiedData) {
                    UOSet1 = DW_UO_SetsAll.get(YM31);
                    if (UOSet1 == null) {
                        env.ge.log("UnderOccupiedSet1 == null, YM31 = " + YM31, true);
                    } else {
                        UOClaimIDSet1 = UOSet1.getClaimIDs();
                    }
                }
                // Get PostcodeTransitionCounts
                TreeMap<String, TreeMap<String, Integer>> postcodeTransitionCounts;
                if ((UOClaimIDSet0 == null || UOClaimIDSet1 == null) && DoUnderOccupiedData) {
                    env.ge.log("Not calculating or writing out postcodeTransitionCounts", true);
                } else {
                    postcodeTransitionCounts = getPTCountsNoTTT(
                            dirOut2,
                            DoUnderOccupiedData,
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
                            TTs,
                            false);
                }
                //YM30 = YM31;
                YM30 = new UKP_YM3(YM31);
                ClaimIDToTTLookup0 = ClaimIDToTTLookup1;
                ClaimIDToPostcodeIDLookup0 = ClaimIDToPostcodeIDLookup1;
                UOClaimIDSet0 = UOClaimIDSet1;
                UOSet0 = UOSet1;
            }
            // Deal with the frequency of under occupancy changes.
            TreeMap<String, Integer> transitions;
            int max;
            Iterator<SHBE_ClaimID> TTCIte;
            // Ungrouped
            dirOut3 = Paths.get(
                    dirOut2.toString(),
                    DW_Strings.sGroupedNo);
            Files.createDirectories(dirOut3);
            transitions = new TreeMap<>();
            max = 0;
            TTCIte = TTCs.keySet().iterator();
            while (TTCIte.hasNext()) {
                SHBE_ClaimID ClaimID;
                ClaimID = TTCIte.next();
                TreeMap<UKP_YM3, String> transition;
                transition = TTCs.get(ClaimID);
                max = Math.max(max, transition.size());
                String out;
                out = "";
                Iterator<UKP_YM3> transitionIte;
                transitionIte = transition.keySet().iterator();
                boolean doneFirst = false;
                while (transitionIte.hasNext()) {
                    UKP_YM3 k;
                    k = transitionIte.next();
                    String t;
                    t = transition.get(k);
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
                        if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out = splitT[0];
                            doneFirst = true;
                        }
                    } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                        out += ", " + splitT[0];
                    }
                }
                if (!out.isEmpty()) {
                    if (transitions.containsKey(out)) {
                        Generic_Collections.addToMapInteger(
                                transitions, out, 1);
                    } else {
                        transitions.put(out, 1);
                    }
                }
            }
            if (max > (include.size() - 1)) {
                env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1692 ", true);
            }
            env.ge.log(includeKey + " maximum number of transitions "
                    + max + " out of a possible " + (include.size() - 1), true);
            writeTransitionFrequencies(
                    transitions,
                    dirOut3,
                    "Frequencies.txt",
                    reportTenancyTransitionBreaks);
            // Grouped
            if (DoGrouped) {
                dirOut3 = Paths.get(
                        dirOut2.toString(),
                        DW_Strings.sGrouped);
                Files.createDirectories(dirOut3);
                transitions = new TreeMap<>();
                max = 0;
                TTCIte = GTTCs.keySet().iterator();
                while (TTCIte.hasNext()) {
                    SHBE_ClaimID ClaimID;
                    ClaimID = TTCIte.next();
                    TreeMap<UKP_YM3, String> transition;
                    transition = GTTCs.get(ClaimID);
                    max = Math.max(max, transition.size());
                    String out;
                    out = "";
                    Iterator<UKP_YM3> transitionsIte;
                    transitionsIte = transition.keySet().iterator();
                    boolean doneFirst = false;
                    while (transitionsIte.hasNext()) {
                        UKP_YM3 k;
                        k = transitionsIte.next();
                        String t;
                        t = transition.get(k);
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
                            if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                                out += splitT[0];
                                doneFirst = true;
                            }
                        } else if (!splitT[0].contains(shbeTTHandler.sMinus999)) {
                            out += ", " + splitT[0];
                        }
                    }
                    if (!out.isEmpty()) {
                        if (transitions.containsKey(out)) {
                            Generic_Collections.addToMapInteger(
                                    transitions, out, 1);
                        } else {
                            transitions.put(out, 1);
                        }
                    }
                }
                if (max > (include.size() - 1)) {
                    env.ge.log("Warning: " + this.getClass().getName() + "." + methodName + " line 1752 ", true);
                }
                env.ge.log(includeKey + " maximum number of transitions "
                        + max + " out of a possible " + (include.size() - 1), true);
                writeTransitionFrequencies(
                        transitions,
                        dirOut3,
                        "FrequenciesGrouped.txt",
                        reportTenancyTransitionBreaks);
            }
        }
    }

    protected Map<SHBE_ClaimID, Integer> loadCid2tt(UKP_YM3 ym3, Integer key,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts) throws IOException, ClassNotFoundException {
        if (cid2tts.containsKey(key)) {
            return cid2tts.get(key);
        }
        Map<SHBE_ClaimID, Integer> r = new HashMap<>();
        SHBE_Records shbeRecords = shbeHandler.getRecords(ym3, env.HOOME);
        Map<SHBE_ClaimID, SHBE_Record> records = shbeRecords.getRecords(env.HOOME);
        Iterator<SHBE_ClaimID> ite = records.keySet().iterator();
        while (ite.hasNext()) {
            r.put(ite.next(), records.get(cid).getDRecord().getTenancyType());
        }
        cid2tts.put(key, r);
        return r;
    }

    protected Map<SHBE_ClaimID, UKP_RecordID> loadCid2postcodeID(
            UKP_YM3 ym3, Integer key,
            Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2pids)
            throws IOException, ClassNotFoundException {
        Map<SHBE_ClaimID, UKP_RecordID> r;
        if (cid2pids.containsKey(key)) {
            return cid2pids.get(key);
        }
        r = shbeHandler.getRecords(ym3, env.HOOME).getCid2postcodeID(env.HOOME);
        cid2pids.put(key, r);
        return r;
    }

    private void writeTransitionFrequencies(
            TreeMap<String, Integer> transitions,
            Path dirOut,
            //String dirname,
            String name,
            boolean reportTenancyTransitionBreaks) throws IOException {
        if (transitions.size() > 0) {
            Path dirOut2;
            dirOut2 = dirOut;
//            dirOut2 = Paths.get(
//                    dirOut,
//                    dirname);
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

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, TreeSet<String>> YM31CountAreas,
            String ym31,
            TreeMap<Integer, Integer> ttCounts,
            String level,
            Path dir) throws IOException {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result = writeResults(
                    areaCounts, ttCounts, level, dir, ym31);
            int num = 5;
            // Write out areas with biggest increases from last year
            writeExtremeAreaChanges(result, YM31CountAreas, "LastYear", num,
                    dir, ym31);
            return result;
        } else {
            return null;
        }
    }

    protected TreeMap<Integer, TreeSet<String>> writeResults(
            TreeMap<String, Integer> areaCounts,
            TreeMap<Integer, Integer> TTCounts,
            String level, Path dir, String YM3) {
        if (areaCounts.size() > 0) {
            int num = 5;
            TreeMap<Integer, TreeSet<String>> result;
            // Write out counts by area
            result = writeCountsByArea(areaCounts, level, dir, YM3);
            // Write out areas with highest counts
            writeAreasWithHighestNumbersOfClaimants(result, num, dir, YM3);
            // Write out counts by TT
            writeCountsByTenure(TTCounts, dir, YM3);
            return result;
        } else {
            return null;
        }
    }

    protected void writeExtremeAreaChanges(
            TreeMap<Integer, TreeSet<String>> countAreas,
            TreeMap<Integer, TreeSet<String>> lastTimeCountAreas,
            String YM30, int num, Path dir, String YM31) throws IOException {
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
    public double getRelativeDifference(double a, double b, int n) {
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
            String name, String YM30, int num, Path dir, String YM31) throws IOException {
        if (diffsAreas.size() > 0) {
            PrintWriter pw;
            String type = "Increases";
            Path p = Paths.get(dir.toString(),
                    "ExtremeAreaChanges" + name + type + YM30 + "_TO_" + YM31 + ".csv");
            pw = Generic_IO.getPrintWriter(p, false);
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

    protected void writeDiffs(TreeMap<Double, TreeSet<String>> diffsAreas, int num,
            String type, PrintWriter pw, Iterator<Double> iteD) {
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
            TreeMap<Integer, TreeSet<String>> countAreas, int num, Path dir,
            String YM3) {
        if (countAreas.size() > 0) {
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(dir,
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
            TreeMap<String, Integer> areaCounts, String level, Path dir,
            String YM3) {
        if (areaCounts.size() > 0) {
            TreeMap<Integer, TreeSet<String>> result;
            result = new TreeMap<>();
            PrintWriter pw;
            pw = init_OutputTextFilePrintWriter(dir, YM3 + ".csv");
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
            Path dir,
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
        Generic_Collections.addToMapInteger(
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
        Generic_Collections.addToMapInteger(
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
//            env.ge.log("No area counts for distance " + distance);
//            env.ge.log("claimantType " + claimantType);
//            env.ge.log("tenure " + tenure);
//            env.ge.log("level " + level);
//            env.ge.log("type " + type);
//        }
        Generic_Collections.addToMapInteger(
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
            SHBE_ClaimID ClaimID,
            HashMap<String, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
            String YM30) {
        HashMap<SHBE_ClaimID, Integer> ClaimIDByTTLookup;
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
            SHBE_ClaimID ClaimID,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTTs,
            //HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
            UKP_YM3 YM3,
            String TTT) {
        TreeMap<UKP_YM3, String> TTCs;
        TTCs = TTTs.get(ClaimID);
        if (TTCs == null) {
            TTCs = new TreeMap<>();
            TTTs.put(ClaimID, TTCs);
        }

//        // Debugging code
//        if (TTCs.containsKey(YM3)) {
//            int DEBUG = 1;
//            System.out.println("TTTs for ClaimID " + ClaimID + 
//                    " already contains the transition " + TTCs.get(YM3) + 
//                    " for " + YM3 + " overwriting with " + TTT + "!");
//        }
        TTCs.put(YM3, TTT + ":" + YM3);
//        ArrayList<String> TTCs;
//        TTCs = TTTs.get(ClaimID);
//        if (TTCs == null) {
//            TTCs = new ArrayList<String>();
//            TTTs.put(ClaimID, TTCs);
//        }
//        TTCs.add(TTT + ":" + YM3);
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
//            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
//            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
//            HashMap<String, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
//            String YM30) {
//        Object[] result;
//        result = new Object[2];
//        TreeMap<Integer, TreeMap<Integer, Integer>> TTTMatrix;
//        TTTMatrix = new TreeMap<Integer, TreeMap<Integer, Integer>>();
//        result[0] = TTTMatrix;
//        HashMap<SHBE_ClaimID, String> TTT;
//        TTT = new HashMap<SHBE_ClaimID, String>();
//        result[1] = TTT;
//        Iterator<SHBE_ClaimID> ite;
//        // Go through all current claims
//        ite = ClaimIDToTTLookup1.keySet().iterator();
//        while (ite.hasNext()) {
//            SHBE_ClaimID ClaimID;
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
//            SHBE_ClaimID ClaimID;
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
     * @param cid2postcodeIDs
     * @param indexYM3s
     * @param cid
     * @param cid2tts
     * @param uoClaimIDSets
     * @param councilUOClaimIDSets
     * @param rslUOClaimIDSets
     * @param i
     * @param include
     * @return Previous TenanctType and Postcode
     */
    public Object[] getPreviousTTAndPU(
            Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs,
            Map<Integer, UKP_YM3> indexYM3s, SHBE_ClaimID cid,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts,
            HashMap<Integer, Set<SHBE_ClaimID>> uoClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> councilUOClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> rslUOClaimIDSets, int i,
            ArrayList<Integer> include) {
        Object[] r = new Object[4];
        ListIterator<Integer> li;
        int index = include.indexOf(i);
        UKP_YM3 ym3 = indexYM3s.get(index);
        UKP_RecordID postcodeID = null;
        String postcode = DW_Strings.sAAN_NAA;
        Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID;

//        env.ge.log("i " + i);
//        env.ge.log("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tt = null;
        String uo = "";
        while (li.hasPrevious()) {
            Integer previousIndex = li.previous();
            ym3 = indexYM3s.get(previousIndex);
            r[3] = ym3;
            cid2postcodeID = cid2postcodeIDs.get(previousIndex);
            //env.logO("previousIndex " + previousIndex);
            Map<SHBE_ClaimID, Integer> cid2tt = cid2tts.get(previousIndex);
            if (cid2tt != null) {
                tt = cid2tt.get(cid);
                Set<SHBE_ClaimID> uoClaimIDSet = uoClaimIDSets.get(previousIndex);
                if (uoClaimIDSet != null) {
                    if (uoClaimIDSet.contains(cid)) {
                        uo = sU;
                        if (tt == null) {
                            tt = -999;
                            Set<SHBE_ClaimID> councilUOClaimIDSet
                                    = councilUOClaimIDSets.get(previousIndex);
                            if (councilUOClaimIDSet.contains(cid)) {
                                r[0] = Integer.toString(tt) + uo + "1";
                            } else {
                                r[0] = Integer.toString(tt) + uo + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            postcodeID = cid2postcodeID.get(cid);
                            if (postcodeID == null) {
                                postcode = DW_Strings.sAAN_NAA;
                            } else {
                                postcode = pid2p.get(postcodeID);
                            }
                            r[2] = postcode;
                            return r;
                        } else if (tt == -999) {
                            Set<SHBE_ClaimID> rslUOClaimIDSet
                                    = rslUOClaimIDSets.get(previousIndex);
                            if (rslUOClaimIDSet.contains(cid)) {
                                r[0] = Integer.toString(tt) + uo + "4";
                            } else {
                                r[0] = Integer.toString(tt) + uo + "1";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            r[2] = DW_Strings.sAAN_NAA;
                            return r;
                        } else {
                            r[0] = Integer.toString(tt) + uo;
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            postcodeID = cid2postcodeID.get(cid);
                            if (postcodeID == null) {
                                postcode = DW_Strings.sAAN_NAA;
                            } else {
                                postcode = pid2p.get(postcodeID);
                            }
                            r[2] = postcode;
                            return r;
                        }
                    }
                }
                if (tt != null) {
                    if (tt != -999) {
                        r[0] = Integer.toString(tt) + uo;
                        //result[1] = include.indexOf(previousIndex);
                        r[1] = previousIndex;
                        r[2] = DW_Strings.sAAN_NAA;
                        return r;
                    }
                }
            }
        }
        if (tt == null) {
            tt = -999;
        }
        r[0] = Integer.toString(tt) + uo;
        r[1] = null;
        if (postcodeID == null) {
            postcode = DW_Strings.sAAN_NAA;
        } else {
            postcode = pid2p.get(postcodeID);
        }
        r[2] = postcode;
        r[3] = ym3;
        return r;
    }

    /**
     *
     * @param cid2postcodeIDs
     * @param indexYM3s
     * @param cid
     * @param cid2tts
     * @param i
     * @param include
     * @return Previous TenancyType and Postcode
     */
    public Object[] getPreviousTTAndP(
            Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs,
            Map<Integer, UKP_YM3> indexYM3s, SHBE_ClaimID cid,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts, int i,
            ArrayList<Integer> include) {
        Object[] r = new Object[4];
        ListIterator<Integer> li;
        int index = include.indexOf(i);
        UKP_YM3 ym3 = indexYM3s.get(index);
        UKP_RecordID postcodeID = null;
        String postcode = DW_Strings.sAAN_NAA;
        Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID;

//        env.ge.log("i " + i);
//        env.ge.log("index " + index);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tt = null;
        while (li.hasPrevious()) {
            Integer previousIndex = li.previous();
            ym3 = indexYM3s.get(previousIndex);
            r[3] = ym3;
            cid2postcodeID = cid2postcodeIDs.get(previousIndex);
            //env.logO("previousIndex " + previousIndex);
            Map<SHBE_ClaimID, Integer> cid2tt = cid2tts.get(previousIndex);
            if (cid2tt != null) {
                tt = cid2tt.get(cid);
                if (tt == null) {
                    tt = -999;
                    //result[1] = include.indexOf(previousIndex);
                    r[1] = previousIndex;
                    postcodeID = cid2postcodeID.get(cid);
                    if (postcodeID == null) {
                        postcode = DW_Strings.sAAN_NAA;
                    } else {
                        postcode = pid2p.get(postcodeID);
                    }
                    r[2] = postcode;
                    //return result;
                } else if (tt == -999) {
                    //result[1] = include.indexOf(previousIndex);
                    r[1] = previousIndex;
                    r[2] = DW_Strings.sAAN_NAA;
                    return r;
                } else {
                    r[0] = Integer.toString(tt);
                    //result[1] = include.indexOf(previousIndex);
                    r[1] = previousIndex;
                    postcodeID = cid2postcodeID.get(cid);
                    if (postcodeID == null) {
                        postcode = DW_Strings.sAAN_NAA;
                    } else {
                        postcode = pid2p.get(postcodeID);
                    }
                    r[2] = postcode;
                    return r;
                }
            }
        }
        if (tt == null) {
            tt = -999;
        }
        r[0] = Integer.toString(tt);
        r[1] = null;
        if (postcodeID == null) {
            postcode = DW_Strings.sAAN_NAA;
        } else {
            postcode = pid2p.get(postcodeID);
        }
        r[2] = postcode;
        r[3] = ym3;
        return r;
    }

    /**
     *
     * @param ClaimID
     * @param cif2tts
     * @param uoClaimIDSets
     * @param councilUOClaimIDSets
     * @param rslUOClaimIDSets
     * @param i
     * @param include
     * @return
     */
    public Object[] getPreviousTT(SHBE_ClaimID cid,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cif2tts,
            HashMap<Integer, Set<SHBE_ClaimID>> uoClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> councilUOClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> rslUOClaimIDSets, int i,
            ArrayList<Integer> include) {
        Object[] r = new Object[2];
        ListIterator<Integer> li;
        int index = include.indexOf(i);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tt = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex = li.previous();
//            env.ge.log("previousIndex " + previousIndex);
            Map<SHBE_ClaimID, Integer> cid2tt = cif2tts.get(previousIndex);
            Set<SHBE_ClaimID> uoClaimIDSet = uoClaimIDSets.get(previousIndex);
            if (cid2tt != null) {
                tt = cid2tt.get(cid);
                if (uoClaimIDSet != null) {
                    if (uoClaimIDSet.contains(cid)) {
                        underOccupancy = sU;
                        if (tt == null) {
                            tt = -999;
                            Set<SHBE_ClaimID> councilUOClaimIDSet
                                    = councilUOClaimIDSets.get(previousIndex);
                            if (councilUOClaimIDSet.contains(cid)) {
                                r[0] = Integer.toString(tt) + underOccupancy + "1";
                            } else {
                                r[0] = Integer.toString(tt) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            return r;
                        } else if (tt == -999) {
                            Set<SHBE_ClaimID> councilUOClaimIDSet
                                    = councilUOClaimIDSets.get(previousIndex);
                            if (councilUOClaimIDSet.contains(cid)) {
                                r[0] = Integer.toString(tt) + underOccupancy + "1";
                            } else {
                                r[0] = Integer.toString(tt) + underOccupancy + "4";
                            }
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            return r;
                        } else {
                            r[0] = Integer.toString(tt) + underOccupancy;
                            //result[1] = include.indexOf(previousIndex);
                            r[1] = previousIndex;
                            return r;
                        }
                    }
                }
                if (tt != null) {
                    if (tt != -999) {
                        r[0] = Integer.toString(tt) + underOccupancy;
                        //result[1] = include.indexOf(previousIndex);
                        r[1] = previousIndex;
                        return r;
                    }
                }
            }
        }
        if (tt == null) {
            tt = -999;
        }
        r[0] = Integer.toString(tt) + underOccupancy;
        r[1] = null;
        return r;
    }

    /**
     *
     * @param cid
     * @param cid2tts
     * @param i
     * @param include
     * @param uoClaimIDSets
     * @return
     */
    public Object[] getPreviousTT(SHBE_ClaimID cid,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts,
            HashMap<Integer, Set<SHBE_ClaimID>> uoClaimIDSets, int i,
            ArrayList<Integer> include) {
        Object[] r = new Object[2];
        ListIterator<Integer> li;
        int index = include.indexOf(i);
        li = include.listIterator(index); // Start listIterator at index and work backwards
        Integer tt = null;
        String underOccupancy = "";
        while (li.hasPrevious()) {
            Integer previousIndex = li.previous();
            //System.out.println("previousIndex " + previousIndex);
            Map<SHBE_ClaimID, Integer> cid2tt = cid2tts.get(previousIndex);
            Set<SHBE_ClaimID> uoClaimIDSet = null;
            if (uoClaimIDSets != null) {
                uoClaimIDSet = uoClaimIDSets.get(previousIndex);
            }
            if (cid2tt != null) {
                tt = cid2tt.get(cid);
                if (uoClaimIDSet != null) {
                    if (uoClaimIDSet.contains(cid)) {
                        underOccupancy = sU;
                    }
                }
                if (tt != null) {
                    if (tt != -999) {
                        r[0] = Integer.toString(tt) + underOccupancy;
                        //result[1] = include.indexOf(previousIndex);
                        r[1] = previousIndex;
                        return r;
                    }
                }
            }
        }
        if (tt == null) {
            tt = -999;
        }
        r[0] = Integer.toString(tt) + underOccupancy;
        r[1] = null;
        return r;
    }

    /**
     *
     * @param ClaimIDToPostcodeIDLookups
     * @param records0
     * @param records1
     * @param cid2tt0
     * @param cid2tt1
     * @param cid2postcodeIDs
     * @param cid2tts
     * @param uoClaimIDSets
     * @param councilUOClaimIDSets
     * @param rslUOClaimIDSets
     * @param ttts
     * @param ym30
     * @param ym31
     * @param checkPreviousTenure
     * @param index
     * @param include
     * @param indexYM3s
     * @param doUnderOccupiedData
     * @param uoSetAll0
     * @param uoSetCouncil0
     * @param uoSetRSL0
     * @param uoSetAll1
     * @param uoSetCouncil1
     * @param uoSetRSL1
     * @param cids
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure0, Tenure1, Count
     * }
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixAndRecordTTT(
            Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs,
            Map<SHBE_ClaimID, SHBE_Record> records0,
            Map<SHBE_ClaimID, SHBE_Record> records1,
            Map<SHBE_ClaimID, Integer> cid2tt0,
            Map<SHBE_ClaimID, Integer> cid2tt1,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts,
            HashMap<Integer, Set<SHBE_ClaimID>> uoClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> councilUOClaimIDSets,
            HashMap<Integer, Set<SHBE_ClaimID>> rslUOClaimIDSets,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> ttts,
            UKP_YM3 ym30, UKP_YM3 ym31, boolean checkPreviousTenure, int index,
            ArrayList<Integer> include, Map<Integer, UKP_YM3> indexYM3s,
            boolean doUnderOccupiedData, DW_UO_Set uoSetAll0,
            DW_UO_Set uoSetCouncil0, DW_UO_Set uoSetRSL0, DW_UO_Set uoSetAll1,
            DW_UO_Set uoSetCouncil1, DW_UO_Set uoSetRSL1,
            Set<SHBE_ClaimID> cids
    ) {
        TreeMap<String, TreeMap<String, Integer>> r = new TreeMap<>();
        if (cids == null) {
            cids = new TreeSet<>();
            if (doUnderOccupiedData) {
                cids.addAll(uoSetAll0.getClaimIDs());
                cids.addAll(uoSetAll1.getClaimIDs());
            }
        }
        SHBE_ClaimID cid;
        SHBE_Record record0;
        SHBE_Record record1;
        SHBE_D_Record dRecord0;
        SHBE_D_Record dRecord1;
        DW_UO_Record uoRecord0 = null;
        DW_UO_Record uoRecord1 = null;
        Iterator<SHBE_ClaimID> ite = cids.iterator();
        while (ite.hasNext()) {
            cid = ite.next();
            record0 = records0.get(cid);
            record1 = records1.get(cid);
            String sTT0;
            if (record0 != null) {
                dRecord0 = record0.getDRecord();
                sTT0 = Integer.toString(dRecord0.getTenancyType());
            } else {
                dRecord0 = null;
                sTT0 = DW_Strings.sMinus999;
            }
            String sTT1;
            if (record1 != null) {
                dRecord1 = record1.getDRecord();
                sTT1 = Integer.toString(dRecord1.getTenancyType());
            } else {
                dRecord1 = null;
                sTT1 = DW_Strings.sMinus999;
            }
            if (doUnderOccupiedData) {
                uoRecord0 = uoSetAll0.getMap().get(cid);
                uoRecord1 = uoSetAll1.getMap().get(cid);
            }
            String pc0 = DW_Strings.sAAN_NAA;
            String pc1 = DW_Strings.sAAN_NAA;
            if (checkPreviousTenure) {
                if (sTT0.equalsIgnoreCase(DW_Strings.sMinus999)) {
                    if (doUnderOccupiedData) {
                        if (uoRecord0 != null) {
                            sTT0 = addUODetails(cid, uoRecord0, uoSetCouncil0,
                                    uoSetRSL0, sTT0);
                            if (dRecord0 != null) {
                                pc0 = dRecord0.getClaimantsPostcode();
                            } else {
                                pc0 = DW_Strings.sAAN_NAA;
                            }
                        } else {
                            Object[] previousTTAndP = getPreviousTTAndPU(
                                    cid2postcodeIDs, indexYM3s, cid, cid2tts,
                                    uoClaimIDSets, councilUOClaimIDSets,
                                    rslUOClaimIDSets, index, include);
                            sTT0 = (String) previousTTAndP[0];
                            pc0 = (String) previousTTAndP[2];
                            ym30 = (UKP_YM3) previousTTAndP[3];
                        }
                    } else {
                        Object[] previousTTAndP = getPreviousTTAndP(
                                cid2postcodeIDs, indexYM3s, cid, cid2tts, index,
                                include);
                        sTT0 = (String) previousTTAndP[0];
                        pc0 = (String) previousTTAndP[2];
                        ym30 = (UKP_YM3) previousTTAndP[3];
                    }
                } else {
                    if (doUnderOccupiedData) {
                        //underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
                        if (uoRecord0 != null) {
                            sTT0 += sU;
                        }
                        if (dRecord0 != null) {
//                            pc0 = DRecord0.getClaimantsPostcode();
                            pc0 = record0.getClaimPostcodeF();
                            UKP_RecordID postcodeID = p2pid.get(pc0);
                            cid2postcodeIDs.get(index).put(cid, postcodeID);
                        } else {
                            pc0 = DW_Strings.sAAN_NAA;
                        }
                    } else {
                        if (dRecord0 != null) {
//                            pc0 = DRecord0.getClaimantsPostcode();
                            pc0 = record0.getClaimPostcodeF();
                            UKP_RecordID postcodeID = p2pid.get(pc0);
                            cid2postcodeIDs.get(index).put(cid, postcodeID);
                        } else {
                            pc0 = DW_Strings.sAAN_NAA;
                        }
                    }
                }
                if (sTT1.equalsIgnoreCase(DW_Strings.sMinus999)) {
                    if (uoRecord1 != null) {
                        sTT1 = addUODetails(cid, uoRecord1, uoSetCouncil1,
                                uoSetRSL1, sTT1);
                        if (dRecord1 != null) {
//                            pc1 = DRecord0.getClaimantsPostcode();
                            pc1 = record0.getClaimPostcodeF();
                            UKP_RecordID postcodeID = p2pid.get(pc1);
                            cid2postcodeIDs.get(index).put(cid, postcodeID);
                        } else {
                            pc1 = DW_Strings.sAAN_NAA;
                        }
                    } else {
                        if (doUnderOccupiedData) {
                            Object[] previousTTAndP = getPreviousTTAndPU(
                                    cid2postcodeIDs,
                                    indexYM3s,
                                    cid,
                                    cid2tts,
                                    uoClaimIDSets,
                                    councilUOClaimIDSets,
                                    rslUOClaimIDSets,
                                    index,
                                    include);
                            sTT1 = (String) previousTTAndP[0];
                            pc1 = (String) previousTTAndP[2];
                            ym31 = (UKP_YM3) previousTTAndP[3];
                        } else {
                            Object[] previousTTAndP;
                            previousTTAndP = getPreviousTTAndP(
                                    cid2postcodeIDs,
                                    indexYM3s,
                                    cid,
                                    cid2tts,
                                    index,
                                    include);
                            sTT1 = (String) previousTTAndP[0];
                            pc1 = (String) previousTTAndP[2];
                            ym31 = (UKP_YM3) previousTTAndP[3];
                        }
                    }
                } else {
                    //underOccupied1 = underOccupiedSet1.getMap().get(CTBRef);
                    if (uoRecord1 != null) {
                        sTT1 += sU;
                    }
                    if (dRecord1 != null) {
//                        pc1 = DRecord0.getClaimantsPostcode();
                        pc1 = record1.getClaimPostcodeF();
                        UKP_RecordID PostcodeID = p2pid.get(pc1);
                        cid2postcodeIDs.get(index).put(cid, PostcodeID);
                    } else {
                        pc1 = DW_Strings.sAAN_NAA;
                    }
                }
            }
            if (doUnderOccupiedData) {
                if (checkPreviousTenure) {
                    if (sTT1.equalsIgnoreCase(DW_Strings.sMinus999)) {
                        Object[] previousTenure;
                        previousTenure = getPreviousTT(
                                cid,
                                cid2tts,
                                uoClaimIDSets,
                                councilUOClaimIDSets,
                                rslUOClaimIDSets,
                                index,
                                include);
                        sTT1 = (String) previousTenure[0];
                    }
                } else {
                    if (uoRecord0 != null) {
                        sTT0 += sU;
                        if (sTT0.startsWith(DW_Strings.sMinus999)) {
                            if (uoSetCouncil0.getMap().containsKey(cid)) {
                                sTT0 += "1";
                            }
                            if (uoSetRSL0.getMap().containsKey(cid)) {
                                sTT0 += "4";
                            }
                        }
                    }
                    if (uoRecord1 != null) {
                        sTT1 += sU;
                        if (sTT1.startsWith(DW_Strings.sMinus999)) {
                            if (uoSetCouncil1.getMap().containsKey(cid)) {
                                sTT1 += "1";
                            }
                            if (uoSetRSL1.getMap().containsKey(cid)) {
                                sTT1 += "4";
                            }
                        }
                    }
                }
            } else {
                if (checkPreviousTenure) {
                    if (sTT1.equalsIgnoreCase(DW_Strings.sMinus999)) {
                        Object[] previousTenure;
                        previousTenure = getPreviousTT(
                                cid,
                                cid2tts,
                                uoClaimIDSets,
                                councilUOClaimIDSets,
                                rslUOClaimIDSets,
                                index,
                                include);
                        sTT1 = (String) previousTenure[0];
                    }
                }
            }
            if (!sTT0.equalsIgnoreCase(sTT1)) {
                String[] TTTDetails;
                TTTDetails = getTTTDetails(
                        sTT0,
                        record0,
                        sTT1,
                        record1);
                recordTTTs(
                        cid,
                        ttts,
                        ym31,
                        TTTDetails[0]);
            } else if (pc0 != null && pc1 != null) {
                if (!pc0.equalsIgnoreCase(pc1)) {
                    if (record0 != null && record1 != null) {
//                        if (Record0.isClaimPostcodeFMappable()
//                                && Record1.isClaimPostcodeFMappable()) {
                        String TTT = sTT0 + " - " + sTT1;
                        TTT += DW_Strings.sPostcodeChanged;
                        recordTTTs(
                                cid,
                                ttts,
                                ym31,
                                TTT);
//                        }
                    }
                }
            }
            if (r.containsKey(sTT1)) {
                TreeMap<String, Integer> TTCount;
                TTCount = r.get(sTT1);
                Generic_Collections.addToMapInteger(
                        TTCount,
                        sTT0,
                        1);
            } else {
                TreeMap<String, Integer> TTCount;
                TTCount = new TreeMap<>();
                TTCount.put(sTT0, 1);
                r.put(sTT1, TTCount);
            }

            //} // For debugging
        }

        // Go through current
        ite = cid2tt1.keySet().iterator();
        while (ite.hasNext()) {
            boolean doMainLoop = true;
            cid = ite.next();

            //if (ClaimID.getID() == 32299) { // For debugging
            if (cids.contains(cid)) {
                if (doUnderOccupiedData) {
                    uoRecord0 = null;
                    uoRecord1 = null;
                    if (uoSetAll0 != null) {
                        uoRecord0 = uoSetAll0.getMap().get(cid);
                    }
                    if (uoSetAll1 != null) {
                        uoRecord1 = uoSetAll1.getMap().get(cid);
                    }
                }
                doMainLoop = (uoRecord0 != null || uoRecord1 != null) || !doUnderOccupiedData;
                if (doMainLoop) {
                    Integer TT0;
                    TT0 = cid2tt0.get(cid);
                    String sTT0;
                    if (TT0 == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    cid,
                                    cid2tts,
                                    uoClaimIDSets,
                                    index,
                                    include);
                            sTT0 = (String) previousTenure[0];
                        } else {
                            sTT0 = DW_Strings.sMinus999;
                        }
                    } else if (TT0 == -999) {
                        if (checkPreviousTenure) {
                            Object[] previousTenure;
                            previousTenure = getPreviousTT(
                                    cid,
                                    cid2tts,
                                    uoClaimIDSets,
                                    index,
                                    include);
                            sTT0 = (String) previousTenure[0];
                        } else {
                            sTT0 = DW_Strings.sMinus999;
                        }
                    } else {
                        sTT0 = Integer.toString(TT0);
                    }
                    Integer TT1Integer;
                    TT1Integer = cid2tt1.get(cid);
                    String TT1 = Integer.toString(TT1Integer);
                    if (doUnderOccupiedData) {
                        if (!checkPreviousTenure) {
                            if (uoRecord0 != null) {
                                sTT0 += sU;
                            }
                        }
                        if (uoRecord1 != null) {
                            TT1 += sU;
                        }
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
//                            env.ge.log(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            env.ge.log(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = underOccupiedInApril2013.contains(CTBRef);
//                            env.ge.log(test);
                            if (!ttts.containsKey(cid)) {
                                //int debug = 1;
                                // This only happens in cases where there is a -999U initial case!
                                if (doUnderOccupiedData) {
                                    if (cids.contains(cid)) {
                                        sTT0 += sU;
                                        TTTDetails[0] = sTT0 + " - " + TT1;
                                        if (sTT0.equalsIgnoreCase(TT1)) {
                                            doRecord = false;
                                            //}
                                        } else {
                                            doRecord = true; // This is for debugging
                                        }
                                    } else {
                                        int debug = 1;
                                    }
                                }
                            } else if (sTT0.equalsIgnoreCase(DW_Strings.sMinus999)) {
                                if (cids.contains(cid)) {
                                    //env.logO(CTBRef);
                                } else {
                                    int debug = 1;
                                    env.ge.log(cid.toString(), true);
                                }
                            }
                        }
                        if (doRecord) {
                            recordTTTs(
                                    cid,
                                    ttts,
                                    ym31,
                                    TTTDetails[0]);
                        }
                    }
                    if (r.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = r.get(TT1);
                        Generic_Collections.addToMapInteger(
                                TTCount,
                                sTT0,
                                1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
                        TTCount.put(sTT0, 1);
                        r.put(TT1, TTCount);
                    }
                }
            }

            //} // for debugging
        }
        // Go through previous for those records not in current
        ite = cid2tt1.keySet().iterator();
        while (ite.hasNext()) {
            cid = ite.next();
            //if (ClaimID.getID() == 32299) { // For debugging
            if (!cids.contains(cid)) {
                boolean doMainLoop = true;
                uoRecord0 = null;
                uoRecord1 = null;
                if (doUnderOccupiedData) {
                    if (uoSetAll0 != null) {
                        if (cids != null) {
                            if (cids.contains(cid)) {
                                uoRecord0 = uoSetAll0.getMap().get(cid);
                            }
                        } else {
                            uoRecord0 = uoSetAll0.getMap().get(cid);
                        }
                    }
                    if (uoSetAll1 != null) {
                        if (cids != null) {
                            if (cids.contains(cid)) {
                                uoRecord1 = uoSetAll1.getMap().get(cid);
                            }
                        } else {
                            uoRecord1 = uoSetAll1.getMap().get(cid);
                        }
                    }
                    doMainLoop = uoRecord0 != null || uoRecord1 != null;
                }
                if (doMainLoop) {
                    Integer TT0Integer = cid2tt1.get(cid);
                    String TT0 = Integer.toString(TT0Integer);
                    Integer TT1Integer = -999;
                    String TT1;
                    TT1 = shbeTTHandler.sMinus999;
                    if (uoRecord0 != null) {
                        TT0 += sU;
                    }
                    if (uoRecord1 != null) {
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
                                doUnderOccupiedData,
                                uoRecord0,
                                uoRecord1,
                                TT0Integer,
                                TT1Integer);
                        TT0 = TTTDetails[1];
                        TT1 = TTTDetails[2];

//                        // For debugging
//                        if (!TT0.equalsIgnoreCase(TTTDetails[1])) {
//                            int debug = 1;
//                        }
//
//                        if (!TT1.equalsIgnoreCase(TTTDetails[2])) {
//                            int debug = 1;
//                        }
                        boolean doRecord = true;
                        if (!TT0.endsWith(sU)) {
//                            env.ge.log(CTBRef);
//                            boolean test = tCTBRefs.contains(CTBRef);
//                            env.ge.log(test);
//                            CTBRef = tIDByCTBRef1.get(tID1);
//                            test = tCTBRefs.contains(CTBRef);
//                            env.ge.log(test);
                            if (!ttts.containsKey(cid)) {
                                //int debug = 1;
                                // This only happens in cases where there is a -999U initial case!
                                if (cids.contains(cid)) {
                                    TT0 += sU;
                                    TTTDetails[0] = TT0 + " - " + TT1;
                                    if (TT0.equalsIgnoreCase(TT1)) {
                                        doRecord = false;
                                    }
                                }
                                // For debugging
//                                } else {
//                                    int debug = 1;
//                                }

                            }
                        }
                        if (doRecord) {
                            recordTTTs(
                                    cid,
                                    ttts,
                                    ym31,
                                    TTTDetails[0]);
                        }
                    }
                    if (r.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = r.get(TT1);
                        Generic_Collections.addToMapInteger(
                                TTCount,
                                TT0,
                                1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
                        TTCount.put(TT0, 1);
                        r.put(TT1, TTCount);
                    }
                }
            }

            //} // For debugging
        }
        if (r.isEmpty()) {
            return null;
        }
        return r;
    }

    /**
     *
     * @param TTGLookup
     * @param DoUnderOccupiedData
     * @param TTTs
     * @return
     */
    public TreeMap<String, TreeMap<String, Integer>> aggregate(
            HashMap<String, String> TTGLookup,
            boolean DoUnderOccupiedData,
            TreeMap<String, TreeMap<String, Integer>> TTTs) {
        if (TTTs == null) {
            return null;
        }
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        Iterator<String> ite0;
        Iterator<String> ite1;
        TreeMap<String, Integer> counts;
        TreeMap<String, Integer> countsG;
        int count;
        String tt0;
        String ttg0;
        String tt1;
        String ttg1;
        ite0 = TTTs.keySet().iterator();
        while (ite0.hasNext()) {
            tt0 = ite0.next();
            ttg0 = TTGLookup.get(tt0);
            if (ttg0 == null) {
                ttg0 = tt0; // deal with -999
            }
            if (result.containsKey(ttg0)) {
                countsG = result.get(ttg0);
            } else {
                countsG = new TreeMap<>();
                result.put(ttg0, countsG);
            }
            counts = TTTs.get(tt0);
            ite1 = counts.keySet().iterator();
            while (ite1.hasNext()) {
                tt1 = ite1.next();
                ttg1 = TTGLookup.get(tt1);
                if (ttg1 == null) {
                    ttg1 = tt1; // deal with -999
                }
                count = counts.get(tt1);
                if (countsG.containsKey(ttg1)) {
                    int i = countsG.get(ttg1);
                    countsG.put(ttg1, count + i);
                } else {
                    countsG.put(ttg1, count);
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    protected String addUODetails(
            SHBE_ClaimID ClaimID,
            DW_UO_Record rec,
            DW_UO_Set CouncilUO,
            DW_UO_Set RSLUO,
            String sTT1) {
        if (rec != null) {
            sTT1 += sU;
            if (sTT1.startsWith(DW_Strings.sMinus999)) {
                if (CouncilUO.getMap().containsKey(ClaimID)) {
                    sTT1 += "1";
                }
                if (RSLUO.getMap().containsKey(ClaimID)) {
                    sTT1 += "4";
                }
            }
        }
        return sTT1;
    }

    /**
     * Get TenancyType Transition Matrix and Write Postcode Transition details.
     *
     * @param dirOut
     * @param cid2tt0
     * @param cid2tt1
     * @param cid2tts
     * @param uo0
     * @param uo1
     * @param uos
     * @param cid2postcodeID0
     * @param uoCouncil
     * @param cid2postcodeID1
     * @param postcodeChange
     * @param ttts Passed in to be modified.
     * @param uoRSL
     * @param ym30
     * @param ym31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param cid2tts
     * @param checkPreviousPostcode
     * @param cid2postcodeIDs
     * @param uoSet0
     * @param uoSet1
     * @param uoInApril2013
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixAndWritePTDetailsU(
            Path dirOut,
            Map<SHBE_ClaimID, Integer> cid2tt0,
            Map<SHBE_ClaimID, Integer> cid2tt1,
            Map<Integer, Map<SHBE_ClaimID, Integer>> cid2tts,
            Set<SHBE_ClaimID> uo0,
            Set<SHBE_ClaimID> uo1,
            HashMap<Integer, Set<SHBE_ClaimID>> uos,
            HashMap<Integer, Set<SHBE_ClaimID>> uoCouncil,
            HashMap<Integer, Set<SHBE_ClaimID>> uoRSL,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> ttts,
            UKP_YM3 ym30,
            UKP_YM3 ym31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID0,
            Map<SHBE_ClaimID, UKP_RecordID> cid2postcodeID1,
            Map<Integer, Map<SHBE_ClaimID, UKP_RecordID>> cid2postcodeIDs,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set uoSet0,
            DW_UO_Set uoSet1,
            Set<SHBE_ClaimID> uoInApril2013) throws IOException, Exception {
        Map<Integer, UKP_YM3> indexYM3s = shbeHandler.getIndexYM3s();
        TreeMap<String, TreeMap<String, Integer>> r = new TreeMap<>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<>();
        }
        Iterator<SHBE_ClaimID> ite;
        // Go through current Tenancy Type
        ite = cid2tt1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            boolean doLoop = true;
            if (uoInApril2013 == null) {
                doLoop = true;
            } else {
                if (uoInApril2013.contains(ClaimID)) {
                    doLoop = true;
                } else {
                    doLoop = false;
                }
            }
// UnderOccupancy
            DW_UO_Record UO0 = null;
            DW_UO_Record UO1 = null;
            if (uoSet0 != null) {
                if (uoInApril2013 == null) {
                    UO0 = uoSet0.getMap().get(ClaimID);
                } else if (uoInApril2013.contains(ClaimID)) {
                    UO0 = uoSet0.getMap().get(ClaimID);
                }
            }
            if (uoSet1 != null) {
                if (uoInApril2013 == null) {
                    UO1 = uoSet1.getMap().get(ClaimID);
                } else if (uoInApril2013.contains(ClaimID)) {
                    UO1 = uoSet1.getMap().get(ClaimID);
                }
            }
            doLoop = UO0 != null || UO1 != null;
            if (doLoop) {
                Integer TT0Integer = shbeTTHandler.iMinus999;
                String TT0 = shbeTTHandler.sMinus999;
                UKP_RecordID postcode0 = null;
                if (cid2tt1 != null) {
                    TT0Integer = cid2tt1.get(ClaimID);
                    postcode0 = cid2postcodeID0.get(ClaimID);
                    if (TT0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTTAndP = getPreviousTTAndPU(
                                    cid2postcodeIDs,
                                    indexYM3s,
                                    ClaimID,
                                    cid2tts,
                                    uos,
                                    uoCouncil,
                                    uoRSL,
                                    i,
                                    include);
                            TT0 = (String) previousTTAndP[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTTAndP[1];
                            if (indexOfLastKnownTenureOrNot != null) {
//                                  env.ge.log("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                if (checkPreviousPostcode) {
                                    postcode0 = cid2postcodeIDs.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                }
                            }
                        } else {
                            TT0 = shbeTTHandler.sMinus999;
                            if (UO0 != null) {
                                TT0 += sU;
                            }
                        }
                    }
                } else {
                    TT0 = shbeTTHandler.sMinus999;
                    if (UO0 != null) {
                        TT0 += sU;
                    }
                }
                Integer TT1Integer = cid2tt1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                UKP_RecordID postcode1;
                postcode1 = cid2postcodeID1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    if (postcode0 == null) {
                        if (postcode1 == null) {
                            doCount = false;
                        } else {
                            doCount = true;
                        }
                    } else {
                        if (postcode1 == null) {
                            doCount = true;
                        } else {
                            doCount = !postcode0.equals(postcode1);
                        }
                    }
                } else {
                    if (postcode0 == null) {
                        if (postcode1 == null) {
                            doCount = true;
                        } else {
                            doCount = false;
                        }
                    } else {
                        if (postcode1 == null) {
                            doCount = false;
                        } else {
                            doCount = postcode0.equals(postcode1);
                        }
                    }
                }
                if (doCount) {
                    if (TT0Integer.compareTo(TT1Integer) != 0) {
                        String[] TTTDetails = getTTTDetails(true, UO0, UO1, TT0Integer, TT1Integer);
//                        TTTDetails = getTTTDetails(TT0, TT1);
                        TT0 = TTTDetails[1];
                        TT1 = TTTDetails[2];
                        recordTTTs(ClaimID, ttts, ym31, TTTDetails[0]);
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(ClaimID, ym30, ym31,
                                    TTTDetails[0], postcode0, postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                    }
                    if (r.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = r.get(TT1);
                        Generic_Collections.addToMapInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
                        TTCount.put(TT0, 1);
                        r.put(TT1, TTCount);
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToMapInteger(
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
        ite = cid2tt1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            boolean doLoop = true;
            if (uoInApril2013 == null) {
                doLoop = true;
            } else {
                if (uoInApril2013.contains(ClaimID)) {
                    doLoop = true;
                } else {
                    doLoop = false;
                }
            }
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (uoSet0 != null) {
                underOccupied0 = uoSet0.getMap().get(ClaimID);
            }
            doLoop = underOccupied0 != null;
            if (uoSet1 != null) {
                underOccupied1 = uoSet1.getMap().get(ClaimID);
            }
            if (doLoop) {
                Integer TT0Integer = cid2tt1.get(ClaimID);
                String TT0 = Integer.toString(TT0Integer);
                Integer TT1Integer = -999;
                String TT1;
                TT1 = shbeTTHandler.sMinus999;
                UKP_RecordID postcode0;
                postcode0 = cid2postcodeID0.get(ClaimID);
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
                        ttts,
                        ym31,
                        TTTDetails[0]);
                //}
                if (r.containsKey(TT1)) {
                    TreeMap<String, Integer> TTCount;
                    TTCount = r.get(TT1);
                    Generic_Collections.addToMapInteger(
                            TTCount, TT0, 1);
                } else {
                    TreeMap<String, Integer> TTCount;
                    TTCount = new TreeMap<>();
                    TTCount.put(TT0, 1);
                    r.put(TT1, TTCount);
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(dirOut,
                        postcodeChanges,
                        ym30,
                        ym31,
                        checkPreviousPostcode);
            }
        }
        if (r.isEmpty()) {
            return null;
        }
        return r;
    }

    /**
     * Get TenancyType Transition Matrix and Write Postcode Transition details.
     *
     * @param dirOut
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToPostcodeIDLookup0
     * @param ClaimIDToPostcodeIDLookup1
     * @param postcodeChange
     * @param TTTs Passed in to be modified.
     * @param YM30
     * @param YM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param ClaimIDToTTLookups
     * @param checkPreviousPostcode
     * @param ClaimIDToPostcodeIDLookups
     * @param UOInApril2013
     * @return {@code
     * TreeMap<Integer, TreeMap<Integer, Integer>>
     * Tenure1, Tenure2, Count}
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixAndWritePTDetails(
            Path dirOut,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTTs,
            //HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
            UKP_YM3 YM30,
            UKP_YM3 YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup0,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, UKP_RecordID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            Set<SHBE_ClaimID> UOInApril2013) throws IOException, Exception {
        Map<Integer, UKP_YM3> indexYM3s;
        indexYM3s = shbeHandler.getIndexYM3s();
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<>();
        }
        boolean doLoop;
        Iterator<SHBE_ClaimID> ite;
        // Go through current Tenancy Type
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            if (UOInApril2013 == null) {
                doLoop = true;
            } else {
                if (UOInApril2013.contains(ClaimID)) {
                    doLoop = true;
                } else {
                    doLoop = false;
                }
            }
            if (doLoop) {
                // Initialise TTOInteger, TTO and postcode0
                Integer TT0Integer = shbeTTHandler.iMinus999;
                String TT0 = shbeTTHandler.sMinus999;
                UKP_RecordID postcode0 = null;
                /**
                 * If we have a non null previous Tenancy type lookup then
                 * reinitialise TT0Integer and postcode0.
                 */
                if (ClaimIDToTTLookup0 != null) {
                    TT0Integer = ClaimIDToTTLookup0.get(ClaimID);
                    postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
                    /**
                     * If TT0Integer is null then if we are checking previous
                     * tenancy types then try to look this up and if then also
                     * checking previous postcode then try to look this up too.
                     */
                    if (TT0Integer == null) {
                        if (checkPreviousTenure) {
                            Object[] previousTTAndP;
                            previousTTAndP = getPreviousTTAndP(
                                    ClaimIDToPostcodeIDLookups,
                                    indexYM3s,
                                    ClaimID,
                                    ClaimIDToTTLookups,
                                    i,
                                    include);
                            TT0 = (String) previousTTAndP[0];
                            Integer indexOfLastKnownTenureOrNot;
                            indexOfLastKnownTenureOrNot = (Integer) previousTTAndP[1];
                            if (indexOfLastKnownTenureOrNot != null) {
//                                  env.ge.log("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                if (checkPreviousPostcode) {
                                    postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                }
                            }
                            if (TT0 == null) {
                                TT0 = shbeTTHandler.sMinus999;
                            }
                        } else {
                            TT0 = shbeTTHandler.sMinus999;
                        }
                    } else {
                        TT0 = Integer.toString(TT0Integer);
                    }
                } else {
                    TT0 = shbeTTHandler.sMinus999;
                }
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                UKP_RecordID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    if (postcode0 == null) {
                        if (postcode1 != null) {
                            doCount = true;
                        } else {
                            doCount = false;
                        }
                    } else {
                        if (postcode1 != null) {
                            doCount = !postcode0.equals(postcode1);
                        } else {
                            doCount = true;
                        }
                    }
                } else {
                    if (postcode0 == null) {
                        if (postcode1 == null) {
                            doCount = true;
                        } else {
                            doCount = false;
                        }
                    } else {
                        if (postcode1 == null) {
                            doCount = true;
                        } else {
                            doCount = postcode0.equals(postcode1);
                        }
                    }
                }
                if (doCount) {
                    boolean doloop;
                    if (TT1Integer == null) {
                        if (TT0Integer == null) {
                            doloop = false;
                        } else {
                            doloop = true;
                        }
                    } else {
                        if (TT0Integer == null) {
                            doloop = true;
                        } else {
                            doloop = TT0Integer.compareTo(TT1Integer) != 0;
                        }
                    }
                    if (doloop) {
                        String[] TTTDetails;
//                    TTTDetails = getTTTDetails(
//                            false,
//                            null,
//                            null,
//                            TT0Integer,
//                            TT1Integer);
                        TTTDetails = getTTTDetails(
                                TT0,
                                TT1);
                        TT0 = TTTDetails[1];
                        TT1 = TTTDetails[2];
                        if (!TT0.equalsIgnoreCase(TT1)) {
                            recordTTTs(
                                    ClaimID,
                                    TTTs,
                                    YM31,
                                    TTTDetails[0]);
                        }
                        if (postcodeChange) {
                            String[] postcodeChangeResult;
                            postcodeChangeResult = getPTName(ClaimID, YM30,
                                    YM31, TTTDetails[0], postcode0, postcode1);
                            postcodeChanges.add(postcodeChangeResult);
                        }
                        if (result.containsKey(TT1)) {
                            TreeMap<String, Integer> TTCount;
                            TTCount = result.get(TT1);
                            Generic_Collections.addToMapInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToMapInteger(
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
        SHBE_ClaimID ClaimID;
        while (ite.hasNext()) {
            ClaimID = ite.next();
            if (UOInApril2013 == null) {
                doLoop = true;
            } else {
                if (UOInApril2013.contains(ClaimID)) {
                    doLoop = true;
                } else {
                    doLoop = false;
                }
            }
            if (doLoop) {
                if (!ClaimIDToTTLookup0.containsKey(ClaimID)) {
                    Integer TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
                    String TT0 = Integer.toString(TT0Integer);
                    //Integer TT1Integer = -999;
                    String TT1;
                    TT1 = shbeTTHandler.sMinus999;
                    //SHBE_ClaimID postcode0;
                    //postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
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
                    // For the time being exclude TT transitions as there may not have been one, but here is where we would record the TT to -999.
//                recordTTTs(
//                        ClaimID,
//                        TTTs,
//                        YM31,
//                        TTTDetails[0]);
                    //}
                    if (result.containsKey(TT1)) {
                        TreeMap<String, Integer> TTCount;
                        TTCount = result.get(TT1);
                        Generic_Collections.addToMapInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        if (postcodeChange) {
            if (!postcodeChanges.isEmpty()) {
                writePostcodeChanges(
                        dirOut,
                        postcodeChanges,
                        YM30,
                        YM31,
                        checkPreviousPostcode);
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

    private String[] getTTTDetails(String sTT0, SHBE_Record Record0,
            String sTT1, SHBE_Record Record1) {
        String[] r = new String[3];
        UKP_RecordID p0 = null;
        boolean isMappablePostcodeFormat0 = false;
        if (Record0 != null) {
            p0 = Record0.getPostcodeID();
            isMappablePostcodeFormat0 = Record0.isClaimPostcodeFMappable();
        }
        UKP_RecordID p1 = null;
        boolean isMappablePostcodeFormat1 = false;
        if (Record1 != null) {
            p1 = Record1.getPostcodeID();
            isMappablePostcodeFormat1 = Record1.isClaimPostcodeFMappable();
        }
        r[0] = sTT0 + " - " + sTT1;
        if (p0 != null && p1 != null) {
            if (!p0.equals(p1)) {
                if (isMappablePostcodeFormat0 && isMappablePostcodeFormat1) {
                    r[0] += "P";
                }
            }
        }
        r[1] = sTT0;
        r[2] = sTT1;
        return r;
    }

    public String[] getPTName(SHBE_ClaimID ClaimID, UKP_YM3 YM30, UKP_YM3 YM31,
            String TTChange, UKP_RecordID PostcodeID0, UKP_RecordID PostcodeID1)
            throws IOException, Exception {
        String[] r;
        r = new String[6];
        r[0] = ClaimID.toString();
        r[1] = YM30.toString();
        r[2] = YM31.toString();
        r[3] = TTChange;
        r[4] = env.getSHBE_Handler().getPid2p().get(PostcodeID0);
        r[5] = env.getSHBE_Handler().getPid2p().get(PostcodeID1);
        return r;
    }

    /**
     * Gets Postcode Transition Counts where there is no TenancyType Transition.
     *
     * @param dirOut
     * @param DoUnderOccupancy
     * @param ClaimIDToTTLookup0
     * @param ClaimIDToTTLookup1
     * @param ClaimIDToTTLookups
     * @param IDs0
     * @param IDs1
     * @param TTTs
     * @param YM30
     * @param IDs
     * @param YM31
     * @param checkPreviousTenure
     * @param i
     * @param include
     * @param ClaimIDToPostcodeIDLookup0
     * @param ClaimIDToPostcodeIDLookup1
     * @param ClaimIDToPostcodeIDLookups
     * @param postcodeChange
     * @param checkPreviousPostcode
     * @param UOSet0
     * @param UOSet1
     * @param ClaimIDs
     * @return A count of changes in matrix form (only entries for the same
     * Tenancy Types and for -999 are in the matrix).
     */
    public TreeMap<String, TreeMap<String, Integer>> getPTCountsNoTTT(
            Path dirOut,
            boolean DoUnderOccupancy,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
            Set<SHBE_ClaimID> IDs0,
            Set<SHBE_ClaimID> IDs1,
            HashMap<Integer, Set<SHBE_ClaimID>> IDs,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTTs,
            //HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
            UKP_YM3 YM30,
            UKP_YM3 YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup0,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, UKP_RecordID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set UOSet0,
            DW_UO_Set UOSet1,
            Set<SHBE_ClaimID> ClaimIDs
    ) throws IOException, Exception {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<>();
        }
        Iterator<SHBE_ClaimID> ite;
        // Go through current claimants
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record underOccupied0 = null;
            DW_UO_Record underOccupied1 = null;
            if (DoUnderOccupancy) {
                if (UOSet0 != null) {
                    if (ClaimIDs == null) {
                        underOccupied0 = UOSet0.getMap().get(ClaimID);
                    } else if (ClaimIDs.contains(ClaimID)) {
                        underOccupied0 = UOSet0.getMap().get(ClaimID);
                    }
                }
                if (UOSet1 != null) {
                    if (ClaimIDs == null) {
                        underOccupied1 = UOSet1.getMap().get(ClaimID);
                    } else if (ClaimIDs.contains(ClaimID)) {
                        underOccupied1 = UOSet1.getMap().get(ClaimID);
                    }
                }
            }
            doMainLoop = (underOccupied0 != null || underOccupied1 != null) || !DoUnderOccupancy;
            if (doMainLoop) {
                Integer TT0Integer = -999;
                String TT0;
                UKP_RecordID postcode0 = null;
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
                                        IDs,
                                        i,
                                        include);
                                TT0 = (String) previousTenure[0];
                                if (checkPreviousPostcode) {
                                    Integer indexOfLastKnownTenureOrNot;
                                    indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
                                    if (indexOfLastKnownTenureOrNot != null) {
//                                       env.ge.log("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                        if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
                                            postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                        }
                                    }
                                }
                            } else {
                                TT0 = shbeTTHandler.sMinus999;
                                TT0 += sU;
                            }
                        }
                    } else {
                        TT0 = shbeTTHandler.sMinus999;
                        TT0 += sU;
                    }
                } else {
                    TT0 = shbeTTHandler.sMinus999;
                    TT0 += sU;
                }
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = Integer.toString(TT1Integer);
                UKP_RecordID postcode1;
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
                    String[] ttc = getTTTName(
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
                            Generic_Collections.addToMapInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToMapInteger(
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
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
                DW_UO_Record underOccupied0 = null;
                //DW_UnderOccupiedReport_Record underOccupied1 = null;
                if (UOSet0 != null) {
                    underOccupied0 = UOSet0.getMap().get(ClaimID);
                }
                doMainLoop = underOccupied0 != null;
                if (doMainLoop) {
                    Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
                    String sTT0 = Integer.toString(TT0);
                    Integer TT1 = -999;
                    String sTT1;
                    sTT1 = shbeTTHandler.sMinus999;
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
                        Generic_Collections.addToMapInteger(
                                TTCount, sTT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
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
                        checkPreviousPostcode);
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

//    /**
//     * Gets Postcode Transition Counts where there is no TenancyType Transition.
//     *
//     * @param dirOut
//     * @param ClaimIDToTTLookup0
//     * @param ClaimIDToTTLookup1
//     * @param ClaimIDToTTLookups
//     * @param tUnderOccupancyIDs0
//     * @param tUnderOccupancyIDs1
//     * @param TTTs
//     * @param YM30
//     * @param tUnderOccupancies
//     * @param YM31
//     * @param checkPreviousTenure
//     * @param i
//     * @param include
//     * @param ClaimIDToPostcodeIDLookup0
//     * @param ClaimIDToPostcodeIDLookup1
//     * @param ClaimIDToPostcodeIDLookups
//     * @param postcodeChange
//     * @param checkPreviousPostcode
//     * @param underOccupiedSet0
//     * @param underOccupiedSet1
//     * @param doUnderOccupiedData
//     * @param tCTBRefs
//     * @return A count of changes in matrix form (only entries for the same
//     * Tenancy Types and for -999 are in the matrix).
//     */
//    public TreeMap<String, TreeMap<String, Integer>> getPTCountsNoTTT(
//            Path dirOut,
//            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
//            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
//            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
//            Set<SHBE_ClaimID> tUnderOccupancyIDs0,
//            Set<SHBE_ClaimID> tUnderOccupancyIDs1,
//            HashMap<Integer, Set<SHBE_ClaimID>> tUnderOccupancies,
//            HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
//            String YM30,
//            String YM31,
//            boolean checkPreviousTenure,
//            int i,
//            ArrayList<Integer> include,
//            HashMap<SHBE_ClaimID, SHBE_ClaimID> ClaimIDToPostcodeIDLookup0,
//            HashMap<SHBE_ClaimID, SHBE_ClaimID> ClaimIDToPostcodeIDLookup1,
//            HashMap<Integer, HashMap<SHBE_ClaimID, SHBE_ClaimID>> ClaimIDToPostcodeIDLookups,
//            boolean postcodeChange,
//            boolean checkPreviousPostcode,
//            DW_UO_Set underOccupiedSet0,
//            DW_UO_Set underOccupiedSet1,
//            boolean doUnderOccupiedData,
//            Set<SHBE_ClaimID> tCTBRefs
//    ) {
//        TreeMap<String, TreeMap<String, Integer>> result;
//        result = new TreeMap<String, TreeMap<String, Integer>>();
//        ArrayList<String[]> postcodeChanges = null;
//        if (postcodeChange) {
//            postcodeChanges = new ArrayList<String[]>();
//        }
//        Iterator<SHBE_ClaimID> ite;
//        // Go through current claimants
//        ite = ClaimIDToTTLookup1.keySet().iterator();
//        while (ite.hasNext()) {
//            SHBE_ClaimID ClaimID;
//            ClaimID = ite.next();
//            boolean doMainLoop = true;
//            // UnderOccupancy
//            DW_UO_Record underOccupied0 = null;
//            DW_UO_Record underOccupied1 = null;
//            if (doUnderOccupiedData) {
//                if (underOccupiedSet0 != null) {
//                    if (tCTBRefs == null) {
//                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
//                    } else if (tCTBRefs.contains(ClaimID)) {
//                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
//                    }
//                }
//                if (underOccupiedSet1 != null) {
//                    if (tCTBRefs == null) {
//                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
//                    } else if (tCTBRefs.contains(ClaimID)) {
//                        underOccupied1 = underOccupiedSet1.getMap().get(ClaimID);
//                    }
//                }
//                doMainLoop = underOccupied0 != null || underOccupied1 != null;
//            }
//            if (doMainLoop) {
//                Integer TT0Integer = -999;
//                String TT0;
//                SHBE_ClaimID postcode0 = null;
//                if (ClaimIDToTTLookup1 != null) {
//                    TT0Integer = ClaimIDToTTLookup1.get(ClaimID);
//                    if (TT0Integer != null) {
//                        TT0 = Integer.toString(TT0Integer);
//                        boolean isValidPostcodeFormPostcode0 = false;
//                        postcode0 = ClaimIDToPostcodeIDLookup0.get(ClaimID);
//                        if (TT0 == null) {
//                            if (checkPreviousTenure) {
//                                Object[] previousTenure;
//                                previousTenure = getPreviousTT(
//                                        ClaimID,
//                                        ClaimIDToTTLookups,
//                                        tUnderOccupancies,
//                                        i,
//                                        include);
//                                TT0 = (String) previousTenure[0];
//                                if (checkPreviousPostcode) {
//                                    Integer indexOfLastKnownTenureOrNot;
//                                    indexOfLastKnownTenureOrNot = (Integer) previousTenure[1];
//                                    if (indexOfLastKnownTenureOrNot != null) {
////                                       env.ge.log("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
//                                        if (!isValidPostcodeFormPostcode0 && checkPreviousPostcode) {
//                                            postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
//                                        }
//                                    }
//                                }
//                            } else {
//                                TT0 = shbeTTHandler.sMinus999;
//                                if (doUnderOccupiedData) {
//                                    TT0 += sU;
//                                }
//                            }
//                        }
//                    } else {
//                        TT0 = shbeTTHandler.sMinus999;
//                        if (doUnderOccupiedData) {
//                            TT0 += sU;
//                        }
//                    }
//                } else {
//                    TT0 = shbeTTHandler.sMinus999;
//                    if (doUnderOccupiedData) {
//                        TT0 += sU;
//                    }
//                }
//                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
//                String TT1 = Integer.toString(TT1Integer);
//                SHBE_ClaimID postcode1;
//                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
//                boolean doCount;
//                if (postcodeChange) {
//                    if (postcode0 == null) {
//                        doCount = postcode1 != null;
//                    } else {
//                        doCount = !postcode0.equals(postcode1);
//                    }
//                } else if (postcode0 == null) {
//                    doCount = postcode1 == null;
//                } else {
//                    doCount = postcode0.equals(postcode1);
//                }
//                if (doCount) {
//                    String TTChange;
//                    if (doUnderOccupiedData) {
//                        String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
//                                TT0Integer,
//                                underOccupied0 != null,
//                                TT1Integer,
//                                underOccupied1 != null);
//                        TTChange = ttc[0];
//                        TT0 = ttc[1];
//
////                            if (!TT0.endsWith(sU)) {
////                                int debug = 1;
////                            }
//                        TT1 = ttc[2];
//                        if (TT0.equalsIgnoreCase(TT1)) {
////                            if (!TT0.equalsIgnoreCase(TT1)) {
////                                recordTTTs(
////                                        tID,
////                                        TTTs,
////                                        YM31,
////                                        TTChange);
////                            }
//                            if (postcodeChange) {
//                                String[] postcodeChangeResult;
//                                postcodeChangeResult = getPTName(
//                                        ClaimID,
//                                        YM30,
//                                        YM31,
//                                        TTChange,
//                                        postcode0,
//                                        postcode1);
//                                postcodeChanges.add(postcodeChangeResult);
//                            }
//                            if (result.containsKey(TT1)) {
//                                TreeMap<String, Integer> TTCount;
//                                TTCount = result.get(TT1);
//                                Generic_Collections.addToMapInteger(
//                                        TTCount, TT0, 1);
//                            } else {
//                                TreeMap<String, Integer> TTCount;
//                                TTCount = new TreeMap<String, Integer>();
//                                TTCount.put(TT0, 1);
//                                result.put(TT1, TTCount);
//                            }
//                        }
//                    } else if (TT0Integer.compareTo(TT1Integer) == 0
//                            || TT0.equalsIgnoreCase(shbeTTHandler.sMinus999)) { // Major diff
//                        TTChange = getTTTName(
//                                TT0Integer,
//                                TT1Integer);
//                        if (postcodeChange) {
//                            String[] postcodeChangeResult;
//                            postcodeChangeResult = getPTName(
//                                    ClaimID,
//                                    YM30,
//                                    YM31,
//                                    TTChange,
//                                    postcode0,
//                                    postcode1);
//                            postcodeChanges.add(postcodeChangeResult);
//                        }
//                        if (result.containsKey(TT1)) {
//                            TreeMap<String, Integer> TTCount;
//                            TTCount = result.get(TT1);
//                            Generic_Collections.addToMapInteger(
//                                    TTCount, TT0, 1);
//                        } else {
//                            TreeMap<String, Integer> TTCount;
//                            TTCount = new TreeMap<String, Integer>();
//                            TTCount.put(TT0, 1);
//                            result.put(TT1, TTCount);
//                        }
//                    }
//                }
////                } else if (isValidPostcode1) {
////                    if (result.containsKey(TT1)) {
////                        TreeMap<String, Integer> TTCount;
////                        TTCount = result.get(TT1);
////                        Generic_Collections.addToMapInteger(
////                                TTCount, TT0, 1);
////                    } else {
////                        TreeMap<String, Integer> TTCount;
////                        TTCount = new TreeMap<String, Integer>();
////                        TTCount.put(TT0, 1);
////                        result.put(TT1, TTCount);
////                    }
////                }
//            }
//        }
//        // Go through all those previously and record for all those that are not
//        // in the current data. Also record for all those that were under 
//        // occupying, but are now not and have changed postcode.
//        ite = ClaimIDToTTLookup0.keySet().iterator();
//        while (ite.hasNext()) {
//            SHBE_ClaimID ClaimID;
//            ClaimID = ite.next();
//            boolean doMainLoop = true;
//            if (!ClaimIDToTTLookup1.containsKey(ClaimID)) {
//// This was double counting!
////                    if (!set.contains(tID)) {
////                        // UnderOccupancy
////                        DW_UO_Record underOccupied0 = null;
////                        DW_UO_Record underOccupied1 = null;
////                        if (doUnderOccupiedData) {
////                            if (underOccupiedSet0 != null) {
////                                if (tIDByCTBRef0 != null) {
////                                    if (CTBRef != null) {
////                                        underOccupied0 = underOccupiedSet0.getMap().get(CTBRef);
////                                    }
////                                }
////                            }
////                            doMainLoop = underOccupied0 != null;
////                        }
////                        if (doMainLoop) {
////                            String TT0 = ClaimIDToTTLookup1.get(
////                                    tID);
////                            String TT1;
////                            TT1 = ClaimIDToTTLookup1.get(tID);
////                            if (TT0.equalsIgnoreCase(TT1)) {
////                                String postcode0;
////                                postcode0 = ClaimIDToPostcodeIDLookup0.get(tID);
////                                boolean isValidPostcode0 = false;
////                                if (postcode0 != null) {
////                                    isValidPostcode0 = ONSPD_Handler.isValidPostcode(postcode0);
////                                }
////                                String postcode1;
////                                postcode1 = ClaimIDToPostcodeIDLookup1.get(tID);
////                                boolean isValidPostcode1 = false;
////                                if (postcode1 != null) {
////                                    isValidPostcode1 = ONSPD_Handler.isValidPostcode(postcode1);
////                                }
////                                if (isValidPostcode0 && isValidPostcode1) {
////                                    boolean doCount = false;
////                                    if (postcodeChange) {
////                                        doCount = !postcode0.equalsIgnoreCase(postcode1);
////                                    } else {
////                                        doCount = postcode0.equalsIgnoreCase(postcode1);
////                                    }
////                                    if (doCount) {
////                                        String TTChange;
////                                        if (doUnderOccupiedData) {
////                                            String[] ttc = getTTTName(
////                                                    TT0,
////                                                    underOccupied0 != null,
////                                                    TT1,
////                                                    underOccupied1 != null);
////                                            TTChange = ttc[0];
////                                            TT0 = ttc[1];
////                                            TT1 = ttc[2];
////                                            if (!TT0.equalsIgnoreCase(TT1)) {
////                                                recordTTTs(
////                                                        tID,
////                                                        TTTs,
////                                                        year,
////                                                        month,
////                                                        TTChange);
////                                            }
////                                        } else {
////                                            TTChange = getTTTName(
////                                                    TT0,
////                                                    TT1);
////                                        }
////                                        if (postcodeChange) {
////                                            String[] postcodeChangeResult;
////                                postcodeChangeResult = getPTName(
////                                        tID,
////                                        YM30, 
////                                        YM31, 
////                                        TTChange,
////                                        postcode0,
////                                        postcode1);
////                                LCCTTAndPT_DoPostcodeChanges.add(postcodeChangeResult);
////                                        }
////                                        if (result.containsKey(TT1)) {
////                                            TreeMap<String, Integer> TTCount;
////                                            TTCount = result.get(TT1);
////                                            Generic_Collections.addToMapInteger(
////                                                    TTCount, TT0, 1);
////                                        } else {
////                                            TreeMap<String, Integer> TTCount;
////                                            TTCount = new TreeMap<String, Integer>();
////                                            TTCount.put(TT0, 1);
////                                            result.put(TT1, TTCount);
////                                        }
////                                    }
////                                }
////                            }
////                        }
////                    } else {
//                // UnderOccupancy
//                DW_UO_Record underOccupied0 = null;
//                //DW_UnderOccupiedReport_Record underOccupied1 = null;
//                if (doUnderOccupiedData) {
//                    if (underOccupiedSet0 != null) {
//                        underOccupied0 = underOccupiedSet0.getMap().get(ClaimID);
//                    }
//                    doMainLoop = underOccupied0 != null;
//                }
//                if (doMainLoop) {
//                    Integer TT0 = ClaimIDToTTLookup0.get(ClaimID);
//                    String sTT0 = Integer.toString(TT0);
//                    Integer TT1 = -999;
//                    String sTT1;
//                    sTT1 = shbeTTHandler.sMinus999;
//                    String TTT;
//                    if (doUnderOccupiedData) {
//                        String[] ttc = DW_ProcessorLCCTTAndPT.this.getTTTName(
//                                TT0,
//                                underOccupied0 != null,
//                                TT1,
//                                false);
//                        TTT = ttc[0];
//                        sTT0 = ttc[1];
//                        sTT1 = ttc[2];
//                        if (sTT0.equalsIgnoreCase(sTT1)) {
//                            recordTTTs(
//                                    ClaimID,
//                                    TTTs,
//                                    YM31,
//                                    TTT);
//                        }
//                    }
//                    if (result.containsKey(sTT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(sTT1);
//                        Generic_Collections.addToMapInteger(
//                                TTCount, sTT0, 1);
//                    } else {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = new TreeMap<String, Integer>();
//                        TTCount.put(sTT0, 1);
//                        result.put(sTT1, TTCount);
//                    }
//                }
//            }
//        }
//        if (postcodeChange) {
//            if (!postcodeChanges.isEmpty()) {
//                writePostcodeChanges(dirOut,
//                        postcodeChanges,
//                        YM30,
//                        YM31,
//                        checkPreviousPostcode);
//            }
//        }
//        if (result.isEmpty()) {
//            return null;
//        }
//        return result;
//    }
    public TreeMap<String, TreeMap<String, Integer>> getPostcodeTransitionCountsNoTenancyTypeChangeGrouped(
            Path dirOut,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
            Set<SHBE_ClaimID> tUnderOccupancy0,
            Set<SHBE_ClaimID> tUnderOccupancy1,
            HashMap<Integer, Set<SHBE_ClaimID>> tUnderOccupancies,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTTs,
            //HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
            UKP_YM3 YM30,
            UKP_YM3 YM31,
            boolean checkPreviousTenure,
            int i,
            ArrayList<Integer> include,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup0,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, UKP_RecordID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set DW_UO_Set0,
            DW_UO_Set DW_UO_Set1,
            boolean doUnderOccupiedData,
            Set<SHBE_ClaimID> underOccupiedInApril2013) throws IOException, Exception {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<>();
        }
        Iterator<SHBE_ClaimID> ite;
        // Go through current claimants
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
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
                UKP_RecordID postcode0 = null;
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
//                                       env.ge.log("indexOfLastKnownTenureOrNot " + indexOfLastKnownTenureOrNot);
                                    if (checkPreviousPostcode) {
                                        postcode0 = ClaimIDToPostcodeIDLookups.get(indexOfLastKnownTenureOrNot).get(ClaimID);
                                    }
                                }
                            }
                        } else {
                            TT0 = DW_Strings.sMinus999;
                        }
                    } else {
                        TT0 = Integer.toString(TT0Integer);
                    }
                } else {
                    TT0 = DW_Strings.sMinus999;
                }
                TT0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT0);
                Integer TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT1Integer);
                UKP_RecordID postcode1;
                postcode1 = ClaimIDToPostcodeIDLookup1.get(ClaimID);
                boolean doCount;
                if (postcodeChange) {
                    doCount = !postcode0.equals(postcode1);
                } else {
                    doCount = postcode0.equals(postcode1);
                }
                if (doCount) {
                    if (TT0.equalsIgnoreCase(TT1)
                            || TT0.equalsIgnoreCase(shbeTTHandler.sMinus999)) { // Major diff
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
                            Generic_Collections.addToMapInteger(
                                    TTCount, TT0, 1);
                        } else {
                            TreeMap<String, Integer> TTCount;
                            TTCount = new TreeMap<>();
                            TTCount.put(TT0, 1);
                            result.put(TT1, TTCount);
                        }
                    }
                }
//                } else if (isValidPostcode1) {
//                    if (result.containsKey(TT1)) {
//                        TreeMap<String, Integer> TTCount;
//                        TTCount = result.get(TT1);
//                        Generic_Collections.addToMapInteger(
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
            SHBE_ClaimID ClaimID;
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
                    TT1 = shbeTTHandler.sMinus999;
                    UKP_RecordID postcode0;
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
                        Generic_Collections.addToMapInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
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
                        checkPreviousPostcode);
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
     * @param UOSet0
     * @param UOSet1
     * @param doUnderOccupiedData
     * @param UOInApril2013
     * @return {@code
     * TreeMap<String, TreeMap<String, Integer>>
     * Tenure1, Tenure2, Count}
     * @throws java.io.IOException
     */
    public TreeMap<String, TreeMap<String, Integer>> getTTTMatrixGroupedAndWritePTDetails(
            Path dirOut,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup0,
            HashMap<SHBE_ClaimID, Integer> ClaimIDToTTLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, Integer>> ClaimIDToTTLookups,
            Set<SHBE_ClaimID> tUnderOccupancy0,
            Set<SHBE_ClaimID> tUnderOccupancy1,
            HashMap<Integer, Set<SHBE_ClaimID>> tUnderOccupancies,
            ArrayList<Integer> regulatedGroups,
            ArrayList<Integer> unregulatedGroups,
            HashMap<SHBE_ClaimID, TreeMap<UKP_YM3, String>> TTTs,
            //HashMap<SHBE_ClaimID, ArrayList<String>> TTTs,
            UKP_YM3 YM30,
            UKP_YM3 YM31,
            boolean checkPreviousTenure,
            int index,
            ArrayList<Integer> include,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup0,
            HashMap<SHBE_ClaimID, UKP_RecordID> ClaimIDToPostcodeIDLookup1,
            HashMap<Integer, HashMap<SHBE_ClaimID, UKP_RecordID>> ClaimIDToPostcodeIDLookups,
            boolean postcodeChange,
            boolean checkPreviousPostcode,
            DW_UO_Set UOSet0,
            DW_UO_Set UOSet1,
            boolean doUnderOccupiedData,
            Set<SHBE_ClaimID> UOInApril2013) throws IOException, Exception {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        ArrayList<String[]> postcodeChanges = null;
        if (postcodeChange) {
            postcodeChanges = new ArrayList<>();
        }
        Iterator<SHBE_ClaimID> ite;
        // Go through for current
        ite = ClaimIDToTTLookup1.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            boolean doMainLoop = true;
            // UnderOccupancy
            DW_UO_Record UO0 = null;
            DW_UO_Record UO1 = null;
            if (doUnderOccupiedData) {
                if (UOSet0 != null) {
                    if (UOInApril2013 == null) {
                        UO0 = UOSet0.getMap().get(ClaimID);
                    } else if (UOInApril2013.contains(ClaimID)) {
                        UO0 = UOSet0.getMap().get(ClaimID);
                    }
                }
                if (UOSet1 != null) {
                    if (UOInApril2013 == null) {
                        UO1 = UOSet1.getMap().get(ClaimID);
                    } else if (UOInApril2013.contains(ClaimID)) {
                        UO1 = UOSet1.getMap().get(ClaimID);
                    }
                }
                doMainLoop = UO0 != null || UO1 != null;
            }
            if (doMainLoop) {
                Integer TT0Integer;
                String TT0;
                UKP_RecordID postcode0 = null;
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
                            TT0 = DW_Strings.sMinus999;
                        }
                    } else {
                        TT0 = Integer.toString(TT0Integer);
                    }
                } else {
                    TT0 = DW_Strings.sMinus999;
                }
                TT0 = getTenancyTypeGroup(regulatedGroups, unregulatedGroups, TT0);
                Integer TT1Integer;
                TT1Integer = ClaimIDToTTLookup1.get(ClaimID);
                String TT1;
                TT1 = getTenancyTypeGroup(
                        regulatedGroups,
                        unregulatedGroups,
                        TT1Integer);
                UKP_RecordID postcode1;
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
                                    UO0 != null,
                                    TT1,
                                    UO1 != null);
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
                        Generic_Collections.addToMapInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
                        TTCount.put(TT0, 1);
                        result.put(TT1, TTCount);
                    }
                }
            }
        }
        // Go through for previous not in current
        ite = ClaimIDToTTLookup0.keySet().iterator();
        while (ite.hasNext()) {
            SHBE_ClaimID ClaimID;
            ClaimID = ite.next();
            if (!ClaimIDToTTLookup0.containsKey(ClaimID)) {
                boolean doMainLoop = true;
                // UnderOccupancy
                DW_UO_Record underOccupied0 = null;
                if (doUnderOccupiedData) {
                    if (UOSet0 != null) {
                        underOccupied0 = UOSet0.getMap().get(ClaimID);
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
                    TT1 = shbeTTHandler.sMinus999;
                    UKP_RecordID postcode0;
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
                        Generic_Collections.addToMapInteger(
                                TTCount, TT0, 1);
                    } else {
                        TreeMap<String, Integer> TTCount;
                        TTCount = new TreeMap<>();
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
                        checkPreviousPostcode);
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
     * @param postcodeChanges (SHBE_ClaimID, StartTime, EndTime,
     * TenancyTypeChange, StartPostcode, End Postcode)
     * @param YM30
     * @param YM31
     * @param checkPreviousPostcode
     * @param type
     */
    private void writePostcodeChanges(
            Path dirOut,
            ArrayList<String[]> postcodeChanges,
            UKP_YM3 YM30,
            UKP_YM3 YM31,
            boolean checkPreviousPostcode) throws IOException {
        Path dirOut2 = Paths.get(
                dirOut.toString(),
                DW_Strings.sPostcodeChanges);
        if (checkPreviousPostcode) {
            dirOut2 = Paths.get(
                    dirOut2.toString(),
                    DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirOut2 = Paths.get(
                    dirOut2.toString(),
                    DW_Strings.sCheckedPreviousPostcodeNo);
        }
        Files.createDirectories(dirOut2);
        Path f;
        f = Paths.get(
                dirOut2.toString(),
                DW_Strings.sPostcodeChanges
                + "_Start_" + YM30
                + "_End_" + YM31 + ".csv");

        env.ge.log("Write " + f, true);
        PrintWriter pw = env.ge.io.getPrintWriter(f, false);
        pw.println("SHBE_ClaimID, StartTime, EndTime, TenancyTypeChange, StartPostcode, End Postcode");
        Iterator<String[]> ite = postcodeChanges.iterator();
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
            result = shbeTTHandler.sMinus999;
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

    protected void writeTTTMatrix(Map<String, Map<String, Integer>> tttm,
            UKP_YM3 ym30, UKP_YM3 ym31, Path dirOut, ArrayList<String> tts,
            boolean doGrouped) throws IOException {
        writeTransitionMatrix(tttm, ym30, ym31, dirOut, tts,
                DW_Strings.sTenancyTypeTransition, doGrouped);
    }

    protected void writeTransitionMatrix(Map<String, Map<String, Integer>> tm,
            UKP_YM3 ym30, UKP_YM3 ym31, Path dirOut, ArrayList<String> ts,
            String name, Boolean doGrouped) throws IOException {
        if (tm == null) {
            return;
        }
        if (tm.isEmpty()) {
            //if (TTMatrix.size() == 0) {
            return;
        }
        Path f = Paths.get(dirOut.toString(), name + "_Start_" + ym30 + "_End_" + ym31 + ".csv");
        env.ge.log("Write " + f.toString(), true);
        try (PrintWriter pw = Generic_IO.getPrintWriter(f, false)) {
            String line = DW_Strings.sTenancyType + ym31 + "\\" + DW_Strings.sTenancyType + ym30;
            Iterator<String> ite = ts.iterator();
            while (ite.hasNext()) {
                line += "," + ite.next();
            }
            if (!doGrouped) {
                line += "," + shbeTTHandler.sMinus999;
                line += "," + shbeTTHandler.sMinus999 + sU;
            }
            pw.println(line);
            ite = ts.iterator();
            while (ite.hasNext()) {
                String t0 = ite.next();
                line = t0;
                Map<String, Integer> tCounts = tm.get(t0);
                if (tCounts == null) {
                    for (String t : ts) {
                        line += ",0";
                    }
                    //line += ",0";
                } else {
                    Iterator<String> ite2 = ts.iterator();
                    while (ite2.hasNext()) {
                        String r1 = ite2.next();
                        Integer count;
                        count = tCounts.get(r1);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    if (!doGrouped) {
                        String r1 = shbeTTHandler.sMinus999;
                        Integer nullCount = tCounts.get(r1);
                        if (nullCount == null) {
                            line += ",0";
                        } else {
                            line += "," + nullCount.toString();
                        }
                        r1 = shbeTTHandler.sMinus999 + sU;
                        nullCount = tCounts.get(r1);
                        if (nullCount == null) {
                            line += ",0";
                        } else {
                            line += "," + nullCount.toString();
                        }
                    }
                }
                pw.println(line);
            }
            if (!doGrouped) {
                Map<String, Integer> ttCounts = tm.get(shbeTTHandler.sMinus999);
                line = shbeTTHandler.sMinus999;
                if (ttCounts == null) {
                    for (String t : ts) {
                        line += ",0";
                    }
                    //line += ",0";
                } else {
                    Iterator<String> ite2 = ts.iterator();
                    while (ite2.hasNext()) {
                        String t1 = ite2.next();
                        Integer count = ttCounts.get(t1);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    String t1 = shbeTTHandler.sMinus999;
                    Integer nullCount = ttCounts.get(t1);
                    if (nullCount == null) {
                        line += ",0";
                    } else {
                        line += nullCount.toString();
                    }
                }
                pw.println(line);
                // -999U
                ttCounts = tm.get(shbeTTHandler.sMinus999 + sU);
                line = shbeTTHandler.sMinus999 + sU;
                if (ttCounts == null) {
                    for (String T : ts) {
                        line += ",0";
                    }
                    //line += ",0";
                } else {
                    String T1;
                    Iterator<String> ite2;
                    ite2 = ts.iterator();
                    while (ite2.hasNext()) {
                        T1 = ite2.next();
                        Integer count;
                        count = ttCounts.get(T1);
                        if (count == null) {
                            line += ",0";
                        } else {
                            line += "," + count.toString();
                        }
                    }
                    T1 = shbeTTHandler.sMinus999;
                    Integer nullCount = ttCounts.get(T1);
                    if (nullCount == null) {
                        line += ",0";
                    } else {
                        line += nullCount.toString();
                    }
                }
                pw.println(line);
            }
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
            int endIndex) throws IOException, ClassNotFoundException {
        Object[] result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> resultMatrix;
        resultMatrix = new TreeMap<>();
        result[0] = resultMatrix;
        TreeSet<Integer> originsAndDestinations;
        originsAndDestinations = new TreeSet<>();
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
        UKP_YM3 YM30;
        YM30 = shbeHandler.getYM3(SHBEFilenames[startIndex]);
        SHBE_Records recs0;
        recs0 = shbeHandler.getRecords(YM30, env.HOOME);
//        recs0 = env.getSHBE_Handler().getData().get(YM30);
        Map<SHBE_ClaimID, SHBE_Record> recordsStart;
        recordsStart = recs0.getRecords(env.HOOME);
        // End
        UKP_YM3 YM31;
        YM31 = shbeHandler.getYM3(SHBEFilenames[endIndex]);
        SHBE_Records recs1;
        recs1 = shbeHandler.getRecords(YM31, env.HOOME);
//        recs1 = env.getSHBE_Handler().getData().get(YM31);
        Map<SHBE_ClaimID, SHBE_Record> recordsEnd;
        recordsEnd = recs1.getRecords(env.HOOME);
        //TreeMap<String, SHBE_Record> SRecordsEnd = (TreeMap<String, SHBE_Record>) SHBEDataEnd[1];
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<SHBE_ClaimID> ite;
        ite = recordsStart.keySet().iterator();
        String councilTaxClaimNumber;
        while (ite.hasNext()) {
            SHBE_ClaimID SHBE_ClaimID = ite.next();
            SHBE_D_Record startDRecord;
            startDRecord = recordsStart.get(SHBE_ClaimID).getDRecord();
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
                    TreeSet<String> DatesOfClaims = new TreeSet<>();
                    DatesOfClaims.add(startMonth + startYear);
                    NationalInsuranceNumbersAndDatesOfClaims.put(NINO, DatesOfClaims);
                }
                //if (!startPostcodeDistrict.isEmpty()) {
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    //originsAndDestinations.add(startPostcodeDistrict);
                }

                SHBE_D_Record endDRecord;
                endDRecord = recordsEnd.get(SHBE_ClaimID).getDRecord();
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
                        TreeSet<String> DatesOfClaims = new TreeSet<>();
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
                        HashSet<String> DatesOfMoves = new HashSet<>();
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
            SHBE_ClaimID SHBE_ClaimID = ite.next();
            SHBE_D_Record DRecordEnd;
            DRecordEnd = recordsEnd.get(SHBE_ClaimID).getDRecord();
            if (DRecordEnd != null) {
                SHBE_D_Record DRecordStart;
                DRecordStart = recordsStart.get(SHBE_ClaimID).getDRecord();
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
                        TreeSet<String> DatesOfClaims = new TreeSet<>();
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
                        destinationCounts = new TreeMap<>();
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
                        HashSet<String> DatesOfMoves = new HashSet<>();
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
            int endIndex) throws IOException, ClassNotFoundException {
        Object[] result = new Object[2];
        TreeMap<Integer, TreeMap<Integer, Integer>> resultMatrix = new TreeMap<>();
        result[0] = resultMatrix;
        TreeSet<Integer> originsAndDestinations = new TreeSet<>();
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
        UKP_YM3 YM30;
        YM30 = shbeHandler.getYM3(SHBEFilenames[startIndex]);
        SHBE_Records recs0 = shbeHandler.getRecords(YM30, env.HOOME);
        //recs0 = env.getSHBE_Handler().getData().get(YM30);
        Map<SHBE_ClaimID, SHBE_Record> recordsStart = recs0.getRecords(env.HOOME);
        UKP_YM3 ym31 = shbeHandler.getYM3(SHBEFilenames[endIndex]);
        SHBE_Records recs1 = shbeHandler.getRecords(ym31, env.HOOME);
        //recs1 = env.getSHBE_Handler().getData().get(YM31);
        Map<SHBE_ClaimID, SHBE_Record> recordsEnd = recs1.getRecords(env.HOOME);
        // Iterate over records and join these with SHBE records to get postcodes
        TreeMap<Integer, Integer> destinationCounts;
        Iterator<SHBE_ClaimID> ite = recordsStart.keySet().iterator();
        SHBE_ClaimID claimID;
        while (ite.hasNext()) {
            claimID = ite.next();
            SHBE_D_Record startDRecord = recordsStart.get(claimID).getDRecord();
            if (startDRecord != null) {
                int startTenancyType = startDRecord.getTenancyType();
                if (resultMatrix.containsKey(startTenancyType)) {
                    destinationCounts = resultMatrix.get(startTenancyType);
                } else {
                    destinationCounts = new TreeMap<>();
                    resultMatrix.put(startTenancyType, destinationCounts);
                    originsAndDestinations.add(startTenancyType);
                }
                SHBE_D_Record endDRecord = recordsEnd.get(claimID).getDRecord();
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
            claimID = ite.next();
            SHBE_D_Record endDRecord = recordsEnd.get(claimID).getDRecord();
            if (endDRecord != null) {
                SHBE_D_Record DRecordStart = recordsStart.get(claimID).getDRecord();
                if (DRecordStart == null) {
                    //String startPostcodeDistrict = "unknown";
                    Integer startTenancyType = -999;
                    Integer endTenancyType = endDRecord.getTenancyType();
                    if (resultMatrix.containsKey(endTenancyType)) {
                        destinationCounts = resultMatrix.get(endTenancyType);
                    } else {
                        destinationCounts = new TreeMap<>();
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
