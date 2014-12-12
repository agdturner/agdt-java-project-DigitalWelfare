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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.Style;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord0;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord2;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.CAB_DataRecord2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.EnquiryClientBureauOutletID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;

/**
 *
 * @author geoagdt
 */
public class DW_SpiderMaps extends DW_Maps {

    public DW_SpiderMaps(File dir) {
        this._DW_dir = dir;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            File dir = new File("/scratch02/DigitalWelfare/");
            new DW_SpiderMaps(dir).run();
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
        boolean showMapsInJMapPane;
        // Initialise styleParameters
        Object[] styleParameters = DW_Style.initStyleParameters();
        int imageWidth = 500;
        
        ShapefileDataStoreFactory sdsf;
        sdsf = new ShapefileDataStoreFactory();
        

        // postcodeToOutletMaps run
        outname = "postcodeToOutletMaps";
        showMapsInJMapPane = false;
        postcodeToOutletMaps(sdsf, outname, showMapsInJMapPane, styleParameters, imageWidth);

        // postcodeToLSOAToOutletMaps run
        showMapsInJMapPane = false;
        outname = "postcodeToLSOAToOutletMaps";
        postcodeToLSOAToOutletMaps(sdsf, outname, showMapsInJMapPane, styleParameters, imageWidth);

        // postcodeToLSOAToMSOAToOutletMaps run
        showMapsInJMapPane = false;
        outname = "postcodeToLSOAToMSOAOutletMaps";
        postcodeToLSOAToMSOAToOutletMaps(sdsf, outname, showMapsInJMapPane, styleParameters, imageWidth);
    }

    public void postcodeToLSOAToMSOAToOutletMaps(
            ShapefileDataStoreFactory sdsf,
            String outname,
            boolean showMapsInJMapPane,
            Object[] styleParameters,
            int imageWidth) throws Exception {

        // Property for selecting
        String targetPropertyNameLSOA = "LSOA11CD";
        String targetPropertyNameMSOA = "MSOA11CD";

        String level;
        File backgroundShapefile;

        // Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile
        System.out.println("Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile");
        level = "LSOA";
        Object[] tLSOACodesAndLeedsLSOAShapefile;
        tLSOACodesAndLeedsLSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameLSOA, sdsf);
        TreeSet<String> tLSOACodes;
        tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[0];
        File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = (FeatureCollection) tLSOACodesAndLeedsLSOAShapefile[2];
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = (SimpleFeatureType) tLSOACodesAndLeedsLSOAShapefile[3];
        File leedsLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[4];
        //backgroundShapefile = leedsLSOAShapefile;

        // Get MSOA Codes, MSOA Shapefile and Leeds MSOA Shapefile
        level = "MSOA";
        Object[] tMSOACodesAndLeedsMSOAShapefile;
        tMSOACodesAndLeedsMSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameMSOA, sdsf);
        TreeSet<String> tMSOACodes;
        tLSOACodes = (TreeSet<String>) tMSOACodesAndLeedsMSOAShapefile[0];
        File tMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[1];
        FeatureCollection tMSOAFeatureCollection;
        tMSOAFeatureCollection = (FeatureCollection) tMSOACodesAndLeedsMSOAShapefile[2];
        SimpleFeatureType tMSOAFeatureType;
        tMSOAFeatureType = (SimpleFeatureType) tMSOACodesAndLeedsMSOAShapefile[3];
        File leedsMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[4];
        backgroundShapefile = leedsMSOAShapefile;

        Style backgroundStyle = (Style) styleParameters[6];
        FeatureLayer backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                leedsLSOAShapefile,
                backgroundStyle);

        int year_int = 2011;
        // Get postcode to LSOA lookup
        level = "LSOA";
        TreeMap<String, String> tLookupFromPostcodeToLSOACensusCodes;
        tLookupFromPostcodeToLSOACensusCodes = DW_Processor.getLookupFromPostcodeToCensusCode(
                level,
                year_int);

        // Get postcode to LSOA lookup
        level = "MSOA";
        TreeMap<String, String> tLookupFromPostcodeToMSOACensusCodes;
        tLookupFromPostcodeToMSOACensusCodes = DW_Processor.getLookupFromPostcodeToCensusCode(
                level,
                year_int);
        // Other variables for selecting and output
        String png_String = "png";
        File mapDirectory = DW_Files.getOutputAdviceLeedsMapsDir();
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        mapDirectory.mkdirs();
        //boolean filter;

        // Step 1. Get Locations of Outlets as Points
        TreeMap<String, String> tCABOutletPostcodes;
        tCABOutletPostcodes = DW_Processor.getOutletsAndPostcodes();

        // Get LSOA to centroid lookup table
        level = "LSOA";
        TreeMap<String, Point> aLSOAToCentroidLookupTable;
        aLSOAToCentroidLookupTable = getCentroidLookupTable(
                level,
                tLSOAShapefile,
                tLSOAFeatureCollection,
                tLSOAFeatureType,
                targetPropertyNameLSOA);

        // Get LSOA to centroid lookup table
        level = "MSOA";
        TreeMap<String, Point> aMSOAToCentroidLookupTable;
        aMSOAToCentroidLookupTable = getCentroidLookupTable(
                level,
                tMSOAShapefile,
                tMSOAFeatureCollection,
                tMSOAFeatureType,
                targetPropertyNameMSOA);
        
        // Get Outlet Points        
        TreeMap<String, DW_Point> tCABOutletPoints;
        tCABOutletPoints = DW_Processor.getOutletsAndPoints();
        
        // Step 2. Read Point data
        CAB_DataRecord2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new CAB_DataRecord2_Handler();
        CAB_DataRecord0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new CAB_DataRecord0_Handler();
