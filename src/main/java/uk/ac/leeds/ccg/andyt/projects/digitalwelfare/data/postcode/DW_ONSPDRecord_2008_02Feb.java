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
public class DW_ONSPDRecord_2008_02Feb extends DW_AbstractONSPDRecord2 {

    protected String oacode;
    protected String oaind;

    protected DW_ONSPDRecord_2008_02Feb(){}
    
    /*
     * NSPDF_FEB_2008_UK_1M.csv 
     * pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
     * "AB1 0AB","AB1  0AB","AB1 0AB","198001","199606","00","QA","MJ","0","385177","0801314","1","SN9","S00","179"," "," ","X","0","802","11","S08","248","012","UKM1001","99ZZ0099","ZZ0099","9","2","3","2","1","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","60QA001270","7","01C31","99","Z99999999","S01000011","Z99999999","9","6","Z","S02000007","99ZZ99Z9","4B3","X98"
     */
    /**
     * @param env
     * @param line
     */
    public DW_ONSPDRecord_2008_02Feb(DW_Environment env, String line) {
        this.Env = env;
        String[] fields = line.split("\",\"");
        /*
         * 2008_FEB 11
         * pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,
         * "AB1 0AB","AB1 0AB","AB1 0AB","198001","199606","00","QA","MJ","0","385177","0801314",
         */
        int n;
        n = initPart1(fields);
        n = initPart2(n, fields);
        /* GENIND,pafind,gor,streg,
         * " "," ","X","0",
         */
        genind = fields[n];
        n++;
        pafind = fields[n];
        n++;
        gor = fields[n];
        n++;
        streg = fields[n];
        n++;
        n = initPart3(n, fields);
        /*
         * ADDRCT,DPCT,MOCT,SMLBUSCT,
         * "3","2","1","SN9",
         */
        addrct = fields[n];
        n++;
        dpct = fields[n];
        n++;
        moct = fields[n];
        n++;
        smlbusct = fields[n];
        n++;
        n = initPart4(n, fields);
        n = initPart5(n, fields);
        n = initPart6(n, fields);
        n = initPart7(n, fields);
    }
    
    protected final int initPart5(int n, String[] fields) {
        oacode = fields[n];
        n++;
        oaind = fields[n];
        n++;
        return n;
    }
        
    protected final int initPart7(int n, String[] fields) {
        soa1 = fields[n];
        n++;
        dzone1 = fields[n];
        n++;
        soa2 = fields[n];
        n++;
        urindew = fields[n];
        n++;
        urindsc = fields[n];
        n++;
        urindni = fields[n];
        n++;
        dzone2 = fields[n];
        n++;
        soa1ni = fields[n];
        n++;
        oac = fields[n];
        n++;
        oldpct = fields[n];
        n++;
        return n;
    }
    
    public String getOacode() {
        return oacode;
    }

    public String getOaind() {
        return oaind;
    }
}
