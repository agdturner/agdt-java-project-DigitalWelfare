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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping;

import com.vividsolutions.jts.geom.Geometry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public class DW_AreaCodesAndShapefiles extends DW_Object {

    protected DW_Maps Maps;

    private final TreeSet<String> LeedsAreaCodes;
    private final TreeSet<String> LeedsAndNeighbouringLADAreaCodes;
    private final TreeSet<String> LeedsAndNearNeighbouringLADAreaCodes;
    private final DW_Shapefile LevelDW_Shapefile;
//    private final FeatureCollection levelFC;
//    private final SimpleFeatureType levelSFT;
    private final DW_Shapefile LeedsLevelDW_Shapefile;
//    private final FeatureCollection leedsLADFC;
//    private final SimpleFeatureType leedsLADSFT;
    private final DW_Shapefile LeedsLADDW_Shapefile;
    private final DW_Shapefile LeedsAndNeighbouringLADDW_Shapefile;
    private final DW_Shapefile LeedsAndNearNeighbouringLADDW_Shapefile;

    public DW_AreaCodesAndShapefiles(DW_Environment e) {
        super(e);
        LeedsAreaCodes = null;
        LeedsAndNeighbouringLADAreaCodes = null;
        LeedsAndNearNeighbouringLADAreaCodes = null;
        LevelDW_Shapefile = null;
        LeedsLevelDW_Shapefile = null;
        LeedsLADDW_Shapefile = null;
        LeedsAndNeighbouringLADDW_Shapefile = null;
        LeedsAndNearNeighbouringLADDW_Shapefile = null;
    }

    /**
     *
     * @param env
     * @param level
     * @param targetPropertyName
     * @param sdsf
     */
    public DW_AreaCodesAndShapefiles(
            DW_Environment env,
            String level,
            String targetPropertyName,
            ShapefileDataStoreFactory sdsf) {
        super(env);
        this.Maps = env.getMaps();
        String tLeedsString = "Leeds";
        String tLeedsAndNeighbouringLADsString;
        tLeedsAndNeighbouringLADsString = "LeedsAndNeighbouringLADs";
        String tLeedsAndNearNeighbouringLADsString;
        tLeedsAndNearNeighbouringLADsString = "LeedsAndNearNeighbouringLADs";
        // Set LAD DW_Shapefiles
        // ---------------------
        // Get Leeds LAD Shapefile
        TreeSet<String> tLeedsLADCensusAreaCode;
        tLeedsLADCensusAreaCode = new TreeSet<>();
        // Code for Leeds 00DA = E08000035
        tLeedsLADCensusAreaCode.add("E08000035");
        LeedsLADDW_Shapefile = getLADShapefile(tLeedsString,
                tLeedsLADCensusAreaCode, sdsf);
        // Get Leeds and Neighbouring LAD Shapefile
        TreeSet<String> tLeedsAndNeighbouringLADCodes;
        tLeedsAndNeighbouringLADCodes = new TreeSet<>();
        tLeedsAndNeighbouringLADCodes.addAll(
                tLeedsLADCensusAreaCode);
        // Code for Selby 36UH = E07000169
        // Code for Bradford 00CX = E08000032
        // Code for Kirklees 00CZ = E08000034
        // Code for Wakefield 00DB = E08000036
        // Code for Harrogate 36UD = E07000165
        tLeedsAndNeighbouringLADCodes.add("E07000169");
        tLeedsAndNeighbouringLADCodes.add("E08000032");
        tLeedsAndNeighbouringLADCodes.add("E08000034");
        tLeedsAndNeighbouringLADCodes.add("E08000036");
        tLeedsAndNeighbouringLADCodes.add("E07000165");
        LeedsAndNeighbouringLADDW_Shapefile = getLADShapefile(
                tLeedsAndNeighbouringLADsString,
                tLeedsAndNeighbouringLADCodes,
                sdsf);
        // Get Leeds and Near Neighbouring LAD Shapefile
        TreeSet<String> tLeedsAndNearNeighbouringLADCodes;
        tLeedsAndNearNeighbouringLADCodes = new TreeSet<>();
        tLeedsAndNearNeighbouringLADCodes.addAll(
                tLeedsAndNeighbouringLADCodes);
        // Code for Calderdale 00CY = E08000033
        // Code for Craven 36UB = E07000163
        // Code for York 00FF = E06000014
        tLeedsAndNearNeighbouringLADCodes.add("E08000033");
        tLeedsAndNearNeighbouringLADCodes.add("E07000163");
        tLeedsAndNearNeighbouringLADCodes.add("E06000014");
        LeedsAndNearNeighbouringLADDW_Shapefile = getLADShapefile(
                tLeedsAndNearNeighbouringLADsString,
                tLeedsAndNearNeighbouringLADCodes,
                sdsf);
        File censusDataDirectory = new File(
                env.files.getInputCensus2011AttributeDataDir(level),
                tLeedsString);
        if (level.equalsIgnoreCase("OA")
                || level.equalsIgnoreCase("LSOA")
                || level.equalsIgnoreCase("MSOA")) {
            // Read Census Boundary Data of level
            File levelShapefile = Maps.getAreaBoundaryShapefile(level);
            LevelDW_Shapefile = new DW_Shapefile(levelShapefile);

            // Read area level Census Codes
            LeedsAreaCodes = env.censusEnv.getCensusCodes(
                    tLeedsString,
                    level,
                    censusDataDirectory);
            // Read Leeds and neighbouring District LADs Census Codes for level
            LeedsAndNeighbouringLADAreaCodes = env.censusEnv.getCensusCodes(
                    tLeedsAndNeighbouringLADsString,
                    level,
                    censusDataDirectory);
            // Read Leeds and neighbouring District LADs and Craven And York Census Codes
            LeedsAndNearNeighbouringLADAreaCodes = env.censusEnv.getCensusCodes(
                    tLeedsAndNearNeighbouringLADsString,
                    level,
                    censusDataDirectory);

            FeatureCollection levelFC;
            SimpleFeatureType levelSFT;
            levelFC = LevelDW_Shapefile.getFeatureCollection();
            levelSFT = LevelDW_Shapefile.getSimpleFeatureType();

            /*
             * Filter Leeds LAD LSOAs and write new Shapefile if it does not exist
             * and set this Leeds LAD LSOA Boundary Data Shapefile as the
             * backgroundShapefile
             */
            File leedsLevelShapefile;
            leedsLevelShapefile = Maps.getCensusBoundaryShapefile(tLeedsString,
                    level);
            LeedsLevelDW_Shapefile = new DW_Shapefile(leedsLevelShapefile);
            if (!leedsLevelShapefile.exists()) {
                Maps.selectAndCreateNewShapefile(sdsf, levelFC, levelSFT,
                        LeedsAreaCodes, targetPropertyName, leedsLevelShapefile);
            }
        } else {
            File levelShapefile = Maps.getAreaBoundaryShapefile(level);
            if (levelShapefile == null) {
                int debug = 1;
            }
            LevelDW_Shapefile = new DW_Shapefile(levelShapefile);
            LeedsAreaCodes = getAreaCodesAndShapefile(tLeedsString, level,
                    targetPropertyName, LeedsLADDW_Shapefile, LevelDW_Shapefile);
            LeedsAndNeighbouringLADAreaCodes = getAreaCodesAndShapefile(
                    tLeedsAndNeighbouringLADsString, level, targetPropertyName,
                    LeedsLADDW_Shapefile, LevelDW_Shapefile);
            LeedsAndNearNeighbouringLADAreaCodes = getAreaCodesAndShapefile(
                    tLeedsAndNearNeighbouringLADsString, level,
                    targetPropertyName, LeedsLADDW_Shapefile, LevelDW_Shapefile);
            LeedsLevelDW_Shapefile = new DW_Shapefile(
                    getPostcodeShapefile(tLeedsString, level));
        }
    }

    public final TreeSet<String> getAreaCodesAndShapefile(String area,
            String level, String targetPropertyName,
            DW_Shapefile tLeedsLADDW_Shapefile,
            DW_Shapefile tLevelDW_Shapefile) {
        TreeSet<String> result;
        File censusDataDirectory = new File(
                env.files.getInputCensus2011AttributeDataDir(level), area);
        if (level.equalsIgnoreCase("OA")
                || level.equalsIgnoreCase("LSOA")
                || level.equalsIgnoreCase("MSOA")) {
            result = env.censusEnv.getCensusCodes(area, level, censusDataDirectory);
        } else {
            File tPostcodeShapefile;
            tPostcodeShapefile = getPostcodeShapefile(
                    area,
                    level);
            result = new TreeSet<>();
            File postcodeAreaCodesFile = getPostcodeDataAreaCodesFile(
                    area,
                    level);
            if (tPostcodeShapefile.exists()
                    && postcodeAreaCodesFile.exists()) {
                //Read files
                result = getAreaCodes(
                        area,
                        level);
            } else {
                tPostcodeShapefile.getParentFile().mkdirs();
                ShapefileDataStoreFactory sdsf;
                sdsf = new ShapefileDataStoreFactory();
                SimpleFeatureType sft = null;
                TreeSetFeatureCollection tsfc;
                tsfc = new TreeSetFeatureCollection();
                // Intersect and return
                FeatureCollection fc;
                fc = tLeedsLADDW_Shapefile.getFeatureCollection();
                FeatureCollection fc2;
                fc2 = tLevelDW_Shapefile.getFeatureCollection();
                FeatureIterator fi2;
                fi2 = fc2.features();
                while (fi2.hasNext()) {
                    SimpleFeature sf2;
                    sf2 = (SimpleFeature) fi2.next();
                    if (sft == null) {
                        sft = sf2.getFeatureType();
                    }
                    Geometry g2 = (Geometry) sf2.getDefaultGeometry();
//                        Polygon p2;
//                        p2 = (Polygon) sf2.getDefaultGeometry();
                    FeatureIterator fi;
                    fi = fc.features();
                    while (fi.hasNext()) {
                        SimpleFeature sf;
                        sf = (SimpleFeature) fi.next();
                        Geometry g = (Geometry) sf.getDefaultGeometry();
//                            Polygon p;
//                            p = (Polygon) sf.getDefaultGeometry();
                        Geometry intersection = g.intersection(g2);
                        if (intersection != null
                                && !intersection.isEmpty()) {
                            tsfc.add(sf2);
                            String postcode;
                            postcode = sf2.getAttribute(targetPropertyName).toString();
                            result.add(postcode);
                        }
                    }
                    fi.close();
                }
                fi2.close();
                DW_Shapefile.transact(tPostcodeShapefile, sft, tsfc, sdsf);
                writeAreaCodes(
                        postcodeAreaCodesFile,
                        result,
                        area,
                        level);
            }
        }
        return result;
    }

    public final File getPostcodeShapefile(String area, String level) {
        File r;
        r = new File(env.files.getGeneratedPostcodeDir(),
                area + level + "PolyShapefile.shp");
        r = new File(r, area + level + "PolyShapefile.shp");
        return r;
    }

    public final DW_Shapefile getLADShapefile(String name,
            TreeSet<String> tLADCensusCodes, ShapefileDataStoreFactory sdsf) {
        DW_Shapefile r;
        // Get LAD shapefiles
        String levelLAD = "LAD";
        // Read LAD Census Boundary Data
        File tLADShapefile = Maps.getAreaBoundaryShapefile(levelLAD);
        DW_Shapefile tLAD_DW_Shapefile;
        tLAD_DW_Shapefile = new DW_Shapefile(tLADShapefile);
        r = getLADShapefile(name, tLADCensusCodes, tLAD_DW_Shapefile, sdsf);
        return r;
    }

    public DW_Shapefile getLADShapefile(String name,
            TreeSet<String> tLADCensusCodes, DW_Shapefile tLAD_DW_Shapefile,
            ShapefileDataStoreFactory sdsf) {
        DW_Shapefile r;
        FeatureCollection tLAD_FC = tLAD_DW_Shapefile.getFeatureCollection();
        SimpleFeatureType tLAD_SFT = tLAD_DW_Shapefile.getSimpleFeatureType();
        // Select tLADCensusCodes from LAD Census Boundary Data
        File tLADShapefile = Maps.getCensusBoundaryShapefile(name, "LAD");
        r = new DW_Shapefile(tLADShapefile);
        if (!tLADShapefile.exists()) {
            Maps.selectAndCreateNewShapefile(sdsf, tLAD_FC, tLAD_SFT, 
                    tLADCensusCodes, "CODE", tLADShapefile);
        }
        return r;
    }

    public File getPostcodeDataAreaCodesFile(            String area,            String level) {
        File r;
        File dir = new File(env.files.getGeneratedPostcodeDir(), area);
        dir = new File(dir, level);
        dir.mkdirs();
        r = new File(dir, "AreaCodes.csv");
        return r;
    }

    public static void writeAreaCodes(File postcodeDataAreaCodesFile,
            TreeSet<String> result, String area, String level) {
        postcodeDataAreaCodesFile.getParentFile().mkdirs();
        try {
            PrintWriter pw;
            pw = new PrintWriter(postcodeDataAreaCodesFile);
            Iterator<String> ite;
            ite = result.iterator();
            while (ite.hasNext()) {
                pw.println(ite.next());
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_AreaCodesAndShapefiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param area
     * @param level "OA" or "LSOA" or "MSOA" currently...
     * @return <code>TreeSet&ltString&gt</code> of LSOA codes for the Leeds
     * Local Authority District loaded from a specific file within
     * digitalWelfareDir.
     */
    public TreeSet<String> getAreaCodes(String area, String level) {
        TreeSet<String> result = null;
        File dir = new File(env.files.getGeneratedPostcodeDir(), area);
        dir = new File(dir, level);
        File file = new File(dir, "AreaCodes.csv");
        if (file.exists()) {
            try {
                BufferedReader br;
                StreamTokenizer st;
                br = env.ge.io.getBufferedReader(file);
                result = new TreeSet<>();
                st = new StreamTokenizer(br);
                env.ge.io.setStreamTokenizerSyntax1(st);
                int token = st.nextToken();
//                    long RecordID = 0;
                String line = "";
                while (!(token == StreamTokenizer.TT_EOF)) {
                    switch (token) {
                        case StreamTokenizer.TT_EOL:
//                                if (RecordID % 100 == 0) {
//                                    System.out.println(line);
//                                }
//                                RecordID++;
                            break;
                        case StreamTokenizer.TT_WORD:
                            line = st.sval;
                            result.add(line);
                            break;
                    }
                    token = st.nextToken();
                }
                br.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * @return the tLeedsAreaCodes
     */
    public TreeSet<String> getLeedsAreaCodes() {
        return LeedsAreaCodes;
    }

    /**
     * @return the LeedsAndNeighbouringLADAreaCodes
     */
    public TreeSet<String> getLeedsAndNeighbouringLADAreaCodes() {
        return LeedsAndNeighbouringLADAreaCodes;
    }

    /**
     * @return the LeedsAndNearNeighbouringLADAreaCodes
     */
    public TreeSet<String> getLeedsAndNearNeighbouringLADAreaCodes() {
        return LeedsAndNearNeighbouringLADAreaCodes;
    }

    /**
     * @return the levelShapefile
     */
    public DW_Shapefile getLevelDW_Shapefile() {
        return LevelDW_Shapefile;
    }

    /**
     * @return getLevelDW_Shapefile().getFeatureCollection();
     */
    public FeatureCollection getLevelFC() {
        return getLevelDW_Shapefile().getFeatureCollection();
    }

    /**
     * @return getLevelDW_Shapefile().getSimpleFeatureType();
     */
    public SimpleFeatureType getLevelSFT() {
        return getLevelDW_Shapefile().getSimpleFeatureType();
    }

    /**
     * @return the LeedsLevelDW_Shapefile
     */
    public DW_Shapefile getLeedsLevelDW_Shapefile() {
        return LeedsLevelDW_Shapefile;
    }

    /**
     * @return getLeedsLADDW_Shapefile().getFeatureCollection();
     */
    public FeatureCollection getLeedsLADFC() {
        return getLeedsLADDW_Shapefile().getFeatureCollection();
    }

    /**
     * @return return getLeedsLADDW_Shapefile().getSimpleFeatureType();
     */
    public SimpleFeatureType getLeedsLADSFT() {
        return getLeedsLADDW_Shapefile().getSimpleFeatureType();
    }

    /**
     * @return the LeedsLADDW_Shapefile
     */
    public DW_Shapefile getLeedsLADDW_Shapefile() {
        return LeedsLADDW_Shapefile;
    }

    /**
     * @return the LeedsAndNeighbouringLADDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNeighbouringLADDW_Shapefile() {
        return LeedsAndNeighbouringLADDW_Shapefile;
    }

    /**
     * @return the LeedsAndNearNeighbouringLADDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNearNeighbouringLADDW_Shapefile() {
        return LeedsAndNearNeighbouringLADDW_Shapefile;
    }

    /**
     * For disposing of resources once used.
     */
    public void dispose() {
        DW_Shapefile tDW_Shapefile;
        tDW_Shapefile = getLeedsLevelDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
        tDW_Shapefile = getLeedsAndNearNeighbouringLADDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
        tDW_Shapefile = getLeedsAndNeighbouringLADDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
        tDW_Shapefile = getLeedsLADDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
        tDW_Shapefile = getLevelDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
    }
}
