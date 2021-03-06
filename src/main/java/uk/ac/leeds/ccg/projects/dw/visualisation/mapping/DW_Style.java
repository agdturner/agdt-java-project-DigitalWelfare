
package uk.ac.leeds.ccg.projects.dw.visualisation.mapping;

import java.awt.Color;
import java.util.ArrayList;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import uk.ac.leeds.ccg.geotools.Geotools_Style;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Style extends Geotools_Style {

    /**
     * Create and returns a Style to draw point features as circles with blue
     * outlines and cyan fill.
     *
     * @return A Style to draw point features as circles with blue outlines and
     * cyan fill.
     */
    public ArrayList<Style> createAdviceLeedsPointStyles() {
//        StyleBuilder sb = new StyleBuilder();
//        PointSymbolizer ps = sb.createPointSymbolizer();
//        FilterFactory2 ff = sb.getFilterFactory();
//        StyleFactory sf = sb.getStyleFactory();
//        sf.
        ArrayList<Style> result;
        result = new ArrayList<>();
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
