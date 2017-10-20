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
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Dimensions;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.statistics.Grids_GridStatistics0;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorGWS;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Records;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_D_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_TenancyType_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied.DW_UO_Set;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_DensityMapsAbstract;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_MapsLCC;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Shapefile;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMaps_LCC.getAllTenancyTypeGroups;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps.initONSPDLookups;
import uk.ac.leeds.ccg.andyt.vector.core.Vector_Environment;

/**
 *
 * @author geoagdt
 */
public class DW_DensityMapsLCC extends DW_DensityMapsAbstract {

    DW_MapsLCC DW_MapsLCC;

    protected Vector_Environment ve;
    protected DW_SHBE_Data DW_SHBE_Data;

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<AGDT_Shapefile> midgrounds;
    protected ArrayList<AGDT_Shapefile> foregrounds;

    public DW_DensityMapsLCC(DW_Environment de) {
        super(de);
        this.DW_SHBE_Data = de.getDW_SHBE_Data();
    }

    public void run() throws Exception, Error {
        ve = new Vector_Environment();
        // If showMapsInJMapPane is true, the maps are presented in individual 
        // JMapPanes
//        DW_Maps.setShowMapsInJMapPane(false);
        //showMapsInJMapPane = true;
        //outputESRIAsciigrids = false;
        //initONSPDLookups();
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
        AGDT_StyleParameters AGDT_StyleParameters;
        AGDT_StyleParameters = new AGDT_StyleParameters();
        AGDT_StyleParameters.setnClasses(9);
        AGDT_StyleParameters.setPaletteName("Reds");
        AGDT_StyleParameters.setAddWhiteForZero(true);
//        AGDT_StyleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        AGDT_StyleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
//        AGDT_StyleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
        AGDT_StyleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                //Color.GREEN,
                Color.BLACK,
                Color.WHITE));
        AGDT_StyleParameters.setForegroundStyleTitle1("Foreground Style 1");
        DW_MapsLCC.setStyleParameters(AGDT_StyleParameters);

        File generatedGridsDirectory;
        generatedGridsDirectory = new File(
                df.getGeneratedGridsDir(),
                "Density");
//        DW_MapsLCC.setMapDirectory(mapDirectory);

        int imageWidth;
        imageWidth = 2000;
//        DW_MapsLCC.setImageWidth(imageWidth);

