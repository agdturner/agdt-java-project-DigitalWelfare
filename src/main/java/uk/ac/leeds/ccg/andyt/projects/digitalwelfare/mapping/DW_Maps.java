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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.PostcodeGeocoder;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor;

/**
 *
 * @author geoagdt
 */
public abstract class DW_Maps {

    public static final String png_String = "png";
    public static final String defaultSRID = "27700";

    private static TreeMap<String, DW_Point>[] ONSPDlookups;

    private static HashMap<String, SimpleFeatureType> pointSimpleFeatureTypes;
    private static HashMap<String, SimpleFeatureType> lineSimpleFeatureTypes;
    private static HashMap<String, SimpleFeatureType> polygonSimpleFeatureTypes;

    /**
     * If showMapsInJMapPane is true, maps are presented in individual JMapPanes
     */
    protected boolean showMapsInJMapPane;
    protected int imageWidth;
    protected boolean commonlyStyled;
    protected boolean individuallyStyled;
    protected File mapDirectory;
    private ShapefileDataStoreFactory sdsf;
    protected DW_StyleParameters styleParameters;
    protected ArrayList<DW_Shapefile> foregroundDW_Shapefile0;
    protected DW_Shapefile foregroundDW_Shapefile1;
    protected DW_Shapefile backgroundDW_Shapefile;

    /**
     * For storing census data level (OA, LSOA, MSOA, ...)
     */
    protected String level;

    public DW_Maps() {
    }

    public ShapefileDataStoreFactory getShapefileDataStoreFactory() {
        if (sdsf == null) {
            sdsf = new ShapefileDataStoreFactory();
        }
        return sdsf;
    }

    public static TreeMap<String, DW_Point>[] getONSPDlookups() {
        if (ONSPDlookups == null) {
            initONSPDLookups();
        }
        return ONSPDlookups;
    }

    public static void initONSPDLookups() {
        ONSPDlookups = new TreeMap[2];
        File generatedONSPDDir = DW_Files.getGeneratedONSPDDir();
        String tONSPDProcessedFilename;
        String tONSPDProcessedFilename2;
        //tONSPDProcessedFilename = "PostcodeLookUp_TreeMap_String_Point.thisFile";
        tONSPDProcessedFilename = "PostcodeUnitLookUp_TreeMap_String_Point.thisFile";
        tONSPDProcessedFilename2 = "PostcodeSectorLookUp_TreeMap_String_Point.thisFile";
        File tONSPDProcessedFile = new File(
                generatedONSPDDir,
                tONSPDProcessedFilename);
        File tONSPDProcessedFile2 = new File(
                generatedONSPDDir,
                tONSPDProcessedFilename2);
        if (tONSPDProcessedFile.exists()) {
            ONSPDlookups[0] = PostcodeGeocoder.getStringToDW_PointLookup(
                    tONSPDProcessedFile);
        } else {
            File tONSPDNov2013Dir = new File(
                    DW_Files.getInputONSPDDir(),
                    "ONSPD_NOV_2013");
            File tONSPDNov2013DataDir = new File(
                    tONSPDNov2013Dir,
                    "Data");
            File tONSPDNov2013DataFile = new File(
                    tONSPDNov2013DataDir,
                    "ONSPD_NOV_2013_UK.csv");
            PostcodeGeocoder pg = new PostcodeGeocoder(
                    tONSPDNov2013DataFile,
                    tONSPDProcessedFile);
            boolean ignorePointsAtOrigin = true;
            ONSPDlookups[0] = pg.getPostcodeUnitPointLookup(
                    ignorePointsAtOrigin);
        }
        if (tONSPDProcessedFile2.exists()) {
            ONSPDlookups[1] = PostcodeGeocoder.getStringToDW_PointLookup(
                    tONSPDProcessedFile2);
        } else {
            File tONSPDNov2013Dir = new File(
                    DW_Files.getInputONSPDDir(),
                    "ONSPD_NOV_2013");
            File tONSPDNov2013DataDir = new File(
                    tONSPDNov2013Dir,
                    "Data");
            File tONSPDNov2013DataFile = new File(
                    tONSPDNov2013DataDir,
                    "ONSPD_NOV_2013_UK.csv");
            PostcodeGeocoder pg = new PostcodeGeocoder(
                    tONSPDNov2013DataFile,
                    tONSPDProcessedFile);
            boolean ignorePointsAtOrigin = true;
            ONSPDlookups[1] = pg.getPostcodeSectorPointLookup(
                    ignorePointsAtOrigin,
                    tONSPDProcessedFilename2,
                    ONSPDlookups[0]);
        }
    }

