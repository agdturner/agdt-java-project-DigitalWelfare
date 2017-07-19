/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public class DW_ONSPDRecord_EastingNorthing extends DW_Object {

    private final String PostcodeF;
    private final int oseast1m;
    private final int osnrth1m;

    public DW_ONSPDRecord_EastingNorthing(DW_Environment env, String line) {
        this.env = env;
        String[] fields = line.split("\",\"");
        //pcd = fields[0].substring(1);
        PostcodeF = env.getDW_Postcode_Handler().formatPostcode(fields[0]);
        if (fields[9].isEmpty()) {
            oseast1m = -1;
        } else {
            oseast1m = Integer.valueOf(fields[9]);
        }
        if (fields[10].isEmpty()) {
            osnrth1m = -1;
        } else {
            osnrth1m = Integer.valueOf(fields[10]);
        }
    }

    public String getPostcodeF() {
        return PostcodeF;
    }

    public int getOseast1m() {
        return oseast1m;
    }

    public int getOsnrth1m() {
        return osnrth1m;
    }
}