//        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();
        init();

        /**
         * Grid Parameters
         */
        handleOutOfMemoryErrors = true;
        ge = new Grids_Environment(generatedGridsDirectory);
        eage = new Grids_ESRIAsciiGridExporter(ge);
        ie = new Grids_ImageExporter(ge);
        gp = new Grids_ProcessorGWS(ge);
        gp.setDirectory(generatedGridsDirectory, false, handleOutOfMemoryErrors);
        gcf = new Grids_Grid2DSquareCellDoubleChunkArrayFactory();
        chunkNRows = 300;//250; //64
        chunkNCols = 350;//300; //64
        gf = new Grids_Grid2DSquareCellDoubleFactory(
                generatedGridsDirectory,
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
        AGDT_StyleParameters.setClassificationFunctionName("EqualInterval");
        AGDT_StyleParameters.setStylesNull();
//        commonStyling = true;
//        DW_MapsLCC.setIndividualStyling(true);

        //int resolutionMultiplier = 4;
        cellsize = 50;
        maxCellDistanceForGeneralisation = 4; // 8;
        runAll();
        //runRateMaps();

//        for (maxCellDistanceForGeneralisation = 4; maxCellDistanceForGeneralisation <= 32; maxCellDistanceForGeneralisation *= 2) {
//            cellsize = (1600 / maxCellDistanceForGeneralisation) / resolutionMultiplier;
//            runAll(resolutionMultiplier);
//        }
    }

    private void init() {
        //initStyleParameters();
        File mapDirectory;
        mapDirectory = df.getOutputSHBELineMapsDir();
//        DW_MapsLCC.setMapDirectory(mapDirectory);
        foregrounds = new ArrayList<AGDT_Shapefile>();
        //midgrounds = new ArrayList<AGDT_Shapefile>();
//        backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                env,
                "LSOA",
                targetPropertyNameLSOA,
                DW_MapsLCC.getShapefileDataStoreFactory());
        //foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
        foregrounds.add(DW_MapsLCC.getCommunityAreasDW_Shapefile());
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

    public void runRateMaps() {
        File dirOut;
        dirOut = new File(
                df.getOutputSHBEMapsDir(),
                "Density");

        String[] SHBEFilenames;
        SHBEFilenames = env.getDW_SHBE_Handler().getSHBEFilenamesAll();
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
        DW_Shapefile backgroundDW_Shapefile;
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
//        DW_MapsLCC.setBackgroundDW_Shapefile(backgroundDW_Shapefile);
//        backgroundDW_Shapefile = new DW_Shapefile(f);
        //foregroundDW_Shapefile0 = new ArrayList<AGDT_Shapefile>();
        //foregroundDW_Shapefile0.add(getCommunityAreasDW_Shapefile());
//        foregroundDW_Shapefile1 = new DW_Shapefile(f);
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        DW_Shapefile foregroundDW_Shapefile1;
        foregroundDW_Shapefile1 = DW_MapsLCC.getCommunityAreasDW_Shapefile();
//        DW_MapsLCC.setForegroundDW_Shapefile1(foregroundDW_Shapefile1);
//        DW_Shapefile sf = getCommunityAreasDW_Shapefile();
//        sf.getFeatureLayer().getFeatureSource();

        nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
        ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
        xllcorner = 413000;
        yllcorner = 422500;
        dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();

        boolean overlaycommunityAreas;
        overlaycommunityAreas = true;
//        overlaycommunityAreas = false;
        File dirOut1;
        if (overlaycommunityAreas) {
            foregroundDW_Shapefile1 = DW_MapsLCC.getCommunityAreasDW_Shapefile();
            dirOut1 = new File(dirOut, "CommunityAreasOverlaid");
        } else {
            dirOut1 = new File(dirOut, "LADOverlaid");
            foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        }
        Grids_Grid2DSquareCellDoubleFactory f;
        f = new Grids_Grid2DSquareCellDoubleFactory(ge, handleOutOfMemoryErrors);
        f.setDimensions(dimensions);
        f.setGridStatistics(new Grids_GridStatistics0(ge));
        Grids_ProcessorGWS p;
        p = new Grids_ProcessorGWS(ge);

        Grids_Grid2DSquareCellDouble numerator = null;
        Grids_Grid2DSquareCellDouble denominator1;
        Grids_Grid2DSquareCellDouble denominator2;
        Grids_Grid2DSquareCellDouble rate;
        rate = (Grids_Grid2DSquareCellDouble) f.create(nrows, ncols);

        int index;
        index = 0;
        List<String> stats = new ArrayList<String>();
        stats.add("WSum");

        File dirIn;
        dirIn = new File(
                dirOut,
                "AllPT/UO/All/CommunityAreasOverlaid/MonthlyUO/Social");
        File numeratorFile;
        File denominatorFile = null;
        if (false) {
            numeratorFile = new File(
                    dirIn,
                    "2013_AprMinus2015_Oct.asc");
            if (numeratorFile.exists()) {
                numerator = (Grids_Grid2DSquareCellDouble) f.create(numeratorFile);
                System.out.println(numerator.toString(handleOutOfMemoryErrors));
            } else {
                int debug = 1;
            }
            denominatorFile = new File(
                    dirIn,
                    "2013_Apr/Density2013_Apr_554_ncols_680_cellsize_50.0.asc");
            denominator1 = (Grids_Grid2DSquareCellDouble) f.create(denominatorFile);
            System.out.println(denominator1.toString(handleOutOfMemoryErrors));
            denominatorFile = new File(
                    dirIn,
                    "2015_Oct/Density2015_Oct_554_ncols_680_cellsize_50.0.asc");
            denominator2 = (Grids_Grid2DSquareCellDouble) f.create(denominatorFile);
            System.out.println(denominator2.toString(handleOutOfMemoryErrors));
            //p.addToGrid(denominator1, denominator2, handleOutOfMemoryErrors);
            //System.out.println(denominator1.toString(handleOutOfMemoryErrors));

            for (long row = 0; row < nrows; row++) {
                for (long col = 0; col < ncols; col++) {
                    double n = numerator.getCell(row, col, handleOutOfMemoryErrors);
                    double d1 = denominator1.getCell(row, col, handleOutOfMemoryErrors);
                    double d2 = denominator2.getCell(row, col, handleOutOfMemoryErrors);
                    double d = d1 + d2;
                    double r;
                    if (d == 0) {
                        r = 0;
                    } else {
                        r = n / (d / 2.0d);
                    }
                    rate.setCell(row, col, r, handleOutOfMemoryErrors);
                }
            }
            System.out.println(rate.toString(handleOutOfMemoryErrors));

            //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
            for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                    cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
                index++;
                double distance = maxCellDistanceForGeneralisation * cellsize;
                double weightIntersect = 1.0d;
                double weightFactor = 2.0d;
                // RegionUnivariateStatistics
                List<Grids_AbstractGrid2DSquareCell> gws;
                gws = gp.regionUnivariateStatistics(
                        rate,
                        stats,
                        distance,
                        weightIntersect,
                        weightFactor,
                        gf);
                Iterator<Grids_AbstractGrid2DSquareCell> itegws;
                itegws = gws.iterator();
                // Set normaliser part of the result to null to save space
                Grids_AbstractGrid2DSquareCell normaliser = itegws.next();
                normaliser = null;
                // Write out grid
                Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();
                String outputName2;
                outputName2 = "Rate" + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                File asciigridFile = new File(
                        dirIn,
                        //dirOut1,
                        outputName2 + ".asc");
                eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

                boolean scaleToFirst = false;
                outputGrid((Grids_Grid2DSquareCellDouble) gwsgrid,
                        dirOut1,
                        outputName2,
                        "name" + index,
                        scaleToFirst);

//            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
//            outputGridToImageUsingGeoToolsAndSetCommonStyle(
//                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
//                    gwsgrid,
//                    asciigridFile,
//                    dirIn,
//                    outputName2,
//                    index,
//                    scaleToFirst);
            }
        }

        // Includes
        TreeMap<String, ArrayList<Integer>> includes;
        includes = env.getDW_SHBE_Handler().getIncludes();
        includes.remove("All");
        includes.remove("Yearly");
        includes.remove("6Monthly");
        includes.remove("3Monthly");
//        includes.remove("MonthlyUO");
        includes.remove("Monthly");

        ArrayList<Integer> include;
        include = includes.get("MonthlyUO");

        File dirOut2;
        dirOut2 = new File(
                dirOut1,
                "Clustering");
        dirOut2.mkdirs();
        Iterator<Integer> includesIte;
        String type = "Social";

        DW_SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = env.getDW_SHBE_Handler();

