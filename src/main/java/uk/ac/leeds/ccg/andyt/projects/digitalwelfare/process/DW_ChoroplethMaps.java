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
import org.geotools.map.FeatureLayer;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps extends DW_Maps {

    public DW_ChoroplethMaps() {
    }

    public DW_ChoroplethMaps(File digitalWelfareDir) {
        this.digitalWelfareDir = digitalWelfareDir;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            File digitalWelfareDir = new File("/scratch02/DigitalWelfare/");
            new DW_ChoroplethMaps(digitalWelfareDir).run();
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
        String classificationFunctionName;
        // If showMapsInJMapPane is true, the maps are presented in individual 
        // JMapPanes
        boolean showMapsInJMapPane = false;
        // Initialise styleParameters
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
        int nClasses = 9;
        String paletteName = "Reds";
        boolean addWhiteForZero = true;
        File choroplethMapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "choropleth");
        int imageWidth = 1000;

        ShapefileDataStoreFactory sdsf;
        sdsf = new ShapefileDataStoreFactory();

        boolean commonlyStyled;
        boolean individuallyStyled;

        // Quantile runs
        commonlyStyled = true;
        individuallyStyled = true;
        classificationFunctionName = "Jenks";
        runAll(showMapsInJMapPane,
                commonlyStyled,
                individuallyStyled,
                nClasses, paletteName, addWhiteForZero,
                imageWidth, choroplethMapDirectory, classificationFunctionName, sdsf);

        
        // Quantile runs
        commonlyStyled = true;
        individuallyStyled = true;
        classificationFunctionName = "Quantile";
        runAll(showMapsInJMapPane,
                commonlyStyled,
                individuallyStyled,
                nClasses, paletteName, addWhiteForZero,
                imageWidth, choroplethMapDirectory, classificationFunctionName, sdsf);

        // EqualInterval runs
        classificationFunctionName = "EqualInterval";
        commonlyStyled = true;
        individuallyStyled = true;
        runAll(showMapsInJMapPane,
                commonlyStyled,
                individuallyStyled,
                nClasses, paletteName, addWhiteForZero,
                imageWidth, choroplethMapDirectory, classificationFunctionName, sdsf);

    }

    public void runAll(
            boolean showMapsInJMapPane,
            boolean commonlyStyled,
            boolean individuallyStyled,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero,
            int imageWidth,
            File choroplethMapDirectory,
            String classificationFunctionName,
            ShapefileDataStoreFactory sdsf) {

        // General parameters
        // Property for selecting
        String targetPropertyName = "LSOA11CD";
        String level = "LSOA";
        // Get LSOA Codes LSOA Shapefile and Leeds LSOA Shapefile
        Object[] tLSOACodesAndLeedsLSOAShapefile;
        tLSOACodesAndLeedsLSOAShapefile = getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyName, sdsf);

        Style backgroundStyle = DW_Style.createDefaultPolygonStyle(
                Color.BLACK,
                Color.WHITE);
        String backgroundStyle_String = "Default Background Style";
        Object[] styleParameters;
        styleParameters = DW_Style.initStyleParameters(
                classificationFunctionName,
                nClasses,
                paletteName,
                addWhiteForZero,
                backgroundStyle,
                backgroundStyle_String);

        Object[] tLSOAData;
        String png_String;
        png_String = "png";
        TreeMap<String, Deprivation_DataRecord> deprivationRecords;

