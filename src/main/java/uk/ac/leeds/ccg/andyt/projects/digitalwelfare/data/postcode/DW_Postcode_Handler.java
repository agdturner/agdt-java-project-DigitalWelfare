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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;

/**
 * A class for adding coordinate data and area codes for UK postcodes.
 * https://geoportal.statistics.gov.uk/Docs/PostCodes/ONSPD_AUG_2013_csv.zip
 */
public class DW_Postcode_Handler extends Generic_UKPostcode_Handler implements Serializable {

    protected transient DW_Environment env;
    protected transient DW_Files DW_Files;
    protected transient DW_Strings DW_Strings;
    protected DW_Maps DW_Maps;
    public final String TYPE_UNIT = "Unit";
    public final String TYPE_SECTOR = "Sector";
    public final String TYPE_DISTRICT = "District";
    public final String TYPE_AREA = "Area";

    public double getDistanceBetweenPostcodes(
            String yM30v,
            String yM31v,
            DW_ID PostcodeID0,
            DW_ID PostcodeID1) {
        double result = 0.0d;
        AGDT_Point aPoint;
        aPoint = env.getDW_SHBE_Data().getPostcodeIDToPointLookup(yM30v).get(PostcodeID0);
        AGDT_Point bPoint;
        bPoint = env.getDW_SHBE_Data().getPostcodeIDToPointLookup(yM31v).get(PostcodeID1);
        if (aPoint != null && bPoint != null) {
            result = aPoint.getDistance(bPoint);
        } else if (env.DEBUG_Level == env.DEBUG_Level_FINEST) {
            System.out.println("<Issue calculating distance between PostcodeID0 " + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + "/>");
            if (aPoint == null) {
                System.out.println("No point look up for PostcodeID0 " + PostcodeID0 + " in " + yM30v);
                aPoint = env.getDW_SHBE_Data().getPostcodeIDToPointLookup(yM31v).get(PostcodeID0);
                if (aPoint != null) {
                    System.out.println("However there is a look up for PostcodeID0 " + PostcodeID0 + " in " + yM31v + "! Maybe use this instead?");
                }
            }
            if (bPoint == null) {
                System.out.println("No point look up for PostcodeID1 " + PostcodeID1 + " in " + yM31v);
                bPoint = env.getDW_SHBE_Data().getPostcodeIDToPointLookup(yM30v).get(PostcodeID1);
                if (bPoint != null) {
                    System.out.println("However there is a look up for PostcodeID1 " + PostcodeID1 + " in " + yM30v + "! Maybe use this instead?");
                }
            }
            System.out.println("</Issue calculating distance between PostcodeID0 " + PostcodeID0 + " and PostcodeID1 " + PostcodeID1 + ">");
        }
        return result;
    }
    
    public double getDistanceBetweenPostcodes(
            String yM30v,
            String yM31v,
            String postcode0,
            String postcode1) {
        double result = 0.0d;
        AGDT_Point aPoint;
        aPoint = getPointFromPostcode(
                yM30v,
                TYPE_UNIT,
                postcode0);
        AGDT_Point bPoint;
        bPoint = getPointFromPostcode(
                yM31v,
                TYPE_UNIT,
                postcode1);
        if (aPoint != null && bPoint != null) {
            result = aPoint.getDistance(bPoint);
        } else if (env.DEBUG_Level == env.DEBUG_Level_FINEST) {
            System.out.println("<Issue calculating distance between postcodes: " + postcode0 + " and " + postcode1 + "/>");
            if (aPoint == null) {
                System.out.println("No point look up for " + postcode0 + " in " + yM30v);
                aPoint = getPointFromPostcode(
                        yM31v,
                        TYPE_UNIT,
                        postcode0);
                if (aPoint != null) {
                    System.out.println("However there is a look up for " + postcode0 + " in " + yM31v + "! Maybe use this instead?");
                }
            }
            if (bPoint == null) {
                System.out.println("No point look up for " + postcode1 + " in " + yM31v);
                bPoint = getPointFromPostcode(
                        yM30v,
                        TYPE_UNIT,
                        postcode1);
                if (bPoint != null) {
                    System.out.println("However there is a look up for " + postcode1 + " in " + yM30v + "! Maybe use this instead?");
                }

            }
            System.out.println("</Issue calculating distance between postcodes: " + postcode0 + " and " + postcode1 + ">");
        }
        return result;
    }

