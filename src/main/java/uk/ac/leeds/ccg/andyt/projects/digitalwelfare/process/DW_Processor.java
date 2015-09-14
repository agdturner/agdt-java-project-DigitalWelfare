/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process;

import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public abstract class DW_Processor {

    /**
     * Returns a transformation of outlet. The first part of outlet (part before
     * the first space) with a upper case first letter and the rest in lower
     * case.
     *
     * @param outlet May not be null.
     * @return
     */
    public static String getCABOutletString(String outlet) {
        String result;
        String outletInUpperCase = outlet.split(" ")[0];
        String outletInUpperCaseFirstLetter;
        outletInUpperCaseFirstLetter = outletInUpperCase.substring(0, 1);
        String outletInUpperCaseNotFirstLetter;
        outletInUpperCaseNotFirstLetter = outletInUpperCase.substring(1);
        String outletInLowerCaseNotFirstLetter;
        outletInLowerCaseNotFirstLetter = Generic_StaticString.getLowerCase(outletInUpperCaseNotFirstLetter);
        result = outletInUpperCaseFirstLetter + outletInLowerCaseNotFirstLetter;
        return result;
    }


//    protected File _DW_directory;
//    protected PrintWriter pw;
//    protected PrintWriter pw2;
    public DW_Processor() {
    }

    public abstract void run();

//    /**
//     * initialises output text files for reporting.
//     *
//     * @param filename
//     */
//    private void init_OutputTextFiles(String filename) {
//        pw = init_OutputTextFilePrintWriter(filename + "1.txt");
//        pw2 = init_OutputTextFilePrintWriter(filename + "2.txt");
//    }
    /**
     * Initialises a PrintWriter for pushing output to.
     *
     * @param dir
     * @param filename The name of the file to be initialised for writing to.
     * @return PrintWriter for pushing output to.
     */
    protected static PrintWriter init_OutputTextFilePrintWriter(
            File dir,
            String filename) {
        PrintWriter result = null;
        File outputTextFile = new File(
                dir,
                filename);
        try {
            outputTextFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(DW_Processor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            result = new PrintWriter(outputTextFile);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_Processor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
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

    /**
     * Initialise a file and to write rectangular csv records to.
     *
     * @param dir
     * @param aSHBEgeneralisation The data to be written out in order of the
     * keys.
     * @param name The first part of the filename.
     * @param var1 The name for column 0
     * @param var2 The name for column 1
     */
    public void createCSV(
            File dir,
            TreeMap<String, Integer> aSHBEgeneralisation,
            String name,
            String var1,
            String var2) {
        PrintWriter printWriter = init_OutputTextFilePrintWriter(
                dir,
                name + ".csv");
        printWriter.println(var1 + "," + var2);
        Iterator<String> ite = aSHBEgeneralisation.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Integer value = aSHBEgeneralisation.get(key);
            printWriter.println(key + "," + value);
        }
        printWriter.close();
    }

    /**
     * Initialise a file and to write rectangular csv records to. In addition to
     * writing out the data in aSHBEgeneralisation,
     * aCouncilRecordgeneralisation, and aRSLRecordgeneralisation some sums and
     * proportions are calculated.
     *
     * @param dir
     * @param aSHBEgeneralisation Data to be written out in order of the keys.
     * @param aCouncilRecordgeneralisation Data to be written out in order of
     * the keys.
     * @param aRSLRecordgeneralisation Data to be written out in order of the
     * keys.
     * @param name The first part of the filename.
     * @param header The header to be written to the file.
     */
    public void createCSV(
            File dir,
            TreeMap<String, Integer> aSHBEgeneralisation,
            TreeMap<String, Integer> aCouncilRecordgeneralisation,
            TreeMap<String, Integer> aRSLRecordgeneralisation,
            String name,
            String header) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                name + ".csv");
        pw.println(header);
        Iterator<String> ite = aSHBEgeneralisation.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Integer value1 = aSHBEgeneralisation.get(key);
            Integer value2;
            if (aCouncilRecordgeneralisation.get(key) == null) {
                value2 = 0;
            } else {
                value2 = aCouncilRecordgeneralisation.get(key);
            }
            Integer value3;
            if (aRSLRecordgeneralisation.get(key) == null) {
                value3 = 0;
            } else {
                value3 = aRSLRecordgeneralisation.get(key);
            }
            Integer value4 = value2 + value3;
            double proportion = ((double) value4 / (double) value1) * 100;
            pw.println(key + "," + value1 + ", " + value2 + ", " + value3 + ", " + value4 + ", " + proportion);
        }
        pw.close();
    }

    /**
     * @param level
     * @return TreeMap<String, String[]> result where:--------------------------
     * Keys are postcodes; values are for level "MSOA" are:---------------------
     * values[0] = rec.getOa01();-----------------------------------------------
     * values[1] = rec.getMsoa01();---------------------------------------------
     * values[2] = rec.getOa11();-----------------------------------------------
     * values[3] = rec.getMsoa11();---------------------------------------------
     * values are for level "LSOA" are:-----------------------------------------
     * values[0] = rec.getOa01();-----------------------------------------------
     * values[1] = rec.getLsoa01();---------------------------------------------
     * values[2] = rec.getOa11();-----------------------------------------------
     * values[3] = rec.getLsoa11();---------------------------------------------
     */
