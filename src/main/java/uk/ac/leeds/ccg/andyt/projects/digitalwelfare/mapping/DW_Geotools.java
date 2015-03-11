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

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Geotools;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_LegendItem;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_LegendLayer;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.AGDT_LegendItem;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.AGDT_LegendLayer;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Shapefile;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Style;
//import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.AGDT_StyleParameters;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps.getOutputImageFile;
/**
 * A class for holding various useful methods for doing things with AGDT_Geotools
 Objects.
 *
 * @author geoagdt
 */
public class DW_Geotools extends AGDT_Geotools {

    /**
     * Warning this will set g to null.
     * @param normalisation
     * @param styleParameters
     * @param index
     * @param outname
     * @param g
     * @param gc
     * @param foregroundDW_Shapefile0
     * @param foregroundDW_Shapefile1
     * @param backgroundDW_Shapefile
     * @param outputDir
     * @param imageWidth
     * @param showMapsInJMapPane 
     * @param scaleToFirst 
     */
    public static void outputToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation,
            AGDT_StyleParameters styleParameters,
            int index,
            String outname,
            AbstractGrid2DSquareCell g,
            GridCoverage2D gc,
            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0,
            AGDT_Shapefile foregroundDW_Shapefile1,
            AGDT_Shapefile backgroundDW_Shapefile,
            File outputDir,
            int imageWidth,
            boolean showMapsInJMapPane,
            boolean scaleToFirst) {
        MapContent mc = createMapContent(
                normalisation,
                g,
                gc,
                foregroundDW_Shapefile0,
                foregroundDW_Shapefile1,
                backgroundDW_Shapefile,
                imageWidth,
                styleParameters,
                index,
                scaleToFirst);
        // Set g to null as it is no longer needed. 
        // This is done to prevent any unwanted OutOfMemory Errors being encountered.
        g = null;

        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        File outputFile = getOutputFile(
                outputDir,
                outname,
                DW_Maps.png_String);
        File outputImageFile = DW_Maps.getOutputImageFile(
                outputFile, DW_Maps.png_String);

        AGDT_Geotools.writeImageFile(
                //g._Grids_Environment,
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                DW_Maps.png_String);

        // Dispose of MapContent to prevent memory leaks
        if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Tidy up
            //gc.dispose();
            // Dispose of mc to avoid memory leaks
            //mc.removeLayer(backgroundFeatureLayer);
            List<Layer> layers = mc.layers();
            Iterator<Layer> ite = layers.iterator();
            while (ite.hasNext()) {
                Layer l = ite.next();
//                if (l.equals(backgroundFeatureLayer)) {
//                    System.out.println("Odd this was removed from MapContent!");
//                } else {
                l.preDispose();
                l.dispose();
//                }
            }
            //mc.removeLayer(backgroundFeatureLayer);
            mc.dispose();
        }
    }

    private static MapContent createMapContent(
            double normalisation,
            AbstractGrid2DSquareCell g,
            GridCoverage2D gc,
            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0,
            AGDT_Shapefile foregroundDW_Shapefile1,
            AGDT_Shapefile backgroundDW_Shapefile,
            int imageWidth,
            AGDT_StyleParameters styleParameters,
            int index,
            boolean scaleToFirst) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = styleParameters.getStyle(index);
        ArrayList<AGDT_LegendItem> legendItems = null;

        if (styleParameters.isDrawBoundaries()) {
            FeatureLayer backgroundFeatureLayer;
            backgroundFeatureLayer = backgroundDW_Shapefile.getFeatureLayer(
                    styleParameters.getBackgroundStyle());
            result.addLayer(backgroundFeatureLayer);
        }

        // Add output to mc
        // ----------------
        // If input style is null then create a basic Style to render the 
        // features
        if (style == null) {
            Object[] styleAndLegendItems;
//            styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
            styleAndLegendItems = DW_Style.getStyleAndLegendItems(
                    normalisation,
                    g,
                    gc,
                    styleParameters.getClassificationFunctionName(),
                    styleParameters.getnClasses(),
                    styleParameters.getPaletteName(),
                    styleParameters.isAddWhiteForZero());
            style = (Style) styleAndLegendItems[0];
            styleParameters.setStyle(style, index);
            legendItems = (ArrayList<AGDT_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems, index);
        } else {
            if (scaleToFirst) {
                legendItems = styleParameters.getLegendItems(index);
            }
        }
        GridCoverageLayer gcl = new GridCoverageLayer(gc, style);
        result.addLayer(gcl);

        // Add foreground0
        // ---------------
        addForeground0(
                result,
                styleParameters,
                foregroundDW_Shapefile0);

        // Add foreground1
        // ---------------
        if (foregroundDW_Shapefile1 != null) {
            FeatureLayer foregroundFeatureLayer1;
            foregroundFeatureLayer1 = foregroundDW_Shapefile1.getFeatureLayer(
                    styleParameters.getForegroundStyle1());
            result.addLayer(foregroundFeatureLayer1);
        }

        int imageHeight = getMapContentImageHeight(result, imageWidth);

        // Add a legend
        // ------------
        if (legendItems != null) {
            boolean addLegendToTheSide = true;
            AGDT_LegendLayer ll = new AGDT_LegendLayer(
                    styleParameters,
                    "Map title.................................",
                    "Legend",
                    legendItems,
                    result,
                    imageWidth,
                    imageHeight,
                    addLegendToTheSide);
            result.addLayer(ll);
        }

        return result;
    }

    // Add foreground0
    // ---------------
    private static void addForeground0(
            MapContent result,
            AGDT_StyleParameters styleParameters,
            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0) {
        if (foregroundDW_Shapefile0 != null) {
            Iterator<AGDT_Shapefile> ite;
            ite = foregroundDW_Shapefile0.iterator();
            int indexx = 0;
            while (ite.hasNext()) {
                AGDT_Shapefile sf = ite.next();
                FeatureLayer foregroundFeatureLayer0;
                foregroundFeatureLayer0 = sf.getFeatureLayer(
                        styleParameters.getForegroundStyle0().get(indexx));
                result.addLayer(foregroundFeatureLayer0);
                indexx++;
            }
        }
    }

    /**
     * Outputs outputShapefile MapContent as an image. Modifies
     * styleParameters[0] if this is currently null using the remaining
     * styleParameters.
     *
     * @param outname
     * @param outputShapefile
     * @param foregroundDW_Shapefile0
     * @param foregroundDW_Shapefile1
     * @param backgroundDW_Shapefile
     * @param attributeName
     * @param outputDir
     * @param png_String
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @param styleIndex
     * @param showMapsInJMapPane
     */
    public static void outputToImage(
            String outname,
            File outputShapefile,
            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0,
            AGDT_Shapefile foregroundDW_Shapefile1,
            AGDT_Shapefile backgroundDW_Shapefile,
            String attributeName,
            File outputDir,
            String png_String,
            int imageWidth,
            AGDT_StyleParameters styleParameters,
            int styleIndex,
            boolean showMapsInJMapPane) {
        //Style resultStyle;
        DW_Shapefile outputDW_Shapefile;
        outputDW_Shapefile = new DW_Shapefile(outputShapefile);
        String title = outname;
        MapContent mc = createMapContent(
                outputDW_Shapefile,
                title,
                attributeName,
                foregroundDW_Shapefile0,
                foregroundDW_Shapefile1,
                backgroundDW_Shapefile,
                imageWidth,
                styleParameters,
                styleIndex);
        outputToImage(
                mc,
                outname,
                outputShapefile,
                outputDir,
                imageWidth,
                showMapsInJMapPane);
    }

    /**
     * Outputs outputShapefile MapContent as an image. Modifies
     * styleParameters[0] if this is currently null using the remaining
     * styleParameters.
     *
     * @param mc
     * @param outname
     * @param outputShapefile
     * @param outputDir
     * @param imageWidth
     * @param showMapsInJMapPane
     */
    public static void outputToImage(
            MapContent mc,
            String outname,
            File outputShapefile,
            File outputDir,
            int imageWidth,
            boolean showMapsInJMapPane) {
        DW_Shapefile outputDW_Shapefile;
        outputDW_Shapefile = new DW_Shapefile(outputShapefile);
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        File outputFile = getOutputFile(
                outputDir,
                outname,
                DW_Maps.png_String);
        File outputImageFile = DW_Maps.getOutputImageFile(outputFile, DW_Maps.png_String);
        AGDT_Geotools.writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                DW_Maps.png_String);
        System.out.println("Written " + outputImageFile);
        // Dispose of MapContent to prevent memory leaks
        if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Tidy up
            outputDW_Shapefile.dispose();
            // Dispose of mc to avoid memory leaks
            //mc.removeLayer(backgroundFeatureLayer);
            List<Layer> layers = mc.layers();
            Iterator<Layer> ite = layers.iterator();
            while (ite.hasNext()) {
                Layer l = ite.next();
//                if (l.equals(backgroundFeatureLayer)) {
//                    System.out.println("Odd this was removed from MapContent!");
//                } else {
                l.preDispose();
                l.dispose();
//                }
            }
            //mc.removeLayer(backgroundFeatureLayer);
            mc.dispose();
        }
    }

    /**
     * This both creates MapContent and returns it and also modifies
     * styleParameters if syleParameters[0] == null changing it to the Style as
     * produced from the rest of the styleParameters.
     *
     * @param outputDW_Shapefile
     * @param title
     * @param attributeName
     * @param foregroundDW_Shapefile0
     * @param foregroundDW_Shapefile1
     * @param backgroundDW_Shapefile
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @param styleIndex
     * @return
     */
    protected static MapContent createMapContent(
            //File file,
            //File file,
            DW_Shapefile outputDW_Shapefile,
            String title,
            String attributeName,
            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0,
            AGDT_Shapefile foregroundDW_Shapefile1,
            AGDT_Shapefile backgroundDW_Shapefile,
            int imageWidth,
            AGDT_StyleParameters styleParameters,
            int styleIndex) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = styleParameters.getStyle(styleIndex);
        ArrayList<AGDT_LegendItem> legendItems;
        legendItems = styleParameters.getLegendItems(styleIndex);

        if (styleParameters.isDrawBoundaries()) {
            FeatureLayer backgroundFeatureLayer;
            backgroundFeatureLayer = backgroundDW_Shapefile.getFeatureLayer(
                    styleParameters.getBackgroundStyle());
            result.addLayer(backgroundFeatureLayer);
        }

        // Add output to mc
        // ----------------
        FeatureSource fs;
        fs = outputDW_Shapefile.getFeatureSource();
        Layer layer;
        // If input style is null then create a basic Style to render the 
        // features
        Object[] styleAndLegendItems;
        if (style == null) {
            styleAndLegendItems = getStyleAndLegendItems(
                    fs,
                    attributeName,
                    styleParameters);
            style = (Style) styleAndLegendItems[0];
            styleParameters.setStyle(style, styleIndex);
            legendItems = (ArrayList<AGDT_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems, styleIndex);
        }
        // Add the features and the associated Style object to mc as a new 
        // Layer
        layer = new FeatureLayer(fs, style);
        result.addLayer(layer);

        // Add foreground0
        // ---------------
        addForeground0(result, styleParameters, foregroundDW_Shapefile0);

        // Add foreground1
        // ---------------
        if (foregroundDW_Shapefile1 != null) {
            FeatureLayer foregroundFeatureLayer1;
            foregroundFeatureLayer1 = foregroundDW_Shapefile1.getFeatureLayer(
                    styleParameters.getForegroundStyle1());
            result.addLayer(foregroundFeatureLayer1);
        }

        int imageHeight = getMapContentImageHeight(result, imageWidth);

        // Add a legend
        // ------------
        if (legendItems != null) {
            boolean addLegendToTheSide = true;
            AGDT_LegendLayer ll = new AGDT_LegendLayer(
                    styleParameters,
                    "Map title.................................",
                    "Legend",
                    legendItems,
                    result,
                    imageWidth,
                    imageHeight,
                    addLegendToTheSide);
            result.addLayer(ll);
        }
        return result;
    }

