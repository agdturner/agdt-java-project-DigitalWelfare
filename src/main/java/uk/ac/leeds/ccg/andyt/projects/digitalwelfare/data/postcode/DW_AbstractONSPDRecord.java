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
public abstract class DW_AbstractONSPDRecord extends DW_Object {

    protected String PostcodeF;
    protected String pcd;
    protected String pcd2;
    protected String pcds;
    protected int dointr;
    protected int doterm;
    protected String oscty;
    protected String oslaua;
    protected String osward;
    protected String usertype;
    protected int oseast1m;
    protected int osnrth1m;
    protected int osgrdind;
    protected String oshlthau;
    protected String hro;
    protected String ctry;
    protected String gor;
    protected String streg;
    protected String pcon;
    protected String eer;
    protected String teclec;
    protected String ttwa;
    protected String pct;
    protected String nuts;
    protected String psed;
    protected String cened;
    protected String edind;
    protected String oshaprev;
    protected String lea;
    protected String oldha;
    protected String wardc91;
    protected String wardo91;
    protected String ward98;
    protected String statsward;
    protected String park;
        protected String casward;



    protected DW_AbstractONSPDRecord() {}
    // 2008_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2008_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2011_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2012_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,GENIND,PAFIND,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,ADDRCT,DPCT,MOCT,SMLBUSCT,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,OACODE,OAIND,oa01,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2012_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11
    // 2013_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2015_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long
    // 2015_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2016_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2008_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","","QA","","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2008_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","","QA","","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2008_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2008_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2009_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2009_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2009_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2009_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2010_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2010_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2010_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2010_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
    // 2011_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,pafind,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
    // 2011_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003"," ","S99999999","0","S14000002","S15000001","","","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","","","","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9"," ","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2011_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2011_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2012_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2012_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2012_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
    // 2012_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","",""
    // 2013_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999"
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999","S03000012"
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2013_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","","","","S99999999","S99999999","S03000012","S99999999","S99999999",""
    // 2013_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2013_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","","","S99999999","S99999999","S03000012","S99999999","S99999999","3"
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2014_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3"
    // 2015_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2015_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3"
    // 2015_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long
    // 2015_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851
    // 2015_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2015_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999"
    // 2015_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2015_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999"
    // 2016_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_FEB "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_MAY "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_AUG "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S99999999","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6002
    // 2016_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    // 2016_NOV "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000020","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000047","S03000012","S31000935","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","01C30","S99999999","S01000011","S02000007","6","3C2","X98","S00090303","S01006514","S02001237","S99999999","S34002990","S03000012","S99999999","S99999999","3","1C3",57.101474,-2.242851,"S99999999","S99999999","S23000009",6808
/*
There are currently 5 Westminster Parliamentary Constituencies in Leeds
E14000777       Leeds Central
E14000778       Leeds East
E14000779       Leeds North East
E14000780       Leeds North West
E14000781       Leeds West
     */
 /*
There are currently 33 Wards in Leeds:
E05001411       00DAGL  Adel and Wharfedale
E05001412       00DAGM  Alwoodley
E05001413       00DAGN  Ardsley and Robin Hood
E05001414       00DAGP  Armley
E05001415       00DAGQ  Beeston and Holbeck
E05001416       00DAGR  Bramley and Stanningley
E05001417       00DAGS  Burmantofts and Richmond Hill
E05001418       00DAGT  Calverley and Farsley
E05001419       00DAGU  Chapel Allerton
E05001420       00DAGW  City and Hunslet
E05001421       00DAGX  Cross Gates and Whinmoor
E05001422       00DAGY  Farnley and Wortley
E05001423       00DAGZ  Garforth and Swillington
E05001424       00DAHA  Gipton and Harehills
E05001425       00DAHB  Guiseley and Rawdon
E05001426       00DAHC  Harewood
E05001427       00DAHD  Headingley
E05001428       00DAHE  Horsforth
E05001429       00DAHF  Hyde Park and Woodhouse
E05001430       00DAHG  Killingbeck and Seacroft
E05001431       00DAHH  Kippax and Methley
E05001432       00DAHJ  Kirkstall
E05001433       00DAHK  Middleton Park
E05001434       00DAHL  Moortown
E05001435       00DAHM  Morley North
E05001436       00DAHN  Morley South
E05001437       00DAHP  Otley and Yeadon
E05001438       00DAHQ  Pudsey
E05001439       00DAHR  Rothwell
E05001440       00DAHS  Roundhay
E05001441       00DAHT  Temple Newsam
E05001442       00DAHU  Weetwood
E05001443       00DAHW  Wetherby
     */
    public DW_AbstractONSPDRecord(DW_Environment env) {
        this.Env = env;
        PostcodeF = "";
        pcd = "";
        pcd2 = "";
        pcds = "";
        dointr = 0;
        doterm = 0;
        oscty = "";
        oslaua = "";
        osward = "";
        usertype = "";
        oseast1m = 0;
        osnrth1m = 0;
        osgrdind = 0;
        oshlthau = "";
        hro = "";
        ctry = "";
        gor = "";
        streg = "";
        pcon = "";
        eer = "";
        teclec = "";
        ttwa = "";
        pct = "";
        nuts = "";
        psed = "";
        cened = "";
        edind = "";
        oshaprev = "";
        lea = "";
        oldha = "";
        wardc91 = "";
        wardo91 = "";
        ward98 = "";
        statsward = "";
//        oa01 = "";
//        casward = "";
        park = "";
//        lsoa01 = "";
//        msoa01 = "";
//        ur01ind = "";
//        oac01 = "";
//        oldpct = "";
//        oa11 = "";
//        lsoa11 = "";
//        msoa11 = "";
//        parish = "";
//        wz11 = "";
//        ccg = "";
//        bua11 = "";
//        buasd11 = "";
//        ru11ind = "";
//        oac11 = "";
//        lat = "";
//        lon = "";
//        lep1 = "";
//        lep2 = "";
//        pfa = "";
//        imd = "";
    }

