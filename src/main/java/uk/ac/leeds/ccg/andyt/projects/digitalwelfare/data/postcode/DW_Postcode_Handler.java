/**
 * Copyright 2013 Andy Turner, The University of Leeds, UK
 *
 * Redistribution and use of this software in source and binary forms, with or
 * without modification is permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor;

/**
 * A class for adding coordinate data and area codes for UK postcodes.
 * https://geoportal.statistics.gov.uk/Docs/PostCodes/ONSPD_AUG_2013_csv.zip
 */
public class DW_Postcode_Handler implements Serializable {

    /**
     * A name for exception and error handling
     */
    private static final String className = "PostcodeGeocoder";

    public static double getDistanceBetweenPostcodes(
            String yM3,
            String aPostcode,
            String bPostcode) {
        double result = 0.0d;
        AGDT_Point aPoint;
        aPoint = getPointFromPostcode(yM3, aPostcode);
        AGDT_Point bPoint;
        bPoint = getPointFromPostcode(yM3, bPostcode);
        if (aPoint != null && bPoint != null) {
            result = aPoint.getDistance(bPoint);
        } else {
            System.out.println("<Issue calculating distance between postcodes: " + aPostcode + " and " + bPostcode + "/>");
//            System.out.println("<Issue calculating distance between postcodes: " + aPostcode + " and " + bPostcode + ">");
//            if (aPoint == null) {
//                System.out.println("No point look up for " + aPostcode);
//            }
//            if (bPoint == null) {
//                System.out.println("No point look up for " + bPostcode);                
//            }
//            System.out.println("</Issue calculating distance between postcodes: " + aPostcode + " and " + bPostcode + ">");
//            int debug = 1;
        }
        return result;
    }

    /**
     *
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param postcode
     * @return
     */
    public static AGDT_Point getPointFromPostcode(
            String yM3,
            String level,
            String postcode) {
        if (level == null) {
            return null;
        }
        if (yM3 == null) {
            yM3 = getDefaultYM3();
        }
        AGDT_Point result;
        String formattedPostcode;
        formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
        result = DW_Maps.getONSPDlookups().get(level).get(getNearestYM3ForONSPDLookup(yM3)).get(formattedPostcode);
        return result;
    }

