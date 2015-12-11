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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorGWS;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UnderOccupiedReport_Set;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps.initONSPDLookups;
import uk.ac.leeds.ccg.andyt.vector.core.Vector_Environment;

/**
 *
 * @author geoagdt
 */
public class DW_DensityMaps_LCC extends DW_DensityMapsAbstract {

    protected Vector_Environment ve;

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<AGDT_Shapefile> midgrounds;
    protected ArrayList<AGDT_Shapefile> foregrounds;

    //DW_StyleParameters styleParameters;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_DensityMaps_LCC().run();
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

    public void run() {
        ve = new Vector_Environment();
        // If showMapsInJMapPane is true, the maps are presented in individual 
        // JMapPanes
        showMapsInJMapPane = false;
        //showMapsInJMapPane = true;
        //outputESRIAsciigrids = false;
        imageWidth = 1000;
        initONSPDLookups();
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
        styleParameters = new AGDT_StyleParameters();
        styleParameters.setnClasses(9);
        styleParameters.setPaletteName("Reds");
        styleParameters.setAddWhiteForZero(true);
//        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
//        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");

        mapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "density");
        imageWidth = 1000;

//        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();
        init();

        /*Grid Parameters
         *_____________________________________________________________________
         */
        handleOutOfMemoryErrors = true;
        File processorDir = new File(
                mapDirectory,
                "processor");
        processorDir.mkdirs();
        ge = new Grids_Environment();
        eage = new ESRIAsciiGridExporter();
        ie = new ImageExporter(ge);
        gp = new Grid2DSquareCellProcessorGWS(ge);
        gp.set_Directory(processorDir, false, handleOutOfMemoryErrors);
        gcf = new Grid2DSquareCellDoubleChunkArrayFactory();
        chunkNRows = 300;//250; //64
        chunkNCols = 350;//300; //64
        gf = new Grid2DSquareCellDoubleFactory(
                processorDir,
                chunkNRows,
                chunkNCols,
                gcf,
                -9999d,
                ge,
                handleOutOfMemoryErrors);
//        // Jenks runs
//        styleParameters.setClassificationFunctionName("Jenks");
//        commonStyling = true;
//        individualStyling = true;
//        runAll(IDType);
//        // Quantile runs
//        styleParameters.setClassificationFunctionName("Quantile");
//        styleParameters.setStylesNull();
//        commonStyling = true;
//        individualStyling = true;
//        runAll(tDW_ID_ClientTypes);
        // Equal Interval runs
        styleParameters.setClassificationFunctionName("EqualInterval");
        styleParameters.setStylesNull();
//        commonStyling = true;
        individualStyling = true;

