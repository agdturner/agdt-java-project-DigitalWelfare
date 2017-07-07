/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

/**
 *
 * @author geoagdt
 */
public class DW_ONSPDRecord_2013_02Feb extends DW_ONSPDRecord_2012_11Nov {

    protected final String parish;
    protected final String wz11;
    
    public DW_ONSPDRecord_2013_02Feb(String line) {
        super(line);
        String[] fields = line.split("\",\"");
        parish = fields[fields.length - 2];
        wz11 = fields[fields.length - 1];
    }

    public String getParish() {
        return parish;
    }
    
    public String getWz11() {
        return wz11;
    }
}
