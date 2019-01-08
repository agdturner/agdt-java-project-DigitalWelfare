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
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataHandler;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataRecord;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;

public abstract class DW_ProcessorAbstract extends DW_Object {

    protected transient ONSPD_Postcode_Handler Postcode_Handler;
    protected transient DW_Files Files;
    protected transient DW_Strings Strings;

    private transient ArrayList<Boolean> b;

    public DW_ProcessorAbstract() {
    }

    public DW_ProcessorAbstract(DW_Environment env) {
        super(env);
        this.Postcode_Handler = env.getPostcode_Handler();
        this.Files = env.getFiles();
        this.Strings = env.getStrings();
    }

    public ArrayList<Boolean> getArrayListBoolean() {
        if (b == null) {
            b = new ArrayList<>();
            b.add(true);
            b.add(false);
        }
        return b;
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
     * @TODO Adapt to use the ONSPD for a particular time for the postcode look
     * up returned.
     * @param env
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @param year If year = 2011 returns 2011 census code. If year = 2001
     * returns 2001 census code.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
//    public TreeMap<String, String> getClaimPostcodeF_To_LevelCode_Map(
//            DW_Environment Env,
//            String level,
//            int year) {
//        TreeMap<String, String> result;
//        String outputFilename;
//        outputFilename = "PostcodeTo" + level + year
//                + "LookUp_TreeMap_String_Strings" + Strings.sBinaryFileExtension;
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
//            Object o = Generic_IO.readObject(outFile);
//            result = (TreeMap<String, String>) o;
//        }
//        return result;
//    }
    /**
     * @param YM3
     * @param CensusYear
     * @TODO Adapt to use the ONSPD for a particular time for the postcode look
     * up returned.
     * @param env
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
    public TreeMap<String, String> getClaimPostcodeF_To_LevelCode_Map(
            DW_Environment env,
            String level,
            int CensusYear,
            ONSPD_YM3 YM3) {
        ONSPD_YM3 YM3Nearest;
        YM3Nearest = Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        TreeMap<String, String> result;
        String outputFilename;
        File dir;
//        String[] YM3NearestSplit;
//        YM3NearestSplit = YM3Nearest.split("_");
//        HashSet CensusAreaAggregations;
//        CensusAreaAggregations = Strings.getCensusAreaAggregations();
//        if (CensusAreaAggregations.contains(level)) {
//            int year = Integer.valueOf(YM3NearestSplit[0]);
//            int month = Integer.valueOf(Generic_Time.getMonthNumber(YM3NearestSplit[1]));
//            String yearString;
//            if (year < 2011 || (year == 2011 && month < 4)) {
//                yearString = "2001";
//            } else {
//                yearString = "2011";
//            }
//            outputFilename = "PostcodeTo" + level + "_" + yearString
//                    + "_LookUp_TreeMap_String_Strings" + Strings.sBinaryFileExtension;
//            dir = new File(
//                    DW_Files.getGeneratedONSPDDir(),
//                    yearString);
//        } else {
        //String month = YM3NearestSplit[1];
        outputFilename = "PostcodeTo" + level + "_" + YM3Nearest
                + "_LookUp_TreeMap_String_Strings" + Strings.sBinaryFileExtension;
                //+ "_LookUp_TreeMap_ONSPD_YM3__Strings" + Strings.sBinaryFileExtension;
        dir = new File(
                Files.getGeneratedONSPDDir(),
                YM3Nearest.toString());
//        }
        File outfile = new File(
                dir,
                outputFilename);
        if (!outfile.exists()) {
            dir.mkdirs();
            File infile = Files.ONSPD_Files.getInputONSPDFile(YM3Nearest);
            result = initLookupFromPostcodeToCensusCodes(
                    infile,
                    outfile,
                    level,
                    CensusYear,
                    YM3Nearest);
        } else {
            Object o = Generic_IO.readObject(outfile);
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
            int CensusYear, // must be 2001 or 2011.
            ONSPD_YM3 YM3NearestFormat) {
        TreeMap<String, String> result;
        result = Postcode_Handler.getPostcodeUnitCensusCodeLookup(
                infile,
                outFile,
                level,
                CensusYear,
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
        expectedPostcodes = new TreeSet<>();
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
        oddPostcodes = new HashSet<>();
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

    public TreeMap<String, Census_DeprivationDataRecord> getDeprivation_Data() {
        File depravationDir = new File(Env.getFiles().getInputCensus2011AttributeDataDir("LSOA"), "England/Deprivation");
        String deprivationFilename = "1871524.csv";
        Census_DeprivationDataHandler aDeprivation_DataHandler;
        aDeprivation_DataHandler = new Census_DeprivationDataHandler();
        TreeMap<String, Census_DeprivationDataRecord> tDeprivationData;
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
            Census_DeprivationDataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
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

    /**
     * Initialises Env logging PrintWriters and returns the directory in which
 logs are written. The directory is in an archive structure where the
     * number of directories or files in the archive (which is a growing
     * structure) is range.
     *
     * @param DEBUG_Level The debugging level - used to control how much is
     * written to the logs about the process. The following DEBUG_Levels are
     * defined: DEBUG_Level_FINEST = 0, DEBUG_Level_FINE = 1, DEBUG_Level_NORMAL
     * = 2.
     * @param processName The name of the process used for the directory inside
     * DW_Files.getOutputSHBELogsDir().
     * @param range The number of directories or files in the archive where the
     * logs are stored.
     * @return
     */
    protected File initLogs(int DEBUG_Level, String processName, int range) {
        Env.DEBUG_Level = DEBUG_Level;
        File dir;
        dir = new File(Files.getOutputSHBELogsDir(), processName);
        if (dir.isDirectory()) {
            dir = Generic_IO.addToArchive(dir, 100);
        } else {
            dir = Generic_IO.initialiseArchive(dir, 100);
        }
        dir.mkdirs();
        File fO;
        fO = new File(dir, "Out.txt");
        PrintWriter PrintWriterO;
        PrintWriterO = null;
        try {
            PrintWriterO = new PrintWriter(fO);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        Env.setPrintWriterOut(PrintWriterO);
        File fE;
        fE = new File(dir, "Err.txt");
        PrintWriter PrintWriterE;
        PrintWriterE = null;
        try {
            PrintWriterE = new PrintWriter(fE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_Processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        Env.setPrintWriterErr(PrintWriterE);
        Env.log("<" + processName + ">");
        Env.log("Log Directory " + dir.toString());
        Env.log("Log Output file " + fO.toString());
        Env.log("Log Error file " + fE.toString());
        Env.log("DEBUG_Level = " + Env.DEBUG_Level);
        Env.log("env.DEBUG_Level_FINEST = " + Env.DEBUG_Level_FINEST);
        Env.log("env.DEBUG_Level_FINE = " + Env.DEBUG_Level_FINE);
        Env.log("env.DEBUG_Level_NORMAL = " + Env.DEBUG_Level_NORMAL);
        return dir;
    }

    /**
     * Closes Env logging PrintWriters.
     *
     * @param processName
     */
    protected void closeLogs(String processName) {
        Env.log("</" + processName + ">");
        Env.getPrintWriterOut().close();
        Env.getPrintWriterErr().close();
    }

}
