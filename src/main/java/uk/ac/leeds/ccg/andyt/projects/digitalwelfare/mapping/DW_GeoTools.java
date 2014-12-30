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
import org.geotools.data.FileDataStore;
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
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps.getOutputImageFile;

/**
 * A class for holding various useful methods for doing things with DW_GeoTools
 * Objects.
 *
 * @author geoagdt
 */
public class DW_GeoTools {

    public static int getMapContentImageHeight(MapContent mc, int imageWidth) {
        int result;
        ReferencedEnvelope re = mc.getMaxBounds();
        double height = re.getHeight();
        double width = re.getWidth();
        //System.out.println("height " + height + ", width " + width);
        double heightToWidth = height / width;
        result = (int) (imageWidth * heightToWidth);
        return result;
    }

    public static void outputToImage(
            double normalisation,
            DW_StyleParameters styleParameters,
            String outname,
            AbstractGrid2DSquareCell g,
            GridCoverage2D gc,
            DW_Shapefile foregroundDW_Shapefile0,
            DW_Shapefile foregroundDW_Shapefile1,
            DW_Shapefile backgroundDW_Shapefile,
            File outputDir,
            int imageWidth,
            boolean showMapsInJMapPane) {
        MapContent mc = DW_GeoTools.createMapContent(
                normalisation,
                g,
                gc,
                foregroundDW_Shapefile0,
                foregroundDW_Shapefile1,
                backgroundDW_Shapefile,
                imageWidth,
                styleParameters);
        // Add a legendLayer
        ArrayList<DW_LegendItem> legendItems;
        legendItems = styleParameters.getLegendItems();
        if (legendItems != null) {
            boolean addLegendToTheSide = true;
            DW_LegendLayer ll = new DW_LegendLayer(
                    styleParameters,
                    "Map title.................................",
                    "Legend",
                    legendItems,
                    mc,
                    imageWidth,
                    0,
                    addLegendToTheSide);
            mc.addLayer(ll);
        }

        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        File outputFile = getOutputFile(
                outputDir,
                outname,
                DW_Maps.png_String);
        File outputImageFile = getOutputImageFile(
                outputFile, DW_Maps.png_String);
        DW_GeoTools.writeImageFile(
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
            DW_Shapefile foregroundDW_Shapefile0,
            DW_Shapefile foregroundDW_Shapefile1,
            DW_Shapefile backgroundDW_Shapefile,
            int imageWidth,
            DW_StyleParameters styleParameters) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = styleParameters.getStyle();
        ArrayList<DW_LegendItem> legendItems = null;

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
            styleAndLegendItems = DW_Style.getStyleAndLegendItems(
                    normalisation,
                    g,
                    gc,
                    styleParameters.getnClasses(),
                    styleParameters.getPaletteName(),
                    styleParameters.isAddWhiteForZero());
            style = (Style) styleAndLegendItems[0];
            styleParameters.setStyle(style);
            legendItems = (ArrayList<DW_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems);
        }
        GridCoverageLayer gcl = new GridCoverageLayer(gc, style);
        result.addLayer(gcl);

        // Add foreground0
        // ---------------
        if (foregroundDW_Shapefile0 != null) {
            FeatureLayer foregroundFeatureLayer0;
            foregroundFeatureLayer0 = foregroundDW_Shapefile0.getFeatureLayer(
                    styleParameters.getForegroundStyle0());
            result.addLayer(foregroundFeatureLayer0);
        }

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
            DW_LegendLayer ll = new DW_LegendLayer(
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
     * @param showMapsInJMapPane
     */
    public static void outputToImage(
            String outname,
            File outputShapefile,
            DW_Shapefile foregroundDW_Shapefile0,
            DW_Shapefile foregroundDW_Shapefile1,
            DW_Shapefile backgroundDW_Shapefile,
            String attributeName,
            File outputDir,
            String png_String,
            int imageWidth,
            DW_StyleParameters styleParameters,
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
                styleParameters);
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        // Output mc as an image
        File outputFile = getOutputFile(
                outputDir,
                outname,
                png_String);
        File outputImageFile = getOutputImageFile(outputFile, png_String);
        DW_GeoTools.writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                png_String);
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
     * @return
     */
    protected static MapContent createMapContent(
            //File file,
            DW_Shapefile outputDW_Shapefile,
            String title,
            String attributeName,
            DW_Shapefile foregroundDW_Shapefile0,
            DW_Shapefile foregroundDW_Shapefile1,
            DW_Shapefile backgroundDW_Shapefile,
            int imageWidth,
            DW_StyleParameters styleParameters) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = styleParameters.getStyle();
        ArrayList<DW_LegendItem> legendItems = null;

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
            styleParameters.setStyle(style);
            legendItems = (ArrayList<DW_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems);
        }
        // Add the features and the associated Style object to mc as a new 
        // Layer
        layer = new FeatureLayer(fs, style);
        result.addLayer(layer);

        // Add foreground0
        // ---------------
        if (foregroundDW_Shapefile0 != null) {
            FeatureLayer foregroundFeatureLayer0;
            foregroundFeatureLayer0 = foregroundDW_Shapefile0.getFeatureLayer(
                    styleParameters.getForegroundStyle0());
            result.addLayer(foregroundFeatureLayer0);
        }

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
            DW_LegendLayer ll = new DW_LegendLayer(
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

    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to get a
     * Style. This methods works out what sort of feature geometry we have in
     * the shapefile and then delegates to an appropriate style creating method.
     */
    private static Object[] getStyleAndLegendItems(
            FeatureSource fs,
            String attributeName,
            DW_StyleParameters styleParameters) {
        SimpleFeatureType schema = (SimpleFeatureType) fs.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            FeatureCollection fc = null;
            try {
                fc = fs.getFeatures();
            } catch (IOException ex) {
                Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
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
        File outDirectory = new File(
                dir,
                name);
        outDirectory.mkdirs();
        String fileAndDirectoryName = name + ".shp";
        File shapefileDirectory = new File(
                outDirectory,
                fileAndDirectoryName);
        if (!shapefileDirectory.exists()) {
            shapefileDirectory.mkdirs();
        }
        File file = new File(
                shapefileDirectory,
                fileAndDirectoryName);
        return file;
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
}
