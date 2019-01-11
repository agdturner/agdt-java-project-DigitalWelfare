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
import java.util.Set;
//import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import uk.ac.leeds.ccg.andyt.chart.Generic_LineGraph;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.core.SHBE_ID;
//import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.execution.Generic_Execution;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_LineGraph extends Generic_LineGraph {

    protected transient DW_Environment Env;

    protected DW_Files Files;
    protected DW_Strings Strings;
    protected SHBE_Handler SHBE_Handler;
    protected DW_UO_Handler UO_Handler;
    protected DW_UO_Data UO_Data;

    HashMap<String, HashSet<String>> AreaCodes;

    public DW_LineGraph(DW_Environment de) {
        this.Env = de;
        this.Files = de.getFiles();
        this.Strings = de.getStrings();
        this.SHBE_Handler = de.getSHBE_Handler();
        this.UO_Data = de.getUO_Data();
        this.UO_Handler = de.getUO_Handler();
    }

    public DW_LineGraph(
            DW_Environment de,
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
        this.Env = de;
        this.Files = de.getFiles();
        this.Strings = de.getStrings();
        this.SHBE_Handler = de.getSHBE_Handler();
        this.UO_Data = de.getUO_Data();
        this.UO_Handler = de.getUO_Handler();
    }

    HashSet<Future> futures;
    String format;
    ArrayList<Boolean> b;
    String[] SHBEFilenames;
    TreeMap<String, ArrayList<Integer>> includes;
    ArrayList<String> PTs;

    /**
     *
     * @param doGraphTenancyTypeTransitions
     * @param doGraphAggregateData
     */
    public void run(
            boolean doGraphTenancyTypeTransitions,
            boolean doGraphAggregateData) {
        Env.log("<run>");
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
        SHBEFilenames = SHBE_Handler.getSHBEFilenamesAll();
//        ArrayList<String> claimantTypes;
//        claimantTypes = DW_Strings.getHB_CTB();

        includes = SHBE_Handler.getIncludes();
        includes.remove(Strings.SHBE_Strings.sIncludeAll);
        includes.remove(Strings.SHBE_Strings.sIncludeYearly);
        includes.remove(Strings.SHBE_Strings.sInclude6Monthly);
        includes.remove(Strings.SHBE_Strings.sInclude3Monthly);
        includes.remove(Strings.SHBE_Strings.sIncludeMonthly);
//        includes.remove(DW_Strings.SHBE_Strings.sIncludeMonthlySinceApril2013);
        includes.remove(Strings.SHBE_Strings.sInclude2MonthlySinceApril2013Offset0);
        includes.remove(Strings.SHBE_Strings.sInclude2MonthlySinceApril2013Offset1);
        includes.remove(Strings.SHBE_Strings.sIncludeStartEndSinceApril2013);
        includes.remove(Strings.SHBE_Strings.sIncludeApril2013May2013);

        futures = new HashSet<>();

        format = "PNG";

//        ArrayList<String> types;
//        types = new ArrayList<String>();
//        types.add(DW_Files.sPostcodeChanged);
//        types.add(sPostcodeChangedNo);
//        types.add("Multiple");
//        types.add("");
        PTs = Strings.SHBE_Strings.getPaymentTypes();
//        PTs.remove(SHBE_Handler.sPaymentTypeAll);
        PTs.remove(Strings.SHBE_Strings.sPaymentTypeIn);
        PTs.remove(Strings.SHBE_Strings.sPaymentTypeSuspended);
        PTs.remove(Strings.SHBE_Strings.sPaymentTypeOther);

        b = new ArrayList<>();
        b.add(true);
        b.add(false);

//        if (doGraphTenancyTypeTransitions) {
//            graphTenancyTypeTransitions();
//        }

        if (doGraphAggregateData) {
            graphAggregateData();
        }
    }

    protected void graphAggregateData() {

        ArrayList<String> AZs; // AggregatedZones
        AZs = new ArrayList<>();
//        AZs.add(DW_Strings.sParliamentaryConstituency);
        AZs.add(Strings.sStatisticalWard);
//        AZs.add(DW_Strings.sLSOA);
//        AZs.add(DW_Strings.sMSOA);

        Iterator<String> PTsIte;
        PTsIte = PTs.iterator();
        while (PTsIte.hasNext()) {
            String PT;
            PT = PTsIte.next();
            File dirIn;
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    Strings.sHBGeneralAggregateStatistics);
            dirIn = new File(
                    dirIn,
                    PT);
            File dirOut;
            dirOut = new File(
                    Files.getOutputSHBEPlotsDir(),
                    Strings.sHBGeneralAggregateStatistics);
            dirOut = new File(
                    dirOut,
                    PT);

            Iterator<String> includesIte;
            includesIte = includes.keySet().iterator();
            String includeName;
            ArrayList<Integer> include;
            while (includesIte.hasNext()) {
                includeName = includesIte.next();
                include = includes.get(includeName);
                Iterator<String> AZsIte;
                AZsIte = AZs.iterator();
                while (AZsIte.hasNext()) {
                    String AZ;
                    AZ = AZsIte.next();
                    File dirIn1;
                    dirIn1 = new File(
                            dirIn,
                            AZ);
                    File dirOut1;
                    dirOut1 = new File(
                            dirOut,
                            AZ);
                    dirOut1 = new File(
                            dirOut1,
                            includeName);
                    //dirOut1.mkdirs();
                    File fout;
                    for (int stat = 1; stat <= 8; stat++) {
                        String filename;
                        String yAxisLabel;
                        String title;
                        switch (stat) {
                            case 1:
                                filename = "NumberOfHBClaims.PNG";
                                yAxisLabel = "Number Of Housing Benefit Claims";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 2:
                                filename = "NumberOfChildDependentsInHBClaimingHouseholds.PNG";
                                yAxisLabel = "Number Of Child Dependents In HB Claiming Households";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 3:
                                filename = "TotalPopulationInHBClaimingHouseholds.PNG";
                                yAxisLabel = "Total Population In HB Claiming Households";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 4:
                                filename = "NumberOfUOHouseholdsRequiring1Room.PNG";
                                yAxisLabel = "Number Of UO Households Requiring 1 Room";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 5:
                                filename = "NumberOfUOHouseholdsRequiring2Rooms.PNG";
                                yAxisLabel = "Number Of UO Households Requiring 2 Rooms";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 6:
                                filename = "NumberOfUOHouseholdsRequiring3Rooms.PNG";
                                yAxisLabel = "Number Of UO Households Requiring 3 Rooms";
                                title = yAxisLabel + " Over Time";
                                break;
                            case 7:
                                filename = "NumberOfUOHouseholdsRequiring4Rooms.PNG";
                                yAxisLabel = "Number Of UO Households Requiring 4 Rooms";
                                title = yAxisLabel + " Over Time";
                                break;
                            default:
                                filename = "NumberOfUOHouseholdsRequiring5OrMoreRooms.PNG";
                                yAxisLabel = "Number Of UO Households Requiring 5 Or More Rooms";
                                title = yAxisLabel + " Over Time";
                                break;
                        }
                        fout = new File(
                                dirOut1,
                                filename);
                        TreeMap<String, TreeMap<ONSPD_YM3, BigDecimal>> data0;
                        data0 = new TreeMap<String, TreeMap<ONSPD_YM3, BigDecimal>>();
                        Iterator<Integer> includeIte;
                        includeIte = include.iterator();
                        while (includeIte.hasNext()) {
                            int i = includeIte.next();
                            ONSPD_YM3 YM3;
                            YM3 = SHBE_Handler.getYM3(SHBEFilenames[i]);
                            File f = new File(
                                    dirIn1,
                                    YM3 + ".csv");
                            // readCSV
                            ArrayList<String> lines;
                            lines = DW_Table.readCSV(f);
                            Iterator<String> ite;
                            ite = lines.iterator();
                            String line;
                            String[] fields;
                            String header;
                            header = ite.next();
                            String[] headerFields;
                            headerFields = header.split(Strings.special_commaSpace);
                            /*
                             * AreaCode, NumberOfHBClaims, 
                             * NumberOfChildDependentsInHBClaimingHouseholds, 
                             * TotalPopulationInHBClaimingHouseholds,
                             * NumberOfUOHouseholdsRequiring1Room,
                             * NumberOfUOHouseholdsRequiring2Rooms,
                             * NumberOfUOHouseholdsRequiring3Rooms,
                             * NumberOfUOHouseholdsRequiring4Rooms,
                             * NumberOfUOHouseholdsRequiring5OrMoreRooms
                             */
                            while (ite.hasNext()) {
                                line = ite.next();
                                //System.out.println(line);
                                fields = line.split(Strings.special_commaSpace);
                                TreeMap<ONSPD_YM3, BigDecimal> dateValue;
                                dateValue = data0.get(fields[0]);
                                if (dateValue == null) {
                                    dateValue = new TreeMap<ONSPD_YM3, BigDecimal>();
                                    data0.put(fields[0], dateValue);
                                }
                                dateValue.put(YM3, new BigDecimal(fields[stat]));
                            }
                        }
                        setyAxisLabel(yAxisLabel);
                        DW_LineGraph chart = new DW_LineGraph(
                                Env,
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
                        //
                        Object[] TreeMapDateLabelSHBEFilename;
                        TreeMapDateLabelSHBEFilename = SHBE_Handler.getTreeMapDateLabelSHBEFilenamesSingle(
                                SHBEFilenames,
                                include);
                        TreeMap<BigDecimal, ONSPD_YM3> xAxisLabels;
                        xAxisLabels = (TreeMap<BigDecimal, ONSPD_YM3>) TreeMapDateLabelSHBEFilename[0];
                        TreeMap<ONSPD_YM3, BigDecimal> fileLabelValue;
                        fileLabelValue = (TreeMap<ONSPD_YM3, BigDecimal>) TreeMapDateLabelSHBEFilename[1];
                        Object[] data;
                        data = getData(
                                data0,
                                xAxisLabels,
                                fileLabelValue);
//                    HashSet<String> selection = allSelections.get(selections);
//                    data = getData(
//                            bigMatrix,
//                            selection,
//                            xAxisLabels);
                        if (data != null) {
                            chart.setData(data);
                            chart.run();
                            future = chart.future;
                            futures.add(future);
                        } else {
                            futures.add(chart.future);
                        }
                    }
                }
            }
        }
    }

    protected void graphTenancyTypeTransitions() {
        ArrayList<String> month3Letters;
        month3Letters = Generic_Time.getMonths3Letters();

        // Init allSelections of different types of tenancy type transitions to graph.
        HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>> allSelections;
        allSelections = new HashMap<>();
        // Init allSelections of different types of tenancy type transitions to graph.
        HashMap<Boolean, HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>>> allSelectionsGrouped;
        allSelectionsGrouped = new HashMap<>();

        Iterator<Boolean> iteB;
        iteB = b.iterator();
        while (iteB.hasNext()) {
            boolean do999;
            do999 = iteB.next();
            HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>> asss;
            asss = new HashMap<>();
            HashMap<Boolean, HashMap<Boolean, TreeMap<String, HashSet<String>>>> asssg;
            asssg = new HashMap<>();
            Iterator<Boolean> iteB2;
            iteB2 = b.iterator();
            while (iteB2.hasNext()) {
                boolean doUnderOccupancy;
                doUnderOccupancy = iteB2.next();
                HashMap<Boolean, TreeMap<String, HashSet<String>>> ass;
                ass = new HashMap<>();
                HashMap<Boolean, TreeMap<String, HashSet<String>>> assg;
                assg = new HashMap<>();
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

        Set<SHBE_ID> UOApril2013ClaimIDs;
        UOApril2013ClaimIDs = UO_Handler.getUOStartApril2013ClaimIDs(UO_Data);

        boolean CheckPreviousTenancyType;
////            checkPreviousTenure = false;
//            checkPreviousTenure = false;
        iteB = b.iterator();
        while (iteB.hasNext()) {
            CheckPreviousTenancyType = iteB.next();
            Env.log("CheckPreviousTenancyType " + CheckPreviousTenancyType);

            File dirIn;
            dirIn = Files.getOutputSHBETablesTenancyTypeTransitionDir(
                    //DW_Strings.sPaymentTypeAll,
                    CheckPreviousTenancyType);
            File dirOut;
            dirOut = Files.getOutputSHBEPlotsTenancyTypeTransitionDir(
                    //DW_Strings.sAll,
                    CheckPreviousTenancyType);

            boolean tenancyOnly;
//            tenancyOnly = false; // Switch for testing.
            Iterator<Boolean> iteB1;
            iteB1 = b.iterator();
            while (iteB1.hasNext()) {
                tenancyOnly = iteB1.next();

                if (tenancyOnly) {
                    File dirIn2 = new File(
                            dirIn,
                            Strings.sWithOrWithoutPostcodeChange);
                    File dirOut2 = new File(
                            dirOut,
                            Strings.sWithOrWithoutPostcodeChange);
                    Iterator<Boolean> iteB2;
                    iteB2 = b.iterator();

                    boolean doUnderOccupancyData;
//                    doUnderOccupancyData = false; // Switch for testing.

                    Iterator<String> includesIte;
                    includesIte = includes.keySet().iterator();
                    while (includesIte.hasNext()) {
                        String includeKey;
                        includeKey = includesIte.next();
                        ArrayList<Integer> include;
                        include = includes.get(includeKey);
                        File dirIn3 = new File(
                                dirIn2,
                                includeKey);
                        File dirOut3 = new File(
                                dirOut2,
                                includeKey);
//fdsafds
                        /**
                         * It is thought this should be a choice between doing
                         * All or doing only those that were UO in April 2013.
                         * This needs checking!
                         */
                        dirIn3 = new File(
                                dirIn3,
                                Strings.sAll);
                        dirOut3 = new File(
                                dirOut2,
                                Strings.sAll);

                        while (iteB2.hasNext()) {
                            doUnderOccupancyData = iteB2.next();
                            if (doUnderOccupancyData) {

//                                boolean DoUOOnlyOnThoseOriginallyUO;
//                                env.logO("<DoUOOnlyOnThoseOriginallyUO " + DoUOOnlyOnThoseOriginallyUO + ">", true);
//                                            Set<SHBE_ID> UOApril2013ClaimIDsDummy;
//                                            if (DoUOOnlyOnThoseOriginallyUO) {
//                                                UOApril2013ClaimIDsDummy = UOApril2013ClaimIDs;
//                                            } else {
//                                                UOApril2013ClaimIDsDummy = null;
//                                            }
                                File dirIn4 = new File(
                                        dirIn3,
                                        Strings.sU);
                                File dirOut4 = new File(
                                        dirOut3,
                                        Strings.sU);
                                File dirIn5;
                                File dirOut5;
                                boolean doAll = true;
                                if (doAll) {
                                    dirIn5 = new File(
                                            dirIn4,
                                            Strings.sB);
                                    dirOut5 = new File(
                                            dirOut4,
                                            Strings.sB);
                                    Iterator<Boolean> iteB4;
                                    iteB4 = b.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean do999 = iteB4.next();
                                        File dirOut6;
                                        if (do999) {
                                            dirOut6 = new File(
                                                    dirOut5,
                                                    Strings.sInclude999);
                                            Iterator<Boolean> iteB5;
                                            iteB5 = b.iterator();
                                            while (iteB5.hasNext()) {
                                                boolean doSameTenancy;
                                                doSameTenancy = iteB5.next();
                                                File dirOut7;
                                                if (doSameTenancy) {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.SHBE_Strings.sIncludeSameTenancy);
                                                } else {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                }
                                                Iterator<Boolean> iteB6;
                                                iteB6 = b.iterator();
                                                while (iteB6.hasNext()) {
                                                    boolean grouped;
                                                    grouped = iteB6.next();
                                                    if (grouped) {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                include,
                                                                includeKey,
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
                                                                include,
                                                                includeKey,
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
                                            dirOut6 = new File(
                                                    dirOut5,
                                                    Strings.sExclude999);
                                            Iterator<Boolean> iteB5;
                                            iteB5 = b.iterator();
                                            while (iteB5.hasNext()) {
                                                boolean doSameTenancy;
                                                doSameTenancy = iteB5.next();
                                                File dirOut7;
                                                if (doSameTenancy) {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.SHBE_Strings.sIncludeSameTenancy);
                                                } else {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                }
                                                Iterator<Boolean> iteB6;
                                                iteB6 = b.iterator();
                                                while (iteB6.hasNext()) {
                                                    boolean grouped;
                                                    grouped = iteB6.next();
                                                    if (grouped) {
                                                        doSumat(
                                                                dirIn5,
                                                                dirOut7,
                                                                include,
                                                                includeKey,
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
                                                                include,
                                                                includeKey,
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
                                boolean doCouncilRSL = false;
                                if (doCouncilRSL) {
                                    Iterator<Boolean> iteB3;
                                    iteB3 = b.iterator();
                                    while (iteB3.hasNext()) {
                                        boolean doCouncil;
                                        doCouncil = iteB3.next();
                                        if (doCouncil) {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sCouncil);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sCouncil);
                                        } else {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sRSL);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sRSL);
                                        }
                                        Iterator<Boolean> iteB4;
                                        iteB4 = b.iterator();
                                        while (iteB4.hasNext()) {
                                            boolean do999 = iteB4.next();
                                            File dirOut6;
                                            if (do999) {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sInclude999);
                                                Iterator<Boolean> iteB5;
                                                iteB5 = b.iterator();
                                                while (iteB5.hasNext()) {
                                                    boolean doSameTenancy;
                                                    doSameTenancy = iteB5.next();
                                                    File dirOut7;
                                                    if (doSameTenancy) {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                Strings.SHBE_Strings.sIncludeSameTenancy);
                                                    } else {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                    }
                                                    Iterator<Boolean> iteB6;
                                                    iteB6 = b.iterator();
                                                    while (iteB6.hasNext()) {
                                                        boolean grouped;
                                                        grouped = iteB6.next();
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn5,
                                                                    dirOut7,
                                                                    include,
                                                                    includeKey,
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
                                                                    include,
                                                                    includeKey,
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
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sExclude999);
                                                Iterator<Boolean> iteB5;
                                                iteB5 = b.iterator();
                                                while (iteB5.hasNext()) {
                                                    boolean doSameTenancy;
                                                    doSameTenancy = iteB5.next();
                                                    File dirOut7;
                                                    if (doSameTenancy) {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                Strings.SHBE_Strings.sIncludeSameTenancy);
                                                    } else {
                                                        dirOut7 = new File(
                                                                dirOut6,
                                                                Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                    }
                                                    Iterator<Boolean> iteB6;
                                                    iteB6 = b.iterator();
                                                    while (iteB6.hasNext()) {
                                                        boolean grouped;
                                                        grouped = iteB6.next();
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn5,
                                                                    dirOut7,
                                                                    include,
                                                                    includeKey,
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
                                                                    include,
                                                                    includeKey,
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
                                File dirIn4 = new File(
                                        dirIn3,
                                        Strings.sU);
                                File dirOut4 = new File(
                                        dirOut3,
                                        Strings.sU);
                                boolean doBoth = true;
                                if (doBoth) {
                                    File dirIn5 = new File(
                                            dirIn4,
                                            Strings.sB); // DW_Strings.sAll
                                    File dirOut5 = new File(
                                            dirOut4,
                                            Strings.sB);
                                    Iterator<Boolean> iteB3;
                                    iteB3 = b.iterator();
                                    while (iteB3.hasNext()) {
                                        boolean do999 = iteB3.next();
                                        File dirOut6;
                                        if (do999) {
                                            dirOut6 = new File(
                                                    dirOut5,
                                                    Strings.sInclude999);
                                            Iterator<Boolean> iteB4;
                                            iteB4 = b.iterator();
                                            while (iteB4.hasNext()) {
                                                boolean grouped;
                                                grouped = iteB4.next();
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
                                                            allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut4,
                                                            include,
                                                            includeKey,
                                                            allSelections.get(false).get(do999).get(doUnderOccupancyData),
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
                                                    Strings.sExclude999);
                                            Iterator<Boolean> iteB4;
                                            iteB4 = b.iterator();
                                            while (iteB4.hasNext()) {
                                                boolean grouped;
                                                grouped = iteB4.next();
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
                                                            allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
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
                } else {
                    File dirIn2;
                    dirIn2 = new File(
                            dirIn,
                            Strings.sTenancyAndPostcodeChanges);
                    File dirOut2;
                    dirOut2 = new File(
                            dirOut,
                            Strings.sTenancyAndPostcodeChanges);
                    Iterator<String> includesIte;
                    includesIte = includes.keySet().iterator();
                    while (includesIte.hasNext()) {
                        String includeKey;
                        includeKey = includesIte.next();
                        ArrayList<Integer> include;
                        include = includes.get(includeKey);
                        File dirIn3;
                        dirIn3 = new File(
                                dirIn2,
                                includeKey);
                        File dirOut3;
                        dirOut3 = new File(
                                dirOut2,
                                includeKey);
                        //fdsaf
                        /**
                         * It is thought this should be a choice between doing
                         * All or doing only those that were UO in April 2013.
                         * This needs checking!
                         */
                        dirIn3 = new File(
                                dirIn3,
                                Strings.sAll);
                        dirOut3 = new File(
                                dirOut2,
                                Strings.sAll);
                        boolean doUnderOccupancyData;
//                    doUnderOccupancyData = false;
                        Iterator<Boolean> iteB2;
                        iteB2 = b.iterator();
                        while (iteB2.hasNext()) {
                            doUnderOccupancyData = iteB2.next();
                            if (doUnderOccupancyData) {
                                File dirIn4;
                                File dirOut4;
                                dirIn4 = new File(
                                        dirIn3,
                                        Strings.sU);
                                dirOut4 = new File(
                                        dirOut3,
                                        Strings.sU);
                                boolean doBoth;
                                doBoth = true;
                                if (doBoth) {
                                    File dirIn5;
                                    File dirOut5;
                                    dirIn5 = new File(
                                            dirIn4,
                                            Strings.sB);
                                    dirOut5 = new File(
                                            dirOut4,
                                            Strings.sB);
//                                Iterator<String> includesIte;
//                                includesIte = includes.keySet().iterator();
//                                while (includesIte.hasNext()) {
//                                    String include;
//                                    include = includesIte.next();
//                                    File dirIn5;
//                                    File dirOut5;
//                                    dirIn5 = new File(
//                                            dirIn4,
//                                            include);
//                                    dirOut5 = new File(
//                                            dirOut4,
//                                            include);
//                                }

                                    Iterator<Boolean> iteB4;
                                    iteB4 = b.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean grouped;
                                        grouped = iteB4.next();
                                        Iterator<Boolean> iteB5;
                                        iteB5 = b.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean postcodeChanged;
                                            postcodeChanged = iteB5.next();
                                            File dirIn6;
                                            File dirOut6;
                                            if (postcodeChanged) {
                                                dirIn6 = new File(
                                                        dirIn5,
                                                        Strings.sPostcodeChanged);
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sPostcodeChanged);
                                            } else {
                                                dirIn6 = new File(
                                                        dirIn5,
                                                        Strings.sPostcodeChangedNo);
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sPostcodeChangedNo);
                                            }
                                            Iterator<Boolean> iteB6;
                                            iteB6 = b.iterator();
                                            while (iteB6.hasNext()) {
                                                boolean do999 = iteB6.next();
                                                File dirOut7;
                                                if (do999) {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.sInclude999);
                                                    Iterator<Boolean> iteB7;
                                                    iteB7 = b.iterator();
                                                    while (iteB7.hasNext()) {
                                                        boolean doSameTenancy;
                                                        doSameTenancy = iteB7.next();
                                                        File dirOut8;
                                                        if (doSameTenancy) {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sIncludeSameTenancy);
                                                        } else {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                        }
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        } else {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        }
                                                    }
                                                } else {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.sExclude999);
                                                    Iterator<Boolean> iteB7;
                                                    iteB7 = b.iterator();
                                                    while (iteB7.hasNext()) {
                                                        boolean doSameTenancy;
                                                        doSameTenancy = iteB7.next();
                                                        File dirOut8;
                                                        if (doSameTenancy) {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sIncludeSameTenancy);
                                                        } else {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                        }
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        } else {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
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

                                Iterator<Boolean> iteB3;
                                iteB3 = b.iterator();
                                while (iteB3.hasNext()) {
                                    boolean doCouncil;
                                    doCouncil = iteB3.next();
                                    if (doCouncil) {
                                        dirIn4 = new File(
                                                dirIn3,
                                                Strings.sCouncil);
                                        dirOut4 = new File(
                                                dirOut3,
                                                Strings.sCouncil);
                                    } else {
                                        dirIn4 = new File(
                                                dirIn3,
                                                Strings.sRSL);
                                        dirOut4 = new File(
                                                dirOut3,
                                                Strings.sRSL);
                                    }
                                    Iterator<Boolean> iteB4;
                                    iteB4 = b.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean grouped;
                                        grouped = iteB4.next();
                                        File dirIn5;
                                        File dirOut5;
                                        if (grouped) {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sGrouped);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sGrouped);
                                        } else {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sGroupedNo);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sGroupedNo);
                                        }
                                        Iterator<Boolean> iteB5;
                                        iteB5 = b.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean postcodeChanged;
                                            postcodeChanged = iteB5.next();
                                            File dirIn6;
                                            File dirOut6;
                                            if (postcodeChanged) {
                                                dirIn6 = new File(
                                                        dirIn5,
                                                        Strings.sPostcodeChanged);
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sPostcodeChanged);
                                            } else {
                                                dirIn6 = new File(
                                                        dirIn5,
                                                        Strings.sPostcodeChangedNo);
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sPostcodeChangedNo);
                                            }
                                            Iterator<Boolean> iteB6;
                                            iteB6 = b.iterator();
                                            while (iteB6.hasNext()) {
                                                boolean do999 = iteB6.next();
                                                File dirOut7;
                                                if (do999) {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.sInclude999);
                                                    Iterator<Boolean> iteB7;
                                                    iteB7 = b.iterator();
                                                    while (iteB7.hasNext()) {
                                                        boolean doSameTenancy;
                                                        doSameTenancy = iteB7.next();
                                                        File dirOut8;
                                                        if (doSameTenancy) {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sIncludeSameTenancy);
                                                        } else {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                        }
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        } else {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelections.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        }
                                                    }
                                                } else {
                                                    dirOut7 = new File(
                                                            dirOut6,
                                                            Strings.sExclude999);
                                                    Iterator<Boolean> iteB7;
                                                    iteB7 = b.iterator();
                                                    while (iteB7.hasNext()) {
                                                        boolean doSameTenancy;
                                                        doSameTenancy = iteB7.next();
                                                        File dirOut8;
                                                        if (doSameTenancy) {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sIncludeSameTenancy);
                                                        } else {
                                                            dirOut8 = new File(
                                                                    dirOut7,
                                                                    Strings.SHBE_Strings.sNotIncludeSameTenancy);
                                                        }
                                                        if (grouped) {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
                                                                    allSelectionsGrouped.get(doSameTenancy).get(do999).get(doUnderOccupancyData),
                                                                    futures,
                                                                    format,
                                                                    SHBEFilenames,
                                                                    grouped,
                                                                    month3Letters);
                                                        } else {
                                                            doSumat(
                                                                    dirIn6,
                                                                    dirOut8,
                                                                    include,
                                                                    includeKey,
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
                                File dirIn4;
                                File dirOut4;
                                dirIn4 = new File(
                                        dirIn3,
                                        Strings.sAll);
                                dirOut4 = new File(
                                        dirOut3,
                                        Strings.sAll);
                                Iterator<Boolean> iteB3;
                                iteB3 = b.iterator();
                                while (iteB3.hasNext()) {
                                    boolean grouped;
                                    grouped = iteB3.next();
                                    Iterator<Boolean> iteB4;
                                    iteB4 = b.iterator();
                                    while (iteB4.hasNext()) {
                                        boolean postcodeChanged;
                                        postcodeChanged = iteB4.next();
                                        File dirIn5;
                                        File dirOut5;
                                        if (postcodeChanged) {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sPostcodeChanged);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sPostcodeChanged);
                                        } else {
                                            dirIn5 = new File(
                                                    dirIn4,
                                                    Strings.sPostcodeChangedNo);
                                            dirOut5 = new File(
                                                    dirOut4,
                                                    Strings.sPostcodeChangedNo);
                                        }
                                        Iterator<Boolean> iteB5;
                                        iteB5 = b.iterator();
                                        while (iteB5.hasNext()) {
                                            boolean do999 = iteB5.next();
                                            File dirOut6;
                                            if (do999) {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sInclude999);
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
                                                            allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
                                                            allSelections.get(false).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                }
                                            } else {
                                                dirOut6 = new File(
                                                        dirOut5,
                                                        Strings.sExclude999);
                                                if (grouped) {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
                                                            allSelectionsGrouped.get(false).get(do999).get(doUnderOccupancyData),
                                                            futures,
                                                            format,
                                                            SHBEFilenames,
                                                            grouped,
                                                            month3Letters);
                                                } else {
                                                    doSumat(
                                                            dirIn5,
                                                            dirOut6,
                                                            include,
                                                            includeKey,
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
        }
        Generic_Execution.shutdownExecutorService(
                executorService, futures,
                this);
        Env.log("</run>");
    }

    private void doSumat(
            File dirIn,
            File dirOut,
            ArrayList<Integer> include,
            String includeKey,
            TreeMap<String, HashSet<String>> allSelections,
            HashSet<Future> futures,
            String format,
            String[] SHBEFilenames,
            boolean grouped,
            ArrayList<String> month3Letters) {
//        Iterator<String> includesIte;
//        includesIte = includes.keySet().iterator();
//        while (includesIte.hasNext()) {
//            String includeKey;
//            includeKey = includesIte.next();
//            File dirIn2;
//            dirIn2 = new File(
//                    dirIn,
//                    includeKey);
////            doneFirsts.put(includeKey, false);
//            ArrayList<Integer> include;
//            include = includes.get(includeKey);
        Object[] treeMapDateLabelSHBEFilename;
        treeMapDateLabelSHBEFilename = SHBE_Handler.getTreeMapDateLabelSHBEFilenames(
                SHBEFilenames,
                include);
        TreeMap<BigDecimal, String> xAxisLabels;
        xAxisLabels = (TreeMap<BigDecimal, String>) treeMapDateLabelSHBEFilename[0];
        TreeMap<String, BigDecimal> fileLabelValue;
        fileLabelValue = (TreeMap<String, BigDecimal>) treeMapDateLabelSHBEFilename[1];
        TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>> bigMatrix;
        bigMatrix = new TreeMap<>();
//            int size = Integer.MIN_VALUE;
//            Double min = Double.MAX_VALUE;
//            Double max = Double.MIN_VALUE;
        boolean doneFirst;
        doneFirst = false;

        ONSPD_YM3 YM30 = null;
        String yM30 = "";

        Iterator<Integer> includeIte;
        includeIte = include.iterator();
        while (includeIte.hasNext()) {
            int i = includeIte.next();
            if (!doneFirst) {
                if (include.contains(i - 1) && (i - 1) >= 0) {
                    String aSHBEFilename0;
                    aSHBEFilename0 = SHBEFilenames[i - 1];
                    YM30 = SHBE_Handler.getYM3(aSHBEFilename0);
                    yM30 = YM30.toString();
                    doneFirst = true;
                }
            }
            if (doneFirst) {
                String aSHBEFilename1;
                aSHBEFilename1 = SHBEFilenames[i];
                ONSPD_YM3 YM31;
                YM31 = SHBE_Handler.getYM3(aSHBEFilename1);
                String yM31;
                yM31 = YM31.toString();
                String filename;
                filename = Strings.sTenancyTypeTransition + "_Start_" + YM30 + "_End_" + YM31 + ".csv";
                if (include.contains(i)) {
                    File dirIn3;
                    double timeDiff;
                    timeDiff = Generic_Time.getMonthDiff(
                            YM30.getYear(),
                            YM31.getYear(),
                            YM30.getMonth(),
                            YM31.getMonth());
                    String label;
                    label = getLabel(yM30, yM31);
                    BigDecimal key;
                    key = fileLabelValue.get(label);
                    if (grouped) {
//                        dirIn3 = new File(
//                                dirIn,
//                                DW_Strings.sAll);
                        dirIn3 = new File(
                                dirIn,
                                Strings.sGrouped);
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
//                        dirIn3 = new File(
//                                dirIn,
//                                DW_Strings.sAll);
                        dirIn3 = new File(
                                dirIn,
                                Strings.sGroupedNo);
                        File f;
                        f = new File(
                                dirIn3,
                                filename);
                        if (f.exists()) {
                            System.out.println("Using file " + f);
                            // TenancyTypeBefore, TenancyTypeNow Counts.
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
                    YM30 = YM31;
                    yM30 = YM30.toString();
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
                        Strings.sGrouped);
            } else {
                dirOut2 = new File(
                        dirOut2,
                        Strings.sGroupedNo);
            }
            Iterator<String> allSelectionsIte;
            allSelectionsIte = allSelections.keySet().iterator();
            while (allSelectionsIte.hasNext()) {
                String selections = allSelectionsIte.next();
                //dirOut2.mkdirs(); // only make if there is result.
                File fout;
                fout = new File(
                        dirOut2,
                        Strings.sTenancyTypeTransitionLineGraph + selections + ".PNG");
                setyAxisLabel("Tenancy Changes Per Month");
                String title;
                title = "Tenancy Transition Line Graph";
                DW_LineGraph chart = new DW_LineGraph(
                        Env,
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
                    BigDecimal newMinY;
                    newMinY = (BigDecimal) data[1];
                    BigDecimal newMaxY;
                    newMaxY = (BigDecimal) data[2];
                    if (newMaxY.compareTo(new BigDecimal(Double.MIN_VALUE)) != 1) {
                        if (newMinY.compareTo(newMaxY) == 1) {
                            Env.log("newMinY.compareTo(newMaxY) == 1 so no graph is produced.");
                        } else {
                            dirOut2.mkdirs();
                            chart.setData(data);
                            //chart.run();
                            future = chart.future;
                            futures.add(future);
                        }
                    } else {
                        Env.log("No values, so no graph is produced.");
                    }
                } else {
                    //futures.add(chart.future);
                }
            }
        }
        //}
    }

    /**
     * @param doUnderOccupancy
     * @param do999
     * @param sameTenancyType
     * @return
     */
    protected TreeMap<String, HashSet<String>> getAllSelections(
            boolean doUnderOccupancy,
            boolean do999,
            boolean sameTenancyType) {
        TreeMap<String, HashSet<String>> result;
        result = new TreeMap<>();
        String selectionName;
        selectionName = Strings.sCouncil;
        HashSet<String> CouncilSelection;
        CouncilSelection = new HashSet<>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                CouncilSelection.add("1UO - 1");
                CouncilSelection.add("1 - 1UO");
                CouncilSelection.add("1UO - 1UO");
            }
            CouncilSelection.add("1UO - 2");
            CouncilSelection.add("1 - 2UO");
            CouncilSelection.add("1UO - 2UO");
            CouncilSelection.add("1UO - 3");
            CouncilSelection.add("1 - 3UO");
            CouncilSelection.add("1UO - 3UO");
            CouncilSelection.add("1UO - 4");
            CouncilSelection.add("1 - 4UO");
            CouncilSelection.add("1UO - 4UO");
            CouncilSelection.add("1UO - 5");
            CouncilSelection.add("1 - 5UO");
            CouncilSelection.add("1UO - 5UO");
            CouncilSelection.add("1UO - 6");
            CouncilSelection.add("1 - 6UO");
            CouncilSelection.add("1UO - 6UO");
            CouncilSelection.add("1UO - 7");
            CouncilSelection.add("1 - 7UO");
            CouncilSelection.add("1UO - 7UO");
            CouncilSelection.add("1UO - 8");
            CouncilSelection.add("1 - 8UO");
            CouncilSelection.add("1UO - 8UO");
            CouncilSelection.add("1UO - 9");
            CouncilSelection.add("1 - 9UO");
            CouncilSelection.add("1UO - 9UO");
            if (do999) {
                CouncilSelection.add("1UO - -999");
                CouncilSelection.add("-999 - 1UO");
                CouncilSelection.add("1 - -999UO");
                CouncilSelection.add("-999UO - 1");
                CouncilSelection.add("1UO - -999UO");
                CouncilSelection.add("-999UO - 1UO");
            }
        } else {
            if (sameTenancyType) {
                CouncilSelection.add("1 - 1");
            }
            CouncilSelection.add("1 - 2");
            CouncilSelection.add("1 - 3");
            CouncilSelection.add("1 - 4");
            CouncilSelection.add("1 - 5");
            CouncilSelection.add("1 - 6");
            CouncilSelection.add("1 - 7");
            CouncilSelection.add("1 - 8");
            CouncilSelection.add("1 - 9");
            if (do999) {
                CouncilSelection.add("1 - -999");
                CouncilSelection.add("-999 - 1");
            }
        }
        result.put(selectionName, CouncilSelection);
        selectionName = "PrivateRegulated";
        HashSet<String> PrivateRegulatedSelection;
        PrivateRegulatedSelection = new HashSet<>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                PrivateRegulatedSelection.add("2UO - 2");
                PrivateRegulatedSelection.add("2 - 2UO");
                PrivateRegulatedSelection.add("2UO - 2UO");
            }
            PrivateRegulatedSelection.add("2UO - 1");
            PrivateRegulatedSelection.add("2 - 1UO");
            PrivateRegulatedSelection.add("2UO - 1UO");
            PrivateRegulatedSelection.add("2UO - 3");
            PrivateRegulatedSelection.add("2 - 3UO");
            PrivateRegulatedSelection.add("2UO - 3UO");
            PrivateRegulatedSelection.add("2UO - 4");
            PrivateRegulatedSelection.add("2 - 4UO");
            PrivateRegulatedSelection.add("2UO - 4UO");
            PrivateRegulatedSelection.add("2UO - 5");
            PrivateRegulatedSelection.add("2 - 5UO");
            PrivateRegulatedSelection.add("2UO - 5UO");
            PrivateRegulatedSelection.add("2UO - 6");
            PrivateRegulatedSelection.add("2 - 6UO");
            PrivateRegulatedSelection.add("2UO - 6UO");
            PrivateRegulatedSelection.add("2UO - 7");
            PrivateRegulatedSelection.add("2 - 7UO");
            PrivateRegulatedSelection.add("2UO - 7UO");
            PrivateRegulatedSelection.add("2UO - 8");
            PrivateRegulatedSelection.add("2 - 8UO");
            PrivateRegulatedSelection.add("2UO - 8UO");
            PrivateRegulatedSelection.add("2UO - 9");
            PrivateRegulatedSelection.add("2 - 9UO");
            PrivateRegulatedSelection.add("2UO - 9UO");
            if (do999) {
                PrivateRegulatedSelection.add("2UO - -999");
                PrivateRegulatedSelection.add("-999 - 2UO");
                PrivateRegulatedSelection.add("2 - -999UO");
                PrivateRegulatedSelection.add("-999UO - 2UO");
                PrivateRegulatedSelection.add("2UO - -999UO");
                PrivateRegulatedSelection.add("-999UO - 2UO");
            }
        } else {
            if (sameTenancyType) {
                PrivateRegulatedSelection.add("2 - 2");
            }
            PrivateRegulatedSelection.add("2 - 1");
            PrivateRegulatedSelection.add("2 - 3");
            PrivateRegulatedSelection.add("2 - 4");
            PrivateRegulatedSelection.add("2 - 5");
            PrivateRegulatedSelection.add("2 - 6");
            PrivateRegulatedSelection.add("2 - 7");
            PrivateRegulatedSelection.add("2 - 8");
            PrivateRegulatedSelection.add("2 - 9");
            if (do999) {
                PrivateRegulatedSelection.add("2 - -999");
                PrivateRegulatedSelection.add("-999 - 2");
            }
        }
        result.put(selectionName, PrivateRegulatedSelection);
        HashSet<String> PrivateDeregulatedSelection;
        PrivateDeregulatedSelection = new HashSet<>();
        selectionName = "PrivateDeregulated";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                PrivateDeregulatedSelection.add("3UO - 3");
                PrivateDeregulatedSelection.add("3 - 3UO");
                PrivateDeregulatedSelection.add("3UO - 3UO");
            }
            PrivateDeregulatedSelection.add("3UO - 1");
            PrivateDeregulatedSelection.add("3 - 1UO");
            PrivateDeregulatedSelection.add("3UO - 1UO");
            PrivateDeregulatedSelection.add("3UO - 2");
            PrivateDeregulatedSelection.add("3 - 2UO");
            PrivateDeregulatedSelection.add("3UO - 2UO");
            PrivateDeregulatedSelection.add("3UO - 2UO");
            PrivateDeregulatedSelection.add("3UO - 4");
            PrivateDeregulatedSelection.add("3 - 4UO");
            PrivateDeregulatedSelection.add("3UO - 4UO");
            PrivateDeregulatedSelection.add("3UO - 5");
            PrivateDeregulatedSelection.add("3 - 5UO");
            PrivateDeregulatedSelection.add("3UO - 5UO");
            PrivateDeregulatedSelection.add("3UO - 6");
            PrivateDeregulatedSelection.add("3 - 6UO");
            PrivateDeregulatedSelection.add("3UO - 6UO");
            PrivateDeregulatedSelection.add("3UO - 7");
            PrivateDeregulatedSelection.add("3 - 7UO");
            PrivateDeregulatedSelection.add("3UO - 7UO");
            PrivateDeregulatedSelection.add("3UO - 8");
            PrivateDeregulatedSelection.add("3 - 8UO");
            PrivateDeregulatedSelection.add("3UO - 8UO");
            PrivateDeregulatedSelection.add("3UO - 9");
            PrivateDeregulatedSelection.add("3 - 9UO");
            PrivateDeregulatedSelection.add("3UO - 9UO");
            if (do999) {
                PrivateDeregulatedSelection.add("3UO - -999");
                PrivateDeregulatedSelection.add("-999 - 3UO");
                PrivateDeregulatedSelection.add("3 - -999UO");
                PrivateDeregulatedSelection.add("-999UO - 3");
                PrivateDeregulatedSelection.add("3UO - -999UO");
                PrivateDeregulatedSelection.add("-999UO - 3UO");
            }
        } else {
            if (sameTenancyType) {
                PrivateDeregulatedSelection.add("3 - 3");
            }
            PrivateDeregulatedSelection.add("3 - 1");
            PrivateDeregulatedSelection.add("3 - 2");
            PrivateDeregulatedSelection.add("3 - 4");
            PrivateDeregulatedSelection.add("3 - 5");
            PrivateDeregulatedSelection.add("3 - 6");
            PrivateDeregulatedSelection.add("3 - 7");
            PrivateDeregulatedSelection.add("3 - 8");
            PrivateDeregulatedSelection.add("3 - 9");
            if (do999) {
                PrivateDeregulatedSelection.add("3 - -999");
                PrivateDeregulatedSelection.add("-999 - 3");
            }
        }
        result.put(selectionName, PrivateDeregulatedSelection);
        HashSet<String> PrivateHousingAssociationSelection;
        PrivateHousingAssociationSelection = new HashSet<>();
        selectionName = "PrivateHousingAssociation";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                PrivateHousingAssociationSelection.add("4UO - 4");
                PrivateHousingAssociationSelection.add("4 - 4UO");
                PrivateHousingAssociationSelection.add("4UO - 4UO");
            }
            PrivateHousingAssociationSelection.add("4UO - 4");
            PrivateHousingAssociationSelection.add("4 - 4UO");
            PrivateHousingAssociationSelection.add("4UO - 1");
            PrivateHousingAssociationSelection.add("4 - 1UO");
            PrivateHousingAssociationSelection.add("4UO - 1UO");
            PrivateHousingAssociationSelection.add("4UO - 2");
            PrivateHousingAssociationSelection.add("4 - 2UO");
            PrivateHousingAssociationSelection.add("4UO - 2UO");
            PrivateHousingAssociationSelection.add("4UO - 3");
            PrivateHousingAssociationSelection.add("4 - 3UO");
            PrivateHousingAssociationSelection.add("4UO - 3UO");
            PrivateHousingAssociationSelection.add("4UO - 5");
            PrivateHousingAssociationSelection.add("4 - 5UO");
            PrivateHousingAssociationSelection.add("4UO - 5UO");
            PrivateHousingAssociationSelection.add("4UO - 6");
            PrivateHousingAssociationSelection.add("4 - 6UO");
            PrivateHousingAssociationSelection.add("4UO - 6UO");
            PrivateHousingAssociationSelection.add("4UO - 7");
            PrivateHousingAssociationSelection.add("4 - 7UO");
            PrivateHousingAssociationSelection.add("4UO - 7UO");
            PrivateHousingAssociationSelection.add("4UO - 8");
            PrivateHousingAssociationSelection.add("4 - 8UO");
            PrivateHousingAssociationSelection.add("4UO - 8UO");
            PrivateHousingAssociationSelection.add("4UO - 9");
            PrivateHousingAssociationSelection.add("4 - 9UO");
            PrivateHousingAssociationSelection.add("4UO - 9UO");
            if (do999) {
                PrivateHousingAssociationSelection.add("4UO - -999");
                PrivateHousingAssociationSelection.add("-999 - 4UO");
                PrivateHousingAssociationSelection.add("4 - -999UO");
                PrivateHousingAssociationSelection.add("-999UO - 4");
                PrivateHousingAssociationSelection.add("4UO - -999UO");
                PrivateHousingAssociationSelection.add("-999UO - 4UO");
            }
        } else {
            if (sameTenancyType) {
                PrivateHousingAssociationSelection.add("4 - 4");
            }
            PrivateHousingAssociationSelection.add("4 - 1");
            PrivateHousingAssociationSelection.add("4 - 2");
            PrivateHousingAssociationSelection.add("4 - 3");
            PrivateHousingAssociationSelection.add("4 - 5");
            PrivateHousingAssociationSelection.add("4 - 6");
            PrivateHousingAssociationSelection.add("4 - 7");
            PrivateHousingAssociationSelection.add("4 - 8");
            PrivateHousingAssociationSelection.add("4 - 9");
            if (do999) {
                PrivateHousingAssociationSelection.add("4 - -999");
                PrivateHousingAssociationSelection.add("-999 - 4");
            }
        }
        result.put(selectionName, PrivateHousingAssociationSelection);
        HashSet<String> PrivateOtherSelection;
        PrivateOtherSelection = new HashSet<>();
        selectionName = "PrivateOther";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                PrivateOtherSelection.add("6UO - 6");
                PrivateOtherSelection.add("6 - 6UO");
                PrivateOtherSelection.add("6UO - 6UO");
            }
            PrivateOtherSelection.add("6UO - 1");
            PrivateOtherSelection.add("6 - 1UO");
            PrivateOtherSelection.add("6UO - 1UO");
            PrivateOtherSelection.add("6UO - 2");
            PrivateOtherSelection.add("6 - 2UO");
            PrivateOtherSelection.add("6UO - 2UO");
            PrivateOtherSelection.add("6UO - 3");
            PrivateOtherSelection.add("6 - 3UO");
            PrivateOtherSelection.add("6UO - 3UO");
            PrivateOtherSelection.add("6UO - 4");
            PrivateOtherSelection.add("6 - 4UO");
            PrivateOtherSelection.add("6UO - 4UO");
            PrivateOtherSelection.add("6UO - 5");
            PrivateOtherSelection.add("6 - 5UO");
            PrivateOtherSelection.add("6UO - 5UO");
            PrivateOtherSelection.add("6UO - 7");
            PrivateOtherSelection.add("6 - 7UO");
            PrivateOtherSelection.add("6UO - 7UO");
            PrivateOtherSelection.add("6UO - 8");
            PrivateOtherSelection.add("6 - 8UO");
            PrivateOtherSelection.add("6UO - 8UO");
            PrivateOtherSelection.add("6UO - 9");
            PrivateOtherSelection.add("6 - 9UO");
            PrivateOtherSelection.add("6UO - 9UO");
            if (do999) {
                PrivateOtherSelection.add("6UO - -999");
                PrivateOtherSelection.add("-999 - 6UO");
                PrivateOtherSelection.add("6 - -999UO");
                PrivateOtherSelection.add("-999UO - 6");
                PrivateOtherSelection.add("6UO - -999UO");
                PrivateOtherSelection.add("-999UO - 6UO");
            }
        } else {
            if (sameTenancyType) {
                PrivateOtherSelection.add("6 - 6");
            }
            PrivateOtherSelection.add("6 - 1");
            PrivateOtherSelection.add("6 - 2");
            PrivateOtherSelection.add("6 - 3");
            PrivateOtherSelection.add("6 - 4");
            PrivateOtherSelection.add("6 - 5");
            PrivateOtherSelection.add("6 - 7");
            PrivateOtherSelection.add("6 - 8");
            PrivateOtherSelection.add("6 - 9");
            if (do999) {
                PrivateOtherSelection.add("6 - -999");
                PrivateOtherSelection.add("-999 - 6");
            }
        }
        result.put(selectionName, PrivateOtherSelection);

        HashSet<String> CTBOnlySelection;
        CTBOnlySelection = new HashSet<>();
        selectionName = "CTBOnly";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                CTBOnlySelection.add("5 - 5UO");
                CTBOnlySelection.add("5UO - 5");
                CTBOnlySelection.add("5UO - 5UO");
                CTBOnlySelection.add("7 - 7UO");
                CTBOnlySelection.add("7UO - 7");
                CTBOnlySelection.add("7UO - 7UO");
            }
            // 5
            CTBOnlySelection.add("5UO - 1");
            CTBOnlySelection.add("5 - 1UO");
            CTBOnlySelection.add("5UO - 1UO");
            CTBOnlySelection.add("5UO - 2");
            CTBOnlySelection.add("5 - 2UO");
            CTBOnlySelection.add("5UO - 2UO");
            CTBOnlySelection.add("5UO - 3");
            CTBOnlySelection.add("5 - 3UO");
            CTBOnlySelection.add("5UO - 3UO");
            CTBOnlySelection.add("5UO - 4");
            CTBOnlySelection.add("5 - 4UO");
            CTBOnlySelection.add("5UO - 4UO");
            CTBOnlySelection.add("5UO - 6");
            CTBOnlySelection.add("5 - 6UO");
            CTBOnlySelection.add("5UO - 6UO");
            CTBOnlySelection.add("5UO - 7");
            CTBOnlySelection.add("5 - 7UO");
            CTBOnlySelection.add("5UO - 7UO");
            CTBOnlySelection.add("5UO - 8");
            CTBOnlySelection.add("5 - 8UO");
            CTBOnlySelection.add("5UO - 8UO");
            CTBOnlySelection.add("5UO - 9");
            CTBOnlySelection.add("5 - 9UO");
            CTBOnlySelection.add("5UO - 9UO");
            // 7
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
                CTBOnlySelection.add("5UO - -999");
                CTBOnlySelection.add("-999 - 5UO");
                CTBOnlySelection.add("5 - -999UO");
                CTBOnlySelection.add("-999UO - 5");
                CTBOnlySelection.add("5UO - -999UO");
                CTBOnlySelection.add("-999UO - 5UO");
                CTBOnlySelection.add("7UO - -999");
                CTBOnlySelection.add("-999 - 7UO");
                CTBOnlySelection.add("7 - -999UO");
                CTBOnlySelection.add("-999UO - 7");
                CTBOnlySelection.add("7UO - -999UO");
                CTBOnlySelection.add("-999UO - 7UO");
            }
        } else {
            if (sameTenancyType) {
                CTBOnlySelection.add("5 - 5");
                CTBOnlySelection.add("7 - 7");
            }
            // 5
            CTBOnlySelection.add("7 - 1");
            CTBOnlySelection.add("7 - 2");
            CTBOnlySelection.add("7 - 3");
            CTBOnlySelection.add("7 - 4");
            CTBOnlySelection.add("7 - 5");
            CTBOnlySelection.add("7 - 6");
            CTBOnlySelection.add("7 - 8");
            CTBOnlySelection.add("7 - 9");
            // 7
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

        HashSet<String> CouncilNonHRAOnlySelection;
        CouncilNonHRAOnlySelection = new HashSet<>();
        selectionName = "CouncilNonHRA";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                CouncilNonHRAOnlySelection.add("8 - 8UO");
                CouncilNonHRAOnlySelection.add("8UO - 8");
                CouncilNonHRAOnlySelection.add("8UO - 8UO");
            }
            CouncilNonHRAOnlySelection.add("8UO - 1");
            CouncilNonHRAOnlySelection.add("8 - 1UO");
            CouncilNonHRAOnlySelection.add("8UO - 1UO");
            CouncilNonHRAOnlySelection.add("8UO - 2");
            CouncilNonHRAOnlySelection.add("8 - 2UO");
            CouncilNonHRAOnlySelection.add("8UO - 2UO");
            CouncilNonHRAOnlySelection.add("8UO - 3");
            CouncilNonHRAOnlySelection.add("8 - 3UO");
            CouncilNonHRAOnlySelection.add("8UO - 3UO");
            CouncilNonHRAOnlySelection.add("8UO - 4");
            CouncilNonHRAOnlySelection.add("8 - 4UO");
            CouncilNonHRAOnlySelection.add("8UO - 4UO");
            CouncilNonHRAOnlySelection.add("8UO - 5");
            CouncilNonHRAOnlySelection.add("8 - 5UO");
            CouncilNonHRAOnlySelection.add("8UO - 5UO");
            CouncilNonHRAOnlySelection.add("8UO - 6");
            CouncilNonHRAOnlySelection.add("8 - 6UO");
            CouncilNonHRAOnlySelection.add("8UO - 6UO");
            CouncilNonHRAOnlySelection.add("8UO - 7");
            CouncilNonHRAOnlySelection.add("8 - 7UO");
            CouncilNonHRAOnlySelection.add("8UO - 7UO");
            CouncilNonHRAOnlySelection.add("8UO - 9");
            CouncilNonHRAOnlySelection.add("8 - 9UO");
            CouncilNonHRAOnlySelection.add("8UO - 9UO");
            if (do999) {
                CouncilNonHRAOnlySelection.add("8UO - -999");
                CouncilNonHRAOnlySelection.add("-999 - 8UO");
                CouncilNonHRAOnlySelection.add("8 - -999UO");
                CouncilNonHRAOnlySelection.add("-999UO - 8");
                CouncilNonHRAOnlySelection.add("8UO - -999UO");
                CouncilNonHRAOnlySelection.add("-999UO - 8UO");
            }
        } else {
            if (sameTenancyType) {
                CouncilNonHRAOnlySelection.add("8 - 8");
            }
            CouncilNonHRAOnlySelection.add("8 - 1");
            CouncilNonHRAOnlySelection.add("8 - 2");
            CouncilNonHRAOnlySelection.add("8 - 3");
            CouncilNonHRAOnlySelection.add("8 - 4");
            CouncilNonHRAOnlySelection.add("8 - 5");
            CouncilNonHRAOnlySelection.add("8 - 6");
            CouncilNonHRAOnlySelection.add("8 - 7");
            CouncilNonHRAOnlySelection.add("8 - 9");
            if (do999) {
                CouncilNonHRAOnlySelection.add("8 - -999");
                CouncilNonHRAOnlySelection.add("-999 - 8");
            }
        }
        result.put(selectionName, CouncilNonHRAOnlySelection);
        HashSet<String> PrivateMealDeductionSelection;
        PrivateMealDeductionSelection = new HashSet<>();
        selectionName = "PrivateMealDeduction";
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                PrivateMealDeductionSelection.add("9 - 9UO");
                PrivateMealDeductionSelection.add("9UO - 9");
                PrivateMealDeductionSelection.add("9UO - 9UO");
            }
            PrivateMealDeductionSelection.add("9UO - 1");
            PrivateMealDeductionSelection.add("9 - 1UO");
            PrivateMealDeductionSelection.add("9UO - 1UO");
            PrivateMealDeductionSelection.add("9UO - 2");
            PrivateMealDeductionSelection.add("9 - 2UO");
            PrivateMealDeductionSelection.add("9UO - 2UO");
            PrivateMealDeductionSelection.add("9UO - 3");
            PrivateMealDeductionSelection.add("9 - 3UO");
            PrivateMealDeductionSelection.add("9UO - 3UO");
            PrivateMealDeductionSelection.add("9UO - 4");
            PrivateMealDeductionSelection.add("9 - 4UO");
            PrivateMealDeductionSelection.add("9UO - 4UO");
            PrivateMealDeductionSelection.add("9UO - 5");
            PrivateMealDeductionSelection.add("9 - 5UO");
            PrivateMealDeductionSelection.add("9UO - 5UO");
            PrivateMealDeductionSelection.add("9UO - 6");
            PrivateMealDeductionSelection.add("9 - 6UO");
            PrivateMealDeductionSelection.add("9UO - 6UO");
            PrivateMealDeductionSelection.add("9UO - 7");
            PrivateMealDeductionSelection.add("9 - 7UO");
            PrivateMealDeductionSelection.add("9UO - 7UO");
            PrivateMealDeductionSelection.add("9UO - 8");
            PrivateMealDeductionSelection.add("9 - 8UO");
            PrivateMealDeductionSelection.add("9UO - 8UO");
            if (do999) {
                PrivateMealDeductionSelection.add("9UO - -999");
                PrivateMealDeductionSelection.add("-999 - 9UO");
                PrivateMealDeductionSelection.add("9 - -999UO");
                PrivateMealDeductionSelection.add("-999UO - 9");
                PrivateMealDeductionSelection.add("9UO - -999UO");
                PrivateMealDeductionSelection.add("-999UO - 9UO");
            }
        } else {
            if (sameTenancyType) {
                PrivateMealDeductionSelection.add("9 - 9");
            }
            PrivateMealDeductionSelection.add("9 - 1");
            PrivateMealDeductionSelection.add("9 - 2");
            PrivateMealDeductionSelection.add("9 - 3");
            PrivateMealDeductionSelection.add("9 - 4");
            PrivateMealDeductionSelection.add("9 - 5");
            PrivateMealDeductionSelection.add("9 - 6");
            PrivateMealDeductionSelection.add("9 - 7");
            PrivateMealDeductionSelection.add("9 - 9");
            if (do999) {
                PrivateMealDeductionSelection.add("9 - -999");
                PrivateMealDeductionSelection.add("-999 - 9");
            }
        }
        result.put(selectionName, PrivateMealDeductionSelection);
        HashSet<String> allSelection;
        allSelection = new HashSet<>();
        selectionName = Strings.sAll;
        allSelection.addAll(CouncilSelection);
        allSelection.addAll(PrivateHousingAssociationSelection);
        allSelection.addAll(PrivateRegulatedSelection);
        allSelection.addAll(PrivateDeregulatedSelection);
        allSelection.addAll(PrivateOtherSelection);
        result.put(selectionName, allSelection);

        selectionName = "ReportSelection";
        HashSet<String> selection;
        selection = new HashSet<>();
        if (doUnderOccupancy) {
            if (sameTenancyType) {
                selection.add("1 - 1UO");
                selection.add("1UO - 1");
                selection.add("1UO - 1UO");
//                selection.add("2 - 2UO");
//                selection.add("2UO - 2");
//                selection.add("2UO - 2UO");
                selection.add("3 - 3UO");
                selection.add("3UO - 3");
                selection.add("3UO - 3UO");
                selection.add("4 - 4UO");
                selection.add("4UO - 4");
                selection.add("4UO - 4UO");
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
     * @param doUnderOccupancy
     * @param do999
     * @param sameTenancyType
     * @return
     */
    protected TreeMap<String, HashSet<String>> getAllSelectionsGrouped(
            boolean doUnderOccupancy,
            boolean do999,
            boolean sameTenancyType) {
        TreeMap<String, HashSet<String>> result;
        result = new TreeMap<>();
        String selectionName;
        selectionName = "Regulated";
        HashSet<String> regulatedSelection;
        regulatedSelection = new HashSet<>();
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
        unregulatedSelection = new HashSet<>();
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
        ungroupedSelection = new HashSet<>();
        selectionName = Strings.sGroupedNo;
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
        allSelection = new HashSet<>();
        selectionName = Strings.sAll;
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
    public TreeMap<String, TreeMap<String, BigDecimal>> getTenancyTypeTransitionMap(
            File f,
            double timeDiff) {
//        Object[] result;
//        result = new Object[4];
        TreeMap<String, TreeMap<String, BigDecimal>> map;
        map = new TreeMap<>();
//        result[0] = map;
//        Double min = Double.MAX_VALUE;
//        Double max = Double.MIN_VALUE;
//        int size = 0;
        ArrayList<String> lines = DW_Table.readCSV(f);
        ArrayList<String> tenancyTypes;
        tenancyTypes = new ArrayList<>();
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
                    transitions = new TreeMap<>();
                    map.put(tenancyBefore, transitions);
                    tenancyTypes.add(tenancyBefore);
                }
                first = false;
            } else {
                String tenancyNow;
                tenancyNow = split[0];
                for (int i = 1; i < split.length; i++) {
                    String tenancyTypeBefore; // This is tenancyStart!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    tenancyTypeBefore = tenancyTypes.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    if (count != 0) {
                        mapContainsNonZeroValues = true;
                    }
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<String, BigDecimal> tenancyTransition;
                    tenancyTransition = map.get(tenancyTypeBefore);
                    tenancyTransition.put(tenancyNow, BigDecimal.valueOf(rate));
//                    if (tenancyTypeBefore.compareTo(tenancyEnd) != 0) {
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
     * @return TenancyTypeBefore, TenancyTypeNow Counts.
     */
    public TreeMap<String, TreeMap<String, BigDecimal>> getTenancyTypeTransitionMapGrouped(
            File f,
            double timeDiff) {
//        Object[] result;
//        result = new Object[4];
        TreeMap<String, TreeMap<String, BigDecimal>> map;
        map = new TreeMap<>();
//        result[0] = map;
//        Double min = Double.MAX_VALUE;
//        Double max = Double.MIN_VALUE;
//        int size = 0;
        ArrayList<String> lines = DW_Table.readCSV(f);
        ArrayList<String> tenancyTypes;
        tenancyTypes = new ArrayList<>();
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
                    transitions = new TreeMap<>();
                    map.put(tenancyStart, transitions);
                    tenancyTypes.add(tenancyStart);
                }
                first = false;
            } else {
                String tenancyEnd;
                tenancyEnd = split[0];
                for (int i = 1; i < split.length; i++) {
                    String tenancyTypeBefore;

                    if (split.length > 9) {
                        int debug = 1;

                    }

                    tenancyTypeBefore = tenancyTypes.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    if (count != 0) {
                        mapContainsNonZeroValues = true;
                    }
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<String, BigDecimal> tenancyTransition;
                    tenancyTransition = map.get(tenancyTypeBefore);
                    tenancyTransition.put(tenancyEnd, BigDecimal.valueOf(rate));
//                    if (tenancyTypeBefore.compareTo(tenancyEnd) != 0) {
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
     * Object[] result; result = new Object[7]; result[0] = maps;
     * TreeMap<String, TreeMap<BigDecimal, BigDecimal>> // label, x, y maps
     * result[1] = newMinY; // BigDecimal minimum y value in selection.
     * result[2] = newMaxY; // BigDecimal maximum y value in selection.
     * result[3] = newMinX; // BigDecimal minimum x value in selection.
     * result[4] = newMaxX; // BigDecimal maximum x value in selection.
     * result[5] = labels; // collection of strings: row index " - " column
     * index result[6] = xAxisLabels; result[7] = TreeMap<String, Boolean> //
     * label, non-zero values result[7] = TreeMap<String, Boolean> // labels,
     * non-zero values
     */
    private Object[] getData(
            TreeMap<String, TreeMap<ONSPD_YM3, BigDecimal>> data,
            TreeMap<BigDecimal, ONSPD_YM3> xAxisLabels,
            TreeMap<ONSPD_YM3, BigDecimal> fileLabelValue) {
        Object[] result;
        result = new Object[9];
        TreeMap<String, TreeMap<BigDecimal, BigDecimal>> maps;
        maps = new TreeMap<>();
        BigDecimal newMinX;
        newMinX = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal newMaxX;
        newMaxX = BigDecimal.valueOf(Double.MIN_VALUE);
        BigDecimal newMinY;
        newMinY = BigDecimal.valueOf(Double.MAX_VALUE);
        BigDecimal newMaxY;
        newMaxY = BigDecimal.valueOf(Double.MIN_VALUE);

        TreeMap<String, Boolean> nonZero;
        nonZero = new TreeMap<>();

        // Initialise data, newMinX, newMaxX, newMinY, newMaxY
        Iterator<String> iteS;
        Iterator<ONSPD_YM3> iteS2;
        iteS = data.keySet().iterator();
        String key;
        TreeMap<ONSPD_YM3, BigDecimal> dataYX;
        while (iteS.hasNext()) {
            key = iteS.next(); // key is aggregate area code
            TreeMap<BigDecimal, BigDecimal> map;
            map = maps.get(key);
            if (map == null) {
                map = new TreeMap<>();
                maps.put(key, map);
            }
            dataYX = data.get(key);
            // init
            Boolean bo;
            bo = nonZero.get(key);
            if (bo == null) {
                nonZero.put(key, false);
            }
            iteS2 = dataYX.keySet().iterator();
            while (iteS2.hasNext()) {
                ONSPD_YM3 YM3;
                YM3 = iteS2.next();
//                System.out.println("YM3 " + YM3);
                BigDecimal x = fileLabelValue.get(YM3);
                if (x == null) {
                    nonZero.put(key, true);
                } else {
                    if (x.compareTo(BigDecimal.ZERO) != 0) {
                        nonZero.put(key, true);
                    }
                    BigDecimal y;
                    y = dataYX.get(YM3);
//                    System.out.println("x " + x);
//                    System.out.println("y " + y);
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

        TreeMap<String, Boolean> nonZero2;
        nonZero2 = new TreeMap<>();

        // Initialise labels.
        ArrayList<String> labels;
        labels = new ArrayList<>();
        iteS = maps.keySet().iterator();
        while (iteS.hasNext()) {
            String label;
            label = iteS.next();
            labels.add(label);
            nonZero2.put(label, nonZero.get(label));
        }

        result[0] = maps;
        result[1] = newMinY;
        result[2] = newMaxY;
        result[3] = newMinX;
        result[4] = newMaxX;
        result[5] = labels;
        result[6] = xAxisLabels;
        result[7] = nonZero;
        result[8] = nonZero2;
        return result;
    }

    /**
     * @param bigMatrix The bigMatrix to repack and select from. Keys are x
     * values to be plot, the final values are y values, the Integer keys
     * between are row and column indexes. TenancyTypeBefore, TenancyTypeNow
     * Counts.
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
     * result[8] = TreeMap<String, Boolean> // labels, non-zero values
     */
    private Object[] getData(
            TreeMap<BigDecimal, TreeMap<String, TreeMap<String, BigDecimal>>> bigMatrix,
            HashSet<String> selection,
            TreeMap<BigDecimal, String> xAxisLabels) {
        Object[] result;
        result = new Object[9];
        TreeMap<String, TreeMap<BigDecimal, BigDecimal>> maps;
        maps = new TreeMap<>();
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
        nonZero = new TreeMap<>();
        Iterator<BigDecimal> ite;
        ite = bigMatrix.keySet().iterator();
        while (ite.hasNext()) {
            BigDecimal x = ite.next();
            TreeMap<String, TreeMap<String, BigDecimal>> iomatrix;
            iomatrix = bigMatrix.get(x);
            Iterator<String> ite2;
            ite2 = iomatrix.keySet().iterator();
            while (ite2.hasNext()) {
                String tenancyTypeBefore = ite2.next(); // This is Before!!!!!!!!!!!!
                TreeMap<String, BigDecimal> omatrix;
                omatrix = iomatrix.get(tenancyTypeBefore);
                Iterator<String> ite3;
                ite3 = omatrix.keySet().iterator();
                while (ite3.hasNext()) {
                    String tenancyTypeNow = ite3.next(); // This is now!!!!!!!!!1
                    if (!tenancyTypeNow.equalsIgnoreCase(tenancyTypeBefore)) {
                        String key;
                        key = "" + tenancyTypeBefore + " - " + tenancyTypeNow;
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
                                map = new TreeMap<>();
                                maps.put(key, map);
                            }
                            BigDecimal y;
                            y = omatrix.get(tenancyTypeNow);
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
        labels = new ArrayList<>();
//        labels.addAll(maps.keySet()); // This does not work as maps is gc resulting in labels becoming null for some reason.
        // Declare nonZero2
        SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;
        DW_SHBE_TenancyType_Handler = Env.getSHBE_TenancyType_Handler();
        TreeMap<String, Boolean> nonZero2;
        nonZero2 = new TreeMap<>();
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
//                String tenancyTypeBefore = ite2.next();
//                TreeMap<String, BigDecimal> omatrix;
//                omatrix = iomatrix.get(tenancyTypeBefore);
//                Iterator<String> ite3;
//                ite3 = omatrix.keySet().iterator();
//                while (ite3.hasNext()) {
//                    String tenancyTypeNow = ite3.next();
//                    if (!tenancyTypeNow.equalsIgnoreCase(tenancyTypeBefore)) {
//                        String key;
//                        key = "" + tenancyTypeBefore + " - " + tenancyTypeNow;
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
//                            y = omatrix.get(tenancyTypeNow);
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
//            newLabel = SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[0]));
//            newLabel += " TO ";
//            newLabel += SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[1]));
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
