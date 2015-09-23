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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Execution;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Time;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import uk.ac.leeds.ccg.andyt.generic.visualisation.charts.Generic_BarChart;
import uk.ac.leeds.ccg.andyt.generic.visualisation.charts.Generic_LineGraph;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_LineGraph extends Generic_LineGraph {

//    private BigDecimal yPin;
//    private BigDecimal yMax;
//    private BigDecimal yIncrement;
//    private int decimalPlacePrecisionForCalculations;
//    private int decimalPlacePrecisionForDisplay;
//    private RoundingMode roundingMode;
//    private MathContext mc;
//    private ExecutorService executorService;
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
        includes = getIncludes();

        TreeMap<String, HashSet<String>> allSelections;
        allSelections = getAllSelections();

        HashSet<Future> futures;
        futures = new HashSet<Future>();

        String format = "PNG";
        int startIndex = 0;

        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add("PostcodeChanged");
        types.add("PostcodeUnchanged");
        types.add("Multiple");
        types.add("");

        ArrayList<Boolean> bArray;
        bArray = new ArrayList<Boolean>();
        bArray.add(true);
        bArray.add(false);

        Iterator<String> ite;
        ite = types.iterator();
        while (ite.hasNext()) {
            String type;
            type = ite.next();
            Iterator<Boolean> iteB;
            iteB = bArray.iterator();
            while (iteB.hasNext()) {
                boolean checkPreviousTenure;
                checkPreviousTenure = iteB.next();
                doSumat(
                        includes,
                        allSelections,
                        futures,
                        format,
                        SHBEFilenames,
                        startIndex,
                        type,
                        checkPreviousTenure);
            }
        }
        Generic_Execution.shutdownExecutorService(
                executorService, futures, this);
    }

    private void doSumat(
            TreeMap<String, ArrayList<Integer>> includes,
            TreeMap<String, HashSet<String>> allSelections,
            HashSet<Future> futures,
            String format,
            String[] SHBEFilenames,
            int startIndex,
            String multiple,
            boolean checkPreviousTenure) {
//        HashMap<String, Boolean> doneFirsts;
//        doneFirsts = new HashMap<String, Boolean>();

        Iterator<String> ite;
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeKey;
            includeKey = ite.next();
//            doneFirsts.put(includeKey, false);
            ArrayList<Integer> include;
            include = includes.get(includeKey);
            Object[] treeMapDateLabelSHBEFilename;
            treeMapDateLabelSHBEFilename = DW_SHBE_Handler.getTreeMapDateLabelSHBEFilenames(
                    SHBEFilenames,
                    startIndex,
                    include);
            TreeMap<BigDecimal, String> xAxisLabels;
            xAxisLabels = (TreeMap<BigDecimal, String>) treeMapDateLabelSHBEFilename[0];
            TreeMap<String, BigDecimal> fileLabelValue;
            fileLabelValue = (TreeMap<String, BigDecimal>) treeMapDateLabelSHBEFilename[1];

            File dirIn;
            dirIn = DW_Files.getOutputSHBETablesTenancyTypeTransitionDir(
                    "All", 
                    checkPreviousTenure);

            TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>> bigMatrix;
            bigMatrix = new TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>>();

//            int size = Integer.MIN_VALUE;
//            Double min = Double.MAX_VALUE;
//            Double max = Double.MIN_VALUE;
            ArrayList<String> month3Letters;
            month3Letters = Generic_Time.getMonths3Letters();

            boolean doneFirst;
            doneFirst = false;

            String month0 = "";
            String year0 = "";
            for (int i = startIndex; i < SHBEFilenames.length; i++) {
                System.out.println("index " + i);
                if (!doneFirst) {
                    if (include.contains(i - 1) && (i - 1) >= 0) {
                        String aSHBEFilename0;
                        aSHBEFilename0 = SHBEFilenames[i - 1];
                        month0 = DW_SHBE_Handler.getMonth(aSHBEFilename0);
                        year0 = DW_SHBE_Handler.getYear(aSHBEFilename0);
                        doneFirst = true;
                    }
                }
                if (doneFirst) {
                    String aSHBEFilename1;
                    aSHBEFilename1 = SHBEFilenames[i];
                    String month;
                    month = DW_SHBE_Handler.getMonth(aSHBEFilename1);
                    String year;
                    year = DW_SHBE_Handler.getYear(aSHBEFilename1);
                    String filename;
                    filename = "TenancyTypeTransition_All_Start_" + year0 + month0 + "_End_" + year + month + ".csv";
                    if (include.contains(i)) {
                        System.out.println("Using file " + filename);
                        File f;
                        f = new File(
                                dirIn,
                                filename);
                        double timeDiff;
                        timeDiff = Generic_Time.getMonthDiff(
                                Integer.valueOf(year0),
                                Integer.valueOf(year),
                                Generic_Time.getMonth(month0, month3Letters),
                                Generic_Time.getMonth(month, month3Letters));
                        //timeDiff = 1;
                        TreeMap<Integer, TreeMap<Integer, BigDecimal>> tenancyTypeTransitionMap;
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
                        String fileLabel;
                        fileLabel = getLabel(year0, month0, year, month);
                        BigDecimal key;
                        key = fileLabelValue.get(fileLabel);
                        //System.out.println("fileLabel " + fileLabel);
                        //System.out.println("key " + key);
                        if (key != null) {
                            bigMatrix.put(key, tenancyTypeTransitionMap);
//                    System.out.println("max " + max);
//                    System.out.println("min " + min);
//                    System.out.println("size " + size);
                        } else {
                            System.out.println("No value for fileLabel " + fileLabel);
                        }
                        year0 = year;
                        month0 = month;
                    } else {
                        System.out.println("Omitted file " + filename);
                    }
                }
            }

            Iterator<String> ite2;
            ite2 = allSelections.keySet().iterator();
            while (ite2.hasNext()) {
                String selections = ite2.next();
                HashSet<String> selection = allSelections.get(selections);
                File dirOut;
                dirOut = DW_Files.getOutputSHBEPlotsTenancyTypeTransitionDir(
                        "All",
                        checkPreviousTenure,
                        multiple);
                if (checkPreviousTenure) {
                   dirOut = new File(
                        dirOut,
                        "CheckedPreviousTenure");
                }
                dirOut.mkdirs();
                File fout;
                fout = new File(
                        dirOut,
                        "TenancyTransitionLineGraph" + selections + "_" + includeKey + ".PNG");
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
                Object[] data = getData(
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

    protected static TreeMap<String, HashSet<String>> getAllSelections() {
        TreeMap<String, HashSet<String>> result;
        result = new TreeMap<String, HashSet<String>>();
        String selections;
        selections = "Council";
        HashSet<String> selection;
//        selection = new HashSet<String>();
//        selection.add("1 - 2");
//        selection.add("1 - 3");
//        selection.add("1 - 4");
//        //selection.add("1 - 5");
//        selection.add("1 - 6");
//        //selection.add("1 - 7");
//        //selection.add("1 - 8");
//        //selection.add("1 - 9");
//        //selection.add("1 - -999");
//        result.put(selections, selection);
//        selection = new HashSet<String>();
//        selections = "PrivateRegulated";
//        selection.add("2 - 1");
//        selection.add("2 - 3");
//        selection.add("2 - 4");
//        selection.add("2 - 6");
//        result.put(selections, selection);
//        selection = new HashSet<String>();
//        selections = "PrivateDeregulated";
//        selection.add("3 - 1");
//        selection.add("3 - 2");
//        selection.add("3 - 4");
//        selection.add("3 - 6");
//        result.put(selections, selection);
//        selection = new HashSet<String>();
//        selections = "PrivateHousingAssociation";
//        selection.add("4 - 1");
//        selection.add("4 - 2");
//        selection.add("4 - 3");
//        selection.add("4 - 6");
//        result.put(selections, selection);
//        selection = new HashSet<String>();
//        selections = "PrivateOther";
//        selection.add("6 - 1");
//        selection.add("6 - 2");
//        selection.add("6 - 3");
//        selection.add("6 - 4");
//        result.put(selections, selection);
        selections = "CouncilPrivateDeregulatedPrivateHousingAssociation";
        selection = new HashSet<String>();
        selection.add("1 - 3");
        selection.add("1 - 4");
        selection.add("3 - 1");
        selection.add("3 - 4");
        selection.add("4 - 1");
        selection.add("4 - 3");
        result.put(selections, selection);
        return result;
    }

    /**
     * Negation of getOmits()
     *
     * @return
     */
    public static TreeMap<String, ArrayList<Integer>> getIncludes() {
        TreeMap<String, ArrayList<Integer>> result;
        result = new TreeMap<String, ArrayList<Integer>>();
        TreeMap<String, ArrayList<Integer>> omits;
        omits = getOmits();
        Iterator<String> ite;
        ite = omits.keySet().iterator();
        while (ite.hasNext()) {
            String omitKey;
            omitKey = ite.next();
            ArrayList<Integer> omit;
            omit = omits.get(omitKey);
            ArrayList<Integer> include;
            include = DW_SHBE_Handler.getSHBEFilenamesIndexesExcept34();
            include.removeAll(omit);
            result.put(omitKey, include);
        }
        return result;
    }

    /**
     * Negation of getIncludes()
     *
     * @return
     */
    public static TreeMap<String, ArrayList<Integer>> getOmits() {
        TreeMap<String, ArrayList<Integer>> result;
        result = new TreeMap<String, ArrayList<Integer>>();
        String omitKey;
        ArrayList<Integer> omitAll;
        omitKey = "All";
        omitAll = new ArrayList<Integer>();
        omitAll.add(34);
        result.put(omitKey, omitAll);
        omitKey = "6Monthly";
        ArrayList<Integer> omit6Monthly;
        omit6Monthly = new ArrayList<Integer>();
        omit6Monthly.add(6);
        omit6Monthly.add(8);
        omit6Monthly.add(10);
        omit6Monthly.add(12);
        omit6Monthly.add(14);
        omit6Monthly.add(15);
        omit6Monthly.add(16);
        omit6Monthly.add(18);
        omit6Monthly.add(19);
        omit6Monthly.add(20);
        omit6Monthly.add(21);
        omit6Monthly.add(22);
        omit6Monthly.add(24);
        omit6Monthly.add(25);
        omit6Monthly.add(26);
        omit6Monthly.add(27);
        omit6Monthly.add(28);
        omit6Monthly.add(30);
        omit6Monthly.add(31);
        omit6Monthly.add(32);
        omit6Monthly.add(33);
        omit6Monthly.add(34);
        omit6Monthly.add(36);
        omit6Monthly.add(37);
        omit6Monthly.add(38);
        omit6Monthly.add(39);
        omit6Monthly.add(40);
        omit6Monthly.add(42);
        omit6Monthly.add(43);
        result.put(omitKey, omit6Monthly);
        omitKey = "Monthly";
        ArrayList<Integer> omitMonthly;
        omitMonthly = new ArrayList<Integer>();
        for (int i = 0; i < 14; i++) {
            omitMonthly.add(i);
        }
        omitMonthly.add(34);
        result.put(omitKey, omitMonthly);
        return result;
    }

    public static String getLabel(
            String year0,
            String month0,
            String year,
            String month) {
        String result;
        result = year0 + " " + month0 + "_" + year + " " + month;
        // System.out.println(result);
        return result;
    }

    /**
     * TenureBefore, TenureNow Count
     *
     * @param f
     * @param timeDiff Normaliser for counts.
     * @return null null null null null null null null null null null null null
     * null null null null null null null null null null null null null null
     * null null     {@code
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
     */
    public TreeMap<Integer, TreeMap<Integer, BigDecimal>> getTenancyTypeTransitionMap(
            File f,
            double timeDiff) {
//        Object[] result;
//        result = new Object[4];
        TreeMap<Integer, TreeMap<Integer, BigDecimal>> map;
        map = new TreeMap<Integer, TreeMap<Integer, BigDecimal>>();
//        result[0] = map;
//        Double min = Double.MAX_VALUE;
//        Double max = Double.MIN_VALUE;
//        int size = 0;
        ArrayList<String> lines = DW_Table.readCSV(f);
        ArrayList<Integer> tenures;
        tenures = new ArrayList<Integer>();
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
                    Integer tenancyStart;
                    tenancyStart = Integer.valueOf(split[i]);
                    TreeMap<Integer, BigDecimal> transitions;
                    transitions = new TreeMap<Integer, BigDecimal>();
                    map.put(tenancyStart, transitions);
                    tenures.add(tenancyStart);
                }
                first = false;
            } else {
                Integer tenancyEnd;
                tenancyEnd = Integer.valueOf(split[0]);
                for (int i = 1; i < split.length; i++) {
                    Integer tenureBefore;
                    tenureBefore = tenures.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<Integer, BigDecimal> tenancyTransition;
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
     * between are row and column indexes.
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
     */
    private Object[] getData(
            TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>> bigMatrix,
            HashSet<String> selection,
            TreeMap<BigDecimal, String> xAxisLabels) {
        Object[] result;
        result = new Object[7];
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
        Iterator<BigDecimal> ite;
        ite = bigMatrix.keySet().iterator();
        while (ite.hasNext()) {
            BigDecimal x = ite.next();
            TreeMap<Integer, TreeMap<Integer, BigDecimal>> iomatrix;
            iomatrix = bigMatrix.get(x);
            Iterator<Integer> ite2;
            ite2 = iomatrix.keySet().iterator();
            while (ite2.hasNext()) {
                Integer tenureNow = ite2.next();
                TreeMap<Integer, BigDecimal> omatrix;
                omatrix = iomatrix.get(tenureNow);
                Iterator<Integer> ite3;
                ite3 = omatrix.keySet().iterator();
                while (ite3.hasNext()) {
                    Integer tenureBefore = ite3.next();
                    if (tenureNow.compareTo(tenureBefore) != 0) {
                        String key;
                        key = "" + tenureBefore + " - " + tenureNow;
                        if (selection.contains(key)) {
                            TreeMap<BigDecimal, BigDecimal> map;
                            map = maps.get(key);
                            if (map == null) {
                                map = new TreeMap<BigDecimal, BigDecimal>();
                                maps.put(key, map);
                            }
                            BigDecimal y;
                            y = omatrix.get(tenureBefore);
                            newMinY = y.min(newMinY);
                            newMaxY = y.max(newMaxY);
                            newMinX = x.min(newMinX);
                            newMaxX = x.max(newMaxX);
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
        Iterator<String> iteS;
        iteS = maps.keySet().iterator();
        while (iteS.hasNext()) {
            String label;
            label = iteS.next();
            String[] tenancyTypes;
            tenancyTypes = label.split(" - ");
            String newLabel;
            newLabel = DW_SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[0]));
            newLabel += " TO ";
            newLabel += DW_SHBE_Handler.getTenancyTypeName(Integer.valueOf(tenancyTypes[1]));
            labels.add(newLabel);
        }
        result[5] = labels;
        result[6] = xAxisLabels;
        return result;
    }
}
