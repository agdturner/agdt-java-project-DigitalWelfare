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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.awt.Color;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;

/**
 *
 * @author geoagdt
 */
public class DW_LineMaps_AdviceLeeds extends DW_Maps {

    private static final String targetPropertyNameMSOA = "MSOA11CD";
    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private TreeMap<String, String> tLookupFromPostcodeToLSOACensusCodes;
    private TreeMap<String, String> tLookupFromPostcodeToMSOACensusCodes;
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    private TreeMap<String, Point> aLSOAToCentroidLookupTable;
    private TreeMap<String, Point> aMSOAToCentroidLookupTable;
    private TreeMap<String, String> tCABOutletPostcodes;
    private TreeMap<String, AGDT_Point> tCABOutletPoints;
    private DW_Data_CAB2_Handler aCAB_DataRecord2_Handler;
    private DW_Data_CAB0_Handler tCAB_DataRecord0_Handler;
    private DW_Data_LCC_WRU_Handler tLCC_WRU_DataRecord_Handler;

    public DW_LineMaps_AdviceLeeds(DW_Environment env) {
        super(env);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_LineMaps_AdviceLeeds(null).run();
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

    public void run() throws Exception {
        String outname;
        // If showMapsInJMapPane == true then resulting maps are displayed on 
        // screen in a JMapPane otherwise maps are only written to file.
        showMapsInJMapPane = false;
        imageWidth = 750;
        init();

        Object IDType;
        IDType = new DW_ID_ClientOutletID();
        //IDType = new DW_ID_ClientOutletEnquiryID();

        // postcodeToOutletMaps run
        //showMapsInJMapPane = false;
        outname = "postcodeToOutletMaps";
        postcodeToOutletMaps(outname, IDType);

        // postcodeToLSOAToOutletMaps run
        //showMapsInJMapPane = false;
        outname = "postcodeToLSOAToOutletMaps";
        postcodeToLSOAToOutletMaps(outname, IDType);

        // postcodeToLSOAToMSOAToOutletMaps run
        //showMapsInJMapPane = false;
        outname = "postcodeToLSOAToMSOAOutletMaps";
        postcodeToLSOAToMSOAToOutletMaps(outname, IDType);

        // Tidy up
        if (!showMapsInJMapPane) {
            tLSOACodesAndLeedsLSOAShapefile.dispose();
            tMSOACodesAndLeedsMSOAShapefile.dispose();
        }
    }

    private void init() {
        level = "MSOA";
        initStyleParameters();
        mapDirectory = tDW_Files.getOutputAdviceLeedsMapsDir();
        // init tLSOACodesAndLeedsLSOAShapefile
        initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        // init tMSOACodesAndLeedsMSOAShapefile
        initMSOACodesAndLeedsMSOAShapefile(targetPropertyNameMSOA);
        initONSPDLookups(env);
        initCABOutletPoints();
        aCAB_DataRecord2_Handler = new DW_Data_CAB2_Handler();
        tCAB_DataRecord0_Handler = new DW_Data_CAB0_Handler();
        tLCC_WRU_DataRecord_Handler = new DW_Data_LCC_WRU_Handler();
        aLSOAToCentroidLookupTable = getCentroidLookupTable(
                "LSOA",
                targetPropertyNameLSOA);
        aMSOAToCentroidLookupTable = getCentroidLookupTable(
                "MSOA",
                targetPropertyNameMSOA);
    }

    private void initStyleParameters() {
        styleParameters = new AGDT_StyleParameters();
//        styleParameters.setnClasses(9);
//        styleParameters.setPaletteName("Reds");
//        styleParameters.setAddWhiteForZero(true);
        styleParameters.setForegroundStyleName(0, "Foreground Style 0");
//        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
                Color.GREEN,
                Color.WHITE));
        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
        styleParameters.setBackgroundStyle(DW_Style.createDefaultPolygonStyle(
                Color.BLACK,
                Color.WHITE));
        styleParameters.setBackgroundStyleTitle("Background Style");
    }

    private void initLSOACodesAndLeedsLSOAShapefile(
            String targetPropertyNameLSOA) {
        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                env,
                "LSOA",
                targetPropertyNameLSOA,
                getShapefileDataStoreFactory());
    }

    private void initMSOACodesAndLeedsMSOAShapefile(
            String targetPropertyNameMSOA) {
        tMSOACodesAndLeedsMSOAShapefile = new DW_AreaCodesAndShapefiles(
                env,
                "MSOA",
                targetPropertyNameMSOA,
                getShapefileDataStoreFactory());
    }

    private void initCABOutletPoints() {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        tCABOutletPostcodes = DW_AbstractProcessor.getOutletsAndPostcodes();
        tCABOutletPoints = new TreeMap<String, AGDT_Point>();
        Iterator<String> ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String outletName = ite_String.next();
            String postcode = tCABOutletPostcodes.get(outletName);
            AGDT_Point p = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (p == null) {
                System.out.println("No point for postcode " + postcode);
            } else {
                tCABOutletPoints.put(outletName, p);
                System.out.println(outletName + " " + p);
            }
        }
    }

    public void postcodeToLSOAToMSOAToOutletMaps(
            String outname,
            Object IDType) throws Exception {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
        int year_int = 2011;
        // init postcode to LSOA lookup
        tLookupFromPostcodeToLSOACensusCodes = DW_AbstractProcessor.getLookupFromPostcodeToLevelCode(
                env,
                "LSOA",
                year_int);
        // init postcode to MSOA lookup
        tLookupFromPostcodeToMSOACensusCodes = DW_AbstractProcessor.getLookupFromPostcodeToLevelCode(
                env,
                "MSOA",
                year_int);
        // Other variables for selecting and output
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        spiderMapDirectory.mkdirs();
        String filename;
        String year;
        // year 2012-2013
        year = "1213";
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_AdviceLeeds.loadLeedsCABData(
                env,
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        // Load Chapeltown CAB data
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_AdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);

        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();
        Iterator<String> ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            tsfcs.put(tCABOutlet, new TreeSetFeatureCollection());
            sfbs.put(tCABOutlet, new SimpleFeatureBuilder(aLineSFT));
        }

