/*
 * Copyright (C) 2015 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.charts;

import java.io.File;
import java.math.BigDecimal;
//import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
//import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Execution;
//import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
//import uk.ac.leeds.ccg.andyt.generic.visualisation.charts.Generic_BarChart;
import uk.ac.leeds.ccg.andyt.generic.visualisation.charts.Generic_LineGraph;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_LineGraph extends Generic_LineGraph {

    HashMap<String, HashSet<String>> areaCodes;

    public DW_LineGraph() {
    }

    public DW_LineGraph(
            ExecutorService executorService,
            File file,
            String format,
            String title,
            int dataWidth,
            int dataHeight,
            String xAxisLabel,
            String yAxisLabel,
            BigDecimal yMax,
            BigDecimal yPin,
            BigDecimal yIncrement,
            int numberOfYAxisTicks,
            int decimalPlacePrecisionForCalculations,
            int decimalPlacePrecisionForDisplay,
            RoundingMode aRoundingMode) {
        super(executorService, file, format, title, dataWidth, dataHeight,
                xAxisLabel, yAxisLabel, yMax, yPin, yIncrement,
                numberOfYAxisTicks, decimalPlacePrecisionForCalculations,
                decimalPlacePrecisionForDisplay, aRoundingMode);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DW_LineGraph().run(args);
    }

    public void run(String[] args) {
        Generic_Visualisation.getHeadlessEnvironment();
        setDataWidth(1300);
        setDataHeight(500);
        setxAxisLabel("Time Periods");
//        setyPin(BigDecimal.ZERO);
        setyPin(null);
//        yMax = new BigDecimal(700);
        setyMax(null);
//        yIncrement = BigDecimal.TEN;
//        yIncrement = BigDecimal.ONE;
        setyIncrement(null);
        //int yAxisStartOfEndInterval = 60;
        setDecimalPlacePrecisionForCalculations(20);
        setDecimalPlacePrecisionForDisplay(3);
        setRoundingMode(RoundingMode.HALF_UP);
//        mc = new MathContext(decimalPlacePrecisionForCalculations, roundingMode);
        setNumberOfYAxisTicks(10);
        executorService = Executors.newSingleThreadExecutor();
        String[] SHBEFilenames;
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
//        ArrayList<String> claimantTypes;
//        claimantTypes = new ArrayList<String>();
//        claimantTypes.add("HB");
//        claimantTypes.add("CTB");

        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_SHBE_Handler.getIncludes();

        ArrayList<Boolean> b;
        b = new ArrayList<Boolean>();
        b.add(true);
        b.add(false);

        // Init allSelections of different types of tenancy type transitions to graph.
        HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>> allSelections;
        allSelections = new HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>>();
        // Init allSelections of different types of tenancy type transitions to graph.
        HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>> allSelectionsGrouped;
        allSelectionsGrouped = new HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>>();

        Iterator<Boolean> iteB;
        iteB = b.iterator();
        while (iteB.hasNext()) {
            boolean do999;
            do999 = iteB.next();
            HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>> asss;
            asss = new HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>();
            HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>> asssg;
            asssg = new HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>();
            Iterator<Boolean> iteB2;
            iteB2 = b.iterator();
            while (iteB2.hasNext()) {
                boolean doUnderOccupancy;
                doUnderOccupancy = iteB2.next();
                HashMap<Boolean, TreeMap<String, HashSet<String>>> ass;
                ass = new HashMap<Boolean, TreeMap<String, HashSet<String>>>();
                HashMap<Boolean, TreeMap<String, HashSet<String>>> assg;
                assg = new HashMap<Boolean, TreeMap<String, HashSet<String>>>();
                TreeMap<String, HashSet<String>> as;
                TreeMap<String, HashSet<String>> asg;
                Iterator<Boolean> iteB3;
                iteB3 = b.iterator();
                while (iteB3.hasNext()) {
                    boolean doSameTenancy;
                    doSameTenancy = iteB3.next();
                    as = getAllSelections(
                            doUnderOccupancy,
                            do999,
                            doSameTenancy);
                    ass.put(doSameTenancy, as);
                    asg = getAllSelectionsGrouped(
                            doUnderOccupancy,
                            do999,
                            doSameTenancy);
                    assg.put(doSameTenancy, asg);
                }
                asss.put(doUnderOccupancy, ass);
                asssg.put(doUnderOccupancy, assg);
            }
            allSelections.put(do999, asss);
            allSelectionsGrouped.put(do999, asssg);
        }

        HashSet<Future> futures;
        futures = new HashSet<Future>();

        String format = "PNG";

//        ArrayList<String> types;
//        types = new ArrayList<String>();
//        types.add(DW_Files.sPostcodeChanged);
//        types.add(sPostcodeUnchanged);
//        types.add("Multiple");
//        types.add("");
        ArrayList<Boolean> bArray;
        bArray = new ArrayList<Boolean>();
        bArray.add(true);
        bArray.add(false);

        ArrayList<String> month3Letters;
        month3Letters = Generic_Time.getMonths3Letters();

        ArrayList<String> paymentTypes;
        paymentTypes = DW_SHBE_Handler.getPaymentTypes();
//        paymentTypes.remove(DW_SHBE_Handler.sAllPT);
//        paymentTypes.remove(DW_SHBE_Handler.sInPayment);
        paymentTypes.remove(DW_SHBE_Handler.sSuspended);
        paymentTypes.remove(DW_SHBE_Handler.sOtherPT);

        Iterator<String> paymentTypesIte;
        paymentTypesIte = paymentTypes.iterator();
        while (paymentTypesIte.hasNext()) {
            String paymentType;
            paymentType = paymentTypesIte.next();

            boolean checkPreviousTenure;
            checkPreviousTenure = false;

//        iteB = bArray.iterator();
//        while (iteB.hasNext()) {
//            boolean checkPreviousTenure;
//            checkPreviousTenure = iteB.next();
//            System.out.println("CheckPreviousTenure " + checkPreviousTenure);
            File dirIn;
            dirIn = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                    "All",
                    paymentType,
                    checkPreviousTenure);
            File dirOut;
            dirOut = DW_Files.getOutputSHBEPlotsTenancyTypeTransitionDir(
                    "All",
                    paymentType,
                    checkPreviousTenure);
            boolean tenancyOnly;
//        tenancyOnly = false;

            Iterator<Boolean> iteB1;
            iteB1 = bArray.iterator();
            while (iteB1.hasNext()) {
                tenancyOnly = iteB1.next();
                if (tenancyOnly) {
                    File dirIn2 = new File(
                            dirIn,
                            "TenancyOnly");
                    File dirOut2 = new File(
                            dirOut,
                            "TenancyOnly");
                    Iterator<Boolean> iteB2;
                    iteB2 = bArray.iterator();

                    boolean doUnderOccupancyData;
                    doUnderOccupancyData = false;

                    while (iteB2.hasNext()) {
                        doUnderOccupancyData = iteB2.next();
                        if (doUnderOccupancyData) {
                            File dirIn3 = new File(
                                    dirIn2,
                                    DW_Files.sUO);
                            File dirOut3 = new File(
                                    dirOut2,
                                    DW_Files.sUO);
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                boolean doCouncil;
                                doCouncil = iteB3.next();
                                File dirIn4;
                                File dirOut4;
                                if (doCouncil) {
                                    dirIn4 = new File(
                                            dirIn3,
                                            DW_Files.sCouncil);
                                    dirOut4 = new File(
                                            dirOut3,
                                            DW_Files.sCouncil);
                                } else {
                                    dirIn4 = new File(
                                            dirIn3,
                                            DW_Files.sRSL);
                                    dirOut4 = new File(
                                            dirOut3,
                                            DW_Files.sRSL);
                                }
                                Iterator<Boolean> iteB4;
                                iteB4 = bArray.iterator();
                                while (iteB4.hasNext()) {
                                    boolean do999 = iteB4.next();
                                    File dirOut5;
                                    if (do999) {
                                        dirOut5 = new File(
                                                dirOut4,
                                                "Include999");
                                        Iterator<Boolean> iteB5;
                                        iteB5 = bArray.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean doSameTenancy;
                                            doSameTenancy = iteB5.next();
                                            File dirOut6;
                                            if (doSameTenancy) {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "IncludeSameTenancy");
                                            } else {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "NotIncludeSameTenancy");
                                            }
                                            Iterator<Boolean> iteB6;
                                            iteB6 = bArray.iterator();
                                            while (iteB6.hasNext()) {
                                                boolean grouped;
                                                grouped = iteB6.next();
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn4,
                                                            dirOut6,
                                                            includes,
                                                            allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn4,
                                                            dirOut6,
                                                            includes,
                                                            allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                }
                                            }
                                        }
                                    } else {
                                        dirOut5 = new File(
                                                dirOut4,
                                                "Exclude999");
                                        Iterator<Boolean> iteB5;
                                        iteB5 = bArray.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean doSameTenancy;
                                            doSameTenancy = iteB5.next();
                                            File dirOut6;
                                            if (doSameTenancy) {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "IncludeSameTenancy");
                                            } else {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "NotIncludeSameTenancy");
                                            }
                                            Iterator<Boolean> iteB6;
                                            iteB6 = bArray.iterator();
                                            while (iteB6.hasNext()) {
                                                boolean grouped;
                                                grouped = iteB6.next();
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn4,
                                                            dirOut6,
                                                            includes,
                                                            allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn4,
                                                            dirOut6,
                                                            includes,
                                                            allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            File dirIn3 = new File(
                                    dirIn2,
                                    "All");
                            File dirOut3 = new File(
                                    dirOut2,
                                    "All");
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                boolean do999 = iteB3.next();
                                File dirOut4;
                                if (do999) {
                                    dirOut4 = new File(
                                            dirOut3,
                                            "Include999");
                                    Iterator<Boolean> iteB4;
                                    iteB4 = bArray.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean grouped;
                                        grouped = iteB4.next();
                                        if (grouped) {
                                            doSumat(
                                                    dirIn3,
                                                    dirOut4,
                                                    includes,
                                                    allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                    futures,
                                                    format,
                                                    SHBEFilenames,
                                                    grouped,
                                                    month3Letters);
                                        } else {
                                            doSumat(
                                                    dirIn3,
                                                    dirOut4,
                                                    includes,
                                                    allSelections.get(false).get(do999).get(doUnderOccupancyData),
                                                    futures,
                                                    format,
                                                    SHBEFilenames,
                                                    grouped,
                                                    month3Letters);
                                        }
                                    }
                                } else {
                                    dirOut4 = new File(
                                            dirOut3,
                                            "Exclude999");
                                    Iterator<Boolean> iteB4;
                                    iteB4 = bArray.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean grouped;
                                        grouped = iteB4.next();
                                        if (grouped) {
                                            doSumat(
                                                    dirIn3,
                                                    dirOut4,
                                                    includes,
                                                    allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                    futures,
                                                    format,
                                                    SHBEFilenames,
                                                    grouped,
                                                    month3Letters);
                                        } else {
                                            doSumat(
                                                    dirIn3,
                                                    dirOut4,
                                                    includes,
                                                    allSelections.get(false).get(do999).get(doUnderOccupancyData),
                                                    futures,
                                                    format,
                                                    SHBEFilenames,
                                                    grouped,
                                                    month3Letters);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    File dirIn2;
                    dirIn2 = new File(
                            dirIn,
                            DW_Files.sTenancyAndPostcodeChanges);
                    File dirOut2;
                    dirOut2 = new File(
                            dirOut,
                            DW_Files.sTenancyAndPostcodeChanges);

                    boolean doUnderOccupancyData;
//                    doUnderOccupancyData = false;

                    Iterator<Boolean> iteB2;
                    iteB2 = bArray.iterator();
                    while (iteB2.hasNext()) {
                        doUnderOccupancyData = iteB2.next();
                        if (doUnderOccupancyData) {
                            File dirIn3;
                            File dirOut3;
                            dirIn3 = new File(
                                    dirIn2,
                                    DW_Files.sUO);
                            dirOut3 = new File(
                                    dirOut2,
                                    DW_Files.sUO);
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                boolean doCouncil;
                                doCouncil = iteB3.next();
                                File dirIn4;
                                File dirOut4;
                                if (doCouncil) {
                                    dirIn4 = new File(
                                            dirIn3,
                                            DW_Files.sCouncil);
                                    dirOut4 = new File(
                                            dirOut3,
                                            DW_Files.sCouncil);
                                } else {
                                    dirIn4 = new File(
                                            dirIn3,
                                            DW_Files.sRSL);
                                    dirOut4 = new File(
                                            dirOut3,
                                            DW_Files.sRSL);
                                }
                                Iterator<Boolean> iteB4;
                                iteB4 = bArray.iterator();
                                while (iteB4.hasNext()) {
                                    boolean grouped;
                                    grouped = iteB4.next();
                                    Iterator<Boolean> iteB5;
                                    iteB5 = bArray.iterator();
                                    while (iteB5.hasNext()) {
                                        boolean postcodeChanged;
                                        postcodeChanged = iteB5.next();
                                        File dirIn5;
                                        File dirOut5;
                                        if (postcodeChanged) {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    DW_Files.sPostcodeChanged);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    DW_Files.sPostcodeChanged);
                                        } else {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    DW_Files.sPostcodeUnchanged);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    DW_Files.sPostcodeUnchanged);
                                        }
                                        Iterator<Boolean> iteB6;
                                        iteB6 = bArray.iterator();
                                        while (iteB6.hasNext()) {
                                            boolean do999 = iteB6.next();
                                            File dirOut6;
                                            if (do999) {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "Include999");
                                                Iterator<Boolean> iteB7;
                                                iteB7 = bArray.iterator();
                                                while (iteB7.hasNext()) {
                                                    boolean doSameTenancy;
                                                    doSameTenancy = iteB7.next();
                                                    File dirOut7;
                                                    if (doSameTenancy) {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                "IncludeSameTenancy");
                                                    } else {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                "NotIncludeSameTenancy");
                                                    }
                                                    if (grouped) {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                includes,
                                                                allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                futures,
                                                                format,
                                                                SHBEFilenames,
                                                                grouped,
                                                                month3Letters);
                                                    } else {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                includes,
                                                                allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                futures,
                                                                format,
                                                                SHBEFilenames,
                                                                grouped,
                                                                month3Letters);
                                                    }
                                                }
                                            } else {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        "Exclude999");
                                                Iterator<Boolean> iteB7;
                                                iteB7 = bArray.iterator();
                                                while (iteB7.hasNext()) {
                                                    boolean doSameTenancy;
                                                    doSameTenancy = iteB7.next();
                                                    File dirOut7;
                                                    if (doSameTenancy) {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                "IncludeSameTenancy");
                                                    } else {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                "NotIncludeSameTenancy");
                                                    }
                                                    if (grouped) {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                includes,
                                                                allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                futures,
                                                                format,
                                                                SHBEFilenames,
                                                                grouped,
                                                                month3Letters);
                                                    } else {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                includes,
                                                                allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                futures,
                                                                format,
                                                                SHBEFilenames,
                                                                grouped,
                                                                month3Letters);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            File dirIn3;
                            File dirOut3;
                            dirIn3 = new File(
                                    dirIn2,
                                    "All");
                            dirOut3 = new File(
                                    dirOut2,
                                    "All");
                            Iterator<Boolean> iteB3;
                            iteB3 = bArray.iterator();
                            while (iteB3.hasNext()) {
                                boolean grouped;
                                grouped = iteB3.next();
                                Iterator<Boolean> iteB4;
                                iteB4 = bArray.iterator();
                                while (iteB4.hasNext()) {
                                    boolean postcodeChanged;
                                    postcodeChanged = iteB4.next();
                                    File dirIn4;
                                    File dirOut4;
                                    if (postcodeChanged) {
                                        dirIn4 = new File(
                                                dirIn3,
                                                DW_Files.sPostcodeChanged);
                                        dirOut4 = new File(
                                                dirOut3,
                                                DW_Files.sPostcodeChanged);
                                    } else {
                                        dirIn4 = new File(
                                                dirIn3,
                                                DW_Files.sPostcodeUnchanged);
                                        dirOut4 = new File(
                                                dirOut3,
                                                DW_Files.sPostcodeUnchanged);
                                    }
                                    Iterator<Boolean> iteB5;
                                    iteB5 = bArray.iterator();
                                    while (iteB5.hasNext()) {
                                        boolean do999 = iteB5.next();
                                        File dirOut5;
                                        if (do999) {
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    "Include999");
                                            if (grouped) {
                                                doSumat(
                                                        dirIn4,
                                                        dirOut5,
                                                        includes,
                                                        allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                        futures,
                                                        format,
                                                        SHBEFilenames,
                                                        grouped,
                                                        month3Letters);
                                            } else {
                                                doSumat(
                                                        dirIn4,
                                                        dirOut5,
                                                        includes,
                                                        allSelections.get(false).get(do999).get(doUnderOccupancyData),
                                                        futures,
                                                        format,
                                                        SHBEFilenames,
                                                        grouped,
                                                        month3Letters);
                                            }
                                        } else {
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    "Exclude999");
                                            if (grouped) {
                                                doSumat(
                                                        dirIn4,
                                                        dirOut5,
                                                        includes,
                                                        allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                        futures,
                                                        format,
                                                        SHBEFilenames,
                                                        grouped,
                                                        month3Letters);
                                            } else {
                                                doSumat(
                                                        dirIn4,
                                                        dirOut5,
                                                        includes,
                                                        allSelections.get(false).get(do999).get(doUnderOccupancyData),
                                                        futures,
                                                        format,
                                                        SHBEFilenames,
                                                        grouped,
                                                        month3Letters);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Generic_Execution.shutdownExecutorService(
                executorService, futures,
                this);
    }

    private void doSumat(
            File dirIn,
            File dirOut,
            TreeMap<String, ArrayList<Integer>> includes,
            TreeMap<String, HashSet<String>> allSelections,
            HashSet<Future> futures,
            String format,
            String[] SHBEFilenames,
            boolean grouped,
            ArrayList<String> month3Letters) {
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeKey;
            includeKey = includesIte.next();
            File dirIn2;
            dirIn2 = new File(
                    dirIn,
                    includeKey);
//            doneFirsts.put(includeKey, false);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Object[] treeMapDateLabelSHBEFilename;
            treeMapDateLabelSHBEFilename = DW_SHBE_Handler.getTreeMapDateLabelSHBEFilenames(
                    SHBEFilenames,
                    include);
            TreeMap<BigDecimal, String> xAxisLabels;
            xAxisLabels = (TreeMap<BigDecimal, String>) treeMapDateLabelSHBEFilename[0];
            TreeMap<String, BigDecimal> fileLabelValue;
            fileLabelValue = (TreeMap<String, BigDecimal>) treeMapDateLabelSHBEFilename[1];
            TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>> bigMatrix;
            bigMatrix = new TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>>();
//            int size = Integer.MIN_VALUE;
//            Double min = Double.MAX_VALUE;
//            Double max = Double.MIN_VALUE;
            boolean doneFirst;
            doneFirst = false;

            String yM30 = "";

            Iterator<Integer> includeIte;
            includeIte = include.iterator();
            while (includeIte.hasNext()) {
                int i = includeIte.next();
                if (!doneFirst) {
                    if (include.contains(i - 1) && (i - 1) >= 0) {
                        String aSHBEFilename0;
                        aSHBEFilename0 = SHBEFilenames[i - 1];
                        yM30 = DW_SHBE_Handler.getYM3(aSHBEFilename0);
                        doneFirst = true;
                    }
                }
                if (doneFirst) {
                    String aSHBEFilename1;
                    aSHBEFilename1 = SHBEFilenames[i];
                    String yM31;
                    yM31 = DW_SHBE_Handler.getYM3(aSHBEFilename1);
                    String filename;
                    filename = "TenancyTypeTransition_Start_" + yM30 + "_End_" + yM31 + ".csv";
                    if (include.contains(i)) {
                        File dirIn3;
                        double timeDiff;
                        String[] split0;
                        split0 = yM30.split("_");
                        String[] split1;
                        split1 = yM31.split("_");
                        timeDiff = Generic_Time.getMonthDiff(
                                Integer.valueOf(split0[0]),
                                Integer.valueOf(split1[0]),
                                Generic_Time.getMonth(split0[1], month3Letters),
                                Generic_Time.getMonth(split1[1], month3Letters));
                        String label;
                        label = getLabel(yM30, yM31);
                        BigDecimal key;
                        key = fileLabelValue.get(label);
                        if (grouped) {
                            dirIn3 = new File(
                                    dirIn2,
                                    DW_Files.sGrouped);
                            File f;
                            f = new File(
                                    dirIn3,
                                    filename);
                            if (f.exists()) {
                                System.out.println("Using file " + f);

                                TreeMap<String, TreeMap<String, BigDecimal>> tenancyTypeTransitionMapGrouped;
                                tenancyTypeTransitionMapGrouped = getTenancyTypeTransitionMapGrouped(
                                        f,
                                        timeDiff);
                                if (key != null) {
                                    bigMatrix.put(key, tenancyTypeTransitionMapGrouped);
                                } else {
                                    System.out.println("No value for label " + label);
                                }
                            } else {
                                System.out.println(f + " does not exist");
                            }
                        } else {
                            dirIn3 = new File(
                                    dirIn2,
                                    DW_Files.sUngrouped);
                            File f;
                            f = new File(
                                    dirIn3,
                                    filename);
                            if (f.exists()) {
                                System.out.println("Using file " + f);
                                // TenureBefore, TenureNow Counts.
                                TreeMap<String, TreeMap<String, BigDecimal>> tenancyTypeTransitionMap;
                                tenancyTypeTransitionMap = getTenancyTypeTransitionMap(
                                        f,
                                        timeDiff);

//                    Object[] tenancyTypeTransitionMatrixMinMaxSize;
//                    tenancyTypeTransitionMatrixMinMaxSize = getTenancyTypeTransitionMap(
//                            f,
//                            timeDiff);
//                    tenancyTypeTransitionMap = (TreeMap<Integer, TreeMap<Integer, BigDecimal>>) tenancyTypeTransitionMatrixMinMaxSize[0];
//                    Double tenancyTypeTransitionMatrixMin;
//                    tenancyTypeTransitionMatrixMin = (Double) tenancyTypeTransitionMatrixMinMaxSize[1];
//                    min = Math.min(tenancyTypeTransitionMatrixMin, min);
//                    Double tenancyTypeTransitionMatrixMax;
//                    tenancyTypeTransitionMatrixMax = (Double) tenancyTypeTransitionMatrixMinMaxSize[2];
//                    max = Math.max(tenancyTypeTransitionMatrixMax, max);
//                    size = Math.max(size, (Integer) tenancyTypeTransitionMatrixMinMaxSize[3]);
                                //System.out.println("fileLabel " + fileLabel);
                                //System.out.println("key " + key);
                                if (key != null) {
                                    bigMatrix.put(key, tenancyTypeTransitionMap);
//                    System.out.println("max " + max);
//                    System.out.println("min " + min);
//                    System.out.println("size " + size);
                                } else {
                                    System.out.println("No value for label " + label);
                                }
                            } else {
                                System.out.println(f + " does not exist");
                            }
                        }
                        yM30 = yM31;
                    } else {
                        System.out.println("Omitted file " + filename);
                    }
                }
            }
            if (!bigMatrix.isEmpty()) {
                File dirOut2;
                dirOut2 = new File(
                        dirOut,
                        includeKey);
                if (grouped) {
                    dirOut2 = new File(
                            dirOut2,
                            DW_Files.sGrouped);
                } else {
                    dirOut2 = new File(
                            dirOut2,
                            DW_Files.sUngrouped);
                }
                Iterator<String> allSelectionsIte;
                allSelectionsIte = allSelections.keySet().iterator();
                while (allSelectionsIte.hasNext()) {
                    String selections = allSelectionsIte.next();
                    dirOut2.mkdirs();
                    File fout;
                    fout = new File(
                            dirOut2,
                            "TenancyTransitionLineGraph" + selections + ".PNG");
                    setyAxisLabel("Tenancy Changes Per Month");
                    String title;
                    title = "Tenancy Transition Line Graph";
                    DW_LineGraph chart = new DW_LineGraph(
                            executorService,
                            fout,
                            format,
                            title,
                            getDataWidth(),
                            getDataHeight(),
                            getxAxisLabel(),
                            getyAxisLabel(),
                            getyMax(),
                            getyPin(),
                            getyIncrement(),
                            getNumberOfYAxisTicks(),
                            getDecimalPlacePrecisionForCalculations(),
                            getDecimalPlacePrecisionForDisplay(),
                            getRoundingMode());
                    Object[] data;
                    HashSet<String> selection = allSelections.get(selections);
                    data = getData(
                            bigMatrix,
                            selection,
                            xAxisLabels);
                    if (data != null) {
                        chart.setData(data);
                        chart.run();
                        Future future = chart.future;
                        futures.add(future);
                    } else {
                        futures.add(chart.future);
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     */
    protected static TreeMap<String, HashSet<String>> getAllSelections(
            boolean doUnderOccupancy,
            boolean do999,
            boolean sameTenancyType) {
        TreeMap<String, HashSet<String>> result;
        result = new TreeMap<String, HashSet<String>>();
        String selectionName;
        selectionName = DW_Files.sCouncil;
        HashSet<String> councilSelection;
        councilSelection = new HashSet<String>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                councilSelection.add("1UO - 1");
                councilSelection.add("1 - 1UO");
                councilSelection.add("1UO - 1UO");
            }
            councilSelection.add("1UO - 2");
            councilSelection.add("1 - 2UO");
            councilSelection.add("1UO - 2UO");
            councilSelection.add("1UO - 3");
            councilSelection.add("1 - 3UO");
            councilSelection.add("1UO - 3UO");
            councilSelection.add("1UO - 4");
            councilSelection.add("1 - 4UO");
            councilSelection.add("1UO - 4UO");
            councilSelection.add("1UO - 5");
            councilSelection.add("1 - 5UO");
            councilSelection.add("1UO - 5UO");
            councilSelection.add("1UO - 6");
            councilSelection.add("1 - 6UO");
            councilSelection.add("1UO - 6UO");
            councilSelection.add("1UO - 7");
            councilSelection.add("1 - 7UO");
            councilSelection.add("1UO - 7UO");
            councilSelection.add("1UO - 8");
            councilSelection.add("1 - 8UO");
            councilSelection.add("1UO - 8UO");
            councilSelection.add("1UO - 9");
            councilSelection.add("1 - 9UO");
            councilSelection.add("1UO - 9UO");
            if (do999) {
                councilSelection.add("1UO - -999");
                councilSelection.add("-999 - 1UO");
                councilSelection.add("1 - -999UO");
                councilSelection.add("-999UO - 1");
                councilSelection.add("1UO - -999UO");
                councilSelection.add("-999UO - 1UO");
            }
        } else {
            if (sameTenancyType) {
                councilSelection.add("1 - 1");
            }
            councilSelection.add("1 - 2");
            councilSelection.add("1 - 3");
            councilSelection.add("1 - 4");
            councilSelection.add("1 - 5");
            councilSelection.add("1 - 6");
            councilSelection.add("1 - 7");
            councilSelection.add("1 - 8");
            councilSelection.add("1 - 9");
            if (do999) {
                councilSelection.add("1 - -999");
                councilSelection.add("-999 - 1");
            }
        }
        result.put(selectionName, councilSelection);
        selectionName = "PrivateRegulated";
        HashSet<String> privateRegulatedSelection;
        privateRegulatedSelection = new HashSet<String>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                privateRegulatedSelection.add("2UO - 2");
                privateRegulatedSelection.add("2 - 2UO");
                privateRegulatedSelection.add("2UO - 2UO");
            }
            privateRegulatedSelection.add("2UO - 1");
            privateRegulatedSelection.add("2 - 1UO");
            privateRegulatedSelection.add("2UO - 1UO");
            privateRegulatedSelection.add("2UO - 3");
            privateRegulatedSelection.add("2 - 3UO");
            privateRegulatedSelection.add("2UO - 3UO");
            privateRegulatedSelection.add("2UO - 4");
            privateRegulatedSelection.add("2 - 4UO");
            privateRegulatedSelection.add("2UO - 4UO");
            privateRegulatedSelection.add("2UO - 5");
            privateRegulatedSelection.add("2 - 5UO");
            privateRegulatedSelection.add("2UO - 5UO");
            privateRegulatedSelection.add("2UO - 6");
            privateRegulatedSelection.add("2 - 6UO");
            privateRegulatedSelection.add("2UO - 6UO");
            privateRegulatedSelection.add("2UO - 7");
            privateRegulatedSelection.add("2 - 7UO");
            privateRegulatedSelection.add("2UO - 7UO");
            privateRegulatedSelection.add("2UO - 8");
            privateRegulatedSelection.add("2 - 8UO");
            privateRegulatedSelection.add("2UO - 8UO");
            privateRegulatedSelection.add("2UO - 9");
            privateRegulatedSelection.add("2 - 9UO");
            privateRegulatedSelection.add("2UO - 9UO");
            if (do999) {
                privateRegulatedSelection.add("2UO - -999");
                privateRegulatedSelection.add("-999 - 2UO");
                privateRegulatedSelection.add("2 - -999UO");
                privateRegulatedSelection.add("-999UO - 2UO");
                privateRegulatedSelection.add("2UO - -999UO");
                privateRegulatedSelection.add("-999UO - 2UO");
            }
        } else {
            if (sameTenancyType) {
                privateRegulatedSelection.add("2 - 2");
            }
            privateRegulatedSelection.add("2 - 1");
            privateRegulatedSelection.add("2 - 3");
            privateRegulatedSelection.add("2 - 4");
            privateRegulatedSelection.add("2 - 5");
            privateRegulatedSelection.add("2 - 6");
            privateRegulatedSelection.add("2 - 7");
            privateRegulatedSelection.add("2 - 8");
            privateRegulatedSelection.add("2 - 9");
            if (do999) {
                privateRegulatedSelection.add("2 - -999");
                privateRegulatedSelection.add("-999 - 2");
            }
        }
        result.put(selectionName, privateRegulatedSelection);
        HashSet<String> privateDeregulatedSelection;
        privateDeregulatedSelection = new HashSet<String>();
        selectionName = "PrivateDeregulated";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                privateDeregulatedSelection.add("3UO - 3");
                privateDeregulatedSelection.add("3 - 3UO");
                privateDeregulatedSelection.add("3UO - 3UO");
            }
            privateDeregulatedSelection.add("3UO - 1");
            privateDeregulatedSelection.add("3 - 1UO");
            privateDeregulatedSelection.add("3UO - 1UO");
            privateDeregulatedSelection.add("3UO - 2");
            privateDeregulatedSelection.add("3 - 2UO");
            privateDeregulatedSelection.add("3UO - 2UO");
            privateDeregulatedSelection.add("3UO - 2UO");
            privateDeregulatedSelection.add("3UO - 4");
            privateDeregulatedSelection.add("3 - 4UO");
            privateDeregulatedSelection.add("3UO - 4UO");
            privateDeregulatedSelection.add("3UO - 5");
            privateDeregulatedSelection.add("3 - 5UO");
            privateDeregulatedSelection.add("3UO - 5UO");
            privateDeregulatedSelection.add("3UO - 6");
            privateDeregulatedSelection.add("3 - 6UO");
            privateDeregulatedSelection.add("3UO - 6UO");
            privateDeregulatedSelection.add("3UO - 7");
            privateDeregulatedSelection.add("3 - 7UO");
            privateDeregulatedSelection.add("3UO - 7UO");
            privateDeregulatedSelection.add("3UO - 8");
            privateDeregulatedSelection.add("3 - 8UO");
            privateDeregulatedSelection.add("3UO - 8UO");
            privateDeregulatedSelection.add("3UO - 9");
            privateDeregulatedSelection.add("3 - 9UO");
            privateDeregulatedSelection.add("3UO - 9UO");
            if (do999) {
                privateDeregulatedSelection.add("3UO - -999");
                privateDeregulatedSelection.add("-999 - 3UO");
                privateDeregulatedSelection.add("3 - -999UO");
                privateDeregulatedSelection.add("-999UO - 3");
                privateDeregulatedSelection.add("3UO - -999UO");
                privateDeregulatedSelection.add("-999UO - 3UO");
            }
        } else {
            if (sameTenancyType) {
                privateDeregulatedSelection.add("3 - 3");
            }
            privateDeregulatedSelection.add("3 - 1");
            privateDeregulatedSelection.add("3 - 2");
            privateDeregulatedSelection.add("3 - 4");
            privateDeregulatedSelection.add("3 - 5");
            privateDeregulatedSelection.add("3 - 6");
            privateDeregulatedSelection.add("3 - 7");
            privateDeregulatedSelection.add("3 - 8");
            privateDeregulatedSelection.add("3 - 9");
            if (do999) {
                privateDeregulatedSelection.add("3 - -999");
                privateDeregulatedSelection.add("-999 - 3");
            }
        }
        result.put(selectionName, privateDeregulatedSelection);
        HashSet<String> privateHousingAssociationSelection;
        privateHousingAssociationSelection = new HashSet<String>();
        selectionName = "PrivateHousingAssociation";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                privateHousingAssociationSelection.add("4UO - 4");
                privateHousingAssociationSelection.add("4 - 4UO");
                privateHousingAssociationSelection.add("4UO - 4UO");
            }
            privateHousingAssociationSelection.add("4UO - 4");
            privateHousingAssociationSelection.add("4 - 4UO");
            privateHousingAssociationSelection.add("4UO - 1");
            privateHousingAssociationSelection.add("4 - 1UO");
            privateHousingAssociationSelection.add("4UO - 1UO");
            privateHousingAssociationSelection.add("4UO - 2");
            privateHousingAssociationSelection.add("4 - 2UO");
            privateHousingAssociationSelection.add("4UO - 2UO");
            privateHousingAssociationSelection.add("4UO - 3");
            privateHousingAssociationSelection.add("4 - 3UO");
            privateHousingAssociationSelection.add("4UO - 3UO");
            privateHousingAssociationSelection.add("4UO - 5");
            privateHousingAssociationSelection.add("4 - 5UO");
            privateHousingAssociationSelection.add("4UO - 5UO");
            privateHousingAssociationSelection.add("4UO - 6");
            privateHousingAssociationSelection.add("4 - 6UO");
            privateHousingAssociationSelection.add("4UO - 6UO");
            privateHousingAssociationSelection.add("4UO - 7");
            privateHousingAssociationSelection.add("4 - 7UO");
            privateHousingAssociationSelection.add("4UO - 7UO");
            privateHousingAssociationSelection.add("4UO - 8");
            privateHousingAssociationSelection.add("4 - 8UO");
            privateHousingAssociationSelection.add("4UO - 8UO");
            privateHousingAssociationSelection.add("4UO - 9");
            privateHousingAssociationSelection.add("4 - 9UO");
            privateHousingAssociationSelection.add("4UO - 9UO");
            if (do999) {
                privateHousingAssociationSelection.add("4UO - -999");
                privateHousingAssociationSelection.add("-999 - 4UO");
                privateHousingAssociationSelection.add("4 - -999UO");
                privateHousingAssociationSelection.add("-999UO - 4");
                privateHousingAssociationSelection.add("4UO - -999UO");
                privateHousingAssociationSelection.add("-999UO - 4UO");
            }
        } else {
            if (sameTenancyType) {
                privateHousingAssociationSelection.add("4 - 4");
            }
            privateHousingAssociationSelection.add("4 - 1");
            privateHousingAssociationSelection.add("4 - 2");
            privateHousingAssociationSelection.add("4 - 3");
            privateHousingAssociationSelection.add("4 - 5");
            privateHousingAssociationSelection.add("4 - 6");
            privateHousingAssociationSelection.add("4 - 7");
            privateHousingAssociationSelection.add("4 - 8");
            privateHousingAssociationSelection.add("4 - 9");
            if (do999) {
                privateHousingAssociationSelection.add("4 - -999");
                privateHousingAssociationSelection.add("-999 - 4");
            }
        }
        result.put(selectionName, privateHousingAssociationSelection);
        HashSet<String> privateOtherSelection;
        privateOtherSelection = new HashSet<String>();
        selectionName = "PrivateOther";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                privateOtherSelection.add("6UO - 6");
                privateOtherSelection.add("6 - 6UO");
                privateOtherSelection.add("6UO - 6UO");
            }
            privateOtherSelection.add("6UO - 1");
            privateOtherSelection.add("6 - 1UO");
            privateOtherSelection.add("6UO - 1UO");
            privateOtherSelection.add("6UO - 2");
            privateOtherSelection.add("6 - 2UO");
            privateOtherSelection.add("6UO - 2UO");
            privateOtherSelection.add("6UO - 3");
            privateOtherSelection.add("6 - 3UO");
            privateOtherSelection.add("6UO - 3UO");
            privateOtherSelection.add("6UO - 4");
            privateOtherSelection.add("6 - 4UO");
            privateOtherSelection.add("6UO - 4UO");
            privateOtherSelection.add("6UO - 5");
            privateOtherSelection.add("6 - 5UO");
            privateOtherSelection.add("6UO - 5UO");
            privateOtherSelection.add("6UO - 7");
            privateOtherSelection.add("6 - 7UO");
            privateOtherSelection.add("6UO - 7UO");
            privateOtherSelection.add("6UO - 8");
            privateOtherSelection.add("6 - 8UO");
            privateOtherSelection.add("6UO - 8UO");
            privateOtherSelection.add("6UO - 9");
            privateOtherSelection.add("6 - 9UO");
            privateOtherSelection.add("6UO - 9UO");
            if (do999) {
                privateOtherSelection.add("6UO - -999");
                privateOtherSelection.add("-999 - 6UO");
                privateOtherSelection.add("6 - -999UO");
                privateOtherSelection.add("-999UO - 6");
                privateOtherSelection.add("6UO - -999UO");
                privateOtherSelection.add("-999UO - 6UO");
            }
        } else {
            if (sameTenancyType) {
                privateOtherSelection.add("6 - 6");
            }
            privateOtherSelection.add("6 - 1");
            privateOtherSelection.add("6 - 2");
            privateOtherSelection.add("6 - 3");
            privateOtherSelection.add("6 - 4");
            privateOtherSelection.add("6 - 5");
            privateOtherSelection.add("6 - 7");
            privateOtherSelection.add("6 - 8");
            privateOtherSelection.add("6 - 9");
            if (do999) {
                privateOtherSelection.add("6 - -999");
                privateOtherSelection.add("-999 - 6");
            }
        }
        result.put(selectionName, privateOtherSelection);

        HashSet<String> CTBOnlySelection;
        CTBOnlySelection = new HashSet<String>();
        selectionName = "CTBOnly";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                CTBOnlySelection.add("7 - 7UO");
                CTBOnlySelection.add("7UO - 7");
                CTBOnlySelection.add("7UO - 7UO");
            }
            CTBOnlySelection.add("7UO - 1");
            CTBOnlySelection.add("7 - 1UO");
            CTBOnlySelection.add("7UO - 1UO");
            CTBOnlySelection.add("7UO - 2");
            CTBOnlySelection.add("7 - 2UO");
            CTBOnlySelection.add("7UO - 2UO");
            CTBOnlySelection.add("7UO - 3");
            CTBOnlySelection.add("7 - 3UO");
            CTBOnlySelection.add("7UO - 3UO");
            CTBOnlySelection.add("7UO - 4");
            CTBOnlySelection.add("7 - 4UO");
            CTBOnlySelection.add("7UO - 4UO");
            CTBOnlySelection.add("7UO - 5");
            CTBOnlySelection.add("7 - 5UO");
            CTBOnlySelection.add("7UO - 5UO");
            CTBOnlySelection.add("7UO - 6");
            CTBOnlySelection.add("7 - 6UO");
            CTBOnlySelection.add("7UO - 6UO");
            CTBOnlySelection.add("7UO - 8");
            CTBOnlySelection.add("7 - 8UO");
            CTBOnlySelection.add("7UO - 8UO");
            CTBOnlySelection.add("7UO - 9");
            CTBOnlySelection.add("7 - 9UO");
            CTBOnlySelection.add("7UO - 9UO");
            if (do999) {
                CTBOnlySelection.add("7UO - -999");
                CTBOnlySelection.add("-999 - 7UO");
                CTBOnlySelection.add("7 - -999UO");
                CTBOnlySelection.add("-999UO - 7");
                CTBOnlySelection.add("7UO - -999UO");
                CTBOnlySelection.add("-999UO - 7UO");
            }
        } else {
            if (sameTenancyType) {
                CTBOnlySelection.add("7 - 7");
            }
            CTBOnlySelection.add("7 - 1");
            CTBOnlySelection.add("7 - 2");
            CTBOnlySelection.add("7 - 3");
            CTBOnlySelection.add("7 - 4");
            CTBOnlySelection.add("7 - 5");
            CTBOnlySelection.add("7 - 6");
            CTBOnlySelection.add("7 - 8");
            CTBOnlySelection.add("7 - 9");
            if (do999) {
                CTBOnlySelection.add("7 - -999");
                CTBOnlySelection.add("-999 - 7");
            }
        }
        result.put(selectionName, CTBOnlySelection);

        HashSet<String> councilNonHRAOnlySelection;
        councilNonHRAOnlySelection = new HashSet<String>();
        selectionName = "CouncilNonHRA";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                councilNonHRAOnlySelection.add("8 - 8UO");
                councilNonHRAOnlySelection.add("8UO - 8");
                councilNonHRAOnlySelection.add("8UO - 8UO");
            }
            councilNonHRAOnlySelection.add("8UO - 1");
            councilNonHRAOnlySelection.add("8 - 1UO");
            councilNonHRAOnlySelection.add("8UO - 1UO");
            councilNonHRAOnlySelection.add("8UO - 2");
            councilNonHRAOnlySelection.add("8 - 2UO");
            councilNonHRAOnlySelection.add("8UO - 2UO");
            councilNonHRAOnlySelection.add("8UO - 3");
            councilNonHRAOnlySelection.add("8 - 3UO");
            councilNonHRAOnlySelection.add("8UO - 3UO");
            councilNonHRAOnlySelection.add("8UO - 4");
            councilNonHRAOnlySelection.add("8 - 4UO");
            councilNonHRAOnlySelection.add("8UO - 4UO");
            councilNonHRAOnlySelection.add("8UO - 5");
            councilNonHRAOnlySelection.add("8 - 5UO");
            councilNonHRAOnlySelection.add("8UO - 5UO");
            councilNonHRAOnlySelection.add("8UO - 6");
            councilNonHRAOnlySelection.add("8 - 6UO");
            councilNonHRAOnlySelection.add("8UO - 6UO");
            councilNonHRAOnlySelection.add("8UO - 7");
            councilNonHRAOnlySelection.add("8 - 7UO");
            councilNonHRAOnlySelection.add("8UO - 7UO");
            councilNonHRAOnlySelection.add("8UO - 9");
            councilNonHRAOnlySelection.add("8 - 9UO");
            councilNonHRAOnlySelection.add("8UO - 9UO");
            if (do999) {
                councilNonHRAOnlySelection.add("8UO - -999");
                councilNonHRAOnlySelection.add("-999 - 8UO");
                councilNonHRAOnlySelection.add("8 - -999UO");
                councilNonHRAOnlySelection.add("-999UO - 8");
                councilNonHRAOnlySelection.add("8UO - -999UO");
                councilNonHRAOnlySelection.add("-999UO - 8UO");
            }
        } else {
            if (sameTenancyType) {
                councilNonHRAOnlySelection.add("8 - 8");
            }
            councilNonHRAOnlySelection.add("8 - 1");
            councilNonHRAOnlySelection.add("8 - 2");
            councilNonHRAOnlySelection.add("8 - 3");
            councilNonHRAOnlySelection.add("8 - 4");
            councilNonHRAOnlySelection.add("8 - 5");
            councilNonHRAOnlySelection.add("8 - 6");
            councilNonHRAOnlySelection.add("8 - 7");
            councilNonHRAOnlySelection.add("8 - 9");
            if (do999) {
                councilNonHRAOnlySelection.add("8 - -999");
                councilNonHRAOnlySelection.add("-999 - 8");
            }
        }
        result.put(selectionName, councilNonHRAOnlySelection);
        HashSet<String> privateMealDeductionSelection;
        privateMealDeductionSelection = new HashSet<String>();
        selectionName = "PrivateMealDeduction";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                privateMealDeductionSelection.add("9 - 9UO");
                privateMealDeductionSelection.add("9UO - 9");
                privateMealDeductionSelection.add("9UO - 9UO");
            }
            privateMealDeductionSelection.add("9UO - 1");
            privateMealDeductionSelection.add("9 - 1UO");
            privateMealDeductionSelection.add("9UO - 1UO");
            privateMealDeductionSelection.add("9UO - 2");
            privateMealDeductionSelection.add("9 - 2UO");
            privateMealDeductionSelection.add("9UO - 2UO");
            privateMealDeductionSelection.add("9UO - 3");
            privateMealDeductionSelection.add("9 - 3UO");
            privateMealDeductionSelection.add("9UO - 3UO");
            privateMealDeductionSelection.add("9UO - 4");
            privateMealDeductionSelection.add("9 - 4UO");
            privateMealDeductionSelection.add("9UO - 4UO");
            privateMealDeductionSelection.add("9UO - 5");
            privateMealDeductionSelection.add("9 - 5UO");
            privateMealDeductionSelection.add("9UO - 5UO");
            privateMealDeductionSelection.add("9UO - 6");
            privateMealDeductionSelection.add("9 - 6UO");
            privateMealDeductionSelection.add("9UO - 6UO");
            privateMealDeductionSelection.add("9UO - 7");
            privateMealDeductionSelection.add("9 - 7UO");
            privateMealDeductionSelection.add("9UO - 7UO");
            privateMealDeductionSelection.add("9UO - 8");
            privateMealDeductionSelection.add("9 - 8UO");
            privateMealDeductionSelection.add("9UO - 8UO");
            if (do999) {
                privateMealDeductionSelection.add("9UO - -999");
                privateMealDeductionSelection.add("-999 - 9UO");
                privateMealDeductionSelection.add("9 - -999UO");
                privateMealDeductionSelection.add("-999UO - 9");
                privateMealDeductionSelection.add("9UO - -999UO");
                privateMealDeductionSelection.add("-999UO - 9UO");
            }
        } else {
            if (sameTenancyType) {
                privateMealDeductionSelection.add("9 - 9");
            }
            privateMealDeductionSelection.add("9 - 1");
            privateMealDeductionSelection.add("9 - 2");
            privateMealDeductionSelection.add("9 - 3");
            privateMealDeductionSelection.add("9 - 4");
            privateMealDeductionSelection.add("9 - 5");
            privateMealDeductionSelection.add("9 - 6");
            privateMealDeductionSelection.add("9 - 7");
            privateMealDeductionSelection.add("9 - 9");
            if (do999) {
                privateMealDeductionSelection.add("9 - -999");
                privateMealDeductionSelection.add("-999 - 9");
            }
        }
        result.put(selectionName, privateMealDeductionSelection);
        HashSet<String> allSelection;
        allSelection = new HashSet<String>();
        selectionName = "All";
        allSelection.addAll(councilSelection);
        allSelection.addAll(privateHousingAssociationSelection);
        allSelection.addAll(privateRegulatedSelection);
        allSelection.addAll(privateDeregulatedSelection);
        allSelection.addAll(privateOtherSelection);
        result.put(selectionName, allSelection);

        selectionName = "ReportSelection";
        HashSet<String> selection;
        selection = new HashSet<String>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                selection.add("1 - 1UO");
                selection.add("1UO - 1UO");
                selection.add("1UO - 1UO");
                selection.add("2 - 2UO");
                selection.add("2UO - 2UO");
                selection.add("2UO - 2UO");
                selection.add("3 - 3UO");
                selection.add("3UO - 3UO");
                selection.add("3UO - 3UO");
            }
            selection.add("1 - 3UO");
            selection.add("1UO - 3");
            selection.add("1UO - 3UO");
            selection.add("1 - 4UO");
            selection.add("1UO - 4");
            selection.add("1UO - 4UO");
            selection.add("3 - 1UO");
            selection.add("3UO - 1");
            selection.add("3UO - 1UO");
            selection.add("3 - 4UO");
            selection.add("3UO - 4");
            selection.add("3UO - 4UO");
            selection.add("4 - 1UO");
            selection.add("4UO - 1");
            selection.add("4UO - 1UO");
            selection.add("4 - 3UO");
            selection.add("4UO - 3");
            selection.add("4UO - 3UO");
            if (do999) {
                selection.add("1UO - -999");
                selection.add("-999 - 1UO");
                selection.add("3UO - -999");
                selection.add("-999 - 3UO");
                selection.add("4UO - -999");
                selection.add("-999 - 4UO");
            }
        } else {
            if (sameTenancyType) {
                selection.add("1 - 1");
                selection.add("3 - 3");
                selection.add("4 - 4");
            }
            selection.add("1 - 3");
            selection.add("1 - 4");
            selection.add("3 - 1");
            selection.add("3 - 4");
            selection.add("4 - 1");
            selection.add("4 - 3");
            if (do999) {
                selection.add("1 - -999");
                selection.add("-999 - 1");
                selection.add("3 - -999");
                selection.add("-999 - 3");
                selection.add("4 - -999");
                selection.add("-999 - 4");
            }
        }
        result.put(selectionName, selection);
        return result;
    }

    /**
     * @return
     */
    protected static TreeMap<String, HashSet<String>> getAllSelectionsGrouped(
            boolean doUnderOccupancy,
            boolean do999,
            boolean sameTenancyType) {
        TreeMap<String, HashSet<String>> result;
        result = new TreeMap<String, HashSet<String>>();
        String selectionName;
        selectionName = "Regulated";
        HashSet<String> regulatedSelection;
        regulatedSelection = new HashSet<String>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                regulatedSelection.add("RegulatedUO - Regulated");
                regulatedSelection.add("Regulated - RegulatedUO");
                regulatedSelection.add("RegulatedUO - RegulatedUO");
            }
            regulatedSelection.add("RegulatedUO - Unregulated");
            regulatedSelection.add("Regulated - UnregulatedUO");
            regulatedSelection.add("RegulatedUO - UnregulatedUO");
            regulatedSelection.add("Regulated - UngroupedUO");
            regulatedSelection.add("RegulatedUO - Ungrouped");
            regulatedSelection.add("RegulatedUO - UngroupedUO");
            if (do999) {
                regulatedSelection.add("RegulatedUO - -999");
                regulatedSelection.add("-999 - RegulatedUO");
                regulatedSelection.add("Regulated - -999UO");
                regulatedSelection.add("-999UO - Regulated");
                regulatedSelection.add("RegulatedUO - -999UO");
                regulatedSelection.add("-999UO - RegulatedUO");
            }
        } else {
            if (sameTenancyType) {
                regulatedSelection.add("Regulated - Regulated");
            }
            regulatedSelection.add("Regulated - Unregulated");
            regulatedSelection.add("Regulated - Ungrouped");
            if (do999) {
                regulatedSelection.add("Regulated - -999");
                regulatedSelection.add("-999 - Regulated");
            }
        }
        result.put(selectionName, regulatedSelection);
        HashSet<String> unregulatedSelection;
        unregulatedSelection = new HashSet<String>();
        selectionName = "Unregulated";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                unregulatedSelection.add("UnregulatedUO - Unregulated");
                unregulatedSelection.add("Unregulated - UnregulatedUO");
                unregulatedSelection.add("UnregulatedUO - UnregulatedUO");
            }
            unregulatedSelection.add("UnregulatedUO - Regulated");
            unregulatedSelection.add("Unregulated - RegulatedUO");
            unregulatedSelection.add("UnregulatedUO - RegulatedUO");
            unregulatedSelection.add("UnregulatedUO - UngroupedUO");
            unregulatedSelection.add("UnregulatedUO - Ungrouped");
            unregulatedSelection.add("Unregulated - UngroupedUO");
            if (do999) {
                unregulatedSelection.add("UnregulatedUO - -999");
                unregulatedSelection.add("-999 - UnregulatedUO");
                unregulatedSelection.add("Unregulated - -999UO");
                unregulatedSelection.add("-999UO - Unregulated");
                unregulatedSelection.add("UnregulatedUO - -999UO");
                unregulatedSelection.add("-999UO - UnregulatedUO");
            }
        } else {
            if (sameTenancyType) {
                unregulatedSelection.add("Unregulated - Unregulated");
            }
            unregulatedSelection.add("Unregulated - Regulated");
            unregulatedSelection.add("Unregulated - Ungrouped");
            if (do999) {
                unregulatedSelection.add("Unregulated - -999");
                unregulatedSelection.add("-999 - Unregulated");
            }
        }
        result.put(selectionName, unregulatedSelection);
        HashSet<String> ungroupedSelection;
        ungroupedSelection = new HashSet<String>();
        selectionName = DW_Files.sUngrouped;
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                ungroupedSelection.add("UngroupedUO - Ungrouped");
                ungroupedSelection.add("Ungrouped - UngroupedUO");
                ungroupedSelection.add("UngroupedUO - UngroupedUO");
            }
            ungroupedSelection.add("UngroupedUO - Regulated");
            ungroupedSelection.add("Ungrouped - RegulatedUO");
            ungroupedSelection.add("UngroupedUO - RegulatedUO");
            ungroupedSelection.add("UngroupedUO - Unregulated");
            ungroupedSelection.add("Ungrouped - UnregulatedUO");
            ungroupedSelection.add("UngroupedUO - UnregulatedUO");
            if (do999) {
                ungroupedSelection.add("UngroupedUO - -999");
                ungroupedSelection.add("-999 - UngroupedUO");
                ungroupedSelection.add("Ungrouped - -999UO");
                ungroupedSelection.add("-999UO - Ungrouped");
                ungroupedSelection.add("UngroupedUO - -999UO");
                ungroupedSelection.add("-999UO - UngroupedUO");
            }
        } else {
            if (sameTenancyType) {
                ungroupedSelection.add("Ungrouped - Ungrouped");
            }
            ungroupedSelection.add("Ungrouped - Regulated");
            ungroupedSelection.add("Ungrouped - Unregulated");
            if (do999) {
                ungroupedSelection.add("Ungrouped - -999");
                ungroupedSelection.add("-999 - Ungrouped");
            }
        }
        result.put(selectionName, ungroupedSelection);
        HashSet<String> allSelection;
        allSelection = new HashSet<String>();
        selectionName = "All";
        allSelection.addAll(regulatedSelection);
        allSelection.addAll(unregulatedSelection);
        allSelection.addAll(ungroupedSelection);
        result.put(selectionName, allSelection);
        return result;
    }

