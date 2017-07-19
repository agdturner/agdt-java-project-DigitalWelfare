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
public class DW_ONSPDRecord_2012_11Nov extends DW_AbstractONSPDRecord1 {

    // 2012_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11
    // 2013_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    
    public DW_ONSPDRecord_2012_11Nov(DW_Environment env, String line) {
        this.env = env;
        String[] fields = line.split("\",\"");
        pcd = fields[0].substring(1);
        PostcodeF = env.getDW_Postcode_Handler().formatPostcode(pcd);
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
    }
}
