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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.chart.execution.Generic_Execution;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import uk.ac.leeds.ccg.andyt.generic.visualisation.charts.Generic_BarChart;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_BarChart extends Generic_BarChart {

    transient DW_Environment Env;
    DW_Strings Strings;
    private int dataWidth;
    private int dataHeight;
    private String xAxisLabel;
    private String yAxisLabel;
    private int barGap;
    private int xIncrement;
    private int numberOfBars;
    private int numberOfYAxisTicks;
    private BigDecimal yPin;
    private BigDecimal yMax;
    private BigDecimal yIncrement;
//    private MathContext mc;
    //private ExecutorService executorService;

    HashMap<String, HashSet<String>> areaCodes;

    public DW_BarChart(DW_Environment env){
        this.Env = env;
        this.Strings = env.getStrings();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DW_BarChart(null).run(args);
    }

    /**
     *
     * @param args These are ignored!
     */
    public void run(String[] args) {
        Generic_Visualisation.getHeadlessEnvironment();
        dataWidth = 400;
        dataHeight = 200;
        xAxisLabel = "Claimant Count";
        yAxisLabel = "Count of Areas";
        //boolean drawOriginLinesOnPlot = true;
        barGap = 1;
//        xIncrement = 10;
//        numberOfBars = 50;
        numberOfYAxisTicks = 5;
        yPin = BigDecimal.ZERO;
//        yMax = new BigDecimal(700);
        yMax = null;
//        yIncrement = BigDecimal.TEN;
//        yIncrement = BigDecimal.ONE;
        yIncrement = null;
        //int yAxisStartOfEndInterval = 60;
        setDecimalPlacePrecisionForCalculations(20);
        //int decimalPlacePrecisionForDisplay = 3;
        setRoundingMode(RoundingMode.HALF_UP);
        executorService = Executors.newSingleThreadExecutor();

        String[] SHBEFilenames;
        SHBEFilenames = Env.getSHBE_Handler().getSHBEFilenamesAll();
        ArrayList<String> claimantTypes;
        claimantTypes = Strings.getHB_CTB();
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

        int startIndex; // For restarting runs at a point.
        startIndex = 0;
//        startIndex = 19;

        ArrayList<Boolean> b;
        b = new ArrayList<Boolean>();
        b.add(true);
        b.add(false);

        Iterator<Boolean> iteb;
        iteb = b.iterator();
        while (iteb.hasNext()) {
            boolean doUnderOccupied;
            doUnderOccupied = iteb.next();
            if (doUnderOccupied) {
                Iterator<Boolean> iteb2;
                iteb2 = b.iterator();
                while (iteb2.hasNext()) {
                    boolean doCouncil;
                    doCouncil = iteb.next();
                    generateBarCharts(
                            doUnderOccupied,
                            doCouncil,
                            SHBEFilenames,
                            startIndex,
                            levels,
                            claimantTypes,
                            tenureTypes,
                            types,
                            distanceTypes,
                            distances);
                }
            } else {
                generateBarCharts(
                        false,
                        false,
                        SHBEFilenames,
                        startIndex,
                        levels,
                        claimantTypes,
                        tenureTypes,
                        types,
                        distanceTypes,
                        distances);
            }
        }
    }

    public void generateBarCharts(
            boolean doUnderOccupied,
            boolean doCouncil,
            String[] SHBEFilenames,
            int startIndex,
            ArrayList<String> levels,
            ArrayList<String> claimantTypes,
            ArrayList<String> tenureTypes,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances) {

        String format = "PNG";
        DW_Files tDW_Files;
        tDW_Files = Env.getFiles();
        File dirOut;
        dirOut = new File(
                tDW_Files.getOutputSHBEPlotsDir(),
                "BarCharts");
        dirOut = tDW_Files.getUODir(dirOut, doUnderOccupied, doCouncil);
        Iterator<String> levelsIte;
        Iterator<String> claimantTypesIte;
        Iterator<String> tenureTypesIte;
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;

        DW_SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = Env.getSHBE_Handler();
        
        for (int i = startIndex + 1; i < SHBEFilenames.length; i++) {
            String aSHBEFilename = SHBEFilenames[i];
            String month = tDW_SHBE_Handler.getMonth(aSHBEFilename);
            String year = tDW_SHBE_Handler.getYear(aSHBEFilename);

            levelsIte = levels.iterator();
            while (levelsIte.hasNext()) {
                String level = levelsIte.next();
                File dirOut2 = new File(
                        dirOut,
                        level);
                claimantTypesIte = claimantTypes.iterator();
                while (claimantTypesIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypesIte.next();
                    File dirOut3 = new File(
                            dirOut2,
                            claimantType);
                    tenureTypesIte = tenureTypes.iterator();
                    while (tenureTypesIte.hasNext()) {
                        String tenure;
                        tenure = tenureTypesIte.next();
                        // Do types
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            File dirOut4 = new File(
                                    dirOut3,
                                    type);
                            dirOut4 = new File(
                                    dirOut4,
                                    tenure);
                            dirOut4.mkdirs();
                            File fout;
                            fout = new File(
                                    dirOut4,
                                    year + month + "BarChart.PNG");
                            String title;
                            title = year + " " + month + " Bar Chart";
                            xAxisLabel = type + " Count";
                            File dirIn = new File(
                                    tDW_Files.getGeneratedSHBEDir(
                                            level,
                                            doUnderOccupied,
                                            doCouncil),
                                    type);
                            dirIn = new File(
                                    dirIn,
                                    claimantType);
                            dirIn = new File(
                                    dirIn,
                                    tenure);
                            File fin = new File(
                                    dirIn,
                                    "" + year + month + ".csv");
                            generateBarChart(
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
                                dir = tDW_Files.getOutputSHBEPlotsDir();
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
                                        tDW_Files.getGeneratedSHBEDir(
                                                level,
                                                doUnderOccupied,
                                                doCouncil),
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
                                generateBarChart(
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

    private void generateBarChart(
            String level,
            File fout,
            File fin,
            String format,
            String title) {
        try {
            Generic_BarChart chart = new Generic_BarChart(
                    executorService,
                    fout,
                    format,
                    title,
                    dataWidth,
                    dataHeight,
                    xAxisLabel,
                    yAxisLabel,
                    false,
                    barGap,
                    xIncrement,
                    yMax,
                    yPin,
                    yIncrement,
                    numberOfYAxisTicks,
                    getDecimalPlacePrecisionForCalculations(),
                    getDecimalPlacePrecisionForDisplay(),
                    getRoundingMode());
            Object[] data = getData(level, fin, numberOfBars);
            if (data != null) {
                chart.setData(data);
                chart.run();
                Future future = chart.future;
            }
        } catch (OutOfMemoryError e) {
            long time;
            //time = 60000L;
            //time = 120000L;
            time = 240000L;
            System.out.println("OutOfMemory, waiting " + time / 1000 + " secs "
                    + "before trying to generate bar chart again...");
            Generic_Execution.waitSychronized(this, time); // wait a minute
            System.out.println("...on we go.");
            generateBarChart(
                    level,
                    fout,
                    fin,
                    format,
                    title);
        }
    }

    public Object[] getData(String level, File f, int numberOfBars) {
        Object[] result;
        result = new Object[3];

        ArrayList<String> lines;
        lines = DW_Table.readCSV(f);

        MathContext mc;
        mc = new MathContext(
                getDecimalPlacePrecisionForCalculations(),
                getRoundingMode());
        HashSet<String> areaCodes2;
        areaCodes2 = areaCodes.get(level);

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
        DW_Files tDW_Files;
        tDW_Files = Env.getFiles();
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
                        tDW_Files.getGeneratedPostcodeDir(),
                        "Leeds");
                dir = new File(
                        dir,
                        level);
                fin = new File(
                        dir,
                        "AreaCodes.csv");
                fout = new File(
                        dir,
                        "AreaCodes_HashSetString" + Strings.sBinaryFileExtension);
            } else {
                dir = new File(
                        tDW_Files.getInputCensus2011AttributeDataDir(level),
                        "Leeds");
                fin = new File(
                        dir,
                        "censusCodes.csv");
                dir = new File(
                        tDW_Files.getGeneratedCensus2011Dir(level),
                        "AttributeData");
                dir = new File(
                        dir,
                        "Leeds");
                dir.mkdirs();
                fout = new File(
                        dir,
                        "AreaCodes_HashSetString" + Strings.sBinaryFileExtension);
            }
            HashSet<String> areaCodesForLevel;
            if (fout.exists()) {
                areaCodesForLevel = (HashSet<String>) Generic_IO.readObject(fout);
            } else {
                ArrayList<String> lines;
                lines = DW_Table.readCSV(fin);
                areaCodesForLevel = new HashSet<String>();
                areaCodesForLevel.addAll(lines);
                Generic_IO.writeObject(areaCodesForLevel, fout);
            }
            areaCodes.put(level, areaCodesForLevel);
        }
    }
}