//    public static String getLabel(
//            String year0,
//            String month0,
//            String year,
//            String month) {
//        String result;
//        result = year0 + " " + month0 + "_" + year + " " + month;
//        // System.out.println(result);
//        return result;
//    }
    public static String getLabel(
            String yM30,
            String yM31) {
        String result;
        result = yM30 + "-" + yM31;
        //result = yM30 + "_TO_" + yM31;
        // System.out.println(result);
        return result;
    }

    /**
     * {@code
     * Object[] result;
     * result = new Object[3];
     * TreeMap<Integer, TreeMap<Integer, BigDecimal>> map;
     * map = new TreeMap<Integer, TreeMap<Integer, BigDecimal>>();
     * result[0] = map;
     * Double min = null;
     * result[1] = min;
     * Double max = null;
     * result[2] = max;
     * result[3] = size;
     * }
     *
     * @param f
     * @param timeDiff Normaliser for counts.
     * @return TenureBefore, TenureNow Counts.
     */
//    public TreeMap<String, TreeMap<String, BigDecimal>> getTenancyTypeTransitionMap(
//            File f,
//            double timeDiff) {
////        Object[] result;
////        result = new Object[4];
//        TreeMap<String, TreeMap<String, BigDecimal>> map;
//        map = new TreeMap<String, TreeMap<String, BigDecimal>>();
////        result[0] = map;
////        Double min = Double.MAX_VALUE;
////        Double max = Double.MIN_VALUE;
////        int size = 0;
//        ArrayList<String> lines = DW_Table.readCSV(f);
//        ArrayList<String> tenures;
//        tenures = new ArrayList<String>();
//        boolean mapContainsNonZeroValues = false;
//        boolean first = true;
//        Iterator<String> ite = lines.iterator();
//        while (ite.hasNext()) {
//            // Skip the first line
//            String line;
//            line = ite.next();
//            String[] split;
//            split = line.split(",");
//            if (first) {
//                for (int i = 1; i < split.length; i++) {
//                    String tenancyStart; // This is tenancyEnd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                    tenancyStart = split[i];
//                    TreeMap<String, BigDecimal> transitions;
//                    transitions = new TreeMap<String, BigDecimal>();
//                    map.put(tenancyStart, transitions);
//                    tenures.add(tenancyStart);
//                }
//                first = false;
//            } else {
//                String tenancyEnd;
//                tenancyEnd = split[0];
//                for (int i = 1; i < split.length; i++) {
//                    String tenureBefore; // This is tenancyStart!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                    tenureBefore = tenures.get(i - 1);
//                    Integer count;
//                    count = Integer.valueOf(split[i]);
//                    if (count != 0) {
//                        mapContainsNonZeroValues = true;
//                    }
//                    Double rate;
//                    rate = count / timeDiff;
//                    TreeMap<String, BigDecimal> tenancyTransition;
//                    tenancyTransition = map.get(tenureBefore);
//                    tenancyTransition.put(tenancyEnd, BigDecimal.valueOf(rate));
////                    if (tenureBefore.compareTo(tenancyEnd) != 0) {
////                        min = Math.min(min, rate);
////                        max = Math.max(max, rate);
////                        size++;
////                    }
//                }
//            }
//        }
//        if (!mapContainsNonZeroValues) {
//            return null;
//        }
////        System.out.println("max " + max);
////        System.out.println("min " + min);
////        System.out.println("size " + size);
////        result[1] = min; // This has to be here because of unboxing!?
////        result[2] = max;
////        result[3] = size;
////        return result;
//        return map;
//    }
    public TreeMap<String, TreeMap<String, BigDecimal>> getTenancyTypeTransitionMap(
            File f,
            double timeDiff) {
//        Object[] result;
//        result = new Object[4];
        TreeMap<String, TreeMap<String, BigDecimal>> map;
        map = new TreeMap<String, TreeMap<String, BigDecimal>>();
//        result[0] = map;
//        Double min = Double.MAX_VALUE;
//        Double max = Double.MIN_VALUE;
//        int size = 0;
        ArrayList<String> lines = DW_Table.readCSV(f);
        ArrayList<String> tenures;
        tenures = new ArrayList<String>();
        boolean mapContainsNonZeroValues = false;
        boolean first = true;
        Iterator<String> ite = lines.iterator();
        while (ite.hasNext()) {
            // Skip the first line
            String line;
            line = ite.next();
            String[] split;
            split = line.split(",");
            if (first) {
                for (int i = 1; i < split.length; i++) {
                    String tenancyBefore;
                    tenancyBefore = split[i];
                    TreeMap<String, BigDecimal> transitions;
                    transitions = new TreeMap<String, BigDecimal>();
                    map.put(tenancyBefore, transitions);
                    tenures.add(tenancyBefore);
                }
                first = false;
            } else {
                String tenancyNow;
                tenancyNow = split[0];
                for (int i = 1; i < split.length; i++) {
                    String tenureBefore; // This is tenancyStart!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    tenureBefore = tenures.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    if (count != 0) {
                        mapContainsNonZeroValues = true;
                    }
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<String, BigDecimal> tenancyTransition;
                    tenancyTransition = map.get(tenureBefore);
                    tenancyTransition.put(tenancyNow, BigDecimal.valueOf(rate));
//                    if (tenureBefore.compareTo(tenancyEnd) != 0) {
//                        min = Math.min(min, rate);
//                        max = Math.max(max, rate);
//                        size++;
//                    }
                }
            }
        }
        if (!mapContainsNonZeroValues) {
            return null;
        }
