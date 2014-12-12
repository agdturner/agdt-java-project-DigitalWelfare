/**
 * *****************************************************************************
 * * * CopyRight (c) 2013 ASEG Inc. All Rights Reserved * * * * This software
 * is the confidential and proprietary information of * * ASEG Inc.
 * ("Confidential Information"). You shall not * * disclose such Confidential
 * Information and shall use it only in * * accordance with the terms of the
 * license agreement you entered into * * with ASEG Inc. * * * * ASEG Inc. MAKES
 * NO REPRESENTATIONS OR WARRANTIES ABOUT THE * * SUITABILITY OF THE SOFTWARE,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT * * NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR * * A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. ASEG Inc. SHALL NOT * * BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, * * MODIFYING OR DISTRIBUTING THIS SOFTWARE
 * OR ITS DERIVATIVES. * * * @version %I% %G% %U%
 *
 *  * * @author Matt Nolan
 *  * * @author Hal Mirsky
 * ****************************************************************************
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_double;

/**
 * Creates a scale bar on the map
 *
 * @author mnolan
 * @author Hal Mirsky
 *
 */
public class DW_ChoroplethMapLegendLayer extends DirectLayer {

    private List<LegendItem> legendItems;
    private MapContent mc;
    ReferencedEnvelope bounds;

    private int originalImageWidth;
    private int originalImageHeight;
    private boolean addLegendToTheSide;
    private String legendTitle;
    private String mapTitle;

    private AffineTransform at;
    private FontRenderContext frc;
    private final Font mapTitleFont;
    //private final FontMetrics mapTitleFontMetrics;
    
    private final Font legendTitleFont;
    //private final FontMetrics legendTitleFontMetrics;
    
    private final Font legendItemFont;
    private int legendItemFontHeight;
    //private final FontMetrics legendItemFontMetrics;
    
    private final Color backgroundColor = Color.WHITE;

    private static final int marginWidth = 2;  // horizontal margin between edge and contents
    private static final int marginHeight = 2; // vertical margin between edge and contents
    private static final int spaceWidth = 3;   // horizontal space between legend components (e.g. label and icon)
    private static final int spaceHeight = 4;  // vertical space between lines of text

    private static final int legendInsetWidth = 2;   // x coord val of left edge of bounds rectangle
    private static final int legendInsetHeight = 2;  // y coord val of bottom edge of bounds rectangle

    private int newImageWidth;
    private int newImageHeight;

    private int legendWidth;
    private int legendHeight;

    private int mapTitleHeight;
    private int mapItemHeight;

    private int legendTitleWidth;
    private int legendTitleHeight;
    private int legendItemLabelMaxWidth;
    private int legendItemLabelMaxHeight;
    private int legendIconWidth;

    private int legendUpperLeftX;
    private int legendUpperLeftY;

    public static class LegendItem {

        public String label;
        public Color color;

        public LegendItem(String l, Color c) {
            label = l;
            color = c;
        }
    }

    public DW_ChoroplethMapLegendLayer(
            String mapTitle,
            String legendTitle,
            List<LegendItem> legendItems,
            MapContent mc,
            int imageWidth,
            int imageHeight,
            boolean addLegendToTheSide) {
        this.mapTitleFont = new Font("Ariel", Font.BOLD, 14);
        //this.mapTitleFontMetrics = new FontMetrics(mapTitleFont);
        this.legendTitleFont = new Font("Ariel", Font.BOLD, 12);
        this.legendItemFont = new Font("Ariel", Font.PLAIN, 10);
        init(mapTitle,
                legendTitle,
                legendItems,
                mc,
                imageWidth,
                imageHeight,
                addLegendToTheSide);
    }

