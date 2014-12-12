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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.ONSPDRecord_Aug_2013_UK_0;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping.DW_Point;

/**
 * A class for adding coordinate data and area codes for UK postcodes.
 * https://geoportal.statistics.gov.uk/Docs/PostCodes/ONSPD_AUG_2013_csv.zip
 */
public class PostcodeGeocoder implements Serializable {

    /**
     * A name for exception and error handling
     */
    private static final String className = "PostcodeGeocoder";
    /**
     * File from which data is read from.
     */
    private File inputFile;
    /**
     * File which data are written to.
     */
    private File outputFile;
    //public TreeMap<String, DW_Point> lookup;

    public PostcodeGeocoder() {
    }

    /**
     * Creates a new instance of this class using directory.
     *
     * @param inputFile
     * @param outputFile
     */
    public PostcodeGeocoder(
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
    public PostcodeGeocoder(
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
            processedFilename = "PostcodeLookUp_TreeMap_String_Point.thisFile"; // For generatePostCodePointLookup()
        }
        inputFile = new File(inputONSPDDir, inputFilename);
        processedFile = new File(processedONSPDDir, processedFilename);
        new PostcodeGeocoder(inputFile, processedFile).generatePostCodePointLookup();
        //new PostcodeGeocoder(inputFile, processedFile).run1();
        //new PostcodeGeocoder(inputFile, processedFile).generatePostcodeStringsLookup();
        //new PostcodeGeocoder(inputFile, processedFile).run3();
    }

    public TreeMap<String, DW_Point> generatePostCodePointLookup() {
        // Read NPD into a lookup
        TreeMap<String, DW_Point> lookup;
        lookup = readONSPDIntoTreeMapPostcodePoint(
                inputFile);
        // Test some postcodes
        String postcode;
        postcode = "LS7 2EU";
        printTest0(
                lookup,
                postcode);
        postcode = "LS2 9JT";
        printTest0(
                lookup,
                postcode);
        Generic_StaticIO.writeObject(lookup, outputFile);
        lookup = (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(outputFile);
        return lookup;
    }

    public TreeMap<String, ONSPDRecord_Aug_2013_UK_0> generatePostCodeONSPDRecordLookup() {
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
        //lookup = (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    public TreeMap<String, String[]> generatePostcodeStringsLookup() {
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
        //lookup = (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
    }

    public TreeMap<String, String> generatePostcodeStringLookup(
            String level,
            int year) {
        // Read NPD into a lookup
        TreeMap<String, String> lookup;
        lookup = readONSPDIntoTreeMapPostcodeString(inputFile, level, year);
        Generic_StaticIO.writeObject(lookup, outputFile);
        //lookup = (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(lookupFile);
        return lookup;
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
        //lookup = (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(lookupFile);
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
            int recordCounter = 0;
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
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
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD2(File)");
            System.exit(2);
        }
        return result;
    }

    public static TreeMap<String, DW_Point> getLookupPointFromPostcode(File file) {
        return (TreeMap<String, DW_Point>) Generic_StaticIO.readObject(file);
    }

    public static TreeMap<String, String[]> getLookupStringsFromPostcode(File file) {
        return (TreeMap<String, String[]>) Generic_StaticIO.readObject(file);
    }

    public static HashMap<String, String> getLookupStringFromString(File file) {
        return (HashMap<String, String>) Generic_StaticIO.readObject(file);
    }

    public static void printTest0(
            TreeMap<String, DW_Point> lookup,
            String postcode) {
        DW_Point p = lookup.get(postcode);
        int lon = p.getX();
        int lat = p.getY();
        System.out.println(postcode + ", " + lon + ", " + lat);
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

    public TreeMap<String, DW_Point> readONSPDIntoTreeMapPostcodePoint(
            File file) {
        TreeMap<String, DW_Point> result = new TreeMap<String, DW_Point>();
        try {
            int lineCounter = 0;
            int recordCounter = 0;
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
            String line = "";
            //Skip the first line
            int tokenType;
            Generic_StaticIO.skipline(aStreamTokenizer);
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        ONSPDRecord_Aug_2013_UK_0 rec = new ONSPDRecord_Aug_2013_UK_0(line);
                        DW_Point point = new DW_Point(rec.getOseast1m(), rec.getOsnrth1m());
                        result.put(rec.getPcd(), point);
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
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
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
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
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
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
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
            StreamTokenizer aStreamTokenizer = getStreamTokeniser(file);
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
        } catch (IOException aIOException) {
            System.err.println(aIOException.getMessage() + " in "
                    + this.getClass().getName()
                    + ".readONSPD(File)");
            System.exit(2);
        }
        return result;
    }

    private StreamTokenizer getStreamTokeniser(File file) {
        StreamTokenizer result = null;
        BufferedReader br;
        br = Generic_StaticIO.getBufferedReader(file);
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
