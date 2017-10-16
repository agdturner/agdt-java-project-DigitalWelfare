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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc.DW_LineMapsLCC;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.statistics.Grids_GridStatistics0;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_DensityMapsAbstract;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getYM3s;
////import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps.initONSPDLookups;
import uk.ac.leeds.ccg.andyt.vector.core.Vector_Environment;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;

/**
 *
 * @author geoagdt
 */
public class DW_LineDensityDifferenceMaps_LCC extends DW_DensityMapsAbstract {

    protected Vector_Environment ve;
    protected DW_LineMapsLCC DW_LineMaps_LCC;

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<AGDT_Shapefile> midgrounds;
    protected ArrayList<AGDT_Shapefile> foregrounds;

    public DW_LineDensityDifferenceMaps_LCC(DW_Environment env) {
        super(env);
        DW_LineMaps_LCC = new DW_LineMapsLCC(env);
        ve = new Vector_Environment();
    }

    //DW_StyleParameters styleParameters;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_LineDensityDifferenceMaps_LCC(null).run();
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

    public void run() throws Exception, Error {
//        // If showMapsInJMapPane is true, the maps are presented in individual 
//        // JMapPanes
//        showMapsInJMapPane = false;
//        //showMapsInJMapPane = true;
//        //outputESRIAsciigrids = false;
//        imageWidth = 1000;
//        initONSPDLookups();
//        // Initialise styleParameters
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
//        styleParameters = new AGDT_StyleParameters();
//        styleParameters.setnClasses(5);
//        styleParameters.setPaletteName("Reds");
//        styleParameters.setPaletteName2("Blues");
//        styleParameters.setAddWhiteForZero(true);
////        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
////        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
////        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
//        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
//                Color.GREEN,
//                Color.WHITE));
//        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
//
//        mapDirectory = new File(
//                DW_Files.getOutputAdviceLeedsMapsDir(),
//                "density");
//        imageWidth = 1000;
//
////        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();
//        init();
//
//        /*Grid Parameters
//         *_____________________________________________________________________
//         */
//        handleOutOfMemoryErrors = true;
//        File processorDir = new File(
//                mapDirectory,
//                "processor");
//        processorDir.mkdirs();
//        ge = new Grids_Environment();
//        eage = new Grids_ESRIAsciiGridExporter();
//        ie = new ImageExporter(ge);
//        gp = new Grid2DSquareCellProcessorGWS(ge);
//        gp.set_Directory(processorDir, false, handleOutOfMemoryErrors);
//        gcf = new Grid2DSquareCellDoubleChunkArrayFactory();
//        chunkNRows = 300;//250; //64
//        chunkNCols = 350;//300; //64
//        gf = new Grids_Grid2DSquareCellDoubleFactory(
//                processorDir,
//                chunkNRows,
//                chunkNCols,
//                gcf,
//                -9999d,
//                ge,
//                handleOutOfMemoryErrors);
////        // Jenks runs
////        styleParameters.setClassificationFunctionName("Jenks");
////        commonStyling = true;
////        individualStyling = true;
////        runAll(IDType);
////        // Quantile runs
////        styleParameters.setClassificationFunctionName("Quantile");
////        styleParameters.setStylesNull();
////        commonStyling = true;
////        individualStyling = true;
////        runAll(tDW_ID_ClientTypes);
//        // Equal Interval runs
//        styleParameters.setClassificationFunctionName("EqualInterval");
//        styleParameters.setStylesNull();
////        commonStyling = true;
//        individualStyling = true;
//
//        //int resolutionMultiplier = 4;
//        cellsize = 50;
//        maxCellDistanceForGeneralisation = 4; //8;
//        runAll();
////        for (maxCellDistanceForGeneralisation = 4; maxCellDistanceForGeneralisation <= 32; maxCellDistanceForGeneralisation *= 2) {
////            cellsize = (1600 / maxCellDistanceForGeneralisation) / resolutionMultiplier;
////            runAll(resolutionMultiplier);
////        }
    }

