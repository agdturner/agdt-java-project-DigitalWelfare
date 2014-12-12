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
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.brewer.color.StyleGenerator;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.dialog.JExceptionReporter;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_double;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_ChoroplethMapLegendLayer.LegendItem;

/**
 *
 * @author geoagdt
 */
public class DW_Style {

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    /**
     * Initialise styleParameters
     *
     * @return
     */
    public static Object[] initStyleParameters() {
        Object[] result = new Object[4];
        Style style = null;
        String classificationFunctionName = "";
        int nClasses = 0;
        String paletteName = "";
        boolean addWhiteForZero = false;
        Style backgroundStyle = null;
        String backgroundStyleTitle = "";
        return initStyleParameters(
                classificationFunctionName,
                nClasses,
                paletteName,
                addWhiteForZero, backgroundStyle,
                backgroundStyleTitle);
    }

    /**
     * Initialise styleParameters Deep copy everything but styleParameters[0]
     *
     * @return
     */
    public static Object[] initStyleParameters(Object[] styleParameters) {
        Object[] result = new Object[4];
        //result[0] = styleFactory.createStyle((Style) styleParameters[0]);
        result[0] = (Style) styleParameters[0];
        result[1] = ((String) styleParameters[1]).toString();
        result[2] = ((Integer) styleParameters[2]).intValue();
        result[3] = ((String) styleParameters[3]).toString();
        return result;
    }

    /**
     * Initialise styleParameters
     *
     * @param classificationFunctionName
     * @param nClasses
     * @param paletteName
     * @return
     */
    public static Object[] initStyleParameters(
            String classificationFunctionName,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero,
            Style backgroundStyle,
            String backgroundStyleTitle) {
        Object[] result = new Object[8];
        Style style = null;
        result[0] = style;
        result[1] = classificationFunctionName;
        result[2] = nClasses;
        result[3] = paletteName;
        result[4] = new ArrayList<LegendItem>();
        result[5] = addWhiteForZero;
        result[6] = backgroundStyle;
        result[7] = backgroundStyleTitle;
        return result;
    }

    /**
     *
     * @param styleParameters
     * @param style
     */
    public static void setStyleParametersStyle(
            Object[] styleParameters,
            Style style) {
        styleParameters[0] = style;
    }

    /**
     * Figure out if a valid SLD file is available.
     *
     * @param file
     * @return
     */
    public static File toSLDFile(File file) {
        String path = file.getAbsolutePath();
        String base = path.substring(0, path.length() - 4);
        String newPath = base + ".sld";
        File sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        return null;
    }

    /**
     * Create a DW_Style object from a definition in a SLD document
     */
    private static org.geotools.styling.Style createFromSLD(File sld) {
        try {
            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
            org.geotools.styling.Style[] style = stylereader.readXML();
            return style[0];

        } catch (Exception e) {
            JExceptionReporter.showDialog(e, "Problem creating style");
        }
        return null;
    }