//    @Deprecated
//    public static TreeMap<String, String[]> getLookupFromPostcodeToCensusCodes(
//            String level) {
//        TreeMap<String, String[]> result;
//        File outputDirectory = DW_Files.getGeneratedONSPDDir();
//        String outputFilename = "fail";
//        if (level.equalsIgnoreCase("MSOA")) {
//            outputFilename = "PostcodeLookUp_TreeMap_String_Strings.thisFile";
//        }
//        if (level.equalsIgnoreCase("LSOA")) {
//            outputFilename = "PostcodeToLSOALookUp_TreeMap_String_Strings.thisFile";
//        }
//        if (level.equalsIgnoreCase("OA")) {
//            outputFilename = "PostcodeToLSOALookUp_TreeMap_String_Strings.thisFile";
//        }
//        File outFile = new File(outputDirectory, outputFilename);
//        if (!outFile.exists()) {
//            result = initLookupFromPostcodeToCensusCodes(level);
//        } else {
//            Object o = Generic_StaticIO.readObject(outFile);
//            result = (TreeMap<String, String[]>) o;
//        }
//        return result;
//    }
    /**
     * @param level If level is "OA" returns OutputArea codes. If level is
     * "LSOA" returns Lower-layer Super Output Area codes. If level is "MSOA"
     * returns Middle-layer Super Output Area codes.
     * @param year If year = 2011 returns 2011 census code. If year = 2001
     * returns 2001 census code.
     * @return TreeMap<String, String> result where:--------------------------
     * Keys are postcodes; values are census area codes.
     */
    public static TreeMap<String, String> getLookupFromPostcodeToLevelCode(
            String level,
            int year) {
        TreeMap<String, String> result;
        String outputFilename;
        outputFilename = "PostcodeTo" + level + year
                + "LookUp_TreeMap_String_Strings.thisFile";
        File outFile = new File(
                DW_Files.getGeneratedONSPDDir(),
                outputFilename);
        if (!outFile.exists()) {
            File inputONSPDDir = new File(
                    DW_Files.getInputONSPDDir(),
                    "ONSPD_NOV_2013/Data");
            File tONSPD_NOV_2013DataFile = new File(
                    inputONSPDDir,
                    "ONSPD_NOV_2013_UK.csv");
            result = initLookupFromPostcodeToCensusCodes(
                    tONSPD_NOV_2013DataFile,
                    outFile,
                    level,
                    year);
        } else {
            Object o = Generic_StaticIO.readObject(outFile);
            result = (TreeMap<String, String>) o;
        }
        return result;
    }

