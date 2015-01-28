/*
 * Copyright (C) 2014 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class Age_EcoAct_LSOA_DataRecord_Handler {

    public Age_EcoAct_LSOA_DataRecord_Handler() {}

    /**
     * Loads LeedsCAB data from a filename.
     *
     * @param filename
     * @return TreeMap<String,LeedsCAB_DataRecord> representing records
     */
    public TreeMap<String,Age_EcoAct_LSOA_DataRecord> loadInputData(
            String filename) {
        System.out.println("Loading " + filename);
        TreeMap<String,Age_EcoAct_LSOA_DataRecord> result;
        result = new TreeMap<String,Age_EcoAct_LSOA_DataRecord>();
        
        File attributeDir = new File(
                DW_Files.getInputCensus2011Dir("LSOA"),
                "AttributeData/Leeds");
        File dir = new File(
            attributeDir,
                filename.substring(5,filename.length() - 4));
        
        File inputFile = new File(
                dir,
                filename);
        BufferedReader br;
        br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st;
        st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        st.wordChars('\'', '\'');
        st.wordChars('(', '(');
        st.wordChars(')', ')');
        st.wordChars('\'', '\'');
        st.wordChars('*', '*');
        st.wordChars('\\', '\\');
        st.wordChars('/', '/');
        st.wordChars('&', '&');
        st.wordChars('£', '£');
        st.wordChars('<', '<');
        st.wordChars('>', '>');
        st.wordChars('=', '=');
        st.wordChars('#', '#');
        st.wordChars(':', ':');
        st.wordChars(';', ';');
        String line = "";
        long RecordID = 0;
        try {
            // Skip the header
            int headerLines = 3;
            for (int i = 0; i < headerLines; i ++) {
                Generic_StaticIO.skipline(st);
            }
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {

                // For debugging
                if (RecordID == 10) {
                    int debug = 1;
                }

                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                            try { 
                                Age_EcoAct_LSOA_DataRecord record;
                                record = new Age_EcoAct_LSOA_DataRecord(
                                        RecordID, line, this);
                                String LSOACode = record.getLSOACode();
                                if (result.containsKey(LSOACode)) {
                                    System.out.println("Additional record for LSOACode " + LSOACode);
                                } else {
                                    result.put(LSOACode, record);
                                }
                            } catch (Exception e) {
                                System.err.println(line);
                                System.err.println("RecordID " + RecordID);
                                System.err.println(e.getLocalizedMessage());
                         }
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Deprivation_DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    
//    // Month_10_2010_11_381112_D_records.csv
//    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
//    // hb9803_SHBE_206728k\ April\ 2008.csv
//    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
//    // 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
//    // 307, 315
//    public int readAndCheckFirstLine(
//            File directory,
//            String filename) {
//        int type = 0;
//        String type0Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341";
//        String type1Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341";
//        File inputFile = new File(
//                directory,
//                filename);
//        BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
//        StreamTokenizer st
//                = new StreamTokenizer(br);
//        Generic_StaticIO.setStreamTokenizerSyntax5(st);
//        st.wordChars('`', '`');
//        try {
//            int tokenType;
//            tokenType = st.nextToken();
//            String line = "";
//            while (tokenType != StreamTokenizer.TT_EOL) {
//                switch (tokenType) {
//                    case StreamTokenizer.TT_WORD:
//                        line += st.sval;
//                        break;
//                }
//                tokenType = st.nextToken();
//            }
//            br.close();
//            if (line.equalsIgnoreCase(type0Header)) {
//                return 0;
//            }
//            if (line.equalsIgnoreCase(type1Header)) {
//                return 1;
//            } else {
//                String[] lineSplit = line.split(",");
//                System.err.println("Unrecognised header");
//                System.out.println("Number of fields in header " + lineSplit.length);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Deprivation_DataHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return 2;
//    }
//
//    public static Aggregate_SHBE_DataRecord aggregate(HashSet<SHBE_DataRecord> records) {
//        Aggregate_SHBE_DataRecord result = new Aggregate_SHBE_DataRecord();
//        Iterator<SHBE_DataRecord> ite = records.iterator();
//        while (ite.hasNext()) {
//            SHBE_DataRecord aSHBE_DataRecord = ite.next();
//            aggregate(aSHBE_DataRecord, result);
//        }
//        return result;
//    }

}
