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
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Style;

/**
 *
 * @author geoagdt
 */
public class DW_Style extends AGDT_Style {

    /**
     * Create and returns a Style to draw point features as circles with blue
     * outlines and cyan fill.
     *
     * @return A Style to draw point features as circles with blue outlines and
     * cyan fill.
     */
    public static ArrayList<Style> createAdviceLeedsPointStyles() {
//        StyleBuilder sb = new StyleBuilder();
//        PointSymbolizer ps = sb.createPointSymbolizer();
//        FilterFactory2 ff = sb.getFilterFactory();
//        StyleFactory sf = sb.getStyleFactory();
//        sf.
        ArrayList<Style> result;
        result = new ArrayList<Style>();
        Mark mark;
        int size;
        String type;
        Color fill;
        Color outline;
        outline = Color.BLUE;
        // Order of styles added is in the order of DW_Processor.getAdviceLeedsServiceNames()
//        ArrayList<String> tAdviceLeedsServiceNames;
//        tAdviceLeedsServiceNames = DW_Processor.getAdviceLeedsServiceNames();
        // CAB as crosses
        size = 9;
        type = "Cross";
        fill = Color.GRAY;
        Style GeneralCABStyle;
        GeneralCABStyle = getPointStyle(size, type, fill, outline);
        //  Otley
        result.add(GeneralCABStyle);
        //  Morley
        result.add(GeneralCABStyle);
        //  Crossgates
        result.add(GeneralCABStyle);
        //  Pudsey
        result.add(GeneralCABStyle);
        // Leeds CAB
        fill = Color.WHITE;
        result.add(getPointStyle(size, type, fill, outline));
        // Chapeltown CAB
        fill = Color.LIGHT_GRAY;
        result.add(getPointStyle(size, type, fill, outline));

        // Charities
        size = 5;
        type = "Square";
        // Ebor Gardens
        fill = Color.BLUE;
        result.add(getPointStyle(size, type, fill, outline));
        // St Vincents
        fill = Color.CYAN;
        result.add(getPointStyle(size, type, fill, outline));

        // Leeds Law Centre
        size = 10;
        type = "X";
        fill = Color.GRAY;
        result.add(getPointStyle(size, type, fill, outline));

        // Others
        size = 8;
        type = "Triangle";
        // BLC
        fill = Color.CYAN;
        result.add(getPointStyle(size, type, fill, outline));
        // LCC_WRU
        fill = Color.BLUE;
        result.add(getPointStyle(size, type, fill, outline));
        return result;
    }
}
