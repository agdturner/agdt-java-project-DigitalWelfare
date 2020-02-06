
package uk.ac.leeds.ccg.projects.dw.process;

import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Maps;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.MapContent;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridFactoryDouble;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.grids.d2.Grids_Dimensions;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.grids.process.Grids_Processor;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.geotools.Geotools_Point;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Shapefile;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_PostcodeMaps extends DW_Maps {

    /**
     * For storing property for selecting
     */
    private String targetPropertyName;

    public DW_PostcodeMaps(DW_Environment de) throws IOException {
        super(de);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_PostcodeMaps(null).run();
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void run() throws IOException, Exception {
        showMapsInJMapPane = true;//false;//true;//false;
        imageWidth = 1000;
        mapDirectory = files.getGeneratedPostcodeDir();

        sdsf = getShapefileDataStoreFactory();

        // Boundaries
        // Postcode Sectors
        Path lpsf = getLeedsPostcodeSectorShapefile();
        // Postcode Units
        DW_Shapefile pup = getPostcodeUnitPoly_DW_Shapefile(dwEnv,
                new ShapefileDataStoreFactory());

        UKP_YM3 yM3 = ukpData.getDefaultYM3();

        int pl;
        // Postcode Unit Centroids
        pl = UKP_Data.TYPE_UNIT;
        String fn;
        fn = "LeedsPostcode" + pl + "PointShapefile.shp";
        Path pups = createPostcodePointShapefileIfItDoesNotExist(
                yM3, pl, mapDirectory, fn, "LS");
        // Postcode Sector Centroids
        pl = UKP_Data.TYPE_SECTOR;
        fn = "LeedsPostcode" + pl + "PointShapefile.shp";
        Path psps = createPostcodePointShapefileIfItDoesNotExist(
                yM3, pl, mapDirectory, fn, "LS");
        // Postcode District Centroids
        pl = UKP_Data.TYPE_DISTRICT;
        fn = "LeedsPostcode" + pl + "PointShapefile.shp";
        Path pdps = createPostcodePointShapefileIfItDoesNotExist(
                yM3, pl, mapDirectory, fn, "LS");

        String level;
        DW_AreaCodesAndShapefiles acas;
        // OA
        level = "OA";
        targetPropertyName = "CODE";
        acas = new DW_AreaCodesAndShapefiles(dwEnv, level, targetPropertyName, sdsf);
        DW_Shapefile oas = acas.getLeedsLevelDW_Shapefile();
        DW_Shapefile lads = acas.getLeedsLADDW_Shapefile();

        // LSOA
        level = "LSOA";
        targetPropertyName = "LSOA11CD";
        acas = new DW_AreaCodesAndShapefiles(dwEnv, level, targetPropertyName, sdsf);
        DW_Shapefile lsoas = acas.getLeedsLevelDW_Shapefile();

        // MSOA
        level = "MSOA";
        targetPropertyName = "MSOA11CD";
        acas = new DW_AreaCodesAndShapefiles(dwEnv, level, targetPropertyName, sdsf);
        DW_Shapefile msoas = acas.getLeedsLevelDW_Shapefile();

       long nrows = 139;//277;//554;
       long ncols = 170;//340;//680;
       double xllcorner = 413000;
       double yllcorner = 422500;
       double cellsize = 200;//100;//50;
        // LineGrid
        DW_Shapefile lineGrid = getLineGrid(nrows, ncols, xllcorner, yllcorner, cellsize);
        // PolyGrid
        DW_Shapefile polyGrid = getPolyGrid(nrows, ncols, xllcorner, yllcorner, cellsize);
        Grids_Dimensions dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));

        String outname = "outname";
        Grids_Environment ge = dwEnv.gridsEnv;
        Grids_Processor gp = ge.getProcessor();
//        Grids_GridFactoryDouble gf = new Grids_GridFactoryDouble(ge,
//                gp.GridChunkDoubleFactory,
//                gp.DefaultGridChunkDoubleFactory, -Double.MAX_VALUE,
//                (int) nrows, (int) ncols, dimensions,
//                new Grids_GridDoubleStatsNotUpdated(ge));
        Grids_GridDouble grid = toGrid(polyGrid, nrows, ncols, xllcorner,
                yllcorner, cellsize, pup, gp.gridFactoryDouble);
        MapContent mc = dwEnv.getGeotools().createMapContent(
                new DW_Shapefile(lpsf),
                oas,
                null,//LSOA_Shapefile,
                pup,//null,//MSOA_Shapefile,
                polyGrid,
                null,//lineGrid,//null,//LAD_Shapefile,
                new DW_Shapefile(pups),
                new DW_Shapefile(psps));
        dwEnv.getGeotools().outputToImage(mc, outname, lpsf, mapDirectory, 
                imageWidth, showMapsInJMapPane);
    }

    public Grids_GridDouble toGrid(DW_Shapefile polyGrid, long nrows, long ncols,
            double xllcorner, double yllcorner, double cellsize,
            DW_Shapefile postcodeUnitPoly_DW_Shapefile, Grids_GridFactoryDouble f) throws IOException, Exception {
        Grids_GridDouble r;
        Grids_Dimensions dimensions;
        dimensions = new Grids_Dimensions(
                new BigDecimal(xllcorner),
                new BigDecimal(yllcorner),
                new BigDecimal(xllcorner + cellsize * ncols),
                new BigDecimal(yllcorner + cellsize * nrows),
                new BigDecimal(cellsize));
        r = (Grids_GridDouble) f.create(nrows, ncols, dimensions);
//        FeatureCollection cells = polyGrid.getFeatureCollection();
//        IntersectUtils.intersection(null, null)
//        JTS. ${geotools.version}
//       JTS.unionintersects
//        
//        FeatureIterator fi;
//        fi = cells.features();
//        while (fi.hasNext()) {
//            SimpleFeature sf;
//            sf = (SimpleFeature) fi.next();
//            Geometry intersection;
//            intersection = sf.i
//        }
        return r;
    }

    public DW_Shapefile getPolyGrid(long nrows, long ncols, double xllcorner,
            double yllcorner, double cellsize) throws IOException {
        String filename = "Gridlines_" + nrows + "_" + ncols + "_" + xllcorner + "_"
                + yllcorner + "_" + cellsize + "_" + ".shp";
        Path dir = Paths.get(files.getGeneratedDir().toString(), "LineGrids");
        Files.createDirectories(dir);
        Path shapefile = createPolyGridShapefileIfItDoesNotExist(dir,
                filename, nrows, ncols, xllcorner, yllcorner, cellsize);
        return new DW_Shapefile(shapefile);
    }

    public DW_Shapefile getLineGrid(long nrows, long ncols, double xllcorner,
            double yllcorner, double cellsize) throws IOException {
        String filename = "Gridlines_" + nrows + "_" + ncols + "_" + xllcorner
                 + "_" + yllcorner + "_" + cellsize + "_" + ".shp";
        Path dir = Paths.get(files.getGeneratedDir().toString(), "LineGrids");
        Files.createDirectories(dir);
        Path shapefile = createLineGridShapefileIfItDoesNotExist(dir, filename,
                nrows, ncols, xllcorner, yllcorner, cellsize);
        return new DW_Shapefile(shapefile);
    }

    /**
     * @param ym3 The year month.
     * @return A List of Shapefiles.
     * @throws java.io.IOException If encountered.
     * @throws java.lang.ClassNotFoundException If encountered.
     */
    public ArrayList<DW_Shapefile> getPostcodePointDW_Shapefiles(
            UKP_YM3 ym3) throws IOException, ClassNotFoundException {
        ArrayList<DW_Shapefile> r = new ArrayList<>();
        Path dir = files.getGeneratedPostcodeDir();
        ArrayList<Integer> postCodeLevels = new ArrayList<>();
        postCodeLevels.add(UKP_Data.TYPE_UNIT);
        postCodeLevels.add(UKP_Data.TYPE_SECTOR);
        postCodeLevels.add(UKP_Data.TYPE_DISTRICT);
        Iterator<Integer> ite = postCodeLevels.iterator();
        while (ite.hasNext()) {
            int pl = ite.next();
            // Create LS Postcode points
            String sf_name = "LS_Postcodes" + pl + "CentroidsPoint.shp";
            Path f = createPostcodePointShapefileIfItDoesNotExist(ym3, pl, dir,
                    sf_name, "LS");
            r.add(new DW_Shapefile(f));
        }
        return r;
    }

    /**
     * @param yM3
     * @param postcodeLevel
     * @param dir
     * @param shapefileFilename
     * @param target
     * @return
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public Path createPostcodePointShapefileIfItDoesNotExist(UKP_YM3 yM3,
            int postcodeLevel, Path dir, String shapefileFilename,
            String target) throws IOException, ClassNotFoundException {
        SimpleFeatureType sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc = getPostcodePointFeatureCollection(yM3, 
                postcodeLevel, sft, target);
        return createShapefileIfItDoesNotExist(dir, shapefileFilename, fc, sft);
    }

    public TreeSetFeatureCollection getPostcodePointFeatureCollection(
            UKP_YM3 yM3, int postcodeLevel, SimpleFeatureType sft,
            String target) throws IOException, ClassNotFoundException {
        TreeSetFeatureCollection r = new TreeSetFeatureCollection();
        UKP_Data dph = dwEnv.getUkpData();
        TreeMap<String, ONSPD_Point> tONSPDlookup
                = dwEnv.shbeEnv.oe.getONSPDlookups().get(postcodeLevel).get(
                dph.getNearestYM3ForONSPDLookup(yM3));
        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        Iterator<String> ite = tONSPDlookup.keySet().iterator();
        while (ite.hasNext()) {
            String postcode = ite.next();
            if (postcode.startsWith(target)) {
                ONSPD_Point p = tONSPDlookup.get(postcode);
                Geotools_Point gp = new Geotools_Point(p.getX(), p.getY());
                String name = postcode;
                addPointFeature(gp, gf, sfb, name, r);
            }
        }
        return r;
    }

}
