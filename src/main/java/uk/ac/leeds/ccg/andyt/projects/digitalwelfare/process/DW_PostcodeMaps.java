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

import com.vividsolutions.jts.geom.GeometryFactory;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.MapContent;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Point;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDoubleFactory;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Dimensions;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.grid.stats.Grids_GridDoubleStatsNotUpdated;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_Files;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_Processor;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Shapefile;

/**
 *
 * @author geoagdt
 */
public class DW_PostcodeMaps extends DW_Maps {

    /**
     * For storing property for selecting
     */
    private String targetPropertyName;

    public DW_PostcodeMaps(DW_Environment de) {
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
     */
    public void run() {
        showMapsInJMapPane = true;//false;//true;//false;
        imageWidth = 1000;
        mapDirectory = Files.getGeneratedPostcodeDir();

        sdsf = getShapefileDataStoreFactory();

        // Boundaries
        // Postcode Sectors
        File aLeedsPostcodeSectorShapefile = getLeedsPostcodeSectorShapefile();
        // Postcode Units
        DW_Shapefile postcodeUnitPoly_DW_Shapefile;
        postcodeUnitPoly_DW_Shapefile = getPostcodeUnitPoly_DW_Shapefile(Env,
                new ShapefileDataStoreFactory());

        ONSPD_YM3 yM3;
        yM3 = Postcode_Handler.getDefaultYM3();

        String postcodeLevel;
        // Postcode Unit Centroids
        postcodeLevel = "Unit";
        String aLeedsPostcodeUnitPointShapefile;
        aLeedsPostcodeUnitPointShapefile = "LeedsPostcode" + postcodeLevel + "PointShapefile.shp";
        File aPostcodeUnitPointShapefile = createPostcodePointShapefileIfItDoesNotExist(
                yM3,
                postcodeLevel,
                mapDirectory,
                aLeedsPostcodeUnitPointShapefile,
                "LS");
        // Postcode Sector Centroids
        postcodeLevel = "Sector";
        String aLeedsPostcodeSectorPointShapefile;
        aLeedsPostcodeSectorPointShapefile = "LeedsPostcode" + postcodeLevel + "PointShapefile.shp";
        File aPostcodeSectorPointShapefile = createPostcodePointShapefileIfItDoesNotExist(
                yM3,
                postcodeLevel,
                mapDirectory,
                aLeedsPostcodeSectorPointShapefile,
                "LS");
        // Postcode District Centroids
        postcodeLevel = "District";
        String aLeedsPostcodeDistrictPointShapefile;
        aLeedsPostcodeDistrictPointShapefile = "LeedsPostcode" + postcodeLevel + "PointShapefile.shp";
        File aPostcodeDistrictPointShapefile = createPostcodePointShapefileIfItDoesNotExist(
                yM3,
                postcodeLevel,
                mapDirectory,
                aLeedsPostcodeSectorPointShapefile,
                "LS");

        String level;
        // OA
        level = "OA";
        targetPropertyName = "CODE";
        DW_AreaCodesAndShapefiles tOACodesAndLeedsLevelShapefile;
        tOACodesAndLeedsLevelShapefile = new DW_AreaCodesAndShapefiles(
                Env,
                level,
                targetPropertyName,
                sdsf);
        DW_Shapefile OA_Shapefile;
        OA_Shapefile = tOACodesAndLeedsLevelShapefile.getLeedsLevelDW_Shapefile();
        DW_Shapefile LAD_Shapefile;
        LAD_Shapefile = tOACodesAndLeedsLevelShapefile.getLeedsLADDW_Shapefile();

        // LSOA
        level = "LSOA";
        targetPropertyName = "LSOA11CD";
        DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLevelShapefile;
        tLSOACodesAndLeedsLevelShapefile = new DW_AreaCodesAndShapefiles(
                Env,
                level,
                targetPropertyName,
                sdsf);
        DW_Shapefile LSOA_Shapefile;
        LSOA_Shapefile = tLSOACodesAndLeedsLevelShapefile.getLeedsLevelDW_Shapefile();

        // MSOA
        level = "MSOA";
        targetPropertyName = "MSOA11CD";
        DW_AreaCodesAndShapefiles tMSOACodesAndLeedsLevelShapefile;
        tMSOACodesAndLeedsLevelShapefile = new DW_AreaCodesAndShapefiles(
                Env,
                level,
                targetPropertyName,
                sdsf);
        DW_Shapefile MSOA_Shapefile;
        MSOA_Shapefile = tMSOACodesAndLeedsLevelShapefile.getLeedsLevelDW_Shapefile();

        long nrows;
        long ncols;
        double xllcorner;
        double yllcorner;
        double cellsize;
        nrows = 139;//277;//554;
        ncols = 170;//340;//680;
        xllcorner = 413000;
        yllcorner = 422500;
        cellsize = 200;//100;//50;
        // LineGrid
        DW_Shapefile lineGrid;
        lineGrid = getLineGrid(
                nrows, ncols, xllcorner, yllcorner, cellsize);
        // PolyGrid
        DW_Shapefile polyGrid;
        polyGrid = getPolyGrid(
                nrows, ncols, xllcorner, yllcorner, cellsize);
        Grids_Dimensions dimensions;
        dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));