    /**
     * Simple convenience method.
     * @param type
     * @param srid
     * @return null if type is not one of "Point", "LineString", or "Polygon"
     */
    public static SimpleFeatureType getSimpleFeatureType(
            String type,
            String srid) {
        if (srid == null) {
            srid = getDefaultSRID();
        }
        if (type.equalsIgnoreCase("Point")) {
            return getPointSimpleFeatureType(srid);
        }
        if (type.equalsIgnoreCase("LineString")) {
            return getLineSimpleFeatureType(srid);
        }
        if (type.equalsIgnoreCase("Polygon")) {
            return getPolygonSimpleFeatureType(srid);
        }
        return null;
    }

    public static String getDefaultSRID() {
        return defaultSRID;//"27700";
    }

    public static HashMap<String, SimpleFeatureType> getPointSimpleFeatureTypes() {
        if (pointSimpleFeatureTypes == null) {
            pointSimpleFeatureTypes = new HashMap<String, SimpleFeatureType>();
            //pointSimpleFeatureTypes = initPointSimpleFeatureTypes();
        }
        return pointSimpleFeatureTypes;
    }

    public static SimpleFeatureType getPointSimpleFeatureType(String srid) {
        if (!getPointSimpleFeatureTypes().containsKey(srid)) {
            SimpleFeatureType sft;
            sft = initSimpleFeatureType(
                    "Point", srid);
            pointSimpleFeatureTypes.put(
                    srid, sft);
            return sft;
        }
        return pointSimpleFeatureTypes.get(srid);
    }