//        // Initialise postcodeCounts
//        // These are for counting how many clients there are coming from each 
//        // postcode to each CAB outlet
//        TreeMap<String, TreeMap<String, Integer>> postcodeCounts;
//        postcodeCounts = new TreeMap<String, TreeMap<String, Integer>>();
//        ite_String = tCABOutletPostcodes.keySet().iterator();
//        while (ite_String.hasNext()) {
//            String tCABOutlet = ite_String.next();
//            TreeMap<String, Integer> postcodeCount;
//            postcodeCount = new TreeMap<String, Integer>();
//            postcodeCounts.put(tCABOutlet, postcodeCount);
//        }
//        
//        // Initialise LSOACounts
//        // These are for counting how many clients there are coming from each 
//        // LSOA to each CAB outlet
//        TreeMap<String, TreeMap<String, Integer>> LSOACounts;
//        LSOACounts = new TreeMap<String, TreeMap<String, Integer>>();
//        ite_String = tCABOutletPostcodes.keySet().iterator();
//        while (ite_String.hasNext()) {
//            String tCABOutlet = ite_String.next();
//            TreeMap<String, Integer> LSOACount;
//            LSOACount = new TreeMap<String, Integer>();
//            LSOACounts.put(tCABOutlet, LSOACount);
//        }
//        
//        // Initialise MSOACounts
//        // These are for counting how many clients there are coming from each 
//        // MSOA to each CAB outlet
//        TreeMap<String, TreeMap<String, Integer>> MSOACounts;
//        MSOACounts = new TreeMap<String, TreeMap<String, Integer>>();
//        ite_String = tCABOutletPostcodes.keySet().iterator();
//        while (ite_String.hasNext()) {
//            String tCABOutlet = ite_String.next();
//            TreeMap<String, Integer> MSOACount;
//            MSOACount = new TreeMap<String, Integer>();
//            LSOACounts.put(tCABOutlet, MSOACount);
//        }
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        GeometryFactory geometryFactory = new GeometryFactory();

        int countPostcodesWithPoints;
        int countPostcodesWithPointsWithoutCensusCodes;
        int countPostcodesWithPointsAndOutlets;
        int countPostcodesWithoutPoints;

        // Leeds CAB
        System.out.println("Leeds CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        //String targetCABOutlet = "Pudsey";
        Iterator ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            Object key = ite2.next();
            DW_Data_CAB2_Record r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
//                    TreeMap<String, Integer> postcodeCount;
//                    postcodeCount = postcodeCounts.get(tCABOutletString);
//                    TreeMap<String, Integer> MSOACount;
//                    MSOACount = MSOACounts.get(tCABOutletString);
//                    TreeMap<String, Integer> LSOACount;
//                    LSOACount = LSOACounts.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
//                    AGDT_Point a_DW_Point;
//                    a_DW_Point = outletPoint;
//                    destinationx = a_DW_Point.getX();
//                    destinationy = a_DW_Point.getY();
                    String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
                    String aLSOACode = tLookupFromPostcodeToLSOACensusCodes.get(formattedPostcode);
                    String aMSOACode = tLookupFromPostcodeToMSOACensusCodes.get(formattedPostcode);
                    if (aLSOACode.equalsIgnoreCase("") || aMSOACode.equalsIgnoreCase("")) {
                        System.out.println("LSOACode, " + aLSOACode + ", postcode " + formattedPostcode);
                        System.out.println("MSOACode, " + aMSOACode + ", postcode " + formattedPostcode);
                        countPostcodesWithPointsWithoutCensusCodes++;
                    } else {
//                        // Add to counts
//                        Generic_Collections.addToTreeMapStringInteger(
//                                postcodeCount, postcode, 1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                LSOACount, aLSOACode, 1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                MSOACount, aMSOACode, 1);
//                        // Get current counts
//                        int aPostcodeCount = postcodeCount.get(postcode);
//                        int aLSOACount = LSOACount.get(aLSOACode);
//                        int aMSOACount = MSOACount.get(aMSOACode);

                        // Get Census geography points
                        Point aLSOAPoint = aLSOAToCentroidLookupTable.get(aLSOACode);
                        Point aMSOAPoint = aMSOAToCentroidLookupTable.get(aMSOACode);
                        Coordinate[] coords;
                        LineString line;
                        SimpleFeature feature;
                        // Add line from postcode to LSOA
                        destinationx = aLSOAPoint.getX();
                        destinationy = aLSOAPoint.getY();
                        //String name = "";
                        //int number = 1;
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aPostcodeCount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

//                        // Add line from LSOA to outlet
//                        originx = aLSOAPoint.getX();
//                        originy = aLSOAPoint.getY();
//                        destinationx = outletPoint.getX();
//                        destinationy = outletPoint.getY();
//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        line = geometryFactory.createLineString(coords);
//                        sfb.add(line);
//                        feature = sfb.buildFeature(null);
//                        tsfc.add(feature);
                        // Add line from LSOA to MSOA
                        originx = aLSOAPoint.getX();
                        originy = aLSOAPoint.getY();
                        destinationx = aMSOAPoint.getX();
                        destinationy = aMSOAPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aLSOACount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        // Add line from MSOA to outlet
                        originx = aMSOAPoint.getX();
                        originy = aMSOAPoint.getY();
                        destinationx = outletPoint.getX();
                        destinationy = outletPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aMSOACount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);
                        countPostcodesWithPointsAndOutlets++;
                    }
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        // Chapeltown CAB
        System.out.println("Chapeltown CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        Iterator<DW_ID_ClientID> ite2_String;
        ite2_String = tChapeltownCABData.keySet().iterator();
        while (ite2_String.hasNext()) {
            DW_ID_ClientID key = ite2_String.next();
            DW_Data_CAB0_Record r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
//                    TreeMap<String, Integer> postcodeCount;
//                    postcodeCount = postcodeCounts.get(tCABOutletString);
//                    TreeMap<String, Integer> MSOACount;
//                    MSOACount = MSOACounts.get(tCABOutletString);
//                    TreeMap<String, Integer> LSOACount;
//                    LSOACount = LSOACounts.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
//                    AGDT_Point a_DW_Point;
//                    a_DW_Point = outletPoint;
//                    destinationx = a_DW_Point.getX();
//                    destinationy = a_DW_Point.getY();
                    String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
                    String aLSOACode = tLookupFromPostcodeToLSOACensusCodes.get(formattedPostcode);
                    String aMSOACode = tLookupFromPostcodeToMSOACensusCodes.get(formattedPostcode);
                    if (aLSOACode.equalsIgnoreCase("") || aMSOACode.equalsIgnoreCase("")) {
                        System.out.println("LSOACode, " + aLSOACode + ", postcode " + formattedPostcode);
                        System.out.println("MSOACode, " + aMSOACode + ", postcode " + formattedPostcode);
                        countPostcodesWithPointsWithoutCensusCodes++;
                    } else {
//                        // Add to counts
//                        Generic_Collections.addToTreeMapStringInteger(
//                                postcodeCount, postcode, 1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                LSOACount, aLSOACode, 1);
//                        Generic_Collections.addToTreeMapStringInteger(
//                                MSOACount, aMSOACode, 1);
//                        // Get current counts
//                        int aPostcodeCount = postcodeCount.get(postcode);
//                        int aLSOACount = LSOACount.get(aLSOACode);
//                        int aMSOACount = MSOACount.get(aMSOACode);

                        // Get Census geography points
                        Point aLSOAPoint = aLSOAToCentroidLookupTable.get(aLSOACode);
                        Point aMSOAPoint = aMSOAToCentroidLookupTable.get(aMSOACode);
                        Coordinate[] coords;
                        LineString line;
                        SimpleFeature feature;
                        // Add line from postcode to LSOA
                        destinationx = aLSOAPoint.getX();
                        destinationy = aLSOAPoint.getY();
                        //String name = "";
                        //int number = 1;
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aPostcodeCount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

//                        // Add line from LSOA to outlet
//                        originx = aLSOAPoint.getX();
//                        originy = aLSOAPoint.getY();
//                        destinationx = outletPoint.getX();
//                        destinationy = outletPoint.getY();
//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        line = geometryFactory.createLineString(coords);
//                        sfb.add(line);
//                        feature = sfb.buildFeature(null);
//                        tsfc.add(feature);
                        // Add line from LSOA to MSOA
                        originx = aLSOAPoint.getX();
                        originy = aLSOAPoint.getY();
                        destinationx = aMSOAPoint.getX();
                        destinationy = aMSOAPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aLSOACount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        // Add line from MSOA to outlet
                        originx = aMSOAPoint.getX();
                        originy = aMSOAPoint.getY();
                        destinationx = outletPoint.getX();
                        destinationy = outletPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aMSOACount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);
                        countPostcodesWithPointsAndOutlets++;
                    }
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            TreeSetFeatureCollection tsfc = tsfcs.get(tCABOutlet);
            File outputShapefile = AGDT_Geotools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_Shapefile.transact(
                    outputShapefile,
                    aLineSFT,
                    tsfc,
                    getShapefileDataStoreFactory());

//            // @TODO Hoped to get away with doing this once.
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
            DW_Geotools.outputToImage(
                    tCABOutlet,
                    outputShapefile,
                    foregroundDW_Shapefile0,
                    foregroundDW_Shapefile1,
                    backgroundDW_Shapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    0,
                    Double.POSITIVE_INFINITY,
                    showMapsInJMapPane);

        }
        // Tidy up
        tLSOACodesAndLeedsLSOAShapefile.dispose();
        tMSOACodesAndLeedsMSOAShapefile.dispose();
    }

    public void postcodeToLSOAToOutletMaps(
            String outname,
            Object IDType) throws Exception {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
        int year_int = 2011;
        // Get postcode to LSOA lookup
        tLookupFromPostcodeToLSOACensusCodes = DW_AbstractProcessor.getLookupFromPostcodeToLevelCode(
                env,
                "LSOA",
                year_int);
        // Get postcode to LSOA lookup
        tLookupFromPostcodeToMSOACensusCodes = DW_AbstractProcessor.getLookupFromPostcodeToLevelCode(
                env,
                "MSOA",
                year_int);
        // Other variables for selecting and output
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        spiderMapDirectory.mkdirs();

        // Step 2. Read Point data
        String filename;
        // year 2012-2013
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_AdviceLeeds.loadLeedsCABData(
                env,
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        // Load Chapeltown CAB data
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_AdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);

        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();
        Iterator<String> ite_String;
        ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            tsfcs.put(tCABOutlet, new TreeSetFeatureCollection());
            sfbs.put(tCABOutlet, new SimpleFeatureBuilder(aLineSFT));
        }
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        GeometryFactory geometryFactory = new GeometryFactory();

        int countPostcodesWithPoints;
        int countPostcodesWithPointsWithoutCensusCodes;
        int countPostcodesWithPointsAndOutlets;
        int countPostcodesWithoutPoints;

        // Leeds CAB
        System.out.println("Leeds CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        //String targetCABOutlet = "Pudsey";
        Iterator<DW_ID_ClientID> ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            DW_ID_ClientID key = ite2.next();
            DW_Data_CAB2_Record r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
                    String aLSOACode = tLookupFromPostcodeToLSOACensusCodes.get(formattedPostcode);
                    String aMSOACode = tLookupFromPostcodeToMSOACensusCodes.get(formattedPostcode);
                    if (aLSOACode.equalsIgnoreCase("") || aMSOACode.equalsIgnoreCase("")) {
                        System.out.println("LSOACode, " + aLSOACode + ", postcode " + formattedPostcode);
                        System.out.println("MSOACode, " + aMSOACode + ", postcode " + formattedPostcode);
                        countPostcodesWithPointsWithoutCensusCodes++;
                    } else {
                        // Get Census geography points
                        Point aLSOAPoint = aLSOAToCentroidLookupTable.get(aLSOACode);
                        Coordinate[] coords;
                        LineString line;
                        SimpleFeature feature;
                        // Add line from postcode to LSOA
                        destinationx = aLSOAPoint.getX();
                        destinationy = aLSOAPoint.getY();
                        //String name = "";
                        //int number = 1;
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aPostcodeCount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        // Add line from LSOA to outlet
                        originx = aLSOAPoint.getX();
                        originy = aLSOAPoint.getY();
                        destinationx = outletPoint.getX();
                        destinationy = outletPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        countPostcodesWithPointsAndOutlets++;
                    }
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        // Chapeltown CAB
        System.out.println("Chapeltown CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        ite2 = tChapeltownCABData.keySet().iterator();
        while (ite2.hasNext()) {
            DW_ID_ClientID key = ite2.next();
            DW_Data_CAB0_Record r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    String formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
                    String aLSOACode = tLookupFromPostcodeToLSOACensusCodes.get(formattedPostcode);
                    String aMSOACode = tLookupFromPostcodeToMSOACensusCodes.get(formattedPostcode);
                    if (aLSOACode.equalsIgnoreCase("") || aMSOACode.equalsIgnoreCase("")) {
                        System.out.println("LSOACode, " + aLSOACode + ", postcode " + formattedPostcode);
                        System.out.println("MSOACode, " + aMSOACode + ", postcode " + formattedPostcode);
                        countPostcodesWithPointsWithoutCensusCodes++;
                    } else {
                        // Get Census geography points
                        Point aLSOAPoint = aLSOAToCentroidLookupTable.get(aLSOACode);
                        Coordinate[] coords;
                        LineString line;
                        SimpleFeature feature;
                        // Add line from postcode to LSOA
                        destinationx = aLSOAPoint.getX();
                        destinationy = aLSOAPoint.getY();
                        //String name = "";
                        //int number = 1;
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aPostcodeCount);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        // Add line from LSOA to outlet
                        originx = aLSOAPoint.getX();
                        originy = aLSOAPoint.getY();
                        destinationx = outletPoint.getX();
                        destinationy = outletPoint.getY();
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        line = geometryFactory.createLineString(coords);
                        sfb.add(line);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);

                        countPostcodesWithPointsAndOutlets++;
                    }
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            TreeSetFeatureCollection tsfc = tsfcs.get(tCABOutlet);
            File outputShapefile = AGDT_Geotools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_Shapefile.transact(
                    outputShapefile,
                    aLineSFT,
                    tsfc,
                    getShapefileDataStoreFactory());
            DW_Geotools.outputToImage(
                    tCABOutlet,
                    outputShapefile,
                    foregroundDW_Shapefile0,
                    foregroundDW_Shapefile1,
                    backgroundDW_Shapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    0,
                    Double.POSITIVE_INFINITY,
                    showMapsInJMapPane);
        }
    }

    public void postcodeToOutletMaps(
            String outname,
            Object IDType) throws Exception {
        DW_Postcode_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getDW_Postcode_Handler();
        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
        // Other variables for selecting and output
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        spiderMapDirectory.mkdirs();
        // Step 2. Read Point data
        String filename;
        // year 2012-2013
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_AdviceLeeds.loadLeedsCABData(
                env,
                filename,
                aCAB_DataRecord2_Handler,
                IDType);
        // Load Chapeltown CAB data
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_AdviceLeeds.getChapeltownCABData(
                tCAB_DataRecord0_Handler,
                IDType);

        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();
        Iterator<String> ite_String;
        ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            tsfcs.put(tCABOutlet, new TreeSetFeatureCollection());
            sfbs.put(tCABOutlet, new SimpleFeatureBuilder(aLineSFT));
        }

        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        GeometryFactory geometryFactory = new GeometryFactory();

        int countPostcodesWithPoints;
        int countPostcodesWithPointsWithoutCensusCodes;
        int countPostcodesWithPointsAndOutlets;
        int countPostcodesWithoutPoints;

        // Leeds CAB
        System.out.println("Leeds CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        //String targetCABOutlet = "Pudsey";
        Iterator ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            Object key = ite2.next();
            DW_Data_CAB2_Record r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
//                    TreeMap<String, Integer> postcodeCount;
//                    postcodeCount = postcodeCounts.get(tCABOutletString);
//                    TreeMap<String, Integer> MSOACount;
//                    MSOACount = MSOACounts.get(tCABOutletString);
//                    TreeMap<String, Integer> LSOACount;
//                    LSOACount = LSOACounts.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    destinationx = outletPoint.getX();
                    destinationy = outletPoint.getY();
                    Coordinate[] coords;
                    LineString line;
                    SimpleFeature feature;
                    coords = new Coordinate[2];
                    coords[0] = new Coordinate(originx, originy);
                    coords[1] = new Coordinate(destinationx, destinationy);
                    line = geometryFactory.createLineString(coords);
                    sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aMSOACount);
                    feature = sfb.buildFeature(null);
                    tsfc.add(feature);
                    countPostcodesWithPointsAndOutlets++;
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        // Chapeltown CAB
        System.out.println("Chapeltown CAB");
        countPostcodesWithPoints = 0;
        countPostcodesWithPointsWithoutCensusCodes = 0;
        countPostcodesWithPointsAndOutlets = 0;
        countPostcodesWithoutPoints = 0;
        //String targetCABOutlet = "Pudsey";
        Iterator<DW_ID_ClientID> ite2_String;
        ite2_String = tChapeltownCABData.keySet().iterator();
        while (ite2_String.hasNext()) {
            DW_ID_ClientID key = ite2_String.next();
            DW_Data_CAB0_Record r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            AGDT_Point origin = tDW_Postcode_Handler.getPointFromPostcode(
                    DW_Postcode_Handler.getDefaultYM3(),
                    DW_Postcode_Handler.TYPE_UNIT,
                    postcode);
            if (origin != null) {
                String tCABOutletString = DW_AbstractProcessor.getCABOutletString(outlet);
                System.out.println("postcode " + postcode + ", outlet " + tCABOutletString);
                if (tCABOutletPostcodes.containsKey(tCABOutletString)) {
                    // Get feature collection and counters
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tCABOutletString);
//                    TreeMap<String, Integer> postcodeCount;
//                    postcodeCount = postcodeCounts.get(tCABOutletString);
//                    TreeMap<String, Integer> MSOACount;
//                    MSOACount = MSOACounts.get(tCABOutletString);
//                    TreeMap<String, Integer> LSOACount;
//                    LSOACount = LSOACounts.get(tCABOutletString);
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tCABOutletString);
                    double originx;
                    double originy;
                    originx = origin.getX();
                    originy = origin.getY();
                    double destinationx;
                    double destinationy;
                    AGDT_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    destinationx = outletPoint.getX();
                    destinationy = outletPoint.getY();
                    Coordinate[] coords;
                    LineString line;
                    SimpleFeature feature;
                    coords = new Coordinate[2];
                    coords[0] = new Coordinate(originx, originy);
                    coords[1] = new Coordinate(destinationx, destinationy);
                    line = geometryFactory.createLineString(coords);
                    sfb.add(line);
//                        // Add attributes to line
//                        sfb.add(outlet);
//                        sfb.add(aMSOACount);
                    feature = sfb.buildFeature(null);
                    tsfc.add(feature);
                    countPostcodesWithPointsAndOutlets++;
                }
                countPostcodesWithPoints++;
            } else {
                countPostcodesWithoutPoints++;
            }
        }
        System.out.println("countPostcodesWithPoints " + countPostcodesWithPoints);
        System.out.println("countPostcodesWithPointsWithoutCensusCodes " + countPostcodesWithPointsWithoutCensusCodes);
        System.out.println("countPostcodesWithPointsAndOutlets " + countPostcodesWithPointsAndOutlets);
        System.out.println("countPostcodesWithoutPoints " + countPostcodesWithoutPoints);

        ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String tCABOutlet = ite_String.next();
            TreeSetFeatureCollection tsfc = tsfcs.get(tCABOutlet);
            File outputShapeFile = AGDT_Geotools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_Shapefile.transact(
                    outputShapeFile,
                    aLineSFT,
                    tsfc,
                    getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
            DW_Geotools.outputToImage(
                    tCABOutlet,
                    outputShapeFile,
                    foregroundDW_Shapefile0,
                    foregroundDW_Shapefile1,
                    backgroundDW_Shapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    0,
                    Double.POSITIVE_INFINITY,
                    showMapsInJMapPane);
        }
    }

    /**
     * Looks for a look up table in digitalWelfareDir. If the one specified does
     * not exist, it is created and returned, otherwise the existing look up
     * table is returned.
     *
     * @param level
     * @param targetPropertyName
     * @return
     */
    public TreeMap<String, Point> getCentroidLookupTable(
            String level,
            String targetPropertyName) {
        TreeMap<String, Point> result;
        File generatedCensus2011LUTsDir = tDW_Files.getGeneratedCensus2011LUTsDir();
        File tLSOAToCentroidLookupTableFile = new File(
                generatedCensus2011LUTsDir,
                level + "ToCentroidLookupTable_TreeMap" + env.getDW_Files().getsDotdat());
        if (tLSOAToCentroidLookupTableFile.exists()) {
            result = (TreeMap<String, Point>) Generic_StaticIO.readObject(
                    tLSOAToCentroidLookupTableFile);
        } else {
            result = new TreeMap<String, Point>();
            FeatureCollection fc;
            if (level.equals("LSOA")) {
                fc = tLSOACodesAndLeedsLSOAShapefile.getLevelFC();
            } else {
                fc = tMSOACodesAndLeedsMSOAShapefile.getLevelFC();
            }
            FeatureIterator featureIterator;
            featureIterator = fc.features();
            while (featureIterator.hasNext()) {
                Feature feature = featureIterator.next();
                Collection<Property> properties;
                properties = feature.getProperties();
                Iterator<Property> itep = properties.iterator();
                while (itep.hasNext()) {
                    Property p = itep.next();
                    //System.out.println("Property " + p.toString());
                    String propertyName = p.getName().toString();
                    //System.out.println("PropertyName " + propertyName);
                    if (propertyName.equalsIgnoreCase(targetPropertyName)) {
                        //PropertyType propertyType = p.getType();
                        //System.out.println("PropertyType " + propertyType);
                        Object value = p.getValue();
                        //System.out.println("PropertyValue " + value);
                        String valueString = value.toString();
                        Geometry geometry;
                        //geometry = (Geometry) feature.getValue(feature.getDefaultGeometryProperty());
                        //geometry = (Geometry) feature.getProperty("the_geom");
                        GeometryAttribute geometryAttribute;
                        //geometryAttribute = (GeometryAttribute) feature.getProperty("the_geom");
                        geometryAttribute = feature.getDefaultGeometryProperty();
                        //String name = geometryAttribute.getName().toString();
                        //MultiPolygon polygon = (MultiPolygon) geometryAttribute.getValue();
                        geometry = (Geometry) geometryAttribute.getValue();
                        Point centroid;
                        centroid = geometry.getCentroid();
                        result.put(valueString, centroid);
                    }
                }
            }
            Generic_StaticIO.writeObject(result, tLSOAToCentroidLookupTableFile);
        }
        return result;
    }

}
