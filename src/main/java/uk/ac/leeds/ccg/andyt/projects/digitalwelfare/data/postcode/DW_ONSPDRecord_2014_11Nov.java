/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;

/**
 *
 * @author geoagdt
 */
public class DW_ONSPDRecord_2014_11Nov extends DW_ONSPDRecord_2013_08Aug {

    protected final String oac11;
    
    public DW_ONSPDRecord_2014_11Nov(DW_Environment env, String line) {
        super(env, line);
        String[] fields = line.split("\",\"");
        oac11 = fields[fields.length - 1];
    }

    public String getOac11() {
        return oac11;
    }
}