        //int resolutionMultiplier = 4;
        cellsize = 50;
        maxCellDistanceForGeneralisation = 4; // 8;
        runAll();
//        for (maxCellDistanceForGeneralisation = 4; maxCellDistanceForGeneralisation <= 32; maxCellDistanceForGeneralisation *= 2) {
//            cellsize = (1600 / maxCellDistanceForGeneralisation) / resolutionMultiplier;
//            runAll(resolutionMultiplier);
//        }
    }

    private void init() {
        //initStyleParameters();
        mapDirectory = DW_Files.getOutputSHBELineMapsDir();
        foregrounds = new ArrayList<AGDT_Shapefile>();
        //midgrounds = new ArrayList<AGDT_Shapefile>();
//        backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
        foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

    //public void runAll(int resolutionMultiplier) {
    public void runAll() {
        boolean scaleToFirst = false;
        File dirOut;
        dirOut = new File(
                DW_Files.getOutputSHBEMapsDir(),
                "Density");

        String[] SHBEFilenames;
        SHBEFilenames = DW_SHBE_Handler.getSHBEFilenamesAll();

        DW_SHBE_Handler tDW_SHBE_Handler = new DW_SHBE_Handler();

        Object[] underOccupiedData;
        underOccupiedData = DW_UnderOccupiedReport_Handler.loadUnderOccupiedReportData();

        Object[] ttgs = DW_SHBE_TenancyType_Handler.getTenancyTypeGroups();
        HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups;
        tenancyTypeGroups = (HashMap<Boolean, TreeMap<String, ArrayList<String>>>) ttgs[0];
        HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped;
        tenancyTypesGrouped = (HashMap<Boolean, ArrayList<String>>) ttgs[1];
        HashMap<Boolean, ArrayList<String>> regulatedGroups;
        regulatedGroups = (HashMap<Boolean, ArrayList<String>>) ttgs[2];
        HashMap<Boolean, ArrayList<String>> unregulatedGroups;
        unregulatedGroups = (HashMap<Boolean, ArrayList<String>>) ttgs[3];

        // Includes
        TreeMap<String, ArrayList<Integer>> includes;
        includes = DW_SHBE_Handler.getIncludes();

        // Specifiy distances
        ArrayList<Double> distances;
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
//        for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        // Set grid dimensions    
//        int multiplier;
//        multiplier = (int) (400 / cellsize);
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
        ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
        xllcorner = 413000;
        yllcorner = 422500;
        dimensions = new BigDecimal[5];
        dimensions[0] = BigDecimal.valueOf(cellsize);
        dimensions[1] = BigDecimal.valueOf(xllcorner);
        dimensions[2] = BigDecimal.valueOf(yllcorner);
        dimensions[3] = BigDecimal.valueOf(xllcorner + (cellsize * ncols));
        dimensions[4] = BigDecimal.valueOf(yllcorner + (cellsize * nrows));

        ArrayList<Boolean> b;
        b = new ArrayList<Boolean>();
        b.add(true);
        b.add(false);

        ArrayList<String> inPaymentTypes;
        inPaymentTypes = DW_SHBE_Handler.getPaymentTypes();
        Iterator<String> inPaymentTypesIte;
        inPaymentTypesIte = inPaymentTypes.iterator();
        while (inPaymentTypesIte.hasNext()) {
            String inPaymentType;
            inPaymentType = inPaymentTypesIte.next();
            File dirOut2 = new File(
                    dirOut,
                    inPaymentType);
            Iterator<Boolean> iteb;
            iteb = b.iterator();
//        boolean doUnderOcupied;
//        doUnderOcupied = false;
            while (iteb.hasNext()) {
                boolean doUnderOccupied;
                doUnderOccupied = iteb.next();
                if (doUnderOccupied) {
                    Iterator<Boolean> iteb2;
                    iteb2 = b.iterator();
                    while (iteb2.hasNext()) {
                        boolean doCouncil;
                        doCouncil = iteb2.next();
                        //boolean doCouncil = false;
                        run(
                                inPaymentType,
                                tenancyTypeGroups,
                                tenancyTypesGrouped,
                                regulatedGroups,
                                unregulatedGroups,
                                underOccupiedData,
                                doUnderOccupied,
                                false,
                                scaleToFirst,
                                DW_Files.getUOFile(dirOut2, doUnderOccupied, doCouncil),
                                SHBEFilenames,
                                tDW_SHBE_Handler,
                                includes);
//                    run(
//                            includes,
//                            tenancyTypeGroups,
//                            tenancyTypesGrouped,
//                            regulatedGroups,
//                            unregulatedGroups,
//                            underOccupiedData,
//                            doUnderOccupied,
//                            doCouncil,
//                            scaleToFirst,
//                            DW_Files.getUOFile(dirOut2, doUnderOccupied, doCouncil),
//                            SHBEFilenames,
//                            tDW_SHBE_Handler);
                    }
                } else {
                    run(
                            inPaymentType,
                            tenancyTypeGroups,
                            tenancyTypesGrouped,
                            regulatedGroups,
                            unregulatedGroups,
                            underOccupiedData,
                            doUnderOccupied,
                            false,
                            scaleToFirst,
                            DW_Files.getUOFile(dirOut2, doUnderOccupied, false),
                            SHBEFilenames,
                            tDW_SHBE_Handler,
                            includes);
//                        startIndex,
//                        endIndex);
//                run(
//                        includes,
//                        tenancyTypeGroups,
//                        tenancyTypesGrouped,
//                        regulatedGroups,
//                        unregulatedGroups,
//                        underOccupiedData,
//                        doUnderOccupied,
//                        false,
//                        scaleToFirst,
//                        DW_Files.getUOFile(dirOut2, doUnderOccupied, false),
//                        SHBEFilenames,
//                        tDW_SHBE_Handler);
                }
            }
        }
    }

    public void run(
            String inPaymentType,
            TreeMap<String, ArrayList<Integer>> includes,
            HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups,
            HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped,
            HashMap<Boolean, ArrayList<String>> regulatedGroups,
            HashMap<Boolean, ArrayList<String>> unregulatedGroups,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean scaleToFirst,
            File dirOut,
            String[] SHBEFilenames,
            DW_SHBE_Handler tDW_SHBE_Handler) {

        Iterator<String> ite;
        ite = includes.keySet().iterator();
        while (ite.hasNext()) {
            String includeName;
            includeName = ite.next();
            ArrayList<Integer> include;
            include = includes.get(includeName);
            for (int i = 0; i < SHBEFilenames.length; i++) {
                if (include.contains(i)) {
                    Object[] SHBEData0;
                    String yM0 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                    // Init underOccupiedSets
                    TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSets0 = null;
                    DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
                    if (doUnderOccupied) {
                        if (doCouncil) {
                            underOccupiedSets0 = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
                        } else {
                            underOccupiedSets0 = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
                        }
                        underOccupiedSet0 = underOccupiedSets0.get(yM0);
                        if (underOccupiedSet0 != null) {
                            SHBEData0 = tDW_SHBE_Handler.loadInputData(
                                    DW_Files.getInputSHBEDir(),
                                    SHBEFilenames[i],
                                    inPaymentType,
                                    false);
                            Grid2DSquareCellDouble g0 = doDensity(
                                    dirOut,
                                    yM0,
                                    SHBEData0,
                                    underOccupiedData,
                                    doUnderOccupied,
                                    underOccupiedSet0,
                                    doCouncil,
                                    scaleToFirst);
                        }
                    } else {
                        SHBEData0 = tDW_SHBE_Handler.loadInputData(
                                DW_Files.getInputSHBEDir(),
                                SHBEFilenames[i],
                                inPaymentType,
                                false);
                        Grid2DSquareCellDouble g0 = doDensity(
                                dirOut,
                                yM0,
                                SHBEData0,
                                underOccupiedData,
                                doUnderOccupied,
                                underOccupiedSet0,
                                doCouncil,
                                scaleToFirst);
                    }
                }
            }
        }
    }

    public void run(
            String inPaymentType,
            HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups,
            HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped,
            HashMap<Boolean, ArrayList<String>> regulatedGroups,
            HashMap<Boolean, ArrayList<String>> unregulatedGroups,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean scaleToFirst,
            File dirOut,
            String[] SHBEFilenames,
            DW_SHBE_Handler tDW_SHBE_Handler,
            TreeMap<String, ArrayList<Integer>> includes) {
//        // Declare iterators
//        Iterator<String> claimantTypesIte;
//        Iterator<String> tenancyTypeIte;
//        Iterator<String> levelsIte;
//        Iterator<String> typesIte;
//        Iterator<String> distanceTypesIte;
//        Iterator<Double> distancesIte;
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        // Set grid dimensions    
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
        dimensions[3] = BigDecimal.valueOf(xllcorner
                + (cellsize * ncols));
        dimensions[4] = BigDecimal.valueOf(yllcorner
                + (cellsize * nrows));

        DW_UnderOccupiedReport_Set underOccupiedSet0 = null;
        TreeMap<String, DW_UnderOccupiedReport_Set> underOccupiedSets = null;
        if (doUnderOccupied) {
            if (doCouncil) {
                underOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[0];
            } else {
                underOccupiedSets = (TreeMap<String, DW_UnderOccupiedReport_Set>) underOccupiedData[1];
            }
        }
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeName;
            includeName = includesIte.next();
            ArrayList<Integer> include;
            include = includes.get(includeName);
            Iterator<Integer> ite;
            ite = include.iterator();
            int i;
            i = ite.next();
            String yM30;
            yM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
            if (doUnderOccupied) {
                boolean initialised = false;
                while (!initialised) {
                    underOccupiedSet0 = underOccupiedSets.get(yM30);
                    if (underOccupiedSet0 != null) {
                        initialised = true;
                    } else {
                        i = ite.next();
                        yM30 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                    }
                }
            }
            Object[] SHBEData0;
            SHBEData0 = tDW_SHBE_Handler.loadInputData(
                    DW_Files.getInputSHBEDir(),
                    SHBEFilenames[i],
                    inPaymentType,
                    false);
            Object[] SHBEData00;
            SHBEData00 = SHBEData0;
            String yM300;
            yM300 = yM30;

            Grid2DSquareCellDouble g0 = doDensity(
                    dirOut,
                    yM30,
                    SHBEData0,
                    underOccupiedData,
                    doUnderOccupied,
                    underOccupiedSet0,
                    doCouncil,
                    scaleToFirst);
            Grid2DSquareCellDouble g00 = g0;
            while (ite.hasNext()) {
                i = ite.next();
                Object[] SHBEData1;
                SHBEData1 = tDW_SHBE_Handler.loadInputData(
                        DW_Files.getInputSHBEDir(),
                        SHBEFilenames[i],
                        inPaymentType,
                        false);
                String yM31;
                yM31 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                // Init underOccupiedSets
                DW_UnderOccupiedReport_Set underOccupiedSet1 = null;
                if (doUnderOccupied) {
                    underOccupiedSet1 = underOccupiedSets.get(yM31);
                }
                Grid2DSquareCellDouble g1 = doDensity(
                        dirOut,
                        yM31,
                        SHBEData1,
                        underOccupiedData,
                        doUnderOccupied,
                        underOccupiedSet1,
                        doCouncil,
                        scaleToFirst);
                gp.addToGrid(g1, g0,
                        -1.0d, handleOutOfMemoryErrors);
                outputGrid(
                        g1,
                        dirOut,
                        yM30 + "Minus" + yM31,
                        "Name" + yM30 + "Minus" + yM31,
                        scaleToFirst);
                underOccupiedSet0 = underOccupiedSet1;
                yM30 = yM31;
                SHBEData0 = SHBEData1;
            }
            Grid2DSquareCellDouble g1 = doDensity(
                    dirOut,
                    yM30,
                    SHBEData0,
                    underOccupiedData,
                    doUnderOccupied,
                    underOccupiedSet0,
                    doCouncil,
                    scaleToFirst);
            gp.addToGrid(g1, g00,
                    -1.0d, handleOutOfMemoryErrors);
            outputGrid(
                    g1,
                    dirOut,
                    yM300 + "Minus" + yM30,
                    "Name" + yM300 + "Minus" + yM30,
                    scaleToFirst);
        }
    }

    protected Grid2DSquareCellDouble doDensity(
            File dirOut,
            String yM3,
            Object[] SHBEData,
            Object[] underOccupiedData,
            boolean doUnderOccupied,
            DW_UnderOccupiedReport_Set underOccupiedSet,
            boolean doCouncil,
            boolean scaleToFirst) {

        File dirOut2 = new File(
                dirOut,
                yM3);
        styleParameters.setnClasses(9);
        styleParameters.setPaletteName2(null);

        String name = "Density" + yM3;

        String outputName;
        outputName = name + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        // Generate initial grid
        File grid = new File(
                dirOut2,
                "Grid_" + outputName);
        grid.mkdirs();
        Grid2DSquareCellDouble g0 = initiliseGrid(grid);

        TreeMap<String, TreeMap<String, TreeMap<String, AGDT_Point>>> lookups;
        lookups = getONSPDlookups();
        TreeMap<String, AGDT_Point> lookup;
        lookup = lookups.get("Unit").get(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM3));

        TreeMap<String, DW_SHBE_Record> records;
        records = (TreeMap<String, DW_SHBE_Record>) SHBEData[0];

        boolean nonZero = false;

        // Iterator over records
        Iterator<String> recordsIte;
        recordsIte = records.keySet().iterator();
        while (recordsIte.hasNext()) {
            String claimID = recordsIte.next();
            DW_SHBE_D_Record DRecord = records.get(claimID).getDRecord();
            String postcode = DRecord.getClaimantsPostcode();
            String councilTaxBenefitClaimReferenceNumber;
            councilTaxBenefitClaimReferenceNumber = DRecord.getCouncilTaxBenefitClaimReferenceNumber();
//            Integer tenancyType1Integer = DRecord.getTenancyType();
//            String tenancyType1 = tenancyType1Integer.toString();
//            tenancyTypeIte = tenancyTypeGroups.keySet().iterator();
//            while (tenancyTypeIte.hasNext()) {
//                String tenancyType;
//                tenancyType = tenancyTypeIte.next();
//                ArrayList<String> tenancyTypes;
//                tenancyTypes = tenancyTypeGroups.get(tenancyType);
//                if (tenancyTypes.contains(tenancyType1)) {

//        Iterator<String> ite;
//        ite = tIDByPostcode0.keySet().iterator();
//        while (ite.hasNext()) {
            boolean doMainLoop = true;
            // Check for UnderOccupied
            if (doUnderOccupied) {
                // UnderOccupancy
                DW_UnderOccupiedReport_Record underOccupied0 = null;
                if (underOccupiedSet != null) {
                    underOccupied0 = underOccupiedSet.getMap().get(
                            councilTaxBenefitClaimReferenceNumber);
                }
                doMainLoop = underOccupied0 != null;
            }
            if (doMainLoop) {
                if (postcode != null) {
                    AGDT_Point p;
                    p = lookup.get(DW_Postcode_Handler.formatPostcodeForMapping(postcode));
//            String formattedPostcode;
//            formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
//            AGDT_Point p1;
//            p1 = DW_Postcode_Handler.getPointFromPostcode(DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode));
                    if (p != null) {
                        g0.addToCell(
                                (double) p.getX(),
                                (double) p.getY(),
                                1.0d,
                                handleOutOfMemoryErrors);
                        nonZero = true;
                    } else {
                        System.out.println("No point for postcode " + postcode + ", "
                                + "DW_Postcode_Handler.formatPostcodeForMapping(postcode)" + DW_Postcode_Handler.formatPostcodeForMapping(postcode) + ", "
                                + "DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode) " + DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode));
                    }
                }
            }
        }

        if (nonZero) {

            // output grid
            gp.set_Directory(dirOut2, doDebug, handleOutOfMemoryErrors);
//            ImageExporter ie;
//            ie = new ImageExporter(ge);
//            File fout = new File(
//                    dirOut2,
//                    name + ".PNG");
//            ie.toGreyScaleImage(g, gp, fout, "PNG", handleOutOfMemoryErrors);
            File asciigridFile = new File(
                    dirOut2,
                    outputName + ".asc");
            eage.toAsciiFile(g0, asciigridFile, handleOutOfMemoryErrors);
            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
            int index = 0;
            outputGridToImageUsingGeoToolsAndSetCommonStyle(
                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                    g0,
                    asciigridFile,
                    dirOut2,
                    outputName,
                    index,
                    scaleToFirst);
            if (!scaleToFirst) {
                styleParameters.setStyle(name, null, index);
            }

            // Generalise the grid
            // Generate some geographically weighted statsitics
            List<String> stats = new ArrayList<String>();
            stats.add("WSum");
            //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
            for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                    cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
                index++;
                double distance = maxCellDistanceForGeneralisation * cellsize;
                double weightIntersect = 1.0d;
                double weightFactor = 2.0d;
//                    // GeometricDensity
//                    Grid2DSquareCellDouble[] gws;
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
                List<AbstractGrid2DSquareCell> gws;
                gws = gp.regionUnivariateStatistics(
                        g0,
                        stats,
                        distance,
                        weightIntersect,
                        weightFactor,
                        gf);
                Iterator<AbstractGrid2DSquareCell> itegws;
                itegws = gws.iterator();
                // Set normaliser part of the result to null to save space
                AbstractGrid2DSquareCell normaliser = itegws.next();
                normaliser = null;
                // Write out grid
                AbstractGrid2DSquareCell gwsgrid = itegws.next();
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
                    styleParameters.setStyle(name, null, index);
                }
            }
        } else {
            System.out.println("Grid " + g0.toString(handleOutOfMemoryErrors) + " is not added to and so is not output.");
        }
        return g0;
    }

    protected void outputGrid(
            Grid2DSquareCellDouble g1,
            File dirOut,
            String outputName,
            String name,
            boolean scaleToFirst) {

        //styleParameters = new AGDT_StyleParameters();
        styleParameters.setnClasses(5);
        //styleParameters.setnClasses2(5);
        //styleParameters.setPaletteName("Reds");
        styleParameters.setPaletteName2("Blues");
        styleParameters.setAddWhiteForZero(true);

        System.out.println("g1 " + g1.toString(handleOutOfMemoryErrors));
        // output grid
//            ImageExporter ie;
//            ie = new ImageExporter(ge);
//            File fout = new File(
//                    dirOut2,
//                    name + ".PNG");
//            ie.toGreyScaleImage(g, gp, fout, "PNG", handleOutOfMemoryErrors);
        File asciigridFile = new File(
                dirOut,
                outputName + ".asc");
        eage.toAsciiFile(g1, asciigridFile, handleOutOfMemoryErrors);
        // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
        int index = 0;
        outputGridToImageUsingGeoToolsAndSetCommonStyle(
                100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                g1,
                asciigridFile,
                dirOut,
                outputName,
                index,
                scaleToFirst);
        if (!scaleToFirst) {
            styleParameters.setStyle(name, null, index);
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
//                    Grid2DSquareCellDouble[] gws;
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
            List<AbstractGrid2DSquareCell> gws;
            gws = gp.regionUnivariateStatistics(
                    g1,
                    stats,
                    distance,
                    weightIntersect,
                    weightFactor,
                    gf);

            Iterator<AbstractGrid2DSquareCell> itegws;
            itegws = gws.iterator();
            // Skip over the normaliser part of the result
            itegws.next();
            AbstractGrid2DSquareCell gwsgrid = itegws.next();

            System.out.println(gwsgrid);

            String outputName2;
            outputName2 = outputName + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

            asciigridFile = new File(
                    dirOut,
                    outputName2 + ".asc");
            eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

            System.out.println(asciigridFile);
            //System.exit(0);
            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
            outputGridToImageUsingGeoToolsAndSetCommonStyle(
                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                    gwsgrid,
                    asciigridFile,
                    dirOut,
                    outputName2,
                    index,
                    scaleToFirst);
            if (!scaleToFirst) {
                styleParameters.setStyle(name, null, index);
            }
        }
    }
}
