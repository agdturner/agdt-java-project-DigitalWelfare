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
    private boolean addWhiteForZero;
    private Style backgroundStyle;
    private String backgroundStyleTitle;
    private boolean drawBoundaries;
    private Style foregroundStyle0;
    private String foregroundStyleTitle0;
    private Style foregroundStyle1;
    private String foregroundStyleTitle1;
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
     * @return the foregroundStyle0
     */
    public Style getForegroundStyle0() {
        return foregroundStyle0;
    }

    /**
     * @param foregroundStyle0 the foregroundStyle0 to set
     */
    public void setForegroundStyle0(Style foregroundStyle0) {
        this.foregroundStyle0 = foregroundStyle0;
    }

    /**
     * @return the foregroundStyleTitle0
     */
    public String getForegroundStyleTitle0() {
        return foregroundStyleTitle0;
    }

    /**
     * @param foregroundStyleTitle0 the foregroundStyleTitle0 to set
     */
    public void setForegroundStyleTitle0(String foregroundStyleTitle0) {
        this.foregroundStyleTitle0 = foregroundStyleTitle0;
    }

    /**
     * @return the foregroundStyle1
     */
    public Style getForegroundStyle1() {
        return foregroundStyle1;
    }

    /**
     * @param foregroundStyle1 the foregroundStyle1 to set
     */
    public void setForegroundStyle1(Style foregroundStyle1) {
        this.foregroundStyle1 = foregroundStyle1;
    }

    /**
     * @return the foregroundStyleTitle0
     */
    public String getForegroundStyleTitle1() {
        return foregroundStyleTitle1;
    }

    /**
     * @param foregroundStyleTitle1 the foregroundStyleTitle1 to set
     */
    public void setForegroundStyleTitle1(String foregroundStyleTitle1) {
        this.foregroundStyleTitle1 = foregroundStyleTitle1;
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
            String backgroundStyleTitle,
            boolean drawBoundaries) {
        this.classificationFunctionName = classificationFunctionName;
        this.nClasses = nClasses;
        this.paletteName = paletteName;
        this.addWhiteForZero = addWhiteForZero;
        this.backgroundStyle = backgroundStyle;
        this.backgroundStyleTitle = backgroundStyleTitle;
        this.drawBoundaries = drawBoundaries;
    }

    /**
     * Initialise styleParameters. No deep copying.
     *
     * @param styleParameters
     * @return
     */
    public DW_StyleParameters(DW_StyleParameters styleParameters) {
        this.addWhiteForZero = styleParameters.addWhiteForZero;
        this.backgroundStyle = styleParameters.backgroundStyle;
        this.backgroundStyleTitle = styleParameters.backgroundStyleTitle;
        this.classificationFunctionName = styleParameters.classificationFunctionName;
        this.foregroundStyle0 = styleParameters.foregroundStyle0;
        this.foregroundStyleTitle0 = styleParameters.foregroundStyleTitle0;
        this.legendItems = styleParameters.legendItems;
        this.nClasses = styleParameters.nClasses;
        this.paletteName = styleParameters.paletteName;
        this.style = styleParameters.style;
        this.drawBoundaries = styleParameters.drawBoundaries;
    }

    /**
     * @return the drawBoundaries
     */
    public boolean isDrawBoundaries() {
        return drawBoundaries;
    }

    /**
     * @param drawBoundaries the drawBoundaries to set
     */
    public void setDrawBoundaries(boolean drawBoundaries) {
        this.drawBoundaries = drawBoundaries;
    }

}
