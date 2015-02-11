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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_StyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps extends DW_Maps {

    protected boolean withBoundariesStyling;
    protected boolean withoutBoundariesStyling;

    protected boolean countInAndOutOfRegion;

    /**
     * For storing property for selecting
     */
    private String targetPropertyName;
    /**
     * The keys are levels and the values are the target property names for
     * those.
     */
    private HashMap<String, String> targetPropertyNames;

    /**
     * Either an instance of DW_ID_ClientOutletEnquiryID or ClientBureauOutletID
     */
    private Object IDType;

    /**
     * Either "DW_ID_ClientOutletEnquiryID" or "ClientBureauOutletID" depending
     * on IDType
     */
    private String IDTypeName;

    public DW_ChoroplethMaps() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_ChoroplethMaps().run();
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
        showMapsInJMapPane = false;//true;//false;
        imageWidth = 1000;
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
        styleParameters = new DW_StyleParameters();
        styleParameters.setnClasses(9);
        styleParameters.setPaletteName("Reds");
        styleParameters.setAddWhiteForZero(true);
        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        styleParameters.setForegroundStyle0(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyle0(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
        styleParameters.setBackgroundStyle(DW_Style.createDefaultPolygonStyle(
                Color.BLACK,
                Color.WHITE));
        styleParameters.setBackgroundStyleTitle("Background Style");

        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();

        // Initialise tDW_ID_ClientTypes
        ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes;
        tDW_ID_ClientTypes = new ArrayList<DW_ID_ClientID>();
        tDW_ID_ClientTypes.add(new DW_ID_ClientID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletEnquiryID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientEnquiryID());

        // Switches for different stylings
        // -------------------------------
        // commonStyling is where all maps are produced according to a common
        // classification
        commonStyling = true;
        // individualStyling is where all maps are produced according to an 
        // individual classification
        individualStyling = true;
        // withBoundariesStyling draws an outline to the boundaries of each 
        // region. 
//        withBoundariesStyling = true;
        // withoutBoundariesStyling does not draw an outline to the boundaries  
        // of each region. 
        withoutBoundariesStyling = true;
        // Initialise classificationFunctionNames
        classificationFunctionNames = new ArrayList<String>();
//        classificationFunctionNames.add("Jenks");
//        classificationFunctionNames.add("Quantile");
        classificationFunctionNames.add("EqualInterval");

        // Initialise levels and targetPropertyNames. 
        levels = new ArrayList<String>();
        targetPropertyNames = new HashMap<String, String>();
//        // OA
//        level = "OA";
//        levels.add(level);
//        targetPropertyNames.put(level, "CODE");
//        // LSOA
//        level = "LSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "LSOA11CD");
        // MSOA
        level = "MSOA";
        levels.add(level);
        targetPropertyNames.put(level, "MSOA11CD");
//        // Postcode Sector
//        level = "PostcodeSector";
//        levels.add(level);
//        targetPropertyNames.put(level, "RMSect");
//        // Postcodes Unit 
//        level = "PostcodeUnit";
//        levels.add(level);
//        targetPropertyNames.put(level, "POSTCODE");
        
        // Currently counting only done for level.equalsIgnoreCase("MSOA")
//        countInAndOutOfRegion = true;
        countInAndOutOfRegion = false;
        
        run(tDW_ID_ClientTypes);
    }

    /**
     * @param tDW_ID_ClientTypes
     */
    public void run(ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes) {
        Iterator<DW_ID_ClientID> ite;
        ite = tDW_ID_ClientTypes.iterator();
        while (ite.hasNext()) {
            IDType = ite.next();
            IDTypeName = IDType.getClass().getSimpleName();
            // General
            mapDirectory = new File(
                    DW_Files.getOutputAdviceLeedsMapsDir(),
                    "choropleth");
            mapDirectory = new File(
                    mapDirectory,
                    IDTypeName);
            if (withBoundariesStyling) {
                runAll(true);
            }
            if (withoutBoundariesStyling) {
                runAll(false);
            }
        }
    }

    public void runAll(
            boolean drawBoundaries) {
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            level = ite.next();
            targetPropertyName = targetPropertyNames.get(level);
            Iterator<String> ite2;
            ite2 = classificationFunctionNames.iterator();
            while (ite2.hasNext()) {
                String classificationFunctionName;
                classificationFunctionName = ite2.next();
                styleParameters.setClassificationFunctionName(classificationFunctionName);
                styleParameters.setDrawBoundaries(drawBoundaries);
                runAllLevel();
            }
        }
    }

    public void runAllLevel() {
        boolean thisCountClientsInAndOutOfRegion;
        thisCountClientsInAndOutOfRegion = false;
        if (level.equalsIgnoreCase("MSOA") && countInAndOutOfRegion == true) {
            thisCountClientsInAndOutOfRegion = true;
        }
        DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles;
        // Get Census Codes and Shapefiles
        if (level.equalsIgnoreCase("PostCodeSector")
                || level.equalsIgnoreCase("PostCodeUnit")) {
            tAreaCodesAndShapefiles = new DW_AreaCodesAndShapefiles(
                    "MSOA",
                    "MSOA11CD",
                    getShapefileDataStoreFactory());
        } else {
            tAreaCodesAndShapefiles = new DW_AreaCodesAndShapefiles(
                    level,
                    targetPropertyName,
                    getShapefileDataStoreFactory());
        }
        Object[] tLevelData;
        TreeMap<String, Deprivation_DataRecord> deprivationRecords;

//        // Map SHBE data
//        deprivationRecords = null;
//        runSHBE(
//                deprivationRecords,
//                tCensusCodesAndShapefiles);
//        deprivationRecords = DW_Processor.getDeprivation_Data();
//        runSHBE(
//                deprivationRecords,
//                tCensusCodesAndShapefiles);
        // Map Advice Leeds data
        File generatedAdviceLeedsDir;
        String[] tAdviceLeedsFilenames;

        generatedAdviceLeedsDir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "Combined");
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                level);
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                IDType.getClass().getSimpleName());

        tAdviceLeedsFilenames = getAdviceLeedsFilenames();
        tLevelData = getLevelData(
                generatedAdviceLeedsDir,
                tAdviceLeedsFilenames);
        int max = (Integer) tLevelData[1];
        System.out.println("Max clients in any area = " + max);
        boolean scaleToFirst;

        /*
         * (filter == 0) Clip all results to Leeds LAD
         * (filter == 1) Clip all results to Leeds And Neighbouring LADs (showing results for all Leeds LAD)
         * (filter == 2) Clip all results to Leeds And Near Neighbouring LADs (showing results for all Leeds LAD)
         * (filter == 3) No clipping
         */
        //for (int filter = 0; filter < 4; filter++) {
        for (int filter = 0; filter < 3; filter++) {
            //for (int filter = 2; filter < 3; filter++) {
            if (filter == 0 || filter == 2) {

                deprivationRecords = null;
                if (individualStyling) {
                    scaleToFirst = false;
                    runAdviceLeeds(deprivationRecords,
                            tAreaCodesAndShapefiles,
                            tAdviceLeedsFilenames,
                            tLevelData,
                            filter,
                            scaleToFirst,
                            thisCountClientsInAndOutOfRegion);
                }
                if (commonStyling) {
                    scaleToFirst = true;
                    runAdviceLeeds(deprivationRecords,
                            tAreaCodesAndShapefiles,
                            tAdviceLeedsFilenames,
                            tLevelData,
                            filter,
                            scaleToFirst,
                            false);
                }

//            // Get deprivation data
//            deprivationRecords = DW_Processor.getDeprivation_Data();
//            if (individualStyling) {
//                scaleToFirst = false;
//                runAdviceLeeds(deprivationRecords,
//                        tCensusCodesAndLeedsLevelShapefile, tLeedsCABFilenames,
//                        levelData, filter, scaleToFirst);
//            }
//            if (commonStyling) {
//                scaleToFirst = true;
//                runAdviceLeeds(deprivationRecords,
//                        tCensusCodesAndLeedsLevelShapefile, tLeedsCABFilenames,
//                        levelData, filter, scaleToFirst);
//            }
            }
//            if (!showMapsInJMapPane) {
//                // Tidy up
//                tCensusCodesAndShapefiles.dispose();
//            }
        }
    }

    public void runAdviceLeeds(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_AreaCodesAndShapefiles tCensusCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            Object[] tLevelData,
            int filter,
            boolean scaleToFirst,
            boolean doInAndOutOfRegionCounts) {

        String style;
        if (scaleToFirst) {
            style = "CommonlyStyled";
        } else {
            style = "IndividuallyStyled";
        }
        File dir;
        dir = new File(
                mapDirectory,
                level);
        if (styleParameters.isDrawBoundaries()) {
            dir = new File(
                    dir,
                    "WithBoundaries");
        } else {
            dir = new File(
                    dir,
                    "WithoutBoundaries");
        }
        dir = new File(
                dir,
                style + "/" + styleParameters.getClassificationFunctionName());

        TreeMap<String, TreeMap<Integer, Integer>> inAndOutOfRegionCounts;
        inAndOutOfRegionCounts = mapCountsForLevel(
                tCensusCodesAndShapefiles,
                tLeedsCABFilenames,
                tLevelData,
                deprivationRecords,
                dir,
                filter,
                scaleToFirst,
                doInAndOutOfRegionCounts);
        if (doInAndOutOfRegionCounts) {
            String aIDTypeName;
            aIDTypeName = IDType.getClass().getSimpleName();
            File outputInAndOutOfRegionCountsDir;
            outputInAndOutOfRegionCountsDir = new File(
                    DW_Files.getOutputAdviceLeedsTablesDir(),
                    aIDTypeName);
            outputInAndOutOfRegionCountsDir.mkdirs();
            File outputInAndOutOfRegionCountsFile;
            outputInAndOutOfRegionCountsFile = new File(
                    outputInAndOutOfRegionCountsDir,
                    "InAndOutOfRegionCounts" + filter + ".csv");
            PrintWriter pw;
            pw = Generic_StaticIO.getPrintWriter(outputInAndOutOfRegionCountsFile, false);
            if (IDType instanceof DW_ID_ClientOutletEnquiryID) {
                pw.println("Year AdviceLeeds Service Region,EnquiryClientCountOutOfRegion,EnquiryClientCountInRegion,PercentageOutOfRegion");
            } else {
                pw.println("Year AdviceLeeds Service Region,ClientCountOutOfRegion,ClientCountInRegion,PercentageOutOfRegion");
            }
            Iterator<String> ite;
            ite = inAndOutOfRegionCounts.keySet().iterator();
            while (ite.hasNext()) {
                String name = ite.next();
                TreeMap<Integer, Integer> inAndOutOfRegionCount;
                inAndOutOfRegionCount = inAndOutOfRegionCounts.get(name);
                pw.print(name);
                Iterator<Integer> ite2;
                ite2 = inAndOutOfRegionCount.keySet().iterator();
                Integer inCount = null;
                Integer outCount = null;
                while (ite2.hasNext()) {
                    Integer inOrOut;
                    inOrOut = ite2.next();
                    if (inOrOut == 0) {
                        inCount = inAndOutOfRegionCount.get(inOrOut);
                        pw.print("," + inCount);
                    } else {
                        outCount = inAndOutOfRegionCount.get(inOrOut);
                        pw.print("," + outCount);
                    }
                }
                // Get Percentage
                if (outCount == 0) {
                    pw.println(",100");
                } else {
                    double percentage;
                    percentage = 100.0d * (double) inCount / (double) outCount;
                    //pw.println("," + percentage);
                    BigDecimal p;
                    p = BigDecimal.valueOf(percentage);
                    int decimalPlaces = 3;
                    BigDecimal roundedp;
                    roundedp = Generic_BigDecimal.roundIfNecessary(
                            p, decimalPlaces, RoundingMode.UP);
                    pw.println("," + roundedp.toPlainString());
                }
            }
            pw.flush();
            pw.close();
        }
    }

    public void runSHBE(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_AreaCodesAndShapefiles tCensusCodesAndShapefiles
    ) {
        int filter;
        boolean scaleToFirst;// Map SHBE data
        // Initiailise filenames of files of claimants to map and get LSOA data
        // to map
        filter = 0; // clip/filter to Leeds LAD
        String AllClaimants_String = "AllClaimants";
        int year = 2011;
        File generatedSHBEDir;
        generatedSHBEDir = new File(
                DW_Files.getGeneratedSHBEDir(level),
                AllClaimants_String);

        String[] tLeedsCABFilenames;
        tLeedsCABFilenames = new String[1];
        //tLeedsCABFilenames[0] = level + AllClaimants_String + year + "April.csv";
        tLeedsCABFilenames[0] = year + "April.csv";
        Object[] tLevelData;
        tLevelData = getLevelData(
                generatedSHBEDir,
                tLeedsCABFilenames);

        File tSHBEMapsLeveldir = new File(
                DW_Files.getOutputSHBEMapsDir(),
                level);
        File dir = new File(
                tSHBEMapsLeveldir,
                AllClaimants_String);

        if (styleParameters.isDrawBoundaries()) {
            dir = new File(dir,
                    "WithBoundaries");
        } else {
            dir = new File(dir,
                    "WithoutBoundaries");
        }

        dir = new File(
                dir,
                styleParameters.getClassificationFunctionName());

        // Rates
        scaleToFirst = false;
        int max;
        max = 0;

        File dirForRates = new File(
                dir,
                "Rates");
        double multiplier = 1000.0d;
        mapRatesForLevel(
                tCensusCodesAndShapefiles, tLeedsCABFilenames, tLevelData,
                dirForRates, multiplier, max, filter, scaleToFirst);
        // Counts
        scaleToFirst = false;
        File dirForCounts = new File(
                dir,
                "Counts");
        TreeMap<String, TreeMap<Integer, Integer>> inAndOutOfRegionCounts;
        inAndOutOfRegionCounts = mapCountsForLevel(
                tCensusCodesAndShapefiles,
                tLeedsCABFilenames,
                tLevelData,
                deprivationRecords,
                dirForCounts,
                filter,
                scaleToFirst,
                false);
    }

    /**
     * @param tAreaCodesAndShapefiles
     * @param tLeedsCABFilenames
     * @param levelData
     * @param dir
     * @param multiplier
     * @param max
     * @param filter
     * @param scaleToFirst
     */
    public void mapRatesForLevel(
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            Object[] levelData,
            File dir,
            double multiplier,
            int max,
            int filter,
            boolean scaleToFirst) {

        TreeSet<String> censusCodes;
        censusCodes = tAreaCodesAndShapefiles.getLeedsAreaCodes();
        //File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = tAreaCodesAndShapefiles.getLevelFC();
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = tAreaCodesAndShapefiles.getLevelSFT();

        backgroundDW_Shapefile = tAreaCodesAndShapefiles.getLeedsLevelDW_Shapefile();

        foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsLADDW_Shapefile();

        // attributeName is the attribute name used in naming attribute in 
        // resulting shapefile
        //String attributeName = "Rate";
        String attributeName = "Count"; // Want to be able to change this 

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                tLSOAFeatureType,
                Double.class,
                attributeName,
                name);

        // Read level population data
        String area;
        if (level.equalsIgnoreCase("LSOA")) {
            area = "England";
        } else {
            area = "GreatBritain";
        }
        TreeMap<String, Integer> pop = getPopData(
                level,
                area);

        for (int i = 0; i < tLeedsCABFilenames.length; i++) {
            Object[] aLSOAData = (Object[]) levelData[0];
            String filename = tLeedsCABFilenames[i];
            Object[] bLSOAData = (Object[]) aLSOAData[i];
            TreeMap<String, Integer> cLSOAData;
            cLSOAData = (TreeMap<String, Integer>) bLSOAData[0];
            String outname;
            outname = getOutName(filename, attributeName, filter);
            File outputShapefile = DW_GeoTools.getOutputShapefile(
                    dir,
                    outname);
            // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
            selectAndCreateNewShapefile(
                    getShapefileDataStoreFactory(),
                    tLSOAFeatureCollection,
                    outputFeatureType,
                    censusCodes,
                    cLSOAData,
                    pop,
                    multiplier,
                    //attributeName,
                    targetPropertyName,
                    outputShapefile,
                    max,
                    filter);
            // Output to image
            DW_GeoTools.outputToImage(
                    outname,
                    outputShapefile,
                    foregroundDW_Shapefile0,
                    foregroundDW_Shapefile1,
                    backgroundDW_Shapefile,
                    attributeName,
                    dir,
                    png_String,
                    imageWidth,
                    styleParameters,
                    0,
                    showMapsInJMapPane);
            if (!scaleToFirst) {
                styleParameters.setStyle(null, 0);
            }
        }
    }

    /**
     * Uses a file dialog to select a file and then
     *
     * @param tCensusCodesAndShapefiles
     * @param tLeedsCABFilenames
     * @param tLevelData
     * @param deprivationRecords
     * @param dir
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     * @param countClientsInAndOutOfRegion
     * @return
     */
    public TreeMap<String, TreeMap<Integer, Integer>> mapCountsForLevel(
            DW_AreaCodesAndShapefiles tCensusCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            Object[] tLevelData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File dir,
            int filter,
            boolean scaleToFirst,
            boolean countClientsInAndOutOfRegion) {
        // Initialise result
        TreeMap<String, TreeMap<Integer, Integer>> result = null;
        if (countClientsInAndOutOfRegion) {
            result = new TreeMap<String, TreeMap<Integer, Integer>>();
        }
        TreeSet<String> censusCodes;
        if (filter == 0) {
            censusCodes = tCensusCodesAndShapefiles.getLeedsAreaCodes();
            foregroundDW_Shapefile1 = tCensusCodesAndShapefiles.getLeedsLADDW_Shapefile();
        } else {
            if (filter == 1) {
                censusCodes = tCensusCodesAndShapefiles.getLeedsAndNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tCensusCodesAndShapefiles.getLeedsAndNeighbouringLADDW_Shapefile();
            } else {
                censusCodes = tCensusCodesAndShapefiles.getLeedsAndNearNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tCensusCodesAndShapefiles.getLeedsAndNearNeighbouringLADDW_Shapefile();
            }
        }
        FeatureCollection levelFC;
        levelFC = tCensusCodesAndShapefiles.getLevelFC();
        SimpleFeatureType levelSFT;
        levelSFT = tCensusCodesAndShapefiles.getLevelSFT();
        backgroundDW_Shapefile = tCensusCodesAndShapefiles.getLeedsLevelDW_Shapefile();

        // attributeName is the attribute name used in naming attribute in 
        // resulting shapefile
        String attributeName = "Count";

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                levelSFT,
                Integer.class,
                attributeName,
                name);

        if (deprivationRecords != null) {
            TreeMap<Integer, Integer> deprivationClasses = null;
            deprivationClasses = Deprivation_DataHandler.getDeprivationClasses(
                    deprivationRecords);
            Iterator<Integer> ite = deprivationClasses.keySet().iterator();
            while (ite.hasNext()) {
                Integer deprivationKey = ite.next();
                Integer deprivationClass = deprivationClasses.get(deprivationKey);

                File mapDirectory2 = new File(
                        dir,
                        "" + deprivationClass);
                mapDirectory2.mkdirs();
                for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                    Object[] aLevelData = (Object[]) tLevelData[0];
                    String filename = tLeedsCABFilenames[i];
                    Object[] bLevelData = (Object[]) aLevelData[i];
                    TreeMap<String, Integer> cLevelData;
                    cLevelData = (TreeMap<String, Integer>) bLevelData[0];
                    String outname;
                    outname = getOutName(filename, attributeName, filter);
                    File outputShapefile = DW_GeoTools.getOutputShapefile(
                            mapDirectory2,
                            outname);
                    // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
                    selectAndCreateNewShapefile(
                            getShapefileDataStoreFactory(),
                            levelFC,
                            outputFeatureType,
                            censusCodes,
                            cLevelData,
                            //attributeName,
                            targetPropertyName,
                            outputShapefile,
                            filter,
                            deprivationClasses,
                            deprivationRecords,
                            deprivationClass);
                    // Output to image
                    DW_GeoTools.outputToImage(
                            outname,
                            outputShapefile,
                            foregroundDW_Shapefile0,
                            foregroundDW_Shapefile1,
                            backgroundDW_Shapefile,
                            attributeName,
                            mapDirectory2,
                            png_String,
                            imageWidth,
                            styleParameters,
                            0,
                            showMapsInJMapPane);
                    if (!scaleToFirst) {
                        styleParameters.setStyle(null, 0);
                    }
                }
            }
        } else {
            File mapDirectory2 = new File(
                    dir,
                    "all");
            mapDirectory2.mkdirs();
            for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                Object[] levelData0 = (Object[]) tLevelData[0];
                String filename = tLeedsCABFilenames[i];
                Object[] levelData0i = (Object[]) levelData0[i];
                TreeMap<String, Integer> levelData0i0;
                levelData0i0 = (TreeMap<String, Integer>) levelData0i[0];
                String outname;
                outname = getOutName(filename, attributeName, filter);
                File outputShapefile = DW_GeoTools.getOutputShapefile(
                        mapDirectory2,
                        outname);
                // Select levelData0i0 Census Codes from all LSOA shapefile and create a new one
                TreeMap<Integer, Integer> inAndOutOfRegionCount;
                inAndOutOfRegionCount = selectAndCreateNewShapefile(
                        getShapefileDataStoreFactory(),
                        levelFC,
                        outputFeatureType,
                        censusCodes,
                        levelData0i0,
                        //attributeName,
                        targetPropertyName,
                        outputShapefile,
                        filter,
                        countClientsInAndOutOfRegion);
                if (countClientsInAndOutOfRegion) {
                    result.put(outname, inAndOutOfRegionCount);
                }
                // Output to image
                DW_GeoTools.outputToImage(
                        outname,
                        outputShapefile,
                        foregroundDW_Shapefile0,
                        foregroundDW_Shapefile1,
                        backgroundDW_Shapefile,
                        attributeName,
                        mapDirectory2,
                        png_String,
                        imageWidth,
                        styleParameters,
                        0,
                        showMapsInJMapPane);
                if (!scaleToFirst) {
                    styleParameters.setStyle(null, 0);
                }
            }
        }
        return result;
    }

    public static File getOutputImageFile(
            File outputFile,
            String outputType) {
        File result;
        String filename = outputFile.getName();
        String outputImageFilename;
        outputImageFilename = filename.substring(0, filename.length() - 4)
                + "." + outputType;
        result = new File(
                outputFile.getParent(),
                outputImageFilename);
        return result;
    }

    public static String[] getAdviceLeedsFilenames() {
        String[] result = new String[27];
        //result[0] = "1213AllCABClients.csv";
        //result[0] = "1213AllAdviceLeeds.csv"; // Should do a max one for mapping purposes too!
        result[0] = "1213Scale.csv"; // For mapping purposes too!
        result[1] = "1213OTLEY - LS21 1BG.csv";
        result[2] = "1213ASIAN WOMANS SERVICE - LS15 8QR.csv";
        result[3] = "1213PUDSEY.csv";
        result[4] = "1213CHILDRENS CENTRES TELEPHONE.csv";
        result[5] = "1213REFUGEE INTEGRATION ADVICE SERVICE.csv";
        result[6] = "1213CITY - LS2 7DT.csv";
        result[7] = "1213SOUTH LEEDS CHILDRENS CENTRES.csv";
        result[8] = "1213CROSSGATES - LS15 8QR.csv";
        result[9] = "1213SOUTH LEEDS PCT.csv";
        result[10] = "1213EAST LEEDS CHILDRENS CENTRES.csv";
        result[11] = "1213SPECIALIST SERVICES - LS2 7DT.csv";
        result[12] = "1213EL PCT SERVICES - LS15 8QR.csv";
        result[13] = "1213TELEPHONE ADVICE.csv";
        result[14] = "1213FIF.csv";
        result[15] = "1213TELEPHONE GATEWAY LEEDS CAB.csv";
        result[16] = "1213HOME VISITS - LS2 7DT.csv";
        result[17] = "1213TELEPHONE RECEPTION LEEDS CAB.csv";
        result[18] = "1213MENTAL HEALTH - LS2 7DT.csv";
        result[19] = "1213WEST LEEDS PCT SERVICE - LS28 7AB.csv";
        result[20] = "1213MORLEY - LS27 9DY.csv";
        result[21] = "1213WHHL - WARM HOMES HEALTHY LIVES.csv";
        result[22] = "1213CHAPELTOWN.csv";
        result[23] = "1213AllCABOutlet.csv";
        result[24] = "1213AllCABNonOutlet.csv";
        result[25] = "1213LCC_WRU.csv";
        result[26] = "1213AllAdviceLeeds.csv";
        return result;
    }
}
