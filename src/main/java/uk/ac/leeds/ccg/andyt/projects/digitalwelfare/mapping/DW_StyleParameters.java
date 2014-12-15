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

import java.awt.Color;
import java.util.ArrayList;
import org.geotools.styling.Style;

/**
 *
 * @author geoagdt
 */
public class DW_StyleParameters {
    
    private Style style;
    private String classificationFunctionName;
    private int nClasses;
    private String paletteName;
    private boolean addWhiteForZero = false;
    private Style backgroundStyle;
    private String backgroundStyleTitle;
    private Style foregroundStyle;
    private String foregroundStyleTitle;
    private ArrayList<DW_LegendItem> legendItems;

    /**
     * @return the style
     */
    public Style getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * @return the classificationFunctionName
     */
    public String getClassificationFunctionName() {
        return classificationFunctionName;
    }

    /**
     * @param classificationFunctionName the classificationFunctionName to set
     */
    public void setClassificationFunctionName(String classificationFunctionName) {
        this.classificationFunctionName = classificationFunctionName;
    }

    /**
     * @return the nClasses
     */
    public int getnClasses() {
        return nClasses;
    }

    /**
     * @param nClasses the nClasses to set
     */
    public void setnClasses(int nClasses) {
        this.nClasses = nClasses;
    }

    /**
     * @return the paletteName
     */
    public String getPaletteName() {
        return paletteName;
    }

    /**
     * @param paletteName the paletteName to set
     */
    public void setPaletteName(String paletteName) {
        this.paletteName = paletteName;
    }

    /**
     * @return the addWhiteForZero
     */
    public boolean isAddWhiteForZero() {
        return addWhiteForZero;
    }

    /**
     * @param addWhiteForZero the addWhiteForZero to set
     */
    public void setAddWhiteForZero(boolean addWhiteForZero) {
        this.addWhiteForZero = addWhiteForZero;
    }

    /**
     * @return the backgroundStyle
     */
    public Style getBackgroundStyle() {
        return backgroundStyle;
    }

    /**
     * @param backgroundStyle the backgroundStyle to set
     */
    public void setBackgroundStyle(Style backgroundStyle) {
        this.backgroundStyle = backgroundStyle;
    }

    /**
     * @return the backgroundStyleTitle
     */
    public String getBackgroundStyleTitle() {
        return backgroundStyleTitle;
    }

    /**
     * @param backgroundStyleTitle the backgroundStyleTitle to set
     */
    public void setBackgroundStyleTitle(String backgroundStyleTitle) {
        this.backgroundStyleTitle = backgroundStyleTitle;
    }

    /**
     * @return the foregroundStyle
     */
    public Style getForegroundStyle() {
        return foregroundStyle;
    }

    /**
     * @param foregroundStyle the foregroundStyle to set
     */
    public void setForegroundStyle(Style foregroundStyle) {
        this.foregroundStyle = foregroundStyle;
    }

    /**
     * @return the foregroundStyleTitle
     */
    public String getForegroundStyleTitle() {
        return foregroundStyleTitle;
    }

    /**
     * @param foregroundStyleTitle the foregroundStyleTitle to set
     */
    public void setForegroundStyleTitle(String foregroundStyleTitle) {
        this.foregroundStyleTitle = foregroundStyleTitle;
    }

    /**
     * @return the legendItems
     */
    public ArrayList<DW_LegendItem> getLegendItems() {
        return legendItems;
    }

    /**
     * @param legendItems the legendItems to set
     */
    public void setLegendItems(ArrayList<DW_LegendItem> legendItems) {
        this.legendItems = legendItems;
    }
    
    public DW_StyleParameters() {
//        this.classificationFunctionName = "";
//        this.nClasses = 0;
//        this.paletteName = "";
//        this.addWhiteForZero = false;
//        this.backgroundStyle = null;
//        this.backgroundStyleTitle = "";
    }
    
    public DW_StyleParameters(
            String classificationFunctionName,
            int nClasses,
            String paletteName,
            boolean addWhiteForZero,
            Style backgroundStyle,
            String backgroundStyleTitle) {
        this.addWhiteForZero = addWhiteForZero;
       this.backgroundStyle = backgroundStyle;
       this.backgroundStyleTitle = backgroundStyleTitle;
       this.classificationFunctionName = classificationFunctionName;
       this.paletteName = paletteName;
    }

    /**
     * Initialise styleParameters.
     * No deep copying.
     * 
     * @param styleParameters
     * @return
     */
    public DW_StyleParameters(DW_StyleParameters styleParameters) {
       this.addWhiteForZero = styleParameters.addWhiteForZero;
       this.backgroundStyle = styleParameters.backgroundStyle;
       this.backgroundStyleTitle = styleParameters.backgroundStyleTitle;
       this.classificationFunctionName = styleParameters.classificationFunctionName;
       this.foregroundStyle = styleParameters.foregroundStyle;
       this.foregroundStyleTitle = styleParameters.foregroundStyleTitle;
       this.legendItems = styleParameters.legendItems;
       this.nClasses = styleParameters.nClasses;
       this.paletteName = styleParameters.paletteName;
       this.style = styleParameters.style;
    }

    
    
}