    private static SimpleFeatureType initSimpleFeatureType(
            String type,
            String srid) {
        SimpleFeatureType result = null;
        try {
            result = DataUtilities.createType(
                    "Location",
                    "the_geom:" + type + ":srid=" + srid + "," + // <- the geometry attribute
                    "name:String," // <- a String attribute
            );
        } catch (SchemaException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static HashMap<String, SimpleFeatureType> getLineSimpleFeatureTypes() {
        if (lineSimpleFeatureTypes == null) {
            lineSimpleFeatureTypes = new HashMap<String, SimpleFeatureType>();
        }
        return lineSimpleFeatureTypes;
    }

    public static SimpleFeatureType getLineSimpleFeatureType(String srid) {
        if (!getLineSimpleFeatureTypes().containsKey(srid)) {
            SimpleFeatureType sft;
            sft = initSimpleFeatureType(
                    "LineString", srid);
            lineSimpleFeatureTypes.put(
                    srid, sft);
            return sft;
        }
        return lineSimpleFeatureTypes.get(srid);
    }

    public static HashMap<String, SimpleFeatureType> getPolygonSimpleFeatureTypes() {
        if (polygonSimpleFeatureTypes == null) {
            polygonSimpleFeatureTypes = new HashMap<String, SimpleFeatureType>();
        }
        return polygonSimpleFeatureTypes;
    }

    public static SimpleFeatureType getPolygonSimpleFeatureType(String srid) {
        if (!getPolygonSimpleFeatureTypes().containsKey(srid)) {
            SimpleFeatureType sft;
            sft = initSimpleFeatureType(
                    "Polygon", srid);
            polygonSimpleFeatureTypes.put(
                    srid, sft);
            return sft;
        }
        return polygonSimpleFeatureTypes.get(srid);
    }

    /*
     * Select and create a new shapefile.
     *
     * @param sdsf
     * @param fc
     * @param sft
     * @param codesToSelect
     * @param targetPropertyName
     * @param outputFile
     */
    public static void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> codesToSelect,
            //String attributeName, 
            String targetPropertyName,
            File outputFile) {
        // Initialise the collection of new Features
        TreeSetFeatureCollection tsfc;
        tsfc = new TreeSetFeatureCollection();
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        SimpleFeatureBuilder sfb;
        sfb = new SimpleFeatureBuilder(sft);
        FeatureIterator featureIterator;
        featureIterator = fc.features();
        int id_int = 0;
        while (featureIterator.hasNext()) {
            Feature inputFeature = featureIterator.next();
            Collection<Property> properties;
            properties = inputFeature.getProperties();
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
                    if (codesToSelect.contains(valueString)) {
                        if (valueString.trim().equalsIgnoreCase("E02002337")) {
                            int debug = 1;
                        }
                        String id = "" + id_int;
                        addFeatureToFeatureCollection(
                                (SimpleFeature) inputFeature,
                                sfb,
                                tsfc,
                                id);
                        id_int++;
                    } else {
//                        System.out.println(valueString);
                    }
                }
            }
        }
        featureIterator.close();
        DW_Shapefile.transact(outputFile, sft, tsfc, sdsf);
    }

    public SimpleFeatureType getFeatureType(
            SimpleFeatureType sft,
            Class<?> binding,
            String attributeName,
            String name) {
        SimpleFeatureType result = null;
        SimpleFeatureTypeBuilder sftb;
        sftb = getNewSimpleFeatureTypeBuilder(sft, name);
        if (binding.equals(Integer.class)) {
            // Add the new attribute
            sftb.add(attributeName, Integer.class);
        }
        if (binding.equals(Double.class)) {
            // Add the new attribute
            sftb.add(attributeName, Double.class);
        }
        result = sftb.buildFeatureType();
        return result;
    }

    public static File getOutputImageFile(
            File outputFile,
            String outputType) {
        File result;
        String filename = outputFile.getName();
        String outputImageFilename;
        outputImageFilename = filename.substring(0, filename.length() - 4)
                + "." + outputType;
        result = new File(
                outputFile.getParent(),
                outputImageFilename);
        return result;
    }

    /**
     *
     * @param sft
     * @param name
     * @return
     */
    public SimpleFeatureTypeBuilder getNewSimpleFeatureTypeBuilder(
            SimpleFeatureType sft,
            String name) {
        SimpleFeatureTypeBuilder result = new SimpleFeatureTypeBuilder();
        result.init(sft);
        result.setName(name);
        return result;
    }

    public void summariseAttributes(SimpleFeatureType sft) {
        int attributeCount = sft.getAttributeCount();
        System.out.println("attributeIndex,attributeDescriptor");
        for (int i = 0; i < attributeCount; i++) {
            AttributeDescriptor attributeDescriptor;
            attributeDescriptor = sft.getDescriptor(i);
            System.out.println("" + i + "," + attributeDescriptor.getLocalName());
        }
    }

    /**
     * Adds sf attributes and value to sfb and builds a new SimpleFeature with
     * id.
     *
     * @param sf The SimpleFeature from which the new SimpleFeature is based.
     * @param sfb The SimpleFeatureBuilder for building the new SimpleFeature.
     * @param value The value to be assigned as an additional attribute in the
     * new SimpleFeature
     * @param fc The TreeSetFeatureCollection to which the new SimpleFeature is
     * added.
     * @param id Null permitted. This will be the id assigned to the built
     * feature.
     */
    public static void addFeatureAttributeAndAddToFeatureCollection(
            SimpleFeature sf,
            SimpleFeatureBuilder sfb,
            Integer value,
            TreeSetFeatureCollection fc,
            String id) {
        sfb.addAll(sf.getAttributes());
        sfb.add(value);
        fc.add(sfb.buildFeature(id));
    }

    /**
     * Adds sf attributes and value to sfb and builds a new SimpleFeature with
     * id.
     *
     * @param sf The SimpleFeature from which the new SimpleFeature is based.
     * @param sfb The SimpleFeatureBuilder for building the new SimpleFeature.
     * @param value The value to be assigned as an additional attribute in the
     * new SimpleFeature
     * @param fc The TreeSetFeatureCollection to which the new SimpleFeature is
     * added.
     * @param id Null permitted. This will be the id assigned to the built
     * feature.
     */
    public static void addFeatureAttributeAndAddToFeatureCollection(
            SimpleFeature sf,
            SimpleFeatureBuilder sfb,
            Double value,
            TreeSetFeatureCollection fc,
            String id) {
        sfb.addAll(sf.getAttributes());
        sfb.add(value);
        fc.add(sfb.buildFeature(id));
    }

    /**
     * Adds sf attributes to sfb and builds a new SimpleFeature with id.
     *
     * @param sf The SimpleFeature from which the new SimpleFeature is based.
     * @param sfb The SimpleFeatureBuilder for building the new SimpleFeature.
     * @param fc The TreeSetFeatureCollection to which the new SimpleFeature is
     * added.
     * @param id Null permitted. This will be the id assigned to the built
     * feature.
     */
    public static void addFeatureToFeatureCollection(
            SimpleFeature sf,
            SimpleFeatureBuilder sfb,
            TreeSetFeatureCollection fc,
            String id) {
        sfb.addAll(sf.getAttributes());
        fc.add(sfb.buildFeature(id));
    }