        String outname = "outname";
        Grids_Environment ge;
        ge = Env.getGrids_Environment();
        Grids_GridDoubleFactory gf;
        Grids_Processor gp;
        gp = ge.getProcessor();
        gf = new Grids_GridDoubleFactory(ge, gp.GridChunkDoubleFactory,
                gp.DefaultGridChunkDoubleFactory, -Double.MAX_VALUE,
                (int) nrows, (int) ncols, dimensions,
                new Grids_GridDoubleStatsNotUpdated(Env.getGrids_Environment()));
        Grids_GridDouble grid;
        grid = toGrid(polyGrid, nrows, ncols, xllcorner, yllcorner, cellsize,
                postcodeUnitPoly_DW_Shapefile, gf);
        MapContent mc;
        mc = Env.getGeotools().createMapContent(
                new DW_Shapefile(aLeedsPostcodeSectorShapefile),
                OA_Shapefile,
                null,//LSOA_Shapefile,
                postcodeUnitPoly_DW_Shapefile,//null,//MSOA_Shapefile,
                polyGrid,
                null,//lineGrid,//null,//LAD_Shapefile,
                new DW_Shapefile(aPostcodeUnitPointShapefile),
                new DW_Shapefile(aPostcodeSectorPointShapefile));

        File outputDir = mapDirectory;

        Env.getGeotools().outputToImage(mc, outname,
                aLeedsPostcodeSectorShapefile, outputDir, imageWidth,
                showMapsInJMapPane);

    }

    public Grids_GridDouble toGrid(
            DW_Shapefile polyGrid,
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize,
            DW_Shapefile postcodeUnitPoly_DW_Shapefile,
            Grids_GridDoubleFactory f) {
        Grids_GridDouble result;
        Grids_Dimensions dimensions;
        dimensions = new Grids_Dimensions(
                new BigDecimal(xllcorner),
                new BigDecimal(yllcorner),
                new BigDecimal(xllcorner + cellsize * ncols),
                new BigDecimal(yllcorner + cellsize * nrows),
                new BigDecimal(cellsize));
        Grids_Files gf;
        gf = ge.getGrids_Environment().getFiles();
        File dir = gf.createNewFile(gf.getGeneratedGridDoubleDir());
        result = (Grids_GridDouble) f.create(dir, nrows, ncols, dimensions);

        FeatureCollection cells;
        cells = polyGrid.getFeatureCollection();

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
        return result;
    }

    public DW_Shapefile getPolyGrid(long nrows, long ncols, double xllcorner,
            double yllcorner, double cellsize) {
        DW_Shapefile result;
        String filename;
        filename = "Gridlines_" + nrows + "_" + ncols + "_" + xllcorner + "_"
                + yllcorner + "_" + cellsize + "_" + ".shp";
        File dir;
        dir = new File(Files.getGeneratedDataDir(), "LineGrids");
        dir.mkdirs();
        File shapefile = createPolyGridShapefileIfItDoesNotExist(dir,
                filename, nrows, ncols, xllcorner, yllcorner, cellsize);
        result = new DW_Shapefile(shapefile);
        return result;
    }

    public DW_Shapefile getLineGrid(long nrows, long ncols, double xllcorner,
            double yllcorner, double cellsize) {
        DW_Shapefile result;
        String filename;
        filename = "Gridlines_" + nrows + "_" + ncols + "_" + xllcorner + "_"
                + yllcorner + "_" + cellsize + "_" + ".shp";
        File dir;
        dir = new File(Files.getGeneratedDataDir(), "LineGrids");
        dir.mkdirs();
        File shapefile = createLineGridShapefileIfItDoesNotExist(dir, filename,
                nrows, ncols, xllcorner, yllcorner, cellsize);
        result = new DW_Shapefile(shapefile);
        return result;
    }

    /**
     * @param yM3
     * @return
     */
    public ArrayList<DW_Shapefile> getPostcodePointDW_Shapefiles(
            ONSPD_YM3 yM3) {
        ArrayList<DW_Shapefile> result;
        result = new ArrayList<>();
        File dir;
        dir = Files.getGeneratedPostcodeDir();
        ArrayList<String> postCodeLevels;
        postCodeLevels = new ArrayList<>();
        postCodeLevels.add("Unit");
        postCodeLevels.add("Sector");
        postCodeLevels.add("District");
        Iterator<String> ite;
        ite = postCodeLevels.iterator();
        while (ite.hasNext()) {
            String postcodeLevel = ite.next();
            // Create LS Postcode points
            String sf_name;
            sf_name = "LS_Postcodes" + postcodeLevel + "CentroidsPoint.shp";
            File sf_File;
            sf_File = createPostcodePointShapefileIfItDoesNotExist(yM3,
                    postcodeLevel, dir, sf_name, "LS");
            result.add(new DW_Shapefile(sf_File));
        }
        return result;
    }

    /**
     * @param yM3
     * @param postcodeLevel
     * @param dir
     * @param shapefileFilename
     * @param target
     * @return
     */
    public File createPostcodePointShapefileIfItDoesNotExist(ONSPD_YM3 yM3,
            String postcodeLevel, File dir, String shapefileFilename,
            String target) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getPostcodePointFeatureCollection(yM3, postcodeLevel, sft, target);
        result = createShapefileIfItDoesNotExist(dir, shapefileFilename, fc,
                sft);
        return result;
    }

    public TreeSetFeatureCollection getPostcodePointFeatureCollection(
            ONSPD_YM3 yM3, String postcodeLevel, SimpleFeatureType sft, 
            String target) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        ONSPD_Postcode_Handler dph;
        dph = Env.getPostcode_Handler();
        TreeMap<String, ONSPD_Point> tONSPDlookup;
        tONSPDlookup = Env.ONSPD_Env.getONSPDlookups().get(postcodeLevel).get(
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
                addPointFeature(gp, gf, sfb, name, result);
            }
        }
        return result;
    }

}
