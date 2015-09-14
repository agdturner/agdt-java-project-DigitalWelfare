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
        ArrayList<String> claimantTypes;
        claimantTypes = new ArrayList<String>();
        claimantTypes.add("HB");
        claimantTypes.add("CTB");
        ArrayList<String> levels;
        levels = new ArrayList<String>();
        levels.add("OA");
        levels.add("LSOA");
        levels.add("MSOA");
        levels.add("PostcodeUnit");
        levels.add("PostcodeSector");
        levels.add("PostcodeDistrict");

        initAreaCodes(levels);

        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add("All"); // Count of all claimants
////                allTypes.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add("OnFlow"); // These are people not claiming the previous month and that have not claimed before.
        types.add("ReturnFlow"); // These are people not claiming the previous month but that have claimed before.
        types.add("Stable"); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add("AllInChurn"); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add("AllOutChurn"); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        ArrayList<String> distanceTypes;
        distanceTypes = new ArrayList<String>();
        distanceTypes.add("InDistanceChurn"); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add("WithinDistanceChurn"); // A count of all claimants that have moved within this area.
        distanceTypes.add("OutDistanceChurn"); // A count of all claimants that have moved out from this area.

        // Tenure Type Groups
        ArrayList<String> tenureTypes;
        tenureTypes = new ArrayList<String>();
        tenureTypes.add("all");
        tenureTypes.add("regulated");
        tenureTypes.add("unregulated");

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        Object[] treeMapDateLabelSHBEFilename;
        treeMapDateLabelSHBEFilename = DW_SHBE_Handler.getTreeMapDateLabelSHBEFilenames();
        
        TreeMap<BigDecimal, String> valueLabel;
        valueLabel = (TreeMap<BigDecimal, String>) treeMapDateLabelSHBEFilename[0];
        TreeMap<String, BigDecimal> fileLabelValue;
        fileLabelValue = (TreeMap<String, BigDecimal>) treeMapDateLabelSHBEFilename[1];
        
        generateLineGraphs(
                SHBEFilenames,
                valueLabel,
                fileLabelValue,
                0,
                levels,
                claimantTypes,
                tenureTypes,
                types,
                distanceTypes,
                distances);
    }

    public void generateLineGraphs(
            String[] SHBEFilenames,
            TreeMap<BigDecimal, String> valueLabel,
            TreeMap<String, BigDecimal> fileLabelValue,
            int startIndex,
            ArrayList<String> levels,
            ArrayList<String> claimantTypes,
            ArrayList<String> tenureTypes,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances) {

        String format = "PNG";

        Iterator<String> levelsIte;
        Iterator<String> claimantTypesIte;
        Iterator<String> tenureTypesIte;
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;

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

        TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, Integer>>> bigMatrix;
        bigMatrix = new TreeMap<BigDecimal, TreeMap<Integer, TreeMap<Integer, Integer>>>();
        
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

            levelsIte = levels.iterator();
            while (levelsIte.hasNext()) {
                String level = levelsIte.next();

                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypesIte.next();

                    tenureTypesIte = tenureTypes.iterator();
                    while (tenureTypesIte.hasNext()) {
                        String tenure;
                        tenure = tenureTypesIte.next();
                        // Do types
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            File dir;
                            dir = new File(
                                    DW_Files.getOutputSHBEPlotsDir(),
                                    level);
                            dir = new File(
                                    dir,
                                    claimantType);
                            dir = new File(
                                    dir,
                                    type);
                            dir = new File(
                                    dir,
                                    tenure);
                            dir.mkdirs();
                            File fout;
                            fout = new File(
                                    dir,
                                    year + month + "BarChart.PNG");
                            String title;
                            title = year + " " + month + " Bar Chart";
                            xAxisLabel = type + " Count";
                            dir = new File(
                                    DW_Files.getGeneratedSHBEDir(level),
                                    type);
                            dir = new File(
                                    dir,
                                    claimantType);
                            dir = new File(
                                    dir,
                                    tenure);
                            File fin = new File(
                                    dir,
                                    "" + year + month + ".csv");
                            generateLineGraph(
                                    level,
                                    fout,
                                    fin,
                                    format,
                                    title);
                        }
                        // Do distance types
                        distanceTypesIte = distanceTypes.iterator();
                        while (distanceTypesIte.hasNext()) {
                            String distanceType;
                            distanceType = distanceTypesIte.next();
                            distancesIte = distances.iterator();
                            while (distancesIte.hasNext()) {
                                Double distanceThreshold = distancesIte.next();
                                File dir;
                                dir = DW_Files.getOutputSHBEPlotsDir();
                                dir = new File(
                                        dir,
                                        level);
                                dir = new File(
                                        dir,
                                        claimantType);
                                dir = new File(
                                        dir,
                                        distanceType);
                                dir = new File(
                                        dir,
                                        tenure);
                                dir = new File(
                                        dir,
                                        distanceThreshold.toString());
                                dir.mkdirs();
                                File fout;
                                fout = new File(
                                        dir,
                                        year + month + "BarChart.PNG");
                                String title;
                                title = year + " " + month + " Bar Chart";
                                xAxisLabel = distanceType + " " + distanceThreshold + " Count";
                                dir = new File(
                                        DW_Files.getGeneratedSHBEDir(level),
                                        distanceType);
                                dir = new File(
                                        dir,
                                        claimantType);
                                dir = new File(
                                        dir,
                                        tenure);
                                dir = new File(
                                        dir,
                                        "" + distanceThreshold);
                                File fin = new File(
                                        dir,
                                        "" + year + month + ".csv");
                                generateLineGraph(
                                        level,
                                        fout,
                                        fin,
                                        format,
                                        title);
                            }
                        }
                    }
                }
            }
        }
        Generic_Execution.shutdownExecutorService(
                executorService, future, this);
    }

    private void generateLineGraph(
            String level,
            File fout,
            File fin,
            String format,
            String title) {
//        try {
//            Generic_LineGraph chart = new Generic_LineGraph(
//                    executorService,
//                    fout,
//                    format,
//                    title,
//                    dataWidth,
//                    dataHeight,
//                    xAxisLabel,
//                    yAxisLabel,
//                    false,
//                    xIncrement,
//                    yMax,
//                    yPin,
//                    yIncrement,
//                    numberOfYAxisTicks,
//                    decimalPlacePrecisionForCalculations,
//                    decimalPlacePrecisionForDisplay,
//                    roundingMode);
//            Object[] data = getData(level, fin, numberOfBars, mc);
//            if (data != null) {
//                chart.setData(data);
//                chart.run();
//                Future future = chart.future;
//            }
//        } catch (OutOfMemoryError e) {
//            long time;
//            //time = 60000L;
//            time = 120000L;
//            System.out.println("OutOfMemory, waiting " + time / 1000 + " secs "
//                    + "before trying to generate bar chart again...");
//            Generic_Execution.waitSychronized(this, time); // wait a minute
//            System.out.println("...on we go.");
//            generateBarChart(
//                    level,
//                    fout,
//                    fin,
//                    format,
//                    title);
//        }
    }

    public Object[] getData(
            String level,
            File f,
            int numberOfBars,
            MathContext mc) {
        HashSet<String> areaCodes2;
        areaCodes2 = areaCodes.get(level);

        Object[] result;
        result = new Object[3];

        ArrayList<String> lines;
        lines = DW_Table.readCSV(f);

        TreeMap<String, BigDecimal> map;
        map = new TreeMap<String, BigDecimal>();

        Iterator<String> ite;
        ite = lines.iterator();
        String header = ite.next();
        String[] headerSplit = header.split(","); // Could set the xAxisLabel and yAxisLabels from this!
        while (ite.hasNext()) {
            String line;
            line = ite.next();
            String[] split;
            split = line.split(",");
            String areaCode = split[0].trim();
            map.put(areaCode, new BigDecimal(split[1].trim()));
        }
        // Add all the zeros
        Set<String> keySet;
        keySet = map.keySet();
        ite = areaCodes2.iterator();
        while (ite.hasNext()) {
            String areaCode;
            areaCode = ite.next();
            if (!keySet.contains(areaCode)) {
                map.put(areaCode, BigDecimal.ZERO);
            }
        }

        BigDecimal[] minMaxBigDecimal;
        minMaxBigDecimal = Generic_Collections.getMinMaxBigDecimal(map);
        BigDecimal intervalWidth;
//        intervalWidth = new BigDecimal(xIncrement);
        if (minMaxBigDecimal[1] != null) {
            if (numberOfBars == 0) {
                intervalWidth = BigDecimal.ONE;
            } else {
                intervalWidth = minMaxBigDecimal[1].subtract(minMaxBigDecimal[0]).divide(new BigDecimal(numberOfBars), mc);
            }
            Object[] intervalCountsLabelsMins;

            BigDecimal min = minMaxBigDecimal[0];
            //BigDecimal max = minMaxBigDEcimal[1];

            intervalCountsLabelsMins = null;
            try {
                intervalCountsLabelsMins = Generic_Collections.getIntervalCountsLabelsMins(
                        min, intervalWidth, map, mc);
            } catch (ArithmeticException e) {
                int debug = 1;
            }

            result[0] = intervalCountsLabelsMins;
            result[1] = minMaxBigDecimal;
            result[2] = intervalWidth;

            TreeMap<Integer, Integer> counts;
            counts = (TreeMap<Integer, Integer>) intervalCountsLabelsMins[0];
            TreeMap<Integer, String> labels;
            labels = (TreeMap<Integer, String>) intervalCountsLabelsMins[1];
            TreeMap<Integer, BigDecimal> mins;
            mins = (TreeMap<Integer, BigDecimal>) intervalCountsLabelsMins[2];
            System.out.println("value, count, label, min");
            Iterator<Integer> ite2;
            ite2 = counts.keySet().iterator();
            while (ite2.hasNext()) {
                Integer value = ite2.next();
                Integer count = counts.get(value);
                BigDecimal minInterval = mins.get(value);
                String label = labels.get(value);
                System.out.println("" + value + ", " + count + ", \"" + label + "\", " + minInterval);
            }
            return result;
        } else {
            System.out.println("File " + f + " contains no values!");
            return null;
        }

    }

    public void initAreaCodes(ArrayList<String> levels) {
        areaCodes = new HashMap<String, HashSet<String>>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level;
            level = ite.next();
            File dir;
            File fin;
            File fout;
            if (level.startsWith("Post")) {
                dir = new File(
                        DW_Files.getGeneratedPostcodeDir(),
                        "Leeds");
                dir = new File(
                        dir,
                        level);
                fin = new File(
                        dir,
                        "AreaCodes.csv");
                fout = new File(
                        dir,
                        "AreaCodes_HashSetString.thisFile");
            } else {
                dir = new File(
                        DW_Files.getInputCensus2011AttributeDataDir(level),
                        "Leeds");
                fin = new File(
                        dir,
                        "censusCodes.csv");
                dir = new File(
                        DW_Files.getGeneratedCensus2011Dir(level),
                        "AttributeData");
                dir = new File(
                        dir,
                        "Leeds");
                dir.mkdirs();
                fout = new File(
                        dir,
                        "AreaCodes_HashSetString.thisFile");
            }
            HashSet<String> areaCodesForLevel;
            if (fout.exists()) {
                areaCodesForLevel = (HashSet<String>) Generic_StaticIO.readObject(fout);
            } else {
                ArrayList<String> lines;
                lines = DW_Table.readCSV(fin);
                areaCodesForLevel = new HashSet<String>();
                areaCodesForLevel.addAll(lines);
                Generic_StaticIO.writeObject(areaCodesForLevel, fout);
            }
            areaCodes.put(level, areaCodesForLevel);
        }
    }

}
