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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCellDoubleChunkFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.GridStatistics0;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorGWS;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler.getNearestYM3ForONSPDLookup;

/**
 *
 * @author geoagdt
 */
public abstract class DW_DensityMapsAbstract extends DW_Maps {

    protected Grids_Environment ge;
    protected ESRIAsciiGridExporter eage;
    protected ImageExporter ie;
    protected Grid2DSquareCellProcessorGWS gp;
    protected Grid2DSquareCellDoubleFactory gf;
    protected AbstractGrid2DSquareCellDoubleChunkFactory gcf;
    protected long nrows;
    protected long ncols;
    protected int chunkNRows;
    protected int chunkNCols;
    protected double cellsize;
    protected BigDecimal[] dimensions;
    protected long xllcorner;
    protected long yllcorner;
    protected TreeMap<String, String> tLookupFromPostcodeToCensusCodes;

    protected int maxCellDistanceForGeneralisation;

    //protected boolean outputESRIAsciigrids;
    protected boolean handleOutOfMemoryErrors;

    public DW_DensityMapsAbstract(DW_Environment env) {
        super(env);
    }
    
    // Add from postcodes
    protected int addFromPostcodes(
            Grid2DSquareCellDouble g,
            ArrayList<String> postcodes,
            TreeMap<String, Deprivation_DataRecord> deprivationRecords,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            String yM3) {
        String nearestYM3ForONSPDLookup;
        nearestYM3ForONSPDLookup = getNearestYM3ForONSPDLookup(yM3);
        int countNonMatchingPostcodes = 0;
        Iterator<String> itep = postcodes.iterator();
        while (itep.hasNext()) {
            String postcode = itep.next();
            String aLSOACode;
            aLSOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
            if (aLSOACode != null) {
                if (deprivationRecords == null) {
                    String postcodeLevel;
                    postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(postcode);
                    AGDT_Point aPoint;
                    aPoint = getONSPDlookups(env).get(postcodeLevel).get(nearestYM3ForONSPDLookup).get(postcode);
                    if (aPoint != null) {
                        int x = aPoint.getX();
                        int y = aPoint.getY();
                        g.addToCell((double) x, (double) y, 1.0, handleOutOfMemoryErrors);
                    } else {
                        countNonMatchingPostcodes++;
                    }
                } else {
                    //Convert postcode to LSOA code
                    Deprivation_DataRecord aDeprivation_DataRecord;
                    aDeprivation_DataRecord = deprivationRecords.get(aLSOACode);
                    // aDeprivation_DataRecord might be null as deprivation data comes from 2001 census...
                    if (aDeprivation_DataRecord != null) {
                        Integer thisDeprivationClass;
                        thisDeprivationClass = Deprivation_DataHandler.getDeprivationClass(
                                deprivationClasses,
                                aDeprivation_DataRecord);
                        if (thisDeprivationClass == deprivationClass.intValue()) {
                            String postcodeLevel;
                            postcodeLevel = DW_Postcode_Handler.getPostcodeLevel(postcode);
                            AGDT_Point aPoint;
                            aPoint = getONSPDlookups(env).get(postcodeLevel).get(nearestYM3ForONSPDLookup).get(postcode);
                            if (aPoint != null) {
                                int x = aPoint.getX();
                                int y = aPoint.getY();
                                g.addToCell((double) x, (double) y, 1.0, handleOutOfMemoryErrors);
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
    public Grid2DSquareCellDouble initiliseGrid(File dir) {
        Grid2DSquareCellDouble result = (Grid2DSquareCellDouble) gf.create(
                new GridStatistics0(),
                dir,
                gcf,
                nrows,
                ncols,
                dimensions,
                ge,
                handleOutOfMemoryErrors);
        //System.out.println("" + result.toString(handleOutOfMemoryErrors));
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                result.setCell(row, col, 0, handleOutOfMemoryErrors); // set all values to 0;
            }
        }
        return result;
    }

    public void outputGridToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation,
            AbstractGrid2DSquareCell g,
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
        agr = getArcGridReader(asciigridFile);
//        // 2.
//        AbstractGridFormat format = GridFormatFinder.findFormat(asciigridFile);
//        GridCoverage2DReader reader = format.getReader(asciigridFile);
        GridCoverage2D gc;
        gc = getGridCoverage2D(agr);

        String outname = nameOfGrid + "GeoToolsOutput";
        //showMapsInJMapPane = true;

//        Object[] styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
//                normalisation, g, gc, "Quantile", ff, 9, "Reds", true);
        //Style style = createGreyscaleStyle(gc, null, sf, ff);
        ReferencedEnvelope re;
        re = backgroundDW_Shapefile.getFeatureLayer().getBounds();
        double minX = re.getMinX();
        double maxX = re.getMaxX();
        double minY = re.getMinY();
        double maxY = re.getMaxY();
        System.out.println("minX " + minX);
        System.out.println("maxX " + maxX);
        System.out.println("minY " + minY);
        System.out.println("maxY " + maxY);

        DW_Geotools.outputToImageUsingGeoToolsAndSetCommonStyle(
                normalisation,
                styleParameters,
                styleIndex,
                outname,
                g,
                gc,
                foregroundDW_Shapefile0,
                foregroundDW_Shapefile1,
                backgroundDW_Shapefile,
                dir,
                imageWidth,
                showMapsInJMapPane,
                scaleToFirst);
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