    public static String getNearestYM3ForONSPDLookup(String yM3) {
        /* 2008_FEB
         * 2008_MAY
         * 2008_AUG
         * 2008_NOV
         * 2009_FEB
         * 2009_MAY
         * 2009_AUG
         * 2009_NOV
         * 2010_FEB
         * 2010_MAY
         * 2010_AUG
         * 2010_NOV
         * 2011_MAY
         * 2011_AUG
         * 2011_NOV
         * 2012_FEB
         * 2012_MAY
         * 2012_AUG
         * 2012_NOV
         * 2013_FEB
         * 2013_MAY
         * 2013_AUG
         * 2013_NOV
         * 2014_FEB
         * 2014_MAY
         * 2014_AUG
         * 2014_NOV
         * 2015_FEB
         * 2015_MAY
         * 2015_AUG
         */
        String[] split;
        split = yM3.split("_");
        String year = split[0];
        int yearint = Integer.valueOf(year);
        String month = split[1];
        if (yearint < 2008) {
            return "2008_FEB";
        }
        if (yearint > 2015) {
            return "2015_AUG";
        }
        if (yearint == 2011) {
            if (month.equalsIgnoreCase("JAN")
                    || month.equalsIgnoreCase("FEB")) {
                return "2011_MAY";
            } else {
                if (month.equalsIgnoreCase("MAR")
                        || month.equalsIgnoreCase("APR")
                        || month.equalsIgnoreCase("MAY")) {
                    return "2011_MAY";
                } else {
                    if (month.equalsIgnoreCase("JUN")
                            || month.equalsIgnoreCase("JUL")
                            || month.equalsIgnoreCase("AUG")) {
                        return "2011_AUG";
                    } else {
                        if (month.equalsIgnoreCase("SEP")
                                || month.equalsIgnoreCase("OCT")
                                || month.equalsIgnoreCase("NOV")) {
                            return "2011_NOV";
                        } else {
                            if (month.equalsIgnoreCase("DEC")) {
                                return "2012_FEB";
                            } else {
                                return null;
                            }
                        }
                    }
                }
            }
        } else {
            // Special Cases
            if (yearint == 2010 && month.equalsIgnoreCase("DEC")) {
                return "2011_MAY";
            }
            if (yearint == 2015) {
                if (month.equalsIgnoreCase("JAN")
                    || month.equalsIgnoreCase("FEB")) {
                return year + "_FEB";
            } else {
                if (month.equalsIgnoreCase("MAR")
                        || month.equalsIgnoreCase("APR")
                        || month.equalsIgnoreCase("MAY")) {
                    return year + "_MAY";
                } else {
                    return year + "_AUG";
// In due course it will be possible to add more ONSPD data in                    
//                    if (month.equalsIgnoreCase("JUN")
//                            || month.equalsIgnoreCase("JUL")
//                            || month.equalsIgnoreCase("AUG")) {
//                        return year + "_AUG";
//                    } else {
//                        if (month.equalsIgnoreCase("SEP")
//                                || month.equalsIgnoreCase("OCT")
//                                || month.equalsIgnoreCase("NOV")) {
//                            return year + "_NOV";
//                        } else {
//                            if (month.equalsIgnoreCase("DEC")) {
//                                return Integer.toString(yearint + 1) + "_FEB";
//                            } else {
//                                return null;
//                            }
//                        }
//                    }
                }
            }
            } else {
                if (month.equalsIgnoreCase("JAN")
                        || month.equalsIgnoreCase("FEB")) {
                    return year + "_FEB";
                } else {
                    if (month.equalsIgnoreCase("MAR")
                            || month.equalsIgnoreCase("APR")
                            || month.equalsIgnoreCase("MAY")) {
                        return year + "_MAY";
                    } else {
                        if (month.equalsIgnoreCase("JUN")
                                || month.equalsIgnoreCase("JUL")
                                || month.equalsIgnoreCase("AUG")) {
                            return year + "_AUG";
                        } else {
                            if (month.equalsIgnoreCase("SEP")
                                    || month.equalsIgnoreCase("OCT")
                                    || month.equalsIgnoreCase("NOV")) {
                                return year + "_NOV";
                            } else {
                                if (month.equalsIgnoreCase("DEC")) {
                                    return Integer.toString(yearint + 1) + "_FEB";
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getDefaultYM3() {
        return "2013_AUG";
    }

    public static AGDT_Point getPointFromPostcode(
            String yM3,
            String postcode) {
        AGDT_Point result;
        String level;
        level = DW_Postcode_Handler.getPostcodeLevel(postcode);
        result = getPointFromPostcode(yM3, level, postcode);
        return result;
    }

    /**
     * @see also
     * @param unformattedUnitPostcode
     * @return A better format of the unformattedUnitPostcode
     */
    public static String formatPostcode(String unformattedUnitPostcode) {
        String result;
        String postcodeNoSpaces;
        if (unformattedUnitPostcode == null) {
            result = "";
        } else {
            postcodeNoSpaces = unformattedUnitPostcode.trim().replaceAll(" ", "");
            postcodeNoSpaces = postcodeNoSpaces.replaceAll("'", "");
            postcodeNoSpaces = postcodeNoSpaces.replaceAll("\\.", "");
            if (postcodeNoSpaces.length() < 5) {
                System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " too few characters to format.");
                result = postcodeNoSpaces;
            } else {
                int length;
                length = postcodeNoSpaces.length();
                String firstPartPostcode;
                firstPartPostcode = postcodeNoSpaces.substring(0, length - 3);
                String secondPartPostcode;
                secondPartPostcode = postcodeNoSpaces.substring(length - 3, length);
                result = firstPartPostcode + " " + secondPartPostcode;
            }
        }
//        String[] postcodeSplit = unformattedUnitPostcode.split(" ");
//        if (postcodeSplit.length > 3) {
//            System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
//        } else {
//            if (postcodeSplit.length == 3) {
//                result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
//                if (postcodeSplit[2].length() != 3) {
//                    System.out.println("Unusual length of second part of postcode " + unformattedUnitPostcode);
//                }
//            } else {
//                if (postcodeSplit.length == 2) {
//                    result = postcodeSplit[0] + " " + postcodeSplit[1];
//                    if (postcodeSplit[1].length() > 4) {
//                        System.out.println("Unusual length of first part of postcode " + unformattedUnitPostcode);
//                    }
//                }
//            }
//        }
        return result;
    }

    /**
     * @param unformattedUnitPostcode
     * @return A format of the unformattedUnitPostcode for ONSPD lookups For
     * example this will return "LS11OJS" for an unformattedUnitPostcode = "LS11
     * 0JS"
     */
    public static String formatPostcodeForONSPDLookup(String postcode) {
        String result;
        result = postcode;
        if (postcode.length() == 8) {
            result = postcode.replaceAll(" ", "");
        }
        if (postcode.length() == 6) {
            result = postcode.substring(0, 2) + " " + postcode.substring(3, 6);
        }
//        if (unformattedUnitPostcode != null) {
//            String result = unformattedUnitPostcode.trim();
//            String[] postcodeSplit = result.split(" ");
//            if (postcodeSplit.length > 3) {
//                // unformattedUnitPostcode is not a valid postcode!
//                System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
//                return null;
//            } else {
//                if (postcodeSplit.length == 3) {
//                    result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
//                    if (postcodeSplit[2].length() != 3) {
//                        // unformattedUnitPostcode is not a valid postcode!
//                        System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " not formatted into a unit postcode");
//                        return null;
//                    }
//                } else {
//                    if (postcodeSplit.length == 2) {
//                        result = postcodeSplit[0] + " " + postcodeSplit[1];
//                        if (postcodeSplit[1].length() != 3) {
//                            // unformattedUnitPostcode is not a valid postcode!
//                            System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " not formatted into a unit postcode");
//                            return null;
//                        }
//                    }
//                }
//            }
//            if (result.length() == 8) {
//                result = result.replaceAll(" ", "");
//            }
//            result = Generic_StaticString.getUpperCase(result);
//            return result;
//        }
//        return null;
        return result;
    }

    /**
     * @param input TreeMap<String, String> where values are postcodes for which
     * the coordinates are to be returned as a AGDT_Point.
     * @return TreeMap<String, AGDT_Point> with the keys as in input and values
     * calculated using getPointFromPostcode(value). If no look up is found for
     * a postcode its key does not get put into the result.
     */
    public static TreeMap<String, AGDT_Point> postcodeToPoints(
            TreeMap<String, String> input) {
        TreeMap<String, AGDT_Point> result;
        result = new TreeMap<String, AGDT_Point>();
        Iterator<String> ite_String = input.keySet().iterator();
        while (ite_String.hasNext()) {
            String key = ite_String.next();
            String postcode = input.get(key);
            AGDT_Point p = getPointFromPostcode(null, postcode);
            if (p == null) {
                System.out.println("No point for postcode " + postcode);
            } else {
                result.put(key, p);
            }
        }
        return result;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode District = "LS2";
     *
     * @param unitPostcode
     * @return
     */
    public static String getPostcodeDistrict(String unitPostcode) {
        String result = "";
        String p;
        p = unitPostcode.trim();
        if (p.length() < 3) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return result;
        } else {
            String[] pp = p.split(" ");
            if (pp.length == 2) {
                result = pp[0];
                return result;
            } else {
                if (pp.length == 1) {
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 3) {
                        return "";
                    }
                    return result;
                } else {
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together.
                    result += pp[0];
                    result += pp[1];
                    if (result.length() < 3) {
                        return "";
                    }
                    return result;
                }
            }
        }
    }

    public DW_Postcode_Handler() {
    }

    public static String getDefaultLookupFilename() {
        String selection = "LS";
        selection += "BD";
        selection += "HG";
        //selection += "HD";
        selection += "CR";
        selection += "W";
        selection += "NP";
        selection += "BL";
        selection += "HX";
        selection += "HD";
        return "PostcodeLookUp_" + selection + "_TreeMap_String_Point.thisFile";
    }

    public static void main(String[] args) {
        new DW_Postcode_Handler().run();
        //new DW_Postcode_Handler(inputFile, processedFile).run1();
        //new DW_Postcode_Handler(inputFile, processedFile).getPostcodeUnitCensusCodesLookup();
        //new DW_Postcode_Handler(inputFile, processedFile).run3();
    }

    public TreeMap<String, AGDT_Point> getPostcodeSectorPointLookup(
            boolean ignorePointsAtOrigin,
            String outputFilename,
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup) {
        TreeMap<String, AGDT_Point> result = null;
//        File outputFile2;
//        outputFile2 = new File(
//                outputFile.getParentFile(),
//                outputFilename);
//        try {
//            result = initPostcodeSectorPointLookup(
//                    postcodeUnitPointLookup,
//                    ignorePointsAtOrigin);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        String postcode;
//        // Test some postcodes
//        postcode = "LS17 2";
//        printTest0(
//                result,
//                postcode);
//        postcode = "LS2 9";
//        printTest0(
//                result,
//                postcode);
//        Generic_StaticIO.writeObject(result, outputFile2);
//        // For testing reading back in
//        result = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outputFile2);
        return result;
    }

    public static TreeMap<String, TreeMap<String, AGDT_Point>> getPostcodeUnitPointLookups(
            boolean ignorePointsAtOrigin,
            TreeMap<String, File> ONSPDFiles,
            String processedFilename) {
        TreeMap<String, TreeMap<String, AGDT_Point>> result;
        result = new TreeMap<String, TreeMap<String, AGDT_Point>>();
        Iterator<String> ite;
        ite = ONSPDFiles.keySet().iterator();
        while (ite.hasNext()) {
            String yM3;
            yM3 = ite.next();
            File outDir = new File(
                    DW_Files.getGeneratedONSPDDir(),
                    yM3);
            File outFile = new File(
                    outDir,
                    processedFilename);
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup;
            if (outFile.exists()) {
                System.out.println("Load " + outFile);
                postcodeUnitPointLookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outFile);
            } else {
                File f;
                f = ONSPDFiles.get(yM3);
                System.out.println("Format " + f);
                postcodeUnitPointLookup = initPostcodeUnitPointLookup(
                        f,
                        ignorePointsAtOrigin);
                outDir.mkdirs();
                Generic_StaticIO.writeObject(postcodeUnitPointLookup, outFile);
            }
            result.put(yM3, postcodeUnitPointLookup);
//            // Test some postcodes
//            String postcode;
//            postcode = "LS7 2EU";
//            printTest0(
//                    postcodeUnitPointLookup,
//                    postcode);
//            postcode = "LS2 9JT";
//            printTest0(
//                    postcodeUnitPointLookup,
//                    postcode);
        }
        return result;
    }

    public TreeMap<String, ONSPDRecord_Aug_2013_UK_0> getPostcodeUnitONSPDRecordLookup() {
        // Read NPD into a lookup
        TreeMap<String, ONSPDRecord_Aug_2013_UK_0> lookup;
        lookup = null;
//        lookup = readONSPDIntoTreeMapPostcodeONSPDRecord(
//                inputFile);
//        // Test some postcodes
//        String postcode;
//        postcode = "LS7 2EU";
//        printTest1(
//                lookup,
//                postcode);
//        postcode = "LS2 9JT";
//        printTest1(
//                lookup,
//                postcode);
//        Generic_StaticIO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    public TreeMap<String, String[]> getPostcodeUnitCensusCodesLookup() {
        // Read NPD into a lookup
        TreeMap<String, String[]> lookup;
        lookup = null;
//        lookup = readONSPDIntoTreeMapPostcodeStrings(
//                inputFile);
//        // Test some postcodes
//        String postcode;
//        postcode = "LS7 2EU";
//        printTest2(
//                lookup,
//                postcode);
//        postcode = "LS2 9JT";
//        printTest2(
//                lookup,
//                postcode);
//        Generic_StaticIO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    /**
     *
     * @param level "OA", "LSOA", "MSOA"
     * @param year
     * @return
     */
    public TreeMap<String, String> getPostcodeUnitCensusCodeLookup(
            String level,
            int year,
            File tONSPD_NOV_2013DataFile,
            File outFile) {
        // Read NPD into a lookup
        TreeMap<String, String> lookup;
        lookup = null;
        lookup = readONSPDIntoTreeMapPostcodeString(tONSPD_NOV_2013DataFile, level, year);
        Generic_StaticIO.writeObject(lookup, outFile);
//        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outFile);
        return lookup;
    }

    /**
     *
     * @param postcode
     * @return
     */
    public static String getPostcodeLevel(String postcode) {
        if (postcode == null) {
            return null;
        }
        String p;
        p = postcode.trim();
        if (p.length() < 2) {
            return null;
        }
        while (p.contains("  ")) {
            p = p.replaceAll("  ", " ");
        }
        String[] pa;
        pa = p.split(" ");
        if (pa.length == 1) {
            String pType;
            pType = Generic_UKPostcode_Handler.getFirstPartPostcodeType(p);
            if (!pType.isEmpty()) {
                return "Area";
            } else {
                return null;
            }
        }
        if (pa.length == 2) {
            String pa1;
            pa1 = pa[1];
            if (pa1.length() == 3) {
                return "Unit";
            }
            if (pa1.length() == 1) {
                return "Sector";
            }
        }
        if (pa.length > 2) {
            // Assume the first two parts should be joined together as the 
            // outward part of the postcode and the third part is the inward 
            // part.
            String pa1;
            pa1 = pa[2];
            if (pa1.length() == 3) {
                return "Unit";
            }
            if (pa1.length() == 1) {
                return "Sector";
            }
        }
        return null;
    }

    public void run() {
        String processedFilename = getDefaultLookupFilename();
        boolean ignorePointsAtOrigin = true;
        TreeMap<String, File> ONSPDFiles;
        ONSPDFiles = getONSPDFiles();
        TreeMap<String, TreeMap<String, AGDT_Point>> postcodeUnitPointLookups;
        postcodeUnitPointLookups = getPostcodeUnitPointLookups(
                ignorePointsAtOrigin,
                ONSPDFiles,
                processedFilename);
    }

    public void run3() {
        // Read NPD OutputArea code mapping
        // There are two OA codes and this is a lookup from one to another.
        HashSet<String> numerals_HashSet = getNumeralsHashSet();
        String filename = "oacode_new_to_old.txt";
        File NPDDocumentsDirectory = new File("/scratch01/Work/Projects/NewEnclosures/ONSPD/Documents/");
        File NPDDocumentsLookUpsDirectory = new File(
                NPDDocumentsDirectory,
                "Look-ups");
        File file = new File(
                NPDDocumentsLookUpsDirectory,
                filename);
        HashMap<String, String> oaCodeLookUp = readONSPD_OACodeLookup(
                file,
                numerals_HashSet);
//        File oaCodeLookUpFile = new File(
//                directory,
//                "oaCodeLookUp_HashmapStringString.thisFile");
//        Generic_StaticIO.writeObject(oaCodeLookUp, oaCodeLookUpFile);
//        Generic_StaticIO.writeObject(oaCodeLookUp, outputFile);
        //oaCodeLookUp = (HashMap<String, String>) Generic_StaticIO.readObject(lookupFile);
    }

//    public TreeMap<String, String[]> run4() {
//        // Read NPD into a lookup
//        TreeMap<String, String[]> lookup;
//        lookup = readONSPDIntoTreeMapPostcodeStrings2(
//                inputFile);
//        // Test some postcodes
//        String postcode;
//        postcode = "LS7 2EU";
//        printTest2(
//                lookup,
//                postcode);
//        postcode = "LS2 9JT";
//        printTest2(
//                lookup,
//                postcode);
//        Generic_StaticIO.writeObject(lookup, outputFile);
//        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
//        return lookup;
//    }
    public HashSet<String> getNumeralsHashSet() {
        HashSet<String> numerals_HashSet = new HashSet<String>();
        numerals_HashSet.add("0");
        numerals_HashSet.add("1");
        numerals_HashSet.add("2");
        numerals_HashSet.add("3");
        numerals_HashSet.add("4");
        numerals_HashSet.add("5");
        numerals_HashSet.add("6");
        numerals_HashSet.add("7");
        numerals_HashSet.add("8");
        numerals_HashSet.add("9");
        return numerals_HashSet;
    }

    public HashMap<String, String> readONSPD_OACodeLookup(
            File file,
            HashSet<String> numerals_HashSet) {
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            int lineCounter = 0;
            //int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            //skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line + " " + line);
                        String[] fields = line.split("  ");
                        //System.out.println("fields.length " + fields.length);
                        if (fields.length != 2) {
                            System.out.println(lineCounter + " " + line);
                            System.out.println("fields.length " + fields.length);
                        }
                        result.put(fields[0], fields[1]);
                        lineCounter++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD2(File)");
            System.exit(2);
        }
        return result;
    }

    public static TreeMap<String, AGDT_Point> getStringToDW_PointLookup(File file) {
        return (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(file);
    }

    public static TreeMap<String, String[]> getStringToStringArrayLookup(File file) {
        return (TreeMap<String, String[]>) Generic_StaticIO.readObject(file);
    }

    public static HashMap<String, String> getStringToStringLookup(File file) {
        return (HashMap<String, String>) Generic_StaticIO.readObject(file);
    }

    public static void printTest0(
            TreeMap<String, AGDT_Point> lookup,
            String postcode) {
        AGDT_Point p = lookup.get(postcode);
        if (p != null) {
            int lon = p.getX();
            int lat = p.getY();
            System.out.println(postcode + ", " + lon + ", " + lat);
        } else {
            System.out.println(postcode + ", no point!");
        }
    }

    public static void printTest1(
            TreeMap<String, ONSPDRecord_Aug_2013_UK_0> lookup,
            String postcode) {
        ONSPDRecord_Aug_2013_UK_0 rec = lookup.get(postcode);
        System.out.println(postcode + ", " + rec);
    }

    public static void printTest2(
            TreeMap<String, String[]> lookup,
            String postcode) {
        String[] values = lookup.get(postcode);
        System.out.println(postcode + ", " + values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3]);
    }

    /**
     * Once all Unit Postcodes are read, then postcode sectors and postcode area
     * centroids are also added.
     *
     * http://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom Postcode
     * area is part of the outward code. The postcode area is between one and
     * two characters long and is all letters. Examples of postcode areas
     * include "L" for Liverpool, "RH" for Redhill and "EH" Edinburgh. A postal
     * area may cover a wide area, for example "RH" covers north Sussex, (which
     * has little to do with Redhill historically apart from the railway links),
     * and "BT" (Belfast) covers the whole of Northern Ireland.
     *
     * Postcode district is part of the outward code. It is one or two digits
     * (and sometimes a final letter) that are added to the end of the postcode
     * area. the outward code is between two and four characters long. Examples
     * of postcode districts include "W1A", "RH1", "RH10" or "SE1P".
     *
     * Postcode sector is made up of the postcode district, the single space,
     * and the first character of the inward code. It is between four and six
     * characters long (including the single space). Examples of postcode
     * sectors include "SW1W 0", "PO16 7", "GU16 7", or "L1 8".
     *
     * Postcode unit is two characters added to the end of the postcode sector.
     * Each postcode unit generally represents a street, part of a street, a
     * single address, a group of properties, a single property, a sub-section
     * of the property, an individual organisation or (for instance Driver and
     * Vehicle Licensing Agency) a subsection of the organisation. The level of
     * discrimination is often based on the amount of mail received by the
     * premises or business. Examples of postcode units include "SW1W 0NY",
     * "PO16 7GZ", "GU16 7HF", or "L1 8JQ".
     *
     * @param file
     * @param postcodeUnitPointLookup
     * @return
     */
    private TreeMap<String, AGDT_Point> initPostcodeSectorPointLookup(
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, AGDT_Point> result;
        result = new TreeMap<String, AGDT_Point>();
        // Aggregate by postcode
        // Create postcodeSector to unitPostcodes look up
        TreeMap<String, HashSet<String>> postcodeSectorsAndUnitPostcodes;
        postcodeSectorsAndUnitPostcodes = new TreeMap<String, HashSet<String>>();
        Iterator<String> ite;
        ite = postcodeUnitPointLookup.keySet().iterator();
        while (ite.hasNext()) {
            String unitPostcode = ite.next();
            String postcodeSector = getPostcodeSector(unitPostcode);
            if (postcodeSectorsAndUnitPostcodes.containsKey(postcodeSector)) {
                postcodeSectorsAndUnitPostcodes.get(postcodeSector).add(
                        unitPostcode);
            } else {
                postcodeSectorsAndUnitPostcodes.put(
                        postcodeSector, new HashSet<String>());
            }
        }
        // Average points to get postcode sector centroids
        ite = postcodeSectorsAndUnitPostcodes.keySet().iterator();
        while (ite.hasNext()) {
            String postcodeSector = ite.next();
            HashSet<String> postcodes = postcodeSectorsAndUnitPostcodes.get(
                    postcodeSector);
            double sumx = 0;
            double sumy = 0;
            double n = postcodes.size();
            Iterator<String> ite2;
            ite2 = postcodes.iterator();
            while (ite2.hasNext()) {
                String unitPostcode = ite2.next();
                AGDT_Point p = postcodeUnitPointLookup.get(unitPostcode);
                sumx += p.getX();
                sumy += p.getY();
            }
            int x = (int) (sumx / n);
            int y = (int) (sumy / n);
            AGDT_Point postcodeSectorPoint = new AGDT_Point(x, y);
            if (ignorePointsAtOrigin) {
                if (!(x < 1 || y < 1)) {
                    result.put(postcodeSector, postcodeSectorPoint);
                }
            } else {
                result.put(postcodeSector, postcodeSectorPoint);
            }
        }
        return result;
    }

    /**
     * Once all Unit Postcodes are read, then postcode sectors and postcode area
     * centroids are also added.
     *
     * http://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom Postcode
     * area is part of the outward code. The postcode area is between one and
     * two characters long and is all letters. Examples of postcode areas
     * include "L" for Liverpool, "RH" for Redhill and "EH" Edinburgh. A postal
     * area may cover a wide area, for example "RH" covers north Sussex, (which
     * has little to do with Redhill historically apart from the railway links),
     * and "BT" (Belfast) covers the whole of Northern Ireland.
     *
     * Postcode district is part of the outward code. It is one or two digits
     * (and sometimes a final letter) that are added to the end of the postcode
     * area. the outward code is between two and four characters long. Examples
     * of postcode districts include "W1A", "RH1", "RH10" or "SE1P".
     *
     * Postcode sector is made up of the postcode district, the single space,
     * and the first character of the inward code. It is between four and six
     * characters long (including the single space). Examples of postcode
     * sectors include "SW1W 0", "PO16 7", "GU16 7", or "L1 8".
     *
     * Postcode unit is two characters added to the end of the postcode sector.
     * Each postcode unit generally represents a street, part of a street, a
     * single address, a group of properties, a single property, a sub-section
     * of the property, an individual organisation or (for instance Driver and
     * Vehicle Licensing Agency) a subsection of the organisation. The level of
     * discrimination is often based on the amount of mail received by the
     * premises or business. Examples of postcode units include "SW1W 0NY",
     * "PO16 7GZ", "GU16 7HF", or "L1 8JQ".
     *
     * @param file
     * @param postcodeUnitPointLookup
     * @return
     */
    private TreeMap<String, AGDT_Point> initPostcodeAreaPointLookup(
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, AGDT_Point> result;
        result = new TreeMap<String, AGDT_Point>();
        // Aggregate by postcode
        // Create postcodeSector to unitPostcodes look up
        TreeMap<String, HashSet<String>> postcodeAreasAndUnitPostcodes;
        postcodeAreasAndUnitPostcodes = new TreeMap<String, HashSet<String>>();
        Iterator<String> ite;
        ite = postcodeUnitPointLookup.keySet().iterator();
        while (ite.hasNext()) {
            String unitPostcode = ite.next();
            String postcodeArea = getPostcodeArea(unitPostcode);
            if (postcodeAreasAndUnitPostcodes.containsKey(postcodeArea)) {
                postcodeAreasAndUnitPostcodes.get(postcodeArea).add(
                        unitPostcode);
            } else {
                postcodeAreasAndUnitPostcodes.put(
                        postcodeArea, new HashSet<String>());
            }
        }
        // Average points to get postcode sector centroids
        ite = postcodeAreasAndUnitPostcodes.keySet().iterator();
        while (ite.hasNext()) {
            String postcodeSector = ite.next();
            HashSet<String> postcodes = postcodeAreasAndUnitPostcodes.get(
                    postcodeSector);
            double sumx = 0;
            double sumy = 0;
            double n = postcodes.size();
            Iterator<String> ite2;
            ite2 = postcodes.iterator();
            while (ite2.hasNext()) {
                String unitPostcode = ite2.next();
                AGDT_Point p = postcodeUnitPointLookup.get(unitPostcode);
                sumx += p.getX();
                sumy += p.getY();
            }
            int x = (int) (sumx / n);
            int y = (int) (sumy / n);
            AGDT_Point postcodeAreaPoint = new AGDT_Point(x, y);
            if (ignorePointsAtOrigin) {
                if (!(x < 1 || y < 1)) {
                    result.put(postcodeSector, postcodeAreaPoint);
                }
            } else {
                result.put(postcodeSector, postcodeAreaPoint);
            }
        }
        return result;
    }

//    public static String formatPostcodeForMapping(String postcode) {
//        String[] split = postcode.split(" ");
//        if (split.length == 2) {
//            return postcode;
//        }
//        int length = postcode.length();
//        String firstPartPostcode;
//        firstPartPostcode = postcode.substring(0,length - 3);
//        String secondPartPostcode;
//        secondPartPostcode = postcode.substring(length - 3);
//        return firstPartPostcode + " " + secondPartPostcode;        
//    }
    public static String formatPostcodeForMapping(String postcode) {
        String[] split = postcode.split(" ");
        if (split.length == 2) {
            if (split[0].length() == 3) {
                return postcode;
            } else {
                return split[0] + split[1];
            }
        } else {
            int debug = 1;
        }
        return postcode;
    }

    public static boolean isValidPostcode(
            String yM3,
            String postcode) {
        if (postcode == null) {
            return false;
        }
        if (postcode.length() > 5) {
            boolean isValidPostcodeForm;
            isValidPostcodeForm = Generic_UKPostcode_Handler.isValidPostcodeForm(
                    postcode);
            if (isValidPostcodeForm) {
                String formattedPostcode;
                formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
//                if (formattedPostcode == null) {
//                    return false;
//                } else {
                boolean isMappablePostcode;
//                isMappablePostcode = DW_Maps.getONSPDlookups().get(getNearestYM3ForONSPDLookup(yM3)).get("Unit").containsKey(formattedPostcode);
                TreeMap<String, TreeMap<String, TreeMap<String, AGDT_Point>>> ONSPDLookups;
                ONSPDLookups = DW_Maps.getONSPDlookups();
                TreeMap<String, TreeMap<String, AGDT_Point>> unitPostcodeONSPDLookups;
                unitPostcodeONSPDLookups = ONSPDLookups.get("Unit");
                TreeMap<String, AGDT_Point> yM3UnitPostcodeONSPDLookupsONS;
                yM3UnitPostcodeONSPDLookupsONS = unitPostcodeONSPDLookups.get(yM3);
                if (yM3UnitPostcodeONSPDLookupsONS == null) {
                    System.err.println("yM3UnitPostcodeONSPDLookupsONS == null for yM3 " + yM3);
                }
                isMappablePostcode = yM3UnitPostcodeONSPDLookupsONS.containsKey(formattedPostcode);
                //isMappablePostcode = DW_Maps.getONSPDlookups().get("Unit").get(yM3).containsKey(formattedPostcode);
                if (isMappablePostcode) {
                    return true;
                } else {
//                        System.out.println("Postcode " + postcode + " format "
//                                + "valid, but not mappable with the ONSPDLookup used.");
                    return false;
                }
//                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode Sector = "LS2 9"
     *
     * @param unitPostcode
     * @return
     * @throws java.lang.Exception
     */
    public static String getPostcodeSector(String unitPostcode) {
        if (unitPostcode == null) {
            return null;
        }
        String result = "";
        String p;
        p = unitPostcode.trim();
        if (p.length() < 5) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return result;
        } else {
            String[] pp = p.split(" ");
            if (pp.length == 2) {
                result = pp[0] + " " + pp[1].substring(0, 1);
                return result;
            } else {
                if (pp.length == 1) {
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 5) {
                        return "";
                    }
                    return result;
                } else {
                    //throw new Exception("Postcode format exception 2 in getPostcodeSector(" + unitPostcode + " )");
                    // Put the first and second parts together and add the first part of the third
                    result += pp[0];
                    result += pp[1] + " ";
                    result += pp[2].substring(0, 1);
                    if (result.length() < 5) {
                        return "";
                    }
                    return result;
                }
            }
        }
        //return result;
    }

    public static String getPostcodeArea(String ONSPDPostcodeUnit) {
        String result;
        int length = ONSPDPostcodeUnit.length();
        result = ONSPDPostcodeUnit.substring(0, length - 3);
        result = result.trim();
//        // With a Space
//        String outward = ONSPDPostcodeUnit.substring(length - 3);
//        String inward = ONSPDPostcodeUnit.substring(0,length - 4).trim();
//        result = outward + " " + inward.substring(0, 1);
        return result;
    }

    public static TreeMap<String, AGDT_Point> initPostcodeUnitPointLookup(
            File file,
            boolean ignorePointsAtOrigin) {
        TreeMap<String, AGDT_Point> result = new TreeMap<String, AGDT_Point>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_EastingNorthing rec;
                        rec = new ONSPDRecord_EastingNorthing(line);
                        int easting = rec.getOseast1m();
                        int northing = rec.getOsnrth1m();
                        AGDT_Point point;
                        point = new AGDT_Point(easting, northing);
                        String postcode = rec.getPcd();
                        if (ignorePointsAtOrigin) {
                            // Test for orgin point (postcodes ending ZZ are usually at origin, but some others are too.)
//                            if (!(easting == 0 && northing == 0)) {
//                                result.put(rec.getPcd(), point);
//                            } else {
//                                int debug = 1;
//                            }
                            if (easting < 1 || northing < 1) {
                                int debug = 1;
                            } else {
                                if (postcode.startsWith("LS")
                                        || postcode.startsWith("BD")
                                        || postcode.startsWith("HG")
                                        || postcode.startsWith("CR")
                                        || postcode.startsWith("W")
                                        || postcode.startsWith("NP")
                                        || postcode.startsWith("BL")
                                        || postcode.startsWith("HX")
                                        || postcode.startsWith("HD")) {
                                    result.put(rec.getPcd(), point);
                                }
                            }
//                            if (postcode.endsWith("ZZ")) {
//                                int debug = 1;
//                            }
                        } else {
                            if (postcode.startsWith("LS")
                                    || postcode.startsWith("BD")
                                    || postcode.startsWith("HG")
                                    || postcode.startsWith("CR")
                                    || postcode.startsWith("W")
                                    || postcode.startsWith("NP")
                                    || postcode.startsWith("BL")
                                    || postcode.startsWith("HX")
                                    || postcode.startsWith("HD")) {
                                result.put(rec.getPcd(), point);
                            }
                        }
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of something like 2560000");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + "DW_Postcode_Handler.initPostcodeUnitPointLookup(File,boolean)");
            System.exit(2);
        }
        return result;
    }

    public TreeMap<String, ONSPDRecord_Aug_2013_UK_0> readONSPDIntoTreeMapPostcodeONSPDRecord(
            File file) {
        TreeMap<String, ONSPDRecord_Aug_2013_UK_0> result = new TreeMap<String, ONSPDRecord_Aug_2013_UK_0>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_Aug_2013_UK_0 rec = new ONSPDRecord_Aug_2013_UK_0(line);
                        result.put(rec.getPcd(), rec);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of 2550320");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    /**
     * MSOA
     *
     * @param file
     * @return
     */
    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings(
            File file) {
        TreeMap<String, String[]> result = new TreeMap<String, String[]>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_Aug_2013_UK_0 rec = new ONSPDRecord_Aug_2013_UK_0(line);
                        String[] values = new String[4];
                        values[0] = rec.getOa01();
                        values[1] = rec.getMsoa01();
                        values[2] = rec.getOa11();
                        values[3] = rec.getMsoa11();
                        result.put(rec.getPcd(), values);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of 2550320");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    /**
     * MSOA
     *
     * @param file
     * @param level
     * @param year
     * @return
     */
    public TreeMap<String, String> readONSPDIntoTreeMapPostcodeString(
            File file,
            String level,
            int year) {
        TreeMap<String, String> result = new TreeMap<String, String>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_Aug_2013_UK_0 rec = new ONSPDRecord_Aug_2013_UK_0(line);
                        String value = "";
                        if (level.equalsIgnoreCase("OA")) {
                            if (year == 2001) {
                                value = rec.getOa01();
                            } else {
                                if (year == 2011) {
                                    value = rec.getOa11();
                                }
                            }
                        }
                        if (level.equalsIgnoreCase("LSOA")) {
                            if (year == 2001) {
                                value = rec.getLsoa01();
                            } else {
                                if (year == 2011) {
                                    value = rec.getLsoa11();
                                }
                            }
                        }
                        if (level.equalsIgnoreCase("MSOA")) {
                            if (year == 2001) {
                                value = rec.getMsoa01();
                            } else {
                                if (year == 2011) {
                                    value = rec.getMsoa11();
                                }
                            }
                        }
                        String postcode = rec.getPcd();
                        if (level.equalsIgnoreCase("PostcodeUnit")) {
                            value = postcode;
                        }
                        if (level.equalsIgnoreCase("PostcodeSector")) {
                            value = getPostcodeSector(postcode);
                        }
                        if (level.equalsIgnoreCase("PostcodeDistrict")) {
                            value = getPostcodeDistrict(postcode);
                        }
                        result.put(rec.getPcd(), value);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of 2550320");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    /**
     * OA01 LSOA01 OA11 LSOA11
     *
     * @param file
     * @return LSOA
     */
    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings2(
            File file) {
        TreeMap<String, String[]> result = new TreeMap<String, String[]>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(file);
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_Aug_2013_UK_0 rec = new ONSPDRecord_Aug_2013_UK_0(line);
                        String[] values = new String[4];
                        values[0] = rec.getOa01();
                        values[1] = rec.getLsoa01();
                        values[2] = rec.getOa11();
                        values[3] = rec.getLsoa11();
                        result.put(rec.getPcd(), values);
                        lineCounter++;
                        if (lineCounter % 100000 == 0) {
                            System.out.println("Read " + lineCounter + " lines out of 2550320");
                        }
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = aStreamTokenizer.sval;
                        break;
                }
                tokenType = aStreamTokenizer.nextToken();
            }
            br.close();
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    private static StreamTokenizer getStreamTokeniser(BufferedReader br) {
        StreamTokenizer result = null;
        result = new StreamTokenizer(br);
        result.resetSyntax();
        result.wordChars(',', ',');
        result.wordChars('"', '"');
        result.wordChars('\'', '\'');
        result.wordChars('&', '&');
        result.wordChars(';', ';');
        result.wordChars('(', '(');
        result.wordChars(')', ')');
        result.wordChars('0', '0');
        result.wordChars('1', '1');
        result.wordChars('2', '2');
        result.wordChars('3', '3');
        result.wordChars('4', '4');
        result.wordChars('5', '5');
        result.wordChars('6', '6');
        result.wordChars('7', '7');
        result.wordChars('8', '8');
        result.wordChars('9', '9');
        result.wordChars('.', '.');
        result.wordChars('-', '-');
        result.wordChars('+', '+');
        result.wordChars('a', 'z');
        result.wordChars('A', 'Z');
        result.wordChars('\t', '\t');
        result.wordChars(' ', ' ');
        result.wordChars('#', '#');
        result.wordChars('*', '*');
        result.wordChars(':', ':');
        String s = "/";
        char c = s.charAt(0);
        int c_int = (int) c;
        //System.out.println("s " + s + " c " + c + " c_int " + c_int) ;
        result.wordChars(c_int, c_int);
        result.eolIsSignificant(true);
        return result;
    }

    /**
     * 2008_FEB 2008_MAY 2008_AUG 2008_NOV 2009_FEB 2009_MAY 2009_AUG 2009_NOV
     * 2010_FEB 2010_MAY 2010_AUG 2010_NOV 2011_MAY 2011_AUG 2011_NOV 2012_FEB
     * 2012_MAY 2012_AUG 2012_NOV 2013_FEB 2013_MAY 2013_AUG 2013_NOV 2014_FEB
     * 2014_MAY 2014_AUG 2014_NOV 2015_FEB 2015_MAY 2015_AUG
     *
     * @return
     */
    public static TreeMap<String, File> getONSPDFiles() {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        File dir;
        File f;
        // 2008
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2008");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_FEB_2008_UK_1M.csv");
        result.put("2008_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2008");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_MAY_2008_UK_1M.csv");
        result.put("2008_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2008");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_AUG_2008_UK_1M.csv");
        result.put("2008_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2008");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_NOV_2008_UK_1M.csv");
        result.put("2008_NOV", f);

        // 2009
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2009");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_FEB_2009_UK_1M.csv");
        result.put("2009_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2009");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_MAY_2009_UK_1M_FP.csv");
        result.put("2009_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2009");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_AUG_2009_UK_1M_FP.csv");
        result.put("2009_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2009");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_NOV_2009_UK_1M_FP.csv");
        result.put("2009_NOV", f);

        // 2010
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2010");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_FEB_2010_UK_1M_FP.csv");
        result.put("2010_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2010");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_MAY_2010_UK_1M_FP.csv");
        result.put("2010_MAY", f);
        // AUG
        // "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","00","QA","MJ","0","385386","0801193","1","SN9","S00","179"," "," ","X","0","","11","","","","","99ZZ0099","ZZ0099","9","1","1","0","0","SN9","QA","SN9","","","","99ZZ00",""," ","","99","Z99999999","","Z99999999","9"," ","Z","","99ZZ99Z9","","X98"
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2010");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_AUG_2010_UK_1M_FP.csv");
        result.put("2010_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2010");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "NSPDF_NOV_2010_UK_1M_FP.csv");
        result.put("2010_NOV", f);

        // 2011
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2011");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_MAY_2011_O.csv");
        result.put("2011_MAY", f);
        // AUG
        // "AB1 0AA","AB1  0AA","AB1 0AA","198001","199606","S99999999","S12000033","S13002484","0","385386","0801193","1","S08000006","S99999999","S92000003","S99999999","0","S14000002","S15000001","S09000001","S22000001","S03000012","UKM5001031","99ZZ0099","ZZ0099","9","SN9","QA","SN9","72UB43","72UB43","","99ZZ00","S00001364","7","01C30","S99999999","S99999999","S01000011","S99999999","9","6","Z","S02000007","99ZZ99Z9","3C2","X98"
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2011");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_AUG_2011_UK_O.csv");
        result.put("2011_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2011");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_NOV_2011_UK_O.csv");
        result.put("2011_NOV", f);

        // 2012
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2012");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_FEB_2012_UK_O.csv");
        result.put("2012_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2012");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_MAY_2012_UK_O.csv");
        result.put("2012_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2012");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_AUG_2012_UK_O.csv");
        result.put("2012_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2012");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_NOV_2012_UK_O.csv");
        result.put("2012_NOV", f);

