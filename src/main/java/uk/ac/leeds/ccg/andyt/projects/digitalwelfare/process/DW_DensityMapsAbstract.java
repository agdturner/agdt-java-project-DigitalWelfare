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
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_AbstractGridChunkDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorGWS;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataHandler;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataRecord;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Point;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Dimensions;
import uk.ac.leeds.ccg.andyt.grids.core.grid.stats.Grids_GridDoubleStats;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;

/**
 *
 * @author geoagdt
 */
public abstract class DW_DensityMapsAbstract extends DW_Object {

    // For Convenience
    protected DW_Maps Maps;
    protected ONSPD_Handler Postcode_Handler;

    protected Grids_Environment ge;
    protected Grids_ESRIAsciiGridExporter eage;
    protected Grids_ImageExporter ie;
    protected Grids_ProcessorGWS gp;
    protected Grids_GridDoubleFactory gf;
    protected Grids_AbstractGridChunkDoubleFactory gcf;
    protected long nrows;
    protected long ncols;
    protected int chunkNRows;
    protected int chunkNCols;
    protected double cellsize;
    protected Grids_Dimensions dimensions;
    protected long xllcorner;
    protected long yllcorner;
    protected TreeMap<String, String> tLookupFromPostcodeToCensusCodes;

    protected int maxCellDistanceForGeneralisation;

    //protected boolean outputESRIAsciigrids;
    protected boolean handleOutOfMemoryErrors;

    public DW_DensityMapsAbstract(DW_Environment de) {
        super(de);
        Maps = de.getMaps();
        Postcode_Handler = de.getPostcode_Handler();
    }

    // Add from postcodes
    protected int addFromPostcodes(
            Grids_GridDouble g,
            ArrayList<String> postcodes,
            TreeMap<String, Census_DeprivationDataRecord> deprivationRecords,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            ONSPD_YM3 yM3) {
        ONSPD_YM3 nearestYM3ForONSPDLookup;
        nearestYM3ForONSPDLookup = Postcode_Handler.getNearestYM3ForONSPDLookup(yM3);
        int countNonMatchingPostcodes = 0;
        Iterator<String> itep = postcodes.iterator();
        while (itep.hasNext()) {
            String postcode = itep.next();
            String aLSOACode;
            aLSOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
            if (aLSOACode != null) {
                if (deprivationRecords == null) {
                    String postcodeLevel;
                    postcodeLevel = Postcode_Handler.getPostcodeLevel(postcode);
                    ONSPD_Point aPoint;
                    aPoint = Postcode_Handler.getPointFromPostcode(nearestYM3ForONSPDLookup, postcodeLevel, postcode);
                    if (aPoint != null) {
                        int x = aPoint.getX();
                        int y = aPoint.getY();
                        g.addToCell((double) x, (double) y, 1.0);
                    } else {
                        countNonMatchingPostcodes++;
                    }
                } else {
                    //Convert postcode to LSOA code
                    Census_DeprivationDataRecord aDeprivation_DataRecord;
                    aDeprivation_DataRecord = deprivationRecords.get(aLSOACode);
                    // aDeprivation_DataRecord might be null as deprivation data comes from 2001 census...
                    if (aDeprivation_DataRecord != null) {
                        Integer thisDeprivationClass;
                        thisDeprivationClass = Census_DeprivationDataHandler.getDeprivationClass(
                                deprivationClasses,
                                aDeprivation_DataRecord);
                        if (thisDeprivationClass == deprivationClass.intValue()) {
                            String postcodeLevel;
                            postcodeLevel = Postcode_Handler.getPostcodeLevel(postcode);
                            ONSPD_Point aPoint;
                            aPoint = Postcode_Handler.getPointFromPostcode(nearestYM3ForONSPDLookup, postcodeLevel, postcode);
                            if (aPoint != null) {
                                int x = aPoint.getX();
                                int y = aPoint.getY();
                                g.addToCell((double) x, (double) y, 1.0);
                            } else {
                                countNonMatchingPostcodes++;
                            }
                        }
                    }
                }
            }
        }
        return countNonMatchingPostcodes;
    }

    /**
     * Initialise and return a grid
     *
     * @param dir
     * @return
     */
    public Grids_GridDouble initiliseGrid(File dir) {
        Grids_GridDouble result = (Grids_GridDouble) gf.create(
                new Grids_GridDoubleStats(ge), dir, gcf, nrows, ncols,
                dimensions);
        //System.out.println("" + result.toString(handleOutOfMemoryErrors));
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                result.setCell(row, col, 0); // set all values to 0;
            }
        }
        return result;
    }

    public void outputGridToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation,
            Grids_AbstractGridNumber g,
            File asciigridFile,
            File dir,
            String nameOfGrid,
            int styleIndex,
            boolean scaleToFirst) {
        //----------------------------------------------------------
        // Create GeoTools GridCoverage
        // Two ways to read the asciigridFile into a GridCoverage
        // 1.
        // Reading the coverage through a file
        ArcGridReader agr;
        agr = Maps.getArcGridReader(asciigridFile);
//        // 2.
//        AbstractGridFormat format = GridFormatFinder.findFormat(asciigridFile);
//        GridCoverage2DReader reader = format.getReader(asciigridFile);
        GridCoverage2D gc;
        gc = Maps.getGridCoverage2D(agr);

        String outname = nameOfGrid + "GeoToolsOutput";
        //showMapsInJMapPane = true;

//        Object[] styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
//                normalisation, g, gc, "Quantile", ff, 9, "Reds", true);
        //Style style = createGreyscaleStyle(gc, null, sf, ff);
        ReferencedEnvelope re;
        re = Maps.getCommunityAreasDW_Shapefile().getFeatureLayer().getBounds();
        double minX = re.getMinX();
        double maxX = re.getMaxX();
        double minY = re.getMinY();
        double maxY = re.getMaxY();
        System.out.println("minX " + minX);
        System.out.println("maxX " + maxX);
        System.out.println("minY " + minY);
        System.out.println("maxY " + maxY);

//        DW_Geotools.outputToImageUsingGeoToolsAndSetCommonStyle(
//                normalisation,
//                Maps.getStyleParameters(),
//                styleIndex,
//                outname,
//                g,
//                gc,
//                
//                getForegroundDW_Shapefile0(),
//                Maps.getForegroundDW_Shapefile1(),
//                Maps.getBackgroundDW_Shapefile(),
//                dir,
//                Maps.getImageWidth(),
//                Maps.isShowMapsInJMapPane(),
//                scaleToFirst);
//        try {
//            reader.dispose();
//        } catch (IOException ex) {
//            Logger.getLogger(DW_DensityMaps.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (agr != null) {
            agr.dispose();
        }
    }

}