//        if (true) {
        if (true) {
            File dirIn2 = new File(
                    dirOut1,
                    "AllPT/CommunityAreasOverlaid/MonthlyUO/" + type);
            includesIte = include.iterator();
            int i;
            String year;
            String month;
            while (includesIte.hasNext()) {
                i = includesIte.next();
                year = tDW_SHBE_Handler.getYear(SHBEFilenames[i]);
                month = tDW_SHBE_Handler.getMonth3(SHBEFilenames[i]);
                numeratorFile = new File(
                        dirIn,
                        year + "_" + month + "/Density" + year + "_" + month + "_554_ncols_680_cellsize_50.0.asc");
                numerator = (Grids_Grid2DSquareCellDouble) f.create(numeratorFile);
                denominatorFile = new File(
                        dirIn2,
                        year + "_" + month + "/Density" + year + "_" + month + "_554_ncols_680_cellsize_50.0.asc");
                denominator2 = (Grids_Grid2DSquareCellDouble) f.create(denominatorFile);
                System.out.println(denominator2.toString(handleOutOfMemoryErrors));
                //p.addToGrid(denominator1, denominator2, handleOutOfMemoryErrors);
                //System.out.println(denominator1.toString(handleOutOfMemoryErrors));

                for (long row = 0; row < nrows; row++) {
                    for (long col = 0; col < ncols; col++) {
                        double n = numerator.getCell(row, col, handleOutOfMemoryErrors);
                        double d1 = denominator2.getCell(row, col, handleOutOfMemoryErrors);
                        double r;
                        if (d1 == 0) {
                            r = 0;
                        } else {
                            r = n / d1;
                        }
                        rate.setCell(row, col, r, handleOutOfMemoryErrors);
                    }
                }
                System.out.println(rate.toString(handleOutOfMemoryErrors));

                //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                        cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
                    index++;
                    double distance = maxCellDistanceForGeneralisation * cellsize;
                    double weightIntersect = 1.0d;
                    double weightFactor = 2.0d;
                    // RegionUnivariateStatistics
                    List<Grids_AbstractGrid2DSquareCell> gws;
                    gws = gp.regionUnivariateStatistics(
                            rate,
                            stats,
                            distance,
                            weightIntersect,
                            weightFactor,
                            gf);
                    Iterator<Grids_AbstractGrid2DSquareCell> itegws;
                    itegws = gws.iterator();
                    // Set normaliser part of the result to null to save space
                    Grids_AbstractGrid2DSquareCell normaliser = itegws.next();
                    normaliser = null;
                    // Write out grid
                    Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();
                    String outputName2;
                    outputName2 = year + "_" + month + "UO_Over_All_" + type + "_Rate";
//                            + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                    File asciigridFile = new File(
                            dirIn,
                            outputName2 + ".asc");
                    eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

                    boolean scaleToFirst = false;
                    outputGrid((Grids_Grid2DSquareCellDouble) gwsgrid,
                            dirOut2,
                            outputName2,
                            "name" + index,
                            scaleToFirst);
                }
            }
        }

        if (true) {
            includesIte = include.iterator();
            int i;
            String year0;
            String month0;
            String year00;
            String month00;
            String year;
            String month;
            i = includesIte.next();
            year0 = tDW_SHBE_Handler.getYear(SHBEFilenames[i]);
            month0 = tDW_SHBE_Handler.getMonth3(SHBEFilenames[i]);
            year00 = year0;
            month00 = month0;
            while (includesIte.hasNext()) {
                i = includesIte.next();
                year = tDW_SHBE_Handler.getYear(SHBEFilenames[i]);
                month = tDW_SHBE_Handler.getMonth3(SHBEFilenames[i]);
                numeratorFile = new File(
                        dirOut2,
                        year0 + "_" + month0 + "UO_Over_All_" + type + "_Rate.asc");
                numerator = (Grids_Grid2DSquareCellDouble) f.create(numeratorFile);
                denominatorFile = new File(
                        dirOut2,
                        year + "_" + month + "UO_Over_All_" + type + "_Rate.asc");
                denominator2 = (Grids_Grid2DSquareCellDouble) f.create(denominatorFile);
                System.out.println(denominator2.toString(handleOutOfMemoryErrors));
                //p.addToGrid(denominator1, denominator2, handleOutOfMemoryErrors);
                //System.out.println(denominator1.toString(handleOutOfMemoryErrors));

                for (long row = 0; row < nrows; row++) {
                    for (long col = 0; col < ncols; col++) {
                        double n = numerator.getCell(row, col, handleOutOfMemoryErrors);
                        double d1 = denominator2.getCell(row, col, handleOutOfMemoryErrors);
                        double r;
                        r = n - d1;
                        rate.setCell(row, col, r, handleOutOfMemoryErrors);
                    }
                }
                System.out.println(rate.toString(handleOutOfMemoryErrors));

                //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                        cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
                    index++;
                    double distance = maxCellDistanceForGeneralisation * cellsize;
                    double weightIntersect = 1.0d;
                    double weightFactor = 2.0d;
                    // RegionUnivariateStatistics
                    List<Grids_AbstractGrid2DSquareCell> gws;
                    gws = gp.regionUnivariateStatistics(
                            rate,
                            stats,
                            distance,
                            weightIntersect,
                            weightFactor,
                            gf);
                    Iterator<Grids_AbstractGrid2DSquareCell> itegws;
                    itegws = gws.iterator();
                    // Set normaliser part of the result to null to save space
                    Grids_AbstractGrid2DSquareCell normaliser = itegws.next();
                    normaliser = null;
                    // Write out grid
                    Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();
                    String outputName2;
                    outputName2 = year0 + month0 + "To" + year + month + "_" + type + "_Diff" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                    File asciigridFile = new File(
                            dirIn,
                            outputName2 + ".asc");
                    eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

                    boolean scaleToFirst = false;
                    outputGrid((Grids_Grid2DSquareCellDouble) gwsgrid,
                            dirOut2,
                            outputName2,
                            "name" + index,
                            scaleToFirst);
                }
                year0 = year;
                month0 = month;
            }

            numeratorFile = new File(
                    dirOut2,
                    year00 + "_" + month00 + "UO_Over_All_" + type + "_Rate.asc");
            numerator = (Grids_Grid2DSquareCellDouble) f.create(numeratorFile);
            denominatorFile = new File(
                    dirOut2,
                    year0 + "_" + month0 + "UO_Over_All_" + type + "_Rate.asc");
            denominator2 = (Grids_Grid2DSquareCellDouble) f.create(denominatorFile);
            System.out.println(denominator2.toString(handleOutOfMemoryErrors));
            //p.addToGrid(denominator1, denominator2, handleOutOfMemoryErrors);
            //System.out.println(denominator1.toString(handleOutOfMemoryErrors));

            for (long row = 0; row < nrows; row++) {
                for (long col = 0; col < ncols; col++) {
                    double n = numerator.getCell(row, col, handleOutOfMemoryErrors);
                    double d1 = denominator2.getCell(row, col, handleOutOfMemoryErrors);
                    double r;
                    r = n - d1;
                    rate.setCell(row, col, r, handleOutOfMemoryErrors);
                }
            }
            System.out.println(rate.toString(handleOutOfMemoryErrors));

            //int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
            for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                    cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
                index++;
                double distance = maxCellDistanceForGeneralisation * cellsize;
                double weightIntersect = 1.0d;
                double weightFactor = 2.0d;
                // RegionUnivariateStatistics
                List<Grids_AbstractGrid2DSquareCell> gws;
                gws = gp.regionUnivariateStatistics(
                        rate,
                        stats,
                        distance,
                        weightIntersect,
                        weightFactor,
                        gf);
                Iterator<Grids_AbstractGrid2DSquareCell> itegws;
                itegws = gws.iterator();
                // Set normaliser part of the result to null to save space
                Grids_AbstractGrid2DSquareCell normaliser = itegws.next();
                normaliser = null;
                // Write out grid
                Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();
                String outputName2;
                outputName2 = year00 + month00 + "To" + year0 + month0 + "_" + type + "_Diff" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                File asciigridFile = new File(
                        dirIn,
                        outputName2 + ".asc");
                eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

                boolean scaleToFirst = false;
                outputGrid((Grids_Grid2DSquareCellDouble) gwsgrid,
                        dirOut2,
                        outputName2,
                        "name" + index,
                        scaleToFirst);
            }

        }

    }

    //public void runAll(int resolutionMultiplier) {
    public void runAll() throws Exception, Error {
        boolean scaleToFirst = false;
        File dirOut;
        dirOut = new File(
                df.getOutputSHBEMapsDir(),
                "Density");
        DW_SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = env.getDW_SHBE_Handler();
        String[] SHBEFilenames;
        SHBEFilenames = tDW_SHBE_Handler.getSHBEFilenamesAll();

        DW_UO_Data DW_UO_Data;
        DW_UO_Data = env.getDW_UO_Data();

//        Object[] ttgs = DW_SHBE_TenancyType_Handler.getTenancyTypeGroups();
//        HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups;
//        tenancyTypeGroups = (HashMap<Boolean, TreeMap<String, ArrayList<String>>>) ttgs[0];
//        HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped;
//        tenancyTypesGrouped = (HashMap<Boolean, ArrayList<String>>) ttgs[1];
//        HashMap<Boolean, ArrayList<String>> regulatedGroups;
//        regulatedGroups = (HashMap<Boolean, ArrayList<String>>) ttgs[2];
//        HashMap<Boolean, ArrayList<String>> unregulatedGroups;
//        unregulatedGroups = (HashMap<Boolean, ArrayList<String>>) ttgs[3];
        TreeMap<String, ArrayList<String>> allTenancyTypeGroups;
        allTenancyTypeGroups = getAllTenancyTypeGroups();
        allTenancyTypeGroups.remove("All");
//        allTenancyTypeGroups.remove("HB");
//        allTenancyTypeGroups.remove("Social");
//        allTenancyTypeGroups.remove("Council");
//        allTenancyTypeGroups.remove("RSL");
//        allTenancyTypeGroups.remove("PrivateDeregulated");
//        allTenancyTypeGroups.remove("CTBOnly");

        // Includes
        TreeMap<String, ArrayList<Integer>> includes;
        includes = tDW_SHBE_Handler.getIncludes();
//        includes.remove("All");
        includes.remove("Yearly");
        includes.remove("6Monthly");
        includes.remove("3Monthly");
        includes.remove("MonthlyUO");
        includes.remove("Monthly");

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
        DW_Shapefile backgroundDW_Shapefile;
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
//        DW_MapsLCC.setBackgroundDW_Shapefile(backgroundDW_Shapefile);
//        backgroundDW_Shapefile = new DW_Shapefile(f);
        //foregroundDW_Shapefile0 = new ArrayList<AGDT_Shapefile>();
        //foregroundDW_Shapefile0.add(getCommunityAreasDW_Shapefile());
//        foregroundDW_Shapefile1 = new DW_Shapefile(f);
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        DW_Shapefile foregroundDW_Shapefile1;
        foregroundDW_Shapefile1 = DW_MapsLCC.getCommunityAreasDW_Shapefile();
//        DW_MapsLCC.setForegroundDW_Shapefile1(foregroundDW_Shapefile1);
//        DW_Shapefile sf = getCommunityAreasDW_Shapefile();
//        sf.getFeatureLayer().getFeatureSource();

        nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
        ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
        xllcorner = 413000;
        yllcorner = 422500;
        dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));
        ArrayList<Boolean> b;
        b = new ArrayList<Boolean>();
        b.add(true);
        b.add(false);

        ArrayList<String> paymentTypes;
        paymentTypes = DW_Strings.getPaymentTypes();
        paymentTypes.remove(DW_Strings.sPaymentTypeAll);
