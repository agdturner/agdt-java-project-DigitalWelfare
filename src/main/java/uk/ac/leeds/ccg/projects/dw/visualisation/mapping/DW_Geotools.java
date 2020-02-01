package uk.ac.leeds.ccg.projects.dw.visualisation.mapping;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.geotools.core.Geotools_Environment;
import uk.ac.leeds.ccg.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.grids.d2.grid.Grids_GridNumber;
import uk.ac.leeds.ccg.geotools.Geotools_LegendItem;
import uk.ac.leeds.ccg.geotools.Geotools_LegendLayer;
import uk.ac.leeds.ccg.geotools.Geotools_StyleParameters;
import uk.ac.leeds.ccg.geotools.core.Geotools_Strings;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;

/**
 * For doing things with Geotools_Environment Objects.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Geotools extends Geotools_Environment {

    public boolean doDebug = true;

    public DW_Environment Env;

    public DW_Geotools(DW_Environment de) throws IOException {
        super(de.Vector_Env, de.ge.files.getDir());
        this.Env = de;
    }

    @Override
    public DW_Style getStyle() {
        if (style == null) {
            style = new DW_Style();
        }
        return (DW_Style) style;
    }

    /**
     * Warning this will set g to null.
     *
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
    @Override
    public void outputToImageUsingGeoToolsAndSetCommonStyle(
            double normalisation, Geotools_StyleParameters styleParameters,
            int index, String outname, Grids_GridNumber g,
            GridCoverage2D gc,
            ArrayList<Geotools_Shapefile> foregroundDW_Shapefile0,
            Geotools_Shapefile foregroundDW_Shapefile1,
            Geotools_Shapefile backgroundDW_Shapefile, Path outputDir,
            int imageWidth, boolean showMapsInJMapPane, boolean scaleToFirst)
            throws IOException, Exception {
        Object[] mcAndNewImageWidthAndHeight = createMapContent(normalisation,
                outname, g, gc, foregroundDW_Shapefile0,
                foregroundDW_Shapefile1, backgroundDW_Shapefile, imageWidth,
                styleParameters, index, scaleToFirst);
        MapContent mc;
        mc = (MapContent) mcAndNewImageWidthAndHeight[0];
        // Set g to null as it is no longer needed. 
        // This is done to prevent any unwanted OutOfMemory Errors being encountered.
        //g = null;
        int newImageWidth = (Integer) mcAndNewImageWidthAndHeight[1];
        int newImageHeight = (Integer) mcAndNewImageWidthAndHeight[2];
        //int imageHeight = getMapContentImageHeight(mc, imageWidth);
        Path outputFile = getOutputFile(outputDir, outname, Geotools_Strings.png_String);
        Path outputImageFile = Env.getMaps().getOutputImageFile(outputFile, Geotools_Strings.png_String);

        writeImageFile(g.env, // For handling OutOfMemoryErrors
                mc, newImageWidth, newImageHeight, outputImageFile, Geotools_Strings.png_String);

//Tried commenting this out as a workaround as somehow loosing the legends, but this was not the solution...
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

    /**
     *
     * @param normalisation
     * @param name
     * @param g
     * @param gc
     * @param foregroundShapefiles
     * @param foregroundDW_Shapefile1
     * @param backgroundDW_Shapefile
     * @param imageWidth
     * @param styleParameters
     * @param index
     * @param scaleToFirst
     * @return
     */
    private Object[] createMapContent(double normalisation, String name,
            Grids_GridNumber g, GridCoverage2D gc,
            ArrayList<Geotools_Shapefile> foregroundShapefiles,
            Geotools_Shapefile foregroundDW_Shapefile1,
            Geotools_Shapefile backgroundDW_Shapefile, int imageWidth,
            Geotools_StyleParameters styleParameters, int index,
            boolean scaleToFirst) throws Exception {
        Object[] r = new Object[3];
        MapContent mc = new MapContent();
        // Unbox styleParameters
        Style style;
        //style = styleParameters.getStyle(name, index);
        style = null; //horrible hack workaound to ensure legend is drawn.
        ArrayList<Geotools_LegendItem> legendItems = null;

        if (styleParameters.isDrawBoundaries()) {
            FeatureLayer backgroundFeatureLayer;
            backgroundFeatureLayer = backgroundDW_Shapefile.getFeatureLayer(
                    styleParameters.getBackgroundStyle());
            mc.addLayer(backgroundFeatureLayer);
        }

        // Add output to mc
        // ----------------
        // If input style is null then create a basic Style to render the 
        // features
        if (style == null) {
            Object[] styleAndLegendItems;
//            styleAndLegendItems = DW_Style.getEqualIntervalStyleAndLegendItems(
            if (styleParameters.getPaletteName2() == null) {
                styleAndLegendItems = getStyle().getStyleAndLegendItems(
                        normalisation, g, gc,
                        styleParameters.getClassificationFunctionName(),
                        styleParameters.getnClasses(),
                        styleParameters.getPaletteName(),
                        styleParameters.isAddWhiteForZero());
            } else {
                styleAndLegendItems = getStyle().getStyleAndLegendItems(
                        normalisation, g, gc,
                        styleParameters.getClassificationFunctionName(),
                        styleParameters.getnClasses(),
                        styleParameters.getPaletteName(),
                        styleParameters.getPaletteName2(),
                        styleParameters.isAddWhiteForZero());
            }
            style = (Style) styleAndLegendItems[0];
            styleParameters.setStyle(name, style, index);
            legendItems = (ArrayList<Geotools_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems, index);
        } else {
            if (scaleToFirst) {
                legendItems = styleParameters.getLegendItems(index);
            }
        }
        GridCoverageLayer gcl = new GridCoverageLayer(gc, style);
        mc.addLayer(gcl);

        // Add foreground0
        // ---------------
        addToMapContent(mc, styleParameters, foregroundShapefiles);

        // Add foreground1
        // ---------------
        if (foregroundDW_Shapefile1 != null) {
            FeatureLayer ffl = foregroundDW_Shapefile1.getFeatureLayer(
                    styleParameters.getForegroundStyle1());
            mc.addLayer(ffl);
        }

        int imageHeight = getMapContentImageHeight(mc, imageWidth);

        // Add a legend
        // ------------
        if (legendItems != null) {
            boolean addLegendToTheSide = true;
            ReferencedEnvelope bounds = mc.getMaxBounds();
            double minX;
            double minY;
            double maxX;
            double maxY;
            minX = bounds.getMinX();
            minY = bounds.getMinY();
            maxX = bounds.getMaxX();
            maxY = bounds.getMaxY();
            System.out.println("minX " + minX);
            System.out.println("minY " + minY);
            System.out.println("maxX " + maxX);
            System.out.println("maxY " + maxY);
            double width = maxX - minX;
            double height = maxY - minY;
            double widthoverimageWidth;
            widthoverimageWidth = width / (double) imageWidth;
            double heightoverimageHeight;
            heightoverimageHeight = height / (double) imageHeight;

            Geotools_LegendLayer ll = new Geotools_LegendLayer(styleParameters,
                    "Legend", legendItems, mc, imageWidth, imageHeight,
                    addLegendToTheSide);
            mc.addLayer(ll);

            //bounds.expandToInclude(ll.getBounds());
            //bounds = mc.getMaxBounds();
            bounds = ll.getBounds();

            minX = bounds.getMinX();
            minY = bounds.getMinY();
            maxX = bounds.getMaxX();
            maxY = bounds.getMaxY();
            System.out.println("minX " + minX);
            System.out.println("minY " + minY);
            System.out.println("maxX " + maxX);
            System.out.println("maxY " + maxY);
            double newwidth = maxX - minX + ll.legendWidth;
            double newheight = maxY - minY;
            if (newwidth > width) {
                int newimageWidth;
                newimageWidth = (int) Math.ceil(newwidth / widthoverimageWidth);
                r[1] = newimageWidth;
            } else {
                r[1] = imageWidth;
            }
            if (newheight > height) {
                int newimageheight;
                newimageheight = (int) Math.ceil(newheight / heightoverimageHeight);
                r[2] = newimageheight;
            } else {
                r[2] = imageHeight;
            }
        } else {
            r[1] = imageWidth;
            r[2] = imageHeight;
        }
        r[0] = mc;
        return r;
    }

    /**
     *
     * @param mc The MapContent to be added to.
     * @param sp The style parameters for the style of the layer to be added.
     * @param shapefiles The shapefiles for creating the layers.
     */
    private void addToMapContent(MapContent mc, Geotools_StyleParameters sp,
            ArrayList<Geotools_Shapefile> shapefiles) {
        if (shapefiles != null) {
            Iterator<Geotools_Shapefile> ite;
            ite = shapefiles.iterator();
            int i = 0;
            while (ite.hasNext()) {
                Geotools_Shapefile sf = ite.next();
                FeatureLayer fl;
                fl = sf.getFeatureLayer(sp.getForegroundStyle0().get(i));
                mc.addLayer(fl);
                i++;
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
     * @param max
     * @param showMapsInJMapPane
     */
    public void outputToImage(String outname, Path outputShapefile,
            ArrayList<Geotools_Shapefile> foregroundDW_Shapefile0,
            Geotools_Shapefile foregroundDW_Shapefile1,
            Geotools_Shapefile backgroundDW_Shapefile, String attributeName,
            Path outputDir, String png_String, int imageWidth,
            Geotools_StyleParameters styleParameters, int styleIndex,
            double max, boolean showMapsInJMapPane) throws IOException {
        //Style resultStyle;
        DW_Shapefile outputDW_Shapefile;
        outputDW_Shapefile = new DW_Shapefile(outputShapefile);
        String title = outname;
        MapContent mc;
        mc = createMapContent(outputDW_Shapefile, title, attributeName,
                foregroundDW_Shapefile0, foregroundDW_Shapefile1,
                backgroundDW_Shapefile, imageWidth, styleParameters, styleIndex,
                max);
        outputToImage(mc, outname, outputShapefile, outputDir, imageWidth,
                showMapsInJMapPane);
    }

    public void outputToImage(String outname,
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> foregrounds, String attributeName,
            Path outputDir, String png_String, int imageWidth,
            Geotools_StyleParameters styleParameters, int styleIndex,
            double max, boolean showMapsInJMapPane) {
//        String title = outname;
//        //Style resultStyle;
//        MapContent mc = createMapContent(
//                midgrounds,title, attributeName, foregrounds, null, null, imageWidth, styleParameters, styleIndex, max)
//                foregrounds,
//                title,
//                attributeName,
//                imageWidth,
//                styleParameters,
//                styleIndex,
//                max);
//        outputToImage(
//                mc,
//                outname,
//                midgrounds,
//                outputDir,
//                imageWidth,
//                showMapsInJMapPane);
    }

    public void outputToImage(
            String legendMessage,
            String outname,
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> foregrounds,
            Path outputDir,
            String png_String,
            int imageWidth,
            DW_StyleParameters styleParameters,
            int styleIndex,
            double max,
            boolean showMapsInJMapPane) throws IOException {
        String title = outname;
        //Style resultStyle;
        MapContent mc = createMapContent(
                legendMessage,
                midgrounds,
                foregrounds,
                title,
                imageWidth,
                styleParameters,
                styleIndex,
                max);
        outputToImage(
                mc,
                outname,
                midgrounds,
                outputDir,
                imageWidth,
                showMapsInJMapPane);
    }

    /**
     * Outputs outputShapefile MapContent as an image. Modifies
     * styleParameters[0] if this is currently null using the remaining
     * styleParameters.
     *
     * @param outname
     * @param foregrounds
     * @param midgrounds
     * @param backgrounds
     * @param attributeName
     * @param outputDir
     * @param png_String
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @param showMapsInJMapPane
     */
    public void outputToImage(
            String outname,
            ArrayList<Geotools_Shapefile> foregrounds,
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> backgrounds,
            String attributeName,
            Path outputDir,
            String png_String,
            int imageWidth,
            DW_StyleParameters styleParameters,
            //int styleIndex,
            //double max,
            boolean showMapsInJMapPane) throws IOException {
        String title = outname;
        MapContent mc = createMapContent(
                midgrounds,
                foregrounds,
                backgrounds,
                imageWidth,
                styleParameters);
        outputToImage(
                mc,
                outname,
                foregrounds,
                midgrounds,
                backgrounds,
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
     * @param foregrounds
     * @param midgrounds
     * @param outputDir
     * @param backgrounds
     * @param imageWidth
     * @param showMapsInJMapPane
     */
    public void outputToImage(
            MapContent mc,
            String outname,
            ArrayList<Geotools_Shapefile> foregrounds,
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> backgrounds,
            Path outputDir,
            int imageWidth,
            boolean showMapsInJMapPane) throws IOException {
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        Path outputFile = getOutputFile(
                outputDir,
                outname,
                Geotools_Strings.png_String);
        Path outputImageFile = Env.getMaps().getOutputImageFile(outputFile, Geotools_Strings.png_String);
        writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                Geotools_Strings.png_String);
        System.out.println("Written " + outputImageFile.toAbsolutePath());
        // Dispose of MapContent to prevent memory leaks
        if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Tidy up
            //dispose(backgrounds);
            dispose(midgrounds);
            //dispose(foregrounds);
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

    private void dispose(ArrayList<Geotools_Shapefile> fs) {
        Iterator<Geotools_Shapefile> ite;
        ite = fs.iterator();
        while (ite.hasNext()) {
            Geotools_Shapefile f;
            f = ite.next();
            f.dispose();
        }
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
    public void outputToImage(
            MapContent mc,
            String outname,
            Path outputShapefile,
            Path outputDir,
            int imageWidth,
            boolean showMapsInJMapPane) throws IOException {
        DW_Shapefile outputDW_Shapefile;
        outputDW_Shapefile = new DW_Shapefile(outputShapefile);
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        Path outputFile = getOutputFile(
                outputDir,
                outname,
                Geotools_Strings.png_String);
        Path outputImageFile = Env.getMaps().getOutputImageFile(outputFile, Geotools_Strings.png_String);
        writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                Geotools_Strings.png_String);
        System.out.println("Written " + outputImageFile.toAbsolutePath());
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
     * Outputs outputShapefile MapContent as an image. Modifies
     * styleParameters[0] if this is currently null using the remaining
     * styleParameters.
     *
     * @param mc
     * @param outname
     * @param midgrounds
     * @param outputDir
     * @param imageWidth
     * @param showMapsInJMapPane
     */
    public void outputToImage(MapContent mc, String outname,
            ArrayList<Geotools_Shapefile> midgrounds, Path outputDir,
            int imageWidth, boolean showMapsInJMapPane) throws IOException {
        //DW_Shapefile outputDW_Shapefile;
        //outputDW_Shapefile = new DW_Shapefile(outputShapefile);
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        Path outputFile = getOutputFile(outputDir, outname,
                Geotools_Strings.png_String);
        Path outputImageFile = Env.getMaps().getOutputImageFile(outputFile,
                Geotools_Strings.png_String);
        writeImageFile(mc, imageWidth, imageHeight, outputImageFile,
                Geotools_Strings.png_String);
        System.out.println("Written " + outputImageFile.toAbsolutePath());
        // Dispose of MapContent to prevent memory leaks
        if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Tidy up
            Iterator<Geotools_Shapefile> ite1 = midgrounds.iterator();
            while (ite1.hasNext()) {
                Geotools_Shapefile s = ite1.next();
                s.dispose();
            }
//            outputDW_Shapefile.dispose();
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
     * @param max
     * @return
     */
    protected MapContent createMapContent(
            //File file,
            //File file,
            DW_Shapefile outputDW_Shapefile,
            String title,
            String attributeName,
            ArrayList<Geotools_Shapefile> foregroundDW_Shapefile0,
            Geotools_Shapefile foregroundDW_Shapefile1,
            Geotools_Shapefile backgroundDW_Shapefile,
            int imageWidth,
            Geotools_StyleParameters styleParameters,
            int styleIndex,
            double max) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = styleParameters.getStyle(attributeName, styleIndex);
        ArrayList<Geotools_LegendItem> legendItems;
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
            styleParameters.setStyle(attributeName, style, styleIndex);
            legendItems = (ArrayList<Geotools_LegendItem>) styleAndLegendItems[1];
            styleParameters.setLegendItems(legendItems, styleIndex);
        }

        // set max for the last legend item
        styleParameters.setMaxForTheLastLegendItem(max, styleIndex);

        // Add the features and the associated Style object to mc as a new 
        // Layer
        layer = new FeatureLayer(fs, style);
        result.addLayer(layer);

        // Add foreground0
        // ---------------
        if (styleParameters.isDoForeground()) {
            addToMapContent(result, styleParameters, foregroundDW_Shapefile0);
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
            Geotools_LegendLayer ll = new Geotools_LegendLayer(
                    styleParameters,
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

//    protected MapContent createMapContent(
//            //File file,
//            //File file,
//            ArrayList<AGDT_Shapefile> midgrounds,
//            String title,
//            String attributeName,
//            ArrayList<AGDT_Shapefile> foregroundDW_Shapefile0,
//            Geotools_Shapefile ForegroundDW_Shapefile1,
//            Geotools_Shapefile BackgroundDW_Shapefile,
//            int imageWidth,
//            Geotools_StyleParameters styleParameters,
//            int styleIndex,
//            double max) {
//        MapContent result;
//        result = new MapContent();
//        // Unbox styleParameters
//        Style style;
//        style = styleParameters.getStyle(attributeName, styleIndex);
//        ArrayList<AGDT_LegendItem> legendItems;
//        legendItems = styleParameters.getLegendItems(styleIndex);
//
//        if (styleParameters.isDrawBoundaries()) {
//            FeatureLayer backgroundFeatureLayer;
//            backgroundFeatureLayer = BackgroundDW_Shapefile.getFeatureLayer(
//                    styleParameters.getBackgroundStyle());
//            result.addLayer(backgroundFeatureLayer);
//        }
//
//        // Add output to mc
//        // ----------------
//        Iterator<AGDT_Shapefile> ite;
//        ite = midgrounds.iterator();
//        while (ite.hasNext()) {
//            FeatureSource fs;
//            fs = ite.next().getFeatureSource();
//            Layer layer;
//            // If input style is null then create a basic Style to render the 
//            // features
//            Object[] styleAndLegendItems;
//            if (style == null) {
//                styleAndLegendItems = getStyleAndLegendItems(
//                        fs,
//                        attributeName,
//                        styleParameters);
//                style = (Style) styleAndLegendItems[0];
//                styleParameters.setStyle(attributeName, style, styleIndex);
//                legendItems = (ArrayList<AGDT_LegendItem>) styleAndLegendItems[1];
//                styleParameters.setLegendItems(legendItems, styleIndex);
//            }
//
//            // set max for the last legend item
//            styleParameters.setMaxForTheLastLegendItem(max, styleIndex);
//
//            // Add the features and the associated Style object to mc as a new 
//            // Layer
//            layer = new FeatureLayer(fs, style);
//            result.addLayer(layer);
//        }
//
//        // Add foreground0
//        // ---------------
//        if (styleParameters.isDoForeground()) {
//            addToMapContent(result, styleParameters, foregroundDW_Shapefile0);
//        }
//
//        // Add foreground1
//        // ---------------
//        if (ForegroundDW_Shapefile1 != null) {
//            FeatureLayer foregroundFeatureLayer1;
//            foregroundFeatureLayer1 = ForegroundDW_Shapefile1.getFeatureLayer(
//                    styleParameters.getForegroundStyle1());
//            result.addLayer(foregroundFeatureLayer1);
//        }
//
//        int imageHeight = getMapContentImageHeight(result, imageWidth);
//
//        // Add a legend
//        // ------------
//        if (legendItems != null) {
//            boolean addLegendToTheSide = true;
//            Geotools_LegendLayer ll = new Geotools_LegendLayer(
//                    styleParameters,
//                    "Legend",
//                    legendItems,
//                    result,
//                    imageWidth,
//                    imageHeight,
//                    addLegendToTheSide);
//            result.addLayer(ll);
//        }
//        return result;
//    }
    protected MapContent createMapContent(
            String legendMessage,
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> foregrounds,
            String title,
            int imageWidth,
            DW_StyleParameters styleParameters,
            int styleIndex,
            double max) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
//        style = styleParameters.getStyle(attributeName, styleIndex);
        style = styleParameters.getMidgroundStyle(styleIndex);
        ArrayList<Geotools_LegendItem> legendItems;

        legendItems = styleParameters.getLegendItems(styleIndex);
        if (legendItems == null) {
            legendItems = new ArrayList<>();
        }
        Geotools_LegendItem li;
        li = new Geotools_LegendItem(legendMessage, Color.WHITE);

        legendItems.add(li);
        li = new Geotools_LegendItem("Origin", Color.RED);
        legendItems.add(li);
        li = new Geotools_LegendItem("Destination", Color.BLUE);
        legendItems.add(li);

//        if (styleParameters.isDrawBoundaries()) {
//            FeatureLayer backgroundFeatureLayer;
//            backgroundFeatureLayer = BackgroundDW_Shapefile.getFeatureLayer(
//                    styleParameters.getBackgroundStyle());
//            result.addLayer(backgroundFeatureLayer);
//        }
        // Add output to mc
        // ----------------
        int i;
        i = 0;
        Iterator<Geotools_Shapefile> ite;
        ite = midgrounds.iterator();
        while (ite.hasNext()) {
            FeatureSource fs;
            fs = ite.next().getFeatureSource();
            Layer layer;
            // If input style is null then create a basic Style to render the 
            // features
            style = styleParameters.getMidgroundStyle(i);
//            Object[] styleAndLegendItems;
//            if (style == null) {
//                styleAndLegendItems = getStyleAndLegendItems(
//                        fs,
//                        attributeName,
//                        styleParameters);
//                style = (Style) styleAndLegendItems[0];
//                //styleParameters.setStyle(attributeName, style, styleIndex);
//                styleParameters.setMidgroundStyle(style, i);
//                legendItems = (ArrayList<AGDT_LegendItem>) styleAndLegendItems[1];
//                styleParameters.setLegendItems(legendItems, styleIndex);
//            }

//        // set max for the last legend item
//        styleParameters.setMaxForTheLastLegendItem(max, styleIndex);
            // Add the features and the associated Style object to mc as a new 
            // Layer
            layer = new FeatureLayer(fs, style);
            result.addLayer(layer);
            i++;
        }

        i = 0;
        ite = foregrounds.iterator();
        while (ite.hasNext()) {
            FeatureSource fs;
            fs = ite.next().getFeatureSource();
            Layer layer;
            // If input style is null then create a basic Style to render the 
            // features
            style = styleParameters.getForegroundStyle(i);
//            
//            Object[] styleAndLegendItems;
//            if (style == null) {
//                styleAndLegendItems = getStyleAndLegendItems(
//                        fs,
//                        attributeName,
//                        styleParameters);
//                style = (Style) styleAndLegendItems[0];
//                //styleParameters.setStyle(attributeName, style, styleIndex);
//                styleParameters.setMidgroundStyle(style, i);
//                legendItems = (ArrayList<AGDT_LegendItem>) styleAndLegendItems[1];
//                styleParameters.setLegendItems(legendItems, styleIndex);
//            }

//        // set max for the last legend item
//        styleParameters.setMaxForTheLastLegendItem(max, styleIndex);
            // Add the features and the associated Style object to mc as a new 
            // Layer
            layer = new FeatureLayer(fs, style);
            result.addLayer(layer);
            i++;
        }

        // Add foreground0
        // ---------------
        //if (styleParameters.isDoForeground()) {
//            addForeground0(result, styleParameters, foregroundDW_Shapefile0);
        //}
//        // Add foreground1
//        // ---------------
//        if (ForegroundDW_Shapefile1 != null) {
//            FeatureLayer foregroundFeatureLayer1;
//            foregroundFeatureLayer1 = ForegroundDW_Shapefile1.getFeatureLayer(
//                    styleParameters.getForegroundStyle1());
//            result.addLayer(foregroundFeatureLayer1);
//        }
        int imageHeight = getMapContentImageHeight(result, imageWidth);

//        // Add a legend
//        // ------------
        if (legendItems != null) {
            boolean addLegendToTheSide = true;
            Geotools_LegendLayer ll = new Geotools_LegendLayer(
                    styleParameters,
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
     * This both creates MapContent and returns it and also modifies
     * styleParameters if syleParameters[0] == null changing it to the Style as
     * produced from the rest of the styleParameters.
     *
     * @param midgrounds
     * @param foregrounds
     * @param backgrounds
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @return
     */
    protected MapContent createMapContent(
            ArrayList<Geotools_Shapefile> midgrounds,
            ArrayList<Geotools_Shapefile> foregrounds,
            ArrayList<Geotools_Shapefile> backgrounds,
            int imageWidth,
            DW_StyleParameters styleParameters) {
        MapContent r = new MapContent();
        Iterator<Geotools_Shapefile> ite;
        int i;
        // Add backgrounds to mc
        // ---------------------
        i = 0;
        ite = backgrounds.iterator();
        while (ite.hasNext()) {
            Geotools_Shapefile sf = ite.next();
            Style style = styleParameters.getBackgroundStyle(i);
            r.addLayer(new FeatureLayer(sf.getFeatureSource(), style));
            i++;
        }
        // Add midgrounds to mc
        // --------------------
        i = 0;
        ite = midgrounds.iterator();
        while (ite.hasNext()) {
            Geotools_Shapefile sf = ite.next();
            Style style = styleParameters.getMidgroundStyle(i);
            r.addLayer(new FeatureLayer(sf.getFeatureSource(), style));
        }
        // Add foregrounds to mc
        // ---------------------
        i = 0;
        ite = foregrounds.iterator();
        while (ite.hasNext()) {
            Geotools_Shapefile sf = ite.next();
            Style style = styleParameters.getForegroundStyle(i);
            r.addLayer(new FeatureLayer(sf.getFeatureSource(), style));
        }
//        int imageHeight = getMapContentImageHeight(result, imageWidth);
//        // Add a legend
//        // ------------
//        if (legendItems != null) {
//            boolean addLegendToTheSide = true;
//            Geotools_LegendLayer ll = new Geotools_LegendLayer(
//                    styleParameters,
//                    "Map title.................................",
//                    "Legend",
//                    legendItems,
//                    result,
//                    imageWidth,
//                    imageHeight,
//                    addLegendToTheSide);
//            result.addLayer(ll);
//        }
        return r;
    }

    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to get a
     * Style. This methods works out what sort of feature geometry we have in
     * the shapefile and then delegates to an appropriate style creating method.
     */
    private Object[] getStyleAndLegendItems(FeatureSource fs,
            String attributeName, Geotools_StyleParameters styleParameters) {
        SimpleFeatureType schema = (SimpleFeatureType) fs.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();
        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            FeatureCollection fc;
            try {
                fc = fs.getFeatures();
            } catch (IOException ex) {
                Logger.getLogger(Geotools_Environment.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            return getStyle().createPolygonStyle(fc, attributeName,
                    styleParameters, doDebug);
        }
        Object[] r = new Object[2];
        if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            r[0] = getStyle().createDefaultLineStyle();
        } else {
            r[0] = getStyle().createDefaultPointStyle();
        }
        return r;
    }

    /**
     * Creates a directory called name in dir and returns a Path within this
     * with name.
     *
     * @param dir
     * @param name
     * @return
     */
    public Path getShapefile(Path dir, String name) throws IOException {
        Path r;
        Path dir2 = Paths.get(dir.toString(), name);
        // Could add extra logic here to deal with issues if directory or a file 
        // of this name already exists...
        if (!Files.exists(dir2)) {
            Files.createDirectories(dir2);
        }
        r = Paths.get(dir2.toString(), name);
        return r;
    }
}