    public String getPostcodeF() {
        return PostcodeF;
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

    public String getPark() {
        return park;
    }
    
    public String getCasward() {
        return casward;
    }

    protected final int initPart1(String[] fields) {
        /*
         * 11
         * pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,
         * "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193",
         */
        int n = 0;
        pcd = fields[n].substring(1);
        n++;
        PostcodeF = Env.getPostcode_Handler().formatPostcode(pcd);
        pcd2 = fields[n];
        n++;
        pcds = fields[n];
        n++;
        if (fields[n].isEmpty()) {
            dointr = -1;
        } else {
            dointr = Integer.valueOf(fields[n]);
        }
        n++;
        if (fields[n].isEmpty()) {
            doterm = -1;
        } else {
            doterm = Integer.valueOf(fields[n]);
        }
        n++;
        oscty = fields[n];
        n++;
        oslaua = fields[n];
        n++;
        osward = fields[n];
        n++;
        usertype = fields[n];
        n++;
        if (fields[n].isEmpty()) {
            oseast1m = -1;
        } else {
            oseast1m = Integer.valueOf(fields[n]);
        }
        n++;
        if (fields[n].isEmpty()) {
            osnrth1m = -1;
        } else {
            osnrth1m = Integer.valueOf(fields[n]);
        }
        n++;
        return n;
    }

    protected final int initPart2(int n, String[] fields) {
        /*
         * 4
         * osgrdind,oshlthau,hro,ctry,
         * "1","SN9","S00","179",
         */
        osgrdind = Integer.valueOf(fields[n]);
        n++;
        oshlthau = fields[n];
        n++;
        hro = fields[n];
        n++;
        ctry = fields[n];
        n++;
        return n;
    }

    protected final int initPart3(int n, String[] fields) {
        /* 9
         * pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,
         * "802","11","S08","248","012","UKM1001","99ZZ0099","ZZ0099","9",
         */
        pcon = fields[n];
        n++;
        eer = fields[n];
        n++;
        teclec = fields[n];
        n++;
        ttwa = fields[n];
        n++;
        pct = fields[n];
        n++;
        nuts = fields[n];
        n++;
        psed = fields[n];
        n++;
        cened = fields[n];
        n++;
        edind = fields[n];
        n++;
        return n;
    }

    protected final int initPart4(int n, String[] fields) {
        /* 21
         * oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oacode,oaind,casward,park,soa1,dzone1,soa2,urindew,urindsc,urindni,dzone2,soa1ni,oac,oldpct
         * "2","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","60QA001270","7","01C31","99","Z99999999","S01000011","Z99999999","9","6","Z","S02000007","99ZZ99Z9","4B3","X98"
         */
        oshaprev = fields[n];
        n++;
        lea = fields[n];
        n++;
        oldha = fields[n];
        n++;
        wardc91 = fields[n];
        n++;
        wardo91 = fields[n];
        n++;
        ward98 = fields[n];
        n++;
        statsward = fields[n];
        n++;
        return n;
    }

    protected final int initPart6(int n, String[] fields) {
        casward = fields[n];
        n++;
        park = fields[n];
        n++;
        return n;
    }

}
