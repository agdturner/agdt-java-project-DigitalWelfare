package uk.ac.leeds.ccg.projects.dw.process;

import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Maps;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import uk.ac.leeds.ccg.grids.d2.grid.Grids_GridNumber;
import uk.ac.leeds.ccg.grids.d2.chunk.d.Grids_ChunkFactoryDouble;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridFactoryDouble;
import uk.ac.leeds.ccg.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.grids.process.Grids_ProcessorGWS;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.grids.d2.Grids_Dimensions;
import uk.ac.leeds.ccg.grids.d2.stats.Grids_StatsDouble;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.generic.io.Generic_Path;

/**
 *
 * @author geoagdt
 */
public abstract class DW_DensityMapsAbstract extends DW_Object {

    // For Convenience
    protected DW_Maps maps;
    protected UKP_Data ukpHandler;

    protected Grids_ESRIAsciiGridExporter eage;
    protected Grids_ImageExporter ie;
    protected Grids_ProcessorGWS gp;
    protected Grids_GridFactoryDouble gf;
    //protected Grids_ChunkFactoryDouble gcf;
    protected long nrows;
    protected long ncols;
    protected int chunkNRows;
    protected int chunkNCols;
    protected double cellsize;
    protected Grids_Dimensions dimensions;
    protected long xllcorner;
    protected long yllcorner;
    protected TreeMap<String, String> tLookupFromPostcodeToCensusCodes;

    /**
     * maxCellDistanceForGeneralisation
     */
    protected int mcdg;

    //protected boolean outputESRIAsciigrids;
    protected boolean handleOutOfMemoryErrors;

    public DW_DensityMapsAbstract(DW_Environment de) throws IOException {
        super(de);
        maps = de.getMaps();
        ukpHandler = de.getUkpData();
    }

    // Add from postcodes
    protected int addFromPostcodes(Grids_GridDouble g,
            ArrayList<String> postcodes,
            TreeMap<Integer, Integer> deprivationClasses,
            Integer deprivationClass,
            UKP_YM3 yM3) throws IOException, ClassNotFoundException, Exception {
        UKP_YM3 nYM3 = ukpHandler.getNearestYM3ForONSPDLookup(yM3);
        int countNonMatchingPostcodes = 0;
        Iterator<String> itep = postcodes.iterator();
        while (itep.hasNext()) {
            String postcode = itep.next();
            String lsoa = tLookupFromPostcodeToCensusCodes.get(postcode);
            if (lsoa != null) {
                int pl = ukpHandler.getPostcodeLevel(postcode);
                ONSPD_Point p = ukpHandler.getPointFromPostcode(
                        nYM3, pl, postcode);
                if (p != null) {
                    g.addToCell(BigDecimal.valueOf(p.getX()),
                            BigDecimal.valueOf(p.getY()), 1.0);
                } else {
                    countNonMatchingPostcodes++;
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
    public Grids_GridDouble initiliseGrid(Path dir)
            throws ClassNotFoundException, Exception {
        Grids_GridDouble r = (Grids_GridDouble) gf.create(nrows, ncols,
                dimensions);
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                r.setCell(row, col, 0); // set all values to 0;
            }
        }
        return r;
    }

    public void outputGridToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation, Grids_GridNumber g, Path asciigridFile,
            Path dir, String nameOfGrid, int styleIndex, boolean scaleToFirst) throws IOException {
        //----------------------------------------------------------
        // Create GeoTools GridCoverage
        // Two ways to read the asciigridFile into a GridCoverage
        // 1.
        // Reading the coverage through a file
        ArcGridReader agr = maps.getArcGridReader(asciigridFile);
//        // 2.
//        AbstractGridFormat format = GridFormatFinder.findFormat(asciigridFile);
//        GridCoverage2DReader reader = format.getReader(asciigridFile);
        GridCoverage2D gc = maps.getGridCoverage2D(agr);

        String outname = nameOfGrid + "GeoToolsOutput";
        //showMapsInJMapPane = true;

//        Object[] styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
//                normalisation, g, gc, "Quantile", ff, 9, "Reds", true);
        //Style style = createGreyscaleStyle(gc, null, sf, ff);
        ReferencedEnvelope re = maps.getCommunityAreasDW_Shapefile()
                .getFeatureLayer().getBounds();
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
//                maps.getStyleParameters(),
//                styleIndex,
//                outname,
//                g,
//                gc,
//                
//                getForegroundDW_Shapefile0(),
//                maps.getForegroundDW_Shapefile1(),
//                maps.getBackgroundDW_Shapefile(),
//                dir,
//                maps.getImageWidth(),
//                maps.isShowMapsInJMapPane(),
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
