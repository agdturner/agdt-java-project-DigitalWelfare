package uk.ac.leeds.ccg.projects.dw.visualisation.mapping;

import uk.ac.leeds.ccg.geotools.Geotools_StyleParameters;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.geotools.Geotools_Maps;
import uk.ac.leeds.ccg.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_Maps extends Geotools_Maps {

    protected final transient DW_Environment env;
    protected transient DW_Geotools geotools;
    protected final transient DW_Files files;
    protected final transient UKP_Data ONSPD_Handler;

    //private TreeMap<String, ONSPD_Point>[] ONSPDlookups;
    //private TreeMap<String, TreeMap<String, ONSPD_Point>> ONSPDlookups;
//    private TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups;
    /**
     * For storing level(s) (OA, LSOA, MSOA, PostcodeSector, PostcodeUnit, ...)
     */
    protected String level;
    protected ArrayList<String> levels;
    protected Geotools_StyleParameters styleParameters;

    public boolean doDebug;

    public DW_Maps(DW_Environment de) throws IOException {
        this.env = de;
        this.geotools = de.getGeotools();
        files = de.files;
        ONSPD_Handler = de.getONSPD_Handler();
        //DW_Postcode_Handler = env.getDW_Postcode_Handler(); Stack overflow doing this here.
    }

//    //public TreeMap<String, ONSPD_Point>[] getONSPDlookups() {
//    public TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> getONSPDlookups() {
//        if (ONSPDlookups == null) {
//            initONSPDLookups();
//        }
//        return ONSPDlookups;
//    }
//
//    protected void initPostcode_Handler() {
//        if (UKP_Data == null) {
////            UKP_Data = new UKP_Data(env.ONSPD_Environment);
//            UKP_Data = new UKP_Data(new ONSPD_Environment(files.getDataDir()));
//        }
//    }
//
//    public void initONSPDLookups() {
//        initPostcode_Handler();
//        ONSPDlookups = new TreeMap<>();
//        levels = new ArrayList<>();
//        levels.add("Unit");
//        //levels.add("Sector");
//        //levels.add("Area");
//        TreeMap<ONSPD_YM3, File> ONSPDFiles;
//        ONSPDFiles = files.getONSPD_Files().getInputONSPDFiles();
//        Iterator<String> ite2;
//        ite2 = levels.iterator();
//        while (ite2.hasNext()) {
//            level = ite2.next();
//            TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>> ONSPDlookup;
//            ONSPDlookup = UKP_Data.getPostcodeUnitPointLookups(true,
//                    ONSPDFiles,
//                    UKP_Data.getDefaultLookupFilename());
//            ONSPDlookups.put(level, ONSPDlookup);
//        }
//    }
    public DW_Shapefile getPostcodeUnitPoly_DW_Shapefile(
            DW_Environment env,
            ShapefileDataStoreFactory sdsf) throws IOException {
        DW_Shapefile r;
        // Code Point Boundaries
        TreeSet<String> postcodes = new TreeSet<>();
        postcodes.add("ls");
        postcodes.add("hg");
        postcodes.add("yo");
        postcodes.add("wf");
        postcodes.add("bd");
        postcodes.add("hd");
        postcodes.add("hx");
        String polyShapefilename;
        polyShapefilename = "LS_HG_YO_WF_BD_HD_HX_PostcodeUnit.shp";
        String year;
        year = "2012";
        Path generatedCodePointDir = Paths.get(env.files.getGeneratedCodePointDir().toString(),
                year);
        Path polyShapefile = Paths.get(generatedCodePointDir.toString(), polyShapefilename);
        Files.createDirectories(polyShapefile);
        polyShapefile = Paths.get(polyShapefile.toString(), polyShapefilename);
        r = getPostcodeUnitPoly_DW_Shapefile(sdsf, polyShapefile,
                generatedCodePointDir, postcodes);
        return r;
    }

    public DW_Shapefile getPostcodeUnitPoly_DW_Shapefile(
            ShapefileDataStoreFactory sdsf,
            Path file,
            Path generatedCodePointDir,
            TreeSet<String> postcodes) {
        DW_Shapefile result;
        if (!Files.exists(file)) {
            // Put it together from source...
            TreeSetFeatureCollection tsfc = new TreeSetFeatureCollection();
            SimpleFeatureType sft = null;
            SimpleFeatureBuilder sfb = null;
            Iterator<String> ite = postcodes.iterator();
            while (ite.hasNext()) {
                String postcode = ite.next();
                Path aShapefile = Paths.get(generatedCodePointDir.toString(),
                        postcode + ".shp", postcode + ".shp");
                DW_Shapefile aDW_Shapefile = new DW_Shapefile(aShapefile);
                FeatureCollection fc = aDW_Shapefile.getFeatureCollection();
                FeatureIterator fi = fc.features();
                if (sft == null) {
                    sft = (SimpleFeatureType) fc.getSchema();
                }
                if (sfb == null) {
                    sfb = new SimpleFeatureBuilder(sft);
                }
                while (fi.hasNext()) {
                    SimpleFeature sf;
                    sf = (SimpleFeature) fi.next();
                    tsfc.add(sf);
                }
                //fi.close();
            }
            DW_Shapefile.transact(file, sft, tsfc, sdsf);
        }
        result = new DW_Shapefile(
                file);
        return result;
    }

    /**
     * level = "District", "Sector" or "Area"
     *
     * @param env
     * @param level
     * @return
     */
    public DW_Shapefile getPostcodePoly_DW_Shapefile(DW_Environment env,
            String level) throws IOException {
        String name = "Postal" + level + ".shp";
        return new DW_Shapefile(Paths.get(env.files.getInputPostcodeDir().toString(),
                "BoundaryData", "GBPostcodeAreaSectorDistrict", "GB_Postcodes", name, name));
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
    public void selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> codesToSelect,
            //String attributeName, 
            String targetPropertyName,
            Path outputFile) {
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

    @Override
    public Path getOutputImageFile(Path outputFile, String outputType) {
        String filename = outputFile.getFileName().toString();
        String fn = filename.substring(0, filename.length() - 4)
                + "." + outputType;
        return Paths.get(outputFile.getParent().toString(), fn);
    }

    /**
     * @param level
     * @param area
     * @return
     */
    public TreeMap<String, Integer> getPopData(String level, String area)
            throws IOException {
        TreeMap<String, Integer> r = new TreeMap<>();
        Path file = Paths.get(env.files.getInputCensus2011AttributeDataDir(level).toString(),
                area, "pop.csv");
        try {
            try (BufferedReader br = Generic_IO.getBufferedReader(file)) {
                StreamTokenizer st = new StreamTokenizer(br);
                Generic_IO.setStreamTokenizerSyntax1(st);
                int token = st.nextToken();
//            //skip header (2 lines)
//            st.nextToken();
//            st.nextToken();
//            st.nextToken();
//            st.nextToken();
//            long RecordID = 0;
                String line;
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
                                    r.put(censusArea, pop);
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
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + "in DW_Maps.getPopData(String=" + level + ",String=" + area + ")");
        }
        return r;
    }

    /**
     * @param level
     * @return File
     */
    protected Path getAreaBoundaryShapefile(String level) throws IOException {
        Path r = null;
        String name;
        if (level.startsWith("Postcode")) {
            if (level.equalsIgnoreCase("PostcodeUnit")) {
                // Get Postcode Unit Boundaries
                r = getPostcodeUnitPoly_DW_Shapefile(env,
                        new ShapefileDataStoreFactory()).getFile();
            }
            if (level.equalsIgnoreCase("PostcodeSector")) {
                // Get PostcodeSector Boundaries
                r = getPostcodePoly_DW_Shapefile(env, "Sector").getFile();
            }
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                // Get PostcodeSector Boundaries
                r = getPostcodePoly_DW_Shapefile(env, "District").getFile();
            }
        } else {
            if (level.equalsIgnoreCase("LAD")) {
                name = "England_lad_2011_gen_clipped.shp";
            } else if (level.equalsIgnoreCase("OA")) {
                name = "England_oa_2011_gen_clipped.shp";
            } else {
                name = level + "_2011_EW_BGC.shp";
            }
            r = Paths.get(env.files.getInputCensus2011Dir(level).toString()
                    , "/BoundaryData/", name, name);
        }
        return r;
    }

    /**
     * @param area
     * @param level
     * @return File
     */
    protected Path getCensusBoundaryShapefile(String area, String level)
            throws IOException {
        String name = area + "_" + level + "_2011_EW_BGC.shp";
        Path boundaryDirectory = Paths.get(
                env.files.getGeneratedCensus2011Dir(level).toString(),
                "/BoundaryData/" + name);
        if (!Files.exists(boundaryDirectory)) {
            Files.createDirectories(boundaryDirectory);
        }
        return Paths.get(boundaryDirectory.toString(), name);
    }

    /**
     * @return File
     */
    protected Path getLeedsPostcodeSectorShapefile() throws IOException {
        String fileAndDirName = "LSPostalSector.shp";
        return Paths.get(env.files.getInputPostcodeDir().toString(),
                "BoundaryData", fileAndDirName, fileAndDirName);
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
    /**
     *
     * @param sdsf
     * @param fc
     * @param sft
     * @param areaCodes For filtering.
     * @param levelData Keys are areaCodes.
     * @param attributeName Expected to be either "Count", "Density" or "Rate"
     * @param targetPropertyName
     * @param population Can be null. Keys are areaCodes.
     * @param populationMultiplier For multiplying count by before dividing by
     * population
     * @param outputFile
     * @param filter If filter &lt; 3 only those areas in areaCodes are mapped.
     * Otherwise additional areas for areaCodes in levelData are also mapped.
     * @param deprivationClasses
     * @param deprivationDataRecords
     * @param deprivationClass
     * @param countClientsInAndOutOfRegion
     * @return
     */
    public TreeMap<Integer, Integer> selectAndCreateNewShapefile(
            ShapefileDataStoreFactory sdsf,
            FeatureCollection fc,
            SimpleFeatureType sft,
            TreeSet<String> areaCodes,
            TreeMap<String, Integer> levelData,
            String attributeName,
            String targetPropertyName,
            TreeMap<String, Integer> population,
            double populationMultiplier,
            Path outputFile,
            int filter,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            boolean countClientsInAndOutOfRegion) {
        TreeMap<Integer, Integer> inAndOutOfRegionCount = null;
        //        summariseAttributes(sft);
        // Initialise the collection of new Features
        TreeSetFeatureCollection tsfc;
        tsfc = new TreeSetFeatureCollection();
        // Create SimpleFeatureBuilder
        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
        SimpleFeatureBuilder sfb;
        sfb = new SimpleFeatureBuilder(sft);
        if (countClientsInAndOutOfRegion) {
            inAndOutOfRegionCount = new TreeMap<>();
            inAndOutOfRegionCount.put(0, 0);
            inAndOutOfRegionCount.put(1, 0);
        }
        Set<String> keySet = levelData.keySet();
        FeatureIterator featureIterator;
        featureIterator = fc.features();
        int id_int = 0;
        while (featureIterator.hasNext()) {
            Feature inputFeature = featureIterator.next();
            SimpleFeature sf;
            Collection<Property> properties;
            properties = inputFeature.getProperties();
            Iterator<Property> itep;
            // get Area
            double area = 0;
            itep = properties.iterator();
            while (itep.hasNext()) {
                Property p = itep.next();
                //System.out.println("Property " + p.toString());
                String propertyName = p.getName().toString();
                //System.out.println("PropertyName " + propertyName);
//                PropertyDescriptor pd;
//                pd = p.getDescriptor();
                if (propertyName.equalsIgnoreCase("the_geom")) {
                    Geometry g;
                    g = (Geometry) p.getValue();
                    area = g.getArea();
                    try {
                        Polygon poly;
                        poly = (Polygon) g;
                        area = poly.getArea();
                    } catch (ClassCastException e) {
                        int debug = 1;
                    }
                    try {
                        MultiPolygon multipoly;
                        multipoly = (MultiPolygon) g;
                        area = multipoly.getArea();
                    } catch (ClassCastException e) {
                        int debug = 1;
                    }
                }
            }
            itep = properties.iterator();
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
                    sf = (SimpleFeature) inputFeature;
                    id_int = calculate0(
                            filter,
                            areaCodes,
                            levelData,
                            keySet,
                            attributeName,
                            sf,
                            sfb,
                            tsfc,
                            id_int,
                            area,
                            population,
                            valueString,
                            populationMultiplier,
                            countClientsInAndOutOfRegion,
                            inAndOutOfRegionCount);
                }
            }
        }
        featureIterator.close();
        DW_Shapefile.transact(outputFile, sft, tsfc, sdsf);
        return inAndOutOfRegionCount;
    }

    /**
     *
     * @param filter
     * @param areaCodes
     * @param levelData
     * @param levelDataKeySet
     * @param attributeName
     * @param sf
     * @param sfb
     * @param tsfc
     * @param id_int
     * @param area
     * @param population
     * @param areaCode
     * @param populationMultiplier
     * @param countClientsInAndOutOfRegion
     * @param inAndOutOfRegionCount
     */
    private int calculate0(
            int filter,
            TreeSet<String> areaCodes,
            TreeMap<String, Integer> levelData,
            Set<String> levelDataKeySet,
            String attributeName,
            SimpleFeature sf,
            SimpleFeatureBuilder sfb,
            TreeSetFeatureCollection tsfc,
            int id_int,
            double area,
            TreeMap<String, Integer> population,
            String areaCode,
            double populationMultiplier,
            boolean countClientsInAndOutOfRegion,
            TreeMap<Integer, Integer> inAndOutOfRegionCount) {
        Integer clientCount = levelData.get(areaCode);
        if (clientCount == null) {
            clientCount = 0;
        }

//        if (areaCode.equalsIgnoreCase("LS7")) {
//            if (doDebug) {
//                int debug = 1;
//            }
//        }
        //if (clientCount != null) {
        if (filter < 3) {
            if (areaCodes.contains(areaCode)) {
                id_int = calculate(
                        clientCount,
                        attributeName,
                        sf,
                        sfb,
                        tsfc,
                        id_int,
                        area,
                        population,
                        areaCode,
                        populationMultiplier,
                        countClientsInAndOutOfRegion,
                        inAndOutOfRegionCount);
            } else if (countClientsInAndOutOfRegion) {
                Generic_Collections.addToMapInteger(
                        inAndOutOfRegionCount, 0, clientCount);
            } //DEBUG
        } else if (levelDataKeySet.contains(areaCode) || areaCodes.contains(areaCode)) {
            id_int = calculate(
                    clientCount,
                    attributeName,
                    sf,
                    sfb,
                    tsfc,
                    id_int,
                    area,
                    population,
                    areaCode,
                    populationMultiplier,
                    countClientsInAndOutOfRegion,
                    inAndOutOfRegionCount);
        } else if (countClientsInAndOutOfRegion) {
            Generic_Collections.addToMapInteger(
                    inAndOutOfRegionCount, 0, clientCount);
        }
        //}
        return id_int;
    }

    /**
     *
     * @param clientCount
     * @param attributeName
     * @param inputFeature
     * @param sfb
     * @param tsfc
     * @param id_int
     * @param area
     * @param population
     * @param areaCode
     * @param populationMultiplier
     * @param countClientsInAndOutOfRegion
     * @param inAndOutOfRegionCount
     * @return
     */
    private int calculate(
            Integer clientCount,
            String attributeName,
            SimpleFeature inputFeature,
            SimpleFeatureBuilder sfb,
            TreeSetFeatureCollection tsfc,
            int id_int,
            double area,
            TreeMap<String, Integer> population,
            String areaCode,
            double populationMultiplier,
            boolean countClientsInAndOutOfRegion,
            TreeMap<Integer, Integer> inAndOutOfRegionCount) {
        String id = "" + id_int;
        if (attributeName.equalsIgnoreCase("Count")) {
//            if (id_int == 0) {
//                System.out.println("id, areaCode, clientCount");
//            }
            addFeatureAttributeAndAddToFeatureCollection(
                    (SimpleFeature) inputFeature, sfb, clientCount, tsfc, id);
//            System.out.println("" + id_int + ", " + areaCode + ", " + clientCount);
        }
        if (attributeName.equalsIgnoreCase("Density")) {
//            if (id_int == 0) {
//                System.out.println("id, areaCode, clientCount, area, density");
//            }
            double densityValue;
            densityValue = ((double) clientCount / area) * 1000000.0D; // * 1000000 for 100 hectares
            addFeatureAttributeAndAddToFeatureCollection(
                    (SimpleFeature) inputFeature, sfb, densityValue, tsfc, id);
//            if (densityValue < 0) {
//                int debug = 1;
//            }
//            System.out.println("" + id_int + ", " + areaCode + ", " + clientCount + ", " + area + ", " + densityValue);
        }
        if (attributeName.equalsIgnoreCase("Rate")) {
//            if (id_int == 0) {
//                System.out.println("id, areaCode, clientCount, area, pop");
//            }
            double rate;
            Integer pop = population.get(areaCode);
            rate = ((double) clientCount / (double) pop) * populationMultiplier;
            addFeatureAttributeAndAddToFeatureCollection(
                    (SimpleFeature) inputFeature, sfb, rate, tsfc, id);
//            System.out.println("" + id_int + ", " + areaCode + ", " + clientCount + ", " + area + ", " + pop);
        }
        if (countClientsInAndOutOfRegion) {
            Generic_Collections.addToMapInteger(inAndOutOfRegionCount, 1, clientCount);
        }
        id_int++;
        return id_int;
    }

    @Override
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
            result += "ClippedToLeedsAndNearNeighbouringLADs";
        }
        if (filter == 3) {
            result += "";
        }
        return result;
    }

    public DW_Shapefile getCommunityAreasDW_Shapefile() throws IOException {
        String name = "communityareas_region.shp";
        Path p = Paths.get(env.files.getInputDir().toString(),
                "CommunityAreas", name, name);
        return new DW_Shapefile(p);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Geotools_StyleParameters getStyleParameters() {
        return styleParameters;
    }

//    public void setONSPDlookups(TreeMap<String, TreeMap<ONSPD_YM3, TreeMap<String, ONSPD_Point>>> ONSPDlookups) {
//        this.ONSPDlookups = ONSPDlookups;
//    }
    public void setStyleParameters(Geotools_StyleParameters styleParameters) {
        this.styleParameters = styleParameters;
    }

}