//        DW_DataProcessor_CAB aDW_DataProcessor_CAB;
//        aDW_DataProcessor_CAB = new DW_DataProcessor_CAB(_DW_dir);
//        String[] args = new String[0];
//        aDW_DataProcessor_CAB.init_tCAB_DataRecord2_Handler(args);

        String year;
        String filename;
        File dir;

        // year 2012-2013
        year = "1213";
        dir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "LeedsCAB");
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<EnquiryClientBureauOutletID, CAB_DataRecord2> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_CAB.loadLeedsCABData(
                filename,aCAB_DataRecord2_Handler);
        // Load Chapeltown CAB data
        TreeMap<String, CAB_DataRecord0> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_CAB.getChapeltownCABData(
                tCAB_DataRecord0_Handler);

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
        Iterator<EnquiryClientBureauOutletID> ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            EnquiryClientBureauOutletID key = ite2.next();
            CAB_DataRecord2 r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
//                    DW_Point a_DW_Point;
//                    a_DW_Point = outletPoint;
//                    destinationx = a_DW_Point.getX();
//                    destinationy = a_DW_Point.getY();
                    String formattedPostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
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
        Iterator<String> ite2_String;
        ite2_String = tChapeltownCABData.keySet().iterator();
        while (ite2_String.hasNext()) {
            String key = ite2_String.next();
            CAB_DataRecord0 r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
//                    DW_Point a_DW_Point;
//                    a_DW_Point = outletPoint;
//                    destinationx = a_DW_Point.getX();
//                    destinationy = a_DW_Point.getY();
                    String formattedPostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
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
            File outputShapeFile = DW_GeoTools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_GeoTools.transact(
                    outputShapeFile,
                    aLineSFT,
                    tsfc,
                    sdsf);
            DW_GeoTools.outputToImage(
                    tCABOutlet,
                    outputShapeFile,
                    backgroundFeatureLayer,
                    //backgroundShapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    showMapsInJMapPane);
        }
    }

    public void postcodeToLSOAToOutletMaps(
            ShapefileDataStoreFactory sdsf,
            String outname,
            boolean showMapsInJMapPane,
            Object[] styleParameters,
            int imageWidth) throws Exception {

        // Property for selecting
        String targetPropertyNameLSOA = "LSOA11CD";
        String targetPropertyNameMSOA = "MSOA11CD";

        String level;
        File backgroundShapefile;

        // Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile
        System.out.println("Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile");
        level = "LSOA";
        Object[] tLSOACodesAndLeedsLSOAShapefile;
        tLSOACodesAndLeedsLSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameLSOA, sdsf);
        TreeSet<String> tLSOACodes;
        tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[0];
        File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = (FeatureCollection) tLSOACodesAndLeedsLSOAShapefile[2];
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = (SimpleFeatureType) tLSOACodesAndLeedsLSOAShapefile[3];
        File leedsLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[4];
        //backgroundShapefile = leedsLSOAShapefile;

        // Get MSOA Codes, MSOA Shapefile and Leeds MSOA Shapefile
        level = "MSOA";
        Object[] tMSOACodesAndLeedsMSOAShapefile;
        tMSOACodesAndLeedsMSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameMSOA, sdsf);
        TreeSet<String> tMSOACodes;
        tLSOACodes = (TreeSet<String>) tMSOACodesAndLeedsMSOAShapefile[0];
        File tMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[1];
        FeatureCollection tMSOAFeatureCollection;
        tMSOAFeatureCollection = (FeatureCollection) tMSOACodesAndLeedsMSOAShapefile[2];
        SimpleFeatureType tMSOAFeatureType;
        tMSOAFeatureType = (SimpleFeatureType) tMSOACodesAndLeedsMSOAShapefile[3];
        File leedsMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[4];
        backgroundShapefile = leedsMSOAShapefile;

        Style backgroundStyle = (Style) styleParameters[6];
        FeatureLayer backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                leedsLSOAShapefile,
                backgroundStyle);

        int year_int = 2011;
        // Get postcode to LSOA lookup
        level = "LSOA";
        TreeMap<String, String> tLookupFromPostcodeToLSOACensusCodes;
        tLookupFromPostcodeToLSOACensusCodes = DW_Processor.getLookupFromPostcodeToCensusCode(
                level,
                year_int);

        // Get postcode to LSOA lookup
        level = "MSOA";
        TreeMap<String, String> tLookupFromPostcodeToMSOACensusCodes;
        tLookupFromPostcodeToMSOACensusCodes = DW_Processor.getLookupFromPostcodeToCensusCode(
                level,
                year_int);
        // Other variables for selecting and output
        String png_String = "png";
        File mapDirectory = DW_Files.getOutputAdviceLeedsMapsDir();
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        spiderMapDirectory.mkdirs();
        //boolean filter;

        // Step 1. Get Locations of Outlets as Points
        TreeMap<String, String> tCABOutletPostcodes;
        tCABOutletPostcodes = DW_Processor.getOutletsAndPostcodes();

        // Get LSOA to centroid lookup table
        level = "LSOA";
        TreeMap<String, Point> aLSOAToCentroidLookupTable;
        aLSOAToCentroidLookupTable = getCentroidLookupTable(
                level,
                tLSOAShapefile,
                tLSOAFeatureCollection,
                tLSOAFeatureType,
                targetPropertyNameLSOA);

        TreeMap<String, DW_Point> tCABOutletPoints;
        tCABOutletPoints = new TreeMap<String, DW_Point>();

        init_ONSPDLookup();

        Iterator<String> ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String outletName = ite_String.next();
            String postcode = tCABOutletPostcodes.get(outletName);
            DW_Point p = DW_Processor.getPointFromPostcode(postcode);
            if (p == null) {
                System.out.println("No point for postcode " + postcode);
            } else {
                tCABOutletPoints.put(outletName, p);
                System.out.println(outletName + " " + p);
            }
        }

        // Step 2. Read Point data
        CAB_DataRecord2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new CAB_DataRecord2_Handler();
        CAB_DataRecord0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new CAB_DataRecord0_Handler();