//    /**
//     * polygon0 PostcodeSector
//     * polygon1 OA
//     * polygon2 LSOA
//     * polygon3 postcodeUnitPoly / MSOA
//     * polygon4 polyGrid,
//     * line0 lineGrid
//     * points0 PostcodeUnitPoint
//     * points1 PostcodeSectorPoint
//     * @param polygon0
//     * @param polygon1
//     * @param polygon2
//     * @param polygon3
//     * @param polygon4
//     * @param line0
//     * @param points0
//     * @param points1
//     * @return
//     */
//    public static MapContent createMapContent(
//            DW_Shapefile polygon0,
//            DW_Shapefile polygon1,
//            DW_Shapefile polygon2,
//            DW_Shapefile polygon3,
//            DW_Shapefile polygon4,
//            DW_Shapefile line0,
//            DW_Shapefile points0,
//            DW_Shapefile points1) {
//        MapContent result;
//        result = new MapContent();
//
//        if (polygon0 != null) {
//            // Add polygon layer 0 to mc
//            // -------------------------
//            Style polygon0Style;
//            polygon0Style = DW_Style.createDefaultPolygonStyle(
//                    Color.BLUE, Color.WHITE);
//            FeatureLayer polygon0layer = new FeatureLayer(
//                    polygon0.getFeatureSource(), polygon0Style);
//            result.addLayer(polygon0layer);
//        }
//
//        if (polygon3 != null) {
//            // Add polygon layer 3 to mc
//            // -------------------------
//            Color c;
//            //c = Color.DARK_GRAY;
//            c = Color.PINK;
//            Style polygon3Style;
//            polygon3Style = DW_Style.createDefaultPolygonStyle(c, null);
//            FeatureLayer polygon3layer = new FeatureLayer(
//                    polygon3.getFeatureSource(), polygon3Style);
//            result.addLayer(polygon3layer);
//        }
//        
//        if (polygon1 != null) {
//            // Add polygon layer 1 to mc
//            // -------------------------
//            Color c;
//            //c = Color.LIGHT_GRAY;
//            c = Color.BLACK;
//            Style polygon1Style;
//            polygon1Style = DW_Style.createDefaultPolygonStyle(c, null);
//            FeatureLayer polygon1layer = new FeatureLayer(
//                    polygon1.getFeatureSource(), polygon1Style);
//            result.addLayer(polygon1layer);
//        }
//
//        if (polygon2 != null) {
//            // Add polygon layer 2 to mc
//            // -------------------------
//            Style polygon2Style;
//            polygon2Style = DW_Style.createDefaultPolygonStyle(
//                    Color.GRAY, null);
//            FeatureLayer polygon2layer = new FeatureLayer(
//                    polygon2.getFeatureSource(), polygon2Style);
//            result.addLayer(polygon2layer);
//        }
//
//        
//
//        if (polygon4 != null) {
//            // Add polygon layer 4 to mc
//            // -------------------------
//            Color c;
//            //c = Color.BLACK;
//            c = Color.LIGHT_GRAY;
//            Style polygon4Style;
//            polygon4Style = DW_Style.createDefaultPolygonStyle(c, null);
//            FeatureLayer polygon4layer = new FeatureLayer(
//                    polygon4.getFeatureSource(), polygon4Style);
//            result.addLayer(polygon4layer);
//        }
//
//        if (line0 != null) {
//            // Add line layer 0 to mc
//            // -------------------------
//            Style line0Style;
//            line0Style = DW_Style.createDefaultLineStyle();
//            FeatureLayer line0layer = new FeatureLayer(
//                    line0.getFeatureSource(), line0Style);
//            result.addLayer(line0layer);
//        }
//
//        // Add point layer 0 to mc
//        // -----------------------
//        Style pointStyle0;
//        pointStyle0 = DW_Style.createDefaultPointStyle();
//        FeatureLayer pointsFeatureLayer0;
//        pointsFeatureLayer0 = points0.getFeatureLayer(
//                pointStyle0);
//        result.addLayer(pointsFeatureLayer0);
//
//        // Add point layer 1 to mc
//        // -----------------------
//        int size;
//        size = 6;
//        String type;
//        type = "Circle";
//        Color fill;
//        fill = Color.GREEN;
//        Color outline;
//        outline = Color.DARK_GRAY;
//        Style pointStyle1;
//        pointStyle1 = DW_Style.getPointStyle(
//                size,
//                type,
//                fill,
//                outline);
//        FeatureLayer pointsFeatureLayer1;
//        pointsFeatureLayer1 = points1.getFeatureLayer(
//                pointStyle1);
//        result.addLayer(pointsFeatureLayer1);
//        return result;
//    }

    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to get a
     * Style. This methods works out what sort of feature geometry we have in
     * the shapefile and then delegates to an appropriate style creating method.
     */
    private static Object[] getStyleAndLegendItems(
            FeatureSource fs,
            String attributeName,
            AGDT_StyleParameters styleParameters) {
        SimpleFeatureType schema = (SimpleFeatureType) fs.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            FeatureCollection fc = null;
            try {
                fc = fs.getFeatures();
            } catch (IOException ex) {
                Logger.getLogger(AGDT_Geotools.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            return DW_Style.createPolygonStyle(
                    fc,
                    attributeName,
                    styleParameters);
        }
        if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            Object[] result = new Object[2];
            result[0] = DW_Style.createDefaultLineStyle();
            return result;
        } else {
            Object[] result = new Object[2];
            result[0] = DW_Style.createDefaultPointStyle();
            DW_Style.createDefaultPointStyle();
            return result;
        }
    }

    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to get a
     * Style. This methods works out what sort of feature geometry we have in
     * the shapefile and then delegates to an appropriate style creating method.
     */
    private static Object[] createStyle(
            GridCoverage cov,
            //String attributeName,
            String classificationFunctionName,
            int nClasses,
            String paletteName,
            StyleFactory sf,
            boolean addWhiteForZero) {
        return null;
    }

    /**
     * @param mapDirectory
     * @param name
     * @param fileExtension
     * @return File
     */
    public static File getOutputFile(
            File mapDirectory,
            String name,
            String fileExtension) {
        File outDirectory = new File(
                mapDirectory,
                name);
        outDirectory.mkdirs();
        File file = new File(
                outDirectory,
                name + "." + fileExtension);
        return file;
    }

    /**
     * Shapefiles are best stored in a directory with the full shapefile name
     * for GeoTools.
     *
     * @param dir
     * @param name
     * @return File
     */
    public static File getOutputShapefile(
            File dir,
            String name) {
        File result;
        File outDirectory = new File(
                dir,
                name);
        outDirectory.mkdirs();
        String shapefileFilename = name + ".shp";
        result = getShapefile(dir, shapefileFilename);
        return result;
    }

    public static File getShapefile(
            File dir,
            String shapefileFilename) {
        File result;
        File shapefileDir;
        shapefileDir = new File(
                dir,
                shapefileFilename);
        // Could add extra logic here to deal with issues if directory or a file 
        // of this name already exists...
        if (!shapefileDir.exists()) {
            shapefileDir.mkdirs();
        }
        result = new File(
                shapefileDir,
                shapefileFilename);
        return result;
    }

