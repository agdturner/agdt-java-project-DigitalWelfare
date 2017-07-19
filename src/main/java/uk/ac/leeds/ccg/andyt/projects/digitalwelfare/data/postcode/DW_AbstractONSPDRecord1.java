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
public abstract class DW_AbstractONSPDRecord1 extends DW_AbstractONSPDRecord {

    protected String oa01;
    protected String casward;
    protected String lsoa01;
    protected String msoa01;
    protected String ur01ind;
    protected String oac01;
    protected String oldpct;
    protected String oa11;
    protected String lsoa11;
    protected String msoa11;
    
    protected DW_AbstractONSPDRecord1() {}

    // 2012_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11
    // 2013_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11
    // 2013_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg
    // 2013_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind
    // 2014_NOV pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11
    // 2015_MAY pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long
    // 2015_AUG pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2
    // 2016_FEB pcd,pcd2,pcds,dointr,doterm,oscty,oslaua,osward,usertype,oseast1m,osnrth1m,osgrdind,oshlthau,hro,ctry,gor,streg,pcon,eer,teclec,ttwa,pct,nuts,psed,cened,edind,oshaprev,lea,oldha,wardc91,wardo91,ward98,statsward,oa01,casward,park,lsoa01,msoa01,ur01ind,oac01,oldpct,oa11,lsoa11,msoa11,parish,wz11,ccg,bua11,buasd11,ru11ind,oac11,lat,long,lep1,lep2,pfa,imd
    
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
    public DW_AbstractONSPDRecord1(DW_Environment env) {
        super(env);
        oa01 = "";
        casward = "";
        park = "";
        lsoa01 = "";
        msoa01 = "";
        ur01ind = "";
        oac01 = "";
        oldpct = "";
        oa11 = "";
        lsoa11 = "";
        msoa11 = "";
    }

    public String getOa01() {
        return oa01;
    }

    public String getCasward() {
        return casward;
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

}