        // 2013
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2013");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_FEB_2013_UK_O.csv");
        result.put("2013_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2013");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_MAY_2013_UK_O.csv");
        result.put("2013_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2013");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_AUG_2013_UK_O.csv");
        result.put("2013_AUG", f);
        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2013");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_NOV_2013_UK.csv");
        result.put("2013_NOV", f);

        // 2014
        // FEB
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2014");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_FEB_2014_UK.csv");
        result.put("2014_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2014");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_MAY_2014_UK.csv");
        result.put("2014_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2014");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_AUG_2014_UK.csv");
        result.put("2014_AUG", f);

        // NOV
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_NOV_2014");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_NOV_2014_UK.csv");
        result.put("2014_NOV", f);

        // 2015
        // FEB     
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_FEB_2015");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_FEB_2015_UK.csv");
        result.put("2015_FEB", f);
        // MAY
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_MAY_2015");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_MAY_2015_UK.csv");
        result.put("2015_MAY", f);
        // AUG
        dir = new File(
                DW_Files.getInputONSPDDir(),
                "ONSPD_AUG_2015");
        dir = new File(
                dir,
                "Data");
        f = new File(
                dir,
                "ONSPD_AUG_2015_UK.csv");
        result.put("2015_AUG", f);
        return result;
    }

}
