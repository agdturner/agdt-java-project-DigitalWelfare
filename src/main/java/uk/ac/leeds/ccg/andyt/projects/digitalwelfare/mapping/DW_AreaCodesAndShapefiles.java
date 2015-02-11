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

import java.io.File;
import java.util.TreeSet;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.DW_Census;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps.getCensusBoundaryShapefile;

/**
 *
 * @author geoagdt
 */
public class DW_AreaCodesAndShapefiles {

    private final TreeSet<String> _LeedsAreaCodes;
    private final TreeSet<String> _LeedsAndNeighbouringLADAreaCodes;
    private final TreeSet<String> _LeedsAndNearNeighbouringLADAreaCodes;
    private final DW_Shapefile _LevelDW_Shapefile;
//    private final FeatureCollection levelFC;
//    private final SimpleFeatureType levelSFT;
    private final DW_Shapefile _LeedsLevelDW_Shapefile;
//    private final FeatureCollection leedsLADFC;
//    private final SimpleFeatureType leedsLADSFT;
    private final DW_Shapefile _LeedsLADDW_Shapefile;
    private final DW_Shapefile _LeedsAndNeighbouringLADDW_Shapefile;
    private final DW_Shapefile _LeedsAndNearNeighbouringLADDW_Shapefile;

    /**
     *
     * @param level
     * @param targetPropertyName
     * @param sdsf
     */
    public DW_AreaCodesAndShapefiles(
            String level,
            String targetPropertyName,
            ShapefileDataStoreFactory sdsf) {
        if (level.equalsIgnoreCase("OA")
                || level.equalsIgnoreCase("LSOA")
                || level.equalsIgnoreCase("MSOA")) {
            String tLeedsString = "Leeds";
            String tLeedsAndNeighbouringLADsString;
            tLeedsAndNeighbouringLADsString = "LeedsAndNeighbouringLADs";
            String tLeedsAndNearNeighbouringLADsString;
            tLeedsAndNearNeighbouringLADsString = "LeedsAndNearNeighbouringLADs";
            String tCODEString = "CODE";

            // Read Census Boundary Data of level
            File levelShapefile = DW_Maps.getAreaBoundaryShapefile(level);
            _LevelDW_Shapefile = new DW_Shapefile(levelShapefile);
            
            // Read area level Census Codes
            _LeedsAreaCodes = DW_Census.getCensusCodes(
                    tLeedsString,
                    level);
            // Read Leeds and neighbouring District LADs Census Codes for level
            _LeedsAndNeighbouringLADAreaCodes = DW_Census.getCensusCodes(
                    tLeedsAndNeighbouringLADsString,
                    level);
            // Read Leeds and neighbouring District LADs and Craven And York Census Codes
            _LeedsAndNearNeighbouringLADAreaCodes = DW_Census.getCensusCodes(
                    tLeedsAndNearNeighbouringLADsString,
                    level);
            
            FeatureCollection levelFC;
            SimpleFeatureType levelSFT;
            levelFC = _LevelDW_Shapefile.getFeatureCollection();
            levelSFT = _LevelDW_Shapefile.getSimpleFeatureType();

            /*
             * Filter Leeds LAD LSOAs and write new Shapefile if it does not exist
             * and set this Leeds LAD LSOA Boundary Data Shapefile as the
             * backgroundShapefile
             */
            File leedsLevelShapefile = getCensusBoundaryShapefile(
                    tLeedsString,
                    level);
            _LeedsLevelDW_Shapefile = new DW_Shapefile(leedsLevelShapefile);
            if (!leedsLevelShapefile.exists()) {
                DW_Maps.selectAndCreateNewShapefile(sdsf,
                        levelFC,
                        levelSFT,
                        _LeedsAreaCodes,
                        targetPropertyName,
                        leedsLevelShapefile);
            }
            //result[4] = leedsShapefile;

            

            // Set LAD DW_Shapefiles
            // ------------------
            // Get Leeds LAD Shapefile
            String levelLAD = "LAD";
            // Set Leeds Census Codes for LAD
            TreeSet<String> tLeedsLADCensusAreaCode;
            tLeedsLADCensusAreaCode = new TreeSet<String>();
            // Code for Leeds 00DA = E08000035
            tLeedsLADCensusAreaCode.add("E08000035");
            _LeedsLADDW_Shapefile = getLADShapefile(
                    tLeedsString,
                    tLeedsLADCensusAreaCode,
                    sdsf);
            // Get Leeds and Neighbouring LAD Shapefile
            TreeSet<String> tLeedsAndNeighbouringLADCodes;
            tLeedsAndNeighbouringLADCodes = new TreeSet<String>();
            tLeedsAndNeighbouringLADCodes.addAll(_LeedsAreaCodes);
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
            _LeedsAndNeighbouringLADDW_Shapefile = getLADShapefile(
                    tLeedsAndNeighbouringLADsString,
                    tLeedsLADCensusAreaCode,
                    sdsf);
            // Get Leeds and Near Neighbouring LAD Shapefile
            TreeSet<String> tLeedsAndNearNeighbouringLADCodes;
            tLeedsAndNearNeighbouringLADCodes = new TreeSet<String>();
            tLeedsAndNearNeighbouringLADCodes.addAll(
                    tLeedsAndNeighbouringLADCodes);
            // Code for Calderdale 00CY = E08000033
            // Code for Craven 36UB = E07000163
            // Code for York 00FF = E06000014
            tLeedsAndNearNeighbouringLADCodes.add("E08000033");
            tLeedsAndNearNeighbouringLADCodes.add("E07000163");
            tLeedsAndNearNeighbouringLADCodes.add("E06000014");
            _LeedsAndNearNeighbouringLADDW_Shapefile = getLADShapefile(
                    tLeedsAndNearNeighbouringLADsString,
                    tLeedsLADCensusAreaCode,
                    sdsf);
        } else {
            // Read Census Boundary Data of level
            File levelShapefile = DW_Maps.getAreaBoundaryShapefile(level);
            _LevelDW_Shapefile = new DW_Shapefile(levelShapefile);
//            levelFC = _LevelDW_Shapefile.getFeatureCollection();
//            levelSFT = _LevelDW_Shapefile.getSimpleFeatureType();
//            // Read area level Codes
//            tLeedsAreaCodes = DW_Census.getCensusCodes(
//                    tLeedsString,
//                    level);
//            // Want a list of all unit postcodes or postcode sectors in Leeds...
//            getLeeds LAD Polygon and check all unit postcodes or postcode sectors 
//            tLeedsAreaCodes = getAreaCodes(
//                    tLeedsString,
//                    level);
            _LeedsAreaCodes = null;
            _LeedsAndNeighbouringLADAreaCodes = null;
            _LeedsAndNearNeighbouringLADAreaCodes = null;
            _LeedsLADDW_Shapefile = null;
            _LeedsAndNeighbouringLADDW_Shapefile = null;
            _LeedsAndNearNeighbouringLADDW_Shapefile = null;
            _LeedsLevelDW_Shapefile = null;
        }
    }

