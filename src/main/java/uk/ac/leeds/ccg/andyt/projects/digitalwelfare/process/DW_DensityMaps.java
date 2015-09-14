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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletID;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor.getLookupFromPostcodeToLevelCode;

/**
 *
 * @author geoagdt
 */
public class DW_DensityMaps extends DW_Maps {

    protected Grids_Environment ge;
    protected ESRIAsciiGridExporter eage;
    protected ImageExporter ie;
    protected Grid2DSquareCellProcessorGWS gp;
    protected Grid2DSquareCellDoubleFactory gf;
    protected AbstractGrid2DSquareCellDoubleChunkFactory gcf;
    protected long nrows;
    protected long ncols;
    protected int chunkNRows;
    protected int chunkNCols;
    protected double cellsize;
    protected BigDecimal[] dimensions;
    protected long xllcorner;
    protected long yllcorner;
    protected TreeMap<String, String> tLookupFromPostcodeToCensusCodes;

    protected int maxCellDistanceForGeneralisation;

    //protected boolean outputESRIAsciigrids;
    protected boolean handleOutOfMemoryErrors;

    public DW_DensityMaps() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_DensityMaps().run();
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
        // If showMapsInJMapPane is true, the maps are presented in individual 
        // JMapPanes
        //showMapsInJMapPane = false;
        showMapsInJMapPane = true;
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
        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        styleParameters.setForegroundStyle0(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyle0(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");

        mapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "density");
        imageWidth = 1000;

        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();

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

        // Initialise tDW_ID_ClientTypes
        ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes;
        tDW_ID_ClientTypes = new ArrayList<DW_ID_ClientID>();
        tDW_ID_ClientTypes.add(new DW_ID_ClientID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletEnquiryID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientEnquiryID());

//        Currently only equal interval implemented
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
        //cellsize = 800;//400;//200;//50;//100;//50; //100;
        //maxCellDistanceForGeneralisation = 2;//4;//8;//16;//32; // add a min too for top up runs.
        for (maxCellDistanceForGeneralisation = 4; maxCellDistanceForGeneralisation <= 32; maxCellDistanceForGeneralisation *= 2) {
            cellsize = 1600 / maxCellDistanceForGeneralisation;
            runAll(tDW_ID_ClientTypes);
        }
    }

