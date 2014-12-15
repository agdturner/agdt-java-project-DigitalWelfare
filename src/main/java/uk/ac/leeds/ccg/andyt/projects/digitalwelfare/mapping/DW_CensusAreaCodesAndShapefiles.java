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
    private final TreeSet<String> leedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes;
    private final File levelShapefile;
    private final FeatureCollection levelFC;
    private final SimpleFeatureType levelSFT;
    private final File leedsLevelShapefile;
    private final FeatureCollection leedsLADFC;
    private final SimpleFeatureType leedsLADSFT;
    private final File leedsLADShapefile;
    private final File leedsAndNeighbouringLADsShapefile;
    private final File leedsAndNeighbouringLADsAndCravenAndYorkShapefile;

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
        String tLeedsAndNeighbouringLADsAndCravenAndYorkString;
        tLeedsAndNeighbouringLADsAndCravenAndYorkString = "LeedsAndNeighbouringLADsAndCravenAndYork";
        String tCODEString = "CODE";
                
        // Read area level Census Codes
        leedsCensusAreaCodes = DW_Census.getLADCensusCodes(tLeedsString, level);
        // Read Census Boundary Data of level
        levelShapefile = getCensusBoundaryShapefile(level);
        Object[] levelFeatureCollectionAndType;
        levelFeatureCollectionAndType = DW_GeoTools.getFeatureCollectionAndType(
                levelShapefile);
        levelFC = (FeatureCollection) levelFeatureCollectionAndType[0];
        levelSFT = (SimpleFeatureType) levelFeatureCollectionAndType[1];
        /*
         * Filter Leeds LAD LSOAs and write new Shapefile if it does not exist
         * and set this Leeds LAD LSOA Boundary Data Shapefile as the
         * backgroundShapefile
         */
        leedsLevelShapefile = getCensusBoundaryShapefile(
                tLeedsString,
                level);
        if (!leedsLevelShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(sdsf, levelFC, levelSFT, leedsCensusAreaCodes,
                    targetPropertyName, leedsLevelShapefile);
        }
        //result[4] = leedsShapefile;

        // Read Leeds and neighbouring District LADs Census Codes for level
        leedsAndNeighbouringLADsCensusAreaCodes = DW_Census.getLADCensusCodes(
                tLeedsAndNeighbouringLADsString, level);
        //result[5] = leedsAndNeighbouringLADsCensusAreaCodes;

        // Read Leeds and neighbouring District LADs and Craven And York Census Codes
        leedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes = DW_Census.getLADCensusCodes(
                tLeedsAndNeighbouringLADsAndCravenAndYorkString, level);

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
        leedsLADFC = (FeatureCollection) tLADFeatureCollectionAndType[0];
        leedsLADSFT = (SimpleFeatureType) tLADFeatureCollectionAndType[1];

        // Select Leeds from LAD Census Boundary Data
        leedsLADShapefile = getCensusBoundaryShapefile(
                tLeedsString,
                levelLAD);
        if (!leedsLADShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(sdsf, leedsLADFC, leedsLADSFT, tLeedsLADCensusAreaCodes,
                    tCODEString, leedsLADShapefile);
        }

        // Select LeedsAndNeighbouringLADs from LAD Census Boundary Data
        // Code for Selby 36UH = E07000169
        // Code for Bradford 00CX = E08000032
        // Code for Kirklees 00CZ = E08000034
        // Code for Wakefield 00DB = E08000036
        // Code for Harrogate 36UD = E07000165
        TreeSet<String> tLeedsAndNeighbouringLADsLADCodes;
        tLeedsAndNeighbouringLADsLADCodes = new TreeSet<String>();
        tLeedsAndNeighbouringLADsLADCodes.addAll(leedsCensusAreaCodes);
        tLeedsAndNeighbouringLADsLADCodes.add("E07000169");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000032");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000034");
        tLeedsAndNeighbouringLADsLADCodes.add("E08000036");
        tLeedsAndNeighbouringLADsLADCodes.add("E07000165");
        leedsAndNeighbouringLADsShapefile = getCensusBoundaryShapefile(
               tLeedsAndNeighbouringLADsString,
                levelLAD);
        if (!leedsAndNeighbouringLADsShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(sdsf, leedsLADFC, leedsLADSFT,
                    tLeedsAndNeighbouringLADsLADCodes,
                    tCODEString, leedsAndNeighbouringLADsShapefile);
        }
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
        leedsAndNeighbouringLADsAndCravenAndYorkShapefile = getCensusBoundaryShapefile(
                tLeedsAndNeighbouringLADsAndCravenAndYorkString,
                levelLAD);
        if (!leedsAndNeighbouringLADsAndCravenAndYorkShapefile.exists()) {
            DW_Maps.selectAndCreateNewShapefile(sdsf, leedsLADFC, leedsLADSFT,
                    tLeedsAndNeighbouringLADsAndCravenAndYorkLADCodes,
                    tCODEString, leedsAndNeighbouringLADsAndCravenAndYorkShapefile);
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
    public TreeSet<String> getLeedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes() {
        return leedsAndNeighbouringLADsAndCravenAndYorkCensusAreaCodes;
    }

    /**
     * @return the levelShapefile
     */
    public File getLevelShapefile() {
        return levelShapefile;
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
     * @return the leedsLevelShapefile
     */
    public File getLeedsLevelShapefile() {
        return leedsLevelShapefile;
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
     * @return the leedsLADShapefile
     */
    public File getLeedsLADShapefile() {
        return leedsLADShapefile;
    }

    /**
     * @return the leedsAndNeighbouringLADsShapefile
     */
    public File getLeedsAndNeighbouringLADsShapefile() {
        return leedsAndNeighbouringLADsShapefile;
    }

    /**
     * @return the leedsAndNeighbouringLADsAndCravenAndYorkShapefile
     */
    public File getLeedsAndNeighbouringLADsAndCravenAndYorkShapefile() {
        return leedsAndNeighbouringLADsAndCravenAndYorkShapefile;
    }
    
    
}