//    public SimpleFeatureType getOutputSimpleFeatureType(
//            SimpleFeatureType inputSimpleFeatureType) {
//        SimpleFeatureType result;
//        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
//        b.setName("Name");
//        //b.add(inType.getName());
//        //b.add("Name", org.opengis.feature.type.Name.class);
//        //b.add(Name);
//        //b.add("name", org.opengis.feature.type.Name.class);
//        //b.add("Name", PropertyType.class);
//        b.add(inputSimpleFeatureType.getGeometryDescriptor());
//        b.addAll(inputSimpleFeatureType.getAttributeDescriptors());
//        //b.add("clients", Integer.class);
//        b.add("clients", String.class);
//        result = b.buildFeatureType();
//        return result;
//    }
    /**
     * @param dir
     * @param filenames
     * @return A Object[] result where: ----------------------------------------
     * result[0] is an Object[] with the same length as filenames.length where
     * each element i is the respective TreeMap<String, Integer> returned from
     * getLevelData(digitalWelfareDir,filenames[i]);
     * ---------------------------- result[1] is the max of all the maximum
     * counts.
     */
    protected static Object[] getLevelData(
            File dir,
            String[] filenames) {
        Object[] result = new Object[2];
        int length = filenames.length;
        Object[] resultPart0 = new Object[length];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            Object[] levelData = getLevelData(
                    dir,
                    filenames[i]);
            resultPart0[i] = levelData;
            max = Math.max(max, (Integer) levelData[1]);
        }
        result[0] = resultPart0;
        result[1] = max;
        return result;
    }

    /**
     * @param dir
     * @param filename
     * @return An Object[] result where: ---------------------------------------
     * result[0] is a TreeMap<String, Integer> with keys which are DW_Census
     * Codes and values that are counts;
     * --------------------------------------------- result[1] is the max count.
     */
    protected static Object[] getLevelData(
            File dir,
            String filename) {
        Object[] result = new Object[2];
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();
        result[0] = map;
        File file = new File(
                dir,
                filename);
        BufferedReader br = Generic_StaticIO.getBufferedReader(file);
        StreamTokenizer st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax1(st);
        try {
            int token = st.nextToken();
            //Need skip some header lines
            st.nextToken();
            st.nextToken();
//            st.nextToken();
//            st.nextToken();
            int max = Integer.MIN_VALUE;
            long RecordID = 0;
            String line = "";
            while (!(token == StreamTokenizer.TT_EOF)) {
                switch (token) {
                    case StreamTokenizer.TT_EOL:
                        if (RecordID % 100 == 0) {
                            System.out.println(line);
                        }
                        RecordID++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        String[] split = line.split(",");
                        Integer value = new Integer(split[1].trim());
                        map.put(split[0], value);
                        max = Math.max(max, value);
                        break;
                }
                token = st.nextToken();
            }
            result[1] = max;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    /**
     * @param level
     * @param area
     * @return
     */
    protected TreeMap<String, Integer> getPopData(
            String level,
            String area) {
        TreeMap<String, Integer> result;
        result = new TreeMap<String, Integer>();
        File dir = new File(
                DW_Files.getInputCensus2011AttributeDataDir(level),
                area);
        File file = new File(
                dir,
                "pop.csv");
        BufferedReader br = Generic_StaticIO.getBufferedReader(file);
        StreamTokenizer st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax1(st);
        try {
            int token = st.nextToken();
//            //skip header (2 lines)
//            st.nextToken();
//            st.nextToken();
//            st.nextToken();
//            st.nextToken();
//            long RecordID = 0;
            String line = "";
            while (!(token == StreamTokenizer.TT_EOF)) {
                switch (token) {
                    case StreamTokenizer.TT_EOL:
//                        if (RecordID % 100 == 0) {
//                            System.out.println(line);
//                        }
//                        RecordID++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        String[] split = line.split(",");
                        if (split.length == 2) {
//                            if (!(split[1].equalsIgnoreCase("MSOAIZ") || 
//                                    split[1].equalsIgnoreCase("Middle Super Output Areas and Intermediate Zones"))) {
                            try {
                                String censusArea = split[0];
                                Integer pop = new Integer(split[1]);
                                result.put(censusArea, pop);
//                            } else {
//                                int debug = 1; //Wierdness for Scotland!
//                            }
                            } catch (NumberFormatException e) {
                                // Carry on regardless
                            }
                        } else {
                            int debug = 1; //Sometimes Scotland or other data is missing!
                        }
                        break;
                }
                token = st.nextToken();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + "in DW_Maps.getPopData(String=" + level + ",String=" + area + ")");
        }
        return result;
    }

    /**
     * @param level
     * @return File
     */
    protected static File getCensusBoundaryShapefile(
            String level) {
        File result;
        String name;
        if (level.equalsIgnoreCase("LAD")) {
            name = "England_lad_2011_gen_clipped.shp";
        } else {
            if (level.equalsIgnoreCase("OA")) {
                name = "England_oa_2011_gen_clipped.shp";
            } else {
                name = level + "_2011_EW_BGC.shp";
            }
        }
        ///scratch02/DigitalWelfare/Input/Census/2011/LSOA/BoundaryData/
        File censusDirectory = DW_Files.getInputCensus2011Dir(level);
        File boundaryDirectory = new File(
                censusDirectory,
                "/BoundaryData/");
        File shapefileDirectory = new File(
                boundaryDirectory,
                name);
//        File boundaryDirectory = new File(
//                digitalWelfareDir,
//                "/BoundaryData/" + level + "/" + name);
        result = new File(shapefileDirectory, name);
        return result;
    }

    /**
     * @param area
     * @param level
     * @return File
     */
    protected static File getCensusBoundaryShapefile(
            String area,
            String level) {
        File result;
        String name = area + "_" + level + "_2011_EW_BGC.shp";
        File boundaryDirectory = new File(
                DW_Files.getGeneratedCensus2011Dir(level),
                "/BoundaryData/" + name);
        if (!boundaryDirectory.exists()) {
            boolean dirCreated;
            dirCreated = boundaryDirectory.mkdirs();
            if (!dirCreated) {
                System.err.println("Warning directory " + boundaryDirectory
                        + " not created in " + DW_Maps.class
                        + ".getLeedsCensusBoundaryShapefile(File).");
            }
        }
        result = new File(boundaryDirectory, name);
        return result;
    }

    /**
     * @return File
     */
    protected static File getLeedsPostcodeSectorShapefile() {
        File result;
        String fileAndDirName = "LSPostalSector.shp";
        File boundaryDirectory = new File(
                DW_Files.getInputPostcodeDir(),
                "BoundaryData");
        File shapefileDir = new File(
                boundaryDirectory,
                fileAndDirName);
        result = new File(
                shapefileDir,
                fileAndDirName);
        return result;
    }

    /*
     * Select and create a new shapefile.
     *
     * @param fc
     * @param sft
     * @param tLSOACodes
     * @param tLSOAData
     * @param attributeName
     * @param outputFile
     * @param max
     * @param filter If filter == true then result is clipped to the LSOA
     * boundary.
     */
    public void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> levelCodes,
            TreeMap<String, Integer> levelData,
            //String attributeName, 
            String targetPropertyName,
            File outputFile,
            int filter) {
        //        summariseAttributes(sft);
        // Initialise the collection of new Features
        TreeSetFeatureCollection tsfc;
        tsfc = new TreeSetFeatureCollection();
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        SimpleFeatureBuilder sfb;
        sfb = new SimpleFeatureBuilder(sft);
        Set<String> keySet = levelData.keySet();
        FeatureIterator featureIterator;
        featureIterator = fc.features();
        int id_int = 0;
        while (featureIterator.hasNext()) {
            Feature inputFeature = featureIterator.next();
            Collection<Property> properties;
            properties = inputFeature.getProperties();
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
                    if (filter < 3) {
                        if (levelCodes.contains(valueString)) {
                            Integer clientCount = levelData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                String id = "" + id_int;
                                addFeatureAttributeAndAddToFeatureCollection(
                                        (SimpleFeature) inputFeature, sfb, clientCount, tsfc, id);
                                id_int++;
                            }
                        }
                    } else {
                        if (keySet.contains(valueString) || levelCodes.contains(valueString)) {
                            Integer clientCount = levelData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                String id = "" + id_int;
                                addFeatureAttributeAndAddToFeatureCollection(
                                        (SimpleFeature) inputFeature, sfb, clientCount, tsfc, id);
                                id_int++;
                            }
                        }
                    }
                }
            }
        }
        featureIterator.close();
        DW_Shapefile.transact(outputFile, sft, tsfc, sdsf);
    }

    /*
     * Select and create a new shapefile.
     *
     * @param fc
     * @param sft
     * @param tLSOACodes
     * @param tLSOAData
     * @param attributeName
     * @param outputFile
     * @param max
     * @param filter If filter == true then result is clipped to the LSOA
     * boundary.
     */
    public void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> censusCodes,
            TreeMap<String, Integer> levelData,
            //String attributeName, 
            String targetPropertyName,
            File outputFile,
            int filter,
            TreeMap<Integer, Integer> deprivationClasses,
            TreeMap<String, Deprivation_DataRecord> deprivationDataRecords,
            Integer deprivationClass) {
        //        summariseAttributes(sft);
        // Initialise the collection of new Features
        TreeSetFeatureCollection tsfc;
        tsfc = new TreeSetFeatureCollection();
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        SimpleFeatureBuilder sfb;
        sfb = new SimpleFeatureBuilder(sft);
        Set<String> keySet = levelData.keySet();
        FeatureIterator featureIterator;
        featureIterator = fc.features();
        int id_int = 0;
        while (featureIterator.hasNext()) {
            Feature inputFeature = featureIterator.next();
            Collection<Property> properties;
            properties = inputFeature.getProperties();
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

                    Deprivation_DataRecord aDeprivation_DataRecord;
                    aDeprivation_DataRecord = deprivationDataRecords.get(valueString);
                    // aDeprivation_DataRecord might be null as deprivation data comes from 2001 census...
                    if (aDeprivation_DataRecord != null) {
                        Integer thisDeprivationClass;
                        thisDeprivationClass = Deprivation_DataHandler.getDeprivationClass(
                                deprivationClasses,
                                aDeprivation_DataRecord);
                        if (thisDeprivationClass == deprivationClass.intValue()) {
                            if (filter < 3) {
                                if (censusCodes.contains(valueString)) {
                                    Integer clientCount = levelData.get(valueString);
                                    //                            if (clientCount == null) {
                                    //                                clientCount = 0;
                                    //                            }
                                    if (clientCount != null) {
                                        String id = "" + id_int;
                                        addFeatureAttributeAndAddToFeatureCollection(
                                                (SimpleFeature) inputFeature, sfb, clientCount, tsfc, id);
                                        id_int++;
                                    }
                                }
                            } else {
                                if (keySet.contains(valueString) || censusCodes.contains(valueString)) {
                                    Integer clientCount = levelData.get(valueString);
                                    //                            if (clientCount == null) {
                                    //                                clientCount = 0;
                                    //                            }
                                    if (clientCount != null) {
                                        String id = "" + id_int;
                                        addFeatureAttributeAndAddToFeatureCollection(
                                                (SimpleFeature) inputFeature, sfb, clientCount, tsfc, id);
                                        id_int++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        featureIterator.close();
        DW_Shapefile.transact(outputFile, sft, tsfc, sdsf);
    }

    /*
     * Get an output file name and create the new shapefile
     */
    public void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> levelCodes,
            TreeMap<String, Integer> levelData,
            TreeMap<String, Integer> pop,
            double multiplier,
            //String attributeName,
            String targetPropertyName,
            File outputFile,
            int max,
            int filter) {
        TreeSetFeatureCollection tsfc;
        tsfc = new TreeSetFeatureCollection();
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        SimpleFeatureBuilder sfb;
        sfb = new SimpleFeatureBuilder(sft);
        int id_int = 0;
        FeatureIterator featureIterator;
        featureIterator = fc.features();
        Set<String> keySet = levelData.keySet();
        while (featureIterator.hasNext()) {
            Feature inputFeature = featureIterator.next();
            Collection<Property> properties;
            properties = inputFeature.getProperties();
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
                    if (filter < 3) {
                        if (levelCodes.contains(valueString)) {
                            Integer clientCount = levelData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                Integer population = pop.get(valueString);
                                double clientRate = (double) clientCount * multiplier / (double) population;
                                String id = "" + id_int;
                                addFeatureAttributeAndAddToFeatureCollection(
                                        (SimpleFeature) inputFeature, sfb, clientRate, tsfc, id);
                                id_int++;
                            }
                        }
                    } else {
                        if (keySet.contains(valueString) || levelCodes.contains(valueString)) {
                            Integer clientCount = levelData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                Integer population = pop.get(valueString);
                                double clientRate = (double) clientCount * multiplier / (double) population;
                                String id = "" + id_int;
                                addFeatureAttributeAndAddToFeatureCollection(
                                        (SimpleFeature) inputFeature, sfb, clientRate, tsfc, id);
                                id_int++;
                            }
                        }
                    }
                }
            }
        }
        featureIterator.close();
        DW_Shapefile.transact(outputFile, sft, tsfc, sdsf);
    }

    public String getOutName(String filename, String attributeName, int filter) {
        String result = filename.substring(0, filename.length() - 4);
        result += attributeName;
        if (filter == 0) {
            result += "ClippedToLeedsLAD";
        }
        if (filter == 1) {
            result += "ClippedToLeedsAndNeighbouringLADs";
        }
        if (filter == 2) {
            result += "ClippedToLeedsAndNeighbouringLADsAndCravenAndYork";
        }
        if (filter == 3) {
            result += "";
        }
        return result;
    }

    public static TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft) {
        TreeSetFeatureCollection result;
        TreeMap<String, DW_Point> tAdviceLeedsNamesAndPoints;
        tAdviceLeedsNamesAndPoints = DW_Processor.getAdviceLeedsNamesAndPoints();
        TreeMap<String, DW_Point> map = tAdviceLeedsNamesAndPoints;
        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = new TreeSetFeatureCollection();
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String outlet = ite.next();
            String name = outlet;
            DW_Point p = map.get(outlet);
            addPointFeature(p, gf, sfb, name, result);
        }
        return result;
    }

    protected static void addPointFeature(
            DW_Point p,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb,
            String name,
            TreeSetFeatureCollection tsfc) {
        Point point = gf.createPoint(new Coordinate(p.getX(), p.getY()));
        sfb.add(point);
        sfb.add(name);
        SimpleFeature feature = sfb.buildFeature(null);
        tsfc.add(feature);
    }

    protected static void addLineFeature(
            DW_Point p1,
            DW_Point p2,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb,
            String name,
            TreeSetFeatureCollection tsfc) {
        Coordinate[] coordinates;
        coordinates = new Coordinate[2];
        coordinates[0] = new Coordinate(p1.getX(), p1.getY());
        coordinates[1] = new Coordinate(p2.getX(), p2.getY());
        LineString line = gf.createLineString(coordinates);
        sfb.add(line);
        sfb.add(name);
        SimpleFeature feature = sfb.buildFeature(null);
        tsfc.add(feature);
    }
    
    protected static void addQuadFeature(
            DW_Point p1,
            DW_Point p2,
            DW_Point p3,
            DW_Point p4,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb,
            String name,
            TreeSetFeatureCollection tsfc) {
        Coordinate[] coordinates;
        coordinates = new Coordinate[4];
        coordinates[0] = new Coordinate(p1.getX(), p1.getY());
        coordinates[1] = new Coordinate(p2.getX(), p2.getY());
        coordinates[1] = new Coordinate(p3.getX(), p3.getY());
        coordinates[1] = new Coordinate(p4.getX(), p4.getY());
        Polygon quad = gf.createPolygon(coordinates);
        sfb.add(quad);
        sfb.add(name);
        SimpleFeature feature = sfb.buildFeature(null);
        tsfc.add(feature);
    }

    public static TreeSetFeatureCollection getGridLineFeatureCollection(
            SimpleFeatureType sft,
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize) {
        TreeSetFeatureCollection result;
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = getGridLineFeatureCollection(
                sft,
                nrows,
                ncols,
                xllcorner,
                yllcorner,
                cellsize,
                gf,
                sfb);
        return result;
    }

    public static TreeSetFeatureCollection getGridLineFeatureCollection(
            SimpleFeatureType sft,
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        // add row lines
        for (long row = 0; row <= nrows; row++) {
            double y;
            y = yllcorner + (row * cellsize);
            double x;
            x = xllcorner + (ncols * cellsize);
            DW_Point p1;
            DW_Point p2;
            p1 = new DW_Point((int) xllcorner, (int) y);
            p2 = new DW_Point((int) x, (int) y);
            addLineFeature(p1, p2, gf, sfb, "", result);
        }
        // add col lines
        for (long col = 0; col <= ncols; col++) {
            double x;
            x = xllcorner + (col * cellsize);
            DW_Point p1;
            DW_Point p2;
            double y;
            y = yllcorner + (nrows * cellsize);
            p1 = new DW_Point((int) x, (int) yllcorner);
            p2 = new DW_Point((int) x, (int) y);
            addLineFeature(p1, p2, gf, sfb, "", result);
        }
        return result;
    }
    
    public static TreeSetFeatureCollection getGridPolyFeatureCollection(
            SimpleFeatureType sft,
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize) {
        TreeSetFeatureCollection result;
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = getGridPolyFeatureCollection(
                sft,
                nrows,
                ncols,
                xllcorner,
                yllcorner,
                cellsize,
                gf,
                sfb);
        return result;
    }

    public static TreeSetFeatureCollection getGridPolyFeatureCollection(
            SimpleFeatureType sft,
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        // add row lines
        for (long row = 0; row < nrows; row++) {
            for (long col = 0; col < ncols; col++) {
                double y1;
                double y2;
                double x1;
                double x2;
                y1 = yllcorner + (row * cellsize);
                y2 = y1 + cellsize;
                x1 = xllcorner + (col * cellsize);
                x2 = x1 + cellsize;
                DW_Point p1;
                DW_Point p2;
                DW_Point p3;
                DW_Point p4;
                p1 = new DW_Point((int) x1, (int) y1);
                p2 = new DW_Point((int) x2, (int) y1);
                p3 = new DW_Point((int) x2, (int) y2);
                p4 = new DW_Point((int) x1, (int) y2);
                addQuadFeature(p1, p2, p3, p4, gf, sfb, "", result);
            }
        }
        return result;
    }

    public static TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft,
            String outletTarget) {
        TreeSetFeatureCollection result;
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = getAdviceLeedsPointFeatureCollection(
                sft,
                outletTarget,
                gf,
                sfb);
        return result;
    }

    public static TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft,
            String outletTarget,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        TreeMap<String, DW_Point> tAdviceLeedsNamesAndPoints;
        tAdviceLeedsNamesAndPoints = DW_Processor.getAdviceLeedsNamesAndPoints();
        TreeMap<String, DW_Point> map = tAdviceLeedsNamesAndPoints;
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String outlet = ite.next();
            if (outlet.equalsIgnoreCase(outletTarget)) {
                DW_Point p = map.get(outlet);
                String name = outlet;
                addPointFeature(p, gf, sfb, name, result);
            }
        }
        return result;
    }

    /**
     * @param dir The directory in which the shapefile is stored.
     * @param shapefileFilename The shapefile filename.
     * @param fc The feature collection to be turned into a shapefile.
     * @param sft
     * @return shapefile File
     */
    public static File createShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename,
            TreeSetFeatureCollection fc,
            SimpleFeatureType sft) {
        File result;
        ShapefileDataStoreFactory sdsf;
        sdsf = new ShapefileDataStoreFactory();
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft,
                sdsf);
        return result;
    }

    /**
     * @param dir The directory in which the shapefile is stored.
     * @param shapefileFilename The shapefile filename.
     * @param fc The feature collection to be turned into a shapefile.
     * @param sft
     * @param sdsf
     * @return shapefile File
     */
    public static File createShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename,
            TreeSetFeatureCollection fc,
            SimpleFeatureType sft,
            ShapefileDataStoreFactory sdsf) {
        File result;
        result = DW_GeoTools.getShapefile(dir, shapefileFilename);
        if (!result.exists()) {
            DW_Shapefile.transact(
                    result,
                    sft,
                    fc,
                    sdsf);
        }
        return result;
    }

    public static File createAdviceLeedsPointShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getAdviceLeedsPointFeatureCollection(sft);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    public static File createGridLineShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename, // Better to internally generate this from other parameters?
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize) {
        File result;
        SimpleFeatureType sft;
        sft = getLineSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getGridLineFeatureCollection(
                sft,
                nrows,
                ncols,
                xllcorner,
                yllcorner,
                cellsize);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    public static File createPolygonShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename, // Better to internally generate this from other parameters?
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize) {
        File result;
        SimpleFeatureType sft;
        sft = getPolygonSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getGridPolyFeatureCollection(
                sft,
                nrows,
                ncols,
                xllcorner,
                yllcorner,
                cellsize);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    /**
     * @param dir
     * @param shapefileFilename
     * @param outletTarget
     * @return
     */
    public static File createAdviceLeedsPointShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename,
            String outletTarget) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getAdviceLeedsPointFeatureCollection(
                sft, outletTarget);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    /**
     * @return
     */
    public static DW_Shapefile getAdviceLeedsPointDW_Shapefile() {
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
        return new DW_Shapefile(tAdviceLeedsPointShapefile);
    }

    /**
     * @return
     */
    public static ArrayList<DW_Shapefile> getAdviceLeedsPointDW_Shapefiles() {
        ArrayList<DW_Shapefile> result;
        result = new ArrayList<DW_Shapefile>();
        ArrayList<String> tAdviceLeedsServiceNames;
        tAdviceLeedsServiceNames = DW_Processor.getAdviceLeedsServiceNames();
        Iterator<String> ite;
        ite = tAdviceLeedsServiceNames.iterator();
        while (ite.hasNext()) {
            String tAdviceLeedsPointName = ite.next();
            File tAdviceLeedsPointShapefileDir;
            tAdviceLeedsPointShapefileDir = new File(
                    DW_Files.getGeneratedAdviceLeedsDir(),
                    tAdviceLeedsPointName);
            String tAdviceLeedsPointShapefileFilename;
            tAdviceLeedsPointShapefileFilename = tAdviceLeedsPointName + ".shp";
            File tAdviceLeedsPointShapefile;
            tAdviceLeedsPointShapefile = createAdviceLeedsPointShapefileIfItDoesNotExist(
                    tAdviceLeedsPointShapefileDir,
                    tAdviceLeedsPointShapefileFilename,
                    tAdviceLeedsPointName);
            result.add(new DW_Shapefile(tAdviceLeedsPointShapefile));
        }
        return result;
    }
    
    /**
     * @param f
     * @return ArcGridReader
     */
    public static ArcGridReader getArcGridReader(File f) {
        ArcGridReader result = null;
        try {
            result = new ArcGridReader(f);
        } catch (DataSourceException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("f.toString()" + f.toString());
        }
        return result;
    }
    
    /**
     * 
     * @param agr
     * @return 
     */
    public static GridCoverage2D getGridCoverage2D(
        ArcGridReader agr) {
        GridCoverage2D result = null;
        try {
            if (agr != null) {
                result = agr.read(null);
            }
        } catch (IOException ex) {
            Logger.getLogger(DW_Maps.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
