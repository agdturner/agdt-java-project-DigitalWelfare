/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps_LCC extends DW_ChoroplethMaps {

    private double distanceThreshold;

    public DW_ChoroplethMaps_LCC(DW_Environment env) {
        super(env);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_ChoroplethMaps_LCC(null).run();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    /**
     *
     */
    public void run() {
        showMapsInJMapPane = false;
//        showMapsInJMapPane = true;
        //The size of the map images in pixels (this does not include the legends)
        imageWidth = 2000;
        // Initialise Parameters
        // Style Parameters
        /*
         * YlOrRd,PRGn,PuOr,RdGy,Spectral,Grays,PuBuGn,RdPu,BuPu,YlOrBr,Greens,
         * BuGn,Accents,GnBu,PuRd,Purples,RdYlGn,Paired,Blues,RdBu,Oranges,
         * RdYlBu,PuBu,OrRd,Set3,Set2,Set1,Reds,PiYG,Dark2,YlGn,BrBG,YlGnBu,
         * Pastel2,Pastel1
         */
//        ColorBrewer brewer = ColorBrewer.instance();
//        //String[] paletteNames = brewer.getPaletteNames(0, nClasses);
//        String[] paletteNames = brewer.getPaletteNames();
//        for (int i = 0; i < paletteNames.length; i++) {
//            System.out.println(paletteNames[i]);
//        }
        styleParameters = new AGDT_StyleParameters();
        styleParameters.setnClasses(9);
        styleParameters.setPaletteName("Reds");
        styleParameters.setAddWhiteForZero(true);
//        styleParameters.setDoForeground(true);
        styleParameters.setDoForeground(false);
//        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
//        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.DARK_GRAY,
                //Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
        styleParameters.setBackgroundStyle(DW_Style.createDefaultPolygonStyle(
                Color.BLACK,
                Color.WHITE));
        styleParameters.setBackgroundStyleTitle("Background Style");
        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();

        // Switches for different stylings
        // -------------------------------
        // commonStyling is where all maps are produced according to a common
        // classification
//        commonStyling = true;
        commonStyling = false;
        // individualStyling is where all maps are produced according to an 
        // individual classification
        individualStyling = true;
//        individualStyling = false;
        // withoutBoundariesStyling does not draw an outline to the boundaries  
        // of each region. 
        withoutBoundariesStyling = true;
        // withBoundariesStyling draws an outline to the boundaries of each 
        // region. 
//        withBoundariesStyling = true;
        // Initialise classificationFunctionNames
        classificationFunctionNames = new ArrayList<String>();
//        classificationFunctionNames.add("Jenks");
//        classificationFunctionNames.add("Quantile");
        classificationFunctionNames.add("EqualInterval");
        doCount = true;
//        doCount = false;
//        doDensity = true;
        doDensity = false;
        doFilter = new boolean[4];
        doFilter[0] = true;
//        doFilter[0] = false;
//        doFilter[1] = true;
        doFilter[1] = false;
//        doFilter[2] = true;
        doFilter[2] = false;
//        doFilter[3] = true;
        doFilter[3] = false;
        // Initialise levels and targetPropertyNames. 
        levels = new ArrayList<String>();
        targetPropertyNames = new HashMap<String, String>();

        ArrayList<String> claimantTypes;
        claimantTypes = new ArrayList<String>();
        claimantTypes.add("HB");
        claimantTypes.add("CTB");

        ArrayList<String> types;
        types = new ArrayList<String>();
        types.add("All"); // Count of all claimants
//                allTypes.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
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

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
            //for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        // A useful indication of places from where people are displaced?
        //types[7] = "Unknown"; // Not worth doing?
        // OA
        level = "OA";
        levels.add(level);
        targetPropertyNames.put(level, "CODE");
        // LSOA
        level = "LSOA";
        levels.add(level);
        targetPropertyNames.put(level, "LSOA11CD");
        // MSOA
        level = "MSOA";
        levels.add(level);
        targetPropertyNames.put(level, "MSOA11CD");
////        // Postcode Area
////        level = "PostcodeArea";
////        levels.add(level);
////        targetPropertyNames.put(level, "PostArea");
        // Postcodes Unit 
        level = "PostcodeUnit";
        levels.add(level);
        targetPropertyNames.put(level, "POSTCODE");
        // Postcode Sector
        level = "PostcodeSector";
        levels.add(level);
        targetPropertyNames.put(level, "RMSect");
        // Postcode District
        level = "PostcodeDistrict";
        levels.add(level);
        targetPropertyNames.put(level, "PostDist");

        // Tenure Type Groups
        ArrayList<String> tenureTypeGroups;
        tenureTypeGroups = new ArrayList<String>();
        tenureTypeGroups.add("all");
        tenureTypeGroups.add("regulated");
        tenureTypeGroups.add("unregulated");
        mapDirectory = new File(
                tDW_Files.getOutputSHBEDir(),
                "Maps");
        mapDirectory = new File(
                mapDirectory,
                "Choropleth");

        TreeMap<String, ArrayList<Integer>> includes;
        includes = env.getDW_SHBE_Handler().getIncludes();
//        includes.remove("All");
//        includes.remove("Yearly");
//        includes.remove("6Monthly");
//        includes.remove("3Monthly");
//        includes.remove("MonthlyUO");
//        includes.remove("Monthly");

        ArrayList<Integer> include;
        ArrayList<Integer> includea;
        // Run for consecutive monthly data
        includea = includes.get("Monthly");
        include = includea;
//        // Run for all monthly data other than Monthly
//        include = includes.get("All");
//        include.removeAll(includea);

        ArrayList<Boolean> b;
        b = new ArrayList<Boolean>();
        b.add(true);
        b.add(false);

        String[] tFilenames;
        tFilenames = getFilenames();

        Iterator<Boolean> iteb;
        iteb = b.iterator();
        while (iteb.hasNext()) {
            boolean doUnderOcupied;
            doUnderOcupied = iteb.next();
            if (doUnderOcupied) {
                Iterator<Boolean> iteb2;
                iteb2 = b.iterator();
                while (iteb2.hasNext()) {
                    boolean doCouncil;
                    doCouncil = iteb2.next();
                    if (withoutBoundariesStyling) {
                        runAll(
                                doUnderOcupied,
                                doCouncil,
                                false,
                                include,
                                types,
                                distanceTypes,
                                distances,
                                tenureTypeGroups,
                                claimantTypes,
                                tFilenames);
                    }
                    if (withBoundariesStyling) {
                        runAll(
                                doUnderOcupied,
                                doCouncil,
                                true,
                                include,
                                types,
                                distanceTypes,
                                distances,
                                tenureTypeGroups,
                                claimantTypes,
                                tFilenames);
                    }
                }
            } else {
                if (withoutBoundariesStyling) {
                    runAll(
                            false,
                            false,
                            false,
                            include,
                            types,
                            distanceTypes,
                            distances,
                            tenureTypeGroups,
                            claimantTypes,
                            tFilenames);
                }
                if (withBoundariesStyling) {
                    runAll(
                            false,
                            false,
                            true,
                            include,
                            types,
                            distanceTypes,
                            distances,
                            tenureTypeGroups,
                            claimantTypes,
                            tFilenames);
                }
            }
        }
    }

    public void runAll(
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean drawBoundaries,
            ArrayList<Integer> include,
            ArrayList<String> types,
            ArrayList<String> distanceTypes,
            ArrayList<Double> distances,
            ArrayList<String> tenureTypeGroups,
            ArrayList<String> claimantTypes,
            String[] tFilenames) {
        styleParameters.setDrawBoundaries(drawBoundaries);

        Iterator<String> claimantTypeIte;
        Iterator<String> levelsIte;
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;

        levelsIte = levels.iterator();
        while (levelsIte.hasNext()) {
            level = levelsIte.next();
            targetPropertyName = targetPropertyNames.get(level);
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles;
            tAreaCodesAndShapefiles = new DW_AreaCodesAndShapefiles(
                   env,
                 level,
                    targetPropertyName,
                    getShapefileDataStoreFactory());
            TreeMap<String, Deprivation_DataRecord> deprivationRecords;
            Iterator<String> classificationFunctionNamesIte;
            classificationFunctionNamesIte = classificationFunctionNames.iterator();
            while (classificationFunctionNamesIte.hasNext()) {
                String classificationFunctionName;
                classificationFunctionName = classificationFunctionNamesIte.next();
                styleParameters.setClassificationFunctionName(classificationFunctionName);
                Iterator<String> tenureTypeGroupsIte = tenureTypeGroups.iterator();

                /* Calculate the max value for each main type of common 
                 * styling. This is used in Equal interval shaders for 
                 * other types of shader it would be better to compose all 
                 * the data together and then calculate the natural breaks 
                 * or standard deviations or quintiles or whatever.
                 * It would be computationally wise to calculate this max once 
                 * for each dataset and store it for when only the max is 
                 * needed. It could then be read in each time rather than 
                 * having to be recalculated. For more complex classification 
                 * shaders, it might always be necessary to compose all the 
                 * data to work out the common intervals. The most problematic
                 */
                TreeMap<String, TreeMap<String, Double>> claimantTypeTypeMax;
                claimantTypeTypeMax = new TreeMap<String, TreeMap<String, Double>>();
                TreeMap<String, TreeMap<String, TreeMap<Double, Double>>> claimantTypeDistanceTypeDistanceMax;
                claimantTypeDistanceTypeDistanceMax = new TreeMap<String, TreeMap<String, TreeMap<Double, Double>>>();
                if (commonStyling) {
                    claimantTypeIte = claimantTypes.iterator();
                    while (claimantTypeIte.hasNext()) {
                        String claimantType;
                        claimantType = claimantTypeIte.next();
                        TreeMap<String, Double> typeMax;
                        typeMax = new TreeMap<String, Double>();
                        claimantTypeTypeMax.put(claimantType, typeMax);
                        // Do types
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            File dirIn = new File(
                                    tDW_Files.getGeneratedSHBEDir(
                                            level,
                                            doUnderOccupied,
                                            doCouncil),
                                    type);
                            dirIn = new File(dirIn,
                                    claimantType);
                            tenureTypeGroupsIte = tenureTypeGroups.iterator();
                            double max = Double.MIN_VALUE;
                            while (tenureTypeGroupsIte.hasNext()) {
                                String tenure;
                                tenure = tenureTypeGroupsIte.next();
                                File dirIn2 = new File(
                                        dirIn,
                                        tenure);
                                Object[] tLevelData = getLevelData(
                                        dirIn2,
                                        tFilenames,
                                        include);
                                if (tLevelData != null) {
                                    Object[] aLevelData = (Object[]) tLevelData[0];
                                    for (Object aLevelData1 : aLevelData) {
                                        if (aLevelData1 != null) {
                                            Object[] data;
                                            data = (Object[]) aLevelData1;
                                            Integer maxInt = (Integer) data[1];
                                            max = Math.max(max, maxInt);
                                        }
                                    }
                                }
//                                System.out.println(
//                                        "Max " + max
//                                        + ", Level " + level
//                                        + ", Type " + type
//                                        + ", Tenure " + tenure);
                            }
                            typeMax.put(type, max);
                        }
                        // Do distanceTypes
                        TreeMap<String, TreeMap<Double, Double>> distanceTypeDistanceMax;
                        distanceTypeDistanceMax = new TreeMap<String, TreeMap<Double, Double>>();
                        claimantTypeDistanceTypeDistanceMax.put(claimantType, distanceTypeDistanceMax);
                        distanceTypesIte = distanceTypes.iterator();
                        while (distanceTypesIte.hasNext()) {
                            String type;
                            type = distanceTypesIte.next();
                            TreeMap<Double, Double> distanceMax;
                            distanceMax = new TreeMap<Double, Double>();
                            distanceTypeDistanceMax.put(type, distanceMax);
                            File dirIn = new File(
                                    tDW_Files.getGeneratedSHBEDir(
                                            level,
                                            doUnderOccupied,
                                            doCouncil),
                                    type);
                            dirIn = new File(dirIn,
                                    claimantType);
                            tenureTypeGroupsIte = tenureTypeGroups.iterator();
                            while (tenureTypeGroupsIte.hasNext()) {
                                String tenure;
                                tenure = tenureTypeGroupsIte.next();
                                File dirIn2 = new File(
                                        dirIn,
                                        tenure);
                                distancesIte = distances.iterator();
                                while (distancesIte.hasNext()) {
                                    distanceThreshold = distancesIte.next();
                                    double max = Double.MIN_VALUE;
                                    if (distanceMax.containsKey(distanceThreshold)) {
                                        max = distanceMax.get(distanceThreshold);
                                    }
                                    File dirIn3 = new File(
                                            dirIn2,
                                            "" + distanceThreshold);
                                    Object[] tLevelData = getLevelData(
                                            dirIn3,
                                            tFilenames,
                                            include);
                                    if (tLevelData != null) {
                                        Object[] aLevelData = (Object[]) tLevelData[0];
                                        for (Object aLevelData1 : aLevelData) {
                                            if (aLevelData1 != null) {
                                                Object[] data;
                                                data = (Object[]) aLevelData1;
                                                Integer maxInt = (Integer) data[1];
                                                max = Math.max(max, maxInt);
                                            }
                                        }
                                        distanceMax.put(distanceThreshold, max);
//                                    System.out.println(
//                                            "Max " + max
//                                            + ", Level " + level
//                                            + ", Type " + type
//                                            + ", Tenure " + tenure);
                                    }
                                }
                            }
                        }
                    }
                }
                claimantTypeIte = claimantTypes.iterator();
                while (claimantTypeIte.hasNext()) {
                    String claimantType;
                    claimantType = claimantTypeIte.next();
                    // Do types
                    typesIte = types.iterator();
                    while (typesIte.hasNext()) {
                        String type;
                        type = typesIte.next();
                        File dirIn = new File(
                                tDW_Files.getGeneratedSHBEDir(
                                        level,
                                        doUnderOccupied,
                                        doCouncil),
                                type);
                        tenureTypeGroupsIte = tenureTypeGroups.iterator();
                        while (tenureTypeGroupsIte.hasNext()) {
                            String tenure = tenureTypeGroupsIte.next();
                            boolean scaleToFirst;
                            File dirIn2;
                            dirIn2 = new File(
                                    dirIn,
                                    claimantType);
                            dirIn2 = new File(
                                    dirIn2,
                                    tenure);
                            Object[] tLevelData = getLevelData(
                                    dirIn2,
                                    tFilenames,
                                    include);
                            if (tLevelData != null) {
                                if (individualStyling) {
                                    scaleToFirst = false;
                                    // Map SHBE data
                                    deprivationRecords = null;
                                    runSHBE(
                                            doUnderOccupied,
                                            doCouncil,
                                            deprivationRecords,
                                            tAreaCodesAndShapefiles,
                                            tFilenames,
                                            include,
                                            tLevelData,
                                            type,
                                            scaleToFirst,
                                            0, // Can be any value
                                            tenure,
                                            claimantType);
                                    if (doDeprivation) {
                                        deprivationRecords = DW_Processor.getDeprivation_Data(env);
                                        runSHBE(
                                                doUnderOccupied,
                                                doCouncil,
                                                deprivationRecords,
                                                tAreaCodesAndShapefiles,
                                                tFilenames,
                                                include,
                                                tLevelData,
                                                type,
                                                scaleToFirst,
                                                0, // Can be any value
                                                tenure,
                                                claimantType);
                                    }
                                }
                                if (commonStyling) {
                                    scaleToFirst = true;
                                    // Map SHBE data
                                    deprivationRecords = null;
                                    double max;
                                    max = claimantTypeTypeMax.get(claimantType).get(type);
                                    runSHBE(
                                            doUnderOccupied,
                                            doCouncil,
                                            deprivationRecords,
                                            tAreaCodesAndShapefiles,
                                            tFilenames,
                                            include,
                                            tLevelData,
                                            type,
                                            scaleToFirst,
                                            max,
                                            tenure,
                                            claimantType);
                                    // Clear commonStyle?
                                    if (doDeprivation) {
                                        deprivationRecords = DW_Processor.getDeprivation_Data(env);
                                        runSHBE(
                                                doUnderOccupied,
                                                doCouncil,
                                                deprivationRecords,
                                                tAreaCodesAndShapefiles,
                                                tFilenames,
                                                include,
                                                tLevelData,
                                                type,
                                                scaleToFirst,
                                                max,
                                                tenure,
                                                claimantType);
                                        // Clear commonStyle?
                                    }
                                }
//                            if (!showMapsInJMapPane) {
//                                // Tidy up
//                                tAreaCodesAndShapefiles.dispose();
//                            }
                            }
                        }
                        if (commonStyling) {
                            String attributeName;
                            if (doCount) {
                                attributeName = "Count";
                                styleParameters.setStyle(attributeName, null, 0);
                            }
                            if (doDensity) {
                                attributeName = "Density";
                                styleParameters.setStyle(attributeName, null, 0);
                            }
                        }
                    }
                    // do distanceTypes
                    distanceTypesIte = distanceTypes.iterator();
                    while (distanceTypesIte.hasNext()) {
                        String type;
                        type = distanceTypesIte.next();
                        File dirIn = new File(
                                tDW_Files.getGeneratedSHBEDir(
                                        level,
                                        doUnderOccupied,
                                        doCouncil),
                                type);
                        tenureTypeGroupsIte = tenureTypeGroups.iterator();
                        while (tenureTypeGroupsIte.hasNext()) {
                            String tenure = tenureTypeGroupsIte.next();
                            boolean scaleToFirst;
                            File dirIn2;
                            dirIn2 = new File(
                                    dirIn,
                                    claimantType);
                            dirIn2 = new File(
                                    dirIn2,
                                    tenure);
                            distancesIte = distances.iterator();
                            while (distancesIte.hasNext()) {
                                distanceThreshold = distancesIte.next();
                                File dirIn3 = new File(
                                        dirIn2,
                                        "" + distanceThreshold);
                                Object[] tLevelData = getLevelData(
                                        dirIn3,
                                        tFilenames,
                                        include);
                                if (tLevelData != null) {
                                    if (individualStyling) {
                                        scaleToFirst = false;
                                        // Map SHBE data
                                        deprivationRecords = null;
                                        runSHBE(
                                                doUnderOccupied,
                                                doCouncil,
                                                deprivationRecords,
                                                tAreaCodesAndShapefiles,
                                                tFilenames,
                                                include,
                                                tLevelData,
                                                type,
                                                scaleToFirst,
                                                0, // Can be anything
                                                tenure,
                                                claimantType);
                                        if (doDeprivation) {
                                            deprivationRecords = DW_Processor.getDeprivation_Data(env);
                                            runSHBE(
                                                    doUnderOccupied,
                                                    doCouncil,
                                                    deprivationRecords,
                                                    tAreaCodesAndShapefiles,
                                                    tFilenames,
                                                    include,
                                                    tLevelData,
                                                    type,
                                                    scaleToFirst,
                                                    0, // Can be anything
                                                    tenure,
                                                    claimantType);
                                        }
                                    }
                                    if (commonStyling) {
                                        scaleToFirst = true;
                                        // Map SHBE data
                                        double max;
                                        max = claimantTypeDistanceTypeDistanceMax.get(claimantType).get(type).get(distanceThreshold);
                                        deprivationRecords = null;
                                        runSHBE(
                                                doUnderOccupied,
                                                doCouncil,
                                                deprivationRecords,
                                                tAreaCodesAndShapefiles,
                                                tFilenames,
                                                include,
                                                tLevelData,
                                                type,
                                                scaleToFirst,
                                                max,
                                                tenure,
                                                claimantType);
                                        // Clear commonStyle

                                        if (doDeprivation) {
                                            deprivationRecords = DW_Processor.getDeprivation_Data(env);
                                            runSHBE(
                                                    doUnderOccupied,
                                                    doCouncil,
                                                    deprivationRecords,
                                                    tAreaCodesAndShapefiles,
                                                    tFilenames,
                                                    include,
                                                    tLevelData,
                                                    type,
                                                    scaleToFirst,
                                                    max,
                                                    tenure,
                                                    claimantType);
                                            // Clear commonStyle
                                        }
                                    }
                                }
                            }
//                            if (!showMapsInJMapPane) {
//                                // Tidy up
//                                tAreaCodesAndShapefiles.dispose();
//                            }
                        }
                        if (commonStyling) {
                            String attributeName;
                            if (doCount) {
                                attributeName = "Count";
                                styleParameters.setStyle(attributeName, null, 0);
                            }
                            if (doDensity) {
                                attributeName = "Density";
                                styleParameters.setStyle(attributeName, null, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    public void runSHBE(
            boolean doUnderOccupied,
            boolean doCouncil,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tFilenames,
            ArrayList<Integer> include,
            Object[] tLevelData,
            String type,
            boolean scaleToFirst,
            double max,
            String tenure,
            String claimantType) {
        int filter = 0;
        String style;
        if (scaleToFirst) {
            style = "CommonlyStyled";
        } else {
            style = "IndividuallyStyled";
        }
        File dirOut;
        dirOut = new File(
                mapDirectory,
                level);
        dirOut = tDW_Files.getUOFile(dirOut, doUnderOccupied, doCouncil);
        dirOut = new File(
                dirOut,
                claimantType);
        dirOut = new File(
                dirOut,
                tenure);
        dirOut = new File(
                dirOut,
                type);
        if (styleParameters.isDrawBoundaries()) {
            dirOut = new File(
                    dirOut,
                    "WithBoundaries");
        } else {
            dirOut = new File(
                    dirOut,
                    "WithoutBoundaries");
        }
        dirOut = new File(
                dirOut,
                style);
        dirOut = new File(
                dirOut,
                styleParameters.getClassificationFunctionName());
        if (type.contains("Distance")) {
            dirOut = new File(
                    dirOut,
                    "" + distanceThreshold);
        }
        TreeMap<String, TreeMap<Integer, Integer>> inAndOutOfRegionCounts = null;
        String attributeName;
        Class<?> binding;
        if (doCount) {
            attributeName = "Count";
            binding = Integer.class;
            inAndOutOfRegionCounts = mapCountsForLevel(
                    tAreaCodesAndShapefiles,
                    tFilenames,
                    include,
                    tLevelData,
                    deprivationRecords,
                    dirOut,
                    attributeName,
                    binding,
                    filter,
                    scaleToFirst,
                    max,
                    true);
//This is now done higher up            // Write out in and out region counts
//            System.out.println("outname, total, in, out");
//            Iterator<String> ite;
//            ite = inAndOutOfRegionCounts.keySet().iterator();
//            while (ite.hasNext()) {
//                String outname = ite.next();
//                TreeMap<Integer, Integer> inAndOutOfRegionCount;
//                inAndOutOfRegionCount = inAndOutOfRegionCounts.get(outname);
//                Integer count;
//                Integer inCount;
//                Integer outCount;
//                outCount = inAndOutOfRegionCount.get(1);
//                inCount = inAndOutOfRegionCount.get(1);
//                count = outCount + inCount;
//                System.out.println("" + outname + ", " + count + ", " + inCount + ", " + outCount);
//            }

//            if (scaleToFirst) {
//                styleParameters.setStyle(attributeName, null, 0);
//            }
        }
        if (doDensity) {
            /*
             * May want to set max to something different. This will always be 
             * honest. The maz is used to set the label in the legend, so until 
             * we have caluclated the densities for each map, we do not know 
             * what the max is, so it is hard to set to something precise at the 
             * outset without it being erroneous. Ideally it would be set to be 
             * something specific.
             */
            attributeName = "Density";
            binding = Double.class;
            inAndOutOfRegionCounts = mapDensitiesForLevel(
                    tAreaCodesAndShapefiles,
                    tFilenames,
                    include,
                    tLevelData,
                    deprivationRecords,
                    dirOut,
                    attributeName,
                    binding,
                    filter,
                    scaleToFirst,
                    Double.POSITIVE_INFINITY,
                    false);
            if (scaleToFirst) {
                styleParameters.setStyle(attributeName, null, 0);
            }
        }

//        // Rates
//        scaleToFirst = false;
//        int max;
//        max = 0;
//        File dirForRates = new File(
//                dir,
//                "Rates");
//        double multiplier = 1000.0d;
//        attributeName = "Rate";
//        binding = Double.class;
//        mapRatesForLevel(
//                tAreaCodesAndShapefiles,
//                tFilenames,
//                tLevelData,
//                dirForRates,
//                attributeName,
//                binding,
//                multiplier,
//                max,
//                filter,
//                scaleToFirst);
//
//        // Counts
//        scaleToFirst = false;
//        File dirForCounts = new File(
//                dir,
//                "Counts");
    }

    public static String[] getFilenames() {
        String[] result = new String[42];
        //result[0] = "2013April.csv";
        //result[0] = "2014July.csv";
        //result[0] = "2014Apr.csv";
        result[0] = "2008April.csv";
        result[1] = "2008October.csv";
        result[2] = "2009April.csv";
        result[3] = "2009October.csv";
        result[4] = "2010April.csv";
        result[5] = "2010October.csv";
        result[6] = "2011March.csv";
        result[7] = "2011April.csv";
        result[8] = "2011July.csv";
        result[9] = "2011October.csv";
        result[10] = "2012January.csv";
        result[11] = "2012April.csv";
        result[12] = "2012July.csv";
        result[13] = "2012October.csv";
        result[14] = "2013January.csv";
        result[15] = "2013February.csv";
        result[16] = "2013March.csv";
        result[17] = "2013April.csv";
        result[18] = "2013May.csv";
        result[19] = "2013June.csv";
        result[20] = "2013July.csv";
        result[21] = "2013August.csv";
        result[22] = "2013September.csv";
        result[23] = "2013Oct.csv";
        result[24] = "2013Nov.csv";
        result[25] = "2013Dec.csv";
        result[26] = "2014Jan.csv";
        result[27] = "2014Feb.csv";
        result[28] = "2014Mar.csv";
        result[29] = "2014Apr.csv";
        result[30] = "2014May.csv";
        result[31] = "2014June.csv";
        result[32] = "2014July.csv";
        result[33] = "2014August.csv";
        result[34] = "2014September.csv";
        result[35] = "2014October.csv";
        result[36] = "2014November.csv";
        result[37] = "2014December.csv";
        result[38] = "2015January.csv";
        result[39] = "2015February.csv";
        result[40] = "2015March.csv";
        result[41] = "2015April.csv";
        return result;
    }

    /**
     * Filename expected in the form "2015April.csv"
     *
     * @param filename Filename of the form "2015April.csv".
     * @return filename.substring(0, 4);
     */
    public static String getFilenameYear(String filename) {
        String result;
        result = filename.substring(0, 4);
        return result;
    }

    /**
     * Filename expected in the form "2015April.csv"
     *
     * @param filename Filename of the form "2015April.csv".
     * @return filename.substring(4, filename.length() - 4);
     */
    public static String getFilenameMonth(String filename) {
        String result;
        result = filename.substring(4, filename.length() - 4);
        return result;
    }

}
