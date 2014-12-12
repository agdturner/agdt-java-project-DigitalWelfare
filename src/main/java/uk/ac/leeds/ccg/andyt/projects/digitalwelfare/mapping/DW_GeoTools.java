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
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Execution;
import uk.ac.leeds.ccg.andyt.generic.visualisation.Generic_Visualisation;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Maps.getOutputImageFile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_ChoroplethMapLegendLayer.LegendItem;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_DensityMaps;

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
            String outname,
            GridCoverage2D gc,
            FeatureLayer foregroundFeatureLayer,
            FeatureLayer backgroundFeatureLayer,
            Object[] legendItemsAndStyle,
            File mapDirectory,
            String png_String,
            int imageWidth,
            boolean showMapsInJMapPane) {
   
        
        MapContent mc;
        mc = new MapContent();
        
//        mc.addLayer(backgroundFeatureLayer);
        
        Style style = (Style) legendItemsAndStyle[0];
        
        GridCoverageLayer gcl = new GridCoverageLayer(gc, style);
        mc.addLayer(gcl);
  
                mc.addLayer(foregroundFeatureLayer);
                mc.addLayer(backgroundFeatureLayer);
        
                
                // Add a legendLayer
                ArrayList<LegendItem> legendItems;
                legendItems = (ArrayList<LegendItem>) legendItemsAndStyle[1];
            if (legendItems != null) {
                boolean addLegendToTheSide = true;
                DW_ChoroplethMapLegendLayer ll = new DW_ChoroplethMapLegendLayer(
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
                mapDirectory,
                outname,
                png_String);
        File outputImageFile = getOutputImageFile(outputFile, png_String);
        DW_GeoTools.writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                png_String);
        
        
    if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Dispose of mc to avoid memory leaks
            //mc.removeLayer(backgroundFeatureLayer);
            List<Layer> layers = mc.layers();
            Iterator<Layer> ite = layers.iterator();
            while (ite.hasNext()) {
                Layer l = ite.next();
//                FeatureLayer fl = (FeatureLayer) l;
//                fl.
//                if (l.equals(backgroundFeatureLayer.)) {
//                    System.out.println("Odd this was removed from MapContent!");
//                } else {
                l.preDispose();
                l.dispose();
//                }
            }
            mc.dispose();
        }
    
    }
    /**
     * Outputs outputShapefile MapContent as an image. Modifies
     * styleParameters[0] if this is currently null using the remaining
     * styleParameters.
     *
     * @param outname
     * @param outputShapeFile
     * @param backgroundFeatureLayer
     * @param attributeName
     * @param mapDirectory
     * @param png_String
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @param showMapsInJMapPane
     */
    public static void outputToImage(
            String outname,
            File outputShapeFile,
            FeatureLayer backgroundFeatureLayer,
            //File backgroundShapefile,
            String attributeName,
            File mapDirectory,
            String png_String,
            int imageWidth,
            Object[] styleParameters,
            boolean showMapsInJMapPane) {
        //Style resultStyle;
        String title = outname;
        MapContent mc = createMapContentFromShapefile(
                outputShapeFile,
                title,
                attributeName,
                backgroundFeatureLayer,
                //backgroundShapefile,
                imageWidth,
                styleParameters);
        int imageHeight = getMapContentImageHeight(mc, imageWidth);
        // Output mc as an image
        File outputFile = getOutputFile(
                mapDirectory,
                outname,
                png_String);
        File outputImageFile = getOutputImageFile(outputFile, png_String);
        DW_GeoTools.writeImageFile(
                mc,
                imageWidth,
                imageHeight,
                outputImageFile,
                png_String);

        // Free up resources
        FileDataStore fds;
        fds = getFileDataStore(outputShapeFile);
        try {
            fds.getFeatureReader().close();
        } catch (IOException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        fds.dispose();

        System.out.println("Written " + outputImageFile);

//        // Add legend
//        DW_Legend l = new DW_Legend();
//        mc.addFeatureLayer(l);
        // Dispose of MapContent to prevent memory leaks
        if (showMapsInJMapPane) {
            // Display mc in a JMapFrame
            JMapFrame.showMap(mc); // Need to not dispose of mc if this is to persist!
        } else {
            // Dispose of mc to avoid memory leaks
            //mc.removeLayer(backgroundFeatureLayer);
            List<Layer> layers = mc.layers();
            Iterator<Layer> ite = layers.iterator();
            while (ite.hasNext()) {
                Layer l = ite.next();
//                FeatureLayer fl = (FeatureLayer) l;
//                fl.
//                if (l.equals(backgroundFeatureLayer.)) {
//                    System.out.println("Odd this was removed from MapContent!");
//                } else {
                l.preDispose();
                l.dispose();
//                }
            }
            mc.dispose();
        }
    }

    /**
     * This both creates MapContent and returns it and also modifies
     * styleParameters if syleParameters[0] == null changing it to the Style as
     * produced from the rest of the styleParameters.
     *
     * @param file
     * @param title
     * @param attributeName
     * @param backgroundFeatureLayer
     * @param imageWidth
     * @param styleParameters styleParameters[0] may be null, but if it is not
     * null then (Style) styleParameters[0] is used to render features.
     * @return
     */
    protected static MapContent createMapContentFromShapefile(
            File file,
            String title,
            String attributeName,
            //File backgroundShapefile,
            FeatureLayer backgroundFeatureLayer,
            int imageWidth,
            Object[] styleParameters) {
        MapContent result;
        result = new MapContent();
        // Unbox styleParameters
        Style style;
        style = (Style) styleParameters[0];
        String classificationFunctionName;
        classificationFunctionName = (String) styleParameters[1];
        int nClasses;
        nClasses = (Integer) styleParameters[2];
        String paletteName;
        paletteName = (String) styleParameters[3];
        ArrayList<LegendItem> legendItems;
        legendItems = (ArrayList<LegendItem>) styleParameters[4];
        boolean addWhiteForZero;
        addWhiteForZero = (Boolean) styleParameters[5];
//        Style backgroundStyle;
//        backgroundStyle = (Style) styleParameters[6];
//        String backgroundLayerTitle;
//        backgroundLayerTitle = (String) styleParameters[7];
        try {
//            if (backgroundLayer != null) {
//                result.addFeatureLayer(backgroundLayer);
//            }

            result.addLayer(backgroundFeatureLayer);

//            if (backgroundShapefile != null) {
//                DW_GeoTools.addFeatureLayer(
//                        result,
//                        backgroundShapefile,
//                        backgroundStyle,
//                        backgroundLayerTitle);
//            }
            // Add foreground to mc
            FileDataStore fds;
            FeatureSource fs;
            Layer layer;
            fds = getFileDataStore(file);
            if (fds != null) {
                fs = fds.getFeatureSource();

                // If input style is null then create a basic Style to render the 
                // features
                Object[] styleAndLegendItems;
                if (style == null) {
                    styleAndLegendItems = getStyleAndLegendItems(
                            fs,
                            attributeName,
                            classificationFunctionName,
                            nClasses,
                            paletteName,
                            addWhiteForZero);
                    style = (Style) styleAndLegendItems[0];
                    styleParameters[0] = style;
                    legendItems = (ArrayList<LegendItem>) styleAndLegendItems[1];
                    styleParameters[4] = legendItems;
                }
                /* Add the features and the associated Style object to mc as a new 
                 * Layer
                 */
                layer = new FeatureLayer(fs, style);
                result.addLayer(layer);
            }

//            // Add background in foreground
//            result.addLayer(backgroundFeatureLayer);
            int imageHeight = getMapContentImageHeight(result, imageWidth);

            // Add a legendLayer
            if (legendItems != null) {
                boolean addLegendToTheSide = true;
                DW_ChoroplethMapLegendLayer ll = new DW_ChoroplethMapLegendLayer(
                        "Map title.................................",
                        "Legend",
                        legendItems,
                        result,
                        imageWidth,
                        imageHeight,
                        addLegendToTheSide);
                result.addLayer(ll);
            }

//            // Add titleLayer
//            if (legendItems != null) {
//                DW_ChoroplethMapLegendLayer ll = new DW_ChoroplethMapLegendLayer(
//                        legendItems,
//                        imageWidth,
//                        imageHeight);
//                result.addLayer(ll);
//            }
        } catch (IOException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
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
            String classificationFunctionName,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero) {
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
                    nClasses,
                    classificationFunctionName,
                    paletteName,
                    addWhiteForZero);
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

    public static FeatureLayer getFeatureLayer(
            File shapefile) {
        FeatureSource fs = null;
        try {
            FileDataStore fds;
            fds = getFileDataStore(shapefile);
            fs = fds.getFeatureSource();
        } catch (IOException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        Style style = DW_Style.createStyle(fs);
        return getFeatureLayer(shapefile, style);
    }

    public static FeatureLayer getFeatureLayer(
            File shapefile,
            Style style) {
        FeatureLayer result;
        FeatureSource fs = null;
        try {
            FileDataStore fds;
            fds = getFileDataStore(shapefile);
            fs = fds.getFeatureSource();
        } catch (IOException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        result = new FeatureLayer(
                fs,
                style);
//        // Debug
//        ReferencedEnvelope re = result.getBounds();
        return result;
    }

    public static FeatureLayer getFeatureLayer(
            File shapefile,
            Style style,
            String title) {
        FeatureLayer result = DW_GeoTools.getFeatureLayer(shapefile, style);
        result.setTitle(title);
        return result;
    }

    public static void addFeatureLayer(
            MapContent mc,
            File shapefile,
            Style style,
            String title) {
        FeatureLayer fl;
        fl = DW_GeoTools.getFeatureLayer(shapefile, style, title);
        mc.addLayer(fl);
    }

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
     * @param f is a file to be used to construct a FileDataStore from which the
     * resulting Feature Collection and SimpleFeatureType of the result are
     * constructed.
     * @return Object[] result, where: -----------------------------------------
     * result[0] is a FeatureCollection; ---------------------------------------
     * result[1] is a SimpleFeatureType; ---------------------------------------
     */
    public static Object[] getFeatureCollectionAndType(File f) {
        Object[] result = new Object[2];
        FileDataStore fds = getFileDataStore(f);
        FeatureCollection fc = getFeatureCollection(fds);
        SimpleFeatureType sft = getFeatureType(fds);
        result[0] = fc;
        result[1] = sft;
        return result;
    }

    /**
     * @param f The File for which a FileDataStore is returned.
     * @return A FileDataStore found via a FileDataStoreFinder.
     */
    protected static FileDataStore getFileDataStore(File f) {
        FileDataStore result = null;
        try {
            result = FileDataStoreFinder.getDataStore(f);
        } catch (IOException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
            //} catch (NullPointerException e) {
        } catch (Exception e) {
            System.err.println("Exception (not an IOException) in DW_GeoTools.getFileDataStore(File)");
            long timeInMilliseconds = 1000;
            Generic_Execution.waitSychronized(f, timeInMilliseconds);
            return getFileDataStore(f);
        } catch (Error e) {
            System.err.println("Error in DW_GeoTools.getFileDataStore(File)");
            long timeInMilliseconds = 1000;
            Generic_Execution.waitSychronized(f, timeInMilliseconds);
            return getFileDataStore(f);
        }
        return result;
    }

    /**
     * @param fds The FileDataStore assumed to contain Features of a single
     * SimpleFeatureType.
     * @return A SimpleFeatureType derived from the Schema of
     * <code>fds.getFeatureSource()</code>.
     */
    protected static SimpleFeatureType getFeatureType(FileDataStore fds) {
        SimpleFeatureType result = null;
        try {
            FeatureSource fs;
            fs = fds.getFeatureSource();
            result = (SimpleFeatureType) fs.getSchema();
        } catch (IOException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * @param fds The FileDataStore from which the FeatureSource Features are
     * returned as a FeatureCollection.
     * @return A FeatureCollection from fds.
     */
    protected static FeatureCollection getFeatureCollection(
            FileDataStore fds) {
        FeatureCollection result = null;
        try {
            result = fds.getFeatureSource().getFeatures();
        } catch (IOException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * Pushes the FeatureCollection holding SimpleFeatureType sft to the
     * shapefile f.
     *
     * @param f
     * @param sft
     * @param fc
     * @param sdsf
     */
    public static void transact(
            File f,
            SimpleFeatureType sft,
            FeatureCollection fc,
            ShapefileDataStoreFactory sdsf) {
        ShapefileDataStore sds;
        sds = initialiseOutputDataStore(f, sft, sdsf);
        SimpleFeatureSource simpleFeatureSource;
        try {
            String typeName = sds.getTypeNames()[0];
            simpleFeatureSource = sds.getFeatureSource(typeName);
        } catch (IOException ex) {
            Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
            simpleFeatureSource = null;
        }
        Transaction transaction = new DefaultTransaction("create");
        if (simpleFeatureSource != null) {
            SimpleFeatureStore sfs;
            sfs = (SimpleFeatureStore) simpleFeatureSource;
            sfs.setTransaction(transaction);
            try {
                sfs.addFeatures(fc);
            } catch (IOException ex) {
                Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        commitTransaction(transaction);
        sds.dispose();
    }

    public static void commitTransaction(Transaction transaction) {
        try {
            transaction.commit();
        } catch (IOException ex) {
            // This may be caused by too many open files so let's not log this 
            // which opens another file and try to deal with it by waiting a bit.
            //Logger.getLogger(DW_Maps.class.getName()).log(Level.SEVERE, null, ex);
            try {
                transaction.rollback();
                try {
                    synchronized (transaction) {
                        transaction.wait(2000L);
                    }
                } catch (InterruptedException ie) {
                    Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ie);
                }
                commitTransaction(transaction); // This may recurse infinitely!
            } catch (IOException ex1) {
                System.out.println("Oh help gromit!");
                Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            if (transaction != null) {
                try {
                    transaction.close();
                } catch (IOException ex) {
                    Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    System.err.println("Exception (not IOException or NullPointerException) in DW_GeoTools.commitTransaction(Transaction))");
                } catch (Error e) {
                    System.err.println("Error in DW_GeoTools.commitTransaction(Transaction))");
                }
            }
        }
    }

    /**
     * Initialises and returns a ShapefileDataStore for output.
     *
     * @param f
     * @param sft
     * @param sdsf
     * @return
     */
    public static ShapefileDataStore initialiseOutputDataStore(
            File f,
            SimpleFeatureType sft,
            ShapefileDataStoreFactory sdsf) {
        ShapefileDataStore result = null;
        //ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        try {
            params.put("url", f.toURI().toURL());
        } catch (MalformedURLException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        params.put("create spatial index", Boolean.TRUE);
        try {
            result = (ShapefileDataStore) sdsf.createNewDataStore(params);
            result.forceSchemaCRS(DefaultGeographicCRS.WGS84);
            result.createSchema(sft);
        } catch (IOException ex) {
            Logger.getLogger(DW_GeoTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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
        StreamingRenderer renderer = new StreamingRenderer();
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