//    public static void addFeatureLayer(
//            MapContent mc,
//            File shapefile,
//            Style style,
//            String title) {
//        FeatureLayer fl;
//        fl = DW_Shapefile.getFeatureLayer(shapefile, style, title);
//        mc.addLayer(fl);
//    }
    /**
     * Produces a verbal comparison of sf1 and sf2 printing the results via
     * System.out.
     *
     * @param sf1 One of two simple features to compare.
     * @param sf2 Two of two simple features to compare.
     */
    public static void compareFeatures(
            SimpleFeature sf1,
            SimpleFeature sf2) {
        String sf1String = sf1.toString();
        System.out.println("sf1String " + sf1String);
        String sf2String = sf2.toString();
        System.out.println("sf2String " + sf2String);
        if (sf1String.equalsIgnoreCase(sf2String)) {
            System.out.println("sf1String.equalsIgnoreCase(sf2String)");
        } else {
            System.out.println("! sf1String.equalsIgnoreCase(sf2String)");
        }
        int sf1AttributeCount = sf1.getAttributeCount();
        int sf2AttributeCount = sf2.getAttributeCount();
        System.out.println("sf1AttributeCount " + sf1AttributeCount);
        System.out.println("sf2AttributeCount " + sf2AttributeCount);
        List<Object> sf1Attributes = sf1.getAttributes();
        List<Object> sf2Attributes = sf2.getAttributes();
        int i;
        Iterator<Object> ite;
        i = 0;
        ite = sf1Attributes.iterator();
        while (ite.hasNext()) {
            Object o = ite.next();
            System.out.println("sf1Attribute[" + i + "].getClass() = " + o.getClass());
            i++;
        }
        i = 0;
        ite = sf2Attributes.iterator();
        while (ite.hasNext()) {
            Object o = ite.next();
            System.out.println("sf2Attribute[" + i + "].getClass() = " + o.getClass());
            i++;
        }
    }

    /**
     * 
     * @param ge
     * @param mapContent
     * @param imageWidth
     * @param imageHeight
     * @param outputImageFile
     * @param outputType 
     */
    public static void writeImageFile(
            Grids_Environment ge,
            MapContent mapContent,
            int imageWidth,
            int imageHeight,
            File outputImageFile,
            String outputType) {
        try {
            writeImageFile(
                    mapContent,
                    imageWidth,
                    imageHeight,
                    outputImageFile,
                    outputType);
        } catch (OutOfMemoryError oome) {
            if (ge._HandleOutOfMemoryError_boolean) {
                ge.clear_MemoryReserve();
                ge.swapToFile_Grid2DSquareCellChunk(true);
                ge.init_MemoryReserve(true);
                writeImageFile(
                        ge,
                        mapContent,
                        imageWidth,
                        imageHeight,
                        outputImageFile,
                        outputType);
            } else {
                throw oome;
            }
        }
    }

    /**
     * Render and output the mapContent and save to a file.
     *
     * @param mapContent The map to be written.
     * @param imageWidth The width of the image to be produced.
     * @param imageHeight The height of the image to be produced.
     * @param outputImageFile The file to be written to.
     * @param outputType The image file type e.g. "png"
     */
    public static void writeImageFile(
            MapContent mapContent,
            int imageWidth,
            int imageHeight,
            File outputImageFile,
            String outputType) {
        // Initialise a renderer
        StreamingRenderer renderer;
        renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        Rectangle rectangle = new Rectangle(imageWidth, imageHeight);
        //System.out.println(rectangle.height + " " + rectangle.width);
        BufferedImage bufferedImage;
        bufferedImage = new BufferedImage(
                rectangle.width,
                rectangle.height,
                //BufferedImage.TYPE_INT_RGB);
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        // Set white background
        //graphics2D.setComposite(AlphaComposite.Clear);
        graphics2D.setBackground(Color.white);
        graphics2D.fillRect(0, 0, imageWidth, imageHeight);
        //graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
        // Render onto background
        //graphics2D.setComposite(AlphaComposite.Src);
        renderer.paint(graphics2D, rectangle, mapContent.getMaxBounds());
        Generic_Visualisation.saveImage(bufferedImage, outputType, outputImageFile);
        graphics2D.dispose();
    }

//    public static GridCoverage2D vectorToRaster(
//            DW_Shapefile polygons,
//            long nrows,
//            long ncols,
//            double xllcorner,
//            double yllcorner,
//            double cellsize) {
//        
//        
//    }
}
