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

    private int dataWidth;
    private int dataHeight;
    private String xAxisLabel;
    private String yAxisLabel;
//    private int barGap;
//    private int xIncrement;
//    private int numberOfBars;
//    private int numberOfYAxisTicks;
    private BigDecimal yPin;
    private BigDecimal yMax;
    private BigDecimal yIncrement;
    private int decimalPlacePrecisionForCalculations;
    private int decimalPlacePrecisionForDisplay;
    private RoundingMode roundingMode;
    private MathContext mc;
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
            boolean drawOriginLinesOnPlot, //Ignored
            int barGap,
            int xIncrement,
            BigDecimal yMax,
            BigDecimal yPin,
            BigDecimal yIncrement,
            int numberOfYAxisTicks,
            int decimalPlacePrecisionForCalculations,
            int decimalPlacePrecisionForDisplay,
            RoundingMode aRoundingMode) {
        super(executorService, file, format, title, dataWidth, dataHeight, xAxisLabel, yAxisLabel, drawOriginLinesOnPlot, barGap, xIncrement, yMax, yPin, yIncrement, numberOfYAxisTicks, decimalPlacePrecisionForCalculations, decimalPlacePrecisionForDisplay, aRoundingMode);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DW_LineGraph().run(args);
    }

    public void run(String[] args) {
        Generic_Visualisation.getHeadlessEnvironment();
        dataWidth = 400;
        dataHeight = 200;
        xAxisLabel = "Dates";
        yAxisLabel = "Y";
        yPin = BigDecimal.ZERO;
//        yMax = new BigDecimal(700);
        yMax = null;
//        yIncrement = BigDecimal.TEN;
//        yIncrement = BigDecimal.ONE;
        yIncrement = null;
        //int yAxisStartOfEndInterval = 60;
        decimalPlacePrecisionForCalculations = 20;
        //int decimalPlacePrecisionForDisplay = 3;
        roundingMode = RoundingMode.HALF_UP;
        mc = new MathContext(decimalPlacePrecisionForCalculations, roundingMode);
        executorService = Executors.newSingleThreadExecutor();
        String[] SHBEFilenames;
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();
//        ArrayList<String> claimantTypes;
//        claimantTypes = new ArrayList<String>();
//        claimantTypes.add("HB");
//        claimantTypes.add("CTB");

        ArrayList<Integer> omit;
        omit = new ArrayList<Integer>();
        omit.add(35);
        omit.add(36);
        int startIndex = 0;

        Object[] treeMapDateLabelSHBEFilename;
        treeMapDateLabelSHBEFilename = DW_SHBE_Handler.getTreeMapDateLabelSHBEFilenames();

        TreeMap<BigDecimal, String> valueLabel;
        valueLabel = (TreeMap<BigDecimal, String>) treeMapDateLabelSHBEFilename[0];
        TreeMap<String, BigDecimal> fileLabelValue;
        fileLabelValue = (TreeMap<String, BigDecimal>) treeMapDateLabelSHBEFilename[1];

        String format = "PNG";

        File dirIn;
        dirIn = new File(
                DW_Files.getOutputSHBETablesDir(),
                "Tenancy");
        dirIn = new File(
                dirIn,
                "All");
        dirIn = new File(
                dirIn,
                "TenancyTypeTransition");

        TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>> bigMatrix;
        bigMatrix = new TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>>();

        int size = Integer.MIN_VALUE;
        Double min = Double.MAX_VALUE;
        Double max = Double.MIN_VALUE;
        
        ArrayList<String> month3Letters;
        month3Letters = Generic_Time.getMonths3Letters();

        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
            String aSHBEFilename0;
            aSHBEFilename0 = SHBEFilenames[i - 1];
            String month0;
            month0 = DW_SHBE_Handler.getMonth(aSHBEFilename0);
            String year0;
            year0 = DW_SHBE_Handler.getYear(aSHBEFilename0);

            String aSHBEFilename1;
            aSHBEFilename1 = SHBEFilenames[i];
            String month;
            month = DW_SHBE_Handler.getMonth(aSHBEFilename1);
            String year;
            year = DW_SHBE_Handler.getYear(aSHBEFilename1);

            String filename;
            filename = "TenancyTypeTransition_All_Start_" + year0 + month0 + "_End_" + year + month + ".csv";
            File f;
            f = new File(dirIn, filename);
            if (!omit.contains(i + 1)) {
                Object[] tenancyTypeTransitionMatrixMinMaxSize;
                
                double timeDiff;
                timeDiff = Generic_Time.getMonthDiff(
                        Integer.valueOf(year0), 
                        Integer.valueOf(year),
                        Generic_Time.getMonth(month0,month3Letters),
                        Generic_Time.getMonth(month,month3Letters));
                //timeDiff = 1;
                tenancyTypeTransitionMatrixMinMaxSize = getTenancyTypeTransitionMatrixMinMaxSize(
                        f,
                        timeDiff);
                TreeMap<Integer, TreeMap<Integer, BigDecimal>> tenancyTypeTransitionMatrix;
                tenancyTypeTransitionMatrix = (TreeMap<Integer, TreeMap<Integer, BigDecimal>>) tenancyTypeTransitionMatrixMinMaxSize[0];
                Double tenancyTypeTransitionMatrixMin;
                tenancyTypeTransitionMatrixMin = (Double) tenancyTypeTransitionMatrixMinMaxSize[1];
                min = Math.min(tenancyTypeTransitionMatrixMin, min);
                Double tenancyTypeTransitionMatrixMax;
                tenancyTypeTransitionMatrixMax = (Double) tenancyTypeTransitionMatrixMinMaxSize[2];
                max = Math.max(tenancyTypeTransitionMatrixMax, max);
                size = Math.max(size, (Integer) tenancyTypeTransitionMatrixMinMaxSize[3]);
                String fileLabel;
                fileLabel = year + "_" + month;
                BigDecimal key;
                key = fileLabelValue.get(fileLabel);
                System.out.println("fileLabel " + fileLabel);
                System.out.println("key " + key);
                if (key != null) {
                    bigMatrix.put(key, tenancyTypeTransitionMatrix);
//                    System.out.println("max " + max);
//                    System.out.println("min " + min);
//                    System.out.println("size " + size);
                }
            } else {
                System.out.println("omitting file " + f.toString());
            }
        }

        System.out.println("max " + max);
        System.out.println("min " + min);

        File fout;
        fout = new File(
                DW_Files.getOutputSHBEPlotsDir(),
                "TenancyTransitionLineGraph.PNG");

        String title;
        title = "TenancyTransitionLineGraph";
        int dataWidth = 500;
        int dataHeight = 250;
        String xAxisLabel = "X";
        String yAxisLabel = "Y";
        int numberOfYAxisTicks = 11;
        BigDecimal yMax;
        yMax = null;
        BigDecimal yPin;