    public static DW_Shapefile getLADShapefile(
            String name,
            TreeSet<String> tLADCensusCodes,
            ShapefileDataStoreFactory sdsf) {
        DW_Shapefile result;
        // Get LAD shapefiles
        String levelLAD = "LAD";
        // Read LAD Census Boundary Data
        File tLADShapefile = DW_Maps.getAreaBoundaryShapefile(levelLAD);
        DW_Shapefile tLAD_DW_Shapefile;
        tLAD_DW_Shapefile = new DW_Shapefile(tLADShapefile);
        result = getLADShapefile(
                name,
                tLADCensusCodes,
                tLAD_DW_Shapefile,
                sdsf);
        return result;
    }

    public static DW_Shapefile getLADShapefile(
            String name,
            TreeSet<String> tLADCensusCodes,
            DW_Shapefile tLAD_DW_Shapefile,
            ShapefileDataStoreFactory sdsf) {
        DW_Shapefile result;
        FeatureCollection tLAD_FC = tLAD_DW_Shapefile.getFeatureCollection();
        SimpleFeatureType tLAD_SFT = tLAD_DW_Shapefile.getSimpleFeatureType();
        // Select tLADCensusCodes from LAD Census Boundary Data
        File tLADShapefile = getCensusBoundaryShapefile(
                name,
                "LAD");
        result = new DW_Shapefile(tLADShapefile);
        if (!tLADShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(
                    sdsf,
                    tLAD_FC,
                    tLAD_SFT,
                    tLADCensusCodes,
                    "CODE",
                    tLADShapefile);
        }
        return result;
    }

    /**
     * @return the tLeedsAreaCodes
     */
    public TreeSet<String> getLeedsAreaCodes() {
        return _LeedsAreaCodes;
    }

    /**
     * @return the _LeedsAndNeighbouringLADAreaCodes
     */
    public TreeSet<String> getLeedsAndNeighbouringLADAreaCodes() {
        return _LeedsAndNeighbouringLADAreaCodes;
    }

    /**
     * @return the _LeedsAndNearNeighbouringLADAreaCodes
     */
    public TreeSet<String> getLeedsAndNearNeighbouringLADAreaCodes() {
        return _LeedsAndNearNeighbouringLADAreaCodes;
    }

    /**
     * @return the levelShapefile
     */
    public DW_Shapefile getLevelDW_Shapefile() {
        return _LevelDW_Shapefile;
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
     * @return the _LeedsLevelDW_Shapefile
     */
    public DW_Shapefile getLeedsLevelDW_Shapefile() {
        return _LeedsLevelDW_Shapefile;
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
     * @return the _LeedsLADDW_Shapefile
     */
    public DW_Shapefile getLeedsLADDW_Shapefile() {
        return _LeedsLADDW_Shapefile;
    }

    /**
     * @return the _LeedsAndNeighbouringLADDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNeighbouringLADDW_Shapefile() {
        return _LeedsAndNeighbouringLADDW_Shapefile;
    }

    /**
     * @return the _LeedsAndNearNeighbouringLADDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNearNeighbouringLADDW_Shapefile() {
        return _LeedsAndNearNeighbouringLADDW_Shapefile;
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