    /**
     * Here is a programmatic alternative to using JSimpleStyleDialog to get a
     * Style. This methods works out what sort of feature geometry we have in
     * the shapefile and then delegates to an appropriate style creating method.
     *
     * @param featureSource
     * @param attributeName
     * @return
     */
    public static Style createStyle(
            FeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            return createDefaultPolygonStyle(
                    Color.BLUE,
                    Color.CYAN);
        } else if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            return createDefaultLineStyle();

        } else {
            return createDefaultPointStyle();
        }
    }

    /**
     * Create and returns a Style to draw point features as circles with blue
     * outlines and cyan fill.
     *
     * @return A Style to draw point features as circles with blue outlines and
     * cyan fill.
     */
    public static Style createDefaultPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(
                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));
        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));
        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features.
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    /**
     * Creates and returns a Style to draw line features as thin blue lines.
     *
     * @return A Style to draw line features as thin blue lines.
     */
    public static Style createDefaultLineStyle() {
        Style style;
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1));
        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features.
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);
        Rule rule;
        rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts;
        fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    public static Stroke getDefaultStroke(Color c) {
        Stroke result;
        double opacity;
        // Stroke
        opacity = 1; // 0.5; // 0.5 is partially opaque
        Literal strokeOpacityLiteral;
        strokeOpacityLiteral = filterFactory.literal(opacity);
        //opacityLiteral = null;
        // create a partially opaque outline stroke
        result = styleFactory.createStroke(
                filterFactory.literal(c),
                filterFactory.literal(1),
                strokeOpacityLiteral);
        return result;
    }

    /**
     * Create a Style to draw polygon features with a outline in outline_Color
     * and a fill in fill_Color.
     *
     * @param outline_Color
     * @param fill_Color
     * @return
     */
    public static Style createDefaultPolygonStyle(
            Color outline_Color,
            Color fill_Color) {

        double opacity;
        // Stroke
        Stroke stroke = getDefaultStroke(outline_Color);

        // Fill
        opacity = 0; // 0 Is totaly clear, 0.5 is partially opaque, 1.0 is solid
        Literal fillOpacityLiteral;
        fillOpacityLiteral = filterFactory.literal(opacity);
        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(fill_Color),
                fillOpacityLiteral);

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geometry of features
         */
        PolygonSymbolizer ps;
        ps = styleFactory.createPolygonSymbolizer(
                stroke,
                fill,
                null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(ps);
        FeatureTypeStyle fts;
        fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * Create a Style to draw polygon features with a thin blue outline and a
     * cyan fill
     *
     * @param featureCollection
     * @param attributeName
     * @param nClasses Should be restricted. Values in the range 0 to 8 should
     * work depending on paletteName.
     * @param classificationFunctionName Known valid names: "EqualInterval",
     * "Jenks", "StandardDeviation", "Quantile"
     * @param paletteName Known valid names: "Reds", "Grays", "PuBuGn"; To find
     * valid palette names use:
     * <code>String[] paletteNames = brewer.getPaletteNames(); for (int i = 0; i < paletteNames.length; i++) {System.out.println(paletteNames[i]);}</code>
     * @param addWhiteForZero @return
     */
    public static Object[] createPolygonStyle(
            FeatureCollection featureCollection,
            String attributeName,
            int nClasses,
            String classificationFunctionName,
            String paletteName,
            boolean addWhiteForZero
    ) {
        Object[] result = new Object[2];

        // STEP 0 Set up Color Brewer
        ColorBrewer brewer = ColorBrewer.instance();

        // STEP 1 - call a classifier function to summarise your content
        FilterFactory2 ff;
        ff = CommonFactoryFinder.getFilterFactory2();
        PropertyName propertyName;
        propertyName = ff.property(attributeName);
        Function classify;
        classify = ff.function(
                classificationFunctionName,
                propertyName,
                ff.literal(nClasses));
//        Classifier groups;
//        groups = (Classifier) classify.evaluate(featureCollection);
        RangedClassifier groups = null;
        Color[] colorsForRenderingFeatures = null;
        if (!featureCollection.isEmpty()) {
            Object classifier = classify.evaluate(featureCollection);
            if (classifier != null) {
                groups = (RangedClassifier) classify.evaluate(featureCollection);
                //System.out.println(groups.toString());

                // STEP 2 - look up a predefined palette from color brewer
                BrewerPalette palette;
                palette = brewer.getPalette(paletteName);

                colorsForRenderingFeatures = palette.getColors(nClasses);
                Color[] colorsForLegend = new Color[colorsForRenderingFeatures.length + 1];
                if (addWhiteForZero) {
                    System.arraycopy(colorsForRenderingFeatures, 0, colorsForLegend, 1, colorsForRenderingFeatures.length);
                    colorsForLegend[0] = Color.WHITE;
                } else {
                    colorsForLegend = colorsForRenderingFeatures;
                }
                // STEP 2b Sort Legend Items
                ArrayList<LegendItem> legendItems = new ArrayList<LegendItem>();
                if (addWhiteForZero) {
                    String newLabel = "0";
                    LegendItem li = new LegendItem(newLabel, colorsForLegend[0]);
                    legendItems.add(li);
                }
                String[] titles;
                titles = groups.getTitles();
                for (int i = 0; i < titles.length; i++) {
                    //String title = titles[i];
                    Object min = groups.getMin(i);
                    Object max = groups.getMax(i);
                    //String legendItemName;

                    // Modify Label so it is only integers in legend
                    String newLabel;
                    String minString = min.toString();
                    String maxString = max.toString();
                    String[] minParts = minString.split("\\.");
                    if (minParts.length == 2) {
                        newLabel = minParts[0];
                    } else {
                        newLabel = minString;
                    }
                    newLabel += "-";
                    String[] maxParts = maxString.split("\\.");
                    if (maxParts.length == 2) {
                        newLabel += maxParts[0];
                    } else {
                        newLabel += maxString;
                    }

                    //legendItemName = "title " + title + ", min " + min + ", max " + max;
                    //System.out.println(legendItemName);
                    LegendItem li;
                    if (addWhiteForZero) {
                        li = new LegendItem(newLabel, colorsForLegend[i + 1]);
                    } else {
                        li = new LegendItem(newLabel, colorsForLegend[i]);
                    }
                    legendItems.add(li);
                }
                result[1] = legendItems;
            } else {
                int debug = 1;
            }
        }
        //LegendLayer ll = new DW_ChoroplethMapLegendLayer(Color.BLACK,"label",legendItems);

        // STEP 3 - ask StyleGenerator to make a set of rules for the Classifier
        // assigning features the correct color based on height
        double opacity;
        opacity = 0.95; //0.5;//1.0; //1.0; //0.95
        int elsemode;
        elsemode = StyleGenerator.ELSEMODE_IGNORE;
        Stroke defaultStroke;
        defaultStroke = null; //getDefaultStroke(); We can set this to not draw outlines?
        String typeID;
        typeID = "Generated FeatureTypeStyle for " + nClasses + " "
                + classificationFunctionName + " classes in palette "
                + paletteName;
        FeatureType featureType;
        featureType = featureCollection.getSchema();
        GeometryDescriptor geometryDescriptor;
        geometryDescriptor = featureType.getGeometryDescriptor();
        Style style;
        style = styleFactory.createStyle();
        if (groups != null) {
            FeatureTypeStyle featureTypeStyle;
            featureTypeStyle = StyleGenerator.createFeatureTypeStyle(
                    groups,
                    propertyName,
                    colorsForRenderingFeatures,
                    typeID,
                    geometryDescriptor,
                    elsemode,
                    opacity,
                    defaultStroke);
            style.featureTypeStyles().add(featureTypeStyle);
        }
//        List<FeatureTypeStyle> ftss = style.featureTypeStyles();
//            Iterator<FeatureTypeStyle> ite = ftss.iterator();
//            while (ite.hasNext()){
//                FeatureTypeStyle fts = ite.next();
//                Description d = fts.getDescription();
//            }
        result[0] = style;
        return result;
    }

    /**
     * Assuming min is 0.
     *
     * @param g
     * @param cov
     * @param classificationFunctionName
     * @param ff
     * @param nClasses
     * @param paletteName
     * @param addWhiteForZero
     * @return
     */
    public static Style getStyle(
            AbstractGrid2DSquareCell g,
            GridCoverage cov,
            String classificationFunctionName,
            FilterFactory2 ff,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero) {
        String[] classNames;
        double[] breaks;
        Generic_double d = new Generic_double();
        double min = g.getGridStatistics(true).getMinDouble(true);
        double max = g.getGridStatistics(true).getMaxDouble(true);
        double interval = (max - min) / (double) nClasses;
        double minInterval = min;
        double maxInterval = min + interval;
        if (addWhiteForZero) {
            nClasses++;
            classNames = new String[nClasses];
            breaks = new double[nClasses];
            classNames[0] = "0 - 0.1";
            minInterval = 0.0d;
            maxInterval = 0.1d;//Math.nextUp(minInterval);
            breaks[0] = minInterval;
            minInterval = maxInterval;
            maxInterval = minInterval + interval;
            for (int i = 1; i < nClasses; i++) {
                if (i < nClasses - 1) {
                    classNames[i] = "" + minInterval + " - " + maxInterval;
                    breaks[i] = minInterval;
                    minInterval += interval;
                    maxInterval += interval;
                } else {
                    classNames[i] = "" + minInterval + " - " + max;
                    breaks[i] = minInterval;
                }
            }
        } else {
            classNames = new String[nClasses];
            breaks = new double[nClasses];
            for (int i = 0; i < nClasses; i++) {
                if (i < nClasses - 1) {
                    classNames[i] = "" + minInterval + " - " + maxInterval;
                    breaks[i] = minInterval;
                    minInterval += interval;
                    maxInterval += interval;
                } else {
                    classNames[i] = "" + minInterval + " - " + max;
                    breaks[i] = minInterval;
                }
            }
        }
//        Function classify;
//        classify = ff.function(
//                classificationFunctionName,
//                ff.literal(nClasses));
//        RangedClassifier rc = null;
//        rc = (RangedClassifier) classify.evaluate(cov);
//        for (int i = 0; i < nClasses; i++) {
//            double min = (Double) rc.getMin(i);
//            double max = (Double) rc.getMax(i);
//            classNames[i] = "" + min + " - " + max;
//            breaks[i] = min;
//        }
        ColorBrewer cb;
        cb = ColorBrewer.instance();
        BrewerPalette bp;
        bp = cb.getPalette(paletteName);
        Color[] colors;
        if (addWhiteForZero) {
            Color[] dummyColors = bp.getColors(nClasses - 1);
            colors = new Color[nClasses];
            colors[0] = Color.WHITE;
            System.arraycopy(dummyColors, 0, colors, 1, nClasses - 1);
        } else {
            colors = bp.getColors(nClasses);
        }
        StyleBuilder sb;
        sb = new StyleBuilder();
        ColorMap cm;
        cm = sb.createColorMap(classNames, breaks, colors, ColorMap.TYPE_RAMP);
        Style style;
        style = sb.createStyle(sb.createRasterSymbolizer(cm, 1));
        return style;
//        StyleBuilder sb = new StyleBuilder();
//        double interval = 1;
//        double min = 0;
//        ColorBrewer brewer = ColorBrewer.instance();
//        brewer.loadPalettes();
//        BrewerPalette[] palettes = brewer.getPalettes(ColorBrewer.SEQUENTIAL);
//        Color[] colors = palettes[1].getColors(5);
//        double[] breaks = new double[]{min, min + interval, min + 2 * interval, min + 3 * interval,
//            min + 4 * interval};
//        ColorMap map = sb.createColorMap(new String[]{"1", "2", "3", "4", "5"}, breaks, colors,
//                ColorMap.TYPE_RAMP);
//        Style style;
//        style = sb.createStyle(sb.createRasterSymbolizer(map, 1));
//        return style;
    }
    
    /**
     * Assuming min is 0.
     *
     * @param g
     * @param cov
     * @param classificationFunctionName
     * @param ff
     * @param nClasses
     * @param paletteName
     * @param addWhiteForZero
     * @return
     */
    public static Object[] getStyleAndLegendItems(
            double normalisation,
            AbstractGrid2DSquareCell g,
            GridCoverage cov,
            String classificationFunctionName,
            FilterFactory2 ff,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero) {
        Object[] result = new Object[2];
        ArrayList<LegendItem> legendItems;
        legendItems = new ArrayList<LegendItem>();
        String[] classNames;
        double[] breaks;
        Generic_double d = new Generic_double();
        double min = g.getGridStatistics(true).getMinDouble(true);
        double max = g.getGridStatistics(true).getMaxDouble(true);
        double interval = (max - min) / (double) nClasses;
        double minInterval = min;
        double maxInterval = min + interval;
        if (addWhiteForZero) {
            nClasses++;
            classNames = new String[nClasses];
            breaks = new double[nClasses];
            classNames[0] = "0";
            minInterval = 0.0d;
            maxInterval = 0.1d;//Math.nextUp(minInterval);
            breaks[0] = minInterval;
            minInterval = maxInterval;
            maxInterval = minInterval + interval;
            for (int i = 1; i < nClasses; i++) {
                if (i < nClasses - 1) {
                    double roundedMinInterval;
                    roundedMinInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + minInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    double roundedMaxInterval;
                    roundedMaxInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + maxInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    classNames[i] = "" + roundedMinInterval + " - " + roundedMaxInterval;
                    breaks[i] = minInterval;
                    minInterval += interval;
                    maxInterval += interval;
                } else {
                    double roundedMinInterval;
                    roundedMinInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + minInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    double roundedMax;
                    roundedMax = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + max/normalisation), 2, RoundingMode.UP).doubleValue();
                    classNames[i] = "" + roundedMinInterval + " - " + roundedMax;
                    breaks[i] = minInterval;
                }
            }
        } else {
            classNames = new String[nClasses];
            breaks = new double[nClasses];
            for (int i = 0; i < nClasses; i++) {
                if (i < nClasses - 1) {
                    
                    
                    double roundedMinInterval;
                    roundedMinInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + minInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    double roundedMaxInterval;
                    roundedMaxInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + maxInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    classNames[i] = "" + roundedMinInterval + " - " + roundedMaxInterval;
                    breaks[i] = minInterval;
                    minInterval += interval;
                    maxInterval += interval;
                } else {
                    double roundedMinInterval;
                    roundedMinInterval = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + minInterval/normalisation), 2, RoundingMode.UP).doubleValue();
                    
                    double roundedMax;
                    roundedMax = Generic_BigDecimal.roundIfNecessary(new BigDecimal("" + max/normalisation), 2, RoundingMode.UP).doubleValue();
                    classNames[i] = "" + roundedMinInterval + " - " + roundedMax;
                    breaks[i] = minInterval;
                }
            }
        }
        ColorBrewer cb;
        cb = ColorBrewer.instance();
        BrewerPalette bp;
        bp = cb.getPalette(paletteName);
        Color[] colors;
        if (addWhiteForZero) {
            Color[] dummyColors = bp.getColors(nClasses - 1);
            colors = new Color[nClasses];
            colors[0] = Color.WHITE;
            System.arraycopy(dummyColors, 0, colors, 1, nClasses - 1);
        } else {
            colors = bp.getColors(nClasses);
        }
        StyleBuilder sb;
        sb = new StyleBuilder();
        ColorMap cm;
        cm = sb.createColorMap(classNames, breaks, colors, ColorMap.TYPE_RAMP);
        Style style;
        style = sb.createStyle(sb.createRasterSymbolizer(cm, 1));
        result[0] = style;
        for (int i = 0; i < nClasses; i++) {
             LegendItem li;
                    li = new LegendItem(classNames[i],colors[i]);
                    legendItems.add(li);
        }
        result[1] = legendItems;
        return result;
    }
}
