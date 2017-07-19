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
public class DW_ONSPDRecord_2012_08Aug extends DW_AbstractONSPDRecord2 {

    protected String oacode;
    protected String oaind;
    protected String oa01;
    protected String casward;
                
    // 2008_FEB  8 pcd,pcd2,pcds,dointr,doterm,{Join these for Ward Code OODAFA oscty,oslaua,osward}, 
    //          12 usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,
    //           7 teclec,ttwa,pct,nuts,{1991 Enumeration District: psed,cened},edind,
    //           4 ADDRCT,DPCT,MOCT,SMLBUSCT,
    //           3 oshaprev,lea,oldha,
    //           1 1991 Census and electoral ward code: wardc91,
    //           1 wardo91,
    //           1 1998 Census and electoral ward code: ward98,
    //           1 2005 statistical Ward: statsward,
    //           1 2001 Output Area Code: oacode,
    //           1 oaind,
    //           1 2001 CAS Ward: casward,
    //           1 park,
    //           1 2001 Lower Layer Super Output Area: soa1,
    //           1 dzone1,
    //           1 2001 Middle Layer Super Output Area: soa2,
    //           3 urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2008_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2008_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2011_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2012_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,PAFIND,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,OACODE,OAIND,oa01,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    
    public DW_ONSPDRecord_2012_08Aug(DW_Environment env, String line) {
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
        genind = fields[15];
        pafind = fields[16];
        gor = fields[17];
        streg = fields[18];
        pcon = fields[19];
        eer = fields[20];
        teclec = fields[21];
        ttwa = fields[22];
        pct = fields[23];
        nuts = fields[24];
        psed = fields[25];
        cened = fields[26];
        edind = fields[27];
        addrct = fields[28];
        dpct = fields[29];
        moct = fields[30];
        smlbusct = fields[31];
        oshaprev = fields[32];
        lea = fields[33];
        oldha = fields[34];
        wardc91 = fields[35];
        wardo91 = fields[36];
        ward98 = fields[37];
        statsward = fields[38];
        oacode = fields[39];
        oaind = fields[40];
        oa01 = fields[41];
        casward = fields[42];
        park = fields[43];
        soa1 = fields[44];
        dzone1 = fields[45];
        soa2 = fields[46];
        urindew = fields[47];
        urindsc = fields[48];
        urindni = fields[49];
        dzone2 = fields[50];
        soa1ni = fields[51];
        oac = fields[52];
        oldpct = fields[53];
    }

    public String getOacode() {
        return oacode;
    }

    public String getOaind() {
        return oaind;
    }

    public String getCasward() {
        return casward;
    }
}