//        paymentTypes.remove(tDW_SHBE_Handler.sPaymentTypeIn);
//        paymentTypes.remove(tDW_SHBE_Handler.sPaymentTypeSuspended);
//        paymentTypes.remove(tDW_SHBE_Handler.sPaymentTypeOther);

        Iterator<String> inPaymentTypesIte;
        inPaymentTypesIte = paymentTypes.iterator();
        while (inPaymentTypesIte.hasNext()) {
            String inPaymentType;
            inPaymentType = inPaymentTypesIte.next();
            File dirOut2 = new File(
                    dirOut,
                    inPaymentType);
            boolean doUnderOccupied;
//            doUnderOccupied = true;
//            doUnderOccupied = false;
            Iterator<Boolean> iteb;
            iteb = b.iterator();
            while (iteb.hasNext()) {
                doUnderOccupied = iteb.next();
                if (doUnderOccupied) {
                    boolean overlaycommunityAreas;
                    overlaycommunityAreas = true;
//                        Iterator<Boolean> iteb3;
//                        iteb3 = b.iterator();
//                        while (iteb3.hasNext()) {
//                            boolean overlaycommunityAreas = iteb3.next();
                    boolean doRSL;
                    boolean doCouncil;
                    doRSL = true;
                    doCouncil = true;
                    run(
                            inPaymentType,
                            allTenancyTypeGroups,
                            DW_UO_Data,
                            doUnderOccupied,
                            doRSL,
                            doCouncil,
                            scaleToFirst,
                            df.getUOFile(dirOut2, doUnderOccupied, doCouncil, doRSL),
                            overlaycommunityAreas,
                            SHBEFilenames,
                            tDW_SHBE_Handler,
                            includes);
                    doRSL = true;
                    doCouncil = false;
                    run(
                            inPaymentType,
                            allTenancyTypeGroups,
                            DW_UO_Data,
                            doUnderOccupied,
                            doRSL,
                            doCouncil,
                            scaleToFirst,
                            df.getUOFile(dirOut2, doUnderOccupied, doCouncil, doRSL),
                            overlaycommunityAreas,
                            SHBEFilenames,
                            tDW_SHBE_Handler,
                            includes);
                    doRSL = false;
                    doCouncil = true;
                    run(
                            inPaymentType,
                            allTenancyTypeGroups,
                            DW_UO_Data,
                            doUnderOccupied,
                            doRSL,
                            doCouncil,
                            scaleToFirst,
                            df.getUOFile(dirOut2, doUnderOccupied, doCouncil, doRSL),
                            overlaycommunityAreas,
                            SHBEFilenames,
                            tDW_SHBE_Handler,
                            includes);

//                    }
                } else {
                    boolean overlaycommunityAreas;
                    overlaycommunityAreas = true;
//                        Iterator<Boolean> iteb3;
//                    iteb3 = b.iterator();
//                    while (iteb3.hasNext()) {
//                        boolean overlaycommunityAreas = iteb3.next();
                    run(
                            inPaymentType,
                            allTenancyTypeGroups,
                            //                            tenancyTypeGroups,
                            //                            tenancyTypesGrouped,
                            //                            regulatedGroups,
                            //                            unregulatedGroups,
                            DW_UO_Data,
                            doUnderOccupied,
                            false,
                            false,
                            scaleToFirst,
                            dirOut2,
                            overlaycommunityAreas,
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
//                        DW_Files.getUODir(dirOut2, doUnderOccupied, false),
//                        SHBEFilenames,
//                        tDW_SHBE_Handler);
//                    }
                }
            }
        }
    }

