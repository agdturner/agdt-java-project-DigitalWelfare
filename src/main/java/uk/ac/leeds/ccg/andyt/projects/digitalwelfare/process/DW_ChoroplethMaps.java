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
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_CensusAreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_StyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps extends DW_Maps {

    /**
     * For storing property for selecting
     */
    private String targetPropertyName;

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
        styleParameters.setForegroundStyle0(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
        styleParameters.setBackgroundStyle(DW_Style.createDefaultPolygonStyle(
                Color.BLACK,
                Color.WHITE));
        styleParameters.setBackgroundStyleTitle("Background Style");
        
        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefile();
        
        // Switches for different outputs
        commonlyStyled = true;
        individuallyStyled = true;

        // General
        mapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "choropleth");
        sdsf = new ShapefileDataStoreFactory();

        styleParameters.setDrawBoundaries(false);

        String classificationFunctionName;
        //boolean drawBoundaries;

        // Jenks runs
        classificationFunctionName = "Jenks";
        runAll(true, classificationFunctionName);
        runAll(false, classificationFunctionName);
        // Quantile runs
        classificationFunctionName = "Quantile";
        runAll(true, classificationFunctionName);
        runAll(false, classificationFunctionName);

        // EqualInterval runs
        classificationFunctionName = "EqualInterval";
        runAll(true, classificationFunctionName);
        runAll(false, classificationFunctionName);
    }

    public void runAll(
            boolean drawBoundaries,
            String classificationFunctionName) {
        styleParameters.setClassificationFunctionName(classificationFunctionName);
        styleParameters.setDrawBoundaries(drawBoundaries);

        // OA
        level = "OA";
        targetPropertyName = "CODE";
        runAllLevel();
        // LSOA
        level = "LSOA";
        targetPropertyName = "LSOA11CD";
        runAllLevel();
        // MSOA
        level = "MSOA";
        targetPropertyName = "MSOA11CD";
        runAllLevel();
    }

    public void runAllLevel() {
        DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
        // Get LSOA Codes LSOA Shapefile and Leeds LSOA Shapefile
        tLSOACodesAndLeedsLSOAShapefile = new DW_CensusAreaCodesAndShapefiles(
                level,
                targetPropertyName,
                sdsf);
        Object[] levelData;
        TreeMap<String, Deprivation_DataRecord> deprivationRecords;

        // Map SHBE data
        deprivationRecords = null;
        runSHBE(
                deprivationRecords,
                tLSOACodesAndLeedsLSOAShapefile);
        deprivationRecords = DW_Processor.getDeprivation_Data();
        runSHBE(
                deprivationRecords,
                tLSOACodesAndLeedsLSOAShapefile);
        // Map CAB data
        File generatedAdviceLeedsDir;
        String[] tLeedsCABFilenames;

        generatedAdviceLeedsDir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "CombinedCAB");
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                level);
        tLeedsCABFilenames = getLeedsCABFilenames();
        levelData = getLevelData(
                generatedAdviceLeedsDir,
                tLeedsCABFilenames);
        int max = (Integer) levelData[1];
        System.out.println("Max clients in any area = " + max);
        boolean scaleToFirst;

        /*
         * (filter == 0) Clip all results to Leeds LAD
         * (filter == 1) Clip all results to Leeds And Neighbouring LADs (showing results for all Leeds LAD)
         * (filter == 2) Clip all results to Leeds And Neighbouring LADs and Craven and York (showing results for all Leeds LAD)
         * (filter == 3) No clipping
         */
        for (int filter = 0; filter < 4; filter++) {
            //int filter = 2;
            deprivationRecords = null;
            if (individuallyStyled) {
                scaleToFirst = false;
                runCAB(deprivationRecords,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        levelData, filter, scaleToFirst);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(deprivationRecords,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        levelData, filter, scaleToFirst);
            }

            // Get deprivation data
            deprivationRecords = DW_Processor.getDeprivation_Data();
            if (individuallyStyled) {
                scaleToFirst = false;
                runCAB(deprivationRecords,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        levelData, filter, scaleToFirst);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(deprivationRecords,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        levelData, filter, scaleToFirst);
            }
        }
