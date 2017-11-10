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
public class DW_ONSPDRecord_2012_08Nov extends DW_ONSPDRecord_2011_05May {

    protected String oa01;
       
    /*
     * "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","00QA36","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
     */
    /**
     * 
     * @param env
     * @param line 
     */
    public DW_ONSPDRecord_2012_08Nov(DW_Environment env, String line) {
        this.Env = env;
        String[] fields = line.split("\",\"");
        int n;
        n = initPart1(fields);
        n = initPart2(n, fields);
        genind = fields[n];
        n ++;
        pafind = fields[n];
        n ++;
        gor = fields[n];
        n ++;
        streg = fields[n];
        n ++;
        n = initPart3(n, fields);
        addrct = fields[n];
        n ++;
        dpct = fields[n];
        n ++;
        moct = fields[n];
        n ++;
        smlbusct = fields[n];
        n ++;
        n = initPart4(n, fields);
        oa01 = fields[n];
        n ++;
        n = initPart6(n, fields);
    }

}