//    public void run(
//            DW_SHBE_DataPT collectionHandler,
//            String inPaymentType,
//            TreeMap<String, ArrayList<Integer>> includes,
//            ArrayList<ArrayList<String>> tenancyTypeGroups,
//            //            HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups,
//            //            HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped,
//            //            HashMap<Boolean, ArrayList<String>> regulatedGroups,
//            //            HashMap<Boolean, ArrayList<String>> unregulatedGroups,
//            Object[] underOccupiedData,
//            boolean doUnderOccupied,
//            boolean doCouncil,
//            boolean doRSL,
//            boolean scaleToFirst,
//            File dirOut,
//            String[] SHBEFilenames,
//            DW_SHBE_Handler tDW_SHBE_Handler) {
//
//        Iterator<String> ite;
//        ite = includes.keySet().iterator();
//        while (ite.hasNext()) {
//            String includeName;
//            includeName = ite.next();
//            ArrayList<Integer> include;
//            include = includes.get(includeName);
//            for (int i = 0; i < SHBEFilenames.length; i++) {
//                if (include.contains(i)) {
//                    DW_SHBE_Records SHBEData0;
//                    String yM0 = DW_SHBE_Handler.getYM3(SHBEFilenames[i]);
//                    // Init underOccupiedSets
//                    TreeMap<String, DW_UO_Set> underOccupiedSets0 = null;
//                    DW_UO_Set underOccupiedSet0 = null;
//                    if (doUnderOccupied) {
//                        if (doCouncil) {
//                            underOccupiedSets0 = (TreeMap<String, DW_UO_Set>) underOccupiedData[0];
//                        } else {
//                            underOccupiedSets0 = (TreeMap<String, DW_UO_Set>) underOccupiedData[1];
//                        }
//                        underOccupiedSet0 = underOccupiedSets0.get(yM0);
//                        if (underOccupiedSet0 != null) {
//                            SHBEData0 = new DW_SHBE_Records(
//                                    SHBEFilenames[i],
//                                    inPaymentType);
//                            Iterator<ArrayList<String>> ites;
//                            ites = tenancyTypeGroups.iterator();
//                            while (ites.hasNext()) {
//                                ArrayList<String> TTs;
//                                TTs = ites.next();
//                                String name;
//                                name = DW_LineMaps_LCC.getName(TTs);
//                                File dirOut2 = new File(
//                                        dirOut,
//                                        inPaymentType);
//                                dirOut2 = new File(
//                                        dirOut2,
//                                        name);
//                                Grids_Grid2DSquareCellDouble g0 = doDensity(
//                                        TTs,
//                                        dirOut2,
//                                        yM0,
//                                        SHBEData0,
//                                        underOccupiedData,
//                                        doUnderOccupied,
//                                        underOccupiedSet0,
//                                        doCouncil,
//                                        doRSL,
//                                        scaleToFirst);
//                            }
//                        }
//                    } else {
//                        SHBEData0 = new DW_SHBE_Records(
//                                SHBEFilenames[i],
//                                inPaymentType);
//                        Iterator<ArrayList<String>> ites;
//                        ites = tenancyTypeGroups.iterator();
//                        while (ites.hasNext()) {
//                            ArrayList<String> TTs;
//                            TTs = ites.next();
//                            String name;
//                            name = DW_LineMaps_LCC.getName(TTs);
//                            File dirOut2 = new File(
//                                    dirOut,
//                                    inPaymentType);
//                            dirOut2 = new File(
//                                    dirOut2,
//                                    name);
//                            Grids_Grid2DSquareCellDouble g0 = doDensity(
//                                    TTs,
//                                    dirOut2,
//                                    yM0,
//                                    SHBEData0,
//                                    underOccupiedData,
//                                    doUnderOccupied,
//                                    underOccupiedSet0,
//                                    underOccupiedSet0,fdsfdss
//                                    doRSL,
//                                    doCouncil,
//                                    scaleToFirst);
//                        }
//                    }
//                }
//            }
//        }
//    }
    public void run(
            String inPaymentType,
            TreeMap<String, ArrayList<String>> tenancyTypeGroups,
            //            HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups,
            //            HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped,
            //            HashMap<Boolean, ArrayList<String>> regulatedGroups,
            //            HashMap<Boolean, ArrayList<String>> unregulatedGroups,
            DW_UO_Data DW_UO_Data,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL,
            boolean scaleToFirst,
            File dirOut,
            boolean overlaycommunityAreas,
            String[] SHBEFilenames,
            DW_SHBE_Handler tDW_SHBE_Handler,
            TreeMap<String, ArrayList<Integer>> includes) throws Exception, Error {
//        // Declare iterators
//        Iterator<String> claimantTypesIte;
//        Iterator<String> tenancyTypeIte;
//        Iterator<String> levelsIte;
//        Iterator<String> typesIte;
//        Iterator<String> distanceTypesIte;
//        Iterator<Double> distancesIte;
//        backgroundDW_Shapefile = getCommunityAreasDW_Shapefile();
        //foregroundDW_Shapefile0 = new DW_Shapefile(f);
        //foregroundDW_Shapefile1 = getCommunityAreasDW_Shapefile();
        DW_Shapefile backgroundDW_Shapefile;
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
//        DW_MapsLCC.setBackgroundDW_Shapefile(backgroundDW_Shapefile);
        File dirOut1;
        if (overlaycommunityAreas) {
//            DW_MapsLCC.setForegroundDW_Shapefile1(DW_MapsLCC.getCommunityAreasDW_Shapefile());
            dirOut1 = new File(dirOut, "CommunityAreasOverlaid");
        } else {
            dirOut1 = new File(dirOut, "LADOverlaid");
//            DW_MapsLCC.setForegroundDW_Shapefile1(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
        }
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
        dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));
        DW_UO_Set underOccupiedSetCouncil0 = null;
        DW_UO_Set underOccupiedSetRSL0 = null;
        TreeMap<DW_YM3, DW_UO_Set> underOccupiedSetsCouncil = null;
        TreeMap<DW_YM3, DW_UO_Set> underOccupiedSetsRSL = null;
        if (doUnderOccupied) {
            if (doCouncil) {
                underOccupiedSetsCouncil = DW_UO_Data.getCouncilUOSets();
            }
            if (doRSL) {
                underOccupiedSetsRSL = DW_UO_Data.getRSLUOSets();
            }
        }
        String tenancyTypeGroupName;
        Iterator<String> includesIte;
        String includeName;
        File dirOut2;
        ArrayList<Integer> include;
        Iterator<Integer> ite;
        int i;
        boolean initialised;
        DW_YM3 yM30;
        DW_SHBE_Records recs0;
        DW_YM3 yM300;

        Iterator<String> ites;
        ites = tenancyTypeGroups.keySet().iterator();
        while (ites.hasNext()) {
            tenancyTypeGroupName = ites.next();
            includesIte = includes.keySet().iterator();
            while (includesIte.hasNext()) {
                includeName = includesIte.next();
                dirOut2 = new File(
                        dirOut1,
                        includeName);
                include = includes.get(includeName);
                ite = include.iterator();
                i = ite.next();
                yM30 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                if (doUnderOccupied) {
                    initialised = false;
                    while (!initialised) {
                        if (doRSL) {
                            underOccupiedSetRSL0 = underOccupiedSetsRSL.get(yM30);
                            if (underOccupiedSetRSL0 != null) {
                                initialised = true;
                            } else {
                                i = ite.next();
                                yM30 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                            }
                        }
                        if (doCouncil) {
                            underOccupiedSetCouncil0 = underOccupiedSetsCouncil.get(yM30);
                            if (underOccupiedSetCouncil0 != null) {
                                initialised = true;
                            } else {
                                i = ite.next();
                                yM30 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                            }
                        }
                    }
                }
                recs0 = env.getDW_SHBE_Data().getDW_SHBE_Records(yM30);
//            DW_SHBE_Records SHBEData00;
//            SHBEData00 = SHBEData0;
                yM300 = yM30;

//            Iterator<String> ites;
//            ites = tenancyTypeGroups.keySet().iterator();
//            while (ites.hasNext()) {
//                String tenancyTypeGroupName;
//                tenancyTypeGroupName = ites.next();
                ArrayList<String> TTs;
                TTs = tenancyTypeGroups.get(tenancyTypeGroupName);
                File dirOut3 = new File(
                        dirOut2,
                        tenancyTypeGroupName);
                Grids_Grid2DSquareCellDouble g0 = doDensity(
                        TTs,
                        dirOut3,
                        yM30,
                        recs0,
                        DW_UO_Data,
                        doUnderOccupied,
                        underOccupiedSetCouncil0,
                        underOccupiedSetRSL0,
                        doCouncil,
                        doRSL,
                        scaleToFirst);
//                outputGrid(
//                        g0,
//                        dirOut3,
//                        yM30,
//                        "Name" + yM30,
//                        scaleToFirst);
                Grids_Grid2DSquareCellDouble g00 = g0;
                boolean hasNext = false;
                while (ite.hasNext()) {
                    hasNext = true;
                    i = ite.next();

//                    // Just go for the last.
//                    while (ite.hasNext()) {
//                        i = ite.next();
//                    }
                    DW_SHBE_Records SHBEData1;
                    SHBEData1 = DW_SHBE_Data.getDW_SHBE_Records(yM30);

                    DW_YM3 yM31;
                    yM31 = tDW_SHBE_Handler.getYM3(SHBEFilenames[i]);
                    // Init underOccupiedSets
                    DW_UO_Set underOccupiedSetCouncil1 = null;
                    if (doCouncil) {
                        underOccupiedSetCouncil1 = underOccupiedSetsCouncil.get(yM31);
                    }
                    DW_UO_Set underOccupiedSetRSL1 = null;
                    if (doRSL) {
                        underOccupiedSetRSL1 = underOccupiedSetsRSL.get(yM31);
                    }
                    Grids_Grid2DSquareCellDouble g1 = doDensity(
                            TTs,
                            dirOut3,
                            yM31,
                            SHBEData1,
                            DW_UO_Data,
                            doUnderOccupied,
                            underOccupiedSetCouncil1,
                            underOccupiedSetRSL1,
                            doCouncil,
                            doRSL,
                            scaleToFirst);
//                    outputGrid(
//                            g0,
//                            dirOut3,
//                            yM30,
//                            "Name" + yM30,
//                            scaleToFirst);
                    gp.addToGrid(g1, g0,
                            -1.0d, handleOutOfMemoryErrors);
                    outputGrid(
                            g1,
                            dirOut3,
                            yM30 + "Minus" + yM31,
                            "Name" + yM30 + "Minus" + yM31,
                            scaleToFirst);
                    underOccupiedSetCouncil0 = underOccupiedSetCouncil1;
                    underOccupiedSetRSL0 = underOccupiedSetRSL1;
                    yM30 = yM31;
                    recs0 = SHBEData1;
                }
                if (hasNext) {
                    Grids_Grid2DSquareCellDouble g1 = doDensity(
                            TTs,
                            dirOut3,
                            yM30,
                            recs0,
                            DW_UO_Data,
                            doUnderOccupied,
                            underOccupiedSetCouncil0,
                            underOccupiedSetRSL0,
                            doCouncil,
                            doRSL,
                            scaleToFirst);
                    gp.addToGrid(g1, g00,
                            -1.0d, handleOutOfMemoryErrors);
                    outputGrid(
                            g1,
                            dirOut3,
                            yM300 + "Minus" + yM30,
                            "Name" + yM300 + "Minus" + yM30,
                            scaleToFirst);
                }
            }
        }
    }

    protected Grids_Grid2DSquareCellDouble doDensity(
            ArrayList<String> TTs,
            File dirOut,
            DW_YM3 yM3,
            DW_SHBE_Records SHBEData,
            DW_UO_Data DW_UO_Data,
            boolean doUnderOccupied,
            DW_UO_Set underOccupiedSetCouncil,
            DW_UO_Set underOccupiedSetRSL,
            boolean doCouncil,
            boolean doRSL,
            boolean scaleToFirst) {

        File dirOut2 = new File(
                dirOut,
                yM3.toString());

        DW_MapsLCC.getStyleParameters().setnClasses(9);
        DW_MapsLCC.getStyleParameters().setPaletteName2(null);

        String name = "Density" + yM3;

        String outputName;
        outputName = name + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        // Generate initial grid
        File grid = new File(
                dirOut2,
                "Grid_" + outputName);
        grid.mkdirs();
        Grids_Grid2DSquareCellDouble g0 = initiliseGrid(grid);

        TreeMap<String, TreeMap<DW_YM3, TreeMap<String, AGDT_Point>>> lookups;
        lookups = DW_MapsLCC.getONSPDlookups();
        TreeMap<String, AGDT_Point> lookup;
        lookup = lookups.get("Unit").get(DW_Postcode_Handler.getNearestYM3ForONSPDLookup(yM3));

        HashMap<DW_ID, DW_SHBE_Record> records;
        records = SHBEData.getClaimIDToDW_SHBE_RecordMap(env.HandleOutOfMemoryError);

        boolean nonZero = false;

        // Iterator over records
        Iterator<DW_ID> recordsIte;
        recordsIte = records.keySet().iterator();
        while (recordsIte.hasNext()) {
            DW_ID ClaimID = recordsIte.next();
            DW_SHBE_D_Record DRecord = records.get(ClaimID).getDRecord();
            String postcode = DRecord.getClaimantsPostcode();

            int TT = DRecord.getTenancyType();
            String sTT = Integer.toString(TT);
            if (TTs.contains(sTT)) {
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
                    if (doCouncil && doRSL) {
                        doMainLoop = false;
                        DW_UO_Record underOccupied0 = null;
                        if (underOccupiedSetCouncil != null) {
                            underOccupied0 = underOccupiedSetCouncil.getMap().get(
                                    ClaimID);
                        }
                        if (underOccupied0 != null) {
                            doMainLoop = true;
                        }
                        if (underOccupiedSetRSL != null) {
                            underOccupied0 = underOccupiedSetRSL.getMap().get(
                                    ClaimID);
                        }
                        if (underOccupied0 != null) {
                            doMainLoop = true;
                        }
                    }
                    if (doCouncil) {
                        DW_UO_Record underOccupied0 = null;
                        if (underOccupiedSetCouncil != null) {
                            underOccupied0 = underOccupiedSetCouncil.getMap().get(
                                    ClaimID);
                        }
                        doMainLoop = underOccupied0 != null;
                    } else if (doRSL) {
                        DW_UO_Record underOccupied0 = null;
                        if (underOccupiedSetRSL != null) {
                            underOccupied0 = underOccupiedSetRSL.getMap().get(
                                    ClaimID);
                        }
                        doMainLoop = underOccupied0 != null;
                    }

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
                                    + "DW_Postcode_Handler.formatPostcode(postcode) " + DW_Postcode_Handler.formatPostcode(postcode));
                        }
                    }
                }
            }
        }

        if (nonZero) {

            // output grid
            gp.setDirectory(dirOut2, false, handleOutOfMemoryErrors);
//            Grids_ImageExporter ie;
//            ie = new Grids_ImageExporter(ge);
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
                DW_MapsLCC.getStyleParameters().setStyle(name, null, index);
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
                        g0,
                        stats,
                        distance,
                        weightIntersect,
                        weightFactor,
                        gf);
                Iterator<Grids_AbstractGrid2DSquareCell> itegws;
                itegws = gws.iterator();
                // Set normaliser part of the result to null to save space
                Grids_AbstractGrid2DSquareCell normaliser = itegws.next();
                normaliser = null;
                // Write out grid
                Grids_AbstractGrid2DSquareCell gwsgrid = itegws.next();
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
                    DW_MapsLCC.getStyleParameters().setStyle(name, null, index);
                }
            }
        } else {
            System.out.println("Grid " + g0.toString(handleOutOfMemoryErrors) + " is not added to and so is not output.");
        }
        return g0;
    }

    protected void outputGrid(
            Grids_Grid2DSquareCellDouble g1,
            File dirOut,
            String outputName,
            String name,
            boolean scaleToFirst) {

        //styleParameters = new AGDT_StyleParameters();
        DW_MapsLCC.getStyleParameters().setnClasses(5);
        //styleParameters.setnClasses2(5);
        //styleParameters.setPaletteName("Reds");
        DW_MapsLCC.getStyleParameters().setPaletteName2("Blues");
        DW_MapsLCC.getStyleParameters().setAddWhiteForZero(true);

        System.out.println("g1 " + g1.toString(handleOutOfMemoryErrors));
        // output grid
//            Grids_ImageExporter ie;
//            ie = new Grids_ImageExporter(ge);
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
            DW_MapsLCC.getStyleParameters().setStyle(name, null, index);
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
                    g1,
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
                DW_MapsLCC.getStyleParameters().setStyle(name, null, index);
            }
        }
    }

    protected TreeMap<String, ArrayList<String>> getAllTenancyTypeGroups() {
        DW_SHBE_TenancyType_Handler DW_SHBE_TenancyType_Handler;
        DW_SHBE_TenancyType_Handler = env.getDW_SHBE_TenancyType_Handler();
        TreeMap<String, ArrayList<String>> result;
        result = new TreeMap<String, ArrayList<String>>();
        ArrayList<String> l;
        // All
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s1);
        l.add(DW_SHBE_TenancyType_Handler.s2);
        l.add(DW_SHBE_TenancyType_Handler.s3);
        l.add(DW_SHBE_TenancyType_Handler.s4);
        l.add(DW_SHBE_TenancyType_Handler.s5);
        l.add(DW_SHBE_TenancyType_Handler.s6);
        l.add(DW_SHBE_TenancyType_Handler.s7);
        l.add(DW_SHBE_TenancyType_Handler.s8);
        l.add(DW_SHBE_TenancyType_Handler.s9);
        result.put("All", l);
        // HB
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s1);
        l.add(DW_SHBE_TenancyType_Handler.s2);
        l.add(DW_SHBE_TenancyType_Handler.s3);
        l.add(DW_SHBE_TenancyType_Handler.s4);
        l.add(DW_SHBE_TenancyType_Handler.s6);
        l.add(DW_SHBE_TenancyType_Handler.s8);
        l.add(DW_SHBE_TenancyType_Handler.s9);
        result.put("HB", l);
        // Social
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s1);
        l.add(DW_SHBE_TenancyType_Handler.s4);
        result.put("Social", l);
        // Council
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s1);
        result.put("Council", l);
        // RSL
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s1);
        l.add(DW_SHBE_TenancyType_Handler.s4);
        result.put("RSL", l);
        // Private Deregulated
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s3);
        l.add(DW_SHBE_TenancyType_Handler.s6);
        result.put("PrivateDeregulated", l);
        // CTB
        l = new ArrayList<String>();
        l.add(DW_SHBE_TenancyType_Handler.s5);
        l.add(DW_SHBE_TenancyType_Handler.s7);
        result.put("CTBOnly", l);
        return result;
    }

}