//        if (!showMapsInJMapPane) {
//            // Tidy up
//            tLSOACodesAndLeedsLSOAShapefile.dispose();
//        }
    }

    public void runCAB(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] levelData,
            int filter,
            boolean scaleToFirst) {

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

        mapCountsForLevel(
                tLSOACodesAndLeedsLSOAShapefile,
                tLeedsCABFilenames,
                levelData,
                deprivationRecords,
                dir,
                filter,
                scaleToFirst);
    }

    public void runSHBE(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile) {
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
        Object[] levelData;
        levelData = getLevelData(
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
                tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames, levelData,
                dirForRates, multiplier, max, filter, scaleToFirst);
        // Counts
        scaleToFirst = false;
        File dirForCounts = new File(
                dir,
                "Counts");
        mapCountsForLevel(
                tLSOACodesAndLeedsLSOAShapefile,
                tLeedsCABFilenames,
                levelData,
                deprivationRecords,
                dirForCounts,
                filter,
                scaleToFirst);
    }

    /**
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLeedsCABFilenames
     * @param levelData
     * @param dir
     * @param multiplier
     * @param max
     * @param filter
     * @param scaleToFirst
     */
    public void mapRatesForLevel(
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] levelData,
            File dir,
            double multiplier,
            int max,
            int filter,
            boolean scaleToFirst) {

        TreeSet<String> censusCodes;
        censusCodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsCensusAreaCodes();
        //File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = tLSOACodesAndLeedsLSOAShapefile.getLevelFC();
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = tLSOACodesAndLeedsLSOAShapefile.getLevelSFT();

        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLevelDW_Shapefile();

        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();

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
                    sdsf,
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
                    showMapsInJMapPane);
            if (!scaleToFirst) {
                styleParameters.setStyle(null);
            }
        }
    }

    /**
     * Uses a file dialog to select a file and then
     *
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLeedsCABFilenames
     * @param levelData
     * @param deprivationRecords
     * @param dir
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     */
    public void mapCountsForLevel(
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] levelData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File dir,
            int filter,
            boolean scaleToFirst) {
        TreeSet<String> censusCodes;
        if (filter == 0) {
            censusCodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsCensusAreaCodes();
            foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        } else {
            if (filter == 1) {
                censusCodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsCensusAreaCodes();
                foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsDW_Shapefile();
            } else {
                censusCodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes();
                foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsAndCravenAndYorkDW_Shapefile();
            }
        }
        FeatureCollection levelFC;
        levelFC = tLSOACodesAndLeedsLSOAShapefile.getLevelFC();
        SimpleFeatureType levelSFT;
        levelSFT = tLSOACodesAndLeedsLSOAShapefile.getLevelSFT();
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLevelDW_Shapefile();

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
                    Object[] aLSOAData = (Object[]) levelData[0];
                    String filename = tLeedsCABFilenames[i];
                    Object[] bLSOAData = (Object[]) aLSOAData[i];
                    TreeMap<String, Integer> cLSOAData;
                    cLSOAData = (TreeMap<String, Integer>) bLSOAData[0];
                    String outname;
                    outname = getOutName(filename, attributeName, filter);
                    File outputShapefile = DW_GeoTools.getOutputShapefile(
                            mapDirectory2,
                            outname);
                    // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
                    selectAndCreateNewShapefile(
                            sdsf,
                            levelFC,
                            outputFeatureType,
                            censusCodes,
                            cLSOAData,
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
                            showMapsInJMapPane);
                    if (!scaleToFirst) {
                        styleParameters.setStyle(null);
                    }
                }
            }
        } else {
            File mapDirectory2 = new File(
                    dir,
                    "all");
            mapDirectory2.mkdirs();
            for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                Object[] levelData0 = (Object[]) levelData[0];
                String filename = tLeedsCABFilenames[i];
                Object[] levelData0i = (Object[]) levelData0[i];
                TreeMap<String, Integer> levelData0i0;
                levelData0i0 = (TreeMap<String, Integer>) levelData0i[0];
                String outname;
                outname = getOutName(filename, attributeName, filter);
                File outputShapefile = DW_GeoTools.getOutputShapefile(
                        mapDirectory2,
                        outname);
                // Select cLSOAData Leeds LSOA Codes from all LSOA shapefile and create a new one
                selectAndCreateNewShapefile(
                        sdsf,
                        levelFC,
                        outputFeatureType,
                        censusCodes,
                        levelData0i0,
                        //attributeName,
                        targetPropertyName,
                        outputShapefile,
                        filter);
                // Output to image
                DW_GeoTools.outputToImage(
                        outname,
                        outputShapefile,
                        foregroundDW_Shapefile0,
                        foregroundDW_Shapefile1,
                        backgroundDW_Shapefile,
                        attributeName, mapDirectory2, png_String,
                        imageWidth, styleParameters, showMapsInJMapPane);
                if (!scaleToFirst) {
                    styleParameters.setStyle(null);
                }
            }
        }
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

    public static String[] getLeedsCABFilenames() {
        String[] result = new String[25];
        result[0] = "1213AllCABClients.csv";
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
        result[23] = "1213AllCABOutletClients.csv";
        result[24] = "1213AllCABNonOutletClients.csv";
        return result;
    }
}
