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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
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
            String aPostcode,
            String bPostcode) {
        double result = 0.0d;
        AGDT_Point aPoint;
        aPoint = getPointFromPostcode(aPostcode);
        AGDT_Point bPoint;
        bPoint = getPointFromPostcode(bPostcode);
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
    public static AGDT_Point getPointFromPostcode(String level, String postcode) {
        if (level == null) {
            return null;
        }
        AGDT_Point result;
        String formattedPostcode;
        formattedPostcode = DW_Postcode_Handler.formatPostcodeForONSPDLookup(postcode);
        result = DW_Maps.getONSPDlookups().get(level).get(formattedPostcode);
        return result;
    }

    public static AGDT_Point getPointFromPostcode(String postcode) {
        AGDT_Point result;
        String level;
        level = DW_Postcode_Handler.getPostcodeLevel(postcode);
        result = getPointFromPostcode(level, postcode);
        return result;
    }

    /**
     *
     * @param unformattedUnitPostcode
     * @return A better format of the unformattedUnitPostcode
     */
    public static String formatPostcode(String unformattedUnitPostcode) {
        String result = unformattedUnitPostcode.trim();
        String[] postcodeSplit = result.split(" ");
        if (postcodeSplit.length > 3) {
            System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
        } else {
            if (postcodeSplit.length == 3) {
                result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
                if (postcodeSplit[2].length() != 3) {
                }
            } else {
                if (postcodeSplit.length == 2) {
                    result = postcodeSplit[0] + " " + postcodeSplit[1];
                    if (postcodeSplit[1].length() != 3) {
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param unformattedUnitPostcode
     * @return A format of the unformattedUnitPostcode for ONSPD lookups For
     * example this will return "LS11OJS" for an unformattedUnitPostcode = "LS11
     * 0JS"
     */
    public static String formatPostcodeForONSPDLookup(String unformattedUnitPostcode) {
        if (unformattedUnitPostcode != null) {
            String result = unformattedUnitPostcode.trim();
            String[] postcodeSplit = result.split(" ");
            if (postcodeSplit.length > 3) {
                System.out.println("unformattedUnitPostcode " + unformattedUnitPostcode + " cannot be formatted into a unit postcode");
            } else {
                if (postcodeSplit.length == 3) {
                    result = postcodeSplit[0] + postcodeSplit[1] + " " + postcodeSplit[2];
                    if (postcodeSplit[2].length() != 3) {
                    }
                } else {
                    if (postcodeSplit.length == 2) {
                        result = postcodeSplit[0] + " " + postcodeSplit[1];
                        if (postcodeSplit[1].length() != 3) {
                        }
                    }
                }
            }
            if (result.length() == 8) {
                result = result.replaceAll(" ", "");
            }
            result = Generic_StaticString.getUpperCase(result);
            return result;
        }
        return null;
    }

    /**
     * @param input TreeMap<String, String> where values are postcodes for which
     * the coordinates are to be returned as a AGDT_Point.
     * @return TreeMap<String, AGDT_Point> with the keys as in input and values
     * calculated using getPointFromPostcode(value). If no look up is found for
     * a postcode its key does not get put into the result.
     */
    public static TreeMap<String, AGDT_Point> postcodeToPoints(TreeMap<String, String> input) {
        TreeMap<String, AGDT_Point> result;
        result = new TreeMap<String, AGDT_Point>();
        Iterator<String> ite_String = input.keySet().iterator();
        while (ite_String.hasNext()) {
            String key = ite_String.next();
            String postcode = input.get(key);
            AGDT_Point p = getPointFromPostcode(postcode);
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

    /**
     * File from which data is read from.
     */
    private File inputFile;
    /**
     * File which data are written to.
     */
    private File outputFile;
    //public TreeMap<String, AGDT_Point> lookup;

    public DW_Postcode_Handler() {
    }

    /**
     * @param inputFile
     * @param outputFile
     */
    public DW_Postcode_Handler(
            File inputFile,
            File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * Creates a new instance of this class using directory.
     *
     * @param inputFile
     */
    public DW_Postcode_Handler(
            File inputFile) {
        this.inputFile = inputFile;
    }

    public static void main(String[] args) {
        File inputONSPDDir = DW_Files.getInputONSPDDir();
        File processedONSPDDir = DW_Files.getGeneratedONSPDDir();
        String inputFilename = null;
        File inputFile;
        String processedFilename = null;
        File processedFile;
        if (args.length == 0) {
            inputFilename = "ONSPD_AUG_2013_UK_O.csv";
            processedFilename = "PostcodeLookUp_TreeMap_String_Point.thisFile"; // For getPostcodeUnitPointLookup()
        }
        inputFile = new File(inputONSPDDir, inputFilename);
        processedFile = new File(processedONSPDDir, processedFilename);
        boolean ignorePointsAtOrigin = true;
        new DW_Postcode_Handler(inputFile, processedFile).getPostcodeUnitPointLookup(ignorePointsAtOrigin);
        //new DW_Postcode_Handler(inputFile, processedFile).run1();
        //new DW_Postcode_Handler(inputFile, processedFile).getPostcodeUnitCensusCodesLookup();
        //new DW_Postcode_Handler(inputFile, processedFile).run3();
    }

    public TreeMap<String, AGDT_Point> getPostcodeSectorPointLookup(
            boolean ignorePointsAtOrigin,
            String outputFilename,
            TreeMap<String, AGDT_Point> postcodeUnitPointLookup) {
        TreeMap<String, AGDT_Point> result = null;
        File outputFile2;
        outputFile2 = new File(
                outputFile.getParentFile(),
                outputFilename);
        try {
            result = initPostcodeSectorPointLookup(
                    postcodeUnitPointLookup,
                    ignorePointsAtOrigin);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        String postcode;
        // Test some postcodes
        postcode = "LS17 2";
        printTest0(
                result,
                postcode);
        postcode = "LS2 9";
        printTest0(
                result,
                postcode);
        Generic_StaticIO.writeObject(result, outputFile2);
//        // For testing reading back in
//        result = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outputFile2);
        return result;
    }

    public TreeMap<String, AGDT_Point> getPostcodeUnitPointLookup(
            boolean ignorePointsAtOrigin) {
        TreeMap<String, AGDT_Point> result;
        result = initPostcodeUnitPointLookup(
                inputFile,
                ignorePointsAtOrigin);
        // Test some postcodes
        String postcode;
        postcode = "LS7 2EU";
        printTest0(
                result,
                postcode);
        postcode = "LS2 9JT";
        printTest0(
                result,
                postcode);
        Generic_StaticIO.writeObject(result, outputFile);
//        // For testing reading back in
//        result = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(outputFile);
        return result;
    }

    public TreeMap<String, ONSPDRecord_Aug_2013_UK_0> getPostcodeUnitONSPDRecordLookup() {
        // Read NPD into a lookup
        TreeMap<String, ONSPDRecord_Aug_2013_UK_0> lookup;
        lookup = readONSPDIntoTreeMapPostcodeONSPDRecord(
                inputFile);
        // Test some postcodes
        String postcode;
        postcode = "LS7 2EU";
        printTest1(
                lookup,
                postcode);
        postcode = "LS2 9JT";
        printTest1(
                lookup,
                postcode);
        Generic_StaticIO.writeObject(lookup, outputFile);
        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    public TreeMap<String, String[]> getPostcodeUnitCensusCodesLookup() {
        // Read NPD into a lookup
        TreeMap<String, String[]> lookup;
        lookup = readONSPDIntoTreeMapPostcodeStrings(
                inputFile);
        // Test some postcodes
        String postcode;
        postcode = "LS7 2EU";
        printTest2(
                lookup,
                postcode);
        postcode = "LS2 9JT";
        printTest2(
                lookup,
                postcode);
        Generic_StaticIO.writeObject(lookup, outputFile);
        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
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
            int year) {
        // Read NPD into a lookup
        TreeMap<String, String> lookup;
        lookup = readONSPDIntoTreeMapPostcodeString(inputFile, level, year);
        Generic_StaticIO.writeObject(lookup, outputFile);
        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    public static String getPostcodeLevel(String postcode) {
        String p;
        p = postcode.trim();
        if (p.length() < 2) {
            return null;
        }
        String[] pa;
        if (p.contains("  ")) {
            pa = p.split("  ");
            if (p.length() == 1) {
                return "Area";
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
            return null;
        }
        pa = p.split(" ");
        if (p.length() == 1) {
            return "Area";
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
        Generic_StaticIO.writeObject(oaCodeLookUp, outputFile);
        //oaCodeLookUp = (HashMap<String, String>) Generic_StaticIO.readObject(lookupFile);
    }

    public TreeMap<String, String[]> run4() {
        // Read NPD into a lookup
        TreeMap<String, String[]> lookup;
        lookup = readONSPDIntoTreeMapPostcodeStrings2(
                inputFile);
        // Test some postcodes
        String postcode;
        postcode = "LS7 2EU";
        printTest2(
                lookup,
                postcode);
        postcode = "LS2 9JT";
        printTest2(
                lookup,
                postcode);
        Generic_StaticIO.writeObject(lookup, outputFile);
        //lookup = (TreeMap<String, AGDT_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

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

    public static boolean isValidPostcode(String postcode) {
        if (postcode.length() > 5) {
            String[] split;
            split = postcode.split(" ");
            if (split.length == 2) {
                return true;
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
                        ONSPDRecord_Aug_2013_UK_0 rec;
                        rec = new ONSPDRecord_Aug_2013_UK_0(line);
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
                                result.put(rec.getPcd(), point);
                            }
//                            if (postcode.endsWith("ZZ")) {
//                                int debug = 1;
//                            }
                        } else {
                            result.put(rec.getPcd(), point);
                        }
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

    private StreamTokenizer getStreamTokeniser(BufferedReader br) {
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

}