/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

/**
 *
 * @author geoagdt
 */
public class ONSPDRecord_Aug_2013_UK_0 {

    private final String pcd;
    private final String pcd2;
    private final String pcds;
    private final int dointr;
    private final int doterm;
    private final String oscty;
    private final String oslaua;
    private final String osward;
    private final String usertype;
    private final int oseast1m;
    private final int osnrth1m;
    private final int osgrdind;
    private final String oshlthau;
    private final String hro;
    private final String ctry;
    private final String gor;
    private final String streg;
    private final String pcon;
    private final String eer;
    private final String teclec;
    private final String ttwa;
    private final String pct;
    private final String nuts;
    private final String psed;
    private final String cened;
    private final String edind;
    private final String oshaprev;
    private final String lea;
    private final String oldha;
    private final String wardc91;
    private final String wardo91;
    private final String ward98;
    private final String statsward;
    private final String oa01;
    private final String casward;
    private final String park;
    private final String lsoa01;
    private final String msoa01;
    private final String ur01ind;
    private final String oac01;
    private final String oldpct;
    private final String oa11;
    private final String lsoa11;
    private final String msoa11;
    private final String parish;
    private final String wz11;
    private final String ccg;
    private final String bua11;
    private final String buasd11;
    private final String ru11ind;

    public ONSPDRecord_Aug_2013_UK_0(String line) {
        String[] fields = line.split("\",\"");
        pcd = fields[0].substring(1);
        pcd2 = fields[1];
        pcds = fields[2];
        if (fields[3].isEmpty()) {
            dointr = -1;
        } else {
            dointr = Integer.valueOf(fields[3]);
        }
        if (fields[4].isEmpty()) {
            doterm = -1;
        } else {
            doterm = Integer.valueOf(fields[4]);
        }
        oscty = fields[5];
        oslaua = fields[6];
        osward = fields[7];
        usertype = fields[8];
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
        osgrdind = Integer.valueOf(fields[11]);
        oshlthau = fields[12];
        hro = fields[13];
        ctry = fields[14];
        gor = fields[15];
        streg = fields[16];
        pcon = fields[17];
        eer = fields[18];
        teclec = fields[19];
        ttwa = fields[20];
        pct = fields[21];
        nuts = fields[22];
        psed = fields[23];
        cened = fields[24];
        edind = fields[25];
        oshaprev = fields[26];
        lea = fields[27];
        oldha = fields[28];
        wardc91 = fields[29];
        wardo91 = fields[30];
        ward98 = fields[31];
        statsward = fields[32];
        oa01 = fields[33];
        casward = fields[34];
        park = fields[35];
        lsoa01 = fields[36];
        msoa01 = fields[37];
        ur01ind = fields[38];
        oac01 = fields[39];
        oldpct = fields[40];
        oa11 = fields[41];
        lsoa11 = fields[42];
        msoa11 = fields[43];
        parish = fields[44];
        wz11 = fields[45];
        ccg = fields[46];
        bua11 = fields[47];
        buasd11 = fields[48];
        ru11ind = fields[49];
    }

    public String getPcd() {
        return pcd;
    }

    public String getPcd2() {
        return pcd2;
    }

    public String getPcds() {
        return pcds;
    }

    public int getDointr() {
        return dointr;
    }

    public int getDoterm() {
        return doterm;
    }

    public String getOscty() {
        return oscty;
    }

    public String getOslaua() {
        return oslaua;
    }

    public String getOsward() {
        return osward;
    }

    public String getUsertype() {
        return usertype;
    }

    public int getOseast1m() {
        return oseast1m;
    }

    public int getOsnrth1m() {
        return osnrth1m;
    }

    public int getOsgrdind() {
        return osgrdind;
    }

    public String getOshlthau() {
        return oshlthau;
    }

    public String getHro() {
        return hro;
    }

    public String getCtry() {
        return ctry;
    }

    public String getGor() {
        return gor;
    }

    public String getStreg() {
        return streg;
    }

    public String getPcon() {
        return pcon;
    }

    public String getEer() {
        return eer;
    }

    public String getTeclec() {
        return teclec;
    }

    public String getTtwa() {
        return ttwa;
    }

    public String getPct() {
        return pct;
    }

    public String getNuts() {
        return nuts;
    }

    public String getPsed() {
        return psed;
    }

    public String getCened() {
        return cened;
    }

    public String getEdind() {
        return edind;
    }

    public String getOshaprev() {
        return oshaprev;
    }

    public String getLea() {
        return lea;
    }

    public String getOldha() {
        return oldha;
    }

    public String getWardc91() {
        return wardc91;
    }

    public String getWardo91() {
        return wardo91;
    }

    public String getWard98() {
        return ward98;
    }

    public String getStatsward() {
        return statsward;
    }

    public String getOa01() {
        return oa01;
    }

    public String getCasward() {
        return casward;
    }

    public String getPark() {
        return park;
    }

    public String getLsoa01() {
        return lsoa01;
    }

    public String getMsoa01() {
        return msoa01;
    }

    public String getUr01ind() {
        return ur01ind;
    }

    public String getOac01() {
        return oac01;
    }

    public String getOldpct() {
        return oldpct;
    }

    public String getOa11() {
        return oa11;
    }

    public String getLsoa11() {
        return lsoa11;
    }

    public String getMsoa11() {
        return msoa11;
    }

    public String getParish() {
        return parish;
    }

    public String getWz11() {
        return wz11;
    }

    public String getCcg() {
        return ccg;
    }

    public String getBua11() {
        return bua11;
    }

    public String getBuasd11() {
        return buasd11;
    }

    public String getRu11ind() {
        return ru11ind;
    }
}
