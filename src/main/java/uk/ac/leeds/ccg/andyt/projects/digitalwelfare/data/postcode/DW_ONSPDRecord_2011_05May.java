/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

/**
 *
 * @author geoagdt
 */
public class DW_ONSPDRecord_2011_05May extends DW_AbstractONSPDRecord2 {

    protected String oacode;
    protected String oaind;
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
    
    public DW_ONSPDRecord_2011_05May(String line) {
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
        pafind = fields[15];
        gor = fields[16];
        streg = fields[17];
        pcon = fields[18];
        eer = fields[19];
        teclec = fields[20];
        ttwa = fields[21];
        pct = fields[22];
        nuts = fields[23];
        psed = fields[24];
        cened = fields[25];
        edind = fields[26];
        oshaprev = fields[27];
        lea = fields[28];
        oldha = fields[29];
        wardc91 = fields[30];
        wardo91 = fields[31];
        ward98 = fields[32];
        statsward = fields[33];
        oacode = fields[34];
        oaind = fields[35];
        casward = fields[36];
        park = fields[37];
        soa1 = fields[38];
        dzone1 = fields[39];
        soa2 = fields[40];
        urindew = fields[41];
        urindsc = fields[42];
        urindni = fields[43];
        dzone2 = fields[44];
        soa1ni = fields[45];
        oac = fields[46];
        oldpct = fields[47];
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