    /**
     *
     * @param yM3v
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param postcode
     * @return
     */
    public AGDT_Point getPointFromPostcode(
            String nearestYM3ForONSPDLookup,
            String level,
            String postcode) {
        AGDT_Point result;
        String formattedPostcode;
        formattedPostcode = formatPostcode(postcode);
        result = DW_Maps.getONSPDlookups(env).get(level).get(nearestYM3ForONSPDLookup).get(formattedPostcode);
        return result;
    }

    /**
     *
     * @param NearestYM3ForONSPDLookup
     * @param level Expects either "Unit", "Sector" or "Area"
     * @param PostcodeF
     * @return
     */
    public AGDT_Point getPointFromPostcodeNew(
            String NearestYM3ForONSPDLookup,
            String level,
            String PostcodeF) {
        AGDT_Point result;
        TreeMap<String, TreeMap<String, TreeMap<String, AGDT_Point>>> ONSPDlookups;
        ONSPDlookups = DW_Maps.getONSPDlookups(env);
        TreeMap<String, TreeMap<String, AGDT_Point>> ONSPDlookupsLevel;
        ONSPDlookupsLevel = ONSPDlookups.get(level);
        TreeMap<String, AGDT_Point> ONSPDlookupsLevelForNearestYM3ForONSPDLookup;
        ONSPDlookupsLevelForNearestYM3ForONSPDLookup = ONSPDlookupsLevel.get(NearestYM3ForONSPDLookup);
        result = ONSPDlookupsLevelForNearestYM3ForONSPDLookup.get(PostcodeF);
        return result;
    }

    /**
     * 2008_FEB
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
     * 
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
     * 2015_NOV
     * 2016_FEB
     * 2016_MAY
     * 2016_AUG
     * 2016_NOV
     * @param YM3
     * @return
     */
    public static String getNearestYM3ForONSPDLookup(String YM3) {
        String defaultLatest = "2017_MAY";//"2016_NOV";
        String[] split;
        split = YM3.split("_");
        String year = split[0];
        int yearint = Integer.valueOf(year);
        String month = split[1];
        if (yearint > 2016) {
           if (month.equalsIgnoreCase("JAN")
                    || month.equalsIgnoreCase("FEB")) {
                return "" + yearint + "_FEB";
           }
           return defaultLatest;
        } else if (yearint < 2008) {
            return "2008_FEB";
        } else if (yearint == 2011) {
            // There was no realease in Feb!
            if (month.equalsIgnoreCase("JAN")
                    || month.equalsIgnoreCase("FEB")
                    ||month.equalsIgnoreCase("MAR")
                    || month.equalsIgnoreCase("APR")
                    || month.equalsIgnoreCase("MAY")){
                return "2011_MAY";
            } else if (
                    month.equalsIgnoreCase("JUN")
                    || month.equalsIgnoreCase("JUL")
                    || month.equalsIgnoreCase("AUG")){
                return "2011_AUG";
            } else if (
                    month.equalsIgnoreCase("SEP")
                    || month.equalsIgnoreCase("OCT")
                    || month.equalsIgnoreCase("NOV")){
                return "2011_NOV";
            } else {
                return "2011_FEB";
            }
        } else {
            if (month.equalsIgnoreCase("JAN")
                    || month.equalsIgnoreCase("FEB")) {
                return "" + yearint + "_FEB";
            } else if (
                    month.equalsIgnoreCase("MAR")
                    || month.equalsIgnoreCase("APR")
                    || month.equalsIgnoreCase("MAY")){
                return "" + yearint + "_MAY";
            } else if (
                    month.equalsIgnoreCase("JUN")
                    || month.equalsIgnoreCase("JUL")
                    || month.equalsIgnoreCase("AUG")){
                return "" + yearint + "_AUG";
            } else if (
                    month.equalsIgnoreCase("SEP")
                    || month.equalsIgnoreCase("OCT")
                    || month.equalsIgnoreCase("NOV")){
                return "" + yearint + "_NOV";
            } else {
                if (yearint == 2010) {
                    return "2011_MAY";
                } else {
                    if (yearint == 2016) {
                        return defaultLatest;
                    } else {
                        return "" + (yearint + 1) + "_FEB";
                    }
                }
            }
        }
                
//        } else if (yearint == 2012) {
//            if (month.equalsIgnoreCase("JAN")
//                    || month.equalsIgnoreCase("FEB")
//                    || month.equalsIgnoreCase("MAR")
//                    || month.equalsIgnoreCase("APR")
//                    || month.equalsIgnoreCase("MAY")
//                    || month.equalsIgnoreCase("JUN")
//                    || month.equalsIgnoreCase("JUL")) {
//                return "2011_MAY";
//            } else if (month.equalsIgnoreCase("AUG")
//                    || month.equalsIgnoreCase("SEP")
//                    || month.equalsIgnoreCase("OCT")) {
//                return "2012_AUG";
//            } else {
//                return "2012_NOV";
//            }
//        } else if (yearint == 2013) {
//            if (month.equalsIgnoreCase("JAN")
//                    || month.equalsIgnoreCase("FEB")) {
//                return "2012_NOV";
//            } else if (month.equalsIgnoreCase("MAR")
//                    || month.equalsIgnoreCase("APR")) {
//                return "2013_FEB";
//            } else if (month.equalsIgnoreCase("MAY")
//                    || month.equalsIgnoreCase("JUN")) {
//                return "2013_MAY";
//            } else {
//                return "2013_AUG";
//            }
//        } else if (yearint == 2014) {
//            if (month.equalsIgnoreCase("NOV")
//                    || month.equalsIgnoreCase("DEC")) {
//                return "2014_NOV";
//            } else {
//                return "2013_AUG";
//            }
//        } else if (yearint == 2015) {
//            if (month.equalsIgnoreCase("JAN")
//                    || month.equalsIgnoreCase("FEB")
//                    || month.equalsIgnoreCase("MAR")
//                    || month.equalsIgnoreCase("APR")) {
//                return "2014_NOV";
//            } else if (month.equalsIgnoreCase("MAY")
//                    || month.equalsIgnoreCase("JUN")
//                    || month.equalsIgnoreCase("JUL")) {
//                return "2015_MAY";
//            } else {
//                return "2015_AUG";
//            }
//        } else if (yearint == 2016) {
//            if (month.equalsIgnoreCase("JAN")
//                    || month.equalsIgnoreCase("FEB")) {
//                return "2015_AUG";
//            } else {
//                
//                return latest;
//            }
//        }
//        return null;
    }
    