    public void runAll(ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes) {
        Iterator<DW_ID_ClientID> ite;
        ite = tDW_ID_ClientTypes.iterator();
        while (ite.hasNext()) {
            Object IDType;
            IDType = ite.next();
            // General parameters
            // Property for selecting
            String targetPropertyName = "LSOA11CD";
            level = "LSOA";
            // Get LSOA Codes LSOA Shapefile and Leeds LSOA Shapefile
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
            tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                    level, targetPropertyName, getShapefileDataStoreFactory());
            tLookupFromPostcodeToCensusCodes = getLookupFromPostcodeToLevelCode(
                    level, 2011);

            TreeMap<String, Deprivation_DataRecord> deprivationRecords;

            // Map CAB data
            boolean scaleToFirst;

            // Grids parameters
            // Filter just for specific outlets
            HashSet<String> outlets;
            outlets = new HashSet<String>();
            outlets.add("OTLEY - LS21 1BG");
            outlets = null; // Set outlets null to run for all outlets

            /*
             * (filter == 0) Clip all results to Leeds LAD
             * (filter == 1) Clip all results to Leeds And Neighbouring LADs (showing results for all Leeds LAD)
             * (filter == 2) Clip all results to Leeds And Near Neighbouring LADs (showing results for all Leeds LAD)
             * (filter == 3) No clipping
             */
            for (int filter = 0; filter < 3; filter++) {
                if (filter == 0) {
                //if (filter == 0 || filter == 2) {
                    //for (int filter = 2; filter < 3; filter++) {
                    //int filter = 0;
                    deprivationRecords = null;
                    if (commonStyling) {
                        scaleToFirst = true;
                        runCAB(deprivationRecords,
                                outlets,
                                targetPropertyName,
                                tLSOACodesAndLeedsLSOAShapefile,
                                tLookupFromPostcodeToCensusCodes,
                                filter,
                                scaleToFirst,
                                IDType,
                                handleOutOfMemoryErrors);
                    }
                    if (individualStyling) {
                        scaleToFirst = false;
                        runCAB(deprivationRecords,
                                outlets,
                                targetPropertyName,
                                tLSOACodesAndLeedsLSOAShapefile,
                                tLookupFromPostcodeToCensusCodes,
                                filter,
                                scaleToFirst,
                                IDType,
                                handleOutOfMemoryErrors);
                    }

//            // Get deprivation data
//            deprivationRecords = DW_Processor.getDeprivation_Data();
//            if (commonStyling) {
//                scaleToFirst = true;
//                runCAB(deprivationRecords,
//                        outlets,
//                        targetPropertyName,
//                        tLSOACodesAndLeedsLSOAShapefile,
//                        tLookupFromPostcodeToCensusCodes,
//                        filter,
//                        scaleToFirst,
//                        handleOutOfMemoryErrors);
//            }
//            if (individualStyling) {
//                scaleToFirst = false;
//                runCAB(deprivationRecords,
//                        outlets,
//                        targetPropertyName,
//                        tLSOACodesAndLeedsLSOAShapefile,
//                        tLookupFromPostcodeToCensusCodes,
//                        filter,
//                        scaleToFirst,
//                        handleOutOfMemoryErrors);
//            }
                }
            }
            // Tidy up
            tLSOACodesAndLeedsLSOAShapefile.dispose();
        }
    }

    public void runCAB(
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            HashSet<String> outlets,
            String targetPropertyName,
            //            String level,
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            int filter,
            boolean scaleToFirst,
            Object IDType,
            boolean handleOutOfMemoryErrors) {

        String style;
        if (scaleToFirst) {
            style = "CommonlyStyled";
        } else {
            style = "IndividuallyStyled";
        }
        File dir;
        dir = new File(
                mapDirectory,
                style + "/" + styleParameters.getClassificationFunctionName());

        mapCounts(
                tLSOACodesAndLeedsLSOAShapefile,
                tLookupFromPostcodeToCensusCodes,
                deprivationRecords,
                outlets,
                dir,
                targetPropertyName,
                filter,
                scaleToFirst,
                IDType,
                handleOutOfMemoryErrors);
    }

    /**
     * Uses a file dialog to select a file and then
     *
     * @param tLSOACodesAndLeedsLSOAShapefile
     * @param tLookupFromPostcodeToCensusCodes
     * @param outlets // May be null, but if not null then filters and only
     * produces results for outlets in the collection
     * @param deprivationRecords
     * @param dir
     * @param targetPropertyName
     * @param filter
     * @param scaleToFirst If scaleToFirst == true then all maps are shaded with
     * the styles provided by the first map.
     * @param IDType
     * @param handleOutOfMemoryErrors
     */
    public void mapCounts(
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            HashSet<String> outlets,
            File dir,
            String targetPropertyName,
            int filter,
            boolean scaleToFirst,
            Object IDType,
            boolean handleOutOfMemoryErrors) {
        int multiplier;
        multiplier = (int) (400 / cellsize);
        if (filter == 0) {
            backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
            nrows = 70 * multiplier; //139 * multiplier; //277 * multiplier;
            ncols = 135 * multiplier; //170 * multiplier; //340 * multiplier;
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
                backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADDW_Shapefile();
                nrows = 212 * multiplier;//414 * multiplier;//827 * multiplier;
                ncols = 192 * multiplier;//383 * multiplier;//765 * multiplier;
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
                backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNearNeighbouringLADDW_Shapefile();
                nrows = 212 * multiplier;//414 * multiplier;//827 * multiplier;
                ncols = 257 * multiplier;//514 * multiplier;//1027 * multiplier;
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
        foregroundDW_Shapefile1 = backgroundDW_Shapefile;

        // Get the CAB Client Data
        TreeMap<String, ArrayList<String>> tLeedsCABData;
        tLeedsCABData = getLeedsCABDataPostcodes(IDType);
        ArrayList<String> tChapeltownCABData;
        tChapeltownCABData = getChapeltownCABDataPostcodes(IDType);
        tLeedsCABData.put("Chapeltown", tChapeltownCABData);
        ArrayList<String> tLCC_WRUData;
        tLCC_WRUData = getLCC_WRUDataPostcodes(IDType);
        tLeedsCABData.put("LCC_WRU", tLCC_WRUData);
        // Initialise grid paramters
        long xurcorner = xllcorner + (long) (nrows * cellsize);
        long yurcorner = yllcorner + (long) (ncols * cellsize);
        dimensions = new BigDecimal[5];
        dimensions[0] = new BigDecimal("" + cellsize); //cellsize
        dimensions[1] = new BigDecimal("" + xllcorner); //xllcorner
        dimensions[2] = new BigDecimal("" + yllcorner); //yllcorner
        dimensions[3] = new BigDecimal("" + xurcorner); //xurcorner
        dimensions[4] = new BigDecimal("" + yurcorner); //yurcorner
        String nameOfGrid;
        nameOfGrid = "gridNrows" + Long.toString(nrows) + "Ncols" + Long.toString(ncols);
        // Grid generalisation paramters

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

                // Process for all outlets
                process(
                        mapDirectory2,
                        "all",
                        outlets,
                        nameOfGrid,
                        maxCellDistanceForGeneralisation,
                        tLeedsCABData,
                        deprivationRecords,
                        deprivationClasses,
                        deprivationClass,
                        scaleToFirst);
                // Process for each outlet in turn
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
                        process(
                                mapDirectory2,
                                outlet,
                                outlets,
                                nameOfGrid,
                                maxCellDistanceForGeneralisation,
                                tLeedsCABData,
                                deprivationRecords,
                                deprivationClasses,
                                deprivationClass,
                                scaleToFirst);
                    }
                }
            }
        } else {
            File mapDirectory2 = new File(
                    dir,
                    "all");
            mapDirectory2.mkdirs();
            // Process for all outlets
            process(
                    mapDirectory2,
                    "all",
                    outlets,
                    nameOfGrid,
                    maxCellDistanceForGeneralisation,
                    tLeedsCABData,
                    deprivationRecords,
                    null,
                    null,
                    scaleToFirst);
            // Process for each outlet in turn
            Iterator<String> ite2;
            ite2 = tLeedsCABData.keySet().iterator();
            while (ite2.hasNext()) {
                String outlet = ite2.next();
                process(
                        mapDirectory2,
                        outlet,
                        outlets,
                        nameOfGrid,
                        maxCellDistanceForGeneralisation,
                        tLeedsCABData,
                        deprivationRecords,
                        null,
                        null,
                        scaleToFirst);
            }
        }
        // Set style back to null so that it is recomputed for the next set of 
        // maps.
        // Style could be set to be returned if it is important.
        styleParameters.setStylesNull();
    }

    private void process(
            File mapDirectory2,
            String outlet,
            HashSet<String> outlets,
            String nameOfGrid,
            int maxCellDistanceForGeneralisation,
            TreeMap<String, ArrayList<String>> tLeedsCABData,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            boolean scaleToFirst) {
        boolean process = false;
        if (outlets != null) {
            if (outlets.contains(outlet) || outlet.equalsIgnoreCase("all")) {
                process = true;
            }
        } else {
            process = true;
        }
        if (process) {
            // Initialise
            File outletmapDirectory = new File(
                    mapDirectory2,
                    outlet);
            outletmapDirectory.mkdirs();
            File grid = new File(
                    outletmapDirectory,
                    nameOfGrid);
            grid.mkdirs();
            Grid2DSquareCellDouble g = initiliseGrid(grid);
            if (outlet.equalsIgnoreCase("all")) {
                // Combine for all Advice Leeds
                System.out.println("All Advice Leeds");
                // Add to grid
                Iterator<String> ite;
                if (outlets == null) {
                    ite = tLeedsCABData.keySet().iterator(); // Assume outlets is null
                } else {
                    ite = outlets.iterator();
                }
                int countNonMatchingPostcodes = 0;
                while (ite.hasNext()) {
                    String outletToAdd = ite.next();
//                    // Check not adding multiple times
//                    System.out.println("outlet added to all: " + outletToAdd);
                    ArrayList<String> postcodes;
                    postcodes = tLeedsCABData.get(outletToAdd);
                    // Add from postcodes
                    countNonMatchingPostcodes += addFromPostcodes(
                            g,
                            postcodes,
                            deprivationRecords,
                            deprivationClasses,
                            deprivationClass);
                }
                System.out.println("" + g.toString(handleOutOfMemoryErrors));
                System.out.println("" + countNonMatchingPostcodes);
            } else {
                System.out.println("Outlet " + outlet);
                // Add to grid
                ArrayList<String> postcodes;
                postcodes = tLeedsCABData.get(outlet);
                int countNonMatchingPostcodes;
                // Add from postcodes
                countNonMatchingPostcodes = addFromPostcodes(
                        g,
                        postcodes,
                        deprivationRecords,
                        deprivationClasses,
                        deprivationClass);
                System.out.println("" + g.toString(handleOutOfMemoryErrors));
                System.out.println("" + countNonMatchingPostcodes);
            }
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

            int index;

            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
            index = 0;
            outputGridToImageUsingGeoToolsAndSetCommonStyle(
                    100.0d,//1.0d,//(double) Math.PI * cellsize * cellsize,
                    g,
                    asciigridFile,
                    outletmapDirectory,
                    nameOfGrid,
                    index,
                    scaleToFirst);
            if (!scaleToFirst) {
                styleParameters.setStyle(nameOfGrid, null, index);
            }

            // Generalise the grid
            // Generate some geographically weighted statsitics
            List<String> stats = new ArrayList<String>();
            stats.add("WSum");
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
                        g,
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
                String outputName;
                outputName = nameOfGrid + "GWS_" + cellDistanceForGeneralisation;// + "_" + i;
//                        imageFile = new File(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

                asciigridFile = new File(
                        outletmapDirectory,
                        outputName + ".asc");
                eage.toAsciiFile(gwsgrid, asciigridFile, handleOutOfMemoryErrors);

                // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
                outputGridToImageUsingGeoToolsAndSetCommonStyle(
                        100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                        gwsgrid,
                        asciigridFile,
                        outletmapDirectory,
                        outputName,
                        index,
                        scaleToFirst);
                if (!scaleToFirst) {
                    styleParameters.setStyle(nameOfGrid, null, index);
                }
            }
        }
    }

    // Add from postcodes
    private int addFromPostcodes(
            Grid2DSquareCellDouble g,
            ArrayList<String> postcodes,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass) {
        int countNonMatchingPostcodes = 0;
        Iterator<String> itep = postcodes.iterator();
        while (itep.hasNext()) {
            String postcode = itep.next();
            String aLSOACode;
            aLSOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
            if (aLSOACode != null) {
                if (deprivationRecords == null) {
                    String postcodeLevel;
                    postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(postcode);
                    AGDT_Point aPoint = getONSPDlookups().get(postcodeLevel).get(postcode);
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
                            String postcodeLevel;
                            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(postcode);
                            AGDT_Point aPoint = getONSPDlookups().get(postcodeLevel).get(postcode);
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
        return countNonMatchingPostcodes;
    }

    /**
     * Initialise and return a grid
     *
     * @param dir
     * @return
     */
    public Grid2DSquareCellDouble initiliseGrid(File dir) {
        Grid2DSquareCellDouble result = (Grid2DSquareCellDouble) gf.create(
                new GridStatistics0(),
                dir,
                gcf,
                nrows,
                ncols,
                dimensions,
                ge,
                handleOutOfMemoryErrors);
        //System.out.println("" + result.toString(handleOutOfMemoryErrors));
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                result.setCell(row, col, 0, handleOutOfMemoryErrors); // set all values to 0;
            }
        }
        return result;
    }

    public void outputGridToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation,
            AbstractGrid2DSquareCell g,
            File asciigridFile,
            File dir,
            String nameOfGrid,
            int styleIndex,
            boolean scaleToFirst) {
        //----------------------------------------------------------
        // Create GeoTools GridCoverage
        // Two ways to read the asciigridFile into a GridCoverage
        // 1.
        // Reading the coverage through a file
        ArcGridReader agr;
        agr = getArcGridReader(asciigridFile);
//        // 2.
//        AbstractGridFormat format = GridFormatFinder.findFormat(asciigridFile);
//        GridCoverage2DReader reader = format.getReader(asciigridFile);
        GridCoverage2D gc;
        gc = getGridCoverage2D(agr);

        String outname = nameOfGrid + "GeoToolsOutput";
        //showMapsInJMapPane = true;

//        Object[] styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
//                normalisation, g, gc, "Quantile", ff, 9, "Reds", true);
        //Style style = createGreyscaleStyle(gc, null, sf, ff);
        ReferencedEnvelope re;
        re = backgroundDW_Shapefile.getFeatureLayer().getBounds();
        double minX = re.getMinX();
        double maxX = re.getMaxX();
        double minY = re.getMinY();
        double maxY = re.getMaxY();
        System.out.println("minX " + minX);
        System.out.println("maxX " + maxX);
        System.out.println("minY " + minY);
        System.out.println("maxY " + maxY);

        DW_Geotools.outputToImageUsingGeoToolsAndSetCommonStyle(
                normalisation,
                styleParameters,
                styleIndex,
                outname,
                g,
                gc,
                foregroundDW_Shapefile0,
                foregroundDW_Shapefile1,
                backgroundDW_Shapefile,
                dir,
                imageWidth,
                showMapsInJMapPane,
                scaleToFirst);
//        try {
//            reader.dispose();
//        } catch (IOException ex) {
//            Logger.getLogger(DW_DensityMaps.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (agr != null) {
            agr.dispose();
        }
    }

    public static TreeMap<String, ArrayList<AGDT_Point>> getLeedsCABDataPoints(
            Object IDType) {
        TreeMap<String, ArrayList<AGDT_Point>> result;
        result = new TreeMap<String, ArrayList<AGDT_Point>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        DW_Data_CAB2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new DW_Data_CAB2_Handler();
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_AdviceLeeds.loadLeedsCABData(
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        int countNonMatchingPostcodes = 0;
        Iterator ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            Object key = ite.next();
            DW_Data_CAB2_Record value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
            String postcodeLevel;
            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(formattedpostcode);
            AGDT_Point aPoint = getONSPDlookups().get(postcodeLevel).get(formattedpostcode);
            if (aPoint != null) {
                if (result.containsKey(outlet)) {
                    result.get(outlet).add(aPoint);
                } else {
                    ArrayList<AGDT_Point> a;
                    a = new ArrayList<AGDT_Point>();
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

    public static TreeMap<String, ArrayList<String>> getLeedsCABDataPostcodes(
            Object IDType) {
        TreeMap<String, ArrayList<String>> result;
        result = new TreeMap<String, ArrayList<String>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        DW_Data_CAB2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new DW_Data_CAB2_Handler();
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_AdviceLeeds.loadLeedsCABData(
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        Iterator ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            Object key = ite.next();
            DW_Data_CAB2_Record value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
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

    public static ArrayList<AGDT_Point> getChapeltownCABDataPoints(
            Object IDType) {
        ArrayList<AGDT_Point> result;
        result = new ArrayList<AGDT_Point>();
        DW_Data_CAB0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new DW_Data_CAB0_Handler();
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_AdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);
        int countNonMatchingPostcodes = 0;
        Iterator ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            Object key = ite.next();
            DW_Data_CAB0_Record value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
            String postcodeLevel;
            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(formattedpostcode);
            AGDT_Point aPoint = getONSPDlookups().get(postcodeLevel).get(formattedpostcode);
            if (aPoint != null) {
                result.add(aPoint);
            } else {
                countNonMatchingPostcodes++;
            }
        }
        System.out.println("countNonMatchingPostcodes " + countNonMatchingPostcodes);
        return result;
    }

    public static ArrayList<String> getLCC_WRUDataPostcodes(
            Object IDType) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        DW_Data_LCC_WRU_Handler h;
        h = new DW_Data_LCC_WRU_Handler();
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> data;
        data = DW_DataProcessor_AdviceLeeds.getLCC_WRUData(
                h,
                IDType);
        Iterator<DW_ID_ClientID> ite;
        ite = data.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_LCC_WRU_Record value = data.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
            result.add(formattedpostcode);
        }
        return result;
    }

    public static ArrayList<String> getChapeltownCABDataPostcodes(
            Object IDType) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        DW_Data_CAB0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new DW_Data_CAB0_Handler();
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_AdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);
        Iterator<DW_ID_ClientID> ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_CAB0_Record value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
            result.add(formattedpostcode);
        }
        return result;
    }

    private final StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

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
