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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import static uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Maps.png_String;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools.getOutputFile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps extends DW_Maps {

    protected boolean withBoundariesStyling;
    protected boolean withoutBoundariesStyling;
    protected boolean doCount;
    protected boolean doDensity;
    protected boolean doDeprivation;
    protected boolean[] doFilter;
    /**
     * For storing property for selecting
     */
    protected String targetPropertyName;
    /**
     * The keys are levels and the values are the target property names for
     * those.
     */
    protected HashMap<String, String> targetPropertyNames;

    /**
     * Uses a file dialog to select a file and then
     *
     * @param tAreaCodesAndShapefiles
     * @param tLeedsCABFilenames
     * @param tLevelData
     * @param deprivationRecords
     * @param dirOut
     * @param attributeName
     * @param binding
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     * @param countClientsInAndOutOfRegion
     * @return
     */
    public TreeMap<String, TreeMap<Integer, Integer>> mapCountsForLevel(
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            ArrayList<Integer> include,
            Object[] tLevelData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File dirOut,
            String attributeName,
            Class<?> binding,
            int filter,
            boolean scaleToFirst,
            double max,
            boolean countClientsInAndOutOfRegion) {
        // Initialise result
        TreeMap<String, TreeMap<Integer, Integer>> result = null;
        if (countClientsInAndOutOfRegion) {
            result = new TreeMap<String, TreeMap<Integer, Integer>>();
        }
        TreeSet<String> areaCodes;
        if (filter == 0) {
            areaCodes = tAreaCodesAndShapefiles.getLeedsAreaCodes();
            foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsLADDW_Shapefile();
        } else {
            if (filter == 1) {
                areaCodes = tAreaCodesAndShapefiles.getLeedsAndNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsAndNeighbouringLADDW_Shapefile();
            } else {
                areaCodes = tAreaCodesAndShapefiles.getLeedsAndNearNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsAndNearNeighbouringLADDW_Shapefile();
            }
        }
        FeatureCollection levelFC;
        levelFC = tAreaCodesAndShapefiles.getLevelFC();
        SimpleFeatureType levelSFT;
        levelSFT = tAreaCodesAndShapefiles.getLevelSFT();
        backgroundDW_Shapefile = tAreaCodesAndShapefiles.getLeedsLevelDW_Shapefile();

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                levelSFT,
                binding,
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
                File dirOut2 = new File(
                        dirOut,
                        "" + deprivationClass);
                dirOut2.mkdirs();
                for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                    boolean doLoop = false;
                    if (include == null) {
                        doLoop = true;
                    } else {
                        if (include.contains(i)) {
                            doLoop = true;
                        }
                    }
                    if (doLoop) {
                        String filename = tLeedsCABFilenames[i];
                        Object[] aLevelData = (Object[]) tLevelData[0];
                        Object[] bLevelData = (Object[]) aLevelData[i];
                        if (bLevelData != null) {
                            TreeMap<String, Integer> cLevelData;
                            cLevelData = (TreeMap<String, Integer>) bLevelData[0];
                            if (cLevelData != null) {
                                if (cLevelData.size() > 0) {
                                    String outname;
                                    outname = getOutName(filename, attributeName, filter);
                                    File outputShapefile = AGDT_Geotools.getOutputShapefile(
                                            dirOut2,
                                            outname);
                                    // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
                                    selectAndCreateNewShapefile(
                                            getShapefileDataStoreFactory(),
                                            levelFC,
                                            outputFeatureType,
                                            areaCodes,
                                            cLevelData,
                                            attributeName,
                                            targetPropertyName,
                                            null,
                                            0.0d,
                                            outputShapefile,
                                            filter,
                                            deprivationClasses,
                                            deprivationRecords,
                                            deprivationClass,
                                            false);
                                    // Output to image
                                    DW_Geotools.outputToImage(
                                            outname,
                                            outputShapefile,
                                            foregroundDW_Shapefile0,
                                            foregroundDW_Shapefile1,
                                            backgroundDW_Shapefile,
                                            attributeName,
                                            dirOut2,
                                            png_String,
                                            imageWidth,
                                            styleParameters,
                                            0,
                                            //(Integer) bLevelData[1],
                                            max,
                                            showMapsInJMapPane);
                                    if (!scaleToFirst) {
                                        styleParameters.setStyle(attributeName, null, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            File dirOut2 = new File(
                    dirOut,
                    "all");
            dirOut2.mkdirs();
            for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                boolean doLoop = false;
                if (include == null) {
                    doLoop = true;
                } else {
                    if (include.contains(i)) {
                        doLoop = true;
                    }
                }
                if (doLoop) {
                    String filename = tLeedsCABFilenames[i];
                    Object[] levelData0 = (Object[]) tLevelData[0];
                    if (levelData0 != null) {
                        Object[] levelData0i = (Object[]) levelData0[i];
                        if (levelData0i != null) {
                            TreeMap<String, Integer> levelData0i0;
                            levelData0i0 = (TreeMap<String, Integer>) levelData0i[0];
                            if (levelData0i0 != null) {
                                if (levelData0i0.size() > 0) {
                                    String outname;
                                    outname = getOutName(filename, attributeName, filter);
                                    File outputShapefile = AGDT_Geotools.getOutputShapefile(
                                            dirOut2,
                                            outname);
                                    // Select levelData0i0 Census Codes from all LSOA shapefile and create a new one
                                    TreeMap<Integer, Integer> inAndOutOfRegionCount;
                                    inAndOutOfRegionCount = selectAndCreateNewShapefile(
                                            getShapefileDataStoreFactory(),
                                            levelFC,
                                            outputFeatureType,
                                            areaCodes,
                                            levelData0i0,
                                            attributeName,
                                            targetPropertyName,
                                            null,
                                            0.0d,
                                            outputShapefile,
                                            filter,
                                            null,
                                            null,
                                            null,
                                            countClientsInAndOutOfRegion);
                                    if (countClientsInAndOutOfRegion) {
                                        result.put(outname, inAndOutOfRegionCount);
                                    }
                                    // Output to image
                                    DW_Geotools.outputToImage(
                                            outname,
                                            outputShapefile,
                                            foregroundDW_Shapefile0,
                                            foregroundDW_Shapefile1,
                                            backgroundDW_Shapefile,
                                            attributeName,
                                            dirOut2,
                                            png_String,
                                            imageWidth,
                                            styleParameters,
                                            0,
                                            // (Integer) levelData0i[1],
                                            max,
                                            showMapsInJMapPane);
                                    // Output counts
                                    Integer count;
                                    Integer inCount;
                                    Integer outCount;
                                    outCount = inAndOutOfRegionCount.get(0);
                                    inCount = inAndOutOfRegionCount.get(1);
                                    count = outCount + inCount;
                                    String outputString = "" + count + ", " + inCount + ", " + outCount;
                                    File outputFile = getOutputFile(
                                            dirOut2,
                                            outname,
                                            "txt");
                                    PrintWriter pw = Generic_StaticIO.getPrintWriter(outputFile, false);
                                    pw.println("Count, InCount, OutCount");
                                    pw.println(outputString);
                                    pw.flush();
                                    pw.close();
                                    if (!scaleToFirst) {
                                        styleParameters.setStyle(attributeName, null, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Uses a file dialog to select a file and then
     *
     * @param tAreaCodesAndShapefiles
     * @param tLeedsCABFilenames
     * @param tLevelData
     * @param deprivationRecords
     * @param dir
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     *
     *
     *
     * String attributeName = "Density"; Class<?> binding = Double.class;
     *
     * @param countClientsInAndOutOfRegion
     * @return
     */
    public TreeMap<String, TreeMap<Integer, Integer>> mapDensitiesForLevel(
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            ArrayList<Integer> include,
            Object[] tLevelData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            File dir,
            String attributeName,
            Class<?> binding,
            int filter,
            boolean scaleToFirst,
            double max, // Should not really pass this in. Should calculate the max density later on...
            boolean countClientsInAndOutOfRegion) {
        // Initialise result
        TreeMap<String, TreeMap<Integer, Integer>> result = null;
        if (countClientsInAndOutOfRegion) {
            result = new TreeMap<String, TreeMap<Integer, Integer>>();
        }
        TreeSet<String> areaCodes;
        if (filter == 0) {
            areaCodes = tAreaCodesAndShapefiles.getLeedsAreaCodes();
            foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsLADDW_Shapefile();
        } else {
            if (filter == 1) {
                areaCodes = tAreaCodesAndShapefiles.getLeedsAndNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsAndNeighbouringLADDW_Shapefile();
            } else {
                areaCodes = tAreaCodesAndShapefiles.getLeedsAndNearNeighbouringLADAreaCodes();
                foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsAndNearNeighbouringLADDW_Shapefile();
            }
        }
        FeatureCollection levelFC;
        levelFC = tAreaCodesAndShapefiles.getLevelFC();
        SimpleFeatureType levelSFT;
        levelSFT = tAreaCodesAndShapefiles.getLevelSFT();
        backgroundDW_Shapefile = tAreaCodesAndShapefiles.getLeedsLevelDW_Shapefile();

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                levelSFT,
                binding,
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
                    if (include.contains(i)) {
                        String filename = tLeedsCABFilenames[i];
                        Object[] aLevelData = (Object[]) tLevelData[0];
                        Object[] bLevelData = (Object[]) aLevelData[i];
                        if (bLevelData != null) {
                            TreeMap<String, Integer> cLevelData;
                            cLevelData = (TreeMap<String, Integer>) bLevelData[0];
                            if (cLevelData.size() > 0) {
                                String outname;
                                outname = getOutName(filename, attributeName, filter);
                                File outputShapefile = AGDT_Geotools.getOutputShapefile(
                                        mapDirectory2,
                                        outname);
                                // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
                                selectAndCreateNewShapefile(
                                        getShapefileDataStoreFactory(),
                                        levelFC,
                                        outputFeatureType,
                                        areaCodes,
                                        cLevelData,
                                        attributeName,
                                        targetPropertyName,
                                        null,
                                        0.0d,
                                        outputShapefile,
                                        filter,
                                        deprivationClasses,
                                        deprivationRecords,
                                        deprivationClass,
                                        countClientsInAndOutOfRegion);
                                // Output to image

                                if (doDebug) {
                                    DW_Geotools.doDebug = true;
                                }

                                DW_Geotools.outputToImage(
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
                                        max,
                                        showMapsInJMapPane);
                                if (!scaleToFirst) {
                                    styleParameters.setStyle(attributeName, null, 0);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            File mapDirectory2 = new File(
                    dir,
                    "all");
            mapDirectory2.mkdirs();
            for (int i = 0; i < tLeedsCABFilenames.length; i++) {
                if (include.contains(i)) {
                    String filename = tLeedsCABFilenames[i];
                    Object[] levelData0 = (Object[]) tLevelData[0];
                    Object[] levelData0i = (Object[]) levelData0[i];
                    if (levelData0i != null) {
                        TreeMap<String, Integer> levelData0i0;
                        levelData0i0 = (TreeMap<String, Integer>) levelData0i[0];
                        if (levelData0i0.size() > 0) {
                            String outname;
                            outname = getOutName(filename, attributeName, filter);
                            if (outname.startsWith("1213AllAdviceLeedsDensityClippedToLeedsLAD")) {
                                int debug = 1;
                                doDebug = true;
                            }

                            File outputShapefile = AGDT_Geotools.getOutputShapefile(
                                    mapDirectory2,
                                    outname);
                            // Select levelData0i0 Census Codes from all LSOA shapefile and create a new one
                            TreeMap<Integer, Integer> inAndOutOfRegionCount;
                            inAndOutOfRegionCount = selectAndCreateNewShapefile(
                                    getShapefileDataStoreFactory(),
                                    levelFC,
                                    outputFeatureType,
                                    areaCodes,
                                    levelData0i0,
                                    attributeName,
                                    targetPropertyName,
                                    null,
                                    0.0d,
                                    outputShapefile,
                                    filter,
                                    null,
                                    deprivationRecords,
                                    null,
                                    countClientsInAndOutOfRegion);
                            if (countClientsInAndOutOfRegion) {
                                result.put(outname, inAndOutOfRegionCount);
                            }
                            // Output to image
                            DW_Geotools.outputToImage(
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
                                    max,
                                    showMapsInJMapPane);
                            if (!scaleToFirst) {
                                styleParameters.setStyle(attributeName, null, 0);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param tAreaCodesAndShapefiles
     * @param tLeedsCABFilenames
     * @param levelData
     * @param dir
     * @param populationMultiplier
     * @param max
     * @param filter
     * @param scaleToFirst
     */
    public void mapRatesForLevel(
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tLeedsCABFilenames,
            Object[] levelData,
            File dir,
            String attributeName,
            Class<?> binding,
            double populationMultiplier,
            int max,
            int filter,
            boolean scaleToFirst) {

        TreeSet<String> areaCodes;
        areaCodes = tAreaCodesAndShapefiles.getLeedsAreaCodes();
        //File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = tAreaCodesAndShapefiles.getLevelFC();
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = tAreaCodesAndShapefiles.getLevelSFT();

        backgroundDW_Shapefile = tAreaCodesAndShapefiles.getLeedsLevelDW_Shapefile();

        foregroundDW_Shapefile1 = tAreaCodesAndShapefiles.getLeedsLADDW_Shapefile();

        // Get output feature types
        String name = attributeName + "FeatureType";
        SimpleFeatureType outputFeatureType;
        outputFeatureType = getFeatureType(
                tLSOAFeatureType,
                binding,
                attributeName,
                name);

        // Read level population data
        String area;
        if (level.equalsIgnoreCase("LSOA")) {
            area = "England";
        } else {
            area = "GreatBritain";
        }
        TreeMap<String, Integer> population = getPopData(
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
            File outputShapefile = AGDT_Geotools.getOutputShapefile(
                    dir,
                    outname);
            // Select cLSOAData LSOA Codes from all LSOA shapefile and create a new one
            selectAndCreateNewShapefile(
                    getShapefileDataStoreFactory(),
                    tLSOAFeatureCollection,
                    outputFeatureType,
                    areaCodes,
                    cLSOAData,
                    attributeName,
                    targetPropertyName,
                    population,
                    populationMultiplier,
                    outputShapefile,
                    filter,
                    null,
                    null,
                    null,
                    false);
            // Output to image
            DW_Geotools.outputToImage(
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
                    max,
                    showMapsInJMapPane);
            if (!scaleToFirst) {
                styleParameters.setStyle(attributeName, null, 0);
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
}