    //public void runAll(int resolutionMultiplier) {
    public void runAll() throws Exception, Error {
//        TreeMap<String, ArrayList<Integer>> includes;
//        includes = env.getDW_SHBE_Handler().getIncludes();
//        includes.remove("All");
////        includes.remove("Yearly");
////        includes.remove("6Monthly");
////        includes.remove("3Monthly");
////        includes.remove("MonthlyUO");
////        includes.remove("Monthly");
//
//        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChanges;
//        allTenancyTypeChanges = DW_LineMaps_LCC.getAllTenancyTypeChanges();
//
//        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesGrouped;
//        allTenancyTypeChangesGrouped = DW_LineMaps_LCC.getAllTenancyTypeChangesGrouped();
//
//        Iterator<String> includesIte;
//        includesIte = includes.keySet().iterator();
//        while (includesIte.hasNext()) {
//            String includeName;
//            includeName = includesIte.next();
//            ArrayList<Integer> include;
//            include = includes.get(includeName);
//
//            TreeMap<String, ArrayList<String>> yearMonths;
//            yearMonths = DW_LineMaps_LCC.getYM3s(include);
//
//            ArrayList<Boolean> b;
//            b = new ArrayList<Boolean>();
//            b.add(true);
//            b.add(false);
//
//            boolean scaleToFirst;
//
//            Iterator<Boolean> iteb;
//            iteb = b.iterator();
//            while (iteb.hasNext()) {
//                boolean doUnderOccupancyData;
//                doUnderOccupancyData = iteb.next();
//                if (doUnderOccupancyData) {
//                    Iterator<Boolean> iteb2;
//                    iteb2 = b.iterator();
//                    while (iteb2.hasNext()) {
//                        boolean doCouncil;
//                        doCouncil = iteb2.next();
//                        if (commonStyling) {
//                            scaleToFirst = true;
//                            postcodeMaps(
//                                    //resolutionMultiplier,
//                                    yearMonths,
//                                    includeName,
//                                    include,
//                                    allTenancyTypeChanges.get(doUnderOccupancyData),
//                                    doUnderOccupancyData,
//                                    doCouncil,
//                                    scaleToFirst);
//                        }
//                        if (individualStyling) {
//                            scaleToFirst = false;
//                            postcodeMaps(
//                                    //resolutionMultiplier,
//                                    yearMonths,
//                                    includeName,
//                                    include,
//                                    allTenancyTypeChanges.get(doUnderOccupancyData),
//                                    doUnderOccupancyData,
//                                    doCouncil,
//                                    scaleToFirst);
//                        }
//                    }
//                } else {
//                    if (commonStyling) {
//                        scaleToFirst = true;
//                        postcodeMaps(
//                                //resolutionMultiplier,
//                                yearMonths,
//                                includeName,
//                                include,
//                                allTenancyTypeChanges.get(doUnderOccupancyData),
//                                doUnderOccupancyData,
//                                false,
//                                scaleToFirst);
//                    }
//                    if (individualStyling) {
//                        scaleToFirst = false;
//                        postcodeMaps(
//                                //resolutionMultiplier,
//                                yearMonths,
//                                includeName,
//                                include,
//                                allTenancyTypeChanges.get(doUnderOccupancyData),
//                                doUnderOccupancyData,
//                                false,
//                                scaleToFirst);
//                    }
//                }
//            }
//        }
//        // Tidy up
//        if (!showMapsInJMapPane) {
////            Iterator<AGDT_Shapefile> ite1;
////            ite1 = midgrounds.iterator();
////            while (ite1.hasNext()) {
////                AGDT_Shapefile s = ite1.next();
////                s.dispose();
////            }
//            Iterator<AGDT_Shapefile> ite;
//            ite = foregrounds.iterator();
//            while (ite.hasNext()) {
//                ite.next().dispose();
////                tLSOACodesAndLeedsLSOAShapefile.dispose();
////            tMSOACodesAndLeedsMSOAShapefile.dispose();
//            }
//        }
    }

