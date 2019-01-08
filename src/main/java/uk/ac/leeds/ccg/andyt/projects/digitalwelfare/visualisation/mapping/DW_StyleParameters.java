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
import uk.ac.leeds.ccg.andyt.geotools.Geotools_LegendItem;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_AbstractStyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_StyleParameters extends Geotools_AbstractStyleParameters {
    
    private ArrayList<Style> midgroundStyles;
    private ArrayList<Style> backgroundStyles;
    private ArrayList<Style> foregroundStyles;
    private ArrayList<ArrayList<Geotools_LegendItem>> legendItems;

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
            styles = new ArrayList<>();
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
            styles = new ArrayList<>();
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
            styles = new ArrayList<>();
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
            styles = new ArrayList<>();
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
            styles = new ArrayList<>();
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
            styles = new ArrayList<>();
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
    public ArrayList<Geotools_LegendItem> getLegendItems(int index) {
        ArrayList<Geotools_LegendItem> result;
        ArrayList<ArrayList<Geotools_LegendItem>> legendItems0;
        legendItems0 = getLegendItems();
        try {
            result = legendItems0.get(index);
        } catch (IndexOutOfBoundsException e) {
            result = null;
            int i = legendItems0.size();
            while (i <= index) {
                ArrayList<Geotools_LegendItem> newLegendItem;
                newLegendItem = new ArrayList<>();
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
    public ArrayList<Geotools_LegendItem> setLegendItems(
            ArrayList<Geotools_LegendItem> legendItems, int index) {
        ArrayList<Geotools_LegendItem> result;
        result = getLegendItems(index); // This ensures that legendItems is initialised to the right length.
        getLegendItems().set(index, legendItems);
        return result;
    }

    /**
     * @return the legendItems
     */
    public ArrayList<ArrayList<Geotools_LegendItem>> getLegendItems() {
        if (legendItems == null) {
            legendItems = new ArrayList<>();
        }
        return legendItems;
    }

    /**
     * @param legendItems the legendItems to set
     */
    public void setLegendItems(ArrayList<ArrayList<Geotools_LegendItem>> legendItems) {
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
