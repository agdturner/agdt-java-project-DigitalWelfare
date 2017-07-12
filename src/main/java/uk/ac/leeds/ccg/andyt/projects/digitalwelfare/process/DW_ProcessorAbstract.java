/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;

public abstract class DW_ProcessorAbstract extends DW_Object {

    protected transient DW_Postcode_Handler DW_Postcode_Handler; 
    protected transient DW_Files DW_Files;
    protected transient DW_Strings DW_Strings;
    
    public DW_ProcessorAbstract() {
    }

    public DW_ProcessorAbstract(DW_Environment env) {
        super(env);
        this.DW_Postcode_Handler = env.getDW_Postcode_Handler();
        this.DW_Files = env.getDW_Files();
        this.DW_Strings = env.getDW_Strings();
    }

    public ArrayList<Boolean> getArrayListBoolean() {
        ArrayList<Boolean> result;
        result = new ArrayList<Boolean>();
        result.add(true);
        result.add(false);
        return result;
    }
        
    /**
     * Initialises a PrintWriter for pushing output to.
     *
     * @param dir
     * @param filename The name of the file to be initialised for writing to.
     * @return PrintWriter for pushing output to.
     */
    protected PrintWriter init_OutputTextFilePrintWriter(
            File dir,
            String filename) {
        PrintWriter result = null;
        File outputTextFile = new File(
                dir,
                filename);
        try {
            outputTextFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(DW_ProcessorAbstract.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result = new PrintWriter(outputTextFile);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_ProcessorAbstract.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * @TODO Adapt to use the ONSPD for a particular time for the postcode look up returned.
     * @param env
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @param year If year = 2011 returns 2011 census code. If year = 2001
     * returns 2001 census code.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
//    public TreeMap<String, String> getLookupFromPostcodeToLevelCode(
//            DW_Environment env,
//            String level,
//            int year) {
//        TreeMap<String, String> result;
//        String outputFilename;
//        outputFilename = "PostcodeTo" + level + year
//                + "LookUp_TreeMap_String_Strings" + DW_Strings.sBinaryFileExtension;
//        File outFile = new File(
//                DW_Files.getGeneratedONSPDDir(),
//                outputFilename);
//        if (!outFile.exists()) {
//            File inputONSPDDir = new File(
//                    DW_Files.getInputONSPDDir(),
//                    "ONSPD_NOV_2013/Data");
//            File tONSPD_NOV_2013DataFile = new File(
//                    inputONSPDDir,
//                    "ONSPD_NOV_2013_UK.csv");
//            result = initLookupFromPostcodeToCensusCodes(
//                    tONSPD_NOV_2013DataFile,
//                    outFile,
//                    level,
//                    year);
//        } else {
//            Object o = Generic_StaticIO.readObject(outFile);
//            result = (TreeMap<String, String>) o;
//        }
//        return result;
//    }

    /**
     * @TODO Adapt to use the ONSPD for a particular time for the postcode look up returned.
     * @param env
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @param year If year = 2011 returns 2011 census code. If year = 2001
     * returns 2001 census code.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
    public TreeMap<String, String> getLookupFromPostcodeToLevelCode(
            DW_Environment env,
            String level,
            String YM3) {
        String YM3Nearest;
        YM3Nearest = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        String[] YM3NearestSplit;
        YM3NearestSplit = YM3Nearest.split("_");
        int year = Integer.valueOf(YM3NearestSplit[0]);
        String month = YM3NearestSplit[1];
        TreeMap<String, String> result;
        String outputFilename;
        outputFilename = "PostcodeTo" + level + "_" + YM3Nearest
                + "_LookUp_TreeMap_String_Strings" + DW_Strings.sBinaryFileExtension;
        File dir;
        dir = new File(
                DW_Files.getGeneratedONSPDDir(),
                YM3Nearest);
        File outfile = new File(
                dir,
                outputFilename);
        if (!outfile.exists()) {
            dir.mkdirs();
            String YM3NearestFormat;
            YM3NearestFormat = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
            File infile = DW_Files.getInputONSPDFile(YM3Nearest);
            result = initLookupFromPostcodeToCensusCodes(
                    infile,
                    outfile,
                    level,
                    YM3NearestFormat);
        } else {
            Object o = Generic_StaticIO.readObject(outfile);
            result = (TreeMap<String, String>) o;
        }
        return result;
    }
    
    /**
     * Keys are postcodes; values are census codes:-----------------------------
     */
    private TreeMap<String, String> initLookupFromPostcodeToCensusCodes(
            File infile,
            File outFile,
            String level,
            String YM3NearestFormat) {
        TreeMap<String, String> result;
        result = DW_Postcode_Handler.getPostcodeUnitCensusCodeLookup(
                infile,
                outFile,
                level,
                YM3NearestFormat);
        return result;
    }

    public String formatPostcodeDistrict(String postcodeDistrict) {
        String formattedPostcode = formatOddPostcodes(postcodeDistrict);
        if (this.getExpectedPostcodes().contains(formattedPostcode)) {
            return formattedPostcode;
        } else {
            return "NotLS";
            //            if (formattedPostcode.equalsIgnoreCase(postcodeDistrict)) {
            //                return "NotLS";
            //            }
        }
    }

    protected String formatOddPostcodes(String postcodeDistrict) {
        if (this.getOddPostcodes().contains(postcodeDistrict)) {
            if (postcodeDistrict.equalsIgnoreCase("")) {
                return "NotRecorded";
            }
            if (postcodeDistrict.equalsIgnoreCase("L1")) {
                return "LS1";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS06")) {
                return "LS6";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS08")) {
                return "LS8";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS09")) {
                return "LS9";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS104UH")) {
                return "LS10";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS83")) {
                return "LS8";
            }
            if (postcodeDistrict.equalsIgnoreCase("LS97")) {
                return "LS9";
            }
        }
        return postcodeDistrict;
    }
    
    /**
     * For storing the expected postcodes for analysis
     */
    private TreeSet<String> expectedPostcodes;

    public TreeSet<String> getExpectedPostcodes() {
        if (expectedPostcodes == null) {
            init_ExpectedPostcodes();
        }
        return expectedPostcodes;
    }

    /**
     * LS1 to LS29 other For the time being all the Bradford and Wakefield
     * postcodes have been left out. The ones known about are as follows:
     * (BD1,BD3,BD4,BD10,BD11,BD16 WF2,WF3,WF10,WF12,WF17)
     *
     * @return
     */
    public TreeSet<String> init_ExpectedPostcodes() {
        expectedPostcodes = new TreeSet<String>();
        for (int i = 1; i < 30; i++) {
            String postcode = "LS" + i;
            expectedPostcodes.add(postcode);
        }
        expectedPostcodes.add("unknown");
        expectedPostcodes.add("unknown_butPreviousClaimant");
        return expectedPostcodes;
    }

    /**
     * For storing a set of oddPostcodes that are identified and processed
     */
    private HashSet<String> oddPostcodes;

    public void initOddPostcodes() {
        oddPostcodes = new HashSet<String>();
        oddPostcodes.add("");
        oddPostcodes.add("DL8");
        oddPostcodes.add("G71");
        oddPostcodes.add("L1");
        oddPostcodes.add("LS06");
        oddPostcodes.add("LS08");
        oddPostcodes.add("LS09");
        oddPostcodes.add("LS104UH");
        oddPostcodes.add("LS83");
        oddPostcodes.add("LS97");
        oddPostcodes.add("TF9");
        oddPostcodes.add("TW5");
    }

    public HashSet<String> getOddPostcodes() {
        if (oddPostcodes == null) {
            initOddPostcodes();
        }
        return oddPostcodes;
    }

    public TreeMap<String, Deprivation_DataRecord> getDeprivation_Data() {
        File depravationDir = new File(env.getDW_Files().getInputCensus2011AttributeDataDir("LSOA"), "England/Deprivation");
        String deprivationFilename = "1871524.csv";
        Deprivation_DataHandler aDeprivation_DataHandler;
        aDeprivation_DataHandler = new Deprivation_DataHandler();
        TreeMap<String, Deprivation_DataRecord> tDeprivationData;
        tDeprivationData = aDeprivation_DataHandler.loadInputData(depravationDir, deprivationFilename);
        System.out.println(tDeprivationData.firstKey());
        System.out.println(tDeprivationData.firstEntry().getValue());
        Iterator<String> ite;
        double minIMDScore = Double.MAX_VALUE;
        double maxIMDScore = Double.MIN_VALUE;
        double sumIMDScore = 0;
        ite = tDeprivationData.keySet().iterator();
        while (ite.hasNext()) {
            String SOACode = ite.next();
            Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
            double aIMDScore = Double.parseDouble(aDeprivation_DataRecord.getIMDScore());
            minIMDScore = Math.min(minIMDScore, aIMDScore);
            maxIMDScore = Math.max(maxIMDScore, aIMDScore);
            sumIMDScore += aIMDScore;
        }
        double meanIMDScore = sumIMDScore / (double) tDeprivationData.size();
        System.out.println("minIMDScore " + minIMDScore);
        System.out.println("maxIMDScore " + maxIMDScore);
        System.out.println("msumIMDScore " + sumIMDScore);
        System.out.println("meanIMDScore " + meanIMDScore);
        return tDeprivationData;
    }

}
