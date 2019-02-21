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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.adviceleeds;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.math.Math_BigDecimal;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletEnquiryID;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_StyleParameters;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ChoroplethMapsAbstract;

/**
 *
 * @author geoagdt
 */
public class DW_ChoroplethMapsAdviceLeeds extends DW_ChoroplethMapsAbstract {

    protected boolean countInAndOutOfRegion;

    /**
     * Either an instance of DW_ID_ClientOutletEnquiryID or ClientBureauOutletID
     */
    private Object IDType;

    /**
     * Either "DW_ID_ClientOutletEnquiryID" or "ClientBureauOutletID" depending
     * on IDType
     */
    private String IDTypeName;

    public DW_ChoroplethMapsAdviceLeeds(DW_Environment env) {
        super(env);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_ChoroplethMapsAdviceLeeds(null).run();
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
    public void run() throws Exception, Error {
//        showMapsInJMapPane = false;
////        showMapsInJMapPane = true;
//        imageWidth = 1000;
//        // Initialise Parameters
//        // Style Parameters
//        /*
//         * YlOrRd,PRGn,PuOr,RdGy,Spectral,Grays,PuBuGn,RdPu,BuPu,YlOrBr,Greens,
//         * BuGn,Accents,GnBu,PuRd,Purples,RdYlGn,Paired,Blues,RdBu,Oranges,
//         * RdYlBu,PuBu,OrRd,Set3,Set2,Set1,Reds,PiYG,Dark2,YlGn,BrBG,YlGnBu,
//         * Pastel2,Pastel1
//         */
////        ColorBrewer brewer = ColorBrewer.instance();
////        //String[] paletteNames = brewer.getPaletteNames(0, nClasses);
////        String[] paletteNames = brewer.getPaletteNames();
////        for (int i = 0; i < paletteNames.length; i++) {
////            System.out.println(paletteNames[i]);
////        }
//        styleParameters = new Geotools_StyleParameters();
//        styleParameters.setnClasses(9);
//        styleParameters.setPaletteName("Reds");
//        styleParameters.setAddWhiteForZero(true);
//        styleParameters.setForegroundStyleName(0,"Foreground Style 0");
////        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
//        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
//        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
//                Color.GREEN,
//                Color.WHITE));
//        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
//        styleParameters.setBackgroundStyle(DW_Style.createDefaultPolygonStyle(
//                Color.BLACK,
//                Color.WHITE));
//        styleParameters.setBackgroundStyleTitle("Background Style");
//
//        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();
//
//        // Initialise tDW_ID_ClientTypes
//        ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes;
//        tDW_ID_ClientTypes = new ArrayList<DW_ID_ClientID>();
//        tDW_ID_ClientTypes.add(new DW_ID_ClientID());
////        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletID());
////        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletEnquiryID());
////        tDW_ID_ClientTypes.add(new DW_ID_ClientEnquiryID());
//
//        // Switches for different stylings
//        // -------------------------------
//        // commonStyling is where all maps are produced according to a common
//        // classification
//        commonStyling = true;
//        // individualStyling is where all maps are produced according to an 
//        // individual classification
//        individualStyling = true;
//        // withBoundariesStyling draws an outline to the boundaries of each 
//        // region. 
//        withBoundariesStyling = true;
//        // withoutBoundariesStyling does not draw an outline to the boundaries  
//        // of each region. 
//        withoutBoundariesStyling = true;
//        // Initialise classificationFunctionNames
//        classificationFunctionNames = new ArrayList<String>();
//        classificationFunctionNames.add("Jenks");
//       classificationFunctionNames.add("Quantile");
//        classificationFunctionNames.add("EqualInterval");
//        doCount = true;
////        doCount = false;
//        doDensity = true;
////        doDensity = false;
////        doDeprivation = true;
//        doDeprivation = false;
//        doFilter = new boolean[4];
//        doFilter[0] = true;
////        doFilter[0] = false;
////        doFilter[1] = true;
//        doFilter[1] = false;
////        doFilter[2] = true;
//        doFilter[2] = false;
////        doFilter[3] = true;
//        doFilter[3] = false;
//        // Initialise levels and targetPropertyNames. 
//        levels = new ArrayList<String>();
//        targetPropertyNames = new HashMap<String, String>();
//        // MSOA
//        level = "MSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "MSOA11CD");
//        // LSOA
//        level = "LSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "LSOA11CD");
//        // OA
//        level = "OA";
//        levels.add(level);
//        targetPropertyNames.put(level, "CODE");
////        // Postcode Area
////        level = "PostcodeArea";
////        levels.add(level);
////        targetPropertyNames.put(level, "PostArea");
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
//
//        // Currently counting only done for level.equalsIgnoreCase("MSOA")
//        countInAndOutOfRegion = true;
//        //countInAndOutOfRegion = false;
//
//        run(tDW_ID_ClientTypes);
    }

//    /**
//     * @param tDW_ID_ClientTypes
//     */
//    public void run(ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes) {
//        Iterator<DW_ID_ClientID> ite;
//        ite = tDW_ID_ClientTypes.iterator();
//        while (ite.hasNext()) {
//            IDType = ite.next();
//            IDTypeName = IDType.getClass().getSimpleName();
//            // General
//            mapDirectory = new File(
//                    DW_Files.getOutputAdviceLeedsMapsDir(),
//                    "choropleth");
//            mapDirectory = new File(
//                    mapDirectory,
//                    IDTypeName);
//            if (withBoundariesStyling) {
//                runAll(true);
//            }
//            if (withoutBoundariesStyling) {
//                runAll(false);
//            }
//        }
//    }
//
//    public void runAll(
//            boolean drawBoundaries) {
//        styleParameters.setDrawBoundaries(drawBoundaries);
//        Iterator<String> ite;
//        ite = levels.iterator();
//        while (ite.hasNext()) {
//            level = ite.next();
//            targetPropertyName = targetPropertyNames.get(level);
//            Iterator<String> ite2;
//            ite2 = classificationFunctionNames.iterator();
//            while (ite2.hasNext()) {
//                String classificationFunctionName;
//                classificationFunctionName = ite2.next();
//                styleParameters.setClassificationFunctionName(classificationFunctionName);
//                runAllLevel();
//            }
//        }
//    }
//
//    public void runAllLevel() {
//        boolean thisCountClientsInAndOutOfRegion;
//        thisCountClientsInAndOutOfRegion = false;
//        if (level.equalsIgnoreCase("MSOA") && countInAndOutOfRegion == true) {
//            thisCountClientsInAndOutOfRegion = true;
//        }
//        DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles;
//        tAreaCodesAndShapefiles = new DW_AreaCodesAndShapefiles(
//                env,
//                level,
//                targetPropertyName,
//                getShapefileDataStoreFactory());
//        Object[] tLevelData;
//        TreeMap<String, Census_DeprivationDataRecord> deprivationRecords;
////        // Map SHBE data
////        deprivationRecords = null;
////        runSHBE(
////                deprivationRecords,
////                tCensusCodesAndShapefiles);
////        deprivationRecords = DW_ProcessorAbstract.getDeprivation_Data();
////        runSHBE(
////                deprivationRecords,
////                tCensusCodesAndShapefiles);
//        // Map Advice Leeds data
//        File generatedAdviceLeedsDir;
//        String[] tAdviceLeedsFilenames;
//
//        generatedAdviceLeedsDir = new File(
//                DW_Files.getGeneratedAdviceLeedsDir(),
//                "Combined");
//        generatedAdviceLeedsDir = new File(
//                generatedAdviceLeedsDir,
//                level);
//        generatedAdviceLeedsDir = new File(
//                generatedAdviceLeedsDir,
//                IDType.getClass().getSimpleName());
//
//        tAdviceLeedsFilenames = getAdviceLeedsFilenames();
//        tLevelData = getLevelData(
//                generatedAdviceLeedsDir,
//                tAdviceLeedsFilenames,
//                null);
//        int max = (Integer) tLevelData[1];
//        System.out.println("Max clients in any area = " + max);
//        boolean scaleToFirst;
//
//        /*
//         * (filter == 0) Clip all results to Leeds LAD
//         * (filter == 1) Clip all results to Leeds And Neighbouring LADs (showing results for all Leeds LAD)
//         * (filter == 2) Clip all results to Leeds And Near Neighbouring LADs (showing results for all Leeds LAD)
//         * (filter == 3) No clipping
//         */
//        for (int filter = 0; filter < 4; filter++) {
//            if (doFilter[filter] == true) {
//                deprivationRecords = null;
//                if (individualStyling) {
//                    scaleToFirst = false;
//                    runAdviceLeeds(
//                            deprivationRecords,
//                            tAreaCodesAndShapefiles,
//                            tAdviceLeedsFilenames,
//                            tLevelData,
//                            filter,
//                            scaleToFirst,
//                            (Double) tLevelData[1],
//                            thisCountClientsInAndOutOfRegion);
//                }
//                if (commonStyling) {
//                    scaleToFirst = true;
//                    runAdviceLeeds(
//                            deprivationRecords,
//                            tAreaCodesAndShapefiles,
//                            tAdviceLeedsFilenames,
//                            tLevelData,
//                            filter,
//                            scaleToFirst,
//                            (Double) tLevelData[1],
//                            false);
//                }
//                if (doDeprivation) {
//                    // Get deprivation data
//                    deprivationRecords = DW_ProcessorAbstract.getDeprivation_Data();
//                    if (individualStyling) {
//                        scaleToFirst = false;
//                        runAdviceLeeds(
//                                deprivationRecords,
//                                tAreaCodesAndShapefiles,
//                                tAdviceLeedsFilenames,
//                                tLevelData,
//                                filter,
//                                scaleToFirst,
//                            (Double) tLevelData[1],
//                                thisCountClientsInAndOutOfRegion);
//                    }
//                    if (commonStyling) {
//                        scaleToFirst = true;
//                        runAdviceLeeds(
//                                deprivationRecords,
//                                tAreaCodesAndShapefiles,
//                                tAdviceLeedsFilenames,
//                                tLevelData,
//                                filter,
//                                scaleToFirst,
//                            (Double) tLevelData[1],
//                                false);
//                    }
//                }
//            }
////            if (!showMapsInJMapPane) {
////                // Tidy up
////                tCensusCodesAndShapefiles.dispose();
////            }
//        }
//    }
//
//    public void runAdviceLeeds(
//            TreeMap<String, Census_DeprivationDataRecord> deprivationRecords,
//            DW_AreaCodesAndShapefiles tAreaCodesAndShapefiles,
//            String[] tLeedsCABFilenames,
//            Object[] tLevelData,
//            int filter,
//            boolean scaleToFirst,
//            double max,
//            boolean doInAndOutOfRegionCounts) {
//
//        String style;
//        if (scaleToFirst) {
//            style = "CommonlyStyled";
//        } else {
//            style = "IndividuallyStyled";
//        }
//        File dir;
//        dir = new File(
//                mapDirectory,
//                level);
//        if (styleParameters.isDrawBoundaries()) {
//            dir = new File(
//                    dir,
//                    "WithBoundaries");
//        } else {
//            dir = new File(
//                    dir,
//                    "WithoutBoundaries");
//        }
//        dir = new File(
//                dir,
//                style + "/" + styleParameters.getClassificationFunctionName());
//
//        TreeMap<String, TreeMap<Integer, Integer>> inAndOutOfRegionCounts = null;
//        String attributeName;
//        Class<?> binding;
//        if (doCount) {
//            attributeName = "Count";
//            binding = Integer.class;
//            inAndOutOfRegionCounts = mapCountsForLevel(tAreaCodesAndShapefiles,
//                    tLeedsCABFilenames,
//                    null,
//                    tLevelData,
//                    deprivationRecords,
//                    dir,
//                    attributeName,
//                    binding,
//                    filter,
//                    scaleToFirst,
//                    max,
//                    doInAndOutOfRegionCounts);
//        }
//        if (doDensity) {
//            /*
//             * May want to set max to something different. This will always be 
//             * honest. The maz is used to set the label in the legend, so until 
//             * we have caluclated the densities for each map, we do not know 
//             * what the max is, so it is hard to set to something precise at the 
//             * outset without it being erroneous. Ideally it would be set to be 
//             * something specific.
//             */
//            attributeName = "Density";
//            binding = Double.class;
//            inAndOutOfRegionCounts = mapDensitiesForLevel(
//                    tAreaCodesAndShapefiles,
//                    tLeedsCABFilenames,
//                    null,
//                    tLevelData,
//                    deprivationRecords,
//                    dir,
//                    attributeName,
//                    binding,
//                    filter,
//                    scaleToFirst,
//                    Double.POSITIVE_INFINITY,
//                    doInAndOutOfRegionCounts);
//        }
//        if (doInAndOutOfRegionCounts) {
//            String aIDTypeName;
//            aIDTypeName = IDType.getClass().getSimpleName();
//            File outputInAndOutOfRegionCountsDir;
//            outputInAndOutOfRegionCountsDir = new File(
//                    DW_Files.getOutputAdviceLeedsTablesDir(),
//                    aIDTypeName);
//            outputInAndOutOfRegionCountsDir.mkdirs();
//            File outputInAndOutOfRegionCountsFile;
//            outputInAndOutOfRegionCountsFile = new File(
//                    outputInAndOutOfRegionCountsDir,
//                    "InAndOutOfRegionCounts" + filter + ".csv");
//            PrintWriter pw;
//            pw = Generic_IO.getPrintWriter(outputInAndOutOfRegionCountsFile, false);
//            if (IDType instanceof DW_ID_ClientOutletEnquiryID) {
//                pw.println("Year AdviceLeeds Service Region,EnquiryClientCountOutOfRegion,EnquiryClientCountInRegion,PercentageOutOfRegion");
//            } else {
//                pw.println("Year AdviceLeeds Service Region,ClientCountOutOfRegion,ClientCountInRegion,PercentageOutOfRegion");
//            }
//            Iterator<String> ite;
//            ite = inAndOutOfRegionCounts.keySet().iterator();
//            while (ite.hasNext()) {
//                String name = ite.next();
//                TreeMap<Integer, Integer> inAndOutOfRegionCount;
//                inAndOutOfRegionCount = inAndOutOfRegionCounts.get(name);
//                pw.print(name);
//                Iterator<Integer> ite2;
//                ite2 = inAndOutOfRegionCount.keySet().iterator();
//                Integer inCount = null;
//                Integer outCount = null;
//                while (ite2.hasNext()) {
//                    Integer inOrOut;
//                    inOrOut = ite2.next();
//                    if (inOrOut == 0) {
//                        inCount = inAndOutOfRegionCount.get(inOrOut);
//                        pw.print("," + inCount);
//                    } else {
//                        outCount = inAndOutOfRegionCount.get(inOrOut);
//                        pw.print("," + outCount);
//                    }
//                }
//                // Get Percentage
//                if (outCount == 0) {
//                    pw.println(",100");
//                } else {
//                    double percentage;
//                    percentage = 100.0d * (double) inCount / (double) outCount;
//                    //pw.println("," + percentage);
//                    BigDecimal p;
//                    p = BigDecimal.valueOf(percentage);
//                    int decimalPlaces = 3;
//                    BigDecimal roundedp;
//                    roundedp = Math_BigDecimal.roundIfNecessary(
//                            p, decimalPlaces, RoundingMode.UP);
//                    pw.println("," + roundedp.toPlainString());
//                }
//            }
//            pw.flush();
//            pw.close();
//        }
//    }
//
//    public static String[] getAdviceLeedsFilenames() {
//        String[] result = new String[27];
//        //result[0] = "1213AllCABClients.csv";
//        //result[0] = "1213AllAdviceLeeds.csv"; // Should do a max one for mapping purposes too!
//        result[0] = "1213Scale.csv"; // For mapping purposes too!
//        result[1] = "1213OTLEY - LS21 1BG.csv";
//        result[2] = "1213ASIAN WOMANS SERVICE - LS15 8QR.csv";
//        result[3] = "1213PUDSEY.csv";
//        result[4] = "1213CHILDRENS CENTRES TELEPHONE.csv";
//        result[5] = "1213REFUGEE INTEGRATION ADVICE SERVICE.csv";
//        result[6] = "1213CITY - LS2 7DT.csv";
//        result[7] = "1213SOUTH LEEDS CHILDRENS CENTRES.csv";
//        result[8] = "1213CROSSGATES - LS15 8QR.csv";
//        result[9] = "1213SOUTH LEEDS PCT.csv";
//        result[10] = "1213EAST LEEDS CHILDRENS CENTRES.csv";
//        result[11] = "1213SPECIALIST SERVICES - LS2 7DT.csv";
//        result[12] = "1213EL PCT SERVICES - LS15 8QR.csv";
//        result[13] = "1213TELEPHONE ADVICE.csv";
//        result[14] = "1213FIF.csv";
//        result[15] = "1213TELEPHONE GATEWAY LEEDS CAB.csv";
//        result[16] = "1213HOME VISITS - LS2 7DT.csv";
//        result[17] = "1213TELEPHONE RECEPTION LEEDS CAB.csv";
//        result[18] = "1213MENTAL HEALTH - LS2 7DT.csv";
//        result[19] = "1213WEST LEEDS PCT SERVICE - LS28 7AB.csv";
//        result[20] = "1213MORLEY - LS27 9DY.csv";
//        result[21] = "1213WHHL - WARM HOMES HEALTHY LIVES.csv";
//        result[22] = "1213CHAPELTOWN.csv";
//        result[23] = "1213AllCABOutlet.csv";
//        result[24] = "1213AllCABNonOutlet.csv";
//        result[25] = "1213LCC_WRU.csv";
//        result[26] = "1213AllAdviceLeeds.csv";
//        return result;
//    }
}
