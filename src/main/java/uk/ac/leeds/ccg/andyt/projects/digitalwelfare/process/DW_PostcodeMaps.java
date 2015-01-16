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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.MapContent;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_CensusAreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_GeoTools;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps.getPointSimpleFeatureType;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Shapefile;

/**
 *
 * @author geoagdt
 */
public class DW_PostcodeMaps extends DW_Maps {

    /**
     * For storing property for selecting
     */
    private String targetPropertyName;

    public DW_PostcodeMaps() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_PostcodeMaps().run();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
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
        mapDirectory = DW_Files.getGeneratedPostcodeDir();

        ShapefileDataStoreFactory sdsf = getShapefileDataStoreFactory();

        // Postcode Sector Boundaries
        File aLeedsPostcodeSectorShapefile = getLeedsPostcodeSectorShapefile();

        // Postcode Unit Centroids
        String aLeedsPostcodeUnitPointShapefile;
        aLeedsPostcodeUnitPointShapefile = "LeedsPostcodeUnitPointShapefile.shp";
        File aPostcodeUnitPointShapefile = createPostcodePointShapefileIfItDoesNotExist(
                0,
                mapDirectory,
                aLeedsPostcodeUnitPointShapefile,
                "LS");

        // Density of postcodes
        // Postcode Sector Centroids
        String aLeedsPostcodeSectorPointShapefile;
        aLeedsPostcodeSectorPointShapefile = "LeedsPostcodeSectorPointShapefile.shp";
        File aPostcodeSectorPointShapefile = createPostcodePointShapefileIfItDoesNotExist(
                1,
                mapDirectory,
                aLeedsPostcodeSectorPointShapefile,
                "LS");

        // OA
        level = "OA";
        targetPropertyName = "CODE";
        DW_CensusAreaCodesAndShapefiles tOACodesAndLeedsLevelShapefile;
        tOACodesAndLeedsLevelShapefile = new DW_CensusAreaCodesAndShapefiles(
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
        DW_CensusAreaCodesAndShapefiles tLSOACodesAndLeedsLevelShapefile;
        tLSOACodesAndLeedsLevelShapefile = new DW_CensusAreaCodesAndShapefiles(
                level,
                targetPropertyName,
                sdsf);
        DW_Shapefile LSOA_Shapefile;
        LSOA_Shapefile = tLSOACodesAndLeedsLevelShapefile.getLeedsLevelDW_Shapefile();

        // MSOA
        level = "MSOA";
        targetPropertyName = "MSOA11CD";
        DW_CensusAreaCodesAndShapefiles tMSOACodesAndLeedsLevelShapefile;
        tMSOACodesAndLeedsLevelShapefile = new DW_CensusAreaCodesAndShapefiles(
                level,
                targetPropertyName,
                sdsf);
        DW_Shapefile MSOA_Shapefile;
        MSOA_Shapefile = tMSOACodesAndLeedsLevelShapefile.getLeedsLevelDW_Shapefile();

        // Grid of lines
        DW_Shapefile gridOfLines;
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
        gridOfLines = getGridOfLines(
                nrows, ncols, xllcorner, yllcorner, cellsize);

        String outname = "outname";

        MapContent mc = DW_GeoTools.createMapContent(
                new DW_Shapefile(aLeedsPostcodeSectorShapefile),
                OA_Shapefile,
                null,//LSOA_Shapefile,
                null,//MSOA_Shapefile,
                null,
                gridOfLines,//null,//LAD_Shapefile,
                new DW_Shapefile(aPostcodeUnitPointShapefile),
                new DW_Shapefile(aPostcodeSectorPointShapefile));

        File outputDir = mapDirectory;

        DW_GeoTools.outputToImage(
                mc,
                outname,
                aLeedsPostcodeSectorShapefile,
                outputDir,
                imageWidth,
                showMapsInJMapPane);

    }

    public static DW_Shapefile getGridOfLines(
            long nrows,
            long ncols,
            double xllcorner,
            double yllcorner,
            double cellsize) {
        DW_Shapefile result;
        String filename;
        filename = "Gridlines_" + nrows + "_" + ncols + "_" + xllcorner + "_"
                + yllcorner + "_" + cellsize + "_" + ".shp";
        File dir;
        dir = new File(
                DW_Files.getGeneratedDir(),
                "LineGrids");
        dir.mkdirs();
        File shapefile = createGridLineShapefileIfItDoesNotExist(
                dir,
                filename,
                nrows,
                ncols,
                xllcorner,
                yllcorner,
                cellsize);
        result = new DW_Shapefile(shapefile);
        return result;
    }

    /**
     * @return
     */
    public static ArrayList<DW_Shapefile> getPostcodePointDW_Shapefiles() {
        ArrayList<DW_Shapefile> result;
        result = new ArrayList<DW_Shapefile>();
        File dir;
        dir = DW_Files.getGeneratedPostcodeDir();
        // Create LS Postcode points
        String sf_name;
        sf_name = "LS_PostcodesUnitCentroidsPoint.shp";
        File sf_File;
        sf_File = createPostcodePointShapefileIfItDoesNotExist(
                0,
                dir,
                sf_name,
                "LS");
        result.add(new DW_Shapefile(sf_File));
        sf_name = "LS_PostcodesSectorCentroidsPoint.shp";
        sf_File = createPostcodePointShapefileIfItDoesNotExist(
                1,
                dir,
                sf_name,
                "LS");
        result.add(new DW_Shapefile(sf_File));
        return result;
    }

    /**
     * @param index
     * @param dir
     * @param shapefileFilename
     * @param target
     * @return
     */
    public static File createPostcodePointShapefileIfItDoesNotExist(
            int index,
            File dir,
            String shapefileFilename,
            String target) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getPostcodePointFeatureCollection(
                index, sft, target);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    public static TreeSetFeatureCollection getPostcodePointFeatureCollection(
            int index,
            SimpleFeatureType sft,
            String target) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        TreeMap<String, DW_Point> tONSPDlookup = getONSPDlookups()[index];
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
                DW_Point p = tONSPDlookup.get(postcode);
                String name = postcode;
                addPointFeature(p, gf, sfb, name, result);
            }
        }
        return result;
    }
}
