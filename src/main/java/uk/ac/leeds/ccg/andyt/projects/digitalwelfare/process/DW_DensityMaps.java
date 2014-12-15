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

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Parameter;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.StyleLayer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.opengis.style.ContrastMethod;

import java.awt.Color;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataSourceException;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.filter.FilterFactory2;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCellDoubleChunkFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.GridStatistics0;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorGWS;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord0;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord2;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.EnquiryClientBureauOutletID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_CensusAreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_StyleParameters;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor.getLookupFromPostcodeToCensusCode;

/**
 *
 * @author geoagdt
 */
public class DW_DensityMaps extends DW_Maps {

    public DW_DensityMaps() {
    }

    public DW_DensityMaps(File digitalWelfareDir) {
        this.digitalWelfareDir = digitalWelfareDir;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            File digitalWelfareDir = new File("/scratch02/DigitalWelfare/");
            new DW_DensityMaps(digitalWelfareDir).run();
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

        init_ONSPDLookup();

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
        File densityMapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "density");
        int imageWidth = 1000;

        ShapefileDataStoreFactory sdsf;
        sdsf = new ShapefileDataStoreFactory();

        boolean commonlyStyled;
        boolean individuallyStyled;

        String tAdviceLeedsPointName = "AllAdviceLeedsPoints";
        File tAdviceLeedsPointShapefileDir;
        tAdviceLeedsPointShapefileDir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                tAdviceLeedsPointName);
        String tAdviceLeedsPointShapefileFilename;
        tAdviceLeedsPointShapefileFilename = tAdviceLeedsPointName + ".shp";
        File tAdviceLeedsPointShapefile;
        tAdviceLeedsPointShapefile = createAdviceLeedsPointShapefileIfItDoesNotExist(
                tAdviceLeedsPointShapefileDir,
                tAdviceLeedsPointShapefileFilename);

        // Equal Interval runs
        classificationFunctionName = "EqualInterval";
        commonlyStyled = true;
        individuallyStyled = true;
        runAll(showMapsInJMapPane,
                commonlyStyled,
                individuallyStyled,
                tAdviceLeedsPointShapefile,
                nClasses,
                paletteName,
                addWhiteForZero,
                imageWidth,
                densityMapDirectory,
                classificationFunctionName,
                sdsf);
    }

    public void runAll(
            boolean showMapsInJMapPane,
            boolean commonlyStyled,
            boolean individuallyStyled,
            File tAdviceLeedsPointShapefile,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero,
            int imageWidth,
            File densityMapDirectory,
            String classificationFunctionName,
            ShapefileDataStoreFactory sdsf) {

        // General parameters
        // Property for selecting
        String targetPropertyName = "LSOA11CD";
        String level = "LSOA";
        // Get LSOA Codes LSOA Shapefile and Leeds LSOA Shapefile
        DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
        tLSOACodesAndLeedsLSOAShapefile = new DW_CensusAreaCodesAndShapefiles(
                level, targetPropertyName, sdsf);

        TreeMap<String, String> tLookupFromPostcodeToCensusCodes;
        tLookupFromPostcodeToCensusCodes = getLookupFromPostcodeToCensusCode(
                level, 2011);

//        Style backgroundStyle = DW_Style.createDefaultPolygonStyle(
//                Color.BLACK,
//                Color.WHITE);
        Style backgroundStyle = DW_Style.createDefaultPolygonStyle(
                Color.BLACK,//YELLOW,
                null);
        String backgroundStyle_String = "Default Background Style";
        DW_StyleParameters styleParameters;
        styleParameters = new DW_StyleParameters(
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
        boolean scaleToFirst;

        // Grids parameters
        boolean handleOutOfMemoryErrors;
        handleOutOfMemoryErrors = true;

        // Filter just for specific outlets
        HashSet<String> outlets;
        outlets = new HashSet<String>();
        outlets.add("OTLEY - LS21 1BG");
        //outlets = null; // Set outlets null to run for all outlets

        /*
         * (filter == 0) Clip all results to Leeds LAD
         * (filter == 1) Clip all results to Leeds And Neighbouring LADs (showing results for all Leeds LAD)
         * (filter == 2) Clip all results to Leeds And Neighbouring LADs and Craven and York (showing results for all Leeds LAD)
         * (filter == 3) No clipping
         */
        for (int filter = 0; filter < 4; filter++) {
            //int filter = 0;
            deprivationRecords = null;
            if (individuallyStyled) {
                scaleToFirst = false;
                runCAB(sdsf,
                        showMapsInJMapPane, styleParameters,
                        tAdviceLeedsPointShapefile,
                        imageWidth, deprivationRecords, outlets, densityMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLookupFromPostcodeToCensusCodes,
                        png_String, filter, scaleToFirst, handleOutOfMemoryErrors);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(sdsf,
                        showMapsInJMapPane, styleParameters,
                        tAdviceLeedsPointShapefile,
                        imageWidth, deprivationRecords, outlets, densityMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLookupFromPostcodeToCensusCodes,
                        png_String, filter, scaleToFirst, handleOutOfMemoryErrors);
            }

            // Get deprivation data
            deprivationRecords = DW_Processor.getDeprivation_Data();
            if (individuallyStyled) {
                scaleToFirst = false;
                runCAB(sdsf,
                        showMapsInJMapPane, styleParameters,
                        tAdviceLeedsPointShapefile,
                        imageWidth, deprivationRecords, outlets, densityMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLookupFromPostcodeToCensusCodes,
                        png_String, filter, scaleToFirst, handleOutOfMemoryErrors);
            }
            if (commonlyStyled) {
                scaleToFirst = true;
                runCAB(sdsf,
                        showMapsInJMapPane, styleParameters,
                        tAdviceLeedsPointShapefile,
                        imageWidth, deprivationRecords, outlets, densityMapDirectory,
                        classificationFunctionName, targetPropertyName, level,
                        tLSOACodesAndLeedsLSOAShapefile, tLookupFromPostcodeToCensusCodes,
                        png_String, filter, scaleToFirst, handleOutOfMemoryErrors);
            }
        }
    }

    public void runCAB(
            ShapefileDataStoreFactory sdsf,
            boolean showMapsInJMapPane,
            DW_StyleParameters styleParameters,
            File tAdviceLeedsPointShapefile,
            int imageWidth,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            HashSet<String> outlets,
            File densityMapDirectory,
            String classificationFunctionName,
            String targetPropertyName,
            String level,
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            String png_String,
            int filter,
            boolean scaleToFirst,
            boolean handleOutOfMemoryErrors) {

        String style;
        if (scaleToFirst) {
            style = "CommonlyStyled";
        } else {
            style = "IndividuallyStyled";
        }
        File dir;
        dir = new File(
                densityMapDirectory,
                style + "/" + classificationFunctionName);

        mapCountsForLSOAs(
                sdsf,
                tAdviceLeedsPointShapefile,
                tLSOACodesAndLeedsLSOAShapefile,
                tLookupFromPostcodeToCensusCodes,
                deprivationRecords,
                outlets,
                dir,
                targetPropertyName,
                png_String,
                imageWidth,
                filter,
                scaleToFirst,
                styleParameters,
                showMapsInJMapPane,
                handleOutOfMemoryErrors);
    }

    /**
     * Uses a file dialog to select a file and then
     *
     * @param sdsf
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLookupFromPostcodeToCensusCodes
     * @param outlets // May be null, but if not null then filters and only
     * produces results for outlets in the collection
     * @param deprivationRecords
     * @param dir
     * @param targetPropertyName
     * @param png_String
     * @param imageWidth
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     * @param handleOutOfMemoryErrors
     * @param styleParameters
     * @param showMapsInJMapPane
     */
    public void mapCountsForLSOAs(
            ShapefileDataStoreFactory sdsf,
            File tAdviceLeedsPointShapefile,
            DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            HashSet<String> outlets,
            File dir,
            String targetPropertyName,
            String png_String,
            int imageWidth,
            int filter,
            boolean scaleToFirst,
            DW_StyleParameters styleParameters,
            boolean showMapsInJMapPane,
            boolean handleOutOfMemoryErrors) {

//        StyleFactory sf;
//        sf = CommonFactoryFinder.getStyleFactory();
//        FilterFactory2 ff;
//        ff = CommonFactoryFinder.getFilterFactory2();
        long nrows;// = 600;//250;//500;//250;
        long ncols;// = 700;//300;//600;//300;
        long cellsize = 50;//100;//50; //100;
        long xllcorner;// = 413000;
        long yllcorner;// = 422000;

        File backgroundShapefile;
        TreeSet<String> tLSOACodes;
        if (filter == 0) {
            tLSOACodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsCensusAreaCodes();
            backgroundShapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADShapefile(); // LAD
            nrows = 554;
            ncols = 680;
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
        } else {
            if (filter == 1) {
                tLSOACodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsCensusAreaCodes();
                backgroundShapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsShapefile();
                nrows = 1654;
                ncols = 1530;
                xllcorner = 396000;
                //minX 396065.904
                //minXRounded = 396000
                //maxX = 472429.3
                //maxXRounded = 472500
                //maxXRounded - minXRounded = 76500
                //(maxXRounded - minXRounded) / cellsize = 1530
                // minY = 402582.531
                // minYRounded = 402500
                // maxY = 485148.936
                // maxYrounded = 485200
                // maxYRounded - minYRounded = 82700
                // (maxYRounded - minYRounded) / cellsize = 1654
                yllcorner = 402500;
            } else {
                tLSOACodes = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes();
                backgroundShapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADsAndCravenAndYorkShapefile();
                nrows = 1652;
                ncols = 2188;
                xllcorner = 363100;
                //minX = 363191.767
                //minXRounded = 363100
                //maxX = 472429.3
                //maxXRounded = 472500
                //maxXRounded - minXRounded = 109400
                //(maxXRounded - minXRounded) / cellsize = 2188
                // minY = 402582.531
                // minYRounded = 402500
                // maxY = 485148.936
                // maxYrounded = 485100
                // maxYRounded - minYRounded = 82600
                // (maxYRounded - minYRounded) / cellsize = 1652 
                yllcorner = 402500;
            }
        }

        Style backgroundStyle = styleParameters.getBackgroundStyle();

        /*Grid Parameters
         *_____________________________________________________________________
         */
        File densityDir = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "density");
        densityDir.mkdirs();
        File processorDir = new File(
                densityDir,
                "processor");
        Grids_Environment ge;
        ge = new Grids_Environment();
        ESRIAsciiGridExporter eage;
        eage = new ESRIAsciiGridExporter();
        ImageExporter ie;
        ie = new ImageExporter(ge);
        processorDir.mkdirs();
        Grid2DSquareCellProcessorGWS gp;
        gp = new Grid2DSquareCellProcessorGWS(ge);
        gp.set_Directory(processorDir, false, handleOutOfMemoryErrors);

        Grid2DSquareCellDoubleChunkArrayFactory gcf;
        gcf = new Grid2DSquareCellDoubleChunkArrayFactory();
        int chunkNRows = 300;//250; //64
        int chunkNCols = 350;//300; //64

        Grid2DSquareCellDoubleFactory gf;
        gf = new Grid2DSquareCellDoubleFactory(
                processorDir,
                chunkNRows,
                chunkNCols,
                gcf,
                -9999d,
                ge,
                handleOutOfMemoryErrors);

        // Get the CAB Client Data
        ArrayList<String> tChapeltownCABData;
        tChapeltownCABData = getChapeltownCABDataPostcodes();
        TreeMap<String, ArrayList<String>> tLeedsCABData;
        tLeedsCABData = getLeedsCABDataPostcodes();
        tLeedsCABData.put("Chapeltown", tChapeltownCABData);
        // Initialise grid paramters
        long xurcorner = xllcorner + (nrows * cellsize);
        long yurcorner = yllcorner + (ncols * cellsize);
        BigDecimal[] dimensions;
        dimensions = new BigDecimal[5];
        dimensions[0] = new BigDecimal("" + cellsize); //cellsize
        dimensions[1] = new BigDecimal("" + xllcorner); //xllcorner
        dimensions[2] = new BigDecimal("" + yllcorner); //yllcorner
        dimensions[3] = new BigDecimal("" + xurcorner); //xurcorner
        dimensions[4] = new BigDecimal("" + yurcorner); //yurcorner
        String nameOfGrid;
        nameOfGrid = "gridNrows" + Long.toString(nrows) + "Ncols" + Long.toString(ncols);
        // Grid generalisation paramters
        int maxCellDistanceForGeneralisation = 32;

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

                // Iterate over the outlets;
                Iterator<String> ite2;
                ite2 = tLeedsCABData.keySet().iterator();
                while (ite2.hasNext()) {
                    String outlet = ite2.next();
                    boolean process = false;
                    if (outlets != null) {
                        if (outlets.contains(outlet)) {
                            process = true;
                        }
                    } else {
                        process = true;
                    }
                    if (process) {
                        processForOutlet(
                                outlet, outlets, mapDirectory, nameOfGrid, gf, gcf,
                                nrows, ncols, maxCellDistanceForGeneralisation,
                                cellsize, dimensions, ge, eage, tLeedsCABData,
                                ie, gp, tLookupFromPostcodeToCensusCodes,
                                tAdviceLeedsPointShapefile, backgroundShapefile, backgroundStyle,
                                deprivationRecords, deprivationClasses,
                                deprivationClass, png_String, imageWidth,
                                showMapsInJMapPane, handleOutOfMemoryErrors);
                    }
                }
                if (!scaleToFirst) {
                    styleParameters.setStyle(null);
                }
            }
        } else {
            File mapDirectory = new File(
                    dir,
                    "all");
            mapDirectory.mkdirs();
            // Iterate over the outlets;
            Iterator<String> ite2;
            ite2 = tLeedsCABData.keySet().iterator();
            while (ite2.hasNext()) {
                String outlet = ite2.next();
                processForOutlet(
                        outlet, outlets, mapDirectory, nameOfGrid, gf, gcf,
                        nrows, ncols, maxCellDistanceForGeneralisation,
                        cellsize, dimensions, ge, eage, tLeedsCABData, ie, gp,
                        tLookupFromPostcodeToCensusCodes, tAdviceLeedsPointShapefile, backgroundShapefile,
                        backgroundStyle, deprivationRecords, null, chunkNCols,
                        png_String, imageWidth, showMapsInJMapPane,
                        handleOutOfMemoryErrors);
            }
            if (!scaleToFirst) {
                styleParameters.setStyle(null);
            }
        }
    }

    private void processForOutlet(
            String outlet,
            HashSet<String> outlets,
            File mapDirectory,
            String nameOfGrid,
            Grid2DSquareCellDoubleFactory gf,
            AbstractGrid2DSquareCellDoubleChunkFactory gcf,
            long nrows,
            long ncols,
            int maxCellDistanceForGeneralisation,
            double cellsize,
            BigDecimal[] dimensions,
            Grids_Environment ge,
            ESRIAsciiGridExporter eage,
            TreeMap<String, ArrayList<String>> tLeedsCABData,
            ImageExporter ie,
            Grid2DSquareCellProcessorGWS gp,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            //FeatureLayer backgroundFeatureLayer,
            File tAdviceLeedsPointShapefile,
            File backgroundShapefile,
            Style backgroundStyle,
            //            ShapefileDataStoreFactory sdsf,
            //            Object[] tLSOACodesAndLeedsLSOAShapefile,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            //            HashSet<String> outlets,
            //            File dir,
            //            String targetPropertyName,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            String png_String,
            int imageWidth,
            //            int filter,
            //            boolean scaleToFirst,
            //            Object[] styleParameters,
            boolean showMapsInJMapPane,
            boolean handleOutOfMemoryErrors) {
        boolean process = false;
        if (outlets != null) {
            if (outlets.contains(outlet)) {
                process = true;
            }
        } else {
            process = true;
        }
        if (process) {
            System.out.println("Outlet " + outlet);
            File outletmapDirectory = new File(
                    mapDirectory,
                    outlet);
            outletmapDirectory.mkdirs();
            File grid = new File(
                    outletmapDirectory,
                    nameOfGrid);
            grid.mkdirs();
            // Initialise grid
            Grid2DSquareCellDouble g;
            g = (Grid2DSquareCellDouble) gf.create(
                    new GridStatistics0(),
                    outletmapDirectory,
                    gcf,
                    nrows,
                    ncols,
                    dimensions,
                    ge,
                    handleOutOfMemoryErrors);
            System.out.println("" + g.toString(handleOutOfMemoryErrors));
            for (int row = 0; row < nrows; row++) {
                for (int col = 0; col < ncols; col++) {
                    g.setCell(row, col, 0, handleOutOfMemoryErrors); // set all values to 0;
                }
            }
            // Add to grid
            ArrayList<String> postcodes;
            postcodes = tLeedsCABData.get(outlet);
            int countNonMatchingPostcodes = 0;
            Iterator<String> itep = postcodes.iterator();
            while (itep.hasNext()) {
                String postcode = itep.next();
                String aLSOACode;
                aLSOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
                if (aLSOACode != null) {
                    if (deprivationRecords == null) {
                        DW_Point aPoint = get_ONSPDlookup().get(postcode);
                        if (aPoint != null) {
                            int x = aPoint.getX();
                            int y = aPoint.getY();
                            g.addToCell((double) x, (double) y, 1.0, handleOutOfMemoryErrors);
                        } else {
                            countNonMatchingPostcodes++;
                        }
                    } else {
                        //Convert postcode to LSOA code
                        Deprivation_DataRecord aDeprivation_DataRecord;
                        aDeprivation_DataRecord = deprivationRecords.get(aLSOACode);
                        // aDeprivation_DataRecord might be null as deprivation data comes from 2001 census...
                        if (aDeprivation_DataRecord != null) {
                            Integer thisDeprivationClass;
                            thisDeprivationClass = Deprivation_DataHandler.getDeprivationClass(
                                    deprivationClasses,
                                    aDeprivation_DataRecord);
                            if (thisDeprivationClass == deprivationClass.intValue()) {
                                DW_Point aPoint = get_ONSPDlookup().get(postcode);
                                if (aPoint != null) {
                                    int x = aPoint.getX();
                                    int y = aPoint.getY();
                                    g.addToCell((double) x, (double) y, 1.0, handleOutOfMemoryErrors);
                                } else {
                                    countNonMatchingPostcodes++;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("" + g.toString(handleOutOfMemoryErrors));
            System.out.println("" + countNonMatchingPostcodes);
            // output the grid
//                    File imageFile;
//                    imageFile = new File(
//                            outletmapDirectory,
//                            nameOfGrid + ".png");
//                    ie.toGreyScaleImage(g, gp, imageFile, "png", handleOutOfMemoryErrors);
            File asciigridFile;
            asciigridFile = new File(
                    outletmapDirectory,
                    nameOfGrid + ".asc");

            eage.toAsciiFile(g, asciigridFile, handleOutOfMemoryErrors);

            FeatureLayer foregroundFeatureLayer;
            foregroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                    tAdviceLeedsPointShapefile,
                    DW_Style.createDefaultPointStyle());
            
            // Had hoped that this could be done once and then passed in 
            FeatureLayer backgroundFeatureLayer;
            backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                    backgroundShapefile,
                    backgroundStyle);

            //----------------------------------------------------------
            // outputGridToImageUsingGeoTools - this styles everything too
            outputGridToImageUsingGeoTools(
                    (double) Math.PI * cellsize * cellsize,
                    g,
                    asciigridFile,
                    outletmapDirectory,
                    foregroundFeatureLayer,
                    backgroundFeatureLayer,
                    nameOfGrid,
                    png_String,
                    imageWidth, sf, ff, showMapsInJMapPane);

            // Generalise the grid
            // Generate some geographically weighted statsitics
            List<String> stats = new ArrayList<String>();
            stats.add("WSum");
            for (int cellDistanceForGeneralisation = maxCellDistanceForGeneralisation;
                    cellDistanceForGeneralisation > 1; cellDistanceForGeneralisation /= 2) {
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
                        g,
                        stats,
                        distance,
                        weightIntersect,
                        weightFactor,
                        gf);
                Iterator<AbstractGrid2DSquareCell> itegws;
                itegws = gws.iterator();
                int i = 0;
                while (itegws.hasNext()) {
                    AbstractGrid2DSquareCell gwsgrid = itegws.next();
                    String outputName;
                    outputName = nameOfGrid + "GWS_" + cellDistanceForGeneralisation + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                    asciigridFile = new File(
                            outletmapDirectory,
                            outputName + ".asc");
                    eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);
                    i++;

                    // Had hoped that this could be done once and then passed in 
                    foregroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                    tAdviceLeedsPointShapefile,
                    DW_Style.createDefaultPointStyle());
                    
                    backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                            backgroundShapefile,
                            backgroundStyle);

                    //----------------------------------------------------------
                    // outputGridToImageUsingGeoTools - this styles everything too
                    outputGridToImageUsingGeoTools(
                            (double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                            gwsgrid,
                            asciigridFile,
                            outletmapDirectory,
                            foregroundFeatureLayer,
                            backgroundFeatureLayer,
                            outputName,
                            png_String,
                            imageWidth, sf, ff, showMapsInJMapPane);
                }
            }
        }
    }

    public static void outputGridToImageUsingGeoTools(
            double normalisation,
            AbstractGrid2DSquareCell g,
            File asciigridFile,
            File dir,
            FeatureLayer foregroundFeatureLayer,
            FeatureLayer backgroundFeatureLayer,
            String nameOfGrid,
            String png_String,
            int imageWidth,
            StyleFactory sf,
            FilterFactory2 ff,
            boolean showMapsInJMapPane) {
        //----------------------------------------------------------
        // Create GeoTools GridCoverage
        // Two ways to read the asciigridFile into a GridCoverage
        // 1.
        // Reading the coverage through a file
        ArcGridReader aArcGridReader = null;
        try {
            aArcGridReader = new ArcGridReader(asciigridFile);
        } catch (DataSourceException ex) {
            Logger.getLogger(DW_DensityMaps.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("asciigridFile.toString()" + asciigridFile.toString());
        }
//        // 2.
//        AbstractGridFormat format = GridFormatFinder.findFormat(asciigridFile);
//        GridCoverage2DReader reader = format.getReader(asciigridFile);
        GridCoverage2D gc = null;
        try {
//            gc = reader.read(null);
            gc = aArcGridReader.read(null);

        } catch (IOException ex) {
            Logger.getLogger(DW_DensityMaps.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        String outname = nameOfGrid + "GeoToolsOutput";
        //showMapsInJMapPane = true;

        Object[] styleAndLegendItems = DW_Style.getStyleAndLegendItems(
                normalisation, g, gc, "Quantile", ff, 9, "Reds", true);
        //Style style = createGreyscaleStyle(gc, null, sf, ff);

        ReferencedEnvelope re;
        re = backgroundFeatureLayer.getBounds();
        double minX = re.getMinX();
        double maxX = re.getMaxX();
        double minY = re.getMinY();
        double maxY = re.getMaxY();
        System.out.println("minX " + minX);
        System.out.println("maxX " + maxX);
        System.out.println("minY " + minY);
        System.out.println("maxY " + maxY);

        DW_GeoTools.outputToImage(
                outname,
                gc,
                foregroundFeatureLayer,
                backgroundFeatureLayer,
                styleAndLegendItems,
                dir,
                png_String,
                imageWidth,
                showMapsInJMapPane);
//        try {
//            reader.dispose();
//        } catch (IOException ex) {
//            Logger.getLogger(DW_DensityMaps.class.getName()).log(Level.SEVERE, null, ex);
//        }
        aArcGridReader.dispose();
    }

    public static TreeMap<String, ArrayList<DW_Point>> getLeedsCABDataPoints() {
        TreeMap<String, ArrayList<DW_Point>> result;
        result = new TreeMap<String, ArrayList<DW_Point>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        CAB_DataRecord2_Handler tCAB_DataRecord2_Handler;
        tCAB_DataRecord2_Handler = new CAB_DataRecord2_Handler();
        TreeMap<EnquiryClientBureauOutletID, CAB_DataRecord2> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_CAB.loadLeedsCABData(
                filename, tCAB_DataRecord2_Handler);
        int countNonMatchingPostcodes = 0;
        Iterator<EnquiryClientBureauOutletID> ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            EnquiryClientBureauOutletID key = ite.next();
            CAB_DataRecord2 value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
            DW_Point aPoint = get_ONSPDlookup().get(formattedpostcode);
            if (aPoint != null) {
                if (result.containsKey(outlet)) {
                    result.get(outlet).add(aPoint);
                } else {
                    ArrayList<DW_Point> a;
                    a = new ArrayList<DW_Point>();
                    a.add(aPoint);
                    result.put(outlet, a);
                }
            } else {
                countNonMatchingPostcodes++;
            }
        }
        System.out.println("countNonMatchingPostcodes " + countNonMatchingPostcodes);
        return result;
    }

    public static TreeMap<String, ArrayList<String>> getLeedsCABDataPostcodes() {
        TreeMap<String, ArrayList<String>> result;
        result = new TreeMap<String, ArrayList<String>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        CAB_DataRecord2_Handler tCAB_DataRecord2_Handler;
        tCAB_DataRecord2_Handler = new CAB_DataRecord2_Handler();
        TreeMap<EnquiryClientBureauOutletID, CAB_DataRecord2> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_CAB.loadLeedsCABData(
                filename, tCAB_DataRecord2_Handler);
        Iterator<EnquiryClientBureauOutletID> ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            EnquiryClientBureauOutletID key = ite.next();
            CAB_DataRecord2 value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
            if (result.containsKey(outlet)) {
                result.get(outlet).add(formattedpostcode);
            } else {
                ArrayList<String> a;
                a = new ArrayList<String>();
                a.add(formattedpostcode);
                result.put(outlet, a);
            }
        }
        return result;
    }

    public static ArrayList<DW_Point> getChapeltownCABDataPoints() {
        ArrayList<DW_Point> result;
        result = new ArrayList<DW_Point>();
        CAB_DataRecord0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new CAB_DataRecord0_Handler();
        TreeMap<String, CAB_DataRecord0> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_CAB.getChapeltownCABData(tCAB_DataRecord0_Handler);
        int countNonMatchingPostcodes = 0;
        Iterator<String> ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            CAB_DataRecord0 value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
            DW_Point aPoint = get_ONSPDlookup().get(formattedpostcode);
            if (aPoint != null) {
                result.add(aPoint);
            } else {
                countNonMatchingPostcodes++;
            }
        }
        System.out.println("countNonMatchingPostcodes " + countNonMatchingPostcodes);
        return result;
    }

    public static ArrayList<String> getChapeltownCABDataPostcodes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        CAB_DataRecord0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new CAB_DataRecord0_Handler();
        TreeMap<String, CAB_DataRecord0> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_CAB.getChapeltownCABData(tCAB_DataRecord0_Handler);
        Iterator<String> ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            CAB_DataRecord0 value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
            result.add(formattedpostcode);
        }
        return result;
    }

    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    private JMapFrame frame;
    private GridCoverage2DReader reader;

    /**
     * Prompts the user for a GeoTIFF file and a Shapefile and passes them to
     * the displayLayers method
     */
    private void getLayersAndDisplay() throws Exception {
        List<Parameter<?>> list = new ArrayList<Parameter<?>>();
        list
                .add(new Parameter<File>("image", File.class, "Image",
                                "GeoTiff or World+Image to display as basemap",
                                new KVP(Parameter.EXT, "tif", Parameter.EXT, "jpg")));
        list.add(
                new Parameter<File>("shape", File.class, "Shapefile",
                        "Shapefile contents to display", new KVP(Parameter.EXT, "shp")));

        JParameterListWizard wizard = new JParameterListWizard("Image Lab",
                "Fill in the following layers", list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
        File shapeFile = (File) wizard.getConnectionParameters().get("shape");

        displayLayers(imageFile, shapeFile);
    }

    /**
     * Displays a GeoTIFF file overlaid with a Shapefile
     *
     * @param rasterFile the GeoTIFF file
     * @param shpFile the Shapefile
     */
    private void displayLayers(File rasterFile, File shpFile) throws Exception {
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        reader = format.getReader(rasterFile);

        // Initially display the raster in greyscale using the
        // data from the first image band
        Style rasterStyle = createGreyscaleStyle(1, sf, ff);

        // Connect to the shapefile
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);
        SimpleFeatureSource shapefileSource = dataStore
                .getFeatureSource();

        // Create a basic style with outlineColour coloured lines and no fill
        Color outlineColour = Color.BLACK;//Color.YELLOW;
        Style shpStyle = SLD.createPolygonStyle(
                outlineColour,
                null,
                0.0f);

        // Set up a MapContent with the two layers
        final MapContent map = new MapContent();
        map.setTitle("ImageLab");

        Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
        map.addLayer(rasterLayer);

        Layer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
        map.addLayer(shpLayer);

        // Create a JMapFrame with a menu to choose the display style for the
        frame = new JMapFrame(map);
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        //frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN, JMapFrame.Tool.RESET);
        frame.enableToolBar(true);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Raster");
        menuBar.add(menu);

        menu.add(new SafeAction("Grayscale display") {
            public void action(ActionEvent e) throws Throwable {
                Style style = createGreyscaleStyle();
                if (style != null) {
                    ((StyleLayer) map.layers().get(0)).setStyle(style);
                    frame.repaint();
                }
            }
        });

        menu.add(new SafeAction("RGB display") {
            public void action(ActionEvent e) throws Throwable {
                Style style = createRGBStyle();
                if (style != null) {
                    ((StyleLayer) map.layers().get(0)).setStyle(style);
                    frame.repaint();
                }
            }
        });
        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);
    }

    /**
     * Create a Style to display a selected band of the GeoTIFF image as a
     * greyscale layer
     *
     * @return a new Style instance to render the image in greyscale
     */
    private Style createGreyscaleStyle() {
        GridCoverage2D cov = null;
        return createGreyscaleStyle(cov);
    }

    /**
     * Create a Style to display a selected band of the GeoTIFF image as a
     * greyscale layer
     *
     * @return a new Style instance to render the image in greyscale
     */
    private Style createGreyscaleStyle(
            GridCoverage2D cov) {
        return createGreyscaleStyle(cov, frame, sf, ff);
    }

    /**
     * Create a Style to display a selected band of the GeoTIFF image as a
     * greyscale layer
     *
     * @return a new Style instance to render the image in greyscale
     */
    private static Style createGreyscaleStyle(
            GridCoverage2D cov,
            JMapFrame frame,
            StyleFactory sf,
            FilterFactory2 ff) {
        int numBands = cov.getNumSampleDimensions();
        if (numBands == 1) {
            return createGreyscaleStyle(1, sf, ff);
        } else {
            Integer[] bandNumbers = new Integer[numBands];
            for (int i = 0; i < numBands; i++) {
                bandNumbers[i] = i + 1;
            }
            Object selection = JOptionPane.showInputDialog(
                    frame,
                    "Band to use for greyscale display",
                    "Select an image band",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    bandNumbers,
                    1);
            if (selection != null) {
                int band = ((Number) selection).intValue();
                return createGreyscaleStyle(band, sf, ff);
            }
        }
        return null;
    }

    /**
     * Create a Style to display the specified band of the GeoTIFF image as a
     * greyscale layer.
     * <p>
     * This method is a helper for createGreyScale() and is also called directly
     * by the displayLayers() method when the application first starts.
     *
     * @param band the image band to use for the greyscale display
     *
     * @return a new Style instance to render the image in greyscale
     */
    private static Style createGreyscaleStyle(
            int band,
            StyleFactory sf,
            FilterFactory2 ff) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    /**
     * This method examines the names of the sample dimensions in the provided
     * coverage looking for "red...", "green..." and "blue..." (case insensitive
     * match). If these names are not found it uses bands 1, 2, and 3 for the
     * red, green and blue channels. It then sets up a raster symbolizer and
     * returns this wrapped in a Style.
     *
     * @return a new Style object containing a raster symbolizer set up for RGB
     * image
     */
    private Style createRGBStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = {-1, -1, -1};
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

}