//        System.out.println("max " + max);
//        System.out.println("min " + min);
//        System.out.println("size " + size);
//        result[1] = min; // This has to be here because of unboxing!?
//        result[2] = max;
//        result[3] = size;
//        return result;
        return map;
    }

    /**
     * {@code
     * Object[] result;
     * result = new Object[3];
     * TreeMap<Integer, TreeMap<Integer, BigDecimal>> map;
     * map = new TreeMap<Integer, TreeMap<Integer, BigDecimal>>();
     * result[0] = map;
     * Double min = null;
     * result[1] = min;
     * Double max = null;
     * result[2] = max;
     * result[3] = size;
     * }
     *
     * @param f
     * @param timeDiff Normaliser for counts.
     * @return TenureBefore, TenureNow Counts.
     */
    public TreeMap<String, TreeMap<String, BigDecimal>> getTenancyTypeTransitionMapGrouped(
            File f,
            double timeDiff) {
//        Object[] result;
//        result = new Object[4];
        TreeMap<String, TreeMap<String, BigDecimal>> map;
        map = new TreeMap<String, TreeMap<String, BigDecimal>>();
//        result[0] = map;
//        Double min = Double.MAX_VALUE;
//        Double max = Double.MIN_VALUE;
//        int size = 0;
        ArrayList<String> lines = DW_Table.readCSV(f);
        ArrayList<String> tenures;
        tenures = new ArrayList<String>();
        boolean mapContainsNonZeroValues = false;
        boolean first = true;
        Iterator<String> ite = lines.iterator();
        while (ite.hasNext()) {
            // Skip the first line
            String line;
            line = ite.next();
            String[] split;
            split = line.split(",");
            if (first) {
                for (int i = 1; i < split.length; i++) {
                    String tenancyStart;
                    tenancyStart = split[i];
                    TreeMap<String, BigDecimal> transitions;
                    transitions = new TreeMap<String, BigDecimal>();
                    map.put(tenancyStart, transitions);
                    tenures.add(tenancyStart);
                }
                first = false;
            } else {
                String tenancyEnd;
                tenancyEnd = split[0];
                for (int i = 1; i < split.length; i++) {
                    String tenureBefore;
                    tenureBefore = tenures.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    if (count != 0) {
                        mapContainsNonZeroValues = true;
                    }
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<String, BigDecimal> tenancyTransition;
                    tenancyTransition = map.get(tenureBefore);
                    tenancyTransition.put(tenancyEnd, BigDecimal.valueOf(rate));
//                    if (tenureBefore.compareTo(tenancyEnd) != 0) {
//                        min = Math.min(min, rate);
//                        max = Math.max(max, rate);
//                        size++;
//                    }
                }
            }
        }
        if (!mapContainsNonZeroValues) {
            return null;
        }
//        System.out.println("max " + max);
//        System.out.println("min " + min);
//        System.out.println("size " + size);
//        result[1] = min; // This has to be here because of unboxing!?
//        result[2] = max;
//        result[3] = size;
//        return result;
        return map;
    }

    /**
     * @param bigMatrix The bigMatrix to repack and select from. Keys are x
     * values to be plot, the final values are y values, the Integer keys
     * between are row and column indexes. TenureBefore, TenureNow Counts.
     * @param selection A collection of Strings for selecting what data to get.
     * @param xAxisLabels Passed in for packaging.
     * @return {
     * @code
     * Object[] result;
     * result = new Object[7];
     * result[0] = maps; TreeMap<String, TreeMap<BigDecimal, BigDecimal>> // label, x, y maps
     * result[1] = newMinY; // BigDecimal minimum y value in selection.
     * result[2] = newMaxY; // BigDecimal maximum y value in selection.
     * result[3] = newMinX; // BigDecimal minimum x value in selection.
     * result[4] = newMaxX; // BigDecimal maximum x value in selection.
     * result[5] = labels; // collection of strings: row index " - " column index
     * result[6] = xAxisLabels;
     * result[7] = TreeMap<String, Boolean> // label, non-zero values
     * result[7] = TreeMap<String, Boolean> // labels, non-zero values
     */
    private Object[] getData(
            TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>> bigMatrix,
            HashSet<String> selection,
            TreeMap<BigDecimal, String> xAxisLabels) {
        Object[] result;
        result = new Object[9];
        TreeMap<String, TreeMap<BigDecimal, BigDecimal>> maps;
        maps = new TreeMap<String, TreeMap<BigDecimal, BigDecimal>>();
        BigDecimal newMinX;
        newMinX = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal newMaxX;
        newMaxX = BigDecimal.valueOf(Double.MIN_VALUE);
        BigDecimal newMinY;
        newMinY = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal newMaxY;
        newMaxY = BigDecimal.valueOf(Double.MIN_VALUE);
        // Declare nonZero
        TreeMap<String, Boolean> nonZero;
        nonZero = new TreeMap<String, Boolean>();
        Iterator<BigDecimal> ite;
        ite = bigMatrix.keySet().iterator();
        while (ite.hasNext()) {
            BigDecimal x = ite.next();
            TreeMap<String, TreeMap<String, BigDecimal>> iomatrix;
            iomatrix = bigMatrix.get(x);
            Iterator<String> ite2;
            ite2 = iomatrix.keySet().iterator();
            while (ite2.hasNext()) {
                String tenureBefore = ite2.next(); // This is Before!!!!!!!!!!!!
                TreeMap<String, BigDecimal> omatrix;
                omatrix = iomatrix.get(tenureBefore);
                Iterator<String> ite3;
                ite3 = omatrix.keySet().iterator();
                while (ite3.hasNext()) {
                    String tenureNow = ite3.next(); // This is now!!!!!!!!!1
                    if (!tenureNow.equalsIgnoreCase(tenureBefore)) {
                        String key;
                        key = "" + tenureBefore + " - " + tenureNow;
                        // init
                        Boolean b;
                        b = nonZero.get(key);
                        if (b == null) {
                            nonZero.put(key, false);
                        }
                        if (selection.contains(key)) {
                            TreeMap<BigDecimal, BigDecimal> map;
                            map = maps.get(key);
                            if (map == null) {
                                map = new TreeMap<BigDecimal, BigDecimal>();
                                maps.put(key, map);
                            }
                            BigDecimal y;
                            y = omatrix.get(tenureNow);
                            // set non-zero
                            if (y.compareTo(BigDecimal.ZERO) != 0) {
                                nonZero.put(key, true);
                            }
                            // set min and max
                            newMinY = y.min(newMinY);
                            newMaxY = y.max(newMaxY);
                            newMinX = x.min(newMinX);
                            newMaxX = x.max(newMaxX);
                            // Add to map
                            map.put(x, y);
                        }
                    }
                }
            }
        }
//        BigDecimal minX = bigMatrix.firstKey();
//        BigDecimal maxX = bigMatrix.lastKey();
//        BigDecimal minX = maps.firstKey();
//        BigDecimal maxX = maps.lastKey();
        result[0] = maps;
        result[1] = newMinY;
        result[2] = newMaxY;
        result[3] = newMinX;
        result[4] = newMaxX;
        ArrayList<String> labels;
        labels = new ArrayList<String>();
//        labels.addAll(maps.keySet()); // This does not work as maps is gc resulting in labels becoming null for some reason.
        // Declare nonZero2
        TreeMap<String, Boolean> nonZero2;
        nonZero2 = new TreeMap<String, Boolean>();
        Iterator<String> iteS;
        iteS = maps.keySet().iterator();
        while (iteS.hasNext()) {
            String label;
            label = iteS.next();
            String[] tenancyTypes;
            tenancyTypes = label.split(" - ");
            String newLabel;
            newLabel = DW_SHBE_TenancyType_Handler.getTenancyTypeName(tenancyTypes[0]);
            newLabel += " TO ";
            newLabel += DW_SHBE_TenancyType_Handler.getTenancyTypeName(tenancyTypes[1]);
            labels.add(newLabel);
            nonZero2.put(newLabel, nonZero.get(label));
        }
        result[5] = labels;
        result[6] = xAxisLabels;
        result[7] = nonZero;
        result[8] = nonZero2;
        return result;
    }