    public String getDefaultYM3() {
        return "2013_AUG";
    }

    /**
     * Return postcodef with a space added between the first and second parts if
     * it is long enough.
     * @param postcodef
     * @return 
     */
    public String getPostcodePrintFormat(String postcodef) {
        int length;
                length = postcodef.length();
                if (length < 5) {
            return postcodef;
        } else {
                String firstPartPostcode;
                firstPartPostcode = postcodef.substring(0, length - 3);
                String secondPartPostcode;
                secondPartPostcode = postcodef.substring(length - 3, length);
                return firstPartPostcode + " " + secondPartPostcode;
            }
    }
    
    /**
     * @see also
     * @param unformattedUnitPostcode
     * @return A better format of the unformattedUnitPostcode
     */
    public String formatPostcode(String unformattedUnitPostcode) {
        if (unformattedUnitPostcode == null) {
            return "";
        } else {
            return unformattedUnitPostcode.replaceAll("[^A-Za-z0-9]", "");
        }
//        String result;
//        String postcodeNoSpaces;
//        if (unformattedUnitPostcode == null) {
//            result = "";
//        } else {
//            // Replace anything that is not Roman A-Z or a-z or 0-9 with nothing.
//            postcodeNoSpaces = unformattedUnitPostcode.replaceAll("[^A-Za-z0-9]", "");
////            postcodeNoSpaces = unformattedUnitPostcode.replaceAll(" ", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("'", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("\\.", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("-", "");
////            postcodeNoSpaces = postcodeNoSpaces.replaceAll("_", "");
//            if (postcodeNoSpaces.length() < 5) {
//                //System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " too few characters to format.");
//                result = postcodeNoSpaces;
//            } else {
//                int length;
//                length = postcodeNoSpaces.length();
//                String firstPartPostcode;
//                firstPartPostcode = postcodeNoSpaces.substring(0, length - 3);
//                String secondPartPostcode;
//                secondPartPostcode = postcodeNoSpaces.substring(length - 3, length);
//                result = firstPartPostcode + " " + secondPartPostcode;
//            }
//        }
////        String[] postcodeSplit = unformattedUnitPostcode.split(" ");
////        if (postcodeSplit.length > 3) {
////            System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
////        } else {
////            if (postcodeSplit.length == 3) {
////                result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
////                if (postcodeSplit[2].length() != 3) {
////                    System.out.println("Unusual length of second part of postcode " + unformattedUnitPostcode);
////                }
////            } else {
////                if (postcodeSplit.length == 2) {
////                    result = postcodeSplit[0] + " " + postcodeSplit[1];
////                    if (postcodeSplit[1].length() > 4) {
////                        System.out.println("Unusual length of first part of postcode " + unformattedUnitPostcode);
////                    }
////                }
////            }
////        }
//        return result;
    }