//        yPin = BigDecimal.ZERO;
        yPin = null;
        BigDecimal yIncrement;
//        yIncrement = BigDecimal.ONE;
        yIncrement = null;
        //int yAxisStartOfEndInterval = 60;
        int decimalPlacePrecisionForCalculations = 10;
        int decimalPlacePrecisionForDisplay = 3;
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        DW_LineGraph chart = new DW_LineGraph(
                executorService,
                fout,
                format,
                title,
                dataWidth,
                dataHeight,
                xAxisLabel,
                yAxisLabel,
                true,
                dataWidth,
                dataHeight,
                yMax,
                yPin,
                yIncrement,
                numberOfYAxisTicks,
                decimalPlacePrecisionForCalculations,
                decimalPlacePrecisionForDisplay,
                roundingMode);
        Object[] data = getData(
                bigMatrix,
                valueLabel,
                BigDecimal.valueOf(min),
                BigDecimal.valueOf(max));
        if (data != null) {
            chart.setData(data);
            chart.run();
            Future future = chart.future;
        }
        Generic_Execution.shutdownExecutorService(
                executorService, chart.future, this);
    }

    /**
     * TenureBefore, TenureNow Count
     *
     * @param f
     * @return null null null null null null null null     {@code
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
    public Object[] getTenancyTypeTransitionMatrixMinMaxSize(
            File f,
            double timeDiff) {
        Object[] result;
        result = new Object[4];
        TreeMap<Integer, TreeMap<Integer, BigDecimal>> map;
        map = new TreeMap<Integer, TreeMap<Integer, BigDecimal>>();
        result[0] = map;
        Double min = Double.MAX_VALUE;
        Double max = Double.MIN_VALUE;
        int size = 0;
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
                    Integer tenureBefore;
                    tenureBefore = Integer.valueOf(split[i]);
                    TreeMap<Integer, BigDecimal> transitions;
                    transitions = new TreeMap<Integer, BigDecimal>();
                    map.put(tenureBefore, transitions);
                    tenures.add(tenureBefore);
                }
                first = false;
            } else {
                Integer tenureAfter;
                tenureAfter = Integer.valueOf(split[0]);
                for (int i = 1; i < split.length; i++) {
                    Integer tenureBefore;
                    tenureBefore = tenures.get(i - 1);
                    Integer count;
                    count = Integer.valueOf(split[i]);
                    Double rate;
                    rate = count / timeDiff;
                    TreeMap<Integer, BigDecimal> transitions;
                    transitions = map.get(tenureBefore);
                    transitions.put(tenureAfter, BigDecimal.valueOf(rate));
                    if (tenureBefore.compareTo(tenureAfter) != 0) {
                        min = Math.min(min, rate);
                        max = Math.max(max, rate);
                        size++;
                    }
                }
            }
        }
        System.out.println("max " + max);
        System.out.println("min " + min);
        System.out.println("size " + size);
        result[1] = min; // This has to be here because of unboxing!?
        result[2] = max;
        result[3] = size;
        return result;
    }

    private Object[] getData(
            TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, BigDecimal>>> bigMatrix,
            TreeMap<BigDecimal, String> xAxisLabels,
            BigDecimal minY,
            BigDecimal maxY) {
        Object[] result;
        result = new Object[7];
        TreeMap<String, TreeMap<BigDecimal, BigDecimal>> maps;
        maps = new TreeMap<String, TreeMap<BigDecimal, BigDecimal>>();
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
                        TreeMap<BigDecimal, BigDecimal> map;
                        map = maps.get(key);
                        if (map == null) {
                            map = new TreeMap<BigDecimal, BigDecimal>();
                            maps.put(key, map);
                        }
                        map.put(x, omatrix.get(tenureBefore));
                    }
                }
            }
        }
        BigDecimal minX = bigMatrix.firstKey();
        BigDecimal maxX = bigMatrix.lastKey();
        result[0] = maps;
        result[1] = minY;
        result[2] = maxY;
        result[3] = minX;
        result[4] = maxX;

        ArrayList<String> labels;
        labels = new ArrayList<String>();
//        labels.addAll(maps.keySet()); // This does not work as maps is gc resulting in labels becoming null for some reason.
        Iterator<String> iteS;
        iteS = maps.keySet().iterator();
        while (iteS.hasNext()) {
            String label;
            label = iteS.next();
            labels.add(label);
        }
        result[5] = labels;
        result[6] = xAxisLabels;
        return result;
    }
}