    private void init() {
        //initStyleParameters();
//        DW_Maps.setMapDirectory(DW_Files.getOutputSHBELineMapsDir());
        
        foregrounds = new ArrayList<AGDT_Shapefile>();
        //midgrounds = new ArrayList<AGDT_Shapefile>();
//        backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                env,
                "LSOA", 
                targetPropertyNameLSOA, 
                DW_Maps.getShapefileDataStoreFactory());
        foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

//    private void initStyleParameters() {
//        styleParameters = new DW_StyleParameters();
//        styleParameters.setForegroundStyle(
//                DW_Style.createDefaultPolygonStyle(
//                        Color.GREEN,
//                        Color.WHITE), 0);
//        styleParameters.setMidgroundStyle(
//                DW_Style.createDefaultLineStyle(Color.LIGHT_GRAY), 0);
//    }
    public void postcodeMaps(
            //int resolutionMultiplier,
            TreeMap<String, ArrayList<String>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean scaleToFirst) {

        // Single run
        File dirIn1 = new File("/scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/");
        File dirOut1 = new File(
                dirIn1,
                "Density");
//        grida File /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/1-3/1-3E/Density/IndividualStyle/1-3EE/1-3EE_554_ncols_680_cellsize_50.0.asc does not exist!
//grida File /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/1-3/3E-1/Density/IndividualStyle/1-3ES/1-3ES_554_ncols_680_cellsize_50.0.asc does not exist!
        String name;
        name = "2013_January_TO_2015_September";
        String namea;
        namea = "1-3";
        String nameb;
        nameb = getReverseName(namea);
        doDensity(
                dirIn1,
                name,
                namea,
                nameb,
                scaleToFirst);
        System.exit(0);

//        //        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
//        //        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
//        // Other variables for selecting and output
//        File dirIn = new File(
//                mapDirectory,
//                "Tenancy");
//        dirIn = new File(
//                dirIn,
//                "All");
//        dirIn = new File(
//                dirIn,
//                "TenancyTypeTransition");
//        dirIn = new File(
//                dirIn,
//                "CheckedPreviousTenure");
//        dirIn = new File(
//                dirIn,
//                "TenancyAndPostcodeChanges");
//        if (doUnderOccupancyData) {
//            dirIn = new File(
//                    dirIn,
//                    "UnderOccupied");
//            if (doCouncil) {
//                dirIn = new File(
//                        dirIn,
//                        "Council");
//            } else {
//                dirIn = new File(
//                        dirIn,
//                        "RSL");
//            }
//        } else {
//            dirIn = new File(
//                    dirIn,
//                    "All");
//        }
//        dirIn = new File(
//                dirIn,
//                "PostcodeChanged");
//        dirIn = new File(
//                dirIn,
//                includeName);
//        dirIn = new File(
//                dirIn,
//                "Ungrouped");
//        dirIn = new File(
//                dirIn,
//                "PostcodeChanges");
//        dirIn = new File(
//                dirIn,
//                "CheckedPreviousPostcode");
//        //dirOut.mkdirs(); // done later
//        //filename = "PostcodeChanges.csv";
//        Iterator<String> yearMonthsIte;
//        yearMonthsIte = yearMonths.keySet().iterator();
//        while (yearMonthsIte.hasNext()) {
//            String name;
//            name = yearMonthsIte.next();
//            File dirIn2;
//            dirIn2 = new File(
//                    dirIn,
//                    name);
//            // Totals for each tenancy type
//            Iterator<ArrayList<String>> allTenancyTypeChangesIte;
//            allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
//            while (allTenancyTypeChangesIte.hasNext()) {
//                ArrayList<String> tenancyTypeChanges;
//                tenancyTypeChanges = allTenancyTypeChangesIte.next();
//                String namea;
//                namea = DW_LineMapsLCC.getName(tenancyTypeChanges);
//                String nameb;
//                nameb = getReverseName(namea);
//                doDensity(
//                        dirIn2,
//                        name,
//                        namea,
//                        nameb,
//                        scaleToFirst);
//            }
//        }
    }