//    /**
//     * @return TreeMap<String, String[]> result where:--------------------------
//     * Keys are postcodes; values are for level "MSOA" are:---------------------
//     * values[0] = rec.getOa01();-----------------------------------------------
//     * values[1] = rec.getMsoa01();---------------------------------------------
//     * values[2] = rec.getOa11();-----------------------------------------------
//     * values[3] = rec.getMsoa11();---------------------------------------------
//     * values are for level "LSOA" are:-----------------------------------------
//     * values[0] = rec.getOa01();-----------------------------------------------
//     * values[1] = rec.getLsoa01();---------------------------------------------
//     * values[2] = rec.getOa11();-----------------------------------------------
//     * values[3] = rec.getLsoa11();---------------------------------------------
//     */
//    private static TreeMap<String, String[]> initLookupFromPostcodeToCensusCodes(String level) {
//        TreeMap<String, String[]> result = null;
//        File inputDirectory = new File("/scratch01/Work/Projects/NewEnclosures/ONSPD/Data/");
//        String inputFilename = inputFilename = "ONSPD_AUG_2013_UK_O.csv";
//        File inFile = new File(inputDirectory, inputFilename);
//        File outputDirectory = new File("/scratch02/DigitalWelfare/ONSPD/processed");
//        String outputFilename;
//        File outFile;
//        if (level.equalsIgnoreCase("MSOA")) {
//            outputFilename = "PostcodeLookUp_TreeMap_String_Strings.thisFile";
//            outFile = new File(outputDirectory, outputFilename);
//            //new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitPointLookup();
//            //new DW_Postcode_Handler(inFile, outFile).run1();
//            result = new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitCensusCodesLookup();
//        }
//        if (level.equalsIgnoreCase("LSOA")) {
//            outputFilename = "PostcodeToLSOALookUp_TreeMap_String_Strings.thisFile";
//            outFile = new File(outputDirectory, outputFilename);
//            //new DW_Postcode_Handler(inFile, outFile).getPostcodeUnitPointLookup();
//            //new DW_Postcode_Handler(inFile, outFile).run1();
//            result = new DW_Postcode_Handler(inFile, outFile).run4();
//        }
//        return result;
//    }

    /**
     * @return TreeMap<String, String> result where:----------------------------
     * Keys are postcodes; values are census codes:-----------------------------
     */
    private static TreeMap<String, String> initLookupFromPostcodeToCensusCodes(
            File tONSPD_NOV_2013DataFile,
            File outFile,
            String level,
            int year) {
        TreeMap<String, String> result = null;
        result = new DW_Postcode_Handler(tONSPD_NOV_2013DataFile, outFile).getPostcodeUnitCensusCodeLookup(
                level,
                year);
        return result;
    }

    public static TreeMap<String, Deprivation_DataRecord> getDeprivation_Data() {
        File depravationDir = new File(
                DW_Files.getInputCensus2011AttributeDataDir("LSOA"),
                "England/Deprivation");
        String deprivationFilename = "1871524.csv";
        Deprivation_DataHandler aDeprivation_DataHandler;
        aDeprivation_DataHandler = new Deprivation_DataHandler();
        TreeMap<String, Deprivation_DataRecord> tDeprivationData;
        tDeprivationData = aDeprivation_DataHandler.loadInputData(
                depravationDir, deprivationFilename);
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

    public static TreeMap<String, String> getOutletsAndPostcodes() {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        // Postcodes in data from Leeds CAB
        result.put("Otley", "LS21 1BG");
        result.put("Morley", "LS27 9DY");
        result.put("Crossgates", "LS15 8QR");
        result.put("Pudsey", "LS28 7AB");
        result.put("City", "LS2 7DT");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.put("Chapeltown", "LS7 4BZ");
        return result;
    }

    public static TreeMap<String, AGDT_Point> getOutletsAndPoints() {
        TreeMap<String, AGDT_Point> result;
        result = DW_Postcode_Handler.postcodeToPoints(DW_Processor.getOutletsAndPostcodes());
        return result;
    }


    public static TreeMap<String, String> getAdviceLeedsNamesAndPostcodes() {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        ArrayList<String> tAdviceLeedsServiceNames;
        tAdviceLeedsServiceNames = getAdviceLeedsServiceNames();
        ArrayList<String> tAdviceLeedsServicePostcodes;
        tAdviceLeedsServicePostcodes = getAdviceLeedsServicePostcodes();
        for (int i = 0; i < tAdviceLeedsServiceNames.size(); i++) {
            result.put(tAdviceLeedsServiceNames.get(i),
                    tAdviceLeedsServicePostcodes.get(i));
        }
        return result;
    }

    /**
     * Postcodes are in the same order as names from
     * getAdviceLeedsServiceNames().
     *
     * @return
     */
    public static ArrayList<String> getAdviceLeedsServicePostcodes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        // Postcodes in data from Leeds CAB
        result.add("LS21 1BG");
        result.add("LS27 9DY");
        result.add("LS15 8QR");
        result.add("LS28 7AB");
        result.add("LS2 7DT");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.add("LS7 4BZ");
        // http://www.adviceleeds.org.uk/partmembers.htm
        result.add("LS9 7UT");
        result.add("LS9 9AA");
        result.add("LS6 1QF");
        result.add("LS9 7BG");
        result.add("LS8 4HS");
        return result;
    }

    /**
     * Names are in the same order as postcodes from
     * getAdviceLeedsServiceNames().
     *
     * @return
     */
    public static ArrayList<String> getAdviceLeedsServiceNames() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        // Postcodes in data from Leeds CAB
        result.add("Leeds CAB Otley");
        result.add("Leeds CAB Morley");
        result.add("Leeds CAB Crossgates");
        result.add("Leeds CAB Pudsey");
        result.add("Leeds CAB City");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.add("Chapeltown CAB");
        // http://www.adviceleeds.org.uk/partmembers.htm
        result.add("Ebor Gardens");
        result.add("St Vincents");
        result.add("Better Leeds Communities");
        result.add("LCC - Welfare Rights Unit");
        result.add("Leeds Law Centre");
        return result;
    }

    public static TreeMap<String, AGDT_Point> getAdviceLeedsNamesAndPoints() {
        TreeMap<String, AGDT_Point> result;
        result = DW_Postcode_Handler.postcodeToPoints(DW_Processor.getAdviceLeedsNamesAndPostcodes());
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
}