//        // Map SHBE data
//        deprivationRecords = null;
//        generateIndividuallyStyledSHBE(
//                sdsf,
//                classificationFunctionName,
//                styleParameters,
//                showMapsInJMapPane,
//                imageWidth,
//                deprivationRecords,
//                targetPropertyName,
//                level,
//                tLSOACodesAndLeedsLSOAShapefile,
//                //String[] tLeedsCABFilenames,
//                png_String);
//        deprivationRecords = DW_Processor.getDeprivation_Data();
//        generateIndividuallyStyledSHBE(
//                sdsf,
//                classificationFunctionName,
//                styleParameters,
//                showMapsInJMapPane,
//                imageWidth,
//                deprivationRecords,
//                targetPropertyName,
//                level,
//                tLSOACodesAndLeedsLSOAShapefile,
//                //String[] tLeedsCABFilenames,
//                png_String);
        // Map CAB data
        File generatedAdviceLeedsDir;
        String[] tLeedsCABFilenames;

        generatedAdviceLeedsDir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "CombinedCAB");
        tLeedsCABFilenames = getLeedsCABFilenames();
        tLSOAData = getLSOAData(
                generatedAdviceLeedsDir,
                tLeedsCABFilenames);
        int max = (Integer) tLSOAData[1];
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
                runCAB(sdsf,
                        showMapsInJMapPane, nClasses, paletteName, styleParameters,
                        imageWidth, deprivationRecords, choroplethMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        tLSOAData, png_String, filter, scaleToFirst);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(sdsf,
                        showMapsInJMapPane, nClasses, paletteName, styleParameters,
                        imageWidth, deprivationRecords, choroplethMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        tLSOAData, png_String, filter, scaleToFirst);
            }

            // Get deprivation data
            deprivationRecords = DW_Processor.getDeprivation_Data();
            if (individuallyStyled) {
                scaleToFirst = false;
                runCAB(sdsf,
                        showMapsInJMapPane, nClasses, paletteName, styleParameters,
                        imageWidth, deprivationRecords, choroplethMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        tLSOAData, png_String, filter, scaleToFirst);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(sdsf,
                        showMapsInJMapPane, nClasses, paletteName, styleParameters,
                        imageWidth, deprivationRecords, choroplethMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames,
                        tLSOAData, png_String, filter, scaleToFirst);
            }
        }
    }

    public void runCAB(
            ShapefileDataStoreFactory sdsf,
            boolean showMapsInJMapPane,
            int nClasses,
            String paletteName,
            Object[] styleParameters,
            int imageWidth,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File choroplethMapDirectory,
            String classificationFunctionName,
            String targetPropertyName,
            String level,
            Object[] tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] tLSOAData,
            String png_String,
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
                choroplethMapDirectory,
                style + "/" + classificationFunctionName);

        mapCountsForLSOAs(
                sdsf,
                tLSOACodesAndLeedsLSOAShapefile,
                tLeedsCABFilenames,
                tLSOAData,
                deprivationRecords,
                dir,
                targetPropertyName,
                png_String,
                imageWidth,
                filter,
                scaleToFirst,
                styleParameters,
                showMapsInJMapPane);
    }

    public void generateIndividuallyStyledSHBE(
            ShapefileDataStoreFactory sdsf,
            String classificationFunctionName,
            Object[] styleParameters,
            boolean showMapsInJMapPane,
            int imageWidth,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            String targetPropertyName,
            String level,
            Object[] tLSOACodesAndLeedsLSOAShapefile,
            //String[] tLeedsCABFilenames,
            String png_String) {
        int filter;
        boolean scaleToFirst;// Map SHBE data
        // Initiailise filenames of files of claimants to map and get LSOA data
        // to map
        filter = 0; // clip/filter to Leeds LAD
        File generatedSHBEDir;
        generatedSHBEDir = new File(
                DW_Files.getGeneratedSHBEDir(),
                "LSOAAllClaimants");
        String[] tLeedsCABFilenames;
        tLeedsCABFilenames = new String[1];
        tLeedsCABFilenames[0] = "LSOAAllClaimants2012April.csv";
        Object[] tLSOAData;
        tLSOAData = getLSOAData(
                generatedSHBEDir,
                tLeedsCABFilenames);

        File dir = new File(
                DW_Files.getOutputSHBEMapsDir(),
                "LSOAAllClaimants");
        // Rates
        scaleToFirst = false;
        mapRatesForLSOAs(
                sdsf, tLSOACodesAndLeedsLSOAShapefile, tLeedsCABFilenames, tLSOAData,
                dir, targetPropertyName, png_String, imageWidth,
                imageWidth, filter, scaleToFirst, styleParameters,
                showMapsInJMapPane);
        // Counts
        scaleToFirst = false;
        mapCountsForLSOAs(
                sdsf,
                tLSOACodesAndLeedsLSOAShapefile,
                tLeedsCABFilenames,
                tLSOAData,
                deprivationRecords,
                dir,
                targetPropertyName,
                png_String,
                imageWidth,
                filter,
                scaleToFirst,
                styleParameters,
                showMapsInJMapPane);
    }

    /**
     * @param sdsf
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLeedsCABFilenames
     * @param tLSOAData
     * @param dir
     * @param targetPropertyName
     * @param png_String
     * @param imageWidth
     * @param max
     * @param filter
     * @param scaleToFirst
     * @param styleParameters
     * @param showMapsInJMapPane
     */
    public void mapRatesForLSOAs(
            ShapefileDataStoreFactory sdsf,
            Object[] tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] tLSOAData,
            File dir,
            String targetPropertyName,
            String png_String,
            int imageWidth,
            int max,
            int filter,
            boolean scaleToFirst,
            Object[] styleParameters,
            boolean showMapsInJMapPane) {

        TreeSet<String> tLSOACodes;
        tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[0];
        //File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = (FeatureCollection) tLSOACodesAndLeedsLSOAShapefile[2];
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = (SimpleFeatureType) tLSOACodesAndLeedsLSOAShapefile[3];
        File leedsLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[4];
        File backgroundShapefile = leedsLSOAShapefile;

        Style backgroundStyle = (Style) styleParameters[6];
        FeatureLayer backgroundFeatureLayer;

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

        // Read LSOA population data
        TreeMap<String, Integer> pop = getPopData("LSOA");

        for (int i = 0; i < tLeedsCABFilenames.length; i++) {
            Object[] aLSOAData = (Object[]) tLSOAData[0];
            //for (int i = 0; i < 2; i++) {
            String filename = tLeedsCABFilenames[i];
            Object[] bLSOAData = (Object[]) aLSOAData[i];
            TreeMap<String, Integer> cLSOAData;
            cLSOAData = (TreeMap<String, Integer>) bLSOAData[0];
            String outname;
            outname = getOutName(filename, attributeName, filter);
            File outputShapefile = DW_GeoTools.getOutputShapefile(
                    dir,
                    outname);

            // Had hoped that this could be done once and then passed in 
            backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                    backgroundShapefile,
                    backgroundStyle);

            // cLSOAData Leeds LSOA Codes from all LSOA shapefile and create a new one
            selectAndCreateNewShapefile(
                    sdsf,
                    tLSOAFeatureCollection,
                    outputFeatureType,
                    tLSOACodes,
                    cLSOAData,
                    pop,
                    //attributeName,
                    targetPropertyName,
                    outputShapefile,
                    max,
                    filter);
            DW_GeoTools.outputToImage(
                    outname,
                    outputShapefile,
                    backgroundFeatureLayer,
                    //backgroundShapefile,
                    attributeName,
                    dir,
                    png_String,
                    imageWidth,
                    styleParameters,
                    showMapsInJMapPane);
            if (!scaleToFirst) {
                styleParameters[0] = null;
            }
        }
    }


    /**
     * Uses a file dialog to select a file and then
     *
     * @param sdsf
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLeedsCABFilenames
     * @param tLSOAData
     * @param deprivationRecords
     * @param dir
     * @param targetPropertyName
     * @param png_String
     * @param imageWidth
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     * @param styleParameters
     * @param showMapsInJMapPane
     */
    public void mapCountsForLSOAs(
            ShapefileDataStoreFactory sdsf,
            Object[] tLSOACodesAndLeedsLSOAShapefile,
            String[] tLeedsCABFilenames,
            Object[] tLSOAData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File dir,
            String targetPropertyName,
            String png_String,
            int imageWidth,
            int filter,
            boolean scaleToFirst,
            Object[] styleParameters,
            boolean showMapsInJMapPane) {

        TreeSet<String> tLSOACodes;
        if (filter == 0) {
            tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[0];
        } else {
            if (filter == 1) {
                tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[5];
            } else {
                tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[6];
            }
        }
        //File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = (FeatureCollection) tLSOACodesAndLeedsLSOAShapefile[2];
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = (SimpleFeatureType) tLSOACodesAndLeedsLSOAShapefile[3];
        File leedsLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[4];
        File backgroundShapefile = leedsLSOAShapefile;

        Style backgroundStyle = (Style) styleParameters[6];
        FeatureLayer backgroundFeatureLayer;

        // attributeName is the attribute name used in naming attribute in 
        // resulting shapefile
        String attributeName = "Count";

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                tLSOAFeatureType,
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

                File mapDirectory = new File(
                        dir,
                        "" + deprivationClass);
                mapDirectory.mkdirs();
                for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                    Object[] aLSOAData = (Object[]) tLSOAData[0];
                    String filename = tLeedsCABFilenames[i];
                    Object[] bLSOAData = (Object[]) aLSOAData[i];
                    TreeMap<String, Integer> cLSOAData;
                    cLSOAData = (TreeMap<String, Integer>) bLSOAData[0];
                    String outname;
                    outname = getOutName(filename, attributeName, filter);
                    File outputshapeFile = DW_GeoTools.getOutputShapefile(
                            mapDirectory,
                            outname);

                    // Had hoped that this could be done once and then passed in 
                    backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                            backgroundShapefile,
                            backgroundStyle);

                    // cLSOAData Leeds LSOA Codes from all LSOA shapefile and create a new one
                    selectAndCreateNewShapefile(
                            sdsf,
                            tLSOAFeatureCollection,
                            outputFeatureType,
                            tLSOACodes,
                            cLSOAData,
                            //attributeName,
                            targetPropertyName,
                            outputshapeFile,
                            filter,
                            deprivationClasses,
                            deprivationRecords,
                            deprivationClass);
                    DW_GeoTools.outputToImage(
                            outname,
                            outputshapeFile,
                            backgroundFeatureLayer,
                            //backgroundShapefile,
                            attributeName,
                            mapDirectory,
                            png_String,
                            imageWidth,
                            styleParameters,
                            showMapsInJMapPane);
                    if (!scaleToFirst) {
                        styleParameters[0] = null;
                    }
                }
            }
        } else {
            File mapDirectory = new File(
                    dir,
                    "all");
            mapDirectory.mkdirs();
            for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                Object[] aLSOAData = (Object[]) tLSOAData[0];
                String filename = tLeedsCABFilenames[i];
                Object[] bLSOAData = (Object[]) aLSOAData[i];
                TreeMap<String, Integer> cLSOAData;
                cLSOAData = (TreeMap<String, Integer>) bLSOAData[0];
                String outname;
                outname = getOutName(filename, attributeName, filter);
                File outputshapeFile = DW_GeoTools.getOutputShapefile(
                        mapDirectory,
                        outname);

                // Had hoped that this could be done once and then passed in 
                backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                        backgroundShapefile,
                        backgroundStyle);

                // cLSOAData Leeds LSOA Codes from all LSOA shapefile and create a new one
                selectAndCreateNewShapefile(
                        sdsf,
                        tLSOAFeatureCollection,
                        outputFeatureType,
                        tLSOACodes,
                        cLSOAData,
                        //attributeName,
                        targetPropertyName,
                        outputshapeFile,
                        filter);
                DW_GeoTools.outputToImage(
                        outname,
                        outputshapeFile,
                        backgroundFeatureLayer,
                        //backgroundShapefile,
                        attributeName,
                        mapDirectory,
                        png_String,
                        imageWidth,
                        styleParameters,
                        showMapsInJMapPane);
                if (!scaleToFirst) {
                    styleParameters[0] = null;
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
