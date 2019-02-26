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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;

public abstract class DW_ProcessorAbstract extends DW_Object {

    protected transient ONSPD_Handler Postcode_Handler;
    private transient ArrayList<Boolean> b;

    public DW_ProcessorAbstract(DW_Environment e) {
        super(e);
        this.Postcode_Handler = e.getONSPD_Handler();
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
    protected PrintWriter init_OutputTextFilePrintWriter(File dir,
            String filename) {
        PrintWriter r = null;
        File f = new File(dir, filename);
        try {
            f.createNewFile();
            r = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_ProcessorAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DW_ProcessorAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    /**
     * @param YM3
     * @param CensusYear
     * @TODO Adapt to use the ONSPD for a particular time for the postcode look
     * up returned.
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
    public TreeMap<String, String> getClaimPostcodeF_To_LevelCode_Map(
            String level, int CensusYear, ONSPD_YM3 YM3) {
        ONSPD_YM3 YM3Nearest;
        YM3Nearest = Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        TreeMap<String, String> r;
        String outputFilename;
        File dir;
        outputFilename = "PostcodeTo" + level + "_" + YM3Nearest
                + "_LookUp_TreeMap_String_Strings"
                + DW_Strings.sBinaryFileExtension;
        dir = new File(files.getGeneratedONSPDDir(), YM3Nearest.toString());
//        }
        File outfile = new File(dir, outputFilename);
        if (!outfile.exists()) {
            dir.mkdirs();
            File infile = env.SHBE_Env.oe.Files.getInputONSPDFile(YM3Nearest);
            r = initLookupFromPostcodeToCensusCodes(infile, outfile, level,
                    CensusYear, YM3Nearest);
        } else {
            r = (TreeMap<String, String>) Generic_IO.readObject(outfile);
        }
        return r;
    }

    /**
     * Keys are postcodes; values are census codes:
     *
     * @param infile
     * @param outFile
     * @param level
     * @param CensusYear // Expecting 2001 or 2011.
     * @param YM3 // Expecting the nearest YM3 that is wanted.
     * @return
     */
    private TreeMap<String, String> initLookupFromPostcodeToCensusCodes(
            File infile, File outFile, String level, int CensusYear,
            ONSPD_YM3 YM3) {
        TreeMap<String, String> r;
        r = Postcode_Handler.getPostcodeUnitCensusCodeLookup(infile, outFile,
                level, CensusYear, YM3);
        return r;
    }

    public String formatPostcodeDistrict(String postcodeDistrict) {
        String formattedPostcode = formatOddPostcodes(postcodeDistrict);
        if (getExpectedPostcodes().contains(formattedPostcode)) {
            return formattedPostcode;
        } else {
            return "NotLS";
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
        File depravationDir = new File(env.files.getInputCensus2011AttributeDataDir("LSOA"), "England/Deprivation");
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

    

}