    public final void init(
            AffineTransform at) {
        this.at = at;
        this.frc = new FontRenderContext(
            at,
            RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
            RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        buildLegendParams();
        initBounds();
    }
    
    /**
     * 
     * @param mapTitle
     * @param legendTitle
     * @param legendItems
     * @param mc
     * @param imageWidth
     * @param imageHeight
     * @param addLegendToTheSide 
     */
    public final void init(
            String mapTitle,
            String legendTitle,
            List<LegendItem> legendItems,
            MapContent mc,
            int imageWidth,
            int imageHeight,
            boolean addLegendToTheSide) {
        this.frc = new FontRenderContext(
            at,
            RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
            RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        this.mapTitle = mapTitle;
        this.legendTitle = legendTitle;
        this.legendItems = legendItems;
        this.mc = mc;
        this.originalImageWidth = imageWidth;
        this.originalImageHeight = imageHeight;
        this.addLegendToTheSide = addLegendToTheSide;
        buildLegendParams();
        initBounds();
    }

    private void buildLegendParams() {
        int[] legendTitleMaxWidthAndHeight;
        legendTitleMaxWidthAndHeight = getLegendTitleMaxWidthAndHeight();
        legendTitleWidth = legendTitleMaxWidthAndHeight[0];
        legendTitleHeight = legendTitleMaxWidthAndHeight[1];
        
        int[] legendItemLabelMaxWidthAndHeight;
        legendItemLabelMaxWidthAndHeight = getLegendItemLabelMaxWidthAndHeight();
        legendItemLabelMaxWidth = legendItemLabelMaxWidthAndHeight[0];
        legendItemLabelMaxHeight = legendItemLabelMaxWidthAndHeight[1];
        legendIconWidth = legendItemLabelMaxHeight;
        
        legendWidth = Math.max(
                legendTitleWidth + (2 * marginWidth),
                legendIconWidth + spaceWidth + legendItemLabelMaxWidth + (2 * marginWidth));

        legendHeight
                = marginHeight
                + legendTitleHeight + spaceHeight
                + (spaceHeight + legendItemLabelMaxHeight) * legendItems.size()
                + marginHeight;

        if (addLegendToTheSide) {
            this.legendUpperLeftX = legendInsetWidth; //imageWidth;
            this.newImageWidth = originalImageWidth + legendWidth + legendInsetWidth;
        } else {
            this.legendUpperLeftX = legendInsetWidth;
        }
        
        //this.legendUpperLeftY = newImageHeight - legendHeight - legendInsetHeight;
    }
    
    /**
     * @TODO Move to somewhere general.
     * @param r
     * @return 
     */
    public static int getMaxHeight(
            Rectangle2D r) {
        int result;
        double h;
        h = r.getHeight();
        result = Generic_double.roundUpToNearestInt(h);
        return result;
    }
    
    /**
     * 
     * @param r
     * @return 
     */
    public static int getMaxWidth(
            Rectangle2D r) {
        int result;
        double w;
        w = r.getWidth();
        result = Generic_double.roundUpToNearestInt(w);
        return result;
    }
    
    /**
     * For some reason, this seems to by returning incorrect width and possibly height!
     * @return 
     */
    public int[] getLegendTitleMaxWidthAndHeight() {
        int[] result = new int[2];
        int width = 0;
        int height = 0;
        //FontMetrics.stringWidth(String str)
        Rectangle2D b;
        b = legendTitleFont.getStringBounds(
                legendTitle,
                frc);
        TextLayout tl = new TextLayout(legendTitle, legendTitleFont, frc);
        b = tl.getBounds();
//        LineMetrics lm = legendTitleFont.getLineMetrics(title, frc);
//        lm.getHeight();
        // width
        width = getMaxWidth(b);
        width += 30; // This is a terrible hack and should be removed. The hack is there because the width is not what is wanted probably as a result of the frc AffineTransform.
        // height
        height = getMaxHeight(b);
        height += 5; // This is a terrible hack and should be removed. The hack is there because the height is not what is wanted probably as a result of the frc AffineTransform.
        result[0] = width;
        result[1] = height;
        return result;
    }

    /**
     * 
     * @return 
     */
    public int[] getLegendItemLabelMaxWidthAndHeight() {
        int[] result = new int[2];
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < legendItems.size(); i++) {
            DW_ChoroplethMapLegendLayer.LegendItem li = legendItems.get(i);
            Rectangle2D b;
            b = legendItemFont.getStringBounds(
                    li.label,
                    frc);
            // width
            int w;
            w = getMaxWidth(b);
            maxWidth = Math.max(
                    maxWidth,
                    w);
            // height
            int h;
            h = getMaxHeight(b);
            maxHeight = Math.max(
                    maxHeight,
                    h);
        }
        result[0] = maxWidth;
        result[1] = maxHeight;
        return result;
    }

    @Override
    public void draw(
            Graphics2D graphics,
            MapContent mapContent,
            MapViewport viewport) {
        try {
//			//Wait for the viewport transform to finish loading
//			while(viewport.getScreenToWorld() == null)
//			{
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//				}
//			}
//		 	
//			//Create the segments of the scale bar
//			Rectangle scrRect = viewport.getScreenArea();	
            
            AffineTransform at = graphics.getTransform(); 
            init(at);
            
            newImageHeight = originalImageHeight; // This is true until title is added
            this.legendUpperLeftY = legendInsetHeight;
//            this.legendUpperLeftY = newImageHeight - legendHeight - legendInsetHeight;
//            this.legendUpperLeftY -= 10; // Terrible hack
            
            int x;
            int y;
            // constructor args: x/y of upper left corner
//            // Add a white background and draw a box
//            graphics.setColor(backgroundColor);
//            graphics.fill(bounds);
//            graphics.setFont(titleFont);
//            graphics.setColor(Color.BLACK);
//            graphics.draw(bounds);
            // Draw Legend title and set font for drawing items
            graphics.setFont(legendTitleFont);
            
            int startLegendX;
            startLegendX = legendUpperLeftX + marginWidth;
            int startLegendTitleY;
            startLegendTitleY = legendUpperLeftY + marginHeight + legendTitleHeight; // legendTitleHeight added as I think we start drawing strings from the bottom left!
            graphics.drawString(
                    legendTitle,
                    startLegendX,
                    startLegendTitleY);
            graphics.setFont(legendItemFont);
            for (int i = 0; i < legendItems.size(); i++) {
                graphics.setColor(Color.BLACK);
                String label = legendItems.get(i).label;
                Rectangle regionIcon;
                regionIcon = new Rectangle(
                        //insetWidth + legendWidth - marginWidth - legendIconWidth + upperLeftX, // For end placement
                        startLegendX, // For start placement
                        startLegendTitleY + spaceHeight + (i * (spaceHeight + legendItemLabelMaxHeight)),
                        legendIconWidth,
                        legendItemLabelMaxHeight);
                graphics.drawString(
                        label,
                        //insetWidth + marginWidth + upperLeftX, // For start placement
                        startLegendX + legendIconWidth + spaceWidth, // For end placement
                        startLegendTitleY + spaceHeight + (i * (spaceHeight + legendItemLabelMaxHeight)) + legendItemLabelMaxHeight);
                if (legendItems.get(i).color != null) {
                    graphics.setColor(legendItems.get(i).color);
                    graphics.fill(regionIcon);
                    graphics.setColor(Color.BLACK);
                    graphics.draw(regionIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initBounds() {
        bounds = mc.getMaxBounds();
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        double maxX = bounds.getMaxX();
        double maxY = bounds.getMaxY();
        System.out.println("minX " + minX);
        System.out.println("minY " + minY);
        System.out.println("maxX " + maxX);
        System.out.println("maxY " + maxY);
        double width = bounds.getWidth();
        //System.out.println("height " + height + ", width " + width);
        double imageWidth_double = originalImageWidth;
        double minXnew = minX - (width / imageWidth_double) * legendWidth;
        //double height = bounds.getHeight();
        //double imageHeight_double = imageHeight;
        //double minYnew = minY - ( height / imageHeight_double ) * legendHeight;
        //bounds.expandToInclude(minXnew, minYnew);
        bounds.expandToInclude(minXnew, minY);
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return bounds;
    }

}
