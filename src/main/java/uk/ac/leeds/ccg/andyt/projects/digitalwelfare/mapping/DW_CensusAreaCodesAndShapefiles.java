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
public class DW_CensusAreaCodesAndShapefiles {

    private final TreeSet<String> leedsCensusAreaCodes;
    private final TreeSet<String> leedsAndNeighbouringLADsCensusAreaCodes;
    private final TreeSet<String> leedsAndNearNeighbouringLADsCensusAreaCodes;
    private final DW_Shapefile levelDW_Shapefile;
    private final FeatureCollection levelFC;
    private final SimpleFeatureType levelSFT;
    private final DW_Shapefile leedsLevelDW_Shapefile;
    private final FeatureCollection leedsLADFC;
    private final SimpleFeatureType leedsLADSFT;
    private final DW_Shapefile leedsLADDW_Shapefile;
    private final DW_Shapefile leedsAndNeighbouringLADsDW_Shapefile;
    private final DW_Shapefile leedsAndNearNeighbouringLADsDW_Shapefile;

    /**
     *
     * @param level
     * @param targetPropertyName
     * @param sdsf
     */
    public DW_CensusAreaCodesAndShapefiles(
            String level,
            String targetPropertyName,
            ShapefileDataStoreFactory sdsf) {
        String tLeedsString = "Leeds";
        String tLeedsAndNeighbouringLADsString;
        tLeedsAndNeighbouringLADsString = "LeedsAndNeighbouringLADs";
        String tLeedsAndNearNeighbouringLADsString;
        tLeedsAndNearNeighbouringLADsString = "LeedsAndNearNeighbouringLADs";
        String tCODEString = "CODE";

        // Read area level Census Codes
        leedsCensusAreaCodes = DW_Census.getCensusCodes(
                tLeedsString, 
                level);
        // Read Census Boundary Data of level
        File levelShapefile = getCensusBoundaryShapefile(level);
        levelDW_Shapefile = new DW_Shapefile(levelShapefile);
        levelFC = levelDW_Shapefile.getFeatureCollection();
        levelSFT = levelDW_Shapefile.getSimpleFeatureType();

        /*
         * Filter Leeds LAD LSOAs and write new Shapefile if it does not exist
         * and set this Leeds LAD LSOA Boundary Data Shapefile as the
         * backgroundShapefile
         */
        File leedsLevelShapefile = getCensusBoundaryShapefile(
                tLeedsString,
                level);
        leedsLevelDW_Shapefile = new DW_Shapefile(leedsLevelShapefile);
        if (!leedsLevelShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(
                    sdsf,
                    levelFC,
                    levelSFT,
                    leedsCensusAreaCodes,
                    targetPropertyName,
                    leedsLevelShapefile);
        }
        //result[4] = leedsShapefile;

        // Read Leeds and neighbouring District LADs Census Codes for level
        leedsAndNeighbouringLADsCensusAreaCodes = DW_Census.getCensusCodes(
                tLeedsAndNeighbouringLADsString, 
                level);
        // Read Leeds and neighbouring District LADs and Craven And York Census Codes
        leedsAndNearNeighbouringLADsCensusAreaCodes = DW_Census.getCensusCodes(
                tLeedsAndNearNeighbouringLADsString, 
                level);

        // Get LAD shapefiles
        String levelLAD = "LAD";
        // Set Leeds Census Codes for LAD
        TreeSet<String> tLeedsLADCensusAreaCodes;
        tLeedsLADCensusAreaCodes = new TreeSet<String>();
        // Code for Leeds 00DA = E08000035
        tLeedsLADCensusAreaCodes.add("E08000035");

        // Read LAD Census Boundary Data
        File tLADShapefile = getCensusBoundaryShapefile(levelLAD);
        DW_Shapefile tLADSDS;
        tLADSDS = new DW_Shapefile(tLADShapefile);
        leedsLADFC = tLADSDS.getFeatureCollection();
        leedsLADSFT = tLADSDS.getSimpleFeatureType();

        // Select Leeds from LAD Census Boundary Data
        File leedsLADShapefile = getCensusBoundaryShapefile(
                tLeedsString,
                levelLAD);
        leedsLADDW_Shapefile = new DW_Shapefile(leedsLADShapefile);
        if (!leedsLADShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(
                    sdsf, 
                    leedsLADFC,
                    leedsLADSFT,
                    tLeedsLADCensusAreaCodes,
                    tCODEString,
                    leedsLADShapefile);
        }

        TreeSet<String> tLeedsAndNeighbouringLADsLADCodes;
        tLeedsAndNeighbouringLADsLADCodes = new TreeSet<String>();
        tLeedsAndNeighbouringLADsLADCodes.addAll(leedsCensusAreaCodes);
        tLeedsAndNeighbouringLADsLADCodes.add("E07000169");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000032");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000034");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000036");
        tLeedsAndNeighbouringLADsLADCodes.add("E07000165");
        if (leedsAndNeighbouringLADsCensusAreaCodes != null) {
            // Select LeedsAndNeighbouringLADs from LAD Census Boundary Data
            // Code for Selby 36UH = E07000169
            // Code for Bradford 00CX = E08000032
            // Code for Kirklees 00CZ = E08000034
            // Code for Wakefield 00DB = E08000036
            // Code for Harrogate 36UD = E07000165
            File leedsAndNeighbouringLADsShapefile = getCensusBoundaryShapefile(
                    tLeedsAndNeighbouringLADsString,
                    levelLAD);
            leedsAndNeighbouringLADsDW_Shapefile = new DW_Shapefile(
                    leedsAndNeighbouringLADsShapefile);
            if (!leedsAndNeighbouringLADsShapefile.exists()) {
                DW_Maps.selectAndCreateNewShapefile(
                        sdsf, 
                        leedsLADFC,
                        leedsLADSFT,
                        tLeedsAndNeighbouringLADsLADCodes,
                        tCODEString,
                        leedsAndNeighbouringLADsShapefile);
            }
        } else {
            leedsAndNeighbouringLADsDW_Shapefile = null;
        }
        if (leedsAndNearNeighbouringLADsCensusAreaCodes != null) {
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
            File leedsAndNeighbouringLADsAndCravenAndYorkShapefile = getCensusBoundaryShapefile(
                    tLeedsAndNearNeighbouringLADsString,
                    levelLAD);
            leedsAndNearNeighbouringLADsDW_Shapefile = new DW_Shapefile(
                    leedsAndNeighbouringLADsAndCravenAndYorkShapefile);
            if (!leedsAndNeighbouringLADsAndCravenAndYorkShapefile.exists()) {
                DW_Maps.selectAndCreateNewShapefile(sdsf, leedsLADFC, leedsLADSFT,
                        tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes,
                        tCODEString,
                        leedsAndNeighbouringLADsAndCravenAndYorkShapefile);
            }
        } else {
            leedsAndNearNeighbouringLADsDW_Shapefile = null;
        }
    }

    /**
     * @return the leedsCensusAreaCodes
     */
    public TreeSet<String> getLeedsCensusAreaCodes() {
        return leedsCensusAreaCodes;
    }

    /**
     * @return the leedsAndNeighbouringLADsCensusAreaCodes
     */
    public TreeSet<String> getLeedsAndNeighbouringLADsCensusAreaCodes() {
        return leedsAndNeighbouringLADsCensusAreaCodes;
    }

    /**
     * @return the leedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes
     */
    public TreeSet<String> getLeedsAndNearNeighbouringLADsCensusAreaCodes() {
        return leedsAndNearNeighbouringLADsCensusAreaCodes;
    }

    /**
     * @return the levelShapefile
     */
    public DW_Shapefile getLevelDW_Shapefile() {
        return levelDW_Shapefile;
    }

    /**
     * @return the levelFC
     */
    public FeatureCollection getLevelFC() {
        return levelFC;
    }

    /**
     * @return the levelSFT
     */
    public SimpleFeatureType getLevelSFT() {
        return levelSFT;
    }

    /**
     * @return the leedsLevelDW_Shapefile
     */
    public DW_Shapefile getLeedsLevelDW_Shapefile() {
        return leedsLevelDW_Shapefile;
    }

    /**
     * @return the leedsLADFC
     */
    public FeatureCollection getLeedsLADFC() {
        return leedsLADFC;
    }

    /**
     * @return the leedsLADSFT
     */
    public SimpleFeatureType getLeedsLADSFT() {
        return leedsLADSFT;
    }

    /**
     * @return the leedsLADDW_Shapefile
     */
    public DW_Shapefile getLeedsLADDW_Shapefile() {
        return leedsLADDW_Shapefile;
    }

    /**
     * @return the leedsAndNeighbouringLADsDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNeighbouringLADsDW_Shapefile() {
        return leedsAndNeighbouringLADsDW_Shapefile;
    }

    /**
     * @return the leedsAndNearNeighbouringLADsDW_Shapefile
     */
    public DW_Shapefile getLeedsAndNearNeighbouringLADsDW_Shapefile() {
        return leedsAndNearNeighbouringLADsDW_Shapefile;
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
        tDW_Shapefile = getLeedsAndNearNeighbouringLADsDW_Shapefile();
        if (tDW_Shapefile != null) {
            tDW_Shapefile.dispose();
        }
        tDW_Shapefile = getLeedsAndNeighbouringLADsDW_Shapefile();
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
