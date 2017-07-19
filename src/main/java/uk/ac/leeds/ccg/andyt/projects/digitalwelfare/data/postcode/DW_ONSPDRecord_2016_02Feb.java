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
public class DW_ONSPDRecord_2016_02Feb extends DW_ONSPDRecord_2015_08Aug {

    protected final String pfa;
    protected final String imd;

    public DW_ONSPDRecord_2016_02Feb(DW_Environment env, String line) {
        super(env, line);
        String[] fields = line.split("\",\"");
        pfa = fields[fields.length - 2];
        imd = fields[fields.length - 1];
    }

    public String getPfa() {
        return pfa;
    }

    public String getImd() {
        return imd;
    }
}
