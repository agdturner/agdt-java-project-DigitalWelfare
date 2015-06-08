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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_AreaCodesAndShapefiles;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMaps_SHBE extends DW_ChoroplethMaps {

    public DW_ChoroplethMaps_SHBE() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_ChoroplethMaps_SHBE().run();
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
        styleParameters = new AGDT_StyleParameters();
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
        withBoundariesStyling = true;
        // withoutBoundariesStyling does not draw an outline to the boundaries  
        // of each region. 
        withoutBoundariesStyling = true;
        // Initialise classificationFunctionNames
        classificationFunctionNames = new ArrayList<String>();
        classificationFunctionNames.add("Jenks");
        classificationFunctionNames.add("Quantile");
        classificationFunctionNames.add("EqualInterval");
        doCount = true;
//        doCount = false;
        doDensity = true;
//        doDensity = false;
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
//        // OA
//        level = "OA";
//        levels.add(level);
//        targetPropertyNames.put(level, "CODE");
        // LSOA
        level = "LSOA";
        levels.add(level);
        targetPropertyNames.put(level, "LSOA11CD");
//        // MSOA
//        level = "MSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "MSOA11CD");
//        // Postcode Area
//        level = "PostcodeArea";
//        levels.add(level);
//        targetPropertyNames.put(level, "PostArea");
//        // Postcode District
//        level = "PostcodeDistrict";
//        levels.add(level);
//        targetPropertyNames.put(level, "PostDist");
//        // Postcode Sector
//        level = "PostcodeSector";
//        levels.add(level);
//        targetPropertyNames.put(level, "RMSect");
//        // Postcodes Unit 
//        level = "PostcodeUnit";
//        levels.add(level);
//        targetPropertyNames.put(level, "POSTCODE");
        mapDirectory = new File(
                DW_Files.getOutputSHBEDir(),
                "Maps");
        mapDirectory = new File(
                mapDirectory,
                "Choropleth");
        if (withBoundariesStyling) {
            runAll(true);
        }
        if (withoutBoundariesStyling) {
            runAll(false);
        }
    }

    public void runAll(
            boolean drawBoundaries) {
        styleParameters.setDrawBoundaries(drawBoundaries);
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
                runAllLevel();
            }
        }
    }

    public void runAllLevel() {
        DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles;
        tAreaCodesAndShapefiles = new DW_AreaCodesAndShapefiles(
                level,
                targetPropertyName,
                getShapefileDataStoreFactory());
        TreeMap<String, Deprivation_DataRecord> deprivationRecords;
        String[] tFilenames;
        tFilenames = getFilenames();
        boolean scaleToFirst;
        Object[] tLevelData;

        String[] types;
        types = new String[4];
        types[0] = "NewEntrant";
        types[1] = "Stable";
        types[2] = "Churn";
        types[3] = "AllClaimants";
        //for (int i = 0; i < types.length; i++) {
            int i = 2;
        //for (int i = 0; i < types.length; i++) {
            //if (i > )
            File dir = new File(
                    DW_Files.getGeneratedSHBEDir(level),
                    types[i]);
            tLevelData = getLevelData(
                    dir,
                    tFilenames);
            if (individualStyling) {
                scaleToFirst = false;
                // Map SHBE data
                deprivationRecords = null;
                runSHBE(
                        deprivationRecords,
                        tAreaCodesAndShapefiles,
                        tFilenames,
                        tLevelData,
                        types[i],
                        scaleToFirst);
                if (doDeprivation) {
                    deprivationRecords = DW_Processor.getDeprivation_Data();
                    runSHBE(
                            deprivationRecords,
                            tAreaCodesAndShapefiles,
                            tFilenames,
                            tLevelData,
                        types[i],
                            scaleToFirst);
                }
            }
            if (commonStyling) {
                scaleToFirst = true;
                // Map SHBE data
                deprivationRecords = null;
                runSHBE(
                        deprivationRecords,
                        tAreaCodesAndShapefiles,
                        tFilenames,
                        tLevelData,
                        types[i],
                        scaleToFirst);
                if (doDeprivation) {
                    deprivationRecords = DW_Processor.getDeprivation_Data();
                    runSHBE(
                            deprivationRecords,
                            tAreaCodesAndShapefiles,
                            tFilenames,
                            tLevelData,
                        types[i],
                            scaleToFirst);
                }
            }
//        if (!showMapsInJMapPane) {
//            // Tidy up
//            tAreaCodesAndShapefiles.dispose();
//        }
        //}
    }

    public void runSHBE(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
            String[] tFilenames,
            Object[] tLevelData,
            String type,
            boolean scaleToFirst) {
        int filter = 0;
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
        dir = new File(
                dir,
                type);
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

        TreeMap<String, TreeMap<Integer, Integer>> inAndOutOfRegionCounts = null;
        String attributeName;
        Class<?> binding;
        if (doCount) {
            attributeName = "Count";
            binding = Integer.class;
            inAndOutOfRegionCounts = mapCountsForLevel(
                    tAreaCodesAndShapefiles,
                    tFilenames,
                    tLevelData,
                    deprivationRecords,
                    dir,
                    attributeName,
                    binding,
                    filter,
                    scaleToFirst,
                    false);
        }
        if (doDensity) {
            attributeName = "Density";
            binding = Double.class;
            inAndOutOfRegionCounts = mapDensitiesForLevel(
                    tAreaCodesAndShapefiles,
                    tFilenames,
                    tLevelData,
                    deprivationRecords,
                    dir,
                    attributeName,
                    binding,
                    filter,
                    scaleToFirst,
                    false);
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
        String[] result = new String[1];
        //result[0] = "2013April.csv";
        //result[0] = "2014July.csv";
        result[0] = "2014Apr.csv";
        //result[0] = "2008April.csv";
        //result[1] = "2008October.csv";
//        result[2] = "2009April.csv";
//        result[3] = "2009October.csv";
//        result[4] = "2010April.csv";
//        result[5] = "2010October.csv";
//        result[6] = "2011March.csv";
//        result[7] = "2011April.csv";
//        result[8] = "2011July.csv";
//        result[9] = "2011October.csv";
//        result[10] = "2012January.csv";
//        result[11] = "2012April.csv";
//        result[12] = "2012July.csv";
//        result[13] = "2012October.csv";
//        result[14] = "2013January.csv";
//        result[15] = "2013February.csv";
//        result[16] = "2013March.csv";
//        result[17] = "2013April.csv";
//        result[18] = "2013May.csv";
//        result[19] = "2013June.csv";
//        result[20] = "2013July.csv";
//        result[21] = "2013August.csv";
//        result[22] = "2013September.csv";
//        result[23] = "2013Oct.csv";
//        result[24] = "2013Nov.csv";
//        result[25] = "2013Dec.csv";
//        result[26] = "2014Jan.csv";
//        result[27] = "2014Feb.csv";
//        result[28] = "2014Mar.csv";
//        result[29] = "2014Apr.csv";
//        result[30] = "2014May.csv";
//        result[31] = "2014June.csv";
//        result[32] = "2014July.csv";
//        result[33] = "2014August.csv";
//        result[34] = "2014September.csv";
//        result[35] = "2014October.csv";
//        result[36] = "2014November.csv";
//        result[37] = "2014December.csv";
//        result[38] = "2015January.csv";
//        result[39] = "2015February.csv";
//        result[40] = "2015March.csv";
//        result[41] = "2015April.csv";
        return result;
    }
}
