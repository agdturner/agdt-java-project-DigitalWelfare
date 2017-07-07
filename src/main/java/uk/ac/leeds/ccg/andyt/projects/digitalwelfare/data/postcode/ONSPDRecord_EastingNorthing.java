/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

/**
 *
 * @author geoagdt
 */
public class ONSPDRecord_EastingNorthing {

    private final String PostcodeF;
    private final int oseast1m;
    private final int osnrth1m;

    public ONSPDRecord_EastingNorthing(String line) {
        String[] fields = line.split("\",\"");
        //pcd = fields[0].substring(1);
        PostcodeF = fields[0].replaceAll("[^A-Za-z0-9]", "");
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
