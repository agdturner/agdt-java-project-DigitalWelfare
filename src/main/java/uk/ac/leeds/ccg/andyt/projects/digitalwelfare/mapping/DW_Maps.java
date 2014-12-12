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
import com.vividsolutions.jts.geom.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.DW_Census;
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

    public File digitalWelfareDir;
    public File _DW_dir;
    private static TreeMap<String, DW_Point> _ONSPDlookup;

    public DW_Maps() {
    }

    public DW_Maps(File digitalWelfareDir) {
        this.digitalWelfareDir = digitalWelfareDir;
    }

    public static TreeMap<String, DW_Point> get_ONSPDlookup() {
        if (_ONSPDlookup == null) {
            init_ONSPDLookup();
        }
        return _ONSPDlookup;
    }

    /**
     *
     * @param level
     * @param targetPropertyName
     * @return
     */
    public static Object[] getCensusAreaCodesAndLeedsShapefile(
            String level,
            String targetPropertyName,
            ShapefileDataStoreFactory sdsf) {
        Object[] result = new Object[10];
        // Read Leeds level Census Codes
        TreeSet<String> tLeedsCensusAreaCodes;
        tLeedsCensusAreaCodes = DW_Census.getLADCensusCodes(level, "Leeds");
        result[0] = tLeedsCensusAreaCodes;
        // Read Census Boundary Data of level
        File levelShapefile = getCensusBoundaryShapefile(level);
        result[1] = levelShapefile;
        Object[] levelFeatureCollectionAndType;
        levelFeatureCollectionAndType = DW_GeoTools.getFeatureCollectionAndType(
                levelShapefile);
        FeatureCollection levelFC;
        levelFC = (FeatureCollection) levelFeatureCollectionAndType[0];
        result[2] = levelFC;
        SimpleFeatureType levelSFT;
        levelSFT = (SimpleFeatureType) levelFeatureCollectionAndType[1];
        result[3] = levelSFT;
        /*
         * Filter Leeds LAD LSOAs and write new Shapefile if it does not exist
         * and set this Leeds LAD LSOA Boundary Data Shapefile as the
         * backgroundShapefile
         */
        File leedsShapefile = getCensusBoundaryShapefile(
                "Leeds",
                level);
        if (!leedsShapefile.exists()) {
            selectAndCreateNewShapefile(
                    sdsf, levelFC, levelSFT, tLeedsCensusAreaCodes,
                    targetPropertyName, leedsShapefile);
        }
        result[4] = leedsShapefile;

        // Read Leeds and neighbouring District LADs Census Codes for level
        TreeSet<String> tLeedsAndNeighbouringLADsCensusAreaCodes;
        tLeedsAndNeighbouringLADsCensusAreaCodes = DW_Census.getLADCensusCodes(
                level, "LeedsAndNeighbouringLADs");
        result[5] = tLeedsAndNeighbouringLADsCensusAreaCodes;

        // Read Leeds and neighbouring District LADs and Craven And York Census Codes
        TreeSet<String> tLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes;
        tLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes = DW_Census.getLADCensusCodes(
                level, "LeedsAndNeighbouringLADsAndCravenAndYork");
        result[6] = tLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes;

        // Get LAD shapefiles
        String levelLAD = "LAD";
        // Set Leeds Census Codes for LAD
        TreeSet<String> tLeedsLADCensusAreaCodes;
        tLeedsLADCensusAreaCodes = new TreeSet<String>();
        // Code for Leeds 00DA = E08000035
        tLeedsLADCensusAreaCodes.add("E08000035");

        // Read LAD Census Boundary Data
        File tLADShapefile = getCensusBoundaryShapefile(levelLAD);
        Object[] tLADFeatureCollectionAndType;
        tLADFeatureCollectionAndType = DW_GeoTools.getFeatureCollectionAndType(tLADShapefile);
        FeatureCollection tLADFC;
        tLADFC = (FeatureCollection) tLADFeatureCollectionAndType[0];
        SimpleFeatureType tLADSFT;
        tLADSFT = (SimpleFeatureType) tLADFeatureCollectionAndType[1];

        // Select Leeds from LAD Census Boundary Data
        File tLeedsLADShapefile = getCensusBoundaryShapefile(
                "Leeds",
                levelLAD);
        if (!tLeedsLADShapefile.exists()) {
            selectAndCreateNewShapefile(
                    sdsf, tLADFC, tLADSFT, tLeedsLADCensusAreaCodes,
                    "CODE", tLeedsLADShapefile);
        }
        result[7] = tLeedsLADShapefile;

        // Select LeedsAndNeighbouringLADs from LAD Census Boundary Data
        // Code for Selby 36UH = E07000169
        // Code for Bradford 00CX = E08000032
        // Code for Kirklees 00CZ = E08000034
        // Code for Wakefield 00DB = E08000036
        // Code for Harrogate 36UD = E07000165
        TreeSet<String> tLeedsAndNeighbouringLADsLADCodes;
        tLeedsAndNeighbouringLADsLADCodes = new TreeSet<String>();
        tLeedsAndNeighbouringLADsLADCodes.addAll(
                tLeedsCensusAreaCodes);
        tLeedsAndNeighbouringLADsLADCodes.add("E07000169");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000032");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000034");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000036");
        tLeedsAndNeighbouringLADsLADCodes.add("E07000165");
        File tLeedsAndNeighbouringLADsShapefile = getCensusBoundaryShapefile(
                "LeedsAndNeighbouringLADs",
                levelLAD);
        if (!tLeedsAndNeighbouringLADsShapefile.exists()) {
            selectAndCreateNewShapefile(
                    sdsf, tLADFC, tLADSFT, tLeedsAndNeighbouringLADsLADCodes,
                    "CODE", tLeedsAndNeighbouringLADsShapefile);
        }
        result[8] = tLeedsAndNeighbouringLADsShapefile;
        // Code for Calderdale 00CY = E08000033
        // Code for Craven 36UB = E07000163
        // Code for York 00FF = E06000014
        TreeSet<String> tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes;
        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes = new TreeSet<String>();
        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes.addAll(
                tLeedsAndNeighbouringLADsLADCodes);
        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes.add("E08000033");
        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes.add("E07000163");
        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes.add("E06000014");
        File tLeedsAndNeighbouringLADsAndCravenAndYorkShapefile = getCensusBoundaryShapefile(
                "LeedsAndNeighbouringLADsAndCravenAndYork",
                levelLAD);
        if (!tLeedsAndNeighbouringLADsAndCravenAndYorkShapefile.exists()) {
            selectAndCreateNewShapefile(
                    sdsf, tLADFC, tLADSFT, tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes,
                    "CODE", tLeedsAndNeighbouringLADsAndCravenAndYorkShapefile);
        }
        result[9] = tLeedsAndNeighbouringLADsAndCravenAndYorkShapefile;
        return result;
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
                        String id = "" + id_int;
                        addFeatureToFeatureCollection((SimpleFeature) inputFeature, sfb, tsfc, id);
                        id_int++;
                    }
                }
            }
        }
        DW_GeoTools.transact(outputFile, sft, tsfc, sdsf);
    }

    public static void init_ONSPDLookup() {
        File generatedONSPDDir = DW_Files.getGeneratedONSPDDir();
        String tONSPDProcessedFilename;
        tONSPDProcessedFilename = "PostcodeLookUp_TreeMap_String_Point.thisFile";
        File tONSPDProcessedFile = new File(generatedONSPDDir, tONSPDProcessedFilename);
        if (tONSPDProcessedFile.exists()) {
            _ONSPDlookup = PostcodeGeocoder.getLookupPointFromPostcode(tONSPDProcessedFile);
        } else {
            File tONSPDNov2013DirectoryFile = new File(DW_Files.getInputONSPDDir(), "ONSPD_NOV_2013");
            File tONSPDNov2013DataFile = new File(tONSPDNov2013DirectoryFile, "Data/ONSPD_NOV_2013_UK.csv");
            PostcodeGeocoder pg = new PostcodeGeocoder(tONSPDNov2013DataFile, tONSPDProcessedFile);
            _ONSPDlookup = pg.generatePostCodePointLookup();
        }
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
     * getLSOAData(digitalWelfareDir,filenames[i]); ----------------------------
     * result[1] is the max of all the maximum counts.
     */
    protected static Object[] getLSOAData(
            File dir,
            String[] filenames) {
        Object[] result = new Object[2];
        int length = filenames.length;
        Object[] resultPart0 = new Object[length];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            Object[] LSOAData = getLSOAData(
                    dir,
                    filenames[i]);
            resultPart0[i] = LSOAData;
            max = Math.max(max, (Integer) LSOAData[1]);
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
    protected static Object[] getLSOAData(
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
     * @return
     */
    protected TreeMap<String, Integer> getPopData(
            String level) {
        TreeMap<String, Integer> result;
        result = new TreeMap<String, Integer>();
        File file = new File(
                DW_Files.getInputCensus2011Dir(level),
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
                        result.put(split[0], new Integer(split[1]));
                        break;
                }
                token = st.nextToken();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
            name = level + "_2011_EW_BGC.shp";
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
     * @param level
     * @return File
     */
    protected static File getCensusBoundaryShapefile(
            String area,
            String level) {
        File result;
        String name = area + level + "_2011_EW_BGC.shp";
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
    protected static File getPostcodeShapefile() {
        File result;
        File boundaryDirectory = new File(
                DW_Files.getInputPostcodeDir(),
                "/BoundaryData/GBPostcode/LSPostcodes.shp");
        result = new File(boundaryDirectory,
                "PostalSector.shp");
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
            TreeSet<String> tLSOACodes,
            TreeMap<String, Integer> tLSOAData,
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
        Set<String> tLSOADataKeySet = tLSOAData.keySet();
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
                        if (tLSOACodes.contains(valueString)) {
                            Integer clientCount = tLSOAData.get(valueString);
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
                        if (tLSOADataKeySet.contains(valueString) || tLSOACodes.contains(valueString)) {
                            Integer clientCount = tLSOAData.get(valueString);
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
        DW_GeoTools.transact(outputFile, sft, tsfc, sdsf);
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
            TreeSet<String> tLSOACodes,
            TreeMap<String, Integer> tLSOAData,
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
        Set<String> tLSOADataKeySet = tLSOAData.keySet();
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
                        if (thisDeprivationClass.intValue() == deprivationClass.intValue()) {
                            if (filter < 3) {
                                if (tLSOACodes.contains(valueString)) {
                                    Integer clientCount = tLSOAData.get(valueString);
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
                                if (tLSOADataKeySet.contains(valueString) || tLSOACodes.contains(valueString)) {
                                    Integer clientCount = tLSOAData.get(valueString);
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
        DW_GeoTools.transact(outputFile, sft, tsfc, sdsf);
    }

    /*
     * Get an output file name and create the new shapefile
     */
    public void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> tLSOACodes,
            TreeMap<String, Integer> tLSOAData,
            TreeMap<String, Integer> pop,
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
        Set<String> tLSOADataKeySet = tLSOAData.keySet();
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
                        if (tLSOACodes.contains(valueString)) {
                            Integer clientCount = tLSOAData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                Integer population = pop.get(valueString);
                                double clientRate = (double) clientCount / (double) population;
                                String id = "" + id_int;
                                addFeatureAttributeAndAddToFeatureCollection(
                                        (SimpleFeature) inputFeature, sfb, clientRate, tsfc, id);
                                id_int++;
                            }
                        }
                    } else {
                        if (tLSOADataKeySet.contains(valueString) || tLSOACodes.contains(valueString)) {
                            Integer clientCount = tLSOAData.get(valueString);
                            //                            if (clientCount == null) {
                            //                                clientCount = 0;
                            //                            }
                            if (clientCount != null) {
                                Integer population = pop.get(valueString);
                                double clientRate = (double) clientCount / (double) population;
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
        DW_GeoTools.transact(outputFile, sft, tsfc, sdsf);
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
        
//        TreeMap<String, DW_Point> tOutletsAndPoints;
//        tOutletsAndPoints = DW_Processor.getOutletsAndPoints();
//        TreeMap<String, DW_Point> map = tOutletsAndPoints;
        TreeMap<String, DW_Point> tAdviceLeedsNamesAndPoints;
        tAdviceLeedsNamesAndPoints = DW_Processor.getAdviceLeedsNamesAndPoints();
        TreeMap<String, DW_Point> map = tAdviceLeedsNamesAndPoints;

        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = new TreeSetFeatureCollection();
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String outlet = ite.next();
            DW_Point p = map.get(outlet);
            String name = outlet;
            Point point = geometryFactory.createPoint(new Coordinate(p.getX(), p.getY()));
            sfb.add(point);
            sfb.add(name);
            SimpleFeature feature = sfb.buildFeature(null);
            result.add(feature);
        }
        return result;
    }
    
    public static SimpleFeatureType getPointSimpleFeatureType() {
        SimpleFeatureType result = null;
        try {
            result = DataUtilities.createType(
                    "Location",
                    "the_geom:Point:srid=27700," + // <- the geometry attribute: Point type
                    "name:String," // <- a String attribute
            );
        } catch (SchemaException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static File createAdviceLeedsPointShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename) {
        File result;
        File shapefileDir = new File(
                dir,
                shapefileFilename);
        shapefileDir.mkdirs();
        result = new File(
                shapefileDir,
                shapefileFilename);
        if (!result.exists()) {
        ShapefileDataStoreFactory sdsf;
        sdsf = new ShapefileDataStoreFactory();
        SimpleFeatureType pointSFT = getPointSimpleFeatureType();
        TreeSetFeatureCollection point_fc;
        point_fc = DW_Maps.getAdviceLeedsPointFeatureCollection(pointSFT);
        DW_GeoTools.transact(
                result,
                pointSFT,
                point_fc,
                sdsf);
        }
        return result;
    }
}
