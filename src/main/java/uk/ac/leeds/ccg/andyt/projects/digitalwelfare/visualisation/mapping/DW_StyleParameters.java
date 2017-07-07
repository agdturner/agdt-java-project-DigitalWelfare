/*
 * Copyright (C) 2015 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping;

import java.util.ArrayList;
import org.geotools.styling.Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_LegendItem;
//import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParameters;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_StyleParametersAbstract;

/**
 *
 * @author geoagdt
 */
public class DW_StyleParameters extends AGDT_StyleParametersAbstract {
    
    private ArrayList<Style> midgroundStyles;
    private ArrayList<Style> backgroundStyles;
    private ArrayList<Style> foregroundStyles;
    private ArrayList<ArrayList<AGDT_LegendItem>> legendItems;

    public DW_StyleParameters() {
    }

    /**
     * @param index
     * @return The style at index.
     */
    public Style getMidgroundStyle(
            int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getMidgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setMidgroundStyles(styles);
        }
        try {
            result = styles.get(index);
        } catch (IndexOutOfBoundsException e) {
            int i = styles.size();
            while (i <= index) {
                styles.add(i, null);
                i++;
            }
            return null;
        }
        return result;
    }

    /**
     * @param style
     * @param index
     * @return The style previously at index.
     */
    public Style setMidgroundStyle(Style style, int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getMidgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setMidgroundStyles(styles);
        }
        result = getMidgroundStyle(index); // This ensures that styles is initialised to the right length.
        styles.set(index, style);            
        return result;
    }
    
    /**
     * @param index
     * @return The style at index.
     */
    public Style getBackgroundStyle(
            int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getBackgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setBackgroundStyles(styles);
        }
        try {
            result = styles.get(index);
        } catch (IndexOutOfBoundsException e) {
            int i = styles.size();
            while (i <= index) {
                styles.add(i, null);
                i++;
            }
            return null;
        }
        return result;
    }

    /**
     * @param style
     * @param index
     * @return The style previously at index.
     */
    public Style setBackgroundStyle(Style style, int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getBackgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setBackgroundStyles(styles);
        }
        result = getBackgroundStyle(index); // This ensures that styles is initialised to the right length.
        styles.set(index, style);            
        return result;
    }
    
    /**
     * @param index
     * @return The style at index.
     */
    public Style getForegroundStyle(
            int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getBackgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setBackgroundStyles(styles);
        }
        try {
            result = styles.get(index);
        } catch (IndexOutOfBoundsException e) {
            int i = styles.size();
            while (i <= index) {
                styles.add(i, null);
                i++;
            }
            return null;
        }
        return result;
    }

    /**
     * @param style
     * @param index
     * @return The style previously at index.
     */
    public Style setForegroundStyle(Style style, int index) {
        Style result;
        ArrayList<Style> styles;
        styles = getBackgroundStyles();
        if (styles == null) {
            styles = new ArrayList<Style>();
            setBackgroundStyles(styles);
        }
        result = getBackgroundStyle(index); // This ensures that styles is initialised to the right length.
        styles.set(index, style);            
        return result;
    }

    /**
     * @param index
     * @return a specific list of legendItems
     */
    public ArrayList<AGDT_LegendItem> getLegendItems(int index) {
        ArrayList<AGDT_LegendItem> result;
        ArrayList<ArrayList<AGDT_LegendItem>> legendItems0;
        legendItems0 = getLegendItems();
        try {
            result = legendItems0.get(index);
        } catch (IndexOutOfBoundsException e) {
            result = null;
            int i = legendItems0.size();
            while (i <= index) {
                ArrayList<AGDT_LegendItem> newLegendItem;
                newLegendItem = new ArrayList<AGDT_LegendItem>();
                legendItems0.add(i, result);
                i++;
                result = newLegendItem;
            }
        }
        return result;
    }

    /**
     * @param legendItems the legendItems to set
     * @param index
     * @return
     */
    public ArrayList<AGDT_LegendItem> setLegendItems(
            ArrayList<AGDT_LegendItem> legendItems, int index) {
        ArrayList<AGDT_LegendItem> result;
        result = getLegendItems(index); // This ensures that legendItems is initialised to the right length.
        getLegendItems().set(index, legendItems);
        return result;
    }

    /**
     * @return the legendItems
     */
    public ArrayList<ArrayList<AGDT_LegendItem>> getLegendItems() {
        if (legendItems == null) {
            legendItems = new ArrayList<ArrayList<AGDT_LegendItem>>();
        }
        return legendItems;
    }

    /**
     * @param legendItems the legendItems to set
     */
    public void setLegendItems(ArrayList<ArrayList<AGDT_LegendItem>> legendItems) {
        this.legendItems = legendItems;
    }

    public ArrayList<Style> getMidgroundStyles() {
        return midgroundStyles;
    }

    public void setMidgroundStyles(ArrayList<Style> midgroundStyles) {
        this.midgroundStyles = midgroundStyles;
    }

    public ArrayList<Style> getBackgroundStyles() {
        return backgroundStyles;
    }

    public void setBackgroundStyles(ArrayList<Style> backgroundStyles) {
        this.backgroundStyles = backgroundStyles;
    }

    public ArrayList<Style> getForegroundStyles() {
        return foregroundStyles;
    }

    public void setForegroundStyles(ArrayList<Style> foregroundStyles) {
        this.foregroundStyles = foregroundStyles;
    }
    
}