//    private Object[] getDataGrouped(
//            TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>> bigMatrixGrouped,
//            HashSet<String> selection,
//            TreeMap<BigDecimal, String> xAxisLabels) {
//        Object[] result;
//        result = new Object[9];
//        TreeMap<String, TreeMap<BigDecimal, BigDecimal>> maps;
//        maps = new TreeMap<String, TreeMap<BigDecimal, BigDecimal>>();
//        BigDecimal newMinX;
//        newMinX = BigDecimal.valueOf(Double.MAX_VALUE);
//        BigDecimal newMaxX;
//        newMaxX = BigDecimal.valueOf(Double.MIN_VALUE);
//        BigDecimal newMinY;
//        newMinY = BigDecimal.valueOf(Double.MAX_VALUE);
//        BigDecimal newMaxY;
//        newMaxY = BigDecimal.valueOf(Double.MIN_VALUE);
//        // Declare nonZero
//        TreeMap<String, Boolean> nonZero;
//        nonZero = new TreeMap<String, Boolean>();
//        Iterator<BigDecimal> ite;
//        ite = bigMatrixGrouped.keySet().iterator();
//        while (ite.hasNext()) {
//            BigDecimal x = ite.next();
//            TreeMap<String, TreeMap<String, BigDecimal>> iomatrix;
//            iomatrix = bigMatrixGrouped.get(x);
//            Iterator<String> ite2;
//            ite2 = iomatrix.keySet().iterator();
//            while (ite2.hasNext()) {
//                String tenureBefore = ite2.next();
//                TreeMap<String, BigDecimal> omatrix;
//                omatrix = iomatrix.get(tenureBefore);
//                Iterator<String> ite3;
//                ite3 = omatrix.keySet().iterator();
//                while (ite3.hasNext()) {
//                    String tenureNow = ite3.next();
//                    if (!tenureNow.equalsIgnoreCase(tenureBefore)) {
//                        String key;
//                        key = "" + tenureBefore + " - " + tenureNow;
//                        // init
//                        Boolean b;
//                        b = nonZero.get(key);
//                        if (b == null) {
//                            nonZero.put(key, false);
//                        }
//                        if (selection.contains(key)) {
//                            TreeMap<BigDecimal, BigDecimal> map;
//                            map = maps.get(key);
//                            if (map == null) {
//                                map = new TreeMap<BigDecimal, BigDecimal>();
//                                maps.put(key, map);
//                            }
//                            BigDecimal y;
//                            y = omatrix.get(tenureNow);
//                            // set non-zero
//                            if (y.compareTo(BigDecimal.ZERO) != 0) {
//                                nonZero.put(key, true);
//                            }
//                            // set min and max
//                            newMinY = y.min(newMinY);
//                            newMaxY = y.max(newMaxY);
//                            newMinX = x.min(newMinX);
//                            newMaxX = x.max(newMaxX);
//                            // add to map
//                            map.put(x, y);
//                        }
//                    }
//                }
//            }
//        }
////        BigDecimal minX = bigMatrix.firstKey();
////        BigDecimal maxX = bigMatrix.lastKey();
////        BigDecimal minX = maps.firstKey();
////        BigDecimal maxX = maps.lastKey();
//        result[0] = maps;
//        result[1] = newMinY;
//        result[2] = newMaxY;
//        result[3] = newMinX;
//        result[4] = newMaxX;
//        // Declare nonZero2
//        TreeMap<String, Boolean> nonZero2;
//        nonZero2 = new TreeMap<String, Boolean>();
//        ArrayList<String> labels;
//        labels = new ArrayList<String>();
////        labels.addAll(maps.keySet()); // This does not work as maps is gc resulting in labels becoming null for some reason.
//        Iterator<String> iteS;
//        iteS = maps.keySet().iterator();
//        while (iteS.hasNext()) {
//            String label;
//            label = iteS.next();
//            String[] tenancyTypes;
//            //tenancyTypes = label.split(" - ");
//            tenancyTypes = label.split(" - ");
//            String newLabel;
//            newLabel = DW_SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[0]));
//            newLabel += " TO ";
//            newLabel += DW_SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[1]));
//            labels.add(newLabel);
//            nonZero2.put(newLabel, nonZero.get(label));
//        }
//        result[5] = labels;
//        result[6] = xAxisLabels;
//        result[7] = nonZero;
//        result[8] = nonZero2;
//        return result;
//    }
}
