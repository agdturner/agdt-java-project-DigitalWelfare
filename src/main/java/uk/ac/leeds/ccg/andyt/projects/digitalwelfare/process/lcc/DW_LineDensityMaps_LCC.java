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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorGWS;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_DensityMapsAbstract;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getYM3s;
import uk.ac.leeds.ccg.andyt.vector.core.Vector_Environment;
import uk.ac.leeds.ccg.andyt.vector.geometry.Vector_LineSegment2D;
import uk.ac.leeds.ccg.andyt.vector.geometry.Vector_Point2D;
import uk.ac.leeds.ccg.andyt.vector.grids.Vector_LineGrid;

/**
 *
 * @author geoagdt
 */
public class DW_LineDensityMaps_LCC extends DW_DensityMapsAbstract {

    protected Vector_Environment ve;
    
protected DW_LineMapsLCC DW_LineMaps_LCC;

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<AGDT_Shapefile> midgrounds;
    protected ArrayList<AGDT_Shapefile> foregrounds;

    public DW_LineDensityMaps_LCC(DW_Environment env) {
        super(env);
        DW_LineMaps_LCC = new DW_LineMapsLCC(env);
    }
    
    public void run() throws Exception, Error {
//        ve = new Vector_Environment();
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
//        styleParameters.setnClasses(9);
//        styleParameters.setPaletteName("Reds");
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
//                DW_Files.sDensity);
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
//        ie = new Grids_ImageExporter(ge);
//        gp = new Grid2DSquareCellProcessorGWS(ge);
//        gp.set_Directory(processorDir, false, handleOutOfMemoryErrors);
//        gcf = new Grids_Grid2DSquareCellDoubleChunkArrayFactory();
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
//        maxCellDistanceForGeneralisation = 4; // 8;
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
//        //initStyleParameters();
//        mapDirectory = DW_Files.getOutputSHBELineMapsDir();
//        foregrounds = new ArrayList<AGDT_Shapefile>();
//        //midgrounds = new ArrayList<AGDT_Shapefile>();
////        backgrounds = new ArrayList<AGDT_Shapefile>();
//        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
//        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
//                env,
//                "LSOA",
//                targetPropertyNameLSOA,
//                getShapefileDataStoreFactory());
//        foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
////        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
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
        
////        // Single run
////         File dirIn1 = new File("/scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/1-3");
////        File dirOut1 = new File(
////                        dirIn1,
////                        "Density");
////        File f1 = AGDT_Geotools.getInputShapefile(
////                        dirIn1,
////                        "1-3E");
////        doDensity(
////                        //resolutionMultiplier,
////                        f1,
////                        dirOut1,
////                        "1-3E",
////                        scaleToFirst);
////        File dirIn2 = new File("/scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/3-1");
////        File dirOut2 = new File(
////                        dirIn2,
////                        "Density");
////        if (scaleToFirst) {
////                    dirOut2 = new File(
////                            dirOut2,
////                            "CommonStyle");
////                } else {
////                    dirOut2 = new File(
////                            dirOut2,
////                            "IndividualStyle");
////                }
////        File f2 = AGDT_Geotools.getInputShapefile(
////                        dirIn2,
////                        "3-1S");
////        doDensity(
////                        //resolutionMultiplier,
////                        f2,
////                        dirOut2,
////                        "3-1S",
////                        scaleToFirst);
////        System.exit(0);
//        
//        //        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
//        //        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
//        // Other variables for selecting and output
//        File dirIn = new File(
//                mapDirectory,
//                tDW_Strings.sTenancy);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sA);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sTenancyTypeTransition);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sCheckedPreviousTenancyType);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sTenancyAndPostcodeChanges);
//        if (doUnderOccupancyData) {
//            dirIn = new File(
//                    dirIn,
//                    tDW_Strings.sU);
//            if (doCouncil) {
//                dirIn = new File(
//                        dirIn,
//                        tDW_Strings.sCouncil);
//            } else {
//                dirIn = new File(
//                        dirIn,
//                        tDW_Strings.sRSL);
//            }
//        } else {
//            dirIn = new File(
//                    dirIn,
//                    tDW_Strings.sA);
//        }
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sPostcodeChanged);
//        dirIn = new File(
//                dirIn,
//                includeName);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sGroupedNo);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sPostcodeChanges);
//        dirIn = new File(
//                dirIn,
//                tDW_Strings.sCheckedPreviousPostcode);
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
//                name = DW_LineMaps_LCC.getName(tenancyTypeChanges);
//                File dirIn3;
//                dirIn3 = new File(
//                        dirIn2,
//                        name);
//                File dirOut = new File(
//                        dirIn3,
//                        DW_Files.sDensity);
//                if (scaleToFirst) {
//                    dirOut = new File(
//                            dirOut,
//                            tDW_Strings.sStyleCommon);
//                } else {
//                    dirOut = new File(
//                            dirOut,
//                            tDW_Strings.sStyleIndividual);
//                }
//                String nameS;
//                nameS = name + "S";
//                File f1 = AGDT_Geotools.getInputShapefile(
//                        dirIn3,
//                        nameS);
//                doDensity(
//                        //resolutionMultiplier,
//                        f1,
//                        dirOut,
//                        nameS,
//                        scaleToFirst);
//                String nameE;
//                nameE = name + "E";
//                File f2 = AGDT_Geotools.getInputShapefile(
//                        dirIn3,
//                        nameE);
//                doDensity(
//                        //resolutionMultiplier,
//                        f2,
//                        dirOut,
//                        nameE,
//                        scaleToFirst);
//            }
//        }
    }

    protected void doDensity(
            //int resolutionMultiplier,
            File f,
            File dirOut,
            String name,
            boolean scaleToFirst) {
//        BigDecimal tollerance;
//        tollerance = new BigDecimal("0.0001");
//        int decimalPlacePrecision = 5;
//
//        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/All/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October
//        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October/1-3/1-3E.shp/1-3E.shp
//        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_April_TO_2013_May_TO_2013_May/1-3/1-3S.shp/1-3S.shp
//        if (f.exists()) {
//
//            System.out.println("Processing input shapefile " + f + "...");
//
//            File dirOut2;
//            dirOut2 = new File(
//                    dirOut,
//                    name);
//            // Set grid dimensions    
////            int multiplier;
////            multiplier = (int) (400 / cellsize);
//            backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
//            foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
//            
//            nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
//            ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
//            xllcorner = 413000;
//            //minX 413220.095
//            //minXRounded = 413200
//            //maxX = 446875.313
//            //maxXRounded = 446900
//            //maxXRounded - minXRounded = 33700
//            //((maxXRounded - minXRounded) / cellsize) + 6 = 680 // 6 added for display purposes
//            // minY = 422595.31
//            // minYRounded = 422500
//            // maxY = 450175.312
//            // maxYrounded = 450200
//            // maxYRounded - minYRounded = 27700
//            // (maxYRounded - minYRounded) / cellsize = 554
//            yllcorner = 422500;
//            dimensions = new BigDecimal[5];
//            dimensions[0] = BigDecimal.valueOf(cellsize);
//            dimensions[1] = BigDecimal.valueOf(xllcorner);
//            dimensions[2] = BigDecimal.valueOf(yllcorner);
//            dimensions[3] = BigDecimal.valueOf(xllcorner + (cellsize * ncols));
//            dimensions[4] = BigDecimal.valueOf(yllcorner + (cellsize * nrows));
//
//            String outputName;
//            outputName = name + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
//            // Generate initial grid
//            File grid = new File(
//                    dirOut2,
//                    "Grid_" + outputName);
//            grid.mkdirs();
//            Grids_Grid2DSquareCellDouble g = initiliseGrid(grid);
//
//            // Open Shapefile reader
//            AGDT_Shapefile shapefile;
//            shapefile = new AGDT_Shapefile(f);
//
//            FeatureCollection fc;
//            fc = shapefile.getFeatureCollection();
//
//            FeatureIterator fi;
//            fi = fc.features();
//            while (fi.hasNext()) {
//                SimpleFeature sf;
//                sf = (SimpleFeature) fi.next();
//                sf.getDefaultGeometry();
//                Geometry geom = (Geometry) sf.getDefaultGeometry();
//                if (geom.isValid()) {
//                    Coordinate[] coordinates = geom.getCoordinates();
//                    Vector_Point2D p0;
//                    p0 = new Vector_Point2D(
//                            ve,
//                            coordinates[0].x,
//                            coordinates[0].y);
//                    for (int i = 1; i < coordinates.length; i++) {
//                        Vector_Point2D p1;
//                        p1 = new Vector_Point2D(
//                                ve,
//                                coordinates[i].x,
//                                coordinates[i].y);
//                        Vector_LineSegment2D l;
//                        l = new Vector_LineSegment2D(p0, p1);
//                        double factor;
//                        factor = 1.0d / l.getLength(decimalPlacePrecision).doubleValue();
//                        // add each line to grid
//                        Vector_LineGrid.addToGrid(
//                                g,
//                                l,
//                                factor,
//                                tollerance,
//                                handleOutOfMemoryErrors);
//                        p0 = p1;
//                    }
//                }
//            }
//            fi.close(); // Can cause problems due to multiple locks!
//            shapefile.dispose();
//            int index;
//            // output grid
//            gp.set_Directory(dirOut2, doDebug, handleOutOfMemoryErrors);
////            Grids_ImageExporter ie;
////            ie = new Grids_ImageExporter(ge);
////            File fout = new File(
////                    dirOut2,
////                    name + ".PNG");
////            ie.toGreyScaleImage(g, gp, fout, "PNG", handleOutOfMemoryErrors);
//            File asciigridFile = new File(
//                    dirOut2,
//                    outputName + ".asc");
//            eage.toAsciiFile(g, asciigridFile, handleOutOfMemoryErrors);
//            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
//            index = 0;
//            outputGridToImageUsingGeoToolsAndSetCommonStyle(
//                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
//                    g,
//                    asciigridFile,
//                    dirOut2,
//                    outputName,
//                    index,
//                    scaleToFirst);
//            if (!scaleToFirst) {
//                styleParameters.setStyle(name, null, index);
//            }
//
//            // Generalise the grid
//            // Generate some geographically weighted statsitics
//            List<String> stats = new ArrayList<String>();
//            stats.add("WSum");
//            for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
//                    cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
//                index++;
//                double distance = cellDistanceForGeneralisation * cellsize;
//                double weightIntersect = 1.0d;
//                double weightFactor = 2.0d;
////                    // GeometricDensity
////                    Grids_Grid2DSquareCellDouble[] gws;
////                    gws = gp.geometricDensity(g, distance, gf);
////                    for (int gwsi = 0; gwsi < gws.length; gwsi++) {
////                        imageFile = new File(
////                                outletmapDirectory,
////                                nameOfGrid + "GWS" + gwsi + ".png");
////                        ie.toGreyScaleImage(gws[gwsi], gp, imageFile, "png", false);
////                        asciigridFile = new File(
////                                outletmapDirectory,
////                                nameOfGrid + "GWS" + gwsi + ".asc");
////                        eage.toAsciiFile(gws[gwsi], asciigridFile, false);
////                        System.out.println(gws[gwsi]);
////                    }
//                // RegionUnivariateStatistics
//                List<AbstractGrid2DSquareCell> gws;
//                gws = gp.regionUnivariateStatistics(
//                        g,
//                        stats,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        gf);
//                Iterator<AbstractGrid2DSquareCell> itegws;
//                itegws = gws.iterator();
//                // Set normaliser part of the result to null to save space
//                AbstractGrid2DSquareCell normaliser = itegws.next();
//                normaliser = null;
//                // Write out grid
//                AbstractGrid2DSquareCell gwsgrid = itegws.next();
//                String outputName2;
//                outputName2 = outputName + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
////                        imageFile = new File(
////                                outletmapDirectory,
////                                outputName + ".png");
////                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);
//
//                asciigridFile = new File(
//                        dirOut2,
//                        outputName2 + ".asc");
//                eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);
//
//                // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
//                outputGridToImageUsingGeoToolsAndSetCommonStyle(
//                        100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
//                        gwsgrid,
//                        asciigridFile,
//                        dirOut2,
//                        outputName2,
//                        index,
//                        scaleToFirst);
//                if (!scaleToFirst) {
//                    styleParameters.setStyle(name, null, index);
//                }
//            }
//        } else {
//            System.out.println("File " + f + " does not exist to process.");
//        }
    }
}