    protected String getReverseName(String namea) {
        String result;
        String[] split;
        split = namea.split("And");
        if (split.length == 1) {
            split = namea.split("-");
            result = split[1] + "-" + split[0];
        } else {
            result = "";
            String[] split2;
            split2 = split[0].split("-");
            result += split2[1] + "-" + split2[0];
            for (int i = 1; i < split.length; i++) {
                split2 = split[0].split("-");
                result += "And" + split2[1] + "-" + split2[0];
            }
        }
        return result;
    }

    protected void doDensity(
            File dirIn,
            String name,
            String namea,
            String nameb,
            boolean scaleToFirst) {
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/All/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October/1-3/1-3E.shp/1-3E.shp
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_April_TO_2013_May_TO_2013_May/1-3/1-3S.shp/1-3S.shp
        File dirIna;
        dirIna = new File(
                dirIn,
                namea);

        File dirInb;
        dirInb = new File(
                dirIn,
                nameb);

        dirIna = new File(
                dirIna,
                "Density");
        dirInb = new File(
                dirInb,
                "Density");
        File dirOut;
        dirOut = new File(
                dirIn,
                namea + "-" + nameb);

        if (scaleToFirst) {
            dirIna = new File(
                    dirIna,
                    "CommonStyle");
            dirInb = new File(
                    dirInb,
                    "CommonStyle");
        } else {
            dirIna = new File(
                    dirIna,
                    "IndividualStyle");
            dirInb = new File(
                    dirInb,
                    "IndividualStyle");
        }
        String name1;
        String name2;
        name1 = namea + "E";
        name2 = nameb + "S";
        doDensity(
                dirOut,
                dirIna,
                dirInb,
                name,
                name1,
                name2,
                scaleToFirst);
        name1 = namea + "S";
        name2 = nameb + "E";
        doDensity(
                dirOut,
                dirInb,
                dirIna,
                name,
                name1,
                name2,
                scaleToFirst);

    }

