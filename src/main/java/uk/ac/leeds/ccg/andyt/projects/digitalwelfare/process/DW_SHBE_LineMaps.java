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
import java.util.ArrayList;
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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_LineMaps extends DW_Maps {

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_SHBE_LineMaps().run();
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
        outname = "Test";
        postcodeMaps(outname);

        // Tidy up
        if (!showMapsInJMapPane) {
            tLSOACodesAndLeedsLSOAShapefile.dispose();
//            tMSOACodesAndLeedsMSOAShapefile.dispose();
        }
    }

    private void init() {
        initStyleParameters();
        mapDirectory = DW_Files.getOutputSHBELineMapsDir();
        initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        backgroundDW_Shapefile = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        //foregroundDW_Shapefile0 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

    private void initStyleParameters() {
        styleParameters = new AGDT_StyleParameters();
//        styleParameters.setnClasses(9);
//        styleParameters.setPaletteName("Reds");
//        styleParameters.setAddWhiteForZero(true);
        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
//        styleParameters.setForegroundStyle0(DW_Style.createDefaultPointStyle());
        styleParameters.setForegroundStyle0(DW_Style.createAdviceLeedsPointStyles());
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
                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
    }

    public void postcodeMaps(
            String outname) throws Exception {
//        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
//        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
        // Other variables for selecting and output
        File dirOut = new File(
                mapDirectory,
                "Postcode/" + outname);
        dirOut.mkdirs();
        // Step 2. Read Postcode data
        String filename;
        filename = "PostcodeChanges.csv";
        File dirIn = new File(
                DW_Files.getOutputSHBETablesDir(),
                "Tenancy");
        dirIn = new File(
                dirIn,
                "All");
        dirIn = new File(
                dirIn,
                "TenancyAndPostcodeChanges");
        dirIn = new File(
                dirIn,
                "Ungrouped");
        dirIn = new File(
                dirIn,
                "CheckedPreviousTenure");
        dirIn = new File(
                dirIn,
                "CheckedPreviousPostcode");
        File f;
        f = new File(
                dirIn,
                filename);
        ArrayList<String> lines;
        lines = DW_Table.readCSV(f);

        // Step 3.
        SimpleFeatureType aLineSFT = DataUtilities.createType(
                "LINE",
                "the_geom:LineString," + "name:String," + "number:Integer");

        // Initialise a FeatureCollections and SimpleFeatureBuilders
        TreeMap<String, TreeSetFeatureCollection> tsfcs;
        tsfcs = new TreeMap<String, TreeSetFeatureCollection>();
        TreeMap<String, SimpleFeatureBuilder> sfbs;
        sfbs = new TreeMap<String, SimpleFeatureBuilder>();

        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        GeometryFactory geometryFactory = new GeometryFactory();

        Iterator<String> ite;
        ite = lines.iterator();
        while (ite.hasNext()) {
            String line;
            line = ite.next();
            String[] lineSplit;
            lineSplit = line.split(",");
            String nino;
            nino = lineSplit[0];
            String year;
            year = lineSplit[1].trim();
            String month;
            month = lineSplit[2].trim();
            String tenancyTypeChange;
            tenancyTypeChange = lineSplit[3].trim();
            if (tenancyTypeChange.equalsIgnoreCase("1 - 3")) {
                //if (year.equalsIgnoreCase("2008")) {
                    String postcode0 = lineSplit[4].trim();
                    String postcode1 = lineSplit[5].trim();
                    AGDT_Point origin;
                    origin = DW_Postcode_Handler.getPointFromPostcode(postcode0);
                    AGDT_Point destination;
                    destination = DW_Postcode_Handler.getPointFromPostcode(postcode1);
                    TreeSetFeatureCollection tsfc;
                    tsfc = tsfcs.get(tenancyTypeChange);
                    if (tsfc == null) {
                        tsfc = new TreeSetFeatureCollection();
                        tsfcs.put(tenancyTypeChange, tsfc);
                    }
                    SimpleFeatureBuilder sfb;
                    sfb = sfbs.get(tenancyTypeChange);
                    if (sfb == null) {
                        sfb = new SimpleFeatureBuilder(aLineSFT);
                    }
                    if (origin != null && destination != null) {
                        double originx;
                        double originy;
                        originx = origin.getX();
                        originy = origin.getY();
                        double destinationx;
                        double destinationy;
                        destinationx = destination.getX();
                        destinationy = destination.getY();
                        Coordinate[] coords;
                        LineString lineString;
                        SimpleFeature feature;
                        coords = new Coordinate[2];
                        coords[0] = new Coordinate(originx, originy);
                        coords[1] = new Coordinate(destinationx, destinationy);
                        lineString = geometryFactory.createLineString(coords);
                        sfb.add(lineString);
                        // Add attributes to line
//          sfb.add(outlet);
                        feature = sfb.buildFeature(null);
                        tsfc.add(feature);
                    }
                //}
            }
        }

        ite = tsfcs.keySet().iterator();
        while (ite.hasNext()) {
            String tenancyTypeChange;
            tenancyTypeChange = ite.next();
            String name;
            name = tenancyTypeChange.replaceAll(" ", "");
            TreeSetFeatureCollection tsfc = tsfcs.get(tenancyTypeChange);
            File outputShapeFile = AGDT_Geotools.getOutputShapefile(
                    dirOut,
                    name);
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
                    name,
                    outputShapeFile,
                    foregroundDW_Shapefile0,
                    foregroundDW_Shapefile1,
                    backgroundDW_Shapefile,
                    "",
                    dirOut,
                    png_String,
                    imageWidth,
                    styleParameters,
                    0,
                    Double.POSITIVE_INFINITY,
                    showMapsInJMapPane);
        }
    }

}