//        DW_DataProcessor_CAB aDW_DataProcessor_CAB;
//        aDW_DataProcessor_CAB = new DW_DataProcessor_CAB(_DW_dir);
//        String[] args = new String[0];
//        aDW_DataProcessor_CAB.init_tCAB_DataRecord2_Handler(args);

        String year;
        String filename;
        File dir;

        // year 2012-2013
        year = "1213";
        dir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "LeedsCAB");
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<EnquiryClientBureauOutletID, CAB_DataRecord2> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_CAB.loadLeedsCABData(
                filename, aCAB_DataRecord2_Handler);
        // Load Chapeltown CAB data
        TreeMap<String, CAB_DataRecord0> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_CAB.getChapeltownCABData(
                tCAB_DataRecord0_Handler);
        
        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();
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
        Iterator<EnquiryClientBureauOutletID> ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            EnquiryClientBureauOutletID key = ite2.next();

            if (ite2.hasNext()) {
                int debug = 1;
            } else {
                int debug = 0;
            }

            CAB_DataRecord2 r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    String formattedPostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
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
        Iterator<String> ite2_String;
        ite2_String = tChapeltownCABData.keySet().iterator();
        while (ite2_String.hasNext()) {
            String key = ite2_String.next();
            CAB_DataRecord0 r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
                    outletPoint = tCABOutletPoints.get(tCABOutletString);
                    String formattedPostcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
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
            File outputShapeFile = DW_GeoTools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_GeoTools.transact(
                    outputShapeFile,
                    aLineSFT,
                    tsfc, sdsf);
            DW_GeoTools.outputToImage(
                    tCABOutlet,
                    outputShapeFile,
                    backgroundFeatureLayer,
                    //backgroundShapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    showMapsInJMapPane);
        }
    }

    public void postcodeToOutletMaps(
            ShapefileDataStoreFactory sdsf,
            String outname,
            boolean showMapsInJMapPane,
            Object[] styleParameters,
            int imageWidth) throws Exception {

        // Property for selecting
        String targetPropertyNameLSOA = "LSOA11CD";
        String targetPropertyNameMSOA = "MSOA11CD";

        String level;
        File backgroundShapefile;

        // Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile
        System.out.println("Get LSOA Codes, LSOA Shapefile and Leeds LSOA Shapefile");
        level = "LSOA";
        Object[] tLSOACodesAndLeedsLSOAShapefile;
        tLSOACodesAndLeedsLSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameLSOA, sdsf);
        TreeSet<String> tLSOACodes;
        tLSOACodes = (TreeSet<String>) tLSOACodesAndLeedsLSOAShapefile[0];
        File tLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[1];
        FeatureCollection tLSOAFeatureCollection;
        tLSOAFeatureCollection = (FeatureCollection) tLSOACodesAndLeedsLSOAShapefile[2];
        SimpleFeatureType tLSOAFeatureType;
        tLSOAFeatureType = (SimpleFeatureType) tLSOACodesAndLeedsLSOAShapefile[3];
        File leedsLSOAShapefile = (File) tLSOACodesAndLeedsLSOAShapefile[4];
        //backgroundShapefile = leedsLSOAShapefile;

        // Get MSOA Codes, MSOA Shapefile and Leeds MSOA Shapefile
        level = "MSOA";
        Object[] tMSOACodesAndLeedsMSOAShapefile;
        tMSOACodesAndLeedsMSOAShapefile = DW_Maps.getCensusAreaCodesAndLeedsShapefile(
                level, targetPropertyNameMSOA, sdsf);
        TreeSet<String> tMSOACodes;
        tLSOACodes = (TreeSet<String>) tMSOACodesAndLeedsMSOAShapefile[0];
        File tMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[1];
        FeatureCollection tMSOAFeatureCollection;
        tMSOAFeatureCollection = (FeatureCollection) tMSOACodesAndLeedsMSOAShapefile[2];
        SimpleFeatureType tMSOAFeatureType;
        tMSOAFeatureType = (SimpleFeatureType) tMSOACodesAndLeedsMSOAShapefile[3];
        File leedsMSOAShapefile = (File) tMSOACodesAndLeedsMSOAShapefile[4];
        backgroundShapefile = leedsMSOAShapefile;

        Style backgroundStyle = (Style) styleParameters[6];
        FeatureLayer backgroundFeatureLayer = DW_GeoTools.getFeatureLayer(
                leedsLSOAShapefile,
                backgroundStyle);

        // Other variables for selecting and output
        String png_String = "png";
        File mapDirectory = DW_Files.getOutputAdviceLeedsMapsDir();
        File spiderMapDirectory = new File(
                mapDirectory,
                "spider/" + outname);
        spiderMapDirectory.mkdirs();
        //boolean filter;

        // Step 1. Get Locations of Outlets as Points
        TreeMap<String, String> tCABOutletPostcodes;
        tCABOutletPostcodes = DW_Processor.getOutletsAndPostcodes();

        TreeMap<String, DW_Point> tCABOutletPoints;
        tCABOutletPoints = new TreeMap<String, DW_Point>();

        init_ONSPDLookup();

        Iterator<String> ite_String = tCABOutletPostcodes.keySet().iterator();
        while (ite_String.hasNext()) {
            String outletName = ite_String.next();
            String postcode = tCABOutletPostcodes.get(outletName);
            DW_Point p = DW_Processor.getPointFromPostcode(postcode);
            if (p == null) {
                System.out.println("No point for postcode " + postcode);
            } else {
                tCABOutletPoints.put(outletName, p);
                System.out.println(outletName + " " + p);
            }
        }

        // Step 2. Read Point data
        CAB_DataRecord2_Handler aCAB_DataRecord2_Handler;
        aCAB_DataRecord2_Handler = new CAB_DataRecord2_Handler();
        CAB_DataRecord0_Handler tCAB_DataRecord0_Handler;
        tCAB_DataRecord0_Handler = new CAB_DataRecord0_Handler();