    protected void doDensity(
            File dirOut,
            File dirIna,
            File dirInb,
            String name,
            String name1,
            String name2,
            boolean scaleToFirst) {
        dirIna = new File(
                dirIna,
                name1);
        dirInb = new File(
                dirInb,
                name2);

        String outputName;
        outputName = name1 + "-" + name2;
        File dirOut2;
        dirOut2 = new File(
                dirOut,
                outputName);
        dirOut2.mkdirs();

        // Set grid dimensions    
//            int multiplier;
//            multiplier = (int) (400 / cellsize);
//        DW_Maps.setBackgroundDW_Shapefile(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        DW_Maps.setForegroundDW_Shapefile1(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
        nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
        ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
        xllcorner = 413000;
        //minX 413220.095
        //minXRounded = 413200
        //maxX = 446875.313
        //maxXRounded = 446900
        //maxXRounded - minXRounded = 33700
        //((maxXRounded - minXRounded) / cellsize) + 6 = 680 // 6 added for display purposes
        // minY = 422595.31
        // minYRounded = 422500
        // maxY = 450175.312
        // maxYrounded = 450200
        // maxYRounded - minYRounded = 27700
        // (maxYRounded - minYRounded) / cellsize = 554
        yllcorner = 422500;
        dimensions = new BigDecimal[5];
        dimensions[0] = BigDecimal.valueOf(cellsize);
        dimensions[1] = BigDecimal.valueOf(xllcorner);
        dimensions[2] = BigDecimal.valueOf(yllcorner);
        dimensions[3] = BigDecimal.valueOf(xllcorner + (cellsize * ncols));
        dimensions[4] = BigDecimal.valueOf(yllcorner + (cellsize * nrows));

        String outputNameE;
        outputNameE = name1 + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        String outputNameS;
        outputNameS = name2 + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        // Generate grids
        File grida = new File(
                dirIna,
                outputNameE + ".asc");
        if (!grida.exists()) {
            System.out.println("grida File " + grida + " does not exist!");
            return;
        }
        File gridb = new File(
                dirInb,
                outputNameS + ".asc");
        if (!gridb.exists()) {
            System.out.println("gridb File " + gridb + " does not exist! So the "
                    + "difference is all shown in grida File " + grida);
            return;
        }
        gf.set_GridStatistics(new Grids_GridStatistics0());
        Grids_Grid2DSquareCellDouble ga = (Grids_Grid2DSquareCellDouble) gf.create(grida);
        System.out.println("ga " + ga.toString(handleOutOfMemoryErrors));
        Grids_Grid2DSquareCellDouble gb = (Grids_Grid2DSquareCellDouble) gf.create(gridb);
        System.out.println("gb " + gb.toString(handleOutOfMemoryErrors));
        gp.set_Directory(dirOut2, DW_Maps.doDebug, handleOutOfMemoryErrors);
        gp.addToGrid(ga, gb, -1.0d, handleOutOfMemoryErrors);
        System.out.println("ga-gb " + ga.toString(handleOutOfMemoryErrors));
        // output grid
//            ImageExporter ie;
//            ie = new ImageExporter(ge);
//            File fout = new File(
//                    dirOut2,
//                    name + ".PNG");
//            ie.toGreyScaleImage(g, gp, fout, "PNG", handleOutOfMemoryErrors);
        File asciigridFile = new File(
                dirOut2,
                outputName + ".asc");
        eage.toAsciiFile(ga, asciigridFile, handleOutOfMemoryErrors);
        // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
        int index = 0;
        outputGridToImageUsingGeoToolsAndSetCommonStyle(
                100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                ga,
                asciigridFile,
                dirOut2,
                outputName,
                index,
                scaleToFirst);
        if (!scaleToFirst) {
            DW_Maps.getStyleParameters().setStyle(name, null, index);
        }

        // Generalise the grid
        // Generate some geographically weighted statsitics
        List<String> stats = new ArrayList<String>();
        stats.add("WSum");
        //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
        for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
            index++;
            double distance = cellDistanceForGeneralisation * cellsize;
            double weightIntersect = 1.0d;
            double weightFactor = 2.0d;
//                    // GeometricDensity
//                    Grids_Grid2DSquareCellDouble[] gws;
//                    gws = gp.geometricDensity(g, distance, gf);
//                    for (int gwsi = 0; gwsi < gws.length; gwsi++) {
//                        imageFile = new File(
//                                outletmapDirectory,
//                                nameOfGrid + "GWS" + gwsi + ".png");
//                        ie.toGreyScaleImage(gws[gwsi], gp, imageFile, "png", false);
//                        asciigridFile = new File(
//                                outletmapDirectory,
//                                nameOfGrid + "GWS" + gwsi + ".asc");
//                        eage.toAsciiFile(gws[gwsi], asciigridFile, false);
//                        System.out.println(gws[gwsi]);
//                    }
            // RegionUnivariateStatistics
            List<Grids_AbstractGrid2DSquareCell> gws;
            gws = gp.regionUnivariateStatistics(
                    ga,
                    stats,
                    distance,
                    weightIntersect,
                    weightFactor,
                    gf);

            Iterator<Grids_AbstractGrid2DSquareCell> itegws;
            itegws = gws.iterator();
            // Skip over the normaliser part of the result
            itegws.next();
            Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();

            System.out.println(gwsgrid);

            String outputName2;
            outputName2 = outputName + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

            asciigridFile = new File(
                    dirOut2,
                    outputName2 + ".asc");
            eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

            System.out.println(asciigridFile);
            //System.exit(0);
            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
            outputGridToImageUsingGeoToolsAndSetCommonStyle(
                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                    gwsgrid,
                    asciigridFile,
                    dirOut2,
                    outputName2,
                    index,
                    scaleToFirst);
            if (!scaleToFirst) {
                DW_Maps.getStyleParameters().setStyle(name, null, index);
            }
        }
    }
}