    /**
     * @param input TreeMap<String, String> where values are postcodes for which
     * the coordinates are to be returned as a AGDT_Point.
     * @param yM3v
     * @return TreeMap<String, AGDT_Point> with the keys as in input and values
     * calculated using getPointFromPostcode(value). If no look up is found for
     * a postcode its key does not get put into the result.
     */
    public TreeMap<String, AGDT_Point> postcodeToPoints(
            TreeMap<String, String> input,
            String yM3v) {
        TreeMap<String, AGDT_Point> result;
        result = new TreeMap<String, AGDT_Point>();
        Iterator<String> ite_String = input.keySet().iterator();
        while (ite_String.hasNext()) {
            String key = ite_String.next();
            String postcode = input.get(key);
            AGDT_Point p = getPointFromPostcode(
                    //env,
                    yM3v,
                    TYPE_UNIT,
                    postcode);
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
    public String getPostcodeDistrict(String unitPostcode) {
        String result = "";
        String p;
        p = unitPostcode.trim();
        if (p.length() < 3) {
            //throw new Exception("Postcode format exception 1 in getPostcodeSector(" + unitPostcode + " )");
            return result;
        } else {
            String[] pp = p.split(" ");
            switch (pp.length) {
                case 2:
                    result = pp[0];
                    return result;
                case 1:
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 3) {
                        return "";
                    }
                    return result;
                default:
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

    public DW_Postcode_Handler() {
    }

    public DW_Postcode_Handler(DW_Environment env) {
        this.env = env;
        this.DW_Strings = env.getDW_Strings();
        this.DW_Files = env.getDW_Files();
        this.DW_Maps = env.getDW_Maps();
    }

    public String getDefaultLookupFilename() {
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
        return "PostcodeLookUp_" + selection 
                //+ "_" + YM3
                + "_TreeMap_String_Point" + DW_Strings.sBinaryFileExtension;
    }

    public static void main(String[] args) {
        new DW_Postcode_Handler().run();
        //new DW_Postcode_Handler(inputFile, processedFile).run1();
        //new DW_Postcode_Handler(inputFile, processedFile).getPostcodeUnitCensusCodesLookup();
        //new DW_Postcode_Handler(inputFile, processedFile).run3();
    }

    public TreeMap<String, TreeMap<String, AGDT_Point>> getPostcodeUnitPointLookups(
            boolean ignorePointsAtOrigin,
            TreeMap<String, File> ONSPDFiles,
            String processedFilename) {
        TreeMap<String, TreeMap<String, AGDT_Point>> result;
        result = new TreeMap<String, TreeMap<String, AGDT_Point>>();
        Iterator<String> ite;
        ite = ONSPDFiles.keySet().iterator();
        while (ite.hasNext()) {
            String YM3;
            YM3 = ite.next();
            File outDir = new File(
                    env.getDW_Files().getGeneratedONSPDDir(),
                    YM3);
            File outFile = new File(
                    outDir,
                    processedFilename);
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup;
            if (outFile.exists()) {
                env.logO("Load " + outFile, true);
                postcodeUnitPointLookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outFile);
            } else {
                File f;
                f = ONSPDFiles.get(YM3);
                env.logO("Format " + f, true);
                postcodeUnitPointLookup = initPostcodeUnitPointLookup(
                        f,
                        ignorePointsAtOrigin);
                outDir.mkdirs();
                Generic_StaticIO.writeObject(postcodeUnitPointLookup, outFile);
            }
            result.put(YM3, postcodeUnitPointLookup);
        }
        return result;
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
     * @param YM3NearestFormat
     * @param infile
     * @param outFile
     * @return
     */
    public TreeMap<String, String> getPostcodeUnitCensusCodeLookup(
            File infile,
            File outFile,
            String level,
            String YM3NearestFormat
    ) {
        // Read NPD into a lookup
        TreeMap<String, String> lookup;
        lookup = readONSPDIntoTreeMapPostcodeString(infile, level, YM3NearestFormat);
        Generic_StaticIO.writeObject(lookup, outFile);
//        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outFile);
        return lookup;
    }

    /**
     *
     * @param postcode
     * @return
     */
    public String getPostcodeLevel(String postcode) {
        if (postcode == null) {
            return null;
        }
        String p;
        p = postcode.trim();
//        if (p.length() < 2) {
//            return null;
//        }
        while (p.contains("  ")) {
            p = p.replaceAll("  ", " ");
        }
        String[] pa;
        pa = p.split(" ");
        if (pa.length == 1) {
            String pType;
            pType = getFirstPartPostcodeType(p);
            if (!pType.isEmpty()) {
                return TYPE_AREA;
            } else {
                return null;
            }
        }
        if (pa.length == 2) {
            String pa1;
            pa1 = pa[1];
            if (pa1.length() == 3) {
                return TYPE_UNIT;
            }
            if (pa1.length() == 1) {
                return TYPE_SECTOR;
            }
        }
        if (pa.length > 2) {
            // Assume the first two parts should be joined together as the 
            // outward part of the postcode and the third part is the inward 
            // part.
            String pa1;
            pa1 = pa[2];
            if (pa1.length() == 3) {
                return TYPE_UNIT;
            }
            if (pa1.length() == 1) {
                return TYPE_SECTOR;
            }
        }
        return null;
    }

    public void run(File logDir) {
       String processedFilename = getDefaultLookupFilename();
        boolean ignorePointsAtOrigin = true;
        TreeMap<String, File> InputONSPDFiles;
        InputONSPDFiles = DW_Files.getInputONSPDFiles();
        TreeMap<String, TreeMap<String, AGDT_Point>> postcodeUnitPointLookups;
        postcodeUnitPointLookups = getPostcodeUnitPointLookups(
                ignorePointsAtOrigin,
                InputONSPDFiles,
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
//                "oaCodeLookUp_HashmapStringString" + DW_Strings.sBinaryFileExtension);
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

    public TreeMap<String, AGDT_Point> getStringToDW_PointLookup(File file) {
        return (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(file);
    }

    public TreeMap<String, String[]> getStringToStringArrayLookup(File file) {
        return (TreeMap<String, String[]>) Generic_StaticIO.readObject(file);
    }

    public HashMap<String, String> getStringToStringLookup(File file) {
        return (HashMap<String, String>) Generic_StaticIO.readObject(file);
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

//    public String formatPostcodeForMapping(String postcode) {
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
    public String formatPostcodeForMapping(String postcode) {
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

    /**
     * 
     * @param NearestYM3ForONSPDLookup
     * @param PostcodeF
     * @return True iff Postcode is a valid Postcode.
     */
    public boolean isMappablePostcode(
            String NearestYM3ForONSPDLookup,
            String PostcodeF) {
        if (PostcodeF == null) {
            return false;
        }
        if (PostcodeF.length() > 5) {
                boolean isMappablePostcode;
                TreeMap<String, TreeMap<String, TreeMap<String, AGDT_Point>>> ONSPDLookups;
                ONSPDLookups = DW_Maps.getONSPDlookups(env);
                TreeMap<String, TreeMap<String, AGDT_Point>> ONSPDLookupUnitPostcode;
                ONSPDLookupUnitPostcode = ONSPDLookups.get(TYPE_UNIT);
                TreeMap<String, AGDT_Point> ONSPDLookupUnitPostcodeNearestYM3;
                ONSPDLookupUnitPostcodeNearestYM3 = ONSPDLookupUnitPostcode.get(NearestYM3ForONSPDLookup);
                if (ONSPDLookupUnitPostcodeNearestYM3 == null) {
                    System.err.println("yM3UnitPostcodeONSPDLookupsONS == null for NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup);
                }
                isMappablePostcode = ONSPDLookupUnitPostcodeNearestYM3.containsKey(PostcodeF);
                return isMappablePostcode;
        }
        return false;
    }

    /**
     * For Unit Postcode "LS2 9JT": Postcode Sector = "LS2 9"
     *
     * @param unitPostcode
     * @return
     */
    public String getPostcodeSector(String unitPostcode) {
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
            switch (pp.length) {
                case 2:
                    result = pp[0] + " " + pp[1].substring(0, 1);
                    return result;
                case 1:
                    int length = p.length();
                    result = p.substring(0, length - 2);
                    result = result.trim();
                    if (result.length() < 5) {
                        return "";
                    }
                    return result;
                default:
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
        //return result;
    }

    public String getPostcodeArea(String ONSPDPostcodeUnit) {
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

    public TreeMap<String, AGDT_Point> initPostcodeUnitPointLookup(
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
                        String PostcodeF = rec.getPostcodeF();
                        if (ignorePointsAtOrigin) {
                            // Test for orgin point (postcodes ending ZZ are usually at origin, but some others are too.)
//                            if (!(easting == 0 && northing == 0)) {
//                                result.put(rec.getPcd(), point);
//                            } else {
//                                int debug = 1;
//                            }
                            if (easting < 1 || northing < 1) {
                                int debug = 1;
                            } else if (PostcodeF.startsWith("LS")
                                    || PostcodeF.startsWith("BD")
                                    || PostcodeF.startsWith("HG")
                                    || PostcodeF.startsWith("CR")
                                    || PostcodeF.startsWith("W")
                                    || PostcodeF.startsWith("NP")
                                    || PostcodeF.startsWith("BL")
                                    || PostcodeF.startsWith("HX")
                                    || PostcodeF.startsWith("HD")) {
                                result.put(PostcodeF, point);
                            }
//                            if (postcode.endsWith("ZZ")) {
//                                int debug = 1;
//                            }
                        } else if (PostcodeF.startsWith("LS")
                                || PostcodeF.startsWith("BD")
                                || PostcodeF.startsWith("HG")
                                || PostcodeF.startsWith("CR")
                                || PostcodeF.startsWith("W")
                                || PostcodeF.startsWith("NP")
                                || PostcodeF.startsWith("BL")
                                || PostcodeF.startsWith("HX")
                                || PostcodeF.startsWith("HD")) {
                            result.put(PostcodeF, point);
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

//    public TreeMap<String, DW_ONSPDRecord_2013_08Aug> readONSPDIntoTreeMapPostcodeONSPDRecord(
//            File file) {
//        TreeMap<String, DW_ONSPDRecord_2013_08Aug> result = new TreeMap<String, DW_ONSPDRecord_2013_08Aug>();
//        try {
//            int lineCounter = 0;
//            int recordCounter = 0;
//            BufferedReader br;
//            br = Generic_StaticIO.getBufferedReader(file);
//            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
//            String line = "";
//            //Skip the first line
//            int tokenType;
//            Generic_StaticIO.skipline(aStreamTokenizer);
//            tokenType = aStreamTokenizer.nextToken();
//            while (tokenType != StreamTokenizer.TT_EOF) {
//                switch (tokenType) {
//                    case StreamTokenizer.TT_EOL:
//                        DW_ONSPDRecord_2013_08Aug rec = new DW_ONSPDRecord_2013_08Aug(line);
//                        result.put(rec.getPcd(), rec);
//                        lineCounter++;
//                        if (lineCounter % 100000 == 0) {
//                            System.out.println("Read " + lineCounter + " lines out of 2550320");
//                        }
//                        break;
//                    case StreamTokenizer.TT_WORD:
//                        line = aStreamTokenizer.sval;
//                        break;
//                }
//                tokenType = aStreamTokenizer.nextToken();
//            }
//            br.close();
//        } catch (IOException aIOException) {
//            System.err.println(aIOException.getMessage() + " in "
//                    + this.getClass().getName()
//                    + ".readONSPD(File)");
//            System.exit(2);
//        }
//        return result;
//    }

//    /**
//     * MSOA
//     *
//     * @param file
//     * @return
//     */
//    public TreeMap<String, String[]> readONSPDIntoTreeMapPostcodeStrings(
//            File file) {
//        TreeMap<String, String[]> result = new TreeMap<String, String[]>();
//        try {
//            int lineCounter = 0;
//            int recordCounter = 0;
//            BufferedReader br;
//            br = Generic_StaticIO.getBufferedReader(file);
//            StreamTokenizer aStreamTokenizer = getStreamTokeniser(br);
//            String line = "";
//            //Skip the first line
//            int tokenType;
//            Generic_StaticIO.skipline(aStreamTokenizer);
//            tokenType = aStreamTokenizer.nextToken();
//            while (tokenType != StreamTokenizer.TT_EOF) {
//                switch (tokenType) {
//                    case StreamTokenizer.TT_EOL:
//                        DW_ONSPDRecord_2013_08Aug rec = new DW_ONSPDRecord_2013_08Aug(line);
//                        String[] values = new String[4];
//                        values[0] = rec.getOa01();
//                        values[1] = rec.getMsoa01();
//                        values[2] = rec.getOa11();
//                        values[3] = rec.getMsoa11();
//                        result.put(rec.getPcd(), values);
//                        lineCounter++;
//                        if (lineCounter % 100000 == 0) {
//                            System.out.println("Read " + lineCounter + " lines out of 2550320");
//                        }
//                        break;
//                    case StreamTokenizer.TT_WORD:
//                        line = aStreamTokenizer.sval;
//                        break;
//                }
//                tokenType = aStreamTokenizer.nextToken();
//            }
//            br.close();
//        } catch (IOException aIOException) {
//            System.err.println(aIOException.getMessage() + " in "
//                    + this.getClass().getName()
//                    + ".readONSPD(File)");
//            System.exit(2);
//        }
//        return result;
//    }

    /**
     * @param file
     * @param level
     * @param YM3NearestFormat
     * @return
     */
    public TreeMap<String, String> readONSPDIntoTreeMapPostcodeString(
            File file,
            String level,
            String YM3NearestFormat) {
        String[] YM3NearestSplit;
        YM3NearestSplit = YM3NearestFormat.split(DW_Strings.sUnderscore);
                int year = Integer.valueOf(YM3NearestSplit[0]);
            String yearString;
            if (year < 2011) {
                yearString = "2001";
            } else {
                yearString = "2011";
            }
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
                        DW_AbstractONSPDRecord1 rec;
                        rec = null;
                        //DW_ONSPDRecord_2013_08Aug rec = new DW_ONSPDRecord_2013_08Aug(line);
                        if (YM3NearestFormat.equalsIgnoreCase("2016_Feb")) {
                            rec = new DW_ONSPDRecord_2016_02Feb(line);
                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_Aug")) {
                            rec = new DW_ONSPDRecord_2015_08Aug(line);
                        } else if (YM3NearestFormat.equalsIgnoreCase("2015_May")) {
                            rec = new DW_ONSPDRecord_2015_05May(line);
                        } else if (YM3NearestFormat.equalsIgnoreCase("2014_Nov")) {
                            rec = new DW_ONSPDRecord_2014_11Nov(line);
                        } else {
                            rec = new DW_ONSPDRecord_2013_08Aug(line);
                        }
                        String value = "";
                        if (level.equalsIgnoreCase("OA")) {
                            if (year < 2011) {
                                value = rec.getOa01();
                            } else {
                                value = rec.getOa11();
                            }
                        }
                        if (level.equalsIgnoreCase("LSOA")) {
                            if (year < 2011) {
                                value = rec.getLsoa01();
                            } else {
                                value = rec.getLsoa11();
                            }
                        }
                        if (level.equalsIgnoreCase("MSOA")) {
                            if (year < 2011) {
                                value = rec.getMsoa01();
                            } else {
                                value = rec.getMsoa11();
                            }
                        }
                        String postcode = rec.getPcd();
                        if (level.equalsIgnoreCase(DW_Strings.sPostcodeUnit)) {
                            value = postcode;
                        }
                        if (level.equalsIgnoreCase(DW_Strings.sPostcodeSector)) {
                            value = getPostcodeSector(postcode);
                        }
                        if (level.equalsIgnoreCase(DW_Strings.sPostcodeDistrict)) {
                            value = getPostcodeDistrict(postcode);
                        }
                        if (level.equalsIgnoreCase(DW_Strings.sParliamentaryConstituency)) {
                            value = rec.getPcon();
                        }
                        if (level.equalsIgnoreCase(DW_Strings.sStatisticalWard)) {
                            value = rec.getStatsward();
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
                        DW_ONSPDRecord_2013_08Aug rec = new DW_ONSPDRecord_2013_08Aug(line);
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

    private StreamTokenizer getStreamTokeniser(BufferedReader br) {
        StreamTokenizer result;
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

    

}