//        DW_DataProcessor_CAB aDW_DataProcessor_CAB;
//        aDW_DataProcessor_CAB = new DW_DataProcessor_CAB(_DW_dir);
//        String[] args = new String[0];
//        aDW_DataProcessor_CAB.init_tCAB_DataRecord2_Handler(args);

        String filename;
        File dir;

        // year 2012-2013
        dir = new File(
                DW_Files.getGeneratedAdviceLeedsDir(),
                "LeedsCAB");
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        // Load Leeds CAB Data
        TreeMap<EnquiryClientBureauOutletID, CAB_DataRecord2> tLeedsCABData;
        tLeedsCABData = DW_DataProcessor_CAB.loadLeedsCABData(
                filename,
                aCAB_DataRecord2_Handler);
        // Load Chapeltown CAB data
        TreeMap<String, CAB_DataRecord0> tChapeltownCABData;
        tChapeltownCABData = DW_DataProcessor_CAB.getChapeltownCABData(
                tCAB_DataRecord0_Handler);

        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();
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
        Iterator<EnquiryClientBureauOutletID> ite2;
        ite2 = tLeedsCABData.keySet().iterator();
        while (ite2.hasNext()) {
            EnquiryClientBureauOutletID key = ite2.next();
            CAB_DataRecord2 r = tLeedsCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = r.getOutlet();
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
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
        Iterator<String> ite2_String;
        ite2_String = tChapeltownCABData.keySet().iterator();
        while (ite2_String.hasNext()) {
            String key = ite2_String.next();
            CAB_DataRecord0 r = tChapeltownCABData.get(key);
            String postcode = r.getPostcode();
            String outlet = "CHAPELTOWN";
            DW_Point origin = DW_Processor.getPointFromPostcode(postcode);
            if (origin != null) {
                String tCABOutletString = DW_Processor.getCABOutletString(outlet);
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
                    DW_Point outletPoint = null;
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
            File outputShapeFile = DW_GeoTools.getOutputShapefile(
                    spiderMapDirectory,
                    tCABOutlet);
            DW_GeoTools.transact(
                    outputShapeFile,
                    aLineSFT,
                    tsfc, sdsf);
            DW_GeoTools.outputToImage(
                    tCABOutlet,
                    outputShapeFile,
                    backgroundFeatureLayer,
                    //backgroundShapefile,
                    "",
                    spiderMapDirectory,
                    png_String,
                    imageWidth,
                    styleParameters,
                    showMapsInJMapPane);
        }
    }

    /**
     * Looks for a look up table in digitalWelfareDir. If the one specified does
     * not exist, it is created and returned, otherwise the existing look up
     * table is returned.
     *
     * @param level
     * @param tLSOAShapefile
     * @param tLSOAFeatureCollection
     * @param tLSOAFeatureType
     * @param targetPropertyName
     * @return
     */
    public static TreeMap<String, Point> getCentroidLookupTable(
            String level,
            File tLSOAShapefile,
            FeatureCollection tLSOAFeatureCollection,
            SimpleFeatureType tLSOAFeatureType,
            String targetPropertyName) {
        TreeMap<String, Point> result;
        File generatedCensus2011LUTsDir = DW_Files.getGeneratedCensus2011LUTsDir();
        File tLSOAToCentroidLookupTableFile = new File(
                generatedCensus2011LUTsDir,
                level + "ToCentroidLookupTable_TreeMap.thisFile");
        if (tLSOAToCentroidLookupTableFile.exists()) {
            result = (TreeMap<String, Point>) Generic_StaticIO.readObject(
                    tLSOAToCentroidLookupTableFile);
        } else {
            result = new TreeMap<String, Point>();
            FeatureIterator featureIterator;
            featureIterator = tLSOAFeatureCollection.features();
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
