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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorGWS;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_DensityMapsAbstract;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_MapsAdviceLeeds;

/**
 *
 * @author geoagdt
 */
public class DW_DensityMapsAdviceLeeds extends DW_DensityMapsAbstract {

    DW_MapsAdviceLeeds DW_MapsAdviceLeeds;
    DW_ProcessorAdviceLeeds DW_ProcessorAdviceLeeds;
            
    public DW_DensityMapsAdviceLeeds(DW_Environment env) {
        super(env);
        DW_ProcessorAdviceLeeds = new DW_ProcessorAdviceLeeds(env);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_DensityMapsAdviceLeeds(null).run();
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
     * @throws java.lang.Exception
     */
    public void run() throws Exception, Error {
        // If showMapsInJMapPane is true, the maps are presented in individual 
        // JMapPanes
        //showMapsInJMapPane = false;
//        DW_Maps.setShowMapsInJMapPane(true);
//        //outputESRIAsciigrids = false;
//        DW_Maps.setImageWidth(1000);
        DW_YM3 YM3;
        YM3 = new DW_YM3(2011,5);
        DW_Maps.initONSPDLookups();
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
        AGDT_StyleParameters styleParameters = new AGDT_StyleParameters();
        styleParameters.setnClasses(9);
        styleParameters.setPaletteName("Reds");
        styleParameters.setAddWhiteForZero(true);
        styleParameters.setForegroundStyleName(0,"Foreground Style 0");
//        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
        DW_MapsAdviceLeeds.setStyleParameters(styleParameters);
        
        File mapDirectory;
        mapDirectory = new File(
                DW_Files.getOutputAdviceLeedsMapsDir(),
                "density");
//        DW_MapsAdviceLeeds.setMapDirectory(mapDirectory);
//        DW_MapsAdviceLeeds.setImageWidth(1000);
//
//        DW_MapsAdviceLeeds.setForegroundDW_Shapefile0(DW_MapsAdviceLeeds.getAdviceLeedsPointDW_Shapefiles());

        /*Grid Parameters
         *_____________________________________________________________________
         */
        handleOutOfMemoryErrors = true;
        File processorDir = new File(
                mapDirectory,
                "processor");
        processorDir.mkdirs();
        ge = new Grids_Environment();
        eage = new Grids_ESRIAsciiGridExporter(ge);
        ie = new Grids_ImageExporter(ge);
        gp = new Grid2DSquareCellProcessorGWS(ge);
        gp.set_Directory(processorDir, false, handleOutOfMemoryErrors);
        gcf = new Grids_Grid2DSquareCellDoubleChunkArrayFactory();
        chunkNRows = 300;//250; //64
        chunkNCols = 350;//300; //64
        gf = new Grids_Grid2DSquareCellDoubleFactory(
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
//        DW_MapsAdviceLeeds.setIndividualStyling(true);
        //cellsize = 800;//400;//200;//50;//100;//50; //100;
        //maxCellDistanceForGeneralisation = 2;//4;//8;//16;//32; // add a min too for top up runs.
        for (maxCellDistanceForGeneralisation = 4; maxCellDistanceForGeneralisation <= 32; maxCellDistanceForGeneralisation *= 2) {
            cellsize = 1600 / maxCellDistanceForGeneralisation;
            runAll(tDW_ID_ClientTypes, YM3);
        }
    }

    public void runAll(ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes, DW_YM3 YM3) throws Exception, Error {
        boolean commonStyling = true;
        boolean individualStyling = true;
        
        Iterator<DW_ID_ClientID> ite;
        ite = tDW_ID_ClientTypes.iterator();
        while (ite.hasNext()) {
            Object IDType;
            IDType = ite.next();
            // General parameters
            // Property for selecting
            String targetPropertyName = "LSOA11CD";
            String level;
            level = "LSOA";
            int     CensusYear = 2011;
            //DW_MapsAdviceLeeds.setLevel(level);
            // Get LSOA Codes LSOA Shapefile and Leeds LSOA Shapefile
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
            tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                    env,
                level, 
                    targetPropertyName,
                    DW_MapsAdviceLeeds.getShapefileDataStoreFactory());
            tLookupFromPostcodeToCensusCodes = DW_ProcessorAdviceLeeds.getClaimPostcodeF_To_LevelCode_Map(
                    env,                
                    level,
                    CensusYear,
                    YM3);
            
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
//            deprivationRecords = DW_ProcessorAbstract.getDeprivation_Data();
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
            boolean handleOutOfMemoryErrors) throws Exception, Error {

        String style;
        if (scaleToFirst) {
            style = "CommonlyStyled";
        } else {
            style = "IndividuallyStyled";
        }
        File dir;
        dir = new File(
                //DW_MapsAdviceLeeds.getMapDirectory(),
                style + "/" + DW_MapsAdviceLeeds.getStyleParameters().getClassificationFunctionName());

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
//            DW_MapsAdviceLeeds.setBackgroundDW_Shapefile(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
            nrows = 70 * multiplier; //139 * multiplier; //277 * multiplier;
            ncols = 85 * multiplier; //170 * multiplier; //340 * multiplier;
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
//                DW_MapsAdviceLeeds.setBackgroundDW_Shapefile(tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNeighbouringLADDW_Shapefile());
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
//                DW_MapsAdviceLeeds.setBackgroundDW_Shapefile(tLSOACodesAndLeedsLSOAShapefile.getLeedsAndNearNeighbouringLADDW_Shapefile());
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
//        DW_MapsAdviceLeeds.setForegroundDW_Shapefile1(DW_MapsAdviceLeeds.getBackgroundDW_Shapefile());

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
        DW_MapsAdviceLeeds.getStyleParameters().setStylesNull();
    }

    protected void process(
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
        DW_YM3 yM3;
        yM3 = DW_Postcode_Handler.getDefaultYM3();
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
            Grids_Grid2DSquareCellDouble g = initiliseGrid(grid);
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
                            deprivationClass,
                            yM3);
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
                        deprivationClass,
                        yM3);
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
                DW_MapsAdviceLeeds.getStyleParameters().setStyle(nameOfGrid, null, index);
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
                        g,
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
                    DW_MapsAdviceLeeds.getStyleParameters().setStyle(nameOfGrid, null, index);
                }
            }
        }
    }

    public TreeMap<String, ArrayList<AGDT_Point>> getLeedsCABDataPoints(
            Object IDType) {
        TreeMap<String, ArrayList<AGDT_Point>> result;
        result = new TreeMap<String, ArrayList<AGDT_Point>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        DW_Data_CAB2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new DW_Data_CAB2_Handler(env);
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessorAdviceLeeds.loadLeedsCABData(
                env,
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        int countNonMatchingPostcodes = 0;
        Iterator<DW_ID_ClientID> ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_CAB2_Record value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Postcode_Handler.formatPostcode(postcode);
            String postcodeLevel;
            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(formattedpostcode);
            AGDT_Point aPoint = DW_MapsAdviceLeeds.getONSPDlookups(env).get(postcodeLevel).get(DW_Postcode_Handler.getDefaultYM3()).get(formattedpostcode);
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

    public TreeMap<String, ArrayList<String>> getLeedsCABDataPostcodes(
            Object IDType) {
        TreeMap<String, ArrayList<String>> result;
        result = new TreeMap<String, ArrayList<String>>();
        String filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        DW_Data_CAB2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new DW_Data_CAB2_Handler(env);
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessorAdviceLeeds.loadLeedsCABData(
                env,
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        Iterator<DW_ID_ClientID> ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_CAB2_Record value = tLeedsCABData.get(key);
            String postcode = value.getPostcode();
            String outlet = value.getOutlet();
            String formattedpostcode = DW_Postcode_Handler.formatPostcode(postcode);
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

    public ArrayList<AGDT_Point> getChapeltownCABDataPoints(
            Object IDType) {
        ArrayList<AGDT_Point> result;
        result = new ArrayList<AGDT_Point>();
        DW_Data_CAB0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new DW_Data_CAB0_Handler(env);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessorAdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);
        int countNonMatchingPostcodes = 0;
        Iterator<DW_ID_ClientID> ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_CAB0_Record value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcode(postcode);
            String postcodeLevel;
            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(formattedpostcode);
            AGDT_Point aPoint = DW_MapsAdviceLeeds.getONSPDlookups(env).get(postcodeLevel).get(DW_Postcode_Handler.getDefaultYM3()).get(formattedpostcode);
            if (aPoint != null) {
                result.add(aPoint);
            } else {
                countNonMatchingPostcodes++;
            }
        }
        System.out.println("countNonMatchingPostcodes " + countNonMatchingPostcodes);
        return result;
    }

    public ArrayList<String> getLCC_WRUDataPostcodes(
            Object IDType) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        DW_Data_LCC_WRU_Handler h;
        h = new DW_Data_LCC_WRU_Handler(env);
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> data;
        data = DW_DataProcessorAdviceLeeds.getLCC_WRUData(
                h,
                IDType);
        Iterator<DW_ID_ClientID> ite;
        ite = data.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_LCC_WRU_Record value = data.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcode(postcode);
            result.add(formattedpostcode);
        }
        return result;
    }

    public ArrayList<String> getChapeltownCABDataPostcodes(
            Object IDType) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        DW_Data_CAB0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new DW_Data_CAB0_Handler(env);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessorAdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);
        Iterator<DW_ID_ClientID> ite;
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID key = ite.next();
            DW_Data_CAB0_Record value = tChapeltownCABData.get(key);
            String postcode = value.getPostcode();
            String formattedpostcode = DW_Postcode_Handler.formatPostcode(postcode);
            result.add(formattedpostcode);
        }
        return result;
    }
}
